/*
 * DiseaseGeneManager.java
 *
 * This class is used to manage the gene and disease relationships.
 *
 * Created on December 11, 2001, 2:24 PM
 */

package edu.harvard.med.hip.metagene.core;

import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.metagene.database.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class DiseaseGeneManager {
    
    /** Creates new DiseaseGeneManager */
    public DiseaseGeneManager() {
    }
    
    /**
     * Query the database to find the related diseases for the given term.
     *
     * @param term The term used for searching.
     * @return A list of Disease objects.
     */
    public Vector findDiseases(String term) {
        DatabaseManager manager = new DatabaseManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        String sql = "select * from disease_list where disease_mesh_term = '"+term+"'";        
        Vector diseases = queryDiseases(conn, sql);
        
        if(diseases != null && diseases.size() == 0) {
            sql = "select dl.hip_disease_id, dl.disease_mesh_term, dl.date_added from disease_list dl, disease_nicknames dn where dl.hip_disease_id=dn.hip_disease_id and dn.disease_nick_name_value='"+term+"'";
            diseases = queryDiseases(conn, sql);
        } 
        
        if(diseases != null && diseases.size() == 0) {
//            sql = "select distinct dl.hip_disease_id, dl.disease_mesh_term, dl.date_added from disease_list dl, disease_nicknames dn where dl.disease_mesh_term like '%"+term+"%' or (dl.hip_disease_id=dn.hip_disease_id and dn.disease_nick_name_value like '%"+term+"%'";

            TreeSet diseaseSet = new TreeSet(new DiseaseComparator());
            sql = "select * from disease_list where disease_mesh_term like '%"+term+"%'";
            diseaseSet.addAll(queryDiseases(conn, sql));
            sql = "select distinct dl.hip_disease_id, dl.disease_mesh_term, dl.date_added from disease_list dl, disease_nicknames dn where dl.hip_disease_id=dn.hip_disease_id and dn.disease_nick_name_value like '%"+term+"%'";
            diseaseSet.addAll(queryDiseases(conn, sql));
            diseases = new Vector(diseaseSet);
        }
        
        manager.disconnect();
        return diseases;
    }

    public Vector getGenes(int disease, int stat, int number) {        
        DatabaseManager manager = new DatabaseManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }        
        
        Vector geneIndexes = queryGeneIndexes(conn, stat, disease);
                
        if(geneIndexes != null) {
            int count = 0;
            while(count<number) {
                
            }
        }
        
        manager.disconnect();
        return geneIndexes;
    }
    
    private Vector queryDiseases(Connection conn, String sql) {
        Statement stmt = null;
        Vector diseases = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            diseases = new Vector();
            
            while(rs.next()) {
                int id = rs.getInt(1);
                String term = rs.getString(2);
                String date = rs.getString(3);
                Disease disease = new Disease(id, term, date);
                diseases.addElement(disease);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return diseases;
    }
    
    private Vector queryGeneIndexes(Connection conn, int stat, int disease) {
        String sql = "select g.gene_index_id, g.gene_index, t.index_type, g.date_added, s.statistic_score"+
                    " from disease_and_gene_association dg, gene_index g, index_type t,"+
                    " association_data a, statistic_analysis s"+
                    " where dg.gene_index_id=g.gene_index_id"+
                    " and g.index_type_id=t.index_type_id"+
                    " and dg.association_id=a.association_id"+
                    " and a.data_id=s.data_id"+
                    " and s.statistic_id="+stat+
                    " and dg.hip_disease_id="+disease+
                    " order by s.statistic_score";        
        
        Statement stmt = null;
        ResultSet rs = null;
        Vector geneIndexes = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            geneIndexes = new Vector();
            
            while(rs.next()) {
                int id = rs.getInt(1);
                String index = rs.getString(2);
                String type = rs.getString(3);
                String date = rs.getString(4);
                double statScore = rs.getDouble(5);
                GeneIndex geneIndex = new GeneIndex(id, index, type, date, statScore);
                geneIndexes.addElement(geneIndex);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }            
        
        return geneIndexes;
    }
}
