<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ page import="edu.harvard.med.hip.flex.Constants"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Search Results</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<H2><bean:message key="flex.name"/> : Search Results</h2>
<hr>
<html:errors/>
<p>

<h3>The following is your search results:</h3>
<logic:present name="queryInfoList">
<TABLE border=1>
    <tr bgcolor="#9bbad6">
    <th>Name</th><th>Date</th><th>Status</th><th>Total Results</th><th>Found</th><th>Not Found</th>
    </tr>
    <logic:iterate name="queryInfoList" id="queryInfo">
    <tr>
        <td><flex:write name="queryInfo" property="searchRecord.searchName"/></td>
        <td><flex:write name="queryInfo" property="searchRecord.searchDate"/></td>
        <td><flex:write name="queryInfo" property="searchRecord.searchStatus"/></td>
        <td><flex:write name="queryInfo" property="numOfResults"/></td>
        <td><a target="_blank" href="GetSearchResults.do?searchid=<bean:write name="queryInfo" property="searchRecord.searchid"/>&condition=found"><flex:write name="queryInfo" property="numOfFounds"/></a></td>
        <td><a target="_blank" href="GetSearchResults.do?searchid=<bean:write name="queryInfo" property="searchRecord.searchid"/>&condition=nofound"><flex:write name="queryInfo" property="numOfNofounds"/></a></td>
    </tr>
    </logic:iterate>
</table>
</logic:present>
<logic:notPresent name="queryInfoList">
You have no search history.
</logic:notPresent>
<p>
<logic:notPresent name="<%=Constants.USER_KEY%>" scope="session">
<jsp:include page="footer.jsp"/>
</logic:notPresent>

</body>
</html>