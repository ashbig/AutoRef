<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title>Pricing</title>
<meta name='description' content='Detailed information about our fees and payment options.'>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
<link href="layout.css" rel="stylesheet" type="text/css" />
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
        <!--<script type="text/javascript" src="SpryMenuBar.js"></script>  code from dreamweaver doesn't work, submenu action re-done with css  
        <script type="text/javascript">
        var MenuBar1 = new Spry.Widget.MenuBar("MenuBar1", {imgDown:"SpryMenuBarDownHover.gif", imgRight:"SpryMenuBarRightHover.gif"});</script> -->
</head>
<div class="gridContainer clearfix">
<body>
<jsp:include page="signinMenuBar.jsp" />



      <table class="table" width="auto" border="0" align="left" id="gray">
        <tr class="tableheader">
          <td> Pricing Category</td>
          <td align="right">Retrieval Fee</td>
        </tr>
        <tr class="tableinfo">
          <td>Harvard University and DF/HCC Members*</td>
          <td align="right">$52.00</td>
        </tr>
        <tr class="tableinfo">
          <td>Other Academics and Nonprofits</td>
          <td align="right">$65.00</td>
        </tr>
        <tr class="tableinfo">
          <td>Commercial Users</td>
          <td align="right">$78.00</td>
        </tr>
        <tr class="tableinfo"><td colspan="2"><br>*Please click <a href="PIList.jsp">here</a> for our current list of DF/HCC members.</td></tr>
      </table>
      
      
      
<p class="mainbodytexthead">Plasmid Retrieval Costs</p>
        <p class='mainbodytext'> The PlasmID Repository is a non-profit service hosted by Harvard Medical School. To ensure our continued operation we charge a retrieval fee to offset the cost of labor, equipment, and lab space.<br> </p>  
      

    
 
    <p class='mainbodytexthead'>Shipping</p>
    <p class='mainbodytext'>Our flat price for shipping is $10 for domestic orders or $20 for international orders.
            You can also provide your own FedEx account number to be charged directly rather than pay our flat fee.
            Select 'Pickup' if you are nearby and want to avoid shipping costs.
            NOTE: You must have building access to use our 'Pickup' service.
            </p>
            <p class="mainbodytexthead">Financial FAQ's</p>
            <p>
            <a class="mainbodytext" href="#cost">How much do plasmids cost?</a><br>
            <a class="mainbodytext" href="#quote">Can I get a price quote?</a><br>
            <a class="mainbodytext" href="#thirtythreedigitcodes">The site asks for a 33-digit code, but I don&rsquo;t have one.  What do I do?</a><br>
            <a class="mainbodytext" href="#remit">What is your remit to address?</a><br>
            <a class="mainbodytext" href="#name">What is your legal name?</a><br>
            <a class="mainbodytext" href="#vendorregistration">Who can complete these Vendor Registration forms?</a><br>
            </p>
            <br>

            <table width="100%" border="0" cellpadding="0"><td>       
<p class="mainbodytexthead">Financial</p>  
           
<p class="mainbodytext"align="justify"><strong>
<a name="cost"></a>Q. How much do plasmids cost?</strong><br>
A. Plasmids from our facility are free, but we do charge a retrieval fee and shipping to cover our operating costs. Please see our current fee structures in the table above</a>.</p>

		<p class="mainbodytext"align="justify"><strong>
<a name="quote"></a>Q. Can I get a price quote?</strong><br>
A. The shopping cart on our website can provide a full summary of all costs before you enter a PO or process a credit card. Most users print from this page, save their shopping cart, and then return to the saved cart when they are ready to place their order. If your institution requires more than this please write to plasmidhelp@hms.harvard.edu. Responses from this email may take up to 24 hours on weekdays, and no responses are given on the weekend.</p>

		<p class="mainbodytext"align="justify"><strong>
<a name="thirtythreedigitcodes"></a>Q. The sites asks for a 33-digit code, but I don't have one. What do I do?</strong><br>
A. Harvard uses 33-digit codes for all internal purchases. If you are at Harvard University you should be able to obtain this account number from the person who places orders in your lab, or from your grant manager. If you are at a Harvard affiliated hospital you are considered external and need to pay by a PO or credit card. You are being asked for a 33-digit code because you improperly registered your account as 'Harvard University' when it should be 'Harvard affiliate.' Please log into your account, select 'My Account' and then 'Update Account' to change this value. Once you have modified your account the website will no longer ask for a 33-digit code.</p>
           
           <p class="mainbodytext"><strong>
<a name="remit"></a>Q. What is your remit to address?</strong><br>
A. We ask that you write PO's and remit payment to: <br><br>
<em>Harvard Medical School<br> Dept BCMP, C1-214<br> 240 Longwood Ave. <br> 
Boston, MA 02115</em></p>

<p class="mainbodytext"align="justify"><strong>
<a name="name"></a>Q. What is your legal name?</strong><br>
A. Our legal name is 'President and Fellows of Harvard College' and we are doing business as 'Harvard Medical School'</p>

<p class="mainbodytext"><strong>
<a name="vendorregistration"></a>Q. Who can complete these vendor registration forms?</strong><br>
A. Vendor registration forms should be sent to <a href="plasmidhelp@hms.harvard.edu">plasmidhelp@hms.harvard.edu</a>.</p>

		
        </td>
    </tr>
  </table>           

  
<jsp:include page="footer.jsp" /></body></div>
</html>
