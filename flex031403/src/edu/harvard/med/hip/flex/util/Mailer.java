/*
 * File : Mailer.java
 * Classes : Mailer
 *
 * Description :
 *
 *      A convieniece class for sending e-mails.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 * 
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.3 $
 * $Date: 2001-06-22 19:12:37 $
 * $Author: dongmei_zuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 22, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-22-2001 : JMM - Class created. 
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/


package edu.harvard.med.hip.flex.util;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * Utility class to send simple messages. 
 *
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.3 $ $Date: 2001-06-22 19:12:37 $
 */

public class Mailer {
    
    public final static String SMTP_HOST ="hms.harvard.edu";
    
    
    /**
     * Utility method to send a message.
     *
     * @param to The address to send to.
     * @param from Email address of who is sending the message.
     * @param subject The subject of the message.
     * @param msgText The text of the message.
     */
    public static void sendMessage(String to, String from, String subject, 
    String msgText) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host",SMTP_HOST);
        Session session = Session.getDefaultInstance(props,null);
        
        
        
        try{
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            
            // create the message body part
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setText(msgText);
            
            // create the multipart and put the message into it.
            Multipart mp = new MimeMultipart();
            
            mp.addBodyPart(mbp);
            
            // add the multipart to the message
            msg.setContent(mp);
            
            // send the message
            Transport.send(msg);
        } catch(MessagingException mex) {
            mex.printStackTrace();
            Exception ex = null;
            if((ex = mex.getNextException()) !=null) {
                ex.printStackTrace();
            }
        }
    }
            
} // End class Mailer


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
