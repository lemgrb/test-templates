package io.github.lemgrb.testtemplates.e2eweb.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Get properties.
 */
@Slf4j
public class ProjectProperties {

  @Getter
  @Setter
  private String environment;

  @Getter
  @Setter
  private Properties properties = new Properties();

  /**
   * Read "environment" property from System environment variables
   * and load it into a HashMap.
   *
   * @throws IOException Failed to read configuration file.
   */
  public ProjectProperties() throws IOException {

    Map<String, String> configMap = new HashMap<>();
    configMap.put("local", "config.local.properties");
    configMap.put("saucelabs", "config.saucelabs.properties");
    configMap.put("remote", "config.remote.properties");

    // `environment` is read from POM.XML

    setEnvironment(System.getProperty("environment"));

    log.info("▒▒▒ LOADING CONFIGURATION FILE: " + configMap.get(getEnvironment()));

    FileInputStream ip = new FileInputStream(configMap.get(getEnvironment()));

    properties.load(ip);
  }

}
