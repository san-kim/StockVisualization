Feature: Graph of stock portfolio

  Scenario: Adding the S&P 500 line to the graph
    Given I am logged in
    And I am on the dashboard page
    When I click Add S&P 500
    Then I should see the S&P 500 line added to the graph
    
  Scenario: Removing the S&P 500 line to the graph
    Given I am logged in
    And I am on the dashboard page
    When I click Remove S&P 500
    Then I should see the S&P 500 line removed from the graph
    
  Scenario: Add a stock to the graph
    Given I am logged in
    And I am on the dashboard page
    When I add a stock (i.e. Apple)
    Then I should see stock's performance line added to the graph
    
  Scenario: Removing a stock from the graph
    Given I am logged in
    And I am on the dashboard page
    When I click remove stock for a stock currently on the graph
    Then I should see the stock's performance line removed from the graph
    
  Scenario: Changing the time frame of the graph
    Given I am logged in
    And I am on the dashboard page
    When I click 1-month
    Then I should see the graph's axes change and the lines reflected change as well to represent 1 month's worth of data
  
  Scenario: Zooming in on the graph
    Given I am logged in
    And I am on the dashboard page
    When I click Zoom in 
    Then I should see the graph's axes change and the range of time displayed decrease
    
  Scenario: Zooming out of the graph
    Given I am logged in
    And I am on the dashboard page
    When I click Zoom out
    Then I should see the graph's axes change and the range of time displayed increase