<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page contentType="text/html"%>
<html>
<head>
    <title><bean:message key="flex.name"/> :  Receiving sequencing Form</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
<h2><bean:message key="flex.name"/> : Sequencing Order Receiving Form</h2>
<hr>
<html:errors/>
<p>
<html:form action="/ReceiveOligoPlates.do" focus="oligoPlateIds">

<logic:present name="projectid">
    <input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
    <input type="hidden" name="projectname" value="<bean:write name="projectname"/>">
</logic:present>
<logic:present name="workflowid">
    <input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">
</logic:present>
<logic:present name="protocolid">
    
    <input type="hidden" name="proctocolid" value="<bean:write name="protocolid"/>">
</logic:present>




<table>
    <tr>
    <td class="prompt">Project name:</td>
    <td><bean:write name="projectname" /></td>
    </tr>
    <tr>
    <td class="prompt">Workflow name:</td>
    <td><bean:write name="workflowname" /></td>
    </tr>   
</table>     

<table>
    <tr>
        <td class="prompt">Please enter plate IDs for all of the sequencing plates received:<p></td>
    </tr>
    <tr>
        <td><html:textarea property="oligoPlateIds" rows="10" cols="35"/><p></td>
    </tr>

    <tr>
        <td class="prompt">Receive Date:&nbsp;&nbsp;&nbsp;
            <html:text property="receiveDate" size="30"/><p></td>
    </tr>

    <tr>
        <td class="prompt"><bean:message key="flex.researcher.barcode.prompt"/>&nbsp;&nbsp;&nbsp;
            <html:password property="researcherBarcode" size="30"/><p></td>
    </tr>
</table>
<p>
<input type=submit value="Submit"> <input type=reset value="Clear">
</html:form>

    <jsp:include page="QueueItemsDisplay.jsp" flush="true"/>
</body>
</html>
