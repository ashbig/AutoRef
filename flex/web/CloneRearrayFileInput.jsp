<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : rearray</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
    
<html:form action="CloneRearrayFileInput.do">
    <h2><bean:message key="flex.name"/> : Rearray Clones</h2>
    <hr>
    <html:errors/>

<html:hidden property="rearrayType"/>

<p><b><u>Choose the file format:</u></b></p>
<dl>
    <dd><html:radio property="fileFormat" value="format1"/>clone ID
    <dd><html:radio property="fileFormat" value="format2"/>clone ID, destination plate, destination well
    <dd>
    <p>
    <table>
        <tr>
            <td class="prompt">Destination Well:</td>
            <td><html:radio property="destWellFormat" value="number"/>number</td>
            <td><html:radio property="destWellFormat" value="alpha"/>alphanumeric</td>
        </tr>
    </table>    
</dl>

<p>
<p><b><u>Choose project and workflow for destination plates:</u></b></p>

<dl>
    <dd>
    <table>
        <tr>
            <td class="prompt">Project:</td>
            <td><html:select property="project">
                <html:options collection="projects" property="id" labelProperty="name"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="prompt">Workflow:</td>
            <td><html:select property="workflow">
                <html:options collection="workflows" property="id" labelProperty="name"/>
                </html:select>
            </td>
        </tr>
    </table>    
</dl>

    <html:submit/>

</html:form>
</body>
</html>