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

                    <table width="100%" border="0">
                        <tr> 
                            <td class="formlabel">Order ID:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="orderid"/></td>
                            <td class="formlabel">Order Date:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="orderDate"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">Email:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="email"/></td>
                            <td class="formlabel">Phone:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="phone"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">PO/Billing Number:</td>
                            <td colspan="3" class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="ponumber"/></td>
                        </tr>
                    </table>

                    <p class="text">Check shipped items:</p>
                    <html:form action="PrintPackingSlip.do">
                        <table width="100%" border="0">
                            <tr> 
                                <td class="tableheader">&nbsp;</td>
                                <td class="tableheader">Item</td>
                                <td class="tableheader">Quantity</td>
                                <td class="tableheader">Platinum Result</td>
                            </tr>
                            <logic:iterate name="<%=Constants.CLONEORDER%>" property="clones" id="clone">
                                <tr> 
                                    <td class="tablebody">
                                        <html:multibox property="shipped"><bean:write name="clone" property="cloneid"/></html:multibox>
                                    </td>
                                    <td class="tablebody"><bean:write name="clone" property="clonename"/></td>
                                    <td class="tablebody"><bean:write name="clone" property="quantity"/></td>
                                    <logic:present name="clone" property="validation">
                                    <td class="tablebody"><bean:write name="clone" property="validation.result"/></td>
                                    </logic:present>
                                    <logic:notPresent name="clone" property="validation">
                                        <td class="tablebody">NA</td>
                                    </logic:notPresent>
                                </tr> 
                            </logic:iterate>
                        </table>
                        <html:submit value="Generate Packing Slip"/>
                    </html:form>

                </td>
            </tr>
        </table>
        <jsp:include page="footer.jsp" /></body>
</html>
