/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package transfer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dzuo
 */
public class ProgramdefinitionTO {
    public static final String STATUS_ACTIVE = "Active";
    
    private String name;
    private String description;
    private String type;
    private String status;
    private int sourcenum;
    private int destnum;
    private String createdate;
    private String researcher;

    private List<ProgramcontainerTO> containers;
    private List<ProgrammappingTO> mappings;
    private List<FilereferenceTO> files;
    
    public ProgramdefinitionTO() {
        containers = new ArrayList<ProgramcontainerTO>();
        mappings = new ArrayList<ProgrammappingTO>();
        files = new ArrayList<FilereferenceTO>();
    }
    
    public ProgramdefinitionTO(String name, String desc, String type, String status, int srcnum, int destnum, String createdate, String researcher) {
        this.setName(name);
        this.setDescription(desc);
        this.setType(type);
        this.setStatus(status);
        this.setSourcenum(srcnum);
        this.setDestnum(destnum);
        this.setCreatedate(createdate);
        this.setResearcher(researcher);
        containers = new ArrayList<ProgramcontainerTO>();
        mappings = new ArrayList<ProgrammappingTO>();
        files = new ArrayList<FilereferenceTO>();
    }
    
    public void addProgramcontainer(ProgramcontainerTO c) {
        this.getContainers().add(c);
    }
    
    public void addProgrammapping(ProgrammappingTO mapping) {
        this.mappings.add(mapping);
    }
    
    public void addFile(FilereferenceTO file) {
        this.files.add(file);
    }
       
    public List<ProgramcontainerTO> getContainersByIO(String io) {
        List<ProgramcontainerTO> cs = new ArrayList<ProgramcontainerTO>();
        for(ProgramcontainerTO c:getContainers()) {
            if(c.getIoflag().equals(io)) {
                cs.add(c);
            }
        }
        return cs;
    }
    
    public int getDestRownum() {
        Set<Integer> s = new HashSet<Integer>();
        for(ProgrammappingTO p:getMappings()) {
            s.add(new Integer(p.getDestwellx()));
        }
        
        return s.size();
    }
    
    public int getDestColnum() {
        Set<Integer> s = new HashSet<Integer>();
        for(ProgrammappingTO p:getMappings()) {
            s.add(new Integer(p.getDestwelly()));
        }
        
        return s.size();
    }
       
    public int getDestBlockRownum() {
        Set<Integer> s = new HashSet<Integer>();
        for(ProgrammappingTO p:getMappings()) {
            s.add(new Integer(p.getDestblockrow()));
        }
        
        return s.size();
    }
    
    public int getDestBlockColnum() {
        Set<Integer> s = new HashSet<Integer>();
        for(ProgrammappingTO p:getMappings()) {
            s.add(new Integer(p.getDestblockcol()));
        }
        
        return s.size();
    }
    
    public ProgrammappingTO getMappingByDestpos(int pos) {
        for(ProgrammappingTO mapping:getMappings()) {
            if(mapping.getDestpos()==pos)
                return mapping;
        }
        return null;
    }
    
    public ProgrammappingTO getMappingBySrc(String srcplate, int srcpos) {
        for(ProgrammappingTO mapping:getMappings()) {
            if(mapping.getSrcplate().equals(srcplate) && mapping.getSrcpos()==srcpos) {
                return mapping;
            }
        }
        return null;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSourcenum() {
        return sourcenum;
    }

    public void setSourcenum(int sourcenum) {
        this.sourcenum = sourcenum;
    }

    public int getDestnum() {
        return destnum;
    }

    public void setDestnum(int destnum) {
        this.destnum = destnum;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getResearcher() {
        return researcher;
    }

    public void setResearcher(String researcher) {
        this.researcher = researcher;
    }

    public

    List<ProgrammappingTO> getMappings() {
        return mappings;
    }

    public void setMappings(List<ProgrammappingTO> mappings) {
        this.mappings = mappings;
    }

    public List<FilereferenceTO> getFiles() {
        return files;
    }

    public void setFiles(List<FilereferenceTO> files) {
        this.files = files;
    }

    public List<ProgramcontainerTO> getContainers() {
        return containers;
    }

    public void setContainers(List<ProgramcontainerTO> containers) {
        this.containers = containers;
    }
}
