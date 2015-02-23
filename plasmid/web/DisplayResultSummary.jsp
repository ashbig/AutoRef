<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.RefseqNameType" %> 
<%@ page import="plasmid.form.RefseqSearchForm" %> 
<%@ page import="plasmid.coreobject.Clone" %> 
<%@ page import="plasmid.coreobject.User" %> 
<%@ page import="plasmid.process.OrderProcessManager" %> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>PlasmID Database</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
 <link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
        <link href="boilerplate.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="respond.min.js"></script>
</head>
     <div class="gridContainer clearfix">

    
    <body>
        <jsp:include page="orderTitle.jsp" />
        <table id='content' width="100%" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <%--<td width="136" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="menu.jsp" />
                </td>--%>
                <td width="100%" align="left" valign="top">
                    <%--<jsp:include page="searchByRefseqTitle.jsp" />--%>
                    <p class="text">Your search has found more than 1000 clones. Please limit your search to be less than 1000 clones.</p>
                </td>
            </tr>
        </table>
    <jsp:include page="footer.jsp" /></body>
    </div>
</html>

