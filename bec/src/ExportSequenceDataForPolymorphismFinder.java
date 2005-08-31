/*
 * ExportSequenceDataForPolymorphismFinder.java
 *
 * Created on August 1, 2005, 2:21 PM
 */

/**
 *
 * @author  htaycher
 */
import java.net.*;
import java.io.*;
import edu.harvard.med.hip.bec.util.*;

    /** Creates a new instance of ExportSequenceDataForPolymorphismFinder */
public class ExportSequenceDataForPolymorphismFinder
{
  
    public static void main(String [] args)
    {
        try 
        {
            String urlString = null;
            BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
            sysProps.verifyApplicationSettings();
            if ( args == null || args.length == 0 || args[0].equalsIgnoreCase("submit"))
                 urlString = "http://localhost:8080/"+ edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION")+"/ExportDataForPolymorphismFinder.do?param=submit";
            else if (args[0].equalsIgnoreCase("get"))
                 urlString = "http://localhost:8080/"+ edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION")+"/ExportDataForPolymorphismFinder.do?param=get";
  
            URL url = new URL(urlString);
            url.openStream();
        } catch (Exception ex) 
        {
            System.out.println(ex);
        }
        System.exit(0);
    }
}
	



