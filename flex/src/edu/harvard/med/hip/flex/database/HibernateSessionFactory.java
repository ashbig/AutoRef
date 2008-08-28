/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.database;

    import org.hibernate.*;
import org.hibernate.cfg.*;
/**
 *
 * @author htaycher
 */
public class HibernateSessionFactory {

    private static String CONFIG_FILE_LOCATION = "config/";
    private static final SessionFactory sessionFactory_flex;
   // private static final SessionFactory sessionFactory_plasmid;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
         
            sessionFactory_flex = new Configuration().configure(CONFIG_FILE_LOCATION+"hibernate_flex.cfg.xml").buildSessionFactory();
        //    sessionFactory_plasmid = new Configuration().configure(CONFIG_FILE_LOCATION+"hibernate_plasmid.cfg.xml").buildSessionFactory();
  
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactoryFLEX() {
        return sessionFactory_flex;
    }
    
  //  public static SessionFactory getSessionFactoryPLASMID() {
  //      return sessionFactory_plasmid;
  //  }
   public static void main(String[] args)
     {
        
        Session session = HibernateSessionFactory.getSessionFactoryFLEX().getCurrentSession();
        
        HibernateSessionFactory.getSessionFactoryFLEX().close();
    }

}
