package com.revature.sets.service;

import java.util.List;

import com.revature.sets.dao.AssociateDao;
import com.revature.sets.dao.AssociateDaoImpl;
import com.revature.sets.dao.ManagerDao;
import com.revature.sets.dao.ManagerDaoImpl;
import com.revature.sets.model.Request;

public class AuthorizationService {

	public AuthorizationService() {
		super();
	}
	
	public boolean hasAccessToUploadToRequest(int employeeId, int requestId) {
		
		AssociateDao ad = new AssociateDaoImpl();
		
		Request request = ad.getPendingRequestByEmployeeAndRequestId(employeeId, requestId);
		
		if (request == null) {
			return false;
		}
		else {
			return true;
		}
		
	}
	
	public boolean isOwnRequest(int employeeId, int requestId) {

		AssociateDao ad = new AssociateDaoImpl();
		
		List<Request> requests = ad.getRequestsByEmployeeId(employeeId);
		requests.removeIf(r -> r.getRequestId() != requestId);
		
		if (requests.isEmpty()) {
			return false;
		}
		else {
			return true;
		}
		
	}
	
	public boolean hasAccessToViewRequest(int employeeId, int accessLevel, int requestId) {
		
		if (isOwnRequest(employeeId, requestId)) {
			return true;
		}
		
		ManagerDao md = new ManagerDaoImpl();
		
		if (accessLevel == 3) {
			return true;
		}
		else if (accessLevel == 2) {
			List<Request> requests = md.getPendingRequestsByManagerId(employeeId);
			requests.addAll(md.getResolvedRequestsAsManager());
			requests.removeIf(r -> r.getRequestId() != requestId);
			
			if (requests.isEmpty()) {
				return false;
			}
			else {
				return true;
			}
		}
		
		return false;
		
	}
	
	public boolean hasAccessToViewFile(int employeeId, int accessLevel, int fileId) {

		AssociateDao ad = new AssociateDaoImpl();
		
		int requestId = ad.getRequestIdByFileId(fileId);
		
		return hasAccessToViewRequest(employeeId, accessLevel, requestId);
		
	}

}
