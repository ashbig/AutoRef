<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>

<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.user.*"%>
<%@ page import="edu.harvard.med.hip.bec.Constants"%>
<%@ page import=" edu.harvard.med.hip.bec.coreobjects.spec.*"%>
<html>

<head>

<title>Select Specification to display</title>
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>
</head>

<body >

<jsp:include page="NavigatorBar_Administrator.jsp" />
<p><P><br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr> <td >
    <font color="#008000" size="5"><b>select Specification to view</font>
    <hr>   <p>  </td> </tr></table>

<div align="center">
  <center>  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%"><html:errors/></td>    </tr>	  </table>
  </center></div>





<form action="Seq_GetSpec.do" > 
<input name="forwardName" type="hidden" value="<%= Constants.AVAILABLE_SPECIFICATION_INT %>" > 


<% 
ArrayList specs = null; 
Spec spec = null;%>
    
<table border="0" cellpadding="1" cellspacing="1" width="74%" align=center>
<tr> <td colspan=2> <div align="right">   <i><b>Note:</b> you can view only one specification at a time</i></div></td><tr>

<tr> <td bgColor='#b8c6ed' height='39' >End Reads Evaluation Specifications:</td>
 <td bgColor='#b8c6ed'><select name="er_specid"> 
<option value="0"> Select specification 
<% 
                    
specs = (ArrayList)request.getAttribute("comparespec");
for (int spec_count = 0; spec_count < specs.size(); spec_count++)
    { spec = (Spec) specs.get(spec_count);%>
 
 <option value="<%= spec.getId()  %>"> <%= spec.getName() %> 

 <%}%></select> </td><tr>

<tr> <td  bgColor='#e4e9f8'  height='39'>Biological Evaluation of Clones Specifications:</td>
<td  bgColor='#e4e9f8' ><select name="be_specid"> 
<option value="0"> Select specification 
<% 
specs = (ArrayList)request.getAttribute("bioevaluation");
for (int spec_count = 0; spec_count < specs.size(); spec_count++)
    { spec = (Spec) specs.get(spec_count);%>
  <option value="<%= spec.getId()  %>"> <%= spec.getName() %> 
    <%}%></select></td><tr>


<tr> <td bgColor='#b8c6ed'  height='39'>Primer Designer Specifications:</td>
<td bgColor='#b8c6ed'><select name="primer_specid"> 
<option value="0"> Select specification 
<% 
specs = (ArrayList)request.getAttribute("primer3");
for (int spec_count = 0; spec_count < specs.size(); spec_count++)
    { spec = (Spec) specs.get(spec_count);%>
  <option value="<%= spec.getId()  %>"> <%= spec.getName() %> 
    <%}%></select> </td><tr>

<tr> <td  bgColor='#e4e9f8'  height='39'>Polymorphism Finder Specifications:</td>
 <td  bgColor='#e4e9f8' ><select name="polym_specid"> 
<option value="0"> Select specification 
<% 
specs = (ArrayList)request.getAttribute("polymerphism");
for (int spec_count = 0; spec_count < specs.size(); spec_count++)
    { spec = (Spec)specs.get(spec_count);%>
  <option value="<%= spec.getId()  %>"> <%= spec.getName() %> 
    <%}%></select> </td><tr>

<tr> <td bgColor='#b8c6ed'  height='39'>Sequence Trimming Parameters Specifications:</td>
 <td bgColor='#b8c6ed'><select name="sl_specid"> 
<option value="0"> Select specification 
<% 
specs = (ArrayList)request.getAttribute("slidingwindow");
for (int spec_count = 0; spec_count < specs.size(); spec_count++)
    { spec = (Spec) specs.get(spec_count);%>
  <option value="<%= spec.getId()  %>"> <%= spec.getName() %> 
    <%}%></select> </td><tr>

<tr> <td colspan=2> <div align="center">   <input type="submit" name="Submit" value="Submit"></div></td><tr>
</table></form>


</body>
</html>