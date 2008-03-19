/*
 * FileReader.java
 *
 * Created on June 22, 2007, 1:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.file_mapping;

import java.util.*;
import java.io.*;
import edu.harvard.med.hip.flex.infoimport.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.*;
/**
 *
 * @author htaycher
 */
public class DataFileReader 
{
    public static final int     SUBMISSION_VECTOR = 0;
     public static final int     SUBMISSION_PLATES = 1;
     public static final int    SUBMISSION_LINKER = 2;
      public static final int    SUBMISSION_NO_SETTINGS_REQUIRED = 3;
   
      //set by user
    private int             m_number_of_samples_per_container = 96;
    private boolean             i_is_CreateCloneObjectPerSample = false;
  
       
       // parsed data
    private ArrayList       m_error_messages = null;
     private HashMap         m_containers = null; // by label
    private HashMap          m_flex_sequences = null; // by id 
    private HashMap          m_authors = null; // by id 
    private HashMap          m_publications = null; // by id 
  
    private HashMap          m_clones = null; // by id 
     private HashMap         m_containers_additional_info = null; // by label
 //   private HashMap         m_flex_sequences_additional_info = null; // by id 
    private HashMap         m_vectors = null;
     private HashMap         m_linkers = null;
    private HashMap         m_additional_info = null; // by label
    private ArrayList        m_some_data = null;
    
    //temp storage
    private HashMap         i_samples_hash = null;
   
    public ArrayList        getErrorMesages(){ return m_error_messages ;}
    public void            setNumberOfWellsInContainer(int v){m_number_of_samples_per_container = v;}
    public void            isCreateCloneObjectPerSample(boolean v){i_is_CreateCloneObjectPerSample = v;}
    public void             emptySampleHashMap(){ i_samples_hash = null;}
    
    public HashMap         getContainers(){ return  m_containers ;} // by label
    public HashMap         getFlexSequences()    {         return m_flex_sequences;    } // by id 
    public HashMap         getVectors(){ return m_vectors;} // by name 
    public HashMap          getLinkers(){ return m_linkers;}// by name
    public  HashMap         getAdditionalInfo(){ return m_additional_info;}
    public  HashMap         getClones(){ return m_clones;}
    public HashMap          getSamples(){ if ( i_samples_hash == null) createSampleHashMap(); return i_samples_hash;}
   
    public ArrayList        getArrayOfObjects(){ return m_some_data;}
    public HashMap          getAuthors(){ return m_authors;}
     public HashMap          getPublications(){ return m_publications;}
   
    public void            resetAdditionalInfo(){  m_additional_info = new HashMap();}
  
    
    public DataFileReader(int submission_type)
    {
         m_error_messages = new ArrayList();
        switch(submission_type)
        {
            case SUBMISSION_PLATES:
            {
        
                  m_containers = new HashMap(); // by label
                  m_flex_sequences = new HashMap(); 
                  m_containers_additional_info= new HashMap(); 
                //  m_flex_sequences_additional_info= new HashMap(); 
                  m_additional_info =  new HashMap();
                  m_clones= new HashMap(); 
            }
            case SUBMISSION_VECTOR:
            {
                m_vectors = new HashMap(); 
                m_additional_info =  new HashMap(); 
            }
            case SUBMISSION_LINKER:
            {
                m_linkers= new HashMap();
                
            }
            case DataFileReader.SUBMISSION_NO_SETTINGS_REQUIRED:
                m_some_data = new ArrayList();
        }
    }
    
    
    public DataFileReader()
    {
          this(SUBMISSION_PLATES);
    }
    
    
    public     void  readFileIntoSetOfObjects(InputStream input, boolean isFirstHeader,
            int file_type, boolean isDropEmptyString, boolean isTrimLine,
             FileStructure      mapping_file_structure ) throws Exception//,String object_type   ) throws Exception
    {

        
        BufferedReader in = null;
        String line = null;
        Collection header_struct = null;
        String[] row_content = null;
        int number_of_columns = -1;
        Container row_container  = null;
        Sample row_sample = null;
        FlexSequence row_sequence = null;
        Construct    row_construct = null;
        InputStreamReader reader = null;
        
         boolean isFirstLine = true;
         try 
        {
             reader = new InputStreamReader(input);
            in = new BufferedReader(reader);
        
            while((line = in.readLine()) != null) 
            {
                 if (isTrimLine) line = line.trim();
                 if (  isFirstHeader && isFirstLine ) 
                 {
                    String[] header_content = line.split(ConstantsImport.TAB_DELIMETER);//Algorithms.splitString(line, ConstantsImport.TAB_DELIMETER, false, -1);
                    number_of_columns = header_content.length;
                     //construct recordStruct
                    header_struct = processHeader(header_content, mapping_file_structure);
                    isFirstLine = false;
                    continue;
                 }
                 if ( isDropEmptyString && !line.equals("") ) 
                 {
                     row_content = line.split(ConstantsImport.TAB_DELIMETER);//Algorithms.splitString(line, ConstantsImport.TAB_DELIMETER, false, -1);
                     if (number_of_columns != row_content.length )
                         m_error_messages.add("Line "+line+" has a problem, correct file.");
                     else
                     {
                         // one row cannot define more than one object of the particular type
                         processRow(header_struct, row_content, mapping_file_structure.getType());//,object_type);
                     }
                 } 
             
            }
            in.close();
           reader.close();
        }
        catch(Exception e)
        {
            in.close();reader.close();
            throw new Exception("Can not read file " +file_type +" line "+line);
        }
 
    
    }
   
    
    //converts header into hash of RecordStructure that defines what columns
    // define what object property, goal: resolve concatenation of multipale columns into one property
   // public static
      private Collection       processHeader
            (String[] header_content, FileStructure mapping_file_structure)
            throws Exception
    {
        HashMap header_struct = new HashMap();
         
        FileStructureColumn current_column = null;
        String header_name = null;
        RecordDescription current_column_structure_description = null;
        for ( int count = 0; count < header_content.length; count++)
        {
            current_column = (FileStructureColumn) mapping_file_structure.getColumns().get(header_content[count].toUpperCase());
            if ( current_column == null) continue;
            if (current_column.getObjectPropertyOrder() < 0 )
            {
                current_column_structure_description = 
                        new     RecordDescription(count, current_column);
                header_struct.put(current_column.getObjectType()+"_"+current_column.getObjectPropertyName() ,current_column_structure_description);
            }
            else
            {
                current_column_structure_description = (RecordDescription) header_struct.get(current_column.getObjectType()+"_"+current_column.getObjectPropertyName() );
                if ( current_column_structure_description == null)
                {
                    current_column_structure_description = new     RecordDescription(count, current_column, mapping_file_structure.getMaxNumberOfColumnsPerProperty());
                    header_struct.put(current_column.getObjectType()+"_"+current_column.getObjectPropertyName() ,current_column_structure_description);
                }
                else
                {
                    current_column_structure_description.setColumnNumber( count, current_column.getObjectPropertyOrder());
                }
                
               
            }
        } 
        return header_struct.values();
        
    }
    
    // record out: object_type, property_name, property_value, property_instruction
  private  void          processRow(Collection header_struct, String[] row_content ,
          int file_type) throws Exception
  {
      int number_of_object_property_value = header_struct.size();
      RecordDescription object_property_value_description = null;
      ColumnValue[]  records_out = constructRecordsOut( header_struct,  row_content);
     // records_out= sortRecordsOut(  records_out);
      switch(file_type) 
      {
          case FileStructure.FILE_TYPE_ONE_FILE_SUBMISSION:
          { processOneFileSubmission(   records_out  ); break;}
          
          case FileStructure.FILE_TYPE_PLATE_MAPPING:
          { processPlateMappingFile(records_out); break;}
       
          case FileStructure.FILE_TYPE_SEQUENCE_INFO:
          { processClonePropertiesFile(records_out); break;}
        
          case FileStructure.FILE_TYPE_GENE_INFO:
          { processGeneInfoFile(records_out); break;}
         
          case FileStructure.FILE_TYPE_AUTHOR_INFO:
          { setAuthorProperties(records_out); break;}
             case FileStructure.FILE_TYPE_AUTHOR_CONNECTION:
          {setOneToManyConnector( records_out,m_additional_info); break;}
           case FileStructure.FILE_TYPE_PUBLICATION_INFO:
          { setPublicationProperties(records_out); break;}
           
           case FileStructure.FILE_TYPE_PUBLICATION_CONNECTION:
          {
              setOneToManyConnector( records_out,m_additional_info);
              break;
           }
      
          case FileStructure.FILE_TYPE_REFERENCE_SEQUENCE_INFO:
          { setFlexSequenceProperties(   records_out); break;}
       
           
          case FileStructure.FILE_TYPE_VECTOR_INFO:
          { setVectors(records_out); break;}
          case FileStructure.FILE_TYPE_VECTOR_FEATURE_INFO:
          { setVectorsFeatures(records_out, m_additional_info ); break;}
          case FileStructure.FILE_TYPE_LINKER_INFO:
          { setLinkers(records_out); break;}
          case FileStructure.FILE_TYPE_CLONING_STRATEGY:
          { setCloningStrategy(records_out); break;}
       /*   case FileStructure.FILE_TYPE_OBJECT_ANNOTATIONS :
          { setAdditionalInfo(   records_out, 
           m_additional_info  ,  object_type    ); break;}*/
      }
  }

     // record out: object_type, property_name, property_value, property_instruction
  private  ColumnValue[]          constructRecordsOut(Collection header_struct, String[] row_content)
  throws Exception
  {
      int number_of_object_property_value = header_struct.size();
      RecordDescription object_property_value_description = null;
      ColumnValue[]   records_out = new ColumnValue[number_of_object_property_value];
      int record_count=0;
      ColumnValue   record_out = null;
      String value = ""; int[] column_numbers = null; boolean isKey = false;
      Iterator iter = header_struct.iterator();
      try
      {
          while ( iter.hasNext() )
     {
          object_property_value_description = (RecordDescription)iter.next();
          column_numbers = object_property_value_description.getColumnsPerProperty();
          for (int count = 0; count < column_numbers.length; count++)
          {
              value += row_content[column_numbers[count]];
          }
          // translate if needed
          if (object_property_value_description.getFileColumnDescription().getPropertyTranslation() != null &&
            object_property_value_description.getFileColumnDescription().getPropertyTranslation() .size() > 0)
          {
              value = (String)object_property_value_description.getFileColumnDescription().getPropertyTranslation() .get(value);
          }   
//create records
          record_out = new  ColumnValue(
                      object_property_value_description.getFileColumnDescription().getObjectType(),
                      object_property_value_description.getFileColumnDescription().getObjectPropertyName(),
                        value,
                        object_property_value_description.getFileColumnDescription().getPropertyInstruction(),
                       object_property_value_description.getFileColumnDescription().isKey(),
                  object_property_value_description.getFileColumnDescription().isSubmit());
          records_out[record_count++] = record_out;
          value = "";
      }
      
      return records_out;
      }
      catch(Exception e)
      {
          System.out.println(e.getMessage());
          throw new Exception(e.getMessage());
      }
  }

  private  ColumnValue[]     sortRecordsOut(ColumnValue[]    records_out)
  {
      
      //  "CONTAINER" /  "CONSTRUCT" / "FLEXSEQUENCE" /   "SAMPLE"
      Arrays.sort( records_out , new Comparator() 
            {
                public int compare(Object o1, Object o2) 
                {
                    ColumnValue  ocast1 = (ColumnValue)o1;
                    ColumnValue ocast2 = (ColumnValue)o2;
                    if(! ocast1.getObjectType().equalsIgnoreCase(ocast2.getObjectType()))
                        return ocast1.getObjectType().compareTo(ocast2.getObjectType());
                    
                    return ocast1.getObjectProperty().compareTo(ocast2.getObjectProperty());
                     
                }
                /** Note: this comparator imposes orderings that are
                 * inconsistent with equals. */
                public boolean equals(java.lang.Object obj)
                {      return false;  }
                // compare
            } );
     return records_out;
  }
   
 
  
  
   private  void              processOneFileSubmission( ColumnValue[] records_out      )
        throws Exception
  {
     
      ImportContainer row_container = setContainerProperties(records_out);
      String flex_sequence_id = setFlexSequenceProperties( records_out);
       ImportSample   sample= setSampleProperties( records_out );
       sample.setSequenceId(flex_sequence_id);
       row_container.addSample(sample);
       ImportClone row_clone = setCloneProperties(records_out);
       ImportClone temp_clone = (ImportClone)m_clones.get( row_clone.getUserId());
       if ( i_is_CreateCloneObjectPerSample && 
               row_clone!= null    ) 
       {    sample.setClone( row_clone) ;}
     
  }
  
  private  String              setAdditionalInfo( ColumnValue[] records_of_row, 
          HashMap additional_info   )throws Exception
  {
        return   setAdditionalInfo( records_of_row, additional_info  ,  null  );
  }
 
   
  private  String              setAdditionalInfo( ColumnValue[] records_of_row, 
          HashMap additional_info  , String object_type  )
        throws Exception
  {
      ArrayList additional_info_from_record = new ArrayList();
      PublicInfoItem p_info = null;
      String key = null;
      String temp_object_type = null;String temp_property_name = null;String temp_column_value = null;
      for ( int count = 0; count < records_of_row.length; count++)
      {
         if ( records_of_row[count].isEmptyField()) continue;
       
         temp_object_type = records_of_row[count].getObjectType() ;
         // allow to collect data for any object
         if ( object_type != null && !temp_object_type.equalsIgnoreCase(object_type) ) continue;
         
         temp_property_name = records_of_row[count].getObjectProperty();
          temp_column_value = records_of_row[count].getColumnValue().trim();
       
          if ( temp_property_name.intern() == FileStructureColumn.PROPERTY_NAME_USER_ID)
         {
              key = temp_column_value.trim();
          }
          else
          {
              p_info= new PublicInfoItem(temp_property_name, temp_column_value);
              p_info.setIsSubmit(records_of_row[count].isPubInfoSubmit());
              additional_info_from_record.add(p_info);
           }
      }
      additional_info.put(key, additional_info_from_record);
      return key;
     // System.out.println(key +" "+ additional_info_from_record.size());
  }
  
  
  
   private  void              processClonePropertiesFile( ColumnValue[] records_out      )
        throws Exception
  {
     try
     {
       String flex_sequence_id = setFlexSequenceProperties( records_out);
       if ( i_samples_hash == null ) createSampleHashMap();
       ImportSample   sample= (ImportSample) i_samples_hash.get( flex_sequence_id );
       sample.setSequenceId(flex_sequence_id);
       
       ImportClone row_clone = setCloneProperties(records_out);
       ImportClone temp_clone = (ImportClone)m_clones.get( row_clone.getUserId());
       if ( i_is_CreateCloneObjectPerSample && 
               row_clone!= null && temp_clone != null               ) 
           temp_clone.reasignCloneProperties( row_clone) ;
     }
     catch(Exception e)
     {
         throw new Exception("Can not read sequence "+ records_out);
     }
   }
   
   
    private void    createSampleHashMap()
    {
        i_samples_hash= new HashMap();
        ImportSample sample = null; ImportContainer cur_container  =null;
        PublicInfoItem p_info = null;
            
        Iterator iter1 = m_containers.values().iterator();
        while(iter1.hasNext())
        {
             cur_container = (ImportContainer) iter1.next();
             for (int count_sample = 0; count_sample < cur_container.getSamples().size();count_sample++)
             {
                 sample = (ImportSample) cur_container.getSamples().get(count_sample);
                 p_info = PublicInfoItem.getPublicInfoByName( FileStructureColumn.PROPERTY_NAME_USER_ID, sample.getPublicInfo());
                 if ( p_info != null) 
                     i_samples_hash.put(p_info.getValue(), sample);
             }
        }
    }
    
    
  private  void              processPlateMappingFile( ColumnValue[] records_out      )
        throws Exception
  {
     
       ImportContainer row_container = setContainerProperties(records_out);
       ImportSample   sample= setSampleProperties( records_out );
       row_container.addSample(sample);
       ImportClone row_clone = setCloneProperties(records_out);
       ImportClone temp_clone = (ImportClone)m_clones.get( row_clone.getUserId());
       temp_clone.reasignCloneProperties( row_clone) ;
      
       System.out.println(row_container.getLabel()+" "+sample.getPosition());
  
  }
  
   private  void              processGeneInfoFile( ColumnValue[] records_out      )
        throws Exception
  {
       ArrayList item_data = null; 
       PublicInfoItem p_info = null;
       
       resetAdditionalInfo();
       String key = setAdditionalInfo( records_out, m_additional_info);
       // get target sequence
       ImportFlexSequence  row_sequence = (ImportFlexSequence)m_flex_sequences.get(key);
        if ( row_sequence == null) return;
// add info
       // assign flexsequence names to flexsequences
        Iterator  iter = m_additional_info.keySet().iterator();
        while(iter.hasNext())
       {
          key = (String)iter.next();
          item_data = (ArrayList) m_additional_info.get(key);
          //reassigne PROPERTY_NAME_SPECIES
          for (int count = 0; count < item_data.size(); count++)
          {
                p_info= ( PublicInfoItem)item_data.get(count);
                if (p_info.getName().intern() == ImportFlexSequence.PROPERTY_NAME_SPECIES )
                 { row_sequence.setSpesies(p_info.getValue());}
                 else row_sequence.addPublicInfo(p_info);
          }
        }
   }

    /*private  void              processAuthorConnectionsFile( ColumnValue[] records_out      )
        throws Exception
  {
  
     //   resetAdditionalInfo();
        //record author name 
        String key = setOneToManyConnector( records_out, m_additional_info);
        ImportAuthor author = (ImportAuthor) m_authors.get(  key );
        DataConnectorObject row_connector_object = (DataConnectorObject)m_additional_info.get(key);
        if (row_connector_object.getType().intern() == FileStructureColumn.OBJECT_TYPE_CONTAINER)
        {
            //container go by label
             ImportContainer cur_container = (ImportContainer) m_containers.get(row_connector_object.getId());
             if ( cur_container != null)  cur_container.addAuthor( author );
        }
        else if (row_connector_object.getType().intern() == FileStructureColumn.OBJECT_TYPE_SAMPLE)
        {
            ImportClone cur_clone = (ImportClone) m_clones.get(key);
            if ( cur_clone != null)cur_clone.addAuthor( author );
        }                                     
              
    }
    
    */
   private  void    setVectors(  ColumnValue[] records_of_row )
  {
      CloneVector row_vector =    new CloneVector();
      String key = null;
      String temp_object_type = null;String temp_property_name = null;
     
      for ( int count = 0; count < records_of_row.length; count++)
     {
             if ( records_of_row[count].isEmptyField()) continue;
       
         temp_object_type = records_of_row[count].getObjectType() ;
         temp_property_name = records_of_row[count].getObjectProperty();
         if ( records_of_row[count].isKey()) key = records_of_row[count].getColumnValue();
         if ( temp_object_type.intern() == FileStructureColumn.OBJECT_TYPE_VECTOR)
          {
             
              if (temp_property_name.intern() == ImportVector.VECTOR_DESCRIPTION)
              {
                  row_vector.setDescription(records_of_row[count].getColumnValue());
              }
              else  if (temp_property_name.intern() == ImportVector.VECTOR_FILE)
              {
                    row_vector.setFile(records_of_row[count].getColumnValue());
              }
               else  if (temp_property_name.intern() == ImportVector.VECTOR_FILEPATH)
              {
                    row_vector.setPath(records_of_row[count].getColumnValue());
              }
               else  if (temp_property_name.intern() == ImportVector.VECTOR_HIPNAME)
              {
                row_vector.setHipname(records_of_row[count].getColumnValue());
              }
               else  if (temp_property_name.intern() == ImportVector.VECTOR_NAME)
              {
                row_vector.setName(records_of_row[count].getColumnValue());
              }
               else  if (temp_property_name.intern() == ImportVector.VECTOR_RESTRICTIONS)
              {
                row_vector.setRestriction(records_of_row[count].getColumnValue());
              }
               else  if (temp_property_name.intern() == ImportVector.VECTOR_SOURCE)
              {
                row_vector.setSource(records_of_row[count].getColumnValue());
              }
               else  if (temp_property_name.intern() == ImportVector.VECTOR_TYPE)
              {
                row_vector.setType(records_of_row[count].getColumnValue());
              }
       }
         
     }
      if ( key == null || row_vector.getName()==null || row_vector.getType()==null) return;
      m_vectors.put(key, row_vector);
   }
   
   
   
  
   private  void    setVectorsFeatures(  ColumnValue[] records_of_row, HashMap items_to_collect )
  {
        VectorFeature row_vector_feature  = new VectorFeature   (); 
        String key = null;
        ArrayList features = null;
      String temp_object_type = null;String temp_property_name = null;
     for ( int count = 0; count < records_of_row.length; count++)
     {
             if ( records_of_row[count].isEmptyField()) continue;
       
         temp_object_type = records_of_row[count].getObjectType() ;
         temp_property_name = records_of_row[count].getObjectProperty();
          
         if ( records_of_row[count].isKey()) key = records_of_row[count].getColumnValue();
         if ( temp_object_type.intern() == FileStructureColumn.OBJECT_TYPE_VECTOR_FEATURE)
          {
              if (temp_property_name.intern() == ImportVectorFeature.VECTOR_FEATURE_DESCRIPTION)
              {
                  row_vector_feature.setDescription(records_of_row[count].getColumnValue());
              }
              else  if (temp_property_name.intern() == ImportVectorFeature.VECTOR_FEATURE_NAME)
              {
                  row_vector_feature.setName(records_of_row[count].getColumnValue());
              }
              else  if (temp_property_name.intern() == ImportVectorFeature.VECTOR_FEATURE_STATUS)
              {
                  row_vector_feature.setStatus(records_of_row[count].getColumnValue());
              }
              else  if (temp_property_name.intern() == ImportVectorFeature.VECTOR_FEATURE_VECTORNAME)
              {
                  row_vector_feature.setVectorName( records_of_row[count].getColumnValue());
              }
            
         }
       }
       if ( key==null || row_vector_feature.getName() == null)
       features = (ArrayList) m_additional_info.get(key);
       if ( features == null )       {           features = new ArrayList();   m_additional_info.put(key,features ) ;    }
       features.add(row_vector_feature);
     //  System.out.println(row_vector_feature.toString());
   }
   
   
   
     private  void    setLinkers(  ColumnValue[] records_of_row )
  {
      CloneLinker row_linker =    new CloneLinker();
      String key = null;
      String temp_object_type = null;String temp_property_name = null;
     
      for ( int count = 0; count < records_of_row.length; count++)
     {
             if ( records_of_row[count].isEmptyField()) continue;
       
         temp_object_type = records_of_row[count].getObjectType() ;
         temp_property_name = records_of_row[count].getObjectProperty();
         if ( temp_object_type.intern() == FileStructureColumn.OBJECT_TYPE_LINKER)
          {
             
              if (temp_property_name.intern() == ImportLinker.LINKER_NAME)
              {
                  row_linker.setName(records_of_row[count].getColumnValue());
              }
              else  if (temp_property_name.intern() == ImportLinker.LINKER_SEQUENCE)
              {
                    row_linker.setSequence(records_of_row[count].getColumnValue());
              }
              
       }
         
     }
      if (  row_linker.getName()== null ) return;
      m_linkers.put(row_linker.getName(), row_linker);
   }
     
     
     
  private  void    setCloningStrategy(  ColumnValue[] records_of_row )
  {
      CloningStrategy row_strategy =    new CloningStrategy();
      String key = null;
      String temp_object_type = null;String temp_property_name = null;
     
     for ( int count = 0; count < records_of_row.length; count++)
     {
             if ( records_of_row[count].isEmptyField()) continue;
       
         temp_object_type = records_of_row[count].getObjectType() ;
         temp_property_name = records_of_row[count].getObjectProperty();
         if ( temp_object_type.intern() == FileStructureColumn.OBJECT_TYPE_CLONING_STRATEGY)
          {
             
              if (temp_property_name.intern() == ImportCloningStrategy.CS_FIVE_PRIME_LINKER_NAME)
              {
                  row_strategy.setLinker5p(new CloneLinker(-1, records_of_row[count].getColumnValue(), null));
              }
              else  if (temp_property_name.intern() == ImportCloningStrategy.CS_THREE_PRIME_LINKER_NAME)
              {
                   row_strategy.setLinker3p(new CloneLinker(-1, records_of_row[count].getColumnValue(), null));
            
              }
               else  if (temp_property_name.intern() == ImportCloningStrategy.CS_TYPE)
              {
                   row_strategy.setType(  records_of_row[count].getColumnValue() );
            
              }
               else  if (temp_property_name.intern() == ImportCloningStrategy.CS_VECTOR_NAME)
              {
                   row_strategy.setClonevector(new CloneVector( records_of_row[count].getColumnValue() ));
            
              }
               else  if (temp_property_name.intern() == ImportCloningStrategy.CS_NAME)
              {
                   row_strategy.setName( records_of_row[count].getColumnValue() );
            
              }
              
       }
         
     }
      if (  row_strategy.getClonevector().getName()== null || 
              row_strategy.getLinker5p().getName() == null ||
              row_strategy.getLinker5p().getName() == null ) return;
      if (row_strategy.getType() == null )row_strategy.setType("entry plasmid");
      m_some_data.add(  row_strategy);
   }
     
     
  //one sample per row
  private  ImportContainer    setContainerProperties(  ColumnValue[] records_of_row )
  {
      ImportContainer row_container = null;
      ArrayList       container_additional_properties = new ArrayList();
      String label = null;
      PublicInfoItem p_info = null;
      String temp_object_type = null;String temp_property_name = null;String temp_column_value= null;
     for ( int count = 0; count < records_of_row.length; count++)
     {
         if ( records_of_row[count].isEmptyField()) continue;
       
         temp_object_type = records_of_row[count].getObjectType() ;
         temp_property_name = records_of_row[count].getObjectProperty();
         temp_column_value = records_of_row[count].getColumnValue().trim();
         
         if ( temp_object_type.intern() == FileStructureColumn.OBJECT_TYPE_CONTAINER)
          {
               if (temp_property_name.intern() == ImportContainer.PROPERTY_LABEL)
              {
                  // container there?
                  label = temp_column_value.toUpperCase();
                  row_container = (ImportContainer) m_containers.get(label);
                  if ( row_container == null)
                  {
                      row_container = new ImportContainer(temp_column_value,
                              m_number_of_samples_per_container);
                      m_containers.put(label, row_container);
                      if ( container_additional_properties.size() > 0)
                          row_container.getPublicInfo().addAll(container_additional_properties);
                  }
              }
              else
              {
                  p_info= new PublicInfoItem(temp_property_name, temp_column_value);
                    p_info.setIsSubmit(records_of_row[count].isPubInfoSubmit());
            
                  if ( row_container != null )
                   {
                      row_container.addPublicInfo(p_info);
                   }
                  else
                  {
                      if  ( !container_additional_properties.contains(p_info)) 
                        container_additional_properties.add(p_info);
                  }
               }
       }
     }
     return row_container;
     
  }
    
  //one sample per row
  private  ImportSample    setSampleProperties(ColumnValue[] records_of_row )
    throws Exception
  {
      ImportSample row_sample = null;ImportClone row_clone = null;
      ArrayList       sample_additional_properties = new ArrayList();
      PublicInfoItem p_info = null;
       int position = -1;
       String temp_object_type = null;String temp_property_name = null;
      String temp_column_value = null;
     for ( int count = 0; count < records_of_row.length; count++)
     {
              if ( records_of_row[count].isEmptyField()) continue;
       
          temp_object_type = records_of_row[count].getObjectType() ;
          temp_property_name = records_of_row[count].getObjectProperty();
          temp_column_value = records_of_row[count].getColumnValue().trim();
       
          
          if (row_sample == null ) 
          {
              row_sample = new ImportSample( );
              if (i_is_CreateCloneObjectPerSample)
              {
                  row_clone = new ImportClone();
                  row_sample.setClone(row_clone);
                  
              }
          }
          if ( temp_object_type.intern() == FileStructureColumn.OBJECT_TYPE_SAMPLE)
          {
              if (temp_property_name.intern() == ImportSample.SAMPLE_POSITION)
              {
                  try
                  {
                      position = Integer.parseInt(temp_column_value);
                  }
                  catch(Exception e)
                  {
                      position = Algorithms.convertWellFromA8_12toInt(temp_column_value);
                  }
                  if ( position < 0) 
                      throw new Exception ("Wrong sample position "+temp_column_value);
                 row_sample.setPosition( position);
                 
              }
              else if (temp_property_name.intern() == ImportSample.SAMPLE_CONSTRUCT_TYPE)
              {
                  row_sample.setConstructType(temp_column_value);
              }
              else if ( temp_property_name.intern() == ImportSample.SAMPLE_CONSTRUCT_SIZE)
              {
                  row_sample.setConstructSize(temp_column_value);
              }
           /*    else if ( temp_property_name.intern() == ImportSample.SAMPLE_CLONE_STATUS)
               {
                  row_sample.setCloneStatus( temp_column_value);
               }
                else if ( temp_property_name.intern() == ImportSample.SAMPLE_CLONE_TYPE )
                {
                  row_sample.setCloneType(temp_column_value);
                }
                else if ( temp_property_name.intern() == ImportSample.SAMPLE_CLONING_STRATEGYID)
                {
                  row_sample.setCloningStrategyId( Integer.parseInt(temp_column_value));
                }
            **/
              else
              {
                  p_info= new PublicInfoItem(temp_property_name, temp_column_value);
                    p_info.setIsSubmit(records_of_row[count].isPubInfoSubmit());
                   row_sample.addPublicInfo(p_info);
                   if ( temp_property_name.intern() == FileStructureColumn.PROPERTY_NAME_USER_ID)
                 {
                      if (row_clone != null) 
                      {
                          row_clone.setUserId(temp_column_value);
                          m_clones.put(temp_column_value, row_clone);
                      }
                 }
                 
               }
       }
     }
       if ( row_sample.getPosition() > 0 ) return row_sample;
       throw new Exception ("Sample position not defined ");
     
  }
  
  
  
  //one sample per row, assume that clones already created
  private  ImportClone    setCloneProperties(ColumnValue[] records_of_row )
    throws Exception
  {
      ImportClone row_clone = null;
      ArrayList       clone_additional_properties = new ArrayList();
      PublicInfoItem p_info = null;
       int position = -1;
       String temp_object_type = null;String temp_property_name = null;
      String temp_column_value = null;
     for ( int count = 0; count < records_of_row.length; count++)
     {
          if ( records_of_row[count].isEmptyField()) continue;
          temp_object_type = records_of_row[count].getObjectType() ;
          temp_property_name = records_of_row[count].getObjectProperty();
          temp_column_value = records_of_row[count].getColumnValue().trim();
          if (row_clone == null ){ row_clone = new ImportClone( );}
          
          // this can be sampleid
           if ( temp_property_name.intern() == FileStructureColumn.PROPERTY_NAME_USER_ID)
          { row_clone.setUserId(temp_column_value);     }
          if ( temp_object_type.intern() == FileStructureColumn.OBJECT_TYPE_CLONE)
          {
              if (temp_property_name.intern() == ImportClone.CLONE_STATUS)
              {row_clone.setStatus(temp_column_value.toUpperCase());}
              else if (temp_property_name.intern() == ImportClone.CLONE_TYPE)
               {row_clone.setType(temp_column_value.toUpperCase());}
              else if ( temp_property_name.intern() == ImportClone.FIVE_PRIME_LINKER)
               {row_clone.set5LinkerName(temp_column_value);}
               else if ( temp_property_name.intern() == ImportClone.THREE_PRIME_LINKER)
                {row_clone.set3LinkerName(temp_column_value);}
               else if ( temp_property_name.intern() == ImportClone.CLONE_SEQUENCE_TEXT)
                {row_clone.setSequenceText(temp_column_value);}
                else if ( temp_property_name.intern() == ImportClone.VECTOR)
                 {row_clone.setVectorName(temp_column_value);}
                else if ( temp_property_name.intern() == ImportClone.CLONING_STRATEGYID)
                {
                   try
                  {
                      row_clone.setCloningStrategyId( Integer.parseInt(temp_column_value));
                  }
                  catch(Exception e)
                  {
                      throw new Exception ("Wrong clon cloning strategy id "+temp_column_value);
                  }
                 
                }
              else
              {
                  p_info= new PublicInfoItem(temp_property_name, temp_column_value);
                    p_info.setIsSubmit(records_of_row[count].isPubInfoSubmit());
                   row_clone.addPublicInfo(p_info);
                  
                 
               }
     }
     }
      return row_clone;
  }
  private  String    setFlexSequenceProperties(          ColumnValue[] records_of_row ) throws Exception
  {
      ImportFlexSequence row_sequence = null;
      PublicInfoItem p_info = null;
      ArrayList       flexsequence_additional_properties = new ArrayList();
       String sequence_id = null;
      String temp_object_type = null;String temp_property_name = null; String temp_column_value = null;
    
      boolean isCheckSequenceTotalNumberCodons = true;
      
      
      for ( int count = 0; count < records_of_row.length; count++)
      {
          if ( records_of_row[count].isEmptyField()) continue;
          temp_object_type = records_of_row[count].getObjectType() ;
          temp_property_name = records_of_row[count].getObjectProperty();
          temp_column_value = records_of_row[count].getColumnValue().trim();
        
          
          if ( temp_object_type.intern() == FileStructureColumn.OBJECT_TYPE_FLEXSEQUENCE)
          {
               if (temp_property_name.intern() == ImportFlexSequence.PROPERTY_NAME_SEARCHID)
              {
                  sequence_id= temp_column_value.toUpperCase();
                  m_flex_sequences.put( sequence_id, records_of_row[count].getInstruction()+
                          temp_column_value.toUpperCase());
                  return sequence_id;
              }
              //coming from file that define one sequence in a row
              if ( row_sequence == null) row_sequence = new ImportFlexSequence();
              if (temp_property_name.intern() == ImportFlexSequence.PROPERTY_NAME_CDSNA_SOURCE)
              {
                row_sequence.setCDNASource(temp_column_value);
              }
              else if (temp_property_name.intern() == ImportFlexSequence.PROPERTY_NAME_SPECIES)
              {
                row_sequence.setSpesies(temp_column_value);
              }
              else if (temp_property_name.intern() == ImportFlexSequence.PROPERTY_NAME_CDS_START)
              {
                row_sequence.setCDSStart(Integer.parseInt(temp_column_value));
              }
              else if (temp_property_name.intern() == ImportFlexSequence.PROPERTY_NAME_CDS_STOP)
              {
                row_sequence.setCDSStop(Integer.parseInt(temp_column_value));
              }
              else if (temp_property_name.intern() == ImportFlexSequence.PROPERTY_NAME_CHROMOSOME)
              {
                row_sequence.setChromosome(temp_column_value);
              }
              else if (temp_property_name.intern() == ImportFlexSequence.PROPERTY_NAME_SEQUENCETEXT)
              {
                row_sequence.setSequenceText(temp_column_value);
              }
              else 
              {
                   if ( temp_property_name.intern() == FileStructureColumn.PROPERTY_NAME_USER_ID)
                   {
                       sequence_id = temp_column_value;
                   }
                   else
                   {
                         p_info = new PublicInfoItem(temp_property_name, temp_column_value);
                         p_info.setIsSubmit(records_of_row[count].isPubInfoSubmit());
                         flexsequence_additional_properties.add(p_info);
            
                   }
                        

              }
           }
         
      }
       row_sequence.setPublicInfo( flexsequence_additional_properties);
          // if by some reason cds_start && cds_stop not set but sequence is OK set them
          if ( row_sequence.getCDSStop() == -1 && row_sequence.getCDSStart() == -1
                  && row_sequence.getSequenceText()!= null && row_sequence.getSequenceText().length() > 3)
          {
              row_sequence.setCDSStart(1);
              row_sequence.setCDSStop(row_sequence.getSequenceText().length()  );
            
              if (row_sequence.getSequenceText().length() % 3 != 0)
              {
                   p_info =  PublicInfoItem.getPublicInfoByName(ImportFlexSequence.PROPERTY_NAME_IS_CHECK_CDS, row_sequence.getPublicInfo());
                   if (  p_info != null && p_info.getValue().intern() == ImportFlexSequence.PROPERTY_VALUE_NOTCHECK_CDS)
                        isCheckSequenceTotalNumberCodons = false;
                   else
                //  System.out.println("sequence length: "+ row_sequence.getSequenceText().length() +" gene id: "+PublicInfoItem.getPublicInfoByName( "GENE_ID", row_sequence.getPublicInfo()));
                      throw new Exception ("Wrong sequence text "+row_sequence.getSequenceText().length());
              }
                  
          }

          if ( isCheckSequenceTotalNumberCodons &&  (row_sequence.getCDSStop() - row_sequence.getCDSStart() + 1) % 3 != 0)
              throw new Exception ("Wrong sequence text ");
          if (sequence_id == null) 
              throw new Exception ("Can not get sequence id. ");
        
          m_flex_sequences.put( sequence_id, row_sequence);
      return sequence_id;
  }
  
  
    //one sample per row
  private  ImportAuthor    setAuthorProperties(ColumnValue[] records_of_row )
    throws Exception
  {
      ImportAuthor row_author = null; String key = null;
      String temp_object_type = null;String temp_property_name = null;
      String temp_column_value = null;
      if ( m_authors == null) m_authors = new HashMap();
      
      for ( int count = 0; count < records_of_row.length; count++)
     {
          if ( records_of_row[count].isEmptyField()) continue;
          temp_object_type = records_of_row[count].getObjectType() ;
          temp_property_name = records_of_row[count].getObjectProperty();
          temp_column_value = records_of_row[count].getColumnValue().trim();
        
          
          if (row_author == null ) row_author = new ImportAuthor( );
          if ( temp_object_type.intern() == FileStructureColumn.OBJECT_TYPE_AUTHOR)
          {
             
              if (temp_property_name.intern() == ImportAuthor.AUTHOR_FNNAME )
              {  row_author.setFNName(temp_column_value);              }
             else if (temp_property_name.intern() == ImportAuthor.AUTHOR_LNNAME )
              {  row_author.setFLName(temp_column_value);  }
             else if (temp_property_name.intern() == ImportAuthor.AUTHOR_TEL)
             {  row_author.setTel(temp_column_value);  }
             else if (temp_property_name.intern() == ImportAuthor.AUTHOR_FAX )
              {  row_author.setFax(temp_column_value);}  
             else if (temp_property_name.intern() == ImportAuthor.AUTHOR_EMAIL )
             {  row_author.setEMail(temp_column_value);  }
             else if (temp_property_name.intern() == ImportAuthor. AUTHOR_ORGANIZATION_NAME)
              {  row_author.setOrgName(temp_column_value);  }
             else if (temp_property_name.intern() == ImportAuthor. AUTHOR_ADDRESS)
              {  row_author.setAdress(temp_column_value);  }
             else if (temp_property_name.intern() == ImportAuthor.  AUTHOR_WWW)
              {  row_author.setWWW(temp_column_value);  }
                else if (temp_property_name.intern() == ImportAuthor.AUTHOR_TYPE)
              {  row_author.setType(temp_column_value);  }
            
             else if (temp_property_name.intern() == ImportAuthor. AUTHOR_DESCRIPTION)
              {  row_author.setDescription(temp_column_value);  }
               else if (temp_property_name.intern() == FileStructureColumn.PROPERTY_NAME_USER_ID)
              {  key = temp_column_value; row_author.setName(temp_column_value); }
          }
 
       }
       if ( row_author != null ) m_authors.put(key, row_author);
       return row_author;
     }
     
  
    private  ImportPublication    setPublicationProperties(ColumnValue[] records_of_row )
    throws Exception
  {
      ImportPublication row_publication = null; String key = null;
      String temp_object_type = null;String temp_property_name = null;
      String temp_column_value = null;
      if ( m_publications == null) m_publications = new HashMap();
      
      for ( int count = 0; count < records_of_row.length; count++)
     {
          if ( records_of_row[count].isEmptyField()) continue;
          temp_object_type = records_of_row[count].getObjectType() ;
          temp_property_name = records_of_row[count].getObjectProperty();
          temp_column_value = records_of_row[count].getColumnValue().trim();
          if (row_publication == null ) row_publication = new ImportPublication( );
          if ( temp_object_type.intern() == FileStructureColumn.OBJECT_TYPE_PUBLICATION)
          {
             if (temp_property_name.intern() == ImportPublication.PUBLICATION_TITLE )
              {  
                 row_publication.setTitle(temp_column_value);           
             }
             else if (temp_property_name.intern() == ImportPublication.PUBLICATION_PBMEDID )
              { 
                 key = temp_column_value; row_publication.setPubMedID(temp_column_value); 
             }
          }
 
       }
       m_publications.put(key, row_publication);
       return row_publication;
     }
    
    
     private  void              setOneToManyConnector( ColumnValue[] records_of_row, 
          HashMap hash_info     )
        throws Exception
  {
      ArrayList items_per_key = new ArrayList();
      String key = null;
      String temp_object_type = null;String temp_property_name = null;String temp_column_value = null;
      DataConnectorObject primary_key_connector_object = null;
      DataConnectorObject f_key_connector_object = null;
      
      for ( int count = 0; count < records_of_row.length; count++)
      {
         if ( records_of_row[count].isEmptyField()) continue;
       
         temp_object_type = records_of_row[count].getObjectType() ;
         temp_property_name = records_of_row[count].getObjectProperty();
         temp_column_value = records_of_row[count].getColumnValue().trim();
       
         if ( temp_property_name.intern() == FileStructureColumn.PROPERTY_NAME_USER_ID)
         {
              primary_key_connector_object = new DataConnectorObject(temp_column_value, temp_object_type);
         }
          else
          {
               f_key_connector_object = new DataConnectorObject(temp_column_value, temp_object_type);
          }
           
       
      }
      if ( ! hash_info.containsKey(primary_key_connector_object)  ) //new entity
      {
         items_per_key = new ArrayList();
         hash_info.put(primary_key_connector_object, items_per_key);
      }
     else
         items_per_key = (ArrayList)hash_info.get(primary_key_connector_object);
     items_per_key.add(f_key_connector_object);
     
     // System.out.println(key +" "+ additional_info_from_record.size());
  }
  
  
}
