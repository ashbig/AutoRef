/*
 * NonHsChipGeneGeneAnalysis.java
 *
 * Created on July 11, 2002, 5:08 PM
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
public class NonHsChipGeneGeneAnalysis extends ChipGeneGeneAnalysis {
    
    public static final int LOCUSID = 1;
    public static final int UNIGENE = 2;
    public static final int ACCESSION = 3;
    public NonHsChipGeneDiseaseAnalysis ana;
        
    public NonHsChipGeneGeneAnalysis(NonHsChipGeneDiseaseAnalysis ana){
        this.ana = ana;
    }
    
    
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
        return ana.hashHomolog(non_hs_homologs, type);
    }
            
    /** generate corresponding human homolog input based on the homolog mapping
     *  @param mapping  homolog mapping hashmap
     */
    public String toHsHomologInput(HashMap mapping){
        return ana.toHsHomologInput(mapping);
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

    
    //////////////////////////////////////// test ////////////////////////////////////////////////
    
    public static void main(String[] args){
        String text ="";
        try{         
        BufferedReader in = new BufferedReader(new FileReader("c:\\temp\\nonhs_gg.txt"));               
        String s;
        
        while((s=in.readLine()) != null){
            text = text + s + " ";    
        }
     
        }catch(Exception e){
            System.out.println(e);
        }
        
        //long start = System.currentTimeMillis();
        String non_hs_locusIDs = text;
        NonHsChipGeneGeneAnalysis ana = new NonHsChipGeneGeneAnalysis(new NonHsChipGeneDiseaseAnalysis());
        HashMap homolog = ana.hashHomolog(non_hs_locusIDs, 2);        
        System.out.println("------ homolog size: " + homolog.size());
        //long end = System.currentTimeMillis();
        //System.out.println("time cost " + (end - start)/1000);
        /*
        Iterator it = homolog.keySet().iterator();
        while (it.hasNext()){
            Object o = it.next();
            System.out.println( " ------ " + ((Integer)(homolog.get(o))).intValue() );
            //System.out.println(o.toString() + " ------ " + ((Integer)(homolog.get(o))).intValue() );
        }
        */
        
        
        ana.hashDirectGenes(560, 1, 2);
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
