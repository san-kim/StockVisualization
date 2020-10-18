package csci310.servlet;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;

public class AddServletTest {

	@Test
	public void testDoPost() throws ServletException, IOException {
		AddServlet l = new AddServlet();
		HttpServletRequest request = null;
		//request.setAttribute("password", "hi");
		HttpServletResponse response = null;
		l.doPost(request, response);
		HttpSession session = request.getSession();
		assertEquals(session.getAttribute("added"), true);
	}

}
