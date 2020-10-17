package csci310.servlet;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;

import csci310.service.DatabaseClient;
import csci310.service.PasswordAuthentication;

public class LoginServletTest extends Mockito {
	
	//Correct username and password
	@Test
	public void testDoPost() throws ServletException, IOException, SQLException, NoSuchAlgorithmException {
		DatabaseClient db = new DatabaseClient();
		//in case it was already here
		db.deleteUser("test2");
		PasswordAuthentication pa = new PasswordAuthentication(); 
		String hashedPass = pa.hash("test2test", null, null);
		db.createUser("test2", hashedPass);
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getSession()).thenReturn(mock(HttpSession.class));
		BufferedReader reader = new BufferedReader(new StringReader("{\"username\":\"test2\",\"password\":\"test2test\"}"));
		when(request.getReader()).thenReturn(reader);

		StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
		
        LoginServlet ls = new LoginServlet();
		ls.doPost(request, response);
		
		writer.flush();
		
		int s = Integer.parseInt(stringWriter.toString());
		assertTrue(s >= 1);
	}
	
	//Correct username, incorrect password
	@Test
	public void testDoPost1() throws ServletException, IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getSession()).thenReturn(mock(HttpSession.class));
		BufferedReader reader = new BufferedReader(new StringReader("{\"username\":\"test2\",\"password\":\"test2test3\"}"));
		when(request.getReader()).thenReturn(reader);
		
		StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
		
        LoginServlet ls = new LoginServlet();
		ls.doPost(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().contains("-2"));
	}
	
	//Incorrect username
	@Test
	public void testDoPost2() throws ServletException, IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getSession()).thenReturn(mock(HttpSession.class));
		BufferedReader reader = new BufferedReader(new StringReader("{\"username\":\"test54\",\"password\":\"test2test3\"}"));
		when(request.getReader()).thenReturn(reader);
		
		StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
		
        LoginServlet ls = new LoginServlet();
		ls.doPost(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().contains("0"));
	}
	
	//Check for IOExceptions
	@Test
	public void testDoPost3() throws ServletException, IOException {
		try {
			HttpServletRequest request = mock(HttpServletRequest.class);
			HttpServletResponse response = mock(HttpServletResponse.class);
			when(request.getSession()).thenReturn(mock(HttpSession.class));
			BufferedReader reader = new BufferedReader(new StringReader("{\"username\":\"test54\",\"password\":\"test2test3\"}"));
			when(request.getReader()).thenReturn(reader);
			when(response.getWriter()).thenThrow(new IOException());

			
	        LoginServlet ls = new LoginServlet();
			ls.doPost(request, response);
			
		}catch(IOException e){
			assertNotNull(e);
		}

	}
	
	//Check for other general Exceptions
	@Test
	public void testDoPost4() throws ServletException, IOException, SQLException {
		try {
			HttpServletRequest request = mock(HttpServletRequest.class);
			HttpServletResponse response = mock(HttpServletResponse.class);
			when(request.getSession()).thenReturn(mock(HttpSession.class));
			BufferedReader reader = new BufferedReader(new StringReader("{\"username\":\"test54\",\"password\":\"test2test3\"}"));
			when(request.getReader()).thenThrow(new IOException());
			
	        LoginServlet ls = new LoginServlet();
			ls.doPost(request, response);
			
		}catch(Exception e){
			assertNotNull(e);
		}

		DatabaseClient db = new DatabaseClient();
		db.deleteUser("test2");
	}
}
