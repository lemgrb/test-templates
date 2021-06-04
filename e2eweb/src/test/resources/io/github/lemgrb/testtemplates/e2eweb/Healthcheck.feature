Feature: Quick site checks
  Quick site checks if site loads and all

  Scenario: Open homepage
    Given user visits "https://strapi-starter-next-ecommerce.vercel.app/" website
    Then "$0.00" is displayed

