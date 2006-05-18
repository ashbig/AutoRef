//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * AssembledSequenceSubmission.java
 *
 * Created on January 14, 2004, 2:45 PM
 */

package edu.harvard.med.hip.bec.util;
import java.sql.*;
import java.io.*;


import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.assembler.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
  import java.util.*;
/**
 *
 * @author  HTaycher
 */
public class AssembledSequenceSubmission
{
    
    /** Creates a new instance of AssembledSequenceSubmission */
    public AssembledSequenceSubmission()
    {
    }
    public static void submitSequence(Connection conn, User user,int cloneid, String sequence, int cdsStart, int cds_end)throws Exception
    {
        
       int[] clone_info = getCloneInfo(cloneid);
        CloneSequence cl_seq = new  CloneSequence( sequence,  clone_info[1]);
        
        cl_seq.setCloneSequenceType ( BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED); //final\conseq\final editied
        cl_seq.setApprovedById ( user.getId() );//BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
      //  cl_seq.setRefSequenceId();  //BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
        cl_seq.setIsolatetrackingId (clone_info[0]); //BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
        cl_seq.setCloneSequenceStatus (BaseSequence.CLONE_SEQUENCE_TYPE_FINAL);
        cl_seq.setCdsStart(cdsStart);
        cl_seq.setCdsStop(cds_end);
        cl_seq.insert(conn);
   
    }
    
    private static int[] getCloneInfo(int cloneid)throws Exception
    {
        String sql = "select flexcloneid, i.isolatetrackingid, refsequenceid from isolatetracking i, sequencingconstruct c, flexinfo f "
+" where  f.isolatetrackingid=i.isolatetrackingid and i.constructid=c.constructid  and  flexcloneid="+cloneid+"))";
        int[] result = new int[2];
        ResultSet rs = null;
    
       
           rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while(rs.next())
            {
                 result[0]= rs.getInt("isolatetrackingid");
                 result[0]= rs.getInt("refsequenceid");
               
            }
           return result;
       
        
    }
    
    public static void main(String args[])
    {
        try
        {
             User user = AccessManager.getInstance().getUser("htaycher1","htaycher");
             Connection conn =DatabaseTransaction.getInstance().requestConnection();
              String sequence="";
            // submitSequence( conn,  user, cloneid,  sequence,  cdsStart,  cds_end);
        }
        catch(Exception e){}
        System.exit(0);
    }
}
