/*
 * File : FlexServlet.java
 * Classes : FlexServlet
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
 * $Revision: 1.6 $
 * $Date: 2009-03-30 14:51:52 $
 * $Author: et15 $
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


package edu.harvard.med.hip.flex.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionServlet;

import edu.harvard.med.hip.flex.util.*;

/**
 *
 * Overides the ActionServlet to provide flex specific functionality
 *
 * @author     $Author: et15 $
 * @version    $Revision: 1.6 $ $Date: 2009-03-30 14:51:52 $
 */

public class FlexServlet extends ActionServlet {
     public static final String filePath = "WEB-INF/classes/config/";
    
     /**
     * Initialize this servlet.  Most of the processing has been factored into
     * support methods so that you can override particular functionality at a
     * fairly granular level.
     *
     * @exception ServletException if we cannot configure ourselves correctly
     */
    public void init() throws ServletException {
         System.out.println("______________P");
        super.init();
        initFlex();        
    }
    
    
    protected void initFlex() throws ServletException {
             System.out.println("______________AP");
    
        initFlexProp("FlexProperties", "SystemConfig.properties");
        initFlexProp("ContainerTypeProperties", "ContainerType.properties");
         
    }
    /**
     * Initializes flex specific resources.
     * 
     * @param className The name of the FlexProperties subclass.
     * @param fileName The name of the property file.
     *
     * @exception ServletException when a configuration error is found.
     */
    protected void initFlexProp(String className, String fileName) throws ServletException {
        String name = "/"+filePath+fileName;
       // first load the system configuration info
        InputStream  systemStream = getServletContext().getResourceAsStream(name);
        if(systemStream == null) {
            System.out.println("__________Unable to read properties file: "+name);
        }
          Properties prop = new Properties();
        try {
            
            prop.load(systemStream);
            /*
             * give load values from the properties file into the 
             * SystemProperties class
             */
            FlexProperties flexProp = StaticPropertyClassFactory.makePropertyClass(className);
            flexProp.setProperties(prop);
           
        } catch (IOException ioE) {
            System.out.println("initProp "+ systemStream.toString() + " eee "+ioE.getMessage());
            throw new ServletException(ioE);
        }
       
    }
    
} // End class FlexServlet


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
