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
    <h1>Top <bean:write name="number"/> Diseases Associated With 
        <bean:write name="gene" property="index"/></h1>
    <h1>By Statistical Method Of "<bean:write name="stat" property="type"/>"
    </h1>    
    </center>

    <table width="80%" align="center" border="0"><tr><td>
    <html:errors/>
    <p>
    <TABLE width="100%" align="center" border="1" cellpadding="2" cellspacing="0">

    <TR bgcolor="gray">
        <TH>Disease Name</TH>
        <TH><A HREF="statistic_menu.jsp" target="_blank">Statistical Score</A></TH>
        <TH><A HREF="NumberOfPapers.jsp" target="_blank">Number of Papers</A></TH>
    </TR>

    <logic:iterate id="association" name="associations"> 
        <tr>
            <TD>
                <bean:write name="association" property="disease.term"/>&nbsp
            </TD>
            <TD align="center">
                <bean:write name="association" property="stat.score"/>&nbsp
            </TD>
            <TD align="center">
                <bean:write name="association" property="data.doublehit"/>&nbsp
            </TD>
        </tr>
    </logic:iterate> 
</TABLE>
<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>