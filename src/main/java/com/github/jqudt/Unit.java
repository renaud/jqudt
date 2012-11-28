/* Copyright (C) 2012  Egon Willighagen <egonw@users.sf.net>
 *
 * License: new BSD
 */
package com.github.jqudt;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Unit {

	private URI resource;
	private String label;
	private List<String> abbreviations = new ArrayList<String>();
	private String symbol;
	private List<URI> types = new ArrayList<URI>();

	private Multiplier multiplier;

	public URI getResource() {
		return resource;
	}

	public void setResource(URI resource) {
		this.resource = resource;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<String> getAbbreviations() {
		return abbreviations;
	}

	public void setAbbreviations(List<String> abbreviations) {
		this.abbreviations = abbreviations;
	}

	public void addAbbreviation(String abbreviation) {
		this.abbreviations.add(abbreviation);
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Multiplier getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(Multiplier multiplier) {
		this.multiplier = multiplier;
	}

	public List<URI> getTypes() {
		return types;
	}

	public void setTypes(List<URI> types) {
		this.types = types;
	}

	public void addType(URI type) {
		this.types.add(type);
	}

	public String toString() {
		return this.getAbbreviations().toString();
	}
}
