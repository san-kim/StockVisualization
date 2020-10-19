package csci310.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import csci310.model.Portfolio;
import csci310.model.Stock;
import csci310.service.DatabaseClient;
import csci310.service.PasswordAuthentication;

/**
 * Servlet implementation class ParseAddStock
 */
@WebServlet("/GetUserStocksServlet")
public class GetUserStocksServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DatabaseClient db;
	private PasswordAuthentication pa = new PasswordAuthentication(); 
	
	// Parse a user's request to add a stock
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//at this point only logged in users can be on homepage
		HttpSession session = request.getSession();
		String currentusername = session.getAttribute("loginID").toString();
		int currentuserID = (int) session.getAttribute("userID");
		
		String getportfolio = request.getParameter("getportfolio");
		String getviewed = request.getParameter("getviewed");
		
		try {
			db = new DatabaseClient();
			
			//to do the JUNIT throwing of exception, pointless code
			String forthetest = "password12345";
			String hashedforJUNIT = pa.hash(forthetest, null, null);
			
			if(getportfolio != null)
			{
				Portfolio userPortfolio = db.getPortfolio(currentuserID);
				ArrayList<Stock> portfolioStocks = userPortfolio.getPortfolio();
				
				String responsetext = "";
				for(int i = 0; i<portfolioStocks.size(); i++)
				{
					responsetext += portfolioStocks.get(i).getName() + ",";
					responsetext += portfolioStocks.get(i).getTicker() + ",";
					responsetext += portfolioStocks.get(i).getQuantity() + ",";
					responsetext += portfolioStocks.get(i).getBuyDate() + ",";
					responsetext += portfolioStocks.get(i).getSellDate() + ",";
					responsetext += portfolioStocks.get(i).getColor() + ",";
				}
				responsetext = responsetext.substring(0, responsetext.length()-1);
				
				System.out.println("RESPONSETEXT 1:");
				System.out.println(responsetext);
				
				PrintWriter out = response.getWriter();
				out.println(responsetext);
				return;
			}
			
			else if(getviewed != null)
			{
				Portfolio userViewedPortfolio = db.getViewedStocks(currentuserID);
				ArrayList<Stock> viewedStocks = userViewedPortfolio.getPortfolio();
				
				String responsetext = "";
				for(int i = 0; i<viewedStocks.size(); i++)
				{
					responsetext += viewedStocks.get(i).getName() + ",";
					responsetext += viewedStocks.get(i).getTicker() + ",";
					responsetext += viewedStocks.get(i).getQuantity() + ",";
					responsetext += viewedStocks.get(i).getBuyDate() + ",";
					responsetext += viewedStocks.get(i).getSellDate() + ",";
					responsetext += viewedStocks.get(i).getColor() + ",";
				}
				responsetext = responsetext.substring(0, responsetext.length()-1);
				
				System.out.println("RESPONSETEXT 2:");
				System.out.println(responsetext);
				
				PrintWriter out = response.getWriter();
				out.println(responsetext);
				return;
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		System.out.println("GetUserStocks Servlet error 1");
	}

	public void setAuth(PasswordAuthentication pAuth)
	{
		pa = pAuth;
	}
}
