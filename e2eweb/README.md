See [INSTALL.md](INSTALL.md) for project setup from scratch.

Demo application : [Next.js E-commerce Starter](https://strapi.io/starters/strapi-starter-next-js-ecommerce)

## Drivers

## Linux

### Firefox
1. Download [GeckoDriver](https://github.com/mozilla/geckodriver/releases)
2. Extract `tar -xvfz geckodriver*`
3. Add exec flag: `chmod+x geckodriver`
4. `export PATH=$PATH:/path/to/geckodriver` or `sudo mv geckodriver /usr/local/bin/`
5. If you don't want to add on path, set programmatically as `System.setProperty("webdriver.gecko.driver", "/path/to/driver")

Source: [How to install Geckodriver in Ubuntu?](https://askubuntu.com/questions/870530/how-to-install-geckodriver-in-ubuntu)
Source 2: [Driver requirements](https://www.selenium.dev/documentation/en/webdriver/driver_requirements/)

### Chrome

1. Download [ChromeDriver](https://chromedriver.chromium.org/downloads)
2. See 2-5 for Firefox

## TEST EXECUTION

`$ mvn [-Denv=local] clean test`

### Run a single feature file

`$ mvn test -Dcucumber.features="src/test/features/com/perspecsys/salesforce/featurefiles/Account.feature"`

### Run tagged scenarios

`$ mvn -Dcucumber.filter.tags="@smoke" test`


