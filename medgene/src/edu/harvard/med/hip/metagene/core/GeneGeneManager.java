/*
 * GeneGeneManager.java
 *
 * Created on April 3, 2002, 10:47 AM
 */

package edu.harvard.med.hip.metagene.core;

import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.metagene.database.*;

/**
 *
 * @author  hweng
 */
public class GeneGeneManager extends DiseaseGeneManager {
    
    /** Creates a new instance of GeneGeneManager */
    public GeneGeneManager() {
        super();
    }
                   
    public Vector getGeneGeneAssociationsByGeneIndexID(int gene_index_id, int stat_id, int number) {
        DBManager manager = new DBManager();
        Connection conn = manager.connect();
     
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        

        Statement stmt = null;
        ResultSet rs = null;
        Gene target_gene = null;
        Gene source_gene = super.queryGeneByIndex(conn, 
                                            getGeneIndexByGeneIndexID(conn, gene_index_id));   
        
        String sql = "select gga.gene1_index_id, gga.gene2_index_id, " +
        "ad.doublehit, sa.statistic_score, st.statistic_type " +
        "from gene_and_gene_association gga, association_data ad, " +
        "statistic_analysis sa, statistic_type st " +
        "where gga.association_id = ad.association_id " +
        "and ad.data_id = sa.data_id " +
        "and sa.statistic_id = st.statistic_id " +
        "and st.statistic_id = " + stat_id +
        " and ( gga.gene1_index_id = " + gene_index_id +
        " or gga.gene2_index_id = " + gene_index_id + " )";
              
        if(stat_id == Statistics.FISCHERID)
            sql = sql + " order by sa.statistic_score";
        else
            sql = sql + " order by sa.statistic_score desc";
        
        Vector gene_gene_associations = new Vector();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
                                   
            while(rs.next()) {
                if(gene_gene_associations.size() == number)                
                    break;

                int gene1_index_id = rs.getInt(1);
                int gene2_index_id = rs.getInt(2);
                int double_hit = rs.getInt(3);
                double stat_score = rs.getDouble(4);
                String stat_type = rs.getString(5);
                
                Statistics stat = new Statistics(stat_id, stat_type);
                StatAnalysis stat_analysis = new StatAnalysis(stat, stat_score);
                AssociationData asso_data = new AssociationData(-1, -1, double_hit, -1, null);
                
                if(gene1_index_id == gene_index_id)
                    target_gene = super.queryGeneByIndex(conn, 
                                             getGeneIndexByGeneIndexID(conn, gene2_index_id));
                
                if(gene2_index_id == gene_index_id)
                    target_gene = super.queryGeneByIndex(conn, 
                                             getGeneIndexByGeneIndexID(conn, gene1_index_id));
                               
                GeneGeneAssociation g_g_association = 
                    new GeneGeneAssociation(source_gene, target_gene, stat_analysis, asso_data);
                
                
                gene_gene_associations.addElement(g_g_association);
            }
             rs.close();
             stmt.close();
                          
        }catch (SQLException e){
            System.out.println(e);
        }
        
        manager.disconnect(conn);
        
        return gene_gene_associations;
    }
           
   
    
    public GeneIndex getGeneIndexByGeneIndexID(Connection conn, int gene_index_id){

        Statement stmt = null;
        ResultSet rs = null;
        GeneIndex geneIndex = null;
        
        
        String sql = "select gi.gene_index, it.index_type, gi.date_added " + 
        "from index_type it, gene_index gi " + 
        "where it.index_type_id = gi.index_type_id and gi.gene_index_id = " + 
        gene_index_id;
        
        try{
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            rs.next();
            String gene_index = rs.getString(1);           
            String index_type = rs.getString(2);
            String date_added = rs.getString(3);
            geneIndex = new GeneIndex(gene_index_id, gene_index, index_type, date_added);        

            rs.close();
            stmt.close();
        }catch (SQLException ex){
            System.out.println(ex);
        }
        return geneIndex;
    }
 
    
     public String getSourceGeneName(Vector g_g_associations){
         GeneGeneAssociation g = (GeneGeneAssociation)(g_g_associations.firstElement());
         return g.getSource_gene().getSymbol();
     }
         
         
         
}
