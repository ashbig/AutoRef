/*
 * Test.java
 *
 * Created on May 21, 2003, 2:52 PM
 */

package edu.harvard.med.hip.flex.special_projects;

/**
 *
 * @author  dzuo
 */
import java.util.*;

 public class Test {

 public static void  main(String arg[]) {
     /* 
     ** on some JDK, the default TimeZone is wrong
     ** we must set the TimeZone manually!!!
     **   Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("EST"));
     */
     Calendar cal = Calendar.getInstance(TimeZone.getDefault());
     
     String DATE_FORMAT = "yyyy_MM_dd_HH_mm_ss";
     java.text.SimpleDateFormat sdf = 
           new java.text.SimpleDateFormat(DATE_FORMAT);
     /*
     ** on some JDK, the default TimeZone is wrong
     ** we must set the TimeZone manually!!!
     **     sdf.setTimeZone(TimeZone.getTimeZone("EST"));
     */
     sdf.setTimeZone(TimeZone.getDefault());          
           
     System.out.println("Now : " + sdf.format(cal.getTime()));
     System.out.println(cal.getTime());
     }
 }
