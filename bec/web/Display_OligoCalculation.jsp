
<%@ page language="java" %>
<%@ page import="java.util.*" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.oligo.*" %>
<html>
<head>
 <title> <bean:message key="bec.name"/> : <%=Constants.JSP_TITLE%></title>
  
<%
        Object forwardName = null;
        if ( request.getAttribute("forwardName") != null)
        {
                forwardName = request.getAttribute("forwardName") ;
        }
        else
        {
                forwardName = request.getParameter("forwardName") ;
        }
	 Object title = null;
	 if (request.getAttribute(Constants.JSP_TITLE ) == null)
	 { 
	 	title =  request.getParameter(  Constants.JSP_TITLE  );
	}
	else
	{
		title = request.getAttribute( Constants.JSP_TITLE );
	}

%>
   
    
</head>
<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
    <tr>
        
    <td > <font color="#008000" size="5"><b> <%= title %></font>
<hr>
    
    <p>
    </td>
    </tr>
</table>

<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="84%">
    <tr>
      <td width="100%"><html:errors/></td>
    </tr>
  </table>
  </center>
</div>
<% 

ArrayList  oligo_calculations = (ArrayList) request.getAttribute("oligo_calculations");
ArrayList items = (ArrayList)request.getAttribute("items");
int item_type = Integer.parseInt( (String)request.getAttribute("item_type")); 

String item_title = null;
            switch (item_type)
            {
                case  Constants.ITEM_TYPE_CLONEID:
                    {item_title = "Clone Id: "; break;}
                case Constants.ITEM_TYPE_PLATE_LABELS :
                    {item_title = "Plate: "; break;}
                case Constants.ITEM_TYPE_BECSEQUENCE_ID :
                    {item_title = "BEC Reference Sequence Id: "; break;}
                case Constants.ITEM_TYPE_FLEXSEQUENCE_ID:
                    {item_title = "FLEX Sequence Id: "; break;}
            }

%>

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<% for (int index = 0; index < items.size(); index ++)
{%>
 <tr><td colspan='2'>&nbsp;</td></tr>
  <TR>     <td height='29' width="30%"bgColor="#b8c6ed" >
           <strong><font color="#000080"><%= item_title%></font></td>
           <td bgColor="#b8c6ed" ><strong><font color="#000080"><%= (String)items.get(index)%></font></strong></td></TR>
    <% 
    ArrayList ol_per_item = (ArrayList) oligo_calculations.get(index);
    for (int oligo_c_count = 0; oligo_c_count < ol_per_item.size();oligo_c_count++)
    {
        OligoCalculation olc = (OligoCalculation) ol_per_item.get(oligo_c_count);
    %>
    <tr><td ><strong>Primer3 Specification: </strong></td>
<td   ><strong>
<a href="/BEC/Seq_GetSpec.do?forwardName=<%= olc.getPrimer3SpecId()  * Spec.SPEC_SHOW_SPEC %> "> <%= olc.getPrimer3SpecId()%></a></strong></td></TR>
    <tr> <td  ><strong>Reference Sequence Id: </strong></td>
<td   ><a href="#" onCLick="window.open('/BEC/Seq_GetItem.do?forwardName=<%=Constants.REFSEQUENCE_DEFINITION_INT%>&amp;ID=<%= olc.getSequenceId()%>','<%=olc.getSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;" > <strong><%= olc.getSequenceId()%></a></strong></td></TR>
    <tr><td colspan='2'>
<table border='1' cellpadding="2" cellspacing="2" width="100%" align=center>
<Tr>
<th bgcolor="#1145A6" width="5%"><strong><font color="#FFFFFF">Name</font></strong></th>
<th bgcolor="#1145A6" ><strong><font color="#FFFFFF">Sequence</font></strong></th>
<th bgcolor="#1145A6" width="5%"><strong><font color="#FFFFFF">Tm</font></strong></th>
<th bgcolor="#1145A6" width="5%"><strong><font color="#FFFFFF">Position</font></strong></th>
<th bgcolor="#1145A6" width="15%"><strong><font color="#FFFFFF">Orientation</font></strong></th>
<th bgcolor="#1145A6" width="25%"><strong><font color="#FFFFFF">Status</font></strong></th>
<th bgcolor="#1145A6" width="25%"><strong><font color="#FFFFFF">Submission Type</font></strong></th>
<th bgcolor="#1145A6"><strong><font color="#FFFFFF">Submitter</font></strong></th>
</tr>
    <%  
String row_color = "bgColor='#e4e9f8'";
for (int oligo_count = 0; oligo_count < olc.getOligos().size();oligo_count++)
    {
        Oligo ol = (Oligo) olc.getOligos().get(oligo_count);
        if ( oligo_count % 2 == 0)
             row_color = "bgColor='#e4e9f8'";
        else
            row_color = "bgColor='#b8c6ed'";
    %>
<tr>
<td  ><strong><% if ( ol.getName() != null){%> <%= ol.getName() %> <%}else{%> &nbsp; <%}%></td>

<td  ><strong><%=ol.getSequence()%></strong></td>
<td  ><strong><%=ol.getTm()%></strong></td>
<td  ><strong><%=ol.getPosition()%></strong></td>
<td  ><strong><%=ol.getOrientationAsString()%></strong></td>
<td  ><strong><%=ol.getStatusAsString()%></strong></td>
<td  ><strong><%=ol.getTypeAsString()%></strong></td>
<td  ><strong><% if (ol.getSubmitterId() != -1){%><%= ol.getSubmitterId() %><%}else{%>System<%}%></strong></td>
</tr>
    <%}%>
</TABLE>
</td></tr><tr><td colspan=2>&nbsp;</td></tr>
<%}%>
 
<%}%>
</TABLE>

 
</body>
</html>