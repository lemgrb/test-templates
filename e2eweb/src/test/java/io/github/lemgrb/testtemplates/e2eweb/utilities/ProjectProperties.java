package io.github.lemgrb.testtemplates.e2eweb.utilities;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class ProjectProperties {

    @Getter
    @Setter
    private String environment;

    @Getter
    @Setter
    private Properties properties = new Properties();

    private Map<String, String> configMap = new HashMap<String, String>();

    public ProjectProperties() throws IOException {

        configMap.put("local","config.local.properties");
        configMap.put("saucelabs","config.saucelabs.properties");

        // `environment` is read from POM.XML

        setEnvironment(System.getProperty("environment"));

        log.info("▒▒▒ LOADING CONFIGURATION FILE: " + configMap.get(getEnvironment()));

        FileInputStream ip = new FileInputStream(configMap.get(getEnvironment()));

        properties.load(ip);
    }

}
