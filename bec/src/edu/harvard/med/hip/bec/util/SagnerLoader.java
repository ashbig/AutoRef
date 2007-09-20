/*
 * SagnerLoader.java
 *
 * Created on September 18, 2007, 10:59 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.bec.util;

import edu.harvard.med.hip.bec.programs.parsers.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.programs.parsers.CloneCollectionElements.*;
import java.io.*;
import java.util.*;
/**
 *
 * @author htaycher
 */
public class SagnerLoader 
{
    
    /** Creates a new instance of SagnerLoader */
    public static void main(String args[])
    {
        String in_file_name ="Z:\\HTaycher\\HIP projects\\Sagner\\entry_info_31045.txt";
        String[] plate_indexes = {"_1","_2","_3","_4"}; 
            int species_id = 3;
            int userid = 2;
            int project_id = 6;
            int format = 0;
            int cloningstrategyid = 7;
            String plate_type = "96 WELL PLATE";
            try
            {
        writeXMLFiles( in_file_name,  plate_indexes, 
             species_id,  userid,  project_id,  format,  cloningstrategyid, plate_type) ;   
            }
            catch(Exception e){}
    }
    
    
    //Plate_Name	Plate_Position	RefSeq_AccNo	Gene_ID	Gene_Symbol	Gene_function	ReferenceSequence
    public static void writeXMLFiles(String in_file_name, String[] plate_indexes, 
            int species_id, int user_id, int project_id, int format, int cloningstrategyid,
            String plate_type)
             throws Exception
    {
        String ref_file_name = in_file_name+"_refseq.xml";
        String file_name =in_file_name+"_plates.xml";
        Hashtable refsequences = readSequences(in_file_name, species_id);
        ArrayList plates = readPlateInfo(in_file_name, plate_type, format, project_id,cloningstrategyid, refsequences);
        plates = duplicatePlates(plate_indexes, plates);
        writeRefSequenceFile( ref_file_name,  refsequences);
        writeCloneCollectionFile( file_name,  plates,  user_id );
    }
    
    private static void writeRefSequenceFile(String file_name, Hashtable ref_seqs) throws Exception
    {
        RefSequenceParser.writeRefSequenceFileHeader(   file_name);
        Iterator iter = ref_seqs.values().iterator();
        while(iter.hasNext())
        {
           RefSequenceParser.writeRefSequence( (RefSequence)iter.next(),  file_name);
        }
        RefSequenceParser.writeRefSequenceFileFooter(   file_name);
    }
    
    
     private static void writeCloneCollectionFile(String file_name, ArrayList collections,
             int   user_id )throws Exception
    {
          CloneCollectionParser.writeCollectionFileHeader( file_name);
         CloneCollectionParser.writeCollectionFile( collections,  file_name,
                 user_id );
          CloneCollectionParser.writeCollectionFileFooter( file_name);
     }
     
  //Plate_Name	Plate_Position	RefSeq_AccNo	Gene_ID	Gene_Symbol	Gene_function	ReferenceSequence
  //    0           1               2               3           4           5               6
    private static  Hashtable  readSequences(String in_file_name, int species_id)throws Exception
    {
        Hashtable refsequences = new Hashtable();
        String[] items = null;
        RefSequence refsequence = null;
        PublicInfoItem p_info = null;
        String line = null;
        BufferedReader in  =   new BufferedReader(new InputStreamReader(new FileInputStream(in_file_name)));
        while((line = in.readLine()) != null)
        {
            items = line.split("\t");//edu.harvard.med.hip.flex.util.Algorithms.splitString(line,"\t",true, -1);
            refsequence= new RefSequence();
            refsequence.setCdsStart(1);
            refsequence.setText(items[6]);
            refsequence.setCdsStop(refsequence.getText().length());
            refsequence.setSpecies(species_id);
            refsequence.setId(BecIDGenerator.getID("userrefseqid"));
            
            p_info = new PublicInfoItem( "GENBANK_ACCESSION", items[2], null, null);
            refsequence.addPublicInfo(p_info);
            p_info = new PublicInfoItem( "GENE_ID", items[3], null, null);
            refsequence.addPublicInfo(p_info);
            p_info = new PublicInfoItem( "GENE_SYMBOL", items[4], null, null);
            refsequence.addPublicInfo(p_info);
            p_info = new PublicInfoItem( "FUNCTION", items[5], null, null);
            refsequence.addPublicInfo(p_info);
            refsequences.put( items[2],refsequence);
        }
        return refsequences;
    }

    private static ArrayList        readPlateInfo(
            String in_file_name, String plate_type,
            int format,  int   project_id,
            int cloningstrategyid, Hashtable refsequences)throws Exception
    {
        CloneCollection plate = null;
        ConstructForCloneCollection construct = null;
        ArrayList plates = new ArrayList();
        ArrayList tmp_plates = new ArrayList();
        String cur_plate = null;
         String line = null;
         String[] items = null;
          SampleForCloneCollection sample = null ;
         RefSequence refsequence = null;
        BufferedReader in  =   new BufferedReader(new InputStreamReader(new FileInputStream(in_file_name)));
        while((line = in.readLine()) != null)
        {
            //Plate_Name	Plate_Position	RefSeq_AccNo
            items = line.split("\t");
            if ( !items[0].equals(cur_plate))
            {
                plate = new CloneCollection();
                cur_plate= items[0];
                plates.add(plate);
                plate.setName(items[0]);
               // plate.setId(BecIDGenerator.getID("userplateid"));
                plate.setType(plate_type);
                plate.setProjectId(project_id);
            }
            construct = new ConstructForCloneCollection();
            //construct.setId(BecIDGenerator.getID("userconstructid"));
            construct.setFormat(format);
            construct.setCloningStrategyId(cloningstrategyid);
            refsequence = (RefSequence) refsequences.get(items[2]);
            construct.setRefSequenceId(refsequence.getId());
            sample = new SampleForCloneCollection();
            sample.setType("ISOLATE");
            sample.setWellName( items[1]);
            //sample.setId();
            construct.addSample(sample);
            plate.addConstruct(construct);
            
            
        }
        return plates;
        
    }
    
    
    private static ArrayList   duplicatePlates(String[] in_file_name, ArrayList old_plates)throws Exception
    {
           SampleForCloneCollection sample = null ;  SampleForCloneCollection org_sample = null ;
         ConstructForCloneCollection construct = null;
         ConstructForCloneCollection org_construct = null;
      
        ArrayList plates = new ArrayList();
        CloneCollection plate = null;
        CloneCollection org_plate = null;
        for (int c_old_plates = 0; c_old_plates < old_plates.size(); c_old_plates++)
        {
            org_plate = (CloneCollection)old_plates.get(c_old_plates);
      
            for (int c_plates = 0; c_plates < in_file_name.length; c_plates++ )
            {
                plate = new CloneCollection();
                plates.add(plate);
                plate.setName(org_plate.getName()+in_file_name[c_plates]);
                plate.setId(BecIDGenerator.getID("userplateid"));
                plate.setType( org_plate.getType() );
                plate.setProjectId(org_plate.getProjectId());
                for (int c_sample = 0; c_sample < org_plate.getSamples().size(); c_sample++)
                {
                   org_sample = (SampleForCloneCollection)org_plate.getSamples().get(c_sample);
                    sample = new SampleForCloneCollection();
                    sample.setType(org_sample.getType());
                    sample.setWellName(org_sample.getWellName());
                   sample.setId(BecIDGenerator.getID("usersampleid"));
                   plate.addSample(sample);
                }
               for (int c_construct = 0; c_construct < org_plate.getConstructs().size(); c_construct++)
               {
                   org_construct =(ConstructForCloneCollection)org_plate.getConstructs().get(c_construct);
                    construct = new ConstructForCloneCollection();
                    construct.setId(BecIDGenerator.getID("userconstructid"));
                    construct.setFormat(org_construct.getFormat());
                    construct.setCloningStrategyId(org_construct.getCloningStrategyId());
                    construct.setRefSequenceId(org_construct.getRefSequenceId());
                    plate.addConstruct(construct);
                   for (int c_sample_construct = 0; c_sample_construct < org_construct.getSamples().size(); c_sample_construct++)
                   {
                        org_sample = (SampleForCloneCollection)org_construct.getSamples().get(c_sample_construct);
                        sample = new SampleForCloneCollection();
                        sample.setType(org_sample.getType());
                        sample.setWellName(org_sample.getWellName());
                        sample.setId(BecIDGenerator.getID("usersampleid"));
                        sample.setCloneId(BecIDGenerator.getID("usercloneid"));
                        construct.addSample(sample);
                   }
              
               }
            }
        }
        return plates;
    }
}
