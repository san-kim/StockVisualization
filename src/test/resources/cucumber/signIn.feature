Feature: Sign in Page
  Scenario: Sign in with username that does not exist
    Given I am on the sign in page
    When I enter an invalid username "test3"
    And I enter any password
    And I click submit on login
    Then I should see the error "No user with this username exists"
    
  Scenario: Sign in with valid username but invalid password
    Given I am on the sign in page
    When I enter an valid username "test2"
    And I enter an incorrect password "test2test2"
    And I click submit on login
    Then I should see the error "The password you entered is invalid"
    
  Scenario: Sign in with 0 char username
    Given I am on the sign in page
    When I enter an invalid username ""
    And I enter any password
    And I click submit on login
    Then I should see the error "Username cannot be empty."
    
  Scenario: Sign in with less than 8 char password
    Given I am on the sign in page
    When I enter an valid username "test1"
    And I enter an incorrect password "test"
    And I click submit on login
    Then I should see the error "Please input a password of at least 8 characters"
    
  Scenario: Sign in with 0 char username and less than 8 char password
    Given I am on the sign in page
    When I enter an invalid username ""
    And I enter an incorrect password "test"
    And I click submit on login
    Then I should see the error "Username cannot be empty and password must be at least 8 characters"
    
  Scenario: Lock out after three unsuccessful attempts
    Given I am on the sign in page
    When I click submit on login
    And I click submit on login
    And I click submit on login
    And I click submit on login
    Then I should see the error "You have been locked for failing to sign in three times"
    
  Scenario: Successful Sign in
    Given I am on the sign in page
    When I enter an valid username "test2"
    And I enter the correct password "test2test"
    And I click submit on login
    Then I should be taken to the home page