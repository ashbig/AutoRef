package plasmid.coreobject;

import java.io.*;

public class VectorGrowthCondition {

    private int vectorid = -1;
    private int growthid = -1;
    private String vectorname = null;
    private String growthname = null;
    private String isrecommended = null;

    /** Creates a new instance of VectorGrowthCondition */
    public VectorGrowthCondition() {
    }

    public VectorGrowthCondition(int vectorid, int growthid, String growthname, String isrecommended, String vectorname) {
        this.growthid = growthid;
        this.growthname = growthname;
        this.vectorid = vectorid;
        this.vectorname = vectorname;
        this.isrecommended = isrecommended;
    }

    public VectorGrowthCondition(int vectorid, int growthid, String growthname, String isrecommended) {
        this.growthid = growthid;
        this.growthname = growthname;
        this.vectorid = vectorid;
        this.isrecommended = isrecommended;
    }

    public VectorGrowthCondition(int vectorid, int growthid, String isrecommended) {
        this.growthid = growthid;
        this.vectorid = vectorid;
        this.isrecommended = isrecommended;
    }

    public int getGrowthid() {
        return growthid;
    }

    public int getVectorid() {
        return vectorid;
    }

    public String getGrowthname() {
        return growthname;
    }
    public String getVectorame() {
        return vectorname;
    }
    
    public String getIsrecommended() {
        return isrecommended;
    }

    public void setVectorid(int id) {
        this.vectorid = id;
    }

    public void setGrowthid(int id) {
        this.growthid = id;
    }

    public void setGrowthame(String s) {
        this.growthname = s;
    }

    public void setIsrecommended(String s) {
        this.isrecommended = s;
    }

    public void setVectorname(String s) {
        this.vectorname = s;
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
