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
<html:form action="/EnterOligoPlates.do" focus="fivepPlate">
<input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
<input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">

<table>
    <tr>
    <td class="prompt">Process name:</td>
    <td><bean:write name="SelectProtocolAction.protocol" property="processname"/></td>
    </tr>

    <tr>
    <td class="prompt">Enter 5P oligo plate barcode:</td>
    <td><html:text property="fivepPlate" size="40"/></td>
    </tr>

    <tr>
    <td class="prompt">Enter 3P Fusion oligo plate barcode:</td>
    <td><html:text property="threepOpenPlate" size="40"/></td>
    </tr>

    <logic:notEqual name="projectid" value="3">
    <tr>
    <td class="prompt">Enter 3P closed oligo plate barcode:</td>
    <td><html:text property="threepClosedPlate" size="40"/></td>
    </tr>
    </logic:notEqual>

    <logic:equal name="SelectProtocolAction.protocol" property="processname" value="generate step1 PCR plates">
    <logic:equal name="workflowid" value="8">
    <tr>
    <td class="prompt">Enter MGC template plate barcode:</td>
    <td><html:text property="templatePlate" size="40"/></td>
    </tr>
    </logic:equal>
    <logic:equal name="workflowid" value="9">
    <tr>
    <td class="prompt">Enter MGC template plate barcode:</td>
    <td><html:text property="templatePlate" size="40"/></td>
    </tr>
    </logic:equal>
    </logic:equal>

    <tr>
    <td class="prompt">Select protocol:</td>
    <td><html:select property="subProtocolName">
        <bean:define id="subprotocols" name="SelectProtocolAction.protocol" property="subprotocol"/>
        <html:options
        collection="subprotocols"
        property="name"
        labelProperty="name"
        />
        </html:select>
    </td>
    </tr>

    <tr>
    <td></td><td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

<jsp:include page="PlatesetQueueItemsDisplay.jsp" flush="true" />

</body>
</html>

