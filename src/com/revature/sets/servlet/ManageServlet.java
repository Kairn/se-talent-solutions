package com.revature.sets.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.sets.model.RestfulResponse;
import com.revature.sets.service.GetService;
import com.revature.sets.utility.UtilityManager;

/**
 * Servlet implementation class ManageServlet
 */
@WebServlet({ "/ManageServlet", "/manage" })
public class ManageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ManageServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		GetService gs = new GetService();
		RestfulResponse rres = new RestfulResponse();
		int status = 0;
		String emps = null;
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			try {
				String employeeId = session.getAttribute("employeeId").toString();
				String accessLevel = session.getAttribute("accessLevel").toString();
				if (Integer.parseInt(accessLevel) > 2) {
					emps = gs.fetchJuniorEmployeesAsExecutive();
					status = 1;
				}
				else if (Integer.parseInt(accessLevel) == 2) {
					emps = gs.fetchJuniorEmployeesAsManager(employeeId);
				}
				else {
					status = 401;
				}
			}
			catch (NumberFormatException ne) {
				status = 400;
			}
			catch (Exception e) {
				status = 440;
			}
		}
		else {
			status = 440;
		}
		
		if (emps != null) {
			if (emps.isEmpty()) {
				status = 404;
			}
			else {
				status += 200;
				rres.setContent(emps);
			}
		}
		else {
			status = 400;
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
		//
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//
	}

}