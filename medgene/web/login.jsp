<%@page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>

<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Medgene Database login</title>
</head>

<body>

<div align="center">
  <center>
  <table border="0" width="90%" height="518" cellpadding="0">
    <tr>
      <td width="100%" colspan="2" height="97" valign="top" align="left">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img border="0" src="./jpg/medgene_logo.jpg" width="440" height="91">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <map name="FPMap2">
        <area href="http://www.hip.harvard.edu" shape="rect" coords="149, 4, 323, 23"></map><img border="0" src="./jpg/bar_2.jpg" width="333" height="24" usemap="#FPMap2">
      </td>
    </tr>
    <tr><html:errors/></tr>
    <tr>
      <td width="100%" colspan="2" height="40" valign="top" align="left">
        <p>&nbsp;&nbsp;&nbsp; <map name="FPMap0">
        <area href="about_medgene.html" shape="rect" coords="22, 5, 76, 22">
        <area href="main.jsp" shape="rect" coords="83, 6, 171, 23">
        <area href="CustomerRegistration.jsp" shape="rect" coords="179, 3, 273, 23">
        <area href="mailto:yanhui_hu@hms.harvard.edu" shape="rect" coords="280, 2, 363, 23"></map><img border="0" src="./jpg/bar_11.jpg" width="454" height="24" usemap="#FPMap0"><img border="0" src="./jpg/bar_12.jpg" width="90" height="24"><a href="http://www.hms.harvard.edu"><img border="0" src="./jpg/bar_13.jpg" width="315" height="24"></a></p>
      </td>
    </tr>
    <tr>
      <td width="22%" valign="top" height="350">
      
    <div align="center">
      <center>

    <html:form action="logon.do" focus="username">
    <table width="153" bordercolor="#0033CC" border="1" cellspacing="0" cellpadding="0" height="468">  
     <tr>
      <td width="142" height="19" bgcolor="#0033CC">&nbsp;&nbsp; <b><font color="#FFFFFF">MedGene
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
     <area href="CustomerRegistration.jsp" shape="rect" coords="1, 3, 24, 24"></map><img border="0" src="./jpg/registration.gif" width="25" height="25" usemap="#FPMap3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>
     &nbsp; &nbsp;&nbsp;&nbsp;&nbsp; <font color="#008080">Registration<br>
     </font></p>
     <p align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     <map name="FPMap4">
     <area href="mailto:yanhui_hu@hms.harvard.edu" shape="rect" coords="0, 4, 23, 23"></map><img border="0" src="./jpg/email_us.gif" width="24" height="24" usemap="#FPMap4">&nbsp;&nbsp;&nbsp;<br>
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
        <table border="0" width="99%" height="501">
          <tr>
            <td width="100%" bgcolor="#0066FF" height="21"><font color="#FFFFFF"><b>Welcome
              to MedGene Database&nbsp;</b></font></td>
          </tr>
          <tr>
            <td width="100%" bgcolor="#EBEFFA" height="78" valign="top" align="left">
              <table border="0" cellpadding="0" cellspacing="0" width="98%" height="207">
                <tr>
                  <td width="2%" height="207"><br>
                  </td>
                  <td width="60%" valign="bottom" height="207">
                    <table cellSpacing="0" cellPadding="0" width="97%" border="0">
                      <tbody>
                        <tr>
                          <td align="right" width="49%"><img border="0" src="jpg/med1.gif" width="176" height="193"></td>
                          <td align="middle" width="22%" bgcolor="#FFFFFF">
                            <p align="center"><img border="0" src="jpg/computer_1.gif" width="32" height="32"></p>
                          </td>
                          <td width="29%"><img border="0" src="jpg/med2.gif" width="84" height="193"></td>
                        </tr>
                      </tbody>
                    </table>
                  </td>
                  <td width="38%" height="207" valign="top" align="left"><br>
                    <img border="0" src="jpg/new1.gif" width="60" height="25">
                    <p><b><font face="Verdana" size="2">&nbsp; <font color="#FF0000">MedGene
                    Database has been&nbsp;&nbsp;<br>
                    &nbsp; updated in September, 2003.
                    </font>
                    </font></b></p>
                      <p>&nbsp;<b>&nbsp;<font face="Verdana" size="2" color="#0066FF">MedGene
                      has been published on </font><font face="Verdana" size="2" color="#FF0000">
                      <br>
                      &nbsp; <i>Journal of Proteome Research <br>(vol 2, page 405-12) <br>Nature Biotechnology <br>(vol 21, page 976-7).</i> <br>
                      &nbsp; </font><font face="Verdana" size="2" color="#0066FF"></font></b></p>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td width="100%" height="227" valign="top" align="left">
              <br>
              <span style="background-color: #FFFFFF"><font color="#008080"></font><b><font color="#006666">
                Based on the co-citations of all Medline records, MedGene can retrieve the following relationships:<br>
              &nbsp;</font></b></span>
              <table border="0" width="100%">
                <tr>
                  <td width="7%" bgcolor="#B8C6ED">
                    <p align="center">1</td>
                  <td width="93%" bgcolor="#B8C6ED"><font color="#000080">
              <a><font size="3"> A list of human genes associated with a
              particular human disease in ranking order</font></a>
              </font></td>
                </tr>
                <tr>
                  <td width="7%" bgcolor="#E4E9F8">
                    <p align="center">2</td>
                  <td width="93%" bgcolor="#E4E9F8"><font color="#000080"><a><font size="3"> A list of human genes associated with
              multiple human diseases in ranking order</font></a>
              </font></td>
                </tr>
                <tr>
                  <td width="7%" bgcolor="#B8C6ED">
                    <p align="center">3</td>
                  <td width="93%" bgcolor="#B8C6ED"><font color="#000080"><a><font size="3"> A list of human diseases associated with a
              particular human gene in ranking order</font></a>
              </font></td>
                </tr>
                <tr>
                  <td width="7%" bgcolor="#E4E9F8">
                    <p align="center">4</td>
                  <td width="93%" bgcolor="#E4E9F8"><font color="#000080"><a><font size="3"> A list of human genes associated with a
              particular human gene in ranking order</font></a>
              </font></td>
                </tr>
                <tr>
                  <td width="7%" bgcolor="#B8C6ED">
                    <p align="center">5</td>
                  <td width="93%" bgcolor="#B8C6ED"><font color="#000080"><a><font size="3"> The
                    sorted gene list from other disease
              related high-throughput experiments,&nbsp;such as micro-array</font></a>
              </font></td>
                </tr>
                <tr>
                  <td width="7%" bgcolor="#E4E9F8">
                    <p align="center">6</td>
                  <td width="93%" bgcolor="#E4E9F8"><font color="#000080"><a><font size="3"> The
                    sorted gene list from other gene
              related high-throughput experiments, such as micro-array</font></a>
              </font></td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td width="100%" colspan="2" height="59">
&nbsp;&nbsp;&nbsp;&nbsp; <img border="0" src="jpg/byline.gif" width="838" height="3">
        <table cellSpacing="0" cellPadding="0" width="100%" border="0">
          <tbody>
            <tr>
              <td vAlign="top" width="11%"><font color="#ff0000"><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                Note:</b></font></td>
              <td vAlign="top" width="89%"><font size="2" color="#FF0000">MedGene
                is freely available for all research purposes. For information
                on commercial use please contact the Harvard Medical School
                Office <br>
                of Technology Licensing at (617) 432-0922.</font></td>
            </tr>
          </tbody>
        </table>
        &nbsp;
        <p>&nbsp;
      </td>
    </tr>
  </table>
</div>

</body>

</html>


















