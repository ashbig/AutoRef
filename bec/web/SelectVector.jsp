<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<html>

<head>
<script language="JavaScript">
		<!--

function fnShowVectorDetails()
{
   /* Use the selectedIndex property of the SELECT control
   to retrieve the text from the options collection. */
   
   var vectorid = vectorid.options(vectorid.selectedIndex).value;
   alert(vectorid);
    var r = "window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.VECTOR_DEFINITION_INT%>&amp;ID="+ vectorid +"','vectordetails','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes')";

}
-->
</script>

</head>

<body>

<% 
   ArrayList vectors = (ArrayList ) request.getAttribute(Constants.VECTOR_COL_KEY);

   int vectorid_value = -1;
%>
 
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> select Vector</font>
    <hr>
    
    <p>
    </td>
    </tr>
</table>

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

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr > 
<td bgcolor="#e4e9f8"><strong>Select vector: </strong><p></p></td>
<TD bgcolor="#e4e9f8" > 
     <SELECT NAME=<%= Constants.VECTOR_ID_KEY %> id=<%= Constants.VECTOR_ID_KEY %>>
        <% 
        	
        	for (int count = 0; count < vectors.size(); count++)
        	
        	{
        		BioVector vector = (BioVector)vectors.get(count);
                        if( count == 0) vectorid_value = vector.getId();
        	%>
        		<OPTION VALUE=<%= vector.getId() %>><%= vector.getName() %>
        	<%
        	}%>
    	</SELECT>
       
      </div>
</td>
<td bgcolor="#e4e9f8">
<input type=BUTTON value="Vector Details" 
 onClick="fnShowVectorDetails()">
</td>
</tr>
  
</table>
<p> 
<p> 
<div align="center"> 
  <p> 
    <input type="submit" value="Submit" >
   
</div>
</html:form> 
</body>
</html>


