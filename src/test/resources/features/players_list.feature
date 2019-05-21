Feature: Players list

  Scenario: Can see players
    Given I am logged with "admin1" and "[9k<k8^z!+$$GkuP"
    When I click by text "Players online / total"
    Then I see url is "/user/player/admin"
    And I see "#payment-system-transaction-grid"

  Scenario: Can sort players
    Given I am logged with "admin1" and "[9k<k8^z!+$$GkuP"
    And I am on page "/user/player/admin"
    When I click column header "Username"
    Then column "Username" is sorted by asc
