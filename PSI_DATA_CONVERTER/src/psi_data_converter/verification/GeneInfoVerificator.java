/*
 * GeneInfoVerificator.java
 *
 * Created on February 5, 2008, 12:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package psi_data_converter.verification;

import java.util.*;
import java.io.*;
//import  edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportFlexSequence;
/**
 *
 * @author htaycher
 */
public class GeneInfoVerificator extends Verifier
{
     /** Creates a new instance of GeneInfoVerificator */
    public GeneInfoVerificator(String filename)    {        super( filename);    }
    
    public ArrayList    getSpeciesNames(ArrayList er_messages, String species_name_header)throws Exception
    {
        ArrayList species = new ArrayList();
        int species_column_num = -1;
      //  String column_name = ImportFlexSequence.PROPERTY_NAME_SPECIES;
        BufferedReader output = null ; 
        String line ; String[] items;
        // read author info and stor author 
        String cur_species;
        try
        {
            output = new BufferedReader(new FileReader(m_file_name));
            line = output.readLine();// read header
            species_column_num = defineColumnNumber(line, species_name_header);
            if (species_column_num < 0 )
            {
                er_messages.add("Cannot find species column in file "+m_file_name);
                return null;
            }
            while ( (line = output.readLine() ) != null)
            {
                items = line.split("\t");
                if ( items.length < species_column_num-1)
                {
                        er_messages.add("Cannot find species value in file "+m_file_name + "\n"+line);
                }
                else
                {
                    cur_species = items[species_column_num].trim();
                    if (! species.contains(cur_species))
                        species.add(cur_species);
                }
            }
            output.close();
            return species;
        }
        catch(Exception e)
        {
            throw new Exception ("Cannot read gene info file info file " + m_file_name);
        }
        finally
        {
            if (output != null) output.close();
        }
        
    }
     public ArrayList getSpeciesNames(List<String[]> records, int species_column)
     {
         ArrayList species_names = new ArrayList();
         for (  String[] record : records)
         {
             if ( !species_names.contains( record[species_column]))
                 species_names.add(record[species_column]);
         }
         return species_names;
     }
    
}
