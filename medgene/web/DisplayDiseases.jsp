<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>MedGene : Gene Search</title>
    </head>
    <body>
    <center>
    <h1>Disease List</h1>    
    </center>

    <table width="80%" align="center" border="0"><tr><td>
    <html:errors/>
    <p>
    <TABLE border="1" cellpadding="2" cellspacing="0" width=80% align=center>
    <TR bgcolor="gray">
        <TH>Disease Name</TH>
        <TH><A HREF="statistic_menu.jsp">Statistical Score</A></TH>
        <TH><A HREF="NumberOfPapers.jsp">Number of Papers</A></TH>
    </TR>

    <logic:iterate id="association" name="associations"> 
        <tr>
            <TD>
                <bean:write name="association" property="disease.term"/>&nbsp
            </TD>
            <TD>
                <bean:write name="association" property="stat.score"/>&nbsp
            </TD>
            <TD>
                <bean:write name="association" property="data.doublehit"/>&nbsp
            </TD>
        </tr>
    </logic:iterate> 
</TABLE>
<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>