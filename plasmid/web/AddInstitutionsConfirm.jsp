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
        <link href="layout.css" rel="stylesheet" type="text/css" />
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
                <td width="100%" align="left" valign="top">
                    <jsp:include page="addInstitutionsTitle.jsp" />
                    <html:form action="AddInstitutionsConfirm.do">
                        <html:errors/>
                        <p class="formlabel">The following institutions will be added to the database:</p>
                        <table width="100%" height="118" border="0" align="center">
                            <tr>
                                <td class="tableheader">Institution</td>
                                <td class="tableheader">Category</td>
                                <td class="tableheader">Country</td>
                            </tr>
                            <logic:iterate name="institutionObjects" id="it">
                                <tr>
                                    <td class="tableinfo"><bean:write name="it" property="name"/></td>
                                    <td class="tableinfo"><bean:write name="it" property="category"/></td>
                                    <td class="tableinfo"><bean:write name="it" property="country"/></td>
                                </tr>
                            </logic:iterate>
                        </table>
                        <html:submit value="Submit"/>
                </html:form></td>
            </tr>
        </table>
    <jsp:include page="footer.jsp" /></body>
        </div>
</html>

