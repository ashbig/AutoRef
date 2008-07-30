/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import core.Slidecontainerlineageinfo;
import dao.ProgramDAO;
import dao.SlidedesignDAO;
import database.DatabaseTransaction;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import process.SlideDesignManager;
import transfer.SlidecelllineageTO;
import transfer.ContainercellTO;
import transfer.LayoutcontainerTO;
import transfer.ProgramdefinitionTO;
import transfer.SlidelayoutTO;
import transfer.SlidetemplateTO;

/**
 *
 * @author dzuo
 */
public class SlideDesignController implements Serializable {
    public boolean checkLayoutName(String name) throws ControllerException {
        SlidedesignDAO dao = new SlidedesignDAO();
        try {
            return dao.isLayoutexist(name);
        } catch (Exception ex) {
            throw new ControllerException(ex.getMessage());
        }
    }
    public boolean checkTemplateName(String name) throws ControllerException {
        SlidedesignDAO dao = new SlidedesignDAO();
        try {
            return dao.isTemplateExist(name);
        } catch (Exception ex) {
            throw new ControllerException(ex.getMessage());
        }
    }
    
    public SlidelayoutTO designSlideLayout(String name, String description, String researcher, String program1, String program2) {
        SlideDesignManager manager = new SlideDesignManager();
        return manager.designLayout(name, description, researcher, SlidelayoutTO.STATUS_ACTIVE, program1, program2);
    }

    public void designSlideLayoutMap(SlidelayoutTO layout) throws ControllerException {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ControllerException("Cannot get database connection.");
        }

        SlideDesignManager manager = new SlideDesignManager();
        ProgramDAO dao = new ProgramDAO(conn);

        try {
            ProgramdefinitionTO programdef1 = dao.getProgram(layout.getProgram1());
            ProgramdefinitionTO programdef2 = dao.getProgram(layout.getProgram2());

            manager.designLayoutContainers(layout.getName(), programdef1, programdef2);
            manager.designLayoutMappings(layout.getName(), programdef1, programdef2);

            layout.setLineageinfo(manager.getContainerlineageinfo());
            layout.setSlide(manager.getSlide());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ControllerException(ex.getMessage());
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }

    public void saveLayout(SlidelayoutTO layout) throws ControllerException {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ControllerException("Cannot get database connection.");
        }

        SlidedesignDAO dao = new SlidedesignDAO(conn);
        try {
            dao.addSlidelayout(layout);
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            throw new ControllerException(ex.getMessage());
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }

    public List<SlidelayoutTO> getLayouts() throws ControllerException {
        return getLayouts("");
    }

    public List<SlidelayoutTO> getLayouts(String name) throws ControllerException {
        SlidedesignDAO dao = new SlidedesignDAO();
        List<SlidelayoutTO> layouts = null;
        try {
            layouts = dao.getSlidelayouts(name);
        } catch (Exception ex) {
            throw new ControllerException(ex.getMessage());
        }

        return layouts;
    }

    public SlidelayoutTO getLayout(String name) throws ControllerException {
        SlidedesignDAO dao = new SlidedesignDAO();
        SlidelayoutTO layout = null;
        try {
            layout = dao.getSlidelayout(name);
        } catch (Exception ex) {
            throw new ControllerException(ex.getMessage());
        }

        return layout;
    }

    public void setControls(SlidelayoutTO layout) {
        List<LayoutcontainerTO> controls = layout.getControls();
        List<Slidecontainerlineageinfo> info = layout.getLineageinfo();
        List<LayoutcontainerTO> containers = new ArrayList<LayoutcontainerTO>();

        for (Slidecontainerlineageinfo lineage : info) {
            List<LayoutcontainerTO> from = lineage.getFrom();

            int count = 0;
            for (LayoutcontainerTO c : from) {
                LayoutcontainerTO controlcontainer = findContainer(controls, c);
                if (controlcontainer != null) {
                    c.setIscontrol(true);
                    c.setContainerid(controlcontainer.getContainerid());
                    setControllcells(c, controlcontainer);
                    containers.add(c);
                    count++;
                }
            }
            if (count != 0 && count == from.size()) {
                List<LayoutcontainerTO> to = lineage.getTo();
                for (LayoutcontainerTO c : to) {
                    c.setIscontrol(true);
                }
            }
        }
        this.changeControls(containers);
        layout.setControls(containers);
    }

    public void changeControls(List<LayoutcontainerTO> containers) {
        SlideDesignManager manager = new SlideDesignManager();
        manager.changeControls(containers);
    }

    public void setControllcells(LayoutcontainerTO container, LayoutcontainerTO controlcontainer) {
        List<SlidecelllineageTO> controlcells = controlcontainer.getCells();
        List<SlidecelllineageTO> cells = container.getCells();
        for(SlidecelllineageTO cell:controlcells) {
            SlidecelllineageTO c = findcell(cell, cells);
            if(c != null) {
                String control = cell.getCell().getControlreagent();
                c.getCell().setControlreagent(control);
                List<ContainercellTO> pre = c.getPre();
                for(ContainercellTO p:pre) {
                    p.setControlreagent(control);
                }
                List<ContainercellTO> post = c.getPost();
                for(ContainercellTO p:post) {
                    p.setControlreagent(control);
                }
            }
        }
    }

    public SlidecelllineageTO findcell(SlidecelllineageTO cell, List<SlidecelllineageTO> cells) {
        for(SlidecelllineageTO c:cells) {
            if(c.getCell().getPos() == cell.getCell().getPos())
                return c;
        }
        return null;
    }
    public LayoutcontainerTO findContainer(List<LayoutcontainerTO> containers, LayoutcontainerTO container) {
        for (LayoutcontainerTO c : containers) {
            if (c.getName().equals(container.getName()) && c.getOrder() == container.getOrder() && c.getLevel() == container.getLevel()) {
                return c;
            }
        }
        return null;
    }

    public SlidetemplateTO designSlideTemplate(SlidelayoutTO layout, String name, String description, String researcher) {
        SlideDesignManager manager = new SlideDesignManager();
        return manager.designTemplate(layout, name, description, researcher, SlidelayoutTO.STATUS_ACTIVE);
    }

    public void saveTemplate(SlidetemplateTO template) throws ControllerException {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ControllerException("Cannot get database connection.");
        }

        SlidedesignDAO dao = new SlidedesignDAO(conn);
        try {
            dao.addSlideTemplate(template);
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            throw new ControllerException(ex.getMessage());
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }

    public SlidetemplateTO getTemplate(String name) throws ControllerException {
        SlidedesignDAO dao = new SlidedesignDAO();
        SlidetemplateTO template = null;
        try {
            template = dao.getSlidetemplate(name);
        } catch (Exception ex) {
            throw new ControllerException(ex.getMessage());
        }

        return template;
    }

    public List<SlidetemplateTO> getTemplates(String name) throws ControllerException {
        SlidedesignDAO dao = new SlidedesignDAO();
        List<SlidetemplateTO> templates = null;
        try {
            templates = dao.getSlidetemplates(name);
        } catch (Exception ex) {
            throw new ControllerException(ex.getMessage());
        }

        return templates;
    }

    public static void main(String args[]) {
        String name = "test design";
        String desc = "this is a test program.";
        String date = "Jun-12-2008";
        String researcher = "dzuo";
        String status = "Active";
        String program1Name = "Genmate program 1";
        String program2Name = "testrunGenie132";

        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        SlideDesignManager manager = new SlideDesignManager();
        ProgramDAO dao = new ProgramDAO(conn);
        try {
            ProgramdefinitionTO program1 = dao.getProgram(program1Name);
            ProgramdefinitionTO program2 = dao.getProgram(program2Name);

            manager.designLayout(name, desc, researcher, status, program1Name, program2Name);
            manager.designLayoutContainers(name, program1, program2);
            manager.designLayoutMappings(name, program1, program2);

            List<Slidecontainerlineageinfo> info = manager.getContainerlineageinfo();
            LayoutcontainerTO control = null;
            System.out.println("Before setting control:");
            for (Slidecontainerlineageinfo l : info) {
                List<LayoutcontainerTO> from = l.getFrom();
                for (LayoutcontainerTO c : from) {
                    System.out.println(c.getName() + "\t" + c.getOrder() + "\t" + c.getLevel() + "\t" + c.getSampletype());
                    List<SlidecelllineageTO> lineages = c.getCells();
                    for (SlidecelllineageTO lineage : lineages) {
                        ContainercellTO cell = lineage.getCell();
                        System.out.println("\t" + cell.getType() + "\t" + cell.getPosx() + "\t" + cell.getPosy());
                        List<ContainercellTO> cells = lineage.getPre();
                        for (ContainercellTO precell : cells) {
                            System.out.println("\t" + precell.getType() + "\t" + precell.getPosx() + "\t" + precell.getPosy());
                        }
                    }
                }
                List<LayoutcontainerTO> to = l.getTo();
                for (LayoutcontainerTO c : to) {
                    System.out.println(c.getName() + "\t" + c.getOrder() + "\t" + c.getLevel() + "\t" + c.getSampletype());
                    control = c;
                    List<SlidecelllineageTO> lineages = c.getCells();
                    for (SlidecelllineageTO lineage : lineages) {
                        ContainercellTO cell = lineage.getCell();
                        System.out.println("\t" + cell.getType() + "\t" + cell.getPosx() + "\t" + cell.getPosy());
                        List<ContainercellTO> cells = lineage.getPre();
                        for (ContainercellTO precell : cells) {
                            System.out.println("\t" + precell.getType() + "\t" + precell.getPosx() + "\t" + precell.getPosy());
                        }
                    }
                }
            }

            List<LayoutcontainerTO> controls = new ArrayList<LayoutcontainerTO>();
            controls.add(control);
            manager.changeControls(controls);
            System.out.println("After setting control:");
            for (Slidecontainerlineageinfo l : info) {
                List<LayoutcontainerTO> from = l.getFrom();
                for (LayoutcontainerTO c : from) {
                    System.out.println(c.getName() + "\t" + c.getOrder() + "\t" + c.getLevel() + "\t" + c.getSampletype());
                    List<SlidecelllineageTO> lineages = c.getCells();
                    for (SlidecelllineageTO lineage : lineages) {
                        ContainercellTO cell = lineage.getCell();
                        System.out.println("\t" + cell.getType() + "\t" + cell.getPosx() + "\t" + cell.getPosy());
                        List<ContainercellTO> cells = lineage.getPre();
                        for (ContainercellTO precell : cells) {
                            System.out.println("\t" + precell.getType() + "\t" + precell.getPosx() + "\t" + precell.getPosy());
                        }
                    }
                }
                List<LayoutcontainerTO> to = l.getTo();
                for (LayoutcontainerTO c : to) {
                    System.out.println(c.getName() + "\t" + c.getOrder() + "\t" + c.getLevel() + "\t" + c.getSampletype());
                    List<SlidecelllineageTO> lineages = c.getCells();
                    for (SlidecelllineageTO lineage : lineages) {
                        ContainercellTO cell = lineage.getCell();
                        System.out.println("\t" + cell.getType() + "\t" + cell.getPosx() + "\t" + cell.getPosy());
                        List<ContainercellTO> cells = lineage.getPre();
                        for (ContainercellTO precell : cells) {
                            System.out.println("\t" + precell.getType() + "\t" + precell.getPosx() + "\t" + precell.getPosy());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        System.exit(0);
    }
}
