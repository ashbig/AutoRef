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
import java.io.*;

import java.sql.*;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.programs.blast.*;
import edu.harvard.med.hip.bec.export.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
//import edu.harvard.med.hip.bec.engine.*;
import edu.harvard.med.hip.bec.database.*;
import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.bioutil.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import  edu.harvard.med.hip.bec.modules.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;



public class SubmitDataFileAction extends ResearcherAction
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
     
        //check if can get input stream
        InputStream input = null;
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
                    Thread t = new Thread(df);
                    t.start();
                   
                  
                    String str="The following process was initiated by user: <i>Run Discrepancy Finder as stand-alone application</i>.<P>The report will be send to user by e-mail.";
                    request.setAttribute(Constants.JSP_TITLE,str);
                     return (mapping.findForward("response"));
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
        return (mapping.findForward("error"));
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
           String refseq = null;BaseSequence  refsequence =null;
           String cloneseq=null;AnalyzedScoredSequence clonesequence = null;String line = null;
            try
                {

                    // NeedleResult res = new NeedleResult();
                    ArrayList pairs = new ArrayList();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(i_Input));

                    while((line = reader.readLine()) != null)
                    {
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
                         i_report.add("Sequence id: "+refseqid +"\n");
                         if ( clonesequence.getDiscrepancies() == null || 
                                clonesequence.getDiscrepancies().size() == 0)
                         {
                             i_report.add("\t\t No discrepancies have been detected\n\n");
                         }
                         else
                         {
                             int discrepancy_number = 1;Mutation discr=null;
                             for (int count = 0; count < clonesequence.getDiscrepancies().size(); count++)
                             {
                                 discr = (Mutation) clonesequence.getDiscrepancies().get(count);
                                 if ( discrepancy_number != discr.getNumber())
                                 {
                                    i_report.add("\n\t\t New Discrepancy ");
                                    discrepancy_number = discr.getNumber();
                                 }
                                 i_report.add( discr.toString() );
                               
                             }
                         }
                                    
                    }
                    reader.close();
                    //get alginment files if needed
                    if ( i_isAttachNeedleFiles)
                    {
                      
                        ArrayList files = new ArrayList();
                        for (int inf = 0; inf < alg_files.size(); inf++)
                        {
                            try{
                            System.out.print("semding files"+(String) alg_files.get(inf));
                            files.add(new File((String) alg_files.get(inf)));
                            }catch(Exception ee){}
                        }
                        System.out.print("semding files"+files.size());
                        String msgText = "Discrepancy Finder Report.";
                        for (int ind = 0; ind < i_report.size(); ind++)
                        {
                            msgText += "\n"+(String) i_report.get(ind);
                        }
                        Mailer.sendMessageWithFileCollections(i_email,  "elena_taycher@hms.harvard.edu",
     "elena_taycher@hms.harvard.edu", "Discrepancy Finder Report.",  msgText, files);
                        System.out.print("semding files"+files.size());
                    }
                    else
                    {
                        Mailer.sendMessage(i_email, "elena_taycher@hms.harvard.edu",
                            "elena_taycher@hms.harvard.edu", "Discrepancy Finder Report.", "Discrepancy Finder Report" ,i_report);
                    }
            }
            catch(Exception e){}
        }
     }
}
