/*
 * MasterToExpressionContainerMapper.java
 *
 * Created on August 7, 2003, 9:29 AM
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author  DZuo
 */
public class MasterToExpressionContainerMapper {
    protected Vector sampleLineageSet;
    
    /** Creates a new instance of MasterToExpressionContainerMapper */
    public MasterToExpressionContainerMapper() {
        super();
    }
 
    public Vector getSampleLineageSet() {return sampleLineageSet;}
    
    public Container doMapping(Container c, String label, String containertype, Location l, int threadid, String sampletype, String vectorname) throws FlexDatabaseException {
        if(containertype == null || containertype.trim().equals("")) {
            containertype = ExpressionCloneContainer.EXPRESSION_CONTAINER_TYPE;
        }
        
        if(sampletype == null || sampletype.trim().equals("")) {
            sampletype = Sample.ISOLATE;
        }
        
        if(l == null) {
            l = new Location(Location.CODE_FREEZER, null, null);
        }
        
        Container newContainer = new ExpressionCloneContainer(containertype, l, label, threadid);  
        
        Vector oldSamples = c.getSamples();
        Enumeration enum = oldSamples.elements();
        String type = null;
        sampleLineageSet = new Vector();
        
        String sql = "select max(cloneid) from clones";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        int maxcloneid = 0;
        try {
            if(rs.next()) {
                maxcloneid=rs.getInt(1);
            } else {
                throw new FlexDatabaseException("Database error.");
            }
        } catch (SQLException ex) {
            throw new FlexDatabaseException(ex.getMessage());
        }
        
        while(enum.hasMoreElements()) {
            CloneSample s = (CloneSample)enum.nextElement();
            if(Sample.CONTROL_POSITIVE.equals(s.getType())) {
                type = Sample.CONTROL_POSITIVE;
            } else if(Sample.CONTROL_NEGATIVE.equals(s.getType())) {
                type = Sample.CONTROL_NEGATIVE;
            } else if(Sample.EMPTY.equals(s.getType())) {
                type = Sample.EMPTY;
            } else {
                type = sampletype;
            }
            
            CloningStrategy cs = CloningStrategy.findStrategyById(s.getStrategyid());
            CloningStrategy expStrategy = CloningStrategy.findStrategyByVectorAndLinker(vectorname, cs.getLinker5p().getId(), cs.getLinker3p().getId());
            ExpressionCloneSample newSample = new ExpressionCloneSample(type, s.getPosition(), newContainer.getId(), s.getConstructid(), s.getOligoid(), Sample.GOOD, 0);
            newSample.setCdslength(s.getCdslength());
            newSample.setGenbank(s.getGenbank());
            newSample.setGeneSymbol(s.getGeneSymbol());
            newSample.setPa(s.getPa());
            newSample.setSgd(s.getSgd());
            newSample.setCloneid(++maxcloneid);
            newSample.setClonetype(CloneSample.EXPRESSION);
            newSample.setMastercloneid(s.getMastercloneid());
            newSample.setSequenceid(s.getSequenceid());
            newSample.setConstructid(s.getConstructid());
            newSample.setStrategyid(expStrategy.getId());
            newSample.setClonestatus(CloneInfo.IN_PROCESS);     
            newSample.setPcrresult(CloneSample.NOT_DONE);
            newSample.setFloresult(CloneSample.NOT_DONE);
            newSample.setProteinresult(CloneSample.NOT_DONE);
            newSample.setRestrictionresult(CloneSample.NOT_DONE);
            newSample.setColonyresult(CloneSample.NOT_DONE);
            
            java.util.Date today = new java.util.Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            newSample.setStartdate(formatter.format(today));

            newContainer.addSample(newSample);
            sampleLineageSet.add(new SampleLineage(s.getId(), newSample.getId()));
        }
        
        return newContainer;
    }
}
