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
dt { font-weight: bold; font-size:small;}
dd { font-weight: bold; font-size:small;}
dl.margins-removed
{
margin: 0;
padding: 0;
}

.margins-removed dt
{
margin: 0;
padding: 0;
}

.margins-removed dd
{
margin: 0 0 1em 0;
padding: 0;
}
</style>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>


<body bgcolor="#9bbad6">
  <div align="center"><center><h3><bean:message key="flex.name"/></h3>    </div>
  <hr>
<dl>
    <dt><p><a href="/FLEX/overview.jsp" target="display">Home</a>  </dt>
<% if (user_level >= WADMIN){%>    
<dt> <a href="/FLEX/AddItems.jsp" target="display">Add</a></dt><%}%>     

 <% if (user_level >= RESEARCHER){%> 
 <dt>Process</dt>
 <% if (user_level >= WADMIN){%> 
 <dd><a href="/FLEX/GetProjects.do?forwardName=SPECIAL_OLIGO_ORDER" target="display">Special Oligo Order</a></dd><%}%>
 <% if (user_level >= RESEARCHER){%>      
<dd><a href="/FLEX/SetReceiveDate.do" target="display">Receive Oligo Orders</a></dd><%}%>
 <% if (user_level >= RESEARCHER){%>      
<dd><a href="/FLEX/GetProjects.do?forwardName=CREATE_PROCESS_PLATES" target="display">Create Process Plates</a></dd><%}%>
 <% if (user_level >= RESEARCHER){%>      
<dd><a href="/FLEX/GetProjects.do?forwardName=ENTER_RESULT" target="display">Enter Process Results</a></dd><%}%>
  <% if (user_level >= WADMIN){%>    
<dd><a href="/FLEX/RearraySelection.jsp" target="display">Rearray</a></dd><%}%>
  <% if (user_level >= RESEARCHER){%>    
<dd><a href="/FLEX/ExpressionCloneEntry.jsp" target="display">Expression Clones</a></dd><%}%>
  <% if (user_level >= RESEARCHER){%>    
<dd><a href="/FLEX/GetProjects.do?forwardName=PLATE_CONDENSATION" target="display">Plate Condensation</a></dd><%}%>
  <% if (user_level >= SADMIN){%>    
<dd><a href="/FLEX/ACEtoFLEXImporterInput.jsp" target="display">ACE to FLEX data transfer</a></dd><%}%>           
<%}%>
<% if (user_level >= COLLABORATOR){%>  
<dt>History</dt>
<dd> <a href="/FLEX/ContainerScan.jsp?title=Container Process History" target="display">Container History</a></dd>
<dd><a href="/FLEX/QuerySequenceHistory.do" target="display">Clone History</a></dd>
<dd><a href="/FLEX/QueryStorageCloneInput.jsp" target="display">Clone Storage</a></dd>
         <%}%>
     
 <% if (user_level >= RESEARCHER){%>    
 <dt> View</dt>
 <dd><a href="/FLEX/ContainerScan.jsp?forwardName=<%= Constants.VIEW_CONTAINER %>&amp;title=Container Details" target="display">Container Details</a></dd>
<dd><a href="/FLEX/GetProjects.do?forwardName=<%= Constants.NEW_PLATE_LABELS%>" target="display">Print Submitted Plates</a></dd>
<dd><a href="/FLEX/ViewItems.do?forwardName=<%= Constants.VIEW_LINKERS%>" target="display">Linker</a></dd>
<dd><a href="/FLEX/ViewItems.do?forwardName=<%= Constants.VIEW_VECTORS%>" target="display">Vectors</a></dd>
<dd><a href="/FLEX/ViewItems.do?forwardName=<%= Constants.VIEW_CLONINGSTRATEGIES%>" target="display">Cloning Strategies</a></dd>
  <%}%>
                
                
                
<% if (user_level >= RESEARCHER){%>    
<dt><a href="/FLEX/menu_MGCProject.jsp" target="display">MGC Project</a></dt><%}%>
                
          
              
 <% if (user_level >= CUSTOMER){%>
 <dt> <a href="/FLEX/menu_Query.jsp" target="display">Query</a></dt><%}%>               
<% if (user_level == CUSTOMER){%>    
 <dt><a href="/FLEX/Help.jsp" target="display">Help</a>          </dt><%}%>

 <dt><a href="/FLEX/Logout.do" target="_top">Logout</a></dt>
 <hr><dt>
     <address><a href="mailto:HIP_Informatics@hms.harvard.edu">FLEXGene Support</a></address>
 </dt>
</dl>
<small>** This system and the underlying database was built in conjunction with
<a href="http://www.3rdmill.com" target="_blank">3rd Millennium Inc.</a> **</small>



</body>
</html>
