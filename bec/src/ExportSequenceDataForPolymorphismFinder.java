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
        try {
           BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
            sysProps.verifyApplicationSettings();
              String urlString = "http://localhost:8080/"+ edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION")+"/ExportDataForPolymorphismFinder.do";

            URL url = new URL(urlString);
            url.openStream();
        } catch (Exception ex) 
        {
            System.out.println(ex);
        }
        System.exit(0);
    }
}
	



