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
        * static variables
        **/
       static final String printerName = "zebra";
       static final int labelWidth = 10;
       static final int labelHeight = 10;
       
       /** 
        * Creates new PrintLabel 
        */
       public PrintLabel() {
       }

       public String execute(String label) {
           /** 
            * the status message that going to return
            **/
           StringBuffer msg = new StringBuffer();
           

           try { 
               /** 
                * Execute the command using the Runtime object and get the 
                * Process which controls this command 
                **/
               String cmd = "perl /kotel/data/home/jmunoz/flex/ApplicationCode/Perl/print_label.pl " + label + " " 
                            + printerName + " " 
                            + labelWidth + " " 
                            + labelHeight ;
               
               Process p = Runtime.getRuntime().exec(cmd); 
                      
               /**
                * Use the following code to wait for the process to finish 
                * and check the return code from the process 
                **/ 
               try { 
                    p.waitFor();
		    
                    String imsg = "";
               	    String emsg = "";
                    
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
               /**
                * Handle exceptions for waitFor() 
                **/ 
               } catch (InterruptedException intexc) { 
                    msg.append("Interrupted exception from waitfor, detail: " 
                                + intexc.getMessage()); 
               
               } catch (Exception e) {
		            msg.append("Exception when printing label, detail: " 
                                + e.getMessage());
               }

           /** 
            * Handle the exceptions for exec() 
            **/ 
           } catch (IOException e) { 
               msg.append("IO Exception from exec, detail : " 
                                  + e.getMessage()); 
           }
           
           finally {
                return msg.toString();
           }
     }
} 

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
