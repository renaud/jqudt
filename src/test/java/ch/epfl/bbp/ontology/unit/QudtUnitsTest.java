package ch.epfl.bbp.ontology.unit;

import static ch.epfl.bbp.ontology.unit.QudtUnits.SecondTime;
import static ch.epfl.bbp.ontology.unit.QudtUnits.Week;
import static ch.epfl.bbp.ontology.unit.QudtUnits.Year;
import static org.junit.Assert.assertTrue;

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

	@Test
	public void testYear() throws IllegalArgumentException,
			IllegalAccessException {
		Quantity year = new Quantity(1, Year);
		Quantity weeks = year.convertTo(Week);
		Assert.assertEquals(Week, weeks.getUnit());
		Assert.assertEquals(52, weeks.getValue(), 0.25);
	}

	@Test
	public void testMoreAbrevs() throws IllegalArgumentException,
			IllegalAccessException {

		String secondsAbbrev = SecondTime.getAbbreviation();
		assertTrue("contains s", secondsAbbrev.contains("s"));
	}

	
	@Test
	public void testAll() throws Exception {

		int f = 0, nf = 0;

		for (String line : new LineReader(new FileInputStream(
		// "src/test/resources/sample_units.txt"))) {
				"src/test/resources/measures_histogram_10Msentences.txt"))) {
			
			String unitStr = line.split("\t")[0];
			int cnt = Integer.parseInt(line.split("\t")[1]);
			
			Unit found = UnitParser.parse(unitStr);

			if (found == null) {
				nf += cnt;
				System.out.println(unitStr + "\t" + cnt);
			} else {
				f += cnt;
			}
		}

		double ratio = (f + 0d) / (nf + f + 0d);
		System.out.println(ratio + " found:" + f + " notfound " + nf);
	}

	
}
