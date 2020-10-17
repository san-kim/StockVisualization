package csci310.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import csci310.service.DatabaseClient;
import csci310.service.PasswordAuthentication;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//HttpSession session = request.getSession();  
		response.setContentType("text/html; charset=UTF-8");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String confirmpassword = request.getParameter("confirmpassword");
		
		
		if(username == null)
		{
			PrintWriter out = response.getWriter();
			out.println("Please enter a username.");
			return;
		}
		if(password == null)
		{
			PrintWriter out = response.getWriter();
			out.println("Please enter a password.");
			return;
		}
		if(confirmpassword == null)
		{
			PrintWriter out = response.getWriter();
			out.println("Please enter a password.");
			return;
		}
		
		if(username.length() == 0)
		{
			PrintWriter out = response.getWriter();
			out.println("Please enter a username.");
			return;
		}
		
		if(password.length() == 0)
		{
			PrintWriter out = response.getWriter();
			out.println("Please enter a password.");
			return;
		}
		
		if(password.length() < 8)
		{
			PrintWriter out = response.getWriter();
			out.println("Password must be longer than 8 characters.");
			return;
		}
		
		if(password.contentEquals(confirmpassword) == false)
		{
			PrintWriter out = response.getWriter();
			out.println("Passwords do not match.");
			return;
		}
		
		
		//at this point we have valid and matching passwords
		try {
			DatabaseClient db = new DatabaseClient();
			PasswordAuthentication passAuth = new PasswordAuthentication();
			String hashedPass = passAuth.hash(password, null, null);
			//getuser of 0 means that the username doesn't exist 
			if(db.getUser(passAuth, username, password) == 0)
			{
				//TODO: do createUser here
				db.createUser(username, hashedPass);
				
				PrintWriter out = response.getWriter();
				out.println("success");
				return;
			}
			
			else
			{
				PrintWriter out = response.getWriter();
				out.println("Username already taken!");
				return;
			}
		} catch (SQLException e) {
			//do nothing
		} catch (NoSuchAlgorithmException e) {
			//do nothing
		}
	}
}
