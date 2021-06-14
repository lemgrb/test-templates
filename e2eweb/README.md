See [INSTALL.md](INSTALL.md) for project setup from scratch.

Demo application : [Next.js E-commerce Starter](https://strapi.io/starters/strapi-starter-next-js-ecommerce) --> The backend (localhost:1337) of this starter web app is used in the tests.

> **Environment** (local, saucelabs) and **platform** (default, chrome, etc) are two properties to be read from command line or environment variables. All other settings are retrieved from config.${ENVIRONMENT}.properties

## Project-specific Environment Variables

### [For saucelabs] Remote run
```bash
export SAUCE_USERNAME="your Sauce username"
export SAUCE_ACCESS_KEY="your Sauce access key"
```

### [For cucumber] Publishing on reports.cucumber.io
```bash
export CUCUMBER_PUBLISH_ENABLED=true
export CUCUMBER_PUBLISH_TOKEN=XXXX 
```

## To run unit tests

Unit tests file format: ExcelTestDataReader**Test**.java

```bash
mvn clean test
```

The ff. non-out of the box features are unit tested:
1. ExcelTestDataReader

## Run Integration Test

Integration tests file format: RunCucumber**IT**.java (IT for Integration Test)

```bash
mvn -Dcucumber.filter.tags="@internet" clean test failsafe:integration-test
```

### Specify *environment* in command line

```bash
mvn [-DENVIRONMENT=local] failsafe:integration-test`
```

Supported Environments: 

1. `local` (default)
2. `saucelabs`

When `local` is selected, `config.local.properties` will be used.

When `saucelabs` is selected, `config.saucelabs.properties` will be used.


### Specify *platform* in command line
```bash
mvn [-Dplatform=default] failsafe:integration-test
```

Local platforms:
- chrome
- firefox
- edge

Saucelabs platforms:
- windows_10_edge
- mac_sierra_chrome
- windows_8_ff
- windows_8_1_ie
- mac_mojave_safari

### Run a single feature file

```bash
mvn -Dcucumber.features="src/test/features/com/perspecsys/salesforce/featurefiles/Account.feature" failsafe:integration-test
```

### Run tagged scenarios

```bash
mvn -Dcucumber.filter.tags="@internet" failsafe:integration-test
```

### Putting them all together

Run all Gherkin Scenarios tagged as `@internet` on Saucelabs:

```bash
mvn -Dcucumber.filter.tags="@internet" \
-Denvironment=saucelabs \
-Dplatform=mac_sierra_chrome \
 clean test failsafe:integration-test
```

Run all Gherkin Scenarios tagged as `@internet` locally:

```bash
mvn -Dcucumber.filter.tags="@internet" \
-Denvironment=local \
-Dplatform=firefox \
 clean test failsafe:integration-test
```

Run all Gherkin Scenarios tagged as `@internet` on remote server:

```bash
mvn -Dcucumber.filter.tags="@internet" \
-Denvironment=remote \
-Dplatform=firefox \
 clean test failsafe:integration-test
```

## Test Data File Format

The Excel test data is located at `src/test/resources/TestData.xlsx`.

Current limitation: 
- **All cells must be formatted as String**
- Every row must have equal number of columns

## Parallel execution with Junit + Failsafe

Current implementation: 

- 1 browser per script execution `mvn failsafe:integration-test -Dplatform=chrome`
- 1 feature file : 1 Thread

Failsafe plugin is used for parallel execution. See `pom.xml` file.
The relevant settings are the following:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
    ...
    <configuration>
        <systemPropertyVariables>
            <environment>local</environment>
        </systemPropertyVariables>
        <parallel>methods</parallel>
        <threadCount>4</threadCount>
    </configuration>
</plugin>
```

## Selenium Grid
- Setup grid using docker-compose [Docker compose](https://github.com/SeleniumHQ/docker-selenium#docker-compose)
- [Increase concurrency per container](https://github.com/SeleniumHQ/docker-selenium#increasing-session-concurrency-per-container)
Example:
```yaml
  1 # To execute this docker-compose yml file use `docker-compose -f docker-compose-v3.yml up`
  2 # Add the `-d` flag at the end for detached execution
  3 # To stop the execution, hit Ctrl+C, and then `docker-compose -f docker-compose-v3.yml down`
  4 version: "3"
  5 services:
  6   chrome:
  7     image: selenium/node-chrome:4.0.0-beta-4-20210608
  8     volumes:
  9       - /dev/shm:/dev/shm
 10     depends_on:
 11       - selenium-hub
 12     environment:
 13       - SE_EVENT_BUS_HOST=selenium-hub
 14       - SE_EVENT_BUS_PUBLISH_PORT=4442
 15       - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
 16       - SE_NODE_OVERRIDE_MAX_SESSIONS=true
 17       - SE_NODE_MAX_SESSIONS=2

```

```bash
$ sudo docker-compose up
```
