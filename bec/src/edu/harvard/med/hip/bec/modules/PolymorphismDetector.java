/*
 * DetectorRunner.java
 *
 * Created on March 12, 2003, 2:07 PM
 */

package edu.harvard.med.hip.bec.modules;

import edu.harvard.med.hip.bec.programs.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.programs.blast.*;
import edu.harvard.med.hip.bec.export.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
//import edu.harvard.med.hip.bec.engine.*;
import edu.harvard.med.hip.bec.database.*;
import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.bioutil.*;
import edu.harvard.med.hip.bec.Constants;
import java.util.*;
import java.sql.Date;
import java.io.*;
import java.math.BigDecimal;
import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  htaycher
 */
public class PolymorphismDetector
{
    //store input & output blast files
    private  String INPUT = Constants.getTemporaryFilesPath();
   // private static final String OUTPUT = "/blastoutput/";
    private  String OUTPUT = Constants.getTemporaryFilesPath();
  
    //submition data
    private AnalyzedScoredSequence m_sequence = null;
    private ArrayList m_sequences = null;
    private PolymorphismSpec m_spec = null;
    
    //exstracted from Polymorphism spec on load
    private  String m_dbFileName = null;
    private String m_matrix = "BLOSUM62";
    private int m_bases = 20;
    
    
    private double m_checkout_identity = 95.0;
    //private String m_species = null;
    
    
    
    /** Creates a new instance of DetectorRunner */
    public PolymorphismDetector(){}
    public PolymorphismDetector(AnalyzedScoredSequence fl, PolymorphismSpec spec) throws BecUtilException
    {
        m_sequence = fl;
        m_spec = spec;
        if (spec != null) extractParameters(m_spec);
        
    }
    
    
    public PolymorphismDetector(ArrayList fls, PolymorphismSpec spec) throws BecUtilException
    {
        m_sequences = fls;
        m_spec = spec;
        if (spec != null)extractParameters(m_spec);
    }
    
    
    
    public void setSpec(PolymorphismSpec spec) throws BecUtilException
    {
        m_spec = spec;
        if (spec != null) extractParameters(m_spec);
    }
    
   
    public void setSequence(AnalyzedScoredSequence fl) 
    {
        m_sequence = fl;
    }
    
    
    private void extractParameters(PolymorphismSpec spec) throws BecUtilException
    {
        try
        {
            
            m_bases = spec.getParameterByNameInt("PL_BASES");
            if (m_bases > 40) m_bases=40;
            m_dbFileName = spec.getParameterByNameString("PL_DATABASE");
            m_matrix =  spec.getParameterByNameString("PL_MATRIX");
        }
        catch (Exception e)
        {
            throw new BecUtilException("Wrong specification");
        }
    }
    
    
    //for testing only
    public void setDB(String s)    {m_dbFileName = s;}
    
    //main calling function for polymorphism finder
    public void run()throws BecDatabaseException,ParseException,BecUtilException
    {
        if (m_sequences != null)
        {
            for (int ind = 0; ind < m_sequences.size(); ind++)
            {
                AnalyzedScoredSequence fl = (AnalyzedScoredSequence) m_sequences.get(ind);
                runSequence(fl);
            }
        }
        else
            
        {
            runSequence(m_sequence);
        }
        
    }
    
    //function processes one sequence through polymorphism finder
    //it calls blast for each mutation
    public void runSequence(AnalyzedScoredSequence fl) throws BecDatabaseException,ParseException,
                                                    BecUtilException
    {
        
        ArrayList discrepancies = null;
        ArrayList matches = new ArrayList();//hash for ids that were matched to sequence
        RNAMutation rnamut= null;
        
        //get all rna discrepancies
        discrepancies = fl.getDiscrepanciesByType(Mutation.RNA);
        for (int ind = 0; ind < discrepancies.size(); ind++)
        {
            rnamut = (RNAMutation) discrepancies.get(ind);
            processDiscrepancy(rnamut, matches);
        }
        
    }
    
    
    //function process single discrepancy and stores the matcher id , so that confirmation for
    //the same id will not be needed
    private void processDiscrepancy(RNAMutation rnamut, ArrayList matches)
                                    throws BecUtilException,ParseException
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
    
    
    
    
    //function tries to get matches for the discrepancy
    private ArrayList matchDiscrepancy(String query)throws BecUtilException,ParseException
    {
        
        BlastParserNew parser = null;
        //write query file
        String queryFile = SequenceManipulation.makeQueryFileInFASTAFormat(INPUT,query, "bn", m_sequence.getId()+"_ex");
        BlastWrapper runner = new BlastWrapper();
        //set matrix to small sequences if not specified by user
        if ( ! m_matrix.equalsIgnoreCase("none") )
        {
            if (query.length() < 100) m_matrix = "PAM30";
            else if (query.length()>100 && query.length()<150) m_matrix="PAM70";
        }
        setBlastWrapper(runner,m_dbFileName,3,m_matrix);
        runner.setInputFN(queryFile+".in");
        runner.setFilter("F");
        runner.setOutputFN(queryFile+".out");
        runner.run();
        
        return parser.parse(queryFile+".out", 8);
        
    }
    
    
    //caller to confirm discrepancy; checks if conformation is needed
    private void confirmDiscrepancy(RNAMutation rnamut, String matchid,
    int idtype, ArrayList matches)throws BecUtilException,ParseException
    {
        //check if this id was already confirmed
        boolean isConfirmed = true;
        if ( ! matches.contains("matchid") )
        {
            isConfirmed = confirmDiscrepancy(matchid,  idtype );
            System.out.println(isConfirmed);
            System.out.println("added "+matchid);
            matches.add( matchid );
        }
        rnamut. setPolymFlag(RNAMutation.FLAG_POLYM_YES);
        //polymorphism or not
        rnamut.setPolymId(matchid);
        rnamut.setPolymDate(new java.util.Date());
        
        
    }
    
    //function tries to confirm that hit sequence belong to the same gene
    private boolean  confirmDiscrepancy(String id , int id_type ) throws BecUtilException, ParseException
    {
        System.out.println(0);
        String hitsequence = IndexerForBlastDB.getSequence(id, id_type);
        
       
        String queryFile = SequenceManipulation.makeQueryFileInFASTAFormat(INPUT,m_sequence.getText(), "bn", m_sequence.getId()+"_ex");
        String  subjFile =  SequenceManipulation.makeQueryFileInFASTAFormat(INPUT,hitsequence, "bn",id );
        //run bl2seq
        BlastWrapper runner = new BlastWrapper();
        runner.setProgramName("blastn");
        runner.setBlastType(BlastWrapper.BLAST2SEQ);
        runner.setExtGap(0);
        runner.setOpenGap(1);
        runner.setFilter("F");
        runner.setSubjectInputFN(subjFile+".in");
        runner.setInputFN(queryFile+".in");
        runner.setOutputFN(queryFile+".out");
  
        runner.run();
     
        BlastResult blresult = edu.harvard.med.hip.bec.programs.blast.Blast2seqParser.parseBl2seqResult(queryFile+".out", 1);
        BlastAligment blaligment = (BlastAligment) blresult.getAligments().get(0);
        //check for identity
        char[] query_arr = blaligment.getQSequence().toCharArray();
        char[] subject_arr =    blaligment.getSSequence().toCharArray();
        int matchcount = 0; int allcount=0;
        for (int ind = 0; ind < query_arr.length; ind ++)
        {
            switch (query_arr[ind])
            {
                case 'a': case 'A': case 'g': case 'G': case 't': case 'T': case 'c':case 'C':
                {
                    if (query_arr[ind]==subject_arr[ind])
                        matchcount++;
                    allcount++;
                }
            }
        }
        System.out.println(matchcount+" "+allcount);
        
        return (matchcount/allcount >= m_checkout_identity)? true: false;
    }
    
    private void setBlastWrapper(BlastWrapper blaster, String db, int hits, String matrix)
    {
        blaster.setProgramName("blastn");
        blaster.setDB(db);
        blaster.setFormat(8);
        
        //blaster.setFilter(String v);
        blaster.setGI("T");
        blaster.setHitNumber(hits);
        if (matrix!= null)blaster.setMatrix(matrix);
        
    }
    
    
    //******************************************
    public static void main(String args[])
    {
        
        String seq = "TTTTTTTTTTTTTTGAATTTGATAATCCTCCTTTTATTcCATATTAAACTTTAAAATTTGTACCACATTATTAAAGTATTACTTTTACTCACAGTAGTATTATACATAGACTTAACACAATTTTTAAAAATGTGTTTACTTAAAACAATATAATTCTCCTTTACAAAAGCAACTTTATATAAAATGTTTGGCTTAAGACTGTCATTGCTATTATGCCTTTGAATGAAATTCCACTCTTTCGCCTCCATTGTCCAGAAACAGGCACATATCAGCTTGTTTTCTTTAATGAATATTCTGTAACAAGTTCCTGAAGTTTTCTAATTCTTTCACACTTGTAGAAATTCTTCCAAATGCGTTGAATAATGATACTATTTCTTGTCTGGTTAGATGGAATTCATAACTAGGTCCACTTTCTGGCATATTTGCTATCAATTTCTCAGAAAATAAGATCTTCAGAGCAGTGCCCAAACCCTGAGTCTGAAGCTTTCCCCACAGACGACATTTAAAACAACCAACACAATCCATAATT";
        // String seq="GCGGCCGCATAACTTCGTATAGCATACATTATACGAAGTTATCAGTCGACACCATGCGCGAGATCGTGCACATCCAGGCGGGCCAGTGCGGCAACCAGATCGGCGCCAAGTTTTGGGAGGTCATCAGTGATGAGCATGGGATTGACCCCACTGGCAGTTACCATGGAGACAGTGATTTGCAGCTGGAGAGAATCAATGTTTACTACAATGAAGCCACTGGTAACAAATATGTTCCTCGGGCCATCCTCGTGGATCTGGAGCCAGGCACGATGGATTCGGTTAGGTCTGGACCATTCGGCCAGATCTTCAGACCAGACAATTTCGTGTTTGGCCAGAGTGGAGCCGGGAATAACTGGGCCAAGGGCCACTACACAGAGGGAGCCGAGCTGGTCGACTCGGTCCTGGATGTGGTGAGGAAGGAGTCAGAGAGCTGTGACTGTCTCCAGGGCTTCCAGCTGACCCACTCTCTGGGGGGCGGCACGGGGTCCGGGATGGGCACCCTGCTCATCAGCAAGATCCGGGAAGAGTACCCAGACCGCATCATGAACACCTTCAGCGTCATGCCCTCACCCAAGGTGTCAGACACGGTGGTGGAGCCCTACAACGCCACCCTCTCGGTCCACCAGCTGGTGGAAAACACAGATGAAACCTACTGCATTGACAACGAGGCCCTGTATGACATCTGCTTCCGCACCCTGAAGCTGACCACCCCCACCTACGGGGACCTCAACCACCTGGTGTCGGCCACCATGAGCGGGGTCACCACCTGCCTGCGCTTCCCGGGCCAGCTGAACGCAGACCTGCGCAAGCTGGCGGTGAACATGGTGCCCTTCCCTCGCCTGCACTTCTTCATGCCCGGCTTCGCGCCCCTGACCAGCCGGGGCAGCCAGCAGTACCGGGCGCTCACGGTGCCCGAGCTCACCCAGCAGATGTTCGACTCCAAGAACATGATGGCCGCCTGCGACCCGCGCCACGGCCGCTACCTGACGGTGGCTGCCATCTTCCGGGGCCGCATGTCCATGAAGGAGGTGGACGAGCAGATGCTCAACGTGCAGAACAAGAACAGCAGCTACTTCGTGGAGTGGATCCCCAACAACGTGAAGACGGCCGTGTGCGACATCCCGCCCCGCGGCCTGAAGATGTCGGCCACCTTCATCGGCAACAGCACGGCCATCCAGGAGCTGTTCAAGCGCATCTCCGAGCAGTTCACGGCCATGTTCCGGCGCAAGGCCTTCCTGCACTGGTACACGGGCGAGGGCATGGACGAGATGGAGTTCACCGAGGCCGAGAGCAACATGAACGACCTGGTGTCCGAGTACCAGCAGTACCAGGACGCCACGGCCGACGAACAAGGGGAGTTCGAGGAGGAGGAGGGCGAGGACGAGGCTTTGGGAAGCTTTCTAGACCATTCGTTTGGCGCGCGGGCCC";
        System.out.println(seq.substring(127,128));
        System.out.println(seq.substring(127-30,127));
        System.out.println(seq.substring(128,128+30));
        try
        {
            AnalyzedScoredSequence fl = new AnalyzedScoredSequence(seq,127);
            RNAMutation mut = new RNAMutation();
            mut.setPolymFlag(RNAMutation.FLAG_POLYM_NOKNOWN);
            mut.setUpstream(seq.substring(127-30, 127));
            mut.setDownStream(seq.substring(128,128+30));
           
            mut.setChangeMut (seq.substring(127,128));
            mut.setNumber (1) ;

            fl.addDiscrepancy(mut);
            mut = new RNAMutation();
            mut.setPolymFlag(RNAMutation.FLAG_POLYM_NOKNOWN);
            mut.setUpstream(seq.substring(355-30, 355));
            mut.setDownStream(seq.substring(356,356+30));
           
            mut.setChangeMut (seq.substring(355,356));
            mut.setNumber (2) ;
            
            fl.addDiscrepancy(mut);
          /*    mut = new RNAMutation(3,0,40, 1,seq.substring(1390,1391), "", 0,
            0, 0,Mutation.RNA,
           seq.substring(1390-30,1390),
           seq.substring(1391,1391+30),
            "","", 0, 0);
            fl.addMutation(mut);*/
            
            PolymorphismDetector dr = new  PolymorphismDetector(  fl, null);
            dr.setDB("C:\\GenBankMonth\\est_human");
            dr.runSequence(fl);
        }catch(Exception e)
        {
            System.out.println(e.getMessage());}
    }
    
    
}
