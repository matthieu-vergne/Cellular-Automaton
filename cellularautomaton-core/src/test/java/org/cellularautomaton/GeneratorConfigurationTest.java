package org.cellularautomaton;

import junit.framework.TestCase;

public class GeneratorConfigurationTest extends TestCase {

	public void testIsValid() {
		GeneratorConfiguration<String> config = new GeneratorConfiguration<String>();

		// check init
		assertFalse(config.isValid());

		// check minimum
		config.initialState = "";
		config.dimensionSizes = new int[] { 1 };
		assertTrue(config.isValid());

		// check initial state
		config.initialState = null;
		assertFalse(config.isValid());
		config.initialState = "";
		assertTrue(config.isValid());

		// check dimensions
		config.dimensionSizes = null;
		assertFalse(config.isValid());
		config.dimensionSizes = new int[] {};
		assertFalse(config.isValid());
		config.dimensionSizes = new int[] { 0 };
		assertFalse(config.isValid());
		config.dimensionSizes = new int[] { 1, 5, 0, 3 };
		assertFalse(config.isValid());
		config.dimensionSizes = new int[] { 1, 5, 4, 3 };
		assertTrue(config.isValid());
	}

	public void testDimensions() {
		GeneratorConfiguration<String> config = new GeneratorConfiguration<String>();

		config.dimensionSizes = new int[] {};
		assertEquals(0, config.getDimensions());

		config.dimensionSizes = new int[] { 4 };
		assertEquals(1, config.getDimensions());

		config.dimensionSizes = new int[] { 1, 5, 0 };
		assertEquals(3, config.getDimensions());
	}

	public void testCellsCount() {
		GeneratorConfiguration<String> config = new GeneratorConfiguration<String>();

		config.dimensionSizes = new int[] { 4 };
		assertEquals(4, config.getCellsCount());

		config.dimensionSizes = new int[] { 1, 5, 3 };
		assertEquals(1 * 5 * 3, config.getCellsCount());
	}
}
