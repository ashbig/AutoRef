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
        
        String sql = "select * from disease_list where disease_mesh_term = ?";
        Vector diseases = queryDiseases(conn, sql, term);
        
        if(diseases != null && diseases.size() == 0) {
            sql = "select dl.hip_disease_id, dl.disease_mesh_term, dl.date_added from disease_list dl, disease_nicknames dn where dl.hip_disease_id=dn.hip_disease_id and dn.disease_nick_name_value=?";
            diseases = queryDiseases(conn, sql, term);
        }
        
        if(diseases != null && diseases.size() == 0) {
            TreeSet diseaseSet = new TreeSet(new DiseaseComparator());
            sql = "select * from disease_list where disease_mesh_term like ?";
            diseaseSet.addAll(queryDiseases(conn, sql, "%"+term+"%"));
            sql = "select distinct dl.hip_disease_id, dl.disease_mesh_term, dl.date_added from disease_list dl, disease_nicknames dn where dl.hip_disease_id=dn.hip_disease_id and dn.disease_nick_name_value like ?";
            diseaseSet.addAll(queryDiseases(conn, sql, "%"+term+"%"));
            diseases = new Vector(diseaseSet);
        }
        
        manager.disconnect();
        return diseases;
    }
    
    public Vector getAssociations(int disease, int stat, int number) {
        DatabaseManager manager = new DatabaseManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        Vector associations = queryAssociations(conn, stat, disease);
        Vector newAssociations = new Vector();
        
        if(associations != null) {
            for(int i=0; i<associations.size(); i++) {
                if(number != -1 && newAssociations.size() == number) {
                    break;
                }
                
                Association association = (Association)associations.elementAt(i);
                
                if(association == null) {
                    continue;
                }
                
                Association existedAssociation = found(newAssociations, association);
                
                if(existedAssociation != null) {
                    if(association.getStat().compareTo(existedAssociation.getStat()) < 0) {
                        newAssociations.removeElement(existedAssociation);
                        newAssociations.addElement(association);
                    }
                } else {
                    newAssociations.addElement(association);
                }
            }
        }
        
        manager.disconnect();
        return newAssociations;
    }
    
    private Vector queryDiseases(Connection conn, String sql, String searchTerm) {
        PreparedStatement stmt = null;
        Vector diseases = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, searchTerm);
            rs = stmt.executeQuery();
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
    
    private Vector queryAssociations(Connection conn, int statid, int diseaseid) {
        String sql = "select g.gene_index_id, g.gene_index, t.index_type, g.date_added, s.statistic_score, st.statistic_type"+
        " from disease_and_gene_association dg, gene_index g, index_type t,"+
        " association_data a, statistic_analysis s, statistic_type st"+
        " where dg.gene_index_id=g.gene_index_id"+
        " and g.index_type_id=t.index_type_id"+
        " and dg.association_id=a.association_id"+
        " and a.data_id=s.data_id"+
        " and s.statistic_id = st.statistic_id"+
        " and s.statistic_id="+statid+
        " and dg.hip_disease_id="+diseaseid;
        
        if(statid == Statistics.FISCHERID)
            sql = sql + " order by s.statistic_score";
        else
            sql = sql + " order by s.statistic_score desc";
        
        Statement stmt = null;
        ResultSet rs = null;
        Vector associations = null;
        Disease disease = new Disease(diseaseid);
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            associations = new Vector();
            
            int count = 0;
            while(rs.next()) {
                count++;
                int id = rs.getInt(1);
                String index = rs.getString(2);
                String type = rs.getString(3);
                String date = rs.getString(4);
                double statScore = rs.getDouble(5);
                //System.out.println(count+": id: "+id+" and score: "+statScore);
                String statType = rs.getString(6);
                Statistics stat = new Statistics(statid, statType);
                StatAnalysis statAnalysis = new StatAnalysis(stat, statScore);
                GeneIndex geneIndex = new GeneIndex(id, index, type, date);
                Association assoc = new Association(disease, geneIndex, statAnalysis);
                Gene gene = queryGeneByIndex(conn, assoc.getGeneIndex());
                assoc.setGene(gene);
                associations.addElement(assoc);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return associations;
    }
    
    private Gene queryGeneByIndex(Connection conn, GeneIndex geneIndex) {
        PreparedStatement stmt = null;
        Gene gene = null;
        ResultSet rs = null;
        String sql = null;
        
        try {
            if(GeneIndex.NAME.equals(geneIndex.getType()) || GeneIndex.SYMBOL.equals(geneIndex.getType())) {
                sql = "select hip_gene_id, symbol_value, quality_id_symbol,"+
                " formal_name_value, quality_id_name, date_added, locus_id"+
                " from gene_list where gene_index_id=?"+
                " order by date_added desc";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, geneIndex.getIndexid());
            } else {
                sql = "select gl.hip_gene_id, gl.symbol_value, gl.quality_id_symbol,"+
                " gl.formal_name_value, gl.quality_id_name, gl.date_added, gl.locus_id"+
                " from gene_list gl, family_mapping f, parent_list p"+
                " where gl.hip_gene_id=f.hip_gene_id"+
                " and f.parent_id=p.parent_id"+
                " and p.parent_value=?"+
                " order by gl.date_added desc";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, geneIndex.getIndex());
            }
            
            rs = stmt.executeQuery();
            
            if(rs.next()) {
                int id = rs.getInt(1);
                String symbol = rs.getString(2);
                int qualSymbol = rs.getInt(3);
                String name = rs.getString(4);
                int qualName = rs.getInt(5);
                String date = rs.getString(6);
                int locusid = rs.getInt(7);
                gene = new Gene(id, symbol, qualSymbol, name, qualName, date, locusid);
            }
            
            rs.close();
            stmt.close();
            
            if(gene != null) {
                Vector nicknames = queryNicknamesByGene(conn, gene.getHipGeneId());
                gene.setNicknames(nicknames);
                Vector geneinfo = queryGeneinfoByGene(conn, gene.getHipGeneId());
                gene.setInformation(geneinfo);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return gene;
    }
    
    
    private Vector queryNicknamesByGene(Connection conn, int id) {
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "select gene_nick_name_value from gene_nicknames"+
        " where hip_gene_id="+id;
        Vector nicknames = new Vector();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                String name = rs.getString(1);
                nicknames.addElement(name);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return nicknames;
    }
    
    public Vector queryGeneinfoByGene(Connection conn, int id) {
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "select t.id_type, g.id_value, g.extra_information"+
        " from id_type t, gene_information g"+
        " where t.type_id=g.type_id"+
        " and g.hip_gene_id="+id;
        Vector geneInfo = new Vector();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                String type = rs.getString(1);
                String value = rs.getString(2);
                String extraInfo = rs.getString(3);
                Geneinfo info = new Geneinfo(type, value, extraInfo);
                geneInfo.addElement(info);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return geneInfo;
    }
    
    private Association found(Vector newAssociations, Association association) {
        if(newAssociations == null) {
            return null;
        }
        
        Association existed = null;
        
        for(int i=0; i<newAssociations.size(); i++) {
            Association asso = (Association)newAssociations.elementAt(i);
            
            if(asso.getGene() == null) {
                System.out.println("asso: "+asso.getGeneIndex().getIndexid());
            }
            
            if(association.getGene() == null) {
                System.out.println("association"+association.getGeneIndex().getIndexid());
            }            
            
            if(asso.getGene().getHipGeneId() == association.getGene().getHipGeneId()) {
                existed = asso;
                break;
            }
        }
        
        return existed;
    }
    
    public Vector queryGeneIndexBySearchTerm(String term) {
        DatabaseManager manager = new DatabaseManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select i.gene_index_id, i.gene_index, t.index_type, i.date_added"+
        " from gene_index i, search_term s, index_type t"+
        " where i.gene_index_id=s.gene_index_id"+
        " and i.index_type_id=t.index_type_id"+
        " and s.gene_term=?";
        Vector geneIndexes = new Vector();
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, term);
            rs = stmt.executeQuery();
            
            while(rs.next()) {
                int id = rs.getInt(1);
                String index = rs.getString(2);
                String type = rs.getString(3);
                String date = rs.getString(4);
                GeneIndex geneIndex = new GeneIndex(id, index, type, date);
                geneIndexes.addElement(geneIndex);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            manager.disconnect();
            return null;
        }
        
        manager.disconnect();
        return geneIndexes;
    }
    
    public Vector queryGeneIndexByLocusid(int locusid) {
        DatabaseManager manager = new DatabaseManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "select i.gene_index_id, i.gene_index, t.index_type, i.date_added"+
        " from gene_index i, gene_list l, index_type t"+
        " where i.gene_index_id=l.gene_index_id"+
        " and i.index_type_id=t.index_type_id"+
        " and l.locus_id="+locusid;
        Vector geneIndexes = new Vector();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if(rs.next()) {
                int id = rs.getInt(1);
                String index = rs.getString(2);
                String type = rs.getString(3);
                String date = rs.getString(4);
                GeneIndex geneIndex = new GeneIndex(id, index, type, date);
                geneIndexes.addElement(geneIndex);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            manager.disconnect();
            return null;
        }
        
        manager.disconnect();
        return geneIndexes;
    }
    
    public Vector getDiseasesByGeneIndex(int geneIndex, int stat, int number) {
        DatabaseManager manager = new DatabaseManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "select d.hip_disease_id, d.disease_mesh_term, d.date_added"+
        " from disease_list d, disease_and_gene_association a,"+
        " association_data ad, statistic_analysis s"+
        " where d.hip_disease_id=a.hip_disease_id"+
        " and a.association_id=ad.association_id"+
        " and ad.data_id=s.data_id"+
        " and a.gene_index_id="+geneIndex+
        " and s.statistic_id="+stat;
        
        if(stat == Statistics.FISCHERID)
            sql = sql + " order by s.statistic_score";
        else
            sql = sql + " order by s.statistic_score desc";
        
        Vector diseases = new Vector();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                if(diseases.size() == number)
                    break;
                
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
            manager.disconnect();
            return null;
        }
        
        manager.disconnect();
        return diseases;
    }
}
