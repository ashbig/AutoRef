/*
 * EndReadsAnalyze.java
 *
 * Created on September 25, 2002, 2:59 PM
 */

package edu.harvard.med.hip.flex.seqprocess;

import java.util.*;
import edu.harvard.med.hip.flex.seqprocess.spec.*;
import edu.harvard.med.hip.flex.database.*;
/**
 *
 * @author  htaycher
 */
public class EndReadsAnalyze
{
    ArrayList m_samples = new ArrayList();
    /** Creates a new instance of EndReadsAnalyze */
    public EndReadsAnalyze()
    {
    }
    
    public  void initiate_analyze(int pr_id, ArrayList plate_ids) throws FlexDatabaseException
    {
        //get spec for the project
        SpecGroup spec= new SpecGroup(pr_id, SpecGroup.OWNER_PROJECT);
        //get all samples that should be processed
        m_samples = getAllSamples(plate_ids);
        //call run phred with parameters on requered plates
        String prhred_params = spec.getEndReadsSpec().getPhredParams();
        // run in separate thread that starts process
        //when process is finished and all data parsed back
        
        runPhred( prhred_params, m_samples);
        //returns array of phred result objects
        ArrayList phred_results = getPhredResults();
    }
    
    
    
    //-------------------------private members ------------------
    
    //function gets all sample ids
    //for selected paltes
    private ArrayList  getAllSamples(ArrayList plate_id)
    {
        ArrayList sample_ids = new ArrayList();
        return sample_ids;
    }
    
    //function starts phred with selected parametrs
    //for all reads comming from qualified samples
    private void      runPhred(String phred_param,  ArrayList  sample_ids){}
    
    //function reads phred output provided by parser
    //in a form of PhredOutput objects and
    //insert data into db
    private ArrayList getPhredResults()
    {
        ArrayList sample_ids = new ArrayList();
        return sample_ids;
    }
    
    
   
}
