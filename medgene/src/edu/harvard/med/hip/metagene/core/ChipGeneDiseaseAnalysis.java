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
int a=0; int b=0; int c=0; int d=0;
    public static final int GENE_SYMBOL_INPUT = 1;
    public static final int LOCUS_ID_INPUT = 2;
    
    // The direct_gene_hashmap contains all non-family genes.
    // It doesn't contain the gene families and their children.
    protected HashMap direct_gene_hashmap = new HashMap();
    
    // The direct children gene hashmap contains the children of gene families.
    // But it doesn't contain the gene families theirselves.
    protected HashMap direct_children_gene_hashmap = new HashMap();
    
    // The source for finding indirect genes. It contains all non-family genes index ids 
    // and their score with the disease. However, it doesn't include all family and children genes.
    protected HashMap source_for_indirect_genes = new HashMap();
    
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
    
    public HashMap getSource_for_indirect_genes(){
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
        
        // sql_124 selects all non-family genes
        String sql_124 = "select gl.gene_index_id, gl.symbol_value, gl.locus_id, sa.statistic_score " +
		        " from disease_and_gene_association dg, gene_index gi, "+
		        " association_data ad, statistic_analysis sa, gene_list gl "+
                        " where gl.gene_index_id=gi.gene_index_id " +
		        " and gi.gene_index_id=dg.gene_index_id "+       
		        " and dg.association_id = ad.association_id"+
		        " and ad.data_id = sa.data_id"+
		        " and sa.statistic_id = "+stat_id+
		        " and dg.hip_disease_id="+disease_id;

        // sql_3 selects all children gene of family genes but not the family genes themselves
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
                if(symbol_value != null) 
                    symbol_value = symbol_value.toUpperCase();
                else
                    symbol_value = "null";
                int locus_id = rs.getInt(3);
                double score = rs.getDouble(4);
                source_for_indirect_genes.put(new Integer(gene_index_id), new Double(score));     
                
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
                if(symbol_value != null) 
                    symbol_value = symbol_value.toUpperCase();
                else
                    symbol_value = "null";
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
   //System.out.println(" we have the number of children pairs      " + direct_children_gene_hashmap.size());

        
        
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
    
    
    ///////////////// Access2000 version //////////////
    /** select all indirectly associated genes from input genes 
     *  and put them into indirect gene hashmap. 
     *  However, the indirect hashmap may contains some genes, 
     *  which are already in direct gene hashmap.
     */
     
    public void hashIndirectGenes(String input_genes, HashMap source_for_indirect_genes,
                                      int input_type, int stat_id, int max_input){
        DBManager manager = new DBManager();
        Connection con = manager.connect();

        if (con == null) {
            System.out.println("Cannot connect to the database.");
            return;
        }
               
        String input = ""; 
        int k=0; // counter 
        StringTokenizer st = new StringTokenizer(input_genes);
        while(st.hasMoreTokens()){               
            if (k < max_input) {
                if(input_type == GENE_SYMBOL_INPUT)
                    input = input + "'" + st.nextToken() + "'" + ", ";            
                if(input_type == LOCUS_ID_INPUT)
                    input = input + st.nextToken() + ", ";               
                k++;
            }
            else
                break;           
        }        
        input += "''";
        
        String elements = "";        
        Iterator it = source_for_indirect_genes.keySet().iterator();
        k=0;
        while (it.hasNext()){
            elements = elements + ((Integer)(it.next())).intValue() + ", "; k++;
        }       
System.out.println("source for indirect genes hash : " + elements + "-----------------\n" + k);
        elements += "-1";
        
        String sql_1 = 
            "SELECT gl.symbol_value,  gl.locus_id, " + 
            "sa.statistic_score, gga.gene2_index_id " +
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
                "And sa.statistic_id= " + stat_id ;

        String sql_2 = 
            "SELECT gl.symbol_value,  gl.locus_id, " + 
            "sa.statistic_score, gga.gene1_index_id " +
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
            "And sa.statistic_id= " + stat_id ;       
        
        try{
            hashIndirectGenes_aux(con, sql_1, source_for_indirect_genes, input_type);
            hashIndirectGenes_aux(con, sql_2, source_for_indirect_genes, input_type);
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            manager.disconnect(con);
        }    

      //System.out.println("indirect hashmap size = " + indirect_gene_hashmap.size() + " --------");
        System.out.println("----------- indirect hash done ----------");
    }
    
    
    
    /**
     * auxiliary method for hashIndirectGenes(...)    
     */    
    protected void hashIndirectGenes_aux(Connection con, String sql, HashMap source_for_indirect_genes, int input_type)
        throws SQLException{
        
        Statement stmt = null;
        ResultSet rs = null;
        
        stmt = con.createStatement();
        rs = stmt.executeQuery(sql);
        while(rs.next()) {
                double score = rs.getDouble(3);           
                int link_gene_index_id = rs.getInt(4);              

                score = ((Double)(source_for_indirect_genes.get(new Integer(link_gene_index_id)))).doubleValue() + score;
                long temp =  (long)(score * 10000 + ( score > 0 ? .5 : -.5 )); 
                score = (double) temp / 10000;
       
                if(input_type == GENE_SYMBOL_INPUT){       
                    String gene_symbol = rs.getString(1); 
                    if(gene_symbol != null) 
                        gene_symbol = gene_symbol.toUpperCase();
                    else
                        gene_symbol = "null";                    
                    if(indirect_gene_hashmap.containsKey(gene_symbol)){                        
                        if( ((ChipGene)(indirect_gene_hashmap.get(gene_symbol))).getScore() < score )                         
                            ((ChipGene)(indirect_gene_hashmap.get(gene_symbol))).setScore(score);
                    }
                    else{
                        int locus_id = rs.getInt(2);                    
                        indirect_gene_hashmap.put(gene_symbol, new ChipGene(gene_symbol, locus_id, score));
                    }
                }
              
                if(input_type == LOCUS_ID_INPUT){    
                    int locus_id = rs.getInt(2); 
                    if(indirect_gene_hashmap.containsKey(new Integer(locus_id))){                        
                        if( ((ChipGene)(indirect_gene_hashmap.get(new Integer(locus_id)))).getScore() < score )
                            ((ChipGene)(indirect_gene_hashmap.get(new Integer(locus_id)))).setScore(score);
                    }
                    else{
                        String gene_symbol = rs.getString(1);                        
                        if(gene_symbol != null) 
                            gene_symbol = gene_symbol.toUpperCase();
                        else
                            gene_symbol = "null";                        
                        indirect_gene_hashmap.put(new Integer(locus_id), new ChipGene(gene_symbol, locus_id, score));
                    }
                }

        }
        rs.close();
        stmt.close();
    }
    
    
    //////////////////////// Oracle version ///////////////////////////
    
    public void hashIndirectGenes_00(String input_genes, HashMap source_for_indirect_genes,
                                      int input_type, int stat_id, int max_input){
                                          
        DBManager manager = new DBManager();
        Connection con = manager.connect();

        if (con == null) {
            System.out.println("Cannot connect to the database.");
            return;
        }
        if(input_genes.trim().length() == 0)
            return;
        if(source_for_indirect_genes.size() == 0)
            return;
                            
        // since the max length of oracle8i inlist statement is 1000, 
        // the input_genes has to be put into an array, each array element contains at most 1000 input genes.
        StringTokenizer st = new StringTokenizer(input_genes);        
        int input_genes_number = (st.countTokens() < max_input) ? st.countTokens() : max_input;
        int input_array_size = (input_genes_number - 1)/1000 + 1;
        String[] input = new String[input_array_size];
        for(int i = 0; i < input_array_size; i++){
            input[i] = "";
            for(int j = 0; j < 1000; j++){
                if(st.hasMoreTokens()){     
                    if(input_type == GENE_SYMBOL_INPUT)
                        input[i] = input[i] + "'" + st.nextToken() + "'" + ", ";            
                    if(input_type == LOCUS_ID_INPUT)
                        input[i] = input[i] + st.nextToken() + ", ";               
                }
                else
                    break;
            }
            input[i] = input[i].substring(0, input[i].length()-2);
        }                
  
        // since the max length of oracle8i inlist statement is 1000, 
        // the source_for_indirect_genes has to be put into an array, each array element contains at most 1000 source genes.        
        int elements_array_size = (source_for_indirect_genes.size()-1)/1000 + 1;        
        String[] elements = new String[elements_array_size];
        for(int i = 0; i < elements_array_size; i++)
            elements[i] = "";                                
        Iterator it = source_for_indirect_genes.keySet().iterator();
        int k=-1;
        while (it.hasNext()){
            k++;
            elements[k/1000] = elements[k/1000] + ((Integer)(it.next())).intValue() + ", "; 
        }       
        for(int i = 0; i < elements_array_size; i++)
            elements[i] = elements[i].substring(0, elements[i].length()-2);        

        // build sql
        String[][] sql_1 = new String[input_array_size][elements_array_size];
        String[][] sql_2 = new String[input_array_size][elements_array_size];
        for(int i = 0; i < input_array_size; i++){
            for(int j = 0; j < elements_array_size; j++){
                sql_1[i][j] =  
                "select gl.symbol_value, gl.locus_id, s.statistic_score, s.gene2_index_id " +
                "from gene_list gl, " + 
                "     (select sa.statistic_score, t.gene1_index_id, t.gene2_index_id " +                                // alias s
                "           from statistic_analysis sa, association_data ad, " +
                "                (SELECT /*+ rule */ gga.association_id, gga.gene1_index_id, gga.gene2_index_id " +     // alias t
                "                        FROM gene_and_gene_association gga WHERE gga.gene1_index_id in ";
        
                if(input_type == GENE_SYMBOL_INPUT)
                    sql_1[i][j] += "(select gene_index_id from gene_list where symbol_value in ( " + input[i] + " )) ";
                if(input_type == LOCUS_ID_INPUT)
                    sql_1[i][j] += "(select gene_index_id from gene_list where locus_id in ( " + input[i] + " )) ";
                
                sql_1[i][j] += 
                    "and gga.gene2_index_id in ( " + elements[j] + " )) t " +
                    "where sa.data_id = ad.data_id and ad.association_id = t.association_id and sa.statistic_id = " + stat_id + " " +
                    ") s where gl.gene_index_id = s.gene1_index_id ";
            }
        }
        
        for(int i = 0; i < input_array_size; i++){
            for(int j = 0; j < elements_array_size; j++){
                sql_2[i][j] =  
                "select gl.symbol_value, gl.locus_id, s.statistic_score, s.gene1_index_id " +
                "from gene_list gl, " + 
                "     (select sa.statistic_score, t.gene1_index_id, t.gene2_index_id " +                                // alias s
                "           from statistic_analysis sa, association_data ad, " +
                "                (SELECT /*+ rule */ gga.association_id, gga.gene1_index_id, gga.gene2_index_id " +     // alias t
                "                        FROM gene_and_gene_association gga WHERE gga.gene2_index_id in ";
        
                if(input_type == GENE_SYMBOL_INPUT)
                    sql_2[i][j] += "(select gene_index_id from gene_list where symbol_value in ( " + input[i] + " )) ";
                if(input_type == LOCUS_ID_INPUT)
                    sql_2[i][j] += "(select gene_index_id from gene_list where locus_id in ( " + input[i] + " )) ";
                
                sql_2[i][j] += 
                    "and gga.gene1_index_id in ( " + elements[j] + " )) t " +
                    "where sa.data_id = ad.data_id and ad.association_id = t.association_id and sa.statistic_id = " + stat_id + " " +
                    ") s where gl.gene_index_id = s.gene2_index_id ";
            }
        }
                    
        try{
            for(int i = 0; i < input_array_size; i++){
                for(int j = 0; j < elements_array_size; j++){
                    hashIndirectGenes_aux(con, sql_1[i][j], source_for_indirect_genes, input_type);
                    hashIndirectGenes_aux(con, sql_2[i][j], source_for_indirect_genes, input_type);                    
                }
            }
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            manager.disconnect(con);
        }    

      //System.out.println("indirect hashmap size = " + indirect_gene_hashmap.size() + " --------");
        System.out.println("----------- indirect hash done ----------");            

    }


    /** parse the text file of chip genes, 
     *  construct the ChipGene objects and put them
     *  into TreeSet data structures according to 
     *  their relationship to the disease
     */
    public void analyzeInputChipGenes(String input_genes, int input_type, int max_input){
        
        int k = 0;
        StringTokenizer st = new StringTokenizer(input_genes);
        if(input_type == GENE_SYMBOL_INPUT){
            while(st.hasMoreTokens()){
                if(k < max_input){ 
                    classify(st.nextToken().toUpperCase());
                    k++;
                }
                else
                    break;
            }
        }
        if(input_type == LOCUS_ID_INPUT){
            while(st.hasMoreTokens()){
                if(k < max_input){
                    classify(Integer.parseInt(st.nextToken()));
                    k++;
                }
                else
                    break;
            }
        }
        //System.out.println("total processed input size = " + k + " ------------------///");
    }
    
    
    public void classify(String gene_symbol){
           
        double score;

        if(direct_gene_hashmap.containsKey(gene_symbol)){a++;
            direct_gene_tree.add((ChipGene)(direct_gene_hashmap.get(gene_symbol))); 
            if(direct_children_gene_hashmap.containsKey(gene_symbol)){b++;
                direct_children_gene_tree.add((ChipGene)(direct_children_gene_hashmap.get(gene_symbol)));        
            }
        }
      
        else if(direct_children_gene_hashmap.containsKey(gene_symbol)){b++;
            direct_children_gene_tree.add((ChipGene)(direct_children_gene_hashmap.get(gene_symbol)));        
        }
                               
        else if(indirect_gene_hashmap.containsKey(gene_symbol)){c++;
            indirect_gene_tree.add((ChipGene)(indirect_gene_hashmap.get(gene_symbol)));                             
        }

        else{d++;
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
        
        else if(direct_children_gene_hashmap.containsKey(new Integer(locus_id))){
            direct_children_gene_tree.add((ChipGene)(direct_children_gene_hashmap.get(new Integer(locus_id))));        
        }                
                
        else if(indirect_gene_hashmap.containsKey(new Integer(locus_id))){
             indirect_gene_tree.add((ChipGene)(indirect_gene_hashmap.get(new Integer(locus_id))));     
        }
                
        else{
            new_gene_tree.add(new ChipGene(" ", locus_id, 0.0));
        }

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
    
    //////////////////////////////////////// test ////////////////////////////////////////////////
    
    public void join(TreeSet tree1, TreeSet tree2){
        TreeSet tree = new TreeSet();
        Iterator t = tree1.iterator();        
        while (t.hasNext()){
            tree.add( ((ChipGene)(t.next())).getGene_symbol() );                        
        }        
        Iterator tt = tree2.iterator();        
        while (tt.hasNext()){
            tree.add( ((ChipGene)(tt.next())).getGene_symbol() );                        
        }  
        System.out.println("direct tree + children direct tree = " + tree.size());
    }
    
    public static void main(String[] args){

        System.out.println("----- start -----");
        String text = "";
/*
        text =
        "13CDNA73\n 6H9A\n AADAC\n AARS\n AASDHPPT\n ABCA12\n ABCA2\n ABCA4\n ABCA5\n" + 
        "ABCA6\n ABCA8\n ABCB1\n ABCB11\n ABCB6\n ABCC1\n ABCC2\n ABCC5\n ABCC5\n" + 
        "ABCC9\n ABCC9\n ABCD2\n ABCD3\n ABCE1\n" +
        "TNF GP2 CD14\n NUDT6\n HHHH\n ESR1\n ESR2\n LRPAP1\n PPP2R3\n PPBP\n";             
   
        text = "10517"; */
       
        try{
         
        BufferedReader in = new BufferedReader(new FileReader("c:\\data\\all_symbol.txt"));       
        
        String s;
        
        while((s=in.readLine()) != null){
            text = text + s + " ";    
        }
        //System.out.println(text); 
        
        }catch(Exception e){
            System.out.println(e);
        }
        
        text=text.toUpperCase(); // only use for access database
        

         
//////////////////////////////////
                                                                                        
        ChipGeneDiseaseAnalysis ana = new ChipGeneDiseaseAnalysis();  
        ana.hashDirectGenes(483, 1, 1);  //402 483 482          (disease_id, statistic_id, input_type)
        
System.out.println("source for indirect gene hash = " + ana.source_for_indirect_genes.size());
        ana.hashIndirectGenes(text, ana.getSource_for_indirect_genes(), 1, 1, 10000);       //(input, source, input_type, statistic_id, max)
        ana.analyzeInputChipGenes(text, 1, 10000);          //(input, input_type, max)
        
System.out.println("a = " + ana.a + "    b = " + ana.b + "    c = " + ana.c + "    d =" + ana.d + "^^^^^^^^^^^");        
        TreeSet direct = ana.getDirect_gene_tree();        
        System.out.println("direct tree:     " + direct.size());
        TreeSet children_direct = ana.getDirect_children_gene_tree();
        System.out.println("children_direct tree:     " + children_direct.size());
        
        ana.join(direct, children_direct);
        
        
        TreeSet indirect = ana.getIndirect_gene_tree();
        System.out.println("indirect tree:     " + indirect.size());       
        TreeSet new_genes = ana.getNew_gene_tree();
        System.out.println("new tree:     " + new_genes.size());   
        
        
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

