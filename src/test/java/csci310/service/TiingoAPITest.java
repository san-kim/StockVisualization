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

}
