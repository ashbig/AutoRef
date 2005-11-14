<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<LINK REL=STYLESHEET       HREF="application_styles.css"      TYPE="text/css">
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
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.utility.*" %>

<%-- The container that was searched --%>

<html>

<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> Container Description  </font>
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
<% Container container = (Container)request.getAttribute("container") ;%>
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
    <td width="19%"><strong>Label: </strong></td>
    <td width="81%"> 
      <%= container.getLabel() %>
    </td>
  </tr>
  <tr> 
    <td><strong>Container Id: </strong></td>
    <td> 
      <%= container.getId() %>
    </td>
  </tr>
   <tr> 
    <td><strong>Container Type: </strong></td>
    <td> 
      <%= container.getType() %>
    </td>
  </tr>
  
  
  <tr> 
    <td><strong>Cloning Strategy: </strong></td>
    <td> 
    <% if ( container.getCloningStrategyId() != BecIDGenerator.BEC_OBJECT_ID_NOTSET){%> 
      <a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%= Constants.CLONING_STRATEGY_DEFINITION_INT %>&amp;ID=<%= container.getCloningStrategyId() %>">
	    <%= container.getCloningStrategyId() %></A>
	    <%} else {%> &nbsp; <%}%>
    </td>
  </tr>
  
  
</table>
<P><P></P></P>
<table border="1" cellpadding="0" cellspacing="0" width="84%" align=center>
    <tr class='headerRow'>
        <th >Position</th>
        <th >Type</th>
    <!--    <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Sample Id</font></strong></th> -->
        <th >Clone Id</th>
        <th> Clone Final Status</th>
        <th >Clone Process Status</th>
        <th >Refference Sequence</th>
	<th >Clone Sequence</th>
    </tr>
<%  
    String row_style = null;String clone_id = null;String refseq_id = null;
    String cloneseq_id = null;
	UICloneSample sample=null;
    for (int count = 0; count < container.getSamples().size(); count ++)
	{
		sample = (UICloneSample)container.getSamples().get(count);
		row_style = (count % 2 == 0) ? "'evenRow'" : "'oddRow'";// bgColor='#e4e9f8'";
		clone_id = (sample.getCloneId() > -1)? String.valueOf(sample.getCloneId() ): "&nbsp;";
                refseq_id = "<a href='#' onCLick=\"window.open('"+edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") +"Seq_GetItem.do?forwardName="+Constants.REFSEQUENCE_DEFINITION_INT +"&amp;ID="+ sample.getRefSequenceId()+"','"+ sample.getRefSequenceId()+"','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;\"> "+ sample.getRefSequenceId()+"</a>";
                refseq_id = (sample.getRefSequenceId() > -1)? refseq_id :  "&nbsp;";
                cloneseq_id = "<a href=\"#\" onCLick=\"window.open('"+ edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") +"Seq_GetItem.do?forwardName="+Constants.CLONE_SEQUENCE_DEFINITION_REPORT_INT+"&amp;ID="+ sample.getSequenceId()+"','"+ sample.getSequenceId() +"','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;\" >"+ sample.getSequenceId()+"</a>";
                cloneseq_id = ( sample.getSequenceId() > -1)? cloneseq_id :  "&nbsp;";
	%>
	<tr class=<%= row_style %>>
		<td width='10%'><%= sample.getPosition() %> </td>
		<td width='15%'> <%= sample.getSampleType()%></td>
                <td width='15%'align='right' > <%= clone_id %>      </td>
                <td > <%= IsolateTrackingEngine.getCloneFinalStatusAsString(sample.getCloneFinalStatus() )%></td>
               <td > <%= IsolateTrackingEngine.getStatusAsString( sample.getCloneStatus () )%></td>
		<td   align='right' width='15%'> <%= refseq_id %></td>
	        <td  align='right' width='15%'> <%= cloneseq_id%></td>
	</tr>
	<%}%>
  
     
    
    </table>
</body>
</html>