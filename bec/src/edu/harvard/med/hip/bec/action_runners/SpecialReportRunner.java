

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
public class SpecialReportRunner extends ProcessRunner 
{
  
    private boolean    m_clone_id= true; //    Clone Id
	//private boolean    m_dir_name= true; // Directory Name
//	private boolean    m_sample_id= true; //      Sample Id
	//private boolean    m_SGD= true; //      Plate Label
	//private boolean    m_sample_type= true; //      Sample Type
	//private boolean    m_position= true; //      Sample Position
	private boolean    m_ref_sequence_id= true; //      Sequence ID
	private boolean    m_clone_seq_id= true; //      Clone Sequence Id
//	private boolean    m_ref_cds_start= true; //      CDS Start
	//private boolean    m_clone_status= true;//      Clone Sequence Analysis Status
//private boolean    m_ref_cds_stop= true; //      CDS Stop
	private boolean    m_clone_discr_high= true; //    Discrepancies High Quality (separated by type)
	private boolean    m_ref_cds_length= true; //      CDS Length
	private boolean    m_clone_disc_low= true; //   Discrepancies Low Quality (separated by type)
//	private boolean    m_ref_gc= true; //     GC Content
	private boolean    m_ref_seq_text= false; //      Sequence Text
	private boolean    m_ref_cds= false; //     CDS
	private boolean    m_ref_gi= true; //      GI Number
//	private boolean    m_ref_gene_symbol= true; //      Gene Symbol
//	private boolean    m_ref_panum= true; //      PA Number (for Pseudomonas project only)
	private boolean    m_ref_sga= true; //      SGA Number (for Yeast project only)
	private boolean    m_rank = true;
//        private boolean    m_score = true;
        private boolean    m_read_length = true;//length of end reads high quality strip
        private boolean    m_clone_seq_cds_start = true;
        private boolean    m_clone_seq_cds_stop = true;
 //       private boolean    m_clone_seq_text = true;
         private String      m_report_title = "";
   

        
    public String       getTitle()     { return "Request for report generator.";     }
    
     public  void        setFields()
                   /* Object clone_id, //    Clone Id
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
                    Object read_length,
                    Object score,
                     Object clone_seq_cds_start ,
                    Object clone_seq_cds_stop ,
                    Object clone_seq_text 
                 )*/
     {
        /*	Clone id 	SGD	CDS length	Forward read length
v.	Forward read start	Reverse read length	Reverse read start
viii.	List of discrepancies
1.	Discrepancy id
2.	discrepancy type
3.	discrepancy quality
4.	start of discrepancy
5.	length of discrepancy
6.	within 300pb from start (Y/N)
7.	within 300 pb from the end */

         m_report_title += "Plate\tWell\tClone ID\tREF: Bec ID\tREF: FLEX Id\tCds Length\t SGD\tForward Read Length \t Forward Start\t"
         +"Reverse read length\tReverse read start\t Clone Sequence Id\tDiscrepancy id\tDiscrepancy type\tDiscrepancy quality\tstart of discrepancy\t"
         +" Length of discrepancy\tWithin 300pb from start (Y/N)\tWithin 300 pb from the end\n";
         
         /*
        if( clone_id!= null){ m_clone_id= true; m_report_title += "Clone ID\t";} //    Clone Id
       
        
        
        
        
        if( plate_label != null) {m_plate_label= true;  m_report_title += "Plate Label\t";}// Plate Label
        if( sample_id != null) {m_sample_id= true;  m_report_title += "Sample ID\t";}//      Sample Id
        if( sample_type != null){ m_sample_type= true; m_report_title += "Sample Type\t";} //      Sample Type
        if( position != null) {m_position= true;  m_report_title += "Position\t";}//      Sample Position
        if ( score!= null) {m_score= true;  m_report_title += "Clone Score\t";}//
        if ( rank!= null) {m_rank= true;  m_report_title += "Clone Rank\t";}//
        if( dir_name != null) {m_dir_name = true;  m_report_title += "Clone Directory Name\t";}// Directory Name
        if ( read_length != null) {m_read_length= true;  m_report_title += "End reads length: Forward\tEnd reads length:Reverse\t";}
      
        if( ref_sequence_id != null){ m_ref_sequence_id= true;  m_report_title += "REF: Bec ID\tREF: FLEX Id\t ";}//      Sequence ID
        if( ref_cds_start != null) {m_ref_cds_start= true; m_report_title += "REF:CDS Start\t";} //      CDS Start
        if( ref_cds_stop != null) {m_ref_cds_stop= true; m_report_title += "REF:CDS Stop\t";}//      CDS Stop
        if( ref_cds_length != null){ m_ref_cds_length= true; m_report_title += "REF:CDS Length\t";}//      CDS Length
        if( ref_gi != null) {m_ref_gi= true; m_report_title += "REF:GI\t";}//      GI Number
        if( ref_gene_symbol != null){ m_ref_gene_symbol= true;m_report_title += "REF:Gene Symbol\t";} //      Gene Symbol
        if( ref_panum != null) {m_ref_panum= true; m_report_title += "REF:PA Number\t";}//      PA Number (for Pseudomonas project only)
        if( ref_sga != null) {m_ref_sga= true;m_report_title += "REF:SGA Number\t";} //      SGA Number (for Yeast project only)
        if( ref_gc != null) {m_ref_gc= true; m_report_title += "REF:GC\t";}//     GC Content
        if( ref_seq_text != null) {m_ref_seq_text= true; m_report_title += "REF:Text\t";}//      Sequence Text
        if( ref_cds != null) {m_ref_cds= true; m_report_title += "REF:CDS \t";}//     CDS
       
        if( clone_seq_id != null){ m_clone_seq_id= true; m_report_title += "Clone:Sequence Id\tClone:Sequence Type\t";}//      Clone Sequence Id
        if( clone_status != null) {m_clone_status = true;m_report_title += "Clone: Sequence Status\t";}//      Clone Sequence Analysis Status
        if( clone_seq_cds_start != null){ m_clone_seq_cds_start= true;m_report_title += "Clone:Cds Start\t";}
        if( clone_seq_cds_stop != null) {m_clone_seq_cds_stop= true;m_report_title += "Clone:Cds Stop\t";}
        if( clone_seq_text != null) {m_clone_seq_text= true;m_report_title += "Clone:Sequence Text\t";}
         if( clone_discr_high != null) {m_clone_discr_high= true;m_report_title += "Clone:Discrepancy High quality\t";} //    Discrepancies High Quality (separated by type)
        if( clone_disc_low != null) {m_clone_disc_low= true; m_report_title += "Clone:Discrepancy Low Quality\t";}//   Discrepancies Low Quality (separated by type)
        */
     }

    public void run()
    {
        // ArrayList file_list = new ArrayList();
        ArrayList items = new ArrayList();
        File report = null;
        Hashtable refsequences = new Hashtable();//containes refsequences by bec id
        Hashtable reads = new Hashtable();//containes reads by isolatetrackingid
        try
        {
            //convert item into array
           items = Algorithms.splitString( m_items);
          
          ArrayList clones = getCloneInfo(m_items_type,items);
          refsequences= extractRefSequences( clones);
           if ( m_read_length )
           {
                reads = extractReads(clones);
                
           }
          report = printReport(clones,refsequences,reads);
          m_file_list_reports.add(report);   
        }
        catch(Exception ex)
        {
            m_error_messages.add(ex.getMessage());
       }
        finally
        {
           
            sendEMails( getTitle() );
        }
        
    }
    
    
    private ArrayList getCloneInfo(int submission_type, ArrayList items)
    {
        ArrayList clones = new ArrayList();
        String sql = null;
        UICloneSample clone = null;UICloneSample previous_clone = null;
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
                 clone.setCloneStatus (rs.getInt("ISOLATESTATUS"));
                 clone.setSequenceId (rs.getInt("CLONESEQUENCEID"));
                clone.setSequenceAnalisysStatus (rs.getInt("analysisSTATUS"));
                 clone.setSequenceType (rs.getInt("SEQUENCETYPE"));
                 clone.setConstructId (rs.getInt("CONSTRUCTID"));
                 clone.setIsolateTrackingId (rs.getInt("ISOLATETRACKINGID"));
                 clone.setRank(rs.getInt("RANK"));
                 clone.setSampleId(rs.getInt("SAMPLEID"));
                clone.setScore(rs.getInt("SCORE"));
                 clone.setRefSequenceId(rs.getInt("REFSEQUENCEID"));
                 clone.setFLEXRefSequenceId(rs.getInt("FLEXSEQUENCEID"));
                 clone.setCloneSequenceCdsStart(rs.getInt("cloneseqcdsstart"));
                 clone.setCloneSequenceCdsStop(rs.getInt("clonesequencecdsstop"));
          //       if ( m_dir_name )
                   // clone.setTraceFilesDirectory( getTraceFilesDirName( clone.getSampleId() ));
                 
                 if ( previous_clone == null || ( previous_clone != null && previous_clone.getCloneId () != clone.getCloneId()))
                 {
                    clones.add(clone);
                    previous_clone = clone;
                 }
            }
           
            
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot get data for clone "+e.getMessage() +"\n"+sql);
        }
       return clones;
    }
    
    
    private String constructQueryString(int submission_type, ArrayList items)
    {
        String sql = null;
        if ( submission_type == Constants.ITEM_TYPE_PLATE_LABELS)//plates
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
            sql="select FLEXSEQUENCEID,LABEL, POSITION,  SAMPLETYPE, s.SAMPLEID as SAMPLEID,flexcloneid  as CLONEID,"
 +" i.STATUS as IsolateStatus,  a.SEQUENCEID as CLONESEQUENCEID, a.cdsstart as cloneseqcdsstart, a.cdsstop as clonesequencecdsstop, analysisSTATUS,  SEQUENCETYPE, "
+"sc.refsequenceid as refsequenceid,  i.CONSTRUCTID,  i.ISOLATETRACKINGID as ISOLATETRACKINGID, RANK, "
+" i.SCORE as SCORE   from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a ,"
+" sequencingconstruct sc where f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
+" and sc.constructid(+)=i.constructid and   s.containerid=c.containerid and a.isolatetrackingid(+) =i.isolatetrackingid "
+" and s.containerid in (select containerid from containerheader where label in ("
+plate_names.toString()+")) order by s.containerid,position, a.submissiondate desc";
        } 
        else if (submission_type == Constants.ITEM_TYPE_CLONEID)
        {
            sql="select FLEXSEQUENCEID,LABEL, POSITION,  SAMPLETYPE, s.SAMPLEID as SAMPLEID,flexcloneid  as CLONEID,"
 +" i.STATUS as IsolateStatus,  a.SEQUENCEID as CLONESEQUENCEID,  a.cdsstart as cloneseqcdsstart, a.cdsstop as clonesequencecdsstop,analysisSTATUS,  SEQUENCETYPE, "
+"sc.refsequenceid as refsequenceid,  i.CONSTRUCTID,  i.ISOLATETRACKINGID as ISOLATETRACKINGID, RANK, "
+" i.SCORE as SCORE   from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a ,"
+" sequencingconstruct sc where rownum<300 and f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
+" and sc.constructid(+)=i.constructid and   s.containerid=c.containerid and a.isolatetrackingid(+) =i.isolatetrackingid "
+"  and flexcloneid in ("+Algorithms.convertStringArrayToString(items,"," )+") order by s.containerid,position, a.submissiondate desc";
        }
       
        else if (submission_type == Constants.ITEM_TYPE_BECSEQUENCE_ID)//bec sequence id
        {
                sql="select FLEXSEQUENCEID,LABEL, POSITION,  SAMPLETYPE, s.SAMPLEID as SAMPLEID,flexcloneid  as CLONEID,"
 +" i.STATUS as IsolateStatus,  a.SEQUENCEID as CLONESEQUENCEID, a.cdsstart as cloneseqcdsstart, a.cdsstop as clonesequencecdsstop, analysisSTATUS,  SEQUENCETYPE, "
+"sc.refsequenceid as refsequenceid,  i.CONSTRUCTID,  i.ISOLATETRACKINGID as ISOLATETRACKINGID, RANK, "
+" i.SCORE as SCORE   from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a ,"
+" sequencingconstruct sc where rownum<300 and f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
+" and sc.constructid(+)=i.constructid and   s.containerid=c.containerid and a.isolatetrackingid(+) =i.isolatetrackingid "
+"  and a.SEQUENCEID in ("+Algorithms.convertStringArrayToString(items,"," )+") order by s.containerid,position,  a.submissiondate desc";
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
            if ( clone.getRefSequenceId()< 1) continue;
            if (! refsequences.containsKey(new Integer(clone.getRefSequenceId())))
            {
                try
                {
                    refsequence = new RefSequence(clone.getRefSequenceId());
                    refsequences.put(new Integer(clone.getRefSequenceId()),refsequence);
                }
                catch(Exception e)
                {
                    m_error_messages.add("Cannot get Refsequence for id "+clone.getRefSequenceId() +"\n"+e.getMessage());
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
        Read read = null;
        StringBuffer isolatetrackingids = new StringBuffer();
        for (int index = 0; index <clones.size();index++)
        {
            clone= (UICloneSample)clones.get(index);
            if ( !clone.getSampleType().equals("ISOLATE") ) continue;
            isolatetrackingids.append( clone.getIsolateTrackingId() +",");
        }
        
        if ( isolatetrackingids.charAt(isolatetrackingids.length() -1) == ',' )
            isolatetrackingids.setCharAt(isolatetrackingids.length() -1, ' ');
        
        String sql = "select readid, cdsstart,cdsstop,isolatetrackingid, READSEQUENCEID,READTYPE,TRIMMEDSTART,TRIMMEDEND from readinfo where isolatetrackingid in ("+isolatetrackingids.toString()+") order by isolatetrackingid";
        
         ResultSet rs = null;
         int istr_id = -1;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                 read = new Read();
                 read.setId (rs.getInt("READID")); 
                read.setSequenceId (rs.getInt("READSEQUENCEID")); 
                read.setType (rs.getInt("READTYPE")); 
                read.setTrimStart (rs.getInt("TRIMMEDSTART")); 
                read.setTrimEnd(rs.getInt("TRIMMEDEND")); 
                read.setCdsStart(rs.getInt("cdsstart"));
                read.setCdsStop(rs.getInt("cdsstop"));
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
            m_error_messages.add("Cannot get reads sql"+sql +"\n"+e.getMessage());
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
                    fl =   new File(Constants.getTemporaryFilesPath() + System.currentTimeMillis()+ ".txt");
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
      Read read = null;
      RefSequence refsequence = null;
       ArrayList discrepancies =  null;
      if ( clone.getRefSequenceId()>0)
      {
         refsequence = (RefSequence)refsequences.get(new Integer(clone.getRefSequenceId()));
      }
       cloneinfo.append(clone.getPlateLabel()+"\t");
       cloneinfo.append(clone.getPosition()+"\t");
      cloneinfo.append(clone.getCloneId()+"\t");
      if (refsequence != null  )
    {
        if(  m_ref_sequence_id){ cloneinfo.append( refsequence.getId() +"\t" + clone.getFLEXRefSequenceId()+"\t");}//      Sequence ID
    //    if( m_ref_cds_start){ cloneinfo.append( refsequence.getCdsStart()+"\t" );} //      CDS Start
    //    if( m_ref_cds_stop){ cloneinfo.append( refsequence.getCdsStop() +"\t");}//      CDS Stop
        if( m_ref_cds_length){ cloneinfo.append( ( refsequence.getCdsStop() - refsequence.getCdsStart())+"\t");}//      CDS Length
    //    if( m_ref_gi){ cloneinfo.append(refsequence.getGi()+"\t");}//      GI Number
   //     if(  m_ref_gene_symbol){ cloneinfo.append(refsequence.getGenesymbolString()+"\t");} //      Gene Symbol
   //     if( m_ref_panum){ cloneinfo.append(refsequence.getPanumber()+"\t");}//      PA Number (for Pseudomonas project only)
        if( m_ref_sga){ cloneinfo.append(refsequence.getPublicInfoParameter("SGD")+"\t");} //      SGA Number (for Yeast project only)
   //     if( m_ref_gc){ cloneinfo.append(refsequence.getGCcontent()+"\t");}//     GC Content
   //     if( m_ref_seq_text){ cloneinfo.append(refsequence.getText()+"\t");}//      Sequence Text
   //     if( m_ref_cds){ cloneinfo.append(refsequence.getCodingSequence()+"\t");}//     CDS
    }
    else
    {
        if(  m_ref_sequence_id){  cloneinfo.append("\t");}//      Sequence ID
     //   if( m_ref_cds_start){  cloneinfo.append("\t" );} //      CDS Start
     //   if( m_ref_cds_stop){  cloneinfo.append("\t");}//      CDS Stop
        if( m_ref_cds_length){  cloneinfo.append("\t");}//      CDS Length
     //   if( m_ref_gi){  cloneinfo.append("\t");}//      GI Number
    //    if(  m_ref_gene_symbol){  cloneinfo.append("\t");} //      Gene Symbol
   //     if( m_ref_panum){ cloneinfo.append("\t");}//      PA Number (for Pseudomonas project only)
        if( m_ref_sga){  cloneinfo.append("\t");} //      SGA Number (for Yeast project only)
    //    if( m_ref_gc){  cloneinfo.append("\t");}//     GC Content
     //   if( m_ref_seq_text){  cloneinfo.append("\t");}//      Sequence Text
      //  if( m_ref_cds){  cloneinfo.append("\t");}//     CDS
    }
    if ( m_read_length )
    {
        if ( clone.getSampleType().equals("ISOLATE"))
        {
            ArrayList samplereads = (ArrayList)reads.get(new Integer(clone.getIsolateTrackingId()));
            if ( samplereads != null) 
            {
                for (int index = 0; index < samplereads.size(); index++)
                {
                    read = (Read) samplereads.get(index);
                    cloneinfo.append(Read.getTypeAsString(read.getType())+"_"+ (read.getTrimEnd() - read.getTrimStart () )+"\t "); 
                    cloneinfo.append(read.getCdsStart()+"_"+ read.getCdsStop()+"\t " ); 

                }
            }
        }
        
    }
   try
    {
        if ( clone.getSequenceId() > 0)
        {
            discrepancies = Mutation.getDiscrepanciesBySequenceId(clone.getSequenceId());
        }  
        else
        {
            discrepancies = new ArrayList();
            if ( clone.getSampleType().equals("ISOLATE"))
            {
                 ArrayList samplereads = (ArrayList)reads.get(new Integer(clone.getIsolateTrackingId()));
                if ( samplereads != null) 
                {
                    for (int index = 0; index < samplereads.size(); index++)
                    {
                        read = (Read) samplereads.get(index);
                        ArrayList read_discrepancies = Mutation.getDiscrepanciesBySequenceId(read.getSequenceId());
                        if ( read_discrepancies != null) discrepancies.addAll( read_discrepancies);
      
                    }
                }

            }
        }
   }
        catch(Exception e){}
        cloneinfo.append( clone.getSequenceId() +"\t");
      //print discrepancies
      if ( discrepancies != null)
      {
         Mutation dd = null;
          for (int count = 0; count < discrepancies.size(); count++)
          {
              
              dd = (Mutation )discrepancies.get(count);
              if (dd.getType() == Mutation.AA) continue;
              cloneinfo.append( "\t\t"+clone.getCloneId()+"\t\t\t\t\t\t\t\t\t\t" );
              cloneinfo.append( dd.getId() +"\t");

                cloneinfo.append( dd.getMutationTypeAsString() +"\t");
                 cloneinfo.append( dd.getQualityAsString() +"\t");
              cloneinfo.append( dd.getPosition() +"\t");
              cloneinfo.append( dd.getLength() +"\t");
              cloneinfo.append( ( dd.getPosition() < 300)+"\t" );
              cloneinfo.append( (( ( refsequence.getCdsStop() - refsequence.getCdsStart()) - dd.getPosition()) < 300 )+"\t");
                cloneinfo.append("\n");
          }
      }
          
    /*  
     if(  m_clone_id){ cloneinfo.append(clone.getCloneId()+"\t");}//    Clone Id
    if( m_plate_label){ cloneinfo.append( clone.getPlateLabel ()+"\t");}// Plate Label
    if( m_sample_id){ cloneinfo.append(clone.getSampleId() +"\t");}//      Sample Id
    if(  m_sample_type){ cloneinfo.append(clone.getSampleType ()+"\t");} //      Sample Type
    if( m_position){ cloneinfo.append(clone.getPosition ()+"\t");}//      Sample Position
    if( m_score)
    {
        if(   clone.getSampleType().equals("ISOLATE")) { cloneinfo.append(clone.getScore ()+"\t");}
        else    {         cloneinfo.append("\t"); }
    }    
    if( m_rank)
    {
        if ( clone.getSampleType().equals("ISOLATE")   )     { cloneinfo.append( IsolateTrackingEngine.getRankAsString(clone.getRank())+"\t");}
        else       { cloneinfo.append("\t");}
    }
      //get directories
    if( m_dir_name)
    { 
        EndReadsWrapperRunner er = new EndReadsWrapperRunner();
        if (clone.getSampleType().indexOf("CONTROL") != -1) 
          cloneinfo.append(er.getControlSamplesDir()+"\t");
       else if ( clone.getSampleType().equals("ISOLATE") )
           if ( refsequence != null)
                cloneinfo.append( clone.getTraceFilesDirectory()+"\t");
           else cloneinfo.append("\t");
      else if (  clone.getSampleType().equals("EMPTY") )
            cloneinfo.append(er.getEmptySamplesDir()+"\t");
            
    }// Directory Name
    if ( m_read_length )
    {
        if ( clone.getSampleType().equals("ISOLATE"))
        {
            ArrayList samplereads = (ArrayList)reads.get(new Integer(clone.getIsolateTrackingId()));
            if ( samplereads != null) 
            {
                for (int index = 0; index < samplereads.size(); index++)
                {
                    read = (UIRead) samplereads.get(index);
                    cloneinfo.append(Read.getTypeAsString(read.getType())+"_"+ (read.getTrimStop() - read.getTrimStart () )+" || "); 

                }
            }
        }
        cloneinfo.append("\t");
    }
    if (refsequence != null  )
    {
        if(  m_ref_sequence_id){ cloneinfo.append( refsequence.getId() +"\t" + clone.getFLEXRefSequenceId()+"\t");}//      Sequence ID
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
    else
    {
        if(  m_ref_sequence_id){  cloneinfo.append("\t");}//      Sequence ID
        if( m_ref_cds_start){  cloneinfo.append("\t" );} //      CDS Start
        if( m_ref_cds_stop){  cloneinfo.append("\t");}//      CDS Stop
        if( m_ref_cds_length){  cloneinfo.append("\t");}//      CDS Length
        if( m_ref_gi){  cloneinfo.append("\t");}//      GI Number
        if(  m_ref_gene_symbol){  cloneinfo.append("\t");} //      Gene Symbol
        if( m_ref_panum){ cloneinfo.append("\t");}//      PA Number (for Pseudomonas project only)
        if( m_ref_sga){  cloneinfo.append("\t");} //      SGA Number (for Yeast project only)
        if( m_ref_gc){  cloneinfo.append("\t");}//     GC Content
        if( m_ref_seq_text){  cloneinfo.append("\t");}//      Sequence Text
        if( m_ref_cds){  cloneinfo.append("\t");}//     CDS
    }

    if( m_clone_seq_id )
    { 
        cloneinfo.append(clone.getSequenceId ()+"\t");
        if ( clone.getSequenceId () != 0)
            cloneinfo.append(BaseSequence.getCloneSequenceTypeAsString(clone.getSequenceType ()));
        cloneinfo.append( "\t");
    }//      Clone Sequence Id
    if( m_clone_status)
    { if (clone.getSequenceId () != 0)cloneinfo.append(BaseSequence.getSequenceAnalyzedStatusAsString( clone.getSequenceAnalisysStatus ()));
      cloneinfo.append("\t");
    }//      Clone Sequence Analysis Status
    if (m_clone_seq_cds_start){cloneinfo.append( clone.getCloneSequenceCdsStart ()+"\t");}
    if (m_clone_seq_cds_stop){cloneinfo.append( clone.getCloneSequenceCdsStop ()+"\t");}
    if (m_clone_seq_text)
    {
        String seqtext = "";
        if ( clone.getSequenceId() > 0)
        {
            try
            {
               seqtext= BaseSequence.getSequenceInfo( clone.getSequenceId(), BaseSequence.SEQUENCE_INFO_TEXT);
            }
            catch(Exception e)
            {
                m_error_messages.add("Cannot get sequence text for sequence id"+clone.getSequenceId () +"\n"+e.getMessage());
            }
        }
        cloneinfo.append(seqtext+"\t");
    }
       
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
    } 
     **/ 
     return  cloneinfo.toString();
            
  }
  
    private String getTraceFilesDirName( int sampleId )
    {
         
        ResultSet rs = null;
        String sql ="select distinct LOCALPATH from filereference where filereferenceid in "
        +" (select filereferenceid from resultfilereference where resultid in "
        +" (select resultid from result where sampleid ="+sampleId+"))";
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                 return rs.getString("LOCALPATH");
            }
           
            
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot get data for clone "+e.getMessage() +"\n"+sql);
        }
       return null;
    }
  
     public static void main(String args[]) 
     
    {
       // InputStream input = new InputStream();
        SpecialReportRunner input = null;
        User user  = null;
        try
        {
             
            user = AccessManager.getInstance().getUser("htaycher123","htaycher");
            input = new SpecialReportRunner();
            input.setItems("     2392       2578       2447       2665       2637       2733       2741       2338       2455       2261       2203       2275       1909       2443       2909       2265       2519       1874       1966       2984       2797       2415       2991       2168       2865       2954       2913       2461       1022        655        546       1147       1006       1118       1667       1067       1036        998       1127       1582       1309       1352       1673        827       1176       3236       4139       3280       4096       3571       3420       3956       3637       3453       3864       3878       3688       3715       3499       4080       4077       3435       3430       1083       1096       1165       1467       1358       1364       1089       1322       1059       1116       1168       1212       1054       1178       1130       1031       1286       1381       1062       1090       1473       1562       1905       1822       1648       1510       1514       1609        678        774        664       1003        942        933        819        892        950        726       2165       2068       1994       2218       1919       2211       2024       2300       2252       2020       2040       2517       2477       3072       3061       2876       3304       2917       3067       3026       2842       3311       2959       3403       3711       3458       3587       3443       3911       3389       3613       3343       3604       3617       3868       3401       3441       4074       4049       4039       4380       4041       3940       4170       4206       4426       4343       3978       4633       4592       4836       4623       4713       4658       4980       4610       4613       5040       4890       4914       4531       4494       4574       4670       4687       5010       4585       5380       5564       5133       5469       5258       5577       5139       5365       5174       5185       5206       5423       5368       5143       5277       5620       5363       6117       5949       6028       5998       5994       5929       6144       5965       5666       5655       5732       6068       5658       6097       5856       6061       5783       6162       6092       6650       6287       6277       6586       6536       6345       6435       6641       6636       6690       6347       6790       6937       6906       6846       7073        737        970        533       7114     113600     113608     113620     113625     113630     113633     113635     113641     113644     113765     113788     113793     113805     113814     113843     113855     113894     113964     113980     114003     114008     114013     114015     114026     114033     114042     114043     114052     114053     114055     114063     114115     114134     114141     114155     114162     114168     114188     114228     114240     114246     114247     114261     114262     114264     114272     114274     114277     114302     114308     114368     114369     114371     114394     114431     114438     114450     114451     114456     114459     114473     114494     114639     114652     114654     114665     114667     114676     114696     114712     114716     114719     114726     114732     114735     114749     114755     114759     114779     114780     114781     114788     114804     114826     114834     114844     114874     114893     114898     114899     114904     114916     114917     114918     114919     114920     114921     114948     114955     114993     114997     114999     115001     115002     115006     115040     115041     115109     115110     115141     115162     115164     115165     115203     115209     115217     115245     115267     115281     115289     115314     115321     115327     115334     115335     115340     115345     115346     115349     115353     115362     110953     110955     110957     110970     110987     111030     111038     111052     111085     111086     111097     111103     111106     111109     111117     111118     111119     111128     111134     111160     111162     111163     111173     111189     111196     111207     111244     111262     111270     111283     111289     111297     111340     111379     111383     111393     111405     111436     111449     111457     111460     111483     111486     111490     111524     111543     111559     111563     111572     111573     111574     111586     111591     111594     112357     112359     112363     112364     112365     112366     112367     112369     112373     112376     112382     112384     112397     112408     112411     112414     112418     112421     112426     112430     112435     112440     112441     112443     112445     112450     112451     112454     112457     112458     112479     112480     112482     112484     112491     112492     112496     112502     112510     112511     112516     112518     112534     112524     112535     112537     112572     112576     112578     112579     112586     112603     112605     112610     112611     112616     112618     112638     112641     112653     112658     112662     112671     112689     112693     112697     112698     112701     112704     112707     112708     112714     112716     112717     112725     112735     112763     112780     112783     112802     112810     112815     112816     112818     112822     112826     112828     112829     112831     112837     112839     112842     112853     112857     112875     112888     112890     112893     112915     112917     112927     112930     112951     112953     112974     112981     112998     113006     113008     113012     113052     113089     113097     113108     113120     113121     113138     113159     113160     113161     113176     113178     113206     113215     113216     113227     113250     113257     113258     113262     113265     113266     113267     113268     113289     113292     113297     113302     113304     113305     113311     113314     113316     113317     113320     113322     113326     113333     113338     113342     113349     113352     113357     113362     113365     113367     113369     113373     113384     113389     113395     113412     113413     113415     113425     113461     113468     113472     113475     113476     113479     113483     113484     113485     113490     113493     113494     113495     113496     113503     113509     113510     113511     113513     113524     113532     113534     113535     113540     113541     113542     113549     113550     113552     113557     113564     113568     113572     113584     133538     133544     133546     133557     133566     133573     133575     133577     133583     133585     133599     133600     133605     133606     133610     133611     133615     133622     133631     133636     133650     133659     133667     133674     133675     133676     133679     133681     133682     133685     133687     133691     133700     133701     133703     133713     133720     133729     133732     133741     133753     133763     133765     133767     133769     133770     133772     133811     133831     133833     133853     133856     133872     133877     133883     133901     133903     133911     133913     133934     133955       2536       2545       2376       2667       2504       2383       2750       2499       2145       2643       1793       2548       1950       1889       2583       1769       2060       2760       2633       2685       2559       2587       2310       2110       2849       2905       2694       2967       2323       2900       2474       1963       3086       2189       2832       2470       2268       2781       1748       2690        981        986       1069       1234        863       1405        513        601       1614       1633        765        523       1388        618        992        596       1635       4109       3706       3187       3753       3543       3184       3575       3293       4000       3684       3594       3366       3702       3725       3515       3486       3359       3214       3562       3179       3738       3363       3517       3164       3724       3729       3720       1401       1112       1103       1484       1374       1425       1227       1394       1136       1223       1279       1294       1312       1173       1297       1156       1776       1848       1638       1583       1783       1785       1494       1890       1632       1593        899        884        639        732        566        839       2150       2081       2320       2054       1933       2350       2342       2290       2160       1953       1942       1976       2330       2288       1980       2140       2285       1985       2136       1929       2193       2686       2437       2573       2497       2422       2552       2834       2479       2527       2672       3054       3081       2870       3303       3006       2971       3247       3243       3105       2920       3059       2966       3153       3089       2930       3825       3475       3699       3595       3506       3352       3642       3379       3385       3554       3742       3510       3719       3886       3502       4368       4156       4127       4462       4062       4167       4245       4391       4304       3992       4398       3920       4162       4237       4242       4214       4123       4372       3982       4444       4233       4289       4176       4515       4977       4667       4530       4851       4468       4675       4799       4662       4886       4508       4935       5216       5303       5073       5239       5228       5512       5522       5318       5197       5189       5232       5531       5287       5144       5085       5095       6003       5842       5758       5931       5796       5860       6147       5743       6051       6184       6151       6203       5672       5639       5907       5865       6311       6599       6538       6672       6767       6510       6213       6691       6734       6615       6730       6779       6303       6660       6798       7031       6802       6834       6958       6963        739        669        783        622        682        857        904        511        632     113596     113598     113605     113618     113621     113634     113640     113650     113651     113764     113771     113774     113780     113817     113826     113833     113844     113864     113865     113883     113930     113946     113949     113952     113958     113975     113982     114009     114019     114022     114025     114027     114029     114034     114040     114054     114058     114062     114085     114104     114109     114121     114137     114158     114161     114164     114211     114214     114250     114252     114278     114281     114291     114303     114304     114340     114351     114359     114372     114376     114430     114440     114448     114455     114460     114508     114511     114519     114521     114526     114527     114643     114644     114645     114660     114675     114680     114697     114698     114705     114713     114714     114718     114727     114730     114740     114741     114751     114758     114763     114770     114782     114784     114892     114925     114927     114930     114938     114939     114947     114974     114975     114994     115015     115022     115039     115043     115047     115059     115061     115076     115083     115097     115129     115138     115140     115142     115185     115274     115276     115290     115297     115311     115315     115316     115322     115329     115330     110968     110972     110976     110985     110989     111014     111037     111090     111094     111098     111099     111110     111112     111127     111142     111166     111186     111188     111198     111215     111249     111292     111327     111330     111341     111343     111361     111394     111400     111413     111414     111415     111416     111417     111420     111429     111438     111454     111472     111482     111533     111550     111558     111565     111599     112329     112340     112346     112348     112358     112372     112375     112391     112393     112399     112406     112410     112424     112432     112437     112439     112446     112453     112459     112460     112466     112485     112489     112490     112497     112499     112501     112513     112514     112522     112526     112527     112528     112554     112573     112597     112598     112599     112604     112608     112613     112617     112640     112649     112664     112669     112678     112683     112692     112694     112695     112703     112705     112709     112712     112713     112718     112719     112726     112737     112743     112757     112758     112764     112766     112771     112774     112776     112795     112796     112797     112805     112808     112812     112813     112814     112824     112835     112841     112852     112854     112856     112865     112871     112876     112929     112990     112994     112995     113004     113009     113013     113034     113042     113051     113053     113081     113082     113088     113118     113131     113135     113136     113150     113179     113185     113190     113194     113196     113198     113204     113205     113242     113243     113245     113273     113282     113284     113285     113286     113290     113301     113318     113327     113344     113345     113356     113364     113391     113393     113397     113399     113423     113428     113430     113431     113437     113448     113450     113456     113457     113466     113471     113473     113477     113478     113482     113486     113487     113488     113507     113519     113520     113528     113529     113533     113545     113546     113553     113554     113555     113562     113567     113586     113589     113590     133553     133571     133581     133587     133591     133602     133613     133620     133629     133633     133648     133649     133654     133665     133678     133689     133690     133693     133696     133704     133706     133717     133722     133734     133738     133745     133746     133774     133783     133785     133795     133797     133799     133809     133818     133832     133845     133846     133848     133849     133850     133855     133857     133861     133866     133867     133874     133885     133887     133890     133891     133892     133893     133894     133904     133921     133923     133927     133953     133975       2433       2701       2407       2713       2771       2732       2405       2814       3013       2661       1810       2240       1999       1870       1866       2806       2789       3032       2563       2645       3012       1899       1029        642        918        697       1719       1244       1158        794       1198        816       1106        578       1505       1440       1017        612       1125       3482       3793       4087       3857       3114       4112       3968       3322       3759       3659       4133       3767       3345       4090       3491       1269       1250       1007       1333       1258       1203       1267       1427       1464       1345       1624       1722       1546        756        574        607        701       2237       2226       1992       2303       2426       2758       2737       3033       3160       3268       3158       2880       3198       3276       2978       3146       2997       3265       3677       3612       3665       3407       3788       3785       3891       3426       3896       3893       3465       3774       3901       4027       4374       4011       4252       4203       4277       4006       4129       4768       4963       4867       4959       5003       4552       4684       4873       5389       5466       5488       5339       5377       5280       5096       5250       5553       5047       5977       6132       5677       5698       5809       5774       5987       5816       6069       6185       6757       6447       6749       6752       6291       6503       6624       6474       6241       6439       6528       6423       6610       6623       6580       7007       7043       6810       6890       7056       6991       6815       6989       6952       6792       6806       6984        717        519        822        921        975        527        940        831        704     113593     113607     113613     113615     113617     113619     113629     113636     113648     113663     113667     113770     113772     113773     113786     113791     113795     113813     113829     113839     113840     113848     113869     113874     113895     113915     114011     114014     114041     114044     114065     114067     114140     114159     114220     114224     114244     114260     114275     114305     114335     114338     114360     114377     114385     114400     114444     114445     114454     114531     114655     114679     114723     114737     114744     114765     114778     114783     114794     114822     114843     114846     114882     114884     114896     114905     114906     114910     114971     114977     114981     115008     115081     115098     115108     115116     115121     115156     115191     115278     115284     115309     115323     115348     115364     110935     111010     111070     111122     111132     111195     111311     111391     111392     111402     111404     111418     111419     111480     111493     111517     111554     111605     112349     112380     112386     112388     112394     112396     112402     112409     112431     112433     112449     112464     112476     112494     112495     112503     112504     112506     112508     112512     112515     112517     112519     112530     112549     112588     112590     112620     112626     112633     112644     112652     112656     112687     112691     112711     112722     112732     112734     112756     112770     112777     112779     112781     112798     112803     112804     112806     112817     112833     112834     112836     112843     112848     112860     112879     112935     112971     112973     112975     113007     113017     113035     113046     113080     113099     113107     113109     113119     113123     113133     113139     113140     113169     113177     113217     113228     113251     113256     113270     113271     113274     113276     113283     113287     113308     113309     113313     113315     113323     113343     113351     113396     113398     113416     113422     113440     113441     113444     113474     113492     113499     113500     113501     113504     113508     113525     113530     113531     113538     113578     113585     133542     133543     133569     133589     133592     133594     133595     133597     133607     133608     133625     133646     133652     133656     133662     133671     133672     133673     133688     133714     133715     133718     133719     133721     133725     133727     133731     133735     133742     133749     133758     133793     133842     133858     133868     133875     133880     133910     133917     133918     133925     133929     133932     133936     133938     133943");
            
            
            
            input.setItemsType( Constants.ITEM_TYPE_CLONEID);
            input.setFields();
            input.run();
        }
        catch(Exception e){}
     
        
       
        System.exit(0);
     }
     
      
}

