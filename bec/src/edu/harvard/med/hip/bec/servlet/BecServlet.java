/*
 * File : BecServlet.java
 * Classes : BecServlet
 *
 * Description :
 *
 *    Insert description here.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 * 
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.2 $
 * $Date: 2005-01-20 16:48:31 $
 * $Author: Elena $
 *
 ******************************************************************************
 *
 * Revision history (Started on July 6, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jul-06-2001 : JMM - Class created. 
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/


package edu.harvard.med.hip.bec.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionServlet;

import edu.harvard.med.hip.bec.util.*;

/**
 *
 * Overides the ActionServlet to provide bec specific functionality
 *
 * @author     $Author: Elena $
 * @version    $Revision: 1.2 $ $Date: 2005-01-20 16:48:31 $
 */

public class BecServlet extends ActionServlet {
     public static final String filePath = "WEB-INF/classes/config/";
    
     /**
     * Initialize this servlet.  Most of the processing has been factored into
     * support methods so that you can override particular functionality at a
     * fairly granular level.
     *
     * @exception ServletException if we cannot configure ourselves correctly
     */
    public void init() throws ServletException {
        super.init();
        initBec();      
       
    }
    
    
    protected void initBec() throws ServletException
    {
        Properties prop = null;
        InputStream systemStream = null;
        BecProperties becProp = BecProperties.getInstance();
        for (int count = 0 ; count < BecProperties.APPLICATION_SETTINGS.length; count++)
        {
            systemStream =   getServletContext().getResourceAsStream(filePath + BecProperties.APPLICATION_SETTINGS[count]);
            if(systemStream == null) {   System.err.println("Unable to read properties file: "+BecProperties.APPLICATION_SETTINGS[count]); }
            prop = new Properties();
            try
            {
                prop.load(systemStream);
              //  System.out.println("properties "+filePath + BecProperties.APPLICATION_SETTINGS[count] +prop.size());
                becProp.setProperties(prop);
            //    System.out.println("properties "+ BecProperties.getInstance().getErrorMessage());
               
             } catch (IOException ioE) 
             {
                throw new ServletException(ioE);
            }
        }
    }
    /**
     * Initializes bec specific resources.
     * 
     * @param className The name of the BecProperties subclass.
     * @param fileName The name of the property file.
     *
     * @exception ServletException when a configuration error is found.
     */
    protected void initBecProp(String className, String fileName) throws ServletException 
    {
        String name = filePath+fileName;
        
       // first load the system configuration info
        InputStream systemStream =   getServletContext().getResourceAsStream(name);
        if(systemStream == null) {
            System.err.println("Unable to read properties file: "+name);
        }
        Properties prop = new Properties();
       // try {
            
            //prop.load(systemStream);
            /*
             * give load values from the properties file into the 
             * SystemProperties class
             */
          //  BecProperties becProp = BecProperties.getInstance();//StaticPropertyClassFactory.makePropertyClass(className);
           // becProp.setProperties(prop);
           
     //   } catch (IOException ioE) {
     //       throw new ServletException(ioE);
     //   }
       
    }
    
} // End class BecServlet


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
