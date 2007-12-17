<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

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
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<html>
<LINK REL=StyleSheet HREF="application_styles.css" TYPE="text/css" MEDIA=screen>

<body>

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
    <td width="19%"><strong>Label:</strong></td>
    <td width="81%"> 
      <%= container.getLabel() %>
    </td>
  </tr>
  <tr> 
    <td><strong>Plate ID:</strong></td>
    <td> 
      <%= container.getId() %>
    </td>
  </tr>
   <tr> 
    <td><strong>Plate Type:</strong></td>
    <td> 
      <%= container.getType() %>
    </td>
  </tr>
  
  
  <tr> 
    <td><strong>Cloning Strategy:</strong></td>
    <td> &nbsp;
        <% if ( container.getCloningStrategyIdAsInt() != BecIDGenerator.BEC_OBJECT_ID_MULTIPAL_VALUES )
     {%> <a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION")  %>Seq_GetItem.do?forwardName=<%= Constants.CLONING_STRATEGY_DEFINITION_INT %>&amp;ID=<%= container.getCloningStrategyIdAsInt() %>">
	    <%= container.getCloningStrategyIdAsInt() %></A><%}%>
    </td>
  </tr>
  
  
</table>
<P><P></P></P>
<table border="1" cellpadding="0" cellspacing="0" width="90%" align=center>
    <tr class='headerRow' >
       <td >Well</td>
        <td >Type</td>
        <td >Forward End Read</td>
        <td >Reverse End Read</td>
        <td >Assembled Sequence</td>
	<td >Clone Status</td>
    </tr>
<%  
String[] row_class = {"evenRow","oddRow"} ; int row_count = 0;
	Sample sample=null;
Result result = null;
String forward_read_status = "";String reverse_read_status = "";
    for (int count = 0; count < container.getSamples().size(); count ++)
	{
		sample = (Sample)container.getSamples().get(count);
                 forward_read_status = "&nbsp"; reverse_read_status = "&nbsp";
if (sample.getResults() != null)
{
                for (int result_count = 0; result_count < sample.getResults().size(); result_count++)
                {
                        result = (Result)sample.getResults().get(result_count);
                       switch (result.getType())
                        {
                            case Result.RESULT_TYPE_ENDREAD_FORWARD:{ forward_read_status = "NOT PROCESSED"; break;}

                            case Result.RESULT_TYPE_ENDREAD_FORWARD_PASS:{ forward_read_status="Pass"; break;}
                            case Result.RESULT_TYPE_ENDREAD_FORWARD_FAIL:{ forward_read_status="Fail";break;}

                            case Result.RESULT_TYPE_ENDREAD_REVERSE_FAIL :{ reverse_read_status = "Fail"; break;}
                            case Result.RESULT_TYPE_ENDREAD_REVERSE_PASS: { reverse_read_status="Pass"; break;}
                            case Result.RESULT_TYPE_ENDREAD_REVERSE  :{ reverse_read_status="NOT PROCESSED";break;}
                        }
                        
                }
}
	%>
	<tr class=<%= row_class[row_count++ % 2] %> >

		<td > <%= sample.getPosition() %> </td>
		<td > <%= sample.getType()%></td>
		<td > <%= forward_read_status%> </td>
                <td> <%= reverse_read_status%></td>
                <td >
                    <%if(sample.getIsolateTrackingEngine() != null && sample.getIsolateTrackingEngine().getAssemblyStatus() == IsolateTrackingEngine.ASSEMBLY_STATUS_PASS) 
                    {%>Clone Sequence Assembled  <%}
                    else if(sample.getIsolateTrackingEngine() != null &&  sample.getIsolateTrackingEngine().getAssemblyStatus() == IsolateTrackingEngine.ASSEMBLY_STATUS_CONFIRMED) 
                    {%> Clone Sequence Confirmed <%}
                    else
                    {%> &nbsp; <%}%>
</td>
		<td >
<% if (sample.getIsolateTrackingEngine() != null )
{%><%= sample.getIsolateTrackingEngine().getStatusAsString()%><%}
else{%>&nbsp;<%}%>
</td></tr>
	<%}%>
  
     
    
    </table>
    
    
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