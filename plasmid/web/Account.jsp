<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.User" %>

<html>
<head>
<title>My Account</title>
<meta name='description' content='View details about my specific account including past orders and account information'>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
       <link href="layout.css" rel="stylesheet" type="text/css" />
        <link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
        <link href="boilerplate.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="respond.min.js"></script>
</head>
<div class="gridContainer clearfix">

<body>
<jsp:include page="signinMenuBar.jsp" />
<div id="aside">
<table border="0">
    <colgroup><col width="130px"><col width="auto"></colgroup>
    <tr><td class="formlabel">First Name:</td><td class="text"><bean:write name="<%=Constants.USER_KEY%>" property="firstname"/></td></tr>
    <tr><td class="formlabel">Last Name:</td><td class="text"><bean:write name="<%=Constants.USER_KEY%>" property="lastname"/></td></tr>
    <tr><td class="formlabel">Email:</td><td class="text"><bean:write name="<%=Constants.USER_KEY%>" property="email"/></td></tr>
    <tr><td class="formlabel">Phone:</td><td class="text"><bean:write name="<%=Constants.USER_KEY%>" property="phone"/></td></tr>

</table>
</div>
<div id="aside">
<table border="0">
    <colgroup><col width="130px"><col width="auto"></colgroup>    
    <tr><td class="formlabel">Institution:</td><td class="text"><bean:write name="<%=Constants.USER_KEY%>" property="institution"/></td></tr>    
    <tr><td class="formlabel">PI Name:</td><td class="text"><bean:write name="<%=Constants.USER_KEY%>" property="piname"/></td></tr>
    <tr><td class="formlabel">PI Email:</td><td class="text"><bean:write name="<%=Constants.USER_KEY%>" property="piemail"/></td></tr>
    <tr><td class="formlabel">Group:</td><td class="text"><bean:write name="<%=Constants.USER_KEY%>" property="group"/></td></tr>
    <tr><td class="formlabel">DF/HCC Member:</td><td class="text"><bean:write name="<%=Constants.USER_KEY%>" property="ismemberString"/></td></tr>
</table>
</div>
<div id="content">
        <li class="text"><a href="PrepareRegistration.do?update=true&first=true">Update Account: </a>Change account information</li>
        <li class="text"><a href="ViewOrderHistory.do">View Orders: </a>View complete order list</li>
        <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
        <li class="text"><a href="ViewContainers.jsp">View Containers: </a>View plasmid information by containers</li> 
        <li class="text"><a href="SearchOrderInput.jsp">Search Orders: </a>Search clone orders by orderid, user last name, order date, shipping date, and organization</li>
        </logic:equal>
</div>
    <jsp:include page="footer.jsp" /></body></div>
</html>

