/*
 * Loc2accParser.java
 *
 * Created on July 18, 2003, 4:11 PM
 */

package edu.harvard.med.hip.flex.infoimport.locuslinkdb;

import java.io.*;
import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.query.core.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 */
public class Loc2accParser {
    
    /** Creates a new instance of Loc2accParser */
    public Loc2accParser() {
    }
    
    public List parseFile(BufferedReader in, String line) throws Exception {
        List result = new ArrayList();
        SequenceRecord gene = null;
        int count = 0;
        
        System.out.println(line);
        gene = getGene(line);
        if(gene != null) {
            result.add(gene);
            count++;
        }
    
        while((line = in.readLine()) != null) {
            System.out.println(line);
            gene = getGene(line);
            if(gene != null) {
                result.add(gene);
                count++;
            }
            
            if(count == 200)
                return result;
        }
        
        return result;
    }
    
    public void insert(Connection conn, List records) throws Exception {
        String sql = "insert into genbankrecord"+
        " (genbankaccession, locusid, type)"+
        " values(?,?,?,)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        for(int i=0; i<records.size(); i++) {
            SequenceRecord gene = (SequenceRecord)records.get(i);
            String acc = gene.getGenbank();
            int locusid = gene.getLocusid();
            String type = gene.getType();
            
            System.out.println("Update: "+acc+", "+locusid);
            stmt.setString(1,  acc);
            stmt.setInt(2, locusid);
            stmt.setString(3,  type);
            DatabaseTransaction.executeUpdate(stmt);
        }
        
        DatabaseTransaction.closeStatement(stmt);
    }
    
    protected SequenceRecord getGene(String line) {
        StringTokenizer st = new StringTokenizer(line, "\t");
        int locusid = Integer.parseInt(st.nextToken());
        String acc = st.nextToken();
        String gistring = st.nextToken();
        String type = st.nextToken();
        String protein = st.nextToken();
        String species = st.nextToken();
       
        if(!"9606".equals(species)) 
            return null;
        
        SequenceRecord seq = new SequenceRecord(0, acc, type);
        seq.setLocusid(locusid);
        return seq;
    }
    
    public static void main(String args[]) {
        String file = "G:\\loc2acc.txt";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        BufferedReader in = null;
        
        Loc2accParser parser = new Loc2accParser();
        List records = null;
        
        try {
            in = new BufferedReader(new FileReader(file));
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            String line = null;
            
            while((line = in.readLine()) != null) {
                records = parser.parseFile(in, line);
                parser.insert(conn, records);
                DatabaseTransaction.commit(conn);
                //DatabaseTransaction.rollback(conn);
            }
            
            in.close();
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
}
