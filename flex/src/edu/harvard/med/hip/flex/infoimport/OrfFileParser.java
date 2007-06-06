/*
 * OrfFileParser.java
 *
 * Created on January 23, 2007, 10:50 AM
 */

package edu.harvard.med.hip.flex.infoimport;

import java.io.*;
import java.util.*;

/**
 *
 * @author  DZuo
 */
public class OrfFileParser {
    private List genbanks;
    private List orfs;
    
    /** Creates a new instance of OrfFileParser */
    public OrfFileParser() {
        genbanks = new ArrayList();
        orfs = new ArrayList();
    }
    
    public List getGenbanks() {return genbanks;}
    public List getOrfs() {return orfs;}
    
    public void parseOrfFile(String fileName) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        String line = null;
        
        String header = in.readLine();
        while((line = in.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, "\t");
            String imageid = st.nextToken();
            String collectionName = st.nextToken();
            String plate = st.nextToken();
            String row = st.nextToken();
            String col = st.nextToken();
            String library = st.nextToken();
            String cloneAcc = st.nextToken();
            String parentImageid = st.nextToken();
            String refAcc = st.nextToken();
            String symbol = st.nextToken();
            String species = st.nextToken();
            String vector = st.nextToken();
            String linker5p = st.nextToken();
            String linker3p = st.nextToken();
            String insertDigest = st.nextToken();
            String stop = st.nextToken();
            
            if(parentImageid.equals(OrfInfo.NULLVALUE))
                parentImageid = null;
            if(symbol.equals(OrfInfo.NULLVALUE))
                symbol = null;
            
            OrfInfo info = new OrfInfo(imageid,collectionName,plate, row, col, library, cloneAcc,
            parentImageid, refAcc, symbol, species, vector, linker5p, linker3p, insertDigest, stop);
            orfs.add(info);
            
            genbanks.add(refAcc);
        }
        
        in.close();
    }
}
