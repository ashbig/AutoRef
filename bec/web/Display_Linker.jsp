<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<html><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<head>
<LINK REL=STYLESHEET       HREF="application_styles.css"      TYPE="text/css">


<title>Available Linkers</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- TemplateParam name="OptionalRegion1" type="boolean" value="true" -->
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
	        </tr>
	    	
	      </table>
	      </center>
	    </div>
	    <div align="center">
	    <table border="0" cellpadding="0" cellspacing="0" width="90%" >
	    <% 
	        BioLinker linker = null;
	        ArrayList linkers = (ArrayList) request.getAttribute("linkers");
	      
	     for (int count = 0; count < linkers.size(); count++)
	    {
	        linker = (BioLinker)linkers.get(count); 
	       
	    //force sequence wrap
	     char[] linkerseq = linker.getSequence().toCharArray();
	    StringBuffer linkerseq_formated = new StringBuffer();
	     for (int count1 = 0; count1 < linkerseq.length; count1++)
	    {
	    linkerseq_formated.append(linkerseq[count1]);
	    if (count1 !=0 && count1 % 40 == 0 )linkerseq_formated.append("<BR>");
	    }
	    
	    %>
	    
	    <tr class='headerRow'>	<td  height="26"> Linker </td><td>&nbsp;</td></tr>
	    <tr class='evenRowColoredFont'><td width="25%" >Linker Name  </td>	<td  ><%= linker.getName() %></td></tr>
	    <tr class='evenRowColoredFont'> 	<td  nowrap >Sequence: </td>	<td > <%= linkerseq_formated.toString() %></td></tr>
	    <% if (linker.getFrameStart() != -1)
	    {%>
	    <tr class='evenRowColoredFont'> 
	    	<td>Frame Start: </td>	<td > <%= linker.getFrameStart() %></td></tr>
	    <%}%>
	    
	    <tr><TD>&nbsp;</TD></TR>
	    
	    <%}%>
	      </table>
</div>

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
