/*
 * GIRecordPopulator.java
 *
 * Created on July 22, 2003, 3:49 PM
 */

package edu.harvard.med.hip.flex.special_projects;

import java.io.*;
import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.query.core.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 */
public class GIRecordPopulator {
    public static final String filepath = "/kotel/data/FLEXRepository/GI_FASTA_DB/";
    //public static final String filepath = "G:\\";
    
    /** Creates a new instance of GIRecordPopulator */
    public GIRecordPopulator() {
    }
    
    public String writeFile(int gi, StringBuffer sf) throws Exception {
        String file = filepath + gi;
        PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        pr.println(sf.toString());
        pr.close();
        
        return file;
    }
    
    public void insertDB(List records, Connection conn) throws Exception {
        String sql = "insert into girecord (gi, sequencefile)"+
        " values(?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        for(int i=0; i<records.size(); i++) {
            GiRecord gr = (GiRecord)records.get(i);
            int gi = gr.getGi();
            String seqfile = gr.getSequenceFile();
            stmt.setInt(1, gi);
            stmt.setString(2, seqfile);
            DatabaseTransaction.executeUpdate(stmt);
            System.out.println("update: "+gi);
        }
        
        DatabaseTransaction.closeStatement(stmt);
    }
    
    public static void main(String args []) {
        String file = "G:\\locus_gi_fasta_1.txt";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        BufferedReader in = null;
        
        GIRecordPopulator parser = new GIRecordPopulator();
        List records = new ArrayList();
        
        try {
            in = new BufferedReader(new FileReader(file));
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            String line = null;
            StringBuffer sb = new StringBuffer();
            int gi = 0;
            String filename = null;
            
            int count = 0;
            while((line = in.readLine()) != null) {
                System.out.println(line);
                if(line.trim().equals("")) {
                    filename = parser.writeFile(gi, sb);
                    sb = new StringBuffer();
                    GiRecord gr = new GiRecord(gi, filename);
                    records.add(gr);
                    count++;
                    
                    if(count == 200) {
                        parser.insertDB(records, conn);
                        DatabaseTransaction.commit(conn);
                        records = new ArrayList();
                        count = 0;
                    }
                    
                    continue;
                }
                
                sb.append(line);
                
                if(line.indexOf(">") == 0) {
                    StringTokenizer st = new StringTokenizer(line);
                    String skip = st.nextToken("|");
                    gi = Integer.parseInt(st.nextToken("|"));
                }
            }
            
            filename = parser.writeFile(gi, sb);
            GiRecord gr = new GiRecord(gi, filename);
            records.add(gr);
            count++;
            parser.insertDB(records, conn);
            DatabaseTransaction.commit(conn);
            
            in.close();
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
}
