<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html>
<head><title>Success Rate</title></head>
<body>
<h2><font color="#000066">FLEXGene : Success Rate</font></h2>
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

    <% int workflowid = Integer.parseInt(request.getParameter("workflowid")); 
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

  </tbody>
</table>

<br><br>

<table border="1" cellpadding="0" cellspacing="0" width="80%">
  <tr>
    <td width="14%" align="center" bgcolor="#E1E1FF">&nbsp;</td>
    <td width="14%" align="center" bgcolor="#E1E1FF">Input #</td>
    <td width="14%" align="center" bgcolor="#E1E1FF">Success #</td>
    <td width="14%" align="center" bgcolor="#E1E1FF">Fail #</td>
    <td width="14%" align="center" bgcolor="#E1E1FF">&nbsp;Not done #</td>
    <td width="15%" align="center" bgcolor="#E1E1FF">Step success rate (%)</td>
    <td width="15%" align="center" bgcolor="#E1E1FF">Overall success rate (%)</td>
  </tr>

  <% int i = 0;   %> 

  <logic:iterate id = "rate" name = "project_success_rate" property="reverseSuccessRates">  
  <% i++; 
     if(! (i == 2 && (workflowid == 4 || workflowid == 11))){
  %>
  <tr>
    <td width="14%" align="center">
        <logic:equal name = "rate" property="step" value="INIT">
            <bean:write name="rate" property="step"/>
        </logic:equal>
        <logic:notEqual name = "rate" property="step" value="INIT">
            <a href="DisplayPlateSuccessInfo.do?step=<%= i %>" target="_blank"><bean:write name="rate" property="step"/></a>
        </logic:notEqual>
    </td>
    <td width="14%" align="center">
        <logic:equal name = "rate" property="total" value="0">
            <bean:write name="rate" property="total"/>
        </logic:equal>     
        <logic:notEqual name = "rate" property="total" value="0">
            <a href="DisplayExpSample.do?step=<%= i %>&sampleStatus=input" target="_blank"><bean:write name="rate" property="total"/></a>
        </logic:notEqual>          
    </td>
    <td width="14%" align="center">
        <logic:equal name = "rate" property="success" value="0">
            <bean:write name="rate" property="success"/>
        </logic:equal>     
        <logic:notEqual name = "rate" property="success" value="0">
            <a href="DisplayExpSample.do?step=<%= i %>&sampleStatus=success" target="_blank"><bean:write name="rate" property="success"/></a>
        </logic:notEqual>           
    </td>
    <td width="14%" align="center">
        <logic:equal name = "rate" property="fail" value="0">
            <bean:write name="rate" property="fail"/>
        </logic:equal>     
        <logic:notEqual name = "rate" property="fail" value="0">
            <a href="DisplayExpSample.do?step=<%= i %>&sampleStatus=fail" target="_blank"><bean:write name="rate" property="fail"/></a>
        </logic:notEqual>      
    </td>
    <td width="14%" align="center">
        <logic:equal name = "rate" property="notdone" value="0">
            <bean:write name="rate" property="notdone"/>
        </logic:equal>     
        <logic:notEqual name = "rate" property="notdone" value="0">
            <a href="DisplayExpSample.do?step=<%= i %>&sampleStatus=notdone" target="_blank"><bean:write name="rate" property="notdone"/></a>
        </logic:notEqual>      
    </td>
    <td width="15%" align="center"><bean:write name="rate" property="curr_step_success_rate"/>&nbsp%</td>
    <td width="15%" align="center"><bean:write name="rate" property="overall_step_success_rate"/>&nbsp%</td>
  </tr>  
  <% } %>
  </logic:iterate>
</table>

</body>
</html>
