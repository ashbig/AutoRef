<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ page import="edu.harvard.med.hip.flex.core.StorageType" %>
<%@ page import="edu.harvard.med.hip.flex.core.StorageForm" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Plate Condensation</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
    
<h2><bean:message key="flex.name"/> : Plate Condensation</h2>
<hr>
<html:errors/>

<html:form action="PlateCondensationInput.do">

<p><table>
    <tr>
    <td class="prompt">Select the project:</td>
    <td><html:select property="projectid">
        <html:options
        collection="projects"
        property="id"
        labelProperty="name"
        />
    </html:select>
    </td>
    </tr>
    <tr>
        <td class="prompt" valign="top">Enter source plates (separated by white space):</td>
        <td><html:textarea rows="10" cols="30" property="srcLabels"/></td>
    </tr>
    <tr>
        <td class="prompt" valign="top">Choose the destination plate type:</td>
        <td><html:select property="destPlateType">
                <html:option value="384 WELL PLATE"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <td class="prompt" valign="top">Choose the destination storage type:</td>
        <td><html:select property="destStorageType">
                <html:option value="<%=StorageType.ARCHIVE%>"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <td class="prompt" valign="top">Choose the destination storage form:</td>
        <td><html:select property="destStorageForm">
                <html:option value="<%=StorageForm.GLYCEROL%>"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <td class="prompt">Check here if the source plates are not muitiple of 4:</td>
        <td><html:checkbox property="isPartial"/></td>
    </tr>
    <tr>
        <td class="prompt">Check here if you only transfer the current working clones:</td>
        <td><html:checkbox property="isWorking"/></td>
    </tr>
    <tr>
        <td class="prompt">Enter researcher ID:</td>
        <td><html:password size="30" property="researcherBarcode"/></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td class="prompt"><html:submit value="Condense"/></td>
    </tr>
</table>

</html:form>
</body>
</html>