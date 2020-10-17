package csci310.service;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import csci310.service.TiingoAPI.Pair;

public class TiingoAPITest {

	@Test
	public void testStockPrice() throws IOException {
		TiingoAPI stock = new TiingoAPI();
		double price = stock.stockPrice("AAPL");
		assertTrue(price > 50);
	}
	
	@Test
	public void testStockPriceFromDate() throws IOException {
		TiingoAPI stock = new TiingoAPI();
		double price = stock.stockPriceFromDate("AAPL", "10-05-06");
		assertTrue(price > 25);
	}
	
	@Test
	public void testStockPriceFromRange() throws IOException {
		TiingoAPI stock = new TiingoAPI();
		double price = stock.stockPriceFromRange("AMZN", "2012-1-1", "2012-1-15");
		assertTrue(price > 25);
	}

}
