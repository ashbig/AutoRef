/*
 * ChipGeneGeneAnalysis.java
 *
 * Created on June 3, 2002, 3:06 PM
 */

package edu.harvard.med.hip.metagene.core;

import java.util.*;
import java.sql.*;
import java.io.*;
import edu.harvard.med.hip.metagene.database.*;

/**
 *
 * @author  hweng
 */
public class ChipGeneGeneAnalysis extends ChipGeneDiseaseAnalysis {
    
    /** Creates a new instance of ChipGeneGeneAnalysis */
    public ChipGeneGeneAnalysis() {
        super();
    }
    
    /** Get directly associated genes for this gene and put them in hashmap. 
     *  Also generate the vector of source_for_indirect_genes.
     */
    public void hashDirectGenes(int geneindex_id, int stat_id, int input_type) {
       
        DBManager manager = new DBManager();
        Connection con = manager.connect();

        if (con == null) {
            System.out.println("Cannot connect to the database.");
            return;
        }
        
        String sql_124a = "select gl.gene_index_id, gl.symbol_value, gl.locus_id, sa.statistic_score " +
		        " from gene_list gl, gene_index gi, gene_and_gene_association gga, " +
                        " association_data ad, statistic_analysis sa " +
                        " where gl.gene_index_id=gi.gene_index_id " +
		        " and gi.gene_index_id=gga.gene1_index_id "+       
		        " and gga.association_id = ad.association_id"+
		        " and ad.data_id = sa.data_id"+
		        " and sa.statistic_id = "+stat_id+
		        " and gga.gene2_index_id="+geneindex_id;
        
        String sql_124b = "select gl.gene_index_id, gl.symbol_value, gl.locus_id, sa.statistic_score " +
		        " from gene_list gl, gene_index gi, gene_and_gene_association gga, " +
                        " association_data ad, statistic_analysis sa " +
                        " where gl.gene_index_id=gi.gene_index_id " +
		        " and gi.gene_index_id=gga.gene2_index_id "+       
		        " and gga.association_id = ad.association_id"+
		        " and ad.data_id = sa.data_id"+
		        " and sa.statistic_id = "+stat_id+
		        " and gga.gene1_index_id="+geneindex_id;
/*
        String sql_3a = "SELECT gl.gene_index_id, gl.symbol_value, gl.locus_id, sa.statistic_score " +
                      " FROM gene_list gl, family_mapping fm, parent_list pl, gene_index gi, " +
                      " gene_and_gene_association gga, association_data ad, statistic_analysis sa " +                   
                      " WHERE gl.hip_gene_id=fm.hip_gene_id and fm.parent_id=pl.parent_id " +
                      " and pl.parent_value=gi.gene_index and gi.gene_index_id=gga.gene1_index_id " +
                      " and gga.association_id = ad.association_id and ad.data_id = sa.data_id " +
                      " and sa.statistic_id = " + stat_id + 
                      " and gga.gene2_index_id=" + gene_index_id;
        
        String sql_3b = "SELECT gl.gene_index_id, gl.symbol_value, gl.locus_id, sa.statistic_score " +
                      " FROM gene_list gl, family_mapping fm, parent_list pl, gene_index gi, " +
                      " gene_and_gene_association gga, association_data ad, statistic_analysis sa " +                   
                      " WHERE gl.hip_gene_id=fm.hip_gene_id and fm.parent_id=pl.parent_id " +
                      " and pl.parent_value=gi.gene_index and gi.gene_index_id=gga.gene2_index_id " +
                      " and gga.association_id = ad.association_id and ad.data_id = sa.data_id " +
                      " and sa.statistic_id = " + stat_id + 
                      " and gga.gene1_index_id=" + gene_index_id;        
   */
        
        Statement stmt = null;
        ResultSet rs = null;             

        try {
            stmt = con.createStatement();
            
            // put all non-family genes into hashmap
            rs = stmt.executeQuery(sql_124a);
            while(rs.next()) {
                int gene_index_id = rs.getInt(1);
                String symbol_value = rs.getString(2);
                int locus_id = rs.getInt(3);
                double score = rs.getDouble(4);
                source_for_indirect_genes.add(new Integer(gene_index_id));     
                
                ChipGene gene = new ChipGene(symbol_value, locus_id, score);   
                if(input_type == GENE_SYMBOL_INPUT)
                    direct_gene_hashmap.put(symbol_value, gene);             
                if(input_type == LOCUS_ID_INPUT)
                    direct_gene_hashmap.put(new Integer(locus_id), gene);
            }
            rs.close(); 
            
            rs = stmt.executeQuery(sql_124b);
            while(rs.next()){
                int gene_index_id = rs.getInt(1);
                String symbol_value = rs.getString(2);
                int locus_id = rs.getInt(3);
                double score = rs.getDouble(4);
                source_for_indirect_genes.add(new Integer(gene_index_id));   
                
                ChipGene gene = new ChipGene(symbol_value, locus_id, score);
                if(input_type == GENE_SYMBOL_INPUT)
                    direct_gene_hashmap.put(symbol_value, gene);             
                if(input_type == LOCUS_ID_INPUT)
                    direct_gene_hashmap.put(new Integer(locus_id), gene);
            }
            rs.close();

            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);                                     
        } finally{
            manager.disconnect(con);
        }       
        
   System.out.println("************ hash direct genes done ************");
   System.out.println(" we have the number of total pairs    " + direct_gene_hashmap.size());  
   
    }
    
    
    public String getGeneSymbol(int gene_index_id){
 
        DBManager manager = new DBManager();
        Connection con = manager.connect();
        
        if (con == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        String symbol = "";
        String sql = "select symbol_value from gene_list where gene_index_id=" + gene_index_id;

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);            
            if(rs.next()) {
                symbol = rs.getString(1);
            }            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally{
            manager.disconnect(con);       
        }
        return symbol;
    }
    
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////
    //////// main method is used for test /////////////////////////
    
    public static void main(String[] args){

        //String text = "";

        String text =
        "STS\n TERF1\n RAF1\n PYY\n MIR\n CCS\n DMT1\n NPY\n PPY\n GCG\n GAL\n" +
        "13CDNA73\n 6H9A\n AADAC\n AARS\n AASDHPPT\n ABCA12\n ABCA2\n ABCA4\n ABCA5\n" + 
        "ABCA6\n ABCA8\n ABCB1\n HHHH\n";             
        
        /*
        try{
         
        BufferedReader in = new BufferedReader(new FileReader("c:\\test.txt"));       
        
        String s;
        
        while((s=in.readLine()) != null){
            text = text + s + " ";    
        }
        //System.out.println(text); 
        
        }catch(Exception e){
            System.out.println(e);
        }
        */
        

         
//////////////////////////////////
                                                                                        
        ChipGeneGeneAnalysis ana = new ChipGeneGeneAnalysis();  
        ana.hashDirectGenes(531, 1, ana.GENE_SYMBOL_INPUT);  
        ana.hashIndirectGenes(text, ana.source_for_indirect_genes, ana.GENE_SYMBOL_INPUT);       
        ana.analyzeInputChipGenes(text, ana.GENE_SYMBOL_INPUT);
        
        
        TreeSet direct = ana.getDirect_gene_tree();        
        System.out.println("direct tree:     " + direct.size());
        TreeSet indirect = ana.getIndirect_gene_tree();
        System.out.println("indirect tree:     " + indirect.size());       
        TreeSet new_genes = ana.getNew_gene_tree();
        
       
        
        Iterator it = direct.iterator();
        System.out.println("direct genes -------------------------------------");
        while (it.hasNext()){
            ChipGene g = (ChipGene)(it.next());
            System.out.println( g.getGene_symbol() + "              " + g.getScore() );
        }
        
        Iterator itt = indirect.iterator();
        System.out.println("indirect genes -------------------------------------");
        while (itt.hasNext()){
            ChipGene g = (ChipGene)(itt.next());
            System.out.println( g.getGene_symbol() + "              " + g.getScore() );
        }
        
        Iterator ittt = new_genes.iterator();
        System.out.println("new genes -------------------------------------");
        while (ittt.hasNext()){
            ChipGene g = (ChipGene)(ittt.next());
            System.out.println( g.getGene_symbol() + "              " + g.getScore() );
        }        
        

        ana.end2 = System.currentTimeMillis();
        
        
        System.out.println("-----------------done--------------");
        System.out.println("time cost 2: " + (ana.end2 - ana.start)/1000 + " seconds");
    }
         
 /***********************************************************************************/     
        
        
        
        
        
        
        
        
        
}
