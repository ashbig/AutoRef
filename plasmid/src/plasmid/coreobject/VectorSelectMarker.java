package plasmid.coreobject;

import java.io.*;

public class VectorSelectMarker {

    private int vectorid = -1;
    private String vectorname = null;
    private String hosttype = null;
    private String marker = null;

    /** Creates a new instance of VectorSelectMarker */
    public VectorSelectMarker() {
    }

    public VectorSelectMarker(int vectorid, String hosttype, String marker) {
        this.hosttype = hosttype;
        this.vectorid = vectorid;
        this.marker = marker;
    }
    
    public VectorSelectMarker(int vectorid, String vectorname, String hosttype, String marker) {
        this.hosttype = hosttype;
        this.vectorid = vectorid;
        this.vectorname = vectorname;
        this.marker = marker;
    }

    public int getVectorid() {
        return vectorid;
    }

    public String getVectorname() {
        return vectorname;
    }
    
    public String getHosttype() {
        return hosttype;
    }

    public String getMarker() {
        return marker;
    }

    public void setVectorid(int id) {
        this.vectorid = id;
    }
    
    public void setVectorname(String s) {
        this.vectorname = s;
    }

    public void setHosttype(String s) {
        this.hosttype = s;
    }

    public void setMarker(String s) {
        this.marker = s;
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
