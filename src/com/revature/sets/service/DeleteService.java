package com.revature.sets.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.revature.sets.dao.AssociateDao;
import com.revature.sets.dao.AssociateDaoImpl;
import com.revature.sets.dao.ExecutiveDao;
import com.revature.sets.dao.ExecutiveDaoImpl;
import com.revature.sets.model.Request;

public class DeleteService {

	public DeleteService() {
		super();
	}
	
	public boolean fireEmployee(String idString) {
		
		ExecutiveDao ed = new ExecutiveDaoImpl();
		
		try {
			JSONObject jo = new JSONObject(idString);
			int employeeId = jo.getInt("employeeId");
			if (ed.deleteEmployee(employeeId) == 0) {
				return false;
			}
			else {
				return true;
			}
		}
		catch (JSONException e) {
			return false;
		}
		
	}
	
	public boolean recallReimbursementRequest(int employeeId, String idString) {
		
		AssociateDao ad = new AssociateDaoImpl();
		
		try {
			JSONObject jo = new JSONObject(idString);
			int requestId = jo.getInt("requestId");
			Request request = ad.getPendingRequestByEmployeeAndRequestId(employeeId, requestId);
			if (request != null) {
				if (ad.recallRequest(request.getRequestId()) == 0) {
					return false;
				}
				else {
					return true;
				}
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
