<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<meta name="GENERATOR" content="Microsoft FrontPage 4.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<title>main menu</title>
</head>

<body>
<html:form action="main.do">
<div align="center">
  <center>
  <table border="0" width="790" height="121" cellpadding="0">
    <tr>
      <td width="542" height="1" valign="top" align="left" rowspan="2">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>
        &nbsp;&nbsp;&nbsp;&nbsp; <img border="0" src="./jpg/medgene_logo.jpg" width="440" height="91"></a>&nbsp;&nbsp;&nbsp;
      </td>
      <td width="238" height="1" valign="top" align="right">
        &nbsp;
        <p>
        <a href="Logout.do"><img border="0" src="jpg/logout.gif" width="50" height="17"></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>
      </td>
    </tr>
    <tr>
      <td width="238" height="1" valign="bottom" align="right">
        &nbsp;<a href="http://www.hip.harvard.edu"><img border="0" src="jpg/bar_2.gif" width="187" height="24"></a>
      </td>
    </tr>
    <tr>
      <td width="784" colspan="2" height="32" valign="top" align="left">
        <p> <map name="FPMap0">
        <area href="about_medgene.html" shape="rect" coords="22, 5, 76, 22">
        <area href="main.jsp" shape="rect" coords="83, 6, 171, 23">
        <area href="CustomerRegistration.jsp" shape="rect" coords="179, 3, 273, 23">
        <area href="mailto:yanhui_hu@hms.harvard.edu" shape="rect" coords="280, 2, 363, 23"></map><img border="0" src="./jpg/bar_11.jpg" width="454" height="24" usemap="#FPMap0"><a href="http://www.hms.harvard.edu"><img border="0" src="./jpg/bar_13.jpg" width="315" height="24"></a></p>
      </td>
    </tr>
    <tr>
      <td width="784" colspan="2" height="82" valign="top" align="left">
        <table border="0" width="98%" cellspacing="0" cellpadding="0" height="1">
          <tr>
            <td width="100%" bgcolor="#FFFFFF" height="28" colspan="3" align="left" valign="top"><p align="center"><br>
              <img border="0" src="jpg/menu_1.gif" width="244" height="32"><img border="0" src="jpg/menu_2.gif" width="269" height="32"><img border="0" src="jpg/menu_3.gif" width="247" height="32"></td>
          </tr>
          <tr>
            <td width="5%" height="1" valign="top" align="left">&nbsp;<img border="0" src="jpg/v_line_left.gif" width="30" height="474"></td>
            <td width="91%" height="1" valign="top" align="center">&nbsp;
              <p align="left">
              <font color="#000080" size="4" face="Times New Roman">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              The MedGene
              database can help you to get the following information:<br>
              </font>
              </p>
              <table border="0" cellpadding="0" cellspacing="0" width="90%">
                <tr>
            <td width="100%" height="23" bgcolor="#1145A6">
              <font color="#FFFFFF"><b>&nbsp; A list of genes associated with</b></font>
              
            </td>
                </tr>
                <tr>
            <td width="100%" height="26" bgcolor="#DCE8FC">
              <font color="#000080">&nbsp;  
                <html:radio property="geneDiseaseSelect" value="geneDisease" /> A
                particular disease</font>                        
            </td>
                </tr>
                <tr>
            <td width="100%" height="30" bgcolor="#DCE8FC" bordercolor="#E8EFFD">              
                <font color="#000080">&nbsp;              
                <html:radio property="geneDiseaseSelect" value="multiDisease"/>  Multiple
                diseases &nbsp;</font></td>
                </tr>
                <tr>
            <td width="100%" height="29" bgcolor="#DCE8FC">
                <font color="#000080">&nbsp;              
                <html:radio property="geneDiseaseSelect" value="geneGene"/>  A particular
                 gene  &nbsp;<br></font>               
				  <font color="#ECECFF">a</font></td>
				  </tr>
                <tr>
            <td width="100%" height="25" bgcolor="#1145A6">
              <font color="#FFFFFF"><b>&nbsp; </b></font><b><font color="#FFFFFF">A
              list of diseases associated with</font></b>
              
            </td>
                </tr>
                <tr>
            <td width="100%" height="26" bgcolor="#DCE8FC">
              <font color="#000080">&nbsp;
              <html:radio property="geneDiseaseSelect" value="diseaseGene"/>  A particular
              gene<br>
              </font><font color="#ECECFF">s</font>
              
            </td>
                </tr>
                <tr>
            <td width="100%" height="23" bgcolor="#1145A6" bordercolor="#E8EFFD">              
                <font color="#000080">&nbsp; </font><b><font color="#FFFFFF">Analyze
                a gene list</font></b></td>
                </tr>
                <tr>
            <td width="100%" height="29" bgcolor="#DCE8FC"><font color="#000080">&nbsp;              
                <html:radio property="geneDiseaseSelect" value="chipGeneDisease"/>  Sort 
                genes from disease related high-throughput experiments such as micro-array
              &nbsp;</font></td>
                </tr>
                <tr>
            <td width="100%" height="27" bgcolor="#DCE8FC"><font color="#000080">&nbsp;
                <html:radio property="geneDiseaseSelect" value="chipGeneGene"/> Sort 
                genes from gene related high-throughput experiments such as micro-array  
              &nbsp;<br>
              </font><font color="#DCE8FC">s</font></td>                </tr>
                <tr>
                  <td width="100%"><br>
        <b><font color="#1145A6">Please make a selection and submit.</font></b>
</center>
                  <p align="left"><input type="submit" value="continue" name="submit"><br>
                  <font color="#FFFFFF">s</font></td>
              </tr>
            </table>
  <center>
  <p><img border="0" src="jpg/h_line.gif" width="700" height="5"></center></td>
            <td width="4%" height="1" valign="top" align="right"><img border="0" src="jpg/v_line_right.gif" width="30" height="474">&nbsp;
            </td>
          </tr>
          <tr>
            <td width="100%" height="1" colspan="3" valign="top" align="left">&nbsp;</td>
          </tr>
        </table>
      </td>
    </tr>
   </table>
  </div>
<div align="center">
  <center>
  <table border="0" width="789">
    <tr>
      <td width="100%"></td>
    </tr>
  </table>
  </center>
</div>
</html:form>

<p>
<table width='80%' align="center">
  <jsp:include page="links.jsp" flush="true"/>
</table>
</body>
