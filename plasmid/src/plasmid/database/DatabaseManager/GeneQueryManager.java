/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.database.DatabaseManager;

import java.io.IOException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import plasmid.coreobject.CloneAnalysis;
import plasmid.coreobject.CloneInformation;
import plasmid.coreobject.CloneVector;
import plasmid.coreobject.Gene2Refseq;
import plasmid.coreobject.Geneinfo;
import plasmid.coreobject.Refseq;
import plasmid.database.DatabaseException;
import plasmid.database.DatabaseTransaction;
import plasmid.util.StringConvertor;

/**
 *
 * @author Lab User
 */
public class GeneQueryManager {

    public static List<Geneinfo> queryGenes(List<String> terms, boolean isCaseSensitive) throws DatabaseException {
        String sql = "select geneid,symbol from geneinfo where geneid in"
                + "(select geneid from synonyms where ";
        if (isCaseSensitive) {
            sql += "symbol=?)";
        } else {
            sql += "lower(symbol)=?)";
        }

        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Geneinfo> genes = new ArrayList<Geneinfo>();
        String currentTerm = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            for (String term : terms) {
                if (term == null || term.length() == 0) {
                    continue;
                }
                currentTerm = term;
                if (isCaseSensitive) {
                    stmt.setString(1, term);
                } else {
                    stmt.setString(1, term.toLowerCase());
                }
                rs = DatabaseTransaction.executeQuery(stmt);
                while (rs.next()) {
                    int geneid = rs.getInt(1);
                    String symbol = rs.getString(2);
                    Geneinfo gene = new Geneinfo();
                    gene.setGeneid(geneid);
                    gene.setSymbol(symbol);
                    gene.setTerm(currentTerm);
                    genes.add(gene);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException("Database error occured.");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DatabaseException("Database error occured while searching for: " + currentTerm);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
        return genes;
    }

    public static List<Geneinfo> queryGenesDetail(List<String> terms, boolean isCaseSensitive, List<String> vptypes, List<String> restrictions, List<String> speciesList) throws DatabaseException {
        String sql = "select geneid,symbol,description,type,locustag,synonyms,chromosome,"
                + "maplocation,symbol_nom,name_nom,nom_status,g.taxid,s.genusspecies"
                + " from geneinfo g, species s"
                + " where g.taxid=s.taxid"
                + " and g.geneid in"
                + " (select geneid from synonyms where ";
        if (isCaseSensitive) {
            sql += "symbol=?)";
        } else {
            sql += "lower(symbol)=?)";
        }
        if (speciesList != null && speciesList.size() > 0) {
            sql += " and g.taxid in (" + StringConvertor.convertFromListToSqlString(speciesList) + ")";
        }
        String sql2 = "select count(cloneid) from clone where cloneid in"
                + "(select cloneid from cloneanalysis where ((type='"
                + CloneAnalysis.TYPE_CDS + "' and percentid>90 and alength>100)"
                + " or (type='" + CloneAnalysis.TYPE_NOSEQ + "'))"
                + " and gi_nt in (select gi_nt from gene2rerseq where geneid=?))"
                + " and status='AVAILABLE'";

        String vptype = null;
        if (vptypes != null && vptypes.size() > 0) {
            vptype = StringConvertor.convertFromListToSqlString(vptypes);
            sql2 += " and vectorid in (select vectorid from vectorproperty where propertytype in (" + vptype + "))";
        }

        if (restrictions != null) {
            String s = StringConvertor.convertFromListToSqlString(restrictions);
            sql2 = sql2 + " and restriction in (" + s + ")";
        }

        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        List<Geneinfo> genes = new ArrayList<Geneinfo>();
        String currentTerm = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();

            if (conn == null) {
                throw new DatabaseException("Cannot connect to database.");
            }

            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement(sql2);
            for (String term : terms) {
                if (term == null || term.length() == 0) {
                    continue;
                }
                currentTerm = term;
                if (isCaseSensitive) {
                    stmt.setString(1, term);
                } else {
                    stmt.setString(1, term.toLowerCase());
                }
                rs = DatabaseTransaction.executeQuery(stmt);
                while (rs.next()) {
                    int geneid = rs.getInt(1);
                    String symbol = rs.getString(2);
                    String description = rs.getString(3);
                    String type = rs.getString(4);
                    String locustag = rs.getString(5);
                    String synonyms = rs.getString(6);
                    String chromosome = rs.getString(7);
                    String maplocation = rs.getString(8);
                    String symbolnom = rs.getString(9);
                    String namenom = rs.getString(10);
                    String nomstatus = rs.getString(11);
                    int taxid = rs.getInt(12);
                    String species = rs.getString(13);
                    Geneinfo gene = new Geneinfo(geneid, symbol, description, type, locustag, synonyms,
                            chromosome, maplocation, symbolnom, namenom, nomstatus, taxid);
                    gene.setTerm(currentTerm);
                    gene.setSpecies(species);

                    stmt2.setInt(1, geneid);
                    rs2 = DatabaseTransaction.executeQuery(stmt2);
                    if (rs2.next()) {
                        int count = rs2.getInt(1);
                        gene.setCloneCount(count);
                    }
                    genes.add(gene);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException("Database error occured.");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs2);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeConnection(conn);
        }
        return genes;
    }

    public static Geneinfo queryGeneWithRefseqs(int geneid) throws DatabaseException {
        String sqlGene = "select geneid, symbol,description,type,locustag,synonyms,chromosome,"
                + "maplocation,symbol_nom,name_nom,nom_status,taxid from geneinfo where geneid=" + geneid;
        String sql = "select accession_nt,gi_nt,status,accession_protein,gi_protein,accession_genomic,"
                + "gi_genomic,start_on_genomic,end_on_genomic,orientation,assembly from gene2rerseq"
                + " where geneid=" + geneid;
        DatabaseTransaction t = null;
        ResultSet rsGene = null;
        ResultSet rs = null;
        Geneinfo gene = null;
        try {
            t = DatabaseTransaction.getInstance();
            rsGene = t.executeQuery(sqlGene);
            if (rsGene.next()) {
                String symbol = rsGene.getString(2);
                String description = rsGene.getString(3);
                String type = rsGene.getString(4);
                String locustag = rsGene.getString(5);
                String synonyms = rsGene.getString(6);
                String chromosome = rsGene.getString(7);
                String maplocation = rsGene.getString(8);
                String symbolnom = rsGene.getString(9);
                String namenom = rsGene.getString(10);
                String nomstatus = rsGene.getString(11);
                int taxid = rsGene.getInt(12);
                gene = new Geneinfo(geneid, symbol, description, type, locustag, synonyms,
                        chromosome, maplocation, symbolnom, namenom, nomstatus, taxid);

                List<Gene2Refseq> seqs = new ArrayList<Gene2Refseq>();
                rs = t.executeQuery(sql);
                while (rs.next()) {
                    String accession = rs.getString(1);
                    int gi = rs.getInt(2);
                    String status = rs.getString(3);
                    String proteinacc = rs.getString(4);
                    int proteingi = rs.getInt(5);
                    String accessiongenomic = rs.getString(6);
                    int gigenomic = rs.getInt(7);
                    int start = rs.getInt(8);
                    int end = rs.getInt(9);
                    String orientation = rs.getString(10);
                    String assembly = rs.getString(11);
                    Gene2Refseq seq = new Gene2Refseq();
                    seq.setAccession(accession);
                    seq.setGi(gi);
                    seq.setStatus(status);
                    seq.setProteinacc(proteinacc);
                    seq.setProteingi(proteingi);
                    seq.setGenomicacc(accessiongenomic);
                    seq.setGenomicgi(gigenomic);
                    seq.setStart(start);
                    seq.setEnd(end);
                    seq.setOrientation(orientation);
                    seq.setAssembly(assembly);
                    seqs.add(seq);
                }
                gene.setSeqs(seqs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException("Database error occured.");
        } finally {
            DatabaseTransaction.closeResultSet(rsGene);
            DatabaseTransaction.closeResultSet(rs);
        }
        return gene;
    }

    public static List<Geneinfo> queryGenesWithRefseqs(List<String> terms, boolean isCaseSensitive, List<String> vptypes, List<String> restrictions, List<String> speciesList) throws DatabaseException {
        String sql = "select geneid,symbol,description,type,locustag,synonyms,chromosome,"
                + "maplocation,symbol_nom,name_nom,nom_status,g.taxid,s.genusspecies"
                + " from geneinfo g, species s"
                + " where g.taxid=s.taxid"
                + " and g.geneid in"
                + " (select geneid from synonyms where ";
        if (isCaseSensitive) {
            sql += "symbol=?)";
        } else {
            sql += "lower(symbol)=?)";
        }
        if (speciesList != null && speciesList.size() > 0) {
            sql += " and g.taxid in (" + StringConvertor.convertFromListToSqlString(speciesList) + ")";
        }
        String sql2 = "select distinct accession_nt,gi_nt,status,accession_protein,gi_protein from gene2rerseq"
                + " where gi_nt<>0 and geneid=?";
        String sql3 = "select seq,cds,proteinseq from refseq where gi_nt=?";

        String sql4 = "select count(cloneid) from clone where cloneid in"
                + "(select cloneid from cloneanalysis where ((type='"
                + CloneAnalysis.TYPE_CDS + "' and percentid>90 and alength>100)"
                + " or (type='" + CloneAnalysis.TYPE_NOSEQ + "'))"
                + " and gi_nt=?) and status='AVAILABLE'";
        String vptype = null;
        if (vptypes != null && vptypes.size() > 0) {
            vptype = StringConvertor.convertFromListToSqlString(vptypes);
            sql4 += " and vectorid in"
                    + " (select vectorid from vectorproperty where propertytype in (" + vptype + "))";
        }
        if (restrictions != null) {
            String s = StringConvertor.convertFromListToSqlString(restrictions);
            sql4 = sql4 + " and restriction in (" + s + ")";
        }

        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;
        List<Geneinfo> genes = new ArrayList<Geneinfo>();
        String currentTerm = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();

            if (conn == null) {
                throw new DatabaseException("Cannot connect to database.");
            }

            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement(sql2);
            stmt3 = conn.prepareStatement(sql3);
            stmt4 = conn.prepareStatement(sql4);
            for (String term : terms) {
                if (term == null || term.length() == 0) {
                    continue;
                }
                currentTerm = term;
                if (isCaseSensitive) {
                    stmt.setString(1, term);
                } else {
                    stmt.setString(1, term.toLowerCase());
                }
                rs = DatabaseTransaction.executeQuery(stmt);

                while (rs.next()) {
                    int geneid = rs.getInt(1);
                    String symbol = rs.getString(2);
                    String description = rs.getString(3);
                    String type = rs.getString(4);
                    String locustag = rs.getString(5);
                    String synonyms = rs.getString(6);
                    String chromosome = rs.getString(7);
                    String maplocation = rs.getString(8);
                    String symbolnom = rs.getString(9);
                    String namenom = rs.getString(10);
                    String nomstatus = rs.getString(11);
                    int taxid = rs.getInt(12);
                    String species = rs.getString(13);
                    Geneinfo gene = new Geneinfo(geneid, symbol, description, type, locustag, synonyms,
                            chromosome, maplocation, symbolnom, namenom, nomstatus, taxid);
                    gene.setTerm(currentTerm);
                    gene.setSpecies(species);

                    List<Gene2Refseq> seqs = doQueryRefseqs(stmt2,stmt3,stmt4,rs2,rs3,rs4,geneid);
                    gene.setSeqs(seqs);
                    genes.add(gene);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException("Database error occured.");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs2);
            DatabaseTransaction.closeResultSet(rs3);
            DatabaseTransaction.closeResultSet(rs4);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt3);
            DatabaseTransaction.closeStatement(stmt4);
            DatabaseTransaction.closeConnection(conn);
        }
        return genes;
    }

    private static List<Gene2Refseq> doQueryRefseqs(PreparedStatement stmt2, PreparedStatement stmt3,
            PreparedStatement stmt4, ResultSet rs2, ResultSet rs3, ResultSet rs4, int geneid)
            throws SQLException, DatabaseException {
        stmt2.setInt(1, geneid);
        rs2 = DatabaseTransaction.executeQuery(stmt2);
        List<Gene2Refseq> seqs = new ArrayList<Gene2Refseq>();
        while (rs2.next()) {
            String accession = rs2.getString(1);
            int gi = rs2.getInt(2);
            String status = rs2.getString(3);
            String proteinacc = rs2.getString(4);
            int proteingi = rs2.getInt(5);
            Gene2Refseq seq = new Gene2Refseq();
            seq.setAccession(accession);
            seq.setGi(gi);
            seq.setStatus(status);
            seq.setProteinacc(proteinacc);
            seq.setProteingi(proteingi);

            stmt3.setInt(1, gi);
            String seqString = "";
            String cdsString = "";
            String proteinString = "";
            rs3 = DatabaseTransaction.executeQuery(stmt3);
            if (rs3.next()) {
                Clob sequence = rs3.getClob(1);
                Clob cds = rs3.getClob(2);
                Clob proteinseq = rs3.getClob(3);
                try {
                    seqString = StringConvertor.convertClobToString(sequence);
                    cdsString = StringConvertor.convertClobToString(cds);
                    proteinString = StringConvertor.convertClobToString(proteinseq);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            seq.setSequence(seqString);
            seq.setCds(cdsString);
            seq.setProteinseq(proteinString);

            stmt4.setInt(1, gi);
            rs4 = DatabaseTransaction.executeQuery(stmt4);
            if (rs4.next()) {
                int count = rs4.getInt(1);
                seq.setCloneCount(count);
            }
            seqs.add(seq);
        }
        return seqs;
    }

    public static List<Gene2Refseq> queryGene2Refseqs(int geneid, List<String> vptypes, List<String> restrictions) throws DatabaseException {
        String sql = "select distinct accession_nt,gi_nt,status,accession_protein,gi_protein from gene2rerseq"
                + " where gi_nt<>0 and geneid=" + geneid;
        String sql2 = "select seq,cds,proteinseq from refseq where gi_nt=?";

        String sql3 = "select count(cloneid) from clone where cloneid in"
                + "(select cloneid from cloneanalysis where ((type='"
                + CloneAnalysis.TYPE_CDS + "' and percentid>90 and alength>100)"
                + " or (type='" + CloneAnalysis.TYPE_NOSEQ + "'))"
                + " and gi_nt=?) and status='AVAILABLE'";
        String vptype = null;
        if (vptypes != null && vptypes.size() > 0) {
            vptype = StringConvertor.convertFromListToSqlString(vptypes);
            sql3 += " and vectorid in"
                    + " (select vectorid from vectorproperty where propertytype in (" + vptype + "))";
        }
        if (restrictions != null) {
            String s = StringConvertor.convertFromListToSqlString(restrictions);
            sql3 = sql3 + " and restriction in (" + s + ")";
        }

        DatabaseTransaction t = null;
        ResultSet rs = null;
        ResultSet rs2 = null;;
        ResultSet rs3 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt3 = null;
        Connection c = null;
        List<Gene2Refseq> seqs = new ArrayList<Gene2Refseq>();
        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            stmt = c.prepareStatement(sql2);
            stmt3 = c.prepareStatement(sql3);
            rs = t.executeQuery(sql);
            while (rs.next()) {
                String accession = rs.getString(1);
                int gi = rs.getInt(2);
                String status = rs.getString(3);
                String proteinacc = rs.getString(4);
                int proteingi = rs.getInt(5);
                Gene2Refseq seq = new Gene2Refseq();
                seq.setAccession(accession);
                seq.setGi(gi);
                seq.setStatus(status);
                seq.setProteinacc(proteinacc);
                seq.setProteingi(proteingi);

                stmt.setInt(1, gi);
                String seqString = "";
                String cdsString = "";
                String proteinString = "";
                rs2 = DatabaseTransaction.executeQuery(stmt);
                if (rs2.next()) {
                    Clob sequence = rs2.getClob(1);
                    Clob cds = rs2.getClob(2);
                    Clob proteinseq = rs2.getClob(3);
                    try {
                        seqString = StringConvertor.convertClobToString(sequence);
                        cdsString = StringConvertor.convertClobToString(cds);
                        proteinString = StringConvertor.convertClobToString(proteinseq);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                seq.setSequence(seqString);
                seq.setCds(cdsString);
                seq.setProteinseq(proteinString);

                stmt3.setInt(1, gi);
                rs3 = DatabaseTransaction.executeQuery(stmt3);
                if (rs3.next()) {
                    int count = rs3.getInt(1);
                    seq.setCloneCount(count);
                }
                seqs.add(seq);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException("Database error occured.");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs2);
            DatabaseTransaction.closeResultSet(rs3);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt3);
            DatabaseTransaction.closeConnection(c);
        }
        return seqs;
    }

    public static List<Refseq> queryRefseqs(int geneid) throws DatabaseException {
        String sql = "select distinct r.gi_nt,r.accession_nt,r.accession_protein,r.gi_protein,g.status"
                + " from refseq r, gene2rerseq g where r.gi_nt=g.gi_nt"
                + " and g.geneid=" + geneid;
        DatabaseTransaction t = null;
        ResultSet rs = null;
        List<Refseq> seqs = new ArrayList<Refseq>();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while (rs.next()) {
                int gi = rs.getInt(1);
                String accession = rs.getString(2);
                String proteinacc = rs.getString(3);
                int proteingi = rs.getInt(4);
                String status = rs.getString(5);
                Refseq seq = new Refseq();
                seq.setAccession(accession);
                seq.setGi("" + gi);
                seq.setProteinacc(proteinacc);
                seq.setProteingi("" + proteingi);
                seq.setStatus(status);
                seqs.add(seq);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException("Database error occured.");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return seqs;
    }

    public static List<CloneInformation> queryClones(String gi, List<String> vptypes, List<String> restrictions) throws DatabaseException {
        int giInt = 0;
        try {
            giInt = Integer.parseInt(gi);
        } catch (Exception ex) {
            throw new DatabaseException("Invalid GI: " + gi);
        }

        String sql = "select ca.cloneid,ca.percentid,ca.alength,ca.coverage,ca.mutation,"
                + " c.clonename,c.restriction,c.vectorname,d.format,d.sizeinbp,c.vectorid,ca.type"
                + " from cloneanalysis ca, clone c, dnainsert d, cloneinsert ci"
                + " where ca.cloneid=c.cloneid"
                + " and c.cloneid=ci.cloneid"
                + " and ci.insertid=d.insertid"
                + " and c.status='AVAILABLE'"
                + " and ((ca.type='" + CloneAnalysis.TYPE_CDS
                + "' and ca.percentid>90 and ca.alength>100)"
                + " or (ca.type='" + CloneAnalysis.TYPE_NOSEQ + "'))"
                + " and ca.gi_nt=" + giInt;
        String vptype = null;
        if (vptypes != null && vptypes.size() > 0) {
            vptype = StringConvertor.convertFromListToSqlString(vptypes);
            sql += " and c.vectorid in"
                    + " (select vectorid from vectorproperty where propertytype in (" + vptype + "))";
        }
        if (restrictions != null) {
            String s = StringConvertor.convertFromListToSqlString(restrictions);
            sql = sql + " and c.restriction in (" + s + ")";
        }
        sql += " order by ca.coverage desc, ca.mutation asc, c.vectorname asc";
        DatabaseTransaction t = null;
        ResultSet rs = null;
        List<CloneInformation> clones = new ArrayList<CloneInformation>();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while (rs.next()) {
                int cloneid = rs.getInt(1);
                double percentid = rs.getDouble(2);
                int alength = rs.getInt(3);
                double coverage = rs.getDouble(4);
                int mutation = rs.getInt(5);
                String clonename = rs.getString(6);
                String restriction = rs.getString(7);
                String vector = rs.getString(8);
                String format = rs.getString(9);
                int size = rs.getInt(10);
                int vectorid = rs.getInt(11);
                String type = rs.getString(12);
                CloneInformation c = new CloneInformation();
                c.setCloneid(cloneid);
                c.setPercentid(percentid);
                c.setAlength(alength);
                c.setCoverage(coverage);
                c.setMutation(mutation);
                c.setGi(giInt);
                c.setClonename(clonename);
                c.setRestriction(restriction);
                c.setVector(vector);
                c.setFormat(format);
                c.setSize(size);
                c.setVectorid(vectorid);
                c.setType(type);
                clones.add(c);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException("Database error occured.");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return clones;
    }

    public static List<CloneVector> queryVectorByType(String vptype) throws DatabaseException {
        String sql = "select vectorid from vectorproperty where propertytype in (" + vptype + ")";

        DatabaseTransaction t = null;
        ResultSet rs = null;
        List<CloneVector> vectors = new ArrayList<CloneVector>();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while (rs.next()) {
                int vectorid = rs.getInt(1);
                CloneVector v = new CloneVector();
                v.setVectorid(vectorid);
                vectors.add(v);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException("Database error occured.");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return vectors;
    }

    public static String querySequence(int gi, String type) {
        String sql = "select SEQ,CDS,PROTEINSEQ from refseq where gi_nt=" + gi;
        DatabaseTransaction t = null;
        ResultSet rs = null;
        String s = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if (rs.next()) {
                Clob seq = rs.getClob(1);
                Clob cds = rs.getClob(2);
                Clob proteinseq = rs.getClob(3);
                if (CloneAnalysis.TYPE_AA.equals(type)) {
                    s = StringConvertor.convertClobToString(proteinseq);
                } else if (CloneAnalysis.TYPE_NT.equals(type)) {
                    s = StringConvertor.convertClobToString(seq);
                } else {
                    s = StringConvertor.convertClobToString(cds);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (DatabaseException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return s;
    }
}
