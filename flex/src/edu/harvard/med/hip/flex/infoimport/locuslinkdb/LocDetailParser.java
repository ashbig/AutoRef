/*
 * LocDetailParser.java
 *
 * Created on July 18, 2003, 11:03 AM
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
public class LocDetailParser {
    
    /** Creates a new instance of LocDetailParser */
    public LocDetailParser() {
    }
    
    public List parseFile(BufferedReader in, String line) throws Exception {
        List result = new ArrayList();
        GeneRecord gene = null;
        List genbanks = new ArrayList();
        List genesymbols = new ArrayList();
        
        int count = 0;
        System.out.println(line);
        int locusid = Integer.parseInt(line.substring(line.indexOf(":")+1).trim());
        gene = new GeneRecord(locusid);
        
        while((line = in.readLine()) != null) {
            System.out.println(line);
            if(line.indexOf(">>") == 0) {
                if(gene != null) {
                    gene.setGenesymbol(genesymbols);
                    gene.setGenbank(genbanks);
                    result.add(gene);
                    genbanks = new ArrayList();
                    genesymbols = new ArrayList();
                }                
                count++;
                System.out.println(count);
                if(count == 200)
                    return result;
            }
            
            if(line.indexOf("LOCUSID") == 0) {
                locusid = Integer.parseInt(line.substring(line.indexOf(":")+1).trim());
                gene = new GeneRecord(locusid);
            }
            if(line.indexOf("CURRENT_LOCUSID") == 0) {
                in.readLine();
                count--;
                continue;
            }
            if(line.indexOf("LOCUS_CONFIRMED") == 0) {
                String locusconfirmed = line.substring(line.indexOf(":")+1).trim().toLowerCase();
                gene.setIsconfirmed(locusconfirmed);
            }
            if(line.indexOf("ORGANISM") == 0) {
                String organism = line.substring(line.indexOf(":")+1).trim();
                gene.setOrganism(organism);
            }
            if(line.indexOf("STATUS") == 0) {
                String status = line.substring(line.indexOf(":")+1).trim();
                gene.setStatus(status);
            }
            if(line.indexOf("NM") == 0) {
                SequenceRecord gr = getGenbank(line, "NM");
                if(gr != null)
                    addGenbank(genbanks, gr);
            }
            if(line.indexOf("XM") == 0) {
                SequenceRecord gr = getGenbank(line, "XM");
                if(gr != null)
                    addGenbank(genbanks, gr);
            }
            if(line.indexOf("ACCNUM") == 0) {
                String nextline = in.readLine();
                String type = nextline.substring(nextline.indexOf(":")+1).trim();
                SequenceRecord gr = getGenbank(line, type);
                if(gr != null)
                    addGenbank(genbanks, gr);
            }
            if(line.indexOf("OFFICIAL_SYMBOL") == 0) {
                String symbol = line.substring(line.indexOf(":")+1).trim();
                GeneSymbol gs = new GeneSymbol(symbol, "OFFICIAL");
                genesymbols.add(gs);
            }
            if(line.indexOf("PREFERRED_SYMBOL") == 0) {
                String symbol = line.substring(line.indexOf(":")+1).trim();
                GeneSymbol gs = new GeneSymbol(symbol, "PREFERRED");
                genesymbols.add(gs);
            }
            if(line.indexOf("ALIAS_SYMBOL") == 0) {
                String symbol = line.substring(line.indexOf(":")+1).trim();
                GeneSymbol gs = new GeneSymbol(symbol, "ALIAS");
                genesymbols.add(gs);
            }
            if(line.indexOf("PREFERRED_GENE_NAME") == 0 || line.indexOf("OFFICIAL_GENE_NAME") == 0) {
                String name = line.substring(line.indexOf(":")+1).trim();
                gene.setGenename(name);
            }
            if(line.indexOf("UNIGENE") == 0) {
                String unigene = line.substring(line.indexOf(":")+1).trim();
                gene.setUnigeneid(unigene);
            }
        }
        
        return result;
    }
    
    private SequenceRecord getGenbank(String line, String type) {
        String tmp = line.substring(line.indexOf(":")+1).trim();
        StringTokenizer st = new StringTokenizer(tmp, "|");
        
        String acc = st.nextToken();
        if("none".equals(acc)) {
            return null;
        }
        
        String giString = st.nextToken();
        int gi = 0;
        if(!"na".equals(giString)) {
            gi = Integer.parseInt(giString);
        }
        
        SequenceRecord gr = new SequenceRecord(gi, acc, type);
        return gr;
    }
    
    private void addGenbank(List genbanks, SequenceRecord gr) {
        for (int i=0; i<genbanks.size(); i++) {
            SequenceRecord genbank = (SequenceRecord)genbanks.get(i);
            if(genbank.getGi() == gr.getGi()) {
                return;
            }
        }
        
        genbanks.add(gr);
    }
    
    public void insert(Connection conn, List records) throws Exception {
        String sql = "insert into generecord(locusid,isconfirmed,"+
        " organism,status,genename,unigeneid)"+
        " values(?,?,?,?,?,?)";
        String sql2 = "insert into genesymbol(symbolid,symbol,type,locusid)"+
        " values(symbolid.nextval,?,?,?)";
        String sql3 = "insert into sequencerecord(accession, gi, type, locusid)"+
        " values(?,?,?,?)";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        PreparedStatement stmt2 = conn.prepareStatement(sql2);
        PreparedStatement stmt3 = conn.prepareStatement(sql3);
        
        for( int i=0; i<records.size(); i++) {
            GeneRecord gene = (GeneRecord)records.get(i);
            
            if(!"Homo sapiens".equals(gene.getOrganism())) {
                continue;
            }
            
            stmt.setInt(1, gene.getLocusid());
            stmt.setString(2, gene.getIsconfirmed());
            stmt.setString(3, gene.getOrganism());
            stmt.setString(4, gene.getStatus());
            stmt.setString(5, gene.getGenename());
            stmt.setString(6, gene.getUnigeneid());
            DatabaseTransaction.executeUpdate(stmt);
            System.out.println("update: "+gene.getLocusid());
            
            List genesymbols = gene.getGenesymbol();
            for(int j=0; j<genesymbols.size(); j++) {
                GeneSymbol gs = (GeneSymbol)genesymbols.get(j);
                stmt2.setString(1, gs.getSymbol());
                stmt2.setString(2, gs.getType());
                stmt2.setInt(3,  gene.getLocusid());
                DatabaseTransaction.executeUpdate(stmt2);
            }
            
            List genbanks = gene.getGenbank();
            for(int n=0; n<genbanks.size(); n++) {
                SequenceRecord gr = (SequenceRecord)genbanks.get(n);
                stmt3.setString(1, gr.getGenbank());
                stmt3.setInt(2, gr.getGi());
                stmt3.setString(3, gr.getType());
                stmt3.setInt(4, gene.getLocusid());
                DatabaseTransaction.executeUpdate(stmt3);
            }
        }
        
        DatabaseTransaction.closeStatement(stmt);
        DatabaseTransaction.closeStatement(stmt2);
        DatabaseTransaction.closeStatement(stmt3);
    }
    
    public static void main(String args[]) {
        String file = "G:\\locus.txt";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        BufferedReader in = null;
        
        LocDetailParser parser = new LocDetailParser();
        List records = null;
        
        try {
            in = new BufferedReader(new FileReader(file));
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            String line = null;
            in.readLine();
            
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
