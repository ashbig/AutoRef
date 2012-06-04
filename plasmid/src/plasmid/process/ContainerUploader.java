/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.process;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import plasmid.coreobject.Clone;
import plasmid.coreobject.Container;
import plasmid.coreobject.Location;
import plasmid.coreobject.Sample;
import plasmid.database.DatabaseException;
import plasmid.database.DatabaseManager.CloneManager;
import plasmid.database.DatabaseManager.DefTableManager;
import plasmid.database.DatabaseManager.PlateManager;
import plasmid.database.DatabaseTransaction;
import plasmid.util.PlatePositionConvertor;

/**
 *
 * @author dongmei
 */
public class ContainerUploader {

    private String containertype;
    private String location;
    private String sampletype;
    private List<String> clonenames;

    public static List<String> getContainertypes() {
        List<String> types = new ArrayList<String>();
        types.add(Container.COSTAR_FLT);
        types.add(Container.COSTAR_RD);
        types.add(Container.GREINER);
        types.add(Container.MICRONIC96TUBEMP16);
        types.add(Container.PLATE_384);
        types.add(Container.TUBE);
        return types;
    }
    
    public static List<String> getSampletypes() {
        List<String> types = new ArrayList<String>();
        types.add(Sample.WORKING_GLYCEROL);
        types.add(Sample.DEEP_ARCHIVE_GLYCEROL);
        return types;
    }
    
    public static List<String> getLocations() {
        List<String> locations = new ArrayList<String>();
        locations.add(Location.BIOBANK);
        locations.add(Location.FREEZER);
        return locations;
    }

    public List<Container> readContainerFile(InputStream input) throws ProcessException {
        List<Container> plates = new ArrayList<Container>();
        String currentLabel = null;
        String lastLabel = null;
        Container c = null;
        clonenames = new ArrayList<String>();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line = in.readLine();
            while ((line = in.readLine()) != null) {
                String[] s = line.split("\t");
                currentLabel = s[0];
                if (!currentLabel.equals(lastLabel)) {
                    c = new Container();
                    int containerid = DefTableManager.getNextid("containerid");
                    c.setContainerid(containerid);
                    c.setType(getContainertype());
                    c.setCapacity(Container.getCapacity(getContainertype()));
                    c.setLocation(getLocation());
                    c.setLabel(currentLabel);
                    c.setOricontainerid(currentLabel);
                    c.setStatus(Container.FILLED);

                    for (int j = 0; j < Container.getCapacity(c.getType()); j++) {
                        Sample sample = new Sample();
                        int sampleid = DefTableManager.getNextid("sampleid");
                        sample.setSampleid(sampleid);
                        sample.setType(Sample.EMPTY);
                        sample.setStatus(Sample.GOOD);
                        sample.setPositions(j + 1, Container.getRow(getContainertype()), Container.getCol(getContainertype()));
                        sample.setContainerid(containerid);
                        sample.setContainerlabel(c.getLabel());
                        c.addSample(sample);
                    }

                    plates.add(c);
                    lastLabel = currentLabel;
                }

                String well = s[1];
                int p = PlatePositionConvertor.convertWellToVPos(well, Container.getRow(getContainertype()));
                Sample currentSample = c.getSample(p);
                if(!currentSample.getType().equals(Sample.EMPTY)) {
                    throw new ProcessException("Container "+lastLabel+" Well "+well+" has duplicate samples.");
                }
                currentSample.setType(getSampletype());

                String cloneid = s[2];
                Clone clone = new Clone();
                clone.setName(cloneid);
                currentSample.setClone(clone);
                getClonenames().add(cloneid);
            }
        } catch (ProcessException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ProcessException("Error reading file.");
        }
        return plates;
    }

    public List<String> retriveClones(List<Container> containers, List<String> names) throws ProcessException {
        DatabaseTransaction t = null;
        Connection c = null;
        List<String> nofound = new ArrayList<String>();
        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            CloneManager manager = new CloneManager(c);
            List<Clone> clones = manager.getCloneInfoByClonenames(names);
            
            for(Container container:containers) {
                List<Sample> samples = container.getSamples();
                for(Sample s:samples) {
                    Clone clone = s.getClone();
                    if(clone != null) {
                        Clone found = foundClone(clone.getName(), clones);
                        if(found==null) {
                            nofound.add(clone.getName());
                        } else {
                            clone.setCloneid(found.getCloneid());
                            s.setCloneid(found.getCloneid());
                        }
                    }
                }
            }
            return nofound;
        } catch (DatabaseException ex) {
            throw new ProcessException(ex.getMessage());
        } catch (Exception ex) {
            throw new ProcessException("Error occured whiling importing containers.");
        } finally {
            DatabaseTransaction.closeConnection(c);
        }
    }
    
    private Clone foundClone(String name, List<Clone> clones) {
        if(name == null || clones==null) {
            return null;
        }
        
        for(Clone clone:clones) {
            if(clone.getName().trim().equals(name.trim()))
                return clone;
        }
        return null;
    }
    
    public void importContainers(List<Container> containers) throws ProcessException {
        DatabaseTransaction t = null;
        Connection c = null;
        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            PlateManager manager = new PlateManager(c);
            if(manager.insertPlateAndSample(containers)) {
                DatabaseTransaction.commit(c);
            } else {
                DatabaseTransaction.rollback(c);
                throw new ProcessException("Error occured whiling updating database.");
            }
        } catch (DatabaseException ex) {
            throw new ProcessException(ex.getMessage());
        } catch (Exception ex) {
            throw new ProcessException("Error occured whiling importing containers.");
        } finally {
            DatabaseTransaction.closeConnection(c);
        }
    }
    
    /**
     * @return the containertype
     */
    public String getContainertype() {
        return containertype;
    }

    /**
     * @param containertype the containertype to set
     */
    public void setContainertype(String containertype) {
        this.containertype = containertype;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the sampletype
     */
    public String getSampletype() {
        return sampletype;
    }

    /**
     * @param sampletype the sampletype to set
     */
    public void setSampletype(String sampletype) {
        this.sampletype = sampletype;
    }

    /**
     * @return the clonenames
     */
    public List<String> getClonenames() {
        return clonenames;
    }
}
