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
    public DiseaseGeneManager() {}
    
    /** round the double with 4 decimal
     *  @param score        a double number
     *  @return             double number rounded with 4 decimal
     */
    public double round_4(double score){        
        long temp =  (long)(score * 10000 + ( score > 0 ? .5 : -.5 )); 
        return (double) temp / 10000;
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
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        String sql; 
        Vector diseases;
        
        TreeSet diseaseSet = new TreeSet(new DiseaseComparator());
        sql = "select distinct hip_disease_id, disease_mesh_term, date_added from disease_list where disease_mesh_term_upper like ?";        
        diseaseSet.addAll(queryDiseases(conn, sql, "%" + term.toUpperCase() + "%"));
        sql = "select distinct dl.hip_disease_id, dl.disease_mesh_term, dl.date_added " + 
              " from disease_list dl, disease_nicknames dn where dl.hip_disease_id=dn.hip_disease_id and dn.disease_nickname_uppercase like ?";
        diseaseSet.addAll(queryDiseases(conn, sql, "%" + term.toUpperCase() + "%"));
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
                newAssociations.addElement(association);
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
    
    /** find disease-gene associations based on the given disease id
     *  @param conn      Connection
     *  @param statid    statistic id
     *  @param diseaseid     disease id
     *  @return vector of associations
     */
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
                        
            int count = 0;
            while(rs.next()) {
                count++;
                int id = rs.getInt(1);
                String index = rs.getString(2);
                String type = rs.getString(3);
                String date = rs.getString(4);
                double statScore = round_4(rs.getDouble(5));
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
                //Gene gene = queryGeneByIndex(conn, assoc.getGeneIndex());
                Gene gene = queryGeneByIndex(conn, assoc.getGeneIndex(), pstmt_symbol, pstmt_family, pstmt_nickname, 
                                              pstmt_geneinfo, pstmt_query_children );
                assoc.setGene(gene);
                associations.addElement(assoc);
            }
            
            rs.close();
            stmt.close();
            pstmt_symbol.close(); 
            pstmt_family.close();
            pstmt_nickname.close();
            pstmt_geneinfo.close();
            pstmt_query_children.close();
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return associations;
    }
    
   ///////////////////////////////////////////
    /** find Object gene if given geneIndex
     *  @param conn         Connection
     *  @param geneIndex    GeneIndex Object
     *  @param pstmt_symbol
     *  @param pstmt_family
     *  @param pstmt_nickname
     *  @param pstmt_geneinfo
     *  @param pstmt_query_children
     *  @return Gene object
     */    
    protected Gene queryGeneByIndex(Connection conn, GeneIndex geneIndex, PreparedStatement pstmt_symbol, 
                                    PreparedStatement pstmt_family, PreparedStatement pstmt_nickname, 
                                    PreparedStatement pstmt_geneinfo, PreparedStatement pstmt_query_children ){
        Gene gene = null;
        ResultSet rs = null;
          
        try {
            if(GeneIndex.NAME.equals(geneIndex.getType()) || GeneIndex.SYMBOL.equals(geneIndex.getType())) {
                pstmt_symbol.setInt(1, geneIndex.getIndexid());         
                rs = pstmt_symbol.executeQuery();                
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
                
                if(gene != null) {
                    Vector nicknames = queryNicknamesByGene(conn, gene.getHipGeneId(), pstmt_nickname);
                    gene.setNicknames(nicknames);
                    Vector geneinfo = queryGeneinfoByGene(conn, gene.getHipGeneId(), pstmt_geneinfo);
                    gene.setInformation(geneinfo);
                }
            } 
            else {  // GeneIndex.FAMILY.equals(geneIndex.getType())
                
                pstmt_family.setString(1, geneIndex.getIndex());                
                rs = pstmt_family.executeQuery();                
                if(rs.next()) {
                    int id = rs.getInt(1);
                    String value = rs.getString(2);
                    String date = rs.getString(3);
                    gene = new Gene(id, null, -1, value, -1, date, -1, Gene.FAMILY);
                }                
                rs.close();
                
                if(gene != null) {
                    Vector nicknames = queryChildren(conn, gene.getHipGeneId(), pstmt_query_children);                            
                    gene.setNicknames(nicknames);
                    Vector geneinfo = new Vector();
                    gene.setInformation(geneinfo);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);System.out.println("Exception throwed in queryGenebyGeneIndex");
        }
        
        return gene;
    
    
    
    
    }
    
    /** find Object gene if given geneIndex
     *  @param conn         Connection
     *  @param geneIndex    GeneIndex Object
     *  @return Gene object
     */    
    
    protected Gene queryGeneByIndex00(Connection conn, GeneIndex geneIndex) {
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
            } 
            else {
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
            System.out.println(ex);System.out.println("Exception in queryGenebyGeneIndex");
        }
        
        return gene;
    }

    // query nick names by gene id (version 1)
    protected Vector queryNicknamesByGene(Connection conn, int id, PreparedStatement pstmt_nickname) {

        Vector nicknames = new Vector();
        
        try {
            ResultSet rs = null;
            pstmt_nickname.setInt(1, id);
            rs = pstmt_nickname.executeQuery();            
            while(rs.next()) {
                String name = rs.getString(1);
                nicknames.addElement(name);
            }            
            rs.close();
            //pstmt_nickname.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return nicknames;
    }
    
    // query nick names by gene id (version 2)
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
    
    // query children by gene id (version 1)
    protected Vector queryChildren(Connection conn, int id, PreparedStatement pstmt_query_children) {

        Vector nicknames = new Vector();
        
        try {
            ResultSet rs = null;
            pstmt_query_children.setInt(1, id);
            rs = pstmt_query_children.executeQuery();
            
            while(rs.next()) {
                String symbol = rs.getString(1);
                String formal_name = rs.getString(2);
                if(symbol == null || symbol.trim().length() == 0)
                    nicknames.addElement(formal_name);
                else
                    nicknames.addElement(symbol);
            }
            
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
 
        return nicknames;
    }
    
    // query children by gene id (version 2)
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
    
    // query gene information by gene id (version 1)
    public Vector queryGeneinfoByGene(Connection conn, int id, PreparedStatement pstmt_geneinfo) {

        Vector geneInfo = new Vector();
        
        try {
            ResultSet rs = null;            
            pstmt_geneinfo.setInt(1, id);
            rs = pstmt_geneinfo.executeQuery();
            
            while(rs.next()) {
                String type = rs.getString(1);
                String value = rs.getString(2);
                String extraInfo = rs.getString(3);
                int refSeq_NM_order = rs.getInt(4);
                Geneinfo info = new Geneinfo(type, value, extraInfo, refSeq_NM_order);
                geneInfo.addElement(info);
            }
            
            rs.close();            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return geneInfo;
    }    
    
    /** query gene information by gene id (version 2)
     *  @param conn         Connection
     *  @param id           hip gene id
     *  @return vector of gene information
     */   
    public Vector queryGeneinfoByGene(Connection conn, int id) {
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "select t.id_type, g.id_value, g.extra_information, g.gi_order"+
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
    
    
    /** query gene information by gene symbol (version 3)
     *  @param conn         Connection
     *  @param symbol       gene symbol
     *  @return vector of gene information
     */   
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
        " and s.gene_term_uppercase like ?";
        Vector geneIndexes = new Vector();
        TreeSet geneIndexesTree = new TreeSet(new GeneIndexComparator());
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + term.toUpperCase() + "%");
            rs = stmt.executeQuery();
            
            while(rs.next()) {
                int id = rs.getInt(1);
                String index = rs.getString(2);
                String type = rs.getString(3);
                String date = rs.getString(4);
                if(index.length() <= 80){
                    GeneIndex geneIndex = new GeneIndex(id, index, type, date);                    
                    geneIndexesTree.add(geneIndex);
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
        Iterator it = geneIndexesTree.iterator();
        while(it.hasNext())
            geneIndexes.addElement((GeneIndex)(it.next()));
        return geneIndexes;
    }
    
    public Vector queryGeneIndexByLocusid(int locusid) {
        DBManager manager = new DBManager();
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
        TreeSet geneIndexesTree = new TreeSet(new GeneIndexComparator());
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if(rs.next()) {
                int id = rs.getInt(1);
                String index = rs.getString(2);
                String type = rs.getString(3);
                String date = rs.getString(4);
                GeneIndex geneIndex = new GeneIndex(id, index, type, date);
                geneIndexesTree.add(geneIndex);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            manager.disconnect(conn);
            return null;
        }
        
        manager.disconnect(conn);
        Iterator it = geneIndexesTree.iterator();
        while(it.hasNext())
            geneIndexes.addElement((GeneIndex)(it.next()));
        return geneIndexes;
    }
    
    
    /** Get gene-disease association by gene_index_id
     *  @param geneIndexid  The gene Index ID
     *  @param statid       Statistics ID 
     *  @param number       How many diseases the user wants to obtain?
     *  @return             The vector of all associations betweeen this gene and all related diseases
     */
    public Vector getAssociationsByGeneIndex(int geneIndexid, int statid, int number) {

        DBManager manager = new DBManager();
        Connection conn = manager.connect();         
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
                double statScore = round_4(rs.getDouble(4));
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
    
    /** get disease by disease ID
     *@param id     disease ID
     *@return disease
     */
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
    
    /** get Statistics object by statistics id
     *  @param id   statistics id
     *  @return     Statistics
     */
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
    
    /** get GeneIndex by gene index id
     *  @param id   gene index id
     *  @return     GeneIndex
     */
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
    
    
    /** get GeneIndex by hip gene id
     *  @param id   gene index id
     *  @return     geneIndex
     
    public String queryGeneIndexByHipGeneId(int hip_gene_id) {
        DBManager manager = new DBManager();
        Connection conn = manager.connect();
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        Statement stmt = null;
        ResultSet rs = null;        
        String sql = "select gi.gene_index from gene_index gi, gene_list gl where gi.gene_index_id = gl.gene_index_id "+
                     " and gl.hip_gene_id = " + hip_gene_id;
        String geneIndex = "";
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if(rs.next()) {             
                String geneIndexString = rs.getString(1);
            }            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally{
            manager.disconnect(conn);
            return geneIndex;
        }
    }       
    */
    
    
    /** get MedlineRecords by disease id and gene index
     *  @param disease_id   disease id
     *  @param gene_index   gene index
     *  @return             The vector of Medline Records ID
     */
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
    
    /** get MedlineRecords by disease mesh term and gene symbol
     *  @param disease_mesh_term   disease mesh term
     *  @param gene_symbol         gene index
     *  @return                    The vector of Medline Records ID
     */
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
    
    /** get MedlineRecords by disease mesh term and child gene symbol if this gene is a child gene of a family
     *  @param disease_term         disease mesh term
     *  @param child_gene_name      child gene symbol
     *  @return                    The vector of Medline Records ID
     */
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
     
    /** get human gene index ids by non-human homolog
     *  @param non_hs_homolog   Non-human homolog id
     *  @param type             homolog input type (locus_id | unigene | accession)
     *  @return                 vector containing the homolog human gene index ids 
     *                          if no such human homolog, return null.
     */
    public Vector getHsGeneIndexesByHomolog(String non_hs_homolog, int type){
        
        int LOCUSID = 1;
        int UNIGENE = 2;
        int ACCESSION = 3;
        
        DBManager manager = new DBManager();
        Connection con = manager.connect();
        int gene_index_id = -1;         
        String gene_index = "";
        Vector hs_homolog_geneIndexes = new Vector();
        if (con == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }

        try
        {
        PreparedStatement pstmt = null;
        String sql = "";
        if(type == LOCUSID){
            sql = "select distinct gi.gene_index_id, gi.gene_index from Homolog_Mapping hm, Gene_List gl, Gene_Index gi " +
                  "where hm.homolog_locusid = ? and hm.h_locusid = gl.locus_id and gl.gene_index_id = gi.gene_index_id "; 
            pstmt = con.prepareStatement(sql);    
            pstmt.setInt(1, Integer.parseInt(non_hs_homolog));
        }
        else if(type == UNIGENE){
            sql = "select distinct gi.gene_index_id, gi.gene_index from Homolog_Mapping hm, Gene_List gl, Gene_Index gi " +
                  "where hm.homolog_unigene = ? and hm.h_locusid = gl.locus_id and gl.gene_index_id = gi.gene_index_id "; 
            pstmt = con.prepareStatement(sql);    
            pstmt.setInt(1, Integer.parseInt(non_hs_homolog));
        }            
        else if(type == ACCESSION){
            sql = "select distinct gi.gene_index_id, gi.gene_index from Homolog_Mapping hm, Gene_List gl, Gene_Index gi " +
                  "where hm.homolog_accession = ? and hm.h_locusid = gl.locus_id and gl.gene_index_id = gi.gene_index_id "; 
            pstmt = con.prepareStatement(sql);    
            pstmt.setString(1, non_hs_homolog);
        }            

        ResultSet rs = pstmt.executeQuery();        
        while(rs.next()){
            gene_index_id = rs.getInt(1);
            gene_index = rs.getString(2);
            hs_homolog_geneIndexes.add(new GeneIndex(gene_index_id, gene_index, null, null));
        }
        rs.close();
        pstmt.close();
        
        }catch(SQLException e){
            System.out.println(e);
        }
        
        if(gene_index_id == -1) 
            return null;
        else
            return hs_homolog_geneIndexes;
    }
    
    long start, end;
    public static void main(String[] args){
        DiseaseGeneManager m = new DiseaseGeneManager();
        //Vector hs_homologs = m.getHsGeneIndexesByHomolog("29634", 1);
        //Vector associations = m.getAssociationsByGeneIndex(((GeneIndex)(hs_homologs.elementAt(0))).getIndexid(), 1, 25);
        //for(int i=0; i<associations.size(); i++){
        //    System.out.println(((Association)(associations.elementAt(i))).getDisease().getTerm());
        //}
        m.start = System.currentTimeMillis();
        Vector v = m.getAssociationsByDisease(483, 1, 3000);
        System.out.println(v.size());
        m.end =  System.currentTimeMillis();
        System.out.println("done  " +  (m.end-m.start)/1000 + " sec");

    }
    
        
}
