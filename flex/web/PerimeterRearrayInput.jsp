<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Cell Culture Perimeter Rearray</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Cell Culture Perimeter Rearray</h2>
<hr>
<html:errors/>

<p>
<html:form action="/PerimeterRearrayInput.do">
<html:hidden property="projectid"/>
<html:hidden property="workflowid"/>
<html:hidden property="projectname"/>
<html:hidden property="workflowname"/>

<table>
    <tr>
    <td class="prompt">Project name:</td>
    <td><bean:write name="perimeterRearrayInputForm" property="projectname" /></td>
    </tr>
    <tr>
    <td class="prompt">Workflow name:</td>
    <td><bean:write name="perimeterRearrayInputForm" property="workflowname" /></td>
    </tr>

    <tr>
    <td class="prompt">Select Source Plate Type:</td>
    <td><html:select property="sourcePlateType">
        <option>Costar flt bttm/MP16-24</option>
        <option>Costar rd bttm/MP16-24</option>
        <option>Deepwell on MP16</option>
        <option>PCR on MP16 landscape</option>
        <option>Reservoir on MP16</option>
        <option>RK riplate dw/MP16-24</option>
        </html:select>
    </td>
    </tr>
    <tr>
    <td class="prompt">Select Destination Plate Type:</td>
    <td><html:select property="destPlateType">
        <option>Costar flt bttm/MP16-24</option>
        <option>Costar rd bttm/MP16-24</option>
        <option>Deepwell on MP16</option>
        <option>PCR on MP16 landscape</option>
        <option>Reservoir on MP16</option>
        <option>RK riplate dw/MP16-24</option>
        </html:select>
    </td>
    </tr>

    <tr>
    <td class="prompt">Volumn (in microliter):</td>
    <td><html:text property="volumn"/></td>
    </tr>

    <tr>
    <td class="prompt">Please enter all the plate labels (one label per line):</td>
    <td><html:textarea rows="5" cols="30" property="labels"/></td>
    </tr>

    <tr>
    <td colspan="2" class="prompt">Please enter all the email addresses that you want the files to be sent to (one email per line):</td>
    </tr>
    <tr>
    <td></td>
    <td><html:textarea rows="5" cols="30" property="emails"/></td>
    </tr>

    <tr>
    <td></td>
    <td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

</body>
</html>