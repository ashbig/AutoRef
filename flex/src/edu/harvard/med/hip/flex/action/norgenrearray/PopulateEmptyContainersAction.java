/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 

package edu.harvard.med.hip.flex.action.norgenrearray;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.form.*;
 import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.action.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.file.*;
/**
 *
 * @author  dzuo
 * @version
 */
public class PopulateEmptyContainersAction extends ResearcherAction {
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
         System.out.println("l1");
        
        NorgenRearrayForm curForm = (NorgenRearrayForm)form;
       int sourceLocation = curForm.getSourceLocation(); 
        System.out.println("l2");
       FormFile logFile = curForm.getLogFile();
       
       //can come as user params
         int         max_number_of_wells_per_plate=95;
       boolean     all_wells_seq=true;
       String sample_type=Sample.ISOLATE;
       
       
        ArrayList<NorgenDestinationPlate> dest_plates =null;
                
        int projectid = curForm.getProjectid();
         System.out.println("l2");
        int workflowid = curForm.getWorkflowid();
         System.out.println("l");
        int protocolid = curForm.getProtocolid();
        
        Researcher researcher = null;Connection conn=null;
             // Validate the researcher barcode.
        System.out.println("l");
        try {
            researcher = new Researcher(curForm.getResearcherBarcode());
        } catch (FlexProcessException ex) {
            request.setAttribute("workflowid", new Integer(workflowid));
            request.setAttribute("projectid", new Integer(projectid));
            
            errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", curForm.getResearcherBarcode()));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } catch (FlexDatabaseException ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
        System.out.println("LLL");
        try
        {
            NorgenLogFile parced_file;String err_message;
             //read file & verify its content
             parced_file = new NorgenLogFile();
             System.out.println("KKKK");
            parced_file.parseLogFile(  logFile.getInputStream());
            dest_plates = parced_file.getDestinationPlates();
            System.out.println(dest_plates.size());
            if (dest_plates != null)
            {
                for (NorgenDestinationPlate plate : dest_plates)
                {
                      err_message =  plate.verify();
                       System.out.println(err_message);
                      if (err_message!= null)
                      { errors.add("cultureFile", new ActionError("flex.infoimport.file", "Wells are not in sequence: "+err_message));
                        saveErrors(request,errors); 
                        return new ActionForward(mapping.getInput()); 
                      }
                    if ( plate.getRecords().size() != max_number_of_wells_per_plate)
                    {
                        err_message= "Well number over the limit: "+plate.getLabel()+" : "+plate.getRecords().size();
                        System.out.println(err_message);
                        errors.add("cultureFile", new ActionError("flex.infoimport.file", err_message));
                        saveErrors(request,errors); 
                        return new ActionForward(mapping.getInput()); 
                    }
                 }
            }
        }
         catch (Exception ex) {
                errors.add("cultureFile", new ActionError("flex.infoimport.file", ex.getMessage()));
                saveErrors(request,errors);
                return new ActionForward(mapping.getInput());}
        
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            Project project = new Project(projectid);
            Workflow workflow = new Workflow(workflowid);
            Protocol protocol = new Protocol(curForm.getProtocolname());
            Vector nextProtocols = workflow.getNextProtocol(protocol);
            int next_protocolid=((Protocol)nextProtocols.get(0)).getId();
            synchronized (this)
            {  
                 verifyPlates(conn,dest_plates, projectid, workflowid,protocolid );
                
                edu.harvard.med.hip.flex.process.Process process = 
                         new edu.harvard.med.hip.flex.process.Process(protocol, 
                         edu.harvard.med.hip.flex.process.Process.SUCCESS, 
                         researcher, project, workflow);
                 process.insert(conn);
                  getDataForOrgPlates(conn,dest_plates);
                 populatePlatesWithSamples(conn,dest_plates ,sample_type,process.getExecutionid());
                 putItemsOnQueue(conn,dest_plates, projectid, workflowid,protocolid, next_protocolid);
                 Container container=null;
                  ArrayList labels = new ArrayList();
                 for( NorgenDestinationPlate dest_plate:   dest_plates)
                 {
                     container = new Container(dest_plate.getId(), null, null, dest_plate.getLabel());
                      FileReference fileRef =   handleFileReference(conn, logFile, container, FileReference.NORGREN_LOG_FILE);
                     labels.add(container.getLabel())    ;
                 }
                 conn.commit();
                  request.setAttribute("LABELS", labels);
                  
            }
             
           //build process record
            
            return (mapping.findForward("success"));
        }
    catch(Exception e)
    {
            DatabaseTransaction.rollback(conn);
            System.out.println(e.getMessage());
            errors.add("cultureFile", new ActionError("flex.infoimport.file", e.getMessage()));
            saveErrors(request,errors);
               
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }

    ////////////////////////////////////
    public static void       getDataForOrgPlates(Connection conn,ArrayList<NorgenDestinationPlate> dest_plates)
            throws Exception
    {
          String sql="select label, sampleid, status_gb,sampletype,containerposition , constructid, c.containerid as containerid, cloneid "
        +" from containerheader c, sample s where s.containerid=c.containerid and label=? and containerposition=?";
          
          PreparedStatement stmt = null;ResultSet rs;
         try {
            stmt = conn.prepareStatement(sql );
            for( NorgenDestinationPlate dest_plate:   dest_plates)
            {
                for (NorgenLogFileRecord record: dest_plate.getRecords())
                {
                    stmt.setString(1, record.getOrgLabel());
                    stmt.setInt(2, record.getOrgPosition());
                    rs=DatabaseTransaction.executeQuery(stmt);
                    while (rs.next())
                    {
                        record.setOrgConstructId(rs.getInt("constructid"));
                        record.setOrgContainerid(rs.getInt("containerid"));
                        record.setOrgSampleId(rs.getInt("sampleid"));
                        record.setCloneId(rs.getInt("cloneid"));
                        record.setSampleType(rs.getString("sampletype"));
                        record.setStatusGB(rs.getString("status_gb"));
                    }
                }
            }
        
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql );
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    public static  void     verifyPlates(Connection conn,ArrayList<NorgenDestinationPlate> dest_plates,
            int projectid, int workflowid, int protocolid)throws Exception
    {
        String sql_check="select  count(sampleid) as samplenumber from sample where containerid=?";
        
        
        String sql_check_plateonqueue="select containerid from queue  where  "
  +"   workflowid="+workflowid+" and projectid="+projectid +"  and protocolid="+protocolid 
                +" and containerid =(select containerid from containerheader where label=?)" ;
         PreparedStatement stmt = null;ResultSet rs;
         PreparedStatement stmt_check_plateonqueue=null;ResultSet rr;
        try {
            stmt = conn.prepareStatement(sql_check);
            stmt_check_plateonqueue =conn.prepareStatement(sql_check_plateonqueue);
            for( NorgenDestinationPlate dest_plate:   dest_plates)
            {
                stmt_check_plateonqueue.setString(1, dest_plate.getLabel());
                rs=DatabaseTransaction.executeQuery(stmt_check_plateonqueue);
                if ( rs.next())
                {
                    int containerid = rs.getInt("containerid");
                    dest_plate.setId(containerid);
                    stmt.setInt(1,containerid);
                    rs=DatabaseTransaction.executeQuery(stmt);
                    if (rs.next())
                    {
                        int samplenumber = rs.getInt("samplenumber");
                        if (  samplenumber != 0)
                        {
                            System.out.println("Plate "+dest_plate.getLabel()+" is not empty");
                            throw new Exception("Plate "+dest_plate.getLabel()+" is not empty");
                        }
                       
                    }
                }
                else
                {
                      System.out.println("Plate "+dest_plate.getLabel()+" is not on queue.");
                       throw new Exception("Plate "+dest_plate.getLabel()+" is not on queue.");
                  
                }
                
                
               
            }
        
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql_check);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
             
             
    }
    
     public static  void populatePlatesWithSamples(Connection conn,
             ArrayList<NorgenDestinationPlate> dest_plates,
             String sample_type, int executionid)throws Exception
     {
           String sql = "insert into sample (sampleid, sampletype, containerid, containerposition,"
+" constructid, status_gb,cloneid) values (?,?, ?,?,?,?,?)" ;
            String sql_no_construct = "insert into sample (sampleid, sampletype, containerid, containerposition,"
+"  status_gb) values (?,?, ?,?,?)" ;
String sql_sample_lin = "insert into samplelineage (executionid, sampleid_from, sampleid_to)" +
        " values ("+executionid+",?,?)" ;
         PreparedStatement stmt = null; 
        PreparedStatement stmt_sample_lin = null;
        PreparedStatement stmt_sql_no_construct=null;
         int count=1;
        Container container; ResultSet rs;
        try {
            stmt = conn.prepareStatement(sql);
            stmt_sql_no_construct = conn.prepareStatement(sql_no_construct);
              stmt_sample_lin = conn.prepareStatement(sql_sample_lin);
            
            for( NorgenDestinationPlate dest_plate:   dest_plates)
            {
                for (NorgenLogFileRecord record: dest_plate.getRecords())
                {
                        int newsampleid = FlexIDGenerator.getID("sampleid");
                      record.setDestSsmpleId(newsampleid);
                      
                      if ( record.getOrgConstructId() >0 )
                      {
                          stmt.setInt(count++,record.getDestSampleId());
                          stmt.setString(count++,record.getSampleType());
                          stmt.setInt(count++, dest_plate.getId());
                          stmt.setInt(count++, record.getDestPosition());
                          stmt.setInt(count++,record.getOrgConstructId() );
                          stmt.setString(count++,record.getStatusGB());
                          stmt.setInt(count++, record.getCloneId());
                            count=1;
                         DatabaseTransaction.executeUpdate(stmt);
                      }
                      else
                      {
                          stmt_sql_no_construct.setInt(count++,record.getDestSampleId());
                          stmt_sql_no_construct.setString(count++,record.getSampleType());
                          stmt_sql_no_construct.setInt(count++, dest_plate.getId());
                          stmt_sql_no_construct.setInt(count++, record.getDestPosition());
                          stmt_sql_no_construct.setString(count++, record.getStatusGB());
                         count=1;
                         DatabaseTransaction.executeUpdate(stmt_sql_no_construct);
                      }
                     stmt_sample_lin.setInt( count++, record.getOrgSampleId());
                     stmt_sample_lin.setInt(count++, record.getDestSampleId());
                     DatabaseTransaction.executeUpdate(stmt_sample_lin);
                     count=1;
                }
            }
        
        } catch (Exception sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
     }
     
     public static  void populateProcessObjects(Connection conn,
             ArrayList<NorgenDestinationPlate> dest_plates,
              int executionid)throws Exception
     {
           String sql = "insert into processobject (executionid, inputoutputflag, containerid)"
                   +" values ("+executionid+"?,?)" ;
 
           PreparedStatement stmt = null;
           ArrayList<String> orglabels=new ArrayList<String>();
          int count=1;ResultSet rs;
        try {
            stmt = conn.prepareStatement(sql);
            for( NorgenDestinationPlate dest_plate:   dest_plates)
            {
                orglabels=new ArrayList<String>();
                for (NorgenLogFileRecord record: dest_plate.getRecords())
                {
                    if ( !orglabels.contains(record.getOrgLabel()))
                    {
                      stmt.setString(count++,"I");
                      stmt.setInt(count,record.getOrgContainerid());
                      DatabaseTransaction.executeUpdate(stmt);
                      count=1;
                    }
                }
                stmt.setString(count++,"O");
                stmt.setInt(count,dest_plate.getId());
                DatabaseTransaction.executeUpdate(stmt);
                count=1;
            }
        
        } catch (Exception sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
     }
       
       
     public static void  putItemsOnQueue(Connection conn,ArrayList<NorgenDestinationPlate> dest_plates,
            int  projectid, int workflowid, int curprotocolid,
            int nextprotocolid) throws Exception
     {
           String sql = "update   queue set protocolid=" +nextprotocolid
          +", dateadded=sysdate "
       + "where protocolid="+curprotocolid+" and  containerid=? and projectid="+projectid+ 
                   " and  workflowid=" +workflowid;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for( NorgenDestinationPlate dest_plate:   dest_plates)
            {
                stmt.setInt(1, dest_plate.getId());
                DatabaseTransaction.executeUpdate(stmt);
            }
        
        } catch (Exception sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
     }
     
     
      /**
     * Creates and uploads a file.
     *
     * @param conn The db connection used to insert the file.
     * @param form The form holding the results.
     *
     * @return FileReference with the file information, could be null if no
     * file reference is associated with the form.
     *
     * @exception FlexDatabaseException when a database error occurs.
     * @exception IOException when an error occurs writing the file to the
     *              repository
     */
    private FileReference handleFileReference(Connection conn, FormFile image, Container container, String type)
    throws FlexDatabaseException, IOException{
        FileReference fileRef = null;
        
        // get the current date
        Calendar cal = Calendar.getInstance();
        
        // month starts with 0 so add 1 so it looks normal
        int monthNum = cal.get(Calendar.MONTH) + 1;
        
        //String version of monthNum
        String monthNumS = Integer.toString(monthNum);
        
        // append a 0 if its less than 10
        if(monthNum < 10) {
            monthNumS = "0"+monthNum;
        }
        
        String subDirName = Integer.toString(cal.get(Calendar.YEAR)) + monthNumS;
        String localPath = FileRepository.NORGREN_LOG_FILE_PATH + subDirName + "/";
        fileRef = FileReference.createFile(conn, image.getFileName(), type ,localPath, container);
        
        FileRepository.uploadFile(fileRef, image.getInputStream());
        
        return fileRef;
    }
     ////////////////////////////////////////

     public static void main(String args[]) {
        Connection conn=null;
        try
         {
          Researcher researcher = new Researcher("htaycher");
          int projectid=24;int  workflowid=93;int protocolid=73;
            NorgenLogFile parced_file = new NorgenLogFile();
            String fn="c:\\bio\\test\\rearray.log";
            parced_file.parseLogFile(new FileInputStream(fn));
            ArrayList<NorgenDestinationPlate> dest_plates =parced_file.getDestinationPlates();
            DatabaseTransaction t = DatabaseTransaction.getInstance();
              conn = t.requestConnection();
            
            {  
                    PopulateEmptyContainersAction.verifyPlates(conn,dest_plates, projectid, workflowid,protocolid );
               
                edu.harvard.med.hip.flex.process.Process process = 
                         new edu.harvard.med.hip.flex.process.Process(new Protocol(protocolid), 
                         edu.harvard.med.hip.flex.process.Process.SUCCESS, 
                         researcher, new Project(projectid), new Workflow(workflowid));
                 process.insert(conn);
                 PopulateEmptyContainersAction.getDataForOrgPlates(conn,dest_plates);
                 PopulateEmptyContainersAction.populatePlatesWithSamples(conn,dest_plates ,"ISOLATE",process.getExecutionid());
                 PopulateEmptyContainersAction.putItemsOnQueue(conn,dest_plates, projectid, workflowid,protocolid, 60);
             conn.commit();
            }
         }catch(Exception e)
         {
             DatabaseTransaction.rollback(conn);
         }
         System.exit(0);
     }
     
}
