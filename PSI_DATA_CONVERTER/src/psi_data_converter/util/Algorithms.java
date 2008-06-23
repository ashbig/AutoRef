/*
 * Algorithms.java
 *
 * Created on May 12, 2008, 1:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package psi_data_converter.util;
import java.util.*;

/**
 *
 * @author htaycher
 */
public class Algorithms {
    
     public static  String convertArrayToString(String[] arr, String delim)
    {
        StringBuffer res = new StringBuffer();
        if ( arr == null) return null;
        for (int count = 0; count < arr.length;count++)
        {
            res.append( arr[count]);
            if (count != arr.length - 1 ) res .append(delim);
        }
        return res.toString();
    }
    
}
