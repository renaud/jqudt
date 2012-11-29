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
	public void testConcentrations() throws IllegalArgumentException,
	IllegalAccessException {
		Quantity temp = new Quantity(250, QudtUnits.MetricTon);
		Quantity temp2 = temp.convertTo(QudtUnits.Gram);
		Assert.assertEquals(QudtUnits.Gram, temp2.getUnit());
		Assert.assertEquals(250000000, temp2.getValue(), 0);
	}

	
	

	
}
