<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>MedGene : Disease Search</title>
    </head>
    <body>
    <center>
    <h1>Related Links</h1>
    </center>

    
    <TABLE width="80%" align="center" border="0"><tr><td>
    <html:errors/>

    <p>
    <ul>
    <logic:iterate id="info" name="infos"> 
    <logic:equal name="info" property="type" value="BUTTON">
        <li>
        <a href="<bean:write name="info" property="extraInfo"/>" target="_new"><bean:write name="info" property="value"/></a>
        </li>
    </logic:equal>
    </logic:iterate> 
    </ul>
<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>