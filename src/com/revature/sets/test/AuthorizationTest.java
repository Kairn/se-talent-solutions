package com.revature.sets.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.sets.service.AuthorizationService;

public class AuthorizationTest {
	
	private static AuthorizationService as;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		as = new AuthorizationService();
	}

	@Test
	public void testHasAccessToUploadToRequestOne() {
		assertTrue(as.hasAccessToUploadToRequest(1001, 1));
	}
	
	@Test
	public void testHasAccessToUploadToRequestTwo() {
		assertFalse(as.hasAccessToUploadToRequest(1020, 7));
	}
	
	@Test
	public void testHasAccessToUploadToRequestThree() {
		assertFalse(as.hasAccessToUploadToRequest(1002, 67));
	}

	@Test
	public void testIsOwnRequest() {
		fail("Not yet implemented");
	}

	@Test
	public void testHasAccessToViewRequest() {
		fail("Not yet implemented");
	}

	@Test
	public void testHasAccessToViewFile() {
		fail("Not yet implemented");
	}

}
