<%@page contentType="text/html"%>
<%@ page language="java" %>


<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*"%>
<%@ page import="edu.harvard.med.hip.flex.process.*"%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
<head>
    <title><bean:message key="flex.name"/> : Enter Process Results</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
<h2><bean:message key="flex.name"/> : Enter Process Results</h2>
<hr>
<html:errors/>
<p>

<logic:present name="<%=Constants.CONTAINER_KEY%>">
        <bean:write name="<%=Constants.CONTAINER_KEY%>" property="label"/> was processed sucessfully.
        <hr>
</logic:present>
Select the type of result you wish to enter.
<p>
<table>
<form action="GetQueueItems.do">
<table>
    <tr>
        <td>
            <select name="<%=Constants.PROTOCOL_NAME_KEY%>">
                <option value="<%=Protocol.RUN_PCR_GEL%>">PCR Gel</option>
                <option value="<%=Protocol.GENERATE_AGAR_PLATES%>">Agar Plate</option>
            </select>
        </td>
    </tr>
</table>
    <br>
    <input type="submit">
    <input type="hidden" name="<%=Constants.FORWARD_KEY%>" value="enterProcessResults"/>
</form>
</body>
</html>
