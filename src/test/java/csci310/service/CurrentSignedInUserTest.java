package csci310.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import csci310.model.User;

public class CurrentSignedInUserTest {
	
	private static CurrentSignedInUser instance;
	
	@Before
	public void setUp() {
		instance = CurrentSignedInUser.getInstance();
		instance.signOutCurrentUser();
	}

	@Test 
	public void testGetCurrentUser() {
		assertTrue(instance.getCurrentUser() == null);
	}
	
	
	@Test
	public void testSignOutCurrentUser() {
		User testUser = new User("username", 1);
		instance.signInCurrentUser(testUser);
		instance.signOutCurrentUser();
		assertTrue(instance.getCurrentUser() == null);
	}
	
	@Test
	public void testSignInCurrentUser() {
		User testUser = new User("username", 1);
		instance.signInCurrentUser(testUser);
		User currentUser = instance.getCurrentUser();
		assertTrue(currentUser != null && currentUser.getUsername().equalsIgnoreCase("username"));
	}

}
