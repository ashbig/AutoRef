/*
 * DnaInsert.java
 *
 * Created on April 1, 2005, 2:13 PM
 */

package plasmid.coreobject;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class DnaInsert {
    public static final String HUMAN = "Homo sapiens";
    public static final String YEAST = "Saccharomyces cerevisiae";
    public static final String PA = "Pseudomonas aeruginosa";
    public static final String HP = "Human papillomavirus-16";
    public static final String MOUSE = "Mus musculus";
    public static final String OC = "Oryctolagus cuniculus";
    public static final String RN = "Rattus norvegicus";
    public static final String BP = "Bacteriophage";
    public static final String SV40 = "SV40";
    public static final String YP = "Yersinia pestis KIM";
    public static final String LM = "Photinus pyralis";
    public static final String HAD = "Human adenovirus C type 2";
    public static final String FT = "Francisella tularensis Schu 4";
    public static final String VC = "Vibrio cholerae O1 biovar eltor str. N16961";
    public static final String DM = "Drosophila melanogaster";                          
    public static final String CE = "Caenorhabditis elegans";                            
    public static final String XL = "Xenopus laevis";
    public static final String YPS = "Yersinia pseudotuberculosis";
    public static final String VC1 = "Vibrio cholerae (unspecified strain)";
    public static final String BA = "Bacillus anthracis str. Ames";
    public static final String ARABIDOPSIS = "Arabidopsis thaliana Columbia";
    public static final String TM = "Thermotoga maritima";
    
    private int insertid;
    private int order;
    private int size;
    private String species;
    private String format;
    private String source;
    private int cloneid;
    private String geneid;
    private String name;
    private String description;
    private String targetseqid;
    private String targetgenbank;
    private String hasdiscrepancy;
    private String hasmutation;
    private String region;
    private int refseqid;
    private List properties;
    private String annotation;
    
    private String sequence;
    
    /** Creates a new instance of DnaInsert */
    public DnaInsert() {
    }
    
    public DnaInsert(int insertid, int order, int size,
    String species, String format, String source, int cloneid) {
        this.insertid=insertid;
        this.order=order;
        this.size=size;
        this.species=species;
        this.format=format;
        this.source=source;
        this.cloneid=cloneid;
    }
        
    public DnaInsert(int insertid, int order, int size, String species, String format, String source, 
    int cloneid, String geneid, String name, String description, String targetseqid, String targetgenbank,
    String hasdiscrepancy, String hasmutation, String region, int refseqid) {
        this.insertid=insertid;
        this.order=order;
        this.size=size;
        this.species=species;
        this.format=format;
        this.source=source;
        this.cloneid=cloneid;
        this.geneid = geneid;
        this.name = name;
        this.description = description;
        this.targetseqid = targetseqid;
        this.targetgenbank = targetgenbank;
        this.hasdiscrepancy = hasdiscrepancy;
        this.hasmutation = hasmutation;
        this.region = region;
        this.refseqid = refseqid;
    }
    
    public int getInsertid() {return insertid;}
    public int getOrder() {return order;}
    public int getSize() {
        if(size > 0)
            return size;
        else {
            if(sequence != null) {
                return sequence.trim().length();
            } else {
                return size;
            }
        }
    }
    public String getSpecies() {return species;}
    public String getFormat() {return format;}
    public String getSource() {return source;}
    public int getCloneid() {return cloneid;}
    public String getGeneid() {return geneid;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public String getTargetseqid() {return targetseqid;}
    public String getTargetgenbank() {return targetgenbank;}
    public String getHasdiscrepancy() {return hasdiscrepancy;}
    public String getHasmutation() {return hasmutation;}
    public String getHasmutdis() {
        return hasmutation+"/"+hasdiscrepancy;
    }
    public int getRefseqid() {return refseqid;}
    public List getProperties() {return properties;}
    public String getSequence() {return sequence;}
    public String getRegion() {return region;}
    public String getAnnotation() {return annotation;}
    public String getTargetseqidForNCBI() {
        if(targetseqid == null)
            return targetgenbank;
        return targetseqid;
    }
    public void setInsertid(int id) {this.insertid = id;}
    public void setOrder(int order) {this.order = order;}
    public void setSize(int i) {this.size = i;}
    public void setSpecies(String s) {this.species = s;}
    public void setFormat(String s) {this.format = s;}
    public void setSource(String s) {this.source = s;}
    public void setCloneid(int cloneid) {this.cloneid=cloneid;}
    public void setGeneid(String id) {this.geneid = id;}
    public void setName(String s) {this.name = s;}
    public void setDescription(String s) {this.description = s;}
    public void setTargetseqid(String s) {this.targetseqid = s;}
    public void setTargetgenbank(String s) {this.targetgenbank = s;}
    public void setHasdiscrepancy(String s) {this.hasdiscrepancy = s;}
    public void setHasmutation(String s) {this.hasmutation = s;}
    public void setRefseqid(int id) {this.refseqid = id;}
    public void setProperties(List s) {this.properties = s;}
    public void setSequence(String s) {this.sequence = s;}
    public void setRegion(String s) {this.region = s;}
    public void setAnnotation(String s) {this.annotation = s;}
    
    public String getFastaSequence() {
        return Dnasequence.convertToFasta(sequence);
    }
    
    public String getSpeciesSpecificid() {
        if(YEAST.equals(species))
            return RefseqNameType.SGD;
        if(PA.equals(species))
            return RefseqNameType.PA;
        if(LM.equals(species))
            return RefseqNameType.GENBANK;
        if(YP.equals(species))
            return RefseqNameType.PRO_GI;
        if(VC.equals(species) || VC1.equals(species))
            return RefseqNameType.LOCUS_TAG;
        if(DM.equals(species))
            return RefseqNameType.FBID;
        if(CE.equals(species))
            return RefseqNameType.WBGENEID;
        if(FT.equals(species))
            return RefseqNameType.LOCUS_TAG;
        if(BA.equals(species)) 
            return RefseqNameType.LOCUS_TAG;
        if(ARABIDOPSIS.equals(species)) 
            return RefseqNameType.TAIR;
        if(TM.equals(species))
            return RefseqNameType.LOCUS_TAG;
        if(RefseqNameType.GENEID != null)
            return RefseqNameType.GENEID;
        return "";
    }
}
