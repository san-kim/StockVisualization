package csci310.service;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import csci310.model.User;

public class CSVFileParsingClientTest extends Mockito {

	private static CSVFileParsingClient parser;

	@BeforeClass
	public static void setUp() throws SQLException {
		parser = new CSVFileParsingClient();
		CurrentSignedInUser.getInstance().signInCurrentUser(new User("username", 1));
	}

	@Test
	public void parseCSVFileTest() {
		try {
			String filename = "stocks_valid.csv";
			int result = parser.parseCSVFile(filename);
			assertTrue("Valid File: Actual result: " + result, result == 0);

			String malformedFilename = "stock_header_malformed.csv";
			result = parser.parseCSVFile(malformedFilename);
			assertTrue("Malformed Header: Actual result: " + result, result == -1);

			String malformedStockFilename = "malformed_stock.csv";
			result = parser.parseCSVFile(malformedStockFilename);
			assertTrue("Malformed Stock: Actual result: " + result, result == -2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void readFileTest() {
		String filename = "bulkStocks.csv";
		try {
			assertTrue(parser.readFile(filename) == null);
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + filename);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void parseStockTest() {
		String dataMissingTicker = "Apple Inc,21,946368000,1609142400";
		assertTrue(parser.parseStock(dataMissingTicker) == null);

		String dataMalformedQuantity = "Tesla,TSLA,TestMalformed,946368000,1609142400";
		assertTrue(parser.parseStock(dataMalformedQuantity) == null);
	}

}
