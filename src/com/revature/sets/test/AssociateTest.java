package com.revature.sets.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.sets.dao.AssociateDao;
import com.revature.sets.dao.AssociateDaoImpl;
import com.revature.sets.model.Employee;
import com.revature.sets.model.Request;

public class AssociateTest {
	
	private static AssociateDao ad;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ad = new AssociateDaoImpl();
	}

	@Test
	public void testGetEmployeeByCredentialsFirstName() {
		assertEquals("Jessica", ad.getEmployeeByCredentials("jrenzi", "jrenzi123456").getFirstName());
	}
	
	@Test
	public void testGetEmployeeByCredentialsLastName() {
		assertEquals("Love", ad.getEmployeeByCredentials("alove", "alove123456").getLastName());
	}
	
	@Test
	public void testGetEmployeeByCredentialsInvalid() {
		assertNull(ad.getEmployeeByCredentials("?", "?"));
	}

	@Test
	public void testGetEmployeeByIdFirstName() {
		assertEquals("Sharon", ad.getEmployeeById(1007).getFirstName());
	}
	
	@Test
	public void testGetEmployeeByIdLastName() {
		assertEquals("Lake", ad.getEmployeeById(1020).getLastName());
	}

	@Test
	public void testGetEmployeeByEmailFirstName() {
		assertEquals("Krystian", ad.getEmployeeByEmail("krystian.marii@mailinator.com").getFirstName());
	}
	
	@Test
	public void testGetEmployeeByEmailLastName() {
		assertEquals("Hodge", ad.getEmployeeByEmail("mccauley.hodge@mailinator.com").getLastName());
	}

	@Test
	public void testGetRequestsByEmployeeId() {
		final Request expected = new Request(1);
		assertEquals(expected, ad.getRequestsByEmployeeId(1001).get(0));
	}
	
	@Test
	public void testGetPendingRequestByEmployeeAndRequestId() {
		assertEquals(1000, (int)ad.getPendingRequestByEmployeeAndRequestId(1001, 1).getAmount());
	}
	
	@Test
	public void testGetPendingRequestByEmployeeAndRequestIdNull() {
		assertNull(ad.getPendingRequestByEmployeeAndRequestId(1017, 1));
	}

	@Test
	public void testChangeCredentialsInvalid() {
		final String invalidPassword = "123456";
		assertEquals(0, ad.changeCredentials(1009, invalidPassword, "myNewName", "myNewPassword"));
	}
	
	@Test
	public void testChangeCredentialsValid() {
		final Employee expected = new Employee(1024);
		final String newUsername = "amunro6";
		final String newPassword = "amunro12345";
		ad.changeCredentials(1024, "amunro123456", newUsername, newPassword);
		assertEquals(expected, ad.getEmployeeByCredentials(newUsername, newPassword));
		ad.changeCredentials(1024, "amunro12345", "amunro", "amunro123456");
	}

}
