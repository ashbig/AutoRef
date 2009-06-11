/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package process;

import core.Slidecontainerlineageinfo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import transfer.SlidecelllineageTO;
import transfer.ContainercellTO;
import transfer.LayoutcontainerTO;
import transfer.ProgramcontainerTO;
import transfer.ProgramdefinitionTO;
import transfer.ProgrammappingTO;
import transfer.ReagentTO;
import transfer.SlidecellTO;
import transfer.SlidelayoutTO;
import transfer.SlidetemplateTO;
import util.Constants;

/**
 *
 * @author dzuo
 */
public class SlideDesignManager {

    private List<LayoutcontainerTO> containers;
    private List<Slidecontainerlineageinfo> containerlineageinfo;
    private LayoutcontainerTO slide;
    private List<String> labelsInMap1;
    private List<String> labelsInMap2;
    private List<Integer> levels;

    public SlidelayoutTO designLayout(String name, String desc, String researcher, String status, String program1, String program2) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        String date = format.format(new java.util.Date());
        return new SlidelayoutTO(name, desc, date, researcher, status, program1, program2);
    }

    public SlidetemplateTO designTemplate(SlidelayoutTO layout, String name, String desc, String researcher, String status) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        String date = format.format(new java.util.Date());
        return new SlidetemplateTO(layout, name, desc, researcher, date, status);
    }

    public void designLayoutContainers(String name, ProgramdefinitionTO program1, ProgramdefinitionTO program2) throws ProcessException {
        List<ProgramcontainerTO> src1 = program1.getContainersByIO(ProgramcontainerTO.INPUT);
        List<ProgramcontainerTO> dest1 = program1.getContainersByIO(ProgramcontainerTO.OUTPUT);
        List<ProgramcontainerTO> src2 = program2.getContainersByIO(ProgramcontainerTO.INPUT);

        containers = new ArrayList<LayoutcontainerTO>();
        containerlineageinfo = new ArrayList<Slidecontainerlineageinfo>();
        Iterator iter = src2.iterator();
        labelsInMap1 = new ArrayList<String>();
        labelsInMap2 = new ArrayList<String>();
        levels = new ArrayList<Integer>();
        for (int i = 0; i < src2.size() / dest1.size(); i++) {
            Slidecontainerlineageinfo info = new Slidecontainerlineageinfo();
            for (ProgramcontainerTO container : src1) {
                LayoutcontainerTO src = new LayoutcontainerTO(name, container.getName(), container.getType(), ReagentTO.getTYPE_CLONE(), container.getOrder(), i + 1, container.getIoflag());
                info.addToFrom(src);
                containers.add(src);
            }

            for (ProgramcontainerTO container : dest1) {
                ProgramcontainerTO c = (ProgramcontainerTO) iter.next();
                LayoutcontainerTO dest = new LayoutcontainerTO(name, c.getName(), c.getType(), ReagentTO.getTYPE_CLONE(), container.getOrder(), i + 1, container.getIoflag());
                info.addToTo(dest);
                containers.add(dest);
                labelsInMap1.add(container.getName());
                labelsInMap2.add(c.getName());
                levels.add(new Integer(i + 1));
            }
            containerlineageinfo.add(info);
        }
    }

    public LayoutcontainerTO findContainer(List<LayoutcontainerTO> containers, String label, String io, int level) {
        for (LayoutcontainerTO c : containers) {
            if (c.getName().equals(label) && c.getIoflag().equals(io) && c.getLevel() == level) {
                return c;
            }
        }
        return null;
    }

    public String findLabelInMap1(String label2) {
        int i = 0;
        for (String l : labelsInMap2) {
            if (l.equals(label2)) {
                return (String) labelsInMap1.get(i);
            }
            i++;
        }
        return null;
    }

    public ProgrammappingTO findMapping(List<ProgrammappingTO> mappings, String plate, int pos) {
        for (ProgrammappingTO m : mappings) {
            if (m.getDestplate().equals(plate) && m.getDestpos() == pos) {
                return m;
            }
        }
        return null;
    }

    public int findLevel(String label2) {
        int i = 0;
        for (String l : labelsInMap2) {
            if (l.equals(label2)) {
                return ((Integer) levels.get(i)).intValue();
            }
            i++;
        }
        return -1;
    }

    public void designLayoutMappings(String name, ProgramdefinitionTO program1, ProgramdefinitionTO program2) throws ProcessException {
        List<ProgrammappingTO> mappings1 = program1.getMappings();
        List<ProgrammappingTO> mappings2 = program2.getMappings();
        slide = new LayoutcontainerTO(name, "slide", LayoutcontainerTO.SLIDE, ReagentTO.getTYPE_CLONE(), 1, 1, LayoutcontainerTO.OUTPUT);

        for (ProgrammappingTO m : mappings2) {
            boolean isSlidecellEmpty = false;
            String label384 = m.getSrcplate();
            if (Constants.NA.equals(label384)) {
                isSlidecellEmpty = true;
            }

            SlidecellTO slideCell = new SlidecellTO(m.getDestpos(), m.getDestwellx(), m.getDestwelly(), ContainercellTO.TYPE_EMPTY, m.getDestblocknum(), m.getDestblockrow(), m.getDestblockcol(), m.getDestblockposx(), m.getDestblockposy(), m.getDestblockwellx(), m.getDestblockwelly());
            slideCell.setContainerlabel(slide.getName());
            SlidecelllineageTO slidelineage = new SlidecelllineageTO(slideCell);
            slide.addCell(slidelineage);

            if (!isSlidecellEmpty) {
                int level = findLevel(label384);
                if (level < 0) {
                    throw new ProcessException("Invalide label: " + label384);
                }
                LayoutcontainerTO plate384 = findContainer(getContainers(), label384, ProgramcontainerTO.OUTPUT, level);
                if (plate384 == null) {
                    throw new ProcessException("Cannot find 384-well container from mapping: " + label384);
                }
                ContainercellTO cell384 = new ContainercellTO(m.getSrcpos(), m.getSrcwellx(), m.getSrcwelly(), plate384.getSampletype(), plate384.getOrder(), 0);
                cell384.setContainerlabel(label384);
                slideCell.setType(cell384.getType());

                String label = findLabelInMap1(label384);
                ProgrammappingTO m1 = findMapping(mappings1, label, m.getSrcpos());
                if (m1 == null) {
                    throw new ProcessException("Cannot find mapping from 96-well plate to 384-well plate for plate " + label + " and pos " + m.getSrcpos());
                }
                String label96 = m1.getSrcplate();
                LayoutcontainerTO plate96 = findContainer(getContainers(), label96, ProgramcontainerTO.INPUT, level);
                if (plate96 == null) {
                    throw new ProcessException("Cannot find 96-well container from mapping: " + label96);
                }
                ContainercellTO cell96 = new ContainercellTO(m1.getSrcpos(), m1.getSrcwellx(), m1.getSrcwelly(), plate96.getSampletype(), plate96.getOrder(), 0);
                cell96.setContainerlabel(level + "-" + label96);

                slidelineage.addPre(cell384);
                slidelineage.addPre(cell96);

                SlidecelllineageTO lineage384 = new SlidecelllineageTO(cell384);
                lineage384.addPre(cell96);
                lineage384.addPost(slideCell);
                plate384.addCell(lineage384);

                SlidecelllineageTO lineage96 = new SlidecelllineageTO(cell96);
                lineage96.addPost(cell384);
                lineage96.addPost(slideCell);
                plate96.addCell(lineage96);
            }
        }
    }

    public void changeControls(List<LayoutcontainerTO> containers) {
        for (LayoutcontainerTO c : containers) {
            String type = c.getSampletype();
            List<SlidecelllineageTO> lineages = c.getCells();
            for (SlidecelllineageTO l : lineages) {
                ContainercellTO containercell = l.getCell();
                containercell.setType(type);
                String controlreagent = containercell.getControlreagent();
                if (controlreagent == null) {
                    controlreagent = ReagentTO.NON_SPOTS;
                }
                containercell.setControlreagent(controlreagent);
                List<ContainercellTO> pre = l.getPre();
                for (ContainercellTO cell : pre) {
                    cell.setType(type);
                    cell.setControlreagent(controlreagent);
                }

                List<ContainercellTO> post = l.getPost();
                for (ContainercellTO cell : post) {
                    cell.setType(type);
                    cell.setControlreagent(controlreagent);
                }
            }
        }
    }

    public List<LayoutcontainerTO> getContainers() {
        return containers;
    }

    public void setContainers(List<LayoutcontainerTO> containers) {
        this.containers = containers;
    }

    public List<Slidecontainerlineageinfo> getContainerlineageinfo() {
        return containerlineageinfo;
    }

    public void setContainerlineageinfo(List<Slidecontainerlineageinfo> containerlineageinfo) {
        this.containerlineageinfo = containerlineageinfo;
    }

    public LayoutcontainerTO getSlide() {
        return slide;
    }

    public void setSlide(LayoutcontainerTO slide) {
        this.slide = slide;
    }

    public List<String> getLabelsInMap1() {
        return labelsInMap1;
    }

    public void setLabelsInMap1(List<String> labelsInMap1) {
        this.labelsInMap1 = labelsInMap1;
    }

    public List<String> getLabelsInMap2() {
        return labelsInMap2;
    }

    public void setLabelsInMap2(List<String> labelsInMap2) {
        this.labelsInMap2 = labelsInMap2;
    }

    public List<Integer> getLevels() {
        return levels;
    }

    public void setLevels(List<Integer> levels) {
        this.levels = levels;
    }
}
