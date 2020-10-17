package csci310.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class HomeServlet
 */

@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
		//add session for how to tell what user it is now
		HttpSession session = request.getSession();  
		response.setContentType("text/html; charset=UTF-8");
		
		//guard against null user
		if(session.getAttribute("login") == null)
		{
			PrintWriter out = response.getWriter();
			out.println("user not logged in");
			return;
		}

		//guard against logged out user
		if(session.getAttribute("login").toString().trim().equalsIgnoreCase("false"))
		{
			PrintWriter out = response.getWriter();
			out.println("user not logged in");
			return;
		}
	
		//when we logout, clear the session currentuser
		String logout = request.getParameter("logout");
		if(logout != null)
		{
			if(logout.contentEquals("true"))
			{
				session.setAttribute("login", false);
				PrintWriter out = response.getWriter();
				out.println("");
				return;
			}
		}

	}

}
