/*
 * UI_GeneOligo.java
 *
 * Created on January 14, 2005, 2:36 PM
 */

package edu.harvard.med.hip.bec.ui_objects;

import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.action_runners.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.*;
import java.util.*;
import java.sql.*;
import java.io.*;

/**
 *
 * @author  htaycher
 */
public class UI_GeneOligo 
{
    private int      m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int      m_clone_id  = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private String      m_oligo_sequence = null;
    private String      m_oligo_name = null;
    private int         m_oligo_position = -1;
    private String      m_plate_label = null;
    private int         m_well = -1;
    private int         m_plate_status = -1;
    private String      m_order_date = null;
    private String         m_user_id  = null;
    /** Creates a new instance of UI_GeneOligo */
    public UI_GeneOligo() {    }
    
    
    
    public int      getOligoID (){ return m_id ; }
     public String      getOligoName (){ return m_oligo_name ; }
    public int      getCloneId (){ return m_clone_id  ; }
    public String      getOligoSequence (){ return m_oligo_sequence ; }
    public int         getOligoPosition (){ return m_oligo_position ; }
    public String      getPlateLabel (){ return m_plate_label ; }
    public int         getWell (){ return m_well ; }
    public int         getPlateStatus (){ return m_plate_status ; }
    public String      getOrderDate (){ return m_order_date ; }
    public String         getUserId (){ return m_user_id ; }
    
    
    public void      setOligoID (int v){   m_id = v ; }
       public void      setOligoName (String v){  m_oligo_name =v ; }
    public void      setCloneId (int v){   m_clone_id  = v ; }
    public void      setOligoSequence ( String v){   m_oligo_sequence = v ; }
    public void         setOligoPosition (int v){   m_oligo_position = v ; }
    public void      setPlateLabel (String v){   m_plate_label = v ; }
    public void         setWell (int v){   m_well = v ; }
    public void         setPlateStatus (int v){   m_plate_status = v ; }
    public void      setOrderDate (String v){   m_order_date = v ; }
    public void         setUserId (String v){   m_user_id = v ; }
    
   public static ArrayList getByCloneId(ArrayList clone_ids) throws Exception
   {
        ArrayList result = new ArrayList();
        ResultSet rs = null;
        String ids = "";
        for (int count = 0; count < clone_ids.size() - 1; count++)
        {
            ids += clone_ids.get(count)+",";
        }
        ids = ids + clone_ids.get(clone_ids.size() - 1);
        String sql = "select g.oligoid, name, cloneid,sequence, g.position as OLIGO_POSITION, "
        +" label, s.position as position , o.status as status, orderdate, "
        +" username as userid from geneoligo g, oligosample s, oligocontainer o, userprofile u "
        +" where u.userid=o.userid and g.oligoid=s.oligoid and s.oligocontainerid = o.OLIGOCONTAINERId and cloneid in ("
        + ids +") order by cloneid , orderdate ";
 
        UI_GeneOligo ui_oligo= null;
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while(rs.next())
            {
                ui_oligo = new UI_GeneOligo();
                ui_oligo.setOligoID (rs.getInt("OLIGOID"));
                ui_oligo.setOligoName(rs.getString("name"));
                ui_oligo.setCloneId (rs.getInt("CLONEID"));
                ui_oligo.setOligoSequence(rs.getString("SEQUENCE"));
                ui_oligo.setOligoPosition (rs.getInt("OLIGO_POSITION"));
                ui_oligo.setPlateLabel(rs.getString("LABEL"));
                ui_oligo.setWell (rs.getInt("POSITION"));
                ui_oligo.setPlateStatus (rs.getInt("STATUS"));
                ui_oligo.setOrderDate (rs.getString("orderdate"));
                ui_oligo.setUserId (rs.getString("USERID"));
                result.add(ui_oligo);
               
            }
            return result;
         }
         catch(Exception e)
         {
             throw new BecDatabaseException("Cannot extract oligo information \nsql "+sql);
         }
            
      
   }
    
    public static void main(String args[])
    {
        ArrayList item_descriptions = null;
        try
        {
            
             ArrayList clone_ids = Algorithms.splitString(ProcessRunner.cleanUpItems( Constants.ITEM_TYPE_CLONEID,  "133913 133894"));
               item_descriptions = UI_GeneOligo.getByCloneId(clone_ids);
                   
        }catch(Exception e){}
        System.exit(0);
     }
   
   }
