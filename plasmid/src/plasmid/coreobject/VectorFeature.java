/*
 * VectorFeature.java
 *
 * Created on March 31, 2005, 3:10 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class VectorFeature {
    private int featureid;
    private String name;
    private String description;
    private int start;
    private int stop;
    private int vectorid;
    private String maptype;
    
    /** Creates a new instance of VectorFeature */
    public VectorFeature() {
    }
    
    public VectorFeature(int id, String name, String description, int start,
        int stop, int vectorid, String maptype) {
            this.featureid = id;
            this.name = name;
            this.description = description;
            this.start = start;
            this.stop = stop;
            this.vectorid = vectorid;
            this.maptype = maptype;
    }
    
    public int getFeatureid() {return featureid;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public int getStart() {return start;}
    public int getStop() {return stop;}
    public int getVectorid() {return vectorid;}
    public String getMaptype() {return maptype;}
    
    public void setFeatureid(int id) {this.featureid = id;}
    public void setName(String name) {this.name = name;}
    public void setDescription(String s) {this.description = s;}
    public void setStart(int i) {this.start = i;}
    public void setStop(int i) {this.stop = i;}
    public void setMaptype(String s) {this.maptype=s;}
    public void setVectorid(int vectorid) {this.vectorid = vectorid;}
    
    public String toXML() {
        String s = "<VECTORFEATURE>\n";
        s += "<MAPTYPE>"+getMaptype()+"</MAPTYPE>\n";
        s += "<NAME>"+getName()+"</NAME>\n";
        s += "<DESCRIPTION>"+getDescription()+"</DESCRIPTION>\n";
        s += "<STARTPOS>"+getStart()+"</STARTPOS>\n";
        s += "<ENDPOS>"+getStop()+"</ENDPOS>\n";
	s += "</VECTORFEATURE>";
        return s;
    }
}
