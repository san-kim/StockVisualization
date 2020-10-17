package csci310.service;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import csci310.model.Stock;

public class CSVFileParsingClientTest {
	
	private static CSVFileParsingClient parser;
	
	@BeforeClass 
	public static void setUp() {
		parser = new CSVFileParsingClient();
	}

	@Test
	public void parseCSVFileTest() {
		String filename = "bulkStocks.csv";
		assertTrue(parser.parseCSVFile(filename) == 0);
	}
	
	@Test
	public void readFileTest() {
		String filename = "bulkStocks.csv";
		assertTrue(parser.readFile(filename) == null);
	}
	
	@Test
	public void parseStockTest() {
		String dataMissingTicker = "Apple Inc,21,946368000,1609142400";
		assertTrue(parser.parseStock(dataMissingTicker) == null);
	}

}
