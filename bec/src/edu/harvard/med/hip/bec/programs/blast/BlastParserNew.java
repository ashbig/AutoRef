/*
 * BlastParserNew.java
 *
 * Created on March 14, 2003, 12:21 PM
 */

package edu.harvard.med.hip.bec.programs.blast;

import java.util.*;
import java.io.*;
import org.apache.regexp.*;

import edu.harvard.med.hip.bec.util.*;
/**
 *
 * @author  htaycher
 */
public class BlastParserNew
{
    private int m_blast_format = 0;
    private static BlastParserNew m_instance = null;
    
    
    /** Creates a new instance of BlastParserNew */
    private BlastParserNew(int format)
    {
        m_blast_format = format;
        try
        {
            // # Identity:    1120/1147 (97.6%)
            switch (m_blast_format)
            {
                case 8: case  9:
                {
                    //1	gi|28363213|gb|CB241569.1|CB241569	99.21	253	0	1	1	251	1	253	6e-136	480.2
                    //1	BX119401	100.00	14	0	0	3	16	1	14	  6.8	28.25
                    return;
                }
            }
            
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    
    //output - array list of blast results
    public static ArrayList parse(String foutput_name, int format) throws ParseException
    {
        if (m_instance == null)  m_instance = new BlastParserNew(format);
        switch (format)
        {
            case 8: case 9:
            {
                return  parseTabularFormat(foutput_name);
                
            }
            default : return null;
        }
        
        
    }
    
    
    /************************************************************************************/
    
    //parse output of 8 and 9 format of blast
     /* new format
     # Fields:
        0 - Query id,
        1 - Subject id,
      2 - % identity,
      3 - alignment length,
      4 - mismatches,
      5 - gap openings,
      6 - q. start,
      7 - q. end,
      8 - s. start,
      9 - s. end,
      10 - e-value,
      11 - bit score
1	gi|28363213|gb|CB241569.1|CB241569	99.21	253	0	1	1	251	1	253	6e-136	480.2
1	gi|28365566|gb|CB243922.1|CB243922	96.15	156	4	1	98	251	421	266	1.6e-068	256.2
1	gi|28362479|gb|CB240835.1|CB240835	100.00	26	0	0	1	26	1	26	4.7e-007	52.03
      
      
      
       old format
      *1	BX119628	100.00	14	0	0	109	122	465	478	  6.8	28.25
      */
    private static ArrayList parseTabularFormat(String foutput_name) throws ParseException
    {
        String line = null;
        ArrayList aligments = new ArrayList();
        ArrayList temp = null;
        BufferedReader  fin = null;
        try
        {
            fin = new BufferedReader(new FileReader(foutput_name));
            while ( (line = fin.readLine()) != null)
            {
                temp = Algorithms.splitString(line,"\t");
                if (temp.size() > 10) // skip description for 9
                {
                    //create new hit
                    BlastAligment alg = new BlastAligment();
                    alg.setScore(  Double.parseDouble(  (String)temp.get(11) ) );
                    alg.setIdentity( Double.parseDouble(  (String)temp.get(2) ) );
                    alg.setExpectValue( (String) temp.get(10)  );
                    //alg.setQStrand(qstrand);                 //alg.setSStrand(  sstrand);                //alg.setSFrame( sframe);               // alg.setQFrame( qframe);
                    alg.setQStart(Integer.parseInt(  (String)temp.get(6)));
                    alg.setQStop(Integer.parseInt(  (String)temp.get(7)));
                    alg.setSStart(Integer.parseInt(  (String)temp.get(8)));
                    alg.setSStop(Integer.parseInt(  (String)temp.get(9)));
                    alg.setNumberOfMismatches(Integer.parseInt( (String) temp.get(4)));//for tabular format
                    alg.setNumberOfGaps(Integer.parseInt(  (String) temp.get(5)));
                    //by some reason these formats do not sort by score, so store result/hit identity temp in sequence
                    alg.setSSequence( (String)temp.get(1));
                    //add hit
                    aligments.add(alg);
                }
                
            }
            fin.close();
            //sort aligments by score
           
            sortAligmentsByScore(aligments);
       
            //reparse to blast results
            return parseTabFormatToResult( aligments);
            
        }
        catch(Exception e)
        {
            try{fin.close();} catch(Exception e1){}
            throw new ParseException("Cannot parse blast output. File name " +foutput_name);
        }
       
    }
    
    
    //function reparce output of tabular formats to blast results from aligment collection
    private static ArrayList parseTabFormatToResult(ArrayList aligments)
    {
        Hashtable results = new Hashtable(); //hash stores results by GI as key
        BlastResult result = null;
        
        ArrayList result_final = new ArrayList();
        ArrayList temp = new ArrayList();
        BlastAligment alg = null;
        
        for (int ind = 0; ind < aligments.size(); ind ++)
        {
            alg = (BlastAligment) aligments.get(ind);
            //parse blast hit information
            temp = Algorithms.splitString( alg.getSSequence(),"|");
            alg.setSSequence(null);
            if (temp.size() > 4)// new blast format
            {
                //check if this result exists already?
                if (!results.isEmpty() && results.containsKey( (String) temp.get(1) ))
                {
                    result.addAligment(alg);
                    
                }
                else // new result
                {
                    result = new BlastResult();
                    result_final.add(result);
                    result.addAligment(alg);
                    
                    result.setGI( (String) temp.get(1));
                    result.setAcesession((String) temp.get(3));
                    
                    results.put(result.getGI(), result);
                }
            }
            else
            {
                if (!results.isEmpty() && results.containsKey( (String) temp.get(0) ))
                {
                    result.addAligment(alg);
                }
                else // new result
                {
                    result = new BlastResult();
                    result_final.add(result);
                    result.addAligment(alg);
                    
                    result.setAcesession((String) temp.get(0));
                    results.put(result.getAcesession(), result);
                }
                
            }
            
        }
        
        return result_final;
    }
    
    
    //function scores blast aligments by score
    private static ArrayList sortAligmentsByScore(ArrayList aligments)
    {
        
        //sort array by cds length
        Collections.sort(aligments, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                double score1 = ((BlastAligment) o1).getScore();
                double score2 = ((BlastAligment) o2).getScore();
                if ( score1 == score2) return 0;
                return (score1 > score2) ? -1: 1;
            }
            
        } );
        return aligments;
        
    }
    
    //******************************************
    public static void main(String args[])
    {
        /*
        try{
            RE letters = new RE("Identities\\s*=\\s*(\\d+)\\/(\\d+)\\s*\\((\\d+) %\\)");
         
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
         */
        
        String queryFile = "C:\\GenBankMonth\\blast2.out";
        BlastResult res = null;
        try
        {
            res = new BlastResult();
            
            BlastParserNew.parse(queryFile, 8);
        }catch(Exception e)
        {}
        System.exit(0);
    }
}
