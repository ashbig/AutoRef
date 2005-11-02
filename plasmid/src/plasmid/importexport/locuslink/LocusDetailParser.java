/*
 * LocusDetailParser.java
 *
 * Created on April 26, 2005, 3:03 PM
 */

package plasmid.importexport.locuslink;

import java.io.*;
import java.util.*;
import java.sql.*;
import plasmid.database.*;
import plasmid.coreobject.*;

/**
 *
 * @author  DZuo
 */
public class LocusDetailParser {
    
    /** Creates a new instance of LocusDetailParser */
    public LocusDetailParser() {
    }
    
    public List parseFile(BufferedReader in, String line) throws Exception {
        List result = new ArrayList();
        Gene gene = null;
        List genbanks = new ArrayList();
        List genesymbols = new ArrayList();
        
        int count = 0;
        //System.out.println(line);
        String locusid = line.substring(line.indexOf(":")+1).trim();
        gene = new Gene(locusid);
        
        while((line = in.readLine()) != null) {
            //System.out.println(line);
            if(line.indexOf(">>") == 0) {
                if(gene != null) {
                    gene.setGenesymbol(genesymbols);
                    gene.setGenbank(genbanks);
                    result.add(gene);
                    genbanks = new ArrayList();
                    genesymbols = new ArrayList();
                    //System.out.println(gene.getLocusid());
                }
                count++;
                //System.out.println(count);
                if(count == 200)
                    return result;
            }
            
            if(line.indexOf("LOCUSID") == 0) {
                locusid = line.substring(line.indexOf(":")+1).trim();
                gene = new Gene(locusid);
            }
            if(line.indexOf("CURRENT_LOCUSID") == 0) {
                gene = null;
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
                gene.setOfficialgenesymbol(symbol);
            }
            if(line.indexOf("PREFERRED_SYMBOL") == 0) {
                String symbol = line.substring(line.indexOf(":")+1).trim();
                GeneSymbol gs = new GeneSymbol(symbol, "PREFERRED");
                genesymbols.add(gs);
                if(gene.getOfficialgenesymbol() == null)
                    gene.setOfficialgenesymbol(symbol);
            }
            if(line.indexOf("ALIAS_SYMBOL") == 0) {
                String symbol = line.substring(line.indexOf(":")+1).trim();
                GeneSymbol gs = new GeneSymbol(symbol, "ALIAS");
                genesymbols.add(gs);
            }
            if(line.indexOf("OFFICIAL_GENE_NAME") == 0) {
                String name = line.substring(line.indexOf(":")+1).trim();
                gene.setGenename(name);
            }
            if(line.indexOf("PREFERRED_GENE_NAME") == 0) {
                String name = line.substring(line.indexOf(":")+1).trim();
                if(gene.getGenename() == null)
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
        
        String gi = st.nextToken();
        if("na".equals(gi)) {
            return null;
        }
        
        
        SequenceRecord gr = new SequenceRecord(gi, acc, type);
        return gr;
    }
    
    private void addGenbank(List genbanks, SequenceRecord gr) {
        for (int i=0; i<genbanks.size(); i++) {
            SequenceRecord genbank = (SequenceRecord)genbanks.get(i);
            if(genbank.getGi().equals(gr.getGi())) {
                return;
            }
        }
        
        genbanks.add(gr);
    }
    
    public void insert(Connection conn, List records) throws Exception {
        String sql = "insert into generecord_tmp(geneid,isconfirmed,"+
        " organism,status,genename,unigeneid,genesymbol)"+
        " values(?,?,?,?,?,?,?)";
        String sql2 = "insert into genesymbol_tmp(symbolid,symbol,type,geneid)"+
        " values(symbolid.nextval,?,?,?)";
        String sql3 = "insert into sequencerecord_tmp(accession, gi, type, geneid)"+
        " values(?,?,?,?)";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        PreparedStatement stmt2 = conn.prepareStatement(sql2);
        PreparedStatement stmt3 = conn.prepareStatement(sql3);
        
        for( int i=0; i<records.size(); i++) {
            Gene gene = (Gene)records.get(i);
            
            /**
            if(!"Homo sapiens".equals(gene.getOrganism())) {
                continue;
            }
            */
            
            stmt.setString(1, gene.getLocusid());
            stmt.setString(2, gene.getIsconfirmed());
            stmt.setString(3, gene.getOrganism());
            stmt.setString(4, gene.getStatus());
            stmt.setString(5, gene.getGenename());
            stmt.setString(6, gene.getUnigeneid());
            stmt.setString(7, gene.getOfficialgenesymbol());
            System.out.println("update: "+gene.getLocusid());
            DatabaseTransaction.executeUpdate(stmt);
            
            List genesymbols = gene.getGenesymbol();
            for(int j=0; j<genesymbols.size(); j++) {
                GeneSymbol gs = (GeneSymbol)genesymbols.get(j);
                stmt2.setString(1, gs.getSymbol());
                stmt2.setString(2, gs.getType());
                stmt2.setString(3,  gene.getLocusid());
                //System.out.println("\tupdate: "+gs.getSymbol());
                DatabaseTransaction.executeUpdate(stmt2);
            }
            
            List genbanks = gene.getGenbank();
            for(int n=0; n<genbanks.size(); n++) {
                SequenceRecord gr = (SequenceRecord)genbanks.get(n);
                stmt3.setString(1, gr.getGenbank());
                stmt3.setString(2, gr.getGi());
                stmt3.setString(3, gr.getType());
                stmt3.setString(4, gene.getLocusid());
                //System.out.println("\tupdate: "+gr.getGenbank());
                DatabaseTransaction.executeUpdate(stmt3);
            }
        }
        
        DatabaseTransaction.closeStatement(stmt);
        DatabaseTransaction.closeStatement(stmt2);
        DatabaseTransaction.closeStatement(stmt3);
    }
    
    public void cleanTable(String table, Connection conn) throws Exception {
        String sql = "truncate table "+table;
        DatabaseTransaction.executeUpdate(sql, conn);
    }
    
    public void transferTable(String from, String to, Connection conn) throws Exception{
        String sql = "insert into "+to+
        " select * from "+from;
        DatabaseTransaction.executeUpdate(sql,  conn);
    }
    
    public void deleteTable(String table, Connection conn) throws Exception {
        String sql = "delete from "+table;
        DatabaseTransaction.executeUpdate(sql, conn);
    }
    
    public void disableConstraints(Connection conn) throws Exception{
            String sqlUpdate = "alter table GENENAME disable constraint GEE_GENE_FK";
            DatabaseTransaction.executeUpdate(sqlUpdate, conn);
            sqlUpdate = "alter table GENESYMBOL disable constraint GSB_GENE_FK";
            DatabaseTransaction.executeUpdate(sqlUpdate, conn);
            sqlUpdate = "alter table SEQUENCERECORD disable constraint SEQRECORD_GENE_FK";
            DatabaseTransaction.executeUpdate(sqlUpdate, conn);
    }
    
    public void enableConstraints(Connection conn) throws Exception{
            String sqlUpdate = "alter table GENENAME enable constraint GEE_GENE_FK";
            DatabaseTransaction.executeUpdate(sqlUpdate, conn);
            sqlUpdate = "alter table GENESYMBOL enable constraint GSB_GENE_FK";
            DatabaseTransaction.executeUpdate(sqlUpdate, conn);
            sqlUpdate = "alter table SEQUENCERECORD enable constraint SEQRECORD_GENE_FK";
            DatabaseTransaction.executeUpdate(sqlUpdate, conn);
    }
    
    public static void main(String args[]) {
        String file = "G:\\tmp\\LL3_050415";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        BufferedReader in = null;
        
        LocusDetailParser parser = new LocusDetailParser();
        List records = null;
        
        try {
            in = new BufferedReader(new FileReader(file));
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            /**
            parser.cleanTable("generecord_tmp", conn);
            parser.cleanTable("sequencerecord_tmp", conn);
            parser.cleanTable("genesymbol_tmp", conn);
            String line = null;
            in.readLine();
            
            while((line = in.readLine()) != null) {
                records = parser.parseFile(in, line);
                parser.insert(conn, records);
                DatabaseTransaction.commit(conn);
                //DatabaseTransaction.rollback(conn);
            }
            
            in.close();
            */
            parser.disableConstraints(conn);
            System.out.println("Truncate table GENESYMBOL");
            parser.cleanTable("genesymbol", conn);
            System.out.println("Truncate table sequencerecord");
            parser.cleanTable("sequencerecord", conn);
            parser.cleanTable("gene", conn);
            System.out.println("Truncate table gene");
            parser.deleteTable("gene", conn);
            
            System.out.println("Transfer table gene");
            parser.transferTable("generecord_tmp", "gene", conn);
            System.out.println("Transfer table sequencerecord");
            parser.transferTable("sequencerecord_tmp", "sequencerecord", conn);
            System.out.println("Transfer table genesymbol");
            parser.transferTable("genesymbol_tmp", "genesymbol", conn);
            DatabaseTransaction.commit(conn);
            
            System.out.println("Enable constraints");
            parser.enableConstraints(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
}
