/*
 * TimeWindow.java
 *
 * Created on March 17, 2003, 2:55 PM
 */

package edu.harvard.med.hip.flex.query;
import java.util.Vector;
/**
 *
 * @author  hweng
 */
public class DateCollection {
    
    public static Vector getMonthCollection(){
        Vector months = new Vector();
        months.add("Month");
        months.add("JAN");
        months.add("FEB");
        months.add("MAR");
        months.add("APR");
        months.add("MAY");
        months.add("JUN");
        months.add("JUL");
        months.add("AUG");
        months.add("SEP");
        months.add("OCT");
        months.add("NOV");
        months.add("DEC");
        return months;
    }
    
    public static Vector getDayCollection(){
        Vector days = new Vector();
        days.add("Day");        
        for(int i = 1; i <= 31; i++){
            String s = "";
            if(i < 10)
                s = s + "0" + i;
            else
                s = s + i;
            days.add(s);
        }
        return days;
    }
    
    public static Vector getYearCollection(){
        Vector years = new Vector();
        years.add("Year");
        years.add("2000");
        years.add("2001");
        years.add("2002");
        years.add("2003");
        years.add("2004");
        years.add("2005");
        return years;
    }
}
