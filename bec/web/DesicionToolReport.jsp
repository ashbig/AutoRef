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
<%@ page import="edu.harvard.med.hip.bec.ui_objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.endreads.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.sequence.*" %>
<%@ page import="edu.harvard.med.hip.bec.Constants" %>
<html>
<head>

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
<% ArrayList clone_info = (ArrayList)request.getAttribute("clone_data") ;
%>


<table border="1" cellpadding="0" cellspacing="0" width="84%" align=center>
    <tr >
        <th bgcolor="#1145A6" width="15%"><strong><font color="#FFFFFF">Plate</font></strong></th>
       <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Position</font></strong></th>
       <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Sample Type</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Clone Id</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Clone Status</font></strong></th>
         <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Sequence ID</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Sequence Status</font></strong></th>
        <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Discrepancy Report</font></strong></th>
	<th bgcolor="#1145A6"><strong><font color="#FFFFFF">Quality</font></strong></th>
    </tr>
<%  
    String row_colorB = " bgColor='#e4e9f8'"; String row_colorA =" bgColor='#b8c6ed'";
	String row_color = " bgColor='#e4e9f8'";
	
	int constructid = -1;
        UICloneSample clone = null;
    for (int count = 0; count < clone_info.size(); count ++)
	{
	
		clone = (UICloneSample)clone_info.get(count);

                if ( clone.getSampleType().equals("CONTROL"))
                {
                  row_color = " bgColor=blue";
                }
                else if ( clone.getSampleType().equals("ISOLATE") )
		{
                    if (constructid != clone.getConstructId())
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

                   constructid = clone.getConstructId();
                }
	%>
	<tr>
                <td <%= row_color %>> <%= clone.getPlateLabel () %>  </td>
		<td <%= row_color %> align="center"><%= clone.getPosition() %> </td>
		<td <%= row_color %> align="center"> <%= clone.getSampleType() %></td>
                <td <%= row_color %> align="center"> 
<% if (clone.getCloneId () == 0 ){%> &nbsp; <%}else{%>  <%= clone.getCloneId () %> <%}%></td>
                <td <%= row_color %> align="center">

 <%= IsolateTrackingEngine.getStatusAsString( clone.getCloneStatus () ) %> </td>
                <td <%= row_color %> align="center">
                <% if (clone.getSequenceId() > 0)
                {%>	<a href="#" onCLick="window.open('/BEC/Seq_GetItem.do?forwardName=<%=Constants.CLONE_SEQUENCE_DEFINITION_REPORT_INT%>&amp;ID=<%= clone.getSequenceId()%>','<%= clone.getSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;" > 
		<%= clone.getSequenceId()%></a> <%}else{%>&nbsp; <%}%></td>
                <td <%= row_color %> align="center">
             <% if ( clone.getSequenceId () > 0){ %> 
            <%=BaseSequence.getSequenceAnalyzedStatusAsString(clone.getSequenceAnalisysStatus ()) %> <%}else{%>&nbsp; <%}%></td>
                <td  <%= row_color %> align="center">
 <% if (clone.getSequenceAnalisysStatus ()==BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_YES_DISCREPANCIES ){%>
<input type=BUTTON value="Report"  onClick="window.open('/BEC/Seq_GetItem.do?forwardName=<%=Constants.ANALYZEDSEQUENCE_DISCREPANCY_REPORT_DEFINITION_INT%>&amp;ID=<%= clone.getSequenceId()%>','<%= clone.getSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;">
<%}%> &nbsp;</td>
                <td <%= row_color %> align="center"><% if ( clone.getCloneQuality() == -1){ %> &nbsp; 
<%}else{%><%=BaseSequence.getSequenceQualityAsString(clone.getCloneQuality()) %> <%}%></td>

	</tr>
	<%}%>
    </table>
<P><P>
<!--<div align="center"> <input type="SUBMIT"/></DIV> -->
</html:form> 
</body>
</html>