/*
 * PolymorphismFinderJob.java
 *
 * Created on June 15, 2005, 11:45 AM
 */

package edu.harvard.med.hip.bec.programs.polymorphism_finder;


import java.util.*;
/**
 *
 * @author  htaycher
 */
public class PolymorphismFinderJob
{
   /* private ArrayList           m_discrepancies_data = null;
    // Creates a new instance of PolymorphismFinderJob 
    public PolymorphismFinderJob(ArrayList discrepancies_data)
    {
         run_job(discrepancies_data);
    }
    
    private void        run_job(ArrayList discrepancies_data)
    {
        ArrayList discr_data = null;
        ArrayList hits = 
        for ( int count = 0; count < discrepancies_data.size(); count ++)
        {
            discr_data = splitString(discr_data, " ");
            findDiscrepancyHit( (String) discr_data.get(0),  (String) discr_data.get(1) );
        }
    }
    
    
    
     //function process single discrepancy and stores the matcher id , so that confirmation for
    //the same id will not be needed
    private void processDiscrepancy(String discr_id, String discr_sequence
    {
       BlastResult blresult = null;
        BlastAligment blalm = null;
        //define wether discrepancy has enough flunking sequence
        String up_fs = rnamut.getUpStream();
        String dn_fs = rnamut.getDownStream();
        if (up_fs.length() < m_bases || dn_fs.length() <m_bases)
            return;
        
        //create query string for the discrepancy
        String query = up_fs.substring(dn_fs.length()-m_bases) + rnamut.getQueryStr() + dn_fs.substring(0,m_bases);
        //match discrepancy
        ArrayList output = matchDiscrepancy(query);
        if (output.size() < 1) return;
        //take best hit
        blresult = (BlastResult)output.get(0);
        if (blresult.getAligments().size() < 1) return;
        blalm = (BlastAligment) blresult.getAligments().get(0);
        //if discrepancy matched 100% by identity on the whole length - confirm it
        boolean isConfirm = blalm.getIdentity() == 100.0 && ( blalm.getQStop()-blalm.getQStart() + 1)== query.length();
        System.out.println("hit "+blresult.getGI() + " " + isConfirm + " "+ blalm.getIdentity() +" "+ (blalm.getQStop()-blalm.getQStart() )+" "+query.length());
        if (isConfirm)
        {
            if ( blresult.getGI() != null )//for new blast
            {
                System.out.println(blresult.getGI());
                confirmDiscrepancy(rnamut, blresult.getGI(), IndexerForBlastDB.GI_INDEX ,  matches);
            }
            else//old blast
            {
                confirmDiscrepancy(rnamut, blresult.getAcesession(), IndexerForBlastDB.ACESSES_INDEX_BASE ,  matches);
            }
        }
        else
        {
            rnamut. setPolymFlag(RNAMutation.FLAG_POLYM_NO);
        }
    }
    
    public static ArrayList splitString(String value, String spliter)
    {
        ArrayList res = new ArrayList();
        StringTokenizer st  = null;
        if (spliter == null)
            st = new StringTokenizer(value);
        else
            st = new StringTokenizer(value, spliter);
        while(st.hasMoreTokens())
        {
            String val = st.nextToken().trim();
            res.add( val );
        }
        return res;
    }
      */
    
    
   
}
