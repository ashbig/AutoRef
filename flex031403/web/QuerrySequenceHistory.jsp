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
<head><title>Query Sequence History</title></head>
<body>
    <center><html:errors/></center>
    <center><h3>Sequence Process History Search</h3></center>
    <html:form action="/ViewSequenceProcessHistory">
        Search By &nbsp;
        <html:select property="searchParam">
            <html:options name="<%=Constants.NAME_TYPE_LIST_KEY%>"/>
        </html:select>
        &nbsp;
        <html:text property="paramValue"/>
        <br>
        <br>
        <html:submit/>
    </html:form>
</body>
</html>
