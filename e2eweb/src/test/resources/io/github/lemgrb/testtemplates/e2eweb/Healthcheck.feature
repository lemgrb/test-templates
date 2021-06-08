Feature: Health check
  Quick site checks if site loads and all
  
  @internet
  Scenario: TC001 Open homepage
    Given user visits "https://strapi-starter-next-ecommerce.vercel.app/" website
    Then "$0.00" is displayed

