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
        <td><i>If you are not sure about certain settings, please, consult help</i> </i> <a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Help_PlateUploader.jsp">[parameter help file]</a>. 
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
 <textarea name="plate_names" id="plate_names" rows="10">
 </textarea>
  </td>
  </tr>
  <tr>
<td><strong>Next step</strong> </td><td>
  <select name="nextstep">
      <option selected value="<%= IsolateTrackingEngine.PROCESS_STATUS_SUBMITTED_FOR_ER%>" >Run end reads</option>
    <option value="<%= IsolateTrackingEngine.PROCESS_STATUS_SUBMITTED_FOR_SEQUENCE_ANALYSIS %>">Run clone evaluation</option>
  </select>
  </strong> </td> </tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td colspan="2" bgcolor="#1145A6" height="29"> <font color="#FFFFFF"><strong>Common parameters </strong></font> 
    </td>
  </tr>
<tr> 
    <td  bgColor="#e4e9f8"><b>Start Codon</b></td>
    <TD bgColor="#e4e9f8"> 
    <SELECT NAME="start_codon">
     		<OPTION VALUE="ATG" selected>ATG
                <OPTION VALUE="NON">Natural
    </SELECT>
     </div></td>
  </tr>

<tr> 
    <td  bgColor="#b8c6ed"><b>Fusion Stop Codon</b></td>
    <TD bgColor="#b8c6ed"> 
    <SELECT NAME="fusion_stop_codon">
                <OPTION VALUE="GGA">GGA
                <OPTION VALUE="TTG" selected>TTG
    	</SELECT>
     </div></td>
  </tr>
<tr> 
    <td  bgColor="#e4e9f8"><b>Closed Stop Codon</b></td>
    <TD bgColor="#e4e9f8"> 
    <SELECT NAME="closed_stop_codon">
   		<OPTION VALUE="TGA" >TGA
                <OPTION VALUE="TAA" >TAA
                <OPTION VALUE="TAG" >TAG
                <OPTION VALUE="NON" selected>Natural
    	</SELECT>
     </div></td>
  </tr>
<tr><TD colspan='2'>&nbsp;</TD></TR>
  <tr> 
    <td  bgcolor="#e4e9f8" width="50%"> <b>Vector </b> 
    </td>
    <TD bgcolor="#e4e9f8" > 
     <SELECT NAME=<%=Constants.VECTOR_ID_KEY %> ID=<%= Constants.VECTOR_ID_KEY %>>
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
    <SELECT NAME="5LINKERID">
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
        <SELECT NAME="3LINKERID">
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


