/*
 * NonHsChipGeneDiseaseAnallysis.java
 *
 * Created on June 27, 2002, 10:40 AM
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

public class NonHsChipGeneDiseaseAnalysis extends ChipGeneDiseaseAnalysis{
    
    public static final int LOCUSID = 1;
    public static final int UNIGENE = 2;
    public static final int ACCESSION = 3;

    // direct gene tree contains all directly disease-associated genes
    protected TreeSet direct_gene_tree = new TreeSet(new HomologChipGeneComparator());
    
    // direct children gene treee contains all directly disease-associated genes by family term
    protected TreeSet direct_children_gene_tree = new TreeSet(new HomologChipGeneComparator());

    // indirect gene tree contains all indirectly disease-associated genes
    protected TreeSet indirect_gene_tree = new TreeSet (new HomologChipGeneComparator());
    
    // new gene tree contains all non-associated genes
    protected TreeSet new_gene_tree = new TreeSet(new HomologChipGeneComparator());
   
    // no homolog tree contains the non-human genes which has no human homolog
    protected TreeSet no_homolog_tree = new TreeSet();
        
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
    public TreeSet getNo_homolog_tree(){
        return no_homolog_tree;
    }
             
    
    /** map a human homolog locusID to a non-human homolog
     *  @param non_hs_homologs  non human homolog input
     *  @param type             input type(locusID, unigene or accession)
     */
    public HashMap hashHomolog(String non_hs_homologs, int type){
                
        DBManager manager = new DBManager();
        Connection con = manager.connect();
         
        if (con == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        HashMap homolog_mapping = new HashMap();        
        String sql = "";
        if(type == LOCUSID)
            sql = "select distinct h_locusID from Homolog_Mapping where homolog_locusid = ?"; 
        else if(type == UNIGENE)
            sql = "select distinct h_locusID from Homolog_Mapping where homolog_unigene = ?"; 
        else if(type == ACCESSION)
            sql = "select distinct h_locusID from Homolog_Mapping where homolog_accession = ?";
        
        try
        {
        
        PreparedStatement pstmt = con.prepareStatement(sql);        
        StringTokenizer st = new StringTokenizer(non_hs_homologs);
        while(st.hasMoreTokens()){
            String non_hs_homolog = st.nextToken();
            if(type == LOCUSID || type == UNIGENE)
                pstmt.setInt(1, Integer.parseInt(non_hs_homolog));
            else if(type == ACCESSION)
                pstmt.setString(1, non_hs_homolog);
            ResultSet rs = pstmt.executeQuery();
            int h_locusID = -1;
            Vector human_homologs = new Vector();   // one non-human gene may have more than one human homologs
            while(rs.next()){
                h_locusID = rs.getInt(1);               
                human_homologs.add(new Integer(h_locusID));
            }
            rs.close();
            if(h_locusID != -1)
                homolog_mapping.put(non_hs_homolog, human_homologs);
        }
        pstmt.close();
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            manager.disconnect(con);
        }
        return homolog_mapping;
    }
    

    
    /** generate corresponding human homolog input based on the homolog mapping
     *  @param mapping  homolog mapping hashmap
     */
    public String toHsHomologInput(HashMap mapping){
        Iterator it = mapping.keySet().iterator();
        String input = "";
        while(it.hasNext()){
            Vector human_homologs = (Vector)(mapping.get(it.next()));
            for(int i=0; i<human_homologs.size(); i++)
                input += ((Integer)(human_homologs.elementAt(i))).toString() + " ";
        }
        return input;
    }
    
    
    /** parse the text file of input genes, construct the ChipGene objects and put them
     *  into TreeSet data structures according to their relationship to the disease
     *  @param input_genes  non-human input genes
     *  @param homolog_mapping homolog mapping hashmap
     *  @param max_input    the max size of gene input
     */
    
    public void analyzeInputChipGenes(String input_genes, HashMap homolog_mapping, int max_input){
        
        int k = 0;
        StringTokenizer st = new StringTokenizer(input_genes);
        while(st.hasMoreTokens()){
            if(k < max_input){ 
                classify(st.nextToken(), homolog_mapping);
                k++;
            }
            else
                break;
        }        
    }
    

    /** classify each gene in the input list and put it in the certain tree data structure
     *  @param id   non-human gene identifier(locusID, Unigene or Accession)
     *  @param mapping  homolog mapping hashmap
     */        
    public void classify(String id, HashMap mapping){

        Object hs_locus_ids = mapping.get(id);
        Object hs_locus_id;
        ChipGene g;
        
        if(hs_locus_ids == null){
            no_homolog_tree.add(id);
        }
        else{
          Vector v = (Vector)(hs_locus_ids);
          for(int i = 0; i < v.size(); i++){
            hs_locus_id = v.elementAt(i);  
            if(direct_gene_hashmap.containsKey(hs_locus_id)){
                g = (ChipGene)(direct_gene_hashmap.get(hs_locus_id));
                direct_gene_tree.add(new HomologChipGene(g.getGene_symbol(), g.getLocus_id(), g.getScore(), id));                                                 
                if(direct_children_gene_hashmap.containsKey(hs_locus_id)){
                    g = (ChipGene)(direct_children_gene_hashmap.get(hs_locus_id));
                    direct_children_gene_tree.add(new HomologChipGene(g.getGene_symbol(), g.getLocus_id(), g.getScore(), id)); 
                }
            }
            
            else if(direct_children_gene_hashmap.containsKey(hs_locus_id)){
                g = (ChipGene)(direct_children_gene_hashmap.get(hs_locus_id));
                direct_children_gene_tree.add(new HomologChipGene(g.getGene_symbol(), g.getLocus_id(), g.getScore(), id));            
            }                
                
            else if(indirect_gene_hashmap.containsKey(hs_locus_id)){
                g = (ChipGene)(indirect_gene_hashmap.get(hs_locus_id));
                indirect_gene_tree.add(new HomologChipGene(g.getGene_symbol(), g.getLocus_id(), g.getScore(), id));     
            }
                
            else{               
                new_gene_tree.add(new HomologChipGene(" ", ((Integer)hs_locus_id).intValue(), 0.0, id));
            }
          }
             
        }

    }
    
    ///////////////////////8888888888888888888888888888888888888888888888
    
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
                    String gene_symbol = rs.getString(1).toUpperCase();     
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
                        String gene_symbol = rs.getString(1).toUpperCase();                        
                        indirect_gene_hashmap.put(new Integer(locus_id), new ChipGene(gene_symbol, locus_id, score));
                    }
                }

        }
        rs.close();
        stmt.close();
    }
    
    
    //////////////////////// Oracle version ///////////////////////////
    
    public void hashIndirectGenes(String input_genes, HashMap source_for_indirect_genes,
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
                    sql_1[i][j] += "(select gene_index_id from gene_list where symbol_uppercase in ( " + input[i] + " )) ";
                if(input_type == LOCUS_ID_INPUT)
                    sql_1[i][j] += "(select gene_index_id from gene_list where gl.locus_id in ( " + input[i] + " )) ";
                
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
                    sql_2[i][j] += "(select gene_index_id from gene_list where symbol_uppercase in ( " + input[i] + " )) ";
                if(input_type == LOCUS_ID_INPUT)
                    sql_2[i][j] += "(select gene_index_id from gene_list where gl.locus_id in ( " + input[i] + " )) ";
                
                sql_2[i][j] += 
                    "and gga.gene1_index_id in ( " + elements[j] + " )) t " +
                    "where sa.data_id = ad.data_id and ad.association_id = t.association_id and sa.statistic_id = " + stat_id + " " +
                    ") s where gl.gene_index_id = s.gene2_index_id ";
            }
        }
                    
        try{
            for(int i = 0; i < input_array_size; i++){
                for(int j = 0; j < elements_array_size; j++){//System.out.println("bbbbbbbbbbbbbb");
                    hashIndirectGenes_aux(con, sql_1[i][j], source_for_indirect_genes, input_type);//System.out.println("aaaaaaaaaaaaaa");
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
    
    
    //////////////////////////////////////////// test ////////////////////////////////////////////////////////////
    
    public static void main(String[] args){
        String text ="";
        try{         
        BufferedReader in = new BufferedReader(new FileReader("c:\\temp\\rat_locusid.txt"));               
        String s;
        
        while((s=in.readLine()) != null){
            text = text + s + " ";    
        }
     
        }catch(Exception e){
            System.out.println(e);
        }
        
        //long start = System.currentTimeMillis();
        String non_hs_locusIDs = text;
        NonHsChipGeneDiseaseAnalysis ana = new NonHsChipGeneDiseaseAnalysis();
        HashMap homolog = ana.hashHomolog(non_hs_locusIDs, 1);        
        System.out.println("------ homolog size: " + homolog.size());
        //long end = System.currentTimeMillis();
        //System.out.println("time cost " + (end - start)/1000);
        /*
        Iterator it = homolog.keySet().iterator();
        while (it.hasNext()){
            Object o = it.next();
            System.out.println(o.toString() + " ------ " + ((Integer)(homolog.get(o))).intValue() );
        }
         **/
        
        
        ana.hashDirectGenes(2031, 1, 2);            
        ana.hashIndirectGenes(ana.toHsHomologInput(homolog), ana.getSource_for_indirect_genes(), 2, 1, 4000);
        ana.analyzeInputChipGenes(text, homolog, 4000);
        
        TreeSet direct = ana.getDirect_gene_tree();        
        System.out.println("direct tree:     " + direct.size());
        TreeSet children_direct = ana.getDirect_children_gene_tree();
        System.out.println("children_direct tree:     " + children_direct.size());
        TreeSet indirect = ana.getIndirect_gene_tree();
        System.out.println("indirect tree:     " + indirect.size());       
        TreeSet new_genes = ana.getNew_gene_tree();
        TreeSet no_homolog = ana.getNo_homolog_tree();
        
       
        
        Iterator it = direct.iterator();
        System.out.println("direct genes -------------------------------------");
        while (it.hasNext()){
            HomologChipGene g = (HomologChipGene)(it.next());
            System.out.println( g.getHomolog_id() + "----->" + g.getLocus_id() + "              " + g.getScore() );
        }
        
        Iterator t = children_direct.iterator();
        System.out.println("direct children genes -------------------------------------");
        while (t.hasNext()){
            HomologChipGene g = (HomologChipGene)(t.next());
            System.out.println( g.getHomolog_id() + "----->" + g.getLocus_id() + "              " + g.getScore() );
        }        
        
        Iterator itt = indirect.iterator();
        System.out.println("indirect genes -------------------------------------");
        while (itt.hasNext()){
            HomologChipGene g = (HomologChipGene)(itt.next());
            System.out.println( g.getHomolog_id() + "----->" + g.getLocus_id() + "              " + g.getScore() );
        }
        
        Iterator ittt = new_genes.iterator();
        System.out.println("new genes -------------------------------------");
        while (ittt.hasNext()){
            HomologChipGene g = (HomologChipGene)(ittt.next());
            System.out.println( g.getHomolog_id() + "----->" + g.getLocus_id() + "              " + g.getScore() );
        }        
        
        Iterator ite = no_homolog.iterator();
        System.out.println("no homolog genes -------------------------------------");
        while (ite.hasNext()){            
            System.out.println( ite.next());
        }       

        //ana.end2 = System.currentTimeMillis();
        
        
        System.out.println("-----------------done--------------");
        
        
    }
        
        

        
    
}
