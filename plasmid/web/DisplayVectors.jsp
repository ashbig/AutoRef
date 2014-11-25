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
<script language="JavaScript">
function readCookie(name){
return(document.cookie.match('(^|; )'+name+'=([^;]*)')||0)[2]
}
</script>
</head>
<div class="gridContainer clearfix">

<body onScroll="document.cookie='ypos=' + window.pageYOffset" onLoad="window.scrollTo(0,readCookie('ypos'))">
<jsp:include page="orderTitle.jsp" />
<table width="100%" height="406" border="0" align="left" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    
    <td width="100%" align="left" valign="top">
	<%--<jsp:include page="viewAllVectorsTitle.jsp" />--%>
      <html:form action="SetVectorDisplay.do">
<div class="content">
<!--<p class="mainbodytexthead">List of search terms found</p>-->
<p class="text">This is only clones without inserts ('empty vectors').  
Many other vectors are represented in the repository 
(try 'search by reference sequence' or 'view collection').</P>
<p class="mainbodytexthead" align="right"><a target="_blank" href="TermDefinition.jsp">Explanation of Terms</a></p>
<table width="100%" border="0">
  <tr>
    <td class="tableheader">&nbsp;</td>
    <td class="tableheader"><a href="SetVectorDisplay.do?sortby=cloneid">Clone ID</a></td>
    <td class="tableheader"><a href="SetVectorDisplay.do?sortby=vectorname">Vector Name</a></td>
    <td class="tableheader">Description</td>
    <td class="tableheader">Cloning Strategy</td>
    <td class="tableheader">Tag</td>
    <td class="tableheader">Special MTA</td>
    <td class="tableheader">Use Restriction</td>
    <td class="tableheader">&nbsp;</td>
  </tr>

  <% int i=0;%>
  <logic:iterate name="directFounds" id="clone">
  <tr class="tableinfo"> 
    <td><%=++i%></td>
    <td><a target="_blank" href="GetCloneDetail.do?cloneid=<bean:write name="clone" property="cloneid"/>"><bean:write name="clone" property="name"/></a></td>
    <td><a target="_blank" href="GetVectorDetail.do?vectorid=<bean:write name="clone" property="vectorid"/>"><bean:write name="clone" property="vectorname"/></a></td>
    <td><bean:write name="clone" property="description"/></td>
    <td><bean:write name="clone" property="vector.cloningSystem"/></td>
    <td><bean:write name="clone" property="vector.vectorTag"/></td>
    <td><bean:write name="clone" property="specialtreatment"/></td>
    <td><bean:write name="clone" property="restriction"/></td>
    <html:form action="SetVectorDisplay.do">
    <input type="hidden" name="cloneid" value="<bean:write name="clone" property="cloneid"/>"/>
    <logic:equal name="clone" property="isAddedToCart" value="true">
        <td valign="center">
            <input name="button" type="submit" class="itemtext" value="In Cart" disabled="true"/>
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
</div>
<jsp:include page="footer.jsp" /></body></div>
</html>]

