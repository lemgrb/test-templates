Feature: Quick site checks
  Quick site checks if site loads and all

  Scenario: Open homepage
    When user opens "https://strapi-starter-next-ecommerce.vercel.app/" web app
    Then homepage with title "Strapi Next.js E-commerce" is displayed 
