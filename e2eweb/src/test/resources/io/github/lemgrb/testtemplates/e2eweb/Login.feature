Feature: Login

  Rule: Users with valid credentials can log in
    
    Scenario: Valid username and password
      Given user visits "http://localhost:1337/admin" website
      Then "Email" is displayed
      And "Password" is displayed
      When user enters "test@mailinator.com" email
      And user enters "TestPass123" password
      And user clicks "Log in"
      Then "Hi LEM!" is displayed

  Rule: Users with invalid credentials cannot log in

    Scenario: Non-existing user
      Given user visits "http://localhost:1337/admin" website
      Then "Email" is displayed
      And "Password" is displayed
      When user enters "unknown@mailinator.com" email
      And user enters "TestPass123" password
      And user clicks "Log in"
      Then "Invalid credentials" is displayed

    Scenario: Existing user, Invalid password
      Given user visits "http://localhost:1337/admin" website
      Then "Email" is displayed
      And "Password" is displayed
      When user enters "test@mailinator.com" email
      And user enters "WRONGPASSWORD" password
      And user clicks "Log in"
      Then "Invalid credentials" is displayed

    Scenario: Blank username and password
      Given user visits "http://localhost:1337/admin" website
      Then "Email" is displayed
      And "Password" is displayed
      And user clicks "Log in"
      Then "This value is required" is displayed
