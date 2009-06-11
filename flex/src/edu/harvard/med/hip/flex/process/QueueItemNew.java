/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/* changes to FLEX to add unique constraint
 * 
 * ALTER TABLE QUEUE
 ADD CONSTRAINT Q1_UK UNIQUE 
  (PROJECTID
  ,WORKFLOWID
  ,PROTOCOLID
  ,SEQUENCEID
  ,CONSTRUCTID
  ,PLATESETID
  ,CONTAINERID) deferrable disable;

 

alter table queue
modify constraint Q1_UK enable novalidate;
 */
package edu.harvard.med.hip.flex.process;

import java.util.Date;
import java.io.Serializable;

 import org.hibernate.*;
import org.hibernate.cfg.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
/**
 *
 * @author htaycher
 */
public class QueueItemNew implements Serializable
{
    private Date m_submission_date = null;
    private String container_label=null;
    private Integer m_projectid = -1;
    private Integer m_workflowid = -1;
    private Integer m_protocolid = -1;
    private Integer m_sequenceid = null;
    private Integer m_constructid = null;
    private Integer m_containerid = null;
    private Integer m_platesetid = null;
    
    public  Integer getProjectid  (){ return m_projectid ;}
    public  Integer getWorkflowid  (){ return m_workflowid ;}
    public  Integer getProtocolid  (){ return m_protocolid ;}
    public  Integer getSequenceid  (){ return m_sequenceid ;}
    public  Integer getConstructid  (){ return m_constructid ;}
    public  Integer getContainerid  (){ return m_containerid ;}
    public  Integer getPlatesetid  (){ return m_platesetid ;}
    public  Date    getDateadded  (){ return m_submission_date ;}
    public String   getContainerLabel(){ return container_label;}
   
       public  void setDateadded  (Date v){   m_submission_date = v;}
     public void   setContainerLabel(String v){ container_label = v;}
   public  void setProjectid  (Integer v){   m_projectid = v;}
public  void setWorkflowid  (Integer v){   m_workflowid = v;}
public  void setProtocolid  (Integer v){   m_protocolid = v;}
public  void setSequenceid  (Integer v){   m_sequenceid = v;}
public  void setConstructid  (Integer v){   m_constructid = v;}
public  void setContainerid  (Integer v){   m_containerid = v;}
public  void setPlatesetid  (Integer v){   m_platesetid = v;}
   
     
      public boolean equals(Object other) {
        if ( !(other instanceof QueueItemNew) ) return false;
        QueueItemNew  castOther = (QueueItemNew ) other;
        return (castOther.getProjectid() == this.getProjectid()
                && castOther.getConstructid() == this.getConstructid()
                && castOther.getContainerid() == this.getContainerid()
                 && castOther.getPlatesetid() == this.getPlatesetid()
                && castOther.getProtocolid() == this.getProtocolid()
                 && castOther.getSequenceid() == this.getSequenceid()
                  && castOther.getWorkflowid() == this.getWorkflowid()
                );
    }

    public int hashCode() {
       return m_projectid +  m_workflowid *100 + 
      m_protocolid *10000+ m_sequenceid * 1000000
   + m_constructid + m_containerid * 100 + m_platesetid * 10000; 
    } 
    
    //---------------------------------------
     
    public void insert( ) throws Exception
     {
         Session session = HibernateSessionFactory.getSessionFactoryFLEX().getCurrentSession();
        //  System.out.println("Cannot get session");
          session.beginTransaction();
        //  System.out.println("Cannot begin");
            session.save(this);
        //    System.out.println("Cannot save");
            session.getTransaction().commit();
       //     System.out.println("Cannot commit");
        
     }
    
    public String toString()
    {
        return ""+  m_projectid +" "+  m_workflowid +" "+  m_protocolid +" "+ m_sequenceid 
  +" "+ m_constructid +" "+  m_containerid +" "+m_platesetid +" "+ m_submission_date;
    }
  
     public static void main(String[] args)
     {
         String container_labels = "YGS000363-2 FPL9 P1PL5 P1PL51 PL50  P1PL51 FPL24 FPL7 FPL8 FPL0061";
         container_labels=    "HMG000758 ";     
        
         java.util.ArrayList containers = Algorithms.splitString(container_labels.trim(), null);
         int project_id =1;
         int workflow_id = 68; 
         int processid=18;
         try
         {
             Session session = HibernateSessionFactory.getSessionFactoryFLEX().getCurrentSession();
        
       //      edu.harvard.med.hip.flex.action.AddItemsAction.putPlatesForSequencing( containers, 
       //     "sequencing_facility_name");
          edu.harvard.med.hip.flex.action.AddItemsAction.putPlatesInProcessingPipeline(
                   containers,  project_id , workflow_id, processid);
         } catch(Exception e)
          {
              System.out.println(e.getMessage());
          }
         System.exit(0);
        
         try
         {
                  QueueItemNew p = new QueueItemNew();
        p.setContainerid(124);
        p.setProjectid(1);
        p.setProtocolid(18);
        p.setWorkflowid(67);
        
        QueueItemNew p2 = new QueueItemNew();
        p2.setContainerid(124);
        p2.setProjectid(1);
        p2.setProtocolid(18);
        p2.setWorkflowid(67);
        
        Session session = HibernateSessionFactory.getSessionFactoryFLEX().getCurrentSession();
         p.insert();
    p2.insert();
      /* 
        Transaction tx = session.beginTransaction();
       session.save(p);
       session.save(p1);
       session.save(p2);
       session.flush();session.clear();
      tx.commit();
session.close();
*/
 session = HibernateSessionFactory.getSessionFactoryFLEX().getCurrentSession();
       session.beginTransaction();
       java.util.List<QueueItemNew> pp = session.createQuery("from QueueItemNew as item where item.containerid =?")//in(select containerid from containerheader where label in (?)) ")
                .setInteger(0,3)
                .list();
         
      
          for (QueueItemNew item: pp)
        {
            System.out.println(item.toString());
        }
          
          
          
          
         HibernateSessionFactory.getSessionFactoryFLEX().close();
      
         }catch(Exception e){
         System.out.println("++++++++++++++++++"+e.getMessage()+"++++++++++++++++++");}
    }
}
