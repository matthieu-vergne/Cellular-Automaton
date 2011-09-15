package org.cellularautomaton.space.builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.rule.DynamicRule;
import org.cellularautomaton.rule.DynamicRule.RulePart;
import org.cellularautomaton.space.ISpace;
import org.cellularautomaton.space.builder.expression.Expression;
import org.cellularautomaton.space.builder.expression.ExpressionHelper;
import org.cellularautomaton.state.DynamicStateFactory;
import org.cellularautomaton.state.IStateFactory;
import org.cellularautomaton.util.Coords;
import org.cellularautomaton.util.Switcher;

/**
 * A file space builder allows to create a space from a description file.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 */
public class ScriptSpaceBuilder {
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

	public ScriptSpaceBuilder() {
		// do nothing
	}

	/**
	 * This method allows to create a space as described in a textual file. This
	 * file should follow this syntax :<br/>
	 * 
	 * <pre>
	 * TODO describe syntax + make Javadoc
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
			builder = new SpaceBuilder<Character>();
			rule = new DynamicRule<Character>();
			stateFactory = new DynamicStateFactory<Character>() {
				public void customize(ICell<Character> cell) {
					customizeCell(cell);
				};
			};

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

			builder.setRule(rule);
			builder.setStateFactory(stateFactory);
			builder.createNewSpace();
			Object reference = characterSpace;
			while (reference instanceof Object[]) {
				Object[] array = (Object[]) reference;
				builder.addDimension(array.length);
				reference = array[0];
			}
			Logger.getAnonymousLogger().info("Finalizing space...");
			builder.finalizeSpace();
			Logger.getAnonymousLogger().info("Space Finalized.");
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

	class Translator {
		class Candidate {
			public Separator separator = null;
			public int matrixLength = 0;
			public boolean isFollowingX = false;
		}

		private final Comparator<Candidate> CANDIDATE_COMPARATOR = new Comparator<Candidate>() {
			public int compare(Candidate c1, Candidate c2) {
				Integer v1 = Integer.valueOf(c1.separator.dimension);
				Integer v2 = Integer.valueOf(c2.separator.dimension);
				return v2.compareTo(v1);
			};
		};

		public boolean isFollowingX = true;
		public int xStart;
		public int yStart;
		public int xLength;
		public int yLength;
		public int matrixLength = 0;
		public Marker[][] description;
		public int separatorLength;

		public int getLengthOfTheGoodAxis() {
			return isFollowingX ? xLength : yLength;
		}

		private int[] getCoordsFollowingTheGoodAxis(int delta) {
			int[] coords = new int[] { xStart, yStart };
			int index = isFollowingX ? 0 : 1;
			coords[index] += delta;
			return coords;
		}

		public Object[] executeFinalCase() {
			if (yLength == 1) {
				isFollowingX = true;
			} else if (xLength == 1) {
				isFollowingX = false;
			} else {
				throw new IllegalStateException(
						"The final case cannot be executed if xLength and yLength are both > 1.");
			}
			Character[] array = new Character[getLengthOfTheGoodAxis()];
			for (int i = 0; i < getLengthOfTheGoodAxis(); i++) {
				int[] coords = getCoordsFollowingTheGoodAxis(i);
				Marker marker = description[coords[0]][coords[1]];
				array[i] = marker.character;
			}
			return array;
		}

		public Object[] executeIntermediaryCase() {
			// look for the available explicit separators
			final List<Candidate> candidates = new ArrayList<Candidate>();
			isFollowingX = true;
			lookForSeparatorsFollowingTheGoodAxis(candidates);
			isFollowingX = false;
			lookForSeparatorsFollowingTheGoodAxis(candidates);

			// if there is not, add the biggest implicit separator (first
			// dimensions)
			if (candidates.isEmpty()) {
				Candidate implicitCandidate = new Candidate();
				candidates.add(implicitCandidate);
				implicitCandidate.isFollowingX = height > 1;
				implicitCandidate.matrixLength = 1;
			}
			// otherwise, sort the separators
			else {
				Collections.sort(candidates, CANDIDATE_COMPARATOR);
			}

			// take the data of the best candidate
			Candidate bestCandidate = candidates.get(0);
			matrixLength = bestCandidate.matrixLength;
			isFollowingX = bestCandidate.isFollowingX;
			separatorLength = bestCandidate.separator == null ? 0 : 1;

			// execute the translation
			return translateSubmatrix();
		}

		private void lookForSeparatorsFollowingTheGoodAxis(
				final List<Candidate> candidates) {
			for (int i = 0; i < getLengthOfTheGoodAxis(); i++) {
				int[] coords = getCoordsFollowingTheGoodAxis(i);
				Marker marker = description[coords[0]][coords[1]];
				if (marker instanceof Separator) {
					Candidate candidate = new Candidate();
					candidate.separator = (Separator) marker;
					candidate.isFollowingX = isFollowingX;
					candidate.matrixLength = i;
					candidates.add(candidate);
				}
			}
		}

		private Object[] translateSubmatrix() {
			List<Object[]> subtranslations = new ArrayList<Object[]>();
			int step = matrixLength + separatorLength;
			for (int i = 0; i < getLengthOfTheGoodAxis(); i += step) {
				int[] coords = getCoordsFollowingTheGoodAxis(i);
				int lengthX = xLength;
				int lengthY = yLength;
				if (isFollowingX) {
					lengthX = matrixLength;
				} else {
					lengthY = matrixLength;
				}
				subtranslations.add(recursiveArrayBuilding(description,
						coords[0], coords[1], lengthX, lengthY));
			}
			return subtranslations.toArray(new Object[subtranslations.size()]);
		}

	}

	private Object[] recursiveArrayBuilding(Marker[][] description, int xStart,
			int yStart, int xLength, int yLength) {
		Object[] array;
		Translator executor = new Translator();
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
		checkOneDirectionDimensionsAreConstantKnowingTheyAreRegular(
				description, true);
		checkOneDirectionDimensionsAreConstantKnowingTheyAreRegular(
				description, false);
	}

	private void checkOneDirectionDimensionsAreConstantKnowingTheyAreRegular(
			Marker[][] description, boolean isFollowingX) {
		int length = isFollowingX ? height : width;
		Collection<Separator> checked = new HashSet<Separator>();
		for (int i = 0; i < length; i++) {
			int[] coords = getCoords(isFollowingX, 0, i);
			Marker marker = description[coords[0]][coords[1]];
			if (marker instanceof Separator && !checked.contains(marker)) {
				Separator separator = (Separator) marker;
				int i1 = i;
				while (i1 < length) {
					int i2 = i1 + 1;
					Marker m = null;
					int[] coords2 = getCoords(isFollowingX, 0, i2);
					while (i2 < length
							&& ((m = description[coords2[0]][coords2[1]]) instanceof Cell || ((Separator) m).dimension < separator.dimension)) {
						i2++;
						coords2 = getCoords(isFollowingX, 0, i2);
					}
					if (i2 - i1 - 1 != i) {
						String sense = isFollowingX ? "horizontal" : "vertical";
						throw new BadFileContentException("the " + sense
								+ " separator " + separator.character
								+ " does not give a constant dimension (" + i
								+ " & " + (i2 - i1) + ").");
					}
					i1 = i2;
				}
				checked.add(separator);
			}
		}
	}

	private int[] getCoords(boolean isFollowingX, int staticValue,
			int dynamicValue) {
		int[] coords = new int[] { staticValue, staticValue };
		coords[isFollowingX ? 0 : 1] = dynamicValue;
		return coords;
	}

	private void checkDimensionsAreRegular(Marker[][] description) {
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				Marker marker = description[x][y];
				if (marker instanceof Separator) {
					Separator separator = (Separator) marker;
					if (!isSeparatorConsistent(description, separator, y, true)
							&& !isSeparatorConsistent(description, separator,
									x, false)) {
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

	private boolean isSeparatorConsistent(Marker[][] description,
			Separator separator, int index, boolean isFollowingX) {
		int length = isFollowingX ? height : width;
		for (int i = 0; i < length; i++) {
			int[] coords = getCoords(isFollowingX, index, i);
			Marker marker2 = description[coords[0]][coords[1]];
			if (marker2 instanceof Cell
					|| separator.dimension > ((Separator) marker2).dimension) {
				return false;
			}
		}
		return true;
	}

	private void preciseSeparators(Marker[][] description) {
		Map<Character, Separator> verticalSeparators = getSeparators(
				description, true);
		Map<Character, Separator> horizontalSeparators = getSeparators(
				description, false);

		// initialize dimensions
		int dimension = 0;
		Switcher<Map<Character, Separator>> mapSwitcher = new Switcher<Map<Character, Separator>>();
		mapSwitcher.add(horizontalSeparators);
		mapSwitcher.add(verticalSeparators);
		Switcher<Iterator<Character>> iteratorSwitcher = new Switcher<Iterator<Character>>();
		iteratorSwitcher.add(horizontalSeparators.keySet().iterator());
		iteratorSwitcher.add(verticalSeparators.keySet().iterator());
		while (!iteratorSwitcher.isEmpty() && iteratorSwitcher.get().hasNext()) {
			mapSwitcher.get().get(iteratorSwitcher.get().next()).dimension = dimension;
			dimension++;
			mapSwitcher.switchComponent();
			iteratorSwitcher.switchComponent();
			if (!iteratorSwitcher.get().hasNext()) {
				iteratorSwitcher.remove(iteratorSwitcher.get());
				mapSwitcher.remove(mapSwitcher.get());
			}
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

	private Map<Character, Separator> getSeparators(Marker[][] description,
			boolean isFollowingX) {
		Map<Character, Separator> separators = new LinkedHashMap<Character, Separator>();
		int length = isFollowingX ? height : width;
		int[] coords = getCoords(isFollowingX, 0, 1);
		if (length > 1 && description[coords[0]][coords[1]] instanceof Cell) {
			separators.put(null, new Separator());
		}
		for (int i = 0; i < length; i++) {
			int[] coords2 = getCoords(isFollowingX, 0, i);
			Marker marker = description[coords2[0]][coords2[1]];
			Character character = marker.character;
			if (marker instanceof Separator
					&& !separators.containsKey(character)) {
				Separator separator = new Separator();
				separator.character = character;
				separators.put(character, separator);
			}
		}
		return separators;
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
		List<Character> possibleStates = stateFactory.getPossibleStates();

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

}
