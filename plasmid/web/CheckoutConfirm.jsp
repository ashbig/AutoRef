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
        <jsp:include page="orderTitle.jsp" />
        <table width="100%" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="menu.jsp" />
                </td>--%>
                <td width="100%" align="left" valign="top">
                    <%--<jsp:include page="checkoutTitle.jsp" />--%>
                    
                    <html:errors/>
                    <table width="100%" border="0">
                        <tr> 
                            <td width="12%" class="formlabel">Name:</td>
                            <td colspan="3" class="text"><bean:write name="<%=Constants.USER_KEY%>" property="username"/></td>
                        </tr>
                        <tr> 
                            <td width="12%" class="formlabel">Email:</td>
                            <td width="40%" class="text"><bean:write name="<%=Constants.USER_KEY%>" property="email"/></td>
                            <td width="12%" class="formlabel">Phone:</td>
                            <td width="36%" class="text"><bean:write name="<%=Constants.USER_KEY%>" property="phone"/></td>
                        </tr>
                        <tr> 
                            <td width="12%" height="24" class="formlabel">Order Date:</td>
                            <td width="40%" class="text"><bean:write name="date"/></td>
                        </tr>
                        <tr> 
                            <td width="12%" height="24" class="formlabel">Shipping Method:</td>
                            <td width="40%" class="text"><bean:write name="checkoutForm" property="shippingMethod"/></td>
                            <td width="12%" class="formlabel">Shipping Account Number:</td>
                            <td width="36%" class="text">
                                <bean:write name="checkoutForm" property="accountNumber"/>
                            </td>
                        </tr>
                    </table>
                    <table width="100%" border="0">
                        <tr> 
                            <td colspan="2" class="featuretext">Shipping Address:</td>
                            <td colspan="2" class="featuretext">Billing Address:</td>
                        </tr>
                        <tr> 
                            <td width="17%" class="formlabel">Name:</td>
                            <td class="text"><bean:write name="checkoutForm" property="shippingto"/></td>
                            <td class="formlabel">Name:</td>
                            <td class="text"><bean:write name="checkoutForm" property="billingto"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">Organization:</td>
                            <td class="text"><bean:write name="checkoutForm" property="organization"/></td>
                            <td class="formlabel">Organization:</td>
                            <td class="text"><bean:write name="checkoutForm" property="billingorganization"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">Street:</td>
                            <td class="text"><bean:write name="checkoutForm" property="addressline1"/></td>
                            <td class="formlabel">Street:</td>
                            <td class="text"><bean:write name="checkoutForm" property="billingaddressline1"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">Street (continued):</td>
                            <td class="text"><bean:write name="checkoutForm" property="addressline2"/></td>
                            <td class="formlabel">Street (continued):</td>
                            <td class="text"><bean:write name="checkoutForm" property="billingaddressline2"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">City:</td>
                            <td class="text"><bean:write name="checkoutForm" property="city"/></td>
                            <td class="formlabel">City:</td>
                            <td class="text"><bean:write name="checkoutForm" property="billingcity"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">State:</td>
                            <td class="text"><bean:write name="checkoutForm" property="state"/></td>
                            <td class="formlabel">State:</td>
                            <td class="text"><bean:write name="checkoutForm" property="billingstate"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">Zip code:</td>
                            <td class="text"><bean:write name="checkoutForm" property="zipcode"/></td>
                            <td class="formlabel">Zip code:</td>
                            <td class="text"><bean:write name="checkoutForm"  property="billingzipcode"/></td>
                        </tr>
                        <tr>
                            <td class="formlabel">Country:</td>
                            <td class="text"><bean:write name="checkoutForm" property="country"/></td>
                            <td class="formlabel">Country:</td>
                            <td class="text"><bean:write name="checkoutForm" property="billingcountry"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">Phone:</td>
                            <td class="text"><bean:write name="checkoutForm" property="phone"/></td>
                            <td class="formlabel">Phone:</td>
                            <td class="text"><bean:write name="checkoutForm"  property="billingphone"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">Fax:</td>
                            <td class="text"><bean:write name="checkoutForm" property="fax"/></td>
                            <td class="formlabel">Fax:</td>
                            <td class="text"><bean:write name="checkoutForm"  property="billingfax"/></td>
                        </tr>
                        <tr> 
                            <td >&nbsp;</td>
                            <td >&nbsp;</td>
                            <td class="formlabel">Email:</td>
                            <td class="text"><bean:write name="checkoutForm"  property="billingemail"/></td>
                        </tr>
                    </table>
                    
                    <p>&nbsp;</p>
                    
                    <p class="text">Order Information:</p>
                    <table width="100%" border="0">
                        <tr> 
                            <td class="tableheader">Item</td>
                            <td class="tableheader">Quantity</td>
                            <td class="tableheader">Price</td>
                        </tr>
                        <tr> 
                            <td class="tablebody">Number of clones:</td>
                            <td class="tablebody"><bean:write name="checkoutForm" property="numOfClones"/></td>
                            <td align="right" class="tablebody">$<bean:write name="checkoutForm" property="costOfClones"/></td>
                        </tr> 
                        <tr> 
                            <td class="tablebody">Number of collections:</td>
                            <td class="tablebody"><bean:write name="checkoutForm" property="numOfCollections"/></td>
                            <td align="right" class="tablebody">$<bean:write name="checkoutForm" property="costOfCollections"/></td>
                        </tr>  
                        <tr> 
                            <td class="tablebody">Platinum service:</td>
                            <td align="right" colspan="2" class="tablebody">$<bean:write name="checkoutForm" property="costOfPlatinum"/></td>
                        </tr> 
                        <tr> 
                            <td class="tablebody">Shipping:</td>
                            <td align="right" colspan="2" class="tablebody">$<bean:write name="checkoutForm" property="costForShipping"/></td>
                        </tr> 
                        <tr> 
                            <td class="tableheader">Total price:</td>
                            <td align="right" colspan="2" class="tablebody">$<bean:write name="checkoutForm" property="totalPrice"/></td>
                        </tr>
                    </table>
                    
                    <html:form action="ChoosePayment.do">
                        <logic:equal name="<%=Constants.USER_KEY%>" property="internalMember" value="true">
                            <table>
                                <tr>
                                    <td class="formlabel">Please enter 33 digit billing code:</td>
                                </tr>
                                <tr>
                                    <td class="text">
                                        <html:text size="3" maxlength="3" property="billing1"/> -
                                        <html:text size="5" maxlength="5" property="billing2"/> -
                                        <html:text size="4" maxlength="4" property="billing3"/> -
                                        <html:text size="6" maxlength="6" property="billing4"/> -
                                        <html:text size="6" maxlength="6" property="billing5"/> -
                                        <html:text size="4" maxlength="4" property="billing6"/> -
                                        <html:text size="5" maxlength="5" property="billing7"/>
                                    </td>
                                </tr>
                            </table>
                        </logic:equal>
                        <logic:notEqual name="<%=Constants.USER_KEY%>" property="internalMember" value="true"><p class="mainbodytexthead">
                                <b>Pay Using a Credit Card </b>
                            </p>
                            <p class="mainbodytexthead">
                                Click the button to pay by credit card through the PayPal site. You DO NOT need to create a PayPal account to pay by credit card: see the bottom right-hand side of the PayPal page for a link to pay by credit card WITHOUT signing in or registering.
                            You will be returned to PlasmID to complete your request.</p> 
                            <p class="text">Please confirm the following information (use back button to make changes):</P>
                            <table>
                                <tr>
                                    <td colspan="3" class="alertbig">
                                        **ATTENTION: Customers paying through wire transfer please be sure to cover transferring bank fees and third-party bank fees where applicable. Thank you.
                                    </td>
                                </tr>
                                <tr>
                                    <td width="30%" class="formlabel">Please choose payment method:</td>
                                    <td width="30%" class="text"><html:radio property="paymentmethod" value="<%=Constants.PAYPAL%>"/><img src="credit_card.jpg"/></td>
                                    <td class="text"><html:radio property="paymentmethod" value="<%=Constants.PO%>"/><%=Constants.PO%></td>
                                </tr>
                                <tr>
                                    <td class="formlabel">*PO Number:</td>
                                    <td colspan="2" class="text">
                                        <html:text maxlength="50" property="ponumber" size="40"/>
                                    </td>
                                </tr>
                            </table>
                        </logic:notEqual>
                        <html:submit value="Place Order"/>
                    </html:form>
                    
                </td>
            </tr>
        </table>
    <jsp:include page="footer.jsp" /></body>
     </div>
</html>
