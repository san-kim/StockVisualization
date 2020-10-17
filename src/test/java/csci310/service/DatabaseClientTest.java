package csci310.service;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import csci310.model.Portfolio;
import csci310.model.Stock;
import io.cucumber.java.Before;


public class DatabaseClientTest extends Mockito {

	private static DatabaseClient db;
	private static DatabaseClient mockDb;
	
	@BeforeClass 
	public static void setUp() {
		try {
			db = new DatabaseClient();
			// write to a different database than our actual data
			String url = "jdbc:sqlite:databaseTest.db";
			// create a connection to the database
			Connection connection = DriverManager.getConnection(url);
			db.setConnection(connection);
			mockDb = new DatabaseClient();
			mockDb.setConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
	}
	
	@Before 
	public void tableExists() throws SQLException {
		db.clearDatabase();
		db.createTable();
		
		db.createUser("username", "password");
		db.createUser("username2", "password");
		
		Stock fb = new Stock("Facebook", "FB", 2, 1599027025, 1601619025);
		Stock msft = new Stock("Microsoft", "MSFT", 2, 1599027025, 1601619025);
		Stock appl = new Stock("Apple", "APPL", 2, 1599027025, 1601619025);
		
		db.addStockToPortfolio(1, fb);
		db.addStockToPortfolio(1, msft);
		db.addStockToPortfolio(1, appl);
	}
	
	
	@Test
	public void testCreateTable() {
		if (db == null) {
			System.out.println("db is null");
		}
		assertTrue(db.createTable());
	}
	
	@Test 
	public void testCreateTableException() {
		try {
			Connection mockConn = mock(Connection.class);
			mockDb.setConnection(mockConn);
			when(mockConn.createStatement()).thenThrow(new SQLException());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mockDb.createTable();
		assertTrue(true);
	}

	@Test
	public void testCreateUser() {
		String username = "testUser2";
		String password = "password";
		assertTrue("New user",db.createUser(username, password));
		// Cannot have two users with the same username
		assertFalse("Duplicate username", db.createUser(username, password));
	}
	
	@Test
	public void testCreateUserThrowsException() {
		try {
			Connection mockConn = mock(Connection.class);
			mockDb.setConnection(mockConn);
			String query = "SELECT COUNT(*) FROM User WHERE username=?";
			when(mockConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());
			String username = "testUser2";
			String password = "password";
			mockDb.createUser(username, password);
			assertTrue(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetUser() throws NoSuchAlgorithmException {
		String username = "username1";
		String password = "password";
		
		PasswordAuthentication passAuth = new PasswordAuthentication();
		String hashedPass = passAuth.hash("password", null, null);
		
		assertTrue("New user",db.createUser(username, hashedPass));
		assertTrue(db.getUser(passAuth, username, password) >= 1);
		
		String wrongUsername = "wrongUsername";
		assertTrue(db.getUser(passAuth, wrongUsername, password) == 0);
		
		String wrongPassword = "wrongpass";
		assertTrue(db.getUser(passAuth, username, wrongPassword) == -2);
	}
	
	@Test
	public void testGetUserThrowsNoSuchAlgorithmException() {
		try {
			PasswordAuthentication mockPassAuth = mock(PasswordAuthentication.class);
			when(mockPassAuth.verify(anyString(), anyString())).thenThrow(new NoSuchAlgorithmException());
			String username = "username1";
			String password = "password";
			db.getUser(mockPassAuth, username, password);
			int result = db.getUser(mockPassAuth, username, password);
			assertTrue("Actual is " + result, result == -1);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetUserThrowsException() {
		try {
			Connection mockConn = mock(Connection.class);
			mockDb.setConnection(mockConn);
			String findUsernameQuery = "SELECT COUNT(*) FROM User WHERE username=?";
			when(mockConn.prepareStatement(findUsernameQuery, Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());
			String username = "testUser2";
			String password = "password";
			assertTrue(mockDb.getUser(new PasswordAuthentication(), username, password) == -1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddStockToPortfolio() {
		Stock testStock = new Stock("Apple", "APPL", 2, 1599027025, 1601619025);
		assertTrue(db.addStockToPortfolio(3, testStock));
		
		assertFalse(db.addStockToPortfolio(3, testStock));
	}
	
	@Test
	public void testAddStockToPortfolioThrowsException() {
		try {
			Connection mockConn = mock(Connection.class);
			mockDb.setConnection(mockConn);
			String query = "SELECT COUNT(*) FROM Portfolio WHERE userID=? AND tickerSymbol=?";
			when(mockConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());
			
			Stock appl = new Stock("Apple", "APPL", 2, 1599027025, 1601619025);
			assertTrue(mockDb.addStockToPortfolio(3, appl) == false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetPortfolio() {
		db.clearDatabase();
		db.createTable();
		
		Stock fb = new Stock("Facebook", "FB", 2, 1599027025, 1601619025);
		Stock msft = new Stock("Microsoft", "MSFT", 2, 1599027025, 1601619025);
		Stock appl = new Stock("Apple", "APPL", 2, 1599027025, 1601619025);
		
		db.addStockToPortfolio(1, fb);
		db.addStockToPortfolio(1, msft);
		db.addStockToPortfolio(2, appl);
		Portfolio p = db.getPortfolio(1);
		int size = p.getSize();
		assertTrue("Actual size: " + size, size == 2);
	}
	
	@Test 
	public void testGetPortfolioThrowsException() {
		try {
			Connection mockConn = mock(Connection.class);
			mockDb.setConnection(mockConn);
			String query = "SELECT name, tickerSymbol, quantity, datePurchased, dateSold FROM Portfolio WHERE userID=?";
			when(mockConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());
			assertTrue(mockDb.getPortfolio(1).getSize() == 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddStockToViewed() {
		Stock appl = new Stock("Apple", "APPL", 2, 1599027025, 1601619025);
		
		assertTrue(db.addStockToViewed(1, appl));
		assertFalse(db.addStockToViewed(1, appl));
	}
	
	@Test
	public void testAddStockToViewedThrowsException() {
		try {
			Connection mockConn = mock(Connection.class);
			mockDb.setConnection(mockConn);
			String query = "SELECT COUNT(*) FROM ViewedStocks WHERE userID=? AND tickerSymbol=?";
			when(mockConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());
			
			Stock appl = new Stock("Apple", "APPL", 2, 1599027025, 1601619025);
			assertTrue(mockDb.addStockToViewed(3, appl) == false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetViewedStocks() {
		db.clearDatabase();
		db.createTable();
		
		Stock fb = new Stock("Facebook", "FB", 2, 1599027025, 1601619025);
		Stock msft = new Stock("Microsoft", "MSFT", 2, 1599027025, 1601619025);
		Stock appl = new Stock("Apple", "APPL", 2, 1599027025, 1601619025);
		
		db.addStockToViewed(1, fb);
		db.addStockToViewed(1, msft);
		db.addStockToViewed(2, appl);
		Portfolio p = db.getViewedStocks(1);
		int size = p.getSize();
		assertTrue("Actual size: " + size, size == 2);
	}
	
	@Test 
	public void testGetViewedStocksThrowsException() {
		try {
			Connection mockConn = mock(Connection.class);
			mockDb.setConnection(mockConn);
			String query = "SELECT name, tickerSymbol, quantity, datePurchased, dateSold FROM ViewedStocks WHERE userID=?";
			when(mockConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());
			assertTrue(mockDb.getViewedStocks(1).getSize() == 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDeleteUser() {
		db.createUser("randomtest123", "asdfasdfasdf123");
		assertTrue(db.deleteUser("randomtest123"));
	}
	
	@Test
	public void testDeleteUserThrowsException() throws SQLException {
		db.createUser("randomtest1234", "asdfasdfasdf123");
		try {
//			Connection mockConn = mock(Connection.class);
//			mockDb.setConnection(mockConn);
//			when(mockConn.createStatement()).thenThrow(new SQLException());
//			
//			
			Connection mockConn = mock(Connection.class);
			mockDb.setConnection(mockConn);
			String query = "DELETE FROM User WHERE username=?";
			when(mockConn.prepareStatement(query)).thenThrow(new SQLException());
			assertTrue(mockDb.deleteUser("randomtest1234") == false);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DatabaseClient db_temp = new DatabaseClient();
		db_temp.deleteUser("randomtest1234");
		//assertTrue(true);
	}
	
	@Test
	public void testClearDatabase() {
		assertTrue(db.clearDatabase());
	}
	
	@Test
	public void testClearDatabaseThrowsException() {
		try {
			Connection mockConn = mock(Connection.class);
			mockDb.setConnection(mockConn);
			when(mockConn.createStatement()).thenThrow(new SQLException());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mockDb.clearDatabase();
		assertTrue(true);
	}
}




