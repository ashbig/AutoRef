/*
 * Workflow.java
 *
 * This class represents a workflow which contains a list of 
 * protocol names with one following another.
 *
 * Created on June 25, 2001, 4:14 PM
 */

package edu.harvard.med.hip.flex.workflow;

import java.util.*;

import edu.harvard.med.hip.flex.process.Protocol;

/**
 *
 * @author  dzuo
 * @version 
 */
public class Workflow {
    private Vector flow;
    
    /** Creates new Workflow */
    public Workflow() {
        flow = new Vector();
        
        //initialize all the flow records. 
        Vector next = new Vector();
        next.addElement(Protocol.APPROVE_SEQUENCES);
        flow.addElement(new FlowRecord("customer request", next));
        
        next = new Vector();
        next.addElement(Protocol.DESIGN_CONSTRUCTS);
        flow.addElement(new FlowRecord(Protocol.APPROVE_SEQUENCES, next));
        
        next = new Vector();
        next.addElement(Protocol.GENERATE_OLIGO_ORDERS);        
        flow.addElement(new FlowRecord(Protocol.DESIGN_CONSTRUCTS, next));
                
        next = new Vector();
        next.addElement(Protocol.GENERATE_OLIGO_ORDERS);        
        flow.addElement(new FlowRecord(Protocol.DESIGN_CONSTRUCTS, next));
        
        next = new Vector();
        next.addElement(Protocol.RECEIVE_OLIGO_PLATES);        
        flow.addElement(new FlowRecord(Protocol.GENERATE_OLIGO_ORDERS, next));
  
        next = new Vector();
        next.addElement(Protocol.GENERATE_PCR_PLATES);        
        flow.addElement(new FlowRecord(Protocol.RECEIVE_OLIGO_PLATES, next)); 
        
        next = new Vector();
        next.addElement(Protocol.RUN_PCR_GEL);        
        flow.addElement(new FlowRecord(Protocol.GENERATE_PCR_PLATES, next));         

        next = new Vector();
        next.addElement(Protocol.ENTER_PCR_GEL_RESULTS);        
        flow.addElement(new FlowRecord(Protocol.RUN_PCR_GEL, next));      

        next = new Vector();
        next.addElement(Protocol.GENERATE_FILTER_PLATES);        
        flow.addElement(new FlowRecord(Protocol.ENTER_PCR_GEL_RESULTS, next));      
                
        next = new Vector();
        next.addElement(Protocol.GENERATE_BP_REACTION_PLATES);        
        flow.addElement(new FlowRecord(Protocol.GENERATE_FILTER_PLATES, next));         

        next = new Vector();
        next.addElement(Protocol.PERFORM_TRANSFORMATION);        
        flow.addElement(new FlowRecord(Protocol.GENERATE_BP_REACTION_PLATES, next)); 

        next = new Vector();
        next.addElement(Protocol.GENERATE_AGAR_PLATES);        
        flow.addElement(new FlowRecord(Protocol.PERFORM_TRANSFORMATION, next)); 

        next = new Vector();
        next.addElement(Protocol.ENTER_AGAR_PLATE_RESULTS);        
        flow.addElement(new FlowRecord(Protocol.GENERATE_AGAR_PLATES, next)); 
        
        next = new Vector();
        next.addElement(Protocol.GENERATE_CULTURE_BLOCKS_FOR_ISOLATES);        
        flow.addElement(new FlowRecord(Protocol.ENTER_AGAR_PLATE_RESULTS, next)); 

        next = new Vector();
        next.addElement(Protocol.GENERATE_DNA_PLATES);  
        next.addElement(Protocol.GENERATE_GLYCEROL_PLATES);
        next.addElement(Protocol.GENERATE_SEQUENCING_PCR_PLATES);      
        flow.addElement(new FlowRecord(Protocol.GENERATE_CULTURE_BLOCKS_FOR_ISOLATES, next)); 

        next = new Vector();
        next.addElement(Protocol.GENERATE_SEQUENCING_DNA_PLATES);        
        next.addElement(Protocol.GENERATE_GLYCEROL_PLATES);
        next.addElement(Protocol.GENERATE_SEQUENCING_PCR_PLATES);      
        flow.addElement(new FlowRecord(Protocol.GENERATE_DNA_PLATES, next)); 

        next = new Vector();
        next.addElement(Protocol.SUBMIT_SEQUENCING_ORDERS);        
        flow.addElement(new FlowRecord(Protocol.GENERATE_SEQUENCING_DNA_PLATES, next)); 

        next = new Vector();
        next.addElement(Protocol.SUBMIT_SEQUENCING_ORDERS);        
        flow.addElement(new FlowRecord(Protocol.GENERATE_GLYCEROL_PLATES, next)); 

        next = new Vector();
        next.addElement(Protocol.SUBMIT_SEQUENCING_ORDERS);        
        flow.addElement(new FlowRecord(Protocol.GENERATE_SEQUENCING_PCR_PLATES, next)); 

        next = new Vector();
        next.addElement(Protocol.RECEIVE_SEQUENCING_RESULTS);        
        flow.addElement(new FlowRecord(Protocol.SUBMIT_SEQUENCING_ORDERS, next));         
    }

    /**
     * Return the next protocol name for the given protocol name.
     *
     * @param protocol The given protocol name.
     * @return The next protocol name.
     */
    public Vector getNextProtocol(String protocol) {
        Enumeration enum = flow.elements();
        while(enum.hasMoreElements()) {
            FlowRecord r = (FlowRecord)enum.nextElement();
            if(r.isEqual(protocol)) {
                return r.getNext();
            }
        }
        
        return null;
    }
}
