/* Copyright (C) 2012  Egon Willighagen <egonw@users.sf.net>
 *
 * License: new BSD
 */
package com.github.jqudt;

import java.net.URI;

import junit.framework.Assert;

import org.junit.Test;

public class UnitTest {

	@Test
	public void testResource() throws Exception {
		Unit unit = new Unit();
		URI resource = new URI("http://qudt.org/vocab/unit#Kelvin");
		Assert.assertNull(unit.getResource());
		unit.setResource(resource);
		Assert.assertNotNull(unit.getResource());
		Assert.assertEquals(resource, unit.getResource());
	}

	@Test
	public void testType() throws Exception {
		Unit unit = new Unit();
		URI resource = new URI("http://qudt.org/vocab/unit#Kelvin");
		Assert.assertTrue(unit.getTypes().isEmpty());
		unit.addType(resource);
		Assert.assertNotNull(unit.getTypes().get(0));
		Assert.assertEquals(resource, unit.getTypes().get(0));
	}

	@Test
	public void testLabel() throws Exception {
		Unit unit = new Unit();
		Assert.assertNull(unit.getLabel());
		unit.setLabel("nanomolar");
		Assert.assertNotNull(unit.getLabel());
		Assert.assertEquals("nanomolar", unit.getLabel());
	}

	@Test
	public void testAbbreviation() throws Exception {
		Unit unit = new Unit();
		Assert.assertTrue(unit.getAbbreviations().isEmpty());
		unit.addAbbreviation("nM");
		Assert.assertNotNull(unit.getAbbreviations().get(0));
		Assert.assertEquals("nM", unit.getAbbreviations().get(0));
	}

	@Test
	public void testSymbol() throws Exception {
		Unit unit = new Unit();
		Assert.assertNull(unit.getSymbol());
		unit.setSymbol("K");
		Assert.assertNotNull(unit.getSymbol());
		Assert.assertEquals("K", unit.getSymbol());
	}
}
