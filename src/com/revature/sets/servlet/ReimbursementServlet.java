package com.revature.sets.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.sets.model.RestfulResponse;
import com.revature.sets.service.DeleteService;
import com.revature.sets.service.GetService;
import com.revature.sets.service.PostService;
import com.revature.sets.utility.UtilityManager;

/**
 * Servlet implementation class ReimbursementServlet
 */
@WebServlet({ "/ReimbursementServlet", "/reimbursement" })
public class ReimbursementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReimbursementServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		GetService gs = new GetService();
		RestfulResponse rres = new RestfulResponse();
		int status = 0;
		String reqs = null;
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			try {
				String employeeId = session.getAttribute("employeeId").toString();
				reqs = gs.fetchRequestsAsEmployee(employeeId);

				if (reqs != null) {
					if (reqs.isEmpty()) {
						status = 404;
					}
					else {
						status = 200;
						rres.setContent(reqs);
					}
				}
				else {
					status = 400;
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PostService ps = new PostService();
		RestfulResponse rres = new RestfulResponse();
		int status = 0;
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			try {
				int employeeId = Integer.parseInt(session.getAttribute("employeeId").toString());
				String requestBody = UtilityManager.readRequest(request.getReader());
				if (requestBody != null) {
					if (ps.submitNewRequest(employeeId, requestBody)) {
						status = 200;
					}
					else {
						status = 404;
					}
				}
				else {
					status = 400;
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
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		DeleteService ds = new DeleteService();
		RestfulResponse rres = new RestfulResponse();
		int status = 0;
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			try {
				int employeeId = Integer.parseInt(session.getAttribute("employeeId").toString());
				String requestBody = UtilityManager.readRequest(request.getReader());
				if (requestBody != null) {
					if (ds.recallReimbursementRequest(employeeId, requestBody)) {
						status = 200;
					}
					else {
						status = 401;
					}
				}
				else {
					status = 400;
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

}
