<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<html>



<body>

<% ArrayList linkers = (ArrayList ) request.getAttribute(Constants.LINKER_COL_KEY);
   ArrayList vectors = (ArrayList ) request.getAttribute(Constants.VECTOR_COL_KEY);
%>
 
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> upload Plate Information from FLEX into BEC</font>
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
        <td><i>If you are not sure about certain settings, please, consult help</i> </i> <a href="/BEC/Help_PlateUploader.jsp">[parameter help file]</a>. 
          </i></td>
      </tr>
  </table>
  </center>
</div>


<html:form action="/RunProcess.do" >  
<input name="forwardName" type="hidden" value="<%= request.getAttribute("forwardName") %>" > 

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
		<td>

<strong>Upload plate information: </strong><p></p></td>
<td>
 <textarea property="plate_names" rows="10">
 </textarea>
  </td>
  </tr>
  <tr>
<td><strong>Next step</strong> </td><td>
  <select name="nextstep">
    <option selected value=1>Run end reads</option>
    <option value=2>Run clone evaluation</option>
  </select>
  </strong> </td> </tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td colspan="2" bgcolor="#1145A6" height="29"> <font color="#FFFFFF"><strong>Common parameters </strong></font> 
    </td>
  </tr>


  <tr> 
    <td  bgcolor="#e4e9f8" width="50%"> <b>Vector </b> 
    </td>
    <TD bgcolor="#e4e9f8" > 
     <SELECT NAME="vectorid">
        <% 
        	
        	for (int count = 0; count < vectors.size(); count++)
        	
        	{
        		BioVector vector = (BioVector)vectors.get(count);
        	%>
        		<OPTION VALUE=<%= vector.getId() %>><%= vector.getName() %>
        	<%
        	}%>
    	</SELECT>
       
      </div></td>
  </tr>
  <tr> 
    <td  bgColor="#b8c6ed"><b>5' clone linker seqment</b></td>
    <TD bgColor="#b8c6ed"> 
    <SELECT NAME="5linkerid">
    <% 
    	
    	for (int count = 0; count < linkers.size(); count++)
    	
    	{
    		BioLinker linker = (BioLinker)linkers.get(count);
    	%>
    		<OPTION VALUE=<%= linker.getId() %>><%= linker.getName() %>
    	<%
    	}%>
    	</SELECT>
     </div></td>
  </tr>
  <tr> 
    <td  bgcolor="#e4e9f8"> <b>3' clone tail seqment</b></td>
    <TD bgcolor="#e4e9f8">
        <SELECT NAME="3linkerid">
          <% 
          	
          	for (int count = 0; count < linkers.size(); count++)
          	
          	{
          		BioLinker linker = (BioLinker)linkers.get(count);
          	%>
          		<OPTION VALUE=<%= linker.getId() %>><%= linker.getName() %>
          	<%
          	}%>
    	</SELECT>
      
      </div></td>
  </tr>
</table>
<p> 
<p> 
<div align="center"> 
  <p> 
    <input type="submit" value="Submit" name="B1">
    &nbsp; 
    <input type="reset" value="Reset" name="B2">
</div>
</html:form> 
</body>
</html>


