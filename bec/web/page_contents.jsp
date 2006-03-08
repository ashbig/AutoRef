<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>

 <%Object forwardName = null;
if ( request.getAttribute("forwardName") != null)
{
        forwardName = request.getAttribute("forwardName") ;
}
else
{
        forwardName = request.getParameter("forwardName") ;
}
 Object title = null;
 int forwardName_int = 0;
if (forwardName!= null && forwardName instanceof String  ) forwardName_int = Integer.parseInt((String)forwardName);
else if (forwardName!= null && forwardName instanceof Integer ) forwardName_int = ((Integer) forwardName).intValue(); 
String jsp_name = null;
switch (forwardName_int)
{

case -Constants.PROCESS_ADD_NEW_LINKER  : 
case -Constants.PROCESS_ADD_NAME_TYPE  : 
case -Constants.PROCESS_ADD_SPECIES_DEFINITION  : 
case -Constants.PROCESS_ADD_PROJECT_DEFINITION  : 
case -Constants.PROCESS_ADD_NEW_VECTOR  : 
case -Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :
case -Constants.PROCESS_SUBMIT_CLONE_SEQUENCES:
case -Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : 
case -Constants.PROCESS_ADD_NEW_COMMON_PRIMER :
case -Constants.PROCESS_ADD_NEW_CLONINGSTRATEGY:
case -Constants.PROCESS_ADD_NEW_CONNECTION_VECTOR_LINKER:
case -Constants.PROCESS_ADD_TRACE_FILE_NAME_FORMAT: {     jsp_name ="InitiateDirectDatabaseSubmission.jsp";   break; }


case Constants.UI_ABOUT_PAGE: { jsp_name = edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION")+"help/about_ace.jsp"; break;}
case Constants.UI_HELP_PAGE: { jsp_name = edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION")+"help/help_menu.jsp"; break;}
case Constants.UI_SELECT_PROCESS_PAGE:
case Constants.UI_SELECT_PROCESS_EREAD_MANIPULATION_PAGE:
case Constants.UI_SELECT_PROCESS_DELETE_DATA_PAGE :
case Constants.UI_SELECT_PROCESS_INTERNAL_PRIMERS_PAGE:



{ jsp_name="SelectProcess.jsp";break;}
case Constants.PROCESS_CREATE_REPORT:{ jsp_name="RunReport.jsp";break;}
case Constants.CONTAINER_PROCESS_HISTORY:
case Constants.CONTAINER_DEFINITION_INT :
case Constants.CONTAINER_RESULTS_VIEW:
{
    jsp_name = "ContainerScan.jsp"; break;
    }
case Constants.PROCESS_SHOW_CLONE_HISTORY:{jsp_name = "initiate_process_no_template.jsp"; break;}
case Constants.UI_VIEW_PROCESS_RESULTS_PAGE:{jsp_name = "ViewProcessResults.jsp"; break;}
case Constants.PROCESS_CREATE_RENAMING_FILE_FOR_TRACEFILES_TRANSFER:{jsp_name = "TraceFileProcessing.jsp"; break;}
case Constants.PROCESS_INITIATE_TRACEFILES_TRANSFER:{jsp_name = "TraceFileProcessing.jsp"; break;}
      
case Constants.UI_ISOLATE_RANKER_SPEC_PAGE :{jsp_name = "Seq_EnterEndReadsParameters.jsp"; break;}
case Constants.UI_PRIMER_DESIGNER_SPEC_PAGE :{jsp_name = "Seq_EnterPrimerParameters.jsp"; break;}
case Constants.UI_POLYMFINDER_SPEC_PAGE :{jsp_name = "Seq_EnterPolymorphismParameters.jsp"; break;}
case Constants.UI_CLONEEVAL_SPEC_PAGE :{jsp_name = "Seq_EnterFullSeqParameters.jsp"; break;}
case Constants.UI_SEQUENCETRIMMING_SPEC_PAGE :{jsp_name = "Seq_EnterSlidingWindowParameters.jsp"; break;}
case Constants.UI_SELECT_PROCESS_UPLOAD_DATA_PAGE:   {jsp_name = "UploadNotHipPlates.jsp"; break;}
}
%>
<jsp:include page="<%= jsp_name %>" flush="true"/> 