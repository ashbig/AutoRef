/*
 * @(#) GBParser.java
 *
 * Created on May 5, 2001, 1:38 PM
 * 
 * Copyright 2001-2001 Harvard Institute of Proteomics. All Rights Reserved.
 */
//package flex.ApplicationCode.Java.util;

import java.io.*;
import java.util.*;
/** This class used to fetch and parse a genbank record. Current Parsing is
 *  delegated to a perl program GBParser.pl
 *  USAGE: 1) create a new GBParser object: parser = new GBParser();
 *         2) add genes to retrieve and parse one by one: parser.addAGene("123435");
 *                                       add another one: parser.addAGene("23456");  
 *         3) call instance method getGBSeqHashtable() to get parsed result as 
 *             a hashtable in a data structure:  gi => GBSequence instance
 *             For example, 
 *                 Hashtable ht = parser.getGBSeqHashtable();    
 *         4) each gene is packaged as an instance of GBSequence. To get info of
 *            each gene parsed you have to use following codes
 *              Enumeration e = ht.elements();
 *              while (e.hasMoreElements()) {
 *                  GBSequence gbseq = (GBSequence)e.nextElement();
 *                  String gi = gbseq.getGI();
 *                  String acc = gbseq.getAccession();
 *                  String org = gbseq.getOrganism();
 *                  String start = gbseq.getStart();
 *                  String stop = gbseq.getStop();
 *                  String cds = gbseq.getCDS();
 *                  String status = gbseq.getStatus();
 *              }
 *
 * @author  twei
 * @version 1.0
 */
public class GBParser extends Object {
    /** storage for GBSequence objects constructed from
     *  info extracted from each genbank record.
     *  gi => GBSequence instance
     */
    Hashtable gbSeqHT = new Hashtable();
    
    /** store the gb genes to retrieved */
    Vector giArray = new Vector(20);
    
    /** result file name from perl parser */
    String result = null;
    
    /** Creates new GBParser 
     */
    public GBParser() {
    }
    
    /** add gene to the list to be retrieved */
    public int addAGene(String gi) {
        giArray.add(gi);
        return giArray.size();
    }
   
    
    public Hashtable getGBSeqHashtable() {
        getAndParse();
        try {
            String gi = null, acc = null, org = null;
            String start = null, stop = null, status = "OK", seq = null;
            BufferedReader bin = null;
            String line = null;
            bin = new BufferedReader(new FileReader(result));
            while ((line = bin.readLine()) != null) {
                //System.out.println(line);
                if (line.equalsIgnoreCase("//")) {
                    GBSequence gbSeq = new GBSequence();
                    gbSeq.setGI(gi);
                    gbSeq.setAccession(acc);
                    gbSeq.setCDS(seq);
                    gbSeq.setOrganism(org);
                    gbSeq.setStart(start);
                    gbSeq.setStop(stop);
                    gbSeq.setStatus(status);
                    gbSeqHT.put(gi, gbSeq);
                } else {
                    StringTokenizer st = new StringTokenizer(line, "!!");              
                    while (st.hasMoreTokens()) {
                        if (st.nextToken().equalsIgnoreCase("OK")) {
                            gi = st.nextToken();
                            acc = st.nextToken();
                            org = st.nextToken();
                            start = st.nextToken();
                            stop = st.nextToken();
                            seq = st.nextToken();
                        } else
                            break;
                    }
                }
            }
            bin.close();
        } catch (FileNotFoundException e) { //
            //
        } catch (IOException e) {
            //
        }
        cleanup();
        return gbSeqHT;
    }
   
    /** call perl program to fetch and parse gb record */
    private void getAndParse() {
        result = getResultFileName(giArray.size());
        String cmd = "perl GBParser.pl " + result;
        for (int i=0; i<giArray.size(); i++)
            cmd += (" " + (String)giArray.get(i));
        
        //System.out.println(cmd);  
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            if (p.exitValue() != 0) {
                System.out.println("Call perl failed");
                //throw new FlexUtilException("call GBSearch.pl failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
            //throw new FlexUtilException(e.getMessage());
        } 
        catch (InterruptedException e) {
            //throw new FlexUtilException(e.getMessage()));
        }              
    }

    private String getResultFileName(int size) {
        return new TempObject().getPageName();
    }
    
    private void cleanup() // throws FlexUtilException
    {
        File temp = new File(result);
        System.out.println(temp.getName());
        //temp.delete();
        
        System.out.println(temp.getName());
        if (temp.delete())
            System.out.println("Delete");
        else 
            System.out.println("Error");
        
    }
    
    public class TempObject {           
        public String getPageName() {
            return this.hashCode() + "_temp";
        }
    }

    class GBSequence extends Properties {        
        public GBSequence() {
            super();
        }
        
        public void setGI(String gi) { setProperty("gi", gi); }
        public void setAccession(String acc) { setProperty("accession", acc); }
        public void setOrganism(String org) { setProperty("organism", org); }
        public void setStart(String start) { setProperty("start", start); }
        public void setStop(String stop) { setProperty("stop", stop); }
        public void setCDS(String cds) { setProperty("cds", cds); }
        public void setStatus(String status) { setProperty("status", status); }
        
        public String getGI() { return getProperty("gi"); }
        public String getAccession() { return getProperty("accession"); }
        public String getOrganism() { return getProperty("organism"); }
        public String getStart() { return getProperty("start"); }
        public String getStop() { return getProperty("stop"); }
        public String getCDS() { return getProperty("cds"); }
        public String getStatus() { return getProperty("status"); }
    }
    
    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        GBParser test = new GBParser();
        test.addAGene("13926267");
        //test.addAGene("000");  // not working
        test.addAGene("13919642");
        Hashtable ht = test.getGBSeqHashtable();
        Enumeration e = ht.elements();
        while (e.hasMoreElements()) {
            GBSequence gbseq = (GBSequence)e.nextElement();
            System.out.println(gbseq.getGI());
            System.out.println(gbseq.getAccession());
            System.out.println(gbseq.getOrganism());
            System.out.println(gbseq.getStart());
            System.out.println(gbseq.getStop());
            System.out.println(gbseq.getCDS());
            System.out.println(gbseq.getStatus());
            System.out.println();
        }
    }

}
