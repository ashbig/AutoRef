/*
 * PlateMapperVerifier.java
 *
 * Created on March 12, 2008, 3:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package psi_data_converter.verification;

import java.util.*;
import java.io.*;
/**
 *
 * @author htaycher
 */
public class PlateMapperVerifier  extends Verifier
{
    
    /** Creates a new instance of PlateMapperVerifier */
    public void writeTempAuthorFile(HashMap<String,String> psi_site_description,
            HashMap<String,String> clone_ids, 
            String file_name, String header,
            ArrayList<String> additional_authors,
            boolean isAppend) throws Exception
    {
         BufferedWriter input = null;
         // read author info and stor author 
        try
        {
            input = new BufferedWriter(new FileWriter(file_name));
            if ( header != null) input.write(header+"\n");
            for ( String clone_id : clone_ids.keySet())  
            {
                input.write( clone_id +"\t" + psi_site_description.get(clone_ids.get(clone_id))+"\n");
                for( String author: additional_authors)
                {
                    input.write(clone_id +"\t" + author +"\n");
                }
            }
            input.flush();
            input.close();
           
        }
        catch(Exception e)
        {
            throw new Exception ("Cannot write author file " + file_name );
        }
        finally
        {
            if (input != null) input.close();
        }
    }
    
}
