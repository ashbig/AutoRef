/*
 * ChipGeneDiseaseAnalysis.java
 *
 * Created on May 7, 2002, 2:42 PM
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
public class ChipGeneDiseaseAnalysis {
    
    public static final int GENE_SYMBOL_INPUT = 1;
    public static final int LOCUS_ID_INPUT = 2;
    
    // The direct_gene_hashmap contains all non-family genes.
    // It doesn't contain the gene families and their children.
    protected HashMap direct_gene_hashmap = new HashMap();
    
    // The direct children gene hashmap contains the children of gene families.
    // But it doesn't contain the gene families theirselves.
    protected HashMap direct_children_gene_hashmap = new HashMap();
    
    // The source for finding indirect genes. It contains all non-family genes index ids plus
    // family gene index ids. However, it doesn't include all children genes from the families.
    protected Vector source_for_indirect_genes = new Vector();
    
    // The indirect_gene_hashmap contains all indirectly disease-associated genes 
    // from the input chip genes. 
    protected HashMap indirect_gene_hashmap = new HashMap();
    
    // direct gene tree contains all directly disease-associated genes
    protected TreeSet direct_gene_tree = new TreeSet(new GeneComparator());
    
    // direct children gene treee contains all directly disease-associated genes by family term
    protected TreeSet direct_children_gene_tree = new TreeSet(new GeneComparator());

    // indirect gene tree contains all indirectly disease-associated genes
    protected TreeSet indirect_gene_tree = new TreeSet (new GeneComparator());
    
    // new gene tree contains all non-associated genes
    protected TreeSet new_gene_tree = new TreeSet(new GeneComparator());

    
public long start, end2;
    

    public ChipGeneDiseaseAnalysis(){       
        start = System.currentTimeMillis();      
    }
    
    public Vector getSource_for_indirect_genes(){
        return source_for_indirect_genes;
    }    
    public TreeSet getDirect_gene_tree(){
        return direct_gene_tree;
    }    
    public TreeSet getDirect_children_gene_tree(){
        return direct_children_gene_tree;
    }
    public TreeSet getIndirect_gene_tree(){
        return indirect_gene_tree;
    }    
    public TreeSet getNew_gene_tree(){
        return new_gene_tree;
    }    
   
    
    
    /*******************************************************************************/
    
    /** Get directly associated genes for this disease and put them in hashmap. 
     *  Also generate the vector of source_for_indirect_genes.
     */
    public void hashDirectGenes(int disease_id, int stat_id, int input_type) {
       
        DBManager manager = new DBManager();
        Connection con = manager.connect();

        if (con == null) {
            System.out.println("Cannot connect to the database.");
            return;
        }
        
        String sql_124 = "select gl.gene_index_id, gl.symbol_value, gl.locus_id, sa.statistic_score " +
		        " from disease_and_gene_association dg, gene_index gi, "+
		        " association_data ad, statistic_analysis sa, gene_list gl "+
                        " where gl.gene_index_id=gi.gene_index_id " +
		        " and gi.gene_index_id=dg.gene_index_id "+       
		        " and dg.association_id = ad.association_id"+
		        " and ad.data_id = sa.data_id"+
		        " and sa.statistic_id = "+stat_id+
		        " and dg.hip_disease_id="+disease_id;

        String sql_3 = "SELECT gl.gene_index_id, gl.symbol_value, gl.locus_id, sa.statistic_score " +
                      " FROM disease_and_gene_association dg, gene_index gi, " +
                      " association_data ad, statistic_analysis sa, parent_list pl, " +
                      " family_mapping fm, gene_list gl " +
                      " WHERE gl.hip_gene_id=fm.hip_gene_id and fm.parent_id=pl.parent_id " +
                      " and pl.parent_value=gi.gene_index and gi.gene_index_id=dg.gene_index_id " +
                      " and dg.association_id = ad.association_id and ad.data_id = sa.data_id " +
                      " and sa.statistic_id = " + stat_id + 
                      " and dg.hip_disease_id=" + disease_id;

        Statement stmt = null;
        ResultSet rs = null;             

        try {
            stmt = con.createStatement();
            
            // put all non-family genes into hashmap
            rs = stmt.executeQuery(sql_124);
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
            
            
            // put all children from family genes into hashmap
            rs = stmt.executeQuery(sql_3);
            while(rs.next()) {
                int gene_index_id = rs.getInt(1);
                String symbol_value = rs.getString(2);
                int locus_id = rs.getInt(3);
                double score = rs.getDouble(4);    
                
                if(input_type == GENE_SYMBOL_INPUT){      
                    if(direct_children_gene_hashmap.containsKey(symbol_value)){
                        if( ((ChipGene)(direct_children_gene_hashmap.get(symbol_value))).getScore() < score )
                            ((ChipGene)(direct_children_gene_hashmap.get(symbol_value))).setScore(score);
                    }
                    else{
                        direct_children_gene_hashmap.put(symbol_value, new ChipGene(symbol_value, locus_id, score));
                    }
                }
                
                if(input_type == LOCUS_ID_INPUT){
                    Integer key = new Integer(locus_id);
                    if(direct_children_gene_hashmap.containsKey(key)){
                        if( ((ChipGene)(direct_children_gene_hashmap.get(key))).getScore() < score )
                            ((ChipGene)(direct_children_gene_hashmap.get(key))).setScore(score);
                    }
                    else{
                        direct_children_gene_hashmap.put(key, new ChipGene(symbol_value, locus_id, score));
                    }
                }
            }
            rs.close();
            stmt.close();
            
        } catch (SQLException ex) {
            System.out.println(ex);                                     
        } finally{
            manager.disconnect(con);
        }
        
        
   System.out.println("************ hash direct genes done ************");
   System.out.println(" we have the number of non-family pairs    " + direct_gene_hashmap.size());     
   System.out.println(" we have the number of children pairs      " + direct_children_gene_hashmap.size());

        
        
    }
    
        
    /** get Children of a gene family */    
    public Vector getChildren(Connection con, String gene_index){
        
        String sql = " select gl.gene_index_id " +
	      	     " from parent_list pl, family_mapping fm, gene_list gl " +
	      	     " where pl.parent_id = fm.parent_id " +
      		     " and fm.hip_gene_id = gl.hip_gene_id " +
		     " and pl.parent_value = ?";
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector children = new Vector();
        
        try{
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, gene_index);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                int gene_index_id = rs.getInt(1);
                children.addElement(new Integer(gene_index_id));
            }
            rs.close();
            pstmt.close();
        }catch(SQLException ex){
            System.out.println(ex);
        }
        
        return children;
    }
    
    
    /** select all indirectly associated genes from input genes 
     *  and put them into indirect gene hashmap. 
     *  However, the indirect hashmap may contains some genes, 
     *  which are already in direct gene hashmap.
     */
    
    public void hashIndirectGenes(String input_genes, Vector source_for_indirect_genes,
                                      int input_type, int stat_id){
        DBManager manager = new DBManager();
        Connection con = manager.connect();

        if (con == null) {
            System.out.println("Cannot connect to the database.");
            return;
        }
               
        String input = ""; 
        int k=0;
        StringTokenizer st = new StringTokenizer(input_genes);
        while(st.hasMoreTokens()){               
            if (k < 2000) {
                if(input_type == GENE_SYMBOL_INPUT)
                    input = input + "'" + st.nextToken() + "'" + ", ";            
                if(input_type == LOCUS_ID_INPUT)
                    input = input + st.nextToken() + ", ";               
                k++;
            }
            else
                break;           
        }
        
        
        //System.out.println("--------------------------  " + k);
        
        String elements = "";
        for (int i = 0; i<source_for_indirect_genes.size(); i++){
            elements = elements + ((Integer)(source_for_indirect_genes.elementAt(i))).intValue() + ", ";
        }
        
        
        //System.out.println(input_genes);
        
        String sql_1 = 
            "SELECT gl.symbol_value,  gl.locus_id, " + 
            "gl.gene_index_id, max(sa.statistic_score) " +
            "FROM gene_and_gene_association gga, association_data ad, " +
            "statistic_analysis sa, gene_index gi, gene_list gl " ;
        
        if(input_type == GENE_SYMBOL_INPUT)
            sql_1 += "WHERE gl.symbol_value in ( " + input + " ) ";
        if(input_type == LOCUS_ID_INPUT)
            sql_1 += "where gl.locus_id in ( " + input + " ) ";
               
        sql_1 = sql_1 +                      
                "and gl.gene_index_id = gi.gene_index_id and gi.gene_index_id = gga.gene1_index_id " + 
                "and gga.gene2_index_id in (" + elements + " ) " +  
                "and gga.association_id=ad.association_id And ad.data_id=sa.data_id " +
                "And sa.statistic_id= " + stat_id + " " + 
                "group by gl.symbol_value, gl.locus_id, gl.gene_index_id";

        String sql_2 = 
            "SELECT gl.symbol_value,  gl.locus_id, " + 
            "gl.gene_index_id, max(sa.statistic_score) " +
            "FROM gene_and_gene_association gga, association_data ad, " +
            "statistic_analysis sa, gene_index gi, gene_list gl ";
        
        if(input_type == GENE_SYMBOL_INPUT)
            sql_2 += "WHERE gl.symbol_value in ( " + input + " ) ";
        if(input_type == LOCUS_ID_INPUT)
            sql_2 += "where gl.locus_id in ( " + input + " ) ";        
        
        sql_2 = sql_2 +                    
            "and gl.gene_index_id = gi.gene_index_id and gi.gene_index_id = gga.gene2_index_id " + 
            "and gga.gene1_index_id in (" + elements + " ) " +  
            "and gga.association_id=ad.association_id And ad.data_id=sa.data_id " +
            "And sa.statistic_id= " + stat_id + " " +
            "group by gl.symbol_value, gl.locus_id, gl.gene_index_id";       
                
        Statement stmt = null;
        ResultSet rs = null;
         
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql_1);
            while(rs.next()) {
                String gene_symbol = rs.getString(1);
                int locus_id = rs.getInt(2);                
                double score = rs.getDouble(4);             
                if(input_type == GENE_SYMBOL_INPUT)
                    indirect_gene_hashmap.put(gene_symbol, new ChipGene(gene_symbol, locus_id, score));
                if(input_type == LOCUS_ID_INPUT)
                    indirect_gene_hashmap.put(new Integer(locus_id), new ChipGene(gene_symbol, locus_id, score));
            
            }
            rs.close();
            
            rs = stmt.executeQuery(sql_2);
            while(rs.next()){
                String gene_symbol = rs.getString(1);
                int locus_id = rs.getInt(2);                
                double score = rs.getDouble(4);     
                if(input_type == GENE_SYMBOL_INPUT){                    
                    if(indirect_gene_hashmap.containsKey(gene_symbol)){
                        if( ((ChipGene)(indirect_gene_hashmap.get(gene_symbol))).getScore() < score )
                            ((ChipGene)(indirect_gene_hashmap.get(gene_symbol))).setScore(score);
                    }
                    else{
                        indirect_gene_hashmap.put(gene_symbol, new ChipGene(gene_symbol, locus_id, score));
                    }
                }
                if(input_type == LOCUS_ID_INPUT){                    
                    if(indirect_gene_hashmap.containsKey(new Integer(locus_id))){
                        if( ((ChipGene)(indirect_gene_hashmap.get(new Integer(locus_id)))).getScore() < score )
                            ((ChipGene)(indirect_gene_hashmap.get(new Integer(locus_id)))).setScore(score);
                    }
                    else{
                        indirect_gene_hashmap.put(new Integer(locus_id), new ChipGene(gene_symbol, locus_id, score));
                    }
                }
            }
            rs.close();
             
            stmt.close();
        }catch(SQLException e){
            System.out.println(e);
        } finally{
            manager.disconnect(con);
        }
                        System.out.println("----------- indirect hash done ----------");
    }
        
    /** parse the text file of chip genes, 
     *  construct the ChipGene objects and put them
     *  into TreeSet data structures according to 
     *  their relationship to the disease
     */
    public void analyzeInputChipGenes(String input_genes, int input_type){
        
        int k = 0;
        StringTokenizer st = new StringTokenizer(input_genes);
        if(input_type == GENE_SYMBOL_INPUT){
            while(st.hasMoreTokens()){
                if(k < 2000){ 
                    classify(st.nextToken());
                    k++;
                }
            }
        }
        if(input_type == LOCUS_ID_INPUT){
            while(st.hasMoreTokens()){
                if(k < 2000){
                    classify(Integer.parseInt(st.nextToken()));
                    k++;
                }
            }
        }
        
    }
    
    
    public void classify(String gene_symbol){
           
        double score;

        if(direct_gene_hashmap.containsKey(gene_symbol)){
            direct_gene_tree.add((ChipGene)(direct_gene_hashmap.get(gene_symbol))); 
            if(direct_children_gene_hashmap.containsKey(gene_symbol)){
                direct_children_gene_tree.add((ChipGene)(direct_children_gene_hashmap.get(gene_symbol)));        
            }
        }
        
        else if(direct_children_gene_hashmap.containsKey(gene_symbol)){
            direct_children_gene_tree.add((ChipGene)(direct_children_gene_hashmap.get(gene_symbol)));        
        }
                               
        else if(indirect_gene_hashmap.containsKey(gene_symbol)){
            indirect_gene_tree.add((ChipGene)(indirect_gene_hashmap.get(gene_symbol)));                             
        }

        else{
            new_gene_tree.add(new ChipGene(gene_symbol, 0, 0.0));           
        }
                          
 
    }
        
    public void classify(int locus_id){

        double score;

        if(direct_gene_hashmap.containsKey(new Integer(locus_id))){
            direct_gene_tree.add((ChipGene)(direct_gene_hashmap.get(new Integer(locus_id)))); 
            if(direct_children_gene_hashmap.containsKey(new Integer(locus_id))){
                direct_children_gene_tree.add((ChipGene)(direct_children_gene_hashmap.get(new Integer(locus_id))));        
            }
        }
        
        if(direct_children_gene_hashmap.containsKey(new Integer(locus_id))){
            direct_children_gene_tree.add((ChipGene)(direct_children_gene_hashmap.get(new Integer(locus_id))));        
        }                
                
        else if(indirect_gene_hashmap.containsKey(new Integer(locus_id))){
             indirect_gene_tree.add((ChipGene)(indirect_gene_hashmap.get(new Integer(locus_id))));     
        }
                
        else
            new_gene_tree.add(new ChipGene(" ", locus_id, 0.0));

    }
    
    
    public String getDiseaseMeshTerm(int disease_id){
 
        DBManager manager = new DBManager();
        Connection con = manager.connect();
        
        if (con == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        String mesh_term = "";
        String sql = "select disease_mesh_term from disease_list where hip_disease_id=" + disease_id;

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);            
            if(rs.next()) {
                mesh_term = rs.getString(1);
            }            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally{
            manager.disconnect(con);       
        }
        return mesh_term;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////
    
    
    public static void main(String[] args){

        //String text = "";

        String text =
        "13CDNA73\n 6H9A\n AADAC\n AARS\n AASDHPPT\n ABCA12\n ABCA2\n ABCA4\n ABCA5\n" + 
        "ABCA6\n ABCA8\n ABCB1\n ABCB11\n ABCB6\n ABCC1\n ABCC2\n ABCC5\n ABCC5\n" + 
        "ABCC9\n ABCC9\n ABCD2\n ABCD3\n ABCE1\n" +
        "TNF GP2 CD14\n NUDT6\n HHHH\n ESR1\n ESR2\n";             
        
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
                                                                                        
        ChipGeneDiseaseAnalysis ana = new ChipGeneDiseaseAnalysis();  
        ana.hashDirectGenes(483, 1, ana.GENE_SYMBOL_INPUT);  //402 483
        ana.hashIndirectGenes(text, ana.source_for_indirect_genes, ana.GENE_SYMBOL_INPUT, 1);       
        ana.analyzeInputChipGenes(text, ana.GENE_SYMBOL_INPUT);
        
        
        TreeSet direct = ana.getDirect_gene_tree();        
        System.out.println("direct tree:     " + direct.size());
        TreeSet children_direct = ana.getDirect_children_gene_tree();
        System.out.println("children_direct tree:     " + children_direct.size());
        TreeSet indirect = ana.getIndirect_gene_tree();
        System.out.println("indirect tree:     " + indirect.size());       
        TreeSet new_genes = ana.getNew_gene_tree();
        
       
        
        Iterator it = direct.iterator();
        System.out.println("direct genes -------------------------------------");
        while (it.hasNext()){
            ChipGene g = (ChipGene)(it.next());
            System.out.println( g.getGene_symbol() + "              " + g.getScore() );
        }
        
        Iterator t = children_direct.iterator();
        System.out.println("direct children genes -------------------------------------");
        while (t.hasNext()){
            ChipGene g = (ChipGene)(t.next());
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

