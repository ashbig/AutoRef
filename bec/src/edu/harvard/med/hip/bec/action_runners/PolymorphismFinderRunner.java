/*
 * PolymorphismFinderRunner.java
 *
 * Created on October 27, 2003, 5:12 PM
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
public class PolymorphismFinderRunner extends ProcessRunner 
{
    private int         m_spec_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;;     
    /** Creates a new instance of PolymorphismFinderRunner */
    public void         setSpecId(int v){m_spec_id = v;}
    
    public void run()
    {
        System.out.println("PolymorphismFinderRunner");
    }
    
}
