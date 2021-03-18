# jv-ServerLoadTestingClient

Simple application for checking server load testing.
You can set your own URL and/or amount of request per second in **src\main\resources\config.properties**.
For using from the command line you must compile this project with Maven to .jar file and run with command -start.
If you want to get a response from the server, you need to add -response after -start.
For example: 
* *- java -jar ServerLoadTestingClient-1.0-SNAPSHOT.jar -start -response*