/*
 * ReportRunner.java
 *
 * Created on October 20, 2003, 4:44 PM
 */

package edu.harvard.med.hip.bec.action_runners;
import java.sql.*;
import java.io.*;


import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.assembler.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
  import java.util.*;
  import edu.harvard.med.hip.utility.*;
   
/**
 *
 * @author  HTaycher
 */
public class ReportRunner extends ProcessRunner 
{
    
    private  static String FILE_PATH = null;
    {
        if (ApplicationHostDeclaration.IS_BIGHEAD)
            FILE_PATH = "d:\\tmp\\";
        else
            FILE_PATH = "c:\\tmp\\";
    }
    
    
    // private ArrayList   m_error_messages = null;
   // private String      m_items = null;
   // private int         m_items_type = -1;
   // private User        m_user = null;
    private boolean    m_clone_id= false; //    Clone Id
	private boolean    m_dir_name= false; // Directory Name
	private boolean    m_sample_id= false; //      Sample Id
	private boolean    m_plate_label= false; //      Plate Label
	private boolean    m_sample_type= false; //      Sample Type
	private boolean    m_position= false; //      Sample Position
	private boolean    m_ref_sequence_id= false; //      Sequence ID
	private boolean    m_clone_seq_id= false; //      Clone Sequence Id
	private boolean    m_ref_cds_start= false; //      CDS Start
	private boolean    m_clone_status= false;//      Clone Sequence Analysis Status
	private boolean    m_ref_cds_stop= false; //      CDS Stop
	private boolean    m_clone_discr_high= false; //    Discrepancies High Quality (separated by type)
	private boolean    m_ref_cds_length= false; //      CDS Length
	private boolean    m_clone_disc_low= false; //   Discrepancies Low Quality (separated by type)
	private boolean    m_ref_gc= false; //     GC Content
	private boolean    m_ref_seq_text= false; //      Sequence Text
	private boolean    m_ref_cds= false; //     CDS
	private boolean    m_ref_gi= false; //      GI Number
	private boolean    m_ref_gene_symbol= false; //      Gene Symbol
	private boolean    m_ref_panum= false; //      PA Number (for Pseudomonas project only)
	private boolean    m_ref_sga= false; //      SGA Number (for Yeast project only)
	private boolean    m_rank = false;
        private boolean    m_read_length = false;//length of end reads high quality strip
        
        
        private String      m_report_title = "";
    /** Creates a new instance of ReportRunner */
    public ReportRunner()
    {
         m_error_messages = new ArrayList();
    }


   // public  void        setUser(User v){m_user=v;}
   // public  void        setItems(String item_ids)
   // {
   //     m_items = item_ids;
   // }
   //  public  void        setItemsType( int type)
    // {
    //     m_items_type = type;
   //  }
                // value="2"//Clone Ids</strong//
     public  void        setFields(
                    Object clone_id, //    Clone Id
                    Object dir_name, // Directory Name
                    Object sample_id, //      Sample Id
                    Object plate_label, //      Plate Label
                    Object sample_type, //      Sample Type
                    Object position, //      Sample Position
                    Object ref_sequence_id, //      Sequence ID
                    Object clone_seq_id, //      Clone Sequence Id
                    Object ref_cds_start, //      CDS Start
                    Object clone_status,//      Clone Sequence Analysis Status
                    Object ref_cds_stop, //      CDS Stop
                    Object clone_discr_high, //    Discrepancies High Quality (separated by type)
                    Object ref_cds_length, //      CDS Length
                    Object clone_disc_low, //   Discrepancies Low Quality (separated by type)
                    Object ref_gc, //     GC Content
                    Object ref_seq_text, //      Sequence Text
                    Object ref_cds, //     CDS
                    Object ref_gi, //      GI Number
                    Object ref_gene_symbol, //      Gene Symbol
                    Object ref_panum, //      PA Number (for Pseudomonas project only)
                    Object ref_sga, //      SGA Number (for Yeast project only)
                    Object rank ,//      Leave Sequence Info Empty for Empty Well
                    Object read_length
                 )
     {
        if( clone_id!= null){ m_clone_id= true; m_report_title += "Clone ID\t";} //    Clone Id
        if( plate_label != null) {m_plate_label= true;  m_report_title += "Plate Label\t";}// Plate Label
        if( sample_id != null) {m_sample_id= true;  m_report_title += "Sample ID\t";}//      Sample Id
        if( sample_type != null){ m_sample_type= true; m_report_title += "Sample Type\t";} //      Sample Type
        if( position != null) {m_position= true;  m_report_title += "Position\t";}//      Sample Position
        if ( rank!= null) {m_rank= true;  m_report_title += "Clone Rank\t";}//
        if( dir_name != null) {m_dir_name = true;  m_report_title += "Clone Directory Name\t";}// Directory Name
        if ( read_length != null) {m_read_length= true;  m_report_title += "End reads length (Forward/Reverse)\t";}
      
        if( ref_sequence_id != null){ m_ref_sequence_id= true;  m_report_title += "RefSequence ID\t";}//      Sequence ID
        if( ref_cds_start != null) {m_ref_cds_start= true; m_report_title += "REF:CDS Start\t";} //      CDS Start
        if( ref_cds_stop != null) {m_ref_cds_stop= true; m_report_title += "REF:CDS Stop\t";}//      CDS Stop
        if( ref_cds_length != null){ m_ref_cds_length= true; m_report_title += "REF:CDS Length\t";}//      CDS Length
        if( ref_gi != null) {m_ref_gi= true; m_report_title += "REF:GI\t";}//      GI Number
        if( ref_gene_symbol != null){ m_ref_gene_symbol= true;m_report_title += "REF:Gene Synbol\t";} //      Gene Symbol
        if( ref_panum != null) {m_ref_panum= true; m_report_title += "REF:PA Number\t";}//      PA Number (for Pseudomonas project only)
        if( ref_sga != null) {m_ref_sga= true;m_report_title += "REF:SGA Number\t";} //      SGA Number (for Yeast project only)
        if( ref_gc != null) {m_ref_gc= true; m_report_title += "REF:GC\t";}//     GC Content
        if( ref_seq_text != null) {m_ref_seq_text= true; m_report_title += "REF:Text\t";}//      Sequence Text
        if( ref_cds != null) {m_ref_cds= true; m_report_title += "REF:CDS \t";}//     CDS
       
        if( clone_seq_id != null){ m_clone_seq_id= true; m_report_title += "Clone:Sequence Id\tCLone:Sequence Type\t";}//      Clone Sequence Id
        if( clone_status != null) {m_clone_status = true;m_report_title += "Clone: Status\t";}//      Clone Sequence Analysis Status
        if( clone_discr_high != null) m_clone_discr_high= true; //    Discrepancies High Quality (separated by type)
        if( clone_disc_low != null) m_clone_disc_low= true; //   Discrepancies Low Quality (separated by type)
       
     }

    public void run()
    {
        ArrayList file_list = new ArrayList();
        ArrayList items = new ArrayList();
        File report = null;
        Hashtable refsequences = new Hashtable();//containes refsequences by bec id
        Hashtable reads = new Hashtable();//containes reads by isolatetrackingid
        try
        {
            //convert item into array
           items = Algorithms.splitString( m_items);
           if ( m_items_type != 1 && m_items_type != 2 && m_items_type != 4)//clones
           {
               m_error_messages.add("No item type has been specified.");
               return;
           }
          ArrayList clones = getCloneInfo(m_items_type,items);
          refsequences= extractRefSequences( clones);
           if ( m_read_length )
           {
                reads = extractReads(clones);
                
           }
          report = printReport(clones,refsequences,reads);
          file_list.add(report);
               
        }
        catch(Exception ex)
        {
            m_error_messages.add(ex.getMessage());

        }
        finally
        {
            try
            {
     //send errors
                if (m_error_messages.size()>0)
                {
                     Mailer.sendMessage(m_user.getUserEmail(), "elena_taycher@hms.harvard.edu",
                    "elena_taycher@hms.harvard.edu", "Request for report generetion: error messages.", "Errors\n " ,m_error_messages);

                }
                if (file_list != null && file_list.size()>0)
                {
                    Mailer.sendMessageWithFileCollections(m_user.getUserEmail(), "elena_taycher@hms.harvard.edu",
                    "elena_taycher@hms.harvard.edu", "Request for report generator", 
                    "Please find attached report files for your request\n Requested item ids:\n"+m_items,
                    file_list);
                }
  
            }
            catch(Exception e){}
          
        }
    }
    
    
    private ArrayList getCloneInfo(int submission_type, ArrayList items)
    {
        ArrayList clones = new ArrayList();
        String sql = null;
        UICloneSample clone = null;
        ResultSet rs = null;
        sql = constructQueryString(submission_type,items);
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                 clone = new UICloneSample();
                 clone.setPlateLabel (rs.getString("LABEL"));
                 clone.setPosition (rs.getInt("POSITION"));
                 clone.setSampleType (rs.getString("SAMPLETYPE"));
                 clone.setCloneId (rs.getInt("CLONEID"));
                 clone.setCloneStatus (rs.getInt("STATUS"));
                 clone.setSequenceId (rs.getInt("CLONESEQUENCEID"));
                 clone.setSequenceAnalisysStatus (rs.getInt("analysisSTATUS"));
                 clone.setSequenceType (rs.getInt("SEQUENCETYPE"));
                 clone.setConstructId (rs.getInt("CONSTRUCTID"));
                 clone.setIsolateTrackingId (rs.getInt("ISOLATETRACKINGID"));
                 clone.setRank(rs.getInt("RANK"));
                 clone.setSampleId(rs.getInt("SAMPLEID"));
                 clone.setRefSequenceId(rs.getInt("REFSEQUENCEID"));
                 clones.add(clone);
            }
           
            
        }
        catch(Exception e)
        {
            m_error_messages.add(e.getMessage());
        }
       return clones;
    }
    
    
    private String constructQueryString(int submission_type, ArrayList items)
    {
        String sql = null;
        if ( submission_type == 1)//plates
        {
            StringBuffer plate_names = new StringBuffer();
            for (int index = 0; index < items.size(); index++)
            {
                plate_names.append( "'");
                plate_names.append((String)items.get(index));
                plate_names.append("'");
                if (index == 3) break;
                if ( index != items.size()-1 ) plate_names.append(",");
            }
            sql="select LABEL, POSITION,  SAMPLETYPE, s.SAMPLEID as SAMPLEID,flexcloneid  as CLONEID, "
 +" i.STATUS,  a.SEQUENCEID as CLONESEQUENCEID,  analysisSTATUS,  SEQUENCETYPE, refsequenceid , "
+" i.CONSTRUCTID,  i.ISOLATETRACKINGID, RANK "
+"  from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a "
+" where f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid and  "
+" s.containerid=c.containerid and a.isolatetrackingid =i.isolatetrackingid "
+"and s.containerid in (select containerid from containerheader where label in ("
+plate_names.toString()+")) order by s.containerid,position";
        } 
        else if (submission_type == 2)
        {
            sql="select LABEL, POSITION,  SAMPLETYPE, s.SAMPLEID as SAMPLEID,flexcloneid  as CLONEID, "
            +" i.STATUS,  a.SEQUENCEID as CLONESEQUENCEID,  analysisSTATUS,  SEQUENCETYPE, refsequenceid , "
            +" i.CONSTRUCTID,  i.ISOLATETRACKINGID, RANK "
            +"  from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a "
            +" where rownum<300 and flexcloneid in ("+Algorithms.convertStringArrayToString(items,"," )+")and f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid and  "
            +" s.containerid=c.containerid and a.isolatetrackingid =i.isolatetrackingid ";
        }
       
        else if (submission_type == 4)//bec sequence id
        {
            sql="select LABEL, POSITION,  SAMPLETYPE, s.SAMPLEID as SAMPLEID,flexcloneid  as CLONEID, "
            +" i.STATUS,  a.SEQUENCEID as CLONESEQUENCEID,  analysisSTATUS,  SEQUENCETYPE, refsequenceid , "
            +" i.CONSTRUCTID,  i.ISOLATETRACKINGID, RANK "
            +"  from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a "
            +" where rownum<300 and a.SEQUENCEID in ("+Algorithms.convertStringArrayToString(items,"," )+")and f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid and  "
            +" s.containerid=c.containerid and a.isolatetrackingid =i.isolatetrackingid ";
        }
        return sql;
    }
    private Hashtable extractRefSequences(ArrayList clones) 
    {
        Hashtable refsequences = new Hashtable();
        UICloneSample clone = null;
        RefSequence refsequence = null;
        for (int index = 0; index <clones.size();index++)
        {
            clone= (UICloneSample)clones.get(index);
            if (! refsequences.containsKey(new Integer(clone.getRefSequenceId())))
            {
                try
                {
                    refsequence = new RefSequence(clone.getRefSequenceId());
                    refsequences.put(new Integer(clone.getRefSequenceId()),refsequence);
                }
                catch(Exception e)
                {
                    m_error_messages.add(e.getMessage());
                }
            }
        }
        return refsequences;
        
    }
    
    
    
    private Hashtable extractReads(ArrayList clones) 
    {
        Hashtable reads = new Hashtable();
        ArrayList samplereads = new ArrayList();
        UICloneSample clone = null;
        UIRead read = null;
        StringBuffer isolatetrackingids = new StringBuffer();
        for (int index = 0; index <clones.size();index++)
        {
            clone= (UICloneSample)clones.get(index);
            isolatetrackingids.append( clone.getIsolateTrackingId() );
            if ( index != clones.size()-1 ) isolatetrackingids.append(",");
        }
        String sql = "select readid, isolatetrackingid, READSEQUENCEID,READTYPE,TRIMMEDSTART,TRIMMEDEND from readinfo where isolatetrackingid in ("+isolatetrackingids.toString()+") order by isolatetrackingid";
        
         ResultSet rs = null;
         int istr_id = -1;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                 read = new UIRead();
                 read.setId (rs.getInt("READID")); 
                read.setSequenceId (rs.getInt("READSEQUENCEID")); 
                read.setType (rs.getInt("READTYPE")); 
                read.setTrimStart (rs.getInt("TRIMMEDSTART")); 
                read.setTrimStop(rs.getInt("TRIMMEDEND")); 
                if ( istr_id!= rs.getInt("isolatetrackingid"))
                {
                    istr_id = rs.getInt("isolatetrackingid");
                    samplereads = new ArrayList();
                }
                
                
                samplereads.add(read);
                reads.put(new Integer(istr_id), samplereads);
            }
            
        }
        catch(Exception e)
        {
            m_error_messages.add(e.getMessage());
        }
        return reads;    
        
        
    }
    
    
     
     
  private File  printReport(ArrayList clones,Hashtable refsequences,Hashtable reads)
  {
        File fl = null;
        String temp = null;
        FileWriter fr = null;
        UICloneSample clone = null;
        try
        {
            for (int count = 0; count < clones.size(); count++)
            {
                clone= (UICloneSample) clones.get(count);
                if (count == 0)
                {
                    fl =   new File(FILE_PATH + System.currentTimeMillis()+ ".txt");
                    fr =  new FileWriter(fl);
                    fr.write(m_report_title+"\n");
                }

                fr.write(writeClone(clone,refsequences,reads)+"\n");

            }
            fr.flush();
            fr.close();
            return fl;
        }
        catch(Exception e){ try { fr.close();}catch(Exception n){} }
        return null;
    
  }
  private String writeClone(UICloneSample clone,Hashtable refsequences,Hashtable reads )
  {
      StringBuffer cloneinfo= new StringBuffer();
      CloneSequence clone_sequence = null;
      UIRead read = null;
      RefSequence refsequence = null;
       ArrayList discrepancies =  null;
      if ( clone.getRefSequenceId()>0)
      {
         refsequence = (RefSequence)refsequences.get(new Integer(clone.getRefSequenceId()));
      }
      
      
     if(  m_clone_id){ cloneinfo.append(clone.getCloneId()+"\t");}//    Clone Id
    if( m_plate_label){ cloneinfo.append( clone.getPlateLabel ()+"\t");}// Plate Label
    if( m_sample_id){ cloneinfo.append(clone.getSampleId() +"\t");}//      Sample Id
    if(  m_sample_type){ cloneinfo.append(clone.getSampleType ()+"\t");} //      Sample Type
    if( m_position){ cloneinfo.append(clone.getPosition ()+"\t");}//      Sample Position
    if( m_rank){ cloneinfo.append(clone.getRank ()+"\t");}
      //get directories
    if( m_dir_name)
    { 
        EndReadsWrapperRunner er = new EndReadsWrapperRunner();
        cloneinfo.append(er.getControlSamplesDir()+ File.separator + refsequence.getId()+File.separator+clone.getCloneId()+"\t");
    }// Directory Name
    if ( m_read_length )
    {
        ArrayList samplereads = (ArrayList)reads.get(new Integer(clone.getIsolateTrackingId()));
        for (int index = 0; index < samplereads.size(); index++)
        {
            read = (UIRead) samplereads.get(index);
            cloneinfo.append(Read.getTypeAsString(read.getType())+"_"+ (read.getTrimStop() - read.getTrimStart () )+"/"); 
                
        }
        cloneinfo.append("\t");
    }
    if (refsequence != null)
    {
        if(  m_ref_sequence_id){ cloneinfo.append( refsequence.getId() +"\t");}//      Sequence ID
        if( m_ref_cds_start){ cloneinfo.append( refsequence.getCdsStart()+"\t" );} //      CDS Start
        if( m_ref_cds_stop){ cloneinfo.append( refsequence.getCdsStop() +"\t");}//      CDS Stop
        if( m_ref_cds_length){ cloneinfo.append( ( refsequence.getCdsStop() - refsequence.getCdsStart())+"\t");}//      CDS Length
        if( m_ref_gi){ cloneinfo.append(refsequence.getGi()+"\t");}//      GI Number
        if(  m_ref_gene_symbol){ cloneinfo.append(refsequence.getGenesymbolString()+"\t");} //      Gene Symbol
        if( m_ref_panum){ cloneinfo.append(refsequence.getPanumber()+"\t");}//      PA Number (for Pseudomonas project only)
        if( m_ref_sga){ cloneinfo.append(refsequence.getPublicInfoParameter("SGD")+"\t");} //      SGA Number (for Yeast project only)
        if( m_ref_gc){ cloneinfo.append(refsequence.getGCcontent()+"\t");}//     GC Content
        if( m_ref_seq_text){ cloneinfo.append(refsequence.getText()+"\t");}//      Sequence Text
        if( m_ref_cds){ cloneinfo.append(refsequence.getCodingSequence()+"\t");}//     CDS
    }

    if( m_clone_seq_id)
    { 
        cloneinfo.append(clone.getSequenceId ()+"\t");
        cloneinfo.append(BaseSequence.getCloneSequenceTypeAsString(clone.getSequenceType ()) + "\t");
    }//      Clone Sequence Id
    if( m_clone_status){ cloneinfo.append(IsolateTrackingEngine.getStatusAsString( clone.getCloneStatus ())+"\t");}//      Clone Sequence Analysis Status
    
    if(  m_clone_discr_high)
    { 
        try
        {
            discrepancies = Mutation.getDiscrepanciesBySequenceId(clone.getSequenceId());
            String discrepancy_report_html = Mutation.discrepancyTypeQualityReport( discrepancies, Mutation.LINKER_5P, true,true);
         discrepancy_report_html += Mutation.discrepancyTypeQualityReport( discrepancies, Mutation.RNA, true,true);
         discrepancy_report_html += Mutation.discrepancyTypeQualityReport( discrepancies, Mutation.LINKER_3P, true,true);

         cloneinfo.append(discrepancy_report_html);
        }
        catch(Exception e){}
        cloneinfo.append("\t");           
        //clone_sequence = 
       // cloneinfo.append(+"\t");
    }//    Discrepancies High Quality (separated by type)
    if(  m_clone_disc_low)
    { 
         try
        {
            if ( discrepancies == null )
            {
             discrepancies = Mutation.getDiscrepanciesBySequenceId(clone.getSequenceId());
            }
            String discrepancy_report_html = Mutation.discrepancyTypeQualityReport( discrepancies, Mutation.LINKER_5P, true,false);
         discrepancy_report_html += Mutation.discrepancyTypeQualityReport( discrepancies, Mutation.RNA, true,false);
         discrepancy_report_html += Mutation.discrepancyTypeQualityReport( discrepancies, Mutation.LINKER_3P, true,false);

         cloneinfo.append(discrepancy_report_html);
        }
        catch(Exception e){}
        cloneinfo.append("\t"); 
    } //   Discrepancies Low Quality (separated by type)
//clone.setSequenceAnalisysStatus (rs.getInt("analysisSTATUS"));
  
      //           clone.setConstructId (rs.getInt("CONSTRUCTID"));
     //            clone.setIsolateTrackingId (rs.getInt("ISOLATETRACKINGID"));
  //  clone.setRank(rs.getInt("RANK"));
     return  cloneinfo.toString();
             
  }
  
  
  
     public static void main(String args[]) 
     
    {
       // InputStream input = new InputStream();
        ReportRunner input = null;
        User user  = null;
        try
        {
             
            user = AccessManager.getInstance().getUser("htaycher1","htaycher");
            input.setItems("65\n651\n652\n653\n58\n581\n582\n583\n874\n875\n876\n");
            input.setItemsType( 2);
           
            input.run();
        }
        catch(Exception e){}
     
        
       
        System.exit(0);
     }
}
