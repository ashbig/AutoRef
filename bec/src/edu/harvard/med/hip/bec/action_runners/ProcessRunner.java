/*
 * ProcessRunner.java
 *
 * Created on October 27, 2003, 4:53 PM
 */

package edu.harvard.med.hip.bec.action_runners;
import java.sql.*;
import java.io.*;


import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.assembler.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
  import java.util.*;
  import edu.harvard.med.hip.utility.*;
   
/**
 *
 * @author  HTaycher
 */
public abstract class ProcessRunner implements Runnable
{
    protected ArrayList   m_error_messages = null;
    protected String      m_items = null;
    protected int         m_items_type = -1;
    protected User        m_user = null;
    /** Creates a new instance of ProcessRunner */
    public ProcessRunner()
    {
        m_error_messages = new ArrayList();
    }
    
    public  void        setUser(User v){m_user=v;}
    public  void        setItems(String item_ids)
    {
        m_items = item_ids;
    }
     public  void        setItemsType( int type)
     {
         
         m_items_type = type;
     }
        
     
     public void run()
     {
         if (this instanceof PrimerDesignerRunner)
         {
                  ((PrimerDesignerRunner)this).run();
         }
         else if  (this instanceof PolymorphismFinderRunner)
         {
            
              ((PolymorphismFinderRunner)this).run();
         }//run polymorphism finder
        else if (this instanceof DiscrepancyFinderRunner)
        {
            ((DiscrepancyFinderRunner)this).run();
        }
     }
}
