package com.revature.sets.service;

import com.revature.sets.dao.AssociateDao;
import com.revature.sets.dao.AssociateDaoImpl;
import com.revature.sets.model.Employee;
import com.revature.sets.utility.UtilityManager;

public class GetService {

	public GetService() {
		super();
	}
	
	public String fetchUserJsonWithSession(String idString) {
		
		AssociateDao ad = new AssociateDaoImpl();
		Employee employee = ad.getEmployeeById(Integer.parseInt(idString));
		if (employee != null) {
			return UtilityManager.toJsonStringJackson(employee);
		}
		
		return null;
		
	}

}
