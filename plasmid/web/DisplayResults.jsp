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
      <html:form action="RefseqSearch.do" enctype="multipart/form-data">
<html:hidden property="pagesize"/>
<html:hidden property="page"/>

<table width="100%" border="0">
  <tr>
    <td class="tableheader">&nbsp;</td>
    <td class="tableheader">Search Term</td>
    <td class="tableheader">Clone ID</td>
    <td class="tableheader">Clone Type</td>
    <td class="tableheader">Gene ID</td>
    <td class="tableheader">Gene Symbol</td>
    <td class="tableheader">Gene Name</td>
    <td class="tableheader">Target Genbank</td>
    <td class="tableheader">Insert Format</td>
    <td class="tableheader">Vector</td>
    <td class="tableheader">Selection Markers</td>
    <td class="tableheader">Restriction</td>
  </tr>
  <% int i=((Integer)request.getAttribute("pagesize")).intValue()*(((Integer)request.getAttribute("page")).intValue()-1);%>
  <logic:iterate name="found" id="element">
  <bean:define name="element" id="term" property="key"/>
  <tr class="tableinfo"> 
    <td rowspan='<%=((Integer)((Map)request.getSession().getAttribute("foundCounts")).get(term)).intValue()%>'><%=++i%></td>
    <td rowspan='<%=((Integer)((Map)request.getSession().getAttribute("foundCounts")).get(term)).intValue()%>'><bean:write name="element" property="key"/></td>
    <% int n=1;%>
    <logic:iterate name="element" property="value" id="clone">
    <% if(n>1) %>
    <tr class="tableinfo"> 
    <% n++;%>
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
  </logic:iterate>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

