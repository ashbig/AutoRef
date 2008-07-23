/*
 * ThreadedExpressionSummaryTablePopulator.java
 *
 * Created on March 16, 2005, 10:08 AM
 */

package edu.harvard.med.hip.flex.util;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class ThreadedExpressionSummaryTablePopulator extends SummaryTablePopulator implements Runnable {
    private List m_storage_containers;
    private List m_sequencing_containers;
    private String m_vector_name;
    private String m_storage_form;
    private String m_storage_type;
    private String m_clone_type;
    int strategyid;
    
    /** Creates a new instance of ThreadedExpressionSummaryTablePopulator */
    public ThreadedExpressionSummaryTablePopulator(List containers, int strategyid) 
    {
        super();
        m_storage_containers = containers;
        this.strategyid = strategyid;
    }
    
    public ThreadedExpressionSummaryTablePopulator(List containers, 
            List seq_containers, String vector_name,
            String storage_form, String storage_type,
             String clone_type) 
    {
        this(containers, -1);
        m_sequencing_containers=seq_containers;
        m_vector_name = vector_name ;
        m_storage_form = storage_form; 
        m_storage_type = storage_type;
        m_clone_type = clone_type;
    }
    
   
    
    /** When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see     java.lang.Thread#run()
     *
     */
    public void run()
    {   
        //populates clones/clonestorage tables with new clones
        //updates sample table for main containers
        boolean result =  populateExpressionClonesWithContainers( m_storage_containers,
              m_vector_name,  m_storage_form,  m_storage_type,m_clone_type);
       //sequencing plates are not expected
        sendEmail(result, m_storage_containers);
         /*
        if(populateExpressionClonesWithContainers(containers, strategyid)) {
            sendEmail(true, containers);
        } else {
            sendEmail(false, containers);
        }
          * */
    }
    
    
      public static void main(String args[]) {
        //List containers = getContainers();
 Vector containers = new Vector();
  List containers1 = new ArrayList();
 try
 {
   edu.harvard.med.hip.flex.workflow.ProjectWorkflowProtocolInfo prf =
                   edu.harvard.med.hip.flex.workflow.ProjectWorkflowProtocolInfo.getInstance();
            
          edu.harvard.med.hip.flex.core.Container container = new edu.harvard.med.hip.flex.core.Container(24172);
            container.restoreSample();
            containers.add(container);
            edu.harvard.med.hip.flex.workflow.Project project = 
                    new edu.harvard.med.hip.flex.workflow.Project(1);
            edu.harvard.med.hip.flex.workflow.Workflow workflow =
                    new edu.harvard.med.hip.flex.workflow.Workflow(68);
            edu.harvard.med.hip.flex.process.Protocol protocol = 
                    new edu.harvard.med.hip.flex.process.Protocol(20);
            edu.harvard.med.hip.flex.process.ContainerMapper mapper = new edu.harvard.med.hip.flex.process.OneToOneNewContainerMapper();
            Vector newContainers_m = mapper.doMapping(containers, protocol, project, workflow);
            
            java.sql.Connection conn = edu.harvard.med.hip.flex.database.DatabaseTransaction.getInstance().requestConnection();
       
           
            for(int i=0; i<newContainers_m.size(); i++) {
                edu.harvard.med.hip.flex.core.Container newContainer = (edu.harvard.med.hip.flex.core.Container)newContainers_m.elementAt(i);
                newContainer.setLocation(new edu.harvard.med.hip.flex.core.Location(edu.harvard.med.hip.flex.core.Location.CODE_FREEZER));
                newContainer.insert(conn);
                containers1.add(newContainer.getId());
            }
           conn.commit();
       
        // containers1.add(24172);
         String key = "-1"+edu.harvard.med.hip.flex.workflow.ProjectWorkflowProtocolInfo.PWP_SEPARATOR+
         "68"+edu.harvard.med.hip.flex.workflow.ProjectWorkflowProtocolInfo.PWP_SEPARATOR+
         "-1"+edu.harvard.med.hip.flex.workflow.ProjectWorkflowProtocolInfo.PWP_SEPARATOR +"VECTOR_NAME";
         String vector_name = edu.harvard.med.hip.flex.workflow.ProjectWorkflowProtocolInfo.getInstance().getPWPProperties().get(key);
         ThreadedExpressionSummaryTablePopulator populator = 
                           new ThreadedExpressionSummaryTablePopulator(
                           containers1,  null,vector_name,
                           edu.harvard.med.hip.flex.core.StorageForm.GLYCEROL, 
                           edu.harvard.med.hip.flex.core.StorageType.WORKING, 
                           
                           edu.harvard.med.hip.flex.core.CloneInfo.EXPRESSION_CLONE);

        
        populator.run();
        
      System.exit(0);
 }catch(Exception e)
 {
     System.out.print(e.getMessage());
 }
    }
}
