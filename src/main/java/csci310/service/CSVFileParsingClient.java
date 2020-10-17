package csci310.service;

import csci310.model.Stock;

public class CSVFileParsingClient {

	public CSVFileParsingClient() {
		
	}
	
	/**
	 * This function will read from the given file (provided it is found) and return either negative error
	 * code if the CSV file is malformed in anyway, or a zero to indicate the file was successfully
	 * read & added to the current signed in user's Portfolio
	 * @param fileName
	 * @return int code representing specific error or success
	 */
	public int parseCSVFile(String filename) {
		return 0;
	}
	
	/**
	 * This helper function will read from the given file (provided it is found) and return an 
	 * array of Strings, with each spot in the array holding one line from the file
	 * @param fileName
	 * @return array of Strings, each String is one line from the file.
	 */
	public String[] readFile(String fileName) {
		String[] array = null;
		return array;
	}
	
	public Stock parseStock(String data) {
		return null;
	}
	
}
