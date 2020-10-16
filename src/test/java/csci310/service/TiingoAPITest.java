package csci310.service;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

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
		double price = stock.stockPriceFromDate("AAPL", "2010-05-06");
		assertTrue(price > 25);
	}

}
