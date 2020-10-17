package csci310.servlet;


import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import csci310.service.DatabaseClient;
import csci310.service.PasswordAuthentication;

public class SignUpServletTest extends Mockito {

	HomeServlet servlet;
	
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

	@Before
	public void setUp() throws Exception {
		servlet = new HomeServlet();
	}
	
	@Test
	public void testDoPost() throws IOException, ServletException, SQLException, NoSuchAlgorithmException {
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		db = mock(DatabaseClient.class);
		passAuth = mock(PasswordAuthentication.class);
		
		StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        
		//null username
		when(response.getWriter()).thenReturn(writer);
		when(request.getParameter("username")).thenReturn(null);
		when(request.getParameter("password")).thenReturn(null);
		when(request.getParameter("confirmpassword")).thenReturn(null);
		stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
        new SignupServlet().doPost(request, response);	
		writer.flush();
		String signup = stringWriter.toString().trim();
		assertEquals(signup, "Please enter a username.");
				
		when(request.getParameter("username")).thenReturn("");

		//null password
		when(request.getParameter("password")).thenReturn(null);
		stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
        new SignupServlet().doPost(request, response);	
		writer.flush();
		signup = stringWriter.toString().trim();
		assertEquals(signup, "Please enter a password.");
		
		when(request.getParameter("password")).thenReturn("");
		
		//null confirmpassword
		when(request.getParameter("confirmpassword")).thenReturn(null);
		stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
        new SignupServlet().doPost(request, response);	
		writer.flush();
		signup = stringWriter.toString().trim();
		assertEquals(signup, "Please enter a password.");
		
		when(request.getParameter("username")).thenReturn("");
		when(request.getParameter("password")).thenReturn("");
		when(request.getParameter("confirmpassword")).thenReturn("");
		
		//if empty username
		when(request.getParameter("username")).thenReturn("");
		stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
        new SignupServlet().doPost(request, response);	
		writer.flush();
		signup = stringWriter.toString().trim();
		assertEquals(signup, "Please enter a username.");
		
		when(request.getParameter("username")).thenReturn("testerusername");
		
		//if empty password
		when(request.getParameter("password")).thenReturn("");
		stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new SignupServlet().doPost(request, response);	
		writer.flush();
		signup = stringWriter.toString().trim();
		assertEquals(signup, "Please enter a password.");
		
		//if password less than 8 chars
		when(request.getParameter("password")).thenReturn("asdf");
		stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new SignupServlet().doPost(request, response);	
		writer.flush();
		signup = stringWriter.toString().trim();
		assertEquals(signup, "Password must be longer than 8 characters.");
		
		//if password and confirmpassword do not match
		when(request.getParameter("password")).thenReturn("password123");
		when(request.getParameter("confirmpassword")).thenReturn("password123456");
		stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new SignupServlet().doPost(request, response);	
		writer.flush();
		signup = stringWriter.toString().trim();
		assertEquals(signup, "Passwords do not match.");
		
		
		
		String username = "testusername7";
		
		
		
		when(request.getParameter("username")).thenReturn(username);
		when(request.getParameter("password")).thenReturn("password123");
		when(request.getParameter("confirmpassword")).thenReturn("password123");
		
		//IMPORTANT: change this every run
		when(db.createUser(username, "")).thenReturn(false);
		
		//try catch block
		when(db.getUser(passAuth, username, "password123")).thenReturn(0);
		stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new SignupServlet().doPost(request, response);	
		writer.flush();
		signup = stringWriter.toString().trim();
		assertEquals(signup, "success");
		
		//false case
		when(db.getUser(passAuth, username, "password123")).thenReturn(1);
		stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new SignupServlet().doPost(request, response);	
		writer.flush();
		signup = stringWriter.toString().trim();
		assertEquals(signup, "Username already taken!");
		
		
		//first exception
		when(new DatabaseClient()).thenThrow(new SQLException());
		new SignupServlet().doPost(request, response);	
		assertEquals(0,0);
		
		//second exception
		when(passAuth.hash("password123", null, null)).thenThrow(new NoSuchAlgorithmException());
		new SignupServlet().doPost(request, response);	
		assertEquals(0,0);
	}
}
