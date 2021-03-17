package com.sltclient.load_test_engine;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;

public class LoadTestEngine {
  private static final int SECOND = 1000;
  private static final int CODE = 200;
  private static final String DATA_OUTPUT = "\r %d req/sec of %d "
    + "(%d seconds passed, %d requests successfully sent)";

  public static void start() {
    PropertiesData propertiesData = new PropertiesData();
    String url = propertiesData.getUrl();
    int reqPerSec = propertiesData.getRequestsPerSecond();
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpGet httpget;
    long startTime = System.currentTimeMillis();
    long startInnerLoopTime;
    int requestPerSecondCounter = 0;
    int code;
    int totalTime = 0;
    int totalRequestCount = 0;
    System.out.println(String.format("Connected to %s", url));
    while (true) {
      if (System.currentTimeMillis() - startTime >= SECOND) {
        startInnerLoopTime = System.currentTimeMillis();
        innerLoop:
        for (int i = 0; i < reqPerSec; i++) {
          try {
            startTime = System.currentTimeMillis();
            if (System.currentTimeMillis() - startInnerLoopTime >= SECOND) {
              break innerLoop;
            }
            httpget = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpget);
            code = response.getCode();
            totalRequestCount++;
            if (code != CODE) {
              System.out.println("Request get bad code: " + code);
            }
            requestPerSecondCounter++;
            response.close();
          } catch (Exception e) {
            System.out.println("Error: " + e);
            System.exit(1);
          }
        }
        System.out.print(String.format(DATA_OUTPUT,
          requestPerSecondCounter, reqPerSec, totalTime, totalRequestCount));
        requestPerSecondCounter = 0;
        totalTime++;
      }
    }
  }
}
