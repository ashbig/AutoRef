/*
 * Reporter.java
 *
 * Created on May 15, 2003, 2:30 PM
 */

package edu.harvard.med.hip.bec.action;

import java.io.*;
import java.sql.*;
import  edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;


import java.sql.*;
import java.util.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.programs.blast.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import javax.sql.*;
/**
 *
 * @author  htaycher
 */
public class Reporter
{
    private String  m_plate_name = null;
    /** Creates a new instance of Reporter */
    public Reporter(String plate_name)
    {
        m_plate_name = plate_name;
    }
    public String getPlateName(){ return m_plate_name;}
    public void simpleReport(String file_name)
    {
        FileWriter writer = null;
        String sql = "select position, flexsequenceid,flexcloneid, rank , score,iso.status as status "
        +" from flexinfo f, isolatetracking iso, sample s where f.isolatetrackingid=iso.isolatetrackingid and"
        +" s.sampleid=iso.sampleid and iso.isolatetrackingid in("
        +" select isolatetrackingid  from isolatetracking where sampleid in ( select sampleid from sample where containerid = "
        +" (select containerid from containerheader where label='" +m_plate_name+"') ) )";
        
        try
        {
            writer = new FileWriter(file_name);
            
            writer.write("Plate Label: " +m_plate_name);
            writer.write("\nPOSITION\t FLEXSEQUENCEID \t FLEXCLONEID\t RANK\t       SCORE\tStatus\n ");
            
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            RowSet rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                writer.write(rs.getInt("position") +"\t"+  rs.getInt("flexsequenceid")+"\t"+rs.getInt("flexcloneid")+"\t"+rs.getInt("rank")+"\t"+rs.getInt("score")+"\t"+rs.getInt("status")+"\n" );
            }
            
            writer.flush();
            writer.close();
        }
        catch(Exception e)
        {
        }
    }
    
    
    public ArrayList simpleReport1(String file_name, int mode)
    {
        FileWriter writer = null;
        String sql = "select  position,  sampletype,  s.sampleid,  score,  rank,  status from sample s, isolatetracking i"
        +" where s.sampleid=i.sampleid and containerid =(select containerid from containerheader where label='" +m_plate_name+"')";
        
        ArrayList wells=new ArrayList();WellReport well;
        try
        {
            
            
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            RowSet rs = t.executeQuery(sql);
            RowSet rs1 = null;
            while(rs.next())
            {
                well = new WellReport();
                well.setWellNumber(rs.getInt("position"));
                well.setSampleID(rs.getInt("sampleid") );
                well.setSampletype(rs.getString("sampletype"));
                well.setRank(rs.getInt("rank"));
                well.setScore(rs.getInt("score"));
                well.setStatus(rs.getInt("status"));
                rs1 = t.executeQuery("select resulttype,readid,readsequenceid   from result r, readinfo read where r.resultid=read.resultid and  sampleid ="+well.getSampleID() );
                while (rs1.next())
                {
                    if (rs1.getInt("resulttype") == -1)
                    {
                        well.setReadidf(rs1.getInt("readid") );
                        well.setSeqidF(rs1.getInt("readsequenceid") );
                    }
                    else if (rs1.getInt("resulttype") == 1)
                    {
                        well.setReadidr(rs1.getInt("readid") );
                        well.setSeqidR(rs1.getInt("readsequenceid") );
                    }
                }
                
                
                wells.add(well);
            }
            
            if (mode == 0) reportWriter( file_name,wells,  "\nPOSITION\tsampletype\t sampleid\t score\t RANK\tStatus\forw readid\t readseqid_f\t readid_r\t seqid_r\n ",0);
            
        }
        catch(Exception e)
        {
        }
        return wells;
    }
    
    
    
    public void reportWriter(String file_name,ArrayList wells, String title, int mode)
    {
        WellReport well;FileWriter writer;
        try
        {
            writer = new FileWriter(file_name);
            
            writer.write("Plate Label: " +m_plate_name);
            writer.write(title);
            for (int i = 0; i < wells.size(); i++)
            {
                well = (WellReport) wells.get(i);
                writer.write(well.toString(mode));
            }
            writer.flush();
            writer.close();
            
        }
        catch(Exception e)
        {
        }
    }
    /*
    public void fullReport(String file_name)
    {
        ArrayList wells = simpleReport1( file_name, 1);
        String data = ""; String mut_data ="";String title ;
        WellReport well;
        try
        {
            
            title ="\nPOSITION\t sample type\t sampleid \t RANK\t       SCORE \t status \t forward read \t f sequence id \t reverse read \t r sequenceid \t Discrepancy Number \t  ";
            for (int ind = 0; ind < 30 ; ind++)
            {
                title+= Mutation.getMutationTypeAsString( ind) +"\t";
            }
            
            title+="\n";
            
            
            
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            
            AnalyzedScoredSequence seq = null;
            RowSet rs;
            for( int i = 0; i < wells.size();i++ )
            {
                well = (WellReport) wells.get(i);
                data = "";  mut_data ="";
                ArrayList discrepancies_forward = new ArrayList();
                ArrayList discrepancies_reverse = new ArrayList();
                ArrayList discrepancies_pairs = new ArrayList();
                ArrayList rnaOnly = new ArrayList();
                int readseq_forward_id = well.getSeqidF();
                int readseq_reverse_id = well.getSeqidR();
                
                if (readseq_reverse_id != -1)
                {
                    seq = new AnalyzedScoredSequence(readseq_reverse_id);
                    discrepancies_reverse = seq.getDiscrepanciesByType(Mutation.RNA);
                }
                if (readseq_forward_id != -1)
                {
                    seq = new AnalyzedScoredSequence(readseq_forward_id);
                    discrepancies_forward = seq.getDiscrepanciesByType(Mutation.RNA);
                }
                
                if ( readseq_forward_id != -1 && readseq_reverse_id == -1 )
                {
                    discrepancies_pairs = DiscrepancyPair.assembleDiscrepanciesInPairs(discrepancies_forward);
                    
                }
                else  if ( readseq_forward_id == -1 && readseq_reverse_id != -1 )
                {
                    discrepancies_pairs = DiscrepancyPair.assembleDiscrepanciesInPairs(discrepancies_reverse);
                }
                else if ( readseq_forward_id != -1 && readseq_reverse_id != -1)
                {
                    discrepancies_pairs = DiscrepancyPair.getDiscrepancyPairsNoDuplicates(
                    discrepancies_reverse,
                    discrepancies_forward);
                    
                }
                for (int count = 0; count < discrepancies_pairs.size(); count++)
                {
                    rnaOnly.add( ( (DiscrepancyPair)discrepancies_pairs.get(count)).getRNADiscrepancy());
                }
                if (rnaOnly.size() > 0)
                {
                    int[] discr_by_type =null;// Mutation.getDiscrepanciesSeparatedByType(rnaOnly);
                    for (int ind = 0; ind < discr_by_type.length ; ind++)
                    {
                        mut_data +=discr_by_type[ind]+"\t";
                    }
                    well.setDiscrNumber( discrepancies_pairs.size());
                    well.setDiscData(mut_data);
                }
                
                
            }
            
            reportWriter( file_name,wells,  title,0);
            
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    
    */
    /*
     public void fullReportWithDiscrepancy(String file_name)
    {
        ArrayList wells = simpleReport1( file_name, 1);
        String data = ""; String mut_data ="";String title ;
        WellReport well;
        try
        {
            
            title ="\nPOSITION\t sample type\t sampleid \t RANK\t       SCORE \t status \t forward read \t f sequence id \t reverse read \t r sequenceid \t Discrepancy Number \t  ";
            for (int ind = 0; ind < 30 ; ind++)
            {
                title+= Mutation.getMutationTypeAsString( ind) +"\t";
            }
            
            title+="\n";
            
            
            
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            
            AnalyzedScoredSequence seq = null;
            RowSet rs;
            for( int i = 0; i < wells.size();i++ )
            {
                well = (WellReport) wells.get(i);
                data = "";  mut_data ="";
                ArrayList discrepancies_forward = new ArrayList();
                ArrayList discrepancies_reverse = new ArrayList();
                ArrayList discrepancies_pairs = new ArrayList();
                ArrayList rnaOnly = new ArrayList();
                int readseq_forward_id = well.getSeqidF();
                int readseq_reverse_id = well.getSeqidR();
                
                if (readseq_reverse_id != -1)
                {
                    seq = new AnalyzedScoredSequence(readseq_reverse_id);
                    discrepancies_reverse = seq.getDiscrepanciesByType(Mutation.RNA);
                }
                if (readseq_forward_id != -1)
                {
                    seq = new AnalyzedScoredSequence(readseq_forward_id);
                    discrepancies_forward = seq.getDiscrepanciesByType(Mutation.RNA);
                }
                
                if ( readseq_forward_id != -1 && readseq_reverse_id == -1 )
                {
                    discrepancies_pairs = DiscrepancyPair.assembleDiscrepanciesInPairs(discrepancies_forward);
                    
                }
                else  if ( readseq_forward_id == -1 && readseq_reverse_id != -1 )
                {
                    discrepancies_pairs = DiscrepancyPair.assembleDiscrepanciesInPairs(discrepancies_reverse);
                }
                else if ( readseq_forward_id != -1 && readseq_reverse_id != -1)
                {
                    discrepancies_pairs = DiscrepancyPair.getDiscrepancyPairsNoDuplicates(
                    discrepancies_reverse,
                    discrepancies_forward);
                    
                }
                for (int count = 0; count < discrepancies_pairs.size(); count++)
                {
                    rnaOnly.add( ( (DiscrepancyPair)discrepancies_pairs.get(count)).getRNADiscrepancy());
                }
                RNAMutation rna = null;
                if (rnaOnly.size() > 0)
                {
                    
                    for (int ind = 0; ind < rnaOnly.size() ; ind++)
                    {
                        rna = (RNAMutation)rnaOnly.get(ind);
                        mut_data +=rna.toString()+"\n";
                    }
                    well.setDiscrNumber( discrepancies_pairs.size());
                    well.setDiscData(mut_data);
                }
                else
                {
                     well.setDiscrNumber( discrepancies_pairs.size());
                }
                
                
            }
            
            reportWriter( file_name,wells,  title,0);
            
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    
    
    */
    public void AssemblyReport(String file_name)
    {
        ArrayList wells = new ArrayList();
        String sql = "select position, sampletype,iso.sampleid as sampleid , iso.score as score , refsequenceid, (cdsstop-cdsstart) as cdslength "
        +" from sequencingconstruct constr, refsequence refs, sample s, isolatetracking iso "
        
        +" where constr.refsequenceid=refs.sequenceid and s.sampleid=iso.sampleid and "
        +" iso.constructid=constr.constructid and iso.rank=1 and iso.constructid in "
        +" (select constructid from isolatetracking where sampleid in "
        +" (select sampleid from sample where containerid ="
        +" (select containerid from containerheader where label='"+m_plate_name+"'))) order by position";
        WellReport well;
        try
        {
            
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            RowSet rs = t.executeQuery(sql);
            RowSet rs1 = null;
            while(rs.next())
            {
                well = new WellReport();
                well.setWellNumber(rs.getInt("position"));
                well.setSampleID(rs.getInt("sampleid") );
                well.setSampletype(rs.getString("sampletype"));
                well.setScore(rs.getInt("score"));
               well.setRefSeqId  (rs.getInt("refsequenceid"));
               well.setRefSequenceCds(rs.getInt("cdslength"));
               well.setRank(1);
               well.setStatus(4);
                rs1 = t.executeQuery("select resulttype,readid,readsequenceid, (trimmedend-trimmedstart) as readlength   from result r, readinfo read where r.resultid=read.resultid and  sampleid ="+well.getSampleID() );
                while (rs1.next())
                {
                    if (rs1.getInt("resulttype") == -1)
                    {
                        well.setReadidf(rs1.getInt("readid") );
                        well.setSeqidF(rs1.getInt("readsequenceid") );
                        well.setReadFLength(rs1.getInt("readlength") );
                    }
                    else if (rs1.getInt("resulttype") == 1)
                    {
                        well.setReadidr(rs1.getInt("readid") );
                        well.setSeqidR(rs1.getInt("readsequenceid") );
                        well.setReadRLength(rs1.getInt("readlength") );
                    }
                }
                
                
                wells.add(well);
            }
            
            String  title ="\nPOSITION\tsampletype\t sampleid\t RANK \t score\tStatus\t forw readid\t readseqid_f\t read_trimmed_length\treadid_r\t seqid_r\t read_trimmed_length\t refsequenceid\t redsequence cds\n ";
            
            reportWriter( file_name,wells,  title,1);
        
    }
    catch(Exception e)
    {
        System.out.println(e.getMessage());
    }
}

class WellReport
{
    private int i_wellid =-1;
    private int i_sampleid =-1;
    private String i_sampletype=null;
    private int i_rank = -100;
    private int i_score =-1;
    private int i_status = -100;
    private int i_readf = -1;
    private int i_readr = -1;
    private int i_readf_seqid = -1;
    private int i_readr_seqid = -1;
    private int i_numberofmut = -1;
    private int i_readf_length = -1;
    private int i_readr_length = -1;
    private int i_refsequenceid = -1;
    private int i_refsequence_cds = -1;
    private String i_disc_data=null;
    
    public WellReport()
    {}
    public int getWellNumber()    { return i_wellid ;}
    public int getSampleID()    { return i_sampleid ;}
    public String getSampletype()    { return i_sampletype;}
    public int getRank()    { return i_rank  ;}
    public int getScore()    { return i_score ;}
    public int getStatus()    { return i_status  ;}
    public int getReadidf()    { return i_readf  ;}
    public int getReadidr()    { return i_readr  ;}
    public int getReadFLength()    { return i_readf_length  ;}
    public int getReadRLength()    { return i_readr_length  ;}
    public int getSeqidF()    { return i_readf_seqid  ;}
    public int getSeqidR()    { return i_readr_seqid  ;}
    public int getDiscr()    { return i_numberofmut  ;}
    public int getRefSeqId  ()    { return i_refsequenceid  ;}
    public int getRefSequenceCds  ()    {   return i_refsequence_cds  ;}
    
    
    public void setWellNumber(int r)    {  i_wellid = r;}
    public void setSampleID(int r)    {  i_sampleid = r;}
    public void setSampletype(String r)    {  i_sampletype= r;}
    public void setRank(int r)    {  i_rank  = r;}
    public void setScore(int r)    {  i_score = r;}
    public void setStatus(int r)    {  i_status  = r;}
    public void setReadidf(int r)    {  i_readf  = r;}
    public void setReadidr(int r)    {  i_readr  = r;}
    public void setSeqidF(int r)    {  i_readf_seqid  = r;}
    public void setSeqidR(int r)    {  i_readr_seqid  = r;}
    public void setDiscrNumber(int r)    {  i_numberofmut  = r;}
    public void setDiscData(String s)    {i_disc_data=s;}
    public void setReadFLength(int v)    {  i_readf_length =v ;}
    public void setReadRLength(int v)    {  i_readr_length  =v;}
    public void setRefSeqId  (int v)    {i_refsequenceid  =v;}
    public void setRefSequenceCds  (int v)    {i_refsequence_cds  =v;}
    
    public String toString(int mode)
    {
        String res =  i_wellid +"\t"+ i_sampletype+"\t"+ i_sampleid +"\t"+ i_rank +"\t"+ i_score +"\t"
        +  i_status+"\t" +i_readf+ "\t"+i_readf_seqid;
        
        if (mode ==1) res +="\t"+ i_readf_length;
        res+="\t"+ i_readr +"\t"+ i_readr_seqid ;
        if (mode ==1) res +="\t"+ i_readr_length;
        
        if (i_numberofmut != -1)res+="\t"+i_numberofmut+"\t"+i_disc_data;
        if (mode==1) res +="\t"+i_refsequenceid+"\t"+i_refsequence_cds;
        return res+"\n";
    }
    
}


public static void main(String args[])
{
    
    try
    {
        //  Reporter reporter = new Reporter("YGS000370-2");
        
        //  reporter.simpleReport("c:\\tmp\\simple"+"YGS000370-2" +".txt");
        // reporter.simpleReport1("c:\\tmp\\simple1"+"YGS00030-2" +".txt");
        Reporter reporter = new Reporter("YGS000359-1");
     //   reporter.fullReportWithDiscrepancy("c:\\yeastbackup\\discr-"+reporter.getPlateName() +".txt");
        //  reporter.simpleReport1("c:\\yeastbackup\\simple1-40att-"+reporter.getPlateName() +".txt",0);
     //  reporter.fullReport("c:\\yeastbackup\\full"+reporter.getPlateName() +".txt");
     //   reporter.AssemblyReport("c:\\yeastbackup\\assembly_"+reporter.getPlateName() +".txt");
     //   reporter = new Reporter("YGS000361-3");
   //       reporter.simpleReport1("c:\\yeastbackup\\simple1-40att-"+reporter.getPlateName() +".txt",0);
  //     reporter.fullReport("c:\\yeastbackup\\full"+reporter.getPlateName() +".txt");
     //   reporter.AssemblyReport("c:\\yeastbackup\\assembly_"+reporter.getPlateName() +".txt");
      
    }
    catch(Exception e)
    {}
    System.exit(0);
}
}
