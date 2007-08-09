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
    
    private ArrayList       m_error_messages = null;
    private int             m_number_of_samples_per_container = 96;
    private HashMap         m_containers = null; // by label
    private HashMap         m_flex_sequences = null; // by id 
     private HashMap         m_containers_additional_info = null; // by label
    private HashMap         m_flex_sequences_additional_info = null; // by id 
    private HashMap         m_vectors = null;
     private HashMap         m_linkers = null;
       private HashMap         m_additional_info = null; // by label
    private ArrayList        m_some_data = null;
    
    public ArrayList        getErrorMesages(){ return m_error_messages ;}
    public void            setNumberOfWellsInContainer(int v){m_number_of_samples_per_container = v;}
    public HashMap         getContainers(){ return  m_containers ;} // by label
    public HashMap         getFlexSequences()
    { 
        return m_flex_sequences;
    } // by id 
    public HashMap         getVectors(){ return m_vectors;} // by name 
    public HashMap          getLinkers(){ return m_linkers;}// by name
    public  HashMap         getAdditionalInfo(){ return m_additional_info;}
    public ArrayList        getArrayOfObjects(){ return m_some_data;}
    
    
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
                  m_flex_sequences_additional_info= new HashMap(); 
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
             FileStructure      mapping_file_structure    ) throws Exception
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
       
         boolean isFirstLine = true;
         try 
        {
            in = new BufferedReader(new InputStreamReader(input));
        
            while((line = in.readLine()) != null) 
            {
                 if (isTrimLine) line = line.trim();
                 if (  isFirstHeader && isFirstLine ) 
                 {
                    String[] header_content = Algorithms.splitString(line, ConstantsImport.TAB_DELIMETER, false, -1);
                    number_of_columns = header_content.length;
                     //construct recordStruct
                    header_struct = processHeader(header_content, mapping_file_structure);
                    isFirstLine = false;
                    continue;
                 }
                 if ( isDropEmptyString && !line.equals("") ) 
                 {
                     row_content = Algorithms.splitString(line, ConstantsImport.TAB_DELIMETER, false, -1);
                     if (number_of_columns != row_content.length)
                         m_error_messages.add("Line "+line+" has a problem, correct file.");
                     else
                     {
                         // one row cannot define more than one object of the particular type
                         processRow(header_struct, row_content, mapping_file_structure.getType());
                     }
                 } 
             
            }
            in.close();
           
        }
        catch(Exception e)
        {
            in.close();
            throw new Exception("Cannot read file " +file_type );
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
          { setObjectPropertyOneFileSubmission(   records_out  ); break;}
          case FileStructure.FILE_TYPE_GENE_INFO:
          { setAdditionalInfo(records_out, m_flex_sequences_additional_info); break;}
          case FileStructure.FILE_TYPE_AUTHOR_INFO:
          { setAdditionalInfo(records_out, m_containers_additional_info); break;}
          case FileStructure.FILE_TYPE_PLATE_MAPPING:
          { setObjectsPlateMappingFileSubmission(records_out); break;}
          case FileStructure.FILE_TYPE_VECTOR_INFO:
          { setVectors(records_out); break;}
          case FileStructure.FILE_TYPE_VECTOR_FEATURE_INFO:
          { setVectorsFeatures(records_out, m_additional_info ); break;}
          case FileStructure.FILE_TYPE_LINKER_INFO:
          { setLinkers(records_out); break;}
          case FileStructure.FILE_TYPE_CLONING_STRATEGY:
          { setCloningStrategy(records_out); break;}
      
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
   
  private  void              setObjectPropertyOneFileSubmission( ColumnValue[] records_out      )
        throws Exception
  {
     
      ImportContainer row_container = setContainerProperties(records_out);
      String flex_sequence_id = setFlexSequenceProperties( records_out);
       ImportSample   sample= setSampleProperties( records_out );
       sample.setSequenceId(flex_sequence_id);
       row_container.addSample(sample);
       System.out.println(row_container.getLabel()+" "+sample.getPosition()+" "+flex_sequence_id);
  }
  
  private  void              setAdditionalInfo( ColumnValue[] records_of_row, HashMap additional_info      )
        throws Exception
  {
      ArrayList additional_info_from_record = new ArrayList();
      PublicInfoItem p_info = null;
      String key = null;
      String temp_object_type = null;String temp_property_name = null;
      for ( int count = 0; count < records_of_row.length; count++)
      {
             if ( records_of_row[count].isEmptyField()) continue;
       
         temp_object_type = records_of_row[count].getObjectType() ;
         temp_property_name = records_of_row[count].getObjectProperty();
         if ( temp_object_type.intern() == FileStructureColumn.PROPERTY_NAME_USER_ID)
         {
              key = records_of_row[count].getColumnValue().toUpperCase();
          }
          else
          {
              p_info= new PublicInfoItem(temp_property_name, records_of_row[count].getColumnValue());
              p_info.setIsSubmit(records_of_row[count].isPubInfoSubmit());
              additional_info_from_record.add(p_info);
           }
      }
      additional_info.put(key, additional_info_from_record);
      System.out.println(key +" "+ additional_info_from_record.size());
  }
  
  private  void              setObjectsPlateMappingFileSubmission( ColumnValue[] records_out      )
        throws Exception
  {
     
       ImportContainer row_container = setContainerProperties(records_out);
       ImportSample   sample= setSampleProperties( records_out );
       row_container.addSample(sample);
       System.out.println(row_container.getLabel()+" "+sample.getPosition());
  }
  
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
      String temp_object_type = null;String temp_property_name = null;
     for ( int count = 0; count < records_of_row.length; count++)
     {
             if ( records_of_row[count].isEmptyField()) continue;
       
         temp_object_type = records_of_row[count].getObjectType() ;
         temp_property_name = records_of_row[count].getObjectProperty();
         if ( temp_object_type.intern() == FileStructureColumn.OBJECT_TYPE_CONTAINER)
          {
               if (temp_property_name.intern() == ImportContainer.PROPERTY_LABEL)
              {
                  // container there?
                  label = records_of_row[count].getColumnValue().toUpperCase();
                  row_container = (ImportContainer) m_containers.get(label);
                  if ( row_container == null)
                  {
                      row_container = new ImportContainer(records_of_row[count].getColumnValue(),
                              m_number_of_samples_per_container);
                      m_containers.put(label, row_container);
                      if ( container_additional_properties.size() > 0)
                          row_container.getPublicInfo().addAll(container_additional_properties);
                  }
              }
              else
              {
                  p_info= new PublicInfoItem(temp_property_name, records_of_row[count].getColumnValue());
                    p_info.setIsSubmit(records_of_row[count].isPubInfoSubmit());
            
                  if ( row_container != null )
                   {
                      row_container.addPublicInfo(p_info);
                   }
                  else
                  {
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
      ImportSample row_sample = null;
      ArrayList       sample_additional_properties = new ArrayList();
      PublicInfoItem p_info = null;
       int position = -1;
       String temp_object_type = null;String temp_property_name = null;
   
     for ( int count = 0; count < records_of_row.length; count++)
     {
              if ( records_of_row[count].isEmptyField()) continue;
       
          temp_object_type = records_of_row[count].getObjectType() ;
          temp_property_name = records_of_row[count].getObjectProperty();
          if (row_sample == null ) row_sample = new ImportSample( );
          if ( temp_object_type.intern() == FileStructureColumn.OBJECT_TYPE_SAMPLE)
          {
              if (temp_property_name.intern() == ImportSample.SAMPLE_POSITION)
              {
                  try
                  {
                      position = Integer.parseInt(records_of_row[count].getColumnValue());
                  }
                  catch(Exception e)
                  {
                      position = Algorithms.convertWellFromA8_12toInt(records_of_row[count].getColumnValue());
                  }
                  if ( position < 0) 
                      throw new Exception ("Wrong sample position "+records_of_row[count].getColumnValue());
                 row_sample.setPosition( position);
                 
              }
              else if (temp_property_name.intern() == ImportSample.SAMPLE_CONSTRUCT_TYPE)
              {
                  row_sample.setConstructType(records_of_row[count].getColumnValue());
              }
              else if ( temp_property_name.intern() == ImportSample.SAMPLE_CONSTRUCT_SIZE)
              {
                  row_sample.setConstructSize(records_of_row[count].getColumnValue());
              }
               else if ( temp_property_name.intern() == ImportSample.SAMPLE_CLONE_STATUS)
               {
                  row_sample.setCloneStatus( records_of_row[count].getColumnValue());
               }
                else if ( temp_property_name.intern() == ImportSample.SAMPLE_CLONE_TYPE )
                {
                  row_sample.setCloneType(records_of_row[count].getColumnValue());
                }
                else if ( temp_property_name.intern() == ImportSample.SAMPLE_CLONING_STRATEGYID)
                {
                  row_sample.setCloningStrategyId( Integer.parseInt(records_of_row[count].getColumnValue()));
                }
              else
              {
                  p_info= new PublicInfoItem(temp_property_name, records_of_row[count].getColumnValue());
                    p_info.setIsSubmit(records_of_row[count].isPubInfoSubmit());
                   row_sample.addPublicInfo(p_info);
                 
               }
       }
     }
       if ( row_sample.getPosition() > 0 ) return row_sample;
       throw new Exception ("Sample position not defined ");
     
  }
  private  String    setFlexSequenceProperties(          ColumnValue[] records_of_row ) throws Exception
  {
      ImportFlexSequence row_sequence = null;
      PublicInfoItem p_info = null;
      ArrayList       flexsequence_additional_properties = new ArrayList();
       String sequence_id = null;
      String temp_object_type = null;String temp_property_name = null;
    boolean isCheckSequenceTotalNumberCodons = true;
      for ( int count = 0; count < records_of_row.length; count++)
      {
          if ( records_of_row[count].isEmptyField()) continue;
          temp_object_type = records_of_row[count].getObjectType() ;
          temp_property_name = records_of_row[count].getObjectProperty();
       
          if ( temp_object_type.intern() == FileStructureColumn.OBJECT_TYPE_FLEXSEQUENCE)
          {
               if (temp_property_name.intern() == ImportFlexSequence.PROPERTY_NAME_SEARCHID)
              {
                  sequence_id= records_of_row[count].getColumnValue().toUpperCase();
                  m_flex_sequences.put( sequence_id, records_of_row[count].getInstruction()+
                          records_of_row[count].getColumnValue().toUpperCase());
                  return sequence_id;
              }
              //coming from file that define one sequence in a row
              if ( row_sequence == null) row_sequence = new ImportFlexSequence();
              if (temp_property_name.intern() == ImportFlexSequence.PROPERTY_NAME_CDSNA_SOURCE)
              {
                row_sequence.setCDNASource(records_of_row[count].getColumnValue());
              }
              else if (temp_property_name.intern() == ImportFlexSequence.PROPERTY_NAME_SPECIES)
              {
                row_sequence.setSpesies( records_of_row[count].getColumnValue() );
              }
              else if (temp_property_name.intern() == ImportFlexSequence.PROPERTY_NAME_CDS_START)
              {
                row_sequence.setCDSStart(Integer.parseInt(records_of_row[count].getColumnValue()));
              }
              else if (temp_property_name.intern() == ImportFlexSequence.PROPERTY_NAME_CDS_STOP)
              {
                row_sequence.setCDSStop(Integer.parseInt(records_of_row[count].getColumnValue()));
              }
              else if (temp_property_name.intern() == ImportFlexSequence.PROPERTY_NAME_CHROMOSOME)
              {
                row_sequence.setChromosome(records_of_row[count].getColumnValue());
              }
              else if (temp_property_name.intern() == ImportFlexSequence.PROPERTY_NAME_SEQUENCETEXT)
              {
                row_sequence.setSequenceText(records_of_row[count].getColumnValue());
              }
              else 
              {
                   if ( temp_property_name.intern() == FileStructureColumn.PROPERTY_NAME_USER_ID)
                   {
                       sequence_id = records_of_row[count].getColumnValue();
                   }
                   else
                   {
                         p_info = new PublicInfoItem(temp_property_name, records_of_row[count].getColumnValue());
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
              throw new Exception ("Cannot id sequence ");
        
          m_flex_sequences.put( sequence_id, row_sequence);
      return sequence_id;
  }
}
