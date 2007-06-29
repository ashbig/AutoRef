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
public class FileReader 
{
     
    private ArrayList       m_error_messages = null;
    private int             m_number_of_samples_per_container = 96;
    private HashMap         m_containers = null; // by label
    private HashMap         m_flex_sequences = null; // by id 
     private HashMap         m_containers_additional_info = null; // by label
    private HashMap         m_flex_sequences_additional_info = null; // by id 
    
    
    public ArrayList        getErrorMesages(){ return m_error_messages ;}
    public void            setNumberOfWellsInContainer(int v){m_number_of_samples_per_container = v;}
    public HashMap         getContainers(){ return  m_containers ;} // by label
    public HashMap         getFlexSequences(){ return m_flex_sequences;} // by id 
     
    public FileReader()
    {
          m_containers = new HashMap(); // by label
          m_flex_sequences = new HashMap(); 
          m_error_messages = new ArrayList();
          m_containers_additional_info= new HashMap(); 
          m_flex_sequences_additional_info= new HashMap(); 
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
    private  Collection       processHeader
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
              setObjectPropertyOneFileSubmission(   records_out  );
          case FileStructure.FILE_TYPE_GENE_INFO:
              setAdditionalInfo(records_out, m_flex_sequences_additional_info);
          case FileStructure.FILE_TYPE_AUTHOR_INFO:
              setAdditionalInfo(records_out, m_containers_additional_info);
          case FileStructure.FILE_TYPE_PLATE_MAPPING:
              setObjectsPlateMappingFileSubmission(records_out);
      
      }
  }

     // record out: object_type, property_name, property_value, property_instruction
  private  ColumnValue[]          constructRecordsOut(Collection header_struct, String[] row_content)
  {
      int number_of_object_property_value = header_struct.size();
      RecordDescription object_property_value_description = null;
      ColumnValue[]   records_out = new ColumnValue[number_of_object_property_value];
      int record_count=0;
      ColumnValue   record_out = null;
      String value = ""; int[] column_numbers = null;
      Iterator iter = header_struct.iterator();
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
                        object_property_value_description.getFileColumnDescription().getPropertyInstruction());
          records_out[record_count++] = record_out;
          value = "";
      }
      
      return records_out;
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
       sample.setSequenceID(flex_sequence_id);
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
         temp_object_type = records_of_row[count].getObjectType() ;
         temp_property_name = records_of_row[count].getObjectProperty();
         if ( temp_object_type.intern() == FileStructureColumn.PROPERTY_NAME_USER_ID)
         {
              key = records_of_row[count].getColumnValue().toUpperCase();
          }
          else
          {
              p_info= new PublicInfoItem(temp_property_name, records_of_row[count].getColumnValue());
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
                  
                  if ( row_container != null && !row_container.getPublicInfo().contains(p_info))
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
              else
              {
                  p_info= new PublicInfoItem(temp_property_name, records_of_row[count].getColumnValue());
                   if ( !row_sample.getPublicInfo().contains(p_info))
                   {
                      row_sample.addPublicInfo(p_info);
                   }
                  
               }
       }
     }
       if ( row_sample.getPosition() > 0 ) return row_sample;
       throw new Exception ("Sample position not defined ");
     
  }
  private  String    setFlexSequenceProperties(          ColumnValue[] records_of_row ) throws Exception
  {
      ImportFlexSequence row_sequence = null;
      ArrayList       flexsequence_additional_properties = new ArrayList();
       String sequence_id = null;
          String temp_object_type = null;String temp_property_name = null;
   
      for ( int count = 0; count < records_of_row.length; count++)
      {
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
                        flexsequence_additional_properties.add(new PublicInfoItem(temp_property_name, records_of_row[count].getColumnValue()));

              }
           }
          row_sequence.setPublicInfo( flexsequence_additional_properties);
          // if by some reason cds_start && cds_stop not set but sequence is OK set them
          if ( row_sequence.getCDSStop() != -1 && row_sequence.getCDSStart() != -1)
          {
              if (row_sequence.getSequenceText().length() / 3 == 0)
              {
                  row_sequence.setCDSStart(1);
                  row_sequence.setCDSStop(row_sequence.getSequenceText().length()  );
              }
              else
                  throw new Exception ("Wrong sequence text ");
          }

          if ( row_sequence.getCDSStart() - row_sequence.getCDSStop() / 3 != 0)
              throw new Exception ("Wrong sequence text ");
          if (sequence_id == null) 
              throw new Exception ("Cannot id sequence ");
          m_flex_sequences.put( sequence_id, row_sequence);
      }
      return sequence_id;
  }
}
