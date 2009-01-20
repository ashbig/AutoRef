/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.action.norgenrearray;

import java.util.*;

/**
 *
 * @author htaycher
 */
public class NorgenDestinationPlate {
    private ArrayList<NorgenLogFileRecord>  i_records= null;
    private String                          i_dest_label=null;
    private int                             i_dest_containerid=-1;
    
    public NorgenDestinationPlate(){i_records= new ArrayList();}
    public void         addWellRecord(NorgenLogFileRecord v){i_records.add(v);}
    public void         setLabel(String v){i_dest_label=v;}
    public void         setId(int v){i_dest_containerid=v;}
    public String       getLabel( ){return i_dest_label ;}
    public int         getId( ){return i_dest_containerid ;}
    public ArrayList<NorgenLogFileRecord>  getRecords(){ return i_records;}
    
    public String         verify()
    {
        NorgenLogFileRecord prev_record=null;
        for (NorgenLogFileRecord record : i_records)
        {
            if (prev_record != null  && !prev_record.getDestLabel().equals(record.getDestLabel()))
            {prev_record=null;}
            if ( prev_record != null && (record.getDestPosition()-prev_record.getDestPosition()!=1))
            {
                return record.toString();
            }
            prev_record = record;
        }
        return null;
    }
  
}
