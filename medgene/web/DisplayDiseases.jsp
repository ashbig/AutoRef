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
    <% int i = 0; %>
    <center>
    <h1>Top <bean:write name="number"/> Diseases Associated With 
        <bean:write name="gene" property="index"/></h1>
    <h1>By Statistical Method Of "<bean:write name="stat" property="type"/>"
    </h1>    
    </center>

    <table width="80%" align="center" border="0"><tr><td>
    <html:errors/>

    <br>
    <hr>
    <b>Note:</b> If you want to find whether your interested diseases are on this page, 
    just click Edit on your browser's menu bar and use Find to search the current page. 
    <hr><br>

    <p>
    <TABLE width="100%" align="center" border="1" cellpadding="2" cellspacing="0">

    <TR bgcolor="gray">
        <TH>Rank</TH>
        <TH>Disease Name</TH>
        <TH><A HREF="statistic_menu.jsp" target="_blank">Statistical Score</A></TH>
        <TH><A HREF="NumberOfPapers.jsp" target="_blank">Number of Papers</A></TH>
    </TR>

    <logic:iterate id="association" name="associations"> 
        <tr>
            <TD align="center"><% out.println(++i); %></TD>
            <TD>
                <bean:write name="association" property="disease.term"/>&nbsp
            </TD>
            <TD align="center">
                <bean:write name="association" property="stat.score"/>&nbsp
            </TD>
            <TD align="center">
                <a href="DisplayPaperLinks.do?disease_id=<bean:write name="association" property="disease.id"/>&gene_index=<bean:write name="gene" property="index"/>
&disease_mesh_term=<bean:write name="association" property="disease.term"/>&gene_symbol=<bean:write name="gene" property="index"/>" target="_blank">
                <bean:write name="association" property="data.doublehit"/></a>&nbsp
            </TD>
        </tr>
    </logic:iterate> 
</TABLE>
<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>