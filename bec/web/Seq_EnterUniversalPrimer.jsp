<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.core.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.core.oligo.*" %>

<html>
<head>
<title><bean:message key="bec.name"/> : Define Universal Primer Set</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="../developed/bec/web/FlexStyle.css" rel="stylesheet" type="text/css">
</head>
<body background='background.gif'>

<h2><bean:message key="bec.name"/> :  Define Universal Primer Set</h2>
<hr>
<p>&nbsp;</p><html:errors/>
<html:form action="/Seq_SubmitSpec.do" > 

<input name="forwardName" type="hidden" value="<%=OligoPair.UNIVERSAL_PAIR_INT%>" >


<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <TD width="21%"><b>Set Name</b></TD>
    <TD colspan="2"><input name="SET_NAME" type="text" id="set_name" size="50"></TD>
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
      <input size="30" name="FORWARD_NAME" type="text" id="FORWARD_NAME">
      </b> <P></td>
    <td  background="barbkgde.gif"><b> 
      <input  size="30" name="REVERSE_NAME" type="text" id="REVERSE_NAME">
      </b> <P></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><strong>Primer Sequence</strong> <P></td>
    <td  background="barbkgde.gif"><input type="text" size="30" name="FORWARD_SEQUENCE" id="FORWARD_SEQUENCE"> 
      <P></td>
    <td  background="barbkgde.gif"><input type="text" size="30" name="REVERSE_SEQUENCE" id="REVERSE_SEQUENCE"> 
      <P></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><strong>Primer Tm</strong> <P></td>
    <td  background="barbkgde.gif"><input name="FORWARD_TM" type="text" id="FORWARD_TM" value="0" size="8"> 
      <P></td>
    <td  background="barbkgde.gif"><input name="REVERSE_TM" type="text" id="REVERSE_TM" value="0" size="8"> 
      <P></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><strong>Primer Start</strong> <P></td>
    <td  background="barbkgde.gif"><input name="FORWARD_START" type="text" id="FORWARD_START" value="0" size="8"> 
      <P></td>
    <td  background="barbkgde.gif"><input name="REVERSE_START" type="text" id="REVERSE_START" value="0" size="8"> 
      <P></td>
  </tr>
</table>
<P>
<P>
<div align="center">
        <html:submit property="submit" value="Submit" />
  </div>

</html:form> 


</body>
</html>