/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.process;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import plasmid.coreobject.Clone;
import plasmid.coreobject.Container;
import plasmid.coreobject.Sample;
import plasmid.database.DatabaseManager.DefTableManager;
import plasmid.database.DatabaseManager.PlateManager;

/**
 *
 * @author Dongmei
 */
public class CloneStorageManager {

    public List getSelectedSamples(int[] sampleids, List samples) {
        List selectedSamples = new ArrayList();
        if (sampleids != null) {
            for (int i = 0; i < sampleids.length; i++) {
                int sampleid = sampleids[i];
                for (int n = 0; n < samples.size(); n++) {
                    Sample sample = (Sample) samples.get(n);
                    if (sample.getSampleid() == sampleid) {
                        selectedSamples.add(sample);
                        break;
                    }
                }
            }
        }
        return selectedSamples;
    }

    public List getNewContainerAndSamples(List clones) {
        List containers = new ArrayList();
        for (int i = 0; i < clones.size(); i++) {
            Clone clone = (Clone) clones.get(i);
            String label = clone.getNewTubeLabel();
            if (label != null && label.trim().length() > 0) {
                int containerid = DefTableManager.getNextid("containerid");
                int sampleid = DefTableManager.getNextid("sampleid");
                Container container = new Container(containerid, Container.TUBE, label.trim(), null, Container.BIOBANK, 1, Container.FILLED);
                Sample sample = new Sample(sampleid, Sample.WORKING_GLYCEROL, Sample.GOOD, clone.getCloneid(), 1, "A", "01", containerid, label);
                sample.setClone(clone);
                container.addSample(sample);
                containers.add(container);
            }
        }
        return containers;
    }

    public List checkContainerLabels(List containers, Connection conn) {
        List labels = new ArrayList();
        for (int i = 0; i < containers.size(); i++) {
            Container container = (Container) containers.get(i);
            labels.add(container.getLabel());
        }

        PlateManager manager = new PlateManager(conn);
        List existContainers = manager.queryContainers(labels, false);
        List existLabels = new ArrayList();
        if (existContainers != null) {
            for (int i = 0; i < existContainers.size(); i++) {
                Container c = (Container) existContainers.get(i);
                existLabels.add(c.getLabel());
            }
        }
        return existLabels;
    }
    
    public List checkDuplicateLabels(List containers){
        List labels = new ArrayList();
        List duplicates = new ArrayList();
        for(int i=0; i<containers.size(); i++) {
            Container container = (Container) containers.get(i);
            String label = container.getLabel();
            if(isDuplicate(labels, label)) {
                duplicates.add(label);
            }
            labels.add(label);
        }
        return duplicates;
    }

    private boolean isDuplicate(List labels, String label) {
        for(int i=0; i<labels.size(); i++) {
            String l = (String)labels.get(i);
            if(l.trim().equals(label.trim())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean updateCloneStorage(List containers, List samples, Connection conn) {
        PlateManager manager = new PlateManager(conn);
        if (removeCloneStorage(samples, manager) && addCloneStorage(containers, manager)) {
            return true;
        }
        return false;
    }

    public boolean removeCloneStorage(List samples, PlateManager manager) {
        List containers = new ArrayList();
        for (int i = 0; i < samples.size(); i++) {
            Sample sample = (Sample) samples.get(i);
            if (Container.TUBE.equals(sample.getContainerType())) {
                Container container = new Container();
                container.setContainerid(sample.getContainerid());
                containers.add(container);
            }
        }

        if (manager.updateSampleType(samples, Sample.CONTAMINATED) && manager.updateContainerLocation(containers, Container.TRASH)) {
            return true;
        }
        return false;
    }

    public boolean addCloneStorage(List containers, PlateManager manager) {
        return manager.insertPlateAndSample(containers);
    }
}
