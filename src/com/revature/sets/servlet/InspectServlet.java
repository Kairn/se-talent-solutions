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
 * Servlet implementation class InspectServlet
 */
@WebServlet({ "/InspectServlet", "/inspect" })
public class InspectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InspectServlet() {
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
				int accessLevel = Integer.parseInt(session.getAttribute("accessLevel").toString());
				if (accessLevel == 2 || accessLevel == 3) {
					reqs = gs.fetchResolvedRequests(accessLevel);
					
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
