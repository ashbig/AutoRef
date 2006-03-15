/*
 * PrintLabel.java
 *
 * Created on July 2, 2001, 11:57 AM
 */

package plasmid.util;

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
       static final String printerName = "Hip Office";       
       //static final String printerName = "128.103.32.158";
       static final int labelWidth = 25;
       //static final int labelHeight = 15;
       static final int labelHeight = 3;
       
       /** 
        * Creates new PrintLabel 
        */
       public PrintLabel() {
       }

       public static String execute(String label) {
           /** 
            * the status message that going to return
            **/
           StringBuffer msg = new StringBuffer();

           try { 
               /** 
                * Execute the command using the Runtime object and get the 
                * Process which controls this command 
                **/
               /**
               String cmd = "perl /usr/local/jakarta-tomcat-4.0.6/webapps/PLASMID/WEB-INF/perl/print_label.pl " 
                            + label + " " 
                            + printerName + " " 
                            + labelWidth + " " 
                            + labelHeight ;
               */
               String cmd = "perl \"F:\\Program Files\\Apache Tomcat 4.0\\webapps\\PLASMID\\WEB-INF\\perl\\print_label.pl\" " 
                            + label + " " 
                            + printerName + " " 
                            + labelWidth + " " 
                            + labelHeight ;
               System.out.println(cmd);
               Process p = Runtime.getRuntime().exec(cmd); 
               System.out.println("printing label");  
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
                   //System.out.println(intexc);
                    msg.append("Interrupted exception from waitfor, detail: " 
                                + intexc.getMessage()); 
               
               } catch (Exception e) {
                   //System.out.println(e);
		            msg.append("Exception when printing label, detail: " 
                                + e.getMessage());
               }

           /** 
            * Handle the exceptions for exec() 
            **/ 
           } catch (IOException e) { 
                   //System.out.println(e);
               msg.append("IO Exception from exec, detail : " 
                                  + e.getMessage()); 
           }
           
           finally {
                return msg.toString();
           }
     }

     /**
      * Testing
      **/
     public static void main(String[] args) {
          
            String status = PrintLabel.execute("Ou000000-f");
            System.out.println("Print Status: " + status);
     }
}

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
