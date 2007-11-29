<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.oligo.*" %>
<html>

<head>


</head>

<body>


 
<table width="100%" border="0" cellpadding="10" style='padding: 0; margin: 0; '>
  <tr>
    <td><%@ include file="page_application_title.html" %></td>
  </tr>
  <tr>
    <td ><%@ include file="page_menu_bar.jsp" %></td>
  </tr>
  <tr>
    <td><table width="100%" border="0">
        <tr> 
          <td  rowspan="3" align='left' valign="top" width="160"  bgcolor='#1145A6'>
		  <jsp:include page="page_left_menu.jsp" /></td>
          <td  valign="top"> <jsp:include page="page_location.jsp" />
           </td>
        </tr>
        <tr> 
          <td valign="top"> <jsp:include page="page_title.jsp" /></td>
        </tr>
        <tr> 
          <td><!-- TemplateBeginEditable name="EditRegion1" -->


<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <td width="100%"><html:errors/></td>
    
  </table>
  </center>
</div>


<html:form action="/SelectProcess.do" >  
<input name="forwardName" type="hidden" value="<%= request.getAttribute("forwardName") %>" > 

<table border="0" cellpadding="2" cellspacing="2" width="84%" align=center>
<tr >  <td bgcolor="#e4e9f8"   >
<strong><p></p>Select sequencing primers: </strong><p></p></td></tr>



<tr><TD   > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>5p sequencing primer: </strong> 
      <SELECT NAME=<%= UI_Constants.SEQUENCING_PRIMER_5P_ID_KEY %> id=<%= UI_Constants.SEQUENCING_PRIMER_5P_ID_KEY %>>
            <% ArrayList p5_primers = (ArrayList ) request.getAttribute(UI_Constants.SEQUENCING_PRIMER_5P_ID_KEY);
 for (int count = 0; count < p5_primers.size(); count++)
            {
            Oligo primer = (Oligo)p5_primers.get(count);%>
            <OPTION VALUE=<%= primer.getId() %>><%= primer.getName() %>
                                  <%}%>
  </SELECT>     
</td></tr>

<tr><TD   > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>3p sequencing primer: </strong> 
     <SELECT NAME=<%= UI_Constants.SEQUENCING_PRIMER_3P_ID_KEY %> id=<%= UI_Constants.SEQUENCING_PRIMER_3P_ID_KEY %>>
            <% ArrayList p3_primers = (ArrayList ) request.getAttribute(UI_Constants.SEQUENCING_PRIMER_3P_ID_KEY);
 for (int count = 0; count < p3_primers.size(); count++)
            {
            Oligo primer = (Oligo)p3_primers.get(count);%>
            <OPTION VALUE=<%= primer.getId() %>><%= primer.getName() %>
                                  <%}%>
  </SELECT>     
</td></tr>
  
</table>
<p> 
<p> 
<div align="center"> 
  <p> 
    <input type="submit" value="Submit" >
   
</div>
</html:form> 

<!-- TemplateEndEditable --></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td><%@ include file="page_footer.jsp" %></td>
  </tr>
</table>
</body>
</html>


