package ch.epfl.bbp.ontology.unit;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.jqudt.Unit;
import com.github.jqudt.onto.UnitFactory;
import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Unit parser for the QUDT ontology
 * 
 * @author renaud.richardet@epfl.ch
 */
public class UnitParser {

	private static final Pattern prefix = Pattern
			.compile("^(?:yotta|zetta|exa|peta|tera|giga|mega|kilo|hecto|deci|centi|milli|micro|nano|pico|femto|atto|zepto|yocto|E|P|T|G|M|k|h|d|c|m|µ|n|p|f)(.*?)");

	private static UnitFactory unitFactory = UnitFactory.getInstance();

	private static LoadingCache<String, Optional<Unit>> unitsCache = CacheBuilder
			.newBuilder().maximumSize(5000)
			// .expireAfterWrite(10, TimeUnit.MINUTES)
			// .removalListener(MY_LISTENER)
			.build(new CacheLoader<String, Optional<Unit>>() {
				public Optional<Unit> load(String key) throws Exception {
					return Optional.fromNullable(uncachedParse(key));
				}
			});

	/**
	 * @param unitStr
	 *            a unit given as a string
	 * @return a {@link Unit} object grounded to the QUDT ontology
	 * @throws ExecutionException
	 */
	public static Unit parse(String unitStr) throws ExecutionException {
		return unitsCache.get(unitStr).orNull();
	}

	/**
	 * @param unitStr
	 *            a unit given as a string
	 * @return a {@link Unit} object grounded to the QUDT ontology
	 */
	public static Unit uncachedParse(String unitStr) {
		//System.out.println(unitStr);

		unitStr = unitStr.replaceAll(" per ", " / ").trim();

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

		// lowercase
		if (u == null) {
			u = scrollLowerCase(unitStr.toLowerCase());
		}

		return u;
	}

	/** Scrolls to every's Unit label, then abbreviation, then symbol */
	private static Unit scroll(String s) {
		Unit found = null;
		for (Unit u : unitFactory.getAllUnits()) {
			if (u.getLabel() != null && s.equals(u.getLabel())) {
				return u;
			}
		}

		if (found == null) {
			for (Unit u : unitFactory.getAllUnits()) {
				if (u.getAbbreviation() != null
						&& s.equals(u.getAbbreviation()))
					return u;
			}
		}

		if (found == null) {
			for (Unit u : unitFactory.getAllUnits()) {
				if (u.getSymbol() != null && s.equals(u.getSymbol())) {
					return u;
				}
			}
		}
		return null;
	}
	/** Scrolls to every's Unit label, then abbreviation, then symbol */
	private static Unit scrollLowerCase(String s) {
		Unit found = null;
		for (Unit u : unitFactory.getAllUnits()) {
			if (u.getLabel() != null && s.equals(u.getLabel().toLowerCase())) {
				return u;
			}
		}
		
		if (found == null) {
			for (Unit u : unitFactory.getAllUnits()) {
				if (u.getAbbreviation() != null
						&& s.equals(u.getAbbreviation().toLowerCase()))
					return u;
			}
		}
		
		if (found == null) {
			for (Unit u : unitFactory.getAllUnits()) {
				if (u.getSymbol() != null && s.equals(u.getSymbol().toLowerCase())) {
					return u;
				}
			}
		}
		return null;
	}
}
