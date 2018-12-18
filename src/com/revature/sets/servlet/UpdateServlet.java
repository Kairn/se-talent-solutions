package com.revature.sets.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.sets.model.RestfulResponse;
import com.revature.sets.service.PostService;
import com.revature.sets.utility.UtilityManager;

/**
 * Servlet implementation class UpdateServlet
 */
@WebServlet({ "/UpdateServlet", "/update" })
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PostService ps = new PostService();
		RestfulResponse rres = new RestfulResponse();
		boolean success = false;
		
		HttpSession session = request.getSession(false);
		int employeeId = Integer.parseInt(session.getAttribute("employeeId").toString());
		String requestBody = UtilityManager.readRequest(request.getReader());
		if (requestBody != null) {
			success = ps.updateEmployeeInformation(employeeId, requestBody);
		}
		
		if (success) {
			rres.setStatus(200);
		}
		else {
			rres.setStatus(400);
		}
		
		response.setContentType("application/json");
		response.getWriter().write(UtilityManager.toJsonStringJackson(rres));
		
	}

}
