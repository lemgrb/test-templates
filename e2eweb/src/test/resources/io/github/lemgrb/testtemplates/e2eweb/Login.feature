Feature: Login

  Rule: Users with valid credentials can log in

    Scenario: TC001 Valid username and password
      Given user visits "http://localhost:1337/admin" website
      Then "Email" is displayed
      And "Password" is displayed
      When user enters "test@mailinator.com" email
      And user enters "TestPass123" password
      And user clicks "Log in"
      Then "Hi Lem!" is displayed

  Rule: Users with invalid credentials cannot log in

    Scenario: TC002 Non-existing user
      Given user visits "http://localhost:1337/admin" website
      Then "Email" is displayed
      And "Password" is displayed
      When user enters "unknown@mailinator.com" email
      And user enters "TestPass123" password
      And user clicks "Log in"
      Then "Invalid credentials" is displayed

    Scenario: TC003 Existing user, Invalid password
      Given user visits "http://localhost:1337/admin" website
      Then "Email" is displayed
      And "Password" is displayed
      When user enters "test@mailinator.com" email
      And user enters "WRONGPASSWORD" password
      And user clicks "Log in"
      Then "Invalid credentials" is displayed

    Scenario: TC004 Blank username and password
      Given user visits "http://localhost:1337/admin" website
      Then "Email" is displayed
      And "Password" is displayed
      And user clicks "Log in"
      Then "This value is required" is displayed

