/*
 * Renamer.java
 *
 * Created on August 28, 2003, 4:33 PM
 */

package edu.harvard.med.hip.bec.util;
import java.io.*;
import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.ui_objects.*;

/**
 *
 * @author  HTaycher
 */
public class Renamer
{
    public Renamer()
    {}
    
    /** Creates a new instance of Renamer */
    public static void main(String args[])
    {
        String dir = null; String line = null;
        String dir_into = "E:\\HTaycher\\YR";
        Renamer r = new Renamer();
        try
        {
            //        moveReverseReads("11836\\39079 11846\\39087" , "C:\\bio\\plate_analysis\\clone_samples");
            //printFileNames("E:\\Sequences for BEC\\files_to_transfer");
            deleteFileDirectories("6815","C:\\bio\\plate_analysis\\clone_samples");
            //printFileNames("E:\\Sequences for BEC\\Breast Cancer\\BSG001050_pDIDDY_rerun");
            //printFileNames("E:\\Sequences for BEC\\Breast Cancer\\BSG001050_T7_rerun");
            ///printFileNames("E:\\Sequences for BEC\\Yersinia pestis\\SAE000866_M13F");
            //printFileNames("E:\\Sequences for BEC\\Yersinia pestis\\SAE000866_M13R");
            
        }
        
        catch(Exception e)
        {}
        System.exit(0);
    }
    
    
    public static void deleteFileDirectories(String cloneids, String root)
    {
        ArrayList clone_ids = Algorithms.splitString(cloneids);
        int flexrefseq = -1;File directory_to_delete = null;
        String directory_name = null;
        for(int clone_count = 0; clone_count < clone_ids.size(); clone_count++)
        {
            try
            {
                flexrefseq = getFlexRefSequenceId( (String)clone_ids.get(clone_count));
                directory_name = root + File.separator + flexrefseq + File.separator + (String)clone_ids.get(clone_count);
                directory_to_delete = new File(directory_name);
          ArrayList files = new ArrayList();
                if (directory_to_delete.exists() && directory_to_delete.isDirectory() )
                {
                    deleteFiles( directory_to_delete );
                }
             directory_to_delete.delete();
            }
            catch(Exception e)
            {}
            
        }
    }
    
    private static void     deleteFiles(File sourceDir )
    {
              
              File [] sourceFiles = sourceDir.listFiles();
         //  files.add( sourceDir );
        // File [] sourceFiles = sourceDir.listFiles();
            for (int i = 0 ; i< sourceFiles.length;i++)
            {
                
                if ( sourceFiles[i].isDirectory() )             deleteFiles(sourceFiles[i]);
                System.out.println(sourceFiles[i]);
                 sourceFiles[i].delete();
                             
            }
           
     
           
    }
    private static int getFlexRefSequenceId(String cloneid)throws Exception
    {
        String sql="select flexsequenceid from flexinfo where flexcloneid="+cloneid;
        int result = -1; ResultSet rs = null;
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            if(rs.next())
            {
                result = rs.getInt("flexsequenceid");
            }
            return result;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());throw new Exception();
        }
    }
    
    public static void printFileNames(String dir_into )
    {
        System.out.println("\n\n"+dir_into);
        try
        {
            File sourceDir = new File(dir_into); //trace file directory
            
            File [] sourceFiles = sourceDir.listFiles();
            for (int i = 0 ; i< sourceFiles.length;i++)
            {
                System.out.println( sourceFiles[i].getName());
            }
        }
        
        catch(Exception e)
        {}
    }
    
    
    public static void moveReverseReads(String clone_info , String root_base)
    {
        ArrayList info = Algorithms.splitString(clone_info, " ");
        String dir_name = null;
        File [] sourceFiles = null;File sourceDir =  null;
        try
        {
            for (int index = 0; index < info.size();index++)
            {
                dir_name = root_base + File.separator + (String)info.get(index);
                sourceDir = new File(dir_name + File.separator +  "chromat_dir"); //trace file directory
                
                sourceFiles = sourceDir.listFiles();
                for (int i = 0 ; i< sourceFiles.length;i++)
                {
                    if ( sourceFiles[i].getName().indexOf("_R0." ) != -1)
                    {
                        FileOperations.moveFile(sourceFiles[i], new File( dir_name + File.separator + sourceFiles[i].getName()), false, true) ;
                    }
                }
                sourceDir = new File(dir_name + File.separator +  "phd_dir"); //trace file directory
                sourceFiles = sourceDir.listFiles();
                for (int i = 0 ; i< sourceFiles.length;i++)
                {
                    sourceFiles[i].delete();
                }
            }
        }
        
        catch(Exception e)
        {}
    }
    public static void renamePseudomonas()
    {
        String dir = "F:\\pseudomonas_dump\\trace_dump";
        File sourceDir = new File(dir); //trace file directory
        
        File [] sourceFiles = sourceDir.listFiles();
        File cur_file = null;
        String dir_name = null;
        File destFile = null;//new File(String new_name);
        // source.renameTo(destFile);
        
        for (int count = 0; count <sourceFiles.length;count++)
        {
            try
            {
                cur_file = sourceFiles[count];
                //dir_name = sourceFiles[count].getPath();
                String filename = cur_file.getAbsolutePath();
                int start = -1;
                //remove primer name
                //3596-H12-14714-35375_SEQL-BR2.ab1 3596-H12-14714-35375_Gateway_FarF1F2.ab1
                if (filename.indexOf("SEQL-B") != -1)
                {
                    start = filename.indexOf("SEQL-B");
                    filename = filename.substring(0, start ) + filename.substring(start + "SEQL-B".length() );
                }
                else if (filename.indexOf("Gateway_FarF1") != -1)
                {
                    start = filename.indexOf("Gateway_FarF1");
                    filename = filename.substring(0, start ) + filename.substring(start + "Gateway_FarF1".length());
                }
                //replace - by _
                filename = Algorithms.replaceChar(filename,'-','_');
                //replace number by 0
                start = filename.indexOf(".ab") - 1;
                filename = filename.substring(0, start ) +"0"+ filename.substring(start +1);
                destFile = new File(filename);
                cur_file.renameTo(destFile);
            }
            catch(Exception e)
            {System.out.println(e.getMessage());}
            
        }
        
    }
    
    
    public  void renamePseudomonas1(String dir , String direction)throws Exception
    {
        File sourceDir = new File(dir); //trace file directory
        ArrayList name_items= new ArrayList();
        File dest_file = null;
        int index = -1;
        File [] sourceFiles = sourceDir.listFiles();
        for (int i = 0 ; i< sourceFiles.length;i++)
        {
            if ( sourceFiles[i].isFile() && sourceFiles[i].getName().indexOf(".ab1") != -1 )
            {
                
                String file_name = sourceFiles[i].getName();
                //platelabel_a01_r.ab1
                index = file_name.indexOf("0.ab1");
                //file_name = file_name.substring(0,index)+"0.ab1";
                name_items = Algorithms.splitString(file_name, "_");
                CloneItem item = getItem( (String) name_items.get(0), (String)name_items.get(1));
                file_name =  item.getPlateId() + "_"+name_items.get(1) +"_" + item.getSequenceId()+"_"+item.getCloneId()+"_"+ direction +"0.ab1";
                dest_file = new File("c:\\bio\\phred\\try\\"+file_name);
                System.out.println( sourceFiles[i].getName()+" "+dest_file  );
                try
                {
                    FileOperations.copyFile(sourceFiles[i], dest_file, false);
                }catch(Exception e)
                {}
            }
        }
        
    }
    
    //D178P103RA1.T0
    public  void renamePseudomonas3(String dir , String org_dir,  String org_platename, String tr_pale_name)throws Exception
    {
        File sourceDir = new File(dir); //trace file directory
        ArrayList name_items= new ArrayList();
        File dest_file = null;
        String well_name = null;
        String direction = null;
        File [] sourceFiles = sourceDir.listFiles();
        for (int i = 0 ; i< sourceFiles.length;i++)
        {
            if ( sourceFiles[i].isFile()  )
            {
                
                String file_name = sourceFiles[i].getName();
                //platelabel_a01_r.ab1
                direction = file_name.substring( tr_pale_name.length(),  tr_pale_name.length()+1);
                well_name = file_name.substring( tr_pale_name.length()+1 ,file_name.indexOf("."));
                well_name = Algorithms.convertWellFromInttoA8_12( Algorithms.convertWellFromA8_12toInt(well_name));
                CloneItem item = getItem( org_platename,well_name);
                file_name =  item.getPlateId() + "_"+well_name +"_" + item.getSequenceId()+"_"+item.getCloneId()+"_"+ direction + "0"+file_name.substring( file_name.indexOf("."), file_name.length());
                dest_file = new File(org_dir+File.separator+"rename"+File.separator+file_name);
                System.out.println( sourceFiles[i].getName()+" "+dest_file  );
                try
                {
                    FileOperations.copyFile(sourceFiles[i], dest_file, false);
                }catch(Exception e)
                {}
            }
        }
        
    }
    public  void renamePseudomonas2(String dir )throws Exception
    {
        File sourceDir = new File(dir); //trace file directory
        ArrayList name_items= new ArrayList();
        File dest_file = null;
        int index = -1;String direction=null;
        File [] sourceFiles = sourceDir.listFiles();
        for (int i = 0 ; i< sourceFiles.length;i++)
        {
            if ( sourceFiles[i].isFile() && sourceFiles[i].getName().indexOf(".ab1") != -1 )
            {
                
                String file_name = sourceFiles[i].getName();
                if (file_name.indexOf("_F.ab1") != -1)
                    direction = "F";
                else if (file_name.indexOf("_RF.ab1") != -1)
                    direction = "R";
                else
                    continue;
                //platelabel_a01_r.ab1
                index = file_name.indexOf("0.ab1");
                //file_name = file_name.substring(0,index)+"0.ab1";
                name_items = Algorithms.splitString(file_name, "_");
                CloneItem item = getItem( (String) name_items.get(0), (String)name_items.get(1));
                file_name =  item.getPlateId() + "_"+name_items.get(1) +"_" + item.getSequenceId()+"_"+item.getCloneId()+"_"+ direction +"0.ab1";
                dest_file = new File("c:\\bio\\phred\\try\\"+file_name);
                System.out.println( sourceFiles[i].getName()+" "+dest_file  );
                try
                {
                    FileOperations.copyFile(sourceFiles[i], dest_file, false);
                }catch(Exception e)
                {}
            }
        }
        
    }
    
    public  void renameYeast(String dir)
    {
        File sourceDir = new File(dir); //trace file directory
        
        File [] sourceFiles = sourceDir.listFiles();
        File cur_file = null;
        String dir_name = null;
        File destFile = null;//new File(String new_name);
        Connection conn = null;
        // source.renameTo(destFile);
        ArrayList name_items = null;
        for (int count = 0; count <sourceFiles.length;count++)
        {
            try
            {
                cur_file = sourceFiles[count];
                //dir_name = sourceFiles[count].getPath();
                String filename = cur_file.getAbsolutePath();
                name_items = Algorithms.splitString(filename, "_");
                if (name_items.size() != 7) continue;
                CloneItem item = getItem( (String) name_items.get(1), (String)name_items.get(3), conn);
                filename = item.getPlateId() + "_"+name_items.get(1) +"_" + name_items.get(3)+"_"+item.getCloneId()+"_R0.ab1";
                destFile = new File(dir+File.separator+filename);
                cur_file.renameTo(destFile);
            }
            catch(Exception e)
            {System.out.println(e.getMessage());}
            
        }
        
        
    }
    
    
    //4528_A01_JLDM_7201_A01_2254_114436_R0_3_001.ab1
    
    //4547_A01_JLDM_7205_A01_869_114792_F05_001.ab1
    public  void renameYeast1(String dir, String dir_into)
    {
        try
        {
            File sourceDir = new File(dir); //trace file directory
            
            File [] sourceFiles = sourceDir.listFiles();
            File cur_file = null;
            String dir_name = null;
            File destFile = null;//new File(String new_name);
            Connection conn = null;
            String type = null;
            // source.renameTo(destFile);
            ArrayList name_items = null;
            if ( sourceFiles == null ) return;
            for (int count = 0; count <sourceFiles.length;count++)
            {
                
                cur_file = sourceFiles[count];
                //dir_name = sourceFiles[count].getPath();
                String filename = cur_file.getName();
                name_items = Algorithms.splitString(filename, "_");
                if (name_items.size() != 9) continue;
                else
                {
                    if ( ((String)name_items.get(7)).indexOf("F") != -1)
                        type = "F0";
                    else if (  ((String)name_items.get(7)).indexOf("R") != -1)
                        type = "R0";
                    else continue;
                }
                filename = name_items.get(3) +"_" + name_items.get(4)+"_"
                +name_items.get(5)+"_"+name_items.get(6)+"_"+type +".ab1";
                destFile = new File(dir_into+File.separator+filename);
                cur_file.renameTo(destFile);
                
                
                
            }
        }
        catch(Exception e)
        {System.out.println(e.getMessage());}
        
    }
    
    
    
    
    private  CloneItem  getItem(String wellid, String sequenceid, Connection conn)throws Exception
    {
        String sql ="select flexsequenceid, position,flexcloneid as cloneid, flexsequencingplateid as plateid  from flexinfo f, isolatetracking i, sample s"
        +" where flexsequenceid ="+sequenceid+" and position = "+Algorithms.convertWellFromA8_12toInt(wellid)+" and s.sampleid=i.sampleid and i.isolatetrackingid=f.isolatetrackingid ";
        CloneItem item = null;
        ResultSet rs = null;
        ArrayList items = new ArrayList();
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while(rs.next())
            {
                item = new CloneItem();
                item.setCloneId( rs.getInt("cloneid"));
                item.setPlateId(rs.getInt("plateid"));
                items.add(item);
            }
            if (items.size()  > 1)
            {System.out.println( sequenceid); throw new Exception();}
            return item;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());throw new Exception();
        }
    }
    
    
    
    private  CloneItem  getItem(String platelabel, String wellid)throws Exception
    {
        int wellid_num = Algorithms.convertWellFromA8_12toInt(wellid);
        String sql ="select flexsequenceid, flexcloneid as cloneid, flexsequencingplateid as plateid  from flexinfo f, isolatetracking i, sample s"
        +" where  s.containerid = (select containerid from containerheader where label='" + platelabel + "')"
        +" and position = "+Algorithms.convertWellFromA8_12toInt(wellid)+" and s.sampleid=i.sampleid and i.isolatetrackingid=f.isolatetrackingid ";
        CloneItem item = null;
        ResultSet rs = null;
        ArrayList items = new ArrayList();
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while(rs.next())
            {
                item = new CloneItem();
                item.setSequenceId( rs.getInt("flexsequenceid"));
                if (item.getSequenceId() == -1) item.setSequenceId(0);
                item.setCloneId( rs.getInt("cloneid"));
                item.setPlateId(rs.getInt("plateid"));
                item.setWellId(wellid_num);
                items.add(item);
            }
            return item;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());throw new Exception();
        }
    }
    
    class CloneItem
    {
        private int i_plateid = -1;
        private int i_wellid = -1;
        private int i_sequenceid = -1;
        private int i_cloneid = -1;
        
        public CloneItem(int p, int w, int c, int s)
        {
            i_plateid = p;
            i_wellid=w;
            i_sequenceid=s;
            i_cloneid=c;
        }
        public CloneItem()
        {}
        public int getPlateId()
        { return i_plateid ;}
        public int getWellId()
        { return i_wellid ;}
        public int getSequenceId()
        { return i_sequenceid ;}
        public int getCloneId()
        { return i_cloneid ;}
        
        public void setPlateId(int v)
        { i_plateid = v;}
        public void setWellId(int v)
        { i_wellid = v;}
        public void setSequenceId(int v)
        { i_sequenceid = v;}
        public void setCloneId(int v)
        { i_cloneid = v;}
        
    }
    
    
    
    private  void getType(String dir)
    {
        File sourceDir = new File(dir); //trace file directory
        File dest_file = null;
        int index = -1;
        File [] sourceFiles = sourceDir.listFiles();
        for (int i = 0 ; i< sourceFiles.length;i++)
        {
            if ( sourceFiles[i].isDirectory() )
            {
                //System.out.println( sourceFiles[i]  );
                getType( sourceFiles[i].getAbsolutePath() );
            }
            else if ( sourceFiles[i].isFile() && sourceFiles[i].getName().indexOf(".ab1") != -1 )
            {
                
                String file_name = sourceFiles[i].getName();
                index = file_name.indexOf("0.ab1");
                //file_name = file_name.substring(0,index)+"0.ab1";
                dest_file = new File("E:\\Sequences for BEC\\Agencourt data 112603_ab1\\2lm_renamed\\"+file_name);
                System.out.println( sourceFiles[i].getName()+" "+dest_file  );
                try
                {
                    FileOperations.copyFile(sourceFiles[i], dest_file, false);
                }catch(Exception e)
                {}
            }
        }
        
        
    }
    public  void renameYeast1(String dir)
    {
        getType(dir);
        
        System.out.print("A");
        /*
        File cur_file = null;
        String dir_name = null;
         File destFile = null;//new File(String new_name);
         Connection conn = null;
        // source.renameTo(destFile);
        ArrayList name_items = null;
        for (int count = 0; count <sourceFiles.length;count++)
        {
            try
            {
                cur_file = sourceFiles[count];
                //dir_name = sourceFiles[count].getPath();
                String filename = cur_file.getAbsolutePath();
                name_items = Algorithms.splitString(filename, "_");
                if (name_items.size() != 7) continue;
                CloneItem item = getItem( (String) name_items.get(1), (String)name_items.get(3), conn);
                filename = item.getPlateId() + "_"+name_items.get(1) +"_" + name_items.get(3)+"_"+item.getCloneId()+"_R0.ab1";
                destFile = new File(dir+File.separator+filename);
                cur_file.renameTo(destFile);
            }
            catch(Exception e){System.out.println(e.getMessage());}
         
        }
         
         */
    }
    
    
    public void renameYeast3(String dir)
    {
        //4458_a03_jlja_7192_a01_2886_113591_r0-3_017.ab1
        File sourceDir = new File(dir); //trace file directory
        File dest_file = null;
        String filename = null;ArrayList name_items = new ArrayList();
        File [] sourceFiles = sourceDir.listFiles();
        for (int i = 0 ; i< sourceFiles.length;i++)
        {
            name_items = Algorithms.splitString(sourceFiles[i].getAbsolutePath(), "_");
            
            filename =(String) name_items.get(4)+"_"+ (String)name_items.get(5)+"_"+  (String)name_items.get(6)+"_"+  (String)name_items.get(7)+"_";
            
            filename = filename.toUpperCase();
            if ( ((String) name_items.get(8)).startsWith("r") )
            {
                filename += "R0.ab1";
            }
            else  if ( ((String) name_items.get(8)).startsWith("f") )
            {
                filename += "F0.ab1";
            }
            dest_file = new File(dir+File.separator+filename);
            System.out.println( sourceFiles[i].getName()+" "+dest_file  );
            sourceFiles[i].renameTo(dest_file);
            
            
            
        }
        
    }
    
    
    
    public void renameYeast1b(String dir_source, String dir_out, String file_name)
    {
        //4458_a03_jlja_7192_a01_2886_113591_r0-3_017.ab1
        File dest_file = null; File org_file = null;
        String filename_org = null;String filename_dest= null;
        ArrayList file_names = null;String line = null;
        BufferedReader fin=null;
        try
        {
            fin = new BufferedReader(new FileReader(file_name));
            while ((line = fin.readLine()) != null)
                
            {
                file_names = Algorithms.splitString(line, "\t");
                filename_org = dir_source + File.separator + (String)file_names.get(1);
                filename_dest= dir_out + File.separator + (String)file_names.get(0);
                try
                {
                    dest_file = new File(filename_dest);
                    org_file= new File(filename_org);
                    System.out.println("trying "+ filename_org+" "+filename_dest  );
                    if (org_file.exists())
                    {
                        // org_file.renameTo(dest_file);
                        org_file.renameTo(dest_file);
                    }
                }
                catch(Exception e1)
                {
                    System.out.println( "error "+filename_org+" "+filename_dest  );
                }
            }
            fin.close();
        }
        catch(Exception e)
        {}
    }
    
    
    public void renameYeastJasonInternalReads(String dir_source, String dir_out, String file_name)
    {
        //4458_a03_jlja_7192_a01_2886_113591_r0-3_017.ab1
        File dest_file = null; File org_file = null;
        String filename_org = null;String filename_dest= null;
        ArrayList file_names = null;String line = null;
        BufferedReader fin=null; Hashtable read_names = new Hashtable();
        
        try
        {
            fin = new BufferedReader(new FileReader(file_name));
            while ((line = fin.readLine()) != null)
                
            {
                file_names = Algorithms.splitString(line, "_");
                read_names.put(file_names.get(1), line);
            }
            File sourceDir = new File(dir_source); //trace file directory
            File [] sourceFiles = sourceDir.listFiles();
            for (int i = 0 ; i< sourceFiles.length;i++)
            {
                String tracefile_name = sourceFiles[i].getName();
                file_names = Algorithms.splitString(tracefile_name, "_");
                
                dest_file = new File(dir_out+File.separator+ (String)read_names.get( file_names.get(1) ));
                org_file= new File(sourceFiles[i].getAbsolutePath());
                org_file.renameTo(dest_file);
            }
            fin.close();
        }
        catch(Exception e)
        {}
    }
    
    
    
    public void yeastCopyIntoNewClones(String fname_dir,String fname_info, String basename)
    {
        ArrayList clone_info = null;
        File dest_file= null;
        File sourceDir =null;
        String line=null;String filetype=null;
        
        Hashtable dir_info = new Hashtable();BufferedReader fin=null;
        UICloneSample clone_discr = null;
        
        try
        {
            fin = new BufferedReader(new FileReader(fname_dir));
            
            while ((line = fin.readLine()) != null)
            {
                clone_info = Algorithms.splitString(line, "\\");
                clone_discr = new UICloneSample();
                clone_discr.setTraceFilesDirectory(line.trim());
                System.out.println((String)clone_info.get(4)+" "+line.trim());
                dir_info.put((String)clone_info.get(4),clone_discr);
            }
            fin.close();
        }catch(Exception e)
        {}
        
        try
        {
            
            fin = new BufferedReader(new FileReader(fname_info));
            //Clone Id	Clone ID	Plate Label	Position	REF: FLEX Id
            //114536	133603	8643	YRG000855	69	11
            
            while ((line = fin.readLine()) != null)
            {
                clone_info = Algorithms.splitString(line, "\t");
                clone_discr = (UICloneSample) dir_info.get((String)clone_info.get(0));
                if ( clone_discr == null) continue;
                clone_discr.setCloneId( Integer.parseInt( (String) clone_info.get(1) ));
                clone_discr.setPosition( Integer.parseInt( (String) clone_info.get(4) ));
                clone_discr.setFLEXRefSequenceId( Integer.parseInt( (String) clone_info.get(5) ));
                clone_discr.setConstructId( Integer.parseInt( (String) clone_info.get(2) ));;//container id
            }
            fin.close();
        }
        catch(Exception e)
        {}
        
        for (Enumeration en = dir_info.elements() ; en.hasMoreElements() ;)
        {
            clone_discr = ( UICloneSample)en.nextElement();
            sourceDir = new File( clone_discr.getTraceFilesDirectory() );
            File [] sourceFiles = sourceDir.listFiles();
            //trace file directory
            for (int i = 0 ; i< sourceFiles.length;i++)
            {
                try
                {
                    filetype = sourceFiles[i].getName().substring(sourceFiles[i].getName().lastIndexOf('_'), sourceFiles[i].getName().length()-1);
                    dest_file = new File(basename+File.separator + clone_discr.getConstructId() +"_" +Algorithms.convertWellFromInttoA8_12(clone_discr.getPosition())+"_" +clone_discr.getFLEXRefSequenceId()+"_" +clone_discr.getCloneId() + "_" +filetype);
                    // FileOperations.copyFile(sourceFiles[i], dest_file, false);
                    System.out.println( sourceFiles[i].getName()+" "+dest_file.getName());
                }catch(Exception e)
                {}
            }
        }
        
        
    }
}
