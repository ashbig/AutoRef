/*
 * NoMatchReportRunner.java
 *
 * Created on March 4, 2004, 1:35 PM
 */

package edu.harvard.med.hip.bec.action_runners;


import java.sql.*;
import java.io.*;


import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.export.*;
import edu.harvard.med.hip.bec.programs.blast.*;
import edu.harvard.med.hip.bec.bioutil.*;
import  edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
  import java.util.*;
  import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  HTaycher
 */
public class NoMatchReportRunner extends ProcessRunner 
{
  
    
    private String      m_blastable_dbname = null;
    private int         m_blast_minimum_stretch = 100;
    private double      m_blast_identity = 95.0;
    private String      m_id_type = null;
    
    public void         setPassParamBlastIdentity(double i){m_blast_identity = i;}
    public void         setPassParamBlastMinimumStretch(int i){m_blast_minimum_stretch = i;}
    public void         setBlastableDBName(String v){ m_blastable_dbname = v;}
    public void         setIdTypeToDisplay(String v){ if ( v != null && v.length() != 0) m_id_type = v;}
    public String getTitle()    { return "Request for NO MATCH report.";    }
    
    
    public void run()
    {
        // ArrayList file_list = new ArrayList();
        ArrayList items = new ArrayList();
        File report = null;
        Hashtable blast_clone_reports = new Hashtable ();
        try
        {
             BlastWrapper blaster = new BlastWrapper();
            //set matrix to small sequences if not specified by user
            blaster.setProgramName("blastn");
           blaster.setFormat(8);blaster.setGI("T");blaster.setHitNumber(10);blaster.setFilter("F");
           blaster.setDB( m_blastable_dbname);
                   
           items = Algorithms.splitString( m_items);
           ArrayList clones = getCloneInfo(items);
           processClones(clones, blaster, blast_clone_reports);
           report = printReport(clones, blast_clone_reports);
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
    
    
    //------------------------------------
   
     private ArrayList getCloneInfo( ArrayList items) throws BecDatabaseException
     {
        ArrayList clones = new ArrayList();String additional_id = null;
        String sql = null;
        UICloneSample clone = null;
        ResultSet rs = null;
        sql = constructQueryString(items);
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                 clone = new UICloneSample();
                 clone.setCloneId (rs.getInt("CLONEID"));
                 clone.setFLEXRefSequenceId(rs.getInt("FLEXSEQUENCEID"));
                 clone.setSequenceId (rs.getInt("CLONESEQUENCEID"));
                 clone.setConstructId (rs.getInt("CONSTRUCTID"));
                 clone.setPlateLabel (rs.getString("LABEL"));
                 clone.setPosition (rs.getInt("POSITION"));
                  clone.setIsolateTrackingId (rs.getInt("ISOLATETRACKINGID"));
                  try
                  {
                  if ( m_id_type.equalsIgnoreCase( PublicInfoItem.GI ) || m_id_type.equalsIgnoreCase( PublicInfoItem.SGD )
                        ||  m_id_type.equalsIgnoreCase( PublicInfoItem.PANUMBER ))
                    {
                        additional_id = PublicInfoItem.getCloneAdditionalId (clone.getFLEXRefSequenceId(), m_id_type);
                    }
                  if (additional_id != null)                 clone.setSampleType( additional_id);
                  }
                  catch(Exception e){ throw new BecDatabaseException("Cannot get "+m_id_type+" for clone "+clone.getCloneId() +"\n");}
                 clones.add(clone);
            }
            return clones;
            
        }
        catch(Exception e)
        {
          throw new BecDatabaseException("Cannot get data for clone "+e.getMessage() +"\n"+sql);
        }
    }
    
      
   
    private String constructQueryString( ArrayList items)
    {
        String sql = null;
        switch (m_items_type)
        {
            case Constants.ITEM_TYPE_PLATE_LABELS://plates
            {
                StringBuffer plate_names = new StringBuffer();
                for (int index = 0; index < items.size(); index++)
                {
                    plate_names.append( "'");
                    plate_names.append((String)items.get(index));
                    plate_names.append("'");
                    if (index == 12) break;
                    if ( index != items.size()-1 ) plate_names.append(",");
                }
                return "select FLEXSEQUENCEID,LABEL, POSITION, flexcloneid  as CLONEID,"
     +" a.SEQUENCEID as CLONESEQUENCEID,sc.refsequenceid as refsequenceid,  i.CONSTRUCTID,  i.ISOLATETRACKINGID as ISOLATETRACKINGID "
    +" from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a ,"
    +" sequencingconstruct sc where f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
    +" and sc.constructid(+)=i.constructid and   s.containerid=c.containerid and a.isolatetrackingid(+) =i.isolatetrackingid "
    +" and i.status=" +IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED_NO_MATCH+" and s.containerid in (select containerid from containerheader where label in ("
    +plate_names.toString()+")) order by s.containerid,position";
            } 
            case Constants.ITEM_TYPE_CLONEID:
            {
                 return "select FLEXSEQUENCEID,LABEL, POSITION,  flexcloneid  as CLONEID,"
        +"   a.SEQUENCEID as CLONESEQUENCEID, sc.refsequenceid as refsequenceid,  i.CONSTRUCTID,  i.ISOLATETRACKINGID as ISOLATETRACKINGID  "
        +"  from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a ,"
        +" sequencingconstruct sc where  f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
        +" and sc.constructid(+)=i.constructid and   s.containerid=c.containerid and a.isolatetrackingid(+) =i.isolatetrackingid "
        +"  and i.status=" +IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED_NO_MATCH+"and flexcloneid in ("+Algorithms.convertStringArrayToString(items,"," )+") ";
            }
            default : return "";
        }
       
   
    }
   
    
    private void            processClones(ArrayList clones, BlastWrapper blaster,  Hashtable blast_clone_reports)
    {
         UICloneSample clone = null;
         for (int clone_count=0; clone_count < clones.size(); clone_count++)
         {
                 clone = (UICloneSample)clones.get(clone_count);
                 processClone(clone, blaster,  blast_clone_reports);
                
         }
    }
    
    private void            processClone(UICloneSample clone, BlastWrapper blaster, Hashtable blast_clone_reports)
    {
        String sequence = null; ArrayList hits = null;
        IsolateTrackingEngine cur_engine = null;
        Read read = null;String match_info = null;
        BlastCloneReport blast_clone_report = new BlastCloneReport();
        blast_clone_report.setCloneId(clone.getCloneId());
        BlastMatch blast_match = null;
        try
        {
             if (clone.getSequenceId() != 0)//assembles sequence exist
            {
                sequence = BaseSequence.getSequenceInfo( clone.getSequenceId(), BaseSequence.SEQUENCE_INFO_TEXT);
                hits = runBlast( sequence,  clone.getSequenceId(),  blaster);
                blast_match = confirmORFMatch(hits, clone.getFLEXRefSequenceId());
                blast_clone_report.addMatch(blast_match);
            }
            else
            {
                cur_engine =IsolateTrackingEngine.getIsolateTrackingEngineById( clone.getIsolateTrackingId(),    null, null, 0);
                for (int read_count = 0; read_count < cur_engine.getEndReads().size(); read_count++)
                {
                    read = (Read)cur_engine.getEndReads().get(read_count);
                    sequence = read.getTrimmedSequence();
                    hits=   runBlast( sequence,  read.getSequenceId(),  blaster);
                    blast_match = confirmORFMatch(hits, clone.getFLEXRefSequenceId());
                    blast_clone_report.addMatch(blast_match);
                }
            }
             
             blast_clone_reports.put(new Integer(clone.getCloneId()),blast_clone_report);
                     
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot process clone "+clone.getCloneId()+"\n"+e.getMessage());
        }
        
                
    }
    
    private void            setMatrix(int sequence_length, BlastWrapper blaster)
    {
        if (sequence_length < 100) blaster.setMatrix( "PAM30");
        else if (sequence_length>100 && sequence_length<150) blaster.setMatrix("PAM70");
        else blaster.setMatrix("BLOSUM62");
    }
    
    
    private ArrayList            runBlast(String sequence, int seq_id, BlastWrapper blaster) throws Exception
    {
          String queryFile = SequenceManipulation.makeQueryFileInFASTAFormat(Constants.getTemporaryFilesPath(),sequence, "bn", ""+ seq_id);
          setMatrix(sequence.length(),blaster);
         blaster.setInputFN(queryFile+".in");
         blaster.setOutputFN(queryFile+".out");
         blaster.run();
         ArrayList hits = BlastParserNew.parse(queryFile+".out", 8);
         return hits;             
    }
    
     private BlastMatch confirmORFMatch(ArrayList blast_output, int flexseqid)
    {
        BlastMatch res = null;
         if (blast_output.size() < 1) return null;
        //take best hit
        BlastResult blresult = null;
        
        BlastAligment blalm = null;
        for (int count = 0 ; count <  blast_output.size(); count++)
        {
            blresult=(BlastResult)blast_output.get(count);
            blalm =(BlastAligment) blresult.getAligments().get(0);
            //if discrepancy matched 100% by identity on the whole length - confirm it
            boolean isConfirm = blalm.getIdentity() >= m_blast_identity
                && ( blalm.getQStop()-blalm.getQStart() + 1)>= m_blast_minimum_stretch;
            if ( isConfirm && count == 0)
            {
                res = new BlastMatch();
                if (blresult.getAcesession()== null )
                {
                    res.setId( String.valueOf( blalm.getSequenceId()));
                }
                else if (blalm.getSequenceId() == -1)
                    res.setId( blresult.getAcesession());
                
                res.setIdentity( blalm.getIdentity() );
                res.setStretchLength( blalm.getQStop()-blalm.getQStart());
            }
            /*if ( isConfirm && count > 0 && blresult.getAcesession().equalsIgnoreCase(""+ flexseqid))
            {
                res+= "\t\tref sequence hit \t"+blresult.getAcesession()+"\t" ;
                res +=  blalm.getSequenceId()+ "\t"+ blalm.getIdentity() +"/"+ ( blalm.getQStop()-blalm.getQStart())+"\t";
            }
             **/
            if ( flexseqid == blalm.getSequenceId()) break;
            
        }
        return res;     
    }
    private File            printReport(ArrayList clones, Hashtable blast_clone_reports)
    {
        String title = " Clone Id\tPlate Label\tPosition\t Exspected FLEX Sequence Id\tConstructId\tSummary\tDetails\n";
        FileWriter in = null; 
        File report = new File(Constants.getTemporaryFilesPath() + "NoMatchReport"+System.currentTimeMillis()+".txt");
        try
        {
            in = new FileWriter(report);
            in.write(title);
            for (int clone_count =0; clone_count<clones.size(); clone_count++)
            {
                  in.write( getCloneEntry( (UICloneSample)clones.get(clone_count) ,blast_clone_reports ));
            }
            in.flush();
            in.close();
            
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot create report file.");
        }
        return report;
    }
    
    private String getCloneEntry(UICloneSample clone, Hashtable blast_clone_reports)
    {
         /*Clone Id\t
          Plate Label\t
          Position\t 
          Exspected FLEX Sequence Id\t
          ConstructId\t
          Assembled Sequence best match\t
          Reads best matches"*/
        StringBuffer clone_info = new StringBuffer();
        clone_info.append( clone.getCloneId ()+"\t");
        clone_info.append( clone.getPlateLabel ()+"\t");
        clone_info.append( clone.getPosition ()+"\t");
        clone_info.append(  clone.getFLEXRefSequenceId()+"\t");
        if ( m_id_type != null)clone_info.append(clone.getSampleType ()+"\t");
        clone_info.append(clone.getConstructId ()+"\t");
        BlastCloneReport blast_clone_report = (BlastCloneReport)blast_clone_reports.get( new Integer(clone.getCloneId ()));
        clone_info.append(     blast_clone_report.getSummary(m_id_type)+"\t");
        clone_info.append(     blast_clone_report.getDetailedReport()+"\t");
        return clone_info.toString()+"\n";
    }
    
    
    private class BlastCloneReport
    {
        private         int         m_cloneid = -1;
        private         String      m_summary = null;
        private         ArrayList   m_matches = null;
        /** Creates a new instance of BlastMatch */
        public BlastCloneReport()        {        }

        public         int         getCloneId (){ return m_cloneid ;}
        public         String      getSummary (String name_value)
        { 
            if ( m_summary == null) processSummary(name_value);
            return m_summary ;
        }
        public         ArrayList   getMatches (){ return m_matches ;}
        public          String    getDetailedReport()
        {
            String report = "";
            for (int count = 0; count < m_matches.size();count++)
            {
                report +=((BlastMatch)m_matches.get(count)).getReport()+"\t";
            }
            return report;
        }
        
        
        public         void        setCloneId (int v){ m_cloneid=v ;}
        public         void         setSummary (String v){  m_summary=v ;}
        public         void        setMatches (ArrayList v){  m_matches =v;}
        public          void        addMatch(BlastMatch v){ if(m_matches==null) m_matches = new ArrayList(); if (v != null)m_matches.add(v);}
        
        private void processSummary(String name_value) 
        {
            if (m_matches == null || m_matches.size() < 1)
                m_summary="No Match found in Database";
            else 
            {
                String previous_id = ((BlastMatch)m_matches.get(0)).getId();
                for (int count = 0 ; count <m_matches.size(); count++)
                {
                    if ( ! previous_id.equalsIgnoreCase(((BlastMatch)m_matches.get(count)).getId() ))
                    { m_summary = "Missmatch"; return;}
                    previous_id = ((BlastMatch)m_matches.get(count)).getId();
                }
                m_summary = "Match to "+previous_id+" sequence id";
                String additional_id = null;
                try
                {
                    if ( name_value.equalsIgnoreCase(PublicInfoItem.GI  ) || name_value.equalsIgnoreCase( PublicInfoItem.SGD )
                        ||  name_value.equalsIgnoreCase( PublicInfoItem.PANUMBER ))
                    {
                        additional_id = PublicInfoItem.getCloneAdditionalId (Integer.parseInt(previous_id), name_value);
                    }
                } catch(Exception e){m_error_messages.add("Cannot get information for flex sequence "+ previous_id);}
              
                if (additional_id != null) m_summary += "("+additional_id+")";
                
            }
        }
    }
    
    private class  BlastMatch
    {
        private String     m_id = null;
        private double      m_identity = 0;
        private int         m_stretch_length = 0;
        private String      m_info = null;
        
        public BlastMatch(){}
        
        public String     getId (){ return m_id ;}
        public double      getIdentity (){ return m_identity ;}
        public int         getStretchLength (){ return m_stretch_length ;}
        public String      getInfo (){ return m_info ;}
        
        public void          setId (String v){  m_id = v ;}
        public void         setIdentity (double v){  m_identity=v ;}
        public void         setStretchLength (int v){  m_stretch_length=v ;}
        public void         setInfo (String v){  m_info=v ;}
        
        public String       getReport()
        {
            return "Match sequence " +m_id +" Match parameters: identity - "+m_identity+"%, length - "+m_stretch_length;
        }

    }

    
    public static void main(String args[]) 
{
    NoMatchReportRunner runner = new NoMatchReportRunner();
      
    try
    {
         runner.setUser( AccessManager.getInstance().getUser("htaycher345","htaycher"));
         runner.setItems("YGS000357-1 YGS000357-2 ");
        runner.setItemsType( Constants.ITEM_TYPE_PLATE_LABELS);
          runner.setBlastableDBName("c:\\blast_db\\Yeast\\genes");     
          runner.setIdTypeToDisplay(PublicInfoItem.SGD);
        runner.run();    
    }catch(Exception e){}
    System.exit(0);
    }
    
   
}
