<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.endreads.*" %>
<html>



<body>

<%ArrayList specs = (ArrayList)request.getAttribute(Constants.SPEC_COLLECTION);
   
%>
 
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> run Decision Tool</font>
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
    </tr>
	<tr>
        <td><i>If you are not sure about certain settings, please, consult help</i> </i> <a href="<%= edu.harvard.med.hip.utility.ApplicationHostDeclaration.JSP_REDIRECTION %>Help_PlateUploader.jsp">[parameter help file]</a>. 
          </i></td>
      </tr>
  </table>
  </center>
</div>


<html:form action="/RunProcess.do" >  
<input name="forwardName" type="hidden" value="<%= request.getAttribute("forwardName") %>" > 

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr><td colspan="2" bgcolor="#1145A6" height="29"> 
    <font color="#FFFFFF"><strong>Submit clone collection </strong></font> </td>
<tr><td><INPUT TYPE=RADIO NAME="data_type" VALUE="PLATE" checked>Plate Label:</td>
<td><INPUT TYPE=RADIO NAME="data_type" VALUE="CLONE">Clone List (separated by spaces, max 300 clone id)</td></tr>
</TR>
<TR>
<td valign="top"><INPUT name="plate_name" value="" type="text"></td>
<td> <textarea name="clone_collection" id="clone_collection" rows="10"> </textarea>  </td>
 </tr>
  
  <tr><td colspan=2>&nbsp;</td></tr>
  <tr>
    <td colspan="2" bgcolor="#1145A6" height="29"> 
    <font color="#FFFFFF"><strong>Select Configuration </strong></font> 
    </td>
  </tr>
   <tr>
   <td  bgcolor="#e4e9f8"> <strong>Bio Evaluation of Clones: </strong>    </td>
    <TD bgcolor="#e4e9f8" > 
    <SELECT NAME="<%= Spec.FULL_SEQ_SPEC %>" id="<%= Spec.FULL_SEQ_SPEC %>">
        <% 
        	Spec spec = null;
        	for (int count_spec = 0; count_spec < specs.size(); count_spec++)
        	
        	{
                    spec = (Spec)specs.get(count_spec);	%>
        		<OPTION VALUE="<%= spec.getId() %>"><%= spec.getName() %>
                   <% }%>
    	</SELECT>
       
      </td>
  </tr>
  
</table>
<p> 
<p> 
<div align="center"> 
  <p> 
    <input type="submit" value="Submit" name="B1">
  
</div>
</html:form> 
</body>
</html>


