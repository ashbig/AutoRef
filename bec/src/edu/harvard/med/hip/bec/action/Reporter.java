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
    
    public void simpleReport(String file_name)
    {
       FileWriter writer = null;
       String sql = "select position, flexsequenceid,flexcloneid, rank , score"
+" from flexinfo f, isolatetracking iso, sample s where f.isolatetrackingid=iso.isolatetrackingid and"
+" s.sampleid=iso.sampleid and iso.isolatetrackingid in("
+" select isolatetrackingid  from isolatetracking where sampleid in ( select sampleid from sample where containerid = "
+" (select containerid from containerheader where label='" +m_plate_name+"') ) )";

        try
        {
            writer = new FileWriter(file_name);
            
            writer.write("Plate Label: " +m_plate_name);
            writer.write("\nPOSITION\t FLEXSEQUENCEID \t FLEXCLONEID\t RANK\t       SCORE\n ");
            
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            RowSet rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                writer.write (rs.getInt("position") +"\t"+  rs.getInt("flexsequenceid")+"\t"+rs.getInt("flexcloneid")+"\t"+rs.getInt("rank")+"\t"+rs.getInt("score")+"\n" );
            }
            
            writer.flush();
            writer.close();
        }
        catch(Exception e)
        {
        }
    }
    
    
      public void fullReport(String file_name)
    {
       FileWriter writer = null;
       String sql = "select s.position, i.score, i.rank, r1.readid, r1.readsequenceid forw_sequenceid, r2.readid, r2.readsequenceid rev_sequenceid "
       +"from sample s, isolatetracking i, (select * from readinfo where readtype=1) r1,"
       +"(select * from readinfo where readtype=-1) r2 where s.sampleid=i.sampleid "
       +" and i.isolatetrackingid=r1.isolatetrackingid(+) and i.isolatetrackingid=r2.isolatetrackingid(+)"
       +" and s.containerid= (select containerid from containerheader where label='" +m_plate_name+"') ";
String data = ""; String mut_data ="";
        try
        {
            writer = new FileWriter(file_name);
            
            writer.write("Plate Label: " +m_plate_name);
            writer.write("\nPOSITION\t  RANK\t       SCORE\t  forward read\t reverse read \t Discrepancy Number \t  ");
            for (int ind = 0; ind < 30 ; ind++)
            {
                writer.write( Mutation.  getMutationTypeAsString( ind) +"\t");
            }
            
            writer.write("\n");
            writer.flush();
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            RowSet rs = t.executeQuery(sql);
            AnalyzedScoredSequence seq = null; 
           
            while(rs.next())
            {
                 data = "";  mut_data ="";
                  ArrayList discrepancies_forward = new ArrayList();
                ArrayList discrepancies_reverse = new ArrayList();
                ArrayList discrepancies_pairs = new ArrayList();
                ArrayList rnaOnly = new ArrayList();
                int readseq_forward_id = rs.getInt("forw_sequenceid");
                int readseq_reverse_id = rs.getInt("rev_sequenceid");
                
                if (readseq_reverse_id != 0)
                {
                    seq = new AnalyzedScoredSequence(readseq_reverse_id);
                    discrepancies_reverse = seq.getDiscrepanciesByType(Mutation.RNA);
                }
                if (readseq_forward_id != 0)
                {
                    seq = new AnalyzedScoredSequence(readseq_forward_id);
                    discrepancies_forward = seq.getDiscrepanciesByType(Mutation.RNA);
                }
                
                if ( readseq_forward_id != 0 && readseq_reverse_id == 0 )
                {
                    discrepancies_pairs = DiscrepancyPair.assembleDiscrepanciesInPairs(discrepancies_forward);

                }
                else  if ( readseq_forward_id == 0 && readseq_reverse_id != 0 )
                {
                    discrepancies_pairs = DiscrepancyPair.assembleDiscrepanciesInPairs(discrepancies_reverse);
                }
                else if ( readseq_forward_id != 0 && readseq_reverse_id != 0)
                {
                    discrepancies_pairs = DiscrepancyPair.getDiscrepancyPairsNoDuplicates(
                             discrepancies_reverse, 
                             discrepancies_forward);

                }
                for (int i = 0; i < discrepancies_pairs.size(); i++)
                {
                   rnaOnly.add( ( (DiscrepancyPair)discrepancies_pairs.get(i)).getRNADiscrepancy());
                }
                if (rnaOnly.size() > 0)
                {
                    int[] discr_by_type = Mutation.getDiscrepanciesSeparatedByType(rnaOnly);
                    for (int ind = 0; ind < discr_by_type.length ; ind++)
                    {
                        mut_data +=discr_by_type[ind]+"\t";
                    }
                      data = rs.getInt("position") +"\t"+  +rs.getInt("rank")+"\t"+rs.getInt("score") ;
                    data +="\t"+ (readseq_forward_id != 0)+"\t" + (readseq_reverse_id != 0) +"\t";
                    data += discrepancies_pairs.size()+"\t"+mut_data;
                }
                else
                {
                    data = rs.getInt("position") +"\t"+  +rs.getInt("rank")+"\t"+rs.getInt("score") ;
                    data +="\t"+ (readseq_forward_id != 0)+"\t" + (readseq_reverse_id != 0) +"\t0";
                    
                }
                writer.write(data+"\n");
                 writer.flush();
            }
            
           
            writer.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    
    
    public static void main(String args[])
    {
      
        try
        {
            Reporter reporter = new Reporter("YGS000357-2");
            reporter.fullReport("c:\\tmp\\"+"YGS000357-2-today" +".txt");
        }
        catch(Exception e){}
        System.exit(0);
    }
}
