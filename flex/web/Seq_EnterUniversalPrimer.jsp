<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Define Universal Primer Set</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="../developed/flex/web/FlexStyle.css" rel="stylesheet" type="text/css">
</head>
<body background='background.gif'>

<h2><bean:message key="flex.name"/> : Define Universal Primer Set</h2>
<hr>
<p>&nbsp;</p><html:errors/>
<html:form action="/logon.do" > 
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <TD width="21%"><b>Set Name</b></TD>
    <TD colspan="2"><input name="set_name" type="text" id="set_name" size="50"></TD>
  </tr>
  </table>
  <P></P>
<table width="100%"  cellspacing="2" cellpadding="2">
 
  <tr > 
    <td width="21%">&nbsp;</td>
    <td width="40%" > <div align="center"> <b><font size="+2">Forward Primer</font></b> 
      </div></td>
    <td width="39%"> <div align="center"><b><font size="+2">Reverse Primer</font></b> 
      </div></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><strong>Primer Name</strong> <P></td>
    <td  background="barbkgde.gif"><b> 
      <input size="30" name="f_name" type="text" id="f_name">
      </b> <P></td>
    <td  background="barbkgde.gif"><b> 
      <input  size="30" name="r_name" type="text" id="r_name">
      </b> <P></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><strong>Primer Sequence</strong> <P></td>
    <td  background="barbkgde.gif"><input type="text" size="30" name="f_sequence" id="f_sequence"> 
      <P></td>
    <td  background="barbkgde.gif"><input type="text" size="30" name="r_sequence" id="r_sequence"> 
      <P></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><strong>Primer Tm</strong> <P></td>
    <td  background="barbkgde.gif"><input name="f_tm" type="text" value="0" size="8"> 
      <P></td>
    <td  background="barbkgde.gif"><input type="text" name="r_tm" value="0" size="8"> 
      <P></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><strong>Primer Start</strong> <P></td>
    <td  background="barbkgde.gif"><input name="f_start" type="text" id="f_start" value="0" size="8"> 
      <P></td>
    <td  background="barbkgde.gif"><input name="r_start" type="text" id="r_start" value="0" size="8"> 
      <P></td>
  </tr>
</table>
<P>
<P>
<div align="center">
        <html:submit property="submit" value="Submit" />
  </div>

</html:form> 

<HR>
<HR>
<h3>Available Sets </h3>
<P> <font color="#2693A6" size="4"> <b>Set Name</b></font> 
 <SELECT NAME="M13" >
    <OPTION VALUE="Less than 1 year.">Set 1
    <OPTION VALUE="1-5 years.">Set 2
 </SELECT>

 <table width="100%"  cellspacing="2" cellpadding="2">
 
  <tr > 
    <td width="21%">&nbsp;</td>
    <td width="40%" > <div align="center"> <b><font size="+2">Forward Primer</font></b> 
      </div></td>
    <td width="39%"> <div align="center"><b><font size="+2">Reverse Primer</font></b> 
      </div></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><strong>Primer Name</strong> <P></td>
    <td  background="barbkgde.gif"><b> 
      M13-F
      </b> <P></td>
    <td  background="barbkgde.gif"><b> 
      M13-R
      </b> <P></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><strong>Primer Sequence</strong> <P></td>
    <td  background="barbkgde.gif">GTTTTCCCATCACGAC<P></td>
    <td  background="barbkgde.gif">CAGGAAACAGCTATGACC<P></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><strong>Primer Tm</strong> <P></td>
    <td  background="barbkgde.gif">0     <P></td>
    <td  background="barbkgde.gif">0      <P></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><strong>Primer Start</strong> <P></td>
    <td  background="barbkgde.gif">-40      <P></td>
    <td  background="barbkgde.gif">0      <P></td>
  </tr>
</table> 
</body>
</html>