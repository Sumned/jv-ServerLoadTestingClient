package com.sltclient.console;

import com.sltclient.load_test_engine.LoadTestEngine;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Console {
  public void run(String[] args) {
    CommandLine commandLine = readCommandArgs(args);
    if (commandLine != null && (commandLine.hasOption("start"))) {
      System.out.println("Press ctrl+C to stop.");
      try {
        LoadTestEngine.start(commandLine.hasOption("response"));
      } catch (Exception e) {
        System.out.println("Error: " + e);
        System.exit(1);
      }
    } else {
      help();
    }
  }

  private CommandLine readCommandArgs(String[] args) {
    CommandLineParser parser = new DefaultParser();
    CommandLine commandLine = null;
    try {
      commandLine = parser.parse(getOptions(), args);
    } catch (ParseException e) {
      System.out.println("Failed: " + e);
      System.exit(1);
    }
    return commandLine;
  }

  private Options getOptions() {
    Options options = new Options();
    options.addOption("start",
      "Input -start after .jar to start the application");
    options.addOption("response", "Input -response after -start if you want to get response");
    return options;
  }

  private void help() {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("Options:", getOptions(), true);
  }
}
