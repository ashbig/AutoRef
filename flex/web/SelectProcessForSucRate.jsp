<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Select Process and Clone Format</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
<h2><bean:message key="flex.name"/> : Select Process and Clone Format</h2>
<hr>
<html:errors/>
<p>

<html:form action="/SelectProcessForSucRate.do">

  <input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
  <input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">
  <input type="hidden" name="projectname" value="<bean:write name="projectname"/>">
  <input type="hidden" name="workflowname" value="<bean:write name="workflowname"/>">

  <table width="507" height="245">
    <tbody>
      <tr>
        <td class="prompt" width="191" height="36">Project name:</td>
        <td width="217" height="36"><bean:write name="projectname" /></td>
      </tr>
      <tr>
        <td class="prompt" width="191" height="33">Workflow name:</td>
        <td width="217" height="33"><bean:write name="workflowname" /></td>
      </tr>
    
    <% int workflowid = Integer.parseInt(request.getAttribute("workflowid").toString()); 
    if( workflowid == 4 || workflowid == 11 ) {
    %>
      <tr>
        <td class="prompt" width="191" height="45">Select process name:</td>
        <td width="217" height="45">    
          <html:select property="processname">          
          <html:option value="AGAR_PLATE"> Generate Agar Plates </html:option>
          <html:option value="CULTURE_PLATE"> Generate Culture Plates </html:option>
          </html:select>
        </td>
      </tr>
    <% 
    } 
    else{
    %>
      <tr>
        <td class="prompt" width="191" height="45">Select process name:</td>
        <td width="217" height="45">    
          <html:select property="processname">
          <html:option value="PCR_GEL"> Run PCR Gel </html:option> 
          <html:option value="AGAR_PLATE"> Generate Agar Plates </html:option>
          <html:option value="CULTURE_PLATE"> Generate Culture Plates </html:option>
          </html:select>
        </td>
      </tr>
     <% } %>

      <tr>
        <td class="prompt" width="191" height="44">Select clone format:</td>
        <td width="217" height="44">
          <html:select property="cloneFormat">
          <html:option value="CLOSED"> CLOSED </html:option>
          <html:option value="FUSION"> FUSION </html:option>
          <html:option value="ALL"> BOTH </html:option>
          </html:select>
        </td>
      </tr>
      <tr>
        <td class="prompt" width="191" valign="top" height="67"><br>
          Oligo received date for Sequences:</td>
        <td width="296" height="67"><br>
          From: 
          <html:select property="initial_month_from">
            <html:options
              name="month_collection"
            />
          </html:select>
          <html:select property="initial_day_from">
            <html:options
              name="day_collection"
            />
          </html:select>
          <html:select property="initial_year_from">
            <html:options
              name="year_collection"
            />
          </html:select>
          <br>
          <br>
          To:&nbsp;&nbsp;&nbsp;&nbsp; 
          <html:select property="initial_month_to">
            <html:options
              name="month_collection"
            />
          </html:select>
          <html:select property="initial_day_to">
            <html:options
              name="day_collection"
            />
          </html:select>
          <html:select property="initial_year_to">
            <html:options
              name="year_collection"
            />
          </html:select>
        </td>
      </tr>
      <tr>
        <td class="prompt" width="191" height="67"><html:submit property="submit" value="Continue"/></td>
        <td width="296" height="67"></td>
      </tr>
    </tbody>
  </table>


</html:form>
</body>
</html>