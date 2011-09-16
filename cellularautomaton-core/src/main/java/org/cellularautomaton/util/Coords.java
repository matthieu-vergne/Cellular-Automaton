package org.cellularautomaton.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A tool to encapsulate the coordinate methods.
 * 
 * @author Matthieu Vergne (matthieu.vergne@gmail.com)
 * 
 */
public class Coords implements Comparable<Coords> {

	/**
	 * The coordinates themselves.
	 */
	private List<Integer> coords = new ArrayList<Integer>();
	/**
	 * Indicate if the coordinates can be modified.
	 */
	private boolean isMutable = true;

	/**
	 * Create an empty set of coordinates (no dimension)
	 */
	public Coords() {
		// nothing to do
	}

	/**
	 * Create a new coordinates with the given values.
	 * 
	 * @param coords
	 *            the coordinates
	 */
	public Coords(int... coords) {
		this();
		setAll(coords);
	}

	/**
	 * Create a new coordinates regarding the given string. This string must
	 * have the same format than {@link #toString()}.
	 * 
	 * @param string
	 *            the string representing the coordinates
	 */
	public Coords(final String string) {
		String buffer = string.replaceAll("\\s", "");
		final String regexDim = "[+-]?[0-9]+";
		final String regexCoords = String.format("\\(%s(,%s)*\\)", regexDim,
				regexDim);
		if (!buffer.matches(regexCoords)) {
			throw new IllegalArgumentException(
					"The coords string is not well-written : " + string);
		} else {
			String[] split = buffer.substring(1, buffer.length() - 1)
					.split(",");
			int[] coords = new int[split.length];
			for (int i = 0; i < split.length; i++) {
				coords[i] = Integer.parseInt(split[i].replace("+", ""));
			}
			setAll(coords);
		}
	}

	/**
	 * 
	 * @param dimensions
	 *            the dimensions of these coordinates
	 */
	public void setDimensions(int dimensions) {
		while (getDimensions() < dimensions) {
			coords.add(0);
		}
		while (getDimensions() > dimensions) {
			coords.remove(coords.size() - 1);
		}
	}

	/**
	 * 
	 * @return the dimensions of these coordinates
	 */
	public int getDimensions() {
		return coords.size();
	}

	/**
	 * 
	 * @param dimension
	 *            the dimension to modify
	 * @param value
	 *            the value to apply as the coordinate
	 */
	public void set(int dimension, int value) {
		coords.set(dimension, value);
	}

	/**
	 * 
	 * @param dimension
	 *            the dimension to read
	 * @return the value of the coordinate
	 */
	public int get(int dimension) {
		return coords.get(dimension);
	}

	/**
	 * This method override all the coordinates, this is the same thing as set
	 * the dimension and then set all the values.
	 * 
	 * @param coords
	 *            the coordinates to save
	 */
	public void setAll(int... coords) {
		this.coords.clear();
		for (int coord : coords) {
			this.coords.add(coord);
		}
	}

	/**
	 * 
	 * @return the complete coordinates
	 */
	public int[] getAll() {
		int[] coords = new int[getDimensions()];
		for (int i = 0; i < getDimensions(); i++) {
			coords[i] = get(i);
		}
		return coords;
	}

	/**
	 * If both coordinates have not the same dimensions, the comparison is done
	 * on the dimensions, else it is done on the values of each coordinate.
	 */
	public int compareTo(Coords o) {
		if (getDimensions() != o.getDimensions()) {
			return Integer.valueOf(getDimensions()).compareTo(
					Integer.valueOf(o.getDimensions()));
		} else {
			for (int dim = 0; dim < getDimensions(); dim++) {
				if (get(dim) != o.get(dim)) {
					return Integer.valueOf(get(dim)).compareTo(
							Integer.valueOf(o.get(dim)));
				}
			}
			return 0;
		}
	}

	/**
	 * Display the coordinates Ci in the form (C0, ..., Cn).
	 */
	@Override
	public String toString() {
		String string = Arrays.toString(coords.toArray());
		return "(" + string.substring(1, string.length() - 1) + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(coords.toArray());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Coords) {
			return compareTo((Coords) obj) == 0;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @return true if the coordinates can be modified, false otherwise
	 */
	public boolean isMutable() {
		return isMutable;
	}

	/**
	 * 
	 * @param isMutable
	 *            indicate if the coordinates can be modified
	 */
	public void setMutable(boolean isMutable) {
		this.isMutable = isMutable;
		if (!isMutable) {
			coords = Collections.unmodifiableList(coords);
		} else {
			coords = new ArrayList<Integer>(coords);
		}
	}
}
