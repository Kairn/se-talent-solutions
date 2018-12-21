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
import com.revature.sets.service.PutService;
import com.revature.sets.utility.UtilityManager;

/**
 * Servlet implementation class ResolveServlet
 */
@WebServlet({ "/ResolveServlet", "/resolve" })
public class ResolveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResolveServlet() {
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
				int employeeId = Integer.parseInt(session.getAttribute("employeeId").toString());
				int accessLevel = Integer.parseInt(session.getAttribute("accessLevel").toString());
				if (accessLevel > 1) {
					reqs = gs.fetchPendingRequestsToBeResolved(employeeId, accessLevel);
				}
				else {
					status = 401;
				}
			}
			catch (RuntimeException e) {
				status = 440;
			}
		}
		else {
			status = 440;
		}
		
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

		rres.setStatus(status);
		response.setContentType("application/json");
		response.getWriter().write(UtilityManager.toJsonStringJackson(rres));
		
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PutService ps = new PutService();
		RestfulResponse rres = new RestfulResponse();
		int status = 0;
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			try {
				int employeeId = Integer.parseInt(session.getAttribute("employeeId").toString());
				int accessLevel = Integer.parseInt(session.getAttribute("accessLevel").toString());
				String requestBody = UtilityManager.readRequest(request.getReader());
				if (accessLevel > 1 && requestBody != null) {
					if (ps.resolvePendingRequest(employeeId, accessLevel, requestBody)) {
						status = 200;
					}
					else {
						status = 400;
					}
				}
				else {
					status = 401;
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
