
<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<%@ page import="java.util.*"%>
<%@ page import="edu.harvard.med.hip.bec.util.*"%>
<%@ page import="edu.harvard.med.hip.bec.user.*"%>
<%@ page import="edu.harvard.med.hip.bec.Constants"%>
<%@ page import="edu.harvard.med.hip.utility.*" %>


<head>
<style type="text/css">
.menuOut {
	cursor: pointer;
	display: block;
	margin:2px;
	color:#ffffff;
	width:151px;
	border:0px solid #000000;
	padding:2px;
	text-align:left;
	font-family:verdana, helvetica, sans-serif;
	font-size:12px;
	font-weight:normal;
}

.menuOver {
	cursor:pointer;
	display: block;
	margin:2px;
	background-color:#D2E7FF;
	color:#0062BF;
	width:151px;
	border:0px solid #000000;
	padding:2px;
	text-align:left;
	font-family:verdana, helvetica, sans-serif;
	font-size:12px;
	font-weight:normal;
}

.submenu {
	line-height: 140%;
	font-family:verdana, helvetica, sans-serif;
	font-size:11px;
	padding-left:12px;
	background-color:#C7E3FF;
}

.submenu a:link {
	color:#0062BF;
	line-height: 140%;
	font-size:11px;
	text-decoration:none;
	font-weight:normal; 
}

.submenu a:visited {
	color:#0062BF;
	font-size:11px;
	text-decoration:none;
	font-weight:normal; 
}

.submenu a:active {
	color:#0062BF;
	font-size:11px;
	text-decoration:none;
	font-weight:normal; 
}

.submenu a:hover {
	color:#000000;
	font-size:11px;
	text-decoration:none;
	font-style: normal;
	font-weight:normal;
	background-color:#7CBFFF;
}
</style>
<script type="text/javascript">

function SwitchMenu(obj){
	if(document.getElementById){
	var el = document.getElementById(obj);
	var ar = document.getElementById("cont").getElementsByTagName("DIV");
		if(el.style.display == "none"){
			for (var i=0; i < ar.length; i++){
				ar[i].style.display = "none";
			}
			el.style.display = "block";
		}else{
			el.style.display = "none";
		}
	}
}
function ChangeClass(menu, newClass) {
	 if (document.getElementById) {
	 	document.getElementById(menu).className = newClass;
	 }
}
document.onselectstart = new Function("return true");
</script>


<% //User user = (User)session.getAttribute(Constants.USER_KEY);
int user_level = 0;
if (user.getUserGroup().equals("Researcher")) user_level = 0;
else if (user.getUserGroup().equals("Researcher2")) user_level = 1;
else if (user.getUserGroup().equals("Manager")) user_level =2;
else if (user.getUserGroup().equals("Administrator")) user_level = 3;

String red_arrow_pass = edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION")+"jpg/red_diam.gif";
String image = "<img border='0' src='" + red_arrow_pass +"' width = '6' height='6'>";

%>
</head>

<body>

<!-- Menu start -->

<div id="cont" >


<% if (user_level > 2)
{%> 

 <p id="menu_configure_system" class="menuOut" onclick="SwitchMenu('sub_configure_system')" onmouseover="ChangeClass('menu_configure_system','menuOver')" onmouseout="ChangeClass('menu_configure_system','menuOut')">Cloning project settings</p>
<div class="submenu" id="sub_configure_system" style="display:none;">
<%= image %><a href="DirectDatabaseCommunications.do?forwardName=<%= Constants.PROCESS_ADD_PROJECT_DEFINITION%>"  title="Add new project definition">Project definition</a><br/>
<%= image %><a href="DirectDatabaseCommunications.do?forwardName=<%= Constants.PROCESS_ADD_SPECIES_DEFINITION%>"  title="Add new species definition">Species definition</a><br/>
<%= image %><a href="DirectDatabaseCommunications.do?forwardName=<%= Constants.PROCESS_ADD_NAME_TYPE%>"  title="Add new name type">Annotation type</a><br/>
<hr>
<%= image %><a href="DirectDatabaseCommunications.do?forwardName=<%= Constants.PROCESS_ADD_NEW_VECTOR%>"  title="Add new vector">Vector information</a><br/>
<%= image %><a href="DirectDatabaseCommunications.do?forwardName=<%= Constants.PROCESS_ADD_NEW_COMMON_PRIMER%>"  title="Add new sequencing primer">Sequencing primer</a><br/>
<%= image %><a href="DirectDatabaseCommunications.do?forwardName=<%= Constants.PROCESS_ADD_NEW_CONNECTION_VECTOR_LINKER%>"  title="Add new vector - common primer association">Link vector with sequencing primer</a><br/>
<%= image %><a href="DirectDatabaseCommunications.do?forwardName=<%= Constants.PROCESS_ADD_NEW_LINKER%>"  title="Add new linker">Linker information</a><br/>
<%= image %><a href="DirectDatabaseCommunications.do?forwardName=<%= Constants.PROCESS_ADD_NEW_CLONINGSTRATEGY%>"  title="Add new cloning strategy">Cloning strategy</a><br/>
</div>
<%}%>
    
   <!-- create process configuration -->
<% if ( user_level > 1)
{%>

<p id="menu_configure_process" class="menuOut" onclick="SwitchMenu('configure_specs')" onmouseover="ChangeClass('menu_configure_process','menuOver')" onmouseout="ChangeClass('menu_configure_process','menuOut')">Analysis settings</p>
<div class="submenu" id="configure_specs" style="display:none;">
<%= image %><a href="page_main.jsp?forwardName=<%=Constants.UI_ISOLATE_RANKER_SPEC_PAGE%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > Analysis Settings > Clone Ranking&amp;<%= Constants.JSP_TITLE%>=Create New Set of Parameter for Clone Ranking"  title="Create new specification for clone ranking">Clone ranking</a><br/>
<%= image %><a href="page_main.jsp?forwardName=<%=Constants.UI_CLONEEVAL_SPEC_PAGE%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > Analysis Settings > Clone Acceptance Criteria&amp;<%= Constants.JSP_TITLE%>=Create New Set of Parameter for Clone Acceptance Criteria"  title="Create new specification for clone acceptance">Clone acceptance criteria</a><br/>
<%= image %><a href="page_main.jsp?forwardName=<%=Constants.UI_PRIMER_DESIGNER_SPEC_PAGE%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > Analysis Settings > Primer Designer&amp;<%= Constants.JSP_TITLE%>=Create New Set of Parameter for Primer Design"  title="Create new specification for design of internal primers">Primer design</a><br/>
<%= image %><a href="page_main.jsp?forwardName=<%=Constants.UI_POLYMFINDER_SPEC_PAGE%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > Analysis Settings > Polymorphism Detection&amp;<%= Constants.JSP_TITLE%>=Create New Set of Parameter for Polymorphism Detection"  title="Create new specification for polymorphism detection">Polymorphism detection</a><br/>
<%= image %><a href="page_main.jsp?forwardName=<%=Constants.UI_SEQUENCETRIMMING_SPEC_PAGE%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > Analysis Settings > Sequence Trimming&amp;<%= Constants.JSP_TITLE%>=Create New Set of Parameter for Sequence Trimming"  title="Create new specification for sequence trimming">Sequence trimming</a><br/>

</div>

<%}%>
<!--end  create process configuration -->


<% if (user_level > 2)
{%>    
<p id="trace_files_upload" class="menuOut" onclick="SwitchMenu('trace_files_upload_menus')" onmouseover="ChangeClass('trace_files_upload','menuOver')" onmouseout="ChangeClass('trace_files_upload','menuOut')" >Trace files</p>

<div class="submenu" id="trace_files_upload_menus" style="display:none;">
<%= image %><a href="DirectDatabaseCommunications.do?forwardName=<%= Constants.PROCESS_ADD_TRACE_FILE_NAME_FORMAT%>"  title="Create trace file name format">Create name format</a><br/>
<%= image %><a href="page_main.jsp?forwardName=<%=Constants.PROCESS_CREATE_RENAMING_FILE_FOR_TRACEFILES_TRANSFER%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > Trace Files > Create Renaming File&amp;<%= Constants.JSP_TITLE%>=Create Renaming File for Trace Files" title="Create renaming file">Create renaming file</a><br/>
<%= image %><a href="page_main.jsp?forwardName=<%=Constants.PROCESS_INITIATE_TRACEFILES_TRANSFER%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > Trace Files > Upload Trace Files&amp;<%= Constants.JSP_TITLE%>=Upload Trace Files" title="Upload trace files">Upload trace files</a><br/>

</div>
<%}%>

<!-- process -->
<% if (user_level > 0)
{%> 
<p id="Process" class="menuOut" onclick="SwitchMenu('process_sub')" onmouseover="ChangeClass('Process','menuOver')" onmouseout="ChangeClass('Process','menuOut')">Process</p>
<div class="submenu" id="process_sub" style="display:none;">
<% if (user_level > 2)
{
 if (BecProperties.getInstance().isInternalHipVersion())
            {%>
 <%= image %><a href="SelectProcess.do?forwardName=<%=Constants.PROCESS_UPLOAD_PLATES%>" title="Upload plate information">Upload plate information</a><br/>
<% }
        else{%>
 <%= image %><a href="page_main.jsp?forwardName=<%=Constants.UI_SELECT_PROCESS_UPLOAD_DATA_PAGE%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > Process > Upload containers&amp;<%= Constants.JSP_TITLE%>=Upload plate information" title="Upload plate information">Upload plate information</a><br/>
<%}%>  


<%= image %><a href="page_main.jsp?forwardName=<%=Constants.UI_SELECT_PROCESS_EREAD_MANIPULATION_PAGE%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > Process > Read Manipulation&amp;<%= Constants.JSP_TITLE%>=End Read Manipulation" title="End read manipulation">Read manipulation</a><br/>
<%= image %><a href="page_main.jsp?forwardName=<%=Constants.UI_SELECT_PROCESS_PAGE%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > Process > Evaluate Clones&amp;<%= Constants.JSP_TITLE%>=Evaluate clones" title="Evaluate Clones">Evaluate clones</a><br/>

<%= image %><a href="page_main.jsp?forwardName=<%=Constants.UI_SELECT_PROCESS_INTERNAL_PRIMERS_PAGE%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > Process > Internal Primer Design and Order&amp;<%= Constants.JSP_TITLE%>=Internal Primer Design and Order" title="Internal primer design and order">Internal primer design and order</a><br/>

<%= image %><a href="DirectDatabaseCommunications.do?forwardName=<%= Constants.PROCESS_SUBMIT_CLONE_SEQUENCES %>"    title="Upload clones sequences">Upload clones sequences</a><br/>

<%= image %><a href="SelectProcess.do?forwardName=<%=Constants.PROCESS_SET_CLONE_FINAL_STATUS%>" title="Set final clone status">Set final clone status</a><br/>


<%= image %><a href="page_main.jsp?forwardName=<%=Constants.UI_SELECT_PROCESS_DELETE_DATA_PAGE%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > Process > Delete Data&amp;<%= Constants.JSP_TITLE%>=Delete data" title="Delete Data">Delete data</a><br/>

<%}%>
<%= image %><a href="page_main.jsp?forwardName=<%=Constants.UI_VIEW_PROCESS_RESULTS_PAGE%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > Process > View Process Results&amp;<%= Constants.JSP_TITLE%>=View Process Results" title="View process results">View process results</a><br/>

</div>
<!-- process end-->

<!-- report -->
<% if ( user_level > 0)
{%>
<p id="report" class="menuOut" onclick="SwitchMenu('sub_report')" onmouseover="ChangeClass('report','menuOver')" onmouseout="ChangeClass('report','menuOut')">Reports</p>
<div class="submenu" id="sub_report" style="display:none;">

    
<%= image %><a href="SelectProcess.do?forwardName=<%=Constants.PROCESS_RUN_DECISION_TOOL%>"  title="Run visual version of decision tool">Quick decision tool </a><br/>
<%= image %><a href="SelectProcess.do?forwardName=<%=Constants.PROCESS_RUN_DECISION_TOOL_NEW%>"  title="Run decision tool ">Detailed decision tool</a><br/>
<hr>
<%= image %><a href="SelectProcess.do?forwardName=<%=Constants.PROCESS_NOMATCH_REPORT%>"  title="Report for clone that do not match reference sequence ">Mismatched clones</a><br/>
<hr>
<%= image %><a href="page_main.jsp?forwardName=<%=Constants.PROCESS_CREATE_REPORT%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > Reports > General Report&amp;<%= Constants.JSP_TITLE%>=Run General Report"  title="General Report">General report</a><br/>
<%= image %><a href="SelectProcess.do?forwardName=<%=Constants.PROCESS_CREATE_REPORT_TRACEFILES_QUALITY%>"  title="Trace file quality report">Trace file quality</a><br/>
<%= image %><a href="SelectProcess.do?forwardName=<%=Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING%>"  title="Order list for end read repeat">End read report</a><br/>

<%}%>
</div>
<!-- report end -->


<!-- view , anyone with updated status-->
<% if ( user_level > 0)
{%>
<p id="view" class="menuOut" onclick="SwitchMenu('sub_view')" onmouseover="ChangeClass('view','menuOver')" onmouseout="ChangeClass('view','menuOut')">View</p>
<div class="submenu" id="sub_view" style="display:none;">
<%= image %><a href="ContainerScan.jsp?forwardName=<%=Constants.CONTAINER_RESULTS_VIEW%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > View > Plate Results&amp;<%= Constants.JSP_TITLE%>=Plate Results"  title="Plate Results">Plate Results</a><br/>
<hr>

<%= image %><a href="ContainerScan.jsp?forwardName=<%=Constants.CONTAINER_PROCESS_HISTORY%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > View > Plate History&amp;<%= Constants.JSP_TITLE%>=Plate History"  title="Plate History">Plate History</a><br/>
<%= image %><a href="ContainerScan.jsp?forwardName=<%=Constants.CONTAINER_DEFINITION_INT%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > View > Plate Description&amp;<%= Constants.JSP_TITLE%>=Plate Description"  title="Plate Description">Plate Description</a><br/>
<%= image %><a href="InitiateProcess.jsp?forwardName=<%=Constants.PROCESS_SHOW_CLONE_HISTORY%>&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > View > Clone History&amp;<%= Constants.JSP_TITLE%>=Clone History"  title="Clone History">Clone History</a><br/>
<hr>
<%= image %><a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.AVAILABLE_CONTAINERS_INT%>"  title="Names of containers submitted into ACE">Plates</a><br/>
<hr>

<%= image %><a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.AVAILABLE_SPECIFICATION_INT%>"  title="All available process configurations">Process configurations</a><br/>
<%= image %><a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.AVAILABLE_VECTORS_DEFINITION_INT%>"  title="All available vectors">Vectors</a><br/>
<%= image %><a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.AVAILABLE_LINKERS_DEFINITION_INT%>"  title="All available linkers">Linkers</a><br/>

<%}%>
</div>
<!-- end view -->
<%}%>	

</div>
</body>
<!-- Menu end -->