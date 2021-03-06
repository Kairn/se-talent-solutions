package com.revature.sets.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.revature.sets.model.RestfulResponse;
import com.revature.sets.service.AuthorizationService;
import com.revature.sets.service.GetService;
import com.revature.sets.service.PostService;
import com.revature.sets.utility.UtilityManager;

/**
 * Servlet implementation class FileServlet
 */
@WebServlet({ "/FileServlet", "/file", "/image/*" })
public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		AuthorizationService as = new AuthorizationService();
		GetService gs = new GetService();
		RestfulResponse rres = new RestfulResponse();
		int status = 0;
		String files = null;
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			try {
				int employeeId = Integer.parseInt(session.getAttribute("employeeId").toString());
				int accessLevel = Integer.parseInt(session.getAttribute("accessLevel").toString());
				String requestString = request.getPathInfo().substring(1);
				
				if (requestString != null && !requestString.isEmpty()) {
					if (requestString.charAt(0) == 'r') {
						int id = Integer.parseInt(requestString.substring(1));
						if (as.hasAccessToViewRequest(employeeId, accessLevel, id)) {
							files = gs.fetchFilesAttachedToRequest(id);
							
							if (files != null && !files.isEmpty()) {
								status = 200;
								rres.setContent(files);
							}
							else {
								status = 404;
							}
						}
						else {
							status = 401;
						}
					}
					else if (requestString.charAt(0) == 'i') {
						try {
							String[] params = requestString.split("_");
							String imageName = "image." + params[1];
							int fileId = Integer.parseInt(params[2]);
							
							if (as.hasAccessToViewFile(employeeId, accessLevel, fileId)) {
								byte[] imageData = gs.fetchFileData(fileId);
								if (imageData != null) {
									response.setContentType(getServletContext().getMimeType(imageName));
									response.setContentLength(imageData.length);
									response.getOutputStream().write(imageData);
									return;
								}
								else {
									status = 404;
								}
							}
							else {
								status = 401;
							}
						}
						catch (RuntimeException e) {
							e.printStackTrace();
							status = 400;
						}
					}
					else {
						status = 400;
					}
				}
				else {
					status = 400;
				}
			}
			catch (RuntimeException e) {
				e.printStackTrace();
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
		
		AuthorizationService as = new AuthorizationService();
		PostService ps = new PostService();
		RestfulResponse rres = new RestfulResponse();
		int status = 0;
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			try {
				int employeeId = Integer.parseInt(session.getAttribute("employeeId").toString());
				
				DiskFileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				try {
					List<FileItem> items = upload.parseRequest(request);
					if (items != null && !items.isEmpty()) {
						Iterator<FileItem> iter = items.iterator();
						Integer requestId = null;
						String imageType = null;
						InputStream image = null;
						
						while (iter.hasNext()) {
							FileItem fi = iter.next();
							if (fi.isFormField() && fi.getFieldName().equals("requestId")) {
								requestId = Integer.parseInt(fi.getString());
							}
							else {
								if (fi.getFieldName().equals("file")) {
									imageType = fi.getName().split("\\.")[1].toUpperCase();
									image = fi.getInputStream();
								}
							}
						}
						
						if (requestId == null || imageType == null || imageType.isEmpty() || image == null) {
							status = 400;
						}
						else {
							if (as.hasAccessToUploadToRequest(employeeId, requestId)) {
								if (ps.uploadImageFileToRequest(requestId, imageType, image)) {
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
					}
					else {
						status = 400;
					}
				} catch (FileUploadException e) {
					status = 400;
				}
			}
			catch (RuntimeException e) {
				status = 440;
				e.printStackTrace();
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
