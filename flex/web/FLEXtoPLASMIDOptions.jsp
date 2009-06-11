<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ page import="edu.harvard.med.hip.flex.infoimport.*" %>
 <%@ page import="edu.harvard.med.hip.flex.infoimport.plasmidimport.*" %>
<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : <bean:message key="flex.name"/><%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CREATE_SUBMISSION_FILES.getMainPageTitle() %></title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
    <link rel="StyleSheet" href="application_styles.css" type="text/css" media="screen">

</head>
<body>
<%@ include file="define_user_access_level.jsp" %>

    <h2><bean:message key="flex.name"/> :  Flex to PLASMID clone information transfer </h2>
<hr>
<html:errors/>

<p>
<p><b>Please select one of the following:</b></p>
<table width="95%" border="0" cellspacing="4" cellpadding="4">
  <tr class="headerSectionRow">  
    <td width="50%" align="center"> Upload clone information from FLEX to PLASMID     </td>
    <td align="center">Update clone status</td>
  </tr>
      
    <tr>
    <td valign="top"  ><ul> 
           <li><a href="/FLEX/FlexToPlasmidTransfer.do?forwardName=<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CREATE_SUBMISSION_FILES %>" ><%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CREATE_SUBMISSION_FILES.getTitle() %>  </a> </li>
        <li><a   href="/FLEX/FlexToPlasmid/TransferCloneDataFromFiles.jsp"><%=  PlasmidImporterDefinitions.IMPORT_ACTIONS.UPLOAD_SUBMISSION_FILES.getTitle() %>   </a> </li>
        <li><a  onclick="javascript:return false;" href="/FLEX/FlexToPlasmidTransfer.do?forwardName=<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.TRANSFER_CLONE_INFORMATION %>"><i>Not active link (under development):<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.TRANSFER_CLONE_INFORMATION.getTitle() %> </i> </a> </li>
            
    </ul> </td>
    <td valign="top">
    <ul> 
         <li><a href="/FLEX/FlexToPlasmidTransfer.do?forwardName=<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CHANGE_CLONE_STATUS %>" ><%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CHANGE_CLONE_STATUS .getTitle() %>  </a> </li>  
            </ul>    </td>
  </tr>
  <tr class="headerSectionRow" ><td align="center" >Populate PLASMID dictionary tables</td>
  <td  align="center" >Map definitions in FLEX and PLASMID</td></tr>
  <tr >    <td  valign="top" >  <ul> 
     
<li><a href="/FLEX/FlexToPlasmid/PopulatePLASMIDTables.jsp">
<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.PLASMID_DICTIONARY_TABLE_POPULATE.getTitle() %>    </a> </li>  
 </ul>
</td> <td  valign="top" >  <ul> 
     
  <li><a href="/FLEX/FlexToPlasmidTransfer.do?forwardName=<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_VECTOR_NAMES %>">
<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_VECTOR_NAMES.getPageTitle() %>    </a> </li>  
 <li><a href="/FLEX/FlexToPlasmidTransfer.do?forwardName=<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_SPECIES %>">
<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_SPECIES.getPageTitle() %>    </a> </li>  


<li><a href="/FLEX/FlexToPlasmidTransfer.do?forwardName=<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_AUTHOR %>">
<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_AUTHOR.getPageTitle() %>    </a> </li>  
<li><a href="/FLEX/FlexToPlasmidTransfer.do?forwardName=<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_AUTHOR_TYPE %>">
<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_AUTHOR_TYPE.getPageTitle() %>    </a> </li>  

<li><a href="/FLEX/FlexToPlasmidTransfer.do?forwardName=<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_NAMETYPE %>">
<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_NAMETYPE.getPageTitle() %>    </a> </li>  
 
<li><a href="/FLEX/FlexToPlasmidTransfer.do?forwardName=<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_CLONE_NAMETYPE %>">
<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_CLONE_NAMETYPE.getPageTitle() %>    </a> </li>  

<li><a href="/FLEX/FlexToPlasmidTransfer.do?forwardName=<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_CLONEPROPERTY_NAMETYPE %>">
<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_CLONEPROPERTY_NAMETYPE.getPageTitle() %>    </a> </li>  

</ul>
</td>
  </tr>
  
</table>

    
  

</body>
</html:html>
