/*
 * SubmitPolymFromFile.java
 *
 * Created on April 14, 2004, 2:49 PM
 */

package src.edu.harvard.med.hip.bec.util;
import java.sql.*;
import java.io.*;
import java.util.*;

import edu.harvard.med.hip.bec.coreobjects.sequence.*;

import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.database.*;

import edu.harvard.med.hip.bec.user.*;

import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
import java.util.*;
import edu.harvard.med.hip.utility.*;

/**
 *
 * @author  HTaycher
 */
public class SubmitPolym
{
    public static void main(String args[])
    {
        SubmitPolym s = new SubmitPolym();
        try
        {
            ArrayList clone_data = s.readData("E:\\HTaycher\\Andreas Clontech\\Reports\\Summary_4.txt");
            clone_data = s.getSequenceId(clone_data); System.out.println("1A");
            System.out.println("A");
           
            s.updatePolymorphismData(clone_data);System.out.println("A3");
             s.checkPolymorphismData(clone_data);
            System.out.println("finish");System.out.println("A2");
        }
        catch(Exception e)
        {}
        System.exit(0);
        
    }
    /** Creates a new instance of SubmitPolymFromFile */
    public SubmitPolym()
    {
    }
    
    public void updatePolymorphismData(ArrayList clone_data) throws Exception
    {
        AnalyzedScoredSequence as_seq = null;DiscrDescription d = null;
        CloneDescription clone = null;
        Hashtable discr = new Hashtable();Mutation mut = null;
        Connection conn= DatabaseTransaction.getInstance().requestConnection();
        
        for (int count = 0; count< clone_data.size();count++)
        {
            try
            {
            clone = (CloneDescription)clone_data.get(count);
            as_seq = new AnalyzedScoredSequence(clone.getSequenceId());
            //put found discr in hash
            
            for (int n=0; n< clone.getDiscrepancies().size();n++)
            {
                d = ( DiscrDescription)clone.getDiscrepancies().get(n);
                discr.put(new Integer(d.getPosition()), d);
            }
            for (int disc_count = 0; disc_count < as_seq.getDiscrepancies().size(); disc_count++)
            {
                mut = (Mutation) as_seq.getDiscrepancies().get(disc_count);
                if ( mut.getType() != Mutation.RNA) continue;
               
                d = ( DiscrDescription)discr.get(new Integer(mut.getExpPosition()));
                if ( d== null) continue;
                if ( d.getFlag() )
                {
                    ((RNAMutation)mut).setPolymFlag( Mutation.FLAG_POLYM_YES);//polymorphism or not
                }
                else
                    ((RNAMutation)mut).setPolymFlag( Mutation.FLAG_POLYM_NO);
                
                ((RNAMutation)mut).setPolymId( d.getPolymDataAsString());
                System.out.println(mut .getId());
                ((RNAMutation)mut).updatePolymorphhismData( conn) ;
                conn.commit();
               //  System.out.println("updated clone "+clone.getId() + " "+mut.getId()+" "+mut.getExpPosition() );
               
            }
            
            }catch(Exception e){ System.out.println(e.getMessage());}
        }
            
    }
    
    
      public void checkPolymorphismData(ArrayList clone_data) throws Exception
    {
        AnalyzedScoredSequence as_seq = null;DiscrDescription d = null;
        CloneDescription clone = null;
        Hashtable discr = new Hashtable();Mutation mut = null;
        Connection conn= DatabaseTransaction.getInstance().requestConnection();
        for (int count = 0; count< clone_data.size();count++)
        {
            clone = (CloneDescription)clone_data.get(count);
            as_seq = new AnalyzedScoredSequence(clone.getSequenceId());
            //put found discr in hash
            
            for (int n=0; n< as_seq.getDiscrepancies().size();n++)
            {
                mut = (Mutation) as_seq.getDiscrepancies().get(n);
                if ( mut.getType() != Mutation.RNA) continue;
                discr.put(new Integer(mut.getExpPosition()), mut);
            }
            for (int disc_count = 0; disc_count < clone.getDiscrepancies().size(); disc_count++)
            {
                d = (DiscrDescription) clone.getDiscrepancies().get(disc_count);
                mut = ( Mutation) discr.get(new Integer(d.getPosition()));
                System.out.print("\nclone "+clone.getId() + " "+d.getPosition() );
                if (mut != null) System.out.print(" "+mut.getId()+" "+( (RNAMutation)mut).getPolymorphismFlag() );
               
            }
            
            
        }
    }
    public  ArrayList getSequenceId(ArrayList clone_data) throws Exception
    {
        ResultSet rs = null;CloneDescription clone= null;
        PreparedStatement get_sequenceid = null;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        
        get_sequenceid= t.requestConnection().prepareStatement("select sequenceid from assembledsequence where isolatetrackingid in (select isolatetrackingid from flexinfo where flexcloneid = ?)");
        for (int count = 0; count < clone_data.size(); count++)
        {
            clone = (CloneDescription)clone_data.get(count);
            get_sequenceid.setInt(1, clone.getId());
            rs = t.executeQuery(get_sequenceid);
            if(rs.next())
            {
                clone.setSequenceId( rs.getInt("Sequenceid"));
            }
        }
        
        
        DatabaseTransaction.closeResultSet(rs);
        
        return clone_data;
    }
    
    public ArrayList readData(String  file_name)throws Exception
    {
        ArrayList clone_data = new ArrayList();String line = null;
        ArrayList line_data = null;CloneDescription clone = null;
        BufferedReader fin=null;DiscrDescription d = null;
        //Clone ID	Position	SNP?	GI_SNP
        
        fin = new BufferedReader(new FileReader(file_name));
        while ((line = fin.readLine()) != null)
        {
            if (line.trim().equals( "") ) continue;
            line_data = edu.harvard.med.hip.bec.util.Algorithms.splitString(line,"\t");
            //   System.out.println(line);
            if (  !line_data.get(0).equals( "a" ))
            {
                clone = new CloneDescription(Integer.parseInt((String)line_data.get(0)));
                clone_data.add(clone);
            }
            {
                d = new DiscrDescription();
                d.setPosition(Integer.parseInt((String)line_data.get(1)));
                
                if (  ((String)line_data.get(2)).equalsIgnoreCase("Y"))
                {
                    d.setFlag(true);
                    d.setPolymDataAsString( (String)line_data.get(3));
                }
                ((CloneDescription)clone_data.get(clone_data.size() - 1)).addDiscrepancy(d);
         //       System.out.println( clone.getId()+" "+ d.getPosition() +" "+d.getFlag()+" "+d.getPolymDataAsString());
                continue;
            }
            
        }
        fin.close();
            /*
            System.out.println("size "+clone_data.size());
            for (int count = 0; count< clone_data.size();count++)
            {
                CloneDescription last_clone = (CloneDescription)clone_data.get(count);
                System.out.println( last_clone.getId() + " "+last_clone.getDiscrepancies().size());
                for (int n=0; n< last_clone.getDiscrepancies().size();n++)
                {
                    d = ( DiscrDescription)last_clone.getDiscrepancies().get(n);
                     System.out.print("    "+d.getPosition()+" "+d.getPolymDataAsString()+" "+d.getFlag());
                }
                System.out.println("\n ");
            }
             **/
        return clone_data;
        
    }
    
    
    class CloneDescription
    {
        private int            i_clone_id = -1;
        private ArrayList      i_discr = null;
        private int            i_sequence_id = -1;
        
        public CloneDescription(int id)
        {
            i_clone_id = id;
            i_discr=new ArrayList();
        }
        
        public  int        getId()
        { return i_clone_id;}
        public ArrayList   getDiscrepancies()
        { return i_discr;}
        public int         getSequenceId()
        { return i_sequence_id;}
        public void         setSequenceId(int i)
        {  i_sequence_id = i;}
        
        public void        addDiscrepancy(DiscrDescription d)
        {
            if (i_discr==null) i_discr = new ArrayList();
            i_discr.add(d);
        }
    }
    class DiscrDescription
    {
        private int        i_position =-1;
        private int        i_id = -1;
        private String     i_polym_data = "";
        private ArrayList  i_polym_data_arr = null;
        private boolean    i_isPolym = false;
        
        public DiscrDescription()
        {}
        
        public int       getPosition()
        {return  i_position ;}
        public int       getId()
        { return i_id;}
        public String    getPolymDataAsString()
        { return i_polym_data ;}
        public boolean   getFlag()
        { return i_isPolym;}
        private ArrayList  getPolymData()
        { return i_polym_data_arr ;}
        
        
        public void       setPosition(int v)
        {  i_position = v ;}
        public void       setId(int v)
        {  i_id = v;}
        public void    setPolymDataAsString(String v)
        {  i_polym_data = v;}
        public void        setFlag(boolean v)
        { i_isPolym= v;}
    }
    
    
    
    
    
}
