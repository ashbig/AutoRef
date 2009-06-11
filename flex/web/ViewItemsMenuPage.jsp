<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ page import="edu.harvard.med.hip.flex.infoimport.*" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : <bean:message key="flex.name"/>View items</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
 </head>
<body>
<%@ include file="define_user_access_level.jsp" %>

    <h2><bean:message key="flex.name"/> :  View items</h2>
<hr>
<html:errors/>

<p>
<p><b>Please select one of the following:</b></p>
<table width="95%" border="0" cellspacing="4" cellpadding="4">
  <tr class="headerSectionRow">  
    <td width="50%" align="center">View processing related items     </td>
      <td align="center">&nbsp;</td>
    <!--<td align="center">View vector growth conditions related items</td>-->
  </tr>
    <tr>   <td valign="top"  > <ul> 
       <li><a href="/FLEX/ViewItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.VIEW_WORKFLOWS.toString()%>" target="display">Workflows</a></li>
    <li><a href="/FLEX/ContainerScan.jsp?forwardName=<%= Constants.VIEW_CONTAINER %>&amp;title=Container Details" target="display">Container Details</a></li>
     <li><a href="/FLEX/GetProjects.do?forwardName=<%= Constants.NEW_PLATE_LABELS%>" target="display">Print Submitted Plates</a></li>
     <P></P><li><a href="/FLEX/ViewItems.do?forwardName=<%= Constants.VIEW_LINKERS%>" target="display">Linker</a></li>
     <li><a href="/FLEX/ViewItems.do?forwardName=<%= Constants.VIEW_VECTORS%>" target="display">Vectors</a></li>
     <li><a href="/FLEX/ViewItems.do?forwardName=<%= Constants.VIEW_CLONINGSTRATEGIES%>" target="display">Cloning Strategies</a></li>
    
</ul></td>
<td valign="top"  >&nbsp;</td>
<!--<td valign="top"  > <ul> 
  <li><a href="/FLEX/ViewItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.VIEW_BIOMATERIAL %>"  target="display"><%= ConstantsImport.PROCESS_NTYPE.VIEW_BIOMATERIAL.getTitle() %>  </a> </li>  
        <li><a href="/FLEX/ViewItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.VIEW_BIOMATERIAL_CONDITION %>"  target="display"><%= ConstantsImport.PROCESS_NTYPE.VIEW_BIOMATERIAL_CONDITION.getTitle() %>  </a> </li>  
        <li><a href="/FLEX/ViewItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.VIEW_BIOMATERIAL_COMBINATION %>"  target="display"><%= ConstantsImport.PROCESS_NTYPE.VIEW_BIOMATERIAL_COMBINATION.getTitle() %>  </a> </li>  
        <li><a href="/FLEX/ViewItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.VIEW_GROWTHCONDITION  %>"  target="display"><%= ConstantsImport.PROCESS_NTYPE.VIEW_GROWTHCONDITION.getTitle() %>  </a> </li>  
       <li><a href="/FLEX/ViewItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.VIEW_VECTOR_GROWTH_CONDITON_TABLE  %>"  target="display"><%= ConstantsImport.PROCESS_NTYPE.VIEW_VECTOR_GROWTH_CONDITON_TABLE.getTitle() %>  </a> </li>  
    </ul> 
</td>-->
</tr>
   
<p>
    
    <tr class="headerSectionRow">  
    <td width="50%" align="center"> Select report type  </td>
    <td align="center"> &nbsp;</td>
  </tr>
  <tr>   <td valign="top"  > <ul> 
<li><a href="/FLEX/ReportGeneralInput.jsp">General Report </a> </li>  
<li><a href="/FLEX/ReportCloneRelationsInput.jsp">Clone Hierarchy Report </a> </li>  
<li><a href="/FLEX/ReportRun.do?reportType=<%= edu.harvard.med.hip.flex.report.ReportDefinition.REPORT_TYPE.CLONING_STRATEGY %>">Cloning Strategy Description Report </a> </li>  
</ul></td><td></td></tr>


</table>

</body>
 
</html:html>
