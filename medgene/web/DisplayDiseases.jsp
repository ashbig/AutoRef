<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>METAGENE : Gene Search</title>
    </head>
    <body>
    <center>
    <h1>Disease List</h1>    
    </center>

    <table width="80%" align="center" border="0"> 
    <html:errors/>
    <p>
    <TABLE border="1" cellpadding="2" cellspacing="0" width=80% align=center>
    <TR bgcolor="gray">
        <TH>Disease Name</TH>
    </TR>

    <logic:iterate id="disease" name="diseases"> 
        <tr>
            <TD>
                <bean:write name="disease" property="term"/>&nbsp
            </TD>
        </tr>
    </logic:iterate> 
</TABLE>
<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>