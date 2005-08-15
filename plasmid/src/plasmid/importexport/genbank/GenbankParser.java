/*
 * GenbankParser.java
 *
 * Created on August 3, 2005, 11:40 AM
 */

package plasmid.importexport.genbank;

import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;

/**
 *
 * @author  DZuo
 */
public class GenbankParser {
    
    /** Creates a new instance of GenbankParser */
    public GenbankParser() {
    }
    
    public GenbankInfo parseGenbank(String genbank) throws Exception {
        String urlString = "http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val="+genbank;
        
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
        
        boolean isSeqStart = false;
        int cdscount = 0;
        String inputLine;
        boolean breakSeq = false;
        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
            //DEFINITION  Mus musculus RIKEN cDNA 0610009K11 gene (0610009K11Rik), mRNA.
            if(inputLine.trim().indexOf("DEFINITION") != -1 && definition.equals("")) {
                definition = inputLine.trim().substring(inputLine.indexOf("DEFINITION")+10).trim();
            }
            //ACCESSION   NM_026689
            if(inputLine.trim().indexOf("ACCESSION") != -1 && accession.equals("")) {
                StringTokenizer tokens = new StringTokenizer(inputLine);
                if(tokens.hasMoreTokens()) {
                    String ignore = tokens.nextToken();
                    accession = tokens.nextToken();
                }
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
            //                     /db_xref="GeneID:<a href=http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=retrieve&dopt=graphics&list_uids=68350>68350</a>"
            if(inputLine.indexOf("GeneID:") != -1 && geneid.equals("") ) {
                geneid = inputLine.substring(inputLine.indexOf(">")+1, inputLine.lastIndexOf("<") );
            }
            //                     /gene="0610009K11Rik"
            if(inputLine.indexOf("/gene=") != -1 && genesymbol.equals("") ) {
                genesymbol = inputLine.substring(inputLine.indexOf("\"")+1, inputLine.lastIndexOf("\"") - 1);
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
        
        GenbankInfo info = new GenbankInfo(genbank, definition,accession, accessionVersion,gi, organism, geneid, genesymbol, cdsstart, cdsstop, sequencetext);
        
        return info;
    }
}
