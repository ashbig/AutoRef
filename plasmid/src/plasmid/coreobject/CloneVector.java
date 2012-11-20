/*
 * CloneVector.java
 *
 * Created on March 31, 2005, 11:36 AM
 */
package plasmid.coreobject;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.sql.Connection;
import plasmid.database.DatabaseManager.VectorManager;
import plasmid.database.DatabaseTransaction;

import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class CloneVector implements Serializable {

    private int vectorid;
    private int cloneid;
    private String name;
    private String description;
    private String form;
    private String type;
    private int size;
    private String mapfilename;
    private String seqfilename;
    private String comments;
    private String status;
    private int userid;
    private String syns;
    private List synonyms;
    private List property;
    private List vectorfeatures;
    private List vectorparents;
    private List publications;
    private List authors;

    /** Creates a new instance of CloneVector */
    public CloneVector() {
    }

    public CloneVector(int vectorid, String name, String description, String form, String type,
            int size, String mapfilename, String seqfilename, String comments) {

        this(vectorid, name, description, form, type,
                size, mapfilename, seqfilename, comments, "", 0);

    }

    public CloneVector(int vectorid, String name, String description, String form, String type,
            int size, String mapfilename, String seqfilename, String comments, String status, int userid) {
        this(vectorid, name, description, form, type,
                size, mapfilename, seqfilename, comments, status, userid, -1);
    }
    
    public CloneVector(int vectorid, String name, String description, String form, String type,
            int size, String mapfilename, String seqfilename, String comments, String status, int userid, int cloneid) {    
        this.vectorid = vectorid;
        this.name = name;
        this.description = description;
        this.form = form;
        this.type = type;
        this.size = size;
        this.mapfilename = mapfilename;
        this.seqfilename = seqfilename;
        this.comments = comments;
        this.status = status;
        this.userid = userid;
        this.cloneid = cloneid;
    }
    
    public String toXML() {
        String s = "<VECTOR>\n";
        s += "<NAME>"+getName()+"</NAME>\n";
        s += "<DESCRIPTION>"+getDescription()+"</DESCRIPTION>\n";
        s += "<SIZEINBP>"+getSize()+"</SIZEINBP>\n";
	s += "</VECTOR>";
        return s;
    }

    public int getVectorid() {
        return vectorid;
    }

    public int getCloneid() {
        return cloneid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getForm() {
        return form;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public String getMapfilename() {
        return mapfilename;
    }

    public String getSeqfilename() {
        return seqfilename;
    }

    public String getStatus() {
        return status;
    }

    public int getUserid() {
        return userid;
    }

    public void setVectorid(int id) {
        this.vectorid = id;
    }

    public void setCloneid(int id) {
        this.cloneid = id;
    }

    public void setName(String s) {
        this.name = s;
    }

    public void setDescription(String s) {
        this.description = s;
    }

    public void setForm(String s) {
        this.form = s;
    }

    public void setType(String s) {
        this.type = s;
    }

    public void setSize(int i) {
        this.size = i;
    }

    public void setMapfilename(String s) {
        this.mapfilename = s;
    }

    public void setSeqfilename(String s) {
        this.seqfilename = s;
    }

    public void setStatus(String s) {
        this.status = s;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
    
    // Regular comments. Within <CMT></CMT>
    public String getTaggedComments() {
        if ((comments == null) || (comments.length() < 1)) {
            return null;
        }
        String bcm = "<CMT>", ecm = "</CMT>", bim = "<IPD>", eim = "</IPD>";
        int bpos = -1, epos = -1;
        String cmt = null;
        bpos = comments.indexOf(bcm);
        if (bpos > -1) {  // Has <CMT></CMT> tag
            epos = comments.indexOf(ecm);
            if (bpos < epos) {
                cmt = comments.substring(bpos + 5, epos);
            }
        } else { // No <CMT></CMT> tag
            bpos = comments.indexOf(bim);
            if (bpos > -1) { // But has <IPD></IPD> tag
                epos = comments.indexOf(eim);
                if (bpos < epos) {  // Remove IPD string.
                    if (bpos == 0) {
                        bpos++;
                    }
                    cmt = comments.substring(0, bpos - 1) + comments.substring(epos + 6);
                }
            } else {  // No <CMT> and <IPD> tag.
                cmt = comments;
            }
        }
        return cmt;
    }
    // Return Full comments contents include <CMT></CMT> and <IPD></IPD>
    public void setTaggedComments(String s) {
        if ((s != null) && s.length() > 0) {
            String bcm = "<CMT>", ecm = "</CMT>", bim = "<IPD>", eim = "</IPD>";

            if ((comments == null) || (comments.length() < 1)) {
                comments = bcm + s + ecm;
            } else {
                int bpos = -1, epos = -1;
                bpos = comments.indexOf(bcm);
                if (bpos > -1) {  // Has <CMT></CMT> tag
                    epos = comments.indexOf(ecm);
                    if (bpos < epos) {
                        if (bpos == 0) {
                            bpos++;
                        }
                        comments = comments.substring(0, bpos - 1) + bcm + s + ecm + comments.substring(epos + 6);
                    }
                } else { // No <CMT></CMT> tag
                    bpos = comments.indexOf(bim);
                    if (bpos > -1) { // But has <IPD></IPD> tag
                        epos = comments.indexOf(eim);
                        if (bpos < epos) {  // Remove IPD string.
                            comments = bcm + s + ecm + comments.substring(bpos, epos + 5);
                        }
                    } else {  // No <CMT> and <IPD> tag.
                        comments = bcm + s + ecm;
                    }
                }
            }
        }
    }

    public String getFullComments() {
        return comments;
    }

    public void setFullComments(String s) {
        this.comments = s;
    }

    public String getComments() {
        return getFullComments();
    }
    public void setComments(String s) {
        setFullComments(s);
    }

    public String getIPD() {
        return getFullComments();
    }
    public void setIPD(String s) {
        setFullComments(s);
    }
    
    // Return IPD. Within <IPD></IPD>
    public String getTaggedIPD() {
        if ((comments == null) || (comments.length() < 1)) {
            return null;
        }
        String bcm = "<CMT>", ecm = "</CMT>", bim = "<IPD>", eim = "</IPD>";
        int bpos = -1, epos = -1;
        String cmt = null;
        bpos = comments.indexOf(bim);
        if (bpos > -1) {  // Has <IPD></IPD> tag
            epos = comments.indexOf(eim);
            if (bpos < epos) {
                cmt = comments.substring(bpos + 5, epos);
            }
        } else { // No <IPD></IPD> tag
            bpos = comments.indexOf(bcm);
            if (bpos > -1) { // But has <CMT></CMT> tag
                epos = comments.indexOf(ecm);
                if (bpos < epos) {  // Remove IPD string.
                    if (bpos == 0) {
                        bpos++;
                    }
                    cmt = comments.substring(0, bpos - 1) + comments.substring(epos + 6);
                }
            } else {  // No <CMT> and <IPD> tag.
                cmt = null;
            }
        }
        return cmt;
    }

    public void setTaggedIPD(String s) {
        if ((s != null) && s.length() > 0) {
            String bcm = "<CMT>", ecm = "</CMT>", bim = "<IPD>", eim = "</IPD>";
            if ((comments == null) || (comments.length() < 1)) {
                comments = bim + s + eim;
            } else {
                int bpos = -1, epos = -1;
                bpos = comments.indexOf(bim);
                if (bpos > -1) {  // Has <IPD></IPD> tag
                    epos = comments.indexOf(eim);
                    if (bpos < epos) {
                        if (bpos == 0) {
                            bpos++;
                        }
                        comments = comments.substring(0, bpos - 1) + bim + s + eim + comments.substring(epos + 6);
                    }
                } else { // No <IPD></IPD> tag
                    bpos = comments.indexOf(bcm);
                    if (bpos > -1) { // But has <CMT></CMT> tag
                        epos = comments.indexOf(ecm);
                        if (bpos < epos) {  // Remove IPD string.
                            comments = bim + s + eim + comments.substring(bpos, epos + 5);
                        }
                    } else {  // No <CMT> and <IPD> tag.
                        comments = bim + s + eim;
                    }
                }
            }
        }
    }

    public List getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List l) {
        this.synonyms = l;
        StringConvertor sc = new StringConvertor();
        this.syns = sc.convertFromListToString(l);
    }

    public List getProperty() {
        return property;
    }

    public void setProperty(List l) {
        this.property = l;
    }

    public List getVectorfeatures() {
        return vectorfeatures;
    }

    public void setVectorfeatures(List l) {
        this.vectorfeatures = l;
    }

    public List getVectorparents() {
        return vectorparents;
    }

    public void setVectorparents(List l) {
        this.vectorparents = l;
    }

    public List getPublications() {
        return publications;
    }

    public void setPublications(List l) {
        this.publications = l;
    }

    public List getAuthors() {
        return authors;
    }

    public void setAuthors(List l) {
        this.authors = l;
    }

    public void setSyns(String s) {
        this.syns = s;
        StringConvertor sc = new StringConvertor();
        synonyms = sc.convertFromStringToList(s, ",");
    }

    public String getSyns() {
        return getSynonymString();
    }

    public String getSynonymString() {
        StringConvertor sc = new StringConvertor();
        return sc.convertFromListToString(synonyms);
    }

    public String getPropertyString() {
        StringConvertor sc = new StringConvertor();
        return sc.convertFromListToString(property);
    }
    
    public String getVectorMapXML() {
        String s = "<VECTORMAP>\n";
        s += this.toXML()+"\n";
        for(int i=0; i<getVectorfeatures().size(); i++) {
            VectorFeature f = (VectorFeature)getVectorfeatures().get(i);
            s += f.toXML()+"\n";
        }
        s += "</VECTORMAP>\n";
        return s;
    }

    public static void main(String args[]) {
        DatabaseTransaction dt = null;
        Connection conn = null;
        try {
            dt = DatabaseTransaction.getInstance();
            conn = dt.requestConnection();
            VectorManager manager = new VectorManager(conn);
            CloneVector v = manager.queryCloneVector(24);
            if(v == null) {
                throw new Exception("Cannot get vector.");
            }
            System.out.println(v.getVectorMapXML());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        /**
        CloneVector v = new CloneVector();
        List l = new ArrayList();
        l.add("abc");
        l.add("def");
        l.add("mnh");
        v.setSynonyms(l);
        System.out.println(v.getSynonymString());
         * */
        System.exit(0);
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
