/*
 * GenbankInfo.java
 *
 * Created on August 3, 2005, 12:28 PM
 */
package plasmid.importexport.genbank;

import java.util.ArrayList;
import java.util.List;
import plasmid.coreobject.Publication;

/**
 *
 * @author  DZuo
 */
public class GenbankInfo {

    private String term;
    private String definition;
    private String accession;
    private String accessionVersion;
    private String gi;
    private String organism;
    private String geneid;
    private String genesymbol;
    private int cdsstart;
    private int cdsstop;
    private String sequencetext;
    private List names;
    
    private List publications;
    private int attL1Start;
    private int attL1Stop;
    private int attL2Start;
    private int attL2Stop;
    private int linker5pStart;
    private int linker5pStop;
    private int linker3pStart;
    private int linker3pStop;

    /** Creates a new instance of GenbankInfo */
    public GenbankInfo() {
        names = new ArrayList();
        publications = new ArrayList();
    }

    public GenbankInfo(String term, String definition, String accession, String accessionVersion, String gi, String organism,
            String geneid, String genesymbol, int cdsstart, int cdsstop, String sequencetext) {
        this.term = term;
        this.definition = definition;
        this.accession = accession;
        this.accessionVersion = accessionVersion;
        this.gi = gi;
        this.organism = organism;
        this.geneid = geneid;
        this.genesymbol = genesymbol;
        this.cdsstart = cdsstart;
        this.cdsstop = cdsstop;
        this.sequencetext = sequencetext;
        this.names = new ArrayList();
        this.publications = new ArrayList();
    }

    public String getTerm() {
        return term;
    }

    public String getDefinition() {
        return definition;
    }

    public String getAccession() {
        return accession;
    }

    public String getAccessionVersion() {
        return accessionVersion;
    }

    public String getGi() {
        return gi;
    }

    public String getOrganism() {
        return organism;
    }

    public String getGeneid() {
        return geneid;
    }

    public String getGenesymbol() {
        return genesymbol;
    }

    public int getCdsstart() {
        return cdsstart;
    }

    public int getCdsstop() {
        return cdsstop;
    }

    public String getSequencetext() {
        return sequencetext;
    }

    public List getNames() {
        return names;
    }

    public void setNames(List names) {
        this.names = names;
    }

    public void addName(GenbankInfoName name) {
        this.names.add(name);
    }

    public List getPublications() {
        return publications;
    }

    public void setPublications(List publications) {
        this.publications = publications;
    }

    public void addPublication(Publication p) {
        this.publications.add(p);
    }

    public int getAttL1Start() {
        return attL1Start;
    }

    public void setAttL1Start(int attL1Start) {
        this.attL1Start = attL1Start;
    }

    public int getAttL1Stop() {
        return attL1Stop;
    }

    public void setAttL1Stop(int attL1Stop) {
        this.attL1Stop = attL1Stop;
    }

    public int getAttL2Start() {
        return attL2Start;
    }

    public void setAttL2Start(int attL2Start) {
        this.attL2Start = attL2Start;
    }

    public int getAttL2Stop() {
        return attL2Stop;
    }

    public void setAttL2Stop(int attL2Stop) {
        this.attL2Stop = attL2Stop;
    }

    public int getLinker5pStart() {
        return linker5pStart;
    }

    public void setLinker5pStart(int linker5pStart) {
        this.linker5pStart = linker5pStart;
    }

    public int getLinker5pStop() {
        return linker5pStop;
    }

    public void setLinker5pStop(int linker5pStop) {
        this.linker5pStop = linker5pStop;
    }

    public int getLinker3pStart() {
        return linker3pStart;
    }

    public void setLinker3pStart(int linker3pStart) {
        this.linker3pStart = linker3pStart;
    }

    public int getLinker3pStop() {
        return linker3pStop;
    }

    public void setLinker3pStop(int linker3pStop) {
        this.linker3pStop = linker3pStop;
    }
}
