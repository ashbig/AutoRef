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
    <title><bean:message key="flex.name"/> : Container Process History</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
    <h2><bean:message key="flex.name"/> : Container Process History</h2>
    <hr>
    <html:errors/>
    <p>
    <H3>Please Scan the Container</h3>
    <form action="ViewContainerProcessHistory.do" >
    <table>
        <tr>
            <td class="prompt">Label:</td>
            <td><input type="text" name="<%=Constants.CONTAINER_BARCODE_KEY%>"/></td>
        </tr>
    </table>
    <input type="SUBMIT"/>
    </form>
</body>
</html>
