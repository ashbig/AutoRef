<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Sort a gene list</title>
</head>

<body>
<jsp:include page="NavigatorBar.jsp" flush="true"/>
<div align="center">
<p align="left"><br>
</p>
  <center>
<table border="0" cellpadding="0" cellspacing="0" width="90%">
  <tr>
    <td width="93%"><b><font color="#000099" face="Verdana" size="5">Sort a Gene-Related Gene List</font></b></td>
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
    
<table align='center'><tr><td><h2><font color="#008000">Analysis Result of Gene List</font></h2></CENTER></td></tr></table>
<br>
<TABLE cellSpacing=0 cellPadding=2 width="80%" align=center border=4 bordercolor="#ECECFF">
  <TR>
    <TD bgcolor="#ECECFF" >Your input genes are grouped into <A href="microArray_category.jsp" 
    target=_black>3 categories</A> according to their relationship with the disease 
    of <B><bean:write name="gene_symbol"/></B> as cited in literature. The statistical method you 
    selected is <B><bean:write name="stat"/></B>. <BR><BR>
    <A href="#direct">First degree associations </A><BR>    
    <A href="#indirect">Second degree associations </A><BR>
    <A href="#new">Genes new to this disease </A><BR>
    </TD>
  </TR>
</TABLE>

    <br><br>
    <% int i = 0; String bgcolor = "";%>
    <a name="direct"> </a><br><br><% i = 0; %>
       
    <TABLE width = "80%" align="center" border="1" cellpadding="2" cellspacing="0" >   
    <TR> <b><font color="#009900"><img border="0" src="jpg/red_diam.gif" width="11" height="11">&nbsp;&nbsp;First degree associations</font></b> <br><br> </TR>
    <TR bgcolor="#cccccc">
        <TH width="7%" style="background-color: #8181AB"><font color="#FFFFFF">Rank</font></TH>
        <TH width="26%" style="background-color: #8181AB"><font color="#FFFFFF">Locus ID</font></TH>
        <TH width="26%" style="background-color: #8181AB"><font color="#FFFFFF">Gene Symbol</font></TH>
        <TH width="26%" style="background-color: #8181AB"><A HREF="statistic_menu.jsp" target="_blank"><font color="#FFFFFF">Statistical Score</font></A></TH>
        <TH width="15%" style="background-color: #8181AB"><font color="#FFFFFF">Selected Papers</font></TH>       
    </TR>
    <logic:iterate id="directChipGene" name="direct_genes">                       
        <% i++; %>
        <% if ((i%2) == 1) { bgcolor = "#ffffff" ;} %> 
        <% if ((i%2) == 0) { bgcolor = "#f5f5fc" ;} %> 
        <tr>
            <TD align="center" bgcolor="<% out.print(bgcolor); %>"><% out.print(i); %></TD>
            <TD align="center" bgcolor= "<% out.print(bgcolor); %>">
                <a href="http://www.ncbi.nlm.nih.gov/LocusLink/LocRpt.cgi?l=<bean:write name="directChipGene" property="locus_id"/>" target="_blank">
                <bean:write name="directChipGene" property="locus_id"/></a>
            </TD>

            <logic:equal name="directChipGene" property="gene_symbol" value="null">
            <TD align="center" bgcolor= "<% out.print(bgcolor); %>"> - </TD>            
            </logic:equal>
            <logic:notEqual name="directChipGene" property="gene_symbol" value="null">
            <TD align="center" bgcolor= "<% out.print(bgcolor); %>">
                <a href="DisplayLinks.do?geneSymbol=<bean:write name="directChipGene" property="gene_symbol"/>" target="_blank"> 
                <bean:write name="directChipGene" property="gene_symbol"/></a>               
            </TD>           
            </logic:notEqual>

            <TD align="center" bgcolor= "<% out.print(bgcolor); %>">
                <bean:write name="directChipGene" property="score"/>
            </TD>
            <TD align="center" bgcolor= "<% out.print(bgcolor); %>">
                <A HREF="DisplayPaperLinks_GeneGene.do?source_gene_index=<bean:write name="gene_symbol"/>&target_gene_locusid=<bean:write name="directChipGene" property="locus_id"/>" target="_blank" >
                Link</A>
            </TD>
        </tr>
    </logic:iterate> 
    </TABLE>

    <br>

    <a name="indirect"> </a><br><br><% i = 0; %>
    
    <TABLE width = "80%" align="center" border="1" cellpadding="2" cellspacing="0" >  
    <TR> <p> <b><font color="#009900"><img border="0" src="jpg/red_diam.gif" width="11" height="11">&nbsp;&nbsp;Second degree associations </font></b> <br><br>  </TR> 
    <TR bgcolor="#cccccc">
        <TH width="10%" style="background-color: #8181AB"><font color="#FFFFFF">Rank</font></TH>
        <TH width="30%" style="background-color: #8181AB"><font color="#FFFFFF">Locus ID</font></TH>
        <TH width="30%" style="background-color: #8181AB"><A HREF="GeneSymbol.jsp" target="_blank"><font color="#FFFFFF">Gene Symbol</font></A></TH>
        <TH width="30%" style="background-color: #8181AB"><A HREF="statistic_menu.jsp" target="_blank"><font color="#FFFFFF">Statistical Score</font></A></TH>
    </TR>

    <logic:iterate id="indirectChipGene" name="indirect_genes">
        <% i++; %>
        <% if ((i%2) == 1) { bgcolor = "#ffffff" ;} %> 
        <% if ((i%2) == 0) { bgcolor = "#f5f5fc" ;} %> 
        <tr>
            <TD align="center" bgcolor="<% out.print(bgcolor); %>"><% out.print(i); %></TD>
            <TD align="center" bgcolor="<% out.print(bgcolor); %>">
                <a href="http://www.ncbi.nlm.nih.gov/LocusLink/LocRpt.cgi?l=<bean:write name="indirectChipGene" property="locus_id"/>" target="_blank">
                <bean:write name="indirectChipGene" property="locus_id"/></a>
            </TD>

            <logic:equal name="indirectChipGene" property="gene_symbol" value="null">
            <TD align="center" bgcolor="<% out.print(bgcolor); %>"> - </TD>            
            </logic:equal>
            <logic:notEqual name="indirectChipGene" property="gene_symbol" value="null">
            <TD align="center" bgcolor="<% out.print(bgcolor); %>">
                <a href="DisplayLinks.do?geneSymbol=<bean:write name="indirectChipGene" property="gene_symbol"/>" target="_blank">
                <bean:write name="indirectChipGene" property="gene_symbol"/></a>
            </TD>
            </logic:notEqual>

            <TD align="center" bgcolor="<% out.print(bgcolor); %>">
                <bean:write name="indirectChipGene" property="score"/>
            </TD>
        </tr>
    </logic:iterate> 
    </TABLE>

    <br>

    <a name="new"> </a><br><br><% i = 0; %>
   
    <TABLE width = "80%" align="center" border="1" cellpadding="2" cellspacing="0" >   
    <TR> <p> <b><font color="#009900"><img border="0" src="jpg/red_diam.gif" width="11" height="11">&nbsp;&nbsp;Genes new to this disease</font></b> <br><br>   </TR> 
    <TR bgcolor="#cccccc">
        <TH width="10%" style="background-color: #8181AB"><font color="#FFFFFF">Rank</font></TH>
        <TH width="30%" style="background-color: #8181AB"><font color="#FFFFFF">Locus ID</font></TH>
        <TH width="30%" style="background-color: #8181AB"><A HREF="GeneSymbol.jsp" target="_blank"><font color="#FFFFFF">Gene Symbol</font></A></TH>
        <TH width="30%" style="background-color: #8181AB"><A HREF="statistic_menu.jsp" target="_blank"><font color="#FFFFFF">Statistical Score</font></A></TH>
    </TR>

    <logic:iterate id="newChipGene" name="new_genes"> 
        <% i++; %>
        <% if ((i%2) == 1) { bgcolor = "#ffffff" ;} %> 
        <% if ((i%2) == 0) { bgcolor = "#f5f5fc" ;} %> 
        <tr>
            <TD align="center" bgcolor="<% out.print(bgcolor); %>"><% out.print(i); %></TD>

            <logic:equal name="newChipGene" property="locus_id" value="0">
            <TD align="center" bgcolor="<% out.print(bgcolor); %>"> - </TD>            
            </logic:equal>
            <logic:notEqual name="newChipGene" property="locus_id" value="0">
            <TD align="center" bgcolor="<% out.print(bgcolor); %>"> <a href="http://www.ncbi.nlm.nih.gov/LocusLink/LocRpt.cgi?l=<bean:write name="newChipGene" property="locus_id"/>" target="_blank">
                <bean:write name="newChipGene" property="locus_id"/></a> </TD>
            </logic:notEqual>

            <logic:equal name="newChipGene" property="gene_symbol" value=" ">
            <TD align="center" bgcolor="<% out.print(bgcolor); %>"> - </TD>            
            </logic:equal>
            <logic:notEqual name="newChipGene" property="gene_symbol" value=" ">
            <TD align="center" bgcolor="<% out.print(bgcolor); %>"> <a href="DisplayLinks.do?geneSymbol=<bean:write name="newChipGene" property="gene_symbol"/>" target="_blank">
                <bean:write name="newChipGene" property="gene_symbol"/></a> </TD>
            </logic:notEqual>

            <TD align="center" bgcolor="<% out.print(bgcolor); %>"> - </TD>
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
