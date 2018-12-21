package com.revature.sets.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.sets.dao.ManagerDao;
import com.revature.sets.dao.ManagerDaoImpl;
import com.revature.sets.model.Request;

public class ManagerTest {
	
	private static ManagerDao md;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		md = new ManagerDaoImpl();
	}

	@Test
	public void testGetEmployeesByManagerIdOne() {
		assertEquals(201, md.getEmployeesByManagerId(1005).get(1).getUpGroup());
	}

	@Test
	public void testGetEmployeesByManagerIdTwo() {
		assertEquals(0, md.getEmployeesByManagerId(1002).size());
	}

	@Test
	public void testGetEmployeesByManagerIdThree() {
		assertEquals(104, md.getEmployeesByManagerId(1009).get(0).getUpGroup());
	}
	
	@Test
	public void testGetPendingRequestsByManagerIdOne() {
		List<Request> list = md.getPendingRequestsByManagerId(1006);
		list.removeIf(r -> r.getRequestId() != 3);
		assertEquals("Sales", list.get(0).getReason());
	}

	@Test
	public void testGetPendingRequestsByManagerIdTwo() {
		List<Request> list = md.getPendingRequestsByManagerId(1006);
		list.removeIf(r -> r.getRequestId() != 4);
		assertEquals("Relocation", list.get(0).getReason());
	}

	@Test
	public void testGetResolvedRequestsAsManagerOne() {
		List<Request> list = md.getResolvedRequestsAsManager();
		list.removeIf(r -> r.getRequestId() != 2);
		assertEquals("Travel", list.get(0).getReason());
	}
	
	@Test
	public void testGetResolvedRequestsAsManagerTwo() {
		List<Request> list = md.getResolvedRequestsAsManager();
		list.removeIf(r -> r.getRequestId() != 10);
		assertEquals("Sales", list.get(0).getReason());
	}
	
	@Test
	public void testGetEmployeeEmailByRequestId() {
		assertEquals("karam.huffman@mailinator.com", md.getEmployeeEmailByRequestId(8));
	}

}
