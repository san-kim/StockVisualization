package csci310.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import csci310.service.DatabaseClient;
import csci310.service.PasswordAuthentication;

public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			DatabaseClient db = new DatabaseClient();
			PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
			
			// Takes POST parameters and parses them into JSON String.
			String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			HttpSession session = request.getSession();
			
			// Parse JSON string into a usable JSON object.
			JSONObject jo = new JSONObject(requestBody);
			String username = jo.getString("username");
			String hashedPassword = passwordAuthentication.hash(jo.getString("password"), null, null);
			//Passes password and username into the database with the password authentication class to be hashed
			boolean result = db.createUser(username, hashedPassword);
			System.out.println(result);
			
			// New user created successfully
			if(result) {
				session.setAttribute("signup", true);
			} else {
				// Sign up unsuccessful because username already exists in the database.
				session.setAttribute("signup", false);
			}
			try {
				PrintWriter pw = response.getWriter();
				pw.write("" + result);
				pw.flush();
			} catch (IOException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				System.out.println("Error");
				return;
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			System.out.println("Error2");
		}
	}
}
