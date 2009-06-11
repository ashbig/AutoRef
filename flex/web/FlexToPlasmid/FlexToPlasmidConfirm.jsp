<%-- 
    Document   : FlexToPlasmidConfirm
    Created on : Nov 19, 2008, 12:33:40 PM
    Author     : htaycher
--%>

<%@ page language="java" %>
<%@ page errorPage="../ProcessError.do"%>

<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ page import="edu.harvard.med.hip.flex.infoimport.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>
 


<%@ page import="edu.harvard.med.hip.flex.util.*" %>
<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="java.util.*" %>
 <%@ page import="edu.harvard.med.hip.flex.infoimport.plasmidimport.databasemanipulation.*" %>

 <%@ page import="edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions" %>
<html><head>
<title><bean:message key="flex.name"/> : <bean:message key="flex.name"/> <%= PlasmidImporterDefinitions.IMPORT_ACTIONS.UPLOAD_SUBMISSION_FILES.getMainPageTitle() %></title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>

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
 
              
<logic:equal name="forwardName"  value="<%=  PlasmidImporterDefinitions.IMPORT_ACTIONS.PLASMID_DICTIONARY_TABLE_POPULATE.toString() %>">
 <logic:present name="displayTable">
 Process finished: populating table <bean:write name="displayTable" property="name"/>
</logic:present>        
</logic:equal>                  

<logic:equal name="forwardName"  value="<%=  PlasmidImporterDefinitions.IMPORT_ACTIONS.PLASMID_DICTIONARY_TABLE_SHOW_CONTENT.toString() %>">
<logic:present name="displayTable">
     <table border="1" cellpadding="2" cellspacing="2">
 <tr ><td colspan="4" class="prompt">Table name: <bean:write name="displayTable" property="name"/></td></tr>
 <tr class="headerSectionRow"> 
 <logic:iterate id="column" name="displayTable" property="columns"  >
  <td><bean:write name="column" property="name"/></td></logic:iterate></tr>
 </tr>
 
 <% DatabaseTable db = (DatabaseTable) request.getAttribute("displayTable");
     int number_columns = db.getColumns().size();
     ArrayList data = ((DatabaseColumn)db.getColumns().get(0)).getData();
    int number_rows = data.size();
    String data_item;
    for (int cc = 0; cc <  number_rows ; cc++)
    	      {
    	      	%><flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
    	  	<%for (int c_columns = 0; c_columns < number_columns; c_columns++)
    	      	{data_item=(String)((DatabaseColumn) db.getColumns().get(c_columns)).getData().get(cc);
    	      	data_item=(data_item==null || data_item.trim().length()==0)?"&nbsp;":data_item;
    	      	%>
    	      	<td> <%=data_item %></td>
    	  	
    	<%}%>	</flex:row>  <% }%>
	
    
    
    
    
     </table>
 </logic:present>
 
</logic:equal>
     
<logic:equal name="forwardName"  value="<%=  PlasmidImporterDefinitions.IMPORT_ACTIONS.PLASMID_DICTIONARY_TABLE_STRUCTURE.toString() %>">

<logic:present name="displayTable">
     <table border="1" cellpadding="2" cellspacing="2">
         <tr ><td colspan="4" class="prompt">Table name: <bean:write name="displayTable" property="name"/></td></tr>
         <tr class="headerSectionRow">  <td>Column Name</td>
         <td>Data type</td><td>Data length</td> <td>is Nullable</td> </tr>
         <logic:iterate id="column" name="displayTable" property="columns"  >
           <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
             <td><bean:write name="column" property="name"/></td>
             <td><bean:write name="column" property="dataType"/></td>
             <td><bean:write name="column" property="length"/></td>
             <td><bean:write name="column" property="isNullable"/></td>
          </flex:row></logic:iterate>
         </table>
 </logic:present>        
</logic:equal>

<% String  output="<b>Flex item: <i>"+ request.getAttribute("flexSelectedItem")+"</i><br>"
        +"  mapping  to PLASMID item <i>"  + request.getAttribute("plasmidSelectedItem")+ "</i>.</b>";
%>
<logic:equal name="forwardName"  value="<%=PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_AUTHOR_SUBMITTED.toString() %>"><%= output%></logic:equal>
<logic:equal name="forwardName"  value="<%=PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_AUTHOR_TYPE_SUBMITTED.toString() %>"><%= output%></logic:equal>
<logic:equal name="forwardName"  value="<%=PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_VECTOR_NAMES_SUBMITTED.toString() %>"><%= output%></logic:equal>
 <logic:equal name="forwardName"  value="<%=PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_SPECIES_SUBMITTED.toString() %>"><%= output%></logic:equal>
<logic:equal name="forwardName"  value="<%=PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_NAMETYPE_SUBMITTED.toString() %>"><%= output%></logic:equal>
<logic:equal name="forwardName"  value="<%=PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_CLONEPROPERTY_NAMETYPE_SUBMITTED.toString() %>"><%= output%></logic:equal>
<logic:equal name="forwardName"  value="<%=PlasmidImporterDefinitions.IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_CLONE_NAMETYPE_SUBMITTED.toString() %>"><%= output%></logic:equal>
<logic:equal name="forwardName"  value="<%=PlasmidImporterDefinitions.IMPORT_ACTIONS.DELETE_MAPPING_ITEM.toString() %>"><%= output%></logic:equal>

</body>
</html>
