/*
 * Mutation.java
 *
 * Created on September 24, 2002, 12:02 PM
 */

package edu.harvard.med.hip.bec.coreobjects.feature;


import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.programs.blast.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.bioutil.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import java.util.*;
import java.sql.*;
/**
 *
 * @author  htaycher
 */
public abstract class Mutation
{
    
  
    //mutation type
    public static final int DNA = 0;
    public static final int AA = 2;
    public static final int RNA = 1;
    public static final int LINKER_3P = 3;
    public static final int LINKER_5P = 4;
    
    public static final int QUALITY_NOTKNOWN = 0;
    public static final int QUALITY_HIGH = 2;
    public static final int QUALITY_LOW = 1;
    
    
    //mutation types
    public static final int TYPE_NOT_DEFINE = 0;
     public static final int TYPE_NOTRESOLVED=23;
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
    public static final int TYPE_RNA_NO_TRANSLATION = 27;
    public static final int TYPE_RNA_POST_ELONGATION = 28;
     public static final int TYPE_RNA_TRANCATION = 29;
     
     //linker mutation type
      public static final int TYPE_LINKER_5_SUBSTITUTION = 40;
        public static final int TYPE_LINKER_3_SUBSTITUTION = 41;
        public static final int TYPE_LINKER_5_INS_DEL = 42;
       public static final int TYPE_LINKER_3_INS_DEL = 43;
    
   
   // global mutation types used for spec query
    public static final int MACRO_SPECTYPE_SILENT = -1;
    public static final int MACRO_SPECTYPE_INFRAME =-2;
    public static final int MACRO_SPECTYPE_CONSERVATIVE = -3;
    public static final int MACRO_SPECTYPE_NONCONSERVATIVE = -4;
    public static final int MACRO_SPECTYPE_FRAMESHIFT = -5;
    public static final int MACRO_SPECTYPE_INFRAME_DELETION =-6;
    public static final int MACRO_SPECTYPE_TRANCATION = -7;
    public static final int MACRO_SPECTYPE_NO_TRANSLATION =-8;
    public static final int MACRO_SPECTYPE_POST_ELONGATION = -9;
    public static final int MACRO_SPECTYPE_LINKER_5_SUBSTITUTION  = -10; 
    public static final int MACRO_SPECTYPE_LINKER_3_SUBSTITUTION  = -11;  
    public static final int MACRO_SPECTYPE_LINKER_5_INS_DEL  = -12;
    public static final int MACRO_SPECTYPE_LINKER_3_INS_DEL  = -13;   
    
    public static final int MACRO_SPECTYPES_COUNT = 13;
	                 
    protected int         m_id =-1;
    protected int         m_position =-1;// start of mutation (on object sequence)
    protected int         m_length=-1;// – length of mutation (optional)
    protected int         m_type = -1;// //rna ! AA
    protected String      m_change_mut = null;// – mutation bases of object sequence
    protected String      m_change_ori = null;// mutation bases of subject sequence
    protected int         m_sequenceid = -1;
    
    protected int         m_number = -1;
    protected int         m_change_type = -1;
    protected int         m_quality = QUALITY_NOTKNOWN; //high or low scores
    
    
    
    public Mutation()
    {}
    /** Creates a new instance of Mutation */
    public Mutation(int id) throws BecDatabaseException
    {
         m_id = id;
     
        String sql = "select  DISCREPANCYID  ,POSITION ,LENGTH ,CHANGEORI ,CHANGEMUT "
 +",TYPE ,SEQUENCEID ,POLYMFLAG ,POLYMID ,POLMDATE,CODONORI ,CODONMUT ,UPSTREAM ,DOWNSTREAM "
 +",CODONPOS ,CHANGETYPE ,DISCRNUMBER ,DISCRPOSITION  ,DISCRLENGTH  ,DISCQUALITY  from discrepancy where discrepancyid = "+id;
        
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                m_type = rs.getInt("TYPE");
                m_position = rs.getInt("POSITION");
                m_length = rs.getInt("LENGTH");
                m_change_ori = rs.getString("CHANGEORI");//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
                m_change_mut = rs.getString("CHANGEMUT");
                m_sequenceid = rs.getInt("SEQUENCEID");
              
                m_change_type = rs.getInt("CHANGETYPE");
                m_number = rs.getInt("DISCRNUMBER");
               
                m_quality = rs.getInt("DISCQUALITY");
            }
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing Oligo with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
    
    
    
    
    
    public void insert(Connection conn) throws BecDatabaseException
    {
        if (m_id == -1) m_id = BecIDGenerator.getID("discrepancyid");
        
        PreparedStatement pstmt = null;
        
        String sql = "INSERT INTO discrepancy  (DISCREPANCYID  ,POSITION ,LENGTH ,CHANGEORI ,CHANGEMUT "
 +",TYPE ,SEQUENCEID ,CHANGETYPE ,DISCRNUMBER,DISCQUALITY  )   VALUES(?, ?, ?, ?,?,?,?,?,?,?)" ;
    
        try
        {
           
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, m_id);
            pstmt.setInt(2, m_position);
            pstmt.setInt(3, m_length);
            if (m_change_ori.length() > 300) m_change_ori=m_change_ori.substring(0,299);
            pstmt.setString(4, m_change_ori);
            if (m_change_mut.length() > 300) m_change_mut=m_change_mut.substring(0,299);
            pstmt.setString(5, m_change_mut);
            pstmt.setInt(6, m_type);
            pstmt.setInt(7, m_sequenceid);
            pstmt.setInt(8,m_change_type);
            pstmt.setInt(9,m_number);
            pstmt.setInt(10,m_quality);
            DatabaseTransaction.executeUpdate(pstmt);
              
            
        } catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
          
            DatabaseTransaction.closeStatement(pstmt);
        }
    }
    
    public void         setId(int v)    { m_id =v;}
    public void         setPosition(int v)    { m_position =v;}// start of mutation (on object sequence)
    public void         setLength(int v)    { m_length=v;}// – length of mutation (optional)
    public void         setType(int v)    { m_type = v;}// //rna ! AA
    public void      	setChangeMut(String v)    { m_change_mut = v;}// – mutation bases of object sequence
    public void      	setChangeOri(String v)    { m_change_ori = v;}// mutation bases of subject sequence
    public void         setSequenceId(int v)    { m_sequenceid = v;}
    public void         setNumber(int v)    { m_number = v;}
    public void         setChangeType(int v)    { m_change_type = v;}
    public void         setQuality(int v)    { m_quality = v;}
    
    public int          getId()    { return m_id ;}
    public int          getSequenceId()    { return m_sequenceid ;}
    public int          getPosition()    { return m_position ;}// start of mutation (on object sequence)
    public int          getLength()    { return m_length;}// – length of mutation (optional)
    public int          getType()    { return   m_type ;}// – mutation type
    public String       getTypeAsString()
    {
        switch(m_type)
        {
            case DNA : return "DNA";
            case AA: return "AA";
            case  RNA : return "RNA";
            default: return "not known";
        }
    }
    public String       getQueryStr()    { return    m_change_mut ;}// – mutation bases of object sequence
    public String       getSubjectStr()    { return   m_change_ori ;}// mutation bases of subject sequence
    public int           getQuality()    { return m_quality;}
    public String       getQualityAsString()
    {
        switch (m_quality)
        {
            
            case QUALITY_HIGH :return "High";
            case QUALITY_LOW: return "Low";
            default  : return "Not Known";
        }
    }
    public int          getNumber()    { return m_number;}
    public int          getChangeType()    { return m_change_type;}
    
    public String       getMutationTypeAsString()
    {
        
        switch (m_change_type)
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
            case  TYPE_RNA_NO_TRANSLATION: return "No Translation";
            case  TYPE_RNA_POST_ELONGATION : return "Post Elongation";
             case  TYPE_RNA_TRANCATION : return "Trancation";
            
            case TYPE_LINKER_5_SUBSTITUTION : return "5' substitution";
            case TYPE_LINKER_3_SUBSTITUTION: return "3' substitution";
            case TYPE_LINKER_5_INS_DEL : return "5' insertion/deletion";
            case TYPE_LINKER_3_INS_DEL : return "3' insertion/deletion";
            default  : return "Not known";
        }
        
        
    }
    
    
     public static String       getMutationTypeAsString(int change_type)
    {
        
        switch (change_type)
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
            case  TYPE_RNA_NO_TRANSLATION: return "No Translation";
            case  TYPE_RNA_POST_ELONGATION : return "Post Elongation";
             case  TYPE_RNA_TRANCATION : return "Trancation";
            
            case TYPE_LINKER_5_SUBSTITUTION : return "5' substitution";
            case TYPE_LINKER_3_SUBSTITUTION: return "3' substitution";
            case TYPE_LINKER_5_INS_DEL : return "5' insertion/deletion";
            case TYPE_LINKER_3_INS_DEL : return "3' insertion/deletion";
            default  : return "Not known";        }
        
        
    }
    
    public String toString()
    {
        String res= "\n\n\t\t\tMutation desc.\n id: " +  m_id +
        "\t position: "+m_position +" length: "+ m_length+"\t type: "+getTypeAsString()
        +"\t mut ori: "+ m_change_mut
        +" \t ori: "+m_change_ori
        +"\t sequence id: "+ m_sequenceid
        
        +"\t mut number: "+ m_number
        + " \t mutation type: "+ getMutationTypeAsString() +"\t quality "+getQualityAsString() ;
        return res;
    }
    
    
    public String toHTMLString()
    {
        String res = "";
        
        res = "<tr><td> id: </td><td>"+m_id + "</td></tr>" +
        "<tr><td>Position</td><td>"  + m_position + "</td></tr>" +
        "<tr><td>Length</td><td>" +m_length + "</td></tr>" ;
        if (m_change_mut ==null)
            res+="<tr><td>Query Str</td><td>&nbsp;</td></tr>";
        else
            res+="<tr><td>Query Str</td><td>" +m_change_mut + "</td></tr>" ;
        if (m_change_ori ==null)
            res+="<tr><td>Query Str</td><td>&nbsp;</td></tr>";
        else
            res+="<tr><td>Subject Str</td><td>"+m_change_ori +"</td></tr>" ;
        res+="<tr><td>Mutation</td><td>" + getMutationTypeAsString() + "</td></tr>" ;
        return res;
    }
    
    public static String HTMLReport(ArrayList discrepancies, int discrepancy_type, boolean isSeparateByQuality)
    {
        if (discrepancies == null || discrepancies.size()==0) return "";
        StringBuffer report = new StringBuffer();
        String line = "";boolean isExists = false;
        int[][] discrepancy_count  = getDiscrepanciesSeparatedByType( discrepancies,  discrepancy_type,  isSeparateByQuality);
        for (int j=0; j< 45;j++)
        {
            for (int i = 0; i < 2;i++)
            {
                line ="<tr><td>"+getMutationTypeAsString(j)+"</td>";
                isExists = false;
                if (discrepancy_count[j][i] != 0)
                {
                    
                    if (! isExists) report.append(line);
                    isExists = true;
                    report.append("<td>"+discrepancy_count[j][i]+"</td>");
                    if (i == 0)
                    {
                        report.append("<td>Quality: High</td>");
                    }
                    else
                    {
                        report.append("<td>Quality: Low</td>");
                    }
                  
                }
                if (isExists) report.append("</tr>");
            }
        }
        return report.toString();
    }
    //---------------------------------
    
    /*
    public ArrayList   getAllMutations(int id, int owner) throws BecDatabaseException
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
                int mutation_type = rs.getInt("mutationtype");
                int number = rs.getInt("mutationnumber");
                if (type == Mutation.RNA)
                {
                    RNAMutation mut = new RNAMutation();
                    mut.setPolymFlag(rs.getInt("polymflag"));
                    mut.setPolymId(rs.getString("polymid"));
                    mut.setPolymDate(rs.getDate("polymdate"));
                    mut.setUpstream(rs.getString("upstream"));
                    mut.setDownStream(rs.getString("downstream"));
                    mut.setCodonOri(rs.getString("codonori"));
                    mut.setCodonMut(rs.getString("codonmut"));
                    mut.setCodonPos(rs.getInt("codonpos"));
                    mut.setPosition( position);// start of mutation (on object sequence)
                    mut.setLength( length);
                    
                    mut.setChangeMut( query_str);
                    mut.setChangeOri( subject_str);
                    mut.setSequenceId( sequenceid) ;
                    mut.setNumber( number) ;
                    mut.setChangeType( mutation_type) ;
                    mut.setId(mutid);
                    res.add(mut);
                    
                }
                else     if (type == Mutation.AA)
                {
                    AAMutation mut = new AAMutation();
                    mut.setPosition( position);// start of mutation (on object sequence)
                    mut.setLength( length);
                    mut.setChangeMut( query_str);
                    mut.setChangeOri( subject_str);
                    mut.setSequenceId( sequenceid) ;
                    mut.setNumber( number) ;
                    mut.setChangeType( mutation_type) ;
                    mut.setId(mutid);
                    res.add(mut);
                }
                else     if (type == Mutation.LINKER_5P || type == Mutation.LINKER_3P)
                {
                    
                    LinkerMutation mut_linker = new LinkerMutation( ( type == Mutation.LINKER_5P));
                    mut_linker.setPosition( position);// start of mutation (on object sequence)
                    mut_linker.setLength( length);
                    mut_linker.setChangeMut( query_str);
                    mut_linker.setChangeOri( subject_str);
                    mut_linker.setSequenceId( sequenceid) ;
                    mut_linker.setNumber( number) ;
                    mut_linker.setChangeType( mutation_type) ;
                    mut_linker.setId(mutid);
                    res.add(mut_linker);
                }
                
                
            }
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing blasthit with id: "+m_id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        return res;
    }
    */
    //function returns macro change type for discrepancy, used for  spec query
    public static int       getMacroChangeType(int change_type)
    {
          switch (change_type)
          {
             case Mutation.TYPE_RNA_SILENT : 
             case Mutation.TYPE_AA_SILENT:
                  return Mutation.MACRO_SPECTYPE_SILENT;
             case Mutation.TYPE_RNA_INFRAME_INSERTION: 
             case Mutation.TYPE_RNA_INFRAME :
                   return Mutation.MACRO_SPECTYPE_INFRAME;
             case Mutation.TYPE_RNA_MISSENSE : 
             case Mutation.TYPE_AA_CONSERVATIVE:
                   return Mutation.MACRO_SPECTYPE_CONSERVATIVE;
             case Mutation.TYPE_AA_NONCONSERVATIVE :
                    return Mutation.MACRO_SPECTYPE_NONCONSERVATIVE;
             case  Mutation.TYPE_RNA_FRAMESHIFT : 
             case Mutation.TYPE_RNA_FRAMESHIFT_DELETION :
             case Mutation.TYPE_RNA_FRAMESHIFT_INSERTION :
             case Mutation.TYPE_AA_FRAMESHIFT:
                 return Mutation.MACRO_SPECTYPE_FRAMESHIFT;
            
            
             case Mutation.TYPE_RNA_INFRAME_DELETION:
                 return Mutation.MACRO_SPECTYPE_INFRAME_DELETION;
            
             case Mutation.TYPE_RNA_INFRAME_STOP_CODON :
             case Mutation.TYPE_RNA_FRAMESHIFT_STOP_CODON :
             case Mutation.TYPE_RNA_NONSENSE:
             case Mutation.TYPE_AA_TRUNCATION:
             case Mutation.TYPE_RNA_TRANCATION:
                    return Mutation.MACRO_SPECTYPE_TRANCATION;
             
             case Mutation.TYPE_AA_NO_TRANSLATION :
              case Mutation.TYPE_RNA_NO_TRANSLATION:
                    return Mutation.MACRO_SPECTYPE_NO_TRANSLATION;
             
             case Mutation.TYPE_AA_POST_ELONGATION :
              case Mutation.TYPE_RNA_POST_ELONGATION:
                    return Mutation.MACRO_SPECTYPE_POST_ELONGATION;
             
             case Mutation.TYPE_LINKER_5_SUBSTITUTION  : 
                    return Mutation.MACRO_SPECTYPE_LINKER_5_SUBSTITUTION;
             
             case Mutation.TYPE_LINKER_3_SUBSTITUTION  :  
                    return Mutation.MACRO_SPECTYPE_LINKER_3_SUBSTITUTION;
             
             case Mutation.TYPE_LINKER_5_INS_DEL  : 
                    return Mutation.MACRO_SPECTYPE_LINKER_5_INS_DEL;
             
             case Mutation.TYPE_LINKER_3_INS_DEL  : 
                    return Mutation.MACRO_SPECTYPE_LINKER_3_INS_DEL;
              default: return TYPE_NOT_DEFINE;
          }
    }
    //function updates set of values to database
    //if no change in field name - user should submit empty string
    public  void updateFields(String[] fields_names, String[] fields_value, Connection conn)
    throws Exception
    {
        String fields ="";
        //wrong submition
        if (fields_value.length != fields_names.length) return;
        for (int count = 0; count < fields_names.length; count++)
        {
            if (  fields_value[count] != null && !fields_value[count].equals("") )
            {
                fields += fields_names[count] + " = " + fields_value[count] + ",";
            }
            
        }
        if (fields.equals("")) return;
        String sql = "update discrepancy set "+ fields +     " where discrepancyid="+m_id;
        
        DatabaseTransaction.executeUpdate(sql, conn);
    }
    
    
    //function returns number of discrepancies of
    //define type (RNA AA)
    //define change type,
    // quality , if quality set to UNknown , function do not separate by quality,
    //          not_set qualified as high
    
    public static int getDiscrepancyNumberByParameters(
                ArrayList discrepancies,
                int dicrepancy_type, 
                int change_type, 
                int quality
                )
    
    {
        Mutation mut = null;
        int res = 0;
        if (discrepancies == null) return res;
        boolean isType = false;
        boolean isQuality = false;
        int mut_quality ;
        for(int i = 0; i < discrepancies.size(); i++)
        {
            if ( discrepancies.get(i) instanceof DiscrepancyDescription)
            {
                switch (dicrepancy_type)
                {
                    case Mutation.AA:
                    {
                        mut = ( (DiscrepancyDescription)discrepancies.get(i)).getAADiscrepancy();
                        break;
                    }
                    case Mutation.RNA:
                    {
                        mut =  ( (DiscrepancyDescription)discrepancies.get(i)).getRNADiscrepancy();
                        break;
                    }
                    case Mutation.LINKER_3P:
                    case Mutation.LINKER_5P:
                    {
                        mut =  ( (DiscrepancyDescription)discrepancies.get(i)).getLinkerDiscrepancy();
                        break;
                    }
                }
            }
            else
                mut = (Mutation)discrepancies.get(i);
            mut_quality = mut.getQuality();
            isType = (mut.getType() == dicrepancy_type && mut.getChangeType() == change_type   );
            if  (quality == QUALITY_NOTKNOWN || quality == QUALITY_HIGH)
            {
                isQuality = (mut_quality == QUALITY_NOTKNOWN || mut_quality == QUALITY_HIGH);
            }
            else if (quality == QUALITY_LOW)
            {
                isQuality = (quality== QUALITY_LOW);
            }
            if (isType && isQuality) res++;  
        }
        return res;
    }
    
    
   
     //function returns number of discrepancies of
    //define type (RNA AA)
    //define change type,
    // quality , if quality set to UNknown , function do not separate by quality,
    //          not_set qualified as high
    // isPolym - specifie include polymorphic change as discrepancy or not
    
    // input can be array of discrepancyPairs or array of mutations
     public static int getDiscrepancyNumberByParameters(
                ArrayList discrepancies,
                int dicrepancy_type, 
                int change_type, 
                int quality, 
                int polym_flag)
    
    {
        RNAMutation mut = null;
        LinkerMutation mut_linker =null;
        int res = -1;
        if (discrepancies == null) return res;
        boolean isType = false;
        boolean isQuality = false;
        boolean isPolym = false;
        for(int i = 0; i < discrepancies.size(); i++)
        {
            if (mut.getType() == Mutation.AA) continue;
            else if(mut.getType() == Mutation.RNA)
            {
                mut = (RNAMutation)discrepancies.get(i);
                isType = (mut.getType() == dicrepancy_type && mut.getChangeType() == change_type   );
                isQuality = ( (quality == QUALITY_HIGH && ( mut.getQuality() == QUALITY_HIGH || mut.getQuality()== QUALITY_NOTKNOWN))
                    || (quality == QUALITY_LOW &&  mut.getQuality() == QUALITY_LOW));
                isPolym = ( polym_flag == RNAMutation.FLAG_POLYM_YES && ( mut.getPolymorphismFlag() == RNAMutation.FLAG_POLYM_YES || mut.getPolymorphismFlag()== RNAMutation.FLAG_POLYM_NOKNOWN))
                    || (polym_flag == RNAMutation.FLAG_POLYM_NO &&  mut.getPolymorphismFlag() == RNAMutation.FLAG_POLYM_NO)
                    ||(polym_flag == RNAMutation.FLAG_POLYM_NOKNOWN);

                if (isType && isQuality && isPolym) res++;  
            }
            else if(mut.getType() == Mutation.LINKER_3P || mut.getType() == LINKER_5P)
            {
                mut_linker = (LinkerMutation)discrepancies.get(i);
                isType = (mut_linker.getType() == dicrepancy_type && mut_linker.getChangeType() == change_type   );
                isQuality = ( (quality == QUALITY_HIGH && ( mut_linker.getQuality() == QUALITY_HIGH || mut_linker.getQuality()== QUALITY_NOTKNOWN))
                    || (quality == QUALITY_LOW &&  mut_linker.getQuality() == QUALITY_LOW));
                if (isType && isQuality ) res++;  
            }
  
        }
        return res;
    }
     
    //function counts number of discrepancies of each type
    public static int[][] getDiscrepanciesSeparatedByType(ArrayList discr, int discrepancy_type, boolean isSeparateByType)
    {
        if (discr == null || discr.size() == 0) return null;
        
        int res[][] = new int[45][2] ;
        for (int count = 0; count < discr.size(); count++)
        {
            Mutation mut = (Mutation) discr.get(count);
            if (discrepancy_type != TYPE_NOT_DEFINE && mut.getType() != discrepancy_type ) continue;
            if (isSeparateByType)
            {
                if (mut.getQuality() == Mutation.QUALITY_HIGH || mut.getQuality() == Mutation.QUALITY_NOTKNOWN)
                {
                    res[ mut.getChangeType() ][0]++;
                }
                else
                {
                    res[ mut.getChangeType() ][1]++;
                }
            }
            else
            {
                res[ mut.getChangeType() ][0]++;
            }
             
        }
        return res;
    }
    
  
    public static Hashtable getDiscrepancySummaryStringDescription(int[] res)
    {
        Hashtable discrepancysummary = new Hashtable();
        if (res[Mutation.TYPE_RNA_INFRAME] != 0)
            discrepancysummary.put("Inframe",String.valueOf(res[Mutation.TYPE_RNA_INFRAME]++));
        if (res[Mutation.TYPE_RNA_FRAMESHIFT] != 0)
            discrepancysummary.put("Frameshift",String.valueOf( res[Mutation.TYPE_RNA_FRAMESHIFT]++));
        if (res[Mutation.TYPE_RNA_INFRAME_DELETION] != 0)
            discrepancysummary.put("Inframe: Deletion",String.valueOf(res[res[Mutation.TYPE_RNA_INFRAME_DELETION]]++));
        if (res[Mutation.TYPE_RNA_INFRAME_INSERTION] != 0)
            discrepancysummary.put("Inframe: Insertion",String.valueOf( res[Mutation.TYPE_RNA_INFRAME_INSERTION]++));
        if (res[Mutation.TYPE_RNA_INFRAME_STOP_CODON] != 0)
            discrepancysummary.put("Inframe: Stop Codon",String.valueOf( res[Mutation.TYPE_RNA_INFRAME_STOP_CODON]++));
        if (res[Mutation.TYPE_RNA_FRAMESHIFT_DELETION] != 0)
            discrepancysummary.put("Frameshift:Deletion",String.valueOf( res[Mutation.TYPE_RNA_FRAMESHIFT_DELETION]++));
        if (res[Mutation.TYPE_RNA_FRAMESHIFT_INSERTION] != 0)
            discrepancysummary.put("Frameshift: Insertion",String.valueOf( res[Mutation.TYPE_RNA_FRAMESHIFT_INSERTION]++));
        if (res[Mutation.TYPE_RNA_FRAMESHIFT_STOP_CODON] != 0)
            discrepancysummary.put("Frameshift: Stop codon",String.valueOf( res[Mutation.TYPE_RNA_FRAMESHIFT_STOP_CODON]++));
        if (res[Mutation.TYPE_RNA_SILENT] != 0)
            discrepancysummary.put("Silent",String.valueOf( res[Mutation.TYPE_RNA_SILENT]++));
        if (res[Mutation.TYPE_RNA_NONSENSE] != 0)
            discrepancysummary.put("Nonsense",String.valueOf( res[Mutation.TYPE_RNA_NONSENSE]++));
        if (res[Mutation.TYPE_RNA_MISSENSE] != 0)
            discrepancysummary.put("Missense" ,String.valueOf( res[Mutation.TYPE_RNA_MISSENSE]++));
        
         if (res[Mutation.TYPE_LINKER_5_SUBSTITUTION] != 0)
            discrepancysummary.put("5' substitution" ,String.valueOf( res[Mutation.TYPE_LINKER_5_SUBSTITUTION]++));
         if (res[Mutation.TYPE_LINKER_3_SUBSTITUTION] != 0)
            discrepancysummary.put("3' substitution" ,String.valueOf(  res[Mutation.TYPE_LINKER_3_SUBSTITUTION]++));
         if (res[Mutation.TYPE_LINKER_5_INS_DEL] != 0)
            discrepancysummary.put("5' insertion/deletion" ,String.valueOf(  res[Mutation.TYPE_LINKER_5_INS_DEL]++));
         if (res[Mutation.TYPE_LINKER_3_INS_DEL] != 0)
            discrepancysummary.put("3' insertion/deletion" ,String.valueOf(  res[Mutation.TYPE_LINKER_3_INS_DEL]++));
                
        return discrepancysummary;
    }
    
    public static ArrayList sortDiscrepanciesByChangeType(ArrayList discrepancies)
    {
        ArrayList result = new ArrayList();
        for (int i=0; i< discrepancies.size();i++)
            
            //sort array by cds length
            Collections.sort(discrepancies, new Comparator()
            {
                public int compare(Object o1, Object o2)
                {
                    return ((Mutation) o1).getChangeType() - ((Mutation) o2).getChangeType();
                }
                /** Note: this comparator imposes orderings that are
                 * inconsistent with equals. */
                public boolean equals(java.lang.Object obj)
                {      return false;  }
                // compare
            } );
            
            
            return discrepancies;
    }
    
     public static ArrayList sortDiscrepanciesByNumberAndType(ArrayList discrepancies)
    {
        ArrayList result = new ArrayList();
        for (int i=0; i< discrepancies.size();i++)
            
            //sort array by cds length
            Collections.sort(discrepancies, new Comparator()
            {
                public int compare(Object o1, Object o2)
                {
                    Mutation mut_1 = (Mutation) o1;
                    Mutation mut_2 = (Mutation) o2;
                    int res = mut_1.getNumber() - mut_2.getNumber();
                    if (res == 0)
                    {
                        return mut_1.getType() - mut_2.getType();
                    }
                    else 
                        return res;
                }
                /** Note: this comparator imposes orderings that are
                 * inconsistent with equals. */
                public boolean equals(java.lang.Object obj)
                {      return false;  }
                // compare
            } );
            
            
            return discrepancies;
    }
    
     public static ArrayList sortDiscrepanciesByChangeTypeAndQuality(ArrayList discrepancies)
    {
        ArrayList result = new ArrayList();
        for (int i=0; i< discrepancies.size();i++)
            
            //sort array by cds length
            Collections.sort(discrepancies, new Comparator()
            {
                public int compare(Object o1, Object o2)
                {
                    Mutation discrepancy_1 =  (Mutation) o1;
                    Mutation discrepancy_2 = (Mutation) o2;
                    int res = discrepancy_1.getChangeType() - discrepancy_2.getChangeType();
                    if (res == 0)//same type
                    {
                        return discrepancy_1.getQuality() - discrepancy_2.getQuality();
                    }
                    else
                        return res;
                }
                /** Note: this comparator imposes orderings that are
                 * inconsistent with equals. */
                public boolean equals(java.lang.Object obj)
                {      return false;  }
                // compare
            } );
            
            
            return discrepancies;
    }
     
     
   
    /*
     
    public static ArrayList run_mutation_analysis(BlastResult res_n, BlastResult res_p,
         FullSequence full_sequence, TheoreticalSequence t_sequence)
     
     
     
    {
        ArrayList res = new ArrayList();
         /*
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
     
     
     
     
     
    
    //used if only blast results provided
    
    
    /*
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
     
     */
    
    
    
    
    
    //_______________________________________________________
    
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
            Read read = Read.getReadById( 2433);
           String r =HTMLReport(read.getSequence().getDiscrepancies(), Mutation.RNA, true);  
         System.out.println(r);
        } catch (Exception e)
        {
            System.out.println(e);
        }
        
        System.exit(0);
    }
}
