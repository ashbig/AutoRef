/*
 * PhrePhrapParser.java
 *
 * Created on August 4, 2003, 6:31 PM
 */

package edu.harvard.med.hip.bec.programs.assembler;

import edu.harvard.med.hip.bec.Constants;
import java.util.*;
import java.io.*;
import org.apache.regexp.*;
//import edu.harvard.med.hip.bec.engine.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.bioutil.*;
import  edu.harvard.med.hip.bec.coreobjects.sequence.*;
import  edu.harvard.med.hip.bec.file.*;
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
    
    
  
    private static  RE p_assembly_info = null ; 
    private static  RE p_contig_info= null ; 
    private static  RE p_contig_quality_info = null ; 
    private static  RE p_read_location = null ; 
    private static  RE p_alignmet_element = null ; 
    private static  RE p_read_info = null ; 
    private static  RE p_read_quality = null ; 
    private static  RE p_description = null ; 
  
    /** Creates a new instance of Needle */
    private static void initialiseRE() throws BecUtilException
    {
        try{
          // # Identity:    1120/1147 (97.6%)
            p_assembly_info = new RE("^AS (\\d+) (\\d+)");
            p_contig_info = new RE("^CO\\s*Contig(\\d+)\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)\\s*(\\w+)");
            p_contig_quality_info = new RE("^BQ"); 
            p_read_location = new RE("^AF (\\S+) (C|U) (-*\\d+)");
            p_alignmet_element = new RE("^BS (\\d+) (\\d+) (\\S+)");
            p_read_info = new RE("^RD (\\S+) (-*\\d+) (\\d+) (\\d+)");
            p_read_quality = new RE("^QA (-?\\d+) (-?\\d+) (-?\\d+) (-?\\d+)");
        //    p_description = new RE("^DS / && do {#	    /CHEM: (\\S+)");
           // p_t = new RE("/^RT\s*\{"); 
           // p_ = new RE("/^WA\s*\{");
        }
        catch(Exception e){throw new BecUtilException("Cannot build patterns for phrap parser");}
    }
    
    /** Creates a new instance of PhrePhrapParser */
    public PhredPhrapParser()
    {
    }
    
    public CloneAssembly    parseAllData(String phrap_output)throws Exception
    {
        
        if ( !FileOperations.isFileExists(phrap_output)) return null;
        String line = null;
        ReadInAssembly read = null;
        CloneAssembly clone_assembly = null;
        Contig contig = null;
        BufferedReader  fin = null;
        Hashtable contig_reads = null;
        StringBuffer scores = new StringBuffer();
        StringBuffer sequence = new StringBuffer();
        try 
        {
            if(p_assembly_info==null) initialiseRE();
            fin = new BufferedReader(new FileReader(phrap_output));
            while ((line = fin.readLine()) != null)
            {
                if (line.trim().equals("")) continue;
               //start assembly
               if ( p_assembly_info.match(line) ) //AS 1 2
               {
                    if ( Integer.parseInt( p_assembly_info.getParen(1)) == 0) return null;
                    clone_assembly = new CloneAssembly();
                    clone_assembly.setNumOfReads( Integer.parseInt(p_assembly_info.getParen(2) ));
      
               }
               //contig start
               else if ( p_contig_info.match(line) )
               {
                   contig = new Contig();
                   clone_assembly.addContig(contig);
                    contig.setNumberOfReadsInContig(Integer.parseInt(p_contig_info.getParen(3)));
                    contig.setNumberOfBases(Integer.parseInt(p_contig_info.getParen(2)));
                    contig.setName("Contig"+p_contig_info.getParen(1));
                    sequence = new StringBuffer();
                    while ((line = fin.readLine()) != null && !line.trim().equals("") )
                    {
                        line = line.trim();
                        line = line.replace('x','n');
                        sequence.append(line);
                    }
                    contig.setSequence( sequence.toString() );
                    contig_reads = new Hashtable();
                        
               }
               else if (p_contig_quality_info.match(line))
               {
                   scores = new StringBuffer();
                    while ((line = fin.readLine()) != null && !line.trim().equals("")  )
                    {
                        line = line.trim();
                        scores.append(line+" ");
                    }
                    contig.setScores( scores.toString());
               }
               else if ( p_read_location.match(line) )
               {
                   read = new ReadInAssembly();
                   read.setName ( p_read_location.getParen(1) );
                   read.setOrientation ( convertOrientation( (String)p_read_location.getParen(2)) );
                   read.setStart (Integer.parseInt(p_read_location.getParen(3) ));
	           contig_reads.put(read.getName(), read);
               }
	 // (Base Segment field)#	/^BS (\d+) (\d+) (\S+)/ && do {#	    if (exists($self->{'contigs'}[$contig]{'reads'}{$3}{'segments'})) {#		$self->{'contigs'}[$contig]{'reads'}{$3}{'segments'} .= " " . $1 . " " . $2;#	    } else { $self->{'contigs'}[$contig]{'reads'}{$3}{'segments'} = $1 . " " . $2 }#	};
               else if ( p_read_info.match(line) )
               {
                   read = ( ReadInAssembly) contig_reads.get(p_read_info.getParen(1));
                   sequence = new StringBuffer();
                   while ((line = fin.readLine()) != null && !line.trim().equals("") )
                   {
                        line = line.trim();
                        line = line.replace('x','n');
                        sequence.append(line);
                   }
                   if (read != null)  read.setSequence( sequence.toString() );
               }
               else if ( p_read_quality.match(line) )
               {
                    read.setQualityStart (Integer.parseInt(p_read_quality.getParen(1)));
                    read.setQualityEnd (Integer.parseInt(p_read_quality.getParen(2)));
                    
                    read.setAlignStart ( Integer.parseInt(p_read_quality.getParen(3)));     //end / reverse
                    read.setAlignEnd ( Integer.parseInt(p_read_quality.getParen(4)));
                    if ( !( (read.getQualityStart () == -1 && read.getQualityEnd() == -1)
                    || (read.getAlignStart () == -1 && read.getAlignEnd () == -1)))
                    {
                        contig.addRead(read);
                    }
               }
            
	   

	//	/^DS / && do {#	    /CHEM: (\S+)/ $self->{'contigs'}[$contig]{'reads'}{$read_name}{'chemistry'} = $1;#	    };#	    /CHROMAT_FILE: (\S+)/ && do {#		$self->{'contigs'}[$contig]{'reads'}{$read_name}{'chromat_file'} = $1;#	    };#	    /DIRECTION: (\w+)/ && do {#		my $ori = $1;#		if    ($ori eq 'rev') { $ori = 'C' }#		elsif ($ori eq 'fwd') { $ori = 'U' }#		$self->{'contigs'}[$contig]{'reads'}{$read_name}{'strand'} = $ori;#	    };#	    /DYE: (\S+)/ && do {#		$self->{'contigs'}[$contig]{'reads'}{$read_name}{'dye'} = $1;#	    };#	    /PHD_FILE: (\S+)/ && do {#		$self->{'contigs'}[$contig]{'reads'}{$read_name}{'phd_file'} = $1;#	    };#	    /TEMPLATE: (\S+)/ && do {#		$self->{'contigs'}[$contig]{'reads'}{$read_name}{'template'} = $1;#	    };#	    /TIME: (\S+ \S+ \d+ \d+\:\d+\:\d+ \d+)/ && do {#		$self->{'contigs'}[$contig]{'reads'}{$read_name}{'phd_time'} = $1;#	    };#	};
	//    /^CT\s*\{/ && do {
	   //	/^RT\s*\{/ && do {
	   //	/^WA\s*\{/ && do {}
            }
            fin.close();
            return clone_assembly;
        }
        catch(Exception e)
        {
            try
            {fin.close();}catch(Exception c)
            {throw new BecUtilException("Cannot parse phrap output "+phrap_output);}
            throw new BecUtilException("Cannot parse phrap output "+phrap_output);
        }
	
}

    
    public  CloneAssembly parse(String foutput_name) throws BecUtilException
    {
         if ( !FileOperations.isFileExists(foutput_name)) return null;
        
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
                    line.replace('x','n');
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
        //CO(0) Contig1(1) 2591 6(3) 378 U(5)
	Contig result = null;
        ArrayList items = Algorithms.splitString(CO_line, " ");
    	result = new Contig();
        result.setNumberOfReadsInContig( Integer.parseInt((String) items.get(3)));
        result.setName( (String) items.get(1) );
        result.setOrientation( convertOrientation ( (String) items.get(5)));
        return result;
    }
    
    
    private int convertOrientation(String ort)
    {
        if (ort.equalsIgnoreCase("U"))
            return Constants.ORIENTATION_FORWARD; 
        else 
            return   Constants.ORIENTATION_REVERSE;
    }
    public static void main(String args[])
    {
        try
        {
       
            String  foutput_name = "\\\\Bighead\\data\\trace_files_root\\clone_samples\\31875\\190984\\contig_dir\\190984.fasta.screen.ace.1";
            PhredPhrapParser pp= new PhredPhrapParser();
            CloneAssembly cl = pp.parseAllData( foutput_name); 
            ArrayList a = cl.getContigs();
            System.out.println("contigs "+a.size() );
            /*
            for (int c=0; c<a.size(); c++)
            {
                Contig cont =(Contig)a.get(c);
                for (int t = 0; t < cont.getReads().size();t++)
                {
                    ReadInAssembly read = (ReadInAssembly) cont.getReads().get(t);
                    String scores = PhredPhrap.getScoresFromPhdFile(read.getName(), "C:\\bio\\plate_analysis\\clone_samples\\2055\\3214\\phd_dir");
                     int[] scores_as_array =  Algorithms.getConvertStringToIntArray( scores, " ") ;
                    //take into acount that phrap gives sense reading of the read in output while scores
                    // are in original direction
                    if ( read.getOrientation() == Constants.ORIENTATION_REVERSE)
                    {
                        int[] compliment_scores = new int[scores_as_array.length];
                        for (int ii =  scores_as_array.length ;  ii > 0; ii--)
                        {
                            compliment_scores[scores_as_array.length - ii ] = scores_as_array[ii - 1];
                        }
                        scores_as_array = compliment_scores;
                    }
                    read.setScores( scores_as_array );
 
                }
             //     cont.prepareContigAligment("refseq");
                  ScoredElement[][]  bases = cont.prepareContigAligmentHorizontalStructure("refsequence");
                  
                  
                  ArrayList gaps = cont.findContigs(bases);
                  for (int count =0; count < gaps.size(); count++)
                  {
                      Stretch s = (Stretch) gaps.get(count);
                      System.out.println(count + " "+s.getCdsStart()+" "+s.getCdsStop() );
                      System.out.println(s.getSequence().getText().length()+" "+s.getSequence().getText() );
                      
                      System.out.println(s.getSequence().getScoresAsArray().length +" "+s.getSequence().getScores() );
                  }
                  gaps = cont.findGaps(bases);
                  for (int count =0; count < gaps.size(); count++)
                  {
                      Stretch s = (Stretch) gaps.get(count);
                      System.out.println(count + " "+s.getCdsStart()+" "+s.getCdsStop() );
                              }
             //   System.out.println(cont.getSequence().length()+" "+cont.getSequence());
              //   System.out.println(cont.getScores());
              //   System.out.println(cont.getReads().size());
              //   for (int t = 0; t < cont.getReads().size();t++)
              //   {
              //       ReadInAssembly r = (ReadInAssembly) cont.getReads().get(t);
              //       getScoresFromPhdFile(String file_name, String file_location)throws BecUtilException
                     
             //   }
                 //System.out.println(Algorithms.splitString(cont.getScores()," ").size());
            }
            /*
     foutput_name = "C:\\BEC_RESEARCH\\1114\\112365\\contig_dir\\a.fasta.screen.ace.1";
                   
             cl = pp.parseAllData( foutput_name); 
             a = cl.getContigs();
            System.out.println("contigs "+a.size() );
            for (int c=0; c<a.size(); c++)
            {
                Contig cont =(Contig)a.get(c);
                cont.prepareContigAligment();
                System.out.println(cont.getSequence().length()+" "+cont.getSequence());
                 System.out.println(cont.getScores());
                 System.out.println(cont.getReads().size());
                 for (int t = 0; t < cont.getReads().size();t++)
                 {
                     ReadInAssembly r = (ReadInAssembly) cont.getReads().get(t);
                     System.out.println(r.getName()+" "+r.getAlignStart() +" "+retAlignEnd()+" "+r.getOrientation());
                     
                 }
                 //System.out.println(Algorithms.splitString(cont.getScores()," ").size());
             *
             *
             
            }**/
        }catch(Exception e){}
    }
}
