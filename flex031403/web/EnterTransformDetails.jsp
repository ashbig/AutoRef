<%@page contentType="text/html"%>
<%@ page language="java" %>
<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<bean:define id="plate" name="<%=Constants.CONTAINER_KEY%>"/>

<%
java.util.Date date = new java.util.Date();
%>
<html>
<head><title>Transformation Plate Details</title></head>
<body>
<CENTER>Enter Transformation Plate Details</CENTER>
<!-- Table to display info about the plate -->
<table>
    <tr>
        <td>Plate ID:</td> <td><bean:write name="plate" property="id"/></td>
        <td>Plate Type:</td> <td><bean:write name="plate" property="type"/></td>
    </tr>
    <tr>
        <td>Process Date:</td> <td><%=date.toString()%></td><td></td>
    </tr>
</table>

<!-- table to enter info about the plate-->
<html:form action="EnterTransformDetails.do">
<table border="1">
    <tr>
        <th>Sample</th><th>Cell</th><th>Status</th><th>Result</th>
    </tr>
    <%int i =0;%>
    <logic:iterate name="<%=Constants.SAMPLES_KEY%>" id="curSample">
    <tr>
        <td><bean:write name="curSample" property="id"/></td>
        <td><bean:write name="curSample" property="position"/></td>
        <td>
            <html:select property='<%="status["+i+"]" %>'>
                <html:option value="<%=Sample.GOOD%>">Good</html:option>
                <html:option value="<%=Sample.BAD%>">Bad</html:option>
            </html:select>
        </td>
        <td>
            <html:select property='<%="result["+ i++ +"]" %>'>
                <html:option value="Many">Many</html:option>
                <html:option value="Few">Few</html:option>
                <html:option value="None">None</html:option>
            </html:select>
        </td>
        
    </tr>
    </logic:iterate>
</table>
<html:submit/>
</html:form>
</body>
</html>
