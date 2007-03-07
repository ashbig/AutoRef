/*
 * GeneInfoImporter.java
 *
 * Created on February 20, 2007, 1:20 PM
 */

package plasmid.importexport.entrezgene;

import plasmid.database.*;

import java.sql.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author  DZuo
 */
public class GeneInfoImporter extends EntrezGeneImporter {
    
    /** Creates a new instance of GeneInfoImporter */
    public GeneInfoImporter() {
    }
    
    public void importInfoToDb(List genes, Connection conn) throws Exception {
        EntrezGeneParser parser = new EntrezGeneParser();
        parser.parseGeneInfo(genes);
        List geneInfo = parser.getGenes();
        List symbols = parser.getSymbols();
        List dbs = parser.getDbs();
        insertGenes(geneInfo, conn);
        insertSymbols(symbols, conn);
        insertDbXrefs(dbs, conn);
    }
    
    private void insertGenes(List genes, Connection conn) throws Exception {
        if(genes == null)
            throw new Exception("Gene list is null");
        
        String sql = "insert into gene_tmp(geneid,taxid,symbol,description,type,locustag,chromosome,maplocation)"+
        " values(?,?,?,?,?,?,?,?)";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        for(int i=0; i<genes.size(); i++) {
            GeneInfo gene = (GeneInfo)genes.get(i);
            stmt.setInt(1, gene.getGeneid());
            stmt.setInt(2, gene.getTaxid());
            stmt.setString(3, gene.getSymbol());
            stmt.setString(4, gene.getDescription());
            stmt.setString(5, gene.getType());
            stmt.setString(6, gene.getLocustag());
            stmt.setString(7, gene.getChromosome());
            stmt.setString(8, gene.getMaplocation());
            System.out.println("Insert gene: "+gene.getGeneid());
            DatabaseTransaction.executeUpdate(stmt);
        }
        DatabaseTransaction.closeStatement(stmt);
    }
    
    private void insertSymbols(List symbols, Connection conn) throws Exception {
        if(symbols == null)
            throw new Exception("Gene symbol list is null");
        
        String sql = "insert into genesymbol_tmp (symbol, geneid)"+
        " values(?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        for(int i=0; i<symbols.size(); i++) {
            GeneSymbol symbol = (GeneSymbol)symbols.get(i);
            stmt.setString(1, symbol.getSymbol());
            stmt.setInt(2, symbol.getGeneid());
            System.out.println("Insert symbol "+symbol.getSymbol()+" for gene: "+symbol.getGeneid());
            DatabaseTransaction.executeUpdate(stmt);
        }
        DatabaseTransaction.closeStatement(stmt);
    }
    
    private void insertDbXrefs(List dbs, Connection conn) throws Exception {
        if(dbs == null)
            throw new Exception("DbXrefs list is null");
        
        String sql = "insert into dbxrefs_tmp (geneid, dbname, dbvalue)"+
        " values(?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        for(int i=0; i<dbs.size(); i++) {
            DbXrefs db = (DbXrefs)dbs.get(i);
            stmt.setInt(1, db.getGeneid());
            stmt.setString(2, db.getDb());
            stmt.setString(3, db.getValue());
            System.out.println("Insert db "+db.getDb()+" for gene: "+db.getGeneid());
            DatabaseTransaction.executeUpdate(stmt);
        }
        DatabaseTransaction.closeStatement(stmt);
    }
}
