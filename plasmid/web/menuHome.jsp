<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.User" %> 

<table width="136" border="0">
  <tr> 
    <td height="31" align="center" valign="middle"><font size="2" face="Verdana, Arial, Helvetica, sans-serif"><strong>Home</strong></font></td>
  </tr>
</table>
<table width="97%" height="115" border="1" bordercolor="#0099FF">
  <logic:present name="<%=Constants.USER_KEY%>" scope="session"> 
  <tr> 
    <td height="25"><a href="Account.jsp" class="leftnavtext">My Account</a></td>
  </tr>
  <tr> 
    <td height=45" valign="middle"> 
      <table width="100%" border="0">
        <tr> 
          <td height="15" valign="middle"><a href="UpdateAccount.jsp" class="leftnavtext">Update Account</a></td>
        </tr>
        <tr> 
          <td height="15" valign="middle"><a href="ViewOrderHistory.do" class="leftnavtext">View Orders</a></td>
        </tr>
        <tr> 
          <td height="15" valign="middle"><a href="ViewContainers.jsp" class="leftnavtext">View Containers</a></td>
        </tr>
        <tr> 
          <td height="15" valign="middle"><a href="vSearch.jsp" class="leftnavtext">Vector Submission</a></td>
        </tr>
        <tr> 
          <td height="15" valign="middle"><a href="vSearchSFD.jsp" class="leftnavtext">Vector for Distribution</a></td>
        </tr>        
        <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
        <tr> 
          <td height="15" valign="middle"><a href="SearchOrderInput.jsp" class="leftnavtext">Search Orders</a></td>
        </tr>
        <tr> 
          <td height="15" valign="middle"><a href="pReceiveSearch.jsp" class="leftnavtext">Receive Plasmids</a></td>
        </tr>
        </logic:equal>
      </table>
    </td>
  </tr>
  </logic:present>
  <logic:notPresent name="<%=Constants.USER_KEY%>" scope="session"> 
  <tr> 
    <td height="25"><a href="Home.jsp" class="leftnavtext">Home</a></td>
  </tr>
  <tr> 
    <td height=45" valign="middle"> 
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
    </td>
  </tr>
  </logic:notPresent>
</table>
