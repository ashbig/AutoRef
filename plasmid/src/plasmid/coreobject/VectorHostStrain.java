package plasmid.coreobject;

import java.io.*;

public class VectorHostStrain {

    private int vectorid = -1;
    private String vectorname = null;
    private String hoststrain = null;
    private String isinuse = null;
    private String description = null;

    /** Creates a new instance of VectorHostStrain */
    public VectorHostStrain() {
    }

    public VectorHostStrain(int vectorid, String hoststrain, String isinuse, String description) {
        this.hoststrain = hoststrain;
        this.vectorid = vectorid;
        this.isinuse = isinuse;
        this.description = description;
    }

    public VectorHostStrain(int vectorid, String hoststrain, String isinuse, String description, String vectorname) {
        this.hoststrain = hoststrain;
        this.vectorid = vectorid;
        this.vectorname = vectorname;
        this.isinuse = isinuse;
        this.description = description;
    }

    public int getVectorid() {
        return vectorid;
    }

    public String getHoststrain() {
        return hoststrain;
    }

    public String getVectorame() {
        return vectorname;
    }

    public String getDescription() {
        return description;
    }

    public String getIsinuse() {
        return isinuse;
    }

    public void setVectorid(int id) {
        this.vectorid = id;
    }

    public void setHoststrain(String s) {
        this.hoststrain = s;
    }

    public void setIsinuse(String s) {
        this.isinuse = s;
    }

    public void setVectorname(String s) {
        this.vectorname = s;
    }

    public void setDescription(String s) {
        this.description = s;
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
