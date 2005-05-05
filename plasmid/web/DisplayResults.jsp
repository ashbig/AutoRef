<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.Map" %> 

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
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menu.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
	<jsp:include page="searchByRefseqTitle.jsp" />
      <html:form action="SetDisplay.do" enctype="multipart/form-data">
<html:hidden property="pagesize"/>
<html:hidden property="page"/>

<table width="100%" border="0">
  <tr>
    <logic:equal name="display" value="genbank">
    <td class="tablebody">Clones found by direct match</td>
    <td class="tablebody">Clones found by indirect match</td>
    </logic:equal>
    <logic:equal name="display" value="symbol">
    <td class="tablebody">Clones found</td>
    </logic:equal>
    <td class="tablebody">Clones not found</td>
  </tr>  
  <tr>
    <logic:equal name="display" value="genbank">
    <td class="tableinfo"><a href="SetDisplay.do?displayPage=direct"><bean:write name="numOfDirectFound"/></a></td>
    <td class="tableinfo"><a href="SetDisplay.do?displayPage=indirect"><bean:write name="numOfFound"/></a></td>
    </logic:equal>
    <logic:equal name="display" value="symbol">
    <td class="tableinfo"><a href="SetDisplay.do?displayPage=indirect"><bean:write name="numOfFound"/></a></td>
    </logic:equal>
    <td class="tableinfo"><a href="SetDisplay.do?displayPage=nofound"><bean:write name="numOfNoFounds"/></a></td>
  </tr>
</table>

<logic:notEqual name="displayPage" value="nofound">
<p class="mainbodytexthead">List of search terms found</p>
<table width="100%" border="0">
  <tr>
    <td class="tableheader">&nbsp;</td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=searchterm&displayPage=<bean:write name="displayPage"/>">Search Term</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=cloneid&displayPage=<bean:write name="displayPage"/>">Clone ID</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=clonetype&displayPage=<bean:write name="displayPage"/>">Clone Type</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=geneid&displayPage=<bean:write name="displayPage"/>">Gene ID</a></td>
    <td class="tableheader">Gene Symbol</td>
    <td class="tableheader">Gene Name</td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=targetseq&displayPage=<bean:write name="displayPage"/>">Reference Sequence</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=insertformat&displayPage=<bean:write name="displayPage"/>">Insert Format</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=vectorname&displayPage=<bean:write name="displayPage"/>">Vector</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=selection&displayPage=<bean:write name="displayPage"/>">Selection Markers</a></td>
    <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=restriction&displayPage=<bean:write name="displayPage"/>">Use Restriction</a></td>
  </tr>

  <logic:equal name="displayPage" value="indirect">
  <% int i=((Integer)request.getAttribute("pagesize")).intValue()*(((Integer)request.getAttribute("page")).intValue()-1);%>
  <logic:iterate name="found" id="clone">
  <tr class="tableinfo"> 
    <td><%=++i%></td>
    <td><bean:write name="clone" property="term"/></td>
    <td><a target="_blank" href="GetCloneDetail.do?cloneid=<bean:write name="clone" property="cloneid"/>"><bean:write name="clone" property="name"/></a></td>
    <td><bean:write name="clone" property="type"/></td>
    <logic:iterate name="clone" property="inserts" id="insert">
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=Retrieve&dopt=Graphics&list_uids=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    <td><bean:write name="insert" property="name"/></td>
    <td><bean:write name="insert" property="description"/></td>
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="targetseqid"/>"><bean:write name="insert" property="targetgenbank"/></a></td>
    <td><bean:write name="insert" property="format"/></td>
    </logic:iterate>
    <td><a target="_blank" href="GetVectorDetail.do?vectorid=<bean:write name="clone" property="vectorid"/>"><bean:write name="clone" property="vectorname"/></a></td>
    <td>
    <logic:iterate name="clone" property="selections" id="selection">
        <bean:write name="selection" property="hosttype"/>: <bean:write name="selection" property="marker"/>
    </logic:iterate>
    </td>
    <td><bean:write name="clone" property="restriction"/></td>
    </tr>
  </logic:iterate>
  </logic:equal>

  <logic:equal name="displayPage" value="direct">
  <% int i=((Integer)request.getAttribute("pagesize")).intValue()*(((Integer)request.getAttribute("page")).intValue()-1);%>
  <logic:iterate name="directFounds" id="clone">
  <tr class="tableinfo"> 
    <td><%=++i%></td>
    <td><bean:write name="clone" property="term"/></td>
    <td><a target="_blank" href="GetCloneDetail.do?cloneid=<bean:write name="clone" property="cloneid"/>"><bean:write name="clone" property="name"/></a></td>
    <td><bean:write name="clone" property="type"/></td>
    <logic:iterate name="clone" property="inserts" id="insert">
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=Retrieve&dopt=Graphics&list_uids=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    <td><bean:write name="insert" property="name"/></td>
    <td><bean:write name="insert" property="description"/></td>
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="targetseqid"/>"><bean:write name="insert" property="targetgenbank"/></a></td>
    <td><bean:write name="insert" property="format"/></td>
    </logic:iterate>
    <td><a target="_blank" href="GetVectorDetail.do?vectorid=<bean:write name="clone" property="vectorid"/>"><bean:write name="clone" property="vectorname"/></a></td>
    <td>
    <logic:iterate name="clone" property="selections" id="selection">
        <bean:write name="selection" property="hosttype"/>: <bean:write name="selection" property="marker"/>
    </logic:iterate>
    </td>
    <td><bean:write name="clone" property="restriction"/></td>
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

