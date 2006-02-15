<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.RefseqNameType" %> 
<%@ page import="plasmid.form.RefseqSearchForm" %> 
<%@ page import="plasmid.coreobject.Clone" %> 

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
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="136" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menu.jsp" />
	</td>
    <td width="864" align="left" valign="top">
	<jsp:include page="searchByRefseqTitle.jsp" />

<html:form action="RefseqSearchContinue.do">
<html:hidden property="cdna"/>
<html:hidden property="shrna"/>
<html:hidden property="genomicfragment"/>
<html:hidden property="tfbindsite"/>
<html:hidden property="genome"/>
<table width="100%" border="0">
  <tr>
    <logic:equal name="refseqSearchForm" property="display" value="genbank">
    <td class="tablebody">Clones found by direct match</td>
    <td class="tablebody">Clones found by indirect match</td>
    </logic:equal>
    <logic:equal name="refseqSearchForm" property="display" value="symbol">
    <td class="tablebody">Matches found</td>
    </logic:equal>
    <td class="tablebody">Search terms not found</td>
  </tr>  
  <tr>
    <logic:equal name="refseqSearchForm" property="display" value="genbank">
    <td class="tableinfo"><a href="RefseqSearchContinue.do?displayPage=direct&page=1&cdna=<bean:write name="refseqSearchForm" property="cdna"/>"><bean:write name="numOfDirectFound"/></a></td>
    <td class="tableinfo"><a href="RefseqSearchContinue.do?displayPage=indirect&page=1&cdna=<bean:write name="refseqSearchForm" property="cdna"/>"><bean:write name="numOfFound"/></a></td>
    </logic:equal>
    <logic:equal name="refseqSearchForm" property="display" value="symbol">
    <td class="tableinfo"><a href="RefseqSearchContinue.do?displayPage=indirect&page=1&cdna=<bean:write name="refseqSearchForm" property="cdna"/>"><bean:write name="numOfFound"/></a></td>
    </logic:equal>
    <td class="tableinfo"><a href="RefseqSearchContinue.do?displayPage=nofound&page=1&cdna=<bean:write name="refseqSearchForm" property="cdna"/>"><bean:write name="numOfNoFounds"/></a></td>
  </tr>
</table>

<bean:define id="size" name="refseqSearchForm" property="pagesize"/>
<bean:define id="p" name="refseqSearchForm" property="page"/>
<bean:define id="total" name="numOfFound"/>

<logic:notEqual name="refseqSearchForm" property="displayPage" value="nofound">
<p class="mainbodytexthead">List of search terms found</p>
<table width="100%" border="0">
    <tr class="mainbodytexthead">
        <td align="left" class="mainbodytexthead">Page: 
            <html:select property="page">
                <%  int i=0;
                    while(i<Integer.parseInt(total.toString())/Integer.parseInt(size.toString())) {
                %>
                        <html:option value="<%=(new Integer(i+1)).toString()%>"/>
                <%      i++;
                    }
                    if((Integer.parseInt(total.toString())%Integer.parseInt(size.toString()))>0)
                %>
                        <html:option value="<%=(new Integer(i+1)).toString()%>"/>
            </html:select>
            <html:submit property="button" value="Display"/>
        </td>
        <td align="right" class="mainbodytexthead"><a target="_blank" href="TermDefinition.jsp">Explanation of Terms</a></td>
    </tr>
</table>

<p>
<table width="100%" border="0">
  <tr>
    <td class="tableheader">&nbsp;</td>
    <td class="tableheader">Search Term</td>
    <td class="tableheader">Clone ID</td>
    <td class="tableheader">Clone Type</td>
    <td class="tableheader">Species Specific ID</td>
    <td class="tableheader">Gene Symbol</td>
    <td class="tableheader">Gene Name</td>
    <td class="tableheader">Reference Sequence</td>
    <td class="tableheader">Mutation</td>
    <td class="tableheader">Discrepancy</td>
    <td class="tableheader">Insert Format</td>
    <td class="tableheader">Vector</td>
    <td class="tableheader">Selection Markers</td>
    <td class="tableheader">Special MTA</td>
    <td class="tableheader">&nbsp;</td>
  </tr>

  <logic:equal name="refseqSearchForm" property="displayPage" value="indirect">
  <% int i= Integer.parseInt(size.toString())*(Integer.parseInt(p.toString())-1);
  %>
  <logic:iterate name="found" id="clone">

  <tr class="tableinfo"> 
    <td><%=++i%></td>
    <td><bean:write name="clone" property="term"/></td>
    <td><a target="_blank" href="GetCloneDetail.do?cloneid=<bean:write name="clone" property="cloneid"/>&species=<bean:write name="refseqSearchForm" property="species"/>"><bean:write name="clone" property="name"/></a></td>
    <td><bean:write name="clone" property="type"/></td>
    <logic:equal name="clone" property="type" value="<%=Clone.NOINSERT%>">
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    </logic:equal>

    <logic:notEqual name="clone" property="type" value="<%=Clone.NOINSERT%>">
    <logic:iterate name="clone" property="inserts" id="insert">
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.GENEID%>">
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=Retrieve&dopt=Graphics&list_uids=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.PA%>">
    <td><a target="_blank" href="http://www.pseudomonas.com/AnnotationByPAU.asp?PA=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.SGD%>">
    <td><a target="_blank" href="http://db.yeastgenome.org/cgi-bin/locus.pl?locus=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.GENBANK%>">
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.VCNUMBER%>">
    <td><a target="_blank" href="http://www.tigr.org/tigr-scripts/CMR2/GenePage.spl?locus=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.FTNUMBER%>">
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=genome&cmd=search&term=txid177416%5borgn"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <td><bean:write name="insert" property="name"/></td>
    <td><bean:write name="insert" property="description"/></td>
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="targetseqid"/>"><bean:write name="insert" property="targetgenbank"/></a></td>
    <td><bean:write name="insert" property="hasmutation"/></td>
    <td><bean:write name="insert" property="hasdiscrepancy"/></td>
    <td><bean:write name="insert" property="format"/></td>
    </logic:iterate>
    </logic:notEqual>
    <td><a target="_blank" href="GetVectorDetail.do?vectorid=<bean:write name="clone" property="vectorid"/>"><bean:write name="clone" property="vectorname"/></a></td>
    <td>
    <logic:iterate name="clone" property="selections" id="selection">
        <bean:write name="selection" property="hosttype"/>: <bean:write name="selection" property="marker"/>
    </logic:iterate>
    </td>
    <td><bean:write name="clone" property="specialtreatment"/></td>
    <html:form action="RefseqSearchContinue.do">
    <html:hidden property="cdna"/>
    <html:hidden property="shrna"/>
    <html:hidden property="genomicfragment"/>
    <html:hidden property="tfbindsite"/>
    <html:hidden property="genome"/>
    <td valign="center">
        <input type="hidden" name="displayPage" value="indirect"/>
        <input type="hidden" name="cloneid" value="<bean:write name="clone" property="cloneid"/>"/>
        <input name="button" type="submit" class="itemtext" value="Add To Cart"/> 
    </td> 
    </html:form>
    </tr>
  </logic:iterate>
  </logic:equal>

  <logic:equal name="refseqSearchForm" property="displayPage" value="direct">
  <%  int i= Integer.parseInt(size.toString())*(Integer.parseInt(p.toString())-1);%>
  <logic:iterate name="directFounds" id="clone">
  <tr class="tableinfo"> 
    <td><%=++i%></td>
    <td><bean:write name="clone" property="term"/></td>
    <td><a target="_blank" href="GetCloneDetail.do?cloneid=<bean:write name="clone" property="cloneid"/>&species=<bean:write name="refseqSearchForm" property="species"/>"><bean:write name="clone" property="name"/></a></td>
    <td><bean:write name="clone" property="type"/></td>
    <logic:equal name="clone" property="type" value="<%=Clone.NOINSERT%>">
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    </logic:equal>
    <logic:notEqual name="clone" property="type" value="<%=Clone.NOINSERT%>">
    <logic:iterate name="clone" property="inserts" id="insert">
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.GENEID%>">
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=Retrieve&dopt=Graphics&list_uids=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.PA%>">
    <td><a target="_blank" href="http://www.pseudomonas.com/AnnotationByPAU.asp?PA=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.SGD%>">
    <td><a target="_blank" href="http://db.yeastgenome.org/cgi-bin/locus.pl?locus=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.GENBANK%>">
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.VCNUMBER%>">
    <td><a target="_blank" href="http://www.tigr.org/tigr-scripts/CMR2/GenePage.spl?locus=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.FTNUMBER%>">
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=genome&cmd=search&term=txid177416%5borgn"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <td><bean:write name="insert" property="name"/></td>
    <td><bean:write name="insert" property="description"/></td>
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="targetseqid"/>"><bean:write name="insert" property="targetgenbank"/></a></td>
    <td><bean:write name="insert" property="hasmutation"/></td>
    <td><bean:write name="insert" property="hasdiscrepancy"/></td>    
    <td><bean:write name="insert" property="format"/></td>
    </logic:iterate>
    </logic:notEqual>
    <td><a target="_blank" href="GetVectorDetail.do?vectorid=<bean:write name="clone" property="vectorid"/>"><bean:write name="clone" property="vectorname"/></a></td>
    <td>
    <logic:iterate name="clone" property="selections" id="selection">
        <bean:write name="selection" property="hosttype"/>: <bean:write name="selection" property="marker"/>
    </logic:iterate>
    </td>
    <td><bean:write name="clone" property="specialtreatment"/></td>
    <html:form action="RefseqSearchContinue.do">
    <html:hidden property="cdna"/>
    <html:hidden property="shrna"/>
    <html:hidden property="genomicfragment"/>
    <html:hidden property="tfbindsite"/>
    <html:hidden property="genome"/>
    <input type="hidden" name="displayPage" value="direct"/>
    <input type="hidden" name="cloneid" value="<bean:write name="clone" property="cloneid"/>"/>
        <TD valign="center">
            <input name="button" type="submit" class="itemtext" value="Add To Cart"/>
        </td>
    </html:form>
    </tr>
  </logic:iterate>
  </logic:equal>
</table>
</logic:notEqual>

<logic:equal name="refseqSearchForm" property="displayPage" value="nofound">
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

</html:form>
      </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

