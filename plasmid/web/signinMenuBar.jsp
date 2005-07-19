<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 

<table width="1000" height="86" border="0" align="center">
  <tr> 
    <td colspan="3" rowspan="2">&nbsp;</td>
    <td width="64%" align="center" valign="bottom" class="title"> 
      <p><strong><font color="#333333" size="5">Welcome To PlasmID</font></strong></p></td>
    <td width="18%" height="54" align="right" valign="bottom" class="countrytext"> 
      <p><a href="ViewCart.do" border="0"><img src="shoppingcart2.gif" width="105" height="18"></a></p></td>
  </tr>
  <tr>
    <td width="64%" align="center" valign="top" class="homepageLink"><a href="http://dnaseq.med.harvard.edu" target="_blank">DF/HCC 
      DNA Resource Core</a></td>
    <td height="26" valign="baseline" class="countrytext"> 
    <logic:present name="<%=Constants.USER_KEY%>" scope="session"> 
      <a href="Logout.do" class="countrytext">Sign Out </a> 
      | <a href="Account.jsp" class="countrytext">My Account</a> 
    </logic:present> 
    <logic:notPresent name="<%=Constants.USER_KEY%>" scope="session"> 
      <a href="Login.jsp" class="countrytext">Sign In </a> 
      | <a href="PrepareRegistration.do" class="countrytext">Registration</a> 
    </logic:notPresent> 
      | <a target="_blank" href="FAQ.jsp" class="countrytext">FAQ</a></td>
  </tr>
</table>
