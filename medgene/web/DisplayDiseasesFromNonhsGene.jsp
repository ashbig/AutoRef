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
    <h1>Top <bean:write name="number"/> Diseases </h1>            
    </center>

    <table width="80%" align="center" border="0"><tr><td>
    <html:errors/>

    <br><br>
    <logic:notEqual name="hs_geneIndexes" value="no">   
    <br>    
    <hr>
    <b>Note:</b> If you want to find whether your interested diseases are on this page, 
    just click Edit on your browser's menu bar and use Find to search the current page. 
    <hr><br><br>

    <logic:iterate id="_assoc" name="all_associations">
    <% int i = 0; %>
    <table width="40%" align="left" border="0">
    <tr><td>Input gene: </td> <td> <bean:write name="input_type"/> <b><bean:write name="searchTerm"/></b></td></tr>
    <tr><td>Human homolog: </td> <td> <bean:write name="_assoc" property="gene_index"/> </td></tr>
    <tr><td>Statistical method: </td> <td> <bean:write name="stat" property="type"/> </td></tr>
    </table>
    <br><br><br><br><br>

    <TABLE width="100%" align="center" border="1" cellpadding="2" cellspacing="0">
    <TR bgcolor="gray">
        <TH>Rank</TH>
        <TH>Disease Name</TH>
        <TH><A HREF="statistic_menu.jsp" target="_blank">Statistical Score</A></TH>
        <TH><A HREF="NumberOfPapers.jsp" target="_blank">Number of Papers</A></TH>
    </TR>

    <logic:iterate id="association" name="_assoc" property="associations"> 
        <tr>
            <TD align="center"><% out.println(++i); %></TD>
            <TD>
                <bean:write name="association" property="disease.term"/>&nbsp
            </TD>
            <TD align="center">
                <bean:write name="association" property="stat.score"/>&nbsp
            </TD>
            <TD align="center">
                <a href="DisplayPaperLinks.do?disease_id=<bean:write name="association" property="disease.id"/>&gene_index=<bean:write name="_assoc" property="gene_index"/>
&disease_mesh_term=<bean:write name="association" property="disease.term"/>&gene_symbol=<bean:write name="_assoc" property="gene_index"/>" target="_blank">
                <bean:write name="association" property="data.doublehit"/></a>&nbsp
            </TD>
        </tr>
    </logic:iterate> 
    </TABLE>
    <br><br><br>

    </logic:iterate> 
    </logic:notEqual>

    <logic:equal name="hs_geneIndexes" value="no">  
    <table width="40%" align="left" border="0">
    <tr><td>Input gene: </td> <td> <bean:write name="input_type"/> <b><bean:write name="searchTerm"/></b></td></tr>
    </table><br><br><p> There is no human homolog found by using HomoloGene at NCBI. Therefore, no associated diseases are displayed.
    </logic:equal>
       
<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>
