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
 * $Revision: 1.11 $
 * $Date: 2003-10-20 18:19:36 $
 * $Author: dzuo $
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
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * Utility class to send simple messages.
 *
 * @author     $Author: dzuo $
 * @version    $Revision: 1.11 $ $Date: 2003-10-20 18:19:36 $
 */

public class Mailer
{
    
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
    throws MessagingException
    {
        Properties props = new Properties();
        props.put("mail.smtp.host",SMTP_HOST);
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
                    filePart.setFileName(curFile.getAbsolutePath());
                    
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
    
    /**
     * Utility Method to send a message
     *
     * @param to The address to send to.
     * @param from Email address of who is sending the message.
     * @param cc Email address to send a carbon copy
     * @param subject The subject of the message.
     * @param msgText The text of the message.
     * @param file_name of file object.
     */
    public static void sendMessageWithAttachedFile(String user_name, String subject,
                                                    String msgText, File fl) 
                                                    throws MessagingException,
                                                    FlexDatabaseException
    {
        AccessManager am = AccessManager.getInstance();
        String to = am.getEmail( user_name );
        String cc = "etaycher@hms.harvard.edu";
        String from = "etaycher@hms.harvard.edu";
        sendMessageWithAttachedFile( to,  from, cc, subject,  msgText,  fl);
    }
    
    
    public static void sendMessageWithAttachedFile(String to, String from,
    String cc, String subject, String msgText, File fl)
    throws MessagingException
    {
        Properties props = new Properties();
        props.put("mail.smtp.host",SMTP_HOST);
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
            
            if(fl !=null )
            {
                // now attach  file if there are any.
                
                BodyPart filePart = new MimeBodyPart();
                
                DataSource source = new FileDataSource(fl);
                filePart.setDataHandler(new DataHandler(source));
                filePart.setFileName(fl.getAbsolutePath());
                
                mp.addBodyPart(filePart);
                
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
        Mailer.sendMessage(to,from,null,subject,msgText, null);
    }
    
     public static void sendMessage(String to, String from, String cc, String subject,
    String msgText) throws MessagingException
    {
        Mailer.sendMessage(to,from,cc,subject,msgText, null);
    }
    
     
     
     //send e-mail to the user with all GI separated to three groups
public static void notifyUser(String user_name, String file_name, String subject, String msg, Vector messages) throws Exception
    {
        if (messages.size()==0) return;
        AccessManager am = AccessManager.getInstance();
        String to = am.getEmail( user_name );
        String cc = "etaycher@hms.harvard.edu";
        String from = "etaycher@hms.harvard.edu";
        File fl = writeFile(messages, file_name)  ;
        Mailer.sendMessageWithAttachedFile( to,  from, cc, subject, msg, fl);
    }


private static File writeFile(Vector fileData, String file_name)
throws IOException
{
    File fl = new File( "/tmp/"  + file_name);
    //File fl = new File( "G:\\"  + file_name);
    FileWriter fr = new FileWriter(fl);
    
    for (int count = 0; count < fileData.size(); count++)
    {
        fr.write((String)fileData.get(count));
    }
    fr.flush();
    fr.close();
 
    return fl;
}




    public static void main(String [] args) throws Exception
    {
        
        Vector messages = new Vector();
        messages.add("lll\n");messages.add("lll\n");messages.add("lll\n");messages.add("lll\n");messages.add("lll\n");
            
        notifyUser("htaycher", "report.txt", "test","test",  messages);
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
