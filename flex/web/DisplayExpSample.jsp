<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><title>Display Experiment Sample Result</title></head>
<body>

<html:form action="/ExportSeqSampleInfo.do">

<input type="hidden" name="sample_type" value="<bean:write name="sample_type"/>">
<input type="hidden" name="sample_status" value="<bean:write name="sample_status"/>">

<h2><font color="#000066">FLEXGene : Display Experiment Sample Result</font></h2>
<hr>

<table>
  <tbody>
    <tr>
      <td class="prompt"><b><font color="#000066">Project name:</font></b></td>
      <td><font color="#000066">&nbsp;&nbsp;&nbsp; <bean:write name="project_success_rate" property="projectname"/></font></td>
    </tr>
    <tr>
      <td class="prompt"><b><font color="#000066">Workflow name:</font></b></td>
      <td><font color="#000066">&nbsp;&nbsp;&nbsp; <bean:write name="project_success_rate" property="workflowname"/></font></td>
    </tr>
    <tr>
      <td class="prompt"><b><font color="#000066">Clone format:</font></b></td>
      <td>
        <font color="#000066">&nbsp;&nbsp;&nbsp;           
          <logic:equal name="project_success_rate" property="cloneFormat" value="ALL">
            CLOSED and FUSION
          </logic:equal>
          <logic:notEqual name="project_success_rate" property="cloneFormat" value="ALL">
            <bean:write name="project_success_rate" property="cloneFormat" />
          </logic:notEqual>
        </font>
      </td>
    </tr>
    <tr>
      <td class="prompt"><b><font color="#000066">Oligo received date for Sequences:<br>&nbsp;</font></b></td>
      <td><font color="#000066">&nbsp;&nbsp;&nbsp;         
        <logic:equal name="init_date_from" value=""> N/A </logic:equal>
        <logic:notEqual name="init_date_from" value="">  
          from &nbsp; <bean:write name="init_date_from" /> &nbsp; to &nbsp; <bean:write name="init_date_to" />
        </logic:notEqual><br>&nbsp;</font>
      </td>
    </tr>

    <% int workflowid = Integer.parseInt(request.getAttribute("workflowid").toString()); 
    if(! (workflowid == 4 || workflowid == 11)) {
    %>
    <tr>
      <td class="prompt" valign="top"><b><font color="#000066">Sucess criteria for the step of PCR Gel:</font></b></td>
      <td>
        <font color="#000066">         
        <logic:iterate id = "criteria" name = "pcr_succ_criteria">
          &nbsp;&nbsp;&nbsp;<bean:write name = "criteria"/><br>
        </logic:iterate><br>
        </font>
      </td>
    </tr>
    <% } %>

    <logic:equal name = "processname" value = "AGAR_PLATE">
    <tr>
      <td class="prompt"><b><font color="#000066">Sucess criteria for the step of Agar Plate:</font></b></td>
      <td><font color="#000066">&nbsp;&nbsp;&nbsp; 
          <bean:write name = "agar_plate_succ_criteria"/></font>
      </td>
    </tr>  
    </logic:equal>

    <logic:equal name = "processname" value = "CULTURE_PLATE">
    <tr>
      <td class="prompt"><b><font color="#000066">Sucess criteria for the step of Agar Plate:</font></b></td>
      <td><font color="#000066">&nbsp;&nbsp;&nbsp; 
          <bean:write name = "agar_plate_succ_criteria"/></font>
      </td>
    </tr>  
    <tr>
      <td class="prompt"><b><font color="#000066">Sucess criteria for the step of Culture Plate:</font></b></td>
      <td><font color="#000066">&nbsp;&nbsp;&nbsp; 
          <bean:write name = "culture_plate_succ_criteria"/></font>
      </td>
    </tr>  
    </logic:equal>

    <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
    <tr>
      <td class="prompt"><b><font color="#000066">Sample type:</font></b></td>
      <td><font color="#000066">&nbsp;&nbsp;&nbsp; 
          <bean:write name = "sample_type"/></font>
      </td>
    </tr>  
    <tr>
      <td class="prompt"><b><font color="#000066">Sample status:</font></b></td>
      <td><font color="#000066">&nbsp;&nbsp;&nbsp; 
          <bean:write name = "sample_status"/></font>
      </td>
    </tr> 


  </tbody>
</table>

<br><br>





<% int i = 0; String bgcolor = ""; %>

<logic:equal name="display_format" value="SPEICIAL">
<table border="1" width="50%" >
  <tr>
    <td width="15%" height="19" bgcolor="#626262"><font color="#FFFFFF">Sequence ID</font></td>    
  </tr>
  <logic:iterate id="sequenceInfo" name="seqSampleInfo">

  <% i++; %>
  <% if ((i%2) == 1) { bgcolor = "#EFEFEF" ;} %> 
  <% if ((i%2) == 0) { bgcolor = "#D4D4D4" ;} %> 

  <tr>
    <td width="15%" height="19" bgcolor="<%= bgcolor %>">
      <a href="ViewSequence.do?FLEX_SEQUENCE_ID=<bean:write name="sequenceInfo" property="sequence.seqID"/>">
      <bean:write name="sequenceInfo" property="sequence.seqID"/></a>
    </td>    
  </tr>
  </logic:iterate>
  <% i = 0; %>
</table>
</logic:equal>

<logic:equal name="display_format" value="REGULAR">
<table border="1" width="100%" >
  <tr>
    <td width="14%" height="19" bgcolor="#626262"><font color="#FFFFFF">Sequence ID</font></td>
    <td width="15%" height="19" bgcolor="#626262"><font color="#FFFFFF">CDS</font></td>
    <td width="14%" height="19" bgcolor="#626262"><font color="#FFFFFF">Sample ID</font></td>
    <td width="14%" height="19" bgcolor="#626262"><font color="#FFFFFF">Construct Type</font></td>
    <td width="14%" height="19" bgcolor="#626262"><font color="#FFFFFF">Container Label</font></td>
    <td width="14%" height="19" bgcolor="#626262"><font color="#FFFFFF">Well</font></td>
    <td width="15%" height="19" bgcolor="#626262"><font color="#FFFFFF">Experiment Result</font></td>
  </tr>

  <logic:iterate id="sequenceInfo" name="seqSampleInfo">
  <% i++; %>
  <% if ((i%2) == 1) { bgcolor = "#EFEFEF" ;} %> 
  <% if ((i%2) == 0) { bgcolor = "#D4D4D4" ;} %> 
  <tr>
    <td width="14%" height="19" bgcolor="<%= bgcolor %>">
      <a href="ViewSequence.do?FLEX_SEQUENCE_ID=<bean:write name="sequenceInfo" property="sequence.seqID"/>">
      <bean:write name="sequenceInfo" property="sequence.seqID"/></a>
    </td>
    <td width="15%" height="19" bgcolor="<%= bgcolor %>"><bean:write name="sequenceInfo" property="sequence.CDS_len"/></td>
    <td width="14%" height="19" bgcolor="<%= bgcolor %>"><bean:write name="sequenceInfo" property="sample.id"/></td>
    <td width="14%" height="19" bgcolor="<%= bgcolor %>"><bean:write name="sequenceInfo" property="construct.type"/></td>
    <td width="14%" height="19" bgcolor="<%= bgcolor %>"><bean:write name="sequenceInfo" property="sample.label"/></td>
    <td width="14%" height="19" bgcolor="<%= bgcolor %>"><bean:write name="sequenceInfo" property="sample.position"/></td>
    <td width="15%" height="19" bgcolor="<%= bgcolor %>"><bean:write name="sequenceInfo" property="sample.result"/></td>
  </tr>
  </logic:iterate>
  <% i = 0; %>
</table>
</logic:equal>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td width="85%"><br>
      &nbsp;Page&nbsp;&nbsp; 
      <logic:iterate id="p" name="pages_prev">
        <a href="DisplayExpSampleByPage.do?curr_page=<bean:write name="p"/>&sample_type=<bean:write name="sample_type"/>
               &sample_status=<bean:write name="sample_status"/>&display_format=<bean:write name="display_format"/>
               &page_size=<bean:write name="page_size"/>&total_pages=<bean:write name="total_pages"/>">
          <bean:write name="p"/>
        </a> &nbsp;&nbsp;&nbsp;
      </logic:iterate>
      <b><bean:write name="curr_page"/></b> &nbsp;&nbsp;&nbsp;
      <logic:iterate id="p" name="pages_next">
        <a href="DisplayExpSampleByPage.do?curr_page=<bean:write name="p"/>&sample_type=<bean:write name="sample_type"/>
               &sample_status=<bean:write name="sample_status"/>&display_format=<bean:write name="display_format"/>
               &page_size=<bean:write name="page_size"/>&total_pages=<bean:write name="total_pages"/>">
          <bean:write name="p"/>
        </a> &nbsp;&nbsp;&nbsp;
      </logic:iterate>
    </td>
    
    <td width="15%" valign="bottom">      
      <html:submit property="submit" value="Export"/>
    </td>
    
  </tr>
</table>
</html:form>
<p>
</p>


</body>
</html>

