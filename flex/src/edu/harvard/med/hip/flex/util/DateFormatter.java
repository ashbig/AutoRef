/*
 * DateFormatter.java
 *
 * Created on July 16, 2001, 1:56 PM
 */

package edu.harvard.med.hip.flex.util;
import java.text.*;
import java.util.*;

/**
 *
 * @author  Wendy
 * @version
 */

public class DateFormatter {
    private SimpleDateFormat _formatter = null;
    
    public DateFormatter() {
        _formatter = new SimpleDateFormat("dd-MMM-yy");
    }
    
    public String getDateString() {
        String result = null;
        java.util.Date currentDate = new java.util.Date();
        result = _formatter.format(currentDate);
        return result;
    } // getDateString
    
    public static void main(String[] args) {
        DateFormatter app = new DateFormatter();
        app.test();
    } // main
    
    private void test() {
        String dateString = getDateString();
        System.out.println("The current date string: "+dateString);
    } // test
} // DateFormatter

