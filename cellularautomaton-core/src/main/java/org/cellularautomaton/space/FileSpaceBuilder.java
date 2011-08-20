package org.cellularautomaton.space;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.state.AbstractStateFactory;
import org.cellularautomaton.state.IStateFactory;

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

	private final SpaceBuilder<Character> builder = new SpaceBuilder<Character>();
	private final Set<Mode> missingSteps = new HashSet<Mode>();
	private Mode actualMode;
	private Character[] states;
	private int memorySize;

	/**
	 * This method allows to create a space as described in a textual file. This
	 * file should follow this structure :<br/>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * @param filePath
	 *            the path to the building file
	 */
	public void createSpaceFromFile(String filePath) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(filePath));
			String line;
			actualMode = null;
			missingSteps.addAll(Arrays.asList(Mode.values()));
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
					// TODO
				} else if (actualMode == Mode.CELLS) {
					// TODO
				} else {
					throw new BadFormatFileException("Unparsable line : "
							+ line);
				}
			}
			in.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		IStateFactory<Character> stateFactory = new AbstractStateFactory<Character>() {
			private final List<Character> stateList = Arrays.asList(states);
			
			@Override
			public List<Character> getPossibleStates() {
				return stateList;
			}
			
			@Override
			public void customize(ICell<Character> cell) {
				// TODO
			}
		};
		builder.setStateFactory(stateFactory);
		builder.setMemorySize(memorySize);
	}

	private void saveConfig(String key, String value) {
		if (key.equals("states")) {
			states = new Character[value.length()];
			for(int i = 0 ; i < value.length() ; i ++) {
				states[i] = value.charAt(i);
			}
		} else if (key.equals("memorySize")) {
			memorySize = Integer.parseInt(value);
		} else {
			throw new IllegalArgumentException("Unknown key : " + key);
		}
	}

	private void switchMode(String header) {
		if (header.equals("[config]")) {
			if (actualMode == null) {
				actualMode = Mode.CONFIG;
				missingSteps.remove(actualMode);
			} else if (Arrays.asList(Mode.CONFIG, Mode.RULE, Mode.CELLS)
					.contains(actualMode)) {
				throw new BadFormatFileException("The " + header
						+ " header appears more than one time.");
			} else {
				throw new IllegalStateException("the mode " + actualMode
						+ " is unknown.");
			}
		} else if (header.equals("[rule]")) {
			if (actualMode == null) {
				throw new BadFormatFileException(
						"The configuration part is missing.");
			} else if (actualMode == Mode.CONFIG) {
				actualMode = Mode.RULE;
				missingSteps.remove(actualMode);
			} else if (Arrays.asList(Mode.RULE, Mode.CELLS)
					.contains(actualMode)) {
				throw new BadFormatFileException("The " + header
						+ " header appears more than one time.");
			} else {
				throw new IllegalStateException("the mode " + actualMode
						+ " is unknown.");
			}
		} else if (header.equals("[cells]")) {
			if (actualMode == null) {
				throw new BadFormatFileException(
						"The configuration part is missing.");
			} else if (actualMode == Mode.CONFIG) {
				throw new BadFormatFileException("The rule part is missing.");
			} else if (actualMode == Mode.RULE) {
				actualMode = Mode.CELLS;
				missingSteps.remove(actualMode);
			} else if (Arrays.asList(Mode.CELLS).contains(actualMode)) {
				throw new BadFormatFileException("The " + header
						+ " header appears more than one time.");
			} else {
				throw new IllegalStateException("the mode " + actualMode
						+ " is unknown.");
			}
		} else {
			throw new BadFormatFileException("The header " + header
					+ " is not a valid one.");
		}
	}

	public IStateFactory<Character> getStateFactory() {
		return builder.getStateFactory();
	}

	public ISpace<Character> getSpaceOfCell() {
		return builder.getSpaceOfCell();
	}
}
