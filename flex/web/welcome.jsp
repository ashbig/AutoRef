<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
<head>
<link type="text/css" rel="stylesheet" href="mail_style.css">
<title>Welcome to FLEXGene</title>
</head>
<body bgcolor="#ffffff">
<center>

  <table cellpadding="0" cellspacing="0" border="0" width="720">
    <!--DWLayoutTable-->
    <tr>
      <td width="720" height="503" valign="top"> <table cellspacing=0 cellpadding=0 width=100% border=0>
          <tr>
            <td width=1% align=center> <table width=1% cellspacing=0 cellpadding=0 border=0>
                <tr>
                  <td width=1% align=center nowrap> <div align="center"><img src="welcome.gif" alt="Welcome to FLEXGene">
                    </div></td>
                </tr>
                <tr>
                  <td width=1% align=center nowrap> <div align="center"><a href="http://www.hip.harvard.edu"><b>Harvard Institute of Proteomics</b></a>
                    </div></td>
                </tr>
              </table></td>
          </tr>
          <tr>
            <td height="20" nowrap><spacer type="block" width="1" height="20"></td>
          </tr>
        </table>
        <table width="720" border="0" cellpadding="0" cellspacing="0" bgcolor="#B6C7E5">
          <!--DWLayoutTable-->
          <tr>
            <td width="12" height="5"></td>
            <td width="250"></td>
          </tr>
          <tr>
            <td height="436">&nbsp;&nbsp;</td>
            <td> <table border="0" cellpadding="0" cellspacing="0" bgcolor="#5350B9" height=96%>
                  <tr bgcolor="#B6C7E5">
                    <td colspan="3" height="10"><SPACER TYPE="vertical" SIZE="10">
                    </td>
                  </tr>
                  <tr>
                    <td align="left" valign="top"><img src="login_r2_c4.gif" width="10" height="9" border="0" alt=""></td>
                    <td rowspan="5"><html:errors/> <table border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td align="left" class="bodywhite" height=50><br> <span style="font-size: 15px; line-height: 17px; font-weight: bold;"><b>Member
                            sign in:</b></span><br> <hr size=1 noshade color="#8380DE">
                            <br> </td>
                        </tr>
                        <!------------------ BEGIN login form ------------------->
                        <tr>
                          <td> <table border="0" cellpadding="2" cellspacing="0">
                                <html:form action="logon.do" focus="username" target="_top">
                              <!--DWLayoutTable-->
                              <tr>
                                <td  align="right" nowrap  class="bodywhite">Username:</td>
                                <td  align="right"><html:text size="17" property="username"/></td>
                              </tr>
                              <tr>
                                <td align="right" nowrap class="bodywhite">Password:</td>
                                <td align="right"><html:password size="17" property="password"/></td>
                              </tr>
                              <tr>
                                <td height="25">&nbsp;</td>
                                <td>&nbsp;</td>
                              </tr>
                              <tr>
                                <td colspan="2" align="right"><input name="submit" type="submit" value="Sign In" class=buttonwhite></td>
                              </tr>
                            </html:form>
                            </table></td>
                        </tr>
                        <!------------------ END login form ----------------------->
                        <!------------------ BEGIN login links -------------------->
                        <tr>
                          <td align="right" valign="top" class="bodywhite"> <hr size=1 noshade color="#8380DE">
                            <br> <html:link forward="register" target="_top" style="color: #7AE8FF">Member
                            Registration</html:link> 
                            <br><html:link forward="findRegistration" target="_top" style="color: #7AE8FF">Find
                            Password</html:link> <br> <br> </td>
                        </tr>
						<tr>
                          <td align="right" valign="top" class="bodywhite"><a href="mailto:HIP_Informatics@hms.harvard.edu" style="color: #7AE8FF">
                            <b>Contact Us</b></a> </td>
                        </tr>
                      </table></td>
                    <!------------------ END login links ---------------------->
                    <td align="right" valign="top"><img src="login_r2_c6.gif" width="9" height="10" border="0" alt=""></td>
                  </tr>
                  <tr>
                    <td align="left" valign="bottom" height=383 width=11><spacer type=vertical size=383></td>
                    <td align="right" valign="bottom" height=383 width=11><spacer type=vertical size=383></td>
                  </tr>
                  <tr>
                    <td align="left" valign="bottom"><img src="login_r8_c4.gif" width="10" height="10" border="0" alt=""></td>
                    <td align="right" valign="bottom"><img src="login_r8_c6.gif" width="9" height="10" border="0" alt=""></td>
                  </tr>
                  <tr bgcolor="#B6C7E5">
                    <td colspan="3" height="10"><SPACER TYPE="vertical" SIZE="10"></td>
                  </tr>
              </table></td>
              <td>&nbsp;&nbsp;</td>
            <td valign="top" nowrap>
<br><b>FLEXGene (Full-Length Expression)</b> will be a complete repository<br>
of full-length cDNA clones for the human and other model organisms.<br>
The clones are all constructed using a recombination-based vector system<br>
so that their coding regions can be simultaneously transferred into any <br>
protein expression vector allowing the broadest variety of experiments.<br><br>

FLEXGene database allows user to browse and search the available<br>
human clones using genbank accesion number, GI, locus ID or gene symbol.<br>
Non registered users can search up to 30 terms each time. Registered<br>
users can search unlimited terms each time.<br><br>

<p><b>Please select one of the following:</b></p>
<ul>
    <li><a href="BrowseFlex.jsp">View available clones</a></li>
    <li><a href="GetSearchTerms.do">Search FLEXGene database</a></li>
</ul>
 
<br><br><i><b>For clone request, please contact <a href="mailto:jason_kramer@hms.harvard.edu">Jason Kramer</a></b></i>
</td>
<td>&nbsp;&nbsp</td>
          </tr>
        </table></td>
    </tr>
  </table>

<br>
<table cellpadding="0" cellspacing="0" border="0" width="720">
	<tr>
		<td align=center>
<div align="center">
          <table border=0 cellspacing=0 cellpadding=2 width=100%>
            <!--DWLayoutTable-->
            <tr>
              <td height="23" align="center"><font size="2">This system and the underlying database was built in conjunction with
<a href="http://www.3rdmill.com" target="_blank">3rd Millennium Inc.</a></font> </td>
            </tr>
          </table>
        </div>
		</td>

      <td align=center valign="middle">&nbsp; </td>
	<td></td>
	</tr>
</table>
</center>
</body>
</html>











<center>
<hr width="710" size=1 noshade>
<table width="710" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td align="middle"><font size=-2 face=arial color="#8d8d8d">Copyright &copy; 2001-2004 Harvard Institute of Proteomics. All rights reserved.<br>
		Last updated: July 1, 2004
		</td>
	</tr>
</table>
</center>
</body>
</html>
