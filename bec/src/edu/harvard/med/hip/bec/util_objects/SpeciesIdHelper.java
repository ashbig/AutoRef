/*
 * SpeciesIdHelper.java
 *
 * Created on September 27, 2005, 12:42 PM
 */

package edu.harvard.med.hip.bec.util_objects;

import edu.harvard.med.hip.bec.*;
import java.util.*;
/**
 *
 * @author  htaycher
 */
public class SpeciesIdHelper
  {
      private int               m_code = -1;
      private String            m_id_name = null;
      private int               m_clone_count = 0;

      public SpeciesIdHelper(int i, String v)
      {
          m_code = i;
          m_id_name =v ;
      }
      public int            getCode(){ return m_code;}
      public String         getIdName(){ return m_id_name;}
      public int            getCloneCount(){ return m_clone_count;}

      public void           incrementCount(){ m_clone_count++;}

      public  static SpeciesIdHelper[]       biuldSpeciesIdDefinitions  ()
        {
            int species = DatabaseToApplicationDataLoader.getSpecies().size();
            SpeciesIdHelper[] species_id_definitions = new SpeciesIdHelper[species];
            SpeciesDefinition sd = null;
            for (Enumeration e = DatabaseToApplicationDataLoader.getSpecies().elements() ; e.hasMoreElements() ;)
            {
                 sd = (SpeciesDefinition) e.nextElement();
                 if ( sd.getIdName() != null && sd.getIdName().trim().length() > 0)
                    species_id_definitions[sd.getCode()-1] = new SpeciesIdHelper(sd.getCode(), sd.getIdName());
            }
            return species_id_definitions;

        }
  }
     
