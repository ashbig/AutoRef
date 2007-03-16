/**
 * $Id: GenbankGeneFinder.java,v 1.18 2007-03-16 19:56:08 dzuo Exp $
 *
 * File     	: GenbankGeneFinder.java
 * Date     	: 05052001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.util;

import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import edu.harvard.med.hip.flex.core.GenbankSequence;

/**
 * GeneBankGeneFinder is an html parser class that performs genbank
 * search and result parsing. The search is performed by sending the http
 * request to Genbank website. This will give us the benefit of using
 * Genbank's search engine.
 */
public class GenbankGeneFinder {
    /**
     * Constructor.
     */
    public GenbankGeneFinder() {}
    
    /**
     * Perform the public database searching.
     *
     * @param query A query string used for search.
     *
     * @return A list of GenbankSequence object.
     */
    public Vector search(String query) throws FlexUtilException {
        try {
            Vector v = new Vector();
            String urlString = "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Search&db=Nucleotide&term="+query.replace(' ', '+');
            URL url = new URL(urlString);
            BufferedReader in = new BufferedReader(
            new InputStreamReader(
            url.openStream()));
            
            String inputLine;
            
            //looking for string like this:
            //<dd>
            //Homo sapiens myocardin (MYOCD), mRNA<br>
            //gi|23957691|ref|NM_153604.1|[23957691]<br>
            while ((inputLine = in.readLine()) != null) {
                if(inputLine.indexOf("<dd>") == 0) {
                    inputLine = in.readLine();
                    String desc = inputLine.substring(0, inputLine.indexOf("<br>"));
                    
                    String s = in.readLine();
                    StringTokenizer st = new StringTokenizer(s, "|");
                    String ignore = st.nextToken();
                    String gi = st.nextToken();
                    ignore = st.nextToken();
                    String gb = st.nextToken();
                    
                    GenbankSequence sequence = new GenbankSequence(gb, gi, desc);
                    v.addElement(sequence);
                }
            }
            in.close();
            return v;
        } catch (Exception e) {
            throw new FlexUtilException("Cannot search for "+query+"\n"+e.getMessage());
        }
    }
    
    /**
     * Perform the public database searching and parse out the detailed info.
     *
     * @param gi A query string used for search (GI number).
     *
     * @return A Hashtable that contains all the information.
     */
    /** public Hashtable searchDetail(String gi) throws FlexUtilException {
        try {
            Hashtable h = new Hashtable();
            String urlString = "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids="+gi+"&dopt=GenBank";
            URL url = new URL(urlString);
            BufferedReader in = new BufferedReader(
            new InputStreamReader(
            url.openStream()));
    
            String accession = "";
            String organism = ""; String gene_name = "";
            String locus_link_id = "";
            int start = -1;
            int stop = -1;
            String sequencetext = "";
            boolean cdsstart = false;
            int cdscount = 0;
            String inputLine;
            boolean breakSeq = false;
            while ((inputLine = in.readLine()) != null) {
                if(inputLine.trim().indexOf("ACCESSION") == 0 && accession.equals("")) {
                    StringTokenizer tokens = new StringTokenizer(inputLine);
                    if(tokens.hasMoreTokens()) {
                        String ignore = tokens.nextToken();
                        accession = tokens.nextToken();
                    }
                }
    
                if(inputLine.trim().indexOf("/organism=") == 0) {
                    organism = inputLine.substring(inputLine.indexOf("\"")+1, inputLine.lastIndexOf("\""));
                }
                // /db_xref="LocusID:<a href=http://www.ncbi.nlm.nih.gov/LocusLink/LocRpt.cgi?l=71>71</a>"
                if(inputLine.indexOf("LocusID:") != -1 && locus_link_id.equals("") ) {
                    locus_link_id = inputLine.substring(inputLine.indexOf(">")+1, inputLine.lastIndexOf("<") );
                }
                //  /gene="CKMT2"
                if(inputLine.indexOf("/gene=") != -1 && gene_name.equals("") ) {
                    gene_name = inputLine.substring(inputLine.indexOf("\"")+1, inputLine.lastIndexOf("\"") - 1);
                }
                if(inputLine.indexOf(">CDS</a>") != -1) {
                    cdscount++;
    
                    //genomic
                    if(cdscount>1) {
                        start = -1;
                        stop = -1;
                        break;
                    }
                    //genomic
                    if(inputLine.indexOf("join") != -1 || inputLine.indexOf("complement") != -1)
                        continue;
    
                    String substr = inputLine.substring(inputLine.indexOf(">CDS</a>"));
                    StringTokenizer st = new StringTokenizer(substr);
                    String cds = "";
                    if(st.hasMoreTokens()) {
                        String ignore = st.nextToken();
                        cds = st.nextToken();
                    }
                    st = new StringTokenizer(cds, "..");
                    if(st.hasMoreTokens()) {
                        String startStr = st.nextToken();
                        String newStart = startStr;
                        //missing start
                        if(startStr.indexOf("&lt;")!=-1) {
                            //newStart = startStr.substring(startStr.indexOf(";")+1);
                            newStart = "-2";
                        }
    
                        String stopStr = st.nextToken();
                        String newStop = stopStr;
                        //missing stop
                        if(stopStr.indexOf("&gt;") != -1) {
                            newStop = "-2";
                        }
                        start = Integer.parseInt(newStart);
                        stop = Integer.parseInt(newStop);
                    }
                }
    
                if(inputLine.indexOf("ORIGIN") != -1) {
                    cdsstart = true;
                    breakSeq = true;
                    continue;
                }
    
                if(inputLine.indexOf("//") == 0 && breakSeq) {
                    cdsstart = false;
                    breakSeq = false;
                    break;
                }
    
                if(cdsstart) {
                    StringTokenizer st = new StringTokenizer(inputLine);
                    while(st.hasMoreTokens()) {
                        sequencetext = sequencetext+st.nextToken(" 1234567890");
                    }
                }
            }
    
            in.close();
    
            h.put("accession",  accession);
            h.put("species", organism);
            h.put("start", new Integer(start));
            h.put("stop", new Integer(stop));
            /*for new format of ncbi delete <a... html
            //ORIGIN
<a name="sequence_64652955"></a>  */
    /**        if ( sequencetext.indexOf(">") != -1)
     * sequencetext = sequencetext.substring(sequencetext.lastIndexOf(">") + 1);
     * h.put("sequencetext", sequencetext);
     * if (! locus_link_id.equals("")) h.put("locus_link", locus_link_id);
     * if (! gene_name.equals("")) h.put("gene_name", gene_name);
     *
     * return h;
     * } catch (Exception e) {
     * throw new FlexUtilException("Cannot do search for gi: "+gi+"\n"+e.getMessage());
     * }
     * }*/
    
    /**
     * Perform the public database searching and parse out the detailed info.
     *
     * @param s A query string used for search (GI number or Genbank Accession number).
     *
     * @return A Hashtable that contains all the information.
     */
    public Hashtable searchDetail(String s) throws FlexUtilException {
        try {
            Hashtable h = new Hashtable();
            String urlString = "http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val="+s;
            URL url = new URL(urlString);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            
            String definition = "";
            String accession = "";
            String accessionVersion = "";
            String gi = "";
            String organism = "";
            String geneid = "";
            String genesymbol = "";
            int cdsstart = -1;
            int cdsstop = -1;
            String sequencetext = "";
            String chromosome = "";
            
            boolean isSeqStart = false;
            int cdscount = 0;
            String inputLine;
            boolean breakSeq = false;
            boolean definitionStop = false;
            while ((inputLine = in.readLine()) != null) {
                //System.out.println(inputLine);
                //ACCESSION   NM_026689
                if(inputLine.trim().indexOf("ACCESSION") != -1 && accession.equals("")) {
                    definitionStop = true;
                    StringTokenizer tokens = new StringTokenizer(inputLine);
                    if(tokens.hasMoreTokens()) {
                        String ignore = tokens.nextToken();
                        accession = tokens.nextToken();
                    }
                }
                //DEFINITION  Mus musculus RIKEN cDNA 0610009K11 gene (0610009K11Rik), mRNA.
                if(!definitionStop) {
                    if(inputLine.trim().indexOf("DEFINITION") != -1)
                        definition = inputLine.trim().substring(inputLine.indexOf("DEFINITION")+10).trim();
                    else
                        definition += " "+ inputLine.trim();
                }
                //VERSION     NM_026689.3  GI:40548428
                if(inputLine.indexOf("VERSION") != -1 && accessionVersion.equals("") ) {
                    String tmp = inputLine.substring(inputLine.indexOf("VERSION:")+8);
                    accessionVersion = tmp.substring(0, tmp.indexOf("GI:")).trim();
                }
                if(inputLine.indexOf("GI:") != -1 && gi.equals("") ) {
                    gi = inputLine.substring(inputLine.indexOf("GI:")+3);
                }
                //                     /organism="Mus musculus"
                if(inputLine.trim().indexOf("/organism=") != -1) {
                    organism = inputLine.substring(inputLine.indexOf("\"")+1, inputLine.lastIndexOf("\""));
                }
                //                      /chromosome="22"
                if(inputLine.trim().indexOf("/chromosome=") != -1) {
                    chromosome = inputLine.substring(inputLine.indexOf("\"")+1, inputLine.lastIndexOf("\""));
                }
                //                     /db_xref="GeneID:<a href=http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=retrieve&dopt=graphics&list_uids=68350>68350</a>"
                if(inputLine.indexOf("GeneID:") != -1 && geneid.equals("") ) {
                    geneid = inputLine.substring(inputLine.indexOf(">")+1, inputLine.lastIndexOf("<") );
                }
                //                     /gene="0610009K11Rik"
                if(inputLine.indexOf("/gene=") != -1 && genesymbol.equals("") ) {
                    genesymbol = inputLine.substring(inputLine.indexOf("\"")+1, inputLine.lastIndexOf("\""));
                }
                //     <a href=/entrez/viewer.fcgi?val=40548428&itemID=1&view=gbwithparts>CDS</a>             89..1147
                if(inputLine.indexOf(">CDS</a>") != -1) {
                    cdscount++;
                    
                    //genomic
                    if(cdscount>1) {
                        cdsstart = -1;
                        cdsstop = -1;
                        break;
                    }
                    //genomic
                    if(inputLine.indexOf("join") != -1 || inputLine.indexOf("complement") != -1)
                        continue;
                    
                    String substr = inputLine.substring(inputLine.indexOf(">CDS</a>"));
                    StringTokenizer st = new StringTokenizer(substr);
                    String cds = "";
                    if(st.hasMoreTokens()) {
                        String ignore = st.nextToken();
                        cds = st.nextToken();
                    }
                    st = new StringTokenizer(cds, "..");
                    if(st.hasMoreTokens()) {
                        String startStr = st.nextToken();
                        String newStart = startStr;
                        //missing start
                        if(startStr.indexOf("&lt;")!=-1) {
                            //newStart = startStr.substring(startStr.indexOf(";")+1);
                            newStart = "-2";
                        }
                        
                        String stopStr = st.nextToken();
                        String newStop = stopStr;
                        //missing stop
                        if(stopStr.indexOf("&gt;") != -1) {
                            newStop = "-2";
                        }
                        cdsstart = Integer.parseInt(newStart);
                        cdsstop = Integer.parseInt(newStop);
                    }
                }
                //ORIGIN
                //1 tcgacgggcg acgggttccg ggaagggctg cgcggctgcg taggggtcgc aggtgatttc
                if(inputLine.indexOf("ORIGIN") != -1) {
                    isSeqStart = true;
                    breakSeq = true;
                    continue;
                }
                
                if(inputLine.indexOf("//") == 0 && breakSeq) {
                    isSeqStart = false;
                    breakSeq = false;
                    sequencetext = sequencetext.substring(sequencetext.indexOf("</a>")+4);
                    break;
                }
                
                if(isSeqStart) {
                    StringTokenizer st = new StringTokenizer(inputLine);
                    while(st.hasMoreTokens()) {
                        sequencetext = sequencetext+st.nextToken(" 1234567890");
                    }
                }
            }
            
            in.close();
            
            h.put("accession",  accession);
            h.put("accessionVersion", accessionVersion);
            h.put("gi", gi);
            h.put("definition", definition);
            h.put("species", organism);
            h.put("start", new Integer(cdsstart));
            h.put("stop", new Integer(cdsstop));
            h.put("sequencetext", sequencetext);
            if (! geneid.equals("")) h.put("locus_link", geneid);
            if (! genesymbol.equals("")) h.put("gene_name", genesymbol);
            if (! chromosome.equals("")) h.put("chromosome", chromosome);
            
            return h;
        } catch (Exception e) {
            throw new FlexUtilException("Cannot do search for identifier: "+s+"\n"+e.getMessage());
        }
    }
    
    //**************************************************************//
    //							Test								//
    //**************************************************************//
    public static void main(String [] args) {
        GenbankGeneFinder finder = new GenbankGeneFinder();
        try{
            Vector v = finder.search("NM_001011658");
            Enumeration enu = v.elements();
            while(enu.hasMoreElements()) {
                GenbankSequence sequence = (GenbankSequence)enu.nextElement();
                System.out.println("Accession: "+sequence.getAccession());
                System.out.println("GI: "+sequence.getGi());
                System.out.println("Description: "+sequence.getDescription());
                System.out.println();
            }
            
            Hashtable h = finder.searchDetail("NM_001098");
            System.out.println(h);
        } catch (FlexUtilException e) {
            System.out.println(e);
        }
    }
}

