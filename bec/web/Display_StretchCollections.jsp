
<%@ page language="java" %>
<%@ page import="java.util.*" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.sequence.*" %>
<%@ page import="edu.harvard.med.hip.bec.ui_objects.*" %>
<html>
<head>
<script language="JavaScript" src="<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>

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
int forwardName_int = 0;
if (forwardName instanceof String) forwardName_int = Integer.parseInt((String)forwardName);
else if (forwardName instanceof Integer) forwardName_int = ((Integer) forwardName).intValue();

%>
   
    
</head>
<body>
<%  if ( request.getAttribute("caller") == null ) {%>
<jsp:include page="NavigatorBar_Administrator.jsp" />
<%}%>
	<p><P>
<br>

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<tr><td > <font color="#008000" size="5"><b> <%= title %></font>
<hr><p></td> </tr></table>

<div align="center">
  <center>  <table border="0" cellpadding="0" cellspacing="0" width="84%">
    <tr>      <td width="100%"><html:errors/></td>    </tr>
  </table>  </center>
</div>


<% 

Hashtable  stretch_collections = (Hashtable) request.getAttribute("stretch_collections");
ArrayList items = (ArrayList)request.getAttribute("items");
int item_type = Integer.parseInt( (String)request.getAttribute("item_type")); 
String item_title =  "Clone Id: "; 

%>

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<%
 
Hashtable ocalc_ids=new Hashtable();
StretchCollection strcol = null; 
ArrayList stretchColl = null;
for (int index = 0; index < items.size(); index ++)
{
    stretchColl = null;
   if (forwardName_int == Constants.STRETCH_COLLECTION_REPORT_INT)
    {
        strcol = (StretchCollection)stretch_collections.get((String) items.get(index));
        if ( strcol != null)
        {
            stretchColl = new ArrayList();
            stretchColl.add(strcol);
        }
    }
    else
    {
        stretchColl = (ArrayList)stretch_collections.get((String) items.get(index));
    }

%>
   <tr><td colspan='2'>&nbsp;</td></tr>
   <TR>     <td height='29' width="30%" bgColor="#b8c6ed" >
           <strong><font color="#000080"><%= item_title%></font></td>
           <td bgColor="#b8c6ed" ><strong><font color="#000080"><%= (String)items.get(index)%></font></strong></td></TR>
    <% if (  stretchColl != null )
{   %>

<tr> <td  ><strong>&nbsp;&nbsp; Reference Sequence Id: </strong></td>
<td   ><a href="#" onCLick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.REFSEQUENCE_DEFINITION_INT%>&amp;ID=<%= ((StretchCollection)stretchColl.get(0)).getRefSequenceId()%>','<%= ((StretchCollection)stretchColl.get(0)).getRefSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;" > <strong><%= ((StretchCollection)stretchColl.get(0)).getRefSequenceId()%></a></strong></td></TR>
<tr><td colspan='2'><P>

<%
 for (int col_count = 0; col_count < stretchColl.size() ; col_count++)
{ 
      strcol = (StretchCollection)stretchColl.get(col_count);
       %>

  <table width="95%" border="1" align="center" cellpadding="2" cellspacing="2">
        <th>Contig Name </th>
        <th>Contig Type</th>
        <th>Contig Id </th>
        <th>Cds Start </th>
        <th>Cds Stop</th>
        <th>LQR defined</th>
        <th>Alignment </th>
        <th>Discrepancy Report </th>
        <%

        UIRead contig   = null;
        for (int contig_count = 0; contig_count < strcol.getStretches().size(); contig_count++)
	  	{
	  		 contig  = (UIRead)strcol.getStretches().get(contig_count);
			%>
<tr>  <td width="15%"><%= contig.getName() %></td>
    <td ><%= Stretch.getStretchTypeAsString( contig.getType() ) %> </td>
     <td width="15%">
<% if (  contig.getType() == Stretch.GAP_TYPE_CONTIG)
{%>
 <A HREF="" onClick="window.open('<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.STRETCH_REPORT_INT%>&amp;ID=<%= contig.getId()%>','<%= contig.getId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;">
		 <%= contig.getId() %>	 </a>
<%} else {%> <%= contig.getId() %> <%}%>
</td>
   <td width="10%" align="right"><%= contig.getTrimStart() %></td>
  <td width="10%" align="right"><%= contig.getTrimStop() %></td>
    
<td width="10%">
<% 
if ( contig.getType() == Stretch.GAP_TYPE_GAP ){ %> &nbsp <%}
else 
    if (contig.getIsProperty()) {%> Yes  <%}
    else {%>  No  <%}%>
<td >



<% 
if ( contig.getType() == Stretch.GAP_TYPE_GAP )
{ %> &nbsp <%}
else if (contig.isAlignmentExists())
{%> <input type=BUTTON value=Alignment onClick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.READSEQUENCE_NEEDLE_ALIGNMENT_INT%>&amp;ID=<%= contig.getSequenceId()%>&amp;TYPE=<%= BaseSequence.CLONE_SEQUENCE %>&amp;<%=BaseSequence.THEORETICAL_SEQUENCE_STR%>=<%= strcol.getRefSequenceId ()%>','<%= "A"+contig.getSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;"><%}
else {%>Not available<%}%>		 </td>
<td ><% 
if ( contig.getType() == Stretch.GAP_TYPE_GAP )
{ %> &nbsp <%}
else 
if (contig.isDiscrepancies())
{%>		
<input type=BUTTON value="Discrepancy Report"  onClick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.ANALYZEDSEQUENCE_DISCREPANCY_REPORT_DEFINITION_INT%>&amp;ID=<%= contig.getSequenceId()%>','<%= "D"+contig.getSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;">
<%}
else {%>No discrepancies<%}%> 
</td>       </tr>       <%}%>      </table>

<P>
<%}%>



</td></tr>
<%}%>
 
<%}%>
</TABLE>

</body>
</html>