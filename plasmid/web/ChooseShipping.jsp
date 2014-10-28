<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 

<html>
<head>
<title>PlasmID Database</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
<script>
    function showMsg() {
        alert("WARNING!\n\nAre you sure you want to remove QC testing?\nWe did not create these bacterial stocks and make no warranty for their identity or fitness. ");
    }
</script>
</head>

<body>
<jsp:include page="orderTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menu.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
	<jsp:include page="checkoutTitle.jsp" />

<html:errors/>

<p class="mainbodytexthead">
          <i>If you do not want to complete your order now, you can save your cart and return to check-out another time (remember to sign in to see your saved cart).</i>
</p>
<p class="mainbodytexthead">
              If you have any questions, please contact <a href="mailto:plasmidhelp@hms.harvard.edu">PlasmID help</a>.   
</p>

<html:form action="EnterAddress.do">

<p class="text">Order Summary:</p>
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
    <td class="tableheader">Total price (before shipping charge):</td>
    <td align="right" colspan="2" class="tablebody">$<bean:write name="checkoutForm" property="totalPrice"/></td>
  </tr>
</table>

<p class="mainbodytexthead">
              Shipping Options:
              <ol class="mainbodytexthead">
                  <li>Include a FedEx account number at check-out and we will charge your account.</li>
                  <li>If you don't provide a FedEx account number, we will apply a standard shipping charge at check-out.</li>
                  <ul>  
                      <li>$10.00 for domestic orders</li>
                      <li>$20.00 for international orders</li>
                </ul>
                <li>No charge for shipping for the Harvard Medical School community. Pick up your order in the Seeley G. Mudd Building in the <a href="http://www.hip.harvard.edu/Location.html#Quad" target="_blank">HMS Quad</a> in the second floor hallway freezer. You MUST have access to the building to take advantage of this option.</li>
      </ol>
</p>

<table width="100%" border="0">
  <tr> 
    <td class="formlabel" width="20%">Choose shipping method:</td>
    <td class="text">
     <td class="text">
        <html:select property="shippingMethod">
            <html:options name="shippingMethods"/>
        </html:select>
    </td>
  </tr>
  <tr> 
    <td class="formlabel">Enter shipping account number (ONLY FedEx number. We do not accept other couriers):</td>
    <td class="text">
        <html:text size="50" property="accountNumber"/>
    </td>
  </tr>
</table>

        <%--<p class="mainbodytexthead">
              Additional Options:
              <ul class="mainbodytexthead">
                  <li>Platinum QC Service: $6 for Harvard University and DF/HCC members; $7 for other academics and non-profits; $8 for commercial users.<p></li>
                  <li>Our platinum QC service has already been selected for your convenience. Please only remove this service if you are prepared to perform your own validation before any downstream experiments. Be aware that the cost of your construct lies within retrieval and distribution and we make no warranty on these clones. Removal of this service will not expedite your order in any way.<p></li>
                  <li>When you order a Platinum Clone we will perform end read sequencing at our in house sequencing facility. Our bench scientists will align the resulting sequence with the construct details that we list, saving you time, effort and money. If a clone fails Platinum QC validation you will be entitled to a refund or may select any replacement construct from our library. For each clone ordered with Platinum QC you will receive not only the standard glycerol stock, but additionally an aliquot of plasmid DNA, and the individual sequencing read will be made available through your account profile. If you have any additional questions about our Platinum Service please feel free to contact <a href="mailto:plasmidhelp@hms.harvard.edu">PlasmID help</a>.</li>
              </ul>
</p>

<table width="100%" border="0">
  <tr> 
    <td class="formlabel" width="20%">Would you like to add platinum service?</td>
    <td class="text">
            <html:radio styleClass="text" property="isplatinum" value="<%=Constants.ISPLATINUM_Y%>">Yes</html:radio>
            <html:radio styleClass="text" property="isplatinum" onclick="showMsg()" value="<%=Constants.ISPLATINUM_N%>">No</html:radio>
    </td>
  </tr>
  <tr> 
    <td></td>
    <td class="text">
        <html:submit value="Continue"/>
    </td>
  </tr>
</table>--%>
    </html:form>

    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>
