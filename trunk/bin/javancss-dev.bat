@echo off
setlocal
REM Please adapt this script to your environment.

@REM Set JAVANCSS_HOME to the directory this batch file is in.
set me=%0
for %%i in (%me%) do set JAVANCSS_HOME=%%~dpi
set JAVANCSS_HOME=%JAVANCSS_HOME%..

REM #################### EDIT THIS ENVIRONMENT VARIABLE IF NOT ALREADY SET #################
set CLASSPATH=%JAVANCSS_HOME%\target\javancss-34.55-SNAPSHOT.jar;%JAVANCSS_HOME%\lib\ccl.jar;%JAVANCSS_HOME%\lib\jhbasic.jar

"%JAVA_HOME%\bin\java" -classpath %CLASSPATH% javancss.Main %1 %2 %3 %4 %5 %6 %7 %8 %9

endlocal
