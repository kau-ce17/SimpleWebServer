@echo off
cls
set fn=MultiThreaded_WebServer.class
if not exist %fn% (
  javac -g -classpath .;webserve.jar; -d . *.java
)
java -classpath .;webserve.jar MultiThreaded_WebServer