package com.sltclient.load_test_engine;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.http.HttpResponse;

import java.io.IOException;
import java.util.concurrent.Future;

public class LoadTestEngine {
  private static final int SECOND = 1000;
  private static final int CODE = 200;
  private static final String DATA_OUTPUT = "\r %d req/sec of %d "
    + "(%d seconds passed, %d requests successfully sent)";

  public static void start(boolean needResponse) throws IOException {
    PropertiesData propertiesData = new PropertiesData();
    String url = propertiesData.getUrl();
    int reqPerSec = propertiesData.getRequestsPerSecond();
    long startTime = System.currentTimeMillis();
    long startInnerLoopTime;
    int requestPerSecondCounter = 0;
    int code;
    int totalTime = 0;
    int totalRequestCount = 0;
    System.out.println(String.format("Connected to %s", url));
    CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
    try {
      httpclient.start();
      HttpGet httpGet = new HttpGet(url);
      while (true) {
        if (System.currentTimeMillis() - startTime >= SECOND) {
          startInnerLoopTime = System.currentTimeMillis();
          innerLoop:
          for (int i = 0; i < reqPerSec; i++) {
            startTime = System.currentTimeMillis();
            if (System.currentTimeMillis() - startInnerLoopTime >= SECOND) {
              break innerLoop;
            }

            if (needResponse) {
              Future<SimpleHttpResponse> future = httpclient.execute(SimpleHttpRequest.copy(httpGet), null);
              HttpResponse response = future.get();
              code = response.getCode();
              totalRequestCount++;
              if (code != CODE) {
                System.out.println("Request get bad code: " + code);
              } else {
                httpclient.execute(SimpleHttpRequest.copy(httpGet), null);
              }
            }

            requestPerSecondCounter++;
          }
          System.out.print(String.format(DATA_OUTPUT,
            requestPerSecondCounter, reqPerSec, totalTime, totalRequestCount));
          requestPerSecondCounter = 0;
          totalTime++;
        }
      }
    } catch (Exception e) {
      System.out.println("Error: " + e);
      System.exit(1);
    } finally {
      httpclient.close();
    }
  }
}
