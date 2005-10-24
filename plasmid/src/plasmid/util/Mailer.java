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
 * $Revision: 1.1.1.1 $
 * $Date: 2005-10-24 15:12:51 $
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


package plasmid.util;

import java.io.*;
import java.util.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * Utility class to send simple messages.
 *
 * @author     $Author: dzuo $
 * @version    $Revision: 1.1.1.1 $ $Date: 2005-10-24 15:12:51 $
 */

public class Mailer
{
    //public static final String FILEPATH = FlexProperties.getInstance().getProperty("tmp");
    //public final static String SMTP_HOST ="hms.harvard.edu";
    public final static String SMTP_HOST ="gate.med.harvard.edu";
    public static final String FROM = "dongmei_zuo@hms.harvard.edu";
    
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
     * @param ccs Email addresses to send a carbon copy
     * @param subject The subject of the message.
     * @param msgText The text of the message.
     * @param fileCol Collection of file objects.
     */
    public static void sendMessages(String to, String from,
    List ccs, String subject, String msgText, Collection fileCol)
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
            if(ccs != null)
            {
                InternetAddress[] ccAddresses = new InternetAddress[ccs.size()];
                for(int i=0; i<ccs.size(); i++) {
                    String cc = (String)ccs.get(i);
                    ccAddresses[i] = new InternetAddress(cc);
                }
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
    

    public static void main(String [] args) throws Exception
    {
        
        Vector messages = new Vector();
        messages.add("lll\n");messages.add("lll\n");messages.add("lll\n");messages.add("lll\n");messages.add("lll\n");

        Collection fileCol = new LinkedList();
        fileCol.add(new File("/NETLOG.TXT"));
        fileCol.add(new File("/j0272560(t).gif"));
        Mailer.sendMessage("jmunoz@3rdmill.com","jmunoz@3rdmill.com",
        "jmunoz@3rdmill.com","Test","Test of cc and file", fileCol);
        
        Mailer.sendMessage("jmunoz@3rdmill.com","dongmei_zuo@hms.harvard.edu", "test","Testing 12 3");
    }
    
} // End class Mailer


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
