/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.process;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import plasmid.coreobject.CloneAnalysis;
import plasmid.coreobject.CloneInformation;
import plasmid.coreobject.CloneVector;
import plasmid.coreobject.Gene2Refseq;
import plasmid.coreobject.Geneinfo;
import plasmid.coreobject.VectorProperty;
import plasmid.database.DatabaseException;
import plasmid.database.DatabaseManager.CloneManager;
import plasmid.database.DatabaseManager.GeneQueryManager;
import plasmid.database.DatabaseManager.VectorManager;

/**
 *
 * @author Lab User
 */
public class GeneSearchManager {
    public static final String TYPE_CDS = CloneAnalysis.TYPE_CDS;
    public static final String TYPE_NT = CloneAnalysis.TYPE_NT;
    public static final String TYPE_AA = CloneAnalysis.TYPE_AA;
    
    public List<Geneinfo> searchGenes(List<String> terms, boolean isCaseSensitive, List<String> vptypes, List<String> restrictions, List<String> species) throws DatabaseException {
        if(terms == null || terms.isEmpty()) {
            return null;
        }      
        return GeneQueryManager.queryGenesWithRefseqs(terms, isCaseSensitive, vptypes, restrictions, species);
    }
    
    public List<Gene2Refseq> searchRefseqs(int geneid, List<String> vptypes, List<String> restrictions) throws DatabaseException, IOException {
        String vptype = null;
        return GeneQueryManager.queryGene2Refseqs(geneid, vptypes, restrictions);
    }
    
    public List<CloneInformation> searchClones(String gi, List<String> vptypes, List<String> restrictions) throws DatabaseException {
        String vptype = null;
        return GeneQueryManager.queryClones(gi, vptypes, restrictions);
    }
    
    /**
    public List<CloneInformation> updateClones(List<CloneInformation> clones, List<String> vptypes) throws DatabaseException {
        String vptype = null;
        if(vptypes != null && vptypes.size()>0) {
            vptype = StringConvertor.convertFromListToSqlString(vptypes);
        }
        List<CloneVector> vectors = GeneQueryManager.queryVectorByType(vptype);
        List<CloneInformation> clones2 = new ArrayList<CloneInformation>();
        for(CloneInformation clone:clones) {
            if(found(vectors, clone.getVectorid())) {
                clones2.add(clone);
            }
        }
        return clones2;
    }
    */
    private boolean found (List<CloneVector> vectors, int vectorid) {
        for(CloneVector v:vectors) {
            if(v.getVectorid()==vectorid)
                return true;
        }
        return false;
    }
    
    public static Map<String,Map> getAllVptypes() throws DatabaseException {
        Map<String, Map> rt = new TreeMap<String,Map>();
        Map types = VectorManager.getAllVectorPerpertyTypes();
        Set keys = types.keySet();
        Iterator iter = keys.iterator();
        while(iter.hasNext()) {
            Map<String,String> m = new HashMap();
            String k = (String)iter.next();
            List<VectorProperty> vps = (List)types.get(k);
            for(VectorProperty vp:vps) {
                m.put(vp.getDisplayValue(), vp.getPropertyType());
            }
            rt.put(k, m);
        }
        return rt;
    }

    public String getBlastOutput(int cloneid, int gi, String clonename, String accession, String type) 
            throws IOException, ProcessException, Exception {
        CloneManager cm = new CloneManager();
        String cloneseq = cm.queryCloneSequenceByCloneid(cloneid);
        if(cloneseq == null || cloneseq.length()==0) {
            throw new ProcessException("Cannot get clone sequence.");
        }
        String seq = GeneQueryManager.querySequence(gi, type);
        if(seq == null || seq.length()==0) {
            throw new ProcessException("Cannot get gene sequence.");
        }
        
        BlastManager manager = new BlastManager();
        return manager.runPairwiseBlast(clonename, cloneseq, accession, seq, type);
    }
}
