/*
 * PhrePhrapParser.java
 *
 * Created on August 4, 2003, 6:31 PM
 */

package edu.harvard.med.hip.bec.programs.assembler;

import java.util.*;
import java.io.*;
import org.apache.regexp.*;
import edu.harvard.med.hip.bec.engine.*;
import edu.harvard.med.hip.bec.util.*;
/**
 *
 * @author  HTaycher
 */
public class PhredPhrapParser
{
   private static final String ASSEMBLY_START_FLAG = "AS";
    private static final String CO_FLAG = "CO";
    private static final String BQ_FLAG = "BQ";
    private static final String AF_FLAG = "AF";
    /** Creates a new instance of PhrePhrapParser */
    public PhredPhrapParser()
    {
    }
    
    public  CloneAssembly parse(String foutput_name) throws BecUtilException
    {
        System.out.println("a");
        String line = null;
        CloneAssembly clone_assembly = null;
        Contig contig = null;
        BufferedReader  fin = null;
        ArrayList data = null;int number_of_contigs = -1;
        boolean isInSequenceInfo = false;
        boolean isInScoresInfo = false;
        StringBuffer scores = new StringBuffer();
        StringBuffer sequence = new StringBuffer();
        try
        {
            fin = new BufferedReader(new FileReader(foutput_name));
            while ((line = fin.readLine()) != null)
            {
                if (line.startsWith(ASSEMBLY_START_FLAG))
                {//AS 1 2
                    data = Algorithms.splitString(line, " ");
                    number_of_contigs = Integer.parseInt( (String) data.get(1));
                    int numreads = Integer.parseInt((String) data.get(2));
                    
                     //reads do not assemble, not contig sequence to parse        
                    if (number_of_contigs == 0) return null;
                   
                    clone_assembly = new CloneAssembly();
                    clone_assembly.setNumOfReads(numreads);
                 }
                        
                //CO line flags a new contig.
                //It is possible to have multiple contigs for one assembly
                if (line.startsWith(CO_FLAG))
                {
                    contig = parseContigInfo(line);
                    isInSequenceInfo = true;
                    continue;
                }

                //BQ line: list of base qualities for the unpadded contig bases
                //The contig is the one from the previous CO line
                if (line.startsWith(BQ_FLAG))
                {
                    isInScoresInfo = true;
                    continue;
                }
                if (isInScoresInfo && !line.trim().equals(""))
                {
                    scores.append( line);
                    continue;
                }
                if (isInSequenceInfo && !line.trim().equals(""))
                {
                    sequence.append( Algorithms.cleanChar(line,'*'));
                    continue;
                }
                if (isInSequenceInfo && line.trim().equals(""))
                {
                    isInSequenceInfo = false;
                    continue;
                }
                if (isInScoresInfo && line.trim().equals(""))
                {
                    isInScoresInfo = false;
                    contig.setSequence(sequence.toString());
                    contig.setScores(scores.toString());
                    clone_assembly.addContig(contig);
                    continue;
                }
             //  if ( line.startsWith(AF_FLAG)) break;
            }
            fin.close();
            return clone_assembly;
        }
        catch(Exception e)
        {
            try
            {fin.close();}catch(Exception c)
            {throw new BecUtilException("Cannot parse phrap output "+foutput_name);}
            throw new BecUtilException("Cannot parse phrap output "+foutput_name);
        }
    }
    
    
    //CO line format: CO <contig name> <# of bases> <# of reads in contig> <# of base segments in contig> <U or C> 
    private Contig parseContigInfo(String CO_line)
    {
	Contig result = null;
       StringTokenizer tokenizer = new StringTokenizer(CO_line);
        tokenizer.nextToken();
        String contigName = tokenizer.nextToken();
        tokenizer.nextToken();
	int numOfReads = Integer.parseInt(tokenizer.nextToken());

	result = new Contig();
        result.setNumberOfReadsInContig(numOfReads);
        result.setName(contigName);

	return result;
    }
    
    
    
    public static void main(String args[])
    {
        try
        {
            String foutput_name = "C:\\bio\\phredphrap\\onecontig.ace.1";
            PhredPhrapParser pp= new PhredPhrapParser();
            CloneAssembly cl = pp.parse( foutput_name); 
            ArrayList a = cl.getContigs();
            for (int c=0; c<a.size(); c++)
            {
                Contig cont =(Contig)a.get(c);
                System.out.println(cont.getSequence().length()+" "+cont.getSequence());
                 System.out.println(cont.getScores());
                 System.out.println(Algorithms.splitString(cont.getScores()," ").size());
            }
        }catch(Exception e){}
    }
}
