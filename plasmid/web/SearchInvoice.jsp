<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.Invoice" %>

<html>
    <head>
        <title>PlasmID Database</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">	
        <script language="JavaScript" src="calendar2.js"></script>
    </head>
    
    <body>
        <jsp:include page="homeTitle.jsp" />
        <table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="menuHome.jsp" />
                </td>
                <td width="83%" align="left" valign="top">
                    <jsp:include page="invoiceTitle.jsp" />
                    
                    <P class="formlabel">Search Invoice</P>
                    <html:form action="SearchInvoice.do">
                        <table width="100%" border="0">
                            <tr>
                                <td class="formlabel">Invoice Number (separate each invoice by comma [,]):</td>
                                <td colspan="4" class="itemtext"><html:text property="invoicenums" size="50"/></td>
                            </tr>
                            <tr></tr>
                            <tr>
                                <td class="formlabel">Invoice Date (mm/dd/yyyy):</td>
                                <td class="formlabel">From</td>
                                <td class="itemtext"><html:text property="invoiceDateFrom"/>&nbsp;<a href="javascript:cal7.popup();"><img src="img/cal.gif" width="16" height="16" border="0" alt="Click Here to Pick up the date"></a></td>
                                <td class="formlabel">To</td>
                                <td class="itemtext"><html:text property="invoiceDateTo"/>&nbsp;<a href="javascript:cal8.popup();"><img src="img/cal.gif" width="16" height="16" border="0" alt="Click Here to Pick up the date"></a></td>
                            </tr>
                            <tr>
                                <td class="formlabel">&nbsp;</td>
                                <td class="formlabel">Month</td>
                                <td class="itemtext"><html:select property="invoiceMonth">
                                        <html:option value="">-- Please Select --</html:option>
                                        <html:option value="January"/>
                                        <html:option value="Feburary"/>
                                        <html:option value="March"/>
                                        <html:option value="April"/>
                                        <html:option value="May"/>
                                        <html:option value="June"/>
                                        <html:option value="July"/>
                                        <html:option value="August"/>
                                        <html:option value="September"/>
                                        <html:option value="October"/>
                                        <html:option value="November"/>
                                        <html:option value="December"/>
                                    </html:select>
                                </td>
                                <td class="formlabel">Year</td>
                                <td class="itemtext"><html:select property="invoiceYear">
                                        <html:option value="">-- Please Select --</html:option>
                                        <html:option value="2011"/>
                                        <html:option value="2012"/>
                                        <html:option value="2013"/>
                                        <html:option value="2014"/>
                                        <html:option value="2015"/>
                                        <html:option value="2016"/>
                                        <html:option value="2017"/>
                                        <html:option value="2018"/>
                                        <html:option value="2019"/>
                                        <html:option value="2020"/>
                                        <html:option value="2021"/>
                                    </html:select>
                                </td>
                            </tr>
                            <tr></tr>
                            <tr>
                                <td class="formlabel">PI Last Name (separate each name by comma [,]):</td>
                                <td colspan="4" class="itemtext"><html:text property="pinames" size="50"/></td>
                            </tr>
                            <tr>
                                <td class="formlabel">PO/Billing Number (separate each number by comma [,]):</td>
                                <td colspan="4" class="itemtext"><html:text property="ponumbers" size="50"/></td>
                            </tr>
                            <tr>
                                <td class="formlabel">Payment Status:</td>
                                <td colspan="4">
                                    <html:select styleClass="itemtext" property="pstatus">
                                        <html:option value="<%=Constants.ALL%>"/>
                                        <html:option value="<%=Invoice.PAYMENTSTATUS_PAID%>"/>
                                        <html:option value="<%=Invoice.PAYMENTSTATUS_UNPAID%>"/>
                                        <html:option value="<%=Invoice.PAYMENTSTATUS_PARTIAL%>"/>
                                    </html:select>
                                </td>
                            </tr>
                            <tr>
                                <td class="formlabel">Harvard Univeristy:</td>
                                <td colspan="4">
                                    <html:select styleClass="itemtext" property="isinternal">
                                        <html:option value="<%=Constants.ALL%>"/>
                                        <html:option value="<%=Constants.YES%>"/>
                                        <html:option value="<%=Constants.NO%>"/>
                                    </html:select>
                                </td>
                            </tr>
                            <tr> 
                                <td  class="formlabel">*PI Institution or Company Name:</td>
                                <td colspan="4"> 
                                    <table width="100%" border="0" bordercolor="#000000">
                                        <tr>
                                            <td class="formlabel" colspan="2">Expedited MTA Member:</td>
                                        </tr>
                                        <tr>
                                            <td class="text" colspan="2">
                                                <html:select property="institution1" styleClass="text">
                                                    <html:option value="">-- Please Select --</html:option>
                                                    <html:options name="members"/>
                                                </html:select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="formlabel" colspan="2">Non Member:</td>
                                        </tr>
                                        <tr>
                                            <td class="text" colspan="2">
                                                <html:select property="institution2" styleClass="text">
                                                    <html:option value="">---------- US Institutions ----------</html:option>
                                                    <html:options name="us"/>
                                                    <html:option value="">---------- Government ----------</html:option>
                                                    <html:options name="government"/>
                                                    <html:option value="">---------- International Institutions ----------</html:option>
                                                    <html:options name="international"/>
                                                    <html:option value="">---------- Companies ----------</html:option>
                                                    <html:options name="company"/>
                                                </html:select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>&nbsp;</td>
                                            <td colspan="4" class="formlabel"><html:submit value="Search"/></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </html:form>
			<script language="JavaScript">
			<!-- // create calendar object(s) just after form tag closed
				 // specify form element as the only parameter (document.forms['formname'].elements['inputname']);
				 // note: you can have as many calendar objects as you need for your application
				var cal7 = new calendar2(document.forms['searchInvoiceForm'].elements['invoiceDateFrom']);
				cal7.year_scroll = true;
				cal7.time_comp = false;
				var cal8 = new calendar2(document.forms['searchInvoiceForm'].elements['invoiceDateTo']);
				cal8.year_scroll = true;
				cal8.time_comp = false;
			//-->
			</script>
                    
                </td>
            </tr>
        </table>
    <jsp:include page="footer.jsp" /></body>
    <HEAD>
        <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
    </HEAD>
</html>
