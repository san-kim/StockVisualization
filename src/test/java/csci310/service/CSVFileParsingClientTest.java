package csci310.service;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class CSVFileParsingClientTest {
	
	private static CSVFileParsingClient parser;
	
	@BeforeClass 
	public void setUp() {
		parser = new CSVFileParsingClient();
	}

	@Test
	public void parseCSVFileTest() {
		String filename = "bulkStocks.csv";
		assertTrue(parser.parseCSVFile(filename) == 0);
	}

}
