<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.programs.blast.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.sequence.*" %>
<%@ page import="edu.harvard.med.hip.bec.action_runners.*" %>
 <%
Object forwardName = null;
if ( request.getAttribute("forwardName") != null)
{
        forwardName = request.getAttribute("forwardName") ;
}
else
{
        forwardName = request.getParameter("forwardName") ;
}
int forwardName_int = 0;
StringBuffer additional_jsp_buffer = new StringBuffer();
String additional_jsp = "";
boolean isTryMode = false;
if (forwardName instanceof String) forwardName_int = Integer.parseInt((String)forwardName);
else if (forwardName instanceof Integer) forwardName_int = ((Integer) forwardName).intValue();

switch( forwardName_int)
{
case Constants.PROCESS_SHOW_CLONE_HISTORY :{ break;}
case Constants.PROCESS_ORDER_INTERNAL_PRIMERS:{

additional_jsp = "<tr><td colspan='2'><table border =0 width='100%'>"
+"<tr><td colspan=2 bgColor='#1145A6' ><font color='#FFFFFF'><strong>Oligo placement format </strong></font> </td></tr>"
+"<tr><td> <input type=RADIO name='oligo_placement_format' value="+ PrimerOrderRunner.PLACEMENT_FORMAT_ALL_TOGETHER+"> checked><b>All primers for clone together </td></tr>"
+"<tr><td>  <input type=RADIO name='oligo_placement_format' value="+ PrimerOrderRunner.PLACEMENT_FORMAT_N_PRIMER+" ><b><i>Nth</i> primer for clone. Primer number (ordered by position from 5p end)"
+"  <input type=text value='1' name='primer_number'  ></td></tr></table></td></tr>";
isTryMode = true; break;}
case Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS:{ break;}
case Constants.PROCESS_RUN_DECISION_TOOL:{break;}
case Constants.PROCESS_FIND_GAPS:
{
additional_jsp = "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input NAME='isRunLQR' id='isRunLQR' type = checkbox value=0><strong>Run LQR Finder on contig sequences</strong></td></tr>";

isTryMode=true; break;}

case Constants.STRETCH_COLLECTION_REPORT_INT:{ break;}
case Constants.STRETCH_COLLECTION_REPORT_ALL_INT:{ break;}
case Constants.LQR_COLLECTION_REPORT_INT:{break;}
case Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE:{isTryMode=true;break;}

case Constants.PROCESS_RUN_END_READS_WRAPPER:{ break;}
case Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS:{break;}

case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER :{ break;}
case Constants.PROCESS_APPROVE_INTERNAL_PRIMERS :{break;}
            
case Constants.PROCESS_RUNPOLYMORPHISM_FINDER:{ break;}            
case Constants.PROCESS_RUN_DISCREPANCY_FINDER:{ break;}

case Constants.PROCESS_NOMATCH_REPORT:
{ 

additional_jsp_buffer.append( "<tr><td colspan =2 bgColor='#1145A6' ><font color='#FFFFFF'><strong>Process Specification</strong></font></td></tr>");
additional_jsp_buffer.append("<tr> <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>Database Name</strong></td>");
additional_jsp_buffer.append("<td><SELECT NAME='DATABASE_NAME' id='DATABASE_NAME'> <OPTION VALUE='"+ BlastWrapper.getHumanDBLocation() +"'>" + BlastWrapper.HUMANDB_NAME );
additional_jsp_buffer.append(" <OPTION VALUE='"+ BlastWrapper.getYeastDBLocation() +"'>");
additional_jsp_buffer.append( BlastWrapper.YEASTDB_NAME);
additional_jsp_buffer.append("<OPTION VALUE='"+ BlastWrapper.getPseudomonasDBLocation() +"'>"+ BlastWrapper.PSEUDOMONASDB_NAME );
additional_jsp_buffer.append("<OPTION VALUE='"+ BlastWrapper.getMGCDBLocation() +"'>"+ BlastWrapper.MGCDB_NAME );
additional_jsp_buffer.append("<OPTION VALUE='"+ BlastWrapper.getYPDBLocation() +"'>"+ BlastWrapper.YPDB_NAME );
additional_jsp_buffer.append("<OPTION VALUE='"+ BlastWrapper.getFTDBLocation()+"'>"+ BlastWrapper.FTDB_NAME );
additional_jsp_buffer.append("<OPTION VALUE='"+ BlastWrapper.getClontechDBLocation()+"'>"+ BlastWrapper.ClontechDB_NAME );
additional_jsp_buffer.append("<OPTION VALUE='"+ BlastWrapper.getNIDDKDBLocation()+"'>"+ BlastWrapper.NIDDKDB_NAME );
additional_jsp_buffer.append("</SELECT></td> </tr>");
additional_jsp_buffer.append("<tr> <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>Display Sequence Identifier</strong></td>");
additional_jsp_buffer.append("<td><SELECT NAME='ID_NAME' id='ID_NAME'>" );
additional_jsp_buffer.append("<OPTION VALUE='NONE'> None");
additional_jsp_buffer.append("<OPTION VALUE='"+ PublicInfoItem.GI +"'>"+ PublicInfoItem.GI);
additional_jsp_buffer.append("<OPTION VALUE='"+ PublicInfoItem.PANUMBER +"'>"+ PublicInfoItem.PANUMBER );
additional_jsp_buffer.append("<OPTION VALUE='"+ PublicInfoItem.SGD +"'>"+ PublicInfoItem.SGD );
additional_jsp_buffer.append("</SELECT></td> </tr>");
  additional_jsp = additional_jsp_buffer.toString();   
break;}

case Constants.PROCESS_RUN_PRIMER3 :{isTryMode = true; break;}
 case Constants.PROCESS_VIEW_INTERNAL_PRIMERS:{ break;}
case Constants.PROCESS_CREATE_REPORT:{break;}
case Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING  :{ break;}
case Constants.PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING  :{ break;}
case Constants.PROCESS_DELETE_PLATE :{  break;}
case Constants.PROCESS_DELETE_CLONE_READS  :{    break;}
case Constants.PROCESS_DELETE_CLONE_FORWARD_READ :{   break;}
case Constants.PROCESS_DELETE_CLONE_REVERSE_READ  :{  break;}
case Constants.PROCESS_DELETE_CLONE_SEQUENCE: { break;}
case  Constants.PROCESS_GET_TRACE_FILE_NAMES :{ break;}
case  Constants.PROCESS_DELETE_TRACE_FILES :{ break;}
                      
}
if ( isTryMode )
{
    additional_jsp += "<tr><td colspan='2'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='checkbox' name='isTryMode' checked value=1><b>Run in try mode?</b></td></tr>";
   }
%>
 <%= additional_jsp %>