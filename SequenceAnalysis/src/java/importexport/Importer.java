/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importexport;

import core.CloneAnalysis;
import core.Dbxref;
import core.Gene2Refseq;
import core.Geneinfo;
import core.Othernames;
import core.Refseq;
import core.Synonyms;
import dao.DaoException;
import dao.DatabaseTransaction;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lab User
 */
public class Importer {

    public void importFile(String file, Connection conn, String type) throws DaoException, ImportExportException {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = in.readLine();
            List l = new ArrayList();
            int n = 0;
            while ((line = in.readLine()) != null) {
                String[] s = line.split("\t");
                l.add(s);
                n++;

                if (n == 1000) {
                    List info = parseInfo(l, type);
                    importInfo(info, conn, type);
                    l = new ArrayList();
                    n = 0;
                }
            }
            if (!l.isEmpty()) {
                List info = parseInfo(l, type);
                importInfo(info, conn, type);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ImportExportException("Error occured while reading file: " + file);
        }
    }

    public List parseInfo(List<String[]> l, String type) throws ImportExportException {
        if ("geneinfo".equals(type)) {
            return parseGeneinfo(l);
        }
        if ("refseq".equals(type)) {
            return parseRefseq(l);
        }
        if ("gene2refseq".equals(type)) {
            return parseGene2Refseq(l);
        }
        if ("synonym".equals(type)) {
            return parseSynonyms(l);
        }
        if ("dbxref".equals(type)) {
            return parseDbxref(l);
        }
        if ("othernames".equals(type)) {
            return parseOthernames(l);
        }
        if ("cloneanalysis".equals(type)) {
            return parseCloneAnalysis(l);
        }
        return null;
    }

    public void importInfo(List info, Connection conn, String type) throws DaoException {
        if ("geneinfo".equals(type)) {
            importGeneinfo(info, conn);
        }
        if ("refseq".equals(type)) {
            importRefseq(info, conn);
        }
        if ("gene2refseq".equals(type)) {
            importGene2Refseq(info, conn);
        }
        if ("synonym".equals(type)) {
            importSynonyms(info, conn);
        }
        if ("dbxref".equals(type)) {
            importDbxref(info, conn);
        }
        if ("othernames".equals(type)) {
            importOthernames(info, conn);
        }
        if ("cloneanalysis".equals(type)) {
            importCloneAnalysis(info, conn);
        }
    }

    public List<Geneinfo> parseGeneinfo(List<String[]> l) throws ImportExportException {
        List<Geneinfo> info = new ArrayList<Geneinfo>();
        for (String[] s : l) {
            try {
                Geneinfo g = new Geneinfo();
                int geneid = Integer.parseInt(s[0]);
                int taxid = Integer.parseInt(s[11]);
                g.setGeneid(geneid);
                g.setSymbol(s[1]);
                g.setDescription(s[2]);
                g.setType(s[3]);
                g.setLocustag(s[4]);
                g.setSynonyms(s[5]);
                g.setChromosome(s[6]);
                g.setMaplocation(s[7]);
                g.setSymbolnom(s[8]);
                g.setNamenom(s[9]);
                g.setNomstatus(s[10]);
                g.setTaxid(taxid);
                info.add(g);
            } catch (NumberFormatException ex) {
                throw new ImportExportException(ex.getMessage());
            }
        }
        return info;
    }

    public List<Refseq> parseRefseq(List<String[]> l) throws ImportExportException {
        List<Refseq> info = new ArrayList<Refseq>();
        for (String[] s : l) {
            try {
                Refseq r = new Refseq();
                r.setGi(s[0]);
                r.setAccession(s[1]);
                r.setProteinacc(s[2]);
                r.setProteingi(s[3]);
                r.setSequence(s[4]);
                r.setCds(s[5]);
                r.setProteinseq(s[6]);
                if (s[7].length() > 0) {
                    r.setHasSequence(Integer.parseInt(s[7]));
                }
                if (s[8].length() > 0) {
                    r.setHasCds(Integer.parseInt(s[8]));
                }
                if (s[9].length() > 0) {
                    r.setHasProteinseq(Integer.parseInt(s[9]));
                }
                r.setTaxid(Integer.parseInt(s[10]));
                info.add(r);
            } catch (NumberFormatException ex) {
                throw new ImportExportException(ex.getMessage());
            }
        }
        return info;
    }

    public List<Gene2Refseq> parseGene2Refseq(List<String[]> l) throws ImportExportException {
        List<Gene2Refseq> info = new ArrayList<Gene2Refseq>();
        for (String[] s : l) {
            try {
                Gene2Refseq r = new Gene2Refseq();
                r.setTaxid(Integer.parseInt(s[0]));
                if (s[1].length() > 1) {
                    r.setAccession(s[1]);
                }
                if (s[2].length() > 0) {
                    r.setGeneid(Integer.parseInt(s[2]));
                }
                if (s[3].length() > 1) {
                    r.setGi(Integer.parseInt(s[3]));
                }
                r.setStatus(s[4]);
                if (s[5].length() > 1) {
                    r.setProteinacc(s[5]);
                }
                if (s[6].length() > 1) {
                    r.setProteingi(Integer.parseInt(s[6]));
                }
                r.setGenomicacc(s[7]);
                r.setGenomicgi(Integer.parseInt(s[8]));
                r.setStart(Integer.parseInt(s[9]));
                r.setEnd(Integer.parseInt(s[10]));
                r.setOrientation(s[11]);
                r.setAssembly(s[12]);
                info.add(r);
            } catch (NumberFormatException ex) {
                throw new ImportExportException(ex.getMessage());
            }
        }
        return info;
    }

    public List<Synonyms> parseSynonyms(List<String[]> l) throws ImportExportException {
        List<Synonyms> info = new ArrayList<Synonyms>();
        for (String[] s : l) {
            try {
                Synonyms synonym = new Synonyms();
                synonym.setGeneid(Integer.parseInt(s[0]));
                synonym.setSymbol(s[1]);
                synonym.setOfficial(Integer.parseInt(s[2]));
                info.add(synonym);
            } catch (NumberFormatException ex) {
                throw new ImportExportException(ex.getMessage());
            }
        }
        return info;
    }

    public List<Dbxref> parseDbxref(List<String[]> l) throws ImportExportException {
        List<Dbxref> info = new ArrayList<Dbxref>();
        for (String[] s : l) {
            try {
                Dbxref d = new Dbxref();
                d.setGeneid(Integer.parseInt(s[0]));
                d.setDbname(s[1]);
                d.setDbid(s[2]);
                info.add(d);
            } catch (NumberFormatException ex) {
                throw new ImportExportException(ex.getMessage());
            }
        }
        return info;
    }

    public List<Othernames> parseOthernames(List<String[]> l) throws ImportExportException {
        List<Othernames> info = new ArrayList<Othernames>();
        for (String[] s : l) {
            try {
                Othernames d = new Othernames();
                d.setGeneid(Integer.parseInt(s[0]));
                d.setDescription(s[1]);
                info.add(d);
            } catch (NumberFormatException ex) {
                throw new ImportExportException(ex.getMessage());
            }
        }
        return info;
    }

    public List<CloneAnalysis> parseCloneAnalysis(List<String[]> l) throws ImportExportException {
        List<CloneAnalysis> info = new ArrayList<CloneAnalysis>();
        for (String[] s : l) {
            try {
                CloneAnalysis c = new CloneAnalysis();
                c.setCloneid(Integer.parseInt(s[0]));
                c.setGi(Integer.parseInt(s[1]));
                c.setType(s[2]);
                c.setPercentid(Double.parseDouble(s[3]));
                c.setAlength(Integer.parseInt(s[4]));
                c.setMismatch(Integer.parseInt(s[5]));
                c.setGap(Integer.parseInt(s[6]));
                c.setEvalue(Double.parseDouble(s[7]));
                c.setScore(Double.parseDouble(s[8]));
                c.setQstart(Integer.parseInt(s[9]));
                c.setQend(Integer.parseInt(s[10]));
                c.setSstart(Integer.parseInt(s[11]));
                c.setSend(Integer.parseInt(s[12]));
                c.setCoverage(Double.parseDouble(s[13]));
                c.setMutation(Integer.parseInt(s[14]));
                info.add(c);
            } catch (NumberFormatException ex) {
                throw new ImportExportException(ex.getMessage());
            }
        }
        return info;
    }

    public void importGeneinfo(List<Geneinfo> info, Connection conn) throws DaoException {
        String sql = "insert into geneinfo (geneid,symbol,description,type,locustag,synonyms,chromosome,"
                + "maplocation,symbol_nom,name_nom,nom_status,taxid) values (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for (Geneinfo g : info) {
                stmt.setInt(1, g.getGeneid());
                stmt.setString(2, g.getSymbol());
                stmt.setString(3, g.getDescription());
                stmt.setString(4, g.getType());
                stmt.setString(5, g.getLocustag());
                stmt.setString(6, g.getSynonyms());
                stmt.setString(7, g.getChromosome());
                stmt.setString(8, g.getMaplocation());
                stmt.setString(9, g.getSymbolnom());
                stmt.setString(10, g.getNamenom());
                stmt.setString(11, g.getNomstatus());
                stmt.setInt(12, g.getTaxid());
                DatabaseTransaction.executeUpdate(stmt);
                System.out.println("import gene: " + g.getGeneid());
            }
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    public void importRefseq(List<Refseq> info, Connection conn) throws DaoException {
        String sql = "insert into refseq (gi_nt,accession_nt,accession_protein,gi_protein,seq,cds,"
                + "proteinseq,hassequence,hascds,hasproteinseq,taxid) values (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for (Refseq r : info) {
                stmt.setInt(1, Integer.parseInt(r.getGi()));
                stmt.setString(2, r.getAccession());
                stmt.setString(3, r.getProteinacc());
                stmt.setInt(4, Integer.parseInt(r.getProteingi()));
                stmt.setObject(5, r.getSequence());
                stmt.setObject(6, r.getCds());
                stmt.setObject(7, r.getProteinseq());
                stmt.setInt(8, r.getHasSequence());
                stmt.setInt(9, r.getHasCds());
                stmt.setInt(10, r.getHasProteinseq());
                stmt.setInt(11, r.getTaxid());
                DatabaseTransaction.executeUpdate(stmt);
                System.out.println("import refseq: " + r.getGi());
            }
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    public void importGene2Refseq(List<Gene2Refseq> info, Connection conn) throws DaoException {
        String sql = "insert into gene2rerseq (taxid,accession_nt,geneid,gi_nt,status,accession_protein,"
                + "gi_protein,accession_genomic,gi_genomic,start_on_genomic,end_on_genomic,orientation,"
                + "assembly) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for (Gene2Refseq g : info) {
                stmt.setInt(1, g.getTaxid());
                stmt.setString(2, g.getAccession());
                stmt.setInt(3, g.getGeneid());
                stmt.setInt(4, g.getGi());
                stmt.setString(5, g.getStatus());
                stmt.setString(6, g.getProteinacc());
                stmt.setInt(7, g.getProteingi());
                stmt.setString(8, g.getGenomicacc());
                stmt.setInt(9, g.getGenomicgi());
                stmt.setInt(10, g.getStart());
                stmt.setInt(11, g.getEnd());
                stmt.setString(12, g.getOrientation());
                stmt.setString(13, g.getAssembly());
                DatabaseTransaction.executeUpdate(stmt);
                System.out.println("import gene2refseq: " + g.getGenomicgi());
            }
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    public void importSynonyms(List<Synonyms> info, Connection conn) throws DaoException {
        String sql = "insert into synonyms(geneid,symbol,isofficial) values(?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for (Synonyms g : info) {
                stmt.setInt(1, g.getGeneid());
                stmt.setString(2, g.getSymbol());
                stmt.setInt(3, g.getOfficial());
                DatabaseTransaction.executeUpdate(stmt);
                System.out.println("import synonym: " + g.getGeneid()+","+g.getSymbol());
            }
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    public void importDbxref(List<Dbxref> info, Connection conn) throws DaoException {
        String sql = "insert into dbxref(geneid,dbname,dbid) values(?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for (Dbxref g : info) {
                stmt.setInt(1, g.getGeneid());
                stmt.setString(2, g.getDbname());
                stmt.setString(3, g.getDbid());
                DatabaseTransaction.executeUpdate(stmt);
                System.out.println("import dbxref: " + g.getGeneid()+","+g.getDbname()+","+g.getDbid());
            }
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    public void importOthernames(List<Othernames> info, Connection conn) throws DaoException {
        String sql = "insert into othernames(geneid,description) values(?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for (Othernames g : info) {
                stmt.setInt(1, g.getGeneid());
                stmt.setString(2, g.getDescription());
                DatabaseTransaction.executeUpdate(stmt);
                System.out.println("import othernames: " + g.getGeneid()+","+g.getDescription());
            }
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    public void importCloneAnalysis(List<CloneAnalysis> info, Connection conn) throws DaoException {
        String sql = "insert into cloneanalysis(cloneid,gi_nt,type,percentid,alength,mismatch,gap,evalue,"
                + "score,qstart,qend,sstart,send,coverage,mutation) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for (CloneAnalysis c : info) {
                stmt.setInt(1, c.getCloneid());
                stmt.setInt(2, c.getGi());
                stmt.setString(3, c.getType());
                stmt.setDouble(4, c.getPercentid());
                stmt.setInt(5, c.getAlength());
                stmt.setInt(6, c.getMismatch());
                stmt.setInt(7, c.getGap());
                stmt.setDouble(8, c.getEvalue());
                stmt.setDouble(9, c.getScore());
                stmt.setInt(10, c.getQstart());
                stmt.setInt(11, c.getQend());
                stmt.setInt(12, c.getSstart());
                stmt.setInt(13, c.getSend());
                stmt.setDouble(14, c.getCoverage());
                stmt.setInt(15, c.getMutation());
                DatabaseTransaction.executeUpdate(stmt);
                System.out.println("import cloneanalysis: " + c.getCloneid()+","+c.getGi()+","+c.getType());
            }
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    public static void main(String args[]) {
        String geneinfoFile = "D:\\dev\\plasmid_db\\import\\geneinfo_import.txt";
        String refseqFile = "D:\\dev\\plasmid_db\\import\\refseq_import.txt";
        String gene2refseqFile = "D:\\dev\\plasmid_db\\import\\gene2refseq_import.txt";
        String synonymFile = "D:\\dev\\plasmid_db\\import\\synonyms_import.txt";
        String dbxrefFile = "D:\\dev\\plasmid_db\\import\\dbxref.txt";
        String othernamesFile = "D:\\dev\\plasmid_db\\import\\othernames.txt";
        String cloneanalysisFile = "D:\\dev\\plasmid_db\\import\\PlasmidAnalysis_import_prod.txt";

        Importer importer = new Importer();
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            //importer.importFile(geneinfoFile, conn, "geneinfo");
            //importer.importFile(refseqFile, conn, "refseq");
            //importer.importFile(gene2refseqFile, conn, "gene2refseq");
            //importer.importFile(synonymFile, conn, "synonym");
            //importer.importFile(dbxrefFile, conn, "dbxref");
            //importer.importFile(othernamesFile, conn, "othernames");
            importer.importFile(cloneanalysisFile, conn, "cloneanalysis");
            
            DatabaseTransaction.commit(conn);
        } catch (ImportExportException ex) {
            DatabaseTransaction.rollback(conn);
            ex.printStackTrace();
        } catch (DaoException ex) {
            DatabaseTransaction.rollback(conn);
            ex.printStackTrace();
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
}
