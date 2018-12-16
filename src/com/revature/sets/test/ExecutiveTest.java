package com.revature.sets.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.sets.dao.AssociateDao;
import com.revature.sets.dao.AssociateDaoImpl;
import com.revature.sets.dao.ExecutiveDao;
import com.revature.sets.dao.ExecutiveDaoImpl;
import com.revature.sets.model.Employee;
import com.revature.sets.model.Request;

public class ExecutiveTest {
	
	private static ExecutiveDao ed;
	private static AssociateDao ad;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ed = new ExecutiveDaoImpl();
		ad = new AssociateDaoImpl();
	}

	@Test
	public void testGetEmployeesAsExecutive() {
		List<Employee> list = ed.getEmployeesAsExecutive();
		list.removeIf(e -> e.getAccessLevel() != 2);
		assertEquals(5, list.size());
	}

	@Test
	public void testGetRequestsAsExecutiveOne() {
		List<Request> list = ed.getRequestsAsExecutive();
		list.removeIf(r -> r.getEmployeeId() > 1002);
		assertTrue(list.size() > 0);
	}

//	@Test
//	public void testGetRequestsAsExecutiveTwo() {
//		assertEquals(10, ed.getRequestsAsExecutive().size());
//	}

	@Test
	public void testChangeEmployeeRole() {
		final int oldUpGroup = 101;
		final int newUpGroup = 105;
		assertNotEquals(newUpGroup, ad.getEmployeeById(1019).getUpGroup());
		ed.changeEmployeeRole(1019, newUpGroup, -1, 1);
		assertEquals(newUpGroup, ad.getEmployeeById(1019).getUpGroup());
		ed.changeEmployeeRole(1019, oldUpGroup, -1, 1);
	}

}
