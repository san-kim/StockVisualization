package csci310.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class TiingoAPI {
	
	class Pair {
		String date;
		double price;
		
		public Pair(String date, double price) {
			this.date = date;
			this.price = price;
		}
	}
	
	private String token = "dfa3aad6d8290db5ee85f160d54a60a7bbc03e1c";
	
	public double stockPrice(String ticker) throws IOException {
		
		URL url = new URL("https://api.tiingo.com/tiingo/daily/" + ticker + "/prices?token=" + token);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/json");
		InputStream is = con.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		String line;
		while ((line = rd.readLine()) != null) {
		   response.append(line);
		   response.append('\r');
		}
		rd.close();
		
		response.deleteCharAt(0);
		response.deleteCharAt(response.length() - 1);
		JSONObject myResponse = new JSONObject(response.toString());
		
		return myResponse.getDouble("close");
	}

	public double stockPriceFromDate(String ticker, String date) throws IOException {
		URL url = new URL("https://api.tiingo.com/tiingo/daily/" + ticker + "/prices?token=" + token);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/json");
		InputStream is = con.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		String line;
		while ((line = rd.readLine()) != null) {
		   response.append(line);
		   response.append('\r');
		}
		rd.close();
		
		response.deleteCharAt(0);
		response.deleteCharAt(response.length() - 1);
		JSONObject myResponse = new JSONObject(response.toString());
		
		return myResponse.getDouble("close");
	}
	
	public double stockPriceFromRange(String ticker, String startDate, String endDate) throws IOException {
		URL url = new URL("https://api.tiingo.com/tiingo/daily/" + ticker + "/prices?token=" + token + "&startDate=" + startDate + "&endDate=" + endDate);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/json");
		InputStream is = con.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		String line;
		while ((line = rd.readLine()) != null) {
		   response.append(line);
		   response.append('\r');
		   response.append('\n');
		}
		rd.close();
		
		//response.deleteCharAt(0);
		//response.deleteCharAt(response.length() - 1);
		//System.out.println(response);
		JSONArray myResponse = new JSONArray(response.toString());
		Pair arr[] = new Pair[myResponse.length()];
		for (int i = 0; i < myResponse.length(); i++) {
			String date = myResponse.getJSONObject(i).getString("date");
			String[] parts = date.split("T");
			Pair n = new Pair(parts[0], myResponse.getJSONObject(i).getDouble("close"));
			arr[i] = n;
		}
		
		for (int i = 0; i < myResponse.length(); i++) {
			System.out.println("Date: " + arr[i].date + " and price: " + arr[i].price);
		}
		
		//System.out.println(myResponse.getJSONObject(0));
		//return myResponse.getDouble("close");
		return 50;
	}
	
	public double stockPriceFromRangeWithFrequency(String ticker, String startDate, String endDate, char frequency) throws IOException {
		return 0;
	}

}
