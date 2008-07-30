/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.FilereferenceDAO;
import dao.ProgramDAO;
import database.DatabaseTransaction;
import io.FileRepository;
import io.ProgramMappingFileParser;
import io.ProgramMappingFileParserException;
import io.StaticProgramMappingFileParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import transfer.FilereferenceTO;
import transfer.ProgramcontainerTO;
import transfer.ProgramdefinitionTO;
import transfer.ProgrammappingTO;
import transfer.ProgramtypeTO;

/**
 *
 * @author dzuo
 */
public class AddProgramController {
    private ProgramdefinitionTO program;

    public boolean programnameExist(String name) throws ControllerException {
        try {
            ProgramDAO dao = new ProgramDAO();
            return dao.programnameExist(name);
        } catch (Exception x) {
            x.printStackTrace();
            throw new ControllerException(x.getMessage());
        }
    }
    
    public void readProgramFile(InputStream file, boolean isNumber) throws ControllerException {
        String type = program.getType();
        ProgramMappingFileParser parser = StaticProgramMappingFileParserFactory.getParser(type);

        if (parser == null) {
            throw new ControllerException("Undefined program type: " + type);
        }

        List<ProgrammappingTO> l = null;
        try {
            l = parser.parseMappingFile(file, isNumber);
        } catch (ProgramMappingFileParserException ex) {
            ex.printStackTrace();
            throw new ControllerException("Error while reading program file.\n" + ex.getMessage());
        }
        
        program.setSourcenum(parser.getSrcContainers().size());
        program.setDestnum(parser.getDestContainers().size());
        program.setMappings(l);
        
        Collection srcs = parser.getSrcContainers();
        Iterator iter =srcs.iterator();
        int i=1;
        while(iter.hasNext()) {
            String s = (String)iter.next();
            ProgramcontainerTO c = new ProgramcontainerTO(program.getName(), s, parser.getSrccontainertype(), i, ProgramcontainerTO.INPUT);
            program.addProgramcontainer(c);
            i++;
        }
        
        Collection dests = parser.getDestContainers();
        iter = dests.iterator();
        i = 1;
        while(iter.hasNext()) {
            String s = (String)iter.next();
            ProgramcontainerTO c = new ProgramcontainerTO(program.getName(), s, parser.getDestcontainertype(), i, ProgramcontainerTO.OUTPUT);
            program.addProgramcontainer(c);
            i++;
        }
    }

    public void persistProgram(InputStream fileinput, String filename) throws ControllerException {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            throw new ControllerException("Cannot get database connection: " + ex.getMessage());
        }

        try {
            FilereferenceTO fileref  =new FilereferenceTO(filename.substring(filename.lastIndexOf("\\") + 1), FilereferenceTO.PATH, FilereferenceTO.TYPE_MAPPING);
            FileRepository.uploadFile(fileref, fileinput);
            
            FilereferenceDAO dao = new FilereferenceDAO(conn);
            List<FilereferenceTO> files = new ArrayList<FilereferenceTO>();
            files.add(fileref);
            dao.addFilereferences(files);
            
            ProgramDAO dao1 = new ProgramDAO(conn);
            dao1.addProgramdefinition(program);
            dao1.addFiles(program.getName(), files);

            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            ex.printStackTrace();
            throw new ControllerException("Cannot add program to the database.");
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }

    public ProgramdefinitionTO getProgram() {
        return program;
    }

    public void setProgram(ProgramdefinitionTO program) {
        this.program = program;
    }

    public static void main(String args[]) {
        String file = "C:\\dev\\test\\nappa\\384well_vertical.txt";
        String programtype = ProgramtypeTO.TYPE_PLATE96TO384;
        String programname = "Genmate program vertical";
        //String file = "C:\\dev\\test\\nappa\\testrunGenie132.gal";
        //String programtype = ProgramtypeTO.TYPE_384TOSLIDE;
        //String programname = "testrunGenie132";
        boolean isnumber = true;
        String description = "test";
        String researcher = "dzuo";
        
        InputStream fileinput = null;
        AddProgramController controller = new AddProgramController();
        try {
            ProgramdefinitionTO program = new ProgramdefinitionTO(programname, description, programtype, ProgramdefinitionTO.STATUS_ACTIVE, 0, 0, null, researcher);
            fileinput = new FileInputStream(new File(file));
            controller.setProgram(program);
            controller.readProgramFile(fileinput, isnumber);
            fileinput.close();
            
            fileinput = new FileInputStream(new File(file));
            controller.persistProgram(fileinput, file);
            fileinput.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
