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
 * $Revision: 1.1 $
 * $Date: 2001-07-06 19:28:57 $
 * $Author: jmunoz $
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

import java.io.*;
import java.util.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;


/**
 *
 * Utility class to send simple messages.
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.1 $ $Date: 2001-07-06 19:28:57 $
 */

public class Mailer {
    
    public final static String SMTP_HOST ="hms.harvard.edu";
    
    /**
     * Utility Method to send a message
     *
     * @param to The address to send to.
     * @param from Email address of who is sending the message.
     * @param cc Email address to send a carbon copy
     * @param subject The subject of the message.
     * @param msgText The text of the message.
     * @param fileCol Collection of file objects.
     */
    public static void sendMessage(String to, String from,
    String cc, String subject, String msgText, Collection fileCol)
    throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host",SMTP_HOST);
        Session session = Session.getDefaultInstance(props,null);
        
        
        
        try{
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            if(cc != null) {
                InternetAddress[] ccAddresses = {new InternetAddress(cc)};
                msg.setRecipients(Message.RecipientType.CC, ccAddresses);
            }
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            
            // create the message body part
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setText(msgText);
            
            
            
            // create the multipart and put the message into it.
            Multipart mp = new MimeMultipart();
            
            mp.addBodyPart(mbp);
            
            if(fileCol !=null ) {
                // now attach all the files if there are any.
                Iterator fileIter = fileCol.iterator();
                while(fileIter.hasNext()) {
                    BodyPart filePart = new MimeBodyPart();
                    File curFile = (File)fileIter.next();
                    DataSource source = new FileDataSource(curFile);
                    filePart.setDataHandler(new DataHandler(source));
                    filePart.setFileName(curFile.getAbsolutePath());
                    
                    mp.addBodyPart(filePart);
                }
            }
            
            
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
        Mailer.sendMessage(to,from,null,subject,msgText, null);
    }
    
    public static void main(String [] args) throws Exception {
        Collection fileCol = new LinkedList();
        fileCol.add(new File("/NETLOG.TXT"));
        fileCol.add(new File("/j0272560(t).gif"));
        Mailer.sendMessage("jmunoz@3rdmill.com","jmunoz@3rdmill.com",
        "jmunoz@3rdmill.com","Test","Test of cc and file", fileCol);
        
        Mailer.sendMessage("jmunoz@3rdmill.com","dzuo@hms.harvard.edu", "test","Testing 12 3");
    }
    
} // End class Mailer


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
