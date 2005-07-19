<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 

<table width="100%" border="0">
  <tr> 
    <td height="31" align="center" valign="middle"><font size="2" face="Verdana, Arial, Helvetica, sans-serif"><strong>Home</strong></font></td>
  </tr>
</table>
<table width="97%" height="115" border="1" bordercolor="#0099FF">
  <tr> 
    <td height="25"><a href="Home.jsp" class="leftnavtext">Overview</a></td>
  </tr>
  <tr> 
    <td height=45" valign="middle"> 
    <logic:present name="<%=Constants.USER_KEY%>" scope="session"> 
      <table width="100%" border="0">
        <tr> 
          <td height="15"><a href="UpdateAccount.jsp" class="leftnavtext">Update Account</a></td>
        </tr>
        <tr> 
          <td height="15" valign="middle"><a href="ViewOrderHistory.do" class="leftnavtext">View Orders</a></td>
        </tr>
      </table>
    </logic:present>
    <logic:notPresent name="<%=Constants.USER_KEY%>" scope="session"> 
      <table width="100%" border="0">
        <tr> 
          <td height="15"><a href="Login.jsp" class="leftnavtext">Sign In</a></td>
        </tr>
        <tr> 
          <td height="15" valign="middle"><a href="FindPassword.jsp" class="leftnavtext">Find Password</a></td>
        </tr>
        <tr> 
          <td height="15"><a href="PrepareRegistration.do" class="leftnavtext">Registration</a></td>
        </tr>
      </table>
    </logic:notPresent>
    </td>
  </tr>
</table>
