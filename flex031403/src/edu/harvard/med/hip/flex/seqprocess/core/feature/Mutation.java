/*
 * Mutation.java
 *
 * Created on September 24, 2002, 12:02 PM
 */

package edu.harvard.med.hip.flex.seqprocess.core.feature;


import  edu.harvard.med.hip.flex.util.*;
import  edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.seqprocess.core.blast.*;
import edu.harvard.med.hip.flex.seqprocess.core.sequence.*;
import edu.harvard.med.hip.flex.seqprocess.util.*;
import edu.harvard.med.hip.flex.seqprocess.programs.needle.*;
import java.util.*;
import java.sql.*;
/**
 *
 * @author  htaycher
 */
public abstract class Mutation
{
   
   public static final int TYPE_NONE = 0;
    
    public static final int FLAG_POLYMORPHISM = -1;
    public static final int FLAG_MUTATION = 1;
    
    //mutation type
    public static final int DNA = 0;
    public static final int AA = 2;
    public static final int RNA = 1;
    
    
    //aa mutation type
   public static final int TYPE_AA_NO_TRANSLATION = 1;
      public static final int TYPE_AA_OUT_OF_FRAME_TRANSLATION = 2;
      public static final int TYPE_AA_FRAMESHIFT = 3;
      public static final int TYPE_AA_INSERTION = 4;
      public static final int TYPE_AA_INSERTION_COMPLEX = 5;
      public static final int TYPE_AA_SILENT = 6;
      public static final int TYPE_AA_POST_ELONGATION = 7;
      public static final int TYPE_AA_SILENT_CONSERVATIVE = 8;
      public static final int TYPE_AA_TRUNCATION = 9;
      public static final int TYPE_AA_DELETION = 10;
      public static final int TYPE_AA_DELETION_COMPLEX = 11;
      public static final int TYPE_AA_SUBSTITUTION = 12;
      public static final int TYPE_AA_CONSERVATIVE = 13;
   public static final int TYPE_AA_NONCONSERVATIVE = 14;
    
    //rna mutastion type
    
    public static final int TYPE_RNA_INFRAME= 15;
    public static final int TYPE_RNA_FRAMESHIFT= 16;
    public static final int TYPE_RNA_INFRAME_DELETION=17;
    public static final int TYPE_RNA_INFRAME_INSERTION=18;
    public static final int TYPE_RNA_INFRAME_STOP_CODON=19;
    public static final int TYPE_RNA_FRAMESHIFT_DELETION=20;
    public static final int TYPE_RNA_FRAMESHIFT_INSERTION=21;
    public static final int TYPE_RNA_FRAMESHIFT_STOP_CODON=22;
    public static final int TYPE_RNA_SILENT= 24;
    public static final int TYPE_RNA_NONSENSE= 25;
    public static final int TYPE_RNA_MISSENSE = 26;
    
     public static final int TYPE_NOTRESOLVED=23; 
    //owner of mutations 
      public static final int OWNER_FULL_SEQUENCE=0;
      public static final int OWNER_BLAST_HIT=1;
      
      //type of mutation analysis
      public static final int TYPE_BLAST_HIT=1;
      public static final int TYPE_NEEDLE = 2;
      
    protected int         m_id =-1;
    protected int         m_position =-1;// start of mutation (on object sequence)
    protected int         m_length=-1;// – length of mutation (optional)
    protected int         m_type = -1;// //rna ! AA 
    protected String      m_query_str = null;// – mutation bases of object sequence
    protected String      m_subject_str = null;// mutation bases of subject sequence
    protected int         m_sequenceid = -1;
    protected int         m_hitid = -1;
    protected int         m_hittype = 2;
    protected int         m_flag = -1;
    protected int         m_number = -1;
    protected int         m_mutation_type = -1;
    
    public Mutation(){}
    /** Creates a new instance of Mutation */
    public Mutation(int id) throws FlexDatabaseException
    {
        m_id = id;
        
        String sql = "select  position,length,subject,query,type, hitid,hittype,sequenceid, flag,mutationtype,mutationnumber from mutation where mutationid = "+id;
        
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                m_type = rs.getInt("type");
                
                m_length = rs.getInt("length");
                m_subject_str = rs.getString("subject");//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
                m_query_str = rs.getString("query");
                m_flag = rs.getInt("flag");
                m_hitid = rs.getInt("hitid");
                m_hittype= rs.getInt("hittype");
                m_sequenceid = rs.getInt("sequenceid");
               m_mutation_type = rs.getInt("mutationtype");
               m_number = rs.getInt("mutationnumber");
               m_position = rs.getInt("POSITION");
            }
        } catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new FlexDatabaseException("Error occured while initializing mutation with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
   
    
     public Mutation(int num,int fl,int pos, int ln,String obj, String subj, int hit, int seq, int id,int type, int hittype)
    {
        m_position = pos;
        m_query_str = obj;
        m_subject_str = subj;
        m_hitid =hit;
        m_hittype = hittype;
        m_sequenceid = seq;
        m_flag = fl;
        m_type =type;
        
        m_id = id;
        m_length = ln;
        m_number =num;
    }
 
    
    
     
    public void insert(Connection conn) throws FlexDatabaseException
    {
         if (m_id == -1) m_id = FlexIDGenerator.getID("mutationid");
       
        PreparedStatement pstmt = null;
        
        String sql = "INSERT INTO mutation (mutationid, position, type, length, "+
        "subject,query,hitid,hittype,sequenceid, flag, mutationtype, mutationnumber) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)" ;
      
        try
        {
           
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, m_id);
            pstmt.setInt(2, m_position);
            pstmt.setInt(3, m_type);
            pstmt.setInt(4, m_length);
            if (m_subject_str.length()>300) m_subject_str = m_subject_str.substring(0,299);
            pstmt.setString(5, m_subject_str);
             if (m_query_str.length()>300) m_query_str = m_query_str.substring(0,299);
            pstmt.setString(6, m_query_str);
            pstmt.setInt(7, m_hitid);
            pstmt.setInt(8,m_hittype);
            pstmt.setInt(9, m_sequenceid);
            pstmt.setInt(10, m_flag);
            pstmt.setInt(11, m_mutation_type);
            pstmt.setInt(12, m_number);
            DatabaseTransaction.executeUpdate(pstmt);
              
            
        } catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
          
            DatabaseTransaction.closeStatement(pstmt);
        }
    }
    
    
    public void updateFlag(Connection conn) throws FlexDatabaseException
    {
        Statement stmt =null;
        try
        {
            String sql = "UPDATE mutation SET flag = "
            + m_flag +" WHERE mutationid = "+ m_id;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception sqlE)
        {
            throw new FlexDatabaseException("Error occured while calling updateStatus method for mutation "+m_id+"\n"+sqlE);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    
    public void         setHitId(int s){  m_hitid = s ;}
    public void         setHitType(int s){  m_hittype = s ;}
    
    
    public int         getId(){ return m_id ;}
    public int         getSequenceId(){ return m_sequenceid ;}
    public int         getPosition(){ return m_position ;}// start of mutation (on object sequence)
    public int          getLength(){ return m_length;}// – length of mutation (optional)
    public int         getType(){ return   m_type ;}// – mutation type 
    public String       getQueryStr(){ return    m_query_str ;}// – mutation bases of object sequence
    public String      getSubjectStr(){ return   m_subject_str ;}// mutation bases of subject sequence
    public int          getFlag(){ return m_flag;} 
    public int          getNumber(){ return m_number;}
    public int          getMutationType(){ return m_mutation_type;}
    public String       getMutationTypeAsString()
    {
        
        switch (m_mutation_type)
        {
            case TYPE_AA_NO_TRANSLATION : return  "No Translation";
            case TYPE_AA_OUT_OF_FRAME_TRANSLATION : return  "OUT_OF_FRAME_TRANSLATION";
            case TYPE_AA_FRAMESHIFT : return  "Frameshift";
            case TYPE_AA_INSERTION : return  "Insertion";
            case TYPE_AA_INSERTION_COMPLEX : return  "Insertion complex";
            case TYPE_AA_SILENT : return  "Silent";
            case TYPE_AA_POST_ELONGATION : return  "Post Elongation";
            case TYPE_AA_SILENT_CONSERVATIVE : return  "Silent Conservative";
            case TYPE_AA_TRUNCATION : return  "Truncation";
            case TYPE_AA_DELETION : return  "Deletion";
            case TYPE_AA_DELETION_COMPLEX : return  "Deletion Complex";
            case TYPE_AA_SUBSTITUTION : return  "Substitution";
            case TYPE_AA_CONSERVATIVE : return  "Conservative Substitution";
            case TYPE_AA_NONCONSERVATIVE : return  "Non-Conservative Substitution";

            //rna mutastion type
            case TYPE_RNA_INFRAME: return  "Inframe";
            case TYPE_RNA_FRAMESHIFT: return  "Frameshift";
            case TYPE_RNA_INFRAME_DELETION: return "Inframe: Deletion";
            case TYPE_RNA_INFRAME_INSERTION: return "Inframe: Insertion";

            case TYPE_RNA_INFRAME_STOP_CODON: return "Inframe: Stop Codon";
             case TYPE_RNA_FRAMESHIFT_DELETION: return "Frameshift: Deletion";
            case TYPE_RNA_FRAMESHIFT_INSERTION: return "Frameshift: Insertion";

            case TYPE_RNA_FRAMESHIFT_STOP_CODON: return "Frameshift: Stop Codon";
            case TYPE_RNA_SILENT: return "Silent";
            case TYPE_RNA_NONSENSE: return "Nonsense";
            case TYPE_RNA_MISSENSE: return "Missense";
            default  : {return "Not known"; }
        }
         
    
    }
    
    
    public String toString()
    {
        String res= "\n\nMutation desc.\n id: " +  m_id +
        "\n position: "+m_position +" length: "+ m_length+"\n type: "+m_type
        +"\n mut ori: "+ m_query_str 
        +" \nori: "+m_subject_str 
        +"\n sequence id: "+ m_sequenceid
        +" \nhit id: " + m_hitid + "\nhit type: " + m_hittype
        +"\n mut number: "+ m_number
        + " \nmutation type: "+ getMutationTypeAsString() +"\n" ;
        return res;
    }
    
    
     public String toHTMLString()
    {
        String res = "";
        
        res = "<tr><td> id: </td><td>"+m_id + "</td></tr>" + 
        "<tr><td>Position</td><td>"  + m_position + "</td></tr>" + 
        "<tr><td>Length</td><td>" +m_length + "</td></tr>" ;
        if (m_query_str ==null)
             res+="<tr><td>Query Str</td><td>&nbsp;</td></tr>"; 
        else
            res+="<tr><td>Query Str</td><td>" +m_query_str + "</td></tr>" ;
        if (m_subject_str ==null)
             res+="<tr><td>Query Str</td><td>&nbsp;</td></tr>"; 
        else
            res+="<tr><td>Subject Str</td><td>"+m_subject_str +"</td></tr>" ;
        res+="<tr><td>Mutation</td><td>" + getMutationTypeAsString() + "</td></tr>" ;
        return res;
    }
    //---------------------------------
     public ArrayList   getAllMutations(int id, int owner) throws FlexDatabaseException
    {
         String sql = "select  position,length,subject,query,type "+
                    "hitid,hittype,sequenceid, flag,upstream,downstream,"+
                    "codonori,codonmut,codonpos, mutationtype,mutationnumber from mutation where " ;
        
         
         if (owner == Mutation.OWNER_BLAST_HIT)
             sql += "hitid = "+ id;
         else if (owner ==Mutation.OWNER_FULL_SEQUENCE)
             sql += " sequenceid = " + id;
        ArrayList res = new ArrayList();
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                int mutid = rs.getInt("mutationid");
                 int type = rs.getInt("type");
                 int position = rs.getInt("position");
                 int length = rs.getInt("length");
                 String  subject_str = rs.getString("subject");//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
                 String query_str = rs.getString("query");
                 int flag = rs.getInt("flag");
                 int sequenceid = rs.getInt("sequenceid");
                 int hitid = rs.getInt("hitid");
                 int hittype = rs.getInt("hittype");
                 String upstream = rs.getString("upstream");
                 String downstream = rs.getString("downstream");
                 String codon_ori = rs.getString("codonori");
                String codon_mut = rs.getString("codonmut");
                 int codon_pos = rs.getInt("codonpos");
                 int mutation_type = rs.getInt("mutationtype");
                 int number = rs.getInt("mutationnumber");
                 if (type == Mutation.RNA)
                 {
                     RNAMutation mut = new RNAMutation(
                                             number, flag, 
                                            position, length,
                                            query_str, subject_str,
                                            hitid, sequenceid, mutid,
                                            mutation_type,
                         upstream,  downstream, codon_ori,codon_mut, codon_pos,
                        hittype);
                     res.add(mut);
                 }
                 else     if (type == Mutation.AA)
                 {
                     AAMutation mut = new AAMutation(
                                            number, flag, 
                                            position, length,
                                            query_str, subject_str,
                                            hitid, sequenceid, mutid,
                                            mutation_type,hittype);
                     res.add(mut);
                 }
                
            }
        } catch (Exception sqlE)
        {
            throw new FlexDatabaseException("Error occured while initializing blasthit with id: "+m_id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        return res;
    }
     
     /*
     
    public static ArrayList run_mutation_analysis(BlastResult res_n, BlastResult res_p,
         FullSequence full_sequence, TheoreticalSequence t_sequence)


         
    {
        ArrayList res = new ArrayList();
        ArrayList char_arrays = prepareData( res_n, res_p);
        boolean isInMutation = false;
        int query_n_start = ((BlastHit)res_n.getHits().get(0)).getQStart();
        int subject_n_start = ((BlastHit)res_n.getHits().get(0)).getSStart();
        int mut_start = 0;
        int mut_count = 0;
        String q_allel = "";
        String s_allel = "";
        char[] sequence_query_n ;char[] sequence_subject_n  ;
        int length  = 0;
        AAMutation cur_aa_mutation = null;
        RNAMutation cur_rna_mutation = null;
        //find longer sequence
        if ( char_arrays.get(0) != null && char_arrays.get(1) != null)
        {
            sequence_query_n = (char[]) char_arrays.get(0);
            sequence_subject_n = (char[]) char_arrays.get(1);
            length = ( sequence_query_n.length >= sequence_subject_n.length) ? sequence_query_n.length -1 :sequence_subject_n.length -1;
        }
        else 
            return res;
        
        
        //check if sequence is good enough to be subject for mutation analyzez
     //   if ( ! goodSequence( full_sequence,  t_sequence, res_p, res, res_n) ) return res;
        try{
        for (int count = 0; count < length; count++ )
        {
            if ( sequence_query_n[count + query_n_start - 1] == sequence_subject_n[count + subject_n_start - 1])
            {
                // do nothing
                if (isInMutation)//mutation finished
                {
                    mut_count++;
                    int upstream_start = 0;
                    if  (mut_start - query_n_start > RNAMutation.RNA_STREAM_RANGE)
                    {
                        upstream_start = RNAMutation.RNA_STREAM_RANGE;
                    }
                    else
                         upstream_start = mut_start - query_n_start ;
                    int downstream_end = (mut_start -2 + s_allel.length() + RNAMutation.RNA_STREAM_RANGE < sequence_query_n.length)?
                        RNAMutation.RNA_STREAM_RANGE: sequence_query_n.length -1;
                    int codon_start =  (int)Math.ceil(count - subject_n_start - 1); 
                    
                    String up =new String(sequence_query_n, mut_start - upstream_start - 1 , upstream_start);
                    String dn =    new String(sequence_query_n, mut_start - 1 + s_allel.length() , downstream_end);
                    String cori =     new String (sequence_subject_n, codon_start , 3);
                    String corm =    new String(sequence_query_n, ( (int)Math.ceil(count - query_n_start - 1)) , 3); //codon mutant
                        
                    cur_rna_mutation = new RNAMutation(mut_count,Mutation.FLAG_MUTATION,
                        mut_start, 
                        s_allel.length(),
                        q_allel, 
                        s_allel,
                        res_n.getId(), 
                        full_sequence.getId(), -1,-1,
                        up, 
                        dn,
                        cori,
                        corm, //codon mutant
                        (count - subject_n_start ) % 3 +1 ,
                        TYPE_BLAST_HIT) ;
                    System.out.println(cur_rna_mutation.toString());
                    res.add(cur_rna_mutation);
                    if (    cur_rna_mutation.getType() != Mutation.TYPE_RNA_SILENT )
                    {
                        String atr =  SequenceManipulation.getTranslation( corm, SequenceManipulation.ONE_LETTER_TRANSLATION_NO_SPACE);
                        String am =  SequenceManipulation.getTranslation(cori, SequenceManipulation.ONE_LETTER_TRANSLATION_NO_SPACE);
                        AAMutation cur_AA_mut =  new AAMutation(mut_count,Mutation.FLAG_MUTATION,
                            (int) Math.ceil(mut_start / 3) +1, 
                            (int) Math.ceil(cori.length() / 3 ),
                            atr, 
                            am,
                            res_p.getId(), 
                            full_sequence.getId(),
                            -1,-1
                            ,TYPE_BLAST_HIT);
                        res.add(cur_AA_mut);
                           
                         System.out.println(cur_AA_mut.toString());
                    }
                    
                    mut_start = -1;
                    s_allel="";
                    q_allel="";
                    isInMutation =false;
                }
            }
            else 
            {
                isInMutation = true;
                mut_start = count - query_n_start + 2;
                if (isInMutation)
                {
                    if (sequence_query_n[count] != '-' && sequence_query_n[count] !='[')
                        q_allel += sequence_query_n[count];
                    if (sequence_subject_n[count] != '-' && sequence_subject_n[count] !='[')
                        s_allel += sequence_subject_n[count];
                }
            }
        }
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        
        return res;
    }
    
    
    */
    public static ArrayList run_mutation_analysis(NeedleResult res_needle,
         int full_sequence_id, int t_sequence_id)
    
    {
        ArrayList res = new ArrayList();
        int length = 0;
        //prepare char arrays
        ArrayList char_arrays = prepareData(res_needle);
        char[] sequence_query_n ;char[] sequence_subject_n ;
        if ( char_arrays.get(0) != null && char_arrays.get(1) != null)
        {
            sequence_query_n = (char[]) char_arrays.get(0);
            sequence_subject_n = (char[]) char_arrays.get(1);
            length = ( sequence_query_n.length >= sequence_subject_n.length) ? sequence_query_n.length -1 :sequence_subject_n.length -1;
        }
        else 
            return res;
        
        boolean isInMutation = false;
       
        int mut_start = -1;        int mut_count = 0; 
        int codon_number = 0 ; int codon_start_mutation = 0 ;
        String q_allel = "";        String s_allel = "";
        StringBuffer q_sequence = new StringBuffer(); 
        StringBuffer s_sequence = new StringBuffer();
        int q_position = 0; int s_position = 0;
        
       
        AAMutation cur_aa_mutation = null;        RNAMutation cur_rna_mutation = null;
      
        try{
            for (int count = 0; count < length; count++ )
            {
                //preserve sequence and position
                if ( ! isWrongChar(sequence_query_n[count])  )
                {
                    q_sequence.append(sequence_query_n[count]);
                    q_position++;
                 }
                 if ( ! isWrongChar( sequence_subject_n[count] ))
                 {
                     s_sequence.append(sequence_subject_n[count]);
                     s_position++;
                 }
                if (s_position  % 3 == 0)  codon_number++;
                
                
                if ( sequence_query_n[count] == sequence_subject_n[count ])
                {
                    // do nothing
                    if (isInMutation)//mutation finished
                    {
                        mut_count++;
                        //get upstream string
                        int upstream_start = ( (q_position - q_allel.length() - RNAMutation.RNA_STREAM_RANGE) > 0 ) ? q_position - q_allel.length() - RNAMutation.RNA_STREAM_RANGE: 0;
                        String up = q_sequence.toString().substring( upstream_start, q_position - q_allel.length() - 1);
                        //get downstream string
                        int pos = count; String dn = ""; int dn_length=0;
                        while(true)
                        {
                            if ( ! isWrongChar( sequence_query_n[pos] ))
                            {
                                dn += sequence_query_n[pos] ;
                                dn_length++;
                                if (dn_length == RNAMutation.RNA_STREAM_RANGE || pos == sequence_query_n.length - 1)
                                    break;
                            }
                            pos++;
                        }
                                               
                      //  int codon_start = 3 * ( (int)Math.ceil( (count - s_allel.length() )/ 3 ) ); 

                        String cori = new String (sequence_subject_n, ( codon_start_mutation - 1) * 3  , 3);
                        String corm = new String(sequence_query_n, (codon_start_mutation -1 ) * 3  , 3); //codon mutant
                        cori =  cori.replace( ' ','-');
                        corm = corm.replace( ' ','-');
                        cur_rna_mutation = new RNAMutation
                        (
                            mut_count,Mutation.FLAG_MUTATION,
                            mut_start, 
                            s_allel.length(),
                            q_allel, 
                            s_allel,
                            res_needle.getId(), 
                            full_sequence_id, -1,-1,
                            up, 
                            dn,
                            cori,
                            corm, //codon mutant
                            s_position % 3 +1 //codon pos
                            ,TYPE_NEEDLE) ;
                        System.out.println(cur_rna_mutation.toString());
                        res.add(cur_rna_mutation);
                        if (    cur_rna_mutation.getType() != Mutation.TYPE_RNA_SILENT )
                        {
                            String atr =  SequenceManipulation.getTranslation( corm, SequenceManipulation.ONE_LETTER_TRANSLATION_NO_SPACE);
                            String am =  SequenceManipulation.getTranslation(cori, SequenceManipulation.ONE_LETTER_TRANSLATION_NO_SPACE);
                            AAMutation cur_AA_mut =  new AAMutation(mut_count,Mutation.FLAG_MUTATION,
                                codon_start_mutation, 
                                (int) Math.ceil(cori.length() / 3 ),
                                atr, 
                                am,
                                res_needle.getId(), 
                                full_sequence_id,
                                -1,-1
                                ,TYPE_NEEDLE);
                            res.add(cur_AA_mut);

                             System.out.println(cur_AA_mut.toString());
                        }

                        mut_start = -1;
                        s_allel="";
                        q_allel="";
                        isInMutation =false;
                    }
                }
                else //not equal
                {
                    isInMutation = true;
                    if (mut_start == -1)
                    {
                        mut_start = s_position ;
                        codon_start_mutation = codon_number;
                    }
                    if (isInMutation)
                    {
                        if ( ! isWrongChar( sequence_query_n[count] ) )
                            q_allel += sequence_query_n[count];
                        if ( ! isWrongChar(sequence_subject_n[count] ) )
                            s_allel += sequence_subject_n[count];
                    }
       
                }
                
            }
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        
        return res;
    }
    
    
    
    
    
    //used if only blast results provided
    private static  ArrayList  prepareData( BlastResult res_n, BlastResult res_p)
    {
        ArrayList res = new ArrayList();
        char[] sequence_query_n = null;
        char[] sequence_subject_n = null;
            
        BlastHit hit = null;
        try
        {
            if (res_n.getHits().size() > 0 )
            {
                hit = (BlastHit) res_n.getHits().get(0);
                sequence_query_n = new char[hit.getQSequence().length() + hit.getQStart()];
                sequence_subject_n = new char[hit.getSSequence().length() + hit.getSStart()];

                hit.getQSequence().getChars(0,hit.getQSequence().length(),sequence_query_n,hit.getQStart() -1);
                hit.getSSequence().getChars(0,hit.getSSequence().length(),sequence_subject_n,hit.getSStart() -1);
                 res.add(sequence_query_n);
                 res.add(sequence_subject_n);
            }
            
        } catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return res;
    }
    
    
    
    
    //used if needle results provided
    private static  ArrayList  prepareData( NeedleResult res_needle)
    {
        ArrayList res = new ArrayList();
        char[] sequence_query_n = null;
        char[] sequence_subject_n = null;
              
        
        try
        {
           /* 
            sequence_query_n = new char[res_needle.getQuery().length() ];
            sequence_subject_n = new char[res_needle.getSubject().length() ];
              res_needle.getQuery().getChars(0,res_needle.getQuery().length(),sequence_query_n,0);
                res_needle.getSubject().getChars(0,res_needle.getSubject().length(),sequence_subject_n, 0);
            */
            sequence_query_n = res_needle.getQuery().toCharArray();
            sequence_subject_n = res_needle.getSubject().toCharArray();
            res.add(sequence_query_n);
            res.add(sequence_subject_n);
        }
         catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return res;
    }
    
    private static boolean isWrongChar(char ch)
    {
         if (ch == '-' || ch =='['  || ch ==' ')
             return true;
         else
             return false;
    }
    
    //function checks for proper start codon
    // and best frame : should be +1
    /*
    private static boolean  goodSequence( FullSequence full_sequence,  TheoreticalSequence t_sequence, 
                                BlastResult res_p, ArrayList res, BlastResult res_n)
    {
        BlastHit bh = (BlastHit) res_p.getHits().get(0);
        boolean isFrame = (bh.getQFrame() == 1 && bh.getQStrand() ==1 );
 
        String f_codon = full_sequence.getCodon(1, bh.getQStrand(), bh.getQFrame());
        String t_f_codon =  "";//t_sequence.getCodingSequence().getCodon(1, 1, 1 );
        boolean isCodon = f_codon.equalsIgnoreCase(t_f_codon);
        
        if (isCodon && isFrame) 
            return true;
        else if ( ! isCodon  )
        {
                
                 RNAMutation cur_rna_mutation = new RNAMutation(1,
                        Mutation.FLAG_MUTATION,
                        -1, 
                        -1,
                        "", 
                        "",
                        res_n.getId(), 
                        full_sequence.getId(), -1,-1,
                        "", 
                        "",
                        t_f_codon,
                        f_codon, //codon mutant
                        -1) ;
                    System.out.println(cur_rna_mutation.toString());
                    res.add(cur_rna_mutation);
                    String atr =  SequenceManipulation.getTranslation( f_codon, SequenceManipulation.ONE_LETTER_TRANSLATION_NO_SPACE);
                    String am =  SequenceManipulation.getTranslation(t_f_codon, SequenceManipulation.ONE_LETTER_TRANSLATION_NO_SPACE);
                    AAMutation cur_AA_mut =  new AAMutation(1,Mutation.FLAG_MUTATION,
                        1, 
                        1,
                        atr, 
                        am,
                        res_p.getId(), 
                        full_sequence.getId(),
                        -1,-1
                        );
                    res.add(cur_AA_mut);

                    System.out.println(cur_AA_mut.toString());
        }
       full_sequence.setStatus( FullSequence.STATUS_MUTATIONS_CLEARED);
       full_sequence.setQuality( FullSequence.QUALITY_RECOMENDED_BAD);
       return false;  
    }
    */
    public static void main(String [] args)
    {
     
           // DatabaseTransaction t = DatabaseTransaction.getInstance();
           // Connection conn =  t.requestConnection();
           // ArrayList hits = new ArrayList();
            //  RNAMutation rn = new RNAMutation(1, Mutation.FLAG_MUTATION,3,1,"a","t",-1,31461, 
           //-1, Mutation.RNA,"aaaaaaaaaaaaaaaa","kkkkkk","aat","ttt",2 );
           //rn.insert(conn);
          //  AAMutation am = new AAMutation(1, Mutation.FLAG_MUTATION,3,1,"A","K",-1,31461, 
          // -1, Mutation.AA);
          //   rn.insert(conn);
          //  conn.commit();
         //   AAMutation mut = new AAMutation(14);
          //  RNAMutation mut1 = new RNAMutation(15);
        String queryFile = "e:\\htaycher\\sequencing\\needle\\ex1.txt";
        NeedleResult res_needle = null;
        try
        {
             res_needle = new NeedleResult();
            
            NeedleParser.parse(queryFile,res_needle);
            ArrayList mutations = Mutation.run_mutation_analysis(res_needle,
                                                                
                                                                -1, 
                                                                -1 );
            System.out.println("finished");
           
        } catch (Exception e)
        {
            System.out.println(e);
        }
        
        System.exit(0);
    }
}
