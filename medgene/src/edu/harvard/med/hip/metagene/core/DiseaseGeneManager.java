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
                if(newAssociations.size() == number) {
                    break;
                }
                
                Association association = (Association)associations.elementAt(i);
                                
                if(association == null) {
                    continue;
                }
                
                Gene gene = queryGeneByIndex(conn, association.getGeneIndex());
                association.setGene(gene);
                
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
        " and dg.hip_disease_id="+diseaseid+
        " order by s.statistic_score";
        
        Statement stmt = null;
        ResultSet rs = null;
        Vector associations = null;
        Disease disease = new Disease(diseaseid);
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            associations = new Vector();
            
            while(rs.next()) {
                int id = rs.getInt(1);
                String index = rs.getString(2);
                String type = rs.getString(3);
                String date = rs.getString(4);
                double statScore = rs.getDouble(5);
                String statType = rs.getString(6);
                Statistics stat = new Statistics(statid, statType);
                StatAnalysis statAnalysis = new StatAnalysis(stat, statScore);
                GeneIndex geneIndex = new GeneIndex(id, index, type, date);
                Association assoc = new Association(disease, geneIndex, statAnalysis);
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
        Statement stmt = null;
        Gene gene = null;
        ResultSet rs = null;
        String sql = null;
        
        if(GeneIndex.NAME.equals(geneIndex.getType()) || GeneIndex.SYMBOL.equals(geneIndex.getType())) {
            sql = "select hip_gene_id, symbol_value, quality_id_symbol,"+
                " formal_name_value, quality_id_name, date_added, locus_id"+
                " from gene_list where gene_index_id="+geneIndex.getIndexid()+
                " order by date_added desc";
        } else {
            sql = "select gl.hip_gene_id, gl.symbol_value, gl.quality_id_symbol,"+
                " gl.formal_name_value, gl.quality_id_name, gl.date_added, gl.locus_id"+
                " from gene_list gl, family_mapping f, parent_list p"+
                " where gl.hip_gene_id=f.hip_gene_id"+
                " and f.parent_id=p.parent_id"+
                " and p.parent_value='"+geneIndex.getIndex()+
                "' order by gl.date_added desc";
        }
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
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
            if(asso.getGene().getHipGeneId() == association.getGene().getHipGeneId()) {
                existed = asso;
            }
        }
        
        return existed;
    }
}
