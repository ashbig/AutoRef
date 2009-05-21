/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bean;

import controller.AddProgramController;
import dao.ResearcherDAO;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import transfer.ProgramdefinitionTO;
import transfer.ProgramtypeTO;
import transfer.ResearcherTO;
import util.Constants;

/**
 *
 * @author dzuo
 */
public class AddProgramBean {
    private String type;
    private UploadedFile file;
    private String name;
    private String description;
    private String format;
    private String message;
    
    public AddProgramBean() {
        reset();
    }
    
    public void reset() {
        setName(null);
        setDescription(null);
        setFormat(Constants.MAPFILE_NUMBER);
        setType(ProgramtypeTO.TYPE_PLATE96TO384);
    }
    
    public List<SelectItem> getAllPrograms() {
        List<SelectItem> programs = new ArrayList<SelectItem>();
        programs.add(new SelectItem(ProgramtypeTO.TYPE_PLATE96TO384));
        programs.add(new SelectItem(ProgramtypeTO.TYPE_384TOSLIDE));
        return programs;
    }

    public List getFileFormats() {
        List<SelectItem> formats = new ArrayList<SelectItem>();
        formats.add(new SelectItem(Constants.MAPFILE_NUMBER));
        formats.add(new SelectItem(Constants.MAPFILE_ALPHA));
        return formats;
    }
    
    public void addProgram() {        
        setMessage("Program is added.");
        boolean isnumber = false;
        if(Constants.MAPFILE_NUMBER.equals(format)) {
            isnumber = true;
        }
        
        AddProgramController controller = new AddProgramController();
        try {
            if(controller.programnameExist(name)) {
                setMessage("Program name has been used. Please enter a different name.");
                return;
            }
            
            String username = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getUserPrincipal().getName();
            ResearcherTO r = ResearcherDAO.getResearcher(username);
            ProgramdefinitionTO program = new ProgramdefinitionTO(name, description, type, ProgramdefinitionTO.STATUS_ACTIVE, 0, 0, null, r.getName());
            InputStream fileinput = file.getInputStream();
            controller.setProgram(program);
            controller.readProgramFile(fileinput, isnumber);
            fileinput.close();
            
            fileinput = file.getInputStream();
            String filename = file.getName();
            controller.persistProgram(fileinput, filename);
            fileinput.close();
            reset();
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage("Error occured.");
        }
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
