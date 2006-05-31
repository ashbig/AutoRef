/*
 * GenbankInfoManager.java
 *
 * Created on August 3, 2005, 12:31 PM
 */

package plasmid.importexport.genbank;

import java.io.*;
import java.util.*;

/**
 *
 * @author  DZuo
 */
public class GenbankInfoManager {
    public static final String GENBANK_ACC = "GenBank Accession";
    public static final String GI = "GI";
    public static final String GENEID = "Gene ID";
    public static final String SYMBOL = "Gene Symbol";
    public static final String GENBANK_URL = "http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=";
    public static final String GENEID_URL = "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=retrieve&dopt=graphics&list_uids=";
    public static final String NA = "NA";
    
    /** Creates a new instance of GenbankInfoManager */
    public GenbankInfoManager() {
    }
    
    public void printGenbankInfo(List infos, String filename) throws Exception {
        OutputStreamWriter f = new FileWriter(filename);
        f.write("Sequence ID\tAccession\tAccession Version\tGI\tOrganism\tGene ID\tSymbol\tDescription\tCDS Start\tCDS Stop\tSequence\n");
        for(int i=0; i<infos.size(); i++) {
            GenbankInfo info = (GenbankInfo)infos.get(i);
            f.write(info.getTerm()+"\t");
            
            if(info.getAccession() == "")
                f.write(NA+"\t");
            else
                f.write(info.getAccession()+"\t");
            
            if(info.getAccessionVersion() == "")
                f.write(NA+"\t");
            else
                f.write(info.getAccessionVersion()+"\t");
            
            if(info.getGi() == "")
                f.write(NA+"\t");
            else
                f.write(info.getGi()+"\t");
            
            if(info.getOrganism() == "")
                f.write(NA+"\t");
            else
                f.write(info.getOrganism()+"\t");
            
            if(info.getGeneid() == "")
                f.write(NA+"\t");
            else
                f.write(info.getGeneid()+"\t");
            
            if(info.getGenesymbol() == "")
                f.write(NA+"\t");
            else
                f.write(info.getGenesymbol()+"\t");
            
            if(info.getDefinition() == "")
                f.write(NA+"\t");
            else
                f.write(info.getDefinition()+"\t");
            
            if(info.getCdsstart() == -1)
                f.write(NA+"\t");
            else
                f.write(info.getCdsstart()+"\t");
            
            if(info.getCdsstop() == -1)
                f.write(NA+"\t");
            else
                f.write(info.getCdsstop()+"\t");
            
            if(info.getSequencetext() == "")
                f.write(NA+"\n");
            else
                f.write(info.getSequencetext()+"\n");
        }
        f.close();
    }
    
    public void printGenbankNames(List infos, String filename) throws Exception {
        OutputStreamWriter f = new FileWriter(filename);
        f.write("Sequence ID\tName Type\tName Value\tName URL\n");
        
        //print Genbank Accession
        for(int i=0; i<infos.size(); i++) {
            GenbankInfo info = (GenbankInfo)infos.get(i);
            if(info.getAccession() != null && info.getAccession().trim().length()>0) {
                f.write(info.getTerm()+"\t");
                f.write(GENBANK_ACC+"\t");
                f.write(info.getAccession()+"\t");
                f.write(GENBANK_URL+info.getGi()+"\n");
            }
        }
        
        //print GI
        for(int i=0; i<infos.size(); i++) {
            GenbankInfo info = (GenbankInfo)infos.get(i);
            if(info.getGi() != null && info.getGi().trim().length()>0) {
                f.write(info.getTerm()+"\t");
                f.write(GI+"\t");
                f.write(info.getGi()+"\t");
                f.write(NA+"\n");
            }
        }
        
        //print Gene ID
        for(int i=0; i<infos.size(); i++) {
            GenbankInfo info = (GenbankInfo)infos.get(i);
            if(info.getGeneid() != null && info.getGeneid().trim().length()>0) {
                f.write(info.getTerm()+"\t");
                f.write(GENEID+"\t");
                f.write(info.getGeneid()+"\t");
                f.write(GENEID_URL+info.getGeneid()+"\n");
            }
        }
        
        //print Gene Symbol
        for(int i=0; i<infos.size(); i++) {
            GenbankInfo info = (GenbankInfo)infos.get(i);
            if(info.getGenesymbol() != null && info.getGenesymbol().trim().length()>0) {
                f.write(info.getTerm()+"\t");
                f.write(SYMBOL+"\t");
                f.write(info.getGenesymbol()+"\t");
                f.write(NA+"\n");
            }
        }
        
        f.close();
    }
    
    public void printError(List errorList, String filename) throws Exception {
        OutputStreamWriter f = new FileWriter(filename);
        for(int i=0; i<errorList.size(); i++) {
            String s = (String)errorList.get(i);
            f.write(s+"\n");
        }
        f.close();
    }
    
    public List readGenbank(String filename) throws Exception {
        List genbanks = new ArrayList();
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String line = null;
        while((line = in.readLine()) != null) {
            if(line.trim().length()>0) {
                genbanks.add(line.trim());
            }
        }
        
        in.close();
        
        return genbanks;
    }
    
    public static void main(String args[]) throws Exception {
        String referenceFile = "G:\\plasmid\\Reference\\KAMIL\\reference.txt";
        String referenceNameFile = "G:\\plasmid\\Reference\\KAMIL\\referenceName.txt";
        String referenceInput = "G:\\plasmid\\Reference\\KAMIL\\input.txt";
        //String referenceInput="G:\\tmp.txt";
        String error = "G:\\plasmid\\Reference\\KAMIL\\error.txt";
        
        GenbankInfoManager manager = new GenbankInfoManager();
        List genbanks = manager.readGenbank(referenceInput);
        if(genbanks == null) {
            System.out.println("Error occured while reading input file.");
            System.exit(0);
        }
        
        List infos = new ArrayList();
        List errorList = new ArrayList();
        GenbankParser parser = new GenbankParser();
        for(int i=0; i<genbanks.size(); i++) {
            String genbank = (String)genbanks.get(i);
            System.out.println("Parsing "+genbank);
            GenbankInfo info = null;
            try {
                info = parser.parseGenbank(genbank);
            } catch (Exception ex) {
                System.out.println(ex);
            }
            
            if(info == null) {
                System.out.println("Error occured while parsing genbank: "+genbank);
                errorList.add(genbank);
            } else {
                infos.add(info);
            }
        }
        
        manager.printGenbankInfo(infos, referenceFile);
        manager.printGenbankNames(infos, referenceNameFile);
        if(errorList.size()>0)
            manager.printError(errorList, error);
        
        System.exit(0);
    }
}
