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

import csci310.service.DatabaseClient;
import csci310.service.PasswordAuthentication;

public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO(JamieFlores): Implement sign up.
	}
}
