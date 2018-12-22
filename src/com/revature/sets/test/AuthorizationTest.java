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
	public void testIsOwnRequestOne() {
		assertTrue(as.isOwnRequest(1023, 9));
	}
	
	@Test
	public void testIsOwnRequestTwo() {
		assertFalse(as.isOwnRequest(1083, 45));
	}
	
	@Test
	public void testIsOwnRequestThree() {
		assertTrue(as.isOwnRequest(1024, 10));
	}

	@Test
	public void testHasAccessToViewRequestOne() {
		assertTrue(as.hasAccessToViewRequest(1001, 3, 2));
	}
	
	@Test
	public void testHasAccessToViewRequestTwo() {
		assertTrue(as.hasAccessToViewRequest(1007, 2, 64));
	}
	
	@Test
	public void testHasAccessToViewRequestThree() {
		assertFalse(as.hasAccessToViewRequest(1016, 1, 68));
	}

	@Test
	public void testHasAccessToViewFileOne() {
		assertFalse(as.hasAccessToViewFile(1025, 1, 21));
	}

	@Test
	public void testHasAccessToViewFileTwo() {
		assertTrue(as.hasAccessToViewFile(1008, 2, 46));
	}

	@Test
	public void testHasAccessToViewFileThree() {
		assertFalse(as.hasAccessToViewFile(1083, 1, 81));
	}
	
}
