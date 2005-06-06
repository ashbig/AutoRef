<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title>PlasmID Database</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
</head>

<body>
<jsp:include page="orderTitle.jsp" />
<table width="800" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="136" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menu.jsp" />
	</td>
    <td width="664" align="left" valign="top">
	<jsp:include page="searchByRefseqTitle.jsp" />
      <html:form action="SetDisplay.do" enctype="multipart/form-data">
<html:hidden property="pagesize"/>
<html:hidden property="page"/>
        <html:hidden property="species"/>
        <html:hidden property="refseqType"/>

<table width="100%" border="0">
  <tr>
    <logic:equal name="display" value="genbank">
    <td class="tablebody">Clones found by direct match</td>
    <td class="tablebody">Clones found by indirect match</td>
    </logic:equal>
    <logic:equal name="display" value="symbol">
    <td class="tablebody">Clones found</td>
    </logic:equal>
    <td class="tablebody">Search terms not found</td>
  </tr>  
  <tr>
    <logic:equal name="display" value="genbank">
    <td class="tableinfo"><a href="SetDisplay.do?displayPage=direct&species=<bean:write name="species"/>"><bean:write name="numOfDirectFound"/></a></td>
    <td class="tableinfo"><a href="SetDisplay.do?displayPage=indirect&species=<bean:write name="species"/>"><bean:write name="numOfFound"/></a></td>
    </logic:equal>
    <logic:equal name="display" value="symbol">
    <td class="tableinfo"><a href="SetDisplay.do?displayPage=indirect&species=<bean:write name="species"/>"><bean:write name="numOfFound"/></a></td>
    </logic:equal>
    <td class="tableinfo"><a href="SetDisplay.do?displayPage=nofound&species=<bean:write name="species"/>"><bean:write name="numOfNoFounds"/></a></td>
  </tr>
</table>

<logic:notEqual name="displayPage" value="nofound">
<p class="mainbodytexthead">List of search terms found</p>
<table width="100%" border="0">
  <tr>
    <td class="tableheader">&nbsp;</td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=searchterm&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Search Term</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=cloneid&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Clone ID</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=clonetype&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Clone Type</a></td>
    <logic:equal name="species" value="Homo sapiens">
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=geneid&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Gene ID</a></td>
    </logic:equal>
    <logic:equal name="species" value="Saccharomyces cerevisiae">
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=geneid&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">SGD</a></td>
    </logic:equal>
    <logic:equal name="species" value="Pseudomonas aeruginosa">
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=geneid&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">PA Number</a></td>
    </logic:equal>
    <td class="tableheader">Gene Symbol</td>
    <td class="tableheader">Gene Name</td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=targetseq&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Reference Sequence</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=insertmutation&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Mutation</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=insertdiscrepancy&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Discrepancy</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=insertformat&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Insert Format</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=vectorname&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Vector</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=selection&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Selection Markers</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=restriction&displayPage=<bean:write name="displayPage"/>&species=<bean:write name="species"/>">Use Restriction</a></td>
    <td class="tableheader">&nbsp;</td>
  </tr>

  <logic:equal name="displayPage" value="indirect">
  <% int i=((Integer)request.getAttribute("pagesize")).intValue()*(((Integer)request.getAttribute("page")).intValue()-1);%>
  <logic:iterate name="found" id="clone">
  <tr class="tableinfo"> 
    <td><%=++i%></td>
    <td><bean:write name="clone" property="term"/></td>
    <td><a target="_blank" href="GetCloneDetail.do?cloneid=<bean:write name="clone" property="cloneid"/>&species=<bean:write name="species"/>"><bean:write name="clone" property="name"/></a></td>
    <td><bean:write name="clone" property="type"/></td>
    <logic:iterate name="clone" property="inserts" id="insert">
    <logic:equal name="species" value="Homo sapiens">
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=Retrieve&dopt=Graphics&list_uids=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="species" value="Pseudomonas aeruginosa">
    <td><a target="_blank" href="http://www.pseudomonas.com/AnnotationByPAU.asp?PA=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="species" value="Saccharomyces cerevisiae">
    <td><a target="_blank" href="http://db.yeastgenome.org/cgi-bin/locus.pl?locus=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <td><bean:write name="insert" property="name"/></td>
    <td><bean:write name="insert" property="description"/></td>
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="targetseqid"/>"><bean:write name="insert" property="targetgenbank"/></a></td>
    <td><bean:write name="insert" property="hasmutation"/></td>
    <td><bean:write name="insert" property="hasdiscrepancy"/></td>
    <td><bean:write name="insert" property="format"/></td>
    </logic:iterate>
    <td><a target="_blank" href="GetVectorDetail.do?vectorid=<bean:write name="clone" property="vectorid"/>"><bean:write name="clone" property="vectorname"/></a></td>
    <td>
    <logic:iterate name="clone" property="selections" id="selection">
        <bean:write name="selection" property="hosttype"/>: <bean:write name="selection" property="marker"/>
    </logic:iterate>
    </td>
    <td><bean:write name="clone" property="restriction"/></td>
    <td valign="center">
            <html:form action="SetDisplay.do">
            <html:hidden property="pagesize"/>
            <html:hidden property="page"/>
        <html:hidden property="species"/>
        <html:hidden property="refseqType"/>
            <input type="hidden" name="displayPage" value="indirect"/>
            <input type="hidden" name="cloneid" value="<bean:write name="clone" property="cloneid"/>"/>
            <input name="button" type="submit" class="itemtext" value="Add To Cart"/>
            </html:form>
    </td>
    </tr>
  </logic:iterate>
  </logic:equal>

  <logic:equal name="displayPage" value="direct">
  <% int i=((Integer)request.getAttribute("pagesize")).intValue()*(((Integer)request.getAttribute("page")).intValue()-1);%>
  <logic:iterate name="directFounds" id="clone">
  <tr class="tableinfo"> 
    <td><%=++i%></td>
    <td><bean:write name="clone" property="term"/></td>
    <td><a target="_blank" href="GetCloneDetail.do?cloneid=<bean:write name="clone" property="cloneid"/>&species=<bean:write name="species"/>"><bean:write name="clone" property="name"/></a></td>
    <td><bean:write name="clone" property="type"/></td>
    <logic:iterate name="clone" property="inserts" id="insert">
    <logic:equal name="species" value="Homo sapiens">
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=Retrieve&dopt=Graphics&list_uids=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="species" value="Pseudomonas aeruginosa">
    <td><a target="_blank" href="http://www.pseudomonas.com/AnnotationByPAU.asp?PA=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="species" value="Saccharomyces cerevisiae">
    <td><a target="_blank" href="http://db.yeastgenome.org/cgi-bin/locus.pl?locus=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <td><bean:write name="insert" property="name"/></td>
    <td><bean:write name="insert" property="description"/></td>
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="targetseqid"/>"><bean:write name="insert" property="targetgenbank"/></a></td>
    <td><bean:write name="insert" property="hasmutation"/></td>
    <td><bean:write name="insert" property="hasdiscrepancy"/></td>    
    <td><bean:write name="insert" property="format"/></td>
    </logic:iterate>
    <td><a target="_blank" href="GetVectorDetail.do?vectorid=<bean:write name="clone" property="vectorid"/>"><bean:write name="clone" property="vectorname"/></a></td>
    <td>
    <logic:iterate name="clone" property="selections" id="selection">
        <bean:write name="selection" property="hosttype"/>: <bean:write name="selection" property="marker"/>
    </logic:iterate>
    </td>
    <td><bean:write name="clone" property="restriction"/></td>
    <TD valign="center">
            <html:form action="SetDisplay.do">
            <html:hidden property="pagesize"/>
            <html:hidden property="page"/>
        <html:hidden property="species"/>
        <html:hidden property="refseqType"/>
            <input type="hidden" name="displayPage" value="direct"/>
            <input type="hidden" name="cloneid" value="<bean:write name="clone" property="cloneid"/>"/>
            <input name="button" type="submit" class="itemtext" value="Add To Cart"/>
            </html:form>
    </td>
    </tr>
  </logic:iterate>
  </logic:equal>
</table>
</logic:notEqual>

<logic:equal name="displayPage" value="nofound">
<p class="mainbodytexthead">List of search terms not found</p>
<table width="50%" border="0">
  <tr>
    <td class="tableheader">Search Term</td>
  </tr>
  <logic:iterate name="nofound" id="nf">
  <tr class="tableinfo"> 
    <td><bean:write name="nf"/></td>
  </tr>
  </logic:iterate>
</table>
</logic:equal>

      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>
