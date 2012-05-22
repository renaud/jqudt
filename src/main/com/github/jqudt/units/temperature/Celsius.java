/* Copyright (C) 2012  Egon Willighagen <egonw@users.sf.net>
 *
 * License: new BSD
 */
package com.github.jqudt.units.temperature;

import java.net.URI;
import java.net.URISyntaxException;

import com.github.jqudt.Multiplier;
import com.github.jqudt.Unit;

public class Celsius extends Unit {

	private static Unit instance;

	private Celsius() {
		setAbbreviation("C");
		setLabel("Celsius");
		Multiplier multiplier = new Multiplier();
		multiplier.setMultiplier(1.0);
		multiplier.setOffset(273.15);
		setMultiplier(multiplier);
		try {
			setResource(new URI("http://qudt.org/vocab/unit#DegreeCelsius"));
			setType(new URI("http://qudt.org/vocab/unit#Kelvin"));
		} catch (URISyntaxException e) { /* this exception will never happen */ } 
		setSymbol("K");
	}

	public static Unit getInstance() {
		if (instance == null) instance = new Celsius();
		return instance;
	}

}
