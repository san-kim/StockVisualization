package csci310.servlet;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import org.mockito.Mock;
import org.mockito.Mockito;

import csci310.model.Stock;
import csci310.service.DatabaseClient;
import csci310.service.PasswordAuthentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GetUserStocksServletTest extends Mockito{
	
	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	@Mock
	HttpSession session;
	@Mock
	DatabaseClient db;
	@Mock
	PasswordAuthentication passAuth;
	@Mock
	GetUserStocksServlet getUserStocksServlet;

	
	private int userID;
	
	@Before
	public void setUp() throws Exception 
	{
		DatabaseClient db = new DatabaseClient();
		db.deleteUser("test2");
		PasswordAuthentication pa = new PasswordAuthentication(); 
		String hashedPass = pa.hash("test2test", null, null);
		db.createUser("test2", hashedPass);
		userID = db.getUser(pa, "test2", "test2test");
		db.deleteUserPortfolio(userID);
		db.deleteUserViewed(userID);

		Stock stock1 = new Stock("Apple","APPL",20, 100000, 2000000);
		Stock stock2 = new Stock("Google","GOOGL",30, 200000, 3000000);
		db.addStockToPortfolio(userID, stock1);
		db.addStockToViewed(userID, stock2);
	}

	@Test
	public void testDoPost() throws ServletException, IOException, SQLException, NoSuchAlgorithmException {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		db = mock(DatabaseClient.class);
		passAuth = mock(PasswordAuthentication.class);
		
		StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        
		//null request
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("loginID")).thenReturn("test2");
		when(session.getAttribute("userID")).thenReturn(userID);
		when(request.getParameter("getportfolio")).thenReturn(null);
		when(request.getParameter("getviewed")).thenReturn(null);
        new GetUserStocksServlet().doPost(request, response);	
		assertTrue(true);
				
		//null getviewed but true getportfolio
		when(request.getParameter("getportfolio")).thenReturn("true");
		when(request.getParameter("getviewed")).thenReturn(null);
		stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new GetUserStocksServlet().doPost(request, response);	
		writer.flush();
		String getstocks = stringWriter.toString().trim();
		getstocks = getstocks.substring(0, getstocks.length()-8);
		assertEquals("Apple,APPL,20,100000,2000000", getstocks);
		
		//null getviewed but true getportfolio
		when(request.getParameter("getportfolio")).thenReturn(null);
		when(request.getParameter("getviewed")).thenReturn("true");
		stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new GetUserStocksServlet().doPost(request, response);	
		writer.flush();
		getstocks = stringWriter.toString().trim();
		getstocks = getstocks.substring(0, getstocks.length()-8);
		assertEquals("Google,GOOGL,30,200000,3000000", getstocks);		
		
		//exception case
		getUserStocksServlet = new GetUserStocksServlet();
		//second exception
		stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        getUserStocksServlet.setAuth(passAuth);
		when(passAuth.hash("password12345", null, null)).thenThrow(new NoSuchAlgorithmException());
		getUserStocksServlet.doPost(request, response);	
		assertEquals(0,0);
	}
	
	@After
	public void cleanup() throws SQLException
	{
		DatabaseClient db = new DatabaseClient();
		db.deleteUserPortfolio(userID);
		db.deleteUserViewed(userID);
		db.deleteUser("test2");
	}
}