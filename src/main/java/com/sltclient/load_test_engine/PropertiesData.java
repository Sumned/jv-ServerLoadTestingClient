package com.sltclient.load_test_engine;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesData {
  private static final String PROP_FILE_NAME = "config.properties";
  private static final String URL = "URL";
  private static final String REQUEST_PER_SECOND = "REQUEST_PER_SECOND";

  private final Map<String, String> data;

  public PropertiesData() {
    this.data = new HashMap<String, String>();
    Properties properties = new Properties();
    try {
      InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROP_FILE_NAME);
      properties.load(inputStream);
      data.put(URL, properties.getProperty(URL));
      data.put(REQUEST_PER_SECOND, properties.getProperty(REQUEST_PER_SECOND));
    } catch (Exception e) {
      System.out.println("Error: " + e);
      System.exit(1);
    }
  }

  public String getUrl() {
    return this.data.get(URL);
  }

  public int getRequestsPerSecond() {
    return Integer.parseInt(this.data.get(REQUEST_PER_SECOND));
  }
}
