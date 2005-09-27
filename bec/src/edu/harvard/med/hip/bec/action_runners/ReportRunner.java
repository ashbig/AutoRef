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
	private boolean    m_ref_species_id= false; //      PA Number (for Pseudomonas project only)
	private boolean    m_ref_ids= false; //      SGA Number (for Yeast project only)
	private boolean    m_rank = false;
        private boolean    m_score = false;
        private boolean    m_read_length = false;//length of end reads high quality strip
        private boolean    m_clone_seq_cds_start = false;
        private boolean    m_clone_seq_cds_stop = false;
        private boolean    m_clone_seq_text = false;
        private boolean    m_assembly_attempt_status = false; 
        private String      m_report_title = "";
   
        private SpeciesIdHelper[] m_species_id_definitions = null;

        
    public String       getTitle()     { return "Request for report generator.";     }
    
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
                    Object ref_ids, //      PA Number (for Pseudomonas project only)
                    Object ref_species_id, //      SGA Number (for Yeast project only)
                    Object rank ,//      Leave Sequence Info Empty for Empty Well
                    Object read_length,
                    Object score,
                     Object clone_seq_cds_start ,
                    Object clone_seq_cds_stop ,
                    Object clone_seq_text ,
                    Object assembly_attempt_status
                 )
     {
         StringBuffer report_title = new StringBuffer();
        if( clone_id!= null){ m_clone_id= true; report_title.append( "Clone ID\t");} //    Clone Id
        if( plate_label != null) {m_plate_label= true;  report_title.append(  "Plate Label\t");}// Plate Label
        if( sample_id != null) {m_sample_id= true;  report_title .append(  "Sample ID\t");}//      Sample Id
        if( sample_type != null){ m_sample_type= true; report_title.append(  "Sample Type\t");} //      Sample Type
        if( position != null) {m_position= true;  report_title.append(  "Position\t");}//      Sample Position
        if ( score!= null) {m_score= true;  report_title.append(  "Clone Score\t");}//
        if ( rank!= null) {m_rank= true;  report_title.append(  "Clone Rank\t");}//
        if( dir_name != null) {m_dir_name = true;  report_title .append(  "Clone Directory Name\t");}// Directory Name
        if ( read_length != null) {m_read_length= true;  report_title.append(  "End reads length: Forward/Reverse\t");}
      
        if( ref_sequence_id != null){ m_ref_sequence_id= true;  report_title .append( "REF: Bec ID\tREF: FLEX Id\t ");}//      Sequence ID
        if( ref_cds_start != null) {m_ref_cds_start= true; report_title.append(  "REF:CDS Start\t");} //      CDS Start
        if( ref_cds_stop != null) {m_ref_cds_stop= true; report_title .append( "REF:CDS Stop\t");}//      CDS Stop
        if( ref_cds_length != null){ m_ref_cds_length= true; report_title.append( "REF:CDS Length\t");}//      CDS Length
        if( ref_gi != null) {m_ref_gi= true; report_title .append(  "REF:GI\t");}//      GI Number
        if( ref_gene_symbol != null){ m_ref_gene_symbol= true;report_title .append(  "REF:Gene Symbol\t");} //      Gene Symbol
        if( ref_species_id != null) 
        {
            m_ref_species_id = true;
            m_species_id_definitions = SpeciesIdHelper.biuldSpeciesIdDefinitions();
               report_title.append("REF: Species Specific ID Name"+ Constants.TAB_DELIMETER);
             report_title.append("REF: Species Specific ID Value"+ Constants.TAB_DELIMETER);
        }//      PA Number (for Pseudomonas project only)
        if( ref_ids != null) {m_ref_ids= true;report_title .append(  "REF: All Sequence IDs\t");} //      SGA Number (for Yeast project only)
        if( ref_gc != null) {m_ref_gc= true; report_title.append( "REF:GC\t");}//     GC Content
        if( ref_seq_text != null) {m_ref_seq_text= true; report_title .append(  "REF:Text\t");}//      Sequence Text
        if( ref_cds != null) {m_ref_cds= true; report_title .append(  "REF:CDS \t");}//     CDS
       
        if ( assembly_attempt_status != null){ m_assembly_attempt_status = true; report_title .append(  "Clone: Sequence Assembly Attempt Status\t");}
        if( clone_seq_id != null){ m_clone_seq_id= true; report_title.append(  "Clone:Sequence Id\tClone:Sequence Type\t");}//      Clone Sequence Id
        if( clone_status != null) {m_clone_status = true;report_title .append(  "Clone: Sequence Status\t");}//      Clone Sequence Analysis Status
        if( clone_seq_cds_start != null){ m_clone_seq_cds_start= true;report_title .append( "Clone:Cds Start\t");}
        if( clone_seq_cds_stop != null) {m_clone_seq_cds_stop= true;report_title .append(  "Clone:Cds Stop\t");}
        if( clone_seq_text != null) {m_clone_seq_text= true;report_title .append( "Clone:Sequence Text\t");}
         if( clone_discr_high != null) {m_clone_discr_high= true;report_title .append(  "Clone:Discrepancy High quality\t");} //    Discrepancies High Quality (separated by type)
        if( clone_disc_low != null) {m_clone_disc_low= true; report_title .append(  "Clone:Discrepancy Low Quality\t");}//   Discrepancies Low Quality (separated by type)
        m_report_title = report_title.toString();
     }

    public void run()
    {
        // ArrayList file_list = new ArrayList();
        ArrayList sql_groups_of_items = new ArrayList();
        File report = null;
        Hashtable refsequences = new Hashtable();//containes refsequences by bec id
        Hashtable reads = new Hashtable();//containes reads by isolatetrackingid
        try
        {
           String  report_file_name =    Constants.getTemporaryFilesPath() + "GeneralReport"+ System.currentTimeMillis()+ ".txt";
          
            //convert item into array
           sql_groups_of_items =  prepareItemsListForSQL();
           for (int count = 0; count < sql_groups_of_items.size(); count++)
           {
              ArrayList clones = getCloneInfo(m_items_type, (String) sql_groups_of_items.get(count));
              refsequences= extractRefSequences( clones);
               if ( m_read_length )               reads = extractReads(clones);
              printReport(report_file_name,clones,refsequences,reads, count);
          }
          m_file_list_reports.add(new File(report_file_name));   
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
    
    
    private ArrayList getCloneInfo(int submission_type, String sql_items) throws Exception
    {
        ArrayList clones = new ArrayList();
        String sql = null;
        UICloneSample clone = null;
        Hashtable processed_clone_ids = new Hashtable();
        Integer current_clone_id = null;
        ResultSet rs = null;
        sql = constructQueryString(submission_type, sql_items);
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                 clone = new UICloneSample();
                 clone.setCloneId (rs.getInt("CLONEID"));
                 if (clone.getCloneId() > 0 )
                 {
                    current_clone_id = new Integer(clone.getCloneId());
                    if (  processed_clone_ids.contains( current_clone_id ))
                    {
                        continue;
                    }
                 }
                 else 
                     current_clone_id = null;
                 clone.setCloneAssemblyStatus   (rs.getInt("assembly_status"));
                 clone.setPlateLabel (rs.getString("LABEL"));
                 clone.setPosition (rs.getInt("POSITION"));
                 clone.setSampleType (rs.getString("SAMPLETYPE"));
                 
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
                 if ( m_dir_name )
                    clone.setTraceFilesDirectory( getTraceFilesDirName( clone ));
                 if (  current_clone_id != null)
                 {
                     processed_clone_ids.put(current_clone_id, current_clone_id );
                 }
                 clones.add(clone);
            }
            return clones;
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot get data for clone "+e.getMessage() +"\n"+sql);
            throw new Exception();
        }
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
    }
    
    
    private String constructQueryString(int submission_type, String sql_items)
    {
        String sql = null;
        if ( submission_type == Constants.ITEM_TYPE_PLATE_LABELS)//plates
        {
            
            sql="select assembly_status , FLEXSEQUENCEID,LABEL, POSITION,  SAMPLETYPE, s.SAMPLEID as SAMPLEID,flexcloneid  as CLONEID,"
 +" i.STATUS as IsolateStatus,  a.SEQUENCEID as CLONESEQUENCEID, a.cdsstart as cloneseqcdsstart, a.cdsstop as clonesequencecdsstop, analysisSTATUS,  SEQUENCETYPE, "
+"sc.refsequenceid as refsequenceid,  i.CONSTRUCTID,  i.ISOLATETRACKINGID as ISOLATETRACKINGID, RANK, "
+" i.SCORE as SCORE   from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a ,"
+" sequencingconstruct sc where rownum<1000 and f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
+" and sc.constructid(+)=i.constructid and   s.containerid=c.containerid and a.isolatetrackingid(+) =i.isolatetrackingid "
+" and s.containerid in (select containerid from containerheader where label in ("
+sql_items+")) order by s.containerid,position, a.submissiondate desc";
        } 
        else if (submission_type == Constants.ITEM_TYPE_CLONEID)
        {
            sql="select assembly_status,FLEXSEQUENCEID,LABEL, POSITION,  SAMPLETYPE, s.SAMPLEID as SAMPLEID,flexcloneid  as CLONEID,"
 +" i.STATUS as IsolateStatus,  a.SEQUENCEID as CLONESEQUENCEID,  a.cdsstart as cloneseqcdsstart, a.cdsstop as clonesequencecdsstop,analysisSTATUS,  SEQUENCETYPE, "
+"sc.refsequenceid as refsequenceid,  i.CONSTRUCTID,  i.ISOLATETRACKINGID as ISOLATETRACKINGID, RANK, "
+" i.SCORE as SCORE   from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a ,"
+" sequencingconstruct sc where rownum<1000 and f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
+" and sc.constructid(+)=i.constructid and   s.containerid=c.containerid and a.isolatetrackingid(+) =i.isolatetrackingid "
+"  and flexcloneid in ("+sql_items+") order by s.containerid,position, a.submissiondate desc";
        }
       
        else if (submission_type == Constants.ITEM_TYPE_BECSEQUENCE_ID)//bec sequence id
        {
                sql="select assembly_status,FLEXSEQUENCEID,LABEL, POSITION,  SAMPLETYPE, s.SAMPLEID as SAMPLEID,flexcloneid  as CLONEID,"
 +" i.STATUS as IsolateStatus,  a.SEQUENCEID as CLONESEQUENCEID, a.cdsstart as cloneseqcdsstart, a.cdsstop as clonesequencecdsstop, analysisSTATUS,  SEQUENCETYPE, "
+"sc.refsequenceid as refsequenceid,  i.CONSTRUCTID,  i.ISOLATETRACKINGID as ISOLATETRACKINGID, RANK, "
+" i.SCORE as SCORE   from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a ,"
+" sequencingconstruct sc where rownum<1000 and f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
+" and sc.constructid(+)=i.constructid and   s.containerid=c.containerid and a.isolatetrackingid(+) =i.isolatetrackingid "
+"  and a.SEQUENCEID in ("+sql_items+") order by s.containerid,position,  a.submissiondate desc";
        }
        else if (submission_type == Constants.ITEM_TYPE_FLEXSEQUENCE_ID)
        {
            sql="select assembly_status,FLEXSEQUENCEID,LABEL, POSITION,  SAMPLETYPE, s.SAMPLEID as SAMPLEID,flexcloneid  as CLONEID,"
 +" i.STATUS as IsolateStatus,  a.SEQUENCEID as CLONESEQUENCEID, a.cdsstart as cloneseqcdsstart, a.cdsstop as clonesequencecdsstop, analysisSTATUS,  SEQUENCETYPE, "
+"sc.refsequenceid as refsequenceid,  i.CONSTRUCTID,  i.ISOLATETRACKINGID as ISOLATETRACKINGID, RANK, "
+" i.SCORE as SCORE   from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a ,"
+" sequencingconstruct sc where  rownum<1000 and f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
+" and sc.constructid(+)=i.constructid and   s.containerid=c.containerid and a.isolatetrackingid(+) =i.isolatetrackingid "
+"  and f.flexSEQUENCEID in ("+sql_items+") order by s.containerid,position,  a.submissiondate desc";

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
        UIRead read = null;
        StringBuffer isolatetrackingids = new StringBuffer();
        int number_of_isolates = 0;
        for (int index = 0; index <clones.size();index++)
        {
            clone= (UICloneSample)clones.get(index);
            if ( !clone.getSampleType().equals("ISOLATE") ) continue;
            isolatetrackingids.append( clone.getIsolateTrackingId() +",");
            number_of_isolates++;
        }
        if ( number_of_isolates == 0) return reads;//no isolates
        
        if ( isolatetrackingids.charAt(isolatetrackingids.length() -1) == ',' )
            isolatetrackingids.setCharAt(isolatetrackingids.length() -1, ' ');
        
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
            m_error_messages.add("Cannot get reads sql"+sql +"\n"+e.getMessage());
        }
        return reads;    
        
        
    }
    
    
     
     
  private void  printReport(String report_file_name, ArrayList clones,
                            Hashtable refsequences,Hashtable reads,
                            int write_cycle)
  {
    //   String report_file_name = null;
        String temp = null;
         
        FileWriter fr = null;
        UICloneSample clone = null;
        try
        {
            fr =  new FileWriter(report_file_name, true);
             if (write_cycle == 0) fr.write(m_report_title+"\n");        
            for (int count = 0; count < clones.size(); count++)
            {
                
                clone= (UICloneSample) clones.get(count);
                fr.write(writeClone(clone,refsequences,reads)+"\n");

            }
            fr.flush();
            fr.close();
         //  return new File(report_file_name);
        }
        catch(Exception e){ try { fr.close();}catch(Exception n){} }
      //  return null;
    
  }
  private String writeClone(UICloneSample clone,Hashtable refsequences,Hashtable reads )
  {
      StringBuffer cloneinfo= new StringBuffer();
      String species_specific_id_name = null;
      String species_data = null;
      CloneSequence clone_sequence = null;
      UIRead read = null;
      RefSequence refsequence = null; 
       ArrayList discrepancies =  null;boolean isItemsAnalized = true;
      // System.out.println(clone.getCloneId());
      if ( clone.getRefSequenceId()>0)
      {
         refsequence = (RefSequence)refsequences.get(new Integer(clone.getRefSequenceId()));
      }
      
      
     if(  m_clone_id){ cloneinfo.append(clone.getCloneId()+"\t");}//    Clone Id
    if( m_plate_label){ cloneinfo.append( clone.getPlateLabel ()+"\t");}// Plate Label
    if( m_sample_id){ cloneinfo.append(clone.getSampleId() +"\t");}//      Sample Id

    if(  m_sample_type ){ cloneinfo.append(clone.getSampleType()+"\t");} //      Sample Type
    if( m_position){ cloneinfo.append(clone.getPosition ()+"\t");}//      Sample Position
    if( m_score)
    {
        if(   clone.getSampleType().equals("ISOLATE")) { cloneinfo.append(clone.getScore ()+"\t");}
        else    {         cloneinfo.append("\t"); }
    }    
    if( m_rank)
    {
        if ( clone.getSampleType().equals("ISOLATE")   )   
        {
            cloneinfo.append( IsolateTrackingEngine.getRankStatusAsString(clone.getRank(), clone.getCloneStatus())+"\t");
        }
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
        if( m_ref_gi){ cloneinfo.append(refsequence.getPublicInfoParameter(PublicInfoItem.GI)+"\t");}//      GI Number
        if(  m_ref_gene_symbol){ cloneinfo.append(refsequence.getPublicInfoParameter(PublicInfoItem.GENE_SYMBOL)+"\t");} //      Gene Symbol
        if( m_ref_species_id)
        { 
            for (int count = 0; count < m_species_id_definitions.length; count++)
            {
                 if (m_species_id_definitions[count] != null)
                 { 
                      species_specific_id_name = m_species_id_definitions[count].getIdName();
                      if (refsequence.getPublicInfoParameter(species_specific_id_name) != null && 
                        refsequence.getPublicInfoParameter(species_specific_id_name).trim().length() != 0)
                      {
                          species_data = species_specific_id_name+Constants.TAB_DELIMETER;
                          species_data += refsequence.getPublicInfoParameter(species_specific_id_name) ;
                      }
                 }
            }
            if ( species_data == null)cloneinfo.append( Constants.TAB_DELIMETER);
            else cloneinfo.append( species_data);
             cloneinfo.append( Constants.TAB_DELIMETER);
        }//    
        if( m_ref_ids)
        { 
            
            ArrayList param_names = new ArrayList();
            param_names.add("GENE_SYMBOL"); param_names.add("GI");param_names.add(species_specific_id_name);
            param_names = refsequence.getPublicInfoParametersNotIncludedInList(param_names);
            for ( int cc = 0; cc < param_names.size(); cc++) 
            {
                cloneinfo.append( param_names.get(cc)+"|"); 
            }
             cloneinfo.append(Constants.TAB_DELIMETER);
        
        } //      SGA Number (for Yeast project only)
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
        if( m_ref_species_id){ cloneinfo.append("\t\t");}//      PA Number (for Pseudomonas project only)
        if( m_ref_ids){  cloneinfo.append("\t");} //      SGA Number (for Yeast project only)
        if( m_ref_gc){  cloneinfo.append("\t");}//     GC Content
        if( m_ref_seq_text){  cloneinfo.append("\t");}//      Sequence Text
        if( m_ref_cds){  cloneinfo.append("\t");}//     CDS
    }

     if ( m_assembly_attempt_status )
     { 
         if ( clone.getCloneId() == 0)
             cloneinfo.append("N/A\t");
         else
              cloneinfo.append(IsolateTrackingEngine.getAssemblyStatusAsString (clone.getCloneAssemblytStatus())+"\t");
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
  
        
        
        int[][] discrepancy_count  = null;
    if(  m_clone_discr_high || m_clone_disc_low )
    {     
        discrepancies = getCloneDiscrepancies(discrepancies ,clone);
        discrepancy_count  = DiscrepancyDescription.getDiscrepanciesSeparatedByType(discrepancies,true, false);
    }
    if(  m_clone_discr_high )
    {
        cloneinfo.append( DiscrepancyDescription.discrepancySummaryReport( discrepancy_count,Mutation.REGION_LINKER_5P, true, false));
        cloneinfo.append( DiscrepancyDescription.discrepancySummaryReport( discrepancy_count,Mutation.REGION_CDS, true, false));
        cloneinfo.append( DiscrepancyDescription.discrepancySummaryReport( discrepancy_count,Mutation.REGION_LINKER_3P, true, false));
        cloneinfo.append(Constants.TAB_DELIMETER ); 
    }
    if(  m_clone_disc_low  )
    {
        cloneinfo.append( DiscrepancyDescription.discrepancySummaryReport( discrepancy_count,Mutation.REGION_LINKER_5P, false, false));
        cloneinfo.append( DiscrepancyDescription.discrepancySummaryReport( discrepancy_count,Mutation.REGION_CDS, false, false));
        cloneinfo.append( DiscrepancyDescription.discrepancySummaryReport( discrepancy_count,Mutation.REGION_LINKER_3P, false, false));
        cloneinfo.append(Constants.TAB_DELIMETER ); 
    }
     return  cloneinfo.toString();
             
  }
  
    private String getTraceFilesDirName(UICloneSample clone )
    {
       if ( clone.getCloneId() < 1) return "";  
       EndReadsWrapperRunner wr = new EndReadsWrapperRunner();
       return  wr.getOuputBaseDir() + File.separator + clone.getFLEXRefSequenceId() +
       File.separator + clone.getCloneId();
       
    }
  
   
    
    private  ArrayList          getCloneDiscrepancies(ArrayList clone_discrepancies, 
                            UICloneSample clone ) 
    {
        ArrayList discrepancies = new ArrayList();
        ArrayList contigs = null; Stretch stretch = null;
        Read read = null;
        boolean is_cds_start_set = false;
        try
        {
            if ( clone.getSequenceId() > 0 )
            {
                 discrepancies = Mutation.getDiscrepanciesBySequenceId(clone.getSequenceId());
                 discrepancies = DiscrepancyDescription.assembleDiscrepancyDefinitions( discrepancies);
            }
            else 
            {    
                contigs = IsolateTrackingEngine.getStretches( clone.getIsolateTrackingId(), Stretch.GAP_TYPE_CONTIG  );
                contigs = Stretch.sortByPosition(contigs);
                if (contigs != null && contigs.size() > 0)
                {
                    for (int count = 0; count < contigs.size(); count++)
                    {
                        stretch = (Stretch) contigs.get(count);
                        if ( stretch.getAnalysisStatus() == -1) break;
                        discrepancies.addAll( stretch.getSequence().getDiscrepancies()); 
                    }
                    discrepancies = DiscrepancyDescription.assembleDiscrepancyDefinitions( discrepancies);
                }
                else
                {
                    contigs = Read.getReadByIsolateTrackingId( clone.getIsolateTrackingId() );
                    if (contigs != null && contigs.size() > 0 )
                    {
                        ArrayList read_1_discrepancies = null;ArrayList read_2_discrepancies = null;
                        for (int count_read = 0; count_read < contigs.size(); count_read++)
                        {
                            read = (Read) contigs.get(count_read);
                            if ( count_read == 0 ) read_1_discrepancies = read.getSequence().getDiscrepancies();
                            if (count_read == 1)read_2_discrepancies= read.getSequence().getDiscrepancies();
                        }
                        if ( ( read_1_discrepancies != null && read_1_discrepancies.size() > 0 ) &&
                            ( read_2_discrepancies != null && read_2_discrepancies.size() > 0 ))
                        {
                            discrepancies = DiscrepancyDescription.getDiscrepancyDescriptionsNoDuplicates(
                                                            read_1_discrepancies,read_2_discrepancies);
                        }
                        else if ( read_1_discrepancies != null && read_1_discrepancies.size() > 0 )
                        {
                            discrepancies = read_1_discrepancies;
                             discrepancies = DiscrepancyDescription.assembleDiscrepancyDefinitions( clone_discrepancies);
                 
                        }
                        else if ( read_2_discrepancies != null && read_2_discrepancies.size() > 0 )
                        {
                            discrepancies = read_2_discrepancies;
                             discrepancies = DiscrepancyDescription.assembleDiscrepancyDefinitions( clone_discrepancies);
                 
                        }
                       
                    }
                }
            }
             
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot extract discrepancies for clone : "+clone.getCloneId());
        }
        return discrepancies ;
    }
    
   
    
    //------------------------------------------
     public static void main(String args[]) 
     
    {
       // InputStream input = new InputStream();
        ReportRunner input = null;
        User user  = null;
        try
        {
                   BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
            sysProps.verifyApplicationSettings();
       edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();

             
            user = AccessManager.getInstance().getUser("htaycher123","htaycher");
            input = new ReportRunner();
             input.setInputData(Constants.ITEM_TYPE_FLEXSEQUENCE_ID,  "123 123456 435 684592 439 9054 5478 1243 548 345	 ");
             
             
             
             
             input. setFields(
                    " clone_id", //    Clone Id
                    " dir_name", // Directory Name
                    " sample_id", //      Sample Id
                    " plate_label", //      Plate Label
                    " sample_type", //      Sample Type
                    " position", //      Sample Position
                    " ref_sequence_id", //      Sequence ID
                    " clone_seq_id", //      Clone Sequence Id
                    " ref_cds_start", //      CDS Start
                    " clone_status",//      Clone Sequence Analysis Status
                    " ref_cds_stop", //      CDS Stop
                    " clone_discr_high", //    Discrepancies High Quality (separated by type)
                    " ref_cds_length", //      CDS Length
                    " clone_disc_low", //   Discrepancies Low Quality (separated by type)
                    " ref_gc", //     GC Content
                  null, //  " ref_seq_text", //      Sequence Text
                  null, //  " ref_cds", //     CDS
                    " ref_gi", //      GI Number
                    " ref_gene_symbol", //      Gene Symbol
                    " ref_ids", //      PA Number (for Pseudomonas project only)
                    " ref_species_id", //      SGA Number (for Yeast project only)
                    " rank ",//      Leave Sequence Info Empty for Empty Well
                    " read_length",
                    " score",
                     " clone_seq_cds_start ",
                    " clone_seq_cds_stop ",
                   null,// " clone_seq_text ",
                    " assembly_attempt_status"
                 );
             
           input.run();
        }
        catch(Exception e){}
     
        
       
        System.exit(0);
     }
     
      
}
