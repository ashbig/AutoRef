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
    <style type="text/css">
dt { font-weight: bold; font-size:small;text-indent: 0.5em;}
dd { font-weight: bold; font-size:small;text-indent: -1em; text-align: left;white-space: nowrap}

ul {
	list-style: none;
	margin-left: 0;
	padding-left: 0.5em;
	text-indent: -0.5em;
	
        font-weight: bold; font-size:small;
       text-align: left;
       white-space: nowrap
	}
        
li {
	list-style: none;
	margin-left: 0;
	padding-left: 0.5em;
	text-indent: -0.5em;
	
        font-weight: bold; font-size:small;
       text-align: left;
       white-space: nowrap
	}
    
</style>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>


<body bgcolor="#9bbad6">
  <div align="center"><center><h3><bean:message key="flex.name"/></h3>    </div>
  <hr>
<ul>
    <li><p><a href="/FLEX/overview.jsp" target="display">Home</a>  </li>
<% if (user_level >= RESEARCHER){%>    
<li> <a href="/FLEX/AddItems.jsp" target="display">Add</a></li><%}%>     

 <% if (user_level >= RESEARCHER){%> 
 <li>Process</li>
 <ul>
     <% if (user_level >= WADMIN){%> 
     <li><a href="/FLEX/GetProjects.do?forwardName=SPECIAL_OLIGO_ORDER" target="display">Special Oligo Order</a></li><%}%>
     <% if (user_level >= RESEARCHER){%>      
     <li><a href="/FLEX/SetReceiveDate.do" target="display">Receive Oligo Orders</a></li><%}%>
     <% if (user_level >= RESEARCHER){%>      
     <li><a href="/FLEX/GetProjects.do?forwardName=CREATE_PROCESS_PLATES" target="display">Create Process Plates</a></li><%}%>
     <% if (user_level >= RESEARCHER){%>      
     <li><a href="/FLEX/GetProjects.do?forwardName=ENTER_RESULT" target="display">Enter Process Results</a></li><%}%>
     <% if (user_level >= WADMIN){%>    
     <li><a href="/FLEX/RearraySelection.jsp" target="display">Rearray</a></li><%}%>
     <% if (user_level >= RESEARCHER){%>    
     <li><a href="/FLEX/ExpressionCloneEntry.jsp" target="display">Expression Clones</a></li><%}%>
     <% if (user_level >= RESEARCHER){%>    
     <li><a href="/FLEX/GenerateMultipleGlycerolInput.jsp" target="display">Create Multiple Glycerol Stocks</a></li><%}%>   
     <% if (user_level >= RESEARCHER){%>    
     <li><a href="/FLEX/GetProjects.do?forwardName=PLATE_CONDENSATION" target="display">Plate Condensation</a></li><%}%>
     <% if (user_level >= WADMIN){%>    
     <li><a href="/FLEX/ACEtoFLEXImporterInput.jsp" target="display">ACE to FLEX data transfer</a></li><%}%>           
     <%}%>
 </ul>
<% if (user_level >= COLLABORATOR){%>  
<li>History</li>
     <ul>
     <li> <a href="/FLEX/ContainerScan.jsp?title=Container Process History" target="display">Container History</a></li>
     <li><a href="/FLEX/QuerySequenceHistory.do" target="display">Clone History</a></li>
     <li><a href="/FLEX/QueryStorageCloneInput.jsp" target="display">Clone Storage</a></li>
     <%}%>
 </ul>
   
 <% if (user_level >= RESEARCHER){%>    
 <li>View</li>
 <ul> 
     <li><a href="/FLEX/ContainerScan.jsp?forwardName=<%= Constants.VIEW_CONTAINER %>&amp;title=Container Details" target="display">Container Details</a></li>
     <li><a href="/FLEX/GetProjects.do?forwardName=<%= Constants.NEW_PLATE_LABELS%>" target="display">Print Submitted Plates</a></li>
     <li><a href="/FLEX/ViewItems.do?forwardName=<%= Constants.VIEW_LINKERS%>" target="display">Linker</a></li>
     <li><a href="/FLEX/ViewItems.do?forwardName=<%= Constants.VIEW_VECTORS%>" target="display">Vectors</a></li>
     <li><a href="/FLEX/ViewItems.do?forwardName=<%= Constants.VIEW_CLONINGSTRATEGIES%>" target="display">Cloning Strategies</a></li>
     <%}%>
     
 </ul>           
                
<% if (user_level >= RESEARCHER){%>    
<li><a href="/FLEX/menu_MGCProject.jsp" target="display">MGC Project</a></li><%}%>
                
          
              
 <% if (user_level >= RESEARCHER){%>
 <li><a href="/FLEX/menu_Query.jsp" target="display">Query</a></li><%}%>               
<% if (user_level == CUSTOMER){%>    
 <li><a href="/FLEX/Help.jsp" target="display">Help</a>          </li><%}%>

 <li><a href="/FLEX/Logout.do" target="_top">Logout</a></li>
 <hr><li>
     <address><a href="mailto:HIP_Informatics@hms.harvard.edu">FLEXGene Support</a></address>
 </li>
</ul>
<small>** This system and the underlying database was built in conjunction with
<a href="http://www.3rdmill.com" target="_blank">3rd Millennium Inc.</a> **</small>



</body>
</html>
