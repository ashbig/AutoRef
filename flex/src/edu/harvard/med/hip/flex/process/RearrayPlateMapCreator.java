/*
 * RearrayPlateMapCreator.java
 *
 * This class converts different input into RearrayPlateMap object by querying database.
 * The converted object can then be used by other rearray classes.
 *
 * Created on May 15, 2003, 2:17 PM
 */

package edu.harvard.med.hip.flex.process;

import java.sql.*;
import java.util.*;
import java.io.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.core.*;

/**
 *
 * @author  dzuo
 */
public class RearrayPlateMapCreator {
    public final static String OLIGO_5P = "OLIGO_5P";
    
    protected boolean isPlateAsLabel = true;
    protected boolean isWellAsNumber = true;
    protected boolean isDestWellAsNumber = true;
    
    public RearrayPlateMapCreator() {}
    
    /** Creates a new instance of RearrayPlateMapCreator */
    public RearrayPlateMapCreator(boolean b1, boolean b2) {
        this.isPlateAsLabel = b1;
        this.isWellAsNumber = b2;
    }
    
    public void setIsDestWellAsNumber(boolean b) {this.isDestWellAsNumber = b;}
    public boolean getIsDestWellAsNumber() {return isDestWellAsNumber;}
    
    /**
     * Get all the necessary information from the database for the given input samples.
     *
     * @param inputSamples A list of samples that containes the information from input.
     * @conn The Database Connection object.
     * @return A ArrayList that contains all the information for samples.
     * @exception SQLException.
     */
    public ArrayList createRearraySamples(List inputSamples, Connection conn)
    throws SQLException, NumberFormatException, RearrayException {
        String s1 = "select s.sampleid,c.constructid,c.constructtype,c.oligoid_5p,c.oligoid_3p,f.sequenceid,f.cdsstart,f.cdsstop,f.cdslength,ch.containerid,ch.label, s.sampletype, s.cloneid"+
        " from flexsequence f, constructdesign c, containerheader ch,"+
        " (select * from sample where containerid in ("+
        " select containerid from containerheader where label=?)) s"+
        " where f.sequenceid=c.sequenceid"+
        " and c.constructid=s.constructid"+
        " and s.containerid=ch.containerid"+
        " and s.containerposition=?";
        String s2 = "select s.sampleid,c.constructid,c.constructtype,c.oligoid_5p,c.oligoid_3p,f.sequenceid,f.cdsstart,f.cdsstop,f.cdslength,ch.containerid,ch.label, s.sampletype, s.cloneid"+
        " from flexsequence f, constructdesign c, containerheader ch,"+
        " (select * from sample where containerid=?) s"+
        " where f.sequenceid=c.sequenceid"+
        " and c.constructid=s.constructid"+
        " and s.containerid=ch.containerid"+
        " and s.containerposition=?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        if(isPlateAsLabel) {
            stmt = conn.prepareStatement(s1);
        } else {
            stmt = conn.prepareStatement(s2);
        }
        
        ArrayList samples = new ArrayList();
        
        try {
            for(int i=0; i<inputSamples.size(); i++) {
                RearrayInputSample s = (RearrayInputSample)inputSamples.get(i);
                if(isPlateAsLabel) {
                    stmt.setString(1, s.getSourcePlate());
                } else {
                    stmt.setInt(1, Integer.parseInt(s.getSourcePlate()));
                }
                
                int position;
                if(!isWellAsNumber) {
                    position = Algorithms.convertWellFromA8_12toInt(s.getSourceWell());
                    //System.out.println(s.getSourceWell()+"\t"+position);
                } else {
                    position = Integer.parseInt(s.getSourceWell());
                }
                
                int destPosition = -1;
                if(s.getDestWell() != null) {
                    if(!isDestWellAsNumber) {
                        destPosition = Algorithms.convertWellFromA8_12toInt(s.getDestWell());
                    } else {
                        destPosition = Integer.parseInt(s.getDestWell());
                    }
                }
                
                stmt.setInt(2, position);
                
                rs = DatabaseTransaction.executeQuery(stmt);
                if(rs.next()) {
                    int sampleid = rs.getInt(1);
                    int constructid = rs.getInt(2);
                    if(constructid == 0) constructid=-1;
                    String constructtype = rs.getString(3);
                    int oligoid5p = rs.getInt(4);
                    int oligoid3p = rs.getInt(5);
                    int sequenceid = rs.getInt(6);
                    int cdsstart = rs.getInt(7);
                    int cdsstop = rs.getInt(8);
                    int cdslength = rs.getInt(9);
                    int containerid = rs.getInt(10);
                    String label = rs.getString(11);
                    String sampletype = rs.getString(12);
                    int cloneid = rs.getInt(13);
                    RearrayPlateMap m = new RearrayPlateMap(sampleid,constructid,constructtype,oligoid5p,oligoid3p,sequenceid,cdsstart,cdsstop,cdslength,containerid,label);
                    m.setSourceWell(position);
                    m.setRearrayInputSample(s);
                    m.setDestPlateLabel(s.getDestPlate());
                    m.setDestWell(destPosition);
                    m.setSampletype(sampletype);
                    m.setCloneid(cloneid);
                    samples.add(m);
                } else {
                    throw new RearrayException("No sample found for plate: "+s.getSourcePlate()+", well: "+s.getSourceWell());
                }
            }
            return samples;
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    
    /**
     * Get all the necessary information from the database for the given input samples.
     *
     * @param inputSamples A list of samples that containes the information from input.
     * @conn The Database Connection object.
     * @return A ArrayList that contains all the information for samples.
     * @exception SQLException.
     */
    public ArrayList createRearrayTemplateSamples(List inputSamples, Connection conn)
    throws SQLException, NumberFormatException, RearrayException {
        String s1 = "select s.sampleid,f.sequenceid,f.cdsstart,f.cdsstop,f.cdslength,ch.containerid,ch.label, s.sampletype, s.cloneid"+
        " from flexsequence f, mgcclone m, containerheader ch,"+
        " (select * from sample where containerid in ("+
        " select containerid from containerheader where label=?)) s"+
        " where f.sequenceid=m.sequenceid"+
        " and m.mgccloneid=s.sampleid"+
        " and s.containerid=ch.containerid"+
        " and s.containerposition=?";
        String s2 = "select s.sampleid,f.sequenceid,f.cdsstart,f.cdsstop,f.cdslength,ch.containerid,ch.label, s.sampletype, s.cloneid"+
        " from flexsequence f, mgcclone m, containerheader ch,"+
        " (select * from sample where containerid=?) s"+
        " where f.sequenceid=c.sequenceid"+
        " and m.mgccloneid=s.sampleid"+
        " and s.containerid=ch.containerid"+
        " and s.containerposition=?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        if(isPlateAsLabel) {
            stmt = conn.prepareStatement(s1);
        } else {
            stmt = conn.prepareStatement(s2);
        }
        
        ArrayList samples = new ArrayList();
        
        try {
            for(int i=0; i<inputSamples.size(); i++) {
                RearrayInputSample s = (RearrayInputSample)inputSamples.get(i);
                if(isPlateAsLabel) {
                    stmt.setString(1, s.getSourcePlate());
                } else {
                    stmt.setInt(1, Integer.parseInt(s.getSourcePlate()));
                }
                
                int position;
                if(!isWellAsNumber) {
                    position = Algorithms.convertWellFromA8_12toInt(s.getSourceWell());
                    //System.out.println(s.getSourceWell()+"\t"+position);
                } else {
                    position = Integer.parseInt(s.getSourceWell());
                }
                
                int destPosition = -1;
                if(s.getDestWell() != null) {
                    if(!isDestWellAsNumber) {
                        destPosition = Algorithms.convertWellFromA8_12toInt(s.getDestWell());
                    } else {
                        destPosition = Integer.parseInt(s.getDestWell());
                    }
                }
                
                stmt.setInt(2, position);
                
                rs = DatabaseTransaction.executeQuery(stmt);
                if(rs.next()) {
                    int sampleid = rs.getInt(1);                    
                    int sequenceid = rs.getInt(2);
                    int cdsstart = rs.getInt(3);
                    int cdsstop = rs.getInt(4);
                    int cdslength = rs.getInt(5);
                    int containerid = rs.getInt(6);
                    String label = rs.getString(7);
                    String sampletype = rs.getString(8);
                    int cloneid = rs.getInt(9);
                    RearrayPlateMap m = new RearrayPlateMap(sampleid,-1,null,-1,-1,sequenceid,cdsstart,cdsstop,cdslength,containerid,label);
                    m.setSourceWell(position);
                    m.setRearrayInputSample(s);
                    m.setDestPlateLabel(s.getDestPlate());
                    m.setDestWell(destPosition);
                    m.setSampletype(sampletype);
                    m.setCloneid(cloneid);
                    samples.add(m);
                } else {
                    throw new RearrayException("No sample found for plate: "+s.getSourcePlate()+", well: "+s.getSourceWell());
                }
            }
            return samples;
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
    }    
    
    /**
     * Get all the necessary information from the database for the given input clones.
     *
     * @param inputSamples A list of clone that containes the information from input.
     * @conn The Database Connection object.
     * @return A ArrayList that contains all the information for clones.
     * @exception SQLException.
     */
    public ArrayList createRearrayClones(List inputSamples, String storageType, String storageForm, Connection conn)
    throws SQLException, NumberFormatException, RearrayException {
        String sql = "select s.sampleid,c.constructid,c.constructtype,c.oligoid_5p,c.oligoid_3p,f.sequenceid,f.cdsstart,f.cdsstop,f.cdslength,ch.containerid,ch.label, s.sampletype, s.containerposition"+
        " from flexsequence f, constructdesign c, containerheader ch,"+
        " (select * from sample where sampleid in ("+
        " select storagesampleid from clonestorage where cloneid=?"+
        " and storageform=?"+
        " and storagetype=?)) s"+
        " where f.sequenceid=c.sequenceid"+
        " and c.constructid=s.constructid"+
        " and s.containerid=ch.containerid";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = null;
        ArrayList samples = new ArrayList();
        
        try {
            for(int i=0; i<inputSamples.size(); i++) {
                RearrayInputSample s = (RearrayInputSample)inputSamples.get(i);
                int cloneid = Integer.parseInt(s.getClone());
                stmt.setInt(1, cloneid);
                stmt.setString(2, storageForm);
                stmt.setString(3, storageType);
                
                int destPosition = -1;
                if(s.getDestWell() != null) {
                    if(!isDestWellAsNumber) {
                        destPosition = Algorithms.convertWellFromA8_12toInt(s.getDestWell());
                    } else {
                        destPosition = Integer.parseInt(s.getDestWell());
                    }
                }
                
                rs = DatabaseTransaction.executeQuery(stmt);
                if(rs.next()) {
                    int sampleid = rs.getInt(1);
                    int constructid = rs.getInt(2);
                    if(constructid == 0) constructid=-1;
                    String constructtype = rs.getString(3);
                    int oligoid5p = rs.getInt(4);
                    int oligoid3p = rs.getInt(5);
                    int sequenceid = rs.getInt(6);
                    int cdsstart = rs.getInt(7);
                    int cdsstop = rs.getInt(8);
                    int cdslength = rs.getInt(9);
                    int containerid = rs.getInt(10);
                    String label = rs.getString(11);
                    String sampletype = rs.getString(12);
                    int position = rs.getInt(13);
                    RearrayPlateMap m = new RearrayPlateMap(sampleid,constructid,constructtype,oligoid5p,oligoid3p,sequenceid,cdsstart,cdsstop,cdslength,containerid,label);
                    m.setSourceWell(position);
                    m.setRearrayInputSample(s);
                    m.setDestPlateLabel(s.getDestPlate());
                    m.setDestWell(destPosition);
                    m.setSampletype(sampletype);
                    m.setCloneid(cloneid);
                    m.setRearrayInputSample(s);
                    samples.add(m);
                } else {
                    throw new RearrayException("No clone found for "+s.getClone()+" with specified sample type and form.");
                }
            }
            return samples;
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    /**
     * Get the paired oligos from database for every oligo in the given samples.
     *
     * @param samples A list of samples.
     * @conn The Database Connection object.
     */
    public void setReversedOligos(List samples, Connection conn)
    throws SQLException, RearrayException {
        String sql = "select c1.oligoid_3p from constructdesign c1, constructdesign c2"+
        " where c1.constructpairid=c2.constructpairid"+
        " and c2.oligoid_3p=?"+
        " and c1.oligoid_3p<>?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            for (int i=0; i<samples.size(); i++) {
                RearrayPlateMap sample = (RearrayPlateMap)samples.get(i);
                stmt.setInt(1, sample.getOligoid3p());
                stmt.setInt(2, sample.getOligoid3p());
                rs = DatabaseTransaction.executeQuery(stmt);
                if(rs.next()) {
                    int id = rs.getInt(1);
                    sample.setOligo3pReversed(id);
                } else {
                    throw new RearrayException("Cannot find paired oligo for sample: "+sample.getSampleid());
                }
            }
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    /**
     * Get all the oligo sample information from the database for a given list of samples.
     *
     * @param samples A list of samples.
     * @param type The oligo type (5p, 3p fusion, 3p close)
     * @param conn The Database Connection object.
     * @return A list of oligo samples.
     * @exception SQLException.
     */
    public ArrayList createRearrayOligoSamples(List samples, String type, Connection conn)
    throws SQLException, RearrayException {
        ArrayList newSamples = new ArrayList();
        
        String sql = "select s.sampleid, s.constructid, s.containerid, s.containerposition, c.label, s.sampletype"+
        " from containerheader c, sample s"+
        " where c.containerid=s.containerid"+
        " and s.oligoid=?"+
        " and c.threadid=?";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            for (int i=0; i<samples.size(); i++) {
                RearrayPlateMap sample = (RearrayPlateMap)samples.get(i);
                setBindParam(stmt, type, sample);
                stmt.setInt(2, Integer.parseInt(sample.getSourcePlateLabel().substring(3,9)));
                rs = DatabaseTransaction.executeQuery(stmt);
                if(rs.next()) {
                    int sampleid = rs.getInt(1);
                    int constructid = rs.getInt(2);
                    if(constructid == 0) constructid=-1;
                    int containerid = rs.getInt(3);
                    int containerposition = rs.getInt(4);
                    String label = rs.getString(5);
                    String sampletype = rs.getString(6);
                    RearrayPlateMap s = new RearrayPlateMap(sampleid, constructid, null, -1, -1, -1, -1, -1, -1, containerid, label);
                    s.setSourceWell(containerposition);
                    s.setRearrayInputSample(sample.getRearrayInputSample());
                    s.setDestPlateLabel(sample.getDestPlateLabel());
                    s.setDestWell(sample.getDestWell());
                    s.setSampletype(sampletype);
                    
                    newSamples.add(s);
                } else {
                    throw new RearrayException("Cannot find oligo for sample: "+sample.getSampleid());
                }
            }
            return newSamples;
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    //---------------------------------------------------------------------------------//
    //                          Private Methods
    //---------------------------------------------------------------------------------//
    
    //Set the bind parameter.
    protected void setBindParam(PreparedStatement stmt, String type, RearrayPlateMap sample)
    throws SQLException {
        if(OLIGO_5P.equals(type)) {
            stmt.setInt(1, sample.getOligoid5p());
        } else if(type.equals(sample.getConstructtype())) {
            stmt.setInt(1, sample.getOligoid3p());
        } else {
            stmt.setInt(1, sample.getOligoid3pReversed());
        }
    }
    
    /********************************************************************************
     *                          Test
     ********************************************************************************/
    
    public static void main(String args[]) {
        //RearrayInputSample s1 = new RearrayInputSample("CGS000068-1", "F12", "DestPlate1", "A1", false);
        //RearrayInputSample s2 = new RearrayInputSample("CGS000068-2", "E6", "DestPlate1", "B1", false);
        //RearrayInputSample s3 = new RearrayInputSample("CGS000068-2", "E2", null, null, false);
        //ArrayList inputSamples = new ArrayList();
        //inputSamples.add(s1);
        //inputSamples.add(s2);
        //inputSamples.add(s3);
        
        //String file = "G:\\rearraytest_source.txt";
        String file = "G:\\rearraytest_clone.txt";
        RearrayPlateMapCreator creator = new RearrayPlateMapCreator(true, false);
        
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            ArrayList inputSamples = readFile(file);
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            //ArrayList outputSamples = creator.createRearraySamples(inputSamples, conn);
            ArrayList outputSamples = creator.createRearrayClones(inputSamples, StorageType.ORIGINAL, StorageForm.GLYCEROL, conn);
            for(int i=0; i<outputSamples.size(); i++) {
                RearrayPlateMap sample = (RearrayPlateMap)outputSamples.get(i);
                System.out.println("source plate ID:\t"+sample.getSourcePlateid());
                System.out.println("source Plate label:\t"+sample.getSourcePlateLabel());
                System.out.println("source well:\t"+sample.getSourceWell());
                System.out.println("construct type:\t"+sample.getConstructtype());
                System.out.println("dest plate label:\t"+sample.getDestPlateLabel());
                System.out.println("dest well:\t"+sample.getDestWell());
                System.out.println("sequence ID:\t"+sample.getSequenceid());
                System.out.println("CDS start:\t"+sample.getCdsstart());
                System.out.println("CDS stop:\t"+sample.getCdsstop());
                System.out.println("CDS length:\t"+sample.getCdslength());
                System.out.println("oligo 5p:\t"+sample.getOligoid5p());
                System.out.println("oligo 3p:\t"+sample.getOligoid3p());
                System.out.println("oligo 3p reversed:\t"+sample.getOligoid3pReversed());
                System.out.println();
                RearrayInputSample s = sample.getRearrayInputSample();
                System.out.println("source plate:\t"+s.getSourcePlate());
                System.out.println("source well:\t"+s.getSourceWell());
                System.out.println("dest plate:\t"+s.getDestPlate());
                System.out.println("dest well:\t"+s.getDestWell());
            }
            System.out.println("========================================================");
            
            creator.setReversedOligos(outputSamples, conn);
            for(int i=0; i<outputSamples.size(); i++) {
                RearrayPlateMap sample = (RearrayPlateMap)outputSamples.get(i);
                System.out.println("source plate ID:\t"+sample.getSourcePlateid());
                System.out.println("source Plate label:\t"+sample.getSourcePlateLabel());
                System.out.println("source well:\t"+sample.getSourceWell());
                System.out.println("construct type:\t"+sample.getConstructtype());
                System.out.println("dest plate label:\t"+sample.getDestPlateLabel());
                System.out.println("dest well:\t"+sample.getDestWell());
                System.out.println("sequence ID:\t"+sample.getSequenceid());
                System.out.println("CDS start:\t"+sample.getCdsstart());
                System.out.println("CDS stop:\t"+sample.getCdsstop());
                System.out.println("CDS length:\t"+sample.getCdslength());
                System.out.println("oligo 5p:\t"+sample.getOligoid5p());
                System.out.println("oligo 3p:\t"+sample.getOligoid3p());
                System.out.println("oligo 3p reversed:\t"+sample.getOligoid3pReversed());
                System.out.println();
                RearrayInputSample s = sample.getRearrayInputSample();
                System.out.println("source plate:\t"+s.getSourcePlate());
                System.out.println("source well:\t"+s.getSourceWell());
                System.out.println("dest plate:\t"+s.getDestPlate());
                System.out.println("dest well:\t"+s.getDestWell());
            }
            System.out.println("========================================================");
            
            ArrayList oligoSamples5p = creator.createRearrayOligoSamples(outputSamples,RearrayPlateMapCreator.OLIGO_5P, conn);
            for(int i=0; i<oligoSamples5p.size(); i++) {
                RearrayPlateMap sample = (RearrayPlateMap)oligoSamples5p.get(i);
                System.out.println("source plate ID:\t"+sample.getSourcePlateid());
                System.out.println("source Plate label:\t"+sample.getSourcePlateLabel());
                System.out.println("source well:\t"+sample.getSourceWell());
                System.out.println("construct type:\t"+sample.getConstructtype());
                System.out.println("dest plate label:\t"+sample.getDestPlateLabel());
                System.out.println("dest well:\t"+sample.getDestWell());
                System.out.println("sequence ID:\t"+sample.getSequenceid());
                System.out.println("CDS start:\t"+sample.getCdsstart());
                System.out.println("CDS stop:\t"+sample.getCdsstop());
                System.out.println("CDS length:\t"+sample.getCdslength());
                System.out.println("oligo 5p:\t"+sample.getOligoid5p());
                System.out.println("oligo 3p:\t"+sample.getOligoid3p());
                System.out.println("oligo 3p reversed:\t"+sample.getOligoid3pReversed());
                System.out.println();
                RearrayInputSample s = sample.getRearrayInputSample();
                System.out.println("source plate:\t"+s.getSourcePlate());
                System.out.println("source well:\t"+s.getSourceWell());
                System.out.println("dest plate:\t"+s.getDestPlate());
                System.out.println("dest well:\t"+s.getDestWell());
            }
            
            System.out.println("========================================================");
            
            ArrayList oligoSamples3p = creator.createRearrayOligoSamples(outputSamples,Construct.FUSION, conn);
            for(int i=0; i<oligoSamples3p.size(); i++) {
                RearrayPlateMap sample = (RearrayPlateMap)oligoSamples3p.get(i);
                System.out.println("source plate ID:\t"+sample.getSourcePlateid());
                System.out.println("source Plate label:\t"+sample.getSourcePlateLabel());
                System.out.println("source well:\t"+sample.getSourceWell());
                System.out.println("construct type:\t"+sample.getConstructtype());
                System.out.println("dest plate label:\t"+sample.getDestPlateLabel());
                System.out.println("dest well:\t"+sample.getDestWell());
                System.out.println("sequence ID:\t"+sample.getSequenceid());
                System.out.println("CDS start:\t"+sample.getCdsstart());
                System.out.println("CDS stop:\t"+sample.getCdsstop());
                System.out.println("CDS length:\t"+sample.getCdslength());
                System.out.println("oligo 5p:\t"+sample.getOligoid5p());
                System.out.println("oligo 3p:\t"+sample.getOligoid3p());
                System.out.println("oligo 3p reversed:\t"+sample.getOligoid3pReversed());
                System.out.println();
                RearrayInputSample s = sample.getRearrayInputSample();
                System.out.println("source plate:\t"+s.getSourcePlate());
                System.out.println("source well:\t"+s.getSourceWell());
                System.out.println("dest plate:\t"+s.getDestPlate());
                System.out.println("dest well:\t"+s.getDestWell());
            }
            
            System.out.println("========================================================");
            
            ArrayList oligoSamples3c = creator.createRearrayOligoSamples(outputSamples,Construct.CLOSED, conn);
            for(int i=0; i<oligoSamples3c.size(); i++) {
                RearrayPlateMap sample = (RearrayPlateMap)oligoSamples3c.get(i);
                System.out.println("source plate ID:\t"+sample.getSourcePlateid());
                System.out.println("source Plate label:\t"+sample.getSourcePlateLabel());
                System.out.println("source well:\t"+sample.getSourceWell());
                System.out.println("construct type:\t"+sample.getConstructtype());
                System.out.println("dest plate label:\t"+sample.getDestPlateLabel());
                System.out.println("dest well:\t"+sample.getDestWell());
                System.out.println("sequence ID:\t"+sample.getSequenceid());
                System.out.println("CDS start:\t"+sample.getCdsstart());
                System.out.println("CDS stop:\t"+sample.getCdsstop());
                System.out.println("CDS length:\t"+sample.getCdslength());
                System.out.println("oligo 5p:\t"+sample.getOligoid5p());
                System.out.println("oligo 3p:\t"+sample.getOligoid3p());
                System.out.println("oligo 3p reversed:\t"+sample.getOligoid3pReversed());
                System.out.println();
                RearrayInputSample s = sample.getRearrayInputSample();
                System.out.println("source plate:\t"+s.getSourcePlate());
                System.out.println("source well:\t"+s.getSourceWell());
                System.out.println("dest plate:\t"+s.getDestPlate());
                System.out.println("dest well:\t"+s.getDestWell());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            DatabaseTransaction.closeConnection(conn);
            System.exit(0);
        }
    }
    
    private static ArrayList readFile(String file)
    throws IOException, SQLException {
        BufferedReader in = new BufferedReader(new FileReader(file));
        String line = null;
        ArrayList inputSamples = new ArrayList();
        
        while((line = (in.readLine())) != null) {
            if(line.trim() == null || line.trim().equals(""))
                continue;
            
            StringTokenizer st = new StringTokenizer(line, "\t");
            String info[] = new String[4];
            int i = 0;
            while(st.hasMoreTokens()) {
                info[i] = st.nextToken();
                i++;
            }
            
            //RearrayInputSample s = new RearrayInputSample(info[0],info[1],info[2],info[3],false);
            RearrayInputSample s = new RearrayInputSample(info[0],info[1],info[2],info[3],true);
            inputSamples.add(s);
        }
        in.close();
        
        return inputSamples;
    }
}
