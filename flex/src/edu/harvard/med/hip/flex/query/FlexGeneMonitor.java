/*
 * QueryFlexGeneManager.java
 *
 * Created on January 9, 2003, 5:14 PM
 */

package edu.harvard.med.hip.flex.query;

import java.util.*;
import java.sql.*;
import javax.sql.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import sun.jdbc.rowset.*;
import java.io.*;


/**
 * The FlexGeneMonitor basically monitor the success/fail status of the sequence
 * going through the flexgene pipeline.
 *
 * @author  hweng
 *
 */
public class FlexGeneMonitor {
    
    public final String PG_step = "PCR_GEL";
    public final String AP_step = "AGAR_PLATE";
    public final String CP_step = "CULTURE_PLATE";
    public final String[] success_PCRGEL = 
    {"Correct", "Multiple w/ Correct", "Multiple with correct", "No visible band with cloning attempt", "Succeeded"};
    public final String[] success_CULTURE = {"Grow", "Succeeded"};
    
    
    /** Creates a new instance of QueryFlexGeneManager */
    public FlexGeneMonitor() {
    }
    
    
    /**
     * Calculate the project success rate up to the given process step.
     * @param projectid                     prject id
     * @param workflowid                    workflow id
     * @param constructtpe                  construct type (close | fusion | all)
     * @param step                          step of the processing
     * @param pcr_succ_criteria             user defined success criteria for the step of PCR
     * @param agar_plate_succ_criteria      user defined success criteria for the step of
     *                                      AGAR PLATE, basically it is the number of colonies
     * @param culture_succ_criteria         user defined success criteria for the step of Culture Plate
     * @param construct_date_from           the lower limit of the time to design construct
     * @param construct_date_to             the upper limit of the time to design construct
     *
     * @return              object of ProjectSuccessRate which capsulates the vector of SuccessRate objects 
     *                      that representing all success rates up to the given step
     */
    public ProjectSuccessRate calSuccessRate(int projectid, int workflowid, String constructtype, String step, 
    String[] pcr_succ_criteria, int agar_plate_succ_criteria, int culture_succ_criteria, 
    String construct_date_from, String construct_date_to) {
        
        Connection conn = null;
        try{
            DatabaseTransaction manager = DatabaseTransaction.getInstance();                    
            conn = manager.requestConnection();
        }catch(FlexDatabaseException e){
            System.out.println("cannot establish the connection to database!");
        }
        
        String workflow_constraint = " 1 = 1 ";
        String constructtype_constraint = " 1 = 1 ";
        int init_protocol_id = 0;
        
        if(workflowid != -1)
            workflow_constraint = "cd.workflowid = " + workflowid + " ";
        if(workflowid == 7 || workflowid == 8 || workflowid == 9)
            init_protocol_id = 36;          // process of MGC design constructs and rearray for MGC projects
        else
            init_protocol_id = 4;           // process of Design constructs for non-MGC projects
        if(!constructtype.equalsIgnoreCase("all"))
            constructtype_constraint = "cd.constructtype = '" + constructtype + "' ";
        
        // get the initial experimental sample number
        String sql_init = "";
        if(construct_date_from.length() == 0 && construct_date_to.length() == 0){ // use does not specify time window
            sql_init =
            "select distinct cd.sequenceid from constructdesign cd where " +
            "cd.projectid = " + projectid + " and " + workflow_constraint + " and " + constructtype_constraint;            
        }
        else{
            sql_init = 
            "select distinct cd.sequenceid " +
            "from constructdesign cd, processobject po, processexecution pe " +
            "where cd.sequenceid = po.sequenceid and po.executionid = pe.executionid " +
            "and cd.projectid = " + projectid + " and " + workflow_constraint + " and " + constructtype_constraint + " " +
            "and pe.protocolid = " + init_protocol_id + " " +
            "and pe.processdate between '" + construct_date_from + "' and '" + construct_date_to + "' ";
        }

        Vector initial_samples = new Vector();
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql_init);
            while(rs.next()){
                initial_samples.add(new Integer(rs.getInt(1)));
            }
            rs.close();
            stmt.close();
        }catch(SQLException e){
            System.out.println(e);
        }

        // the success result obtained from calSuccessRate_aux(...) without date window restriction
        Vector success_rate_by_step = calSuccessRate_aux(projectid, workflowid, constructtype, step,
                                                         pcr_succ_criteria, agar_plate_succ_criteria, culture_succ_criteria, new Vector(), conn);
        
        // make correction for the success result of all the steps if user specifies date window
        if(construct_date_from.length() != 0 && construct_date_to.length() != 0){
            for(int i = 0; i < success_rate_by_step.size(); i++){
                SuccessRate sr = (SuccessRate)success_rate_by_step.elementAt(i);
                sr.setTotal_samples(intersectVectors(sr.getTotal_samples(), initial_samples));
                sr.setSuccess_samples(intersectVectors(sr.getSuccess_samples(), initial_samples));
            }
        }
        
        success_rate_by_step.add(new SuccessRate(initial_samples, initial_samples, "INIT"));
      
        /// make virtual PCR result for (Yeast project / phase I failed ORFs workflow) 
        // and (pseudomonas project/pseudomonas cloning workflow)
        // since they don't require and don't have PCR result records
        if((projectid == 2 && workflowid == 11) || (projectid == 3 && workflowid == 4)){
            SuccessRate sr_init = (SuccessRate)(success_rate_by_step.lastElement());
            SuccessRate sr_pcr = (SuccessRate)(success_rate_by_step.elementAt(success_rate_by_step.size()-2));
            sr_pcr.setTotal_samples(sr_init.getTotal_samples());
            sr_pcr.setSuccess_samples(sr_init.getTotal_samples());            
        }
            
        
        // make correction for the total sample and success sample in all steps
        for(int i = success_rate_by_step.size()-1; i > 0; i--){
            SuccessRate sr1 = (SuccessRate)success_rate_by_step.elementAt(i);
            SuccessRate sr2 = (SuccessRate)success_rate_by_step.elementAt(i-1);
            sr2.setTotal_samples(intersectVectors(sr2.getTotal_samples(), sr1.getSuccess_samples()));            
            sr2.setSuccess_samples(intersectVectors(sr2.getSuccess_samples(), sr1.getSuccess_samples()));                        
        }
                       
        // construct the new object of ProjectSuccessRate
        ProjectSuccessRate project_succ_rate = new ProjectSuccessRate(success_rate_by_step, projectid, workflowid, constructtype);
        for(int i = 0; i < success_rate_by_step.size(); i++) {
            SuccessRate r = (SuccessRate)success_rate_by_step.elementAt(i);
            r.setNotDone_samples(project_succ_rate.getNotDoneSamplesInStep(r.getStep()));
            r.setOverall_step_success_rate(project_succ_rate.getOverallSucRate(r.getStep()));
        }
        
        
        return project_succ_rate;
    }
    
    
    
    /**
     * Auxilary method for calculating the project success rate up to the given process step.
     * @param projectid         prject id
     * @param workflowid        workflow id
     * @param constructtpe      construct type (close | fusion | all)
     * @param step              step of the processing
     * @param pcr_succ_criteria             user defined success criteria for the step of PCR GEL
     * @param agar_plate_succ_criteria      user defined success criteria for the step of
     *                                      AGAR PLATE, basically it is the number of colonies
     * @param success_rate_by_step          vector to store the success information, it will be passed during recursive call
     * @param conn                          database Connection
     *
     * @return vector of SuccessRate objects that representing the success rate up to the step user selected
     */
    public Vector calSuccessRate_aux(int projectid, int workflowid, String constructtype, String step,
    String[] pcr_succ_criteria, int agar_plate_succ_criteria, int culture_succ_criteria, Vector success_rate_by_step, Connection conn) {
                       
        String workflow_constraint = " 1 = 1 ";
        String constructtype_constraint = " 1 = 1 ";
        
        if(workflowid != -1)
            workflow_constraint = "cd.workflowid = " + workflowid + " ";
        if(!constructtype.equalsIgnoreCase("all"))
            constructtype_constraint = "cd.constructtype = '" + constructtype + "' ";
        
        String sql_total = "";  // sql to query the total number of samples going through one particular step
        String sql_success = "";  // sql to query the number of samples successfully passing one particular step
        TreeSet t_total = new TreeSet();
        TreeSet t_success = new TreeSet();
        
        if(step.equalsIgnoreCase(PG_step)){
            sql_total = "select distinct cd.sequenceid, r.resultvalue from constructdesign cd, sample s, result r " +
            "where cd.constructid = s.constructid and s.sampleid = r.sampleid " +
            "and cd.projectid = " + projectid + " " +
            "and " + workflow_constraint + " and " + constructtype_constraint + " " +
            "and s.sampletype = 'GEL' ";
            
            try{
                Statement stmt1 = conn.createStatement();                
                ResultSet rs_total = stmt1.executeQuery(sql_total);               
                while(rs_total.next()) 
                {
                    int seqid = rs_total.getInt(1);
                    String result = rs_total.getString(2);                    
                    t_total.add(new Integer(seqid));
                    if(isSuccessful_PCR_GEL(result, pcr_succ_criteria)){
                        t_success.add(new Integer(seqid));
                    }
                }             
                
                rs_total.close();                
                stmt1.close();                
            }catch(SQLException e){
                System.out.println(e);
                System.out.println("Exception throwed in PCR_GEL step in method of calSuccessRate_aux");
            }
                                    
            success_rate_by_step.add(new SuccessRate(treeSetToVector(t_total), treeSetToVector(t_success), PG_step));
            return success_rate_by_step;
        }
        
        else if(step.equalsIgnoreCase(AP_step)){

            String[] agar_plate_failures = new String[agar_plate_succ_criteria + 1];
            for(int i = 0; i < agar_plate_succ_criteria; i++)
                agar_plate_failures[i] = ""+i;
            agar_plate_failures[agar_plate_succ_criteria] = "none";
            
            sql_total = "select distinct cd.sequenceid, r.resultvalue from constructdesign cd, sample s, result r " +
            "where cd.constructid = s.constructid and s.sampleid = r.sampleid " +
            "and cd.projectid = " + projectid + " " +
            "and " + workflow_constraint + " and " + constructtype_constraint + " " +            
            //"and s.sampletype = 'AGAR' ";
            "and s.sampletype in ('AGAR', 'TRANSFORMATION') ";
                       
            try{
                Statement stmt1 = conn.createStatement();                
                ResultSet rs_total = stmt1.executeQuery(sql_total);                
                while(rs_total.next()) {
                    int seqid = rs_total.getInt(1);
                    String result = rs_total.getString(2);
                    t_total.add(new Integer(seqid));
                    if(isSuccessful_AGAR_PLATE(result, agar_plate_failures)){                       
                        t_success.add(new Integer(seqid));
                    }
                } 
                    
                rs_total.close();                
                stmt1.close();
                
            }catch(SQLException e){
                System.out.println(e);
                System.out.println("Exception throwed in AGAR_PLATE step in method of calSuccessRate_aux");
            }
            success_rate_by_step.add(new SuccessRate(treeSetToVector(t_total), treeSetToVector(t_success), AP_step));
            return calSuccessRate_aux(projectid, workflowid, constructtype, PG_step, pcr_succ_criteria,
                                        agar_plate_succ_criteria, culture_succ_criteria, success_rate_by_step, conn);
        }
        
        else if(step.equalsIgnoreCase(CP_step)){
                        
            sql_total = 
            "select distinct cd.sequenceid from constructdesign cd, sample s, result r " +
            "where cd.constructid = s.constructid and s.sampleid = r.sampleid " +
            "and cd.projectid = " + projectid + " " +
            "and " + workflow_constraint + " and " + constructtype_constraint + " " +            
            "and s.sampletype = 'ISOLATE' ";            
                        
            sql_success = 
            "select  t.id, count(t.id) from " +
            "(select cd.sequenceid as id , r.resultvalue as rs from constructdesign cd, sample s, result r " +
            "where cd.constructid = s.constructid and s.sampleid = r.sampleid " +
            "and cd.projectid = " + projectid + " " +
            "and " + workflow_constraint + " and " + constructtype_constraint + " " +
            "and s.sampletype = 'ISOLATE' and r.resultvalue in ('Grow', 'Succeeded'))t " +        
            "group by t.id, t.rs ";            
            
            try{
                Statement stmt1 = conn.createStatement();
                Statement stmt2 = conn.createStatement();
                ResultSet rs_total = stmt1.executeQuery(sql_total);
                ResultSet rs_success = stmt2.executeQuery(sql_success);
                while(rs_total.next())
                {
                    int seqid = rs_total.getInt(1);
                    t_total.add(new Integer(seqid));
                }
                rs_total.close();                
                stmt1.close();                                
                                
                while(rs_success.next())
                {
                    int seqid = rs_success.getInt(1);
                    int success_isolate = rs_success.getInt(2); 
                    if(success_isolate >= culture_succ_criteria){
                        t_success.add(new Integer(seqid));
                    }
                }
                rs_success.close();                
                stmt2.close();                
                
            }catch(SQLException e){
                System.out.println(e);
                System.out.println("Exception throwed in CULTURE_PLATE step in method of calSuccessRate_aux");
            }
            
            success_rate_by_step.add(new SuccessRate(treeSetToVector(t_total), treeSetToVector(t_success), CP_step));
            
            return calSuccessRate_aux(projectid, workflowid, constructtype, AP_step, pcr_succ_criteria, 
                                             agar_plate_succ_criteria, culture_succ_criteria, success_rate_by_step, conn);
        }
        
        return new Vector();  // dummy code, never executed
    }
    
    
    
    /* test whether the sample is successfully passed the particular step */
    public boolean isSuccessful(String step, String result, String[] criteria){
        
        if(step.equalsIgnoreCase("GEL") || step.equalsIgnoreCase(PG_step))
            return isSuccessful_PCR_GEL(result, criteria);
        else if(step.equalsIgnoreCase("AGAR") || step.equalsIgnoreCase(AP_step) || step.equalsIgnoreCase("TRANSFORMATION"))
            return isSuccessful_AGAR_PLATE(result, criteria);
        else if(step.equalsIgnoreCase("ISOLATE") || step.equalsIgnoreCase(CP_step))
            return isSuccessful_CULTURE_PLATE(result);
        else
            return false;
    }
    
    public boolean isSuccessful_PCR_GEL(String result, String[] success_pcr){       
        if(result == null || result.trim().length() == 0)
            return false;
        for(int i = 0; i < success_pcr.length; i++){
            if (result.equalsIgnoreCase(success_pcr[i])){
                return true;
            }
        }
        return false;
    }
    
    public boolean isSuccessful_AGAR_PLATE(String result, String[] failure_agar){
        if(result == null || result.trim().length() == 0)
            return false;
        for(int i = 0; i < failure_agar.length; i++){
            if(result.equalsIgnoreCase(failure_agar[i]))
                return false;
        }            
        return true;
    }        
    
    public boolean isSuccessful_CULTURE_PLATE(String result){
        for(int i = 0; i < success_CULTURE.length; i++){
            if (result.equalsIgnoreCase(success_CULTURE[i])){
                return true;
            }
        }
        return false;
    }                
        
    /**
     * transform TreeSet to Vector
     * @return  vector
     */
    public Vector treeSetToVector(TreeSet t){
        Vector v = new Vector();
        Iterator it = t.iterator();
        while(it.hasNext())
            v.add(it.next());
        return v;
    }
            
    
    /**
     * intersect two vectors
     * @return      vector
     */
    public Vector intersectVectors(Vector v1, Vector v2){
        Vector v = new Vector();
        for(int i = 0; i < v1.size(); i++){
            if(v2.contains(v1.elementAt(i)))
                v.add(v1.elementAt(i));
        }        
        return v;
    }
    

    /** 
     * Get samples information based on its sequence id, construct type, sample type and the project, workflow where it belong to.
     * @param seqence_id        int[]       a group of sequence ids
     * @param projectid         int         project id
     * @param workflowid        int         workflow id
     * @param construct_type    String      construct type (closed | fusion | all)
     * @param sample_type       String      sample type 
     * @return                  a TreeSet containing all the information of the sequences 
     *                              (sequenceid, cdslen, constructid, constructtype, sampleid, resulttype, resultvalue, label, position)
     *                          if param sample_type is "INIT", the TreeSet returned only contains sequence id. 
     */ 
    
    public TreeSet getSampleInfoBySeqids(int[] sequence_id, int projectid, int workflowid, String construct_type, String sample_type){
        
        if(sequence_id.length == 0) 
            return new TreeSet();
                      
        TreeSet seq_samples_info = new TreeSet(new SeqInfoComparator());
        
        if(sample_type.equalsIgnoreCase("INIT")){            
            for(int i = 0; i < sequence_id.length; i++){
                seq_samples_info.add(new SequenceInfo(new Sequence(sequence_id[i], -1, null)));
            }
            return seq_samples_info;
        }
        
        
        
        // divide the sequence_id array into several string arrays whose size is no more than 1000
        // to meet Oracle8.0 query restriction
        int k = (sequence_id.length - 1)/ 1000 + 1;
        String[] seq_id_in_list = new String[k];
        for(int i = 0; i < k; i++){
            seq_id_in_list[i] = "";
            for(int j = 1000*i; j < (sequence_id.length > 1000*(i+1) ? 1000*(i+1) : sequence_id.length); j++){
                seq_id_in_list[i] += "," + sequence_id[j];
            }
            seq_id_in_list[i] = seq_id_in_list[i].substring(1);
        }
                
        
        Connection conn = null;
        try{
            DatabaseTransaction manager = DatabaseTransaction.getInstance();
            conn = manager.requestConnection();
        }catch(FlexDatabaseException e){
            System.out.println("cannot establish the connection to database!");
        }
        
        String workflow_constraint = " 1 = 1 ";            
        if(workflowid != -1)
            workflow_constraint = "cd.workflowid = " + workflowid + " ";
        
        String constructtype[] = new String[2];        
        if(construct_type.equalsIgnoreCase("all") || construct_type.equalsIgnoreCase("CLOSED"))                           
            constructtype[0] = "CLOSED";
        if(construct_type.equalsIgnoreCase("all") || construct_type.equalsIgnoreCase("FUSION"))    
            constructtype[1] = "FUSION";                
        
        // speical case for old yeast project
        if(projectid == 2 && workflowid == 3 && sample_type.equalsIgnoreCase("AGAR"))
            sample_type = "TRANSFORMATION";
                      
        
        for(int j = 0; j < constructtype.length; j++){                        
            if(constructtype[j] == null)
                continue;
                 
            for(int i = 0; i < k; i++){
                String sql =                 
                "select distinct cd.sequenceid, f.cdslength, cd.constructid, cd.constructtype, s.sampleid, " +
                "r.resulttype, r.resultvalue, ch.label, s.containerposition, pe.processdate " +
                "from constructdesign cd, sample s, result r, containercell cc, containerheader ch, " +
                "flexsequence f, processexecution pe " +
                "where cd.constructid = s.constructid and s.sampleid = r.sampleid " +
                "and s.sampleid = cc.sampleid and cc.containerid = ch.containerid and cd.sequenceid = f.sequenceid " +
                "and r.executionid = pe.executionid " +
                "and cd.projectid = " + projectid + " " +
                "and " + workflow_constraint + " and cd.constructtype = '" + constructtype[j] + "' " +
                "and cd.sequenceid in (" + seq_id_in_list[i] + ") and s.sampletype = '" + sample_type + "' " +
                "order by cd.sequenceid ";

                try{
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);
                
                    while(rs.next()){                        
                        int sequenceid = rs.getInt(1);
                        int cdslen = rs.getInt(2);
                        int constructid = rs.getInt(3);
                        String type = rs.getString(4);
                        int sampleid = rs.getInt(5);
                        String resulttype = rs.getString(6);
                        String resultvalue = rs.getString(7);
                        String label = rs.getString(8);
                        int position = rs.getInt(9);                        

                        Sequence s = new Sequence(sequenceid, cdslen, null);
                        Construct cons = new Construct(constructid, type, null);
                        Sample sample = new Sample(sampleid, resulttype, resultvalue, label, position);
                        SequenceInfo seq_info = new SequenceInfo(s, cons, sample);
                        seq_samples_info.add(seq_info);

                    }
                }catch(SQLException e){
                    System.out.println(e);
                }                                    
            }        
        }
       
        
        return seq_samples_info;
    }
    
    
    /**
     * Maps the successful sequences and failed sequences in the particular project and workflow
     * to the plates and calculate the success and fail number for those plates. 
     * @param sequence_id_success       int array, the successful sequences
     * @param sequence_id_fail          int array, the failed sequences
     * @param projectid                 project id
     * @param workflowed                workflow id
     * @param construct_type            construct type
     * @param sample_type               sample type
     * @param criteria                  criteria to determine the success or failure of the step
     * @param min                       minium number of successful samples for a sequence in a particular plate
     *                                  
     *
     * @return                          HashMap, key are plate labels, values are PlateSuccessInfo objects
     *
     */

    public HashMap MapSuccInfoToPlate(int[] sequence_id_success, int[] sequence_id_fail, int projectid, int workflowid, 
                                      String construct_type, String sample_type, String[] criteria, int min){
        
        
        int success_list_number;
        int fail_list_number;
     
        // divide the sequence_id_success array into several lists whose size is no more than 1000
        // Oracle8.0 sql limitation 
                
        if(sequence_id_success.length == 0) 
            success_list_number = 0;
        else
            success_list_number = (sequence_id_success.length - 1)/ 1000 + 1;        
        String[] success_seq_id_list = new String[success_list_number];        
        for(int i = 0; i < success_list_number; i++){
            success_seq_id_list[i] = "";
            for(int j = 1000*i; j < (sequence_id_success.length > 1000*(i+1) ? 1000*(i+1) : sequence_id_success.length); j++){
                success_seq_id_list[i] += "," + sequence_id_success[j];
            }
            success_seq_id_list[i] = success_seq_id_list[i].substring(1);
        }
                
        
        // divide the sequence_id_fail array into several lists whose size is no more than 1000
        // Oracle8.0 sql limitation      
        
        if(sequence_id_fail.length == 0)
            fail_list_number = 0;
        else
            fail_list_number = (sequence_id_fail.length - 1)/ 1000 + 1;           
        String[] fail_seq_id_list = new String[fail_list_number];        
        for(int i = 0; i < fail_list_number; i++){
            fail_seq_id_list[i] = "";
            for(int j = 1000*i; j < (sequence_id_fail.length > 1000*(i+1) ? 1000*(i+1) : sequence_id_fail.length); j++){
                fail_seq_id_list[i] += "," + sequence_id_fail[j];
            }
            fail_seq_id_list[i] = fail_seq_id_list[i].substring(1);
        }
                 
        
        Connection conn = null;
        try{
            DatabaseTransaction manager = DatabaseTransaction.getInstance();
            conn = manager.requestConnection();
        }catch(FlexDatabaseException e){
            System.out.println("cannot establish the connection to database!");
        }
        
        String workflow_constraint = " 1 = 1 ";                        
        if(workflowid != -1)
            workflow_constraint = "cd.workflowid = " + workflowid + " ";
        
        HashMap plateSucInfo = new HashMap();

        // speical case for old yeast project
        if(projectid == 2 && workflowid == 3 && sample_type.equalsIgnoreCase("AGAR"))
            sample_type = "TRANSFORMATION";
        
        
        for(int i = 0; i < fail_list_number; i++){
            
            String sql = "";
            if(construct_type.equalsIgnoreCase("all") ){            
                sql = 
                "select t.label, count(t.label) from " +
                "(select distinct cd.sequenceid as id, ch.label as label " +
                "from constructdesign cd, sample s, containercell cc, containerheader ch, result r " +
                "where cd.constructid = s.constructid and s.sampleid = r.sampleid " +
                "and s.sampleid = cc.sampleid and cc.containerid = ch.containerid " +                
                "and cd.projectid = " + projectid + " " +
                "and " + workflow_constraint + " and cd.constructtype = 'CLOSED' " +
                "and cd.sequenceid in (" + fail_seq_id_list[i] + ") and s.sampletype = '" + sample_type + "' " +
                "union " +
                "select distinct cd.sequenceid as id, ch.label as label " +
                "from constructdesign cd, sample s, containercell cc, containerheader ch, result r " +
                "where cd.constructid = s.constructid and s.sampleid = r.sampleid " +
                "and s.sampleid = cc.sampleid and cc.containerid = ch.containerid " +                
                "and cd.projectid = " + projectid + " " +
                "and " + workflow_constraint + " and cd.constructtype = 'FUSION' " +
                "and cd.sequenceid in (" + fail_seq_id_list[i] + ") and s.sampletype = '" + sample_type + "' " +
                ") t " +
                "group by t.label ";
            }
            else {      
                sql = 
                "select t.label, count(t.label) from " +
                "(select distinct cd.sequenceid as id, ch.label as label " +
                "from constructdesign cd, sample s, containercell cc, containerheader ch, result r " +
                "where cd.constructid = s.constructid and s.sampleid = r.sampleid " +
                "and s.sampleid = cc.sampleid and cc.containerid = ch.containerid " +                
                "and cd.projectid = " + projectid + " " +
                "and " + workflow_constraint + " and cd.constructtype = '" + construct_type + "' " +
                "and cd.sequenceid in (" + fail_seq_id_list[i] + ") and s.sampletype = '" + sample_type + "' ) t " +
                "group by t.label ";
            }
            
            try{            
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
              
                while(rs.next()){                                      
		    String label = rs.getString(1);    
                    int fail = rs.getInt(2);
                    if(plateSucInfo.containsKey(label)){
                        PlateSuccessInfo p = (PlateSuccessInfo)(plateSucInfo.get(label));
                        p.setFail(p.getFail() + fail);
                    }
                    else
                       plateSucInfo.put(label, new PlateSuccessInfo(label, 0, fail)); 
                }
                rs.close();
                stmt.close();
            }catch(SQLException e){
                System.out.println(e);
            }
        }
        
 
        
        for(int i = 0; i < success_list_number; i++){
            
            String sql = "";
            if(construct_type.equalsIgnoreCase("all") ){               
                sql = 
                "select * from ( " +
                "select cd.sequenceid, ch.label, r.resultvalue " +
                "from constructdesign cd, sample s, containercell cc, containerheader ch, result r " +
                "where cd.constructid = s.constructid and s.sampleid = r.sampleid " +
                "and s.sampleid = cc.sampleid and cc.containerid = ch.containerid " +                                
                "and cd.projectid = " + projectid + " " +
                "and " + workflow_constraint + " and cd.constructtype = 'CLOSED' " + 
                "and cd.sequenceid in (" + success_seq_id_list[i] + ") and s.sampletype = '" + sample_type + "' " +              
                "union all " +
                "select cd.sequenceid, ch.label, r.resultvalue " +
                "from constructdesign cd, sample s, containercell cc, containerheader ch, result r " +
                "where cd.constructid = s.constructid and s.sampleid = r.sampleid " +
                "and s.sampleid = cc.sampleid and cc.containerid = ch.containerid " +                                
                "and cd.projectid = " + projectid + " " +
                "and " + workflow_constraint + " and cd.constructtype = 'FUSION' " + 
                "and cd.sequenceid in (" + success_seq_id_list[i] + ") and s.sampletype = '" + sample_type + "' " +
                ") order by sequenceid, label";                
            }
            else{                                   
                sql = 
                "select cd.sequenceid, ch.label, r.resultvalue " +
                "from constructdesign cd, sample s, containercell cc, containerheader ch, result r " +
                "where cd.constructid = s.constructid and s.sampleid = r.sampleid " +
                "and s.sampleid = cc.sampleid and cc.containerid = ch.containerid " +                                
                "and cd.projectid = " + projectid + " " +
                "and " + workflow_constraint + " and cd.constructtype = '" + construct_type + "' " +
                "and cd.sequenceid in (" + success_seq_id_list[i] + ") and s.sampletype = '" + sample_type + "' "; 
            }

            int cursor_seqid = 0;
            String cursor_label = "";
            int score = 0;
           
            try{            
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
              
                while(rs.next()){              
                    int seqid = rs.getInt(1);
		    String label = rs.getString(2);
                    String result = rs.getString(3);
    
                    if(rs.isFirst() || (seqid == cursor_seqid && label.equalsIgnoreCase(cursor_label) )){
                        if(isSuccessful(sample_type, result, criteria))
                            score++;
                    }
                    else{
                        if(score >= min){
                            if(plateSucInfo.containsKey(cursor_label)){
                                PlateSuccessInfo p = (PlateSuccessInfo)(plateSucInfo.get(cursor_label));
                                p.increSuccess();
                            }
                            else
                                plateSucInfo.put(cursor_label, new PlateSuccessInfo(cursor_label, 1, 0));                                
                        }
                        else{
                            if(plateSucInfo.containsKey(cursor_label)){
                                PlateSuccessInfo p = (PlateSuccessInfo)(plateSucInfo.get(cursor_label));
                                p.increFail();
                            }
                            else
                                plateSucInfo.put(cursor_label, new PlateSuccessInfo(cursor_label, 0, 1));                                
                        }
                        score = 0;                      
                        if(isSuccessful(sample_type, result, criteria))
                            score++;
                    }
                    cursor_seqid = seqid;
                    cursor_label = label;
                                
                }                                                                                   
                
                // for the last row of the result set
                if(score >= min){
                    if(plateSucInfo.containsKey(cursor_label)){
                        PlateSuccessInfo p = (PlateSuccessInfo)(plateSucInfo.get(cursor_label));
                        p.increSuccess();
                    }
                    else
                        plateSucInfo.put(cursor_label, new PlateSuccessInfo(cursor_label, 1, 0));                                
                }
                else{
                    if(plateSucInfo.containsKey(cursor_label)){
                        PlateSuccessInfo p = (PlateSuccessInfo)(plateSucInfo.get(cursor_label));
                        p.increFail();
                    }
                    else
                        plateSucInfo.put(cursor_label, new PlateSuccessInfo(cursor_label, 0, 1));                                
                }
               
                
                
            }catch(SQLException e){
                System.out.println(e);
            }
        }                                            

        return plateSucInfo;
    }
                    
                    
                    
    

    ///////////////////////////////////  main  /////////////////////////////////////////////////////
    public static void main(String[] args){
       
        FlexGeneMonitor m = new FlexGeneMonitor();

        long start = System.currentTimeMillis();
 /*       
        //Vector v = (m.calSuccessRate(5, 6, "all", "Culture_Plate", m.success_PCRGEL, 4, 2, "03-May-01", "03-AUG-03")).getSuccess_rates();
        Vector v = (m.calSuccessRate(5, 6, "CLOSED", "Culture_Plate", m.success_PCRGEL, 4, 4, "03-May-01", "03-AUG-03")).getSuccess_rates();
        for(int i = 0; i < v.size(); i++){
            SuccessRate s = (SuccessRate)(v.elementAt(i));
            System.out.println("____ " + s.getTotal() + " ; " + s.getSuccess());
        }
        
        //System.out.println("------- " + m.getOverallRateByStep(v, "CULTURE_PLATE"));
        
        long end = System.currentTimeMillis();
        System.out.println("time cost : " + (end - start)/1000 + " seconds");
  
  /*   
        int[] sequence_id = {22104, 22102, 22101, 22034, 22039, 22043};
        TreeSet t = m.getSampleInfoBySeqids(sequence_id, 5, 6, "all", "ISOLATE");
   System.out.println(t.size());
        Iterator it = t.iterator();
        while(it.hasNext()){
          SequenceInfo seq = (SequenceInfo)(it.next());
          System.out.println("seq_id: " + seq.getSequence().getSeqID() + "\n" +
                             "CD length: " + seq.getSequence().getCDS_len() + "\n" +          
                            "const_id: " + seq.getConstruct().getId() + "\n" +
                                "const_type: " + seq.getConstruct().getType() + "\n" +
                             "sample_id: " + seq.getSample().getId() + "\n" +
                                    "sample_type: " + seq.getSample().getType() + "\n" +
                                    "sample_result: " + seq.getSample().getResult() + "\n" +
                                    "label: " + seq.getSample().getLabel() + "\n" +
                                    "position: " + seq.getSample().getPosition() + "\n\n");
        }
          
          
        
        System.out.println("------ done");        
                long end = System.currentTimeMillis();
        System.out.println("time cost : " + (end - start)/1000 + " seconds");
*/
   
        int[] sequence_id_success = {18819, 18823, 18827, 19013, 19022};
        int[] sequence_id_fail = {18818, 18889, 18907, 18917, 19041};
        
        HashMap hm = m.MapSuccInfoToPlate(sequence_id_success, sequence_id_fail, 5, 6, "CLOSED", "GEL", m.success_PCRGEL, 1);
        Iterator it = hm.keySet().iterator();
        while(it.hasNext()){
            PlateSuccessInfo p = (PlateSuccessInfo)(hm.get(it.next()));
            System.out.println(p.getLabel() + " " + p.getSuccess() + " " + p.getFail());
        }
        
             

        
    }
    
}
