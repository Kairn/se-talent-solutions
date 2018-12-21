package com.revature.sets.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.revature.sets.dao.AssociateDaoImpl;
import com.revature.sets.dao.ExecutiveDao;
import com.revature.sets.dao.ExecutiveDaoImpl;
import com.revature.sets.dao.ManagerDao;
import com.revature.sets.dao.ManagerDaoImpl;
import com.revature.sets.model.Request;
import com.revature.sets.utility.UtilityManager;

public class PutService {

	public PutService() {
		super();
	}
	
	public boolean changeEmployeeRole(String jsonString) {
		
		ExecutiveDao ed = new ExecutiveDaoImpl();
		
		try {
			JSONObject jo = new JSONObject(jsonString);
			int employeeId = jo.getInt("employeeId");
			int upGroup = jo.getInt("upGroup");
			int downGroup = jo.getInt("downGroup");
			int accessLevel = jo.getInt("accessLevel");
			
			if (upGroup < 100 || upGroup > 999) {
				upGroup = -1;
			}
			if (downGroup < 100 || downGroup > 999) {
				downGroup = -1;
			}
			
			if (accessLevel < 1 || accessLevel > 2) {
				return false;
			}
			else {
				if (ed.changeEmployeeRole(employeeId, upGroup, downGroup, accessLevel) == 0) {
					return false;
				}
				else {
					return true;
				}
			}
		}
		catch (JSONException e) {
			return false;
		}
		
	}
	
	public boolean confirmResolution(int employeeId, int requestId, boolean action) {
		
		ManagerDao md = new ManagerDaoImpl();
		boolean success = false;
		
		String subject = "Your Reimbursement Request Has Been Resolved";
		String content = null;
		
		if (action) {
			success = md.approveRequest(requestId, employeeId) != 0;
			content = new String("Reimbursement with ID: " + requestId + " was approved.\n");
		}
		else {
			success = md.denyRequest(requestId, employeeId) != 0;
			content = new String("Reimbursement with ID: " + requestId + " was denied.\n");
		}
		
		if (success) {
			String email = md.getEmployeeEmailByRequestId(requestId);
			String managerName = (new AssociateDaoImpl()).getFullNameByEmployeeId(employeeId);
			content = content.concat("\nSincerely,\n" + managerName);
			UtilityManager.sendEmail(email, subject, content);
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public boolean resolvePendingRequest(int employeeId, int accessLevel, String jsonString) {
		
		ManagerDao md = new ManagerDaoImpl();
		
		try {
			JSONObject jo = new JSONObject(jsonString);
			int requestId = jo.getInt("requestId");
			boolean action = jo.getString("action").equals("approve") ? true : false;

			if (accessLevel == 2) {
				List<Request> requests = md.getPendingRequestsByManagerId(employeeId);
				requests.removeIf(r -> r.getRequestId() != requestId);
				if (requests.isEmpty()) {
					return false;
				}
				else {
					return confirmResolution(employeeId, requestId, action);
				}
			}
			else if (accessLevel == 3) {
				return confirmResolution(employeeId, requestId, action);
			}
			else {
				return false;
			}
		}
		catch (JSONException e) {
			return false;
		}
		
	}

}
