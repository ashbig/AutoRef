<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><title>Make Reservation</title></head>
<body>

<html:form action="reserve.do" focus="firstName">

<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <td width="32%">
        <h2><font color="#000066"><i>Make Reservation</i></font></h2>
        <p>&nbsp;</td>
      <td width="68%"> </td>
    </tr>
    <tr>
      <td width="100%" colspan="2">
        <logic:present name="makeReservation.error">
            <p><font color="#FF0000"><bean:write name="makeReservation.error"/></font></p><br>
        </logic:present>
      </td>
    </tr>
    <tr>
      <td width="32%"><b><font color="#002677">Customer Information<br>
        &nbsp; <font size="2">(</font></font><font size="2" color="#FF0000">*</font><font color="#002677"><font size="2">
        required field)</font>
        <br>
        <br>
        </font></b></td>
      <td width="68%"> </td>
    </tr>
    <tr>
      <td width="32%"><font color="#000000">First Name</font><b><font size="2" color="#FF0000">*</font></b></td>
      <td width="68%"> <html:text property="firstName" size="40"/></td>
    </tr>
    <tr>
      <td width="32%"><font color="#000000">Last Name</font><b><font size="2" color="#FF0000">*</font></b></td>
      <td width="68%"><html:text property="lastName" size="40"/></td>
    </tr>
    
    <tr>
      <td width="32%"><br><font color="#000000">Email</font><b><font size="2" color="#FF0000">*</font></b></td>
      <td width="68%"><br><html:text property="email" size="40"/></td>
    </tr>

    <tr>
      <td width="32%"><font color="#000000">Password</font><b><font size="2" color="#FF0000">*</font></b></td>
      <td width="68%"><html:password property="password1" size="40"/></td>
    </tr>    
    <tr>
      <td width="32%"><font color="#000000">Re-enter password</font><b><font size="2" color="#FF0000">*</font></b></td>
      <td width="68%"><html:password property="password2" size="40"/></td>
    </tr>
    <br>&nbsp;<br>
    <tr>
      <td width="32%"><br><font color="#000000">Department</font><b><font size="2" color="#FF0000">*</font></b></td>
      <td width="68%"><br><html:text property="department" size="40"/></td>
    </tr>
    <tr>
      <td width="32%"><font color="#000000">Institute</font><b><font size="2" color="#FF0000">*</font></b></td>
      <td width="68%"><html:text property="institution" size="40"/></td>
    </tr>
    <tr>
      <td width="32%"><font color="#000000">Shipping address</font></td>
      <td width="68%"></td>
    </tr>
    <tr>
      <td width="32%"><font color="#000000">Street line 1</font><b><font size="2" color="#FF0000">*</font></b></td>
      <td width="68%"><html:text property="street1" size="40"/></td>
    </tr>
    <tr>
      <td width="32%"><font color="#000000">Street line 2</font></td>
      <td width="68%"><html:text property="street2" size="40"/></td>
    </tr>
    <tr>
      <td width="32%"><font color="#000000">City</font><b><font size="2" color="#FF0000">*</font></b></td>
      <td width="68%"><html:text property="city" size="40"/></td>
    </tr>
    <tr>
      <td width="32%"><font color="#000000">State</font><b><font size="2" color="#FF0000">*</font></b></td>
      <td width="68%"><html:text property="state" size="40"/></td>
    </tr>
    <tr>
      <td width="32%"><font color="#000000">Zip Code</font><b><font size="2" color="#FF0000">*</font></b></td>
      <td width="68%"><html:text property="zipcode" size="40"/></td>
    </tr>
    <tr>
      <td width="32%"><font color="#000000">Country</font><b><font size="2" color="#FF0000">*</font></b></td>
      <td width="68%"><html:text property="country" size="40"/></td>
    </tr>
    <tr>
      <td width="32%"><font color="#000000"><br>
        Authorized Institutional Official</font></td>
      <td width="68%"></td>
    </tr>
    <tr>
      <td width="32%"><font color="#000000"><br>
        Name</font><b><font size="2" color="#FF0000">*</font></b></td>
      <td width="68%"><html:text property="authorizedname" size="40"/></td>
    </tr>
    <tr>
      <td width="32%"><font color="#000000">Title</font><b><font size="2" color="#FF0000">*</font></b></td>
      <td width="68%"><html:text property="title" size="40"/></td>
    </tr>
    <tr>
      <td width="100%" colspan="2">
          <br>
          <br>
          <b><font color="#002677">Available Clone Set:</font> </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          Kinase Clone Set</td>
    </tr>
    <tr>
      <td width="100%" colspan="2">
          <p><br>
          <br>
          <html:submit property="submit" value="Submit"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <html:reset/></p>
      </td>
    </tr>

  </table>
  </center>
</div>

</html:form>
</body>
</html>
