<%@ page language="java" %>
<%@ page errorPage="../ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>


<%@ page import="edu.harvard.med.hip.flex.infoimport.*" %>
<%@ page import="edu.harvard.med.hip.flex.util.*" %>
<%@ page import="java.util.*" %>
 <%@ page import="edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions" %>
<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : <bean:message key="flex.name"/> <%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CREATE_SUBMISSION_FILES.getMainPageTitle() %></title>
<LINK REL=StyleSheet HREF="./FlexStyle.css" TYPE="text/css" MEDIA=screen>

</head>
<body>
  <%
  PlasmidImporterDefinitions.IMPORT_ACTIONS cur_process = 
    PlasmidImporterDefinitions.IMPORT_ACTIONS.valueOf( (String)request.getAttribute("forwardName"));%>
    
    
<h2><bean:message key="flex.name"/> : <%= cur_process.getMainPageTitle() %>
</h2><hr>
<html:errors/>
 
 <h3 style="text-align: left; color: rgb(0, 128, 0); white-space: nowrap;">

<%= cur_process.getPageTitle() %></h3>
<html:form action="/FlexToPlasmidTransfer.do" enctype="multipart/form-data"> 
<input type="hidden" name="forwardName"  value="<%= cur_process.getNextProcess().toString() %>" >   

<table cellpadding="2" cellspacing="2" width="90%" align="center">
<tr>
    <td colspan=2><%= cur_process.getTitle()%><BR><BR></td></tr>

<tr class="headerSectionRow">  
    <td  height="29" align="center" width="50%">   FLEX definition   </td>
    <td align="center">    PLASMID definiton  </td>
    </tr>
 
<tr class="evenRow">   <td>  
    <select name="flexSelectedItem">
     <logic:iterate id="item" name="flexItems"  >
    	     <option value="<bean:write name="item" />">
    	     <bean:write name="item" />  
         </logic:iterate>
</select></td><td>
<select name="plasmidSelectedItem">
<logic:iterate id="item" name="plasmidItems"  >
	     <option value="<bean:write name="item" />">
	     <bean:write name="item" />  
     </logic:iterate>
</select>
</td></tr>
<tr><td align="center" colspan="2"><html:submit/></td></tr>

</table>
<P><P>

</html:form>
<hr>
    <h3 style="text-align: center; color: rgb(0, 128, 0); white-space: nowrap;">
Currenlty mapped items </h3>

<table cellpadding="2" cellspacing="2" width="90%" align="center" border="1">
<tr class="headerSectionRow">  
    <td  height="29" align="center" width="50%" colspan="2">   FLEX definition   </td>
    <td align="center"  colspan="2">    PLASMID definiton  </td>    
    <td align="center"  colspan="2">  Delete mapping  </td>    </tr>
   
   <tr class="evenRow" > 
        <td  align="center"> FLEX item ID </td>  
        <td  align="center"> FLEX item name </td>  
        <td  align="center"> PLASMID item ID </td>  
        <td  align="center">PLASMID item name  </td>  
          <td  align="center">&nbsp; </td>     
    </tr> 
    <logic:iterate id="item" name="mappedItems"  >
        <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
            <td> <logic:notEqual name="item" property="flexId" value="-1">
                <bean:write name="item" property="flexId" />
            </logic:notEqual> &nbsp; </td>  
        <td align="right"> <bean:write name="item" property="flexName" />  </td>  
        <td>  <logic:notEqual name="item" property="plasmidId" value="-1">
                <bean:write name="item" property="plasmidId" />
            </logic:notEqual> &nbsp;  </td>  
        <td align="right"> <bean:write name="item" property="plasmidName" />  </td>  
        <td align="center">
            <a href="FlexToPlasmidTransfer.do?MAPTYPE=<%=cur_process.getNextProcess().toString()%>&amp;PLASMIDNAME=<bean:write name="item" property="plasmidName" />&amp;FLEXNAME=<bean:write name="item" property="flexName" />&amp;forwardName=<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.DELETE_MAPPING_ITEM.toString()%>" onclick="return confirm('Are you sure you want to delete mapping?');">Delete </a>
          </td>  
             
    </flex:row>
    </logic:iterate>
    
</table>

</body>
</html:html>
