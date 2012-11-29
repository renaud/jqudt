package ch.epfl.bbp.ontology.unit;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;

import org.junit.Test;

import com.github.jqudt.Unit;

public class UnitParserTest {

	@Test
	public void testS() throws Exception {
		Unit unit = UnitParser.parse("mm");
		assertNotNull(unit);
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
