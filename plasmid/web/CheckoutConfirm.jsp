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
        <div id="noprint">
                <html:errors/>
                    <table width="100%" border="0" style='display:none;'>
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
                    <html:form action="ChoosePayment.do">
                        <logic:equal name="<%=Constants.USER_KEY%>" property="internalMember" value="true">
                            <p class="mainbodytexthead">On Campus requests must be paid using a valid 33 digit code. You or your grant manager can obtain this from the HCOM system. If you are at a Harvard affiliated institution and do not have a 33 digit code you must <a href="PrepareRegistration.do?update=true&first=true">update your account</a> to 'Harvard affiliate' before you can checkout.</p>
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
                            <table>
                                <tr><td width="50%" class="formlabel">How would you like to pay?</td></tr>
                                <tr><td width="50%" class="text"><html:radio property="paymentmethod" value="<%=Constants.PAYPAL%>"/> Credit Card</td></tr>
                                <tr><td width="50%" class="text" id="PO" class="formlabel"><html:radio property="paymentmethod" value="<%=Constants.PO%>"/> I have a <%=Constants.PO%>&nbsp;*
                                &nbsp;<html:text maxlength="50" property="ponumber" size="40"/>&nbsp;</td>
                                </tr>
                            </table>
                        </logic:notEqual>
                        <html:submit value="Place Order"/>
                        </html:form>                       
                        <p class="mainbodytext">* Still need to request a PO? Print out this <a href="javascript:if(window.print)window.print()" style='text-decoration:none;'>Price Quote</a> for your finance folks, then be sure to <a href="ViewCart.do">View Your Cart</a> and hit save. You can come back and complete your order once you have the PO number.</p><br>
</div>                    
                        <table border="0" cellpadding="5" style="table{border-collapse: collapse;}"><tr><td>                    
                    <p class="text">Harvard Medical School<br>Dept. BCMP, C1-214<br>240 Longwood Ave.<br>Boston, MA 02115</p>
                    <p class="text" align="middle"><strong>PRICE QUOTE</strong></p>
                        <table border="0">
                            <tr><td>DATE:</td><td><bean:write name="date"/></td><td>(Quote valid for 30 days)</td></tr>
                        </table>
                        <table>
                            <tr><td>&nbsp;</td></tr>
                            <tr><td>REQUESTED BY: <bean:write name="<%=Constants.USER_KEY%>" property="username"/></td></tr>
                            <tr><td>EMAIL: <bean:write name="<%=Constants.USER_KEY%>" property="email"/></td></tr>
                            <tr><td>PHONE: <bean:write name="checkoutForm" property="phone"/></td></tr>
                            <tr><td>FAX: <bean:write name="checkoutForm" property="fax"/></td></tr>
                            <tr><td>&nbsp;</td></tr>
                            <tr><td>DELIVERY ADDRESS:</td></tr>
                            <tr><td><bean:write name="checkoutForm" property="shippingto"/></td></tr>
                            <tr><td><bean:write name="checkoutForm" property="organization"/></td></tr>
                            <tr><td><bean:write name="checkoutForm" property="addressline1"/></td></tr>
                            <tr><td><bean:write name="checkoutForm" property="addressline2"/></td></tr>
                            <tr><td><bean:write name="checkoutForm" property="city"/>, <bean:write name="checkoutForm" property="state"/> <bean:write name="checkoutForm" property="zipcode"/></td></tr>
                            <tr><td><bean:write name="checkoutForm" property="country"/></td></tr>
                        </table>
                        <table width="100%" border="0">
                                                <colgroup>
                                                    <col width="100px">
                                                    <col width="auto">                            
                                                    <col width="auto">
                                                    <col width="auto">                            
                                                </colgroup>
                                                <tr> 
                                                    <th class="tableheader">Line</th>
                                                    <th align="left" class="tableheader">Item</th>
                                                    <th class="tableheader">Quantity</th>
                                                    <th align="right" class="tableheader">Subtotal</th>
                                                </tr>
                                                <tr> 
                                                    <td align="middle" class="tablebody">1</td>
                                                    <td align="left" class="tablebody">Individual Plasmid Retrieval</td>
                                                    <td align="middle" class="tablebody"><bean:write name="checkoutForm" property="numOfClones"/></td>
                                                    <td align="right" class="tablebody">$<bean:write name="checkoutForm" property="costOfClones"/></td>
                                                </tr> 
                                                <tr> 
                                                    <td align="middle" class="tablebody">2</td>
                                                    <td align="left" class="tablebody">Plasmid Collection Retrieval</td>
                                                    <td align="middle" class="tablebody"><bean:write name="checkoutForm" property="numOfCollections"/></td>
                                                    <td align="right" class="tablebody">$<bean:write name="checkoutForm" property="costOfCollections"/></td>
                                                </tr>  
                                                <%--<tr> 
                                                    <td class="tablebody">Platinum service:</td>
                                                    <td align="right" colspan="2" class="tablebody">$<bean:write name="checkoutForm" property="costOfPlatinum"/></td>
                                                </tr> --%>
                                                <tr> 
                                                    <td align="middle" class="tablebody">3</td>
                                                    <td align="left" class="tablebody">Shipping</td>
                                                    <td align="right" colspan="2" class="tablebody">$<bean:write name="checkoutForm" property="costForShipping"/></td>
                                                </tr> 
                                                <tr> 
                                                    <td align="middle" class="tableheader">Total price:</td>
                                                    <td align="right" colspan="3" class="tablebody">$<bean:write name="checkoutForm" property="totalPrice"/></td>
                                                </tr>
                        </table>
                                                <p>Conditions:<br>
                                                    - All orders must be placed online via a registered account<br>
                                                      &nbsp;&nbsp;(http://plasmid.med.harvard.edu)<br>
                                                    - Payment allowed by credit card or PO<br>
                                                    - Final payment of PO: NET 30, check or wire transfer, wire transfer fees to be paid by recipient<br>
                                                    - Estimated delivery = 7-10 business days from order</p>
                                                <p>PLEASE NOTE: We are the Dana Farber/ Harvard Cancer Center at Harvard Medical School and would prefer checks payable to <u><strong>HARVARD MEDICAL SCHOOL</strong></u>.</p>
        <p>Please feel free to contact us with any questions or concerns at <a href="mailto:plasmidhelp@hms.harvard.edu">plasmidhelp@hms.harvard.edu</a></p>
                </td></tr></table>
                        
                        </td></tr> 

        </table>
    <jsp:include page="footer.jsp" /></body>
     </div>
</html>
