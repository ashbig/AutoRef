//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * SubmitDataFileAction.java
 *
 * Created on June 19, 2003, 1:01 PM
 */

package edu.harvard.med.hip.bec.action;

/**
 *
 * @author  htaycher
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.programs.blast.*;
import edu.harvard.med.hip.bec.export.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
//import edu.harvard.med.hip.bec.engine.*;
import edu.harvard.med.hip.bec.database.*;
import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.bioutil.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import  edu.harvard.med.hip.bec.modules.*;
import  edu.harvard.med.hip.bec.action_runners.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;



public class SubmitDataFileAction extends BecAction
{
    
    public ActionForward becPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException
    {
        ActionErrors errors = new ActionErrors();
        int forwardName = ((SubmitDataFileForm)form).getForwardName();
        FormFile requestFile = ((SubmitDataFileForm)form).getFileName();
      String title = "";
        //check if can get input stream
        InputStream input = null;Thread t = null;
        try
        {
            input = requestFile.getInputStream();
        } catch (FileNotFoundException ex)
        {
            errors.add("fileName", new ActionError("bec.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        } catch (IOException ex)
        {
            errors.add("fileName", new ActionError("bec.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
       
        try
        {
             User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
            switch (forwardName)
            {
                case Constants.PROCESS_RUN_DISCREPANCY_FINDER_STANDALONE:
                {
                    
                     boolean isSendNeedleFiles = false;
                     if ( request.getParameter("send_needle_results") == null)
                     {
                         isSendNeedleFiles = false;
                     }
                     else
                     {  isSendNeedleFiles = true;}
                    
                    RunDiscrepancyFinder df= new RunDiscrepancyFinder(input,isSendNeedleFiles, user.getUserEmail());
                    t = new Thread(df);
                    t.start();
                    title="The following process was initiated by user: <i>Run Discrepancy Finder as stand-alone application</i>.<P>The report will be send to user by e-mail.";
                    request.setAttribute(Constants.JSP_TITLE,title);
                     return (mapping.findForward("response"));
                }
                case Constants.PROCESS_SUBMIT_ASSEMBLED_SEQUENCE:
                {
                    SequenceDataUploadRunner sd= new SequenceDataUploadRunner(input, user);
                    t = new Thread(sd);
                    t.start();
                    title="The following process was initiated by user: <i>Submit sequence data</i>.<P>Report will be send to user by e-mail.";
                    request.setAttribute(Constants.JSP_TITLE,title);
                    return (mapping.findForward("response"));
                }
                case  Constants.PROCESS_DELETE_TRACE_FILES :
                case  Constants.PROCESS_MOVE_TRACE_FILES:
                {
                    
                    switch(forwardName)
                    {
                         case  Constants.PROCESS_GET_TRACE_FILE_NAMES :{title = "request for list of Trace Files' names"; break;}
                         case  Constants.PROCESS_DELETE_TRACE_FILES :{title = "request for Trace Files deletion"; break;}
                    }
                    ProcessRunner runner = new DeleteObjectRunner();
                    runner.setProcessType(forwardName);
                              
                    ArrayList items = getInputItems( input);
                    String  item_ids = Algorithms.convertStringArrayToString(items, " ");
                    runner.setInputData( Constants.ITEM_TYPE_PLATE_LABELS,item_ids);
                    runner.setUser(user);
                       runner.setProcessType(forwardName);
                 
                    t = new Thread(runner);                    t.start();
                    
                    request.setAttribute(Constants.JSP_TITLE,title);
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Processing items:<P>"+item_ids);
                    return mapping.findForward("processing");
                }
             
            }
        }
        catch (Exception e)
        {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
        return (mapping.findForward("error"));
    }
    
    
      ////////////////////
     private ArrayList getInputItems(InputStream input) throws Exception
     {
         BufferedReader reader = null;
         ArrayList items = new ArrayList();  String line = null;
         try
         {
                reader = new BufferedReader(new InputStreamReader(input));
                while  ((line = reader.readLine()) != null)
                {
                    items.add(line.trim().toUpperCase() );
                 }
                return items;
         }
         catch(Exception e)
         {
             throw new BecUtilException("Cannot read input file");
         }
     }
    
     class RunDiscrepancyFinder implements Runnable
    {
        private InputStream     i_Input = null;
        private boolean         i_isAttachNeedleFiles = false;
        private ArrayList       i_files = null;
        private String          i_email=null;
        private ArrayList       i_report = null;
        
        private static final String FILE_PATH ="/discrepancyfinder/";
        private static final String REPORT_NAME ="DiscrepancyFinderReport.txt";
        
        public RunDiscrepancyFinder(InputStream in, boolean isFiles ,String user_email)
        {
            i_Input = in;
            i_email = user_email;
            i_isAttachNeedleFiles = isFiles;
            i_report = new ArrayList();
        }
        
       
        
        public void run()
        {
           SequencePair pair = null;
           ArrayList alg_files = new ArrayList();
           int refseqid = -1;
            StringBuffer report = new StringBuffer();
           ArrayList files = new ArrayList();
           String refseq = null;BaseSequence  refsequence =null;
           BufferedReader reader = null;
           String cloneseq=null;AnalyzedScoredSequence clonesequence = null;String line = null;
            try
                {

                    // NeedleResult res = new NeedleResult();
                    ArrayList pairs = new ArrayList();

                    reader = new BufferedReader(new InputStreamReader(i_Input));
                    report.append("<html><body>");
                    while((line = reader.readLine()) != null)
                    {
                        if (line.trim().length() < 1) break;
                        StringTokenizer st = new StringTokenizer(line, "\t");
                        String [] info = new String[3];
                        int i = 0;

                        while(st.hasMoreTokens())
                        {
                            info[i] = st.nextToken();
                            i++;
                        }
                        cloneseq = info[2];
                        refseq=info[1];
                       // refseq = refseq.substring(0, refseq.length()-3);
                        refseqid = Integer.parseInt(info[0]);
                        clonesequence = new AnalyzedScoredSequence(cloneseq,   refseqid);
                        clonesequence.setId(-refseqid);
                        refsequence = new BaseSequence(refseq,BaseSequence.BASE_SEQUENCE); 
                        refsequence.setId(refseqid);
                        pair = new SequencePair(clonesequence ,  refsequence );
                        pairs.add(pair);
                         DiscrepancyFinder d =new DiscrepancyFinder(pair);
                       
                         d.run();
                          alg_files.addAll( d.getAligmentFileNames() );
                        //write down mutations
                          //write down mutations
                          report.append("<hr><h2>Sequence id: "+refseqid +"</h2>");
                         if ( clonesequence.getDiscrepancies() == null || 
                                clonesequence.getDiscrepancies().size() == 0)
                         {
                            report.append("<h3> No discrepancies have been detected </h3>");
                         }
                         else
                         {
                             report.append(Mutation.toHTMLString( clonesequence.getDiscrepancies()));
                         }
                    }
                    reader.close();
                       //write report file
                    File reportFile = new File( Constants.getTemporaryFilesPath()+"report"+System.currentTimeMillis()+".html");
                    FileWriter fr = new FileWriter(reportFile);
                    fr.write(report.toString());
                    fr.close();
                    files.add( reportFile);
                    //get alginment files if needed
                    if ( i_isAttachNeedleFiles)
                    {
                      
                        
                        for (int inf = 0; inf < alg_files.size(); inf++)
                        {
                            try{
                                                   files.add(new File((String) alg_files.get(inf)));
                            }catch(Exception ee){}
                        }
                    }
                    //send report  
                    String msgText = "Please find Discrepancy Finder Report enclosed.";
                   
                    Mailer.sendMessageWithFileCollection(i_email,  BecProperties.getInstance().getACEEmailAddress(),
                    BecProperties.getInstance().getProperty("ACE_CC_EMAIL_ADDRESS"), "Discrepancy Finder Report.",  msgText, files);
                 
            }
            catch(Exception e){if (reader != null) try{reader.close();}catch(Exception i){}}
        }
     }
     
     
   
}
