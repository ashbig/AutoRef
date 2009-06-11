<%@ page language="java" %>
<%@ page import="java.util.*"%>
<%@ page import="edu.harvard.med.hip.flex.user.*"%>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="edu.harvard.med.hip.flex.Constants"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>


<%@ include file="define_user_access_level.jsp" %>
<html>
<head>
     <LINK href="left_menu_style.css" type="text/css" rel="stylesheet">

 <!--  <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>-->
</head>

<body  bgcolor="#9bbad6"  >

<table width="100%"   align="center" cellpadding="2" cellspacing="2" border="0">
<tr><td><div align="center"><h3><bean:message key="flex.name"/></h3><hr>    </div>
  


<div id="myMenuMain">
<a href="/FLEX/overview.jsp" target="display">Home</a> <span>|</span>
</div>


 
<% if (user_level >= RESEARCHER){%>    
<div id="myMenuMain">
    <a href="/FLEX/AddItems.jsp" target="display" >Add</a><span>|</span>
</div><%}%>

<% if (user_level >= WADMIN){%>    
<div id="myMenuMain">
    <a href="/FLEX/AddWorkflowItems.jsp" target="display" >Workflow managment</a><span>|</span>
</div><%}%>

 <% if (user_level >= RESEARCHER){%> 
 <div class="navheader">Process</div>
  
     <% if (user_level >= WADMIN){%> 
<div id="myMenu"><a href="/FLEX/GetProjects.do?forwardName=SPECIAL_OLIGO_ORDER" target="display">Special Oligo Order</a></div><%}%>
     <% if (user_level >= RESEARCHER){%>      
<div id="myMenu"><a href="/FLEX/SetReceiveDate.do" target="display">Receive Oligo Orders</a></div><%}%>
     <% if (user_level >= RESEARCHER){%>      
<div id="myMenu"><a href="/FLEX/GetProjects.do?forwardName=CREATE_PROCESS_PLATES" target="display">Create Process Plates</a></div><%}%>
     <% if (user_level >= RESEARCHER){%>      
<div id="myMenu"><a href="/FLEX/GetProjects.do?forwardName=ENTER_RESULT" target="display">Enter Process Results</a></div><%}%>
     <% if (user_level >= WADMIN){%>    
<div id="myMenu"><a href="/FLEX/RearraySelection.jsp" target="display">Rearray</a></div><%}%>
     <% if (user_level >= RESEARCHER){%>    
<div id="myMenu"><a href="/FLEX/ExpressionCloneEntry.jsp" target="display">Expression Clones</a></div><%}%>
     <% if (user_level >= RESEARCHER){%>    
<div id="myMenu"><a href="/FLEX/GenerateMultipleGlycerolInput.jsp" target="display">Create Multiple Glycerol Stocks</a></div><%}%>   
     <% if (user_level >= RESEARCHER){%>    
<div id="myMenu"><a href="/FLEX/GetProjects.do?forwardName=PLATE_CONDENSATION" target="display">Plate Condensation</a></div><%}%>
     <% if (user_level >= WADMIN){%>    
<div id="myMenu"><a href="/FLEX/ACEtoFLEXImporterInput.jsp" target="display">ACE to FLEX data transfer</a></div><%}%>           
    
 <% if (user_level >= WADMIN){%>    
<div id="myMenu"><a href="/FLEX/FLEXtoPLASMIDOptions.jsp" target="display">FLEX to PLASMID data transfer</a></div><%}%>           
     <%}%>
 
<% if (user_level >= COLLABORATOR){%>  
<div class="navheader">History</div>
  <div id="myMenu"> 
     <a href="/FLEX/ContainerScan.jsp?title=Container Process History" target="display">Container History</a>
     <a href="/FLEX/QuerySequenceHistory.do" target="display">Clone History</a>
     <a href="/FLEX/QueryStorageCloneInput.jsp" target="display">Clone Storage</a>
 </div>
     <%}%>
 
 <% if (user_level >= RESEARCHER){%>   
 <div id="myMenuMain">  <a href="/FLEX/ViewItemsMenuPage.jsp" target="display">View</a>
 </div><%}%>    

 <% if (user_level >= RESEARCHER){%>    
<div id="myMenuMain"> <a href="/FLEX/menu_MGCProject.jsp" target="display">MGC Project</a></div><%}%>
                
          
              
 <% if (user_level >= RESEARCHER){%>
 <div id="myMenuMain"> <a href="/FLEX/menu_Query.jsp" target="display">Query</a></div><%}%>               

 <div id="myMenuMain"> <a href="/FLEX/Logout.do" target="_top">Logout</a></div>
 <hr>
     <address><a href="mailto:HIP_Informatics@hms.harvard.edu"><b>FLEXGene Support</b></a></address>

<small>** This system and the underlying database was built in conjunction with
<a href="http://www.3rdmill.com" target="_blank">3rd Millennium Inc.</a> **</small>
</td></tr></table>


</body>
</html>
