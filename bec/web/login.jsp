<%@page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>

<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>ACE (<i>A</i>utomatic <i>E</i>valuation of <i>C</i>lones) login</title>
</head>

<body>

<div align="center">
  <center>
 
  <table border="0" width="90%" height="518" cellpadding="0">
   
     <tr><TD colspan="2"> <div align="center">
  <center>
  <table border="0" width="790" height="121" cellpadding="0">
    <tr>
     <td width="542" height="1" valign="top" align="left" rowspan="2">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>
        &nbsp;&nbsp;&nbsp;&nbsp; <!--<img border="0" src="./jpg/earth.gif" width="76" height="76"> -->
		<img border="0" src="./jpg/pc&woman.gif" width="96" height="76"></a>&nbsp;&nbsp;&nbsp;
      </td> 
      <td width="238" height="1" valign="top" align="right">
        &nbsp;
        <p>
        <a href="Logout.do">
		<img border="0" src="./jpg/logout.gif" width="50" height="17"></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>
      </td>
    </tr>
    <tr>
      <td width="238" height="1" valign="bottom" align="right">
        &nbsp;<a href="http://www.hip.harvard.edu"><img border="0" src="./jpg/bar_2.gif" width="187" height="24"></a>
      </td>
    </tr>
    <tr>
      <td width="784" colspan="2" height="32" valign="top" align="left">
        <p> <map name="FPMap0">
        <area href="about_bec.jsp" shape="rect" coords="22, 5, 76, 22">
        <area href="process.html" shape="rect" coords="83, 6, 171, 23">
        <area href="CustomerRegistration.jsp" shape="rect" coords="179, 3, 273, 23">
        <area href="mailto:elena_taycheru@hms.harvard.edu" shape="rect" coords="280, 2, 363, 23">
		</map><img border="0" src="./jpg/bar_11.jpg" width="454" height="24" usemap="#FPMap0">
		<a href="http://www.hms.harvard.edu"><img border="0" src="./jpg/bar_13.jpg" width="315" height="24"></a></p>
		<p><html:errors/></p>
      </td>
    </tr>
    
</table>
</td></tr>
    </tr>
    <tr>
      <td width="22%" valign="top" height="350">
      
    <div align="center">
      <center>

    <html:form action="logon.do" focus="username">
    <table width="153" bordercolor="#0033CC" border="1" cellspacing="0" cellpadding="0" height="468">  
     <tr>
              <td width="142" height="19" bgcolor="#0033CC">&nbsp;&nbsp; <b><font color="#FFFFFF">ACE 
                Login</font></b></td>      
     </tr>
     <tr>
     <td width="142" height="187" bgcolor="#FFFFFF">
      <p align="left"><b><font color="#FFFFFF">&nbsp;&nbsp;</font><font color="#000000"><br>
      &nbsp;&nbsp; </font><font color="#000099">User Name</font><font color="#FFFFFF"><br>
      &nbsp; </font></b><font color="#000099"><html:text property="username" size="17"/></font></p>
      &nbsp;&nbsp; <b><font color="#000099">Password</font></b>&nbsp;<br>
      &nbsp; <html:password property="password" size="17"/><br>

      <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="submit" value="Login" name="B1"><br>
      </p>     
     </td>
     </tr>
     <tr>
     <td width="142" height="256" bgcolor="#E4E9F8">
      &nbsp;
  </center>
     <p align="left">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     <map name="FPMap3">
     <area href="CustomerRegistration.jsp" shape="rect" coords="1, 3, 24, 24"></map>
	 <img border="0" src="./jpg/registration.gif" width="25" height="25" usemap="#FPMap3">
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>
     &nbsp; &nbsp;&nbsp;&nbsp;&nbsp; <font color="#008080">Registration<br>
     </font></p>
     <p align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     <map name="FPMap4">
     <area href="mailto:elena_taycher@hms.harvard.edu" shape="rect" coords="0, 4, 23, 23"></map>
	 <img border="0" src="./jpg/email_us.gif" width="24" height="24" usemap="#FPMap4">&nbsp;&nbsp;&nbsp;<br>
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <font color="#008080">Email us</font></p>
  <center>
  <p>&nbsp;
     </center>
      </td>
     </tr>
    </table>
    </html:form>
      
    </center>
      </div>
      
      
      </td>
      <td width="78%" valign="top" height="350">
        <table border="0" width="99%" height="105">
          <tr> 
            <td width="100%" bgcolor="#0066FF" height="21"><font color="#FFFFFF"><b>Welcome 
              to ACE&nbsp;</b></font></td>
          </tr>
          <tr> 
            <td width="100%" bgcolor="#EBEFFA" height="78" valign="top" align="left"> 
              <table border="0" cellpadding="0" cellspacing="0" width="98%" height="207">
                <tr> 
                  <td width="2%" height="207" align="center"><img src="./jpg/bec_schema.jpg"  border="0"> 
                </tr>
              </table></td>
          </tr>
        </table>
      </td>
    </tr>
    
  </table>
</div>

</body>

</html>


















