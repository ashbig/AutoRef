/**
 * $Id: GenbankGeneFinder.java,v 1.5 2001-07-23 20:49:06 dzuo Exp $
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
            
            while ((inputLine = in.readLine()) != null) {
                if(inputLine.indexOf("<dd>") == 0) {
                    String desc = inputLine.substring(inputLine.indexOf("<dd>")+4, inputLine.indexOf("<br>"));
                    StringTokenizer st = new StringTokenizer(inputLine.substring(inputLine.indexOf("<br>")+4));
                    if (st.hasMoreTokens()) {
                        String giIgnore = st.nextToken("|<");
                        String gi = st.nextToken("|<");
                        String gbIgnore = st.nextToken("|<");
                        String gb = st.nextToken("|<");
                        GenbankSequence sequence = new GenbankSequence(gb, gi, desc);
                        v.addElement(sequence);
                    }
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
    public Hashtable searchDetail(String gi) throws FlexUtilException {
        try {
            Hashtable h = new Hashtable();
            String urlString = "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids="+gi+"&dopt=GenBank";
            URL url = new URL(urlString);
            BufferedReader in = new BufferedReader(
            new InputStreamReader(
            url.openStream()));
            
            String organism = "";
            int start = -1;
            int stop = -1;
            String sequencetext = "";
            boolean cdsstart = false;
            int cdscount = 0;
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if(inputLine.trim().indexOf("/organism=") == 0) {
                    organism = inputLine.substring(inputLine.indexOf("\"")+1, inputLine.lastIndexOf("\""));
                }
                
                if(inputLine.indexOf(">CDS</a>") != -1) {
                    cdscount++;
                    
                    if(cdscount>1) {
                        start = -1;
                        stop = -1;
                        break;
                    }
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
                        if(startStr.indexOf("&lt;")!=-1) {
                            //newStart = startStr.substring(startStr.indexOf(";")+1);
                            newStart = "-1";
                        }
                        
                        String stopStr = st.nextToken();
                        String newStop = stopStr;
                        if(stopStr.indexOf("&gt;") != -1) {
                            newStop = "-1";
                        }
                        start = Integer.parseInt(newStart);
                        stop = Integer.parseInt(newStop);
                    }
                }
                
                if(inputLine.indexOf("ORIGIN") != -1) {
                    cdsstart = true;
                    continue;
                }
                
                if(inputLine.indexOf("//") == 0) {
                    cdsstart = false;
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
            
            h.put("species", organism);
            h.put("start", new Integer(start));
            h.put("stop", new Integer(stop));
            h.put("sequencetext", sequencetext);
            
            return h;
        //} catch (Exception e) {
        } catch (IOException e) {
            throw new FlexUtilException("Cannot do search for gi: "+gi+"\n"+e.getMessage());
        }
    }
    
    //**************************************************************//
    //							Test								//
    //**************************************************************//
    public static void main(String [] args) {
        GenbankGeneFinder finder = new GenbankGeneFinder();
        try {
            Vector v = finder.search("human kinase");
            Enumeration enum = v.elements();
            while(enum.hasMoreElements()) {
                GenbankSequence sequence = (GenbankSequence)enum.nextElement();
                System.out.println("Accession: "+sequence.getAccession());
                System.out.println("GI: "+sequence.getGi());
                System.out.println("Description: "+sequence.getDescription());
                System.out.println();
            }
            
            Hashtable h = finder.searchDetail("13016955");
            System.out.println(h);
        } catch (FlexUtilException e) {
            System.out.println(e);
        }
    }
}

