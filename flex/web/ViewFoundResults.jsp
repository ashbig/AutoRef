<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Query Results</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<H2><bean:message key="flex.name"/> : Query Results</h2>
<hr>
<html:errors/>
<p>

<TABLE border=1>
    <tr bgcolor="#9bbad6">
    <th>Search Term</th><th>Match Genbank</th><th>Match FLEXGene</th><th>Status</th><th>Found By</th><th>Search Method</th>
    </tr>
    <logic:iterate name="results" id="result">
    <tr>
        <td rowspan="<bean:write name="result" property="numOfFoundFlex"/>"><bean:write name="result" property="searchTerm"/></td>
        <logic:iterate name="result" property="found" id="mgr">
            <td rowspan="<bean:write name="mgr" property="numOfMatchFlexSequence"/>"><bean:write name="mgr" property="ganbankAccession"/></td>
            <logic:iterate name="mgr" property="matchFlexSequence" id="mfs">
                <td><bean:write name="mfs" property="flexsequenceid"/></td>
                <td><bean:write name="mfs" property="flexSequence.flexstatus"/></td>
                <td><bean:write name="mfs" property="foundBy"/></td>
                <td><bean:write name="mgr" property="searchMethod"/></td>
                </tr>
            </logic:iterate>
        </logic:iterate>    
    </TR>
    </logic:iterate>
</table>

</body>
</html>