package edu.harvard.med.hip.flex.core;
import  edu.harvard.med.hip.flex.util.*;
import  edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import edu.harvard.med.hip.flex.workflow.*;

/**
 * This class represents an oligo object.
 * $Id: Oligo.java,v 1.3 2003-10-20 18:33:14 dzuo Exp $
 * @File:	Oligo.java
 * @Date:	4/30/01
 * @author:	Wendy Mar
 *
 * modified 5/28/01 wmar:   added insert method
 * modified 6/28/01 wmar:   added constructor taking oligoid as parameter
 * modified 1/28/02 wmar:   added project specific tagged oligos
 */
public class Oligo {
    private static final String GATEWAYTAG_5p = "AAAGCAGGCTCCACC";
    private static final String GATEWAYTAG_3p_CLOSE = "ACAAGAAAGCTGGGTCCTA";
    private static final String GATEWAYTAG_3p_CLOSE_PA = "AGAAAGCTGGGTCCTA";
    private static final String GATEWAYTAG_3p_FUSION = "ACAAGAAAGCTGGGTCCAA";
    private static final String CLONETECH_5p = "GAAGTTATCAGTCGACACC";
    private static final String CLONETECH_3p_CLOSE = "ATGGTCTAGAAAGCTTCCCTA";
    private static final String CLONETECH_3p_FUSION = "ATGGTCTAGAAAGCTTCCCAA";
    public static final String PSEUDOMONAS_5p = "AAAGCAGGCTCCGAAGGAGATACC";
    public static final String PSEUDOMONAS_3p_FUSION = "AGAAAGCTGGGTCTCC";
     private static final String YEAST_REV_3p_CLOSE = "GTATCCCCGGGAATTGCCATG";
    private static final String YEAST_REV_3p_FUSION = "";
    private static final String YEAST_REV_5p = "CAGGCTTCCAGCTGACCACC";
  //  private Project project;
  //  private Workflow workflow;
    private int id;
    private String type;
    private String sequence;
    private double Tm;
    private String tagSequence;
    
    /**
     * Constructor. Return an Oligo object.
     *
     * @param type An oligo type (five, three, threeopen).
     * @param sequence The oligo sequence text.
     * @param Tm  A double type.
     * @return An Oligo object.
     */
    public Oligo(String type, String sequence, double Tm) throws FlexDatabaseException {
        this.type = type;
        this.sequence = sequence;
        this.Tm = Tm;
        this.id = FlexIDGenerator.getID("oligoid");
    }
    
    /**
     * Constructor. Return an Oligo object.
     *
     * @param id The oligoID
     * @param type An oligo type (five, three, threeopen).
     * @param sequence The oligo sequence text.
     * @param Tm  A double type.
     * @return An Oligo object.
     */
    public Oligo(int id, String type, String sequence, double Tm) {
        this.id = id;
        this.type = type;
        this.sequence = sequence;
        this.Tm = Tm;
    }
    
    /**
     * Constructor.
     *
     * @param id The oligo id.
     *
     * @return A oligo object with id.
     * @exception FlexCoreException, FlexDatabaseException.
     */
    public Oligo(int id) throws FlexDatabaseException {
        this.id = id;
        
        String sql = "select o.oligoid, o.oligosequence, "+
        "o.tm, o.gatewaysequence\n"+
        "from oligo o\n"+
        "where o.oligoid = "+id; 
        
        ResultSet rs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next()) {
                sequence = rs.getString("OLIGOSEQUENCE");
                Tm = rs.getDouble("TM");
                tagSequence = rs.getString("gatewaysequence");
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while initializing Oligo with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    } //constructor
    
    /**
     * Add the 5p tag to 5p oligo corresponding to the project and workflow
     */
    public void setTagSequence_5p(Project project, Workflow workflow) {
        //int projectId = project.getId();
        /*
        if (projectId == Project.BREASTCANCER) {
            //breast cancer project
            tagSequence = GATEWAYTAG_5p + sequence;
        }
        */
        /**
         * The tag sequences should be attached to workflow rather than the project.
         * - dzuo 7/10/02
         if (projectId == Project.CLONTECH) {
            //clonetech project
            tagSequence = CLONETECH_5p + sequence;
        } else if (projectId == Project.PSEUDOMONAS) {
            tagSequence = PSEUDOMONAS_5p + sequence;
        } else{
            //all other projects
            tagSequence = GATEWAYTAG_5p + sequence;
        }  
         */
        int workflowid = workflow.getId();
        
        if(workflowid == Workflow.CREATOR_WORKFLOW || workflowid == Workflow.MGC_CREATOR_WORKFLOW || workflowid == Workflow.DNA_TEMPLATE_CREATOR) {
            tagSequence = CLONETECH_5p + sequence;
        } else if(workflowid == Workflow.PSEUDOMONAS_WORKFLOW) {
            tagSequence = PSEUDOMONAS_5p + sequence;
        } else if(workflowid == Workflow.MGC_GATEWAY_WORKFLOW || workflowid == Workflow.STANDARD_WORKFLOW) {
            tagSequence = GATEWAYTAG_5p + sequence;
        } else {
            tagSequence = CLONETECH_5p + sequence;
        }
    }
    
    /**
     * Add the 3p stop tag to 3s oligo corresponding to the project and workflow
     * the stop codon is included into the tag sequence
     */
    public void setTagSequence_3p_Close(Project project, Workflow workflow) {
        //int projectId = project.getId();
        /*
        if (projectId == Project.BREASTCANCER) {
            //breast cancer project
            tagSequence = GATEWAYTAG_3p_CLOSE + sequence;
        }
         */
         /**
        if (projectId == Project.CLONTECH) {
            //clonetech project
            tagSequence = CLONETECH_3p_CLOSE + sequence;
        } else {
            if (projectId == Project.PSEUDOMONAS) {
            //pseudomonas project
            tagSequence = GATEWAYTAG_3p_CLOSE_PA + sequence;
            } else {
                //breast cancer project and other human projects
                tagSequence = GATEWAYTAG_3p_CLOSE + sequence;
            } //inner if
        } //outter if
          */
        
        /**
         * Changed the tag sequence to attached to workflow rather than project. - dzuo 7/10/02
         */
        int workflowid = workflow.getId();
        
        if(workflowid == Workflow.CREATOR_WORKFLOW || workflowid == Workflow.MGC_CREATOR_WORKFLOW || workflowid == Workflow.DNA_TEMPLATE_CREATOR) {
            tagSequence = CLONETECH_3p_CLOSE + sequence;
        } else if(workflowid == Workflow.PSEUDOMONAS_WORKFLOW) {
            tagSequence = GATEWAYTAG_3p_CLOSE_PA + sequence;
        } else if (workflowid == Workflow.MGC_GATEWAY_WORKFLOW || workflowid == Workflow.STANDARD_WORKFLOW) {
            tagSequence = GATEWAYTAG_3p_CLOSE + sequence; 
        } else {
            tagSequence = CLONETECH_3p_CLOSE + sequence;
        }        
    }
    
    /**
     * Add the 3p open tag to 3op oligo corresponding to the project and workflow
     * 
     */
    public void setTagSequence_3p_Fusion(Project project, Workflow workflow) {
        //int projectId = project.getId();
        /**
        if (projectId == Project.CLONTECH) {
            //clonetech project
            tagSequence = CLONETECH_3p_FUSION + sequence;
        } else {
            if (projectId == Project.PSEUDOMONAS) {
            //pseudomonas project
            tagSequence = PSEUDOMONAS_3p_FUSION + sequence;
            } else {
                //breast cancer project and other human projects
                tagSequence = GATEWAYTAG_3p_FUSION + sequence;
            } //inner if
        } //outter if
         */
        
        /**
         * Changed the tag sequence to attached to workflow rather than project. - dzuo 7/10/02
         */
        int workflowid = workflow.getId();
        
        if(workflowid == Workflow.CREATOR_WORKFLOW || workflowid == Workflow.MGC_CREATOR_WORKFLOW || workflowid == Workflow.DNA_TEMPLATE_CREATOR) {
            tagSequence = CLONETECH_3p_FUSION + sequence;
        } else if(workflowid == Workflow.PSEUDOMONAS_WORKFLOW) {
            tagSequence = PSEUDOMONAS_3p_FUSION + sequence;
        } else if(workflowid == Workflow.MGC_GATEWAY_WORKFLOW || workflowid == Workflow.STANDARD_WORKFLOW) {
            tagSequence = GATEWAYTAG_3p_FUSION + sequence;
        } else {
            tagSequence = CLONETECH_3p_FUSION + sequence;
        }                
    }
    
    /**
     * Return oligo sequence without tag as a string.
     *
     * @return A string representation of the oligo sequence.
     */
    public String getSequence() {
        return sequence;
    }
    
    /**
     * Return tagSequence as a String.
     * @return A String representation for tagSequence.
     */
    public String getTagOligoSequence() {
        
            return tagSequence;
     
    }
    
    /**
     * Return oligo type.
     *
     * @return A String representation for the oligo type.
     */
    public String getType() {
        return type;
    }
    
    /**
     * Return oligo melting temp: Tm.
     *
     * @return A double which indicates the oligo Tm.
     */
    public double getTm() {
        return Tm;
    }
    
    /**
     * Return oligo length.
     *
     * @return A integer which indicates the oligo length.
     */
    public int getOligoLength() {
        return sequence.length();
    }
    
    /**
     * Return oligo id.
     *
     * @return A integer which indicates the oligo length.
     */
    public int getOligoID() {
        return id;
    }
    
    /**
     * insert oligos into Oligo table.
     *
     */
    public void insert(Connection conn) throws FlexDatabaseException {
       // String tagSequence = this.getGatewayOligoSequence(OligoType);
        Statement stmt = null;
        
        String sql = "INSERT INTO oligo\n" +
        "(oligoid, oligosequence, tm, gatewaysequence)\n" +
        " VALUES(" +id+ ",'" + sequence + "',"
         + Tm + ", '" + tagSequence + "')";
        
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }        
    } //insertOligo
    
    public static void main(String [] args) {
        Connection c = null;
        int oligoid = 1;
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            Oligo o = new Oligo(oligoid);
            
            System.out.print("Oligo ID: "+o.getOligoID()+ "\n");
            System.out.println("Oligo Sequence: "+o.getSequence());
            System.out.println("Oligo TM: "+ o.getTm());
            
        }
        catch (FlexDatabaseException exception) {
            System.out.println(exception.getMessage());        
        }finally {
            DatabaseTransaction.closeConnection(c);
        }
    } //main
    
}
