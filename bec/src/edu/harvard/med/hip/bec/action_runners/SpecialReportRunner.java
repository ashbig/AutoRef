

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
         private int         m_5linker_length=36;
         private int         m_3linker_length=36;
   

        
         public String getTitle()     { return "Request for report generator.";     }
    
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
         +"Reverse read length\tReverse read start\t Clone Sequence Id\t"
         +" ForwardReadNeeded\tReverseReadNeeded\tInternalReadNeeded\tisFailed\t"
         +"Discrepancy id\tDiscrepancy type\tDiscrepancy quality\tstart of discrepancy\t"
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

    public void       set5LinkerLength(int v){ m_5linker_length=v;}
     public void       set3LinkerLength(int v){  m_3linker_length=v;}
    public void run_report()
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
         // m_file_list_reports.add(report);   
        }
        catch(Exception ex)
        {
            m_error_messages.add(ex.getMessage());
       }
        finally
        {
           
           // sendEMails( getTitle() );
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
        //set type of clone 
       cloneinfo.append( isForwardReadNeeded((ArrayList)reads.get(new Integer(clone.getIsolateTrackingId())), discrepancies) +"\t");
       cloneinfo.append( isReverseReadNeeded((ArrayList)reads.get(new Integer(clone.getIsolateTrackingId())), discrepancies,refsequence) +"\t");
       cloneinfo.append( isInternalReadNeeded((ArrayList)reads.get(new Integer(clone.getIsolateTrackingId())), discrepancies) +"\t");
       cloneinfo.append(isFailed( discrepancies, refsequence)+"\t");
      //print discrepancies
      if ( discrepancies != null)
      {
         Mutation dd = null;
          for (int count = 0; count < discrepancies.size(); count++)
          {
              
              dd = (Mutation )discrepancies.get(count);
              if (dd.getType() == Mutation.AA) continue;
                cloneinfo.append("\n");
              cloneinfo.append( "\t\t"+clone.getCloneId()+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t" );
              cloneinfo.append( dd.getId() +"\t");

                cloneinfo.append( dd.getMutationTypeAsString() +"\t");
                 cloneinfo.append( dd.getQualityAsString() +"\t");
              cloneinfo.append( dd.getPosition() +"\t");
              cloneinfo.append( dd.getLength() +"\t");
              cloneinfo.append( ( dd.getPosition() < 300)+"\t" );
              cloneinfo.append( (( ( refsequence.getCdsStop() - refsequence.getCdsStart()) - dd.getPosition()) < 300 )+"\t");
              
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
    
    
    
       private String isForwardReadNeeded(ArrayList reads, ArrayList discrepancies) 
       {
           Read read  = null;
            if ( reads == null || reads.size() == 0) return "TRUE";
           try
           {
                for (int index = 0; index < reads.size(); index++)
                {
                    read = (Read) reads.get(index);

                    if (read.getType() == Read.TYPE_ENDREAD_FORWARD_SHORT ||
                        read.getType() == Read.TYPE_ENDREAD_FORWARD_NO_MATCH ) return "TRUE";
                    if ( read.getType() == Read.TYPE_ENDREAD_FORWARD)
                    {
                        if ( read.getCdsStart() < m_5linker_length ) return "TRUE";
                        if ( (read.getCdsStop() - read.getCdsStart()) < 300 ) return "TRUE";
                        ArrayList mutations = Mutation.getDiscrepanciesBySequenceId(read.getSequenceId()) ;
                        if (mutations == null || (mutations != null && mutations.size() == 0) )return "FALSE";
                        for (int count = 0; count < mutations.size() ; count++)
                        {
                            Mutation mut = (Mutation) mutations.get(count);
                            if  ( mut.getPosition() < 300) return "TRUE";
                        }
                        return "FALSE";

                    }
                }
                  return "TRUE";
           }
           catch(Exception e){ return "CANNOT RESOLVE";}
            
       }
        private String  isReverseReadNeeded(ArrayList reads,ArrayList discrepancies, RefSequence refsequence)
        {
            Read read  = null;
              if ( reads == null || reads.size() == 0) return "TRUE";
            try
            {
            for (int index = 0; index < reads.size(); index++)
            {
                read = (Read) reads.get(index);
                
                if (read.getType() == Read.TYPE_ENDREAD_REVERSE_SHORT ||
                    read.getType() == Read.TYPE_ENDREAD_REVERSE_NO_MATCH ) return "TRUE";
                if ( read.getType() == Read.TYPE_ENDREAD_REVERSE)
                {
                    if ( read.getCdsStart() < m_5linker_length ) return "TRUE";
                    if ( (read.getCdsStop() - read.getCdsStart()) < 300 ) return "TRUE";
                    ArrayList mutations = Mutation.getDiscrepanciesBySequenceId(read.getSequenceId()) ;
                    if (mutations == null || (mutations != null && mutations.size() == 0)) return "FALSE";
                    for (int count = 0; count < mutations.size() ; count++)
                    {
                        Mutation mut = (Mutation) mutations.get(count);
                        if  ( (refsequence.getCdsStop() - refsequence.getCdsStart()) - mut.getPosition() < 300 ) return "TRUE";
                        if ( mut.getType() == Mutation.LINKER_3P)return "TRUE";
                    }
                    return "FALSE";
                
                }
            }
               return "TRUE";
           }
           catch(Exception e){ return "CANNOT RESOLVE";}
        }
       private String  isInternalReadNeeded(ArrayList reads,ArrayList discrepancies)
       {
           return "";
       }
       private String  isFailed(ArrayList discrepancies, RefSequence refsequence)
       {
            Mutation dd = null;
          for (int count = 0; count < discrepancies.size(); count++)
          {
              
              dd = (Mutation )discrepancies.get(count);
              if (dd.getType() == Mutation.AA) continue;
              //filter quality
              if( dd.getQuality() == Mutation.QUALITY_LOW) continue;
              //filter position
              if (  dd.getPosition() < 300 ||
               ( refsequence.getCdsStop() - refsequence.getCdsStart() - dd.getPosition()) < 300 ) continue;
              if ( dd.getChangeType() !=  Mutation.TYPE_RNA_SILENT)
                  return "TRUE";
              
          }
            return "FALSE";
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
            input.setItems("113593	113596	113598	113600");//"     6610	      6615	      6623	      6624	      6636	      6641	      6650	      6660	      6672	      6690	      6691	      6730	      6734	      6749	      6752	      6757	      6767	      6779	      6790	      6792	      6798	      6802	      6806	      6810	      6815	      6834	      6846	      6890	      6906	      6937	      6952	      6958	      6963	      6984	      6989	      6991	      7007	      7031	      7043	      7056	      7073	      7114	    110935	    110953	    110955	    110957	    110968	    110970	    110972	    110976	    110985	    110987	    110989	    111010	    111014	    111030	    111037	    111038	    111052	    111070	    111085	    111086	    111090	    111094	    111097	    111098	    111099	    111103	    111106	    111109	    111110	    111112	    111117	    111118	    111119	    111122	    111127	    111128	    111132	    111134	    111142	    111160	    111162	    111163	    111166	    111173	    111186	    111188	    111189	    111195	    111196	    111198	    111207	    111215	    111244	    111249	    111262	    111270	    111283	    111289	    111292	    111297	    111311	    111327	    111330	    111340	    111341	    111343	    111361	    111379	    111383	    111391	    111392	    111393	    111394	    111400	    111402	    111404	    111405	    111413	    111414	    111415	    111416	    111417	    111418	    111419	    111420	    111429	    111436	    111438	    111449	    111454	    111457	    111460	    111472	    111480	    111482	    111483	    111486	    111490	    111493	    111517	    111524	    111533	    111543	    111550	    111554	    111558	    111559	    111563	    111565	    111572	    111573	    111574	    111586	    111591	    111594	    111599	    111605	    112329	    112340	    112346	    112348	    112349	    112357	    112358	    112359	    112363	    112364	    112365	    112366	    112367	    112369	    112372	    112373	    112375	    112376	    112380	    112382	    112384	    112386	    112388	    112391	    112393	    112394	    112396	    112397	    112399	    112402	    112406	    112408	    112409	    112410	    112411	    112414	    112418	    112421	    112424	    112426	    112430	    112431	    112432	    112433	    112435	    112437	    112439	    112440	    112441	    112443	    112445	    112446	    112449	    112450	    112451	    112453	    112454	    112457	    112458	    112459	    112460	    112464	    112466	    112476	    112479	    112480	    112482	    112484	    112485	    112489	    112490	    112491	    112492	    112494	    112495	    112496	    112497	    112499	    112501	    112502	    112503	    112504	    112506	    112508	    112510	    112511	    112512	    112513	    112514	    112515	    112516	    112517	    112518	    112519	    112522	    112524	    112526	    112527	    112528	    112530	    112534	    112535	    112537	    112549	    112554	    112572	    112573	    112576	    112578	    112579");
            
            input.setItemsType( Constants.ITEM_TYPE_CLONEID);
           input.setFields();
           input.run_report();
          /*
            String m_items="511	513	523	527	533	546	566	578	596	601	607	612	618	622	632	639	642	655	669	682	697	701	704	726	732	756	783	794	816	827	831	839	857	884	892	904	918	921	940	950	970	975	981	986	992	998	1003	1006	1007	1017	1022	1029	1031	1036	1054	1059	1062	1067	1069	1089	1090	1096	1103	1106	1116	1118	1130	1136	1156	1158	1165	1168	1173	1176	1178	1198	1212	1234	1244	1258	1267	1279	1286	1294	1297	1309	1312	1322	1333	1345	1352	1358	1364	1381	1388	1394	1405	1425	1427	1464	1473	1494	1505	1510	1514	1546	1562	1582	1583	1593	1609	1624	1632	1633	1635	1638	1648	1667	1673	1719	1769	1783	1785	1793	1810	1822	1848	1866	1870	1874	1890	1899	1905	1909	1919	1929	1933	1942	1950	1953	1966	1980	1985	1992	1994	1999	2020	2040	2081	2110	2136	2140	2145	2160	2168	2189	2193	2203	2211	2226	2237	2240	2252	2261	2265	2268	2275	2285	2288	2290	2300	2303	2310	2320	2323	2330	2338	2342	2383	2392	2407	2422	2433	2443	2447	2455	2461	2470	2474	2477	2479	2499	2504	2519	2527	2545	2552	2559	2578	2583	2587	2633	2637	2643	2661	2665	2667	2672	2685	2690	2694	2701	2713	2732	2733	2737	2741	2750	2758	2760	2771	2781	2789	2797	2806	2814	2832	2834	2842	2849	2876	2880	2900	2905	2913	2917	2920	2930	2954	2966	2967	2978	2984	2991	2997	3006	3012	3026	3032	3054	3059	3061	3067	3072	3086	3089	3114	3146	3153	3158	3160	3164	3179	3184	3198	3214	3236	3247	3265	3268	3276	3280	3293	3303	3304	3311	3322	3343	3345	3352	3366	3379	3385	3389	3401	3407	3420	3426	3430	3435	3441	3443	3465	3475	3482	3491	3499	3502	3506	3510	3515	3517	3543	3554	3562	3575	3587	3594	3595	3604	3612	3613	3617	3637	3642	3659	3665	3684	3688	3699	3711	3715	3719	3720	3724	3729	3742	3753	3759	3767	3774	3785	3788	3793	3825	3864	3868	3878	3886	3891	3893	3896	3901	3911	3920	3940	3956	3968	3978	3982	3992	4000	4006	4011	4041	4062	4077	4080	4087	4090	4109	4112	4123	4127	4129	4133	4139	4156	4167	4170	4176	4203	4206	4214	4233	4237	4242	4245	4277	4289	4304	4343	4372	4374	4391	4398	4426	4444	4468	4494	4508	4530	4552	4574	4585	4592	4623	4658	4667	4670	4675	4687	4713	4799	4851	4867	4873	4886	4890	4935	4959	4963	4977	4980	5003	5040	5047	5073	5085	5095	5096	5133	5143	5144	5174	5185	5189	5197	5206	5232	5250	5258	5277	5280	5287	5303	5318	5339	5363	5365	5368	5377	5423	5466	5469	5488	5512	5522	5531	5553	5620	5639	5655	5658	5666	5672	5677	5732	5743	5774	5783	5796	5809	5816	5842	5856	5860	5865	5907	5931	5977	5987	6028	6051	6068	6069	6092	6097	6117	6132	6147	6151	6162	6184	6203	6213	6241	6287	6291	6303	6345	6347	6435	6439	6447	6474	6503	6510	6528	6536	6580	6586	6610	6624	6636	6641	6650	6690	6691	6730	6734	6749	6767	6790	6792	6802	6806	6815	6834	6846	6906	6952	6958	6963	6984	6989	6991	7073	7114	113598	113600	113605	113607	113608	113617	113618	113619	113620	113629	113630	113634	113635	113636	113640	113648	113650	113651	113663	113764	113765	113770	113771	113772	113773	113774	113786	113788	113791	113793	113795	113814	113817	113833	113839	113843	113844	113848	113855	113864	113865	113895	113946	113958	113964	113975	113980	114003	114009	114011	114015	114019	114025	114026	114027	114029	114033	114034	114041	114043	114044	114052	114053	114054	114055	114065	114067	114085	114104	114109	114121	114134	114137	114140	114141	114155	114158	114159	114161	114162	114164	114168	114188	114211	114214	114220	114224	114228	114240	114244	114246	114247	114250	114252	114260	114262	114277	114278	114291	114302	114303	114304	114305	114308	114338	114340	114351	114359	114368	114369	114372	114377	114385	114394	114400	114431	114440	114448	114450	114454	114455	114456	114459	114460	114473	114511	114521	114526	114527	114531	114639	114644	114652	114655	114667	114675	114676	114679	114680	114696	114698	114705	114712	114713	114714	114716	114718	114719	114723	114726	114727	114730	114732	114735	114737	114740	114741	114744	114749	114751	114758	114759	114770	114778	114779	114780	114781	114782	114783	114784	114788	114794	114804	114822	114834	114843	114844	114846	114874	114882	114884	114893	114896	114898	114899	114905	114906	114910	114916	114917	114918	114919	114920	114921	114925	114927	114938	114939	114947	114955	114971	114975	114977	114981	114993	114994	114997	115002	115006	115008	115022	115039	115040	115041	115043	115061	115076	115081	115083	115098	115108	115109	115110	115116	115129	115138	115142	115156	115185	115191	115209	115217	115267	115274	115276	115278	115281	115284	115309	115311	115314	115315	115316	115321	115322	115323	115327	115329	115330	115334	115335	115345	115346	115348	115349	115353	115362	115364																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																													";
            ArrayList cloneids = Algorithms.splitString( m_items);
          
        
         
         */  
        }
        catch(Exception e){}
     
        
       
        System.exit(0);
     }
     
      
}
