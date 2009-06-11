<%-- 
    Document   : TransferCloneDataFromFiles
    Created on : Nov 13, 2008, 2:45:50 PM
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
 


<%@ page import="edu.harvard.med.hip.flex.util.*" %>
<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="java.util.*" %>
 <%@ page import="edu.harvard.med.hip.flex.infoimport.plasmidimport.*" %>
 <%@ page import="edu.harvard.med.hip.flex.infoimport.plasmidimport.filemanipulation.*" %>
 <html><head>
<title><bean:message key="flex.name"/> : <bean:message key="flex.name"/> <%= PlasmidImporterDefinitions.IMPORT_ACTIONS.UPLOAD_SUBMISSION_FILES.getMainPageTitle() %></title>
<LINK REL=StyleSheet HREF="../FlexStyle.css" TYPE="text/css" MEDIA=screen>

</head>
<body>

<h2><bean:message key="flex.name"/> : <%= PlasmidImporterDefinitions.IMPORT_ACTIONS.UPLOAD_SUBMISSION_FILES.getMainPageTitle() %>
</h2><hr>
<html:errors/>
 <h1> Page under development </h1>
 <h3 style="text-align: left; color: rgb(0, 128, 0); white-space: nowrap;">

<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.UPLOAD_SUBMISSION_FILES.getPageTitle() %></h3>
<html:form action="/FlexToPlasmidFileSubmission.do" enctype="multipart/form-data"> 
<input type="hidden" name="forwardName"  value="<%= PlasmidImporterDefinitions.IMPORT_ACTIONS.UPLOAD_SUBMISSION_FILES.toString() %>" >   

<table cellpadding="2" cellspacing="2" width="90%" align="center">
 <tr><td colspan=2>    <h3>Parameter setting:</h3></td></tr>
<tr><td colspan=2> 
<table border=0 width="100%" bgcolor=e5f6ff>
<tr><td class="prompt">Plate type:</td>
<td><select name="plateType">
                        <option selected value="<%= RearrayConstants.PLATE_TYPE.PLATE_96_WELL.toString()%>"/><%= RearrayConstants.PLATE_TYPE.PLATE_96_WELL.getTitle()%>
                        <option value="<%= RearrayConstants.PLATE_TYPE.PLATE_384_WELL.toString()%>"/><%= RearrayConstants.PLATE_TYPE.PLATE_384_WELL.getTitle()%>
    </select> </td>            </tr>
<tr> <td class="prompt">Sample type:</td>
 <td><select name="sampleType">
    <option selected value="<%= RearrayConstants.SAMPLE_TYPE.WORKING_GLYCEROL.toString()%>"  > <%= RearrayConstants.SAMPLE_TYPE.WORKING_GLYCEROL.getTitle()%>
    <option value="<%= RearrayConstants.SAMPLE_TYPE.DEEP_ARCHIVE_GLYCEROL.toString()%>"/> <%= RearrayConstants.SAMPLE_TYPE.DEEP_ARCHIVE_GLYCEROL.getTitle()%>
</select></td></tr></table></td></tr>
<tr>    <td class="prompt" colspan=2>&nbsp;</td></tr>
                     
<tr><td  colspan=2>       <h3>Input files:</h3></td></tr>
  <tr><td  colspan=2>      <table border=0  bgcolor=e5f6ff width="100%">
<tr> <td class="prompt"><%= PlasmIDFileType.PLATE.getDisplayTile() %>:</td>
<td><html:file property="<%= PlasmIDFileType.PLATE.toString() %>" />
[<a target="_blank"   href="/FLEX/FlexToPlasmid/help/<%=PlasmIDFileType.PLATE.getHelpFileLocation() %>">sample file</a>]</td>
</tr>

<tr>    <td class="prompt" colspan=2>&nbsp;</td></tr>
 


<tr class="headerSectionRow">    <td class="prompt" colspan=2  >Reference Sequence information</td></tr>
<tr>   <td class="prompt"><%= PlasmIDFileType.REFSEQ.getDisplayTile() %>:</td>
<td><html:file property="<%= PlasmIDFileType.REFSEQ.toString() %>" />
[<a target="_blank"   href="/FLEX/FlexToPlasmid/help/<%= PlasmIDFileType.REFSEQ.getHelpFileLocation() %>">sample file</a>]</td>
</tr>

 
<tr> <td class="prompt"><%= PlasmIDFileType.REFSEQNAME.getDisplayTile() %>:</td>
<td><html:file property="<%= PlasmIDFileType.REFSEQNAME.toString() %>" />
[<a target="_blank"   href="/FLEX/FlexToPlasmid/help/<%= PlasmIDFileType.REFSEQNAME.getHelpFileLocation() %>">sample file</a>]</td>
</tr>
   

<tr>    <td class="prompt" colspan=2>&nbsp;</td></tr>
<tr class="headerSectionRow">    <td class="prompt" colspan=2  >Clone information</td></tr>

<tr><td class="prompt"><%=PlasmIDFileType.CLONEINSERT.getDisplayTile() %>:</td>
<td><html:file property="<%=PlasmIDFileType.CLONEINSERT.toString() %>" />
[<a target="_blank"   href="/FLEX/FlexToPlasmid/help/<%=PlasmIDFileType.CLONEINSERT.getHelpFileLocation() %>">sample file</a>]</td>
</tr>
<tr><td class="prompt"><%=PlasmIDFileType.CLONEPROPERTY.getDisplayTile() %>:</td>
<td><html:file property="<%=PlasmIDFileType.CLONEPROPERTY.toString() %>" />
[<a target="_blank"   href="/FLEX/FlexToPlasmid/help/<%=PlasmIDFileType.CLONEPROPERTY.getHelpFileLocation() %>">sample file</a>]</td>
</tr>
<tr><td class="prompt"><%=PlasmIDFileType.INSERTPROPERTY.getDisplayTile() %>:</td>
<td><html:file property="<%=PlasmIDFileType.INSERTPROPERTY.toString() %>" />
[<a target="_blank"   href="/FLEX/FlexToPlasmid/help/<%=PlasmIDFileType.INSERTPROPERTY.getHelpFileLocation() %>">sample file</a>]</td>
</tr>
<tr><td class="prompt"><%=PlasmIDFileType.CLONEINSERTONLY.getDisplayTile() %>:</td>
<td><html:file property="<%=PlasmIDFileType.CLONEINSERTONLY.toString() %>" />
[<a target="_blank"   href="/FLEX/FlexToPlasmid/help/<%=PlasmIDFileType.CLONEINSERTONLY.getHelpFileLocation() %>">sample file</a>]</td>
</tr>
<tr><td class="prompt"><%=PlasmIDFileType.CLONESELECTION.getDisplayTile() %>:</td>
<td><html:file property="<%=PlasmIDFileType.CLONESELECTION.toString() %>" />
[<a target="_blank"   href="/FLEX/FlexToPlasmid/help/<%=PlasmIDFileType.CLONESELECTION.getHelpFileLocation() %>">sample file</a>]</td>
</tr>
<tr><td class="prompt"><%=PlasmIDFileType.CLONEGROWTH.getDisplayTile() %>:</td>
<td><html:file property="<%=PlasmIDFileType.CLONEGROWTH.toString() %>" />
[<a target="_blank"   href="/FLEX/FlexToPlasmid/help/<%=PlasmIDFileType.CLONEGROWTH.getHelpFileLocation() %>">sample file</a>]</td>
</tr>
<tr><td class="prompt"><%=PlasmIDFileType.CLONEHOST.getDisplayTile() %>:</td>
<td><html:file property="<%=PlasmIDFileType.CLONEHOST.toString() %>" />
[<a target="_blank"   href="/FLEX/FlexToPlasmid/help/<%=PlasmIDFileType.CLONEHOST.getHelpFileLocation() %>">sample file</a>]</td>
</tr>
<tr><td class="prompt"><%=PlasmIDFileType.CLONENAME.getDisplayTile() %>:</td>
<td><html:file property="<%=PlasmIDFileType.CLONENAME.toString() %>" />
[<a target="_blank"   href="/FLEX/FlexToPlasmid/help/<%=PlasmIDFileType.CLONENAME.getHelpFileLocation() %>">sample file</a>]</td>
</tr>
<tr><td class="prompt"><%=PlasmIDFileType.CLONECOLLECTION.getDisplayTile() %>:</td>
<td><html:file property="<%=PlasmIDFileType.CLONECOLLECTION.toString() %>" />
[<a target="_blank"   href="/FLEX/FlexToPlasmid/help/<%=PlasmIDFileType.CLONECOLLECTION.getHelpFileLocation() %>">sample file</a>]</td>
</tr>

<tr>    <td class="prompt" colspan=2>&nbsp;</td></tr>

<tr class="headerSectionRow">    <td class="prompt" colspan=2  >Author information</td></tr>
<tr><td class="prompt"><%=PlasmIDFileType.AUTHOR.getDisplayTile() %>:</td>
<td><html:file property="<%=PlasmIDFileType.AUTHOR.toString() %>" />
[<a target="_blank"   href="/FLEX/FlexToPlasmid/help/<%=PlasmIDFileType.AUTHOR.getHelpFileLocation() %>">sample file</a>]</td>
</tr>
<tr>
<td class="prompt"><%=PlasmIDFileType.CLONEAUTHOR.getDisplayTile() %>:</td>
<td><html:file property="<%=PlasmIDFileType.CLONEAUTHOR.toString() %>" />
[<a target="_blank"   href="/FLEX/FlexToPlasmid/help/<%=PlasmIDFileType.CLONEAUTHOR.getHelpFileLocation() %>">sample file</a>]</td>
</tr>
<tr>    <td class="prompt" colspan=2>&nbsp;</td></tr>

<tr class="headerSectionRow">    <td class="prompt" colspan=2  >Publication information</td></tr>
<tr>
<td class="prompt"><%=PlasmIDFileType.PUBLICATION.getDisplayTile() %>:</td>
<td><html:file property="<%=PlasmIDFileType.PUBLICATION.toString() %>" />
[<a target="_blank"   href="/FLEX/FlexToPlasmid/help/<%=PlasmIDFileType.PUBLICATION.getHelpFileLocation() %>">sample file</a>]</td>
</tr>
<tr>
<td class="prompt"><%=PlasmIDFileType.CLONEPUBLICATION.getDisplayTile() %>:</td>
<td><html:file property="<%=PlasmIDFileType.CLONEPUBLICATION.toString() %>" />
[<a target="_blank"   href="/FLEX/FlexToPlasmid/help/<%=PlasmIDFileType.CLONEPUBLICATION.getHelpFileLocation() %>">sample file</a>]</td>
</tr>
</TABLE> </TD></TR>
 <tr><td colspan=2>&nbsp;</td></tr>
     
<tr><td colspan="2"><h3>Researcher Information</h3></td></tr>
<tr bgcolor=e5f6ff><td class="prompt" >Researcher ID: </td><td><html:password property="researcherBarcode"/> </td></tr>  
 <tr><td colspan=2>&nbsp;</td></tr>
  
  
<!-- <tr><td colspan="2" align="center"><html:submit/></td>    </tr>-->
</table>
</html:form>
</body>
</html>


    </body>
</html>
