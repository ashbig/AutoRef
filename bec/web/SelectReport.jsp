<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<html>
<head>
<title>select report</title>
</head>
<body >
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td > <font color="#008000" size="5"><b> select Report Type</font>    <hr>    <p>    </td>
    </tr>
</table>

<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%"><html:errors/></td>    </tr>
  </table>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%">
 <i>If you are not sure what kind of information each report provides please consult <a href="Help_SpecialReports.jsp"> help
</a>. </i> 
  </td>    </tr>
  </table>
  </center>
</div>

<html:form action="/SelectProcess.do" > 



<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<tr><td>
<input type="radio" name="forwardName" value="<%=Constants.PROCESS_CREATE_REPORT%>" checked><strong>Genral Report </strong><br>
<P>
<input type="radio" name="forwardName" value="<%=  Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING%>" ><strong>Order list for End Reads repeat </strong><br>
<!--<input type="radio" name="forwardName" value="<%= Constants.PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING%>"> <strong> Order list for Internal Reads repeat</strong>  <br> -->

<P><input type="radio" name="forwardName" value="<%= Constants.PROCESS_CREATE_REPORT_TRACEFILES_QUALITY%>"> <strong> Trace Files Quality Report</strong>  <br>
</td></tr>

</table>


<div align="center"> 
  <p> 
    <input type="submit" value="Submit" name="B1">
  
</div>
</html:form> 
</body>

</html>


