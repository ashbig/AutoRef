@echo off
rem build.bat -- Build Script for the FLEX Application
rem $Id: build.bat,v 1.2 2001-05-24 17:50:35 dongmei_zuo Exp $

set _CP=%CP%
set FLEX_TOMCAT_HOME=..\jakarta-tomcat-3.2.1

rem Identify the custom class path components we need
set CP=.\lib\ant.jar;.\lib\servlet.jar
set CP=%CP%;.\lib\jaxp.jar;.\lib\parser.jar
set CP=%CP%;.\lib\tools.jar;.\lib\xerces.jar
set CP=%CP%;.\lib\classes12.zip;.\lib\jdbc2_0-stdext.jar;.\lib\jmxri.jar;.lib\jmxtools.jar;.\lib\jta.jar;.\lib\poolman.jar;xerces.jar;.\lib\rowset.jar

rem Execute ANT to perform the requird build target
java -classpath %CP%;%CLASSPATH% org.apache.tools.ant.Main -Dtomcat.home=%TOMCAT_HOME% %1 %2 %3 %4 %5 %6 %7 %8 %9

set CP=%_CP%
set _CP=