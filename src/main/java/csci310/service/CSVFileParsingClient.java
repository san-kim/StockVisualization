package csci310.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Scanner;

import csci310.model.Stock;

public class CSVFileParsingClient {

	private DatabaseClient db;
	private CurrentSignedInUser instance;

	public CSVFileParsingClient() throws SQLException {
		db = new DatabaseClient();
		instance = CurrentSignedInUser.getInstance();
	}

	/**
	 * This function will read from the given file (provided it is found) and return
	 * either negative error code if the CSV file is malformed in anyway, or a zero
	 * to indicate the file was successfully read & added to the current signed in
	 * user's Portfolio
	 * 
	 * @param fileName
	 * @return int code representing specific error or success
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public int parseCSVFile(String filename) throws FileNotFoundException, IOException {
		String[] rawData = readFile(filename);
		String header = rawData[0];

		// Verify file data is as expected
		if (header.equalsIgnoreCase("Name,Ticker,Quantity,DatePurchased,DateSold")) {
			for (int i = 1; i < rawData.length; i++) {
				String data = rawData[i];
				Stock stock = parseStock(data);
				if (stock == null) {
					System.err.println("One or more stocks in improperly formatted.");
					return -2;
				}
				db.addStockToPortfolio(instance.getCurrentUser().getUserID(), stock);
			}
		} else {
			System.err.println("File header : " + header + "\nis not what was expected. Exiting program.");
			return -1;
		}
		return 0;
	}

	/**
	 * This helper function will read from the given file (provided it is found) and
	 * return an array of Strings, with each spot in the array holding one line from
	 * the file
	 * 
	 * @param fileName
	 * @return array of Strings, each String is one line from the file.
	 */
	public String[] readFile(String fileName) throws FileNotFoundException, IOException {
		String[] array = null;
		int num = 0;
		FileInputStream fis = new FileInputStream(fileName);
		Scanner scan = new Scanner(fis);
		// Get the number of lines in the file in order to initialize the array.
		long lineCount = Files.lines(Paths.get(fileName)).count();

		array = new String[(int) lineCount]; // typecast will work as long as file isn't too big

		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			array[num] = line;
			num++;
		}
		scan.close();
		return array;
	}

	/**
	 * This helper function will read from the given line of raw data and return a
	 * Stock object constructed from the data, or null if the stock is improperly
	 * formatted
	 * 
	 * @param data
	 * @return Stock object
	 */
	public Stock parseStock(String data) {
		Stock stock = null;
		Scanner sc = new Scanner(data);
		sc.useDelimiter(",");

		String name = sc.next();
		String ticker = sc.next();

		if (!sc.hasNextInt()) {
			sc.close();
			return null;
		}
		int quantity = sc.nextInt();

		if (!sc.hasNextInt()) {
			sc.close();
			return null;
		}
		int datePurchased = sc.nextInt();

		if (!sc.hasNextInt()) {
			sc.close();
			return null;
		}

		int dateSold = sc.nextInt();
		stock = new Stock(name, ticker, quantity, datePurchased, dateSold);
		sc.close();
		return stock;
	}
}
