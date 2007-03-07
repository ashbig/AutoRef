/*
 * EntrezGeneParser.java
 *
 * Created on February 20, 2007, 11:02 AM
 */

package plasmid.importexport.entrezgene;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class EntrezGeneParser {
    public static final char COMMENTLINE = '#';
    public static final String DELIM = "\t";
    public static final String DELIMINRECORD = "|";
    public static final String NULLSTRING = "-";
    public static final String GENOMIC = "g";
    public static final String MRNA = "m";
    
    public static final int HUMAN = 9606;
    
    private List genes;
    private List symbols;
    private List sequences;
    private List dbs;
    
    /** Creates a new instance of EntrezGeneParser */
    public EntrezGeneParser() {
        reset();
    }
    
    public List getGenes() {return genes;}
    public List getSymbols() {return symbols;}
    public List getSequences() {return sequences;}
    public List getDbs() {return dbs;}
    
    public void reset() {
        genes = new ArrayList();
        symbols = new ArrayList();
        sequences = new ArrayList();
        dbs = new ArrayList();
    }
    
    /**
     * Format: tax_id GeneID Symbol LocusTag Synonyms dbXrefs
     * chromosome map_location description type_of_gene
     * Symbol_from_nomenclature_authority
     * Full_name_from_nomenclature_authority
     * Nomenclature_status Other_designations
     * (tab is used as a separator, pound sign - start of a comment)
     */
    public void parseGeneInfo(List input) throws Exception {
        if(input == null)
            throw new Exception("Input is null");
        
        for(int i=0; i<input.size(); i++) {
            String line = (String)input.get(i);
            if(line.charAt(0) == COMMENTLINE)
                continue;
            
            StringTokenizer tokenizer = new StringTokenizer(line, DELIM);
            int taxid = Integer.parseInt(convertNull(tokenizer.nextToken()));
            int geneid = Integer.parseInt(convertNull(tokenizer.nextToken()));
            String symbol = convertNull(tokenizer.nextToken());
            String locustag = convertNull(tokenizer.nextToken());
            String synonyms = convertNull(tokenizer.nextToken());
            String dbXrefs = convertNull(tokenizer.nextToken());
            String chromosome = convertNull(tokenizer.nextToken());
            String map = convertNull(tokenizer.nextToken());
            String description = convertNull(tokenizer.nextToken());
            String type = convertNull(tokenizer.nextToken());
            
            GeneInfo gene = new GeneInfo(geneid, taxid, symbol, description, type, locustag, chromosome, map);
            genes.add(gene);
            
            if(synonyms != null) {
                StringTokenizer st1 = new StringTokenizer(synonyms, DELIMINRECORD);
                while(st1.hasMoreTokens()) {
                    String s = st1.nextToken();
                    GeneSymbol gs = new GeneSymbol(s, geneid);
                    symbols.add(gs);
                }
            }
            
            if(dbXrefs != null) {
                StringTokenizer st1 = new StringTokenizer(dbXrefs, DELIMINRECORD);
                while(st1.hasMoreTokens()) {
                    String s = st1.nextToken();
                    int index = s.indexOf(":");
                    String db = s.substring(0, index);
                    String value = s.substring(index+1);
                    DbXrefs ref = new DbXrefs(geneid, db, value);
                    dbs.add(ref);
                }
            }
        }
    }
    
    /**
     * Format: tax_id GeneID status RNA_nucleotide_accession.version RNA_nucleotide_gi
     * protein_accession.version protein_gi genomic_nucleotide_accession.version
     * genomic_nucleotide_gi start_position_on_the_genomic_accession
     * end_position_on_the_genomic_accession orientation assembly
     * (tab is used as a separator, pound sign - start of a comment)
     */
    public void parseGene2accession(List input, int species) throws Exception {
        if(input == null)
            throw new Exception("Input is null");
        
        Set seqs = new TreeSet(new SequenceInfoComparator());
        for(int i=0; i<input.size(); i++) {
            String line = (String)input.get(i);
            if(line.charAt(0) == COMMENTLINE)
                continue;
            
            StringTokenizer tokenizer = new StringTokenizer(line, DELIM);
            int taxid = Integer.parseInt(convertNull(tokenizer.nextToken()));
            if(taxid != species)
                continue;
            
            int geneid = Integer.parseInt(convertNull(tokenizer.nextToken()));
            String status = convertNull(tokenizer.nextToken());
            String accession = convertAccession(convertNull(tokenizer.nextToken()));
            String gi = convertNull(tokenizer.nextToken());
            String paccession = convertAccession(convertNull(tokenizer.nextToken()));
            String pgi = convertNull(tokenizer.nextToken());
            String genomeAccession = convertAccession(convertNull(tokenizer.nextToken()));
            String genomeGi = convertNull(tokenizer.nextToken());
                
            seqs.add(new SequenceInfo(accession, gi, paccession, pgi, MRNA, geneid));
            seqs.add(new SequenceInfo(genomeAccession, genomeGi, null, null, GENOMIC, geneid));
        }
        
        sequences.addAll(seqs);
    }
    
    public String convertNull(String s) {
        if(NULLSTRING.equals(s))
            return null;
        else
            return s;
    }
    
    public String convertAccession(String s) {
        if(s == null)
            return s;
        
        int index = s.indexOf('.');
        if(index != -1)
            return s.substring(0, index);
        else
            return s;
    }
}
