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
<head><title><bean:message key="flex.name"/> : Query Sequence History</title></head>
<body>
    <h2><bean:message key="flex.name"/> : Query Sequence History</h2>
    <hr>
    <html:errors/>
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
