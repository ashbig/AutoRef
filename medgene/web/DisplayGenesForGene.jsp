<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Genes associated with a particular gene</title>
</head>

<body>
<jsp:include page="NavigatorBar.jsp" flush="true"/>
<div align="center">
<p align="left"><br>
</p>
  <center>
<table border="0" cellpadding="0" cellspacing="0" width="90%">
  <tr>
    <td width="93%"><b><font color="#000099" face="Verdana" size="5">Genes
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
        <bean:write name="number"/>
        Genes Associated With <bean:write name="source_gene_index"/>
    </font>
    </h2>
    <h2> <font color="#008000">
        By Statistical Method Of "<bean:write name="stat_type"/>"
    </font>
    </h2>
  </center>

<table width="90%" align="center" border="0">
  <tbody>
    <tr>
      <td><br>
        <hr>
        <b>Note:</b> If you want to find whether your interested genes are on
        this page, just click Edit on your browser's menu bar and use Find to
        search the current page.
        <hr>
        <br>
        <table cellSpacing="0" cellPadding="2" width="100%" align="center" border="1">
          <colgroup>
            <col width="3%">
            <col width="17%">
            <col width="5%">
            <col width="9%">
            <col width="25%">
            <col width="26%">
            <col width="5%">
            <col width="5%">
            <col width="5%">
          <thead>
            <tr bgColor="#cccccc">
              <th style="background-color: #8181AB"><font color="#FFFFFF">NO.</font></th>
              <th style="background-color: #8181AB"><a href="KeySearchTerm.jsp" target="_blank"><font color="#FFFFFF">
                Key Search Term</font></a></th>
              <th style="background-color: #8181AB"><a href="SearchType.jsp" target="_blank"><font color="#FFFFFF">
                Search Type</font></a></th>
              <th style="background-color: #8181AB"><a href="GeneSymbol.jsp" target="_blank"><font color="#FFFFFF">
                Gene Symbol</font></a></th>
              <th style="background-color: #8181AB"><a href="AllSearchTerms.jsp" target="_blank"><font color="#FFFFFF">
                All Search Terms</font></a></th>
              <th style="background-color: #8181AB"><a href="GOAnnotation.jsp" target="_blank"><font color="#FFFFFF">
                GO Annotations</font></a></th>
              <th style="background-color: #8181AB"><a href="statistic_menu.jsp" target="_blank"><font color="#FFFFFF">
                Statistical Score</font></a></th>
              <th style="background-color: #8181AB"><a href="NumberOfPapers.jsp" target="_blank"><font color="#FFFFFF">
                Papers</font></a></th>
              <th style="background-color: #8181AB"><a href="DefaultRefSeqID.jsp" target="_blank"><font color="#FFFFFF">
                Default RefSeq ID</font></a></th>
            </tr>

            <logic:iterate id="association" name="associations"> 
            <tr>
              <% i++; %>
              <% if ((i%2) == 1) { bgcolor = "#ffffff" ;} %> 
              <% if ((i%2) == 0) { bgcolor = "#f5f5fc" ;} %> 

              <td align="middle" bgcolor="<% out.print(bgcolor); %>"><% out.print(i); %></td>

              <logic:equal name="association" property="target_gene.type" value="GENE">              
              <td bgcolor= "<% out.print(bgcolor); %>">
                <a href="DisplayLinks.do?hipGeneId=<bean:write name="association" property="target_gene.hipGeneId"/>" target="_blank"><bean:write name="association" property="target_gene.name"/>&nbsp</a>
              </td>
              <td> By gene term </td>
              </logic:equal>
              <logic:equal name="association" property="target_gene.type" value="FAMILY">
              <td bgcolor="<% out.print(bgcolor); %>">
                <bean:write name="association" property="target_gene.name"/>&nbsp                           
              </td>
              <td> By family term </td>
              </logic:equal>

              <td bgcolor="<% out.print(bgcolor); %>" align="center">
                <font size=2><bean:write name="association" property="target_gene.symbol"/>&nbsp</font>
              </td>

              <logic:equal name="association" property="target_gene.type" value="GENE">
              <td bgcolor="<% out.print(bgcolor); %>">
                <bean:write name="association" property="target_gene.nicknamesString"/>&nbsp
              </td>
              </logic:equal>
              <logic:equal name="association" property="target_gene.type" value="FAMILY">
              <td bgcolor="<% out.print(bgcolor); %>">
                <logic:iterate id="child" name="association" property="target_gene.nicknames"> 
                    <a href="DisplayLinks.do?geneSymbol=<bean:write name="child"/>" target="_blank">
                    <bean:write name="child"/></a>&nbsp
                </logic:iterate>
              </td>
              </logic:equal>
 
              <td bgcolor="<% out.print(bgcolor); %>">
                <bean:write name="association" property="target_gene.gosString"/>&nbsp
              </td>

              <td bgcolor="<% out.print(bgcolor); %>" align="center">
                <bean:write name="association" property="stat_analysis.score"/>&nbsp
              </td>

              <td bgcolor="<% out.print(bgcolor); %>" align="center">
                <a href="DisplayPaperLinks_GeneGene.do?source_gene_index=<bean:write name="source_gene_index"/>
&target_gene_locusid=<bean:write name="association" property="target_gene.locusid"/>
&target_gene_name=<bean:write name="association" property="target_gene.name"/>" target="_blank">
                <bean:write name="association" property="asso_data.doublehit"/></a>&nbsp
              </td>

              <td bgcolor="<% out.print(bgcolor); %>">
                <a href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?val=<bean:write name="association" property="target_gene.refSeq_NM2"/>" target="_blank">
                <font size=2> <bean:write name="association" property="target_gene.refSeq_NM"/> </font> </a>&nbsp
              </td>
            </tr>
            </logic:iterate> 
          </thead>
        </table>
        <p>&nbsp;
    </tr>
  </tbody>
</table>

<p>&nbsp;</p>

<p>
<table width='90%' align="center">
  <jsp:include page="links.jsp" flush="true"/>
</table>
</body>
</html>