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
    
    //protected ConnectionPool pool;
    
    /** Creates new DiseaseGeneManager */
    public DiseaseGeneManager() {
        //pool = ConnectionPool.getInstance();
    }
    
    /**
     * Query the database to find the related diseases for the given term.
     *
     * @param term The term used for searching.
     * @return A list of Disease objects.
     */
    public Vector findDiseases(String term) {
        DBManager manager = new DBManager();
        Connection conn = manager.connect();
 
        
        //Connection conn = pool.getConnection();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        String sql; Vector diseases;
        /*
        String sql = "select * from disease_list where disease_mesh_term = ?";
        Vector diseases = queryDiseases(conn, sql, "%"+term+"%");
        
        if(diseases != null && diseases.size() == 0) {
            sql = "select dl.hip_disease_id, dl.disease_mesh_term, dl.date_added from disease_list dl, disease_nicknames dn where dl.hip_disease_id=dn.hip_disease_id and dn.disease_nick_name_value=?";
            diseases = queryDiseases(conn, sql, term);
        }
        
        if(diseases != null && diseases.size() == 0) {
         */
            TreeSet diseaseSet = new TreeSet(new DiseaseComparator());
            sql = "select * from disease_list where disease_mesh_term like ?";
            diseaseSet.addAll(queryDiseases(conn, sql, "%"+term+"%"));
            sql = "select distinct dl.hip_disease_id, dl.disease_mesh_term, dl.date_added from disease_list dl, disease_nicknames dn where dl.hip_disease_id=dn.hip_disease_id and dn.disease_nick_name_value like ?";
            diseaseSet.addAll(queryDiseases(conn, sql, "%"+term+"%"));
            diseases = new Vector(diseaseSet);
        
                               
        manager.disconnect(conn);

        return diseases;
    }
    
    public Vector getAssociationsByDisease(int disease, int stat, int number) {
        DBManager manager = new DBManager();
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
                
                //                Association existedAssociation = found(newAssociations, association);
                
                //                if(existedAssociation != null) {
                //                    if(association.getStat().compareTo(existedAssociation.getStat()) < 0) {
                //                        newAssociations.removeElement(existedAssociation);
                //                        newAssociations.addElement(association);
                //                    }
                //                } else {
                newAssociations.addElement(association);
                //                }
            }
        }
               
        manager.disconnect(conn);
        
        return newAssociations;
    }
    
    protected Vector queryDiseases(Connection conn, String sql, String searchTerm) {
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
    
    protected Vector queryAssociations(Connection conn, int statid, int diseaseid) {
        String sql = "select g.gene_index_id, g.gene_index, t.index_type,"+
        " g.date_added, s.statistic_score, st.statistic_type, a.singlehit_disease,"+
        " a.singlehit_gene, a.doublehit, a.doublenegative, a.date_added"+
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
                int singlehitDisease = rs.getInt(7);
                int singlehitGene = rs.getInt(8);
                int doublehit = rs.getInt(9);
                int doublehitNegative = rs.getInt(10);
                String dateadded = rs.getString(11);
                
                Statistics stat = new Statistics(statid, statType);
                StatAnalysis statAnalysis = new StatAnalysis(stat, statScore);
                GeneIndex geneIndex = new GeneIndex(id, index, type, date);
                AssociationData data = new AssociationData(singlehitDisease,singlehitGene,doublehit,doublehitNegative,dateadded);
                Association assoc = new Association(disease, geneIndex, statAnalysis, data);
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
    
    protected Gene queryGeneByIndex(Connection conn, GeneIndex geneIndex) {
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
/*            } else {
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
 */
                rs = stmt.executeQuery();
                
                if(rs.next()) {
                    int id = rs.getInt(1);
                    String symbol = rs.getString(2);
                    int qualSymbol = rs.getInt(3);
                    String name = rs.getString(4);
                    int qualName = rs.getInt(5);
                    String date = rs.getString(6);
                    int locusid = rs.getInt(7);
                    gene = new Gene(id, symbol, qualSymbol, name, qualName, date, locusid, Gene.GENE);
                }
                
                rs.close();
                stmt.close();
                
                if(gene != null) {
                    Vector nicknames = queryNicknamesByGene(conn, gene.getHipGeneId());
                    gene.setNicknames(nicknames);
                    Vector geneinfo = queryGeneinfoByGene(conn, gene.getHipGeneId());
                    gene.setInformation(geneinfo);
                }
            } else {
                sql = "select p.parent_id, p.parent_value, p.date_added"+
                " from parent_list p"+
                " where p.parent_value=?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, geneIndex.getIndex());
                
                rs = stmt.executeQuery();
                
                if(rs.next()) {
                    int id = rs.getInt(1);
                    String value = rs.getString(2);
                    String date = rs.getString(3);
                    gene = new Gene(id, null, -1, value, -1, date, -1, Gene.FAMILY);
                }
                
                rs.close();
                stmt.close();
                
                if(gene != null) {
                    Vector nicknames = queryChildren(conn, gene.getHipGeneId());
                    gene.setNicknames(nicknames);
                    Vector geneinfo = new Vector();
                    gene.setInformation(geneinfo);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return gene;
    }
    
    
    protected Vector queryNicknamesByGene(Connection conn, int id) {
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
    
    protected Vector queryChildren(Connection conn, int id) {
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "select l.symbol_value, l.formal_name_value "+
        " from gene_list l, family_mapping m"+
        " where l.hip_gene_id = m.hip_gene_id"+
        " and m.parent_id = "+id;
        
        Vector nicknames = new Vector();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                String symbol = rs.getString(1);
                String formal_name = rs.getString(2);
                if(symbol == null || symbol.trim().length() == 0)
                    nicknames.addElement(formal_name);
                else
                    nicknames.addElement(symbol);
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
        String sql = "select t.id_type, g.id_value, g.extra_information, g.order"+
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
                int refSeq_NM_order = rs.getInt(4);
                Geneinfo info = new Geneinfo(type, value, extraInfo, refSeq_NM_order);
                geneInfo.addElement(info);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return geneInfo;
    }
    
    // override queryGeneinfoByGene(Connection, id)
    public Vector queryGeneinfoByGene(Connection conn, String symbol) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select t.id_type, g.id_value, g.extra_information"+
        " from id_type t, gene_information g, gene_list gl "+
        " where t.type_id=g.type_id and g.hip_gene_id = gl.hip_gene_id "+
        " and (gl.symbol_value = ? or gl.formal_name_value = ?)";
        Vector geneInfo = new Vector();
        
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, symbol);
            pstmt.setString(2, symbol);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                String type = rs.getString(1);
                String value = rs.getString(2);
                String extraInfo = rs.getString(3);
                Geneinfo info = new Geneinfo(type, value, extraInfo, -1);
                geneInfo.addElement(info);
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return geneInfo;
    }
    
    
    protected Association found(Vector newAssociations, Association association) {
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
        DBManager manager = new DBManager();
        Connection conn = manager.connect();
 
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select distinct i.gene_index_id, i.gene_index, t.index_type, i.date_added"+
        " from gene_index i, search_term s, index_type t"+
        " where i.gene_index_id=s.gene_index_id"+
        " and i.index_type_id=t.index_type_id"+
        " and s.gene_term like ?";
        Vector geneIndexes = new Vector();
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%"+term+"%");
            rs = stmt.executeQuery();
            
            while(rs.next()) {
                int id = rs.getInt(1);
                String index = rs.getString(2);
                String type = rs.getString(3);
                String date = rs.getString(4);
                if(index.length() <= 80){
                    GeneIndex geneIndex = new GeneIndex(id, index, type, date);
                    geneIndexes.addElement(geneIndex);
                }
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            manager.disconnect(conn);
            return null;
        }
        
        manager.disconnect(conn);
        return geneIndexes;
    }
    
    public Vector queryGeneIndexByLocusid(int locusid) {
        DBManager manager = new DBManager();
        Connection conn = manager.connect();
 
        
        //Connection conn = pool.getConnection();
        
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
            manager.disconnect(conn);
            return null;
        }
        
        manager.disconnect(conn);
        return geneIndexes;
    }
    
    public Vector getAssociationsByGeneIndex(int geneIndexid, int statid, int number) {
        DBManager manager = new DBManager();
        Connection conn = manager.connect();
 
        
        //Connection conn = pool.getConnection();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        Statement stmt = null;
        ResultSet rs = null;
        
        String sql = "select d.hip_disease_id, d.disease_mesh_term, d.date_added,"+
        " s.statistic_score, st.statistic_type, ad.singlehit_disease,"+
        " ad.singlehit_gene, ad.doublehit, ad.doublenegative, ad.date_added"+
        " from disease_list d, disease_and_gene_association a, statistic_type st,"+
        " association_data ad, statistic_analysis s"+
        " where d.hip_disease_id=a.hip_disease_id"+
        " and a.association_id=ad.association_id"+
        " and ad.data_id=s.data_id"+
        " and s.statistic_id = st.statistic_id"+
        " and a.gene_index_id="+geneIndexid+
        " and s.statistic_id="+statid;
        
        if(statid == Statistics.FISCHERID)
            sql = sql + " order by s.statistic_score";
        else
            sql = sql + " order by s.statistic_score desc";
        
        Vector associations = new Vector();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                if(associations.size() == number)
                    break;
                
                int id = rs.getInt(1);
                String term = rs.getString(2);
                String date = rs.getString(3);
                double statScore = rs.getDouble(4);
                String statType = rs.getString(5);
                int singlehitDisease = rs.getInt(6);
                int singlehitGene = rs.getInt(7);
                int doublehit = rs.getInt(8);
                int doublehitNegative = rs.getInt(9);
                String dateadded = rs.getString(10);
                Disease disease = new Disease(id, term, date);
                GeneIndex geneIndex = new GeneIndex(geneIndexid);
                Statistics stat = new Statistics(statid, statType);
                StatAnalysis statAnalysis = new StatAnalysis(stat, statScore);
                AssociationData data = new AssociationData(singlehitDisease,singlehitGene,doublehit,doublehitNegative,dateadded);
                Association assoc = new Association(disease, geneIndex, statAnalysis, data);
                associations.addElement(assoc);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            manager.disconnect(conn);
            return null;
        }
        
        manager.disconnect(conn);
        return associations;
    }
    
    public Disease queryDiseaseById(int id) {
        DBManager manager = new DBManager();
        Connection conn = manager.connect();

        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        Statement stmt = null;
        ResultSet rs = null;
        
        String sql = "select * from disease_list where hip_disease_id="+id;
        Disease disease = null;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if(rs.next()) {
                int diseaseid = rs.getInt(1);
                String term = rs.getString(2);
                String date = rs.getString(3);
                disease = new Disease(diseaseid, term, date);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            manager.disconnect(conn);
            return null;
        }
        
        manager.disconnect(conn);
        return disease;
    }
    
    public Statistics queryStatById(int id) {
        DBManager manager = new DBManager();
        Connection conn = manager.connect();

        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        Statement stmt = null;
        ResultSet rs = null;
        
        String sql = "select * from statistic_type where statistic_id="+id;
        Statistics stat = null;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if(rs.next()) {
                int statid = rs.getInt(1);
                String type = rs.getString(2);
                stat = new Statistics(statid, type);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            manager.disconnect(conn);
            return null;
        }
        manager.disconnect(conn);
        return stat;
    }        
    
    public GeneIndex queryGeneIndexById(int id) {
        DBManager manager = new DBManager();
        Connection conn = manager.connect();

        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        Statement stmt = null;
        ResultSet rs = null;
        
        String sql = "select * from gene_index where gene_index_id="+id;
        GeneIndex geneIndex = null;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if(rs.next()) {
                int indexid = rs.getInt(1);
                String geneIndexString = rs.getString(2);
                String type = rs.getString(3);
                String date = rs.getString(4);
                geneIndex = new GeneIndex(indexid, geneIndexString, type, date);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            manager.disconnect(conn);
            return null;
        }
        manager.disconnect(conn);
        return geneIndex;
    }       
    
    
    public Vector getMedlineRecords(int disease_id, String gene_index){
        DBManager dbm = new DBManager();
        Connection con = dbm.connect();
        if(con == null){
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector pubmed_ids = new Vector();
        
        String sql = "select mr.pubmedid from medline_records mr, disease_list dl " +
                     "where dl.disease_mesh_term = mr.disease_index " +
                     "and dl.hip_disease_id = ? and mr.gene_index = ? order by mr.pubmedid desc";
        try{
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, disease_id);
            pstmt.setString(2, gene_index);
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
    
    
    public Vector getMedlineRecords(String disease_mesh_term, String gene_symbol){
        DBManager dbm = new DBManager();
        Connection con = dbm.connect();
        if(con == null){
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector pubmed_ids = new Vector();
        
        String sql = "select mr.pubmedid from medline_records mr, gene_index gi, gene_list gl " +
                     "where mr.disease_index = ? and mr.gene_index = gi.gene_index " +
                     "and gi.gene_index_id = gl.gene_index_id and gl.symbol_value = ? " +
                     "order by mr.pubmedid desc ";
        try{
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, disease_mesh_term);
            pstmt.setString(2, gene_symbol);
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
    
    
    public Vector getMedlineRecordsByFamily(String disease_term, String child_gene_name) {
        DBManager dbm = new DBManager();
        Connection con = dbm.connect();
        if(con == null){
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector pubmed_ids = new Vector();
        
        String sql = "select mr.pubmedid from medline_records mr, gene_index gi, " +
                     "parent_list pl, family_mapping fm, gene_list gl " +
                     "where mr.disease_index = ? and mr.gene_index = gi.gene_index " +
                     "and gi.gene_index = pl.parent_value and pl.parent_id = fm.parent_id " +
                     "and fm.hip_gene_id = gl.hip_gene_id and gl.symbol_value = ? " +
                     "order by mr.pubmedid desc ";
        try{
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, disease_term);
            pstmt.setString(2, child_gene_name);
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
