Feature: Admin login form

  Scenario: Admin can log in
    Given I am on page "/admin/login"
    And I fill Login with "admin1"
    And I fill Password "[9k<k8^z!+$$GkuP"
    When I click Sign in
    Then I see url is "/configurator/dashboard/index"
    And I see Admin panel
