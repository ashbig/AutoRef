/*
 * ActionExecution.java
 *
 * Created on April 3, 2003, 4:34 PM
 */

package edu.harvard.med.hip.bec.action;


import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.database.*;

import java.util.*;

import java.io.*;
/**
 *
 * @author  htaycher
 */
public class ActionExecution
{
    /*
     private static final String DILIM = "\t";
    private static final String FILE_PATH = "/tmp/";
    
    
    private static final String m_filePath = null;
 
    /*-	new request-	
        new process(es) records
         1.	new ER Container 
         2.	sample lineage
         3.	new processObject per ER container
         4.	new plateset record per master platenaming file for sequencing
     *
     *Master plate status – in ER
     * function create new ER containers , rearray file for each plate and naming file for each plate
     */
    
    /*
    public static ContainerCopyer createEndReadPlates(ArrayList master_containers, 
                    boolean forward, boolean reverse, int executionid , 
                    boolean write_rearrayfile , ArrayList files )
                    throws BecDatabaseException,IOException,BecUtilException
    {
        ContainerCopyer cp =new ContainerCopyer();
        cp.setExecutionId(executionid);
        ArrayList containers = new ArrayList();File fl = null;
        
        for (int ind = 0 ; ind < master_containers.size(); ind++)
        {
            Container master_container = (Container) master_containers.get(ind);
            String label = null;
            if ( reverse)
            {
             //   cp.setDestinationContainerWellNumber()  ;
                cp.setContainerType(Container.TYPE_ER_REVERSE_CONTAINER)  ;
                label = master_container.labelParsing()[0]+master_container.labelParsing()[1]+Container.SUF_ER_REVERSE_CONTAINER;
                containers.addAll( cp.doMapping( master_container, label, write_rearrayfile ));
                 //wrtie naming file
                 fl =  writeNamingFile((Container)cp.getNewContainers().get(0),".R00",  m_filePath);
                 if (fl != null) files.add(fl);
                 //write robot file
                 fl = RearrayFileEntry.createRearrayFile(cp.getRearrayFileEntries());
                 if (fl != null) files.add(fl);
             }
            if (forward )
            {
                cp.setContainerType(Container.TYPE_ER_FORWARD_CONTAINER)  ;
                label = master_container.labelParsing()[0]+master_container.labelParsing()[1]+Container.SUF_ER_FORWARD_CONTAINER;
                containers.addAll( cp.doMapping( master_container, label, write_rearrayfile ) );
                 //wrtie naming file
                 fl =  writeNamingFile((Container)cp.getNewContainers().get(0),".F00",  m_filePath);
                 if (fl != null) files.add(fl);
                 //write robot file
                 fl = RearrayFileEntry.createRearrayFile(cp.getRearrayFileEntries());
                 if (fl != null) files.add(fl);
             }
         }
        return cp;
    }
    
    
    //function writes naming files for sequencing machine in format
    // plateid_wellid_sequenceid_cloneid.F(R)00
    public static File writeNamingFile(Container container, String sufix, String m_filePath)
                        throws BecUtilException
    {
        File fl = null;
        String temp = null;
        FileWriter fr = null;
        Sample s  = null;
        if (m_filePath == null) m_filePath = FILE_PATH;
        try
        {
            for (int count = 0; count < container.getSamples().size(); count++)
            {
                s  = (Sample)container.getSamples().get(count);
                if (count == 0)//make shure that containeris not empty
                {
                    fl =   new File(m_filePath +container.getLabel()+ ".txt");
                    fr =  new FileWriter(fl);
                }
                fr.write(container.getId()+"_"+ s.getPosition() +"_"+ s.getRefSequenceId() +"_"+s.getCloneId()+"."+sufix+"\n");
            }
            fr.flush();
            fr.close();
            return fl;
        }
        catch(Exception e){ try { fr.close();}catch(Exception n){}; throw new BecUtilException("Cannot write naming file") ;}
        
    }
    
        */
}
