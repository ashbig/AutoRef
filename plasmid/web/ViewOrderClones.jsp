<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.Clone" %> 
<%@ page import="plasmid.coreobject.User" %>

<html>
<head>
<title>PlasmID Database</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
</head>

<body>
<jsp:include page="homeTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menuHome.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
	<jsp:include page="orderHistoryTitle.jsp" />

<p class="text">Order ID: <bean:write name="orderid"/>
<p>
<table width="100%" border="0">
  <tr>
    <td class="tableheader">&nbsp;</td>
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
    <td class="tableheader">Quantity</td>
  </tr>

  <% int i=0;%>
  <logic:iterate name="orderClones" id="clone">
  <tr class="tableinfo"> 
    <td><%=++i%></td>
    <td><a target="_blank" href="GetCloneDetail.do?cloneid=<bean:write name="clone" property="cloneid"/>"><bean:write name="clone" property="name"/></a></td>
    <td><bean:write name="clone" property="type"/></td>    
    <logic:notEqual name="clone" property="type" value="<%=Clone.NOINSERT%>">
    <logic:iterate name="clone" property="inserts" id="insert">
        <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=Retrieve&dopt=Graphics&list_uids=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
        <td><bean:write name="insert" property="name"/></td>
        <td><bean:write name="insert" property="description"/></td>
        <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="targetseqid"/>"><bean:write name="insert" property="targetgenbank"/></a></td>
        <td><bean:write name="insert" property="hasmutation"/></td>
        <td><bean:write name="insert" property="hasdiscrepancy"/></td>
        <td><bean:write name="insert" property="format"/></td>
    </logic:iterate>
    </logic:notEqual>
    <logic:equal name="clone" property="type" value="<%=Clone.NOINSERT%>">
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </logic:equal>
    <td><a target="_blank" href="GetVectorDetail.do?vectorid=<bean:write name="clone" property="vectorid"/>"><bean:write name="clone" property="vectorname"/></a></td>
    <td>
    <logic:iterate name="clone" property="selections" id="selection">
        <bean:write name="selection" property="hosttype"/>: <bean:write name="selection" property="marker"/>;
    </logic:iterate>
    </td>
    <td><bean:write name="clone" property="specialtreatment"/></td>
    <td><bean:write name="clone" property="quantity"/></td>
    </tr>
  </logic:iterate>
</table>

<html:form action="DownloadClones.do">
<input type="hidden" name="type" value="<bean:write name="type"/>">
<input type="hidden" name="orderid" value="<bean:write name="orderid"/>">
<input type="hidden" name="collectionName" value="<bean:write name="collectionName"/>">
<table width="100%" border="0">
  <tr>
    <td width="50%">&nbsp;</td>
    <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
    <td>
        <html:submit styleClass="text" property="button" value="<%=Constants.BUTTON_CREATE_BIOBANK_WORKLIST%>"/>
    </td>
    </logic:equal>
    <td>
        <html:submit styleClass="text" property="button" value="Download Clone List"/>
    </td>
  </tr>
</table>
</html:form>

    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

