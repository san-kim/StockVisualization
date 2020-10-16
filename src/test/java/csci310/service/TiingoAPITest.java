package csci310.service;

import static org.junit.Assert.*;

import org.junit.Test;

public class TiingoAPITest {

	@Test
	public void testStockPrice() {
		TiingoAPI stock = new TiingoAPI();
		double price = stock.stockPrice("AAPL");
		assertTrue(price > 50);
	}

}
