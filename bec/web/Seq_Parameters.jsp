<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.oligo.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html>
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body background='background.gif'>
<h2><bean:message key="bec.name"/> : Sequence Evaluation Settings</h2>
<hr>
<h3>
Create new set of parameters or view all available sets.</h3>

<table width="75%" border="0" cellspacing="5" cellpadding="2" align="center">
  <tr> 
    <td width="50%">End Reads Evaluation</td>
    <td width="25%"><div align="center"> <b><a href="Seq_EnterEndReadsParameters.jsp"> 
        Create </a></b> </div></td>
    <td width="25%"><div align="center"> <b><a href="/BEC/Seq_GetSpec.do?forwardName=<%=Spec.END_READS_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS %> "> 
        View Mine </a></b> </div></td>
    <td width="25%"><div align="center"> <b><a href="/BEC/Seq_GetSpec.do?forwardName=<%=Spec.END_READS_SPEC_INT%>"> 
        View All </a></b> </div></td>
  </tr>
  <tr> 
    <td>Primer Disigner Parameters</td>
    <td><div align="center"> <b><a href="Seq_EnterPrimerParameters.jsp"> Create 
        </a></b> </div></td>
    <td><div align="center"> <b><a href="/BEC/Seq_GetSpec.do?forwardName=<%=Spec.PRIMER3_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS %>"> 
        View Mine </a></b> </div></td>
    <td><div align="center"> <b><a href="/BEC/Seq_GetSpec.do?forwardName=<%=Spec.PRIMER3_SPEC_INT%>"> 
        View All </a></b> </div></td>
  </tr>
  <tr> 
    <td>Biological Evaluation of Clones</td>
    <td><div align="center"> <b><a href="Seq_EnterFullSeqParameters.jsp"> Create 
        </a></b> </div></td>
    <td><div align="center"> <b><a href="/BEC/Seq_GetSpec.do?forwardName=<%=Spec.FULL_SEQ_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS%>"> 
        View Mine </a></b> </div></td>
    <td><div align="center"> <b><a href="/BEC/Seq_GetSpec.do?forwardName=<%=Spec.FULL_SEQ_SPEC_INT%>"> 
        View All </a></b> </div></td>
  </tr>
  <tr>
    <td>Polymorphism Finder Parameter</td>
    <td><div align="center"> <b><a href="Seq_EnterPolymorphismParameters.jsp"> Create </a></b> </div></td>
    <td><div align="center"> <b><a href="/BEC/Seq_GetSpec.do?forwardName=<%=Spec.POLYMORPHISM_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS%>"> 
        View Mine </a></b> </div></td>
    <td><div align="center"> <b><a href="/BEC/Seq_GetSpec.do?forwardName=<%=Spec.POLYMORPHISM_SPEC_INT%>"> 
        View All </a></b> </div></td>
  </tr>
<!--
  <tr> 
    <td>Universal Primers Pairs</td>
    <td><div align="center"> <b><a href="Seq_EnterUniversalPrimer.jsp"> Create 
        </a></b> </div></td>
    <td>&nbsp;</td>
    <td><div align="center"> <b><a href="/BEC/Seq_GetSpec.do?forwardName=< %=OligoPair.UNIVERSAL_PAIR_INT%>"> 
        View All </a></b> </div></td>
  </tr>
-->
</table>



</body>
</html>
