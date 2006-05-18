//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * File : Mailer.java
 * Classes : Mailer
 *
 * Description :
 *
 *      A convieniece class for sending e-mails.
 *
 *
 
 *
 * The following information is used by CVS
 * $Revision: 1.11 $
 * $Date: 2006-05-18 15:43:36 $
 * $Author: Elena $
 *
 ******************************************************************************
 
 */




package edu.harvard.med.hip.bec.util;

import java.io.*;
import java.util.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.database.*;

/**
 *
 * Utility class to send simple messages.
 *
 * @author     $Author: Elena $
 * @version    $Revision: 1.11 $ $Date: 2006-05-18 15:43:36 $
 */

public class Mailer
{
    
    public final static String SMTP_HOST ="gate.med.harvard.edu";
    
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
    public static void sendMessageWithFileCollection(String to, String from,
                        String cc, String subject, 
                        String msgText, Collection fileCol)
    throws MessagingException
    {
        Properties props = new Properties();
        props.put("mail.smtp.host",BecProperties.getInstance().getProperty("mail.smtp.host"));
        Session session = Session.getDefaultInstance(props,null);
        
        
        
        try
        {
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address =
            {new InternetAddress(to)};
            if(cc != null)
            {
                InternetAddress[] ccAddresses =
                {new InternetAddress(cc)};
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
            
            if(fileCol !=null )
            {
                // now attach all the files if there are any.
                Iterator fileIter = fileCol.iterator();
                while(fileIter.hasNext())
                {
                    BodyPart filePart = new MimeBodyPart();
                    File curFile = (File)fileIter.next();
                    DataSource source = new FileDataSource(curFile);
                    filePart.setDataHandler(new DataHandler(source));
                    filePart.setFileName(curFile.getName());
                    mp.addBodyPart(filePart);
                }
            }
            // add the multipart to the message
            msg.setContent(mp);
             // send the message
            Transport.send(msg);
        } catch(MessagingException mex)
        {
            mex.printStackTrace();
            Exception ex = null;
            if((ex = mex.getNextException()) !=null)
            {
                ex.printStackTrace();
            }
        }
    }
    
    
    public static void sendMessageWithAttachedFile(String to, String from,
                        String cc, String subject, String msgText, File fl)
                        throws MessagingException
    {
        ArrayList fileCol = new ArrayList();
        fileCol.add(fl);
        sendMessageWithFileCollection( to,  from,  cc,  subject,  msgText,  fileCol);
        
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
    String msgText) throws MessagingException
    {
         Mailer.sendMessage(to,from,null,subject,msgText, null, null);
    }
    
     public static void sendMessage(String to, String from, String cc, String subject,
                                            String msgText) throws MessagingException
    {
         Mailer.sendMessage(to,from,cc,subject,msgText, null, null);
    }
    
    public static void sendMessage(String to, String from, String cc, String subject,
                String msgText, ArrayList msgs, Collection fileCol) throws MessagingException
    {
        StringBuffer messages = new StringBuffer();
        if (msgs != null && msgs.size()> 0)
        {
             for (int ind = 0; ind < msgs.size(); ind++)
            {
                messages.append( "\n"+(String) msgs.get(ind));
            }
        }
          Mailer.sendMessageWithFileCollection( to,  from,cc,  subject,  msgText+messages, fileCol);
   }
    
     
     
     

    public static void main(String [] args) throws Exception
    {
        
      }
    
} // End class Mailer


