package csci310.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class TiingoAPI {
	
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

}
