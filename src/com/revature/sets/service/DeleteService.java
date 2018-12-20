package com.revature.sets.service;

import org.json.JSONObject;

import com.bea.httppubsub.json.JSONException;
import com.revature.sets.dao.ExecutiveDao;
import com.revature.sets.dao.ExecutiveDaoImpl;

public class DeleteService {

	public DeleteService() {
		super();
	}
	
	public boolean fireEmployee(String idString) {
		
		ExecutiveDao ed = new ExecutiveDaoImpl();
		
		JSONObject jo = new JSONObject(idString);
		try {
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

}
