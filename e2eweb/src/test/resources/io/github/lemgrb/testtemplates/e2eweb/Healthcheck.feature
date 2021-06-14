Feature: Health check
  Quick site checks if site loads and all

  @internet
  Scenario: TC001 open homepage
    Given user visits "https://strapi-starter-next-ecommerce.vercel.app/" website
    Then "$0.00" is displayed

  @internet
  Scenario Outline: TC002 parameterized test
    Given user visits "<website>" website
    Then "<textToVerify>" is displayed

    Examples:
      | website                                           | textToVerify                      |
      | https://webdriver.io                              | Follow @webdriverio               |
      | https://www.kickstart.ph                          | We fund the future we believe in. |
      | https://strapi-starter-next-ecommerce.vercel.app/ | $0.00                             |
