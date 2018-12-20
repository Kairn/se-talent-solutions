package com.revature.sets.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.revature.sets.dao.ExecutiveDao;
import com.revature.sets.dao.ExecutiveDaoImpl;

public class PutService {

	public PutService() {
		super();
	}
	
	public boolean changeEmployeeRole(String jsonString) {
		
		ExecutiveDao ed = new ExecutiveDaoImpl();
		
		JSONObject jo = new JSONObject(jsonString);
		try {
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

}
