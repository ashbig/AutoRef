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
    
    // no homolog tree contains the non-human genes which has no human homolog
    protected TreeSet no_homolog_tree = new TreeSet();
    
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
            while(rs.next()){
                h_locusID = rs.getInt(1);
            }
            rs.close();
            if(h_locusID != -1)
                homolog_mapping.put(non_hs_homolog, new Integer(h_locusID));
        }
        pstmt.close();
        }catch(SQLException e){
            System.out.println(e);
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
            input += ((Integer)(mapping.get(it.next()))).toString() + " ";
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
    

    /** classify each gene in the input list and put it in the certain tree
     *  @param id   gene identifier
     *  @param mapping  homolog mapping hashmap
     */        
    public void classify(String id, HashMap mapping){

        Object hs_locus_id = mapping.get(id);
        ChipGene g;
        
        if(hs_locus_id == null){
            no_homolog_tree.add(id);
        }
        else{
            
            if(direct_gene_hashmap.containsKey(hs_locus_id)){
                g = (ChipGene)(direct_gene_hashmap.get(hs_locus_id));
                direct_gene_tree.add(new HomologChipGene(g.getGene_symbol(), g.getLocus_id(), g.getScore(), id));                                                 
                if(direct_children_gene_hashmap.containsKey(hs_locus_id)){
                    g = (ChipGene)(direct_children_gene_hashmap.get(hs_locus_id));
                    direct_children_gene_tree.add(new HomologChipGene(g.getGene_symbol(), g.getLocus_id(), g.getScore(), id)); 
                }
            }
            
            if(direct_children_gene_hashmap.containsKey(hs_locus_id)){
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
        ana.hashIndirectGenes(ana.toHsHomologInput(homolog), ana.source_for_indirect_genes, 2, 1, 4000);
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
