package com.revature.sets.service;

import org.json.JSONObject;

import com.revature.sets.dao.AssociateDao;
import com.revature.sets.dao.AssociateDaoImpl;
import com.revature.sets.model.Employee;
import com.revature.sets.utility.UtilityManager;

public class PostService {

	public PostService() {
		super();
	}
	
	public String fetchUserJsonWithCredentials(String jsonString) {
		
		AssociateDao ad = new AssociateDaoImpl();
		
		JSONObject jo = new JSONObject(jsonString);
		String username = jo.getString("username");
		String password = jo.getString("password");
		
		Employee employee = ad.getEmployeeByCredentials(username, password);
		if (employee != null) {
			return UtilityManager.toJsonStringJackson(employee);
		}
		else {
			return null;
		}
		
	}

}
