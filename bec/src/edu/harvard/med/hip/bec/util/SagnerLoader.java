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

import java.sql.*;
import java.util.*;
import sun.jdbc.rowset.*;
import javax.sql.*;

import  edu.harvard.med.hip.bec.bioutil.*;
import  edu.harvard.med.hip.bec.coreobjects.sequence.*;

import edu.harvard.med.hip.bec.database.*;
/**
 *
 * @author htaycher
 */
public class SagnerLoader 
{
    
    /** Creates a new instance of SagnerLoader */
    public static void main(String args[])
    {
        String rename_all_bat = "O:\\plate_mapping\\rename_all";
        String destination = "d:\\traces_uploaded\\";
        
        String db_name="O:\\blast_db\\ORFEOME\\genes";
        creatBlastableDB( db_name);
        
      /*   try
        {
            BufferedWriter out  =   new BufferedWriter(new FileWriter("O:\\plate_mapping\\totalfiles.txt" ));
            File dir_file = new File("o:\\traces_uploaded");
            String[] traces_name = dir_file.list();
            for(int count = 0; count < traces_name.length; count++)
            {
                out.write(traces_name[count]+"\n");
                out.flush();
            }
            out.close();
        }
        catch(Exception e){}
      */
        
      //+writeTracesNames(rename_all_bat+"11001.bat","O:\\temp_traces\\11001", "pDONR223","M13R");
       //+writeTracesNames(rename_all_bat+"004.bat","O:\\temp_traces\\sanger004", "d:\\temp_traces\\sanger004\\", destination,"pDONR223","M13R");
      //  +writeTracesNames(rename_all_bat+"31045.bat","O:\\temp_traces\\31045", "pDONR223","M13R");
      //   writeTracesNames(rename_all_bat+"003.bat","O:\\temp_traces\\sanger003",  "d:\\temp_traces\\sanger003\\", destination,"pDONR223","M13R");
     //writeTracesNames(rename_all_bat+"002.bat","O:\\temp_traces\\sanger002",  "d:\\temp_traces\\sanger002\\", destination,"pDONR223","M13R");
      

// writeTracesNames(rename_all_bat+"001.bat","O:\\temp_traces\\sanger001",  "d:\\temp_traces\\sanger001\\", destination,"pDONR223","M13R");
     
  //writeTracesNames(rename_all_bat+"11002.bat","O:\\temp_traces\\11002", "d:\\temp_traces\\11002\\", destination,"pDONR223","M13R");
  //    writeTracesNames(rename_all_bat+"11003.bat","O:\\temp_traces\\11003", "d:\\temp_traces\\11003\\", destination,"pDONR223","M13R");
        
      /*  String in_file_name ="Z:\\HTaycher\\HIP projects\\Sagner\\10000-1200.txt";
        String[] plate_indexes = {"_1","_2","_3","_4"}; 
            int species_id = 2;
            int userid = 1;
            int project_id = 2;
            int format = 0;
            int cloningstrategyid = 2;
            String plate_type = "96 WELL PLATE";
            try
            {
        writeXMLFiles( in_file_name,  plate_indexes, 
             species_id,  userid,  project_id,  format,  cloningstrategyid, plate_type) ;   
            }
            catch(Exception e)
            {
            System.out.println(e.getMessage());
            }
       */
            
    }
    
    
    
    public static void writeTracesNames(String rename_all_bat,String dir, String origin,
            String destination,String forward_primer, String rev_primer)
    {
        String line = null;
        String file_names = dir+"\\renaming_traces_1"+System.currentTimeMillis()+".bat";
        try
        {
            BufferedWriter out  =   new BufferedWriter(new FileWriter(rename_all_bat ));
           File dir_file = new File(dir);
            String[] traces_name = dir_file.list();
            int rec_count = 0; int count ;
            for(count = 0; count < traces_name.length; count++)
            {
                line = traces_name[count];
        //     System.out.println(line);
                //31044_3_E9.p1kpDONR223_1
              //  int index_second_ = line.indexOf('_');
                line=line.replaceFirst("\\.","_");
    //     System.out.println(line);
                String[] items= line.split("_");
                if ( items[2].length()==2) 
                    items[2]=items[2].charAt(0)+"0"+items[2].charAt(1);
              
                //31045_1_D1.q1kaM13R.scf 31045_1_D1.q1kM13R.scf
                //31045_1_D1.p1kapDONR223_1.scf
                int forw_index = items[3].indexOf(forward_primer);
                int index_rev = items[3].indexOf(rev_primer);
                if( forw_index ==3) 
                    items[3]= items[3].replaceFirst(forward_primer,"_F");
                else if ( index_rev ==3 )
                {
                   items[3]= items[3].replaceFirst(rev_primer,"_R");
                }
                else
                {
                   
                    if (items.length == 5 )
                    {
                         items[3]="I_"+count;
                    }
                    else
                         items[3]="I_"+count+".scf";
                }
                line = Algorithms.convertArrayToString(items,"_");
                out.write("cp "+origin+traces_name[count]+" "+ destination + line+"\n");
                rec_count++;
                out.flush();
            }
           System.out.println( dir+" "+ count +" "+rec_count);
            out.close();
        }
        catch(Exception e){}
      
        
    }
    //Plate_Name	Plate_Position	RefSeq_AccNo	Gene_ID	Gene_Symbol	Gene_function	ReferenceSequence
    public static void writeXMLFiles(String in_file_name, String[] plate_indexes, 
            int species_id, int user_id, int project_id, int format, int cloningstrategyid,
            String plate_type)
             throws Exception
    {
        long timestamp=System.currentTimeMillis();
        String ref_file_name = in_file_name+"_refseq"+timestamp+".xml";
        String file_name =in_file_name+"_plates"+timestamp+".xml";
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
   
            items = line.trim().split("\t");//edu.harvard.med.hip.flex.util.Algorithms.splitString(line,"\t",true, -1);
    System.out.println(line);      
            refsequence= new RefSequence();
            refsequence.setCdsStart(1);
           
            // cut last base and replace it to 'c'
            String sequence_text = items[6];
   
            sequence_text = sequence_text.substring(0, sequence_text.length()-1)+"C";
             refsequence.setText(sequence_text);
            refsequence.setCdsStop(refsequence.getText().length());
            refsequence.setSpecies(species_id);
            refsequence.setId(BecIDGenerator.getID("userrefseqid"));
            
            if( items[2].trim().length()>0)
            {p_info = new PublicInfoItem( "GENBANK_ACCESSION", items[2], null, null);
            refsequence.addPublicInfo(p_info);}
            if( items[3].trim().length()>0)
            {p_info = new PublicInfoItem( "GENE_ID", items[3], null, null);
            refsequence.addPublicInfo(p_info);}
           if( items[4].trim().length()>0)
            { p_info = new PublicInfoItem( "GENE_SYMBOL", items[4], null, null);
            refsequence.addPublicInfo(p_info);}
           if( items[5].trim().length()>0)
            { p_info = new PublicInfoItem( "FUNCTION", items[5], null, null);
            refsequence.addPublicInfo(p_info);}
            refsequences.put( items[2],refsequence);
           
            
        }
        return refsequences;
    }

    
   public static void creatBlastableDB(String db_name)
   {
        int refseq_id  = -1; String refseq_cds = null;BufferedWriter out  = null;
        RefSequence refseq = null; int add_number = 200;int flex_refseq_id = 0;
        int start_count = 0;
         try
         {
             
            while( start_count < 30921)
            {
                 String sql = "select distinct flexsequenceid, refsequenceid from sequencingconstruct s, "
+ " flexinfo f, isolatetracking i where s.constructid = i.isolatetrackingid and i.isolatetrackingid=f.isolatetrackingid "
                +" and f.flexsequenceid > "+start_count+" and flexsequenceid < " + (start_count+add_number) +" order by flexsequenceid";
 
                if ( start_count == 0)
                     out  =   new BufferedWriter(new FileWriter(db_name ));
                else 
                     out  =   new BufferedWriter(new FileWriter(db_name, true ));
start_count +=add_number-1;
 
                DatabaseTransaction t = DatabaseTransaction.getInstance();
                RowSet rs = t.executeQuery(sql);
                 while(rs.next())
                {
                     flex_refseq_id  = rs.getInt("flexsequenceid");
                     refseq_id  = rs.getInt("refsequenceid");
                     
                      refseq = new RefSequence(refseq_id, false);
                        refseq_cds = refseq.getCodingSequence();
                        refseq_cds = SequenceManipulation.convertToFasta(refseq_cds);
                        out.write(">"+flex_refseq_id);
                        out.write(refseq_cds);
                        out.write(System.getProperty("line.separator"));
                        out.flush();
                    
                }
               
                out.close();
            }
         }
         catch (Exception e)
         {
             System.out.println(e.getMessage());
         }
       
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
