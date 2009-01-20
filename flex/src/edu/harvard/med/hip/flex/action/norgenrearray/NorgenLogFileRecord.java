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
public class NorgenLogFileRecord {
// //         1         K1  A   1      AAA05  B   1    07Jan2009 10:10:50
            
    private String  i_orgLabel;
    private String  i_orgWellName;
    private int     i_orgWellPosition;
    private String  i_destLabel;
    private int     x_orgContainerid;
    private int     x_orgSampleId;
    private int     x_orgConstructId;
    private String  i_destWellName;
    private int     i_destWellPosition;
    private int     i_recordNumber;
    private int     x_destSsmpleId;
    private int         x_cloneid;
    private String  x_sample_type;
    private String  x_status_gb;
    
    public NorgenLogFileRecord(String v)
    {
        String[] temp = v.trim().split("\\s+");
        i_orgLabel = temp[4];
       // i_destWellName = temp[2]+String.format("%1$02s", temp[3]);
        String tp = (temp[3].length() == 1)? "0"+temp[3]:temp[3];
         i_destWellName = temp[2]+tp;
         tp = (temp[6].length() == 1)? "0"+temp[6]:temp[6];
         i_orgWellName = temp[5]+tp;
     
      i_orgWellPosition=edu.harvard.med.hip.flex.util.Algorithms.convertWellFromA8_12toInt(i_orgWellName);
      i_destLabel = temp[1];
       i_destWellPosition =edu.harvard.med.hip.flex.util.Algorithms.convertWellFromA8_12toInt(i_destWellName);
        i_recordNumber = Integer.parseInt(temp[0]);
    }
    
    public  String  getOrgLabel  (){ return i_orgLabel;}
    public  String  getOrgWellName  (){ return i_orgWellName;}
    public  int     getOrgPosition  (){ return i_orgWellPosition;}
    public  String  getDestLabel  (){ return i_destLabel;}
    public  String  getDestWellName  (){ return i_destWellName;}
    public  int     getDestPosition  (){ return i_destWellPosition;}
    public  int     getRecordNumber  (){ return i_recordNumber;}
    public int      getOrgContainerid(){ return x_orgContainerid;}
    public int      getCloneId(){ return x_cloneid;}
    public String   getSampleType(){ return x_sample_type;}
    public String   getStatusGB(){ return x_status_gb;}
    
    public int      getOrgSampleId(){ return x_orgSampleId;}
    public int      getDestSampleId(){ return x_destSsmpleId;}
    public void      setOrgContainerid(int v){   x_orgContainerid=v;}
    public void     setOrgSampleId(int v){ x_orgSampleId=v;}
    public void     setDestSsmpleId(int v){ x_destSsmpleId = v;}
    public void     setOrgConstructId(int v){x_orgConstructId=v;}
    public int      getOrgConstructId(){ return x_orgConstructId;}
    public void     setCloneId(int v){ x_cloneid = v;}
    public void     setSampleType(String v){ x_sample_type= v;}
    public void     setStatusGB(String v){ x_status_gb= v;}
    
    public String   toString()
    {
        return ""+ i_recordNumber+" "+i_orgLabel+" "+i_orgWellName+"("
                +i_orgWellPosition+") "+  i_destLabel+" "+ i_destWellName
                +"("+i_destWellPosition+")";
    }
}
