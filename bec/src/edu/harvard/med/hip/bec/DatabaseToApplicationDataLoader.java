/*
 * DataLoader.java
 *
 * Created on January 18, 2005, 3:57 PM
 */


package edu.harvard.med.hip.bec;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.util.*;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;



/**
 *
 * @author  htaycher
 *load additiona definitions
 */
public class DatabaseToApplicationDataLoader 
{
    public   static  final int SPECIES_NOT_SET  = 0;
    
     
    private static Hashtable      m_project_definition = null;
    private static Hashtable      m_species_definition = null;
  
    public static void            loadDefinitionsFromDatabase()
    {
        loadProjectDefinition();
        loadBiologicalSpeciesNames();
    }
    /** Creates a new instance of DataLoad */
    private static void            loadProjectDefinition()
    {
      
        ResultSet rs = null; 
        m_project_definition = new Hashtable();
        
        String sql = "SELECT PROJECTNAME , PROJECTCODE from PROJECTDEFINITION ";
       
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while ( rs.next())
            {
                m_project_definition.put( rs.getString("PROJECTCODE"), rs.getString("PROJECTNAME"));
            }
        }
        catch(Exception e)
        {
            System.out.println( "Cannot load projects definition");
        }

    }
     
    
    private static void loadBiologicalSpeciesNames()
    {
        ResultSet rs = null; 
        m_species_definition = new Hashtable();
        String sql = "SELECT speciesId, speciesname,IDNAME  from speciesDEFINITION ";
       SpeciesDefinition sd = null;
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while ( rs.next())
            {
                sd = new SpeciesDefinition(rs.getInt("speciesId"), rs.getString("speciesname"), rs.getString("IDNAME"));
                m_species_definition.put( String.valueOf( sd.getCode()), sd);
            }
        }
        catch(Exception e)
        {
            System.out.println( "Cannot load biological species definition");
        }
    }
    
    public static String getSpeciesName(int species_code)
    {
        SpeciesDefinition sd = (SpeciesDefinition) m_species_definition.get( String.valueOf(species_code ));
        if ( m_species_definition == null || m_species_definition.size() == 0 || sd == null)
            return "Not known species";
        else 
            return  sd.getName();
    }
    
    public static Hashtable getSpecies(){ return m_species_definition;}
    
    public static int getSpeciesId(String  species_name)
    {
        if ( m_species_definition == null || m_species_definition.size() == 0) return SPECIES_NOT_SET;
        SpeciesDefinition sd = null;
        for (Enumeration e = m_species_definition.keys() ; e.hasMoreElements() ;)
        {
            sd = (SpeciesDefinition)m_species_definition.get( e.nextElement() );
            if ( sd.getName().trim().equalsIgnoreCase(species_name))
                return sd.getCode();
        }
        return SPECIES_NOT_SET;
    }
    
     public static String getSpeciesUniqueId(int species_code)
    {
        SpeciesDefinition sd = (SpeciesDefinition) m_species_definition.get( String.valueOf(species_code ));
        if ( m_species_definition == null || m_species_definition.size() == 0 || sd == null)
            return "Not known species";
        else 
            return  sd.getIdName();
    }
     public static String getProjectName(char project_code) 
    {
        String project_code_str =  String.valueOf(project_code).toUpperCase();
        String project_name = (String) m_project_definition.get( project_code_str );
        if ( m_project_definition == null || m_project_definition.size() == 0 || project_name == null)
            return "Not known project";
        else 
            return  project_name;
    }
    
      public static void main(String args[])
    {
    DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();
    int n = DatabaseToApplicationDataLoader.getSpeciesId("Homo sapiens");
    SpeciesDefinition sd  = null;
    for (Enumeration e = DatabaseToApplicationDataLoader.getSpecies().keys() ; e.hasMoreElements() ;)
{
	sd = (SpeciesDefinition) DatabaseToApplicationDataLoader.getSpecies().get(e.nextElement());
	System.out.println( sd.getIdName() );
}
      }
}

    

