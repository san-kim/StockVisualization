package csci310.servlet;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.mockito.Mockito;

public class SignUpServletTest extends Mockito {
	
	// Successful sign up.
	@Test
	public void testDoPost() throws ServletException, IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getSession()).thenReturn(mock(HttpSession.class));
		BufferedReader reader = new BufferedReader(new StringReader("{\"username\":\"test2\",\"password\":\"test2test3\"}"));
		when(request.getReader()).thenReturn(reader);
		
		StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
		
        SignUpServlet ss = new SignUpServlet();
		ss.doPost(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().contains("true"));
	}
	
	// Unsuccessful sign up because username already exists.
	@Test
	public void testDoPost1() throws ServletException, IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getSession()).thenReturn(mock(HttpSession.class));
		BufferedReader reader = new BufferedReader(new StringReader("{\"username\":\"test2test\",\"password\":\"test2test3\"}"));
		when(request.getReader()).thenReturn(reader);
		
		StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
		
        SignUpServlet ss = new SignUpServlet();
		ss.doPost(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().contains("false"));
	}
	
	// IOException thrown.
	@Test
	public void testDoPost2() throws ServletException, IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getSession()).thenReturn(mock(HttpSession.class));
		BufferedReader reader = new BufferedReader(new StringReader("{\"username\":\"test2test\",\"password\":\"test2test3\"}"));
		when(request.getReader()).thenReturn(reader);
		
		StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenThrow(new IOException());
		
        SignUpServlet ss = new SignUpServlet();
		ss.doPost(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().isEmpty());
	}
	
	// Exception thrown.
	@Test
	public void testDoPost3() throws ServletException, IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getSession()).thenReturn(mock(HttpSession.class));
		when(request.getReader()).thenThrow(new IOException());
		
		StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
		
        SignUpServlet ss = new SignUpServlet();
		ss.doPost(request, response);
		
		writer.flush();
		assertTrue(stringWriter.toString().isEmpty());
	}
}
