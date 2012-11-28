package com.github.jqudt.onto.units;

import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import com.github.jqudt.Quantity;
import com.github.jqudt.Unit;

public class QudtUnitsTest {

	@Test
	public void test() throws IllegalArgumentException, IllegalAccessException {
		Quantity temp = new Quantity(250, QudtUnits.MetricTon);
		Quantity temp2 = temp.convertTo(QudtUnits.Gram);
		Assert.assertEquals(QudtUnits.Gram, temp2.getUnit());
		Assert.assertEquals(250000000, temp2.getValue(), 0);
	}

	Pattern prefix = Pattern
			.compile("^(?:yotta|zetta|exa|peta|tera|giga|mega|kilo|hecto|deci|centi|milli|micro|nano|pico|femto|atto|zepto|yocto|E|P|T|G|M|k|h|d|c|m|µ|n|p|f)(.*?)");

	@Test
	public void testAll() throws Exception {

		int f = 0, nf = 0;

		for (String line : new LineReader(new FileInputStream(
				"src/test/resources/sample_units.txt"))) {

			for (String split : split(line)) {
				split = split.toLowerCase().trim();

				// System.out.println(split);

				Unit found = scroll(split);

				// remove s
				if (found == null && split.endsWith("s")) {
					found = scroll(split.substring(0, split.length() - 1));
				}

				// remove prefix
				if (found == null) {

					Matcher matcher = prefix.matcher(split);
					if (matcher.find()) {
						String noprefix = matcher.group(0);
						// System.out.println(" --" + noprefix);
						found = scroll(noprefix);
					}
				}

				if (found == null) {
					nf++;
					System.out.println("no " + split);// + " [[" + line + "]]");
				} else {
					f++;
				}
			}
		}

		System.out.println(f + "/" + nf);
	}

	private Unit scroll(String split) {
		Unit found = null;
		for (Unit u : QudtUnits.ALL_UNITS) {
			if (u.getLabel() != null
					&& split.equals(u.getLabel().toLowerCase())) {
				found = u;
			}
		}

		if (found == null) {
			for (Unit u : QudtUnits.ALL_UNITS) {
				if (!u.getAbbreviations().isEmpty()) {
					for (String abr : u.getAbbreviations()) {
						if (split.equals(abr.toLowerCase()))
							found = u;
					}
				}
			}
		}

		if (found == null) {
			for (Unit u : QudtUnits.ALL_UNITS) {
				if (u.getSymbol() != null
						&& split.equals(u.getSymbol().toLowerCase()

						)) {
					found = u;
				}
			}
		}
		return found;
	}

	private String[] split(String line) {

		// List l = new ArrayList<String>();

		if (line.indexOf(" per ") > -1) {
			return line.split(" per ");
		} else if (line.startsWith("per ")) {
			return new String[] { line.replaceAll("per ", "") };
		} else
			return line.split("/");

	}
}
