@echo off
rem build.bat -- Build Script for the FLEX Application
rem $Id: build.bat,v 1.1 2001-07-06 19:28:28 jmunoz Exp $

set _CP=%CP%
set FLEX_TOMCAT_HOME=..\jakarta-tomcat-3.2.1
set TOMCAT_HOME=%FLEX_TOMCAT_HOME%

rem Identify the custom class path components we need
set CP=.;.\lib\ant.jar;%FLEX_TOMCAT_HOME%\lib\servlet.jar;
set CP=%CP%;%FLEX_TOMCAT_HOME%\lib\jaxp.jar;%FLEX_TOMCAT_HOME%\lib\parser.jar;
set CP=%CP%;.\lib\xerces.jar;.\lib\struts.jar
set CP=%CP%;.\lib\classes12.zip;
set CP=%CP%;.\lib\jdbc2_0-stdext.jar;
set CP=%CP%;.\lib\jmxri.jar;.\lib\jmxtools.jar;.\lib\jta.jar;
set CP=%CP%;.\lib\poolman.jar;.\lib\xerces.jar;.\lib\rowset.jar;
set CP=%CP%;%JAVA_HOME%\lib\tools.jar
rem Execute ANT to perform the requird build target
java -classpath %CP%;%CLASSPATH% org.apache.tools.ant.Main -Dtomcat.home=%TOMCAT_HOME% %1 %2 %3 %4 %5 %6 %7 %8 %9

rem set CP=%_CP%
rem set _CP=
