<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 

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
    <td align="left" valign="top">
	<jsp:include page="viewAllVectorsTitle.jsp" />
      <html:form action="SetVectorDisplay.do">

<p class="mainbodytexthead">List of search terms found</p>
<p class="mainbodytexthead" align="right"><a target="_blank" href="TermDefinition.jsp">Explanation of Terms</a></p>
<table width="100%" border="0">
  <tr>
    <td class="tableheader">&nbsp;</td>
    <td class="tableheader"><a href="SetVectorDisplay.do?sortby=cloneid">Clone ID</a></td>
    <td class="tableheader"><a href="SetVectorDisplay.do?sortby=clonetype">Clone Type</a></td>
    <td class="tableheader"><a href="SetVectorDisplay.do?sortby=vectorname">Vector Name</a></td>
    <td class="tableheader"><a href="SetVectorDisplay.do?sortby=selection">Selection Markers</a></td>
    <td class="tableheader"><a href="SetVectorDisplay.do?sortby=restriction">Distribution</a></td>
    <td class="tableheader">&nbsp;</td>
  </tr>

  <% int i=0;%>
  <logic:iterate name="directFounds" id="clone">
  <tr class="tableinfo"> 
    <td><%=++i%></td>
    <td><a target="_blank" href="GetCloneDetail.do?cloneid=<bean:write name="clone" property="cloneid"/>"><bean:write name="clone" property="name"/></a></td>
    <td><bean:write name="clone" property="type"/></td>
    <td><a target="_blank" href="GetVectorDetail.do?vectorid=<bean:write name="clone" property="vectorid"/>"><bean:write name="clone" property="vectorname"/></a></td>
    <td>
    <logic:iterate name="clone" property="selections" id="selection">
        <bean:write name="selection" property="hosttype"/>: <bean:write name="selection" property="marker"/>
    </logic:iterate>
    </td>
    <td><bean:write name="clone" property="restriction"/></td>
    <html:form action="SetVectorDisplay.do">
    <input type="hidden" name="cloneid" value="<bean:write name="clone" property="cloneid"/>"/>
    <logic:equal name="clone" property="isAddedToCart" value="true">
        <td valign="center" bgcolor="blue">
            <input name="button" type="submit" class="itemtext" value="Add To Cart"/>
        </td>
    </logic:equal>
    <logic:notEqual name="clone" property="isAddedToCart" value="true">
        <td valign="center">
            <input name="button" type="submit" class="itemtext" value="Add To Cart"/>
        </td>
    </logic:notEqual>
    </html:form>
    </td>
    </tr>
  </logic:iterate>

      </html:form>
</td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

