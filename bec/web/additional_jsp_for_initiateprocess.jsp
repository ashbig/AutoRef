<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.programs.blast.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.sequence.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.oligo.*" %>
<%@ page import="edu.harvard.med.hip.bec.action_runners.*" %>
<%@ page import="edu.harvard.med.hip.bec.programs.assembler.*" %>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.util_objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.action_runners.*" %>
<%@ page import="java.util.*" %>

<head>
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>
</head>

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

additional_jsp_buffer.append("<tr><td colspan='2'><table border =0 width='100%'>");
additional_jsp_buffer.append("<tr><td colspan=2  bgColor='#1145A6' ><font color='#FFFFFF'><strong>Primers selection rule  </strong></font> </td></tr>");
additional_jsp_buffer.append("<tr><td> <input checked type=RADIO name='oligo_grouping_rule' value="+ PrimerOrderRunner.OLIGO_SELECTION_FORMAT_STRETCH_COOLECTION_ONLY+"> <b>Only primers designed for Stretch Collections</td></tr>");
additional_jsp_buffer.append("<tr><td>  <input  type=RADIO name='oligo_grouping_rule' value="+ PrimerOrderRunner.OLIGO_SELECTION_FORMAT_REFSEQ_ONLY+" ><b> Only primers designed for Refernce Sequence</td></tr> ");
additional_jsp_buffer.append("<tr><td>  <input  type=RADIO name='oligo_grouping_rule' value="+ PrimerOrderRunner.OLIGO_SELECTION_FORMAT_STRETCH_COLLECTION_REFSEQ+" ><b> Primers designed for Stretch Collections and Reference Sequence</td></tr>");

additional_jsp_buffer.append("<tr><td colspan=2 bgColor='#1145A6' ><font color='#FFFFFF'><strong><P>Primers placement rule </strong></font> </td></tr>");
additional_jsp_buffer.append("<tr><td> <input  type=RADIO name='oligo_placement_format' value="+ PrimerOrderRunner.PLACEMENT_FORMAT_ALL_TOGETHER+" checked><b>All primers for clone together </td></tr>");
//+"<tr><td>  <input disabled type=RADIO name='oligo_placement_format' value="+ PrimerOrderRunner.PLACEMENT_FORMAT_N_PRIMER+" ><b><i>Nth</i> primer for clone. Primer number (ordered by position from 5p end)"
//+"  <input type=text value='1' name='primer_number'  >";
additional_jsp_buffer.append("<tr><td><strong>Place first primer in well:</strong></td><td> <input  type=text name='first_well' value='B01' ></td></tr>");
additional_jsp_buffer.append("<tr><td><strong>Place last primer in well:</strong></td><td> <input  type=text name='last_well' value='G12' ></td></tr></table></td></tr>");

//for company order file

additional_jsp_buffer.append("<tr><td colspan=2 bgColor='#1145A6' ><font color='#FFFFFF'><strong><P>");
additional_jsp_buffer.append("<input type='checkbox' name='show' value='1' checked onclick= \"javascript:showhide('divShowHide', this.checked); \">");
additional_jsp_buffer.append("Create order file</strong></font> </td></tr>");
additional_jsp_buffer.append("\n<tr><td><DIV  ID='divShowHide' STYLE='position:relative;  clip:rect(0px 120px 120px 0px);'> ");
additional_jsp_buffer.append(" \n<table width='85%' border='0' align='center'>");
additional_jsp_buffer.append("\n <tr> <td align='center' bgColor='#e4e9f8'><strong>Column Content </strong></td><td bgColor='#e4e9f8' align='center'><strong>Column Number </strong></td></tr>");
additional_jsp_buffer.append("\n <tr> <td><strong> Primer Sequence</strong></td><td><input size='2' type='text' name='primer_sequence' value=0 onBlur= \";checkNumeric(this,0,30,'','',''); \"> </td></tr>");

   additional_jsp_buffer.append("\n <tr> <td><strong> Primer Name (Sample Id)</strong></td><td><input type='text' size='2' name='primer_name' value=0 onBlur=\"checkNumeric(this,0,30,'','','');\"> </td></tr>");

additional_jsp_buffer.append("\n <tr> <td><strong>Primer Column</strong> </td><td><input type='text' size='2' name='primer_column' value=0 onBlur=\"checkNumeric(this,0,30,'','','');\"> </td></tr>");
additional_jsp_buffer.append("\n <tr> <td><strong>Primer Row</strong></td><td> <input type='text' size='2' name='primer_row' value=0 onBlur=\"checkNumeric(this,0,30,'','','');\"> </td></tr>");
additional_jsp_buffer.append(" \n<tr> <td><strong>Plate Name</strong> </td><td> <input type='text' size='2' name='plate_name' value=0 onBlur=\"checkNumeric(this,0,30,'','','');\"></td></tr>");
additional_jsp_buffer.append(" \n<tr><td colspan=2 align='center' bgColor='#e4e9f8'><strong>Empty Well Presentation</strong></td><tr>");
additional_jsp_buffer.append("\n<tr> <td><strong>No primer Sequence presentation</strong></td><td><input type='text' size='5' name='empty_sequence' value='.'> </td></tr>");
additional_jsp_buffer.append("\n<tr> <td><strong>No primer Sequence Name presentation</strong></td><td><input type='text' size='5' name='empty_sequence_name' value=' '> </td></tr>");

additional_jsp_buffer.append(" \n<tr><tr><td colspan=2 align='center' bgColor='#e4e9f8'><strong>Number of Order Files</strong></td><tr>");
additional_jsp_buffer.append("<tr> <td><strong>Create order file for:</strong></td><td><select name='number_of_files' > ");
additional_jsp_buffer.append("<option selected value='"+PrimerOrderRunner.NO_ORDER_FILES+"' >--------<option value='"+PrimerOrderRunner.ONE_FILE_PER_PLATE+"' >One file per oligo plate<option value='"+PrimerOrderRunner.ONE_FILE_PER_ORDER+"'>One file per order</select></td></tr>");

additional_jsp_buffer.append("</table></div></td></tr>");
      


additional_jsp = additional_jsp_buffer.toString();


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




additional_jsp_buffer.append(" <script defer='defer' type='text/javascript'><!--");
additional_jsp_buffer.append("\n function isTrimm(e,checked){	; var form = e.form;");
additional_jsp_buffer.append("	 if(!checked) 	{");
additional_jsp_buffer.append("\n form.elements."+ PhredPhrap.QUALITY_TRIMMING_SCORE +".value = 0;");
additional_jsp_buffer.append("\n form.elements."+ PhredPhrap.QUALITY_TRIMMING_FIRST_BASE+".value = 0;");
 additional_jsp_buffer.append("\n form.elements."+  PhredPhrap.QUALITY_TRIMMING_LAST_BASE+".value = 0;\n}\n else\n {");
additional_jsp_buffer.append("\n form.elements."+  PhredPhrap.QUALITY_TRIMMING_SCORE+".value= 10;");
additional_jsp_buffer.append("\nform.elements."+  PhredPhrap.QUALITY_TRIMMING_FIRST_BASE+".value= 50;");
additional_jsp_buffer.append("form.elements."+  PhredPhrap.QUALITY_TRIMMING_LAST_BASE+".value = 800;\n	}");
additional_jsp_buffer.append("\n }  \n -->\n</Script>");

additional_jsp_buffer.append("\n <tr><td>");
additional_jsp_buffer.append("<table width='90%' border='0' cellspacing='2' cellpadding='2' aligh='center'>");
additional_jsp_buffer.append("<tr> <td colspan='2'>&nbsp;<strong>For assembly process:</strong> </td> </tr>");
additional_jsp_buffer.append(" <tr> <td>"+line_padding+"Select what vectors to trim</td>");
additional_jsp_buffer.append("<td><select NAME='isRunVectorTrimming' id='isRunVectorTrimming'>");

String vector_file_path = null;String vector_name = null;
for (Enumeration e = BecProperties.getInstance().getVectorLibraries().keys() ; e.hasMoreElements() ;)
{
	vector_name = (String) e.nextElement();
	vector_file_path = (String)BecProperties.getInstance().getVectorLibraries().get(vector_name);
	additional_jsp_buffer.append(" <OPTION VALUE='" + vector_file_path +"'>"+vector_name);
}

additional_jsp_buffer.append("</select></td> </tr>");
additional_jsp_buffer.append("<tr> <td colspan='2'><strong> <input type='checkbox' checked name='all' id='all' onClick='isTrimm(this, this.checked)'>");
additional_jsp_buffer.append(" Perform quality trimming:</strong></td> </tr>");
 additional_jsp_buffer.append(" <tr> <td>"+line_padding+"Phred score&nbsp;&nbsp; </td>");
 additional_jsp_buffer.append(" <td><input type = text value = 10 name='"+PhredPhrap.QUALITY_TRIMMING_SCORE+"' ></td> </tr>");
 additional_jsp_buffer.append(" <tr> <td>"+line_padding+"First base to include &nbsp;&nbsp; </td>");
additional_jsp_buffer.append(" <td><input type = text value = 50 name='"+PhredPhrap.QUALITY_TRIMMING_FIRST_BASE+"' ></td>");
additional_jsp_buffer.append(" </tr> <tr> <td>"+line_padding+"Last base to include</td> <td><input type = text value = 900 name='"+PhredPhrap.QUALITY_TRIMMING_LAST_BASE + "' ></td> </table>");




if ( forwardName_int ==  Constants.PROCESS_FIND_GAPS)
{
additional_jsp_buffer.append("<tr><td><P>"+line_padding+"<input NAME='isRunLQR' id='isRunLQR' type = checkbox value=0 checked><strong>Run LQR Finder on contig sequences</strong></td></tr>");
isTryMode=true;
}
additional_jsp = additional_jsp_buffer.toString();
break;
}




case Constants.PROCESS_RUNPOLYMORPHISM_FINDER:{ break;}
case Constants.PROCESS_RUN_DISCREPANCY_FINDER:{ break;}

case Constants.PROCESS_NOMATCH_REPORT:
{

additional_jsp_buffer.append( "<tr><td colspan =2 bgColor='#1145A6' ><font color='#FFFFFF'><strong>Process Specification</strong></font></td></tr>");
additional_jsp_buffer.append("<tr> <td>"+line_padding+"<strong>Database Name</strong></td>");
additional_jsp_buffer.append("<td><SELECT NAME='DATABASE_NAME' id='DATABASE_NAME'> ");
String dbpath = null;String dbname = null;
for (Enumeration e = BecProperties.getInstance().getBlastableDatabases().keys() ; e.hasMoreElements() ;)
{
	dbname = (String) e.nextElement();
	dbpath = (String)BecProperties.getInstance().getBlastableDatabases().get(dbname);
	additional_jsp_buffer.append(" <OPTION VALUE='" + dbpath +"'>"+dbname);
}
additional_jsp_buffer.append("</SELECT></td> </tr>");
additional_jsp_buffer.append("<tr> <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>Display Sequence Identifier</strong></td>");
additional_jsp_buffer.append("<td><SELECT NAME='ID_NAME' id='ID_NAME'>" );
additional_jsp_buffer.append("<OPTION VALUE=' '> None");
additional_jsp_buffer.append("<OPTION VALUE='"+ PublicInfoItem.GI +"'>"+ PublicInfoItem.GI);
SpeciesDefinition sd = null;
for (Enumeration e = DatabaseToApplicationDataLoader.getSpecies().keys() ; e.hasMoreElements() ;)
{
	sd = (SpeciesDefinition) DatabaseToApplicationDataLoader.getSpecies().get( e.nextElement());
        if ( sd.getIdName() != null && sd.getIdName().trim().length() > 0)
            {
	additional_jsp_buffer.append(" <OPTION VALUE='" + sd.getIdName() +"'>"+sd.getIdName());
        }
}

//additional_jsp_buffer.append("<OPTION VALUE='"+ PublicInfoItem.PANUMBER +"'>"+ PublicInfoItem.PANUMBER );
//additional_jsp_buffer.append("<OPTION VALUE='"+ PublicInfoItem.SGD +"'>"+ PublicInfoItem.SGD );

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


additional_jsp_buffer.append("<tr><td ><input type='radio' name='typeLQRCoverage' value='"+ PrimerDesignerRunner.LQR_COVERAGE_TYPE_NONE + "' checked>");
additional_jsp_buffer.append("Is not to cover Low Quality Regions</td></tr>");
additional_jsp_buffer.append("<tr><td ><input type='radio' name='typeLQRCoverage' value='"+  PrimerDesignerRunner.LQR_COVERAGE_TYPE_ANY_LQR +"'>");
additional_jsp_buffer.append("Is cover any Low Quality Regions</td></tr>");
additional_jsp_buffer.append("<tr><td ><input type='radio' name='typeLQRCoverage' value='"+ PrimerDesignerRunner.LQR_COVERAGE_TYPE_LQR_WITH_DISCREPANCY +"'>");
additional_jsp_buffer.append("Is cover only Low Quality Regions with discrepancies</td></tr>");
additional_jsp_buffer.append("<tr><td><input type='radio' name='typeLQRCoverage' value='"+ PrimerDesignerRunner.LQR_COVERAGE_TYPE_LQR_DISCREPANCY_REGIONS+"' >");
additional_jsp_buffer.append("Is cover only Regions with Discrepancies inside Low Quality Regions</td></tr>");
additional_jsp_buffer.append("<tr><td><input type='radio' name='typeLQRCoverage' value='"+ PrimerDesignerRunner.LQR_COVERAGE_TYPE_COVER_EACH_DISCREPANCY+"' >");
additional_jsp_buffer.append("Is cover any Discrepancy inside Low Quality Regions</td></tr>");
additional_jsp_buffer.append("<tr><td><input type='radio' name='typeLQRCoverage' value='"+ PrimerDesignerRunner.LQR_COVERAGE_TYPE_COVER_EACH_LOWQ_DISCREPANCY+"' >");
additional_jsp_buffer.append("Is cover only Low Quality Discrepancies inside Low Quality Regions</td></tr>");

additional_jsp = additional_jsp_buffer.toString();
isTryMode = true; break;}


case Constants.PROCESS_VIEW_INTERNAL_PRIMERS:
case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER :
case Constants.PROCESS_APPROVE_INTERNAL_PRIMERS :
{
additional_jsp_buffer.append("<tr><td colspan = 2><table width='100%' border='0' cellspacing='2' cellpadding='2'><tr><td ><strong>");
additional_jsp_buffer.append("<input type='radio' name='"+ PrimerDesignerRunner.STRETCH_PRIMERS_APNAME_SEQUENCE_COVERAGE_TYPE+"' value='"+ OligoCalculation.TYPE_OF_OLIGO_CALCULATION_REFSEQUENCE+ "' checked>");
additional_jsp_buffer.append("Display primers for Reference Sequence</strong> </td></tr><tr><td><strong>");
additional_jsp_buffer.append("<input type='radio' name='"+PrimerDesignerRunner.STRETCH_PRIMERS_APNAME_SEQUENCE_COVERAGE_TYPE +"' value='"+ OligoCalculation.TYPE_OF_OLIGO_CALCULATION_STRETCH_COLLECTION +"'>");
additional_jsp_buffer.append("Display primers for Stretch Collection</strong></td></tr><tr>");
additional_jsp = additional_jsp_buffer.toString();

 break;
}

case Constants.PROCESS_VIEW_OLIGO_ORDER_BY_CLONEID:
    {
        additional_jsp ="<tr><td ><i>Note:</i>No more than 20 clones per request!</td></tr>";
        break;
        }

case Constants.PROCESS_CREATE_REPORT:{break;}
case Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING  :{ break;}
case Constants.PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING  :{ break;}
case Constants.PROCESS_CREATE_REPORT_TRACEFILES_QUALITY:  { break;}
case Constants.PROCESS_DELETE_PLATE :{  break;}
case Constants.PROCESS_DELETE_CLONE_READS  :{    break;}
case Constants.PROCESS_DELETE_CLONE_FORWARD_READ :{   break;}
case Constants.PROCESS_DELETE_CLONE_REVERSE_READ  :{  break;}
case Constants.PROCESS_DELETE_CLONE_SEQUENCE: { break;}
case  Constants.PROCESS_GET_TRACE_FILE_NAMES :{ break;}
case  Constants.PROCESS_DELETE_TRACE_FILES :{ break;}
case  Constants.PROCESS_MOVE_TRACE_FILES  :{ break;}
case Constants.PROCESS_PROCESS_OLIGO_PLATE:
{
 additional_jsp_buffer.append("<tr><td colspan='2'><table border =0 width='100%'>");
additional_jsp_buffer.append("<tr>     <td><strong>Set Container Status:</strong></td>");
additional_jsp_buffer.append( " <td>   <select name='status'>");
additional_jsp_buffer.append(   "     <option value="+ OligoContainer.STATUS_RECIEVED+ ">Recieved</option>");
additional_jsp_buffer.append(    "    <option value="+  OligoContainer.STATUS_SENT_FOR_SEQUENCING+">Used for sequencing</option>");
additional_jsp_buffer.append(   "   </select>   </td></tr>");
additional_jsp_buffer.append( " <tr>     <td><strong>Order Comments</strong></td>");
additional_jsp_buffer.append( "   <td> 	<textarea name='order_comments' rows='2' cols='40' ></textarea></td></tr>");
additional_jsp_buffer.append(" <tr>     <td><strong>Sequencing Comments</strong></td>");
additional_jsp_buffer.append( "   <td> <textarea name='sequencing_comments' rows='6' cols='50' ");
additional_jsp_buffer.append(  "  if ( container.getStatus() !=  OligoContainer.STATUS_RECIEVED) { disabled}></textarea></td></tr>");
additional_jsp = additional_jsp_buffer.toString();
break;
}
}
if ( isTryMode )
{
    additional_jsp += "<tr><td colspan='2'>"+line_padding+"<input type='checkbox' name='isTryMode' checked value=1><b>Run in try mode?</b></td></tr>";
   }
%>


 <%= additional_jsp %>
 
 <!-- special case -->
 
 <% if ( forwardName_int == Constants.PROCESS_RUN_DECISION_TOOL_NEW)
     { %> <jsp:include page="decision_tool_input.jsp" />  <%}%>