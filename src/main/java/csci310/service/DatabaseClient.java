package csci310.service;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import csci310.model.Portfolio;
import csci310.model.Stock;

/**
 *
 * @author sqlitetutorial.net
 */
public class DatabaseClient {
	
	private Connection connection;
	
	public DatabaseClient() throws SQLException {
		// db parameters
		String url = "jdbc:sqlite:database.db";
		// create a connection to the database
		connection = DriverManager.getConnection(url);

		System.out.println("Connection to SQLite has been established.");
	}
	
	public void setConnection(Connection c) {
		connection = c;
	}
	
	public boolean createTable() {
		String createTable = "CREATE TABLE IF NOT EXISTS User ("
        		+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
        		+ "username TEXT NOT NULL,"
        		+ "password TEXT NOT NULL"
        		+ ");"; 
		String createPortfolioTable = "CREATE TABLE IF NOT EXISTS Portfolio ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "name TEXT NOT NULL,"
				+ "tickerSymbol TEXT NOT NULL,"
				+ "quantity INTEGER NOT NULL,"
				+ "datePurchased INTEGER NOT NULL,"
				+ "dateSold INTEGER NOT NULL,"
				+ "userID INTEGER NOT NULL"
				+ ");";
		String createViewedStockTable = "CREATE TABLE IF NOT EXISTS ViewedStocks ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "name TEXT NOT NULL,"
				+ "tickerSymbol TEXT NOT NULL,"
				+ "quantity INTEGER NOT NULL,"
				+ "datePurchased INTEGER NOT NULL,"
				+ "dateSold INTEGER NOT NULL,"
				+ "userID INTEGER NOT NULL"
				+ ");";
        Statement createTableStatement;
        try {
        	createTableStatement = connection.createStatement();
        	createTableStatement.executeUpdate(createTable); 
        	createTableStatement.executeUpdate(createPortfolioTable);
        	createTableStatement.executeUpdate(createViewedStockTable);
        	return true;
        } catch(SQLException e) {
        	e.printStackTrace();
        	return false;
        }
	}
	
	
	public boolean createUser(String username, String password) {
		try {
			boolean uniqueUsername = true;
			String query = "SELECT COUNT(*) FROM User WHERE username=?";
			PreparedStatement checkUsername = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			checkUsername.setString(1, username);
			ResultSet rs = checkUsername.executeQuery();
			while (rs.next()) {
				// if a user already exists with this username, this username is not unique
				uniqueUsername = (rs.getInt(1) == 0);
			}
			if (uniqueUsername) {
				String createUserQuery = "INSERT INTO User(username, password)"
										 + "VALUES(?,?);";
				PreparedStatement createUser = connection.prepareStatement(createUserQuery);
				createUser.setString(1, username);
				createUser.setString(2, password);
				createUser.executeUpdate();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Assuming that date objects will be stored as unix time 
	// Reference: https://stackoverflow.com/questions/2881321/how-to-insert-date-in-sqlite-through-java
	// Reference: https://stackoverflow.com/questions/3371326/java-date-from-unix-timestamp
	// Reference: https://stackoverflow.com/questions/17432735/convert-unix-time-stamp-to-date-in-java
	public boolean addStockToPortfolio(Integer userID, Stock stock) {
		try {
			// Get stock parameters from Stock object
			String name = stock.getName();
			String tickerSymbol = stock.getTicker();
			int quantity = stock.getQuantity();
			Integer datePurchased = stock.getBuyDate();
			Integer dateSold = stock.getSellDate();
			
			boolean inPortfolio = false;
			String query = "SELECT COUNT(*) FROM Portfolio WHERE userID=? AND tickerSymbol=?";
			PreparedStatement checkContainsStock = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			checkContainsStock.setInt(1, userID);
			checkContainsStock.setString(2, tickerSymbol);
			ResultSet rs = checkContainsStock.executeQuery();
			while (rs.next()) {
				// if this stock already exists in the portfolio of this user
				inPortfolio = (rs.getInt(1) != 0);
			}
			if (!inPortfolio) {
				String createStockQuery = "INSERT INTO Portfolio(name, tickerSymbol, quantity, datePurchased, dateSold, userID)"
										 + "VALUES(?,?,?,?,?,?);";
				PreparedStatement createStock = connection.prepareStatement(createStockQuery);
				createStock.setString(1, name);
				createStock.setString(2, tickerSymbol);
				createStock.setInt(3, quantity);
				createStock.setInt(4, datePurchased);
				createStock.setInt(5, dateSold);
				createStock.setInt(6, userID);
				createStock.executeUpdate();
				return true;
			} else {
				// Can users add stock that already exists in their Portfolio?
				// should we just overwrite the previous values?
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Portfolio getPortfolio(Integer userID) {
		Portfolio portfolio = new Portfolio();
		String query = "SELECT name, tickerSymbol, quantity, datePurchased, dateSold FROM Portfolio WHERE userID=?";
		PreparedStatement getPortfolioStocks;
		try {
			getPortfolioStocks = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			getPortfolioStocks.setInt(1, userID);
			ResultSet rs = getPortfolioStocks.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name"); 
				String tickerSymbol = rs.getString("tickerSymbol");
				int quantity = rs.getInt("quantity");
				int datePurchased = rs.getInt("datePurchased");
				int dateSold = rs.getInt("dateSold");
				portfolio.addStock(new Stock(name, tickerSymbol, quantity, datePurchased, dateSold));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return portfolio; 
	}
	
	public boolean addStockToViewed(Integer userID, Stock stock) {
		try {
			// Get stock parameters from Stock object
			String name = stock.getName();
			String tickerSymbol = stock.getTicker();
			int quantity = stock.getQuantity();
			Integer datePurchased = stock.getBuyDate();
			Integer dateSold = stock.getSellDate();
						
			boolean alreadyViewedStock = false;
			String query = "SELECT COUNT(*) FROM ViewedStocks WHERE userID=? AND tickerSymbol=?";
			PreparedStatement checkContainsStock = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			checkContainsStock.setInt(1, userID);
			checkContainsStock.setString(2, tickerSymbol);
			ResultSet rs = checkContainsStock.executeQuery();
			while (rs.next()) {
				// if this stock already exists in the viewed stock list of this user
				alreadyViewedStock = (rs.getInt(1) != 0);
			}
			if (!alreadyViewedStock) {
				String createStockQuery = "INSERT INTO ViewedStocks(name, tickerSymbol, quantity, datePurchased, dateSold, userID)"
										 + "VALUES(?,?,?,?,?,?);";
				PreparedStatement createStock = connection.prepareStatement(createStockQuery);
				createStock.setString(1, name);
				createStock.setString(2, tickerSymbol);
				createStock.setInt(3, quantity);
				createStock.setInt(4, datePurchased);
				createStock.setInt(5, dateSold);
				createStock.setInt(6, userID);
				createStock.executeUpdate();
				return true;
			} else {
				// Can users add stock that they've already viewed?
				// should we just overwrite the previous values?
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Portfolio getViewedStocks(Integer userID) {
		Portfolio portfolio = new Portfolio();
		String query = "SELECT name, tickerSymbol, quantity, datePurchased, dateSold FROM ViewedStocks WHERE userID=?";
		PreparedStatement getViewedStocks;
		try {
			getViewedStocks = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			getViewedStocks.setInt(1, userID);
			ResultSet rs = getViewedStocks.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name"); 
				String tickerSymbol = rs.getString("tickerSymbol");
				int quantity = rs.getInt("quantity");
				int datePurchased = rs.getInt("datePurchased");
				int dateSold = rs.getInt("dateSold");
				portfolio.addStock(new Stock(name, tickerSymbol, quantity, datePurchased, dateSold));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return portfolio; 
	}
	
	public int getUser(PasswordAuthentication passAuth, String username, String password) {
		try {
			boolean usernameExists = false;
			boolean correctPassword = false;
			String findUsernameQuery = "SELECT COUNT(*) FROM User WHERE username=?";
			PreparedStatement findUsername = connection.prepareStatement(findUsernameQuery, Statement.RETURN_GENERATED_KEYS);
			findUsername.setString(1, username);
			ResultSet rs = findUsername.executeQuery();
			
			while(rs.next()) {
				// Validates that there exists a single user with this corresponding username
				usernameExists = (rs.getInt(1) == 1);
			} 
			
			if(usernameExists) {
				String passwordCheckQuery = "SELECT id, password FROM User WHERE username=?";
				PreparedStatement passwordCheck = connection.prepareStatement(passwordCheckQuery);
				passwordCheck.setString(1, username);
				rs = passwordCheck.executeQuery();
				int userID = -1;
				while(rs.next()) {
					String dbPass = rs.getString("password");
					correctPassword = passAuth.verify(password, dbPass);
					userID = rs.getInt("id");
				}
				if(correctPassword) {
					// Password is valid (return userID)
					return userID;
				} else {
					// Password is invalid (return -2)
					return -2;
				}
			} else {
				// Username was not found (return 0)
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// SQLException (return -1)
			return -1;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public boolean deleteUser(String username)
	{
		try {
			String deleteUserCommand = "DELETE FROM User WHERE username=?";
			System.out.println(deleteUserCommand);
			PreparedStatement dps = connection.prepareStatement(deleteUserCommand);
			System.out.println(dps);
			dps.setString(1, username);
			dps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean clearDatabase() {
		try {
			String clearUserCommand = "DELETE FROM 'User'";
			String clearPortfolioCommand = "DELETE FROM 'Portfolio'";
			String clearViewedStocksCommand = "DELETE FROM 'ViewedStocks'";
			Statement clearDatabase = connection.createStatement();
			clearDatabase.executeUpdate(clearUserCommand);
			clearDatabase.executeUpdate(clearPortfolioCommand);
			clearDatabase.executeUpdate(clearViewedStocksCommand);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
