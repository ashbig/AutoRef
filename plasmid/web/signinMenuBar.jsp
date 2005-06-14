<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<link href="file:///G|/dev/plasmid/web/plasmidstyle.css" rel="stylesheet" type="text/css">

<table width="1000" height="86" border="0" align="center">
  <tr> 
    <td colspan="3" rowspan="2">&nbsp;</td>
    <td width="52%" align="center" valign="bottom" class="title"> 
      <p><strong><font color="#333333" size="5">Welcome To PlasmID</font></strong></p></td>
    <td width="30%" height="54" align="right" valign="bottom" class="countrytext"> 
      <p><a href="ViewCart.do" border="0"><img src="shoppingcart2.gif" width="105" height="18"></a></p></td>
  </tr>
  <tr>
    <td width="52%" align="center" valign="top" class="homepageLink"><a href="http://dnaseq.med.harvard.edu" target="_blank">DF/HCC 
      DNA Resource Core</a></td>
    <td height="26" valign="baseline" class="countrytext"> <logic:present name="USER" scope="session"> 
      <a href="Logout.do" class="countrytext">Sign Out </a> </logic:present> <logic:notPresent name="USER" scope="session"> 
      <a href="Login.jsp" class="countrytext">Sign In </a> </logic:notPresent> 
      | <a href="PrepareRegistration.do" class="countrytext">Registration</a> 
      | <a href="Account.jsp" class="countrytext">My Account</a> | <a href="#" class="countrytext">Check 
      Out</a></td>
  </tr>
</table>
