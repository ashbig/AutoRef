/*
 * ContainerInfoExporter.java
 *
 * Created on February 5, 2002, 3:45 PM
 */

package edu.harvard.med.hip.flex.export;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;

import java.util.*;
import java.io.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class ContainerInfoExporter {
    private boolean sampleid = false;
    private boolean type = false;
    private boolean position = false;
    private boolean status = false;
    private boolean sequenceid = false;
    private boolean cdsstart = false;
    private boolean cdsstop = false;
    private boolean cdslength = false;
    private boolean gccontent = false;
    private boolean sequencetext = false;
    private boolean cds = false;
    private boolean isEmpty = false;
    
    /** Creates new ContainerInfoExporter */
    public ContainerInfoExporter() {
    }
    
    public ContainerInfoExporter(boolean sampleid, boolean type, boolean position,
    boolean status, boolean sequenceid, boolean cdsstart, boolean cdsstop,
    boolean cdslength, boolean gccontent, boolean sequencetext, boolean cds, boolean isEmpty) {
        this.sampleid = sampleid;
        this.type = type;
        this.position = position;
        this.status = status;
        this.sequenceid = sequenceid;
        this.cdsstart = cdsstart;
        this.cdsstop = cdsstop;
        this.cdslength = cdslength;
        this.gccontent = gccontent;
        this.sequencetext = sequencetext;
        this.cds = cds;
        this.isEmpty = isEmpty;
    }
    
    public boolean doExport(int id, PrintWriter out) {
        try {
            Container container = new Container(id);
            container.restoreSample();
            Vector samples = container.getSamples();
            
            out.println("Container: "+container.getLabel());
            out.println();
            
            if(sampleid)
                out.print("Sample ID\t");
            if(type)
                out.print("Sample Type\t");
            if(position)
                out.print("Sample Position\t");
            if(status)
                out.print("Sample Status\t");
            if(sequenceid)
                out.print("Sequence ID\t");
            if(cdsstart)
                out.print("CDS Start\t");
            if(cdsstop)
                out.print("CDS Stop\t");
            if(cdslength)
                out.print("CDS Length\t");
            if(gccontent)
                out.print("GC Content\t");
            if(sequencetext)
                out.print("Sequence Text\t");
            if(cds)
                out.print("CDS");
            out.println();
            
            for(int i=0; i<samples.size(); i++) {
                Sample sample = (Sample)samples.elementAt(i);
                
                if(sampleid)
                    out.print(sample.getId()+"\t");
                if(type)
                    out.print(sample.getType()+"\t");
                if(position)
                    out.print(sample.getPosition()+"\t");
                if(status)
                    out.print(sample.getStatus()+"\t");
                
                if(Sample.CONTROL_POSITIVE.equals(sample.getType()) ||
                Sample.CONTROL_NEGATIVE.equals(sample.getType())){
                    out.println();
                    continue;
                }
                
                if(isEmpty && Sample.EMPTY.equals(sample.getType())) {
                    out.println();
                    continue;
                }
                
                FlexSequence sequence = sample.getFlexSequence();
                if(sequenceid)
                    out.print(sequence.getId()+"\t");
                if(cdsstart)
                    out.print(sequence.getCdsstart()+"\t");
                if(cdsstop)
                    out.print(sequence.getCdsstop()+"\t");
                if(cdslength)
                    out.print(sequence.getCdslength()+"\t");
                if(gccontent)
                    out.print(sequence.getGccontent()+"\t");
                if(sequencetext)
                    out.print(sequence.getSequencetext()+"\t");
                if(cds)
                    out.print(sequence.getSequencetext().substring(sequence.getCdsstart()-1,sequence.getCdsstop()));
                out.println();
            }
        } catch (FlexCoreException ex) {
            System.out.println(ex);
            return false;
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
            return false;
        }
        
        return true;
    }
}
