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
        
    /** Get gene-gene association by gene_index_id
     *  @param gene_index_id  The gene Index ID
     *  @param statid       Statistics ID 
     *  @param number       How many genes the user wants to obtain?
     *  @return             The vector of all associations betweeen this gene and all related genes
     */               
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
            ///////////////////////////////////////

            String sql1 = "select hip_gene_id, symbol_value, quality_id_symbol,"+
                  " formal_name_value, quality_id_name, date_added, locus_id"+
                  " from gene_list where gene_index_id=?";
            PreparedStatement pstmt_symbol = conn.prepareStatement(sql1);
            String sql2 = "select p.parent_id, p.parent_value, p.date_added"+
                  " from parent_list p"+
                  " where p.parent_value=?";
            PreparedStatement pstmt_family = conn.prepareStatement(sql2);
            String sql3 = "select gene_nick_name_value from gene_nicknames"+
                  " where hip_gene_id=?";
            PreparedStatement pstmt_nickname = conn.prepareStatement(sql3);
            String sql4 = "select t.id_type, g.id_value, g.extra_information, g.gi_order"+
                  " from id_type t, gene_information g"+
                  " where t.type_id=g.type_id"+
                  " and g.hip_gene_id=?";
            PreparedStatement pstmt_geneinfo = conn.prepareStatement(sql4);
            String sql5 = "select l.symbol_value, l.formal_name_value "+
                  " from gene_list l, family_mapping m"+
                  " where l.hip_gene_id = m.hip_gene_id"+
                  " and m.parent_id = ?";
            PreparedStatement pstmt_query_children = conn.prepareStatement(sql5);
            
            Gene source_gene = super.queryGeneByIndex(conn, getGeneIndexByGeneIndexID(conn, gene_index_id),
                           pstmt_symbol, pstmt_family, pstmt_nickname, pstmt_geneinfo, pstmt_query_children);
            //Gene source_gene = super.queryGeneByIndex(conn, getGeneIndexByGeneIndexID(conn, gene_index_id));            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
                                   
            while(rs.next()) {
                if(gene_gene_associations.size() == number)                
                    break;

                int gene1_index_id = rs.getInt(1);
                int gene2_index_id = rs.getInt(2);
                int double_hit = rs.getInt(3);
                double stat_score = round_4(rs.getDouble(4));
                String stat_type = rs.getString(5);
                
                Statistics stat = new Statistics(stat_id, stat_type);
                StatAnalysis stat_analysis = new StatAnalysis(stat, stat_score);
                AssociationData asso_data = new AssociationData(-1, -1, double_hit, -1, null);
                
                if(gene1_index_id == gene_index_id)
                    target_gene = super.queryGeneByIndex(conn, getGeneIndexByGeneIndexID(conn, gene2_index_id),
                               pstmt_symbol, pstmt_family, pstmt_nickname, pstmt_geneinfo, pstmt_query_children);
                    //target_gene = super.queryGeneByIndex(conn, getGeneIndexByGeneIndexID(conn, gene2_index_id));
                if(gene2_index_id == gene_index_id)
                    target_gene = super.queryGeneByIndex(conn, getGeneIndexByGeneIndexID(conn, gene1_index_id),
                               pstmt_symbol, pstmt_family, pstmt_nickname, pstmt_geneinfo, pstmt_query_children);
                    //target_gene = super.queryGeneByIndex(conn, getGeneIndexByGeneIndexID(conn, gene1_index_id));
                GeneGeneAssociation g_g_association = 
                    new GeneGeneAssociation(source_gene, target_gene, stat_analysis, asso_data);
                                
                gene_gene_associations.addElement(g_g_association);
            }
            rs.close();
            stmt.close();
            pstmt_symbol.close(); 
            pstmt_family.close();
            pstmt_nickname.close();
            pstmt_geneinfo.close();
            pstmt_query_children.close();
                          
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
 
    
     public String getSourceGeneIndex(Vector g_g_associations){
         GeneGeneAssociation g = (GeneGeneAssociation)(g_g_associations.firstElement());
         String geneName = g.getSource_gene().getSymbol();
         if(geneName != null)
             return geneName;
         else
            return (g.getSource_gene().getName());
     }
         
     public Vector getMedlineRecords_gg(String source_gene_index, String target_gene_index){
        DBManager dbm = new DBManager();
        Connection con = dbm.connect();
        if(con == null){
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector pubmed_ids = new Vector();
        
        String sql = "select pubmedid from medline_records_gene_assoc " +
                     "where (gene_index1 = ? and gene_index2 = ?) or " +
                     "(gene_index2 = ? and gene_index1 = ?) " +
                     "order by pubmedid desc ";
        try{
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, source_gene_index);
            pstmt.setString(2, target_gene_index);
            pstmt.setString(3, source_gene_index);
            pstmt.setString(4, target_gene_index);
            rs = pstmt.executeQuery();
            while(rs.next()){
                int pubmed_id = rs.getInt(1);
                pubmed_ids.add(new Integer(pubmed_id).toString());
            }
            rs.close();
            pstmt.close();
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            dbm.disconnect(con);
        }
        return pubmed_ids;
     
     }
         
     public Vector getMedlineRecords_gg(String source_gene_index, int target_gene_locusid){
        DBManager dbm = new DBManager();
        Connection con = dbm.connect();
        if(con == null){
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector pubmed_ids = new Vector();
        
        String sql = "select pubmedid from medline_records_gene_assoc mrga, gene_index gi, gene_list gl " +
                     "where (mrga.gene_index1 = ? and mrga.gene_index2 = gi.gene_index and gi.gene_index_id = gl.gene_index_id and gl.locus_id = ? ) " +
                     "or (mrga.gene_index2 = ? and mrga.gene_index1 = gi.gene_index and gi.gene_index_id = gl.gene_index_id and gl.locus_id = ? )" +
                     "order by pubmedid desc ";
        try{
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, source_gene_index);
            pstmt.setInt(2, target_gene_locusid);
            pstmt.setString(3, source_gene_index);
            pstmt.setInt(4, target_gene_locusid);
            rs = pstmt.executeQuery();
            while(rs.next()){
                int pubmed_id = rs.getInt(1);
                pubmed_ids.add(new Integer(pubmed_id).toString());
            }
            rs.close();
            pstmt.close();
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            dbm.disconnect(con);
        }
        return pubmed_ids;
     
     }         
}
