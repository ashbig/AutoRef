<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*" %>
<%@ page import="edu.harvard.med.hip.flex.process.*" %>
<%@ page import="edu.harvard.med.hip.flex.process.Process" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>



<html>
<head><title><bean:message key="flex.name"/> : Container Details</title></head>
<body>
<h2><bean:message key="flex.name"/> : Container Details</h2>
<hr>
<logic:present name="<%=Constants.PROCESS_KEY%>">
    <Center><h3>Results desplayed are for protocol 
    <bean:write name="<%=Constants.PROCESS_KEY%>" property="protocol.processname"/>
    </h3></center>
</logic:present>

<%--Loop through all the containers and display all their details--%>
<logic:iterate id="container" name="<%=Constants.CONTAINER_LIST_KEY%>">
<!--display general info about the container.-->
<table>
    <tr>
        <td>Id:</td>
        <td><bean:write name="container" property="id"/></td>
    </tr>
    <tr>
        <td>Plate Set Id:</td>
        <td><bean:write name="container" property="platesetid"/></td>
    </tr>
    <tr>
        <td>Type:</td>
        <td><bean:write name="container" property="type"/></td>
    </tr>
    <tr>
        <td>Location:</td>
        <td><bean:write name="container" property="location.type"/></td>
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
        <logic:present name="<%=Constants.PROCESS_KEY%>">
            <th>Result</th>
        </logic:present>
    </tr>
    <logic:iterate id="sample" name="container" property="samples">
    <tr>
        <td><bean:write name="sample" property="id"/></td>
        <td><bean:write name="sample" property="type"/></td>
        <td><bean:write name="sample" property="position"/></td>
        <td><bean:write name="sample" property="status"/></td>
        <logic:equal name="sample" property="constructid" value="-1">
            <td>&nbsp;</td>
        </logic:equal>
        <logic:notEqual name="sample" property="constructid" value="-1">
            <td><bean:write name="sample" property="constructid"/></td>
        </logic:notEqual>
        <logic:equal name="sample" property="oligoid" value="-1">
            <td>&nbsp;</td>
        </logic:equal>
        <logic:notEqual name="sample" property="oligoid" value="-1">
            <td><bean:write name="sample" property="oligoid"/></td>
        </logic:notEqual>
        <logic:present name="<%=Constants.PROCESS_KEY%>">
            <bean:define id="process" name="<%=Constants.PROCESS_KEY%>"/>
            <td><%
                   Result result = 
                        Result.findResult((Sample)sample,(Process)process);
                  if(result == null) {
                    out.println("&nbsp;");
                  } else {
                        out.println(result);
                   }
                %><td>
        </logic:present>
    </tr>
    </logic:iterate>
</table>
<br>
<CENTER><h3>Files</h3></CENTER>
<br>
<%-- Display all files associated with this container --%>
<table>
<tr><th>Name</th><th>Type</th></tr>
<logic:iterate id="file" name="container" property="fileReferences">
    
    <tr>
        <td>
            <flex:linkFileRef name="file">
                <bean:write name="file" property="baseName"/>
            </flex:linkFileRef>
        </td>
        <td><bean:write name="file" property="fileType"/></td>
    </tr>
</logic:iterate>
</table>
</logic:iterate>
</body>
</html>
