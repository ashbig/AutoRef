<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*" %>
<%@ page import="edu.harvard.med.hip.flex.process.*" %>
<%@ page import="edu.harvard.med.hip.flex.process.Process" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>
<html>
<head>
    <title><bean:message key="flex.name"/> : Query Sequence History</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
    <h2><bean:message key="flex.name"/> : Query Sequence History</h2>
    <hr>
    <html:errors/>
    <p>
    <html:form action="/ViewSequenceProcessHistory">
        <table>
            <tr>
                <td class="prompt">Search By: </td>
                <td>
                    <html:select property="searchParam">
                        <html:options name="<%=Constants.NAME_TYPE_LIST_KEY%>"/>
                    </html:select>
                </td>
                <td><html:text property="paramValue"/></td>
            </tr>
        </table>
        <br>
        <html:submit/>
    </html:form>
</body>
</html>
