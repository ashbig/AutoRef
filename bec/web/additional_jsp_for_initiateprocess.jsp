<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.programs.blast.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.sequence.*" %>
<%@ page import="edu.harvard.med.hip.bec.action_runners.*" %>
<%@ page import="edu.harvard.med.hip.bec.programs.assembler.*" %>


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
String line_padding = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
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
case Constants.PROCESS_RUN_DECISION_TOOL:{break;}


case Constants.STRETCH_COLLECTION_REPORT_INT:{ break;}
case Constants.STRETCH_COLLECTION_REPORT_ALL_INT:{ break;}
case Constants.LQR_COLLECTION_REPORT_INT:{break;}
case Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE:{isTryMode=true;break;}

case Constants.PROCESS_RUN_END_READS_WRAPPER:{ break;}

case Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS:
case Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS:
case Constants.PROCESS_FIND_GAPS:
{
additional_jsp_buffer.append("<tr><td>"+line_padding+"<strong>For assembly process:</strong>");
additional_jsp_buffer.append("<P>"+line_padding+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Select what vectors to trim&nbsp;&nbsp;");

additional_jsp_buffer.append("<select NAME='isRunVectorTrimming' id='isRunVectorTrimming'>");
additional_jsp_buffer.append("<option value='"+PhredPhrap.VECTOR_LIBRARY_NAME+"' selected> All available vectors");
additional_jsp_buffer.append("<option value='"+PhredPhrap.VECTOR_LIBRARY_NAME_EMPTY+"' > No vector triming");
additional_jsp_buffer.append("<option value='vector_pDonor221_altered.txt' > pDonor221 only");
additional_jsp_buffer.append("<option value='pBY011_cleaned.txt' > pBY011 only");
additional_jsp_buffer.append("<option value='pBY011_cleaned_201.txt' > pBY011 and pDONR201");
additional_jsp_buffer.append("<option value='pDONR201_cleaned.txt' > pDonor201 only");
additional_jsp_buffer.append("<option value='vector_pDonorDual_altered.txt' > pDonorDual only");
additional_jsp_buffer.append("</select></td></tr>");

if ( forwardName_int ==  Constants.PROCESS_FIND_GAPS)
{
additional_jsp_buffer.append("<tr><td><P>"+line_padding+"<input NAME='isRunLQR' id='isRunLQR' type = checkbox value=0 checked><strong>Run LQR Finder on contig sequences</strong></td></tr>");
isTryMode=true;
}
additional_jsp = additional_jsp_buffer.toString();
break;
}


case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER :{ break;}
case Constants.PROCESS_APPROVE_INTERNAL_PRIMERS :{break;}
            
case Constants.PROCESS_RUNPOLYMORPHISM_FINDER:{ break;}            
case Constants.PROCESS_RUN_DISCREPANCY_FINDER:{ break;}

case Constants.PROCESS_NOMATCH_REPORT:
{ 

additional_jsp_buffer.append( "<tr><td colspan =2 bgColor='#1145A6' ><font color='#FFFFFF'><strong>Process Specification</strong></font></td></tr>");
additional_jsp_buffer.append("<tr> <td>"+line_padding+"<strong>Database Name</strong></td>");
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

case Constants.PROCESS_RUN_PRIMER3 :
{

additional_jsp_buffer.append("<tr><td colspan = 2><table width='100%' border='0' cellspacing='2' cellpadding='2'><tr><td ><strong><input type='radio' name='typeSequenceCoverage' value='"+ PrimerDesignerRunner.COVERAGE_TYPE_REFERENCE_CDS+ "' checked>");
additional_jsp_buffer.append("Design primers for Reference Sequence</strong> </td></tr><tr><td><strong><input type='radio' name='typeSequenceCoverage' value='"+ PrimerDesignerRunner.COVERAGE_TYPE_GAP_LQR +"'>");
additional_jsp_buffer.append("Design primers for Stretch Collection</strong></td></tr><tr>");
additional_jsp_buffer.append("<td ><div align='center'><em>The following parameters for Stretch Collections only<P></em></div></td></tr><tr><td >");
additional_jsp_buffer.append("Minimun distance between two stretches that should be combined ");
additional_jsp_buffer.append("<input type='text' width=20 value=50 name='minDistanceBetweenStretchesToBeCombined'></td></tr>");

  
additional_jsp_buffer.append("<tr><td ><input type='radio' name='typeLQRCoverage' value='"+ PrimerDesignerRunner.LQR_COVERAGE_TYPE_NONE + "'>");
additional_jsp_buffer.append("Is not to cover Low Quality Regions</td></tr>");
additional_jsp_buffer.append("<tr><td ><input type='radio' name='typeLQRCoverage' value='"+  PrimerDesignerRunner.LQR_COVERAGE_TYPE_ANY_LQR +"'>");
additional_jsp_buffer.append("Is cover any Low Quality Regions</td></tr>");
additional_jsp_buffer.append("<tr><td ><input type='radio' name='typeLQRCoverage' value='"+ PrimerDesignerRunner.LQR_COVERAGE_TYPE_LQR_WITH_DISCREPANCY +"'>");
additional_jsp_buffer.append("Is cover only Low Quality Regions with discrepancies</td></tr>");
additional_jsp_buffer.append("<tr><td><input type='radio' name='typeLQRCoverage' value='"+ PrimerDesignerRunner.LQR_COVERAGE_TYPE_LQR_DISCREPANCY_REGIONS+"' disabled>");
additional_jsp_buffer.append("Is cover only Regions with Discrepancies inside Low Quality Regions</td></tr>");
additional_jsp = additional_jsp_buffer.toString();   
isTryMode = true; break;}
 

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
case  Constants.PROCESS_MOVE_TRACE_FILES  :{ break;}                 
}
if ( isTryMode )
{
    additional_jsp += "<tr><td colspan='2'>"+line_padding+"<input type='checkbox' name='isTryMode' checked value=1><b>Run in try mode?</b></td></tr>";
   }
%>
 <%= additional_jsp %>