/**
 * $Id: CDNASequence.java,v 1.2 2001-07-09 16:00:56 jmunoz Exp $
 *
 * File     : CDNASequence.java
 * Date     : 06112001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.core;

import java.util.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.sql.*;
import sun.jdbc.rowset.*;

/**
 * This class represents a cDNA sequence which has a start, stop, and a sequence text.
 */
public class CDNASequence {
    
    public final static int FASTA_BASES_PER_LINE = 60;
    protected String sequencetext;    
    protected int cdsstart = -1;
    protected int cdsstop = -1;
    protected int cdslength = -1;
    protected int gccontent;

    /**
     * Constructor.
     *
     * @return The CDNASequence object.
     */
    public CDNASequence() {}
    
    /**
     * Constructor.
     *
     * @param sequencetext The sequence string.
     * @param cdsstart The cds start position.
     * @param cdsstop The cds stop position.
     *
     * @return The CDNASequence object.
     */
    public CDNASequence(int cdsstart, int cdsstop, String sequencetext) {
        this.cdsstart = cdsstart;
        this.cdsstop = cdsstop;
        this.sequencetext = sequencetext;
        this.cdslength = cdsstop - cdsstart + 1;
        this.gccontent = getGccontent();
    }

    /**
     * Constructor.
     *
     * @param sequencetext The sequence string.
     * @param cdsstart The cds start position.
     * @param cdsstop The cds stop position.
     * @param cdslength The cds length.
     * @param gccontent The GC content of this sequence.
     *
     * @return The CDNASequence object.
     */
    public CDNASequence(int cdsstart, int cdsstop, String sequencetext, int cdslength, int gccontent) {
        this.cdsstart = cdsstart;
        this.cdsstop = cdsstop;
        this.sequencetext = sequencetext;
        this.cdslength = cdslength;
        this.gccontent = gccontent;
    }
    
    /**
     * Set the sequencetext to the given value.
     *
     * @value The given value.
     */
    public void setSequencetext(String value){
        this.sequencetext = value.toUpperCase();
    }
    
    /**
     * Set the cdsstart to the given value.
     *
     * @value The given value.
     */
    public void setCdsstart(int value) {
        this.cdsstart = value;
    }
    
    /**
     * Set the cdsstop to the given value.
     *
     * @value The given value.
     */
    public void setCdsstop(int value) {
        cdsstop = value;
    }
    
    /**
     * Set the cds length to the given value.
     *
     * @value The given value.
     */
    public void setCdslength(int value) {
        cdslength = value;
    }
       
    /**
     * Get the sequencetext value.
     *
     * @return The sequencetext value.
     */
    public String getSequencetext() {
        return sequencetext;
    }
    
    /**
     * Get the length of the sequencetext.
     *
     * @return The length of the sequence text.
     */
    public int getSequenceLength() {
        return sequencetext.length();
    }
    
    /**
     * Converts a string to fasta format
     *
     * @param sequenceString sequence to convert to fasta
     *
     * @return string sequenceString in fasta
     */
    public static String convertToFasta(String sequenceString) {
        
        StringBuffer seqBuff = new StringBuffer();
        
        for (int i=0; i < sequenceString.length(); i++){
            if(i%FASTA_BASES_PER_LINE == 0) {
                seqBuff.append("\n");
            }
            
            seqBuff.append(sequenceString.charAt(i));
        }
        
        return seqBuff.toString();
    }
    
    /**
     * Get the sequence in fasta format
     *
     * @return the sequence text in fasta format
     */
    public String getFastaSequence() {
        
        return convertToFasta(getSequencetext());
    }
    
    /**
     * gets the cds part of the sequence in fasta format
     *
     * @return the cds part of the sequence in fasta
     */
    public String getCDSFasta() {
        return convertToFasta(getSequencetext().substring(getCdsstart()-1,getCdsstop()));
    }

    /**
     * Get the gccontent value.
     *
     * @return The gccontent value.
     */
    public int getGccontent() {
        if(gccontent > 0) {
            return gccontent;
        }
        
        if(sequencetext==null || sequencetext.equals("") || cdsstart==-1 || cdsstop==-1)
            return 0;
        
        int count=0;
        String cds = sequencetext.substring(cdsstart-1, cdsstop);
        for(int i=0; i<cds.length(); i++) {
            String nt = cds.substring(i, i+1);
            if(nt.equals("G") || nt.equals("C")) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Get the cdsstart value.
     *
     * @return The cdsstart value.
     */
    public int getCdsstart() {
        return cdsstart;
    }
    
    /**
     * Get the cdsstop value.
     *
     * @return The cdsstop value.
     */
    public int getCdsstop() {
        return cdsstop;
    }
    
    /**
     * Get the cdslength value.
     *
     * @return The cdslength value.
     */
    public int getCdslength() {
        return cdslength;
    }

    //******************************************************************//
    //			Test                                            //								//
    //******************************************************************//
    public static void main(String [] args) throws Exception {
        String seq = "ATGAGGCGCTGCCCGTGCCGTGGGAGCCTGAACGAGGCGGAGGCCGGGGCGCTGCCCGCGGCGGCCCGCA"+
                     "TGGGACTGGAGGCGCCGCGAGGAGGGCGGCGGCGGCAGCCGGGACAGCAGCGACCTGGGCCCGGCGCAGG"+
                     "GGCCCCGGCGGGGCGGCCGGAGGGGGGCGGGCCCTGGGCCCGGACAGAGGGGTCCAGCCTCCACAGCGAG"+
                     "CCTGAGAGGGCCGGCCTCGGGCCTGCGCCGGGGACAGAGAGTCCGCAGGCAGAATTCTGGACAGACGGAC"+
                     "AGACTGAGCCCGCGGCAGCTGGCCTTGGAGTAGAGACCGAGAGGCCCAAGCAAAAGACGGAGCCAGACAG"+
                     "GTCCAGCCTCCGGACGCATCTAGAATGGAGCTGGTCAGAGCTGGAGACGACTTGTCTTTGGACGGAGACC"+
                     "GGGACAGATGGCCTTTGGACTGATCCGCACAGGTCCGACCTCCAGTTTCAGCCCGAGGAGGCCAGCCCCT"+
                     "GGACACAGCCAGGGGTTCATGGGCCCTGGACAGAGCTGGAAACGCATGGGTCACAGACTCAGCCAGAGAG"+
                     "GGTCAAGTCCTGGGCTGATAACCTCTGGACCCACCAGAACAGTTCCAGCCTCCAGACTCACCCAGAAGGA"+
                     "GCCTGTCCCTCAAAAGAGCCAAGTGCTGATGGCTCCTGGAAAGAATTGTATACTGATGGCTCCAGGACAC"+
                     "AACAGGATATTGAAGGTCCCTGGACAGAGCCATATACTGATGGCTCCCAGAAAAAACAGGATACTGAAGC"+
                     "AGCCAGGAAACAGCCTGGCACTGGTGGTTTCCAAATACAACAGGATACTGATGGCTCCTGGACACAACCT"+
                     "AGCACTGACGGTTCCCAGACAGCACCTGGGACAGACTGCCTCTTGGGAGAGCCTGAGGATGGCCCATTAG"+
                     "AGGAACCAGAGCCTGGAGAATTGCTGACTCACCTGTACTCTCACCTGAAGTGTAGCCCCCTGTGCCCTGT"+
                     "GCCCCGCCTCATCATTACCCCTGAGACCCCTGAGCCTGAGGCCCAGCCAGTGGGACCCCCCTCCCGGGTT"+
                     "GAGGGGGGCAGCGGCGGCTTCTCCTCTGCCTCTTCTTTCGACGAGTCTGAGGATGACGTGGTGGCCGGGG"+
                     "GCGGAGGTGCCAGCGATCCCGAGGACAGGTCTGGGAGCAAACCCTGGAAGAAGCTGAAGACAGTTCTGAA"+
                     "GTATTCACCCTTTGTGGTCTCCTTCCGAAAACACTACCCTTGGGTCCAGCTTTCTGGACATGCTGGGAAC"+
                     "TTCCAGGCAGGAGAGGATGGTCGGATTCTGAAACGTTTCTGTCAGTGTGAGCAGCGCAGCCTGGAGCAGC"+
                     "TGATGAAAGACCCGCTGCGACCTTTCGTGCCTGCCTACTATGGCATGGTGCTGCAGGATGGCCAGACCTT"+
                     "CAACCAGATGGAAGACCTCCTGGCTGACTTTGAGGGCCCCTCCATTATGGACTGCAAGATGGGCAGCAGG"+
                     "ACCTATCTGGAAGAGGAGCTAGTGAAGGCACGGGAACGTCCCCGTCCCCGGAAGGACATGTATGAGAAGA"+
                     "TGGTGGCTGTGGACCCTGGGGCCCCTACCCCTGAGGAGCATGCCCAGGGTGCAGTCACCAAGCCCCGCTA"+
                     "CATGCAGTGGAGGGAAACCATGAGCTCCACCTCTACCCTGGGCTTCCGGATCGAGGGCATCAAGAAGGCA"+
                     "GATGGGACCTGTAACACCAACTTCAAGAAGACGCAGGCACTGGAGCAGGTGACAAAAGTGCTGGAGGACT"+
                     "TCGTGGATGGAGACCACGTCATCCTGCAAAAGTACGTGGCATGCCTAGAAGAACTTCGTGAAGCTCTGGA"+
                     "GATCTCCCCCTTCTTCAAGACCCACGAGGTGGTAGGCAGCTCCCTCCTCTTCGTGCACGACCACACCGGC"+
                     "CTGGCCAAGGTCTGGATGATAGACTTCGGCAAGACGGTGGCCTTGCCCGACCACCAGACGCTCAGCCACA"+
                     "GGCTGCCCTGGGCTGAGGGCAACCGTGAGGACGGCTACCTCTGGGGCCTGGACAACATGATCTGCCTCCT"+
                     "GCAGGGGCTGGCACAGAGCTGA";

        CDNASequence sequence = new CDNASequence(1, 2052, seq);
        
        if(sequence.getCdsstart() == 1)
            System.out.println("Testing getCdsstart - OK");
        else
            System.out.println("Testing getCdsstart - ERROR");

        if(sequence.getCdsstop() == 2052)
            System.out.println("Testing getCdsstop - OK");
        else
            System.out.println("Testing getCdsstop - ERROR");

        if(sequence.getCdslength() == 2052)
            System.out.println("Testing getCdslength - OK");
        else
            System.out.println("Testing getCdslength - ERROR");        

        if(sequence.getGccontent() == 1261)
            System.out.println("Testing getGccontent - OK");
        else
            System.out.println("Testing getGccontent - ERROR");               
    }
}

