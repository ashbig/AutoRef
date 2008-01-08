/*
 * BlastableDBBuilder.java
 *
 * Created on January 8, 2008, 1:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.bec.util;

import edu.harvard.med.hip.flex.process.FlexProcessException;
import java.util.*;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.sql.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;


import sun.jdbc.rowset.*;


/**
 *
 * @author htaycher
 */
public class BlastableDBBuilder {
    
    /** Creates a new instance of BlastableDBBuilder */
    public BlastableDBBuilder() {
    }
    
    
    public void buildDBForProject(String project_name, String location, 
            String vector_sequences_file, String blast_location)throws Exception
    {
       // int[][] sequence_ids = getAllSequenceIDsForProject(project_name, true);
        int[] sequence_ids = getAllSequenceIDsForProject(project_name, true);
        writeBlastableDB(sequence_ids, location);
        addVectorSequences(location, vector_sequences_file);
        formatDB(location, blast_location);
    }
    
     public void buildCommonDB( String location, String vector_sequences_file, String blast_location)throws Exception
    {
      //  int[][] sequence_ids = getAllSequenceIDsForProject(null, false);
         int[]sequence_ids = getAllSequenceIDsForProject(null, true);
        writeBlastableDB(sequence_ids, location);
        addVectorSequences(location, vector_sequences_file);
        formatDB(location, blast_location);
    }
     
     
   /* private  int[][]   getAllSequenceIDsForProject(String project_name, boolean isProject) throws Exception
    {
        int[][] result = null; int number_of_sequences = 0;int count = 0;
          ResultSet rs = null;String sql = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            sql = "select count(DISTINCT  refsequenceid) as rnumber from sequencingconstruct where constructid in "
+" (select  constructid from isolatetracking where sampleid in "
+" (select sampleid from sample where containerid in "
+" (select containerid from containerheader where project_id in "
+" (select projectid from projectdefinition where projectname='"+project_name+"'))))";

            rs = t.executeQuery(sql);
            if (rs.next())
            {
                number_of_sequences= rs.getInt("rnumber");
                
                int cycle_count = 0;int number_of_cycles = 0;int[] cycle_ids =null;
                result = new int[ (number_of_sequences / 100 + 1) ][100];
                sql="select DISTINCT  refsequenceid as rnumber from sequencingconstruct where constructid in "
+" (select  constructid from isolatetracking where sampleid in "
+" (select sampleid from sample where containerid in "
+" (select containerid from containerheader where project_id in "
+" (select projectid from projectdefinition where projectname='"+project_name+"'))))";
                     rs = t.executeQuery(sql);
                      cycle_ids = new int[100];
                      result [ number_of_cycles ] = cycle_ids ;
                     while(rs.next())
                     {
                      //   result[count++]=rs.getInt("rnumber");
                       
                        if ( cycle_count == 100 )
                        {
                            cycle_ids = new int[100];
                            result [ ++number_of_cycles ] = cycle_ids ;
                            cycle_count=0;
                         }
                        cycle_ids[cycle_count++] = rs.getInt("rnumber");
                     }
                }
                   
            return result;
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    
        
    }
    private void    writeBlastableDB(int[][] sequence_ids,String location) throws Exception
    {
        ResultSet rs = null; BufferedWriter  fout = null;
        String[] sql = new String[sequence_ids.length];
        for (int count = 0; count < sequence_ids.length; count++)
        {
            sql[count] = "select sequenceid, infotext from sequenceinfo where " +
                    " infotype = "+BaseSequence.SEQUENCE_INFO_TEXT + " and sequenceid in ("
                    + Algorithms.convertArrayToString(sequence_ids[count], ",")
                    +") order by sequenceid";
        }
        int cur_sequence_id =0 ; 
        //delete old version
        File fdb = new File(location);
        if (fdb.exists()) fdb.delete();
        try
        {
             // by 100 sequences
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            for (int count = 0; count < sql.length; count++)
            {
                   
                fout = new BufferedWriter(new FileWriter(location,true));
    
                rs = t.executeQuery(sql[count]);
                while ( rs.next() )
                {
                    if ( cur_sequence_id != rs.getInt("sequenceid"))
                    {
                        cur_sequence_id = rs.getInt("sequenceid");
                        fout.write("\n>"+cur_sequence_id+"\n");
                    }
                    fout.write(rs.getString("infotext" ));
                }
                fout.close();
            }
          
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured "+sql);
        } finally
        {
            if ( fout != null) fout.close();
            DatabaseTransaction.closeResultSet(rs);
        }
    
    }
    */
     private  int[]   getAllSequenceIDsForProject(String project_name, boolean isProject) throws Exception
    {
        int[] result = null; int number_of_sequences = 0;int count = 0;
          ResultSet rs = null;String sql = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            sql = "select count(DISTINCT  refsequenceid) as rnumber from sequencingconstruct where constructid in "
+" (select  constructid from isolatetracking where sampleid in "
+" (select sampleid from sample where containerid in "
+" (select containerid from containerheader where project_id in "
+" (select projectid from projectdefinition where projectname='"+project_name+"'))))";

            rs = t.executeQuery(sql);
            if (rs.next())
            {
                number_of_sequences= rs.getInt("rnumber");
                result = new int[ number_of_sequences ];
                sql="select DISTINCT  refsequenceid as rnumber from sequencingconstruct where constructid in "
+" (select  constructid from isolatetracking where sampleid in "
+" (select sampleid from sample where containerid in "
+" (select containerid from containerheader where project_id in "
+" (select projectid from projectdefinition where projectname='"+project_name+"'))))";
                     rs = t.executeQuery(sql);
                     
                     while(rs.next())
                     {
                         result[count++]=rs.getInt("rnumber");
                     }
                }
                   
            return result;
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    
        
    }
    private void    writeBlastableDB(int[] sequence_ids,String location) throws Exception
    {
        ResultSet rs = null; BufferedWriter  fout = null;
        RefSequence ref = null;
        int cur_sequence_id =0 ; 
        //delete old version
        File fdb = new File(location);
        if (fdb.exists()) fdb.delete();
        try
        {
             // by 100 sequences
            DatabaseTransaction t = DatabaseTransaction.getInstance();
             fout = new BufferedWriter(new FileWriter(location,true));
    
            for (int count = 0; count < sequence_ids.length; count++)
            {
                 ref = new RefSequence(sequence_ids[count], false);
                fout.write("\n>"+ref.getId()+"\n");
                fout.write(  ref.getCodingSequence()  );
                fout.flush();
            }
             fout.write("\n");
            fout.close();
        } 
        catch (Exception sql)
        {
            throw new BecDatabaseException("Error occured "+sql.getMessage());
        } finally
        {
            if ( fout != null) fout.close();
            DatabaseTransaction.closeResultSet(rs);
        }
    
    }
    public void    addVectorSequences(String location, String vector_sequences_file) throws Exception
    {
        String cmd = "cmd /c cat "+vector_sequences_file+" >> "+location  ;
        edu.harvard.med.hip.bec.programs.SystemProcessRunner.runOSCall(cmd);
     
    }
    public  void  formatDB(String location, String blast_location) throws Exception
    {
        String cmd = "cmd /c "+ blast_location +"\\formatdb -pF -oT -i "+ location;
        edu.harvard.med.hip.bec.programs.SystemProcessRunner.runOSCall(cmd);
        
    }
    
    public static void main(String args[])
    {
        String project_name ="Aventis";
        String location="c:\\tmp\\genes";
            String vector_sequences_file="c:\\tmp\\vector_library.seq";
            String blast_location="C:\\blast_new";
       BlastableDBBuilder bd = new BlastableDBBuilder();
       try
       {
         //bd.buildDBForProject( project_name,  location,  vector_sequences_file,  blast_location);
           // bd.addVectorSequences( location,  vector_sequences_file);
          bd.formatDB( location,  blast_location) ;
       }catch(Exception e){}
    }
}
