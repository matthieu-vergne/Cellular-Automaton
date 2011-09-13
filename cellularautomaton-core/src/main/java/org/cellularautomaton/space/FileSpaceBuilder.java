package org.cellularautomaton.space;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.rule.DynamicRule;
import org.cellularautomaton.rule.DynamicRule.RulePart;
import org.cellularautomaton.space.expression.Expression;
import org.cellularautomaton.space.expression.ExpressionHelper;
import org.cellularautomaton.state.DynamicStateFactory;
import org.cellularautomaton.state.IStateFactory;
import org.cellularautomaton.util.Coords;

/**
 * A file space builder allows to create a space from a description file.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 */
public class FileSpaceBuilder {
	private static enum Mode {
		CONFIG, RULE, CELLS
	}

	private SpaceBuilder<Character> builder;
	private DynamicRule<Character> rule;
	private DynamicStateFactory<Character> stateFactory;
	private Mode actualMode;
	private int width;
	private int height;
	private Object[] characterSpace;
	private final Logger logger = Logger.getAnonymousLogger();

	public FileSpaceBuilder() {
		logger.setLevel(Level.OFF);
	}

	/**
	 * This method allows to create a space as described in a textual file. This
	 * file should follow this syntax :<br/>
	 * 
	 * <pre>
	 * TODO describe syntax
	 * </pre>
	 * 
	 * @param filePath
	 *            the path to the building file
	 * @return the created space
	 */
	public void createSpaceFromFile(File file) {
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		String line;
		String description = "";
		actualMode = null;
		try {
			while ((line = in.readLine()) != null) {
				description += line + "\n";
			}
		} catch (IOException e) {
			try {
				in.close();
			} catch (IOException e1) {
				throw new RuntimeException(e1);
			}
			throw new RuntimeException(e);
		}
		try {
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		createSpaceFromString(description);
	}

	public void createSpaceFromString(String description) {
		try {
			width = 0;
			height = 0;
			rule = new DynamicRule<Character>();
			stateFactory = new DynamicStateFactory<Character>() {
				public void customize(ICell<Character> cell) {
					customizeCell(cell);
				};
			};
			builder = new SpaceBuilder<Character>();
			builder.setRule(rule);
			builder.setStateFactory(stateFactory);

			BufferedReader in = new BufferedReader(
					new StringReader(description));
			String line;
			String cellsDescription = "";
			actualMode = null;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.length() == 0) {
					continue;
				} else if (line.matches("^\\[.+\\]$")) {
					switchMode(line);
				} else if (actualMode == Mode.CONFIG
						&& line.matches("^[^=]+=.+$")) {
					String[] parts = line.split("=", 2);
					saveConfig(parts[0], parts[1]);
				} else if (actualMode == Mode.RULE) {
					addRule(line);
				} else if (actualMode == Mode.CELLS) {
					cellsDescription = addCells(cellsDescription, line);
				} else {
					throw new BadFileContentException("Unparsable line : "
							+ line);
				}
			}
			in.close();

			characterSpace = translateDescriptionInSpace(cellsDescription);

			builder.createNewSpace();
			Object reference = characterSpace;
			while (reference instanceof Object[]) {
				Object[] array = (Object[]) reference;
				builder.addDimension(array.length);
				reference = array[0];
			}
			builder.finalizeSpace();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void customizeCell(ICell<Character> cell) {
		Coords coords = cell.getCoords();
		Object reference = characterSpace;
		for (int dimension = 0; dimension < coords.getDimensions(); dimension++) {
			int coord = coords.get(dimension);
			reference = ((Object[]) reference)[coord];
		}
		cell.setCurrentState((Character) reference);
	}

	private Object[] translateDescriptionInSpace(String stringDescription) {
		Marker[][] description = translateDescriptionInMatrix(stringDescription);
		preciseSeparators(description);
		checkDescription(description);
		return translateMatrixInSpace(description);
	}

	private Object[] translateMatrixInSpace(Marker[][] description) {
		return recursiveArrayBuilding(description, 0, 0, height, width);
	}

	class Executor {
		public boolean isFollowingX = true;
		public int xStart;
		public int yStart;
		public int xLength;
		public int yLength;
		public int stepLength = 0;
		public Marker[][] description;
		public int separatorLength;

		public Object[] executeFinalCase() {
			if (yLength == 1) {
				isFollowingX = true;
			} else if (xLength == 1) {
				isFollowingX = false;
			} else {
				throw new IllegalStateException(
						"The final case cannot be executed if xLength and yLength are both > 1.");
			}
			Character[] array = new Character[getLength()];
			for (int i = 0; i < getLength(); i++) {
				int[] coords = getCoordsFollowingTheGoodAxis(i);
				Marker marker = description[coords[0]][coords[1]];
				array[i] = marker.character;
			}
			return array;
		}

		private Object[] translateSubmatrix() {
			List<Object[]> subtranslations = new ArrayList<Object[]>();
			for (int i = 0; i < getLength(); i += stepLength + separatorLength) {
				int[] coords = getCoordsFollowingTheGoodAxis(i);
				int lengthX = xLength;
				int lengthY = yLength;
				if (isFollowingX) {
					lengthX = stepLength;
				} else {
					lengthY = stepLength;
				}
				subtranslations.add(recursiveArrayBuilding(description,
						coords[0], coords[1], lengthX, lengthY));
			}
			return subtranslations.toArray(new Object[subtranslations.size()]);
		}

		private int[] getCoordsFollowingTheGoodAxis(int i) {
			int[] coords = new int[] { xStart, yStart };
			if (isFollowingX) {
				coords[0] += i;
			} else {
				coords[1] += i;
			}
			return coords;
		}

		public int getLength() {
			return isFollowingX ? xLength : yLength;
		}

		class Candidate {
			public Separator separator = null;
			public int stepLength = 0;
			public boolean isFollowingX = false;
		}

		public Object[] executeIntermediaryCase() {
			// look for the available explicit separators
			final List<Candidate> candidates = new ArrayList<Candidate>();
			isFollowingX = true;
			lookForSeparatorsFollowingTheGoodAxis(candidates);
			isFollowingX = false;
			lookForSeparatorsFollowingTheGoodAxis(candidates);

			if (candidates.isEmpty()) {
				// add the biggest implicit separator (first dimensions)
				Candidate implicitCandidate = new Candidate();
				candidates.add(implicitCandidate);
				implicitCandidate.isFollowingX = height > 1;
				implicitCandidate.stepLength = 1;
			}

			// sort the separators
			Collections.sort(candidates, new Comparator<Candidate>() {
				public int compare(Candidate c1, Candidate c2) {
					return -Integer.valueOf(c1.separator.dimension).compareTo(
							Integer.valueOf(c2.separator.dimension));
				};
			});

			// take the data of the best candidate
			Candidate bestCandidate = candidates.get(0);
			stepLength = bestCandidate.stepLength;
			isFollowingX = bestCandidate.isFollowingX;
			separatorLength = bestCandidate.separator == null ? 0 : 1;

			// execute the translation
			return translateSubmatrix();
		}

		private void lookForSeparatorsFollowingTheGoodAxis(
				final List<Candidate> candidates) {
			for (int i = 0; i < getLength(); i++) {
				int[] coords = getCoordsFollowingTheGoodAxis(i);
				Marker marker = description[coords[0]][coords[1]];
				if (marker instanceof Separator) {
					int start = isFollowingX ? xStart : yStart;
					Candidate candidate = new Candidate();
					candidate.separator = (Separator) marker;
					candidate.isFollowingX = isFollowingX;
					candidate.stepLength = i - start;
					candidates.add(candidate);
				}
			}
		}

	}

	private Object[] recursiveArrayBuilding(Marker[][] description, int xStart,
			int yStart, int xLength, int yLength) {
		Object[] array;
		Executor executor = new Executor();
		executor.description = description;
		executor.xStart = xStart;
		executor.yStart = yStart;
		executor.xLength = xLength;
		executor.yLength = yLength;
		// final case : row or column (1D)
		if (xLength == 1 || yLength == 1) {
			array = executor.executeFinalCase();
		}
		// intermediary case : matrix (2D)
		else {
			array = executor.executeIntermediaryCase();
		}
		return array;
	}

	private void checkDescription(Marker[][] description) {
		checkDimensionsAreRegular(description);
		checkDimensionsAreConstantKnowingTheyAreRegular(description);
	}

	private void checkDimensionsAreConstantKnowingTheyAreRegular(
			Marker[][] description) {
		// TODO factorize
		// vertical check
		Collection<Separator> checked = new HashSet<Separator>();
		for (int x = 0; x < height; x++) {
			Marker marker = description[x][0];
			if (marker instanceof Separator && !checked.contains(marker)) {
				Separator separator = (Separator) marker;
				int length = x;
				int x1 = x;
				while (x1 < height) {
					int x2 = x1 + 1;
					Marker m = null;
					while (x2 < height
							&& ((m = description[x2][0]) instanceof Cell || ((Separator) m).dimension < separator.dimension)) {
						x2++;
					}
					if (x2 - x1 - 1 != length) {
						throw new BadFileContentException(
								"the horizontal separator "
										+ separator.character
										+ " does not give a constant dimension ("
										+ length + " & " + (x2 - x1) + ").");
					}
					x1 = x2;
				}
				checked.add(separator);
			}
		}

		// horizontal check
		checked.clear();
		for (int y = 0; y < width; y++) {
			Marker marker = description[0][y];
			if (marker instanceof Separator && !checked.contains(marker)) {
				Separator separator = (Separator) marker;
				int length = y;
				int y1 = y;
				while (y1 < width) {
					int y2 = y1 + 1;
					Marker m = null;
					while (y2 < width
							&& ((m = description[0][y2]) instanceof Cell || ((Separator) m).dimension < separator.dimension)) {
						y2++;
					}
					if (y2 - y1 - 1 != length) {
						throw new BadFileContentException(
								"the vertical separator "
										+ separator.character
										+ " does not give a constant dimension ("
										+ length + " & " + (y2 - y1) + ").");
					}
					y1 = y2;
				}
				checked.add(separator);
			}
		}
	}

	private void checkDimensionsAreRegular(Marker[][] description) {
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				Marker marker = description[x][y];
				if (marker instanceof Separator) {
					boolean consistent = true;
					for (int x2 = 0; x2 < height; x2++) {
						Marker marker2 = description[x2][y];
						if (marker2 instanceof Cell
								|| ((Separator) marker).dimension > ((Separator) marker2).dimension) {
							consistent = false;
							break;
						}
					}

					if (!consistent) {
						consistent = true;
						for (int y2 = 0; y2 < width; y2++) {
							Marker marker2 = description[x][y2];
							if (marker2 instanceof Cell
									|| ((Separator) marker).dimension > ((Separator) marker2).dimension) {
								consistent = false;
								break;
							}
						}
					}

					if (!consistent) {
						throw new BadFileContentException(
								"the character "
										+ marker.character
										+ " at ("
										+ x
										+ ","
										+ y
										+ ") is not a consistent separator nor a known state.");
					}
				}
			}
		}
	}

	private void preciseSeparators(Marker[][] description) {
		// get horizontal separators
		final Map<Character, Separator> horizontalSeparators = new LinkedHashMap<Character, Separator>();
		if (width > 1 && description[0][1] instanceof Cell) {
			horizontalSeparators.put(null, new Separator());
		}
		for (int y = 0; y < width; y++) {
			Marker marker = description[0][y];
			Character character = marker.character;
			if (marker instanceof Separator
					&& !horizontalSeparators.containsKey(character)) {
				Separator separator = new Separator();
				separator.character = character;
				horizontalSeparators.put(character, separator);
			}
		}

		// get vertical separators
		final Map<Character, Separator> verticalSeparators = new LinkedHashMap<Character, Separator>();
		if (height > 1 && description[1][0] instanceof Cell) {
			verticalSeparators.put(null, new Separator());
		}
		for (int x = 0; x < height; x++) {
			Marker marker = description[x][0];
			Character character = marker.character;
			if (marker instanceof Separator
					&& !verticalSeparators.containsKey(character)) {
				Separator separator = new Separator();
				separator.character = character;
				verticalSeparators.put(character, separator);
			}
		}

		// initialize dimensions
		int dimension = 0;
		Iterator<Character> horizontalIterator = horizontalSeparators.keySet()
				.iterator();
		Iterator<Character> verticalIterator = verticalSeparators.keySet()
				.iterator();
		Map<Character, Separator> map = horizontalSeparators;
		Iterator<Character> iterator = horizontalIterator;
		while (iterator.hasNext()) {
			map.get(iterator.next()).dimension = dimension;
			dimension++;
			map = map == horizontalSeparators ? verticalSeparators
					: horizontalSeparators;
			iterator = iterator == horizontalIterator ? verticalIterator
					: horizontalIterator;
		}
		map = map == horizontalSeparators ? verticalSeparators
				: horizontalSeparators;
		iterator = iterator == horizontalIterator ? verticalIterator
				: horizontalIterator;
		while (iterator.hasNext()) {
			map.get(iterator.next()).dimension = dimension;
			dimension++;
		}

		// precise description
		Map<Character, Separator> allSeparators = new LinkedHashMap<Character, Separator>();
		allSeparators.putAll(horizontalSeparators);
		allSeparators.putAll(verticalSeparators);
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				Marker marker = description[x][y];
				if (marker instanceof Separator) {
					description[x][y] = allSeparators.get(marker.character);
				}
			}
		}
	}

	private Marker[][] translateDescriptionInMatrix(String stringDescription) {
		Marker[][] description = new Marker[height][width];
		StringReader reader = new StringReader(stringDescription);
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				Character character;
				try {
					character = (char) reader.read();
				} catch (IOException e) {
					reader.close();
					throw new RuntimeException(e);
				}
				Marker marker;
				if (isKnownState(character)) {
					marker = new Cell();
				} else {
					marker = new Separator();
				}
				marker.character = character;
				description[x][y] = marker;
			}
			// remove the newline
			try {
				reader.read();
			} catch (IOException e) {
				reader.close();
				throw new RuntimeException(e);
			}
		}
		reader.close();
		for (int x = 0; x < height; x++) {
			logger.info(Arrays.deepToString(description[x]));
		}
		return description;
	}

	private boolean isKnownState(Character character) {
		return stateFactory.getPossibleStates().contains(character);
	}

	private String addCells(String cellsDescription, String line) {
		if (width == 0) {
			width = line.length();
		} else if (width != line.length()) {
			throw new BadFileContentException(
					"the cell space must have a constant width.");
		}
		cellsDescription += line + "\n";
		height++;
		return cellsDescription;
	}

	private void addRule(String rulePart) {
		String[] split = rulePart.split(":");
		List<Character> possibleStates = builder.getStateFactory()
				.getPossibleStates();

		// get the condition
		final Expression expression = ExpressionHelper.parseRulePart(split[0],
				possibleStates);

		// get the state
		String value = split[1].trim();
		if (value.length() > 1) {
			throw new BadFileContentException(
					"Several values are defined for the rule : " + rulePart);
		}
		final Character assignedState = value.charAt(0);
		if (!possibleStates.contains(assignedState)) {
			throw new BadFileContentException(value + " is not a known state.");
		}

		// complete rule
		rule.addPart(new RulePart<Character>() {
			@Override
			public boolean isVerifiedBy(ICell<Character> cell) {
				ExpressionHelper.setExpressionForOriginCell(expression, cell);
				return expression.evaluate();
			}

			@Override
			public Character getAssignedState() {
				return assignedState;
			}
		});
	}

	private void saveConfig(String key, String value) {
		if (key.equalsIgnoreCase("states")) {
			for (int i = 0; i < value.length(); i++) {
				stateFactory.addPossibleState(value.charAt(i));
			}
		} else if (key.equalsIgnoreCase("memorySize")) {
			builder.setMemorySize(Integer.parseInt(value));
		} else {
			throw new IllegalArgumentException("Unknown key : " + key);
		}
	}

	private void switchMode(String header) {
		if (header.equals("[config]")) {
			actualMode = Mode.CONFIG;
		} else if (header.equals("[rule]")) {
			actualMode = Mode.RULE;
		} else if (header.equals("[cells]")) {
			actualMode = Mode.CELLS;
		} else {
			throw new BadFileContentException("The header " + header
					+ " is not a valid one.");
		}
	}

	public IStateFactory<Character> getStateFactory() {
		return builder.getStateFactory();
	}

	public ISpace<Character> getSpaceOfCell() {
		return builder.getSpaceOfCell();
	}

	private static class Marker {
		public Character character;

		@Override
		public String toString() {
			return character.toString();
		}
	}

	private static class Cell extends Marker {
	}

	private static class Separator extends Marker {
		public int dimension;
	}

	public void setLogLevel(Level level) {
		logger.setLevel(level);
	}

}
