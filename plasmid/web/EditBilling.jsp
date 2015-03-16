<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.User" %> 

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
        <table id='content' width="100%" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="menuHome.jsp" />
                </td>--%>
                <td width="100%" align="left" valign="top">
                    <%--<jsp:include page="orderHistoryTitle.jsp" />--%>
                    
                    <html:errors/>
                    
                    <html:form action="UpdateBilling.do">
                        <html:hidden property="orderid"/>
                        <table border="0">
                            <tr> 
                                <td colspan="2" class="featuretext">Edit Billing Address:</td>
                            </tr>
                            <tr> 
                                <td class="formlabel">*Name(Accounts Payable or Grant manager):</td>
                                <td><html:text size="50" styleClass="text" property="billingto"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel">Organization:</td>
                                <td><html:text size="50" styleClass="text" property="billingorganization"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel">*Street:</td>
                                <td><html:text size="50" styleClass="text" property="billingaddressline1"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel">Street (continued):</td>
                                <td><html:text size="50" styleClass="text" property="billingaddressline2"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel">*City:</td>
                                <td><html:text size="50" styleClass="text" property="billingcity"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel">*State:</td>
                                <td><html:text size="50" styleClass="text" property="billingstate"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel">*Zip code:</td>
                                <td><html:text size="50" styleClass="text" property="billingzipcode"/></td>
                            </tr>
                            <tr>
                                <td class="formlabel">*Country:</td>
                                <td><html:select styleClass="text" property="billingcountry">
                                        <html:options name="countryList"/>
                                    </html:select>
                                </td>
                            </tr>
                            <tr> 
                                <td class="formlabel">*Phone:</td>
                                <td><html:text size="50" styleClass="text" property="billingphone"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel">*Fax:</td>
                                <td><html:text size="50" styleClass="text" property="billingfax"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel">*Email (Accounts Payable or Grant manager):</td>
                                <td><html:text size="50" styleClass="text" property="billingemail"/></td>
                            </tr>
                        </table>
                        
                        <p>&nbsp;</p>
                        <html:submit value="Update"/>
                    </html:form>
                    
                </td>
            </tr>
        </table>
    <jsp:include page="footer.jsp" /></body>
     </div>
</html>
