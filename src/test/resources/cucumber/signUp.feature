Feature: Sign Up Page
	Scenario: Sign up with username already taken
		Given I am on the sign up page
		When I enter an invalid username "username1234" that already exists
		And I enter a password and confirmpassword "asdfasdf123"
		Then I should see the invalid username error "Username already taken!"
		
  Scenario: Passwords do not match
		Given I am on the sign up page
		When I enter a valid username "user_unused" that does not exist
		And I enter a password "password1"
		And I enter a different password "password2"
		Then I should see the invalid username error "Passwords do not match."
	
	Scenario: Successful register
		Given I am on the sign up page
		When I enter a valid username "user3004" that does not exist
		And I enter a valid password and confirmpassword "pass3000"
		Then I should be redirected to login page from signUp
