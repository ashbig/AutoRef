<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Create Process Plate </title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Create Process Plate</h2>
<hr>
<html:errors/>
<p>
<html:form action="/GetNewOligoLocation.do" focus="researcherBarcode">
<input type="hidden" name="workflowid" value=<bean:write name="workflowid"/>>
<input type="hidden" name="projectid" value=<bean:write name="projectid"/>> 
<input type="hidden" name="projectname" value=<bean:write name="projectname"/>> 
<input type="hidden" name="workflowid" value="<%= request.getAttribute("workflowname")%>" > 

<logic:present name="templateid">
    <input type="hidden" name="templateid" value=<bean:write name="templateid"/>> 
</logic:present>

<table>
     <td class="prompt">Project name:</td>
    <td><bean:write name="projectname"/></td>
    </tr>
    <tr>
    <td class="prompt">Workflow name:</td>
    <td><%= request.getAttribute("workflowname")%></td>
    </tr>
    <tr>
    <td class="label">Process name:</td>
    <td><bean:write name="SelectProtocolAction.protocol" property="processname"/></td>
    </tr>

    <tr>
    <td class="label">Protocol:</td>
    <td><bean:write name="EnterOligoPlateAction.subprotocol" property="name"/></td>
    </tr>

    <tr>
    <td class="label">5P oligo plate barcode:</td>
    <td><bean:write name="EnterOligoPlateAction.fivep" property="label"/></td>
    <td class="prompt">Location:</td>
    <td><html:select property="fivepSourceLocation">
        <html:options
        collection="EnterOligoPlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>
    
 <logic:present name="EnterOligoPlateAction.threepOpen">
    <tr>
    <td class="label">3P fusion oligo plate barcode:</td>
    <td><bean:write name="EnterOligoPlateAction.threepOpen" property="label"/></td>
    <td class="prompt">Location:</td>
    <td><html:select property="threepOpenSourceLocation">
        <html:options
        collection="EnterOligoPlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>
    </logic:present>

    <logic:present name="EnterOligoPlateAction.threepClosed">
    <tr>
    <td class="label">3P closed oligo plate barcode:</td>
    <td><bean:write name="EnterOligoPlateAction.threepClosed" property="label"/></td>
    <td class="prompt">Location:</td>
    <td><html:select property="threepClosedSourceLocation">
        <html:options
        collection="EnterOligoPlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>
    </logic:present>

    <tr>
    <td class="label">5P oligo daughter plate barcode:</td>
    <td><bean:write name="EnterOligoPlateAction.fivepOligoD" property="label"/></td>
    <td class="prompt">Location:</td>
    <td><html:select property="fivepDaughterLocation">
        <html:options
        collection="EnterOligoPlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>

    <logic:present name="EnterOligoPlateAction.threepOpenD">
    <tr>
    <td class="label">3P fusion oligo daughter plate barcode:</td>
    <td><bean:write name="EnterOligoPlateAction.threepOpenD" property="label"/></td>
    <td class="prompt">Location:</td>
    <td><html:select property="threepOpenDaughterLocation">
        <html:options
        collection="EnterOligoPlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>
     </logic:present>

    <logic:present name="EnterOligoPlateAction.threepClosedD">
    <tr>
    <td class="label">3P closed oligo daughter plate barcode:</td>
    <td><bean:write name="EnterOligoPlateAction.threepClosedD" property="label"/></td>
    <td class="prompt">Location:</td>
    <td><html:select property="threepClosedDaughterLocation">
        <html:options
        collection="EnterOligoPlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>
    </logic:present>

    <tr>
    <td></td><td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

</body>
</html>