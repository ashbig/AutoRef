<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Query History</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<H2><bean:message key="flex.name"/> : Query History</h2>
<hr>
<html:errors/>
<p>

<h3>The following is your search history:</h3>
<logic:present name="queryInfoList">
<TABLE border=1>
    <tr bgcolor="#9bbad6">
    <th>Name</th><th>Date</th><th>Status</th><th>Total Results</th><th>Found</th><th>Not Found</th>
    </tr>
    <logic:iterate name="queryInfoList" id="queryInfo">
    <tr>
        <td><bean:write name="queryInfo" property="searchRecord.searchName"/></td>
        <td><bean:write name="queryInfo" property="searchRecord.searchDate"/></td>
        <td><bean:write name="queryInfo" property="searchRecord.searchStatus"/></td>
        <td><bean:write name="queryInfo" property="numOfResults"/></td>
        <td><a href="GetSearchResults.do?searchid=<bean:write name="queryInfo" property="searchRecord.searchid"/>&condition=found"><bean:write name="queryInfo" property="numOfFounds"/></a></td>
        <td><a href="GetSearchResults.do?searchid=<bean:write name="queryInfo" property="searchRecord.searchid"/>&condition=nofound"><bean:write name="queryInfo" property="numOfNofounds"/></a></td>
    </tr>
    </logic:iterate>
</table>
</logic:present>
<logic:notPresent name="queryInfoList">
You have no search history.
</logic:notPresent>

</body>
</html>