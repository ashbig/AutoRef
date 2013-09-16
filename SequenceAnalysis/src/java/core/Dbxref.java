/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author Lab User
 */
public class Dbxref {
    private int geneid;
    private String dbname;
    private String dbid;

    /**
     * @return the geneid
     */
    public int getGeneid() {
        return geneid;
    }

    /**
     * @param geneid the geneid to set
     */
    public void setGeneid(int geneid) {
        this.geneid = geneid;
    }

    /**
     * @return the dbname
     */
    public String getDbname() {
        return dbname;
    }

    /**
     * @param dbname the dbname to set
     */
    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    /**
     * @return the dbid
     */
    public String getDbid() {
        return dbid;
    }

    /**
     * @param dbid the dbid to set
     */
    public void setDbid(String dbid) {
        this.dbid = dbid;
    }
}
