<%@page contentType="text/html"%>
<%@ page language="java" %>
<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>



<html>
<head><title>Container Details</title></head>
<body>

<%--Loop through all the containers and display all their details--%>
<logic:iterate id="container" name="<%=Constants.CONTAINER_LIST_KEY%>">
<!--display general info about the container.-->
<table>
    <tr>
        <td>Id:</td>
        <td><bean:write name="container" property="id"/></td>
    </tr>
    <tr>
        <td>Type:</td>
        <td><bean:write name="container" property="type"/></td>
    </tr>
    <tr>
        <td>Label:</td>
        <td><bean:write name="container" property="label"/></td>
    </tr>

</table>
<!-- display the container sample info.-->
<table>
    <tr>
        <th>ID</th> 
        <th>Type</th>
        <th>Position</th>
        <th>Status</th>
        <th>Construct</th>
        <th>Oligo</th>
    </tr>
    <logic:iterate id="sample" name="container" property="samples">
    <tr>
        <td><bean:write name="sample" property="id"/></td>
        <td><bean:write name="sample" property="type"/></td>
        <td><bean:write name="sample" property="position"/></td>
        <td><bean:write name="sample" property="status"/></td>
        <td><bean:write name="sample" property="constructid"/></td>
        <td><bean:write name="sample" property="oligoid"/></td>
    </tr>
    </logic:iterate>
</table>
</logic:iterate>
</body>
</html>
