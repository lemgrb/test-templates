# INSTALL

1. Generate new maven project from cucumber archetype
  ```bash
  mvn archetype:generate                      \
   "-DarchetypeGroupId=io.cucumber"           \
   "-DarchetypeArtifactId=cucumber-archetype" \
   "-DarchetypeVersion=6.10.4"               \
   "-DgroupId=io.github.lemgrb.testtemplates"                  \
   "-DartifactId=e2eweb"               \
   "-Dpackage=io.github.lemgrb.testtemplates.e2eweb"                  \
   "-Dversion=0.0.1-SNAPSHOT"                 \
   "-DinteractiveMode=false"
  ```
2. If you use IntelliJ as IDE
    1. Install plugin `Cucumber for java` -> also installs `Gherkin` plugin.
3. If you want to use lambda expressions, update `pom.xml` dependency
    - Before: `<artifactId>cucumber-java</artifactId>`
    - After: `<artifactId>cucumber-java8</artifactId>`
4. In Step Definition file, `import io.cucumber.java8.En`; and have the class implement `En`
5. Add `selenium-java` as dependency in `pom.xml`
   ```xml
   <dependency>
      <groupId>org.seleniumhq.selenium</groupId>
      <artifactId>selenium-java</artifactId>
      <version>3.X</version>
   </dependency>
   ```
6. Download the [WebDriver binary](https://www.selenium.dev/documentation/en/webdriver/driver_requirements/) supported by your browser and place it in the System PATH.
   - [Open cmd as admin] `setx /m path "%path%;C:\drivers\chromedriver\91.0.4472.19"`
7. `mvn clean test`
8. Also added `slf4j` and `lombok` in `pom.xml`
9. Add the ff. in `~.bash_profile`
```bash
export SAUCE_USERNAME="your Sauce username"
export SAUCE_ACCESS_KEY="your Sauce access key"
```

## Drivers Setup

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