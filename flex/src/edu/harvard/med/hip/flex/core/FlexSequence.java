/**
 * $Id: FlexSequence.java,v 1.4 2003-10-17 13:08:38 dzuo Exp $
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
    public static final String OBTAINED = "SEQUENCE VERIFIED";
    public static final String FAILED = "FAILED";
    public static final String INPROCESS = "INPROCESS";
    public static final String REJECTED = "REJECTED";
    public static final String PENDING = "PENDING";
    public static final String NEW = "No FLEXGene Match";
    public static final String GOOD = "GOOD";
    public static final String QUESTIONABLE = "QUESTIONABLE";
    
    public static final String NAMETYPE = "NAMETYPE";
    public static final String NAMEVALUE = "NAMEVALUE";
    public static final String NAMEURL = "NAMEURL";
    public static final String DESCRIPTION = "DESCRIPTION";
    
    public static final String GI = "GI";
    public static final String LOCUS_ID = "LOCUS_ID";
    public static final String GENBANK_ACCESSION = "GENBANK_ACCESSION";
    public static final String UNIGENE_SID = "UNIGENE_SID";
    public static final String GENE_SYMBOL = "GENE_SYMBOL";
    public static final String PANUMBER = "PANUMBER";
    
    public static final String SGD  = "SGD";
    public static final String ALT_GENE_NAME    = "ALT_GENE_NAME";
    public static final String ALT_PROTEIN_NAME = "ALT_PROTEIN_NAME";
    public static final String GENE_NAME    = "GENE_NAME";
    public static final String MGC_ID     = "MGC_ID";
    public static final String IMAGE_ID = "IMAGE_ID";
    
    public static final String HUMAN = "Homo sapiens";
    public static final String YEAST = "Saccharomyces cerevisiae";
    public static final String PSEUDOMONAS = "Pseudomonas aeruginosa";
    public static final String MOUSE = "Mus musculus";
    
    protected int id = -1;
    protected String flexstatus;
    protected String species;
    protected String dateadded;
    protected String quality;
    protected String currentAccession = null;
    protected String currentGi = null;
    protected String currentLocusLink = null;
    protected String currentDescription = null;
    protected String cdnasource = null;
    protected String chromosome = null;
    
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
            System.out.println(fde.getMessage());
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
            if(((String)h.get(NAMETYPE)).equals(GI)) {
                namevalue = (String)h.get(NAMEVALUE);
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
            flexstatus = NEW;
            quality = GOOD;
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
     * @param cdnasource The cdnasource field.
     * @param chromosome The chromosome field.
     * @return The FlexSequence object.
     */
    public FlexSequence(int id, String flexstatus, String species, String dateadded, String sequencetext, int cdsstart, int cdsstop, int cdslength, int gccontent, Vector publicInfo, String cdnasource, String chromosome) {
        super(cdsstart, cdsstop, sequencetext, cdslength, gccontent);
        
        this.id = id;
        this.requestingUserSet = new HashSet();
        this.flexstatus = flexstatus;
        this.species = species;
        this.dateadded = dateadded;
        this.publicInfo = publicInfo;
        this.quality = calculateQuality(flexstatus);
        this.cdnasource = cdnasource;
        this.chromosome = chromosome;
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
    public void setQuality(String value) {
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
     * updates the status of this sequence in the database
     *
     * @param status The status to set to.
     * @param conn The <code>Connection</code> to use to preform the update
     */
    public void updateProperty(String propertyName, String propertyValue, Connection conn)
    throws FlexDatabaseException {
        Statement stmt =null;
        String sql = null;
        try {
            if (propertyName.equalsIgnoreCase( NAMETYPE) ||
            propertyName.equalsIgnoreCase( NAMEVALUE ) ||
            propertyName.equalsIgnoreCase( NAMEURL)||
            propertyName.equalsIgnoreCase( DESCRIPTION) || propertyName.equalsIgnoreCase( GI) ||
            propertyName.equalsIgnoreCase( LOCUS_ID) || propertyName.equalsIgnoreCase( GENBANK_ACCESSION )||
            propertyName.equalsIgnoreCase(  UNIGENE_SID)||propertyName.equalsIgnoreCase(  GENE_SYMBOL) ||
            propertyName.equalsIgnoreCase( PANUMBER)) {
                sql = "UPDATE NAME SET namevalue=" + propertyValue +
                " WHERE SEQUENCEID = "+id +" AND NAMETYPE = '" + propertyName +"'";
            }
            else
                sql = "UPDATE FLEXSEQUENCE SET " + propertyName +" = '"
                + propertyValue +"' WHERE SEQUENCEID = "+id;
            
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
        if(currentAccession != null)
            return currentAccession;
        
        String namevalue=null;
        Enumeration enum = publicInfo.elements();
        while(enum.hasMoreElements()) {
            Hashtable h = (Hashtable)enum.nextElement();
            if(((String)h.get(NAMETYPE)).equals(GENBANK_ACCESSION)) {
                namevalue = (String)h.get(NAMEVALUE);
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
        if(currentGi != null)
            return currentGi;
        
        String namevalue=null;
        Enumeration enum = publicInfo.elements();
        while(enum.hasMoreElements()) {
            Hashtable h = (Hashtable)enum.nextElement();
            if(((String)h.get(NAMETYPE)).equals(GI)) {
                namevalue = (String)h.get(NAMEVALUE);
                break;
            }
        }
        return namevalue;
    }
    /**
     * Return Genbank gi number.
     *
     * @return The genbank gi number.
     */
    public String getLocusLinkId() {
        if(currentLocusLink != null)
            return currentLocusLink;
        
        String namevalue=null;
        Enumeration enum = publicInfo.elements();
        while(enum.hasMoreElements()) {
            Hashtable h = (Hashtable)enum.nextElement();
            if(((String)h.get(NAMETYPE)).equals(LOCUS_ID)) {
                namevalue = (String)h.get(NAMEVALUE);
                break;
            }
        }
        return namevalue;
    }
    /**
     * Return all the gi numbers as a string.
     *
     * @return All the gi numbers as a string.
     */
    public String getGiString() {
        String namevalue=null;
        
        Enumeration enum = publicInfo.elements();
        while(enum.hasMoreElements()) {
            Hashtable h = (Hashtable)enum.nextElement();
            if(((String)h.get(NAMETYPE)).equals(GI)) {
                if(namevalue == null) {
                    namevalue = (String)h.get(NAMEVALUE);
                } else {
                    namevalue = namevalue+", "+(String)h.get(NAMEVALUE);
                }
            }
        }
        
        return namevalue;
    }
    
    /**
     * Return all the gene symbols as a string.
     *
     * @return All the gene symbols as a string.
     */
    public String getGenesymbolString() {
        String namevalue = null;
        
        Enumeration enum = publicInfo.elements();
        while(enum.hasMoreElements()) {
            Hashtable h = (Hashtable)enum.nextElement();
            if(((String)h.get(NAMETYPE)).equals(GENE_SYMBOL)) {
                if(namevalue == null) {
                    namevalue = (String)h.get(NAMEVALUE);
                } else {
                    namevalue = namevalue+", "+(String)h.get(NAMEVALUE);
                }
            }
        }
        
        return namevalue;
    }
    
    /**
     * Return the PA number for Pseudomonas gene.
     *
     * @return The PA number for Pseudomonas gene.
     */
    public String getPanumber() {
        String namevalue=null;
        Enumeration enum = publicInfo.elements();
        while(enum.hasMoreElements()) {
            Hashtable h = (Hashtable)enum.nextElement();
            if(((String)h.get(NAMETYPE)).equals(PANUMBER)) {
                namevalue = (String)h.get(NAMEVALUE);
                break;
            }
        }
        return namevalue;
    }
    
    /**
     * Return the PA number for Pseudomonas gene.
     *
     * @return The PA number for Pseudomonas gene.
     */
    public String getInfoValue(String info_type) {
        String namevalue="";
        Enumeration enum = publicInfo.elements();
        while(enum.hasMoreElements()) {
            Hashtable h = (Hashtable)enum.nextElement();
            if(((String)h.get(NAMETYPE)).equals(info_type)) {
                namevalue = (String)h.get(NAMEVALUE);
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
        if(currentDescription != null)
            return currentDescription;
        
        String description = null;
        Enumeration enum = publicInfo.elements();
        while(enum.hasMoreElements()) {
            Hashtable h = (Hashtable)enum.nextElement();
            
            if(((String)h.get(NAMETYPE)).equals(GENBANK_ACCESSION)) {
                description = (String)h.get(DESCRIPTION);
                if (description != null)
                    return description;
            }
            
            if(((String)h.get(NAMETYPE)).equals(GI)) {
                description = (String)h.get(DESCRIPTION);
                if (description != null)
                    return description;
            }
            
            if(((String)h.get(NAMETYPE)).equals(UNIGENE_SID)) {
                description = (String)h.get(DESCRIPTION);
                if (description != null)
                    return description;
            }
        }
        return description;
    }
    
    /**
     * Set the current accession number.
     *
     * @param accession The value to be set to.
     */
    public void setCurrentAccession(String accession) {
        this.currentAccession = accession;
    }
    
    /**
     * Set the current GI number.
     *
     * @param gi The value to be set to.
     */
    public void setCurrentGi(String gi) {
        this.currentGi = gi;
    }
    
    /**
     * Set the current description.
     *
     * @param description The value to be set to.
     */
    public void setCurrentDescription(String description) {
        this.currentDescription = description;
    }
    
    public String getSpeciesCategory() {
        if("Homo sapiens".equals(getSpecies()) || "Saccharomyces cerevisiae".equals(getSpecies())) {
            return "allowed";
        } else {
            return "notallowed";
        }
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
        "s.gccontent as gccontent,"+
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
                gccontent = rs.getInt("GCCONTENT");
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
            if(flexstatus.equals(NEW))
                flexstatus = PENDING;
            
            String cdnasourceString = "'"+cdnasource+"'";
            if(cdnasource == null) {
                cdnasourceString = "null";
            }
            
            String chromosomeString = "'"+chromosome+"'";
            if(chromosome == null) {
                chromosomeString = "null";
            }
            
            //insert into flexsequence table.
            String sql = 	"insert into flexsequence\n" +
            "(sequenceid, flexstatus, genusspecies, dateadded,"+
            "cdsstart, cdsstop, cdslength, gccontent, cdnasource, chromosome)\n"+
            "values ("+id+", '"+flexstatus+"', '"+species+"',sysdate,"+
            cdsstart+","+cdsstop+","+cdslength+","+getGccontent()+
            ","+cdnasourceString+","+chromosomeString+")";
            
            DatabaseTransaction.executeUpdate(sql,conn);
            
            //insert into sequencetext table.
            if(sequencetext != null) {
                int i=0;
                while(sequencetext.length()-4000*i>4000) {
                    String text = sequencetext.substring(4000*(i), 4000*(i+1)).toUpperCase();
                    sql = "insert into sequencetext(sequenceid, sequenceorder, sequencetext)\n"+
                    "values("+id+","+(i+1)+",'"+text+"')";;
                    DatabaseTransaction.executeUpdate(sql,conn);
                    
                    i++;
                }
                String text = sequencetext.substring(4000*(i));
                sql = "insert into sequencetext(sequenceid, sequenceorder, sequencetext)\n"+
                "values("+id+","+(i+1)+",'"+text+"')";;
                DatabaseTransaction.executeUpdate(sql,conn);
            }
            
            //insert into name table.
            insertPublicInfo(publicInfo, conn);
        }
    }
    
    /**
     * Insert into name table.
     *
     * @param info The public information to be inserted into name table.
     * @param conn The <code>Connection</code> to preform the insert with.
     * @exception FlexDatabaseException.
     */
    public void insertPublicInfo(Vector info, Connection conn)
    throws FlexDatabaseException {
        String sql = "insert into name(sequenceid,nametype,namevalue,nameurl,description)"+
        " values(?,?,?,?,?)";
        PreparedStatement stmt = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            
            Enumeration enum = info.elements();
            while(enum.hasMoreElements()) {
                Hashtable h = (Hashtable)enum.nextElement();
                
                stmt.setInt(1, id);
                stmt.setString(2, (String)h.get(NAMETYPE));
                stmt.setString(3, (String)h.get(NAMEVALUE));
                stmt.setString(4, (String)h.get(NAMEURL));
                stmt.setString(5, (String)h.get(DESCRIPTION));
                
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (SQLException ex) {
            throw new FlexDatabaseException(ex);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    /**
     * Insert into name table.
     *
     * @param info The public information to be inserted into name table.
     * @param conn The <code>Connection</code> to preform the insert with.
     * @exception FlexDatabaseException.
     */
    public void insertPublicInfo(String typename, String typevalue,
    String nameurl, String namedesc, Connection conn)
    throws FlexDatabaseException {
        
        String sql = "insert into name(sequenceid,nametype,namevalue,nameurl,description)"+
        " values(?,?,?,?,?)";
        PreparedStatement stmt = null;
        
        try {
            conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setString(2, typename);
            stmt.setString(3, typevalue);
            stmt.setString(4, nameurl);
            stmt.setString(5, namedesc);
            
            DatabaseTransaction.executeUpdate(stmt);
        } catch (SQLException ex) {
            throw new FlexDatabaseException(ex);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    /**
     * Add the public info to the database.
     *
     * @param info The public information to be added.
     * @exception FlexDatabaseException.
     */
    public void addPublicInfo(Vector info) throws FlexDatabaseException {
        for(int i=0; i<info.size(); i++) {
            Hashtable h = (Hashtable)info.elementAt(i);
            if(publicInfoExists(h)) {
                info.removeElementAt(i);
            }
        }
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection c = t.requestConnection();
        insertPublicInfo(info, c);
        try {
            c.commit();
        } catch (SQLException ex) {
            throw new FlexDatabaseException(ex.getMessage());
        } finally {
            DatabaseTransaction.closeConnection(c);
        }
    }
    
    private boolean publicInfoExists(Hashtable info) {
        if (publicInfo == null) {
            return false;
        }
        
        String nameType = (String)info.get(NAMETYPE);
        String nameValue = (String)info.get(NAMEVALUE);
        
        Enumeration enum = publicInfo.elements();
        while(enum.hasMoreElements()) {
            Hashtable h = (Hashtable)enum.nextElement();
            if(nameType.equals(h.get(NAMETYPE)) && nameValue.equals(h.get(NAMEVALUE))) {
                return true;
            }
        }
        
        return false;
    }
    
    public static FlexSequence findSequenceByGi(String gi) {
        FlexSequence seq = null;
        DatabaseTransaction t = null;
        CachedRowSet crs = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            
            String sql = "select sequenceid, nametype, namevalue\n"+
            "from name\n"+
            "where nametype='GI'\n"+
            "and namevalue='"+gi+"'";
            crs = t.executeQuery(sql);
            
            if(crs.next()) {
                int id = crs.getInt("SEQUENCEID");
                seq = new FlexSequence(id);
            }
        } catch (SQLException sqlE) {
        } catch (FlexDatabaseException e) {
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
        return seq;
    }
    
    //call the parser with sequence gid, and set sequence values.
    public void setSequenceInfo(Hashtable h) throws FlexUtilException {
        setSpecies((String)h.get("species"));
        int start = ((Integer)h.get("start")).intValue();
        int stop = ((Integer)h.get("stop")).intValue();
        setCdsstart(start);
        setCdsstop(stop);
        setCdslength(stop-start+1);
        setSequencetext((String)h.get("sequencetext"));
        if(start < 0 || stop < 0) {
            setCdslength(0);
            setQuality(FlexSequence.QUESTIONABLE);
        }
        else {
            if(checkSequence()) {
                setQuality(FlexSequence.GOOD);
            } else {
                setQuality(FlexSequence.QUESTIONABLE);
            }
        }
    }
    
    public boolean checkSequence() {
        boolean result = true;
        if( (cdsstop - cdsstart + 1) % 3 != 0) {
            result = false;
        }
        else {
            String start_codon = sequencetext.substring(cdsstart-1, cdsstart+2);
            if ( !start_codon.equalsIgnoreCase("atg") && !start_codon.equalsIgnoreCase("gtg")) {
                result = false;
            }
            else {
                String stop_codon = sequencetext.substring(cdsstop-3,cdsstop);
                if (!stop_codon.equalsIgnoreCase("taa") && !stop_codon.equalsIgnoreCase("tag") && !stop_codon.equalsIgnoreCase("tga")) {
                    result = false;
                }
                else {
                    int first = cdsstart-1;
                    int second = cdsstart+2;
                    while (second < cdsstop) {
                        String codon = sequencetext.substring(first,second);
                        //System.out.println("codon: "+codon);
                        if (codon.equalsIgnoreCase("taa") || codon.equalsIgnoreCase("tag") || codon.equalsIgnoreCase("tga")) {
                            result = false;
                            break;
                        }
                        first = first+3;
                        second = second+3;
                    }
                }
            }
        }
        return result;
    }
    
    //******************************************************************//
    //				Private Methods                 	//
    //******************************************************************//
    
    // Calculate the sequence quality based on flex status.
    private String calculateQuality(String status) {
        String quality;
        
        if(REJECTED.equals(status)) {
            quality = QUESTIONABLE;
        } else {
            quality = GOOD;
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
            String text = "aaaaaaaaaaaaaaaaggggggggggggggggggttttttttttttt";
            FlexSequence seq = new FlexSequence(v);
            seq.setSequencetext(text);
            seq.insert(null);
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
            
            seq1 = null;
            seq1 = FlexSequence.findSequenceByGi("2345027");
            if(seq1 != null) {
                System.out.println("Testing findSequenceByGi: "+seq1.getId());
            } else {
                System.out.println("Testing findSequenceByGi: "+null);
            }
        } catch (FlexDatabaseException e) {
            System.out.println(e);
        } catch(FlexCoreException e) {
            System.out.println(e);
        } finally {
            DatabaseTransaction.closeResultSet(sequenceRs);
            DatabaseTransaction.closeResultSet(textRs);
            DatabaseTransaction.closeResultSet(nameRs);
            System.exit(0);
        }
    }
    
    
    
}

