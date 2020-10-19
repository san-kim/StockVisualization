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

import csci310.model.User;
import csci310.service.CurrentSignedInUser;
import csci310.service.DatabaseClient;
import csci310.service.PasswordAuthentication;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			DatabaseClient db = new DatabaseClient();
			PasswordAuthentication passAuth = new PasswordAuthentication();
			int result = 0;
			
			//Takes POST parameters and parses them into JSON String
			String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			HttpSession session = request.getSession();
			//Parse JSON string into a useable JSON object
			//JSON object can be used to extract username and password
			JSONObject jo = new JSONObject(requestBody);
			String uname = jo.getString("username");
			String password =jo.getString("password");
			//Passes password and username into the database with the password authentication class to be hashed
			//returned result is the userID as it's stored in the database
			result = db.getUser(passAuth, uname, password);
			System.out.println(result);
			
			User currentUser = null;
			
			//Username is incorrect
			if(result == 0) {
				session.setAttribute("login", false);
			}
			//Valid username and password
			else if(result >= 1) {
				session.setAttribute("login", true);
				session.setAttribute("loginID", uname);
				session.setAttribute("userID", result);
				currentUser = new User(uname, result);
				CurrentSignedInUser.getInstance().signInCurrentUser(currentUser);
			}
			//Password is incorrect for user
			else{
				session.setAttribute("login", false);
			}
			// Will only be non null if the user has successfully logged in 
			session.setAttribute("currentUser", currentUser);
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
