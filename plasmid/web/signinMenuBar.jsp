<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<table width="800" height="86" border="0" align="center">
  <tr> 
    <td colspan="3" rowspan="2">&nbsp;</td>
    <td width="47%" rowspan="2" align="left" valign="middle" class="title"> <p><strong><font color="#333333" size="5">Welcome 
        To PlasmID Database</font></strong></p></td>
    <td width="35%" height="54" align="right" valign="bottom" class="countrytext"> 
      <p><a href="ViewCart.do" border="0"><img src="shoppingcart2.gif" width="105" height="18"></a></p></td>
  </tr>
  <tr> 
    <td height="26" valign="baseline" class="countrytext">
        <logic:present name="USER" scope="session">
        <a href="Logout.do" class="countrytext">Sign Out </a>
        </logic:present>
        <logic:notPresent name="USER" scope="session">
        <a href="Login.jsp" class="countrytext">Sign In </a>
        </logic:notPresent>
        | <a href="PrepareRegistration.do" class="countrytext">Registration</a> | <a href="Account.jsp" class="countrytext">My 
      Account</a> | <a href="#" class="countrytext">Check Out</a></td>
  </tr>
</table>