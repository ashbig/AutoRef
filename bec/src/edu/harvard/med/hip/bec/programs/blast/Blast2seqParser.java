/*
 * Blast2seqParser.java
 *
 * Created on October 23, 2002, 9:49 AM
 */

package edu.harvard.med.hip.bec.programs.blast;

import java.io.*;
import java.util.*;
import org.apache.regexp.*;

/**
 *
 * @author  htaycher
 */
public class Blast2seqParser
{
    private String  m_query_file_name = null;
    
    //paterns
    /*
  private static final Pattern p_letters = Pattern.compile("\\s+\\((\\d+)\\sletters)");
    private static final Pattern p_matchStart = Pattern.compile("^((\\S+).*");
    private static final Pattern p_length = Pattern.compile("\\s+Length\\s*=\\s*(\\d)");
    private static final Pattern p_score = Pattern.compile("Score\\s*=\\s*(\\d)");//Score =  159 bits (342)
    private static final Pattern p_expect = Pattern.compile("Expect\\s*=\\s*(\\S+)");
    private static final Pattern p_identity = Pattern.compile("Identities\\s*=\\s*(\\S+)");//Identities = 67/67 (100%)
    private static final Pattern p_positives = Pattern.compile("Positives\\s*=\\s*(\\S+)");//Positives = 67/67 (100%)
    private static final Pattern p_gaps = Pattern.compile("Gaps\\s*=\\s*(\\d+)");//Gaps = 2/236 (0%)
    private static final Pattern p_frame = Pattern.compile("Frame\\s*=\\s*(\\d+)");//Frame = +3 / +3
    private static final Pattern p_strand = Pattern.compile("Strand\\s*=\\s*(\\S+)\\s* /\\s*(\\S+)");//Strand = Plus / Plus
    private static final Pattern p_query = Pattern.compile("^Query:\\s+(\\d+).*\\s(d+)\\s*$");
    private static final Pattern p_subject = Pattern.compile("^Sbjct:\\s+(\\d+).*\\s(d+)\\s*$");
     
    private static final Pattern p_stop1= Pattern.compile("^s*$");
    private static final Pattern p_stop2= Pattern.compile("^[\\d]$");
    private static final Pattern p_stop3= Pattern.compile("^Query:");
    private static final Pattern p_stop4= Pattern.compile("^>");
     **/
    private static  RE p_letters = null ;
    private static  RE p_matchStart = null ;
    private static  RE p_length = null ;
    private static  RE p_score = null ;
    private static  RE p_expect = null ;
    private static  RE p_identity = null ;
    private static  RE p_positives = null ;
    private static  RE p_gaps = null ;
    private static  RE p_frame = null ;
    private static  RE p_strand = null ;
    private static  RE p_query = null ;
    private static  RE p_subject = null ;
    
    private static  RE p_stop1= null ;
    private static  RE p_stop2= null ;
    private static  RE p_stop3= null ;
    private static  RE p_stop4= null ;
    
    private static Blast2seqParser m_instance = null;
    
    
    //colors for blast html output
    public static final String COLOR_N_MISTMATCH_SUBSTITUTION = "FFFFFF";
    public static final String COLOR_N_MISTMATCH_DELETION = "F10000";
    
    public static final String COLOR_P_MISTMATCH_CONSERVATIVE = "FF00FF";
    public static final String COLOR_P_MISTMATCH_NONCONSERVATIVE = "FF0FFF";
    
    public static final int BLAST_FILE_TYPE_N = 0;
    public static final int BLAST_FILE_TYPE_P = 1;
    
    private  Blast2seqParser()
    {
        try
        {
            p_letters = new RE("\\s+\\((\\d+)\\s+letters\\)"); //+
            p_matchStart = new RE(">.*");//+
            p_length = new RE("\\s+Length\\s*=\\s*(\\d+)");//+
            p_score = new RE("Score\\s*=\\s*(\\d+)");//Score =  159 bits (342)//+
            p_expect = new RE(".*Expect(\\S*)\\s*=\\s*(\\S+)");
            
            p_identity = new RE("Identities\\s*=\\s*(\\d+)\\/(\\d+)\\s*\\((\\d+)\\%\\)");//+Identities = 67/67 (100%)
            p_positives = new RE(".*Positives\\s*=\\s*(\\d+)\\/(\\d+)\\s*\\((\\d+)\\%\\)");//Positives = 67/67 (100%)
            p_gaps = new RE("Gaps\\s*=\\s*(\\d+)");// + Gaps = 2/236 (0%)
            p_frame = new RE("Frame\\s*=\\s*([+-])(\\d+)\\s*/*\\s*(([+-])(\\d+))*");// + Frame = +3 / +3//+
            p_strand = new RE("Strand\\s*=\\s*(\\S+)\\s*\\/\\s*(\\S+)");//+ Strand = Plus / Plus
            p_query = new RE("^Query:\\s+(\\d+)\\s*(\\S+)\\s*(\\d+)\\s*$");
            p_subject = new RE("Sbjct:\\s*(\\d+)\\s*(\\S+)\\s*(\\d+)\\s*");
            
            p_stop1= new RE("^s*$");
            p_stop2= new RE("^[\\d]$");
            p_stop3= new RE("^Query:");
            p_stop4= new RE(">.*");
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    
    
    
    //method returns arraylist of hashtables
    //where each hashtable is one match
    
    public static ArrayList parse(String queryFile, int hits) throws ParseException
    {
        if (m_instance == null)
            m_instance = new Blast2seqParser();
        boolean isStartOfMatch = false;
        boolean isInHit = false;
        boolean isStartOfHit  = false;
        boolean isSequenceMatch = false;
        String line = null;
        BufferedReader  fin = null;
        String letters = null;
        String length = null;
        ArrayList res = new ArrayList();
        String query = ""; String qend = "";
        String subject = "";String send = "";
        Hashtable cur_hit = null;
        
        try
        {
            fin = new BufferedReader(new FileReader(queryFile));
            while ((line = fin.readLine()) != null)
            {
                //System.out.println(line);
                //finished read hits - exit
                if ( line.indexOf("Lambda") != -1)
                {
                    if (cur_hit != null)
                    {
                        cur_hit.put("sstop", send );
                        cur_hit.put("qstop", qend );
                        cur_hit.put("query", query);
                        cur_hit.put("length",length);
                        cur_hit.put("subject", subject);
                        query ="";
                        subject="";
                        res.add(cur_hit);
                    }
                    return res;
                }
                //read subject
                if ( p_letters.match(line) )
                    letters = p_letters.getParen(1);
                if (p_matchStart.match(line) )
                {
                    isStartOfMatch = true;
                    continue;
                }
                if ( isStartOfMatch)
                {
                    if ( p_length.match(line) )
                        length=p_length.getParen(1);
                    if ( p_score.match(line) )
                    {
                        isStartOfHit = true;
                        if (cur_hit != null)
                        {
                            cur_hit.put("sstop", send );
                            cur_hit.put("qstop", qend );
                            cur_hit.put("query", query);
                            cur_hit.put("subject", subject);
                            cur_hit.put("length",length);
                            res.add(cur_hit);
                            if (res.size() == hits) return res;
                            query="";
                            subject="";
                        }
                        
                        cur_hit = new Hashtable();
                        cur_hit.put("score", p_score.getParen(1) );
                    }
                    if ( p_expect.match(line) )
                    { cur_hit.put("expect", p_expect.getParen(2) );                }
                    if ( p_identity.match(line) )
                    { cur_hit.put("identity", p_identity.getParen(3) );                }
                    if ( p_gaps.match(line) )
                    { cur_hit.put("gaps", p_gaps.getParen(1) ); }
                    if ( p_strand.match(line) )
                    {
                        cur_hit.put("qstrand",  p_strand.getParen(1) );
                        cur_hit.put("sstrand",  p_strand.getParen(2) );
                    }
                    if ( p_frame.match(line) )
                    {
                        cur_hit.put("qstrand",  p_frame.getParen(1) );
                        cur_hit.put("qframe",  p_frame.getParen(2) );
                        if ( p_frame.getParenCount() > 3 && !p_frame.getParen(3).equals("") )
                        {
                            cur_hit.put("sstrand",  p_frame.getParen(4) );
                            cur_hit.put("sframe",  p_frame.getParen(5) );
                        }
                    }
                    if ( p_query.match( line)  )
                    {
                        //set query start
                        if (isStartOfHit && !cur_hit.containsKey("qstart"))
                        {
                            isSequenceMatch = true;
                            cur_hit.put("qstart",  p_query.getParen(1) );
                        }
                        query += p_query.getParen(2);
                        qend =  p_query.getParen(3);
                    }
                    if ( p_subject.match( line) )
                    {
                        //set query start
                        if (isStartOfHit && !cur_hit.containsKey("sstart") )
                        {   cur_hit.put("sstart",  p_subject.getParen(1) );                   }
                        subject += p_subject.getParen(2);
                        send = p_subject.getParen(3);
                    }
                }
                
            }
            
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            throw new ParseException("Cannot parse blast output");
        }
        return null;
    }
    
    
    public static BlastResult parseBl2seqResult(String queryFile, int hits_number)   throws ParseException
    {
        
        
        BlastResult result = null;
        ArrayList hits = new ArrayList();
        int sstrand = -1; int qstrand = -1; int qframe = -1; int sframe = -1;int gaps = -1;
        String strand = null;
        
        ArrayList parsed_hits = parse( queryFile,  hits_number);
        for (int hit_count = 0; hit_count < parsed_hits.size(); hit_count++)
        {
            Hashtable hit_data = (Hashtable) parsed_hits.get(hit_count);
            if ( hit_data.containsKey("qframe") )
                qframe = Integer.parseInt( (String)hit_data.get("qframe"));
            else
                qframe = 0;
            
            if ( hit_data.containsKey("gaps") )
                gaps = Integer.parseInt( (String)hit_data.get("gaps"));
            else
                gaps = 0;
            
            if ( hit_data.containsKey("sframe") )
                sframe = Integer.parseInt( (String)hit_data.get("sframe"));
            else
                sframe = 0;
            
            if ( hit_data.containsKey("sstrand") )
            {
                strand = (String)hit_data.get("sstrand");
                if ( strand.equalsIgnoreCase("plus") || strand.equalsIgnoreCase("+"))
                    sstrand = 1;
                else
                    sstrand = -1;
            }
            else
                sstrand = 0;
            
            if ( hit_data.containsKey("qstrand") )
            {
                strand = (String)hit_data.get("qstrand");
                if ( strand.equalsIgnoreCase("plus") || strand.equalsIgnoreCase("+"))
                    qstrand = 1;
                else
                    qstrand = -1;
            }
            else
                qstrand = 0;
            
            int score = Integer.parseInt( (String) hit_data.get("score"));
            double identity =    Double.parseDouble( (String) hit_data.get("identity")) ;
            String expect =   (String) hit_data.get("expect") ;
            int qstart =    Integer.parseInt( (String)   hit_data.get("qstart") );
            int  qstop=     Integer.parseInt( (String)  hit_data.get("qstop"));
            int   sstart =  Integer.parseInt( (String)   hit_data.get("sstart")) ;
            int sstop=    Integer.parseInt( (String)   hit_data.get("sstop") );
            int length =    Integer.parseInt( (String)   hit_data.get("length") );
            String query = (String)hit_data.get("query");
            String sbj =      (String) hit_data.get("subject");
            BlastAligment alg = new BlastAligment();
            alg.setScore( score);
            alg.setIdentity(identity);
            alg.setExpectValue(expect);
            
            alg.setQStrand(qstrand);
            alg.setSStrand(  sstrand);
            alg.setSFrame( sframe);
            alg.setQFrame( qframe);
            alg.setQStart(qstart);
            alg.setQStop(qstop);
            alg.setSStart(sstart);
            alg.setSStop(sstop);
            alg.setProgramScore(0);
            alg.setQSequence(query);
            alg.setSSequence(sbj  );
            alg.setNumberOfGapBases(gaps  );
            
            hits.add(alg);
        }
        
        result = new BlastResult();
        
        result.setAligments(hits);
        
        return result;
    }
    
    public static String formatBlastOutputToHTML(String queryFile, int mode) throws Exception
    {
        if (m_instance == null)
            m_instance = new Blast2seqParser();
        boolean isStartOfMatch = false;
        boolean isInHit = false;
        boolean isStartOfHit  = false;
        boolean isSequenceMatch = false;
        String line = null;
        BufferedReader  fin = null;
        
        String query = ""; String qend = "";
        String qstart = ""; String sstart = "";
        String subject = "";String send = "";
        String fquery = "";String fmatch = "";String fsubject = "";
        StringBuffer buf = new StringBuffer();
        
        try
        {
            fin = new BufferedReader(new FileReader(queryFile));
            
            while ((line = fin.readLine()) != null)
            {
                
                if ( p_query.match( line)  )
                {
                    //take next two lines
                    String line_match = fin.readLine();
                    String subject_line = fin.readLine();
                    
                    //set query items
                    
                    query = p_query.getParen(2);
                    
                    int start = line.indexOf(query) ;
                    int end = line.indexOf(query) + query.length() ;
                    qstart = line.substring(0,start);
                    qend =  line.substring(end);
                    //parse subject
                    p_subject.match( subject_line  );
                    
                    subject += p_subject.getParen(2);
                    sstart =  subject_line.substring(0,subject_line.indexOf(subject)) ;
                    send =  subject_line.substring(subject_line.indexOf(subject) + subject.length() );
                    
                    fmatch = line_match.substring(0,start);
                    //format string
                    
                    for (int curr_char = start  ; curr_char < end -1; curr_char++)
                    {
                        
                        if ( mode == BLAST_FILE_TYPE_N )
                        {
                            if (line_match.charAt(curr_char) == '|')
                            {
                                fquery += query.charAt(curr_char - start);
                                fmatch += line_match.charAt(curr_char);
                                fsubject += subject.charAt(curr_char - start);
                            }
                            else
                                if (subject.charAt(curr_char - start) =='-' || query.charAt(curr_char - start)=='-')
                                {
                                    fquery += "<FONT COLOR=\"" + COLOR_N_MISTMATCH_DELETION +"\">"+
                                    query.charAt(curr_char-start)+"</FONT>";
                                    fmatch += "<FONT COLOR=\"" + COLOR_N_MISTMATCH_DELETION +"\">"+
                                    line_match.charAt(curr_char)+"</FONT>";
                                    fsubject += "<FONT COLOR=\"" + COLOR_N_MISTMATCH_DELETION+ "\">"+
                                    subject.charAt(curr_char-start)+"</FONT>";
                                }
                                else
                                {
                                    fquery += "<FONT COLOR=\"" + COLOR_N_MISTMATCH_SUBSTITUTION+ "\">"+
                                    query.charAt(curr_char-start)+"</FONT>";
                                    fmatch += "<FONT COLOR=\"" + COLOR_N_MISTMATCH_SUBSTITUTION +"\">"+
                                    line_match.charAt(curr_char)+"</FONT>";
                                    fsubject += "<FONT COLOR=\"" + COLOR_N_MISTMATCH_SUBSTITUTION+ "\">"+
                                    subject.charAt(curr_char-start)+"</FONT>";
                                }
                        }
                        else if (mode == BLAST_FILE_TYPE_P)
                        {
                            if (line_match.charAt(curr_char) == query.charAt(curr_char-start) )
                            {
                                fquery += query.charAt(curr_char-start);
                                fmatch += line_match.charAt(curr_char);
                                fsubject += subject.charAt(curr_char-start);
                            }
                            else
                                if (line_match.charAt(curr_char)=='+')
                                {
                                    fquery += "<FONT COLOR=\"" + COLOR_P_MISTMATCH_CONSERVATIVE +"\">"+
                                    query.charAt(curr_char-start)+"</FONT>";
                                    fmatch += "<FONT COLOR=\"" + COLOR_P_MISTMATCH_CONSERVATIVE +"\">"+
                                    line_match.charAt(curr_char)+"</FONT>";
                                    fsubject += "<FONT COLOR=\"" + COLOR_P_MISTMATCH_CONSERVATIVE+ "\">"+
                                    subject.charAt(curr_char-start)+"</FONT>";
                                }
                                else
                                {
                                    fquery += "<FONT COLOR=\"" + COLOR_P_MISTMATCH_NONCONSERVATIVE+ "\">"+
                                    query.charAt(curr_char-start)+"</FONT>";
                                    fmatch += "<FONT COLOR=\"" + COLOR_P_MISTMATCH_NONCONSERVATIVE +"\">"+
                                    line_match.charAt(curr_char)+"</FONT>";
                                    fsubject += "<FONT COLOR=\"" + COLOR_P_MISTMATCH_NONCONSERVATIVE+ "\">"+
                                    subject.charAt(curr_char-start)+"</FONT>";
                                }
                        }
                    }
                }
                else
                {
                    if (!query.equals(""))
                    {
                        buf.append(qstart);buf.append(fquery);buf.append(qend+"\n");
                        buf.append(fmatch+"\n");
                        buf.append(sstart);buf.append(fsubject);buf.append(send+"\n");
                        query = ""; qend = "";qstart = ""; sstart = "";
                        subject = ""; send = ""; fquery = ""; fmatch = ""; fsubject = "";
                    }
                    buf.append(line+"\n");
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            throw new Exception();
        }
        finally
        {
            fin.close();
            
        }
        
        return buf.toString();
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
        String queryFile = "C:\\blastoutput\\b2n31462.out";
        try
        {
            // String sa = formatBlastOutputToHTML("c:\\blastoutput\\b2n31482.out", Blast2seqParser.BLAST_FILE_TYPE_N);
            //  String o = formatBlastOutputToHTML("c:\\blastoutput\\b2tp31482.out",Blast2seqParser.BLAST_FILE_TYPE_P);
            // System.out.println(sa);
            // System.out.println(o);
            ArrayList a = Blast2seqParser.parse(queryFile,2);
            a.size();
        }catch(Exception e)
        {}
    }
}
