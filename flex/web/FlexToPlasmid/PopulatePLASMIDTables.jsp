<%@ page language="java" %>
<%@ page errorPage="../ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ page import="edu.harvard.med.hip.flex.infoimport.*" %>
<%@ page import="edu.harvard.med.hip.flex.infoimport.plasmidimport.*" %>

<%@ page import="edu.harvard.med.hip.flex.infoimport.plasmidimport.filemanipulation.*" %>
 <%@ page import="edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions" %>
<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : <bean:message key="flex.name"/> <%= PlasmidImporterDefinitions.IMPORT_ACTIONS.PLASMID_DICTIONARY_TABLE_POPULATE.getMainPageTitle() %></title>
<LINK REL=StyleSheet HREF="../FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : <%= PlasmidImporterDefinitions.IMPORT_ACTIONS.PLASMID_DICTIONARY_TABLE_POPULATE.getMainPageTitle() %></h2>
<hr>
<html:errors/>
<h3><%= PlasmidImporterDefinitions.IMPORT_ACTIONS.PLASMID_DICTIONARY_TABLE_POPULATE.getPageTitle() %>
</h3>


This page allows you to add new records to the dictionary
tables of PLASMID database. 
The submission file MUST have the following format:
<ul>
     <li>File format: tab delimited file;
    <li>First line: dictionary table name (see drop down below);
    <li>Second line: table column names (column order is irrelavant);
    <li>If table has id column populated by Oracle sequence, column value should be sequence_name.nextVal;
    </ul>
<br><br>
<table border="0" cellpadding="2" cellspacing="2">
    <tr><td class="prompt">View table stucture:</td>
    <td> <html:form action="/FlexToPlasmidFileSubmission.do"  > 
        <html:hidden value="<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.PLASMID_DICTIONARY_TABLE_STRUCTURE.toString()%>" property="forwardName"/>
 <select name="tablename">
           <option value="<%= PlasmIDFileType.CLONEINSERT .toString()%>"> <%= PlasmIDFileType.CLONEINSERT.getDisplayTile() %>
<option value="<%= PlasmIDFileType.CLONEPROPERTY .toString()%>"> <%= PlasmIDFileType.CLONEPROPERTY.getDisplayTile() %>
<option value="<%= PlasmIDFileType.INSERTPROPERTY .toString()%>"> <%= PlasmIDFileType.INSERTPROPERTY.getDisplayTile() %>
<option value="<%= PlasmIDFileType.CLONEINSERTONLY .toString()%>"> <%= PlasmIDFileType.CLONEINSERTONLY.getDisplayTile() %>
<option value="<%= PlasmIDFileType.CNAMETYPE .toString()%>"> <%= PlasmIDFileType.CNAMETYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.CLONETYPE .toString()%>"> <%= PlasmIDFileType.CLONETYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.CLONENAMETYPE .toString()%>"> <%= PlasmIDFileType.CLONENAMETYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.SPECIES.toString()%>"> <%= PlasmIDFileType.SPECIES.getDisplayTile() %>
<option value="<%= PlasmIDFileType.REFSEQTYPE.toString()%>"> <%= PlasmIDFileType.REFSEQTYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.SPECIESREFSEQTYPE.toString()%>"> <%= PlasmIDFileType.SPECIESREFSEQTYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.REFSEQNAMETYPE .toString()%>"> <%= PlasmIDFileType.REFSEQNAMETYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.GROWTHCONDITION .toString()%>"> <%= PlasmIDFileType.GROWTHCONDITION.getDisplayTile() %>
<option value="<%= PlasmIDFileType.INSERTPROPERTYTYPE.toString()%>"> <%= PlasmIDFileType.INSERTPROPERTYTYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.SEQNAMETYPE.toString()%>"> <%= PlasmIDFileType.SEQNAMETYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.SAMPLETYPE.toString()%>"> <%= PlasmIDFileType.SAMPLETYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.CONTAINERTYPE.toString()%>"> <%= PlasmIDFileType.CONTAINERTYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.SPECIALTREATMENT.toString()%>"> <%= PlasmIDFileType.SPECIALTREATMENT.getDisplayTile() %>
<option value="<%= PlasmIDFileType.COUNTRY.toString()%>"> <%= PlasmIDFileType.COUNTRY.getDisplayTile() %>
<option value="<%= PlasmIDFileType.COLLECTION.toString()%>"> <%= PlasmIDFileType.COLLECTION.getDisplayTile() %>


        </select><html:submit value="View"/>
</html:form>
</td> </tr> 
   <tr><td class="prompt">View table content:</td>
   <td> <html:form action="/FlexToPlasmidFileSubmission.do"  > 
        <html:hidden value="<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.PLASMID_DICTIONARY_TABLE_SHOW_CONTENT.toString()%>" property="forwardName"/>
        <select name="tablename">
           <option value="<%= PlasmIDFileType.CLONEINSERT .toString()%>"> <%= PlasmIDFileType.CLONEINSERT.getDisplayTile() %>
<option value="<%= PlasmIDFileType.CLONEPROPERTY .toString()%>"> <%= PlasmIDFileType.CLONEPROPERTY.getDisplayTile() %>
<option value="<%= PlasmIDFileType.INSERTPROPERTY .toString()%>"> <%= PlasmIDFileType.INSERTPROPERTY.getDisplayTile() %>
<option value="<%= PlasmIDFileType.CLONEINSERTONLY .toString()%>"> <%= PlasmIDFileType.CLONEINSERTONLY.getDisplayTile() %>
<option value="<%= PlasmIDFileType.CNAMETYPE .toString()%>"> <%= PlasmIDFileType.CNAMETYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.CLONETYPE .toString()%>"> <%= PlasmIDFileType.CLONETYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.CLONENAMETYPE .toString()%>"> <%= PlasmIDFileType.CLONENAMETYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.SPECIES.toString()%>"> <%= PlasmIDFileType.SPECIES.getDisplayTile() %>
<option value="<%= PlasmIDFileType.REFSEQTYPE.toString()%>"> <%= PlasmIDFileType.REFSEQTYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.SPECIESREFSEQTYPE.toString()%>"> <%= PlasmIDFileType.SPECIESREFSEQTYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.REFSEQNAMETYPE .toString()%>"> <%= PlasmIDFileType.REFSEQNAMETYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.GROWTHCONDITION .toString()%>"> <%= PlasmIDFileType.GROWTHCONDITION.getDisplayTile() %>
<option value="<%= PlasmIDFileType.INSERTPROPERTYTYPE.toString()%>"> <%= PlasmIDFileType.INSERTPROPERTYTYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.SEQNAMETYPE.toString()%>"> <%= PlasmIDFileType.SEQNAMETYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.SAMPLETYPE.toString()%>"> <%= PlasmIDFileType.SAMPLETYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.CONTAINERTYPE.toString()%>"> <%= PlasmIDFileType.CONTAINERTYPE.getDisplayTile() %>
<option value="<%= PlasmIDFileType.SPECIALTREATMENT.toString()%>"> <%= PlasmIDFileType.SPECIALTREATMENT.getDisplayTile() %>
<option value="<%= PlasmIDFileType.COUNTRY.toString()%>"> <%= PlasmIDFileType.COUNTRY.getDisplayTile() %>
<option value="<%= PlasmIDFileType.COLLECTION.toString()%>"> <%= PlasmIDFileType.COLLECTION.getDisplayTile() %>


        </select><html:submit value="View"/>
</html:form></td> </tr>  
    <tr><td colspan="2"> &nbsp;<br><br><br></td> </tr>  
    <html:form action="/FlexToPlasmidFileSubmission.do"  enctype="multipart/form-data" method="post"> 
        <html:hidden value="<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.PLASMID_DICTIONARY_TABLE_POPULATE.toString()%>" property="forwardName"/>
    <tr><td class="prompt">Please select submission file:</td> 
    <td ><html:file property="<%= PlasmIDFileType.PLATE.toString() %>" /> </td></tr>
    <tr><td colspan=2 align="center"><html:submit/></td> </tr>  
</html:form>
</table>


</body>
</html:html>
