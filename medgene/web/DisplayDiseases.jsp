<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Genes associated with a particular disease</title>
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
<br><br><br><br>

<% int i = 0; String bgcolor = "";%>

  <center>
    <h2> <font color="#008000">Top  
    <bean:write name="number"/> Diseases Associated With <bean:write name="gene" property="index"/>
    </font>
    </h2>
    <h2> <font color="#008000">
        By Statistical Method Of "<bean:write name="stat" property="type"/>"
    </font>
    </h2>
  </center>

<table width="90%" align="center" border="0">
  <tbody>
    <tr>
      <td><br>
        <hr>
        <b>Note:</b> If you want to find whether your interested diseases are on
        this page, just click Edit on your browser's menu bar and use Find to
        search the current page.
        <hr>
        <br>
        <table cellSpacing="0" cellPadding="2" width="100%" align="center" border="1">
          <tr bgColor="#cccccc">
              <th style="background-color: #8181AB"><font color="#FFFFFF">NO.</font></th>
              <th style="background-color: #8181AB"><font color="#FFFFFF">Disease Name</font></th>
              <th style="background-color: #8181AB"><a href="statistic_menu.jsp" target="_blank"><font color="#FFFFFF">
                Statistical Score</font></a></th>
              <th style="background-color: #8181AB"><a href="NumberOfPapers.jsp" target="_blank"><font color="#FFFFFF">
                Papers</font></a></th>
          </tr>

          <logic:iterate id="association" name="associations"> 
          <tr>
              <% i++; %>
              <% if ((i%2) == 1) { bgcolor = "#ffffff" ;} %> 
              <% if ((i%2) == 0) { bgcolor = "#f5f5fc" ;} %> 
            <td align="center" bgcolor="<% out.print(bgcolor); %>"><% out.print(i); %></td>
            <td bgcolor= "<% out.print(bgcolor); %>">
                <bean:write name="association" property="disease.term"/>&nbsp
            </TD>
            <td bgcolor= "<% out.print(bgcolor); %>" align="center">
                <bean:write name="association" property="stat.score"/>&nbsp
            </TD>
            <td bgcolor= "<% out.print(bgcolor); %>" align="center">
                <a href="DisplayPaperLinks.do?disease_id=<bean:write name="association" property="disease.id"/>&gene_index=<bean:write name="gene" property="index"/>
&disease_mesh_term=<bean:write name="association" property="disease.term"/>&gene_symbol=<bean:write name="gene" property="index"/>" target="_blank">
                <bean:write name="association" property="data.doublehit"/></a>&nbsp
            </TD>
          </tr>
          </logic:iterate> 
    </TABLE>
<p>&nbsp;</p>

<p>
<table width='80%' align="center">
  <jsp:include page="links.jsp" flush="true"/>
</table>
</body>
</html>