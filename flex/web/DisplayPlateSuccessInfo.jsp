<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><title>Display plates success information</title></head>
<body>

<h2><font color="#000066">FLEXGene : Display Plate Statistics</font></h2>
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

    <logic:equal name = "sample_type" value = "AGAR">
    <tr>
      <td class="prompt"><b><font color="#000066">Sucess criteria for the step of Agar Plate:</font></b></td>
      <td><font color="#000066">&nbsp;&nbsp;&nbsp; 
          <bean:write name = "agar_plate_succ_criteria"/></font>
      </td>
    </tr>  
    </logic:equal>

    <logic:equal name = "sample_type" value = "ISOLATE">
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

  </tbody>
</table>

<br><br>



<table border="1" cellpadding="0" cellspacing="0" width="80%">
  <tr>
    <td width="30%" align="center" bgcolor="#E1E1FF">Label</td>
    <td width="17%" align="center" bgcolor="#E1E1FF">Total #</td>
    <td width="17%" align="center" bgcolor="#E1E1FF">Success #</td>
    <td width="17%" align="center" bgcolor="#E1E1FF">Failure #</td>
    <td width="19%" align="center" bgcolor="#E1E1FF">Rate(%)</td>
  </tr>
  <logic:iterate id="plate" name="plate_success_info">
  <tr>
    <td width="30%" align="center">
        <a href="ViewContainerDetails.do?CONTAINER_BARCODE=<bean:write name="plate" property="label"/>">
        <bean:write name="plate" property="label"/></a>
    </td>
    <td width="17%" align="center"><bean:write name="plate" property="total"/></td>
    <td width="17%" align="center"><bean:write name="plate" property="success"/></td>
    <td width="17%" align="center"><bean:write name="plate" property="fail"/></td>   
    <td width="19%" align="center"><bean:write name="plate" property="rate"/> &nbsp; %</td>
  </tr>
  </logic:iterate>
 
</table>



</body>
</html>
