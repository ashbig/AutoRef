/*
 * @(#)BlastParser.java	1.00 04/023/01
 * 
 * Copyright 2001-2001 Harvard Institute of Proteomics. All Rights Reserved.
 * 
 * @author  Tao Wei
 * @version 1.00
 *
 * Usage of the class is very simple. Construct a BlastParser object with 
 * a blast output result file. Call parseBlast() and then call accessor methods
 * to obtain information you are interested.
 */

package flex.ApplicationCode.Java.blast;

import java.io.*;
import java.util.*;

public class BlastParser implements BlastParserConstants {
    /** store query information, such as name and sequence length. */
    Hashtable qryInfoHT = new Hashtable();
    /** store blast database information, such as path and size. */
    Hashtable dbInfoHT  = new Hashtable();
    /** store blast search information, such as program used, matrix etc. */
     Hashtable srchInfoHT = new Hashtable();
    /** store a set of <code>HomologItem</code> objects
     *  which captures information such as subject sequence name, score, evalue
     *  and <code>Alignment</code> object
     */
     ArrayList homologList = new ArrayList();
    /** specify how many hits to be parsed */
     int hits = 5;

    /** Constructor takes two parameters:
     *  @param InputStream in specifies an InputStream instance. 
     *  @param int hits specifies how many hits are parsed. Not implemented yet.
     */
    public BlastParser(InputStream in, int hits) {
        this(in);
        this.hits = hits;
    }

    /** Constructor takes a file name which then open the file as a
     *  FileInputStream instance and construct the parser
     */
    public BlastParser(String fname) {
        this(System.in);
        FileInputStream fin;
        try {
            fin = new FileInputStream(fname);
            ReInit(fin);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** To access query information which returns a Properties object */
    public  Hashtable getQuryInfo()      { return qryInfoHT;     }

    /** Return query sequence ID as a String object */
    public  String getQuerySeqID() { return (String)qryInfoHT.get("qry_name"); }

    /** Return query sequence length as a String object */
    public  String getQuerySeqLength() { return (String)qryInfoHT.get("qry_length"); }

    /** Return blast search program used */
    public  String getBlastProgram() {
        return (String)srchInfoHT.get("program");
    }

    /** Return matrix used in blast search */
    public  String getMatrix() {
        return (String)srchInfoHT.get("matrix");
    }

    /** Return gap penalties in blast alignment */
    public  String getGapPenalties() { return (String)srchInfoHT.get("gaps"); }

    /** Return blast database path */
    public  String getDBPath() { return (String)srchInfoHT.get("dbpath"); }

    /** Return the number of sequence in current blast database */
    public  String getDBSize() { return (String)srchInfoHT.get("dbsize"); }

    /** Return the total number of residues in current blast database */
    public  String getDBLetters() { return (String)srchInfoHT.get("dbletters"); }

    /** To access database information which returns a Hashtable object */
    public  Hashtable getDBInfo()        { return dbInfoHT;      }

    /** To access blast search information again returning a Hashtable object */
    public  Hashtable getSearchInfo()    { return srchInfoHT;    }

    /** To access parsed homologs found by blast search. See <code>
     *  HomologItem</code> for its API. 
     */
    public  ArrayList getHomologList()   { return homologList;   }

    /** Set the number of homologs to parse. Current implementation parses all
     *  homologs in the blast result file.
     */
    public  void      setHits(int n)     { hits = n;             }

    /** HomologItem as an inner class to store information of identified
     *  homologs.
     */
    public class HomologItem {
        /** subID, homolog sequence ID */
        String subID = null;
        /** subLen, homolog sequence length */
        int subLen = 0;
        /** alignList, an ArrayList of local Alignment objects */
        ArrayList alignList = new ArrayList();

        public void setSubID(String id)           { subID  = id;  }
        public void setSubLen(int len)            { subLen = len; }
        public void addAlignItem(Alignment align) { alignList.add(align); }

        /** Return homolog sequence ID */
        public String getSubID()                  { return subID;  }

        /** Return homolog sequence length */
        public int getSubLen()                    { return subLen; }

        /** Return a local Alignment object at specified index */
        public Alignment getAlignItem(int index)  {
            return (Alignment)alignList.get(index);
        }

        /** Return the number of local Alignment objects */
        public int getSize() { return alignList.size(); }
    }

    /** LOCAL alignments found by blast as significant */
    public class Alignment {
        /** alignInfo, store information of a local Alignment */
        Properties alignInfo = new Properties();

        /** qryStart and qryEnd are query sequence offsets 
         *  starting this local alignment */
        int qryStart, qryEnd = 0;

        /** sbjSeq, a region of database sequence participating 
         *  this local alignment 
         */
        StringBuffer qrySeq = new StringBuffer();

        /** sbjStart and sbjEnd are database sequence offsets 
         *  starting this local alignment 
         */
        int sbjStart, sbjEnd = 0;

        /** sbjSeq, a region of database sequence participating 
         *  this local alignment 
         */
        StringBuffer sbjSeq = new StringBuffer();

        private void setScore(String score) {
            alignInfo.setProperty("score", score);
        }

        private void setEvalue(String evalue) {
            alignInfo.setProperty("evalue", evalue);
        }

        private void setIdentity(String ident) {
            alignInfo.setProperty("identity", ident);
        }

        private void setGap(String gap) {
            alignInfo.setProperty("gap", gap);
        }

        private void setStrand(String strand) {
            alignInfo.setProperty("strand", strand);
        }

        /** Return similarity scores of a local alignment */
        public String getScore()    { return alignInfo.getProperty("score");    }

        /** Return E value of a local alignment */
        public String getEvalue()   { return alignInfo.getProperty("evalue");   }

        /** Return identities of residues of a local alignment */
        public String getIdentity() { return alignInfo.getProperty("identity"); }

        /** Return sum of gaps of both aligned strands */
        public String getGap()      { return alignInfo.getProperty("gap");      }
        /** Return relative direction of aligned two strands */
        private String getStrand()   { return alignInfo.getProperty("strand");   }

        private void setQryStart(int start) { qryStart = start;   }
        private void setQryEnd(int end)     { qryEnd   = end;     }
        private void setQrySeq(String seq)  { qrySeq.append(seq); }

        private void setSbjStart(int start) { sbjStart = start;   }
        private void setSbjEnd(int end)     { sbjEnd   = end;     }
        private void setSbjSeq(String seq)  { sbjSeq.append(seq); }

        public int getQryStart()  { return qryStart; }
        public int getQryEnd()    { return qryEnd;   }
        private String getQrySeq() { return qrySeq.toString();   }

        private int getSbjStart()  { return sbjStart; }
        private int getSbjEnd()    { return sbjEnd;   }
        private String getSbjSeq() { return sbjSeq.toString();   }

    }

    public static void main(String args[]) throws ParseException {
        BlastParser parser = new BlastParser(System.in, 10);
        parser.parseBlast();
        parser.displayParsed();
    }

    /** Display parsed results */
    public  void displayParsed() {
        // print out query info
        System.out.println("Query sequence name:    " + getQuerySeqID());
        System.out.println("Query sequence length:  " + getQuerySeqLength());
        System.out.println();

        // print out search info
        System.out.println("Blast program:     " + getBlastProgram());
        System.out.println("Score matrix:      " + getMatrix());
        System.out.println("Gap penalties:     " + getGapPenalties());
        System.out.println("Database path:     " + getDBPath());
        System.out.println("Database size:     " + getDBSize());
        System.out.println("Database letters:  " + getDBLetters());
        System.out.println();

        //System.out.println(srchInfoHT.toString());

        // print out how many homologs found
        System.out.println("TOTAL HOMOLOGS PARSED : " + homologList.size());

        // print out details
        for (int i=0; i<homologList.size(); i++) {  // loop through all homologs
            int count = i + 1;
            System.out.println("Homolog : " + count);
            System.out.println("------------");
            HomologItem x = (HomologItem)homologList.get(i);
            System.out.println("\tHomolog ID:    " + x.getSubID());
            System.out.println("\tHomolog LEN:   " + x.getSubLen());
            System.out.println();
            for (int j=0; j<x.getSize(); j++) { // each homolog has one or more Alignment
                count = j + 1;
                System.out.println("\t\tLOCAL ALIGNMENT : " + count);
                System.out.println("\t\t--------------------");
                Alignment y = (Alignment)x.getAlignItem(j);
                System.out.println("\t\tScore:        " + y.getScore());
                System.out.println("\t\tEvalue:       " + y.getEvalue());
                System.out.println("\t\tIdentity:     " + y.getIdentity());
                System.out.println("\t\tGaps:         " + y.getGap());
                System.out.println("\t\tStrand:       " + y.getStrand());

                System.out.println("\t\tQuery Start:  " + y.getQryStart());
                System.out.println("\t\tQuery End:    " + y.getQryEnd());
                System.out.println("\t\tQuery seq:    " + y.getQrySeq());

                System.out.println("\t\tSbjct Start:  " + y.getSbjStart());
                System.out.println("\t\tSbjct End:    " + y.getSbjEnd());
                System.out.println("\t\tSbjct seq:    " + y.getSbjSeq());

                System.out.println();
            }
            System.out.println();
        }
        System.out.println();
    }

                        // END OF PARSER DEF

// PARSER SPECIFICATIONS BEGIN HERE
/** This method does all parsing work. User must call this method before
 *  calling any other methods to extract interested information about
 *  the blast search.
 */
  final public void parseBlast() throws ParseException {
    Token qryID = null, qryLen = null;
    Token dbPath = null, dbSize = null, dbLett = null;
    Token program = null, matrix = null, gaps = null;
    // aHomolog has subID and subLen
    Token subID = null, subLen = null;
    HomologItem aHomolog = null;
    // aAlign has following info properties and Alignment
    Token score = null, evalue = null, identity = null, gap = null, strand = null;
    Alignment aAlign = null;     /////////////////////////
    Token qryStart = null, qrySeq = null, qryEnd = null;
    Token sbjStart = null, sbjSeq = null, sbjEnd = null;
    /** hold query sequence alignment coordinates */
    ArrayList qryOffsets = new ArrayList();
    /** hold subject sequence alignment coordinates */
    ArrayList sbjOffsets = new ArrayList();
    /** hold temp qry sequence */
    StringBuffer querySeq = new StringBuffer();
    /** hold temp subject sequence */
    StringBuffer subjectSeq = new StringBuffer();
    /** flag the first alignment for a given HomologItem */
    boolean firstAlign = true;
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PROGRAM:
        program = jj_consume_token(PROGRAM);
            srchInfoHT.put("program", program.image);
            //System.out.println("Program:          " + program.image);

        break;
      case ID:
        qryID = jj_consume_token(ID);
            // save query name upon a match
            qryInfoHT.put("qry_name", qryID.image);
            //System.out.println("Query ID:         " + qryID.image);

        break;
      case DBPATH:
        dbPath = jj_consume_token(DBPATH);
            srchInfoHT.put("dbpath", dbPath.image);
            //System.out.println("Database path:    " + dbPath.image);

        break;
      case SUB_ID:
        subID = jj_consume_token(SUB_ID);
            aHomolog = new HomologItem();
            aHomolog.setSubID(subID.image);
            //System.out.println("SUBJ ID:          " + subID.image);

        break;
      case SUB_LENGTH:
        subLen = jj_consume_token(SUB_LENGTH);
            aHomolog.setSubLen(Integer.parseInt(subLen.image));
            homologList.add(aHomolog);
            //System.out.println("SUBJ LENGTH:     " + subLen.image);

        break;
      case ALIGN_SCORE:
        score = jj_consume_token(ALIGN_SCORE);
            if (!firstAlign) {
                // save the previous alignment of the given HomologItem
                aAlign.setQryStart(Integer.parseInt((String)qryOffsets.get(0)));
                aAlign.setQryEnd(Integer.parseInt(
                                 (String)qryOffsets.get(qryOffsets.size() - 1)));
                aAlign.setQrySeq(querySeq.toString());

                aAlign.setSbjStart(Integer.parseInt((String)sbjOffsets.get(0)));
                aAlign.setSbjEnd(Integer.parseInt(
                                 (String)sbjOffsets.get(sbjOffsets.size() - 1)));
                aAlign.setSbjSeq(subjectSeq.toString());
                /*
                {   // Output query offsets for exam
                    ListIterator iterator = qryOffsets.listIterator();
                    while (iterator.hasNext()) {
                        System.out.println("Qry Element: " + (String)iterator.next());
                    }
                }

                {   // Output sbjct offsets for exam
                    ListIterator iterator = sbjOffsets.listIterator();
                    while (iterator.hasNext()) {
                        System.out.println("Subj Element: " + (String)iterator.next());
                    }
                }
                */
                // then reset the two offset array list and two temp sequences
                qryOffsets.clear();
                sbjOffsets.clear();
                querySeq = querySeq.delete(0, querySeq.length());
                subjectSeq = subjectSeq.delete(0, subjectSeq.length());
            } // This block has to be repeated for the last one
            firstAlign = false;
            aAlign = new Alignment();
            aHomolog.addAlignItem(aAlign);
            aAlign.setScore(score.image);
            //System.out.println("Score:           " + score.image);

        break;
      case ALIGN_EVALUE:
        evalue = jj_consume_token(ALIGN_EVALUE);
            aAlign.setEvalue(evalue.image);
            //System.out.println("E-value:         " + evalue.image);

        break;
      case ALIGN_IDENTITY:
        identity = jj_consume_token(ALIGN_IDENTITY);
            aAlign.setIdentity(identity.image);
            //System.out.println("Identity:        " + identity.image);

        break;
      case ALIGN_GAPS:
        gap = jj_consume_token(ALIGN_GAPS);
            aAlign.setGap(gap.image);
            //System.out.println("Gaps:            " + gap.image);

        break;
      case ALIGN_STRAND:
        strand = jj_consume_token(ALIGN_STRAND);
            aAlign.setStrand(strand.image);
            //System.out.println("Strand:         " + strand.image);

        break;
      case QRY_START:
        qryStart = jj_consume_token(QRY_START);
            qryOffsets.add(qryStart.image);
            //System.out.println("Query start = " + qryStart.image);

        break;
      case QRY_SEQ:
        qrySeq = jj_consume_token(QRY_SEQ);
            querySeq.append(qrySeq.image);
            //System.out.println("Query seq = " + qrySeq.image);

        break;
      case QRY_END:
        qryEnd = jj_consume_token(QRY_END);
            qryOffsets.add(qryEnd.image);
            //System.out.println("Query end = " + qryEnd.image);

        break;
      case SUBJ_START:
        sbjStart = jj_consume_token(SUBJ_START);
            sbjOffsets.add(sbjStart.image);
            //System.out.println("Sbjct start = " + sbjStart.image);

        break;
      case SUBJ_SEQ:
        sbjSeq = jj_consume_token(SUBJ_SEQ);
            subjectSeq.append(sbjSeq.image);
            //System.out.println("Sbjct seq = " + sbjSeq.image);

        break;
      case SUBJ_END:
        sbjEnd = jj_consume_token(SUBJ_END);
            sbjOffsets.add(sbjEnd.image);
            //System.out.println("Sbjct end = " + sbjEnd.image);

        break;
      case MATRIX:
        matrix = jj_consume_token(MATRIX);
            //System.out.println("Matrix:           " + matrix.image);
            srchInfoHT.put("matrix", matrix.image);
        break;
      case GAP:
        gaps = jj_consume_token(GAP);
            //System.out.println("Gaps:             " + gaps.image);
            srchInfoHT.put("gaps", gaps.image);
        break;
      case DBSIZE:
        dbSize = jj_consume_token(DBSIZE);
            //System.out.println("Database size:    " + dbSize.image);
            srchInfoHT.put("dbsize", dbSize.image);
        break;
      case QRYLENGTH:
        qryLen = jj_consume_token(QRYLENGTH);
            //System.out.println("Query length:     " + qryLen.image);
            qryInfoHT.put("qry_length", qryLen.image);
        break;
      case DBLETTERS:
        dbLett = jj_consume_token(DBLETTERS);
            //System.out.println("Database letters: " + dbLett.image);
            srchInfoHT.put("dbletters", dbLett.image);
        break;
      default:
        jj_la1[0] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PROGRAM:
      case ID:
      case DBPATH:
      case QRYLENGTH:
      case SUB_ID:
      case SUB_LENGTH:
      case ALIGN_EVALUE:
      case ALIGN_IDENTITY:
      case ALIGN_STRAND:
      case ALIGN_SCORE:
      case ALIGN_GAPS:
      case QRY_START:
      case QRY_SEQ:
      case QRY_END:
      case SUBJ_START:
      case SUBJ_SEQ:
      case SUBJ_END:
      case MATRIX:
      case GAP:
      case DBSIZE:
      case DBLETTERS:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_1;
      }
    }
    jj_consume_token(0);

        // save the last one parsed
                aAlign.setQryStart(Integer.parseInt((String)qryOffsets.get(0)));
                aAlign.setQryEnd(Integer.parseInt(
                                 (String)qryOffsets.get(qryOffsets.size() - 1)));
                aAlign.setQrySeq(querySeq.toString());

                aAlign.setSbjStart(Integer.parseInt((String)sbjOffsets.get(0)));
                aAlign.setSbjEnd(Integer.parseInt(
                                 (String)sbjOffsets.get(sbjOffsets.size() - 1)));
                aAlign.setSbjSeq(subjectSeq.toString());
        /*
        // print out query info
        System.out.println("Query sequence name:    " + qryInfoHT.get("qry_name"));
        System.out.println("Query sequence length:  " + qryInfoHT.get("qry_length"));
        System.out.println();

        // print out search info
        System.out.println("Blast program:     " + (String)srchInfoHT.get("program"));
        System.out.println("Score matrix:      " + (String)srchInfoHT.get("matrix"));
        System.out.println("Gap penalties:     " + (String)srchInfoHT.get("gaps"));
        System.out.println("Database path:     " + (String)srchInfoHT.get("dbpath"));
        System.out.println("Database size:     " + (String)srchInfoHT.get("dbsize") + " sequences");
        System.out.println("Database letters:  " + (String)srchInfoHT.get("dbletters") + " residues");
        System.out.println();
        
        //System.out.println(srchInfoHT.toString());

        // print out how many homologs found
        System.out.println("TOTAL HOMOLOGS PARSED : " + homologList.size());
        
        // print out details
        for (int i=0; i<homologList.size(); i++) {  // loop through all homologs
            int count = i + 1;
            System.out.println("Homolog : " + count);
            System.out.println("------------");
            HomologItem x = (HomologItem)homologList.get(i);
            System.out.println("\tHomolog ID:    " + x.getSubID());
            System.out.println("\tHomolog LEN:   " + x.getSubLen());
            System.out.println();
            for (int j=0; j<x.getSize(); j++) { // each homolog has one or more Alignment
                count = j + 1;
                System.out.println("\t\tLOCAL ALIGNMENT : " + count);
                System.out.println("\t\t--------------------");
                Alignment y = (Alignment)x.getAlignItem(j);
                System.out.println("\t\tScore:        " + y.getScore());
                System.out.println("\t\tEvalue:       " + y.getEvalue());
                System.out.println("\t\tIdentity:     " + y.getIdentity());
                System.out.println("\t\tGaps:         " + y.getGap());
                System.out.println("\t\tStrand:       " + y.getStrand());

                System.out.println("\t\tQuery Start:  " + y.getQryStart());
                System.out.println("\t\tQuery End:    " + y.getQryEnd());
                System.out.println("\t\tQuery seq:    " + y.getQrySeq());

                System.out.println("\t\tSbjct Start:  " + y.getSbjStart());
                System.out.println("\t\tSbjct End:    " + y.getSbjEnd());
                System.out.println("\t\tSbjct seq:    " + y.getSbjSeq());
                
                System.out.println();
            }
            System.out.println();
        }
        System.out.println();
        */

  }

  public BlastParserTokenManager token_source;
  ASCII_CharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[2];
  final private int[] jj_la1_0 = {0x0,0x0,};
  final private int[] jj_la1_1 = {0x3cb2fff1,0x3cb2fff1,};

  public BlastParser(java.io.InputStream stream) {
    jj_input_stream = new ASCII_CharStream(stream, 1, 1);
    token_source = new BlastParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  public BlastParser(java.io.Reader stream) {
    jj_input_stream = new ASCII_CharStream(stream, 1, 1);
    token_source = new BlastParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  public BlastParser(BlastParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  public void ReInit(BlastParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;

  final public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[62];
    for (int i = 0; i < 62; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 2; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 62; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

}
