/*
 * @(#) GenbankGeneFinder.java
 *
 * Created on May 3, 2001, 1:38 PM
 * 
 * Copyright 2001-2001 Harvard Institute of Proteomics. All Rights Reserved.
 */
//package flex.ApplicationCode.Java.util;

import java.io.*;
import java.util.*;
/** This class used to search genes using NCBI entrez service. 
 *  USAGE: 1) instantiate the GenbankGeneFinder with a search string
 *         2) call search() method which returns a Vector of EntrezItem object
 *         3) call EntrezItem getters to retrieve gi, accession and description
 *         4) call cleanup() to delete temp file from the system
 *
 * @author  twei
 * @version 1.0
 */
public class GenbankGeneFinder extends java.lang.Object {
    String srchStr;
    String pageName;
    Vector entrezItemVT;
    
    /** Creates new GenbankGeneFinder */
    public GenbankGeneFinder(String srchStr) {
        this.srchStr = srchStr;
        entrezItemVT = new Vector(20);
    }
    
    public Vector search() //throws FlexUtilException 
    { 
        pageName = new TempObject(srchStr).getPageName();
        //System.out.println("Page name: " + pageName);
	FileReader fin;
        
        // call perl routine to fetch and parse the first page
        String cmd = "perl GBSearch.pl " + pageName + " " + srchStr;
        //System.out.println(cmd);        
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            if (p.exitValue() != 0) {
                //throw new FlexUtilException("call GBSearch.pl failed");
            }
        } catch (IOException e) {
            //throw new FlexUtilException(e.getMessage());
        } catch (InterruptedException e) {
            //throw new FlexUtilException(e.getMessage()));
        }
        
        try {
            fin = new FileReader(pageName + ".txt");
            makeEntrezVT(fin);
            fin.close();
        } catch (FileNotFoundException e) {
	    e.printStackTrace();
            //throw new FlexUtilException(e.getMessage()));
        } catch (IOException e) {
	    e.printStackTrace();
            //throw new FlexUtilException(e.getMessage()));
        }
        return entrezItemVT;
    }
    
    public void cleanup() // throws FlexUtilException
    {
        File temp = new File(pageName + ".txt");
        /*System.out.println(temp.getName());
        if (temp.delete())
            System.out.println("Delete");
        else 
            System.out.println("Error");
         */
    }
    
    public Vector getEntrezVT() { return entrezItemVT; }
    private void makeEntrezVT(FileReader fin) // throws FlexUtilException
    {
        BufferedReader bin = new BufferedReader(fin);
        String line;
        String acc = null, gi = null, desc = null;
        
        try {
            while ((line = bin.readLine()) != null) {
                //System.out.println(line);
                if (line.equalsIgnoreCase("//")) {
                    EntrezItem item = new EntrezItem();
                    item.setAccession(acc);
                    item.setGI(gi);
                    item.setDescription(desc);
                    entrezItemVT.add(item);
                } else {
                    StringTokenizer st = new StringTokenizer(line, "!!");              
                    while (st.hasMoreTokens()) {
                        gi = st.nextToken();
                        acc = st.nextToken();
                        desc = st.nextToken();
                    }
                }
            }
        } catch (IOException e) {
            //throw new FlexUtilException(e.getMessage()));
        }
    }

    class TempObject {
        String str;
        long time;
        
        public TempObject(String str) {
            this.str = str;
            this.time = System.currentTimeMillis();
        }
        
        public String getPageName() {
            String fname = this.hashCode() + ".html";
            return fname;
        }
    }
    
    // This inner class represent 
    class EntrezItem extends java.util.Properties {
        public EntrezItem() {
            super();
        }
        
        public String getAccession() { return getProperty("accession"); }
        public String getGI() { return getProperty("gi"); }
        public String getDescription() { return getProperty("description"); }
        
        public void setAccession(String acc) { setProperty("accession", acc); }
        public void setGI(String gi) {setProperty("gi", gi); }
        public void setDescription(String desc) { setProperty("description", desc); }
    }
    
    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        GenbankGeneFinder finder1 = new GenbankGeneFinder("human AND histone");
        Vector itemsVT = finder1.search();
        finder1.cleanup();
        
        if (!itemsVT.isEmpty()) {
            for (int i=0; i<itemsVT.size(); i++) {
                EntrezItem item = (EntrezItem)itemsVT.get(i);
                System.out.println("GI:          " + item.getGI());
                System.out.println("ACCESSION:   " + item.getAccession());
                System.out.println("DESCRIPTION: " + item.getDescription());
		System.out.println();
            }
        } 
        else 
            System.out.println("No item returned");
    }
}







