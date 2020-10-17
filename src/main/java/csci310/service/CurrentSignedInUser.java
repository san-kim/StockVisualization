package csci310.service;

import csci310.model.User;

public class CurrentSignedInUser {
	
	private static CurrentSignedInUser instance = new CurrentSignedInUser();
	private User currentUser;
	
	public static CurrentSignedInUser getInstance() {
		return instance;
	}
	
	public User getCurrentUser() {
		return currentUser;
	}
	
	public void signOutCurrentUser() {
		currentUser = null;
	}
	
	public void signInCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

}
