<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Select success criteria </title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><font color="#000066"><bean:message key="flex.name"/> : Select success criteria </font></h2>      
<hr>
<html:errors/>
<p>
<html:form action="/SelectSucCriteria.do">

<input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
<input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">
<input type="hidden" name="projectname" value="<bean:write name="projectname"/>">
<input type="hidden" name="workflowname" value="<bean:write name="workflowname"/>">
<input type="hidden" name="cloneFormat" value="<bean:write name="cloneFormat"/>">
<input type="hidden" name="processname" value="<bean:write name="processname"/>">
<input type="hidden" name="initial_date_from" value="<bean:write name="initial_date_from"/>">
<input type="hidden" name="initial_date_to" value="<bean:write name="initial_date_to"/>">

<table>
  <tbody>
    <tr>
      <td class="prompt" bgcolor="#E1E1FF"><font color="#000066">Project name:</font></td>
      <td bgcolor="#E1E1FF"><font color="#000066">&nbsp;&nbsp;&nbsp; <bean:write name="projectname" /></font></td>
    </tr>
    <tr>
      <td class="prompt" bgcolor="#E1E1FF"><font color="#000066">Workflow name:</font></td>
      <td bgcolor="#E1E1FF"><font color="#000066">&nbsp;&nbsp;&nbsp; <bean:write name="workflowname" /></font></td>
    </tr>
    <tr>
      <td class="prompt" bgcolor="#E1E1FF"><font color="#000066">Clone format:</font></td>
      <td bgcolor="#E1E1FF">
        <font color="#000066">&nbsp;&nbsp;&nbsp;           
          <logic:equal name="cloneFormat" value="ALL">
            CLOSED and FUSION
          </logic:equal>
          <logic:notEqual name="cloneFormat" value="ALL">
            <bean:write name="cloneFormat" />
          </logic:notEqual>
        </font>
      </td>
    </tr>
    <tr>
      <td class="prompt" bgcolor="#E1E1FF"><font color="#000066">Process:</font></td>
      <td bgcolor="#E1E1FF">
        <font color="#000066">&nbsp;&nbsp;&nbsp;           
          <logic:equal name="processname" value="PCR_GEL"> Run PCR Gel </logic:equal>
          <logic:equal name="processname" value="AGAR_PLATE"> Generate Agar Plates </logic:equal>
          <logic:equal name="processname" value="CULTURE_PLATE"> Generate Culture Plates </logic:equal>
        </font>
      </td>
    </tr>
    <tr>
      <td class="prompt" bgcolor="#E1E1FF"><font color="#000066">Oligo received date for Sequences:</font></td>
      <td bgcolor="#E1E1FF">
        <font color="#000066">&nbsp;&nbsp;&nbsp;         
        <logic:equal name="initial_date_from" value=""> Any time </logic:equal>
        <logic:notEqual name="initial_date_from" value="">  
          from &nbsp; <bean:write name="initial_date_from" /> &nbsp; to &nbsp; <bean:write name="initial_date_to" />         
        </logic:notEqual>
        </font>
      </td>
    </tr>
    
    <% int workflowid = Integer.parseInt(request.getParameter("workflowid")); 
    if(! (workflowid == 4 || workflowid == 11)) {
    %>
    <tr>
      <td class="prompt" vAlign="top" align="left" width="351" bgcolor="#E1E1FF"><font color="#000066">Success
        criteria for the step of PCR GEL:</font></td>
      <td vAlign="top" align="left" width="338" bgcolor="#E1E1FF">
        <font color="#000066">&nbsp;&nbsp;
          <html:multibox property="pcr_succ_criteria" value="Correct"/> Correct <br> &nbsp;&nbsp; 
          <html:multibox property="pcr_succ_criteria" value="Multiple w/ Correct"/> Multiple w/ Correct <br> &nbsp;&nbsp;          
          <html:multibox property="pcr_succ_criteria" value="No visible band with cloning attempt"/> 
            No visible band with cloning attempt <br> &nbsp;&nbsp;
          <html:multibox property="pcr_succ_criteria" value="Multiple without correct"/> Multiple without correct <br> &nbsp;&nbsp;
          <html:multibox property="pcr_succ_criteria" value="No product"/> No product <br> &nbsp;&nbsp;          
          <html:multibox property="pcr_succ_criteria" value="No product"/> Incorrect <br> 
          &nbsp;
        </font>
      </td>
    </tr>
    <% } %>

    <logic:equal name="processname" value="AGAR_PLATE">
    <tr>
      <td class="prompt" vAlign="top" align="left" width="351" bgcolor="#E1E1FF"><font color="#000066">The
        minimum number of colonies in the step of<br>
        AGAR PLATE to be considered as success:<br>
        &nbsp;</font></td>
      <td vAlign="top" align="left" width="338" bgcolor="#E1E1FF">
      <font color="#000066">&nbsp;&nbsp;&nbsp;
        <html:select property="agar_plate_succ_criteria">
            <html:option key="4" value="4"/>
            <html:option key="3" value="3"/>
            <html:option key="2" value="2"/>
            <html:option key="1" value="1"/>
        </html:select>
      </font>
      </td>
    </tr>
    </logic:equal>

    <logic:equal name="processname" value="CULTURE_PLATE">
    <tr>
      <td class="prompt" vAlign="top" align="left" width="351" bgcolor="#E1E1FF"><font color="#000066">The
        minimum number of colonies in the step of<br>
        AGAR PLATE to be considered as success:<br>
        &nbsp;</font></td>
      <td vAlign="top" align="left" width="338" bgcolor="#E1E1FF">
      <font color="#000066">&nbsp;&nbsp;&nbsp;
        <html:select property="agar_plate_succ_criteria">
            <html:option key="4" value="4"/>
            <html:option key="3" value="3"/>
            <html:option key="2" value="2"/>
            <html:option key="1" value="1"/>
        </html:select>
      </font>
      </td>
    </tr>
    <tr>
      <td class="prompt" vAlign="top" align="left" width="351" bgcolor="#E1E1FF"><font color="#000066">The
        minimum number of growing isolates<br>
        per clone to be considered as success<br>
        in the step of CULTURE PLATE:<br>
        &nbsp;</font></td>
      <td vAlign="top" align="left" width="338" bgcolor="#E1E1FF"><font color="#000066">&nbsp;&nbsp;&nbsp;
        <html:select property="culture_plate_succ_criteria">
            <html:option key="1" value="1"/>
            <html:option key="2" value="2"/>
            <html:option key="3" value="3"/>
            <html:option key="4" value="4"/>
        </html:select>
      </font></td>
    </tr>
    </logic:equal>

    <tr>
      <td class="prompt" bgcolor="#E1E1FF"><font color="#000066"><br>
        &nbsp; <html:submit property="submit" value="Continue"/>
        <br>
        &nbsp;</font></td>
      <td vAlign="bottom" align="left" bgcolor="#E1E1FF">&nbsp;</td>
    </tr>
  </tbody>
</table>



</html:form>

</body>
</html>
