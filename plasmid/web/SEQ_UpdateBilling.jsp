<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.User" %>
<%@ page import="plasmid.coreobject.CloneOrder" %>

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
        <jsp:include page="signinMenuBar.jsp" />
        <table width="100%" id='content' border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="seq_menuHome.jsp" />
                </td>--%>
                <td width="83%" align="left" valign="top">
                    <%--<jsp:include page="seq_invoiceTitle.jsp" />--%>

                    <p>
                        <logic:present name="ordermessage">
                        <p class="text"><bean:write name="ordermessage"/></p>
                    </logic:present>
                    <html:errors/>

                    <html:form action="SEQ_UpdateBillingContinue.do">
                        <table width="100%" border="0">
                            <tr> 
                                <td class="formlabel">Order ID:</td>
                                <td class="text"><bean:write name="seqorder" property="orderid"/></td>
                                <td class="formlabel">Order Date:</td>
                                <td class="text"><bean:write name="seqorder" property="orderdate"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel">User:</td>
                                <td class="text"><bean:write name="seqorder" property="username"/></td>
                                <td class="formlabel">Samples:</td>
                                <td class="text"><bean:write name="seqorder" property="samples"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel">Cost:</td>
                                <td class="text"><bean:write name="seqorder" property="cost"/></td>
                                <td class="formlabel">PI Email:</td>
                                <td class="text"><bean:write name="seqorder" property="piemail"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel">Affiliation:</td>
                                <td class="text"><bean:write name="seqorder" property="affiliation"/></td>
                                <td class="formlabel">&nbsp;</td>
                                <td class="text">&nbsp;</td>
                            </tr>
                            <tr> 
                                <td class="formlabel">Billing Address:</td>
                                <td colspan="3" class="text"><html:text size="80" name="seqorder" property="billingaddress"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel">Billing Email:</td>
                                <td colspan="3" class="text"><html:text size="80" name="seqorder" property="billingemail"/></td>
                            </tr>
                            <tr> 
                                <td>&nbsp;</td>
                                <td class="text"><html:submit styleClass="text" value="Save"/></td>                          
                                <td>&nbsp;</td>
                            </tr>
                        </table>
                                <html:hidden name="seqorder" property="orderid"/>
                                <html:hidden name="seqorder" property="invoiceid"/>
                    </html:form>

                </td>
            </tr>
        </table>
        <jsp:include page="footer.jsp" /></body>
</div>
</html>
