/*
 * ExportSeqSampleInfoAction.java
 *
 * Created on March 23, 2003, 9:39 PM
 */

package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.export.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.query.*;
import edu.harvard.med.hip.flex.Constants;
/**
 *
 * @author  hweng
 */
public class ExportSeqSampleInfoAction extends CollaboratorAction{
    
    public ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        ActionErrors errors = new ActionErrors();       
        response.setContentType("application/x-msexcel");
        response.setHeader("Content-Disposition", "attachment;filename=SequenceInfo_flexquery.xls"); 

        
        ProjectSuccessRate project_success_rate = (ProjectSuccessRate)(request.getSession().getAttribute("project_success_rate"));
        int projectid = project_success_rate.getProjectid();
        int workflowid = project_success_rate.getWorkflowid();
        String construct_type = project_success_rate.getCloneFormat();

        Vector success_rates = project_success_rate.getSuccess_rates();
        String sample_type = ((ExportSeqSampleInfoForm)form).getSample_type().trim();
        String sample_status = ((ExportSeqSampleInfoForm)form).getSample_status().trim();
                        
        SuccessRate sr = null;
        if(sample_type.equalsIgnoreCase("INIT")){
            sr = (SuccessRate)success_rates.elementAt(success_rates.size()-1);
        }
        else if(sample_type.equalsIgnoreCase("GEL")){            
            sr = (SuccessRate)success_rates.elementAt(success_rates.size()-2);
        }
        else if (sample_type.equalsIgnoreCase("AGAR")){
            sr = (SuccessRate)success_rates.elementAt(success_rates.size()-3);
        }
        else if (sample_type.equalsIgnoreCase("ISOLATE")){
            sr = (SuccessRate)success_rates.elementAt(success_rates.size()-4);
        }
        
        request.setAttribute("sample_type", sample_type);       // setAttribute, important
        String _sampleType = sample_type;
        
        Vector seqids = new Vector();
        if(sample_status.equalsIgnoreCase("input"))
            seqids = sr.getTotal_samples();
        else if(sample_status.equalsIgnoreCase("success"))
            seqids = sr.getSuccess_samples();
        else if(sample_status.equalsIgnoreCase("fail"))
            seqids = sr.getFailed_samples();
        // if sample status in current step is 'notdone', then the sample information
        // in the previous step will be displayed        
        else if(sample_status.equalsIgnoreCase("notdone")){
            seqids = sr.getNotDone_samples();
            if(sample_type.equalsIgnoreCase("ISOLATE"))
                sample_type = "AGAR";
            else if(sample_type.equalsIgnoreCase("AGAR")){
                if(workflowid == 4 || workflowid == 11){    // previous step is 'INIT'
                    sample_type = "INIT";
                }
                else{   // previous step is 'GEL'
                    sample_type = "GEL";
                }                                                            
            }                
            else if(sample_type.equalsIgnoreCase("GEL"))
                sample_type = "INIT";
        }
        //
        
                
        int[] sequence_ids = new int[seqids.size()];
        for(int i = 0; i < seqids.size(); i++)
            sequence_ids[i] = ((Integer)(seqids.elementAt(i))).intValue();
        

        FlexGeneMonitor m = new FlexGeneMonitor();
        TreeSet seqSampleInfo = m.getSampleInfoBySeqids(sequence_ids, projectid, workflowid, construct_type, sample_type); 
        
        
        try {
            
            PrintWriter out = response.getWriter();
            SeqSampleInfoExporter exporter = new SeqSampleInfoExporter();
            exporter.doExport(seqSampleInfo, sample_type, out);
            out.print("\n\n");
            out.print("Project name: " + project_success_rate.getProjectname() + "\n");
            out.print("Workflow name: " + project_success_rate.getWorkflowname() + "\n");
            out.print("Clone format: " + project_success_rate.getCloneFormat() + "\n");
            if(((String)(request.getSession().getAttribute("init_date_from"))).equalsIgnoreCase(""))
                out.print("Oligo received date for Sequences:   any time \n"); 
            else
                out.print("Oligo received date for Sequences:   From " + request.getSession().getAttribute("init_date_from") +
                        "  To " + request.getSession().getAttribute("init_date_to") + "\n");
            out.print("\n");
            
            if(_sampleType.equalsIgnoreCase("GEL") || _sampleType.equalsIgnoreCase("AGAR") || _sampleType.equalsIgnoreCase("ISOLATE")){
                if(!(workflowid == 4 || workflowid == 11))
                {
                out.print("Sucess criteria for the step of PCR Gel:    ");
                Vector pcr_criteria = (Vector)(request.getSession().getAttribute("pcr_succ_criteria"));
                for(int i = 0; i < pcr_criteria.size(); i++){
                    out.print(pcr_criteria.elementAt(i) + ",  ");
                }
                out.println();
                }
            }
            if(_sampleType.equalsIgnoreCase("AGAR") || _sampleType.equalsIgnoreCase("ISOLATE")){
                out.println("Sucess criteria for the step of Agar Plate:    " + 
                ((Integer)(request.getSession().getAttribute("agar_plate_succ_criteria"))).intValue() );
            }
            if(_sampleType.equalsIgnoreCase("ISOLATE")){
                out.println("Sucess criteria for the step of Culture Plate:    " + 
                ((Integer)(request.getSession().getAttribute("culture_plate_succ_criteria"))).intValue() );
            }            

            out.print("\n");
            out.println("Sample status:  " + sample_status);
            out.println("Sample type:  " + _sampleType);            
            
            out.close();
        } catch (IOException ex) {
            return mapping.findForward("fail");
        }
        
        return null;
    }
        
        
    
}
