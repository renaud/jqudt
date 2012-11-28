package ch.epfl.bbp.ontology.unit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.jqudt.Unit;

/**
 * Unit parser for the QUDT ontology
 * 
 * @author renaud.richardet@epfl.ch
 */
public class UnitParser {

	private static final Pattern prefix = Pattern
			.compile("^(?:yotta|zetta|exa|peta|tera|giga|mega|kilo|hecto|deci|centi|milli|micro|nano|pico|femto|atto|zepto|yocto|E|P|T|G|M|k|h|d|c|m|µ|n|p|f)(.*?)");

	/**
	 * @param unitStr
	 *            a unit given as a string
	 * @return a {@link Unit} object grounded to the QUDT ontology
	 */
	public static Unit parse(String unitStr) {

		unitStr = unitStr.replaceAll(" per ", " / ").toLowerCase().trim();

		Unit u = scroll(unitStr);

		// stem: remove leading dash and ending 's'
		if (u == null) {
			if (unitStr.endsWith("s"))
				unitStr = unitStr.substring(0, unitStr.length() - 1);
			if (unitStr.startsWith("-"))
				unitStr = unitStr.substring(1);
			u = scroll(unitStr);
		}

		// remove units prefix
		if (u == null) {
			Matcher matcher = prefix.matcher(unitStr);
			if (matcher.find()) {
				String noprefix = matcher.group(0);
				u = scroll(noprefix);
			}
		}

		return u;
	}

	/** Scrolls to every's Unit label, then abreviation, then symbol */
	private static Unit scroll(String s) {
		Unit found = null;
		for (Unit u : QudtUnits.ALL_UNITS) {
			if (u.getLabel() != null && s.equals(u.getLabel().toLowerCase())) {
				found = u;
			}
		}

		if (found == null) {
			for (Unit u : QudtUnits.ALL_UNITS) {
				if (u.getAbbreviation() != null
						&& s.equals(u.getAbbreviation().toLowerCase()))
					found = u;
			}
		}

		if (found == null) {
			for (Unit u : QudtUnits.ALL_UNITS) {
				if (u.getSymbol() != null
						&& s.equals(u.getSymbol().toLowerCase())) {
					found = u;
				}
			}
		}
		return found;
	}
}
