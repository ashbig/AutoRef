<%@page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<html>

<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>ACE (Automatic Evaluation of Clones) login</title>
</head>

<body>

<div align="center">
  
 
  <table border="0" width="90%" height="518" cellpadding="0">
   
     <tr> 
            
          <tr>
            <td height="22" colspan=2><h1 style='font: bold, 22px, Times New Roman ;text-align: center; color: #3333CC;white-space: nowrap'>Welcome 
          to Automatic Clone Evaluation (ACE)</h1>
        
        </td>
          </tr>
		  <td height="1" colspan=2 align=center> <a href="http://www.hip.harvard.edu"><b>Harvard Institute of Proteomics</b></a></td>
          </tr>
			  <tr><td colspan=2><p><html:errors/></p></td>          </tr>
   <tr><td colspan=2><hr></td></tr>

    <tr>
      <td width="22%" valign="top" height="350">
      
    <div align="center">
      <center>

    <html:form action="logon.do" focus="username">
    <table width="153" bordercolor="#0033CC" border="1" cellspacing="0" cellpadding="0" height="468">  
     <tr>
              <td width="142" height="19" bgcolor="#1145A6" align="center">&nbsp;&nbsp; <b><font color="#FFFFFF">ACE 
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
     <area href="mailto:<%=  BecProperties.getInstance().getACEEmailAddress() %>" shape="rect" coords="0, 4, 23, 23"></map>
	 <img border="0" src="./jpg/email_us.gif" width="24" height="24" usemap="#FPMap4">&nbsp;&nbsp;&nbsp;<br>
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="#008080">Contact Us</font></p>
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
      <td  valign="top" height="350">
        <table  width='98%' bordercolor="#0033CC" border="1" cellspacing="0" cellpadding="0" height="468">  
          <tr> 
            <td  bgcolor="#1145A6" height="21" align="center"><font color="#FFFFFF"><b>Welcome 
              to ACE</b></font></td>
          </tr>
          <tr> 
            <td height="438"  valign="center"  bgcolor="#EBEFFA" > 
              <table border="0" cellpadding="0" cellspacing="0" width="98%"  align="center">
                <tr> 
                  <td align="center"><img src="./jpg/bec_schema.jpg"  border="0"> 
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


















