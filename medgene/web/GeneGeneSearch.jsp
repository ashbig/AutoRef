<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Genes associated with a particular gene</title>
</head>

<body>
<jsp:include page="NavigatorBar.jsp" flush="true"/>

<div align="center">
<p align="left"><br>
</p>
  <center>
<table border="0" cellpadding="0" cellspacing="0" width="80%">
  <tr>
    <td width="93%"><b><font color="#000099" face="Verdana" size="5">Genes
      Associated with a Particular Gene</font></b></td>
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

<html:form action="SearchGenesForGene_step1.do">   
<div align="center">
  <center>
        <table border="1" cellpadding="0" cellspacing="0" width="70%" bordercolor="#99CCFF">
          <tr>
            <td width="100%" bgcolor="#99CCFF"><b><font color="#000099">&nbsp; <img border="0" src="jpg/prompt.gif" width="13" height="13">&nbsp;
              Please type in a gene term.&nbsp;</font></b></td>
          </tr>
          <tr>
            <td width="100%" bgcolor="#F0F8FF">
          <font color="#000099"><b>
          <br>
          <br>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></font><font color="#003399"><b>Input
              type:&nbsp;&nbsp;&nbsp;</b></font> 
                <html:select property="term">
                  <html:option value="Gene Name" />
                  <html:option value="Gene Symbol" />
                  <html:option value="Locus ID" />
                </html:select>
             
              <p><b><font color="#000099">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Gene terms:&nbsp;
              </font></b><html:text property="searchTerm" size="50"/><br>
              <br>
              <br>
              <br>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <font face="Arial" color="#003399">
              </font><b><font color="#000099">The corresponding official gene
              symbol(s) will be displayed in the next step.</font><font color="#003399">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></b>&nbsp;&nbsp;&nbsp;
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>
          <table border="0" cellpadding="0" cellspacing="0" width="91%">
            <tr>
              <td width="50%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <html:submit property="submit" value="continue"/></td>
              <td width="50%" align="right"><img border="0" src="jpg/dna.gif" width="55" height="64"><br>
                &nbsp;</td>
            </tr>
          </table>
            </td>
          </tr>
        </table>
  </center>
</div>
</html:form>
<br>
<table width='80%' align="center">
  <jsp:include page="links.jsp" flush="true"/>
</table>
</body>
