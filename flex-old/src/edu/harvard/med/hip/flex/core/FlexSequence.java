/**
 * $Id: FlexSequence.java,v 1.2 2001-07-09 16:00:56 jmunoz Exp $
 *
 * File     : FlexSequence.java
 * Date     : 05022001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.core;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.user.*;
import java.util.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.sql.*;
import sun.jdbc.rowset.*;

/**
 * This class represents a flex sequence corresponding to Flexsequence table.
 */
public class FlexSequence extends CDNASequence {
    
    protected int id = -1;
    protected String flexstatus;
    protected String species;
    protected String dateadded;    
    protected String quality;
    
    protected Set requestingUserSet;
    
    // This field stores the following information as a Vector of Hashtables.
    // The Hashtable contains the following info as key/value pairs:
    // "NAMETYPE" => value of nametype	ex: "NAMETYPE"=>"GI"
    // "NAMEVALUE" => value of namevalue ex: "NAMEVALUE"=>genbank accession number
    // "NAMEURL" => url (optional)
    // "DESCRIPTION"=>description (optional)
    protected Vector publicInfo = new Vector();
    
    /**
     * Constructor.
     *
     * @param id The sequence id.
     * @return A FlexSequence object.
     * @exception FlexDatabaseException.
     */
    public FlexSequence(int id) {
        this.id = id;
        this.requestingUserSet = new HashSet();
        try {
            restore(id);
        } catch (FlexDatabaseException fde) {
            System.err.println(fde.getMessage());
        }
    }
    
    /**
     * Constructor. It will compare the GI number in publicInfo field with
     * name table. If the GI number is found in name table, this sequence
     * is considered to be the same as the one in database, and the information
     * of this sequence will be pulled out from the database. If the GI number
     * is not found in the name table, it will be considered as a new sequence.
     *
     * @param publicInfo The Vector containing public information.
     * @return A FlexSequence object.
     * @exception FlexCoreException, FlexDatabaseException.
     */
    public FlexSequence(Vector publicInfo) throws FlexCoreException, FlexDatabaseException {
        this.publicInfo = publicInfo;
        this.requestingUserSet = new HashSet();
        String namevalue=null;
        Enumeration enum = publicInfo.elements();
        while(enum.hasMoreElements()) {
            Hashtable h = (Hashtable)enum.nextElement();
            if(((String)h.get("NAMETYPE")).equals("GI")) {
                namevalue = (String)h.get("NAMEVALUE");
                break;
            }
        }
        
        
        if(namevalue == null)
            throw new FlexCoreException("GI number cannot be null.");
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        
        String sql = "select sequenceid, nametype, namevalue\n"+
        "from name\n"+
        "where nametype='GI'\n"+
        "and namevalue='"+namevalue+"'";
        CachedRowSet crs = t.executeQuery(sql);
        
        
        if(crs.size() == 0) {
            flexstatus = "NEW";
            quality = "GOOD";
        } else {
            try {
                while(crs.next()) {
                    id = crs.getInt("SEQUENCEID");
                    break;
                }
            } catch (SQLException sqlE) {
                throw new FlexDatabaseException("Error occured while initializiing FlexSequence object with public info\n"+sqlE+"\nSQL: "+sql);
            } finally {
                DatabaseTransaction.closeResultSet(crs);
            }
            restore(id);
        }
    }
    
    /**
     * Constructor. Instantiated when restoring the data from the database.
     *
     * @param id The sequence id.
     * @param flexstatus The sequence status.
     * @param species The species that sequence comes from.
     * @param dateadded The date when the sequence is added to the database.
     * @param sequencetext The text string of the sequence.
     * @param cdsstart The cds start of the sequence. -1 is considered to be no start.
     * @param cdsstop The cds stop of the sequence. -1 is considered to be no stop.
     * @param cdslength The cdslength of the sequence. -1 is considered to be no start or stop.
     * @param gccontent The gc content of the sequence.
     * @param publicInfo The public information stored as a Vector of Hashtables.
     * @return The FlexSequence object.
     */
    public FlexSequence(int id, String flexstatus, String species, String dateadded, String sequencetext, int cdsstart, int cdsstop, int cdslength, int gccontent, Vector publicInfo) {
        super(cdsstart, cdsstop, sequencetext, cdslength, gccontent);
        
        this.id = id;
        this.requestingUserSet = new HashSet();
        this.flexstatus = flexstatus;
        this.species = species;
        this.dateadded = dateadded;
        this.publicInfo = publicInfo;
        this.quality = calculateQuality(flexstatus);
    }
    
    /**
     * Set the species to the given value.
     *
     * @value The given value.
     */
    public void setSpecies(String value) {
        this.species = value;
    }
    
    /**
     * Set the quality to the given value.
     *
     * @value The given value.
     */
    public void setQuality(String value){
        this.quality = value;
    }
    
    /**
     * Set the id value.
     *
     * @param The id value.
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Set the flexstatus field to the given value.
     *
     * @param status The value to be set to.
     */
    public void setFlexstatus(String status) {
        flexstatus = status;
    }
    
    /**
     * updates the status of this sequence in the database
     *
     * @param status The status to set to.
     * @param conn The <code>Connection</code> to use to preform the update
     */
    public void updateStatus(String status, Connection conn)
    throws FlexDatabaseException {
        Statement stmt =null;
        try {
            String sql = "UPDATE FLEXSEQUENCE SET FLEXSTATUS = '"
                        + status +"' WHERE SEQUENCEID = "+id;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while calling updateStatus method for sequence "+id+"\n"+sqlE);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    /**
     * Get the id value.
     *
     * @return The id value.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Get the flexstatus value.
     *
     * @return The flexstatus value.
     */
    public String getFlexstatus() {
        return flexstatus;
    }
    
    /**
     * Get the species value.
     *
     * @return The species value.
     */
    public String getSpecies() {
        return species;
    }
    
    /**
     * Get the quality value.
     *
     * @return The quality value.
     */
    public String getQuality() {
        return quality;
    }
    
    /**
     * Get the dateadded value.
     *
     * @return The dateadded value.
     */
    public String getDateadded() {
        return dateadded;
    }
    
    /**
     * Convert the sequence gi number to the header of FASTA format.
     *
     * @return A String representing the FASTA header.
     */
    public String getFastaHeader() {
        return new String(">"+this.getId());
    }
        
    /**
     * Get the publicInfo value.
     *
     * @return The publicInfo value.
     */
    public Vector getPublicInfo() {
        return publicInfo;
    }
    
    /**
     * Return Genbank accession number.
     *
     * @return The genbank accesion number.
     */
    public String getAccession() {
        String namevalue=null;
        Enumeration enum = publicInfo.elements();
        while(enum.hasMoreElements()) {
            Hashtable h = (Hashtable)enum.nextElement();
            if(((String)h.get("NAMETYPE")).equals("GENBANK_ACCESSION")) {
                namevalue = (String)h.get("NAMEVALUE");
                break;
            }
        }
        return namevalue;
    }
    
    /**
     * Gets the users who are requesting that this sequence
     *
     * @return Set set of users requesting this sequence.
     */
    public Set getRequestingUsers() {
        
        return requestingUserSet;
    }
    
    /**
     * Return Genbank gi number.
     *
     * @return The genbank gi number.
     */
    public String getGi() {
        String namevalue=null;
        Enumeration enum = publicInfo.elements();
        while(enum.hasMoreElements()) {
            Hashtable h = (Hashtable)enum.nextElement();
            if(((String)h.get("NAMETYPE")).equals("GI")) {
                namevalue = (String)h.get("NAMEVALUE");
                break;
            }
        }
        return namevalue;
    }
    
    /**
     * Return sequence description.
     *
     * @return The sequence description.
     */
    public String getDescription() {
        String description = null;
        Enumeration enum = publicInfo.elements();
        while(enum.hasMoreElements()) {
            Hashtable h = (Hashtable)enum.nextElement();
            
            if(((String)h.get("NAMETYPE")).equals("GENBANK_ACCESSION")) {
                description = (String)h.get("DESCRIPTION");
                if (description != null)
                    return description;
            }
            
            if(((String)h.get("NAMETYPE")).equals("GI")) {
                description = (String)h.get("DESCRIPTION");
                if (description != null)
                    return description;
            }
            
            if(((String)h.get("NAMETYPE")).equals("UNIGENE_SID")) {
                description = (String)h.get("DESCRIPTION");
                if (description != null)
                    return description;
            }
        }
        return description;
    }

    /**
     * Restore the data from database by sequence id.
     *
     * @param id The sequence id.
     * @exception FlexDatabaseException.
     */
    public void restore(int id) throws FlexDatabaseException {
        String sql = "select s.flexstatus as status,"+
        "s.genusspecies as species,"+
        "s.cdsstart as cdsstart,"+
        "s.cdsstop as cdsstop,"+
        "s.cdslength as cdslength,"+
        "to_char(s.dateadded, 'fmYYYY-MM-DD') as dateadded\n"+
        "from flexsequence s\n"+
        "where s.sequenceid="+id;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        RowSet rs = t.executeQuery(sql);
        try {
            while(rs.next()) {
                flexstatus = rs.getString("STATUS");
                species = rs.getString("SPECIES");
                dateadded = rs.getString("DATEADDED");
                cdsstart = rs.getInt("CDSSTART");
                cdsstop = rs.getInt("CDSSTOP");
                cdslength = rs.getInt("CDSLENGTH");
            }
            
            // public info stuff
            sql = "select * from name where sequenceid="+id;
            
            rs = t.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            publicInfo = new Vector();
            while(rs.next()) {
                Hashtable ht = new Hashtable(cols);

                for(int i = 1 ;i <= cols; i++) {
                    Object o = rs.getObject(i);
                    if(o != null)
                        ht.put(meta.getColumnLabel(i),o);
                }
                publicInfo.addElement(ht);
            }
            
            sequencetext = "";
            sql = "select * from sequencetext where sequenceid="+id+" order by sequenceorder";
            rs = t.executeQuery(sql);
            
            
            while(rs.next()) {
                sequencetext = sequencetext+rs.getString("SEQUENCETEXT");
            }
            this.quality = calculateQuality(flexstatus);
            
            // clear out the old requesting users name
            requestingUserSet.clear();
            // get the values from the database
            sql = "select userprofile.username, userprofile.useremail, "+
            "userprofile.usergroup \n" +
            "from userprofile, request, requestsequence\n" +
            "where requestsequence.requestid = request.requestid AND\n"+
            "request.username = userprofile.username AND\n" +
            "requestsequence.sequenceid = " + this.id;
            rs = t.executeQuery(sql);
            
            while(rs.next()) {
                
                User curUser = new User(rs.getString("USERNAME"),
                rs.getString("USEREMAIL"),
                rs.getString("USERGROUP"));
                requestingUserSet.add(curUser);
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while restoring sequence with id "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    /**
     * Insert the sequence record into flexsequence table, name table and
     * sequence text table.
     *
     * @param conn The <code>Connection</code> to preform the insert with
     * @exception FlexDatabaseException.
     */
    public void insert(Connection conn) throws FlexDatabaseException {
        if(id == -1) {
            id = FlexIDGenerator.getID("sequenceid");
            
            //if flex status is "NEW", we need to change it to "PENDING".
            if(flexstatus.equals("NEW"))
                flexstatus = "PENDING";
            
            //if the species is not found in species table, we need to insert it.            
            //String sql = "select * from species where genusspecies='"+species+"'";
            //RowSet speciesRs = null;
            //try {
                //speciesRs =
                //DatabaseTransaction.executeQuery(conn.prepareStatement(sql));
                
                //if(!speciesRs.next()) {
                //    sql = "insert into species(genusspecies) values('"+species+"')";
                //    DatabaseTransaction.executeUpdate(sql,conn);
                //}
                
                //insert into flexsequence table.
                String sql = 	"insert into flexsequence\n" +
                "(sequenceid, flexstatus, genusspecies, dateadded,"+
                "cdsstart, cdsstop, cdslength, gccontent)\n"+
                "values ("+id+", '"+flexstatus+"', '"+species+"',sysdate,"+
                cdsstart+","+cdsstop+","+cdslength+","+getGccontent()+")";
                
                DatabaseTransaction.executeUpdate(sql,conn);
                
                //insert into sequencetext table.
                if(sequencetext != null) {
                    int i=1;
                    while(sequencetext.length()>4000) {
                        String text = sequencetext.substring(4000*(i-1), 4000*i);
                        sql = "insert into sequencetext(sequenceid, sequenceorder, sequencetext)\n"+
                        "values("+id+","+i+",'"+text+"')";;
                        DatabaseTransaction.executeUpdate(sql,conn);
                        
                        
                        i++;
                    }
                    String text = sequencetext.substring(4000*(i-1));
                    sql = "insert into sequencetext(sequenceid, sequenceorder, sequencetext)\n"+
                    "values("+id+","+i+",'"+text+"')";;
                    DatabaseTransaction.executeUpdate(sql,conn);
                }
                
                //insert into name table.
                Enumeration enum = publicInfo.elements();
                while(enum.hasMoreElements()) {
                    Hashtable h = (Hashtable)enum.nextElement();
                    sql = "insert into name (sequenceid, nametype, namevalue";
                    String appSql = "values("+id+",'"+(String)h.get("NAMETYPE")+"','"+(String)h.get("NAMEVALUE")+"'";
                    
                    if(h.get("NAMEURL")!= null) {
                        sql = sql+",nameurl";
                        appSql = appSql+",'"+(String)h.get("NAMEURL")+"'";
                    }
                    
                    if(h.get("DESCRIPTION")!= null) {
                        sql = sql+",description";
                        appSql = appSql+",'"+(String)h.get("DESCRIPTION")+"'";
                    }
                    
                    sql = sql+")\n"+appSql+")";
                    DatabaseTransaction.executeUpdate(sql,conn);
                }
                //		}else {
                //			insertPublicInfo(t);
            //} catch(SQLException sqlE) {
                
            //    throw new FlexDatabaseException(sqlE);
            //} finally {
             //   DatabaseTransaction.closeResultSet(speciesRs);
            //}
        }
    }
    
    /**
     * Insert into name table.
     *
     * @param conn The <code>Connection</code> to preform the insert with.
     * @exception FlexDatabaseException.
     */
    public void insertPublicInfo(Connection conn)
    throws FlexDatabaseException {
        
        String sql = "insert into name (sequenceid, nametype, namevalue)\n"+
        "values("+id+",'GI','"+this.getGi()+"')";
        DatabaseTransaction.executeUpdate(sql,conn);
    }
    //******************************************************************//
    //				Private Methods                 	//
    //******************************************************************//
    
    // Calculate the sequence quality based on flex status.
    private String calculateQuality(String status) {
        String quality;
        
        if(status.equals("REJECTED")) {
            quality = "QUESTIONABLE";
        } else {
            quality = "GOOD";
        }
        return quality;
    }
    
    //******************************************************************//
    //			Test                                            //								//
    //******************************************************************//
    public static void main(String [] args) throws Exception {
        Vector v = new Vector();
        Hashtable h = new Hashtable();
        h.put("NAMETYPE", "GENBANK_ACCESSION");
        h.put("NAMEVALUE", "AB0000");
        h.put("DESCRIPTION", "test1");
        v.addElement(h);
        
        h = new Hashtable();
        h.put("NAMETYPE", "GI");
        h.put("NAMEVALUE", "CD1234");
        h.put("DESCRIPTION", "test2");
        v.addElement(h);
        
        RowSet sequenceRs = null;
        RowSet textRs = null;
        RowSet nameRs = null;
        
        try {
            System.out.println("New sequence constructed with publicInfo:");
            FlexSequence seq = new FlexSequence(v);
            System.out.println("ID:\t"+seq.getId());
            System.out.println("Flex Status:\t"+seq.getFlexstatus());
            System.out.println("Species:\t"+seq.getSpecies());
            System.out.println("Sequence Text:\t"+seq.getSequencetext());
            System.out.println("Sequence quality:\t"+seq.getQuality());
            System.out.println("Date added:\t"+seq.getDateadded());
            System.out.println("GC content:\t"+seq.getGccontent());
            System.out.println("CDS start:\t"+seq.getCdsstart());
            System.out.println("CDS stop:\t"+seq.getCdsstop());
            System.out.println("CDS length:\t"+seq.getCdslength());
            Vector info = seq.getPublicInfo();
            Enumeration enum = info.elements();
            while(enum.hasMoreElements()) {
                System.out.println(enum.nextElement());
            }
            System.out.println();
            
            System.out.println("Calling the set methods:");
            seq.setSpecies("Homo sapiens");
            seq.setSequencetext("AATCGGAATTTTTCCCGGG");
            seq.setCdsstart(4);
            seq.setCdsstop(19);
            seq.setCdslength(15);
            seq.setQuality("QUESTIONABLE");
            
            System.out.println("ID:\t"+seq.getId());
            System.out.println("Flex Status:\t"+seq.getFlexstatus());
            System.out.println("Species:\t"+seq.getSpecies());
            System.out.println("Sequence Text:\t"+seq.getSequencetext());
            System.out.println("Sequence quality:\t"+seq.getQuality());
            System.out.println("Date added:\t"+seq.getDateadded());
            System.out.println("GC content:\t"+seq.getGccontent());
            System.out.println("CDS start:\t"+seq.getCdsstart());
            System.out.println("CDS stop:\t"+seq.getCdsstop());
            System.out.println("CDS length:\t"+seq.getCdslength());
            info = seq.getPublicInfo();
            enum = info.elements();
            while(enum.hasMoreElements()) {
                System.out.println(enum.nextElement());
            }
            System.out.println();
            
            System.out.println("Insert into database:");
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            Connection conn = t.requestConnection();
            seq.insert(conn);
            
            Vector v3 = new Vector();
            Hashtable h3 = new Hashtable();
            h3.put("NAMETYPE", "GENBANK_ACCESSION");
            h3.put("NAMEVALUE", "AB0000");
            h3.put("DESCRIPTION", "test1");
            v3.addElement(h);
            
            h3 = new Hashtable();
            h3.put("NAMETYPE", "GI");
            h3.put("NAMEVALUE", "CD5678");
            h3.put("DESCRIPTION", "test2");
            v3.addElement(h);
            FlexSequence s = new FlexSequence(v3);
            s.setId(1);
            System.out.println("Insert into name table...");
            //s.insertPublicInfo(conn);
            
            sequenceRs = t.executeQuery("select * from flexsequence where sequenceid="+seq.getId());
            System.out.println(sequenceRs);
            nameRs = t.executeQuery("select * from name where sequenceid="+seq.getId());
            System.out.println(nameRs);
            textRs = t.executeQuery("select * from sequencetext where sequenceid="+seq.getId());
            System.out.println(textRs);
            System.out.println();
            
            System.out.println("New sequence with the same GI number:");
            FlexSequence seq1 = new FlexSequence(v);
            System.out.println("ID:\t"+seq1.getId());
            System.out.println("Flex Status:\t"+seq1.getFlexstatus());
            System.out.println("Species:\t"+seq1.getSpecies());
            System.out.println("seq1uence Text:\t"+seq1.getSequencetext());
            System.out.println("Sequence quality:\t"+seq1.getQuality());
            System.out.println("Date added:\t"+seq1.getDateadded());
            System.out.println("GC content:\t"+seq1.getGccontent());
            System.out.println("CDS start:\t"+seq1.getCdsstart());
            System.out.println("CDS stop:\t"+seq1.getCdsstop());
            System.out.println("CDS length:\t"+seq1.getCdslength());
            info = seq1.getPublicInfo();
            enum = info.elements();
            while(enum.hasMoreElements()) {
                System.out.println(enum.nextElement());
            }
            System.out.println();
            
            System.out.println("New sequence with all information provided:");
            int seqID = FlexIDGenerator.getID("sequenceid");
            FlexSequence seq3 = new FlexSequence(seqID, "REJECTED", "Test Species", "2001-12-12", "ATCG", 1, 3, 2, 2, v);
            System.out.println("ID:\t"+seq3.getId());
            System.out.println("Flex Status:\t"+seq3.getFlexstatus());
            System.out.println("Species:\t"+seq3.getSpecies());
            System.out.println("Sequence Text:\t"+seq3.getSequencetext());
            System.out.println("Sequence quality:\t"+seq3.getQuality());
            System.out.println("Date added:\t"+seq3.getDateadded());
            System.out.println("GC content:\t"+seq3.getGccontent());
            System.out.println("CDS start:\t"+seq3.getCdsstart());
            System.out.println("CDS stop:\t"+seq3.getCdsstop());
            System.out.println("CDS length:\t"+seq3.getCdslength());
            Vector info3 = seq3.getPublicInfo();
            enum = info3.elements();
            while(enum.hasMoreElements()) {
                System.out.println(enum.nextElement());
            }
            
            seq1 = new FlexSequence(1);
            System.out.println("ID:\t"+seq1.getId());
            System.out.println("Flex Status:\t"+seq1.getFlexstatus());
            System.out.println("Species:\t"+seq1.getSpecies());
            System.out.println("seq1uence Text:\t"+seq1.getSequencetext());
            System.out.println("Sequence quality:\t"+seq1.getQuality());
            System.out.println("Date added:\t"+seq1.getDateadded());
            System.out.println("GC content:\t"+seq1.getGccontent());
            System.out.println("CDS start:\t"+seq1.getCdsstart());
            System.out.println("CDS stop:\t"+seq1.getCdsstop());
            System.out.println("CDS length:\t"+seq1.getCdslength());
            System.out.println("Description:\t"+seq1.getDescription());
            info = seq1.getPublicInfo();
            enum = info.elements();
            while(enum.hasMoreElements()) {
                System.out.println(enum.nextElement());
            }
            System.out.println();
            
        } catch (FlexDatabaseException e) {
            System.out.println(e);
        } catch(FlexCoreException e){
            System.out.println(e);
        } finally {
            DatabaseTransaction.closeResultSet(sequenceRs);
            DatabaseTransaction.closeResultSet(textRs);
            DatabaseTransaction.closeResultSet(nameRs);
            System.exit(0);
        }
    }
    
    
    
}

