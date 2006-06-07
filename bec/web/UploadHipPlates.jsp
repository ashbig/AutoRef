<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.endreads.*" %>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.util_objects.*" %>
<html>

<head>
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>

</head>

<body>

<% ArrayList linkers = (ArrayList ) request.getAttribute(Constants.LINKER_COL_KEY);
   ArrayList vectors = (ArrayList ) request.getAttribute(Constants.VECTOR_COL_KEY);
%>
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


<!--<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <td width="100%"><html:errors/></td>
    </tr>
	<tr>
        <td><i>If you are not sure about certain settings, please, consult help</i> </i> <a href="Help_PlateUploader.jsp">[parameter help file]</a>. 
          </i></td>
      </tr>
  </table>
  </center>
</div>-->


<form action="RunProcess.do" onsubmit="return validate_upload_hipplates(this);">  
<input name="forwardName" type="hidden" value="<%= request.getAttribute("forwardName") %>" > 

<table border="0" cellpadding="0" cellspacing="0" width="90%" align=center>
  <tr> 
		<td>

<strong>Enter plate names here: </strong><p></p></td>
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
    <td colspan="2" bgcolor="#1145A6" height="29"> <font color="#FFFFFF"><strong>Choose common parameters </strong></font> 
    </td>
  </tr>
<tr> 
    <td  bgColor="#e4e9f8"><b>Start Codon</b></td>
    <TD bgColor="#e4e9f8"> 
    <SELECT NAME="start_codon">
     		 <%  String value = null;String name = null;
for (Enumeration e = BecProperties.getInstance().getStartCodons().keys() ; e.hasMoreElements() ;)
{
	name = (String) e.nextElement();
	value = (String)BecProperties.getInstance().getStartCodons().get(name);%>
	 <OPTION VALUE='<%= value %>'> <%= name%>
<%}%>
    </SELECT>
     </div></td>
  </tr>

<tr> 
    <td  bgColor="#b8c6ed"><b>Fusion Stop Codon</b></td>
    <TD bgColor="#b8c6ed"> 
    <SELECT NAME="fusion_stop_codon">
                <% 
for (Enumeration e = BecProperties.getInstance().getStopFusionCodons().keys() ; e.hasMoreElements() ;)
{
	name = (String) e.nextElement();
	value = (String)BecProperties.getInstance().getStopFusionCodons().get(name);%>
	 <OPTION VALUE='<%= value %>'> <%= name%>
<%}%>
    	</SELECT>
     </div></td>
  </tr>
<tr> 
    <td  bgColor="#e4e9f8"><b>Closed Stop Codon</b></td>
    <TD bgColor="#e4e9f8"> 
    <SELECT NAME="closed_stop_codon">
   		<%  
for (Enumeration e = BecProperties.getInstance().getStopClosedCodons().keys() ; e.hasMoreElements() ;)
{
	name = (String) e.nextElement();
	value = (String)BecProperties.getInstance().getStopClosedCodons().get(name);%>
	 <OPTION VALUE='<%= value %>'> <%= name%>
<%}%>
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
  <tr><td colspan='2'> &nbsp; </td></tr>
  <tr> 
    <td  bgcolor="#e4e9f8"> <b>Project name:</b></td>
    <TD bgcolor="#e4e9f8">
        <SELECT NAME="<%= UI_Constants.PROJECT_ID%>">
           
          <option value = '-1' >Select project...	</option>
        <%ProjectDefinition pdef = null;
        for ( Enumeration e = DatabaseToApplicationDataLoader.getProjectDefinitions().elements(); e.hasMoreElements() ;)
        {
            pdef = (ProjectDefinition)  e.nextElement();
            %><option value = '<%= pdef.getId() %>' > <%= pdef.getName() %>

        <%}  	
         %>
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
</form> 

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


