/*
 * PrintLabel.java
 *
 * Created on July 2, 2001, 11:57 AM
 */

package edu.harvard.med.hip.flex.util;

/**
 *
 * @author  FLEX
 * @version 
 */

import java.io.*;
import java.lang.Runtime; 
import java.lang.Process; 
import java.io.IOException; 
import java.lang.InterruptedException; 

public class PrintLabel {

       /** 
        * Creates new PrintLabel 
        */
       public PrintLabel() {
       }

       public static void main(String[] args) { 

           try { 
               /* Execute the command using the Runtime object and get the 
                  Process which controls this command */ 
               Process p = Runtime.getRuntime().exec(args[0]); 

               /* Use the following code to wait for the process to finish 
                  and check the return code from the process */ 
               try { 
                    p.waitFor();
		    
                    String imsg = "";
               	    String emsg = "";
                    StringBuffer msg = new StringBuffer();

                    BufferedReader in = new
                    BufferedReader(new InputStreamReader (p.getInputStream()));

                    BufferedReader err = new
                    BufferedReader(new InputStreamReader (p.getErrorStream()));

                    while ((imsg = in.readLine()) != null) {
                        msg.append(imsg + "\n");
                    }

                    while ((emsg = err.readLine()) != null) {
                        msg.append(emsg + "\n");
                    }
                    
                    p.destroy();
                    System.out.println(msg);
                    
               /* Handle exceptions for waitFor() */ 
               } catch (InterruptedException intexc) { 
                  System.out.println("Interrupted Exception on waitFor: " +  
                            intexc.getMessage()); 
               
               } catch (Exception e) {
		            System.out.println("Exception: " + e.getMessage());
	            }

           /* Handle the exceptions for exec() */ 
           } catch (IOException e) { 
               System.out.println("IO Exception from exec : " + 
                                                e.getMessage()); 
               e.printStackTrace(); 
           }
     }
} 

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
