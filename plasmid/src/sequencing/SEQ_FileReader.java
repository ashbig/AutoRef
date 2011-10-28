/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sequencing;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dongmei
 */
public class SEQ_FileReader {
    public List<SEQ_Order> readSeqOrders(InputStream input) throws SEQ_Exception {
        List<SEQ_Order> orders = new ArrayList<SEQ_Order>();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line = in.readLine();
            while((line = in.readLine())!= null) {
                int userid = Integer.parseInt(line.substring(0, line.indexOf("\t")));
                line = line.substring(line.indexOf("\t")+1).replaceAll("\"", "");
                
                int orderid = Integer.parseInt(line.substring(0, line.indexOf("\t")));
                line = line.substring(line.indexOf("\t")+1).replaceAll("\"", "");
                
                String billingemail = line.substring(0, line.indexOf("\t"));
                line = line.substring(line.indexOf("\t")+1).replaceAll("\"", "");
                
                String billingaddress = line.substring(0, line.indexOf("\t"));
                line = line.substring(line.indexOf("\t")+1).replaceAll("\"", "");
                
                String pifirstname = line.substring(0, line.indexOf("\t"));
                line = line.substring(line.indexOf("\t")+1).replaceAll("\"", "");
                
                String pilastname = line.substring(0, line.indexOf("\t"));
                line = line.substring(line.indexOf("\t")+1).replaceAll("\"", "");
                
                String piemail = line.substring(0, line.indexOf("\t"));
                line = line.substring(line.indexOf("\t")+1).replaceAll("\"", "");
                
                String username = line.substring(0, line.indexOf("\t"));
                line = line.substring(line.indexOf("\t")+1).replaceAll("\"", "");
                
                String ponumber = line.substring(0, line.indexOf("\t"));
                line = line.substring(line.indexOf("\t")+1).replaceAll("\"", "");
                
                String date = line.substring(0, line.indexOf("\t"));
                line = line.substring(line.indexOf("\t")+1).replaceAll("\"", "");
                
                int samples = Integer.parseInt(line.substring(0, line.indexOf("\t")));
                line = line.substring(line.indexOf("\t")+1).replaceAll("\"", "");
                
                String costString = line.substring(0, line.indexOf("\t"));
                int n = costString.indexOf("$");
                if(n<0) {
                    throw new SEQ_Exception("Column cost is wrong format: "+line);
                }
                double cost = Double.parseDouble(costString.substring(n+1).replaceAll(",", ""));
                line = line.substring(line.indexOf("\t")+1).replaceAll("\"", "");
                
                String paymentmethod = line.substring(0, line.indexOf("\t"));
                line = line.substring(line.indexOf("\t")+1).replaceAll("\"", "");
                
                String service = line.substring(0, line.indexOf("\t"));
                line = line.substring(line.indexOf("\t")+1).replaceAll("\"", "");
                
                String institution = line.substring(0, line.indexOf("\t"));
                line = line.substring(line.indexOf("\t")+1).replaceAll("\"", "");
                
                String affiliation = line.substring(0, line.indexOf("\t"));
                line = line.substring(line.indexOf("\t")+1).replaceAll("\"", "");
                
                String harvard = line;
                if("Harvard".equals(harvard.trim())) {
                    harvard = SEQ_Order.ISHARVARD_Y;
                } else {
                    harvard = SEQ_Order.ISHARVARD_N;
                }

                SEQ_Order order = new SEQ_Order(orderid, date, billingemail, billingaddress, pifirstname, 
                        pilastname, piemail, username, ponumber, samples, cost, paymentmethod, service, 
                        institution, affiliation, harvard, userid);
                orders.add(order);
            }
            
            in.close();
            return orders;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SEQ_Exception("Error reading file.");
        }
    }
}
