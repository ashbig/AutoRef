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
	<jsp:include page="orderHistoryTitle.jsp" />

<p class="text">Search Orders</P>
<html:form action="SearchOrderInput.do" focus="orderid">
<table width="100%" border="0">
  <tr>
    <td class="formlabel">Order ID (separate each ID by comma [,]):</td>
    <td colspan="4" class="itemtext"><html:text property="orderid" size="50"/></td>
  </tr>
  <tr></tr>
  <tr>
    <td class="formlabel">Select Order Date (mm/dd/yyyy):</td>
    <td class="formlabel">From</td>
    <td class="itemtext"><html:text property="orderDateFrom"/>&nbsp;<a href="javascript:cal5.popup();"><img src="img/cal.gif" width="16" height="16" border="0" alt="Click Here to Pick up the date"></a></td>
    <td class="formlabel">To</td>
    <td class="itemtext"><html:text property="orderDateTo"/>&nbsp;<a href="javascript:cal6.popup();"><img src="img/cal.gif" width="16" height="16" border="0" alt="Click Here to Pick up the date"></a></td>
  </tr>
  <tr>
    <td class="formlabel">Select Shipping Date (mm/dd/yyyy):</td>
    <td class="formlabel">From</td>
    <td class="itemtext"><html:text property="shippingDateFrom"/>&nbsp;<a href="javascript:cal7.popup();"><img src="img/cal.gif" width="16" height="16" border="0" alt="Click Here to Pick up the date"></a></td>
    <td class="formlabel">To</td>
    <td class="itemtext"><html:text property="shippingDateTo"/>&nbsp;<a href="javascript:cal8.popup();"><img src="img/cal.gif" width="16" height="16" border="0" alt="Click Here to Pick up the date"></a></td>
  </tr>
  <tr></tr>
  <tr>
    <td class="formlabel">Order Status:</td>
    <td colspan="4">
        <html:select styleClass="itemtext" property="status">
            <html:option value="<%=CloneOrder.ALL%>"/>
            <html:option value="<%=CloneOrder.PENDING%>"/>
            <html:option value="<%=CloneOrder.PENDING_MTA%>"/>
            <html:option value="<%=CloneOrder.PENDING_PAYMENT%>"/>
            <html:option value="<%=CloneOrder.TROUBLESHOOTING%>"/>
            <html:option value="<%=CloneOrder.INPROCESS%>"/>
            <html:option value="<%=CloneOrder.SHIPPED%>"/>
            <html:option value="<%=CloneOrder.CANCEL%>"/>
        </html:select>
    </td>
  </tr>
  <tr>
    <td class="formlabel">User Last Name (separate each ID by comma [,]):</td>
    <td colspan="4" class="itemtext"><html:text property="lastName"/></td>
  </tr>
  <tr>
    <td class="formlabel">Organization:</td>
    <td colspan="4">
        <html:select styleClass="itemtext" property="organization">
            <html:option value="<%=Constants.ALL%>"/>
            <html:option value="<%=Constants.MEMBER%>"/>
            <html:option value="<%=Constants.MTAMEMBER%>"/>
        </html:select>
    </td>
  </tr>
  <tr>
    <td class="formlabel">Sort By:</td>
    <td colspan="4">
        <html:select styleClass="itemtext" property="sort">
            <html:option value="<%=Constants.SORTBY_ORDERID%>"/>
            <html:option value="<%=Constants.SORTBY_USERNAME%>"/>
            <html:option value="<%=Constants.SORTBY_ORDERDATE%>"/>
            <html:option value="<%=Constants.SORTBY_SHIPDATE%>"/>
            <html:option value="<%=Constants.SORTBY_STATUS%>"/>
        </html:select>
    </td>
  </tr>
  <tr>
    <td class="formlabel">Clone provider:</td>
    <td colspan="4">
        <html:select styleClass="itemtext" property="cloneProvider">
            <html:option value="<%=Constants.ALL%>"/>
            <html:option value="<%=Constants.PSI%>"/>
            <html:option value="<%=Constants.PSI_CESG%>"/>
            <html:option value="<%=Constants.PSI_NYSGX%>"/>
            <html:option value="<%=Constants.PSI_ATCG%>"/>
            <html:option value="<%=Constants.PSI_BSGC%>"/>
            <html:option value="<%=Constants.PSI_CHTSB%>"/>
            <html:option value="<%=Constants.PSI_CSMP%>"/>
            <html:option value="<%=Constants.PSI_ICSFI%>"/>
            <html:option value="<%=Constants.PSI_JCSG%>"/>
            <html:option value="<%=Constants.PSI_MCSG%>"/>
            <html:option value="<%=Constants.PSI_NSGC%>"/>
            <html:option value="<%=Constants.PSI_NYCMPS%>"/>
            <html:option value="<%=Constants.PSI_SCSG%>"/>
            <html:option value="<%=Constants.PSI_SGPP%>"/>
        </html:select>
    </td>
  </tr>
  <tr></tr>
  <tr>
    <td>&nbsp;</td>
    <td colspan="4" class="formlabel"><html:submit value="Search"/></td>
  </tr>
</table>
</html:form>
			<script language="JavaScript">
			<!-- // create calendar object(s) just after form tag closed
				 // specify form element as the only parameter (document.forms['formname'].elements['inputname']);
				 // note: you can have as many calendar objects as you need for your application

				var cal5 = new calendar2(document.forms['searchOrderForm'].elements['orderDateFrom']);
				cal5.year_scroll = true;
				cal5.time_comp = false;
				var cal6 = new calendar2(document.forms['searchOrderForm'].elements['orderDateTo']);
				cal6.year_scroll = true;
				cal6.time_comp = false;
				var cal7 = new calendar2(document.forms['searchOrderForm'].elements['shippingDateFrom']);
				cal7.year_scroll = true;
				cal7.time_comp = false;
				var cal8 = new calendar2(document.forms['searchOrderForm'].elements['shippingDateTo']);
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
