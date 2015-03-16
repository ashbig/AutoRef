<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.CloneOrder" %>
<%@ page import="plasmid.coreobject.User" %>

<html>
    <head>
        <title>PlasmID Database</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
    <link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
        <link href="boilerplate.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="respond.min.js"></script>
</head>
     <div class="gridContainer clearfix">

    
    <body>
        <jsp:include page="signinMenuBar.jsp" />
        <table id='content' width="100%" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="menuHome.jsp" />
                </td>--%>
                <td width="83%" align="left" valign="top">
                    <%--<jsp:include page="orderHistoryTitle.jsp" />--%>
                    
                    <p class="text">Orders have been processed.</p>
                    <logic:equal value="true" name="changeOrderStatusForm" property="isDownload">
                        <html:form action="DownloadCloneWorklist.do">
                            <html:submit value="Download Clone Worklist" styleClass="itemtext"/>
                        </html:form>
                    </logic:equal>
                </td>
            </tr>
        </table>
    <jsp:include page="footer.jsp" /></body>
    <HEAD>
        <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
    </HEAD>
     </div>
</html>
