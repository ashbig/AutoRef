<%@page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>

<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>MedGene : Login</title>
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
      
    <table width="153" bordercolor="#0033CC" border="1" cellspacing="0" cellpadding="0">  
     <tr>
      <td width="142" height="1" bgcolor="#0033CC">&nbsp;&nbsp; <b><font color="#FFFFFF">MedGene
        Login</font></b></td>      
     </tr>
     <html:form action="logon.do" focus="username">
     <tr>
     <td width="142" height="23" bgcolor="#FFFFFF">
      <p align="left"><b><font color="#FFFFFF">&nbsp;&nbsp;</font><font color="#000000"><br>
      &nbsp;&nbsp; </font><font color="#000099">User Name</font><font color="#FFFFFF"><br>
      &nbsp; </font></b><font color="#000099"><html:text property="username" size="17"/></font></p>
      &nbsp;&nbsp; <b><font color="#000099">Password</font></b>&nbsp;<br>
      &nbsp; <html:password property="password" size="17"/><br>      
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:submit property="submit" value="Login"/><br>
        </p>
      </html:form>

      </td>
     </tr>
     <tr>
     <td width="142" height="22" bgcolor="#E4E9F8">
      &nbsp;
      <p>&nbsp;</p>
  </center>
     <p align="left">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     <map name="FPMap3">
     <area href="CustomerRegistration.jsp" shape="rect" coords="1, 3, 24, 24"></map><img border="0" src="./jpg/registration.gif" width="25" height="25" usemap="#FPMap3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>
     &nbsp; &nbsp;&nbsp;&nbsp;&nbsp; <font color="#008080">Registration</font></p>
     <p align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     <map name="FPMap4">
     <area href="mailto:yanhui_hu@hms.harvard.edu" shape="rect" coords="0, 4, 23, 23"></map><img border="0" src="./jpg/email_us.gif" width="24" height="24" usemap="#FPMap4">&nbsp;&nbsp;&nbsp;<br>
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <font color="#008080">Email us</font></p>
  <center>
  <p>&nbsp;</p>
  <p>&nbsp;
     </center>
      </td>
     </tr>
    </table>
      
      
    </center>
      </div>
      
      
      </td>
      <td width="78%" valign="top" height="350">
        <table border="0" width="99%">
          <tr>
            <td width="100%" bgcolor="#0066FF"><font color="#FFFFFF"><b>Welcome
              to MedGene Database&nbsp;</b></font></td>
          </tr>
          <tr>
            <td width="100%">
              <p align="left"><a><font size="3"><font color="#000080"><br>
              The accelerated pace of biological research and the advent of
              genomics and proteomics have resulted in the collection of prodigious amounts of data on
              diseases and the genes that play a&nbsp;role in them. Computational methods are needed to assimilate this
              surfeit of information. The MedGene project was designed to develop the automated extraction
              of biomedical knowledge from Medline database to create a human gene-to-disease
              co-occurrence network for all named human genes and all human diseases by automated analysis of MeSH
              indexes, title and abstracts in over 11 million Medline records. Statistical analysis is
              applied to score the association between a known human gene and a particular human disease based on the
              frequency with which they were co-cited in Medline records. Users can review:</font></font></a></p>
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
                  <td width="93%" bgcolor="#B8C6ED"><font color="#000080"><a><font size="3"> The analyzed gene list from other disease
              related high-throughput experiments,&nbsp;such as micro-array</font></a>
              </font></td>
                </tr>
                <tr>
                  <td width="7%" bgcolor="#E4E9F8">
                    <p align="center">6</td>
                  <td width="93%" bgcolor="#E4E9F8"><font color="#000080"><a><font size="3"> The analyzed gene list from other gene
              related high-throughput experiments, such as micro-array</font></a>
              </font></td>
                </tr>
              </table>
              <p align="left"><font color="#000080"><br>
              </font></p>
              <p align="left">&nbsp;</p>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td width="100%" colspan="2" height="59">&nbsp;
        <p>&nbsp;</td>
    </tr>
  </table>
</div>

</body>

</html>
