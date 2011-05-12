package org.cellularautomaton.util;

import java.util.Arrays;

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
	private int[] coords;

	/**
	 * Create an empty set of coordinates (no dimension)
	 */
	public Coords() {
		coords = new int[0];
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
	 * 
	 * @param dimensions
	 *            the dimensions of these coordinates
	 */
	public void setDimensions(int dimensions) {
		coords = Arrays.copyOf(coords, dimensions);
	}

	/**
	 * 
	 * @return the dimensions of these coordinates
	 */
	public int getDimensions() {
		return coords.length;
	}

	/**
	 * 
	 * @param dimension
	 *            the dimension to modify
	 * @param value
	 *            the value to apply as the coordinate
	 */
	public void set(int dimension, int value) {
		coords[dimension] = value;
	}

	/**
	 * 
	 * @param dimension
	 *            the dimension to read
	 * @return the value of the coordinate
	 */
	public int get(int dimension) {
		return coords[dimension];
	}

	/**
	 * This method override all the coordinates, this is the same thing as set
	 * the dimension and then set all the values.
	 * 
	 * @param coords
	 *            the coordinates to save
	 */
	public void setAll(int... coords) {
		this.coords = Arrays.copyOf(coords, coords.length);
	}

	/**
	 * 
	 * @return the complete coordinates
	 */
	public int[] getAll() {
		return Arrays.copyOf(coords, coords.length);
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

	@Override
	public String toString() {
		String s = "";
		for (int i : coords) {
			s += "," + i;
		}
		return "(" + (s.length() > 0 ? s.substring(1) : "") + ")";
	}

	/**
	 * A coordinate is equal to another coordinate if they have the same
	 * dimension and same values in each dimension. Moreover, instead of a
	 * {@link Coords} object, a simple int[] can be given.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Coords) {
			return compareTo((Coords) obj) == 0;
		} else if (obj instanceof int[]) {
			return equals(new Coords((int[]) obj));
		} else {
			return false;
		}
	}
}
