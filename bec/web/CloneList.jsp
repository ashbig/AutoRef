<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="java.util.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.mapping.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.endreads.*" %>
<%@ page import="edu.harvard.med.hip.bec.Constants" %>
<html>
<head>
<script language="JavaScript">
<!--
function SetChecked(e, val) {
  // Check all of the checkboxes in a group
  // Initialization
  var iElt, currElt, f;

    // Identify the form
   f = e.form;
    // Loop over all elements in the form
    for (iElt=0; iElt < f.elements.length; iElt++)
 {
      currElt = f.elements[iElt];	
      // If the element is one of the checkboxes in the group containing the checkbox which was just clicked...
      if (currElt.name == 'chkClone')
      {
       // Check the checkbox
        currElt.checked = val;
      }  // end if
    }  // end loop
  
}  // end of function CheckAllCheckboxesInGroup
-->
</script>
</head>
<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> <%= request.getAttribute(Constants.JSP_TITLE)%>  </font>
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
	
  </table>
  </center>
</div>
<p></p>
<% Container container = (Container)request.getAttribute("container") ;
int forwardName = Integer.parseInt( (String) request.getParameter("forwardName"));
%>
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
    <td width="19%"><strong>Container Label:</strong></td>
    <td width="81%"> 
      <%= container.getLabel() %>
    </td>
  </tr>
 <tr><TD colspan=2><input type="checkbox" name="all" value="All" onclick="SetChecked(this, this.checked)">Select All Clones
</td></TR>
 
  
  
</table>

 
 <html:form action="/RunProcess.do" > 
<input name="forwardName" type="hidden" value="<%= request.getAttribute("forwardName") %>" >
 <input name="containeLabel" type="hidden" value="<%= container.getLabel() %>" >
<P><P></P></P>
<table border="1" cellpadding="0" cellspacing="0" width="84%" align=center>
    <tr >
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Select Clone</font></strong></th>
       <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Position</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Sample Type</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Clone Id</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Flex Sequence Id</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Bec Sequence Id</font></strong></th>
	<th bgcolor="#1145A6"><strong><font color="#FFFFFF">Clone Status</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Clone Rank</font></strong></th>
    </tr>
<%  
    String row_colorB = " bgColor='#e4e9f8'"; String row_colorA =" bgColor='#b8c6ed'";
	String row_color = " bgColor='#e4e9f8'";
	Sample sample=null;
	IsolateTrackingEngine iso = null;FlexInfo fl = null;
	int constructid = -1;
    for (int count = 0; count < container.getSamples().size(); count ++)
	{
	
		sample = (Sample)container.getSamples().get(count);
                if ( sample.getType().equals("ISOLATE") )
		{
                    iso = sample.getIsolateTrackingEngine();
                    fl = iso.getFlexInfo();
                    if ((forwardName == Constants.PROCESS_ACTIVATE_CLONES && iso.getStatus() < 0)
                    ||
                    (forwardName == Constants.PROCESS_PUT_CLONES_ON_HOLD && iso.getStatus() >-1))
                    {
                       if ( sample.getType().equals("CONTROL_POSITIVE") ||  sample.getType().equals("CONTROL_NEGATIVE"))
                        {
                          row_color = " bgColor=blue";
                        }
                        else if (constructid != iso.getConstructId())
                        {
                                if (row_color.equals(row_colorA))
                                {
                                        row_color = row_colorB;
                                        }
                                        else
                                        {
                                        row_color = row_colorA;
                                        }
                        }

                        constructid = iso.getConstructId();
	%>
	<tr>
                <td <%= row_color %>> 
<% if ( fl != null ) {%>
&nbsp; <input type="checkbox" name="chkClone" value='<%= iso.getId()%>'>    &nbsp;
   <%}else{%> &nbsp;<%}%></td>
		<td <%= row_color %> align="center"><%= sample.getPosition() %> </td>
		<td <%= row_color %> align="center"> <%= sample.getType()%></td>
		<td <%= row_color %> align="center"><% if ( fl != null ) {%> <%= fl.getFlexCloneId()%> <%}else{%> &nbsp;<%}%></td>
		<td <%= row_color %> align="center"><% if (fl != null){%>	<%= fl.getFlexSequenceId()%> <%}else{%>&nbsp; <%}%></td>
	   <td <%= row_color %>><% if (sample.getRefSequenceId() == -1)
	{%>
	&nbsp;
	<%}else{%>
		<a href="#" onCLick="window.open('/BEC/Seq_GetItem.do?forwardName=<%=Constants.REFSEQUENCE_DEFINITION_INT%>&amp;ID=<%= sample.getRefSequenceId()%>','<%= sample.getRefSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;" > 
		<%= sample.getRefSequenceId()%></a>
		
	<%}%>	</td>
		<td <%= row_color %>><% if ( iso != null ) {%> <%= iso.getStatusAsString()%> <%}else{%> &nbsp;<%}%></td>
                <td <%= row_color %>><% if ( iso != null && iso.getRank()>0) {%> <%= iso.getRank()%> <%}else{%> &nbsp;<%}%></td>

	</tr>
	<%}}}%>
    </table>
<P><P>
<div align="center"> <input type="SUBMIT"/></DIV>
</html:form> 
</body>
</html>