<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Vector</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- TemplateParam name="OptionalRegion1" type="boolean" value="true" -->
<LINK REL=StyleSheet HREF="application_styles.css" TYPE="text/css" MEDIA=screen>
<link href="FlexStyle.css" rel="application_styles" type="text/css">

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
	      <table border="0" cellpadding="0" cellspacing="0" width="90%">
	        <tr>
	          <td width="100%"><html:errors/></td>
	        </tr>
	    
	      </table>
	      </center>
	    </div>
	    
	    <table border="0" cellpadding="0" cellspacing="0" width="90%" align=center>
	    <% BioVector vector = null;
	        ArrayList vectors = (ArrayList) request.getAttribute("vectors");
	    String[] row_class = {"evenRowColoredFont","oddRowColoredFont"} ; int row_count = 0;
	     for (int count1 = 0; count1 < vectors.size(); count1++)
	    {
	    
	        vector = (BioVector)vectors.get(count1);
	    
	    %>
	    <tr><td colspan=2><HR></td></tr>
	    <tr class=<%= row_class[row_count++ % 2]%>>
	    	<td >Vector Name  </td>	<td  ><%= vector.getName() %></td></tr>
	    <!--<tr> 
	    	<td  bgColor="#e4e9f8" ><b>Vector Id:</b></td>
	    	 <td bgColor="#e4e9f8">= vector.getId() %></td>  
	    	  </tr>-->
	    <tr class=<%= row_class[row_count++ % 2]%>> 
	    	<td  >Vector Source:</td><td ><%= vector.getSource() %></td>  	  </tr>
	    <tr class=<%= row_class[row_count++ % 2]%>> 
	    	<td >Vector Type:</td>	 <td ><%= vector.getTypeAsString() %></td>  	  </tr>
	    
	    <tr> 
	    <td colspan=2 ALIGN='CENTER'>
	    <p><b><P></P>Vector Features</b> </p> 
	         
	          <table width="85%" border="0" align="center">
	    		   <tr  class='headerRow'> 
	    			  <th >Name</th>
	    			  <th >Type </th>
	    			  <th >Description </th>
	    			</tr>
	    	  <% ArrayList features = (ArrayList) vector.getFeatures();
	    
	    	     String row_color = "bgColor='#e4e9f8'" ;
	    		 BioVectorFeature vf = null;
	    
	    		 for (int count = 0; count < features.size(); count++)
	    		{
	    			vf = (BioVectorFeature)features.get(count);
	                            row_count = 0;
	    		%>
	           
	            <tr class=<%= row_class[row_count++ % 2]%> > 
	              <td width="44%" ><%= vf.getName() %></td>
	              <td width="16%"><div align="center"><%= vf.getTypeAsString() %></div></td>
	              <td ><div align="center"> <%= vf.getDescription() %></div></td>
	            </tr>
	    		<%}%>
	    		</table>
	    </td>
	    </tr>
	    <TR><TD>&nbsp;</TD></TR>
	    <%}%>
  </table>
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
