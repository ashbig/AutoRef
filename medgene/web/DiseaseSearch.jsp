<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Genes associated with a particular disease</title>
</head>

<body>
<jsp:include page="NavigatorBar.jsp" flush="true"/>

<div align="center">
<p align="left"><br>
</p>
  <center>
<table border="0" cellpadding="0" cellspacing="0" width="90%">
  <tr>
    <td width="93%"><b><font color="#000099" face="Verdana" size="5">Genes
      Associated with a Particular Disease</font></b></td>
    <td width="22%" align="right"><b><font color="#003366" size="6"><img border="0" src="jpg/medgene02.gif" width="85" height="35"></font></b></td>
  </tr>
  <tr>
    <td width="126%"></td>
    <td width="9%"></td>
  </tr>
  <tr>
    <td width="135%" colspan="2"><br>
      <img border="0" src="jpg/menubar_upper.gif" width="900" height="10"></td>
  </tr>
</table>
  </center>
</div>

<br><br><br><br>

<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <td width="100%"><html:errors/></td>
    </tr>
  </table>
  </center>
</div>

<html:form action="diseaseSearch.do"> 
<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="77%" height="1">
    <tr>
      <td width="75%" align="left" valign="top" rowspan="2" height="1">
        <table border="1" cellpadding="0" cellspacing="0" width="100%" bordercolor="#99CCFF">
          <tr>
            <td width="100%" bgcolor="#99CCFF"><b><font color="#000099">&nbsp; <img border="0" src="jpg/prompt.gif" width="13" height="13">&nbsp;
              </font><font face="Arial" color="#003399">Please type in a disease
              name. </font></b></td>
          </tr>
          <tr>
            <td width="100%" bgcolor="#F0F8FF">
              <b><font color="#000099"><br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Disease term: </font></b>
              <html:text property="searchTerm" size="50" /><br>
              <br>
              <br>
              <br>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b><font face="Arial" color="#003399">
              </font><font color="#003399">The corresponding MeSH term(s) will
              be displayed in the next step.</font></b>
              <p><br>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <html:submit property="submit" value="continue"/><br>          
              </p>
              <p><b><font face="Arial" color="#003399">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <br>
              </font></b></td>
          </tr>
        </table>
      </td>
      <td width="25%" valign="top" align="right" height="1">
        <table border="1" cellpadding="0" cellspacing="0" width="70%" bordercolor="#D1D1BA">
          <tr>
            <td width="100%" bgcolor="#D1D1BA">
              <p align="center"><b><font color="#666633">Hint</font></b></td>
          </tr>
          <tr>
            <td width="100%" valign="top" align="right">
              <p align="center"><br>
              <br>
              <a href="http://www.nlm.nih.gov/mesh/" target="_blank"><font color="#666633"><b>MeSH
              home</b></font></a><br>
              <p align="center">
      <b> <a href="DiseaseList.jsp" target="_blank"><font color="#666633">Disease
      <br>
      MeSH terms</font></a>
      </b>
              <p>&nbsp;</td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td width="25%" valign="top" align="right" height="30"><img border="0" src="jpg/human_disease.jpg" width="45" height="45">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </td>
    </tr>
  </table>
  </center>
</div>
</html:form> 
  <p>
      <br>
      <br>
      <br>
      <br>
  </p>

<table width='80%' align="center">
  <jsp:include page="links.jsp" flush="true"/>
</table>
</body>

