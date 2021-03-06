package com.revature.sets.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.revature.sets.model.RestfulResponse;
import com.revature.sets.service.GetService;
import com.revature.sets.service.PostService;
import com.revature.sets.utility.UtilityManager;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet({ "/LoginServlet", "/login" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		GetService gs = new GetService();
		RestfulResponse rres = new RestfulResponse();
		int status = 0;
		String emp = null;
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			try {
				String employeeId = session.getAttribute("employeeId").toString();
				if (employeeId != null) {
					emp = gs.fetchEmployeeJsonWithSession(employeeId);
					
					if (emp != null) {
						status = 200;
						rres.setContent(emp);
					}
					else {
						status = 404;
					}
				}
				else {
					status = 440;
				}
			}
			catch (RuntimeException e) {
				status = 440;
			}
		}
		else {
			status = 440;
		}
		
		rres.setStatus(status);
		response.setContentType("application/json");
		response.getWriter().write(UtilityManager.toJsonStringJackson(rres));
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PostService ps = new PostService();
		RestfulResponse rres = new RestfulResponse();
		int status = 0;
		String emp = null;

		String requestBody = UtilityManager.readRequest(request.getReader());
		if (requestBody != null) {
			emp = ps.fetchEmployeeJsonWithCredentials(requestBody);

			if (emp != null) {
				status = 200;
				rres.setContent(emp);
				JSONObject empJson = new JSONObject(emp);
				HttpSession session = request.getSession(true);
				session.setAttribute("employeeId", empJson.get("employeeId"));
				session.setAttribute("upGroup", empJson.getInt("upGroup"));
				session.setAttribute("downGroup", empJson.getInt("downGroup"));
				session.setAttribute("accessLevel", empJson.getInt("accessLevel"));
				session.setMaxInactiveInterval(600);
			}
			else {
				status = 401;
			}
		}
		else {
			status = 400;
		}

		rres.setStatus(status);
		response.setContentType("application/json");
		response.getWriter().write(UtilityManager.toJsonStringJackson(rres));

	}

}
