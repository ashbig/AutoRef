<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Diseases associated with a particular gene</title>
</head>

<body>
<jsp:include page="NavigatorBar.jsp" flush="true"/>
<div align="center">
<p align="left"><br>
</p>
  <center>
<table border="0" cellpadding="0" cellspacing="0" width="90%">
  <tr>
    <td width="93%"><b><font color="#000099" face="Verdana" size="5">Diseases
      Associated with a Particular Gene</font></b></td>
    <td width="22%" align="right"><b><font color="#003366" size="6"><img border="0" src="jpg/medgene02.gif" width="85" height="35"></font></b></td>
  </tr>
  <tr>
    <td width="126%"></td>
    <td width="9%"></td>
  </tr>
  <tr>
    <td width="135%" colspan="2"><br>
      <img border="0" src="jpg/menubar_upper.gif" width="900" height="10"></td>
  </tr>
</table>
  </center>
</div>
<br><br><br>

<logic:notEqual name="hs_geneIndexes" value="no">      
<center>
  <h1><font color="#008000">Top <bean:write name="number"/> Genes </font></h1>
</center>

<table width="90%" align="center" border="0">
  <tbody>
    <tr>
      <td>
    <br>
    <hr>
    <b>Note:</b> If you want to find whether your interested diseases are on this page, 
    just click Edit on your browser's menu bar and use Find to search the current page. 
    <hr><br><br>
    <% int homolog = 0; int num_of_homologs = ((java.util.Vector)(request.getAttribute("all_associations"))).size();   %>
    <logic:iterate id="_assoc" name="all_associations">
    <% int rank = 0; homolog ++;%>
    <a name="<%out.print("#"+homolog);%>"></a><br>

    <table width="100%" align="center" border="0">
    <tr><td bgcolor="#FFFFFF" width='20%'><b><font color="#9900CC">Input gene: </td> 
        <td bgcolor="#FFFFFF" width='20%'><b><font color="#9900CC"> <bean:write name="input_type"/> <b><bean:write name="searchTerm"/></b></td>
        <% if (homolog > 1) { %>
        <td width = '60%' align="right"><a href="<%out.print("#"+(homolog-1));%>"><img border="0" src="jpg/up.gif"></a></td> <% } %>
    </tr>
    <tr><td bgcolor="#FFFFFF" width='20%'><b><font color="#9900CC">Human homolog: </td> 
        <td bgcolor="#FFFFFF" width='80%'><b><font color="#9900CC"> <bean:write name="_assoc" property="gene_index"/> </td>
        <% if (homolog < num_of_homologs) { %>
        <td width = '60%' align="right"><a href="<%out.print("#"+(homolog+1));%>"><img border="0" src="jpg/down.gif"></a></td> <% } %>
    </tr>
    <tr><td bgcolor="#FFFFFF" width='20%'><b><font color="#9900CC">Statistical method: </td> 
        <td bgcolor="#FFFFFF" width='80%'><b><font color="#9900CC"> <bean:write name="stat" property="type"/> </td>
        <td width = '60%' align="right"></td>
    </tr>
    </table>
    <br><br>

    <p>
    
    <table cellSpacing="0" cellPadding="2" width="100%" align="center" border="1">
    <TR bgcolor="#cccccc">
        <th style="background-color: #8181AB"><font color="#FFFFFF">NO.</font></th>
        <th style="background-color: #8181AB"><font color="#FFFFFF">Disease Name</font></TH>
        <TH style="background-color: #8181AB"><A HREF="statistic_menu.jsp" target="_blank"><font color="#FFFFFF">Statistical Score</font></A></TH>
        <TH style="background-color: #8181AB"><A HREF="NumberOfPapers.jsp" target="_blank"><font color="#FFFFFF">Number of Papers</font></A></TH>
    </TR>

    <logic:iterate id="association" name="_assoc" property="associations"> 
        <tr>
            <TD align="center"><% out.println(++rank); %></TD>
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
    </table>
    <br><br><br>
    </logic:iterate> 
</tbody>
</table>
</logic:notEqual>

<logic:equal name="hs_geneIndexes" value="no">  
    <table width="90%" align="center" border="0">
    <tr>      
      <td bgcolor="#FFFFFF" width="20%"><b><font color="#9900CC"> <bean:write name="input_type"/>:</font></b></td>
      <td bgcolor="#FFFFFF" width="80%"><b><font color="#9900CC"> <bean:write name="searchTerm"/></font></b></td>
    </tr>
    <tr>
      <td bgcolor="#FFFFFF" width="100%" colspan="2">There is no human homolog
        found by using HomoloGene at NCBI. Therefore, no associated diseases are
        displayed.</td>
    </tr>
    </table>
</logic:equal>
       
<p>
<table width='90%' align="center">
  <jsp:include page="links.jsp" flush="true"/>
</table>
</body>
</html>