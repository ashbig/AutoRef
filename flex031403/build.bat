@echo off
rem build.bat -- Build Script for the FLEX Application
rem $Id: build.bat,v 1.1 2001-05-24 17:04:38 dongmei_zuo Exp $

set _CP=%CP%
set FLEX_TOMCAT_HOME=..\jakarta-tomcat-3.2.1

rem Identify the custom class path components we need
set CP=%FLEX_TOMCAT_HOME%\lib\ant.jar;%FLEX_TOMCAT_HOME%\lib\servlet.jar
set CP=%CP%;%FLEX_TOMCAT_HOME%\lib\jaxp.jar;%FLEX_TOMCAT_HOME%\lib\parser.jar
set CP=%CP%;%JAVA_HOME%\lib\tools.jar
set CP=%CP%;.\lib\classes12.zip;.\lib\jdbc2_0-stdext.jar;.\lib\jmxri.jar;.lib\jmxtools.jar;.\lib\jta.jar;.\lib\poolman.jar;xerces.jar;.\lib\rowset.jar

rem Execute ANT to perform the requird build target
java -classpath %CP%;%CLASSPATH% org.apache.tools.ant.Main -Dtomcat.home=%TOMCAT_HOME% %1 %2 %3 %4 %5 %6 %7 %8 %9

set CP=%_CP%
set _CP=