/*
 * BiojavaBlast.java
 *
 * Created on March 13, 2003, 10:52 AM
 */

package edu.harvard.med.hip.bec.programs.blast;

/**
 *
 * @author  htaycher
 */
import java.io.*;
import java.util.*;


import org.biojava.bio.program.sax.*;
import org.biojava.bio.program.ssbind.*;
import org.biojava.bio.search.*;
import org.biojava.bio.seq.db.*;
import org.xml.sax.*;
import org.biojava.bio.*;

import edu.harvard.med.hip.bec.util.*;


public class BiojavaBlast
{
    
    
    /**
     * args[0] is assumed to be the name of a Blast output file
     */
    public static List parceBlastOutput(String fileName) throws BecUtilException
    {
        try
        {
            //get the Blast input as a Stream
            InputStream is = new FileInputStream(fileName);
            
            //make a BlastLikeSAXParser
            BlastLikeSAXParser parser = new BlastLikeSAXParser();
           parser.setModeLazy();
            //make the SAX event adapter that will pass events to a Handler.
            SeqSimilarityAdapter adapter = new SeqSimilarityAdapter();

            //set the parsers SAX event adapter
            parser.setContentHandler(adapter);
            
            //The list to hold the SeqSimilaritySearchResults
            List results = new ArrayList();
            
            //create the SearchContentHandler that will build SeqSimilaritySearchResults
            //in the results List
           SearchContentHandler builder = new BlastLikeSearchBuilder(results,
            new DummySequenceDB("queries"), new DummySequenceDBInstallation());
            
            //register builder with adapter
            adapter.setSearchContentHandler(builder);
            
            //parse the file, after this the result List will be populated with
            //SeqSimilaritySearchResults
            
            parser.parse(new InputSource(is));
            return results;
        }
        catch (SAXException ex)
        {
            //XML problem
            ex.printStackTrace();
            throw new BecUtilException("Blast parser problem");
        }catch (IOException ex)
        {
            //IO problem, possibly file not found
            ex.printStackTrace();
            throw new BecUtilException("Blast parser problem");
        }
    }
    
    private static void formatResults(List results)
    {
        
        //iterate through each SeqSimilaritySearchResult
        for (Iterator i = results.iterator(); i.hasNext(); )
        {
            SeqSimilaritySearchResult result = (SeqSimilaritySearchResult)i.next();
            
            //iterate through the hits
            for (Iterator i2 = result.getHits().iterator(); i2.hasNext(); )
            {
                SeqSimilaritySearchHit hit = (SeqSimilaritySearchHit)i2.next();
                
                //output the hit ID, bit score and e score
                System.out.println("subject:\t"+hit.getSubjectID() +
                " bits:\t"+hit.getScore()+
                " e:\t"+hit.getEValue());
            }
        }
    }
    
    
    
    /**
     * args[0] is assumed to be the name of a Blast output file
     */
    public static void main(String[] args)
    {
        String fileName="C:\\genbankmonth\\blast.out";
        try
        {
           ArrayList a =  (ArrayList)parceBlastOutput( fileName);
        }
        catch (Exception ex)
        {
            //XML problem
            ex.printStackTrace();
        }
       
    }
    
    
}