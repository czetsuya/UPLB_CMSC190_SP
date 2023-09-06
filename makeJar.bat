@echo off
rem make a jar file for the application

rem Uncomment this if you jave a previous manifest file.
rem jar cvfm vc.jar manifest -C *

rem Uncomment this if you start from scratch. I mean no manifest file.
jar cvf VisualCryptography.jar .