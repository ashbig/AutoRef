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
    
    
    public static final int FLAG_POLYM_YES = 1;
    public static final int FLAG_POLYM_NO = -1;
    public static final int FLAG_POLYM_NOKNOWN = 0;
    
    //mutation types(0,23)
    public static final int TYPE_NOT_DEFINE = 0;
     public static final int TYPE_NOTRESOLVED=23;
    //aa mutation type (1-14, 30-34)
    public static final int TYPE_AA_INFRAME = 30;
    public static final int TYPE_AA_INFRAME_INSERTION =31;
    public static final int TYPE_AA_INFRAME_DELETION =32;
    public static final int TYPE_AA_FRAMESHIFT_INSERTION =34;
    public static final int TYPE_AA_FRAMESHIFT_DELETION = 33;

      
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
    
    //rna mutastion type (15-22,24-29,35)
    
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
     public static final int TYPE_RNA_SUBSTITUTION= 35;
     
     //linker mutation type (40-43)
      public static final int TYPE_LINKER_5_SUBSTITUTION = 40;
        public static final int TYPE_LINKER_3_SUBSTITUTION = 41;
        public static final int TYPE_LINKER_5_INS_DEL = 42;
       public static final int TYPE_LINKER_3_INS_DEL = 43;
    //ambiquos base discrepancies (50 - 60)
       
       public static final int TYPE_N_SUBSTITUTION_CDS = 50;//N substitution – gene region
        public static final int TYPE_N_SUBSTITUTION_LINKER5 = 51;//N substitution – linker region
        public static final int TYPE_N_SUBSTITUTION_LINKER3 = 52;
        public static final int TYPE_N_INSERTION_LINKER5 = 53;//N substitution – linker region
        public static final int TYPE_N_INSERTION_LINKER3 = 54;
        
        public static final int TYPE_N_SUBSTITUTION_START_CODON = 55;//N substitution start codon
        public static final int TYPE_N_SUBSTITUTION_STOP_CODON = 56;//N substitution stop codon
        public static final int TYPE_N_FRAMESHIFT_INSERTION = 58;// N frameshift (introduced only by N)
        public static final int TYPE_N_INFRAME_INSERTION = 60;
        
                   // |
                   // |
                    //update
         public static final int MAXIMUM_CHANGE_TYPE = 61;
   
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
    
   public static final int MACRO_SPECTYPE_N_SUBSTITUTION_CDS = -14;//N substitution – gene region
    public static final int MACRO_SPECTYPE_N_SUBSTITUTION_LINKER5 = -15;//N substitution – linker region
    public static final int MACRO_SPECTYPE_N_SUBSTITUTION_LINKER3 = -16;
    public static final int MACRO_SPECTYPE_N_INSERTION_LINKER5 = -17;//N substitution – linker region
    public static final int MACRO_SPECTYPE_N_INSERTION_LINKER3 = -18;

    public static final int MACRO_SPECTYPE_N_SUBSTITUTION_START_CODON = -19;//N substitution start codon
    public static final int MACRO_SPECTYPE_N_SUBSTITUTION_STOP_CODON = -20;//N substitution stop codon
    public static final int MACRO_SPECTYPE_N_FRAMESHIFT_INSERTION = -21;// N frameshift (introduced only by N)
    public static final int MACRO_SPECTYPE_N_INFRAME_INSERTION = -22;
    
    public static final int MACRO_SPECTYPES_COUNT = 22;//22
	                 
    protected int         m_id =-1;
    protected int         m_position =-1;// start of mutation (on ref sequence)
     protected int        m_exp_position =-1;// start of mutation (on exp sequence)
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
                m_exp_position = rs.getInt("DISCRPOSITION");
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
 +",TYPE ,SEQUENCEID ,CHANGETYPE ,DISCRNUMBER,DISCQUALITY , DISCRPOSITION )   VALUES(?, ?, ?, ?,?,?,?,?,?,?,?)" ;
    
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
            pstmt.setInt(11,m_exp_position);
            DatabaseTransaction.executeUpdate(pstmt);
              
            
        } catch (Exception sqlE)
        {
            System.out.println(this.toString());
            System.out.println(sqlE.getMessage());
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
          
            DatabaseTransaction.closeStatement(pstmt);
        }
    }
    
    public void         setId(int v)    { m_id =v;}
    public void         setPosition(int v)    { m_position =v;}// start of mutation (on object sequence)
    public void         setExpPosition(int v)    { m_exp_position =v;}// start of mutation (on object sequence)
    public void         setLength(int v)    { m_length=v;}// – length of mutation (optional)
    public void         setType(int v)    { m_type = v;}// //rna ! AA
    public void      	setChangeMut(String v)    { if ( v != null) v = v.toUpperCase();m_change_mut = v;}// – mutation bases of object sequence
    public void      	setChangeOri(String v)    { if ( v != null) v = v.toUpperCase(); m_change_ori = v;}// mutation bases of subject sequence
    public void         setSequenceId(int v)    { m_sequenceid = v;}
    public void         setNumber(int v)    { m_number = v;}
    public void         setChangeType(int v)    { m_change_type = v;}
    public void         setQuality(int v)    { m_quality = v;}
    
    public int          getId()    { return m_id ;}
    public int          getSequenceId()    { return m_sequenceid ;}
    public int          getPosition()    { return m_position ;}// start of mutation (on refseq sequence)
     public int          getExpPosition()    { return m_exp_position ;}// start of mutation (on experimental sequence)
    public int          getLength()    { return m_length;}// – length of mutation (optional)
    public int          getType()    { return   m_type ;}// – mutation type
    public String       getTypeAsString()
    {
        switch(m_type)
        {
            case DNA : return "DNA";
            case AA: return "AA";
            case  RNA : return "RNA";
            case  LINKER_3P : return "Linker 3'";
            case  LINKER_5P : return "Linker 5'";
            default: return "not known";
        }
    }
    public String       getQueryStr()    { return    m_change_mut ;}// – mutation bases of object sequence
    public String       getSubjectStr()    { return   m_change_ori ;}// mutation bases of subject sequence
    public int           getQuality()    { return m_quality;}
    public String       getQualityAsString()
    {
        return getQualityAsString(m_quality);
    }
    public static String       getQualityAsString( int quality)
    {
        switch (quality)
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
           case TYPE_AA_INFRAME  : return "Inframe";
            case TYPE_AA_INFRAME_INSERTION : return "Inframe Insertion";
            case TYPE_AA_INFRAME_DELETION : return "Inframe Deletion";
            case TYPE_AA_FRAMESHIFT_INSERTION : return "Frameshift Insertion";
            case TYPE_AA_FRAMESHIFT_DELETION  :return "Frameshift Deletion";

            
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
            case TYPE_RNA_SUBSTITUTION: return "Substitution";
            
            case TYPE_LINKER_5_SUBSTITUTION : return "5' substitution";
            case TYPE_LINKER_3_SUBSTITUTION: return "3' substitution";
            case TYPE_LINKER_5_INS_DEL : return "5' insertion/deletion";
            case TYPE_LINKER_3_INS_DEL : return "3' insertion/deletion";
            
            case TYPE_N_SUBSTITUTION_CDS : return "Amb. substitution";//N substitution – gene region
            case TYPE_N_SUBSTITUTION_LINKER5 : return "Amb. linker 5 substitution";//N substitution – linker region
            case TYPE_N_SUBSTITUTION_LINKER3 : return "Amb. linker 3 substitution";
            case TYPE_N_INSERTION_LINKER5 : return "Amb. linker 5 insertion";//N substitution – linker region
            case TYPE_N_INSERTION_LINKER3 : return "Amb. linker 3 insertion";

            case TYPE_N_SUBSTITUTION_START_CODON : return "Amb. start codon substitution";//N substitution start codon
            case TYPE_N_SUBSTITUTION_STOP_CODON : return "Amb. stop codon substitution";//N substitution stop codon
            case TYPE_N_FRAMESHIFT_INSERTION : return "Amb. frameshift insertion";// N frameshift (introduced only by N)
            case TYPE_N_INFRAME_INSERTION : return "Amb. inframe insertion";
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
             case TYPE_AA_INFRAME  : return "Inframe";
            case TYPE_AA_INFRAME_INSERTION : return "Inframe Insertion";
            case TYPE_AA_INFRAME_DELETION : return "Inframe Deletion";
            case TYPE_AA_FRAMESHIFT_INSERTION : return "Frameshift Insertion";
            case TYPE_AA_FRAMESHIFT_DELETION  :return "Frameshift Deletion";
            
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
             case TYPE_RNA_SUBSTITUTION: return "Substitution";
            
            case TYPE_LINKER_5_SUBSTITUTION : return "5' substitution";
            case TYPE_LINKER_3_SUBSTITUTION: return "3' substitution";
            case TYPE_LINKER_5_INS_DEL : return "5' insertion/deletion";
            case TYPE_LINKER_3_INS_DEL : return "3' insertion/deletion";
            
            case TYPE_N_SUBSTITUTION_CDS : return "Amb. substitution";//N substitution – gene region
            case TYPE_N_SUBSTITUTION_LINKER5 : return "Amb. linker 5 substitution";//N substitution – linker region
            case TYPE_N_SUBSTITUTION_LINKER3 : return "Amb. linker 3 substitution";
            case TYPE_N_INSERTION_LINKER5 : return "Amb. linker 5 insertion";//N substitution – linker region
            case TYPE_N_INSERTION_LINKER3 : return "Amb. linker 3 insertion";

            case TYPE_N_SUBSTITUTION_START_CODON : return "Amb. start codon substitution";//N substitution start codon
            case TYPE_N_SUBSTITUTION_STOP_CODON : return "Amb. stop codon substitution";//N substitution stop codon
            case TYPE_N_FRAMESHIFT_INSERTION : return "Amb. frameshift insertion";// N frameshift (introduced only by N)
            case TYPE_N_INFRAME_INSERTION : return "Amb. inframe insertion";
           
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
    
    public String toStringSeparateType()
    {
       switch (m_type)
       {
           case   AA : return ((AAMutation)this).toString();
           case RNA :return ((RNAMutation)this).toString();
           case LINKER_3P :
           case LINKER_5P :
                return ((LinkerMutation)this).toString();
       }
       return "";
    }
    
    public static String toHTMLString (ArrayList discrepancies)
    {
        StringBuffer report = new StringBuffer();
        String row_color = " bgColor='#e4e9f8'";
        Mutation mut = null;
        RNAMutation rm = null;
        AAMutation am = null; 
        int cur_number =1;

        report.append("<table border=1 cellpadding=0 cellspacing=0 width=90% align=center>");
        report.append("<tr >");
        report.append("<th width='13%' bgcolor='#1145A6'><strong><font color='#FFFFFF'>Number</font></strong></th>");
        report.append("<th width='31%' bgcolor='#1145A6'><strong><font color='#FFFFFF'>Description</font></strong></th>");
        report.append("<th width='25%' bgcolor='#1145A6'><strong><font color='#FFFFFF'>Protein Description</font></strong></th>");
        report.append("<th width='17%' bgcolor='#1145A6'><strong><font color='#FFFFFF'>Polymorphism</font></strong></th>");
        report.append("<th width='14%' bgcolor='#1145A6'><strong><font color='#FFFFFF'>Confidence</font></strong></th>");
        report.append("</tr>");
	DiscrepancyDescription discr_definition = null;
        ArrayList rna_discrepancies = null;
	//convert to discrepancy definition
        ArrayList discrepancy_definition = DiscrepancyDescription.assembleDiscrepancyDefinitions(discrepancies);
        if ( discrepancy_definition == null || discrepancy_definition.size() == 0) return "";
        for (int count = 0; count < discrepancy_definition.size(); count ++)
	{
        	if (count % 2 == 0){  row_color = " bgColor='#e4e9f8'";	}
		else	{	row_color =" bgColor='#b8c6ed'";	}
                discr_definition = (DiscrepancyDescription)discrepancy_definition.get(count);
                if ( discr_definition.getDiscrepancyDefintionType() == DiscrepancyDescription.TYPE_AA)
                {
                        rna_discrepancies = discr_definition.getRNACollection();
			
                        for (int rna_count = 0; rna_count < rna_discrepancies.size(); rna_count++)
                        {
                            rm = (RNAMutation)rna_discrepancies.get(rna_count);
                            report.append("<tr><td  "+row_color +">" + rm.getNumber() +"</td>");
                            report.append("<td "+ row_color+" > "+ rm.toHTMLString()+"</td>");
                            if (rna_count== 0)
                                report.append("<td "+ row_color +" rowspan="+rna_discrepancies.size()+" >"+ ((AAMutation)discr_definition.getAADefinition()).toHTMLString() +"</td>");
                            report.append(" <td  align='center'"+ row_color +"><b>"+ rm.getPolymorphismFlagAsString()+"</b></td>");  
                            report.append(" <td  align='center'"+ row_color +">"+ rm.getQualityAsString()+"</td>");  
                            report.append("</tr> "); 
                        }
             
		}
	   else if ( discr_definition.getDiscrepancyDefintionType() != DiscrepancyDescription.TYPE_AA)
           {
               mut = discr_definition.getRNADefinition();
               report.append("<tr>");
	       report.append("<td  "+ row_color +">"+ mut.getNumber() +"</td> ");
	    //   if (discr_definition.getDiscrepancyDefintionType() == DiscrepancyDescription.TYPE_NOT_AA_LINKER)
             //  {
                    report.append("<td  "+ row_color +">"+ ((Mutation)discr_definition.getRNADefinition()).toHTMLString()+"</td>");  
             //  }
               
               report.append("<td  "+ row_color +">&nbsp;</td><td  "+ row_color +">&nbsp;</td> ");       
		report.append("<td  align='center'"+ row_color +">"+ mut.getQualityAsString() +"</td>  ");  
		report.append("</tr>");     
      
	}}
	
        report.append("</table>");
    /*
    for (int count = 0; count < discrepancies.size(); count ++)
	{
	
		if (count % 2 == 0)
		{
		  row_color = " bgColor='#e4e9f8'";
		}
		else
		{
			row_color =" bgColor='#b8c6ed'";
		}
	    mut = (Mutation)discrepancies.get(count);
        if (mut.getType() == Mutation.RNA)
		{

			rm = (RNAMutation)discrepancies.get(count);
			report.append("<tr><td  "+row_color +">" + rm.getNumber() +"</td>");
                        report.append("<td "+ row_color+" > "+ rm.toHTMLString()+"</td>");
  			if ( discrepancies.size() >= count )
	   		{
				mut= (Mutation) discrepancies.get(count+1);

				if (mut.getType() == Mutation.AA)
				{

					am = (AAMutation)discrepancies.get(count+1);
					count++;
					report.append("<td "+ row_color +">"+ am.toHTMLString() +"</td>");
 				}
 				else
 				{
					report.append(" <td  "+ row_color +">&nbsp;</td> ");
				}
			}
			report.append(" <td  align='center'"+ row_color +">"+ rm.getPolymorphismFlagAsString()+"</td>");  
			report.append(" <td  align='center'"+ row_color +">"+ rm.getQualityAsString()+"</td>");  
		    report.append("</tr> ");               
		}
	   else if (mut.getType() == Mutation.LINKER_3P || mut.getType() == Mutation.LINKER_5P)
	   {
	   	linker = (LinkerMutation) discrepancies.get(count);
		report.append("<tr>");
		report.append("<td  "+ row_color +">"+ linker.getNumber() +"</td> ");
        report.append("<td  "+ row_color +">"+ linker.toHTMLString()+"</td>");   
		report.append("<td  "+ row_color +">&nbsp;</td><td  "+ row_color +">&nbsp;</td> ");       
		report.append("<td  align='center'"+ row_color +">"+ linker.getQualityAsString() +"</td>  ");  
		report.append("</tr>");                
	}}
	
        report.append("</table>");
     **/
        return report.toString();
    }
    
     public static String toString (ArrayList discrepancies)
    {
        return  toString ( discrepancies, false);
    
     }
     public static String toString (ArrayList discrepancies, boolean isRNADescriptionsForAA)
    {
        StringBuffer report = new StringBuffer();
        Mutation mut = null;
        String tmp = null;
	DiscrepancyDescription discr_definition = null;
        ArrayList rna_discrepancies = null;
	//convert to discrepancy definition
        ArrayList discrepancy_definition = DiscrepancyDescription.assembleDiscrepancyDefinitions(discrepancies);
        if ( discrepancy_definition == null || discrepancy_definition.size() == 0) return "";
        for (int count = 0; count < discrepancy_definition.size(); count ++)
	{
        	discr_definition = (DiscrepancyDescription)discrepancy_definition.get(count);
                if ( discr_definition.getDiscrepancyDefintionType() == DiscrepancyDescription.TYPE_AA)
                {
                    if ( isRNADescriptionsForAA )
                       mut = (Mutation) discr_definition.getRNACollection().get(0); 
                    else
                        mut = discr_definition.getAADefinition();
                }
                else if ( discr_definition.getDiscrepancyDefintionType() != DiscrepancyDescription.TYPE_AA)
                {
                    mut = (Mutation)discr_definition.getRNADefinition();
                 }
                report.append("\nid: " +  mut.getId() + "\t position: "+mut.getPosition() );
                tmp = ( mut.getQueryStr() == null) ? " " : mut.getQueryStr() ;
                report.append("\tchange ori "+ tmp);
                tmp = (mut.getSubjectStr() == null) ? " ": mut.getSubjectStr();
                report.append("\tmut ori "+ tmp);
                report.append("\ttype: "+ mut.getMutationTypeAsString() +"\tquality "+mut.getQualityAsString() );
                    
        }
  
        return report.toString();
    }
    public boolean isNotAmbiquousRNADiscrepancy()
    {
        if ( m_type != RNA) return false;
        switch (m_change_type) 
        {
            case  TYPE_N_SUBSTITUTION_CDS :
            case TYPE_N_SUBSTITUTION_LINKER5 :
            case TYPE_N_SUBSTITUTION_LINKER3:
            case TYPE_N_INSERTION_LINKER5 :
            case TYPE_N_INSERTION_LINKER3 :
            case TYPE_N_SUBSTITUTION_START_CODON :
            case TYPE_N_SUBSTITUTION_STOP_CODON :
            case TYPE_N_FRAMESHIFT_INSERTION :
            case TYPE_N_INFRAME_INSERTION :
                return true;
            default:
                return true;
        }
    }
     
    public String toHTMLString()
    {
        String res = "";
        
        res = "<tr><td>Discrepancy id: </td><td>"+m_id + "</td></tr>" +
        "<tr><td>Position ("+ getDescriptionRefSequencePositionForReport()+")</td><td>"  + m_position + "</td></tr>" +
        "<tr><td>Length</td><td>" +m_length + "</td></tr>" ;
        if ( m_exp_position > 0)
            res += "<tr><td>Position (ExpSequence)</td><td>"  + m_exp_position + "</td></tr>" ;
        if (m_change_ori ==null)
            res+="<tr><td>Ori Str.</td><td>&nbsp;</td></tr>";
        else
            res+="<tr><td>Ori Str.</td><td>"+m_change_ori +"</td></tr>" ;
        if (m_change_mut ==null)
            res+="<tr><td>Mutant Str.</td><td>&nbsp;</td></tr>";
        else
            res+="<tr><td>Mutant Str.</td><td>" +m_change_mut + "</td></tr>" ;
        res+="<tr><td>Type</td><td>" + getMutationTypeAsString() + "</td></tr>" ;
        return res;
    }
    
    public static String HTMLReport(ArrayList discrepancies, int discrepancy_type, boolean isSeparateByQuality)
    {
        if (discrepancies == null || discrepancies.size()==0) return "";
        StringBuffer report = new StringBuffer();
        String line = "";boolean isExists = false;
        int[][] discrepancy_count  = getDiscrepanciesSeparatedByType( discrepancies,  discrepancy_type,  isSeparateByQuality);
        for (int j=0; j< MAXIMUM_CHANGE_TYPE;j++)
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
    
    public static String discrepancyTypeQualityReport(ArrayList discrepancies, int discrepancy_type, boolean isSeparateByQuality, boolean isHighQuality)
    {
        if (discrepancies == null || discrepancies.size()==0) return "";
        StringBuffer report = new StringBuffer();
        String line = "";boolean isExists = false;
        String quality  ;int i;
        int[][] discrepancy_count  = getDiscrepanciesSeparatedByType( discrepancies,  discrepancy_type,  isSeparateByQuality);
        for (int j=0; j< 45;j++)
        {
            if (isHighQuality)
            {
                i = 0;
                quality = "(High/Not known)";
            }
            else 
            {
                 quality = "(Low)";
                i = 1;
            }
            line =getMutationTypeAsString(j);
            isExists = false;
            if (discrepancy_count[j][i] != 0)
            {

                if (! isExists) report.append(line+quality+": ");
                isExists = true;
                report.append(discrepancy_count[j][i]);
                
            }
            if (isExists) report.append(" | ");
            
        }
        return report.toString();
    }
    
    public static ArrayList   getDiscrepanciesBySequenceId(int seq_id) throws BecDatabaseException
    {
         String sql = "select  DISCREPANCYID  ,POSITION ,LENGTH ,CHANGEORI ,CHANGEMUT "
 +",TYPE ,SEQUENCEID ,POLYMFLAG ,POLYMID ,POLMDATE,CODONORI ,CODONMUT ,UPSTREAM ,DOWNSTREAM "
 +",CODONPOS ,CHANGETYPE ,DISCRNUMBER ,DISCRPOSITION  ,DISCRLENGTH  ,DISCQUALITY  from discrepancy where sequenceid = "+seq_id +" order by discrnumber, type";
        return getByRule(sql);
    }
    
     public static ArrayList   getByIds(String ids ) throws BecDatabaseException
    {
         String sql = "select  DISCREPANCYID  ,POSITION ,LENGTH ,CHANGEORI ,CHANGEMUT "
 +",TYPE ,SEQUENCEID ,POLYMFLAG ,POLYMID ,POLMDATE,CODONORI ,CODONMUT ,UPSTREAM ,DOWNSTREAM "
 +",CODONPOS ,CHANGETYPE ,DISCRNUMBER ,DISCRPOSITION  ,DISCRLENGTH  ,DISCQUALITY  from discrepancy where DISCREPANCYID in ( "+ids +")";
        return getByRule(sql);
    }
     public static Object   getById(int id) throws BecDatabaseException
    {
         String sql = "select  DISCREPANCYID  ,POSITION ,LENGTH ,CHANGEORI ,CHANGEMUT "
 +",TYPE ,SEQUENCEID ,POLYMFLAG ,POLYMID ,POLMDATE,CODONORI ,CODONMUT ,UPSTREAM ,DOWNSTREAM "
 +",CODONPOS ,CHANGETYPE ,DISCRNUMBER ,DISCRPOSITION  ,DISCRLENGTH  ,DISCQUALITY  from discrepancy where DISCREPANCYID = "+id ;
               
        ArrayList discrepancies =  getByRule(sql);
        if ( discrepancies == null || discrepancies.size() < 1) return null;
        return discrepancies.get(0);
    }
     
     
     
   
   
    //function returns macro change type for discrepancy, used for  spec query
    public static int       getMacroChangeType(int change_type)
    {
          switch (change_type)
          {
             case Mutation.TYPE_RNA_SILENT : 
             case Mutation.TYPE_AA_SILENT:
             case Mutation.TYPE_AA_SILENT_CONSERVATIVE:
                  return Mutation.MACRO_SPECTYPE_SILENT;
             case Mutation.TYPE_RNA_INFRAME_INSERTION: 
             case Mutation.TYPE_RNA_INFRAME :
            case Mutation.TYPE_AA_INFRAME  : 
            case Mutation.TYPE_AA_INFRAME_INSERTION : 
                   return Mutation.MACRO_SPECTYPE_INFRAME;
           // case Mutation.TYPE_RNA_MISSENSE : 
             case Mutation.TYPE_AA_SUBSTITUTION:
             case Mutation.TYPE_AA_CONSERVATIVE:
                   return Mutation.MACRO_SPECTYPE_CONSERVATIVE;
             case Mutation.TYPE_AA_NONCONSERVATIVE :
                    return Mutation.MACRO_SPECTYPE_NONCONSERVATIVE;
             case  Mutation.TYPE_RNA_FRAMESHIFT : 
             case Mutation.TYPE_RNA_FRAMESHIFT_DELETION :
             case Mutation.TYPE_RNA_FRAMESHIFT_INSERTION :
             case Mutation.TYPE_AA_FRAMESHIFT:
             case Mutation.TYPE_AA_DELETION:
             case Mutation.TYPE_AA_INSERTION:
             case Mutation.TYPE_AA_INSERTION_COMPLEX:
             case Mutation.TYPE_AA_DELETION_COMPLEX:
             case Mutation.TYPE_AA_FRAMESHIFT_INSERTION :
             case Mutation.TYPE_AA_FRAMESHIFT_DELETION  :
                 return Mutation.MACRO_SPECTYPE_FRAMESHIFT;
                  
             case Mutation.TYPE_RNA_INFRAME_DELETION:
             case Mutation.TYPE_AA_INFRAME_DELETION:
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
                    
             case TYPE_N_SUBSTITUTION_CDS : return Mutation.MACRO_SPECTYPE_N_SUBSTITUTION_CDS ;//N substitution – gene region
            case TYPE_N_SUBSTITUTION_LINKER5 : return Mutation.MACRO_SPECTYPE_N_SUBSTITUTION_LINKER5;//N substitution – linker region
            case TYPE_N_SUBSTITUTION_LINKER3 : return Mutation.MACRO_SPECTYPE_N_SUBSTITUTION_LINKER3;
            case TYPE_N_INSERTION_LINKER5 : return Mutation.MACRO_SPECTYPE_N_INSERTION_LINKER5;//N substitution – linker region
            case TYPE_N_INSERTION_LINKER3 : return Mutation.MACRO_SPECTYPE_N_INSERTION_LINKER3;

            case TYPE_N_SUBSTITUTION_START_CODON : return Mutation.MACRO_SPECTYPE_N_SUBSTITUTION_START_CODON;//N substitution start codon
            case TYPE_N_SUBSTITUTION_STOP_CODON : return Mutation.MACRO_SPECTYPE_N_SUBSTITUTION_STOP_CODON;//N substitution stop codon
            case TYPE_N_FRAMESHIFT_INSERTION : return Mutation.MACRO_SPECTYPE_N_FRAMESHIFT_INSERTION;// N frameshift (introduced only by N)
            case TYPE_N_INFRAME_INSERTION : return Mutation.MACRO_SPECTYPE_N_INFRAME_INSERTION;
       
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
    /*
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
        DiscrepancyDescription discr_description = null;
        for(int count = 0; count < discrepancies.size(); count++)
        {
            if ( discrepancies.get(count) instanceof DiscrepancyDescription)
            {
                discr_description = (DiscrepancyDescription)discrepancies.get(count);
                switch (discr_description.getDiscrepancyDefintionType() )
                {
                    case DiscrepancyDescription.TYPE_AA:
                    {
                        mut = discr_description.getAADefinition();
                        break;
                    }
                    case DiscrepancyDescription.TYPE_NOT_AA_LINKER:
                    {
                        mut =  discr_description.getRNADefinition();
                        break;
                    }
                   
                }
            }
            else
                mut = (Mutation)discrepancies.get(count);
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
 */
    //function counts number of discrepancies of each type
    public static int[][] getDiscrepanciesSeparatedByType(ArrayList discr, int discrepancy_type, boolean isSeparateByType)
    {
        if (discr == null || discr.size() == 0) return null;
        
        int res[][] = new int[MAXIMUM_CHANGE_TYPE][2] ;
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
    
 
    public static ArrayList getDiscrepanciesBySequenceType(ArrayList discrepancies, int dicrepancy_type)  throws BecDatabaseException
    {
        ArrayList discrepancies_of_requested_type = new ArrayList();
        Mutation mut = null;
        for(int i = 0; i < discrepancies.size(); i++)
        {
            mut = (Mutation)discrepancies.get(i);
            if (mut.getType() == dicrepancy_type)
                discrepancies_of_requested_type.add( mut );
        }
        return discrepancies_of_requested_type;
    }
    public static ArrayList sortDiscrepanciesByPosition(ArrayList discrepancies)
    {
        ArrayList result = new ArrayList();
        for (int i=0; i< discrepancies.size();i++)
            Collections.sort(discrepancies, new Comparator()
            {
                public int compare(Object o1, Object o2)
                {
                    return ((Mutation) o1).getPosition() - ((Mutation) o2).getPosition();
                }
                public boolean equals(java.lang.Object obj)               {      return false;  }
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
     
//----------------------------------------------------------------------------
     
     private static ArrayList   getByRule(String sql ) throws BecDatabaseException
    {
        int type =-1; 
        int mid = -1;
        
        ArrayList discrepancies = new ArrayList();
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                mid = rs.getInt("DISCREPANCYID");
                type = rs.getInt("TYPE");
                if  (type == Mutation.AA)
                {
                    AAMutation cur_aa_mutation = new AAMutation();
                    cur_aa_mutation.setId(mid);
                    cur_aa_mutation.setPosition ( rs.getInt("POSITION") );// start of mutation (on object sequence)
                    cur_aa_mutation.setLength ( rs.getInt("LENGTH") );
                    cur_aa_mutation.setChangeMut ( rs.getString("CHANGEMUT"));
                    cur_aa_mutation.setChangeOri ( rs.getString("CHANGEORI"));
                    cur_aa_mutation.setSequenceId ( rs.getInt("SEQUENCEID") ) ;
                    cur_aa_mutation.setNumber ( rs.getInt("DISCRNUMBER")) ;
                    cur_aa_mutation.setChangeType ( rs.getInt("CHANGETYPE")) ;
                    cur_aa_mutation.setQuality( rs.getInt("DISCQUALITY") );
                    discrepancies.add(cur_aa_mutation);
                }
                else if (type == Mutation.LINKER_5P || type == Mutation.LINKER_3P )
                {
                    LinkerMutation cur_linker_mutation = new LinkerMutation(type == Mutation.LINKER_5P);
                    cur_linker_mutation.setType(type);
                    cur_linker_mutation.setId(mid);
                    cur_linker_mutation.setPosition ( rs.getInt("POSITION") );// start of mutation (on object sequence)
                    cur_linker_mutation.setLength ( rs.getInt("LENGTH") );
                    cur_linker_mutation.setChangeMut ( rs.getString("CHANGEMUT"));
                    cur_linker_mutation.setChangeOri ( rs.getString("CHANGEORI"));
                    cur_linker_mutation.setSequenceId ( rs.getInt("SEQUENCEID")) ;
                    cur_linker_mutation.setNumber ( rs.getInt("DISCRNUMBER")) ;
                    cur_linker_mutation.setChangeType ( rs.getInt("CHANGETYPE")) ;
                    cur_linker_mutation.setQuality( rs.getInt("DISCQUALITY") );
                    cur_linker_mutation.setExpPosition( rs.getInt("DISCRPOSITION") );
                    discrepancies.add( cur_linker_mutation );
                }
               else if(type == Mutation.RNA)
                {
                    RNAMutation cur_rna_mutation = new RNAMutation();
                    cur_rna_mutation.setId(mid);
                     cur_rna_mutation.setPolymFlag( rs.getInt("POLYMFLAG"));
                     cur_rna_mutation.setPolymDate(rs.getDate("POLMDATE"));
                     cur_rna_mutation.setPolymId( rs.getString("POLYMID"));
                    cur_rna_mutation.setUpstream(rs.getString("UPSTREAM"));
                    cur_rna_mutation.setDownStream(rs.getString("DOWNSTREAM"));
                    cur_rna_mutation.setCodonOri( rs.getString("CODONORI") );
                    cur_rna_mutation.setCodonMut(rs.getString("CODONMUT"));
                    cur_rna_mutation.setCodonPos( rs.getInt("CODONPOS") );
                     cur_rna_mutation.setPosition ( rs.getInt("POSITION") );// start of mutation (on object sequence)
                    cur_rna_mutation.setLength ( rs.getInt("LENGTH") );
                    cur_rna_mutation.setChangeMut ( rs.getString("CHANGEMUT"));
                    cur_rna_mutation.setChangeOri ( rs.getString("CHANGEORI"));
                    cur_rna_mutation.setSequenceId ( rs.getInt("SEQUENCEID")) ;
                    cur_rna_mutation.setNumber ( rs.getInt("DISCRNUMBER")) ;
                    cur_rna_mutation.setChangeType ( rs.getInt("CHANGETYPE")) ;
                    cur_rna_mutation.setQuality( rs.getInt("DISCQUALITY") );
                    cur_rna_mutation.setExpPosition( rs.getInt("DISCRPOSITION") );
                    discrepancies.add( cur_rna_mutation);
                }
            }
           return discrepancies;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting discrepancies: \nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
    }
     private String getDescriptionRefSequencePositionForReport()
     {
         if ( m_type == LINKER_5P   || (m_type == RNA && 
            (m_change_type ==  TYPE_N_SUBSTITUTION_LINKER5 
                || m_change_type == TYPE_N_INSERTION_LINKER5 )))
             return "Upstream linker region";
         else if (m_type == LINKER_3P || (m_type == RNA && 
            (m_change_type ==  TYPE_N_SUBSTITUTION_LINKER3 
                || m_change_type == TYPE_N_INSERTION_LINKER3 )))
            return "Downstream linker region";
         else 
             return "Gene region";
     }
  /* 
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
               
                public boolean equals(java.lang.Object obj)
                {      return false;  }
                // compare
            } );
            
            
            return discrepancies;
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
            Read read = Read.getReadById( 25638);
            String str = Mutation.toHTMLString(read.getSequence().getDiscrepancies() ) ;
             System.out.println(str);
             
             read = Read.getReadById( 25639);
             str = Mutation.toHTMLString(read.getSequence().getDiscrepancies() ) ;
               System.out.println(str);
               read = Read.getReadById( 25640);
             str = Mutation.toHTMLString(read.getSequence().getDiscrepancies() ) ;
         //String r =HTMLReport(read.getSequence().getDiscrepancies(), Mutation.RNA, true);  
         System.out.println(str);
         read = Read.getReadById( 25641);
             str = Mutation.toHTMLString(read.getSequence().getDiscrepancies() ) ;
              System.out.println(str);
               read = Read.getReadById( 25643);
             str = Mutation.toHTMLString(read.getSequence().getDiscrepancies() ) ;
              System.out.println(str);
               read = Read.getReadById( 25645);
             str = Mutation.toHTMLString(read.getSequence().getDiscrepancies() ) ;
              System.out.println(str);
        } catch (Exception e)
        {
            System.out.println(e);
        }
        
        System.exit(0);
    }
}
