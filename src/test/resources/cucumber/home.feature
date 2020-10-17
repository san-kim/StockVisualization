Feature: Home Page
  Scenario: Looking at the home page
    Given I am on the home page
    When I click on the sign out button
    Then I should be signed out and taken back to the sign in page
    
  Scenario: Timing out user
    Given I am on the home page
    When 120 seconds of inactivity occurs
    Then I should see an alert that I am being logged out

	Scenario: User not logged in
		Given I am forcefully on the home page
    When I am not logged in
    Then I should be redirected to login page