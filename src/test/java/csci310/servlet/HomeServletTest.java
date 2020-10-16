package csci310.servlet;


import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class HomeServletTest extends Mockito {

	HomeServlet servlet;
	
	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	@Mock
	HttpSession session;

	@Before
	public void setUp() throws Exception {
		servlet = new HomeServlet();
	}
	
	@Test
	public void testDoPost() throws IOException, ServletException {
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		//null user case
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("login")).thenReturn(null);
		StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new HomeServlet().doPost(request, response);	
		writer.flush();
		String login = stringWriter.toString().trim();
		assertEquals(login, "user not logged in");
		
		//logged out user case
		when(session.getAttribute("login")).thenReturn(false);
		stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
		new HomeServlet().doPost(request, response);	
		writer.flush();
		login = stringWriter.toString().trim();
		assertEquals(login, "user not logged in");

		//logged in and we want to log out
		when(session.getAttribute("login")).thenReturn(true);
		
		//testlogout "false"
		when(request.getParameter("logout")).thenReturn("false");
		new HomeServlet().doPost(request, response);	
		//nothing happens
		assertEquals(0, 0);
		
		//testlogout "null"
		when(request.getParameter("logout")).thenReturn(null);
		new HomeServlet().doPost(request, response);	
		//nothing happens
		assertEquals(0, 0);
		
		//testlogout "true"
		when(session.getAttribute("login")).thenReturn(true);
		when(request.getParameter("logout")).thenReturn("true");
		
		stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new HomeServlet().doPost(request, response);	
		writer.flush();
		String login2 = stringWriter.toString().trim();
		assertEquals(login2, "");
	}
}
