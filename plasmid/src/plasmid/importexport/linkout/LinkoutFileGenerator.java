/*
 * LinkoutFileGenerator.java
 *
 * Created on February 14, 2007, 1:13 PM
 */

package plasmid.importexport.linkout;

import java.util.*;
import java.sql.*;
import java.io.*;
import plasmid.coreobject.Clone;
import plasmid.database.DatabaseTransaction;

/**
 *
 * @author  DZuo
 */
public class LinkoutFileGenerator {
    public static final String HEADER = "?xml version=\"1.0\"?>\n<!DOCTYPE LinkSet PUBLIC \"-//NLM//DTD LinkOut 1.0//EN\"\n\"http://www.ncbi.nlm.nih.gov/entrez/linkout/doc/LinkOut.dtd\"";
    public static final String BASEURL = "[<!ENTITY base.url \"http://plasmid.med.harvard.edu/PLASMID/RefseqSearchContinue.do?cdna=true&amp;shrna=true&amp;genomicfragment=true&amp;tfbindsite=true&amp;genome=true&amp;pagesize=50\">]";
    public static final String PRIVIDERID = "6200";
    public static final String HUMAN_HTML = "Homo%20sapiens";
    public static final String HUMAN = "Homo sapiens";
    public static final String REFSEQTYPE_CDNA = "cDNA";
    
    /** Creates a new instance of LinkoutFileGenerator */
    public LinkoutFileGenerator() {
    }
    
    public String generateEntrezGene(String refseqtype, String species, List genes) {
        StringBuffer sf = new StringBuffer();
        String linkid = "EntrezGene-"+species.replaceAll("%20", " ");
        generateHeader(sf, linkid, "Gene");
        
        for(int i=0; i<genes.size(); i++) {
            GeneInfo gene = (GeneInfo)genes.get(i);
            String geneid = gene.getGeneid();
            System.out.println(i+1+": Add gene: "+geneid);
            sf.append("        <ObjId>"+geneid+"</ObjId>\n");
        }
        
        generateFooter(sf, species, refseqtype, "Gene%20ID");
        
        return sf.toString();
    }
    
    public String generateGenbank(String refseqtype, String species, List genes) {
        StringBuffer sf = new StringBuffer();
        String linkid = "Nucleotide-"+species.replaceAll("%20", " ");
        generateHeader(sf, linkid, "Nucleotide");
        
        
        for(int i=0; i<genes.size(); i++) {
            GeneInfo gene = (GeneInfo)genes.get(i);
            String geneid = gene.getGeneid();
            System.out.println(i+1+": Add gene: "+geneid);
            sf.append("        <ObjId>"+geneid+"</ObjId>\n");
        }
        
        generateFooter(sf, species, refseqtype, "GenBank%20Accession");
        
        return sf.toString();
    }
    
    public String generateGi(String refseqtype, String species, List genes) {
        StringBuffer sf = new StringBuffer();
        String linkid = "Nucleotide-"+species.replaceAll("%20", " ");
        generateHeader(sf, linkid, "Nucleotide");
        
        
        for(int i=0; i<genes.size(); i++) {
            GeneInfo gene = (GeneInfo)genes.get(i);
            String geneid = gene.getGeneid();
            System.out.println(i+1+": Add gene: "+geneid);
            sf.append("        <ObjId>"+geneid+"</ObjId>\n");
        }
        
        generateFooter(sf, species, refseqtype, "GI");
        
        return sf.toString();
    }
    
    protected void generateHeader(StringBuffer sf, String linkid, String db) {
        sf.append("<LinkSet>\n");
        sf.append("  <Link>\n");
        sf.append("    <LinkId>"+linkid+"</LinkId>\n");
        sf.append("    <ProviderId>"+PRIVIDERID+"</ProviderId>\n");
        sf.append("    <ObjectSelector>\n");
        sf.append("      <Database>"+db+"</Database>\n");
        sf.append("      <ObjectList>\n");    
    }
    
    protected void generateFooter(StringBuffer sf, String species, String refseqtype, String identifier) {
        sf.append("      </ObjectList>\n");
        sf.append("    </ObjectSelector>\n");
        sf.append("    <ObjectUrl>\n");
        sf.append("      <Base>&base.url;</Base>\n");
        sf.append("      <Rule>&amp;species="+species+"&amp;refseqType="+refseqtype+"&amp;searchType="+identifier+"&amp;searchString=&lo.id;</Rule>\n");
        sf.append("      <UrlName>Order full-length cDNA clone</UrlName>\n");
        sf.append("      <Attribute>subscription/membership/fee required</Attribute>\n");
        sf.append("      <Attribute>order form</Attribute>\n");
        sf.append("    </ObjectUrl>\n");
        sf.append("  </Link>\n");
        sf.append("</LinkSet>\n");
    }
    
    public List getHumanGenes() throws Exception {
        String sql = "select distinct geneid from clonegene where cloneid in"+
        " (select cloneid from clone where domain='Homo sapiens'"+
        " and restriction <> 'HIP only'"+
        " and status='"+Clone.AVAILABLE+"')";
        List genes = new ArrayList();
        DatabaseTransaction t = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                String geneid = rs.getString(1);
                genes.add(geneid);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return genes;
    }
    
    public List getAllGenes(String species) throws Exception {
        String sql = "select distinct g.geneid"+
        " from clone c, clonegene g"+
        " where c.cloneid=g.cloneid"+
        " and c.domain='"+species+"'"+
        " and c.restriction <> 'HIP only'"+
        " and c.status='"+Clone.AVAILABLE+"'";
        List genes = new ArrayList();
        DatabaseTransaction t = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                String geneid = rs.getString(1);
                genes.add(new GeneInfo(geneid, species));
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return genes;
    }
    
    public List getAllGenbanks(String species) throws Exception {
        String sql = "select distinct g.accession"+
        " from clone c, clonegenbank g"+
        " where c.cloneid=g.cloneid"+
        " and c.domain='"+species+"'"+
        " and c.restriction <> 'HIP only'"+
        " and c.status='"+Clone.AVAILABLE+"'";
        List genes = new ArrayList();
        DatabaseTransaction t = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                String geneid = rs.getString(1);
                genes.add(new GeneInfo(geneid, species));
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return genes;
    }
    
    public List getAllGis(String species) throws Exception {
        String sql = "select distinct g.gi"+
        " from clone c, clonegi g"+
        " where c.cloneid=g.cloneid"+
        " and c.domain='"+species+"'"+
        " and c.restriction <> 'HIP only'"+
        " and c.status='"+Clone.AVAILABLE+"'";
        List genes = new ArrayList();
        DatabaseTransaction t = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                String geneid = rs.getString(1);
                genes.add(new GeneInfo(geneid, species));
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return genes;
    }
    
    public static void main(String args[]) {
        //String output = "G:\\linkout\\human\\resources_HumanEntrez.xml";
        String output = "G:\\linkout\\human\\resources_HumanGenbank.xml";
        String refseqtype = REFSEQTYPE_CDNA;
        String species = HUMAN;
        String htmlSpecies = HUMAN_HTML;
        
        LinkoutFileGenerator g = new LinkoutFileGenerator();
        try {
            //List genes = g.getAllGenes(species);
            //String record = g.generateEntrezGene(refseqtype, htmlSpecies, genes);
            //List genes = g.getAllGenbanks(species);
            //String record = g.generateGenbank(refseqtype, htmlSpecies, genes);
            List genes = g.getAllGis(species);
            String record = g.generateGi(refseqtype, htmlSpecies, genes);
            OutputStreamWriter out = new FileWriter(output);
            out.write("<"+HEADER+"\n"+BASEURL+">\n");
            out.write(record);
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
