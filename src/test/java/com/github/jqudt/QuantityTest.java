/* Copyright (C) 2012  Egon Willighagen <egonw@users.sf.net>
 *
 * License: new BSD
 */
package com.github.jqudt;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import ch.epfl.bbp.ontology.unit.UnitParser;

import com.github.jqudt.onto.units.ConcentrationUnit;
import com.github.jqudt.onto.units.TemperatureUnit;

public class QuantityTest {

	@Test
	public void testConstructorNullUnit() {
		Quantity quantity = new Quantity(0.1, null);
		assertEquals(0.1, quantity.getValue(), 0.01);
		assertNull(quantity.getUnit());
	}

	@Test
	public void testConstructor() {
		Quantity quantity = new Quantity(0.1, TemperatureUnit.CELSIUS);
		assertEquals(0.1, quantity.getValue(), 0.01);
		assertEquals(TemperatureUnit.CELSIUS, quantity.getUnit());
	}

	@Test
	public void testIsConc() throws Exception {

		Quantity q = new Quantity(0.1, TemperatureUnit.CELSIUS);
		assertFalse(q.isConcentration());

		Quantity q2 = new Quantity(0.1, ConcentrationUnit.MICROMOLAR);
		assertTrue(q2.isConcentration());

//		Unit m = UnitParser.parse("M");
		Unit m = UnitParser.uncachedParse("mM");
		assertNotNull(m);
		Quantity q3 = new Quantity(0.1, m);
		
		Unit unit = q3.getUnit();
		
		assertTrue(q3.isConcentration());
	}
}
