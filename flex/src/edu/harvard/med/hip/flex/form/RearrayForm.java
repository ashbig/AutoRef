/*
 * RearrayForm.java
 *
 * Created on December 12, 2002, 3:20 PM
 */

package edu.harvard.med.hip.flex.form;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.infoimport.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  htaycher
 */
public class RearrayForm extends ProjectWorkflowProtocolForm
{
    
   /* private String        m_forwardName = null;
    private int           m_projectid = -1;
    private int           m_workflowid = -1;
     private String        m_workflowname = null;
    private String        m_projectname = null;
    private String        m_processname = null;
    */
    private FormFile      m_rearrayFile = null;
    private   boolean       m_isPutOnQueue = true;
    private String         m_platetype = null;
    private int            m_wells_on_plate = 96;
    private String          m_sample_type = null;
    private   boolean       m_isSortBySawToothpatern= true;
    private boolean         m_isFullPlate = false;
    private boolean         m_small = false;
    private boolean         m_medium = false;
    private boolean     m_large = false;
    
    
    
    private boolean             m_isControls = true;
   private boolean              m_isArrangeBySize= true;
    
   public void          setWellsOnPlate(int s){ m_wells_on_plate = s;}
   public int           getWellsOnPlate(){ return m_wells_on_plate;}
   
    public void         setPlatetype(String platetype)            {        m_platetype = platetype;    }
    public String       getPlatetype()       {return m_platetype;}
    
    public void         setSampletype(String sample_type)            {        m_sample_type = sample_type;    }
    public String       getSampletype()       {return m_sample_type;}
    
    
    public void         setIsSortBySawToothpatern(boolean isSortBySawToothpatern)   {      m_isSortBySawToothpatern = isSortBySawToothpatern;    }
    public boolean      getIsSortBySawToothpatern(){ return m_isSortBySawToothpatern;}
    
    public void         setIsArrangeBySize(boolean isArrangeBySize)   {      m_isArrangeBySize = isArrangeBySize;    }
    public boolean      getIsArrangeBySize(){ return m_isArrangeBySize;}
    
    public void         setIsControls(boolean isControls)   {      m_isControls = isControls;    }
    public boolean      getIsControls()  { return m_isControls;}
    
      
    public void         setIsPutOnQueue(boolean isPutOnQueue)   {      m_isPutOnQueue = isPutOnQueue;    }
    public boolean      getIsPutOnQueue()                          {        return m_isPutOnQueue;    }
    
    public void         setRequestFile(FormFile requestFile)   {      m_rearrayFile = requestFile;    }
    public FormFile     getRequestFile()                          {        return m_rearrayFile;    }
   
  /*  public void         setForwardName(String forwardName)            {        m_forwardName = forwardName;    }
    public String       getForwardName()                               {        return m_forwardName;    }
    
    public void         setWorkflowname(String name)    {        m_workflowname = name;    }
    public String       getWorkflowname()    {        return m_workflowname;    }
    
    public void         setProcessname(String name)    {        m_processname = name;    }
    public String       getProcessname()    {        return m_processname;    }
    
    public void         setProjectname(String name)    {        m_projectname = name;    }
    public String       getProjectname()    {        return m_projectname;    }
    
    public void         setProjectid(int projectid)    {        m_projectid = projectid;   }
    public int          getProjectid()    {        return m_projectid;    }
    
    public void         setWorkflowid(int workflowid)    {        m_workflowid = workflowid;    }
    public int          getWorkflowid()   {        return m_workflowid;    }
    
 */
    public void setIsFullPlate(boolean isFullPlate) {        m_isFullPlate = isFullPlate;    }
    public boolean getIsFullPlate() {        return m_isFullPlate;    }
 
    public void setSmall(boolean small) {m_small = small;    }
    public boolean getSmall() {        return m_small;    }
    
    public void setMedium(boolean medium) {  m_medium = medium;    }
    public boolean getMedium() {        return m_medium;    }
    
    public void setLarge(boolean large) { m_large = large;    }
    public boolean getLarge() {        return m_large;    }
    /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping,
    HttpServletRequest request)
    {
        
        ActionErrors errors = new ActionErrors();
        if ((m_rearrayFile.getFileName() == null) || (m_rearrayFile.getFileName().trim().length() < 1))
            errors.add("mgcRequestFile", new ActionError("error.mgcRequestFile.required"));
        
        
        return errors;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        m_isPutOnQueue = true;
        m_wells_on_plate = 96;
        m_isSortBySawToothpatern= true;
        m_isFullPlate = false;
        m_small = false;
        m_medium = false;
        m_large = false;
    }   
    
}
