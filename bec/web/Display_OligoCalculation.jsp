
<%@ page language="java" %>
<%@ page import="java.util.*" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.oligo.*" %>
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


<% if (forwardName_int == -Constants.PROCESS_APPROVE_INTERNAL_PRIMERS)
{%><form action="RunProcess.do" ><%}%>

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
                case Constants.ITEM_TYPE_ACE_REF_SEQUENCE_ID :
                    {item_title = "ACE Reference Sequence Id: "; break;}
                case Constants.ITEM_TYPE_FLEXSEQUENCE_ID:
                    {item_title = "FLEX Sequence Id: "; break;}
            }
int cloneid = 0;
%>

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<%
 
Hashtable ocalc_ids=new Hashtable();
for (int index = 0; index < items.size(); index ++)

{
    cloneid = Integer.parseInt( (String)items.get(index) );%>
 <tr><td colspan='2'>&nbsp;</td></tr>
  <TR>     <td height='29' width="30%"bgColor="#b8c6ed" >
           <strong><font color="#000080"><%= item_title%></font></td>
           <td bgColor="#b8c6ed" ><strong><font color="#000080"><%= cloneid%></font></strong></td></TR>
    <% 
    ArrayList ol_per_item = (ArrayList) oligo_calculations.get(index);
    for (int oligo_c_count = 0; oligo_c_count < ol_per_item.size();oligo_c_count++)
    {
        OligoCalculation olc = (OligoCalculation) ol_per_item.get(oligo_c_count);
        String chkName = "chkPrimer"+olc.getId();
        
    %>
<!--<% if (forwardName_int == -Constants.PROCESS_APPROVE_INTERNAL_PRIMERS)
{%>   <tr><td colspan=2><strong>&nbsp;&nbsp;  <input type="radio" name ="<%= olc.getSequenceId()%>" > 
Select Oligo Set </strong></td></tr><%}%> -->
<% if (forwardName_int == -Constants.PROCESS_APPROVE_INTERNAL_PRIMERS)
{%><tr><td><strong>&nbsp;&nbsp;<input type=checkbox name=<%= chkName %> value=""  onclick="SetChecked(this, this.checked, '<%= chkName %>')" >Change Status</strong></td></tr><%}%>
    <tr><td ><strong>&nbsp;&nbsp;  Primer3 Specification: </strong></td>
<td   ><strong>
<a href="<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION")%>Seq_GetSpec.do?forwardName=<%= olc.getPrimer3SpecId()  * Spec.SPEC_SHOW_SPEC %> "> <%= olc.getPrimer3SpecId()%></a></strong></td></TR>
    <tr> <td  ><strong>&nbsp;&nbsp; Reference Sequence Id: </strong></td>
<td   ><a href="#" onCLick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.REFSEQUENCE_DEFINITION_INT%>&amp;ID=<%= olc.getSequenceId()%>','<%=olc.getSequenceId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;" > <strong><%= olc.getSequenceId()%></a></strong></td></TR>
<% if (olc.getStretchCollectioId() > 0)
{%>
<tr><td   ><strong>
<strong>&nbsp;&nbsp; Gaps/LQR Collection Id: </strong>
<td   ><a href="#" onCLick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%=Constants.STRETCH_COLLECTION_REPORT_INT%>&amp;ID=<%= olc.getStretchCollectioId() %>&amp;cloneid=<%=cloneid%>','<%=olc.getStretchCollectioId()%>','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;" > <strong><%= olc.getStretchCollectioId()%></a></strong></td></TR>
<%}%>
 
<% if ( ocalc_ids.containsKey(new Integer(olc.getId()) ))
{%><tr><td colspan=2>See Primers above</td></tr><% continue;}%>  
 <tr><td colspan='2'>
<table border='1' cellpadding="2" cellspacing="2" width="100%" align=center>
<Tr>
<% if (forwardName_int == -Constants.PROCESS_APPROVE_INTERNAL_PRIMERS)
{%><th bgcolor="#1145A6" width="5%"><strong><font color="#FFFFFF"></font></strong></th><%
}%>
<th bgcolor="#1145A6" width="5%"><strong><font color="#FFFFFF">Name</font></strong></th>
<th bgcolor="#1145A6" ><strong><font color="#FFFFFF">Sequence</font></strong></th>
<th bgcolor="#1145A6" width="5%"><strong><font color="#FFFFFF">Tm</font></strong></th>
<th bgcolor="#1145A6" width="5%"><strong><font color="#FFFFFF">Position</font></strong></th>
<th bgcolor="#1145A6" width="15%"><strong><font color="#FFFFFF">Orientation</font></strong></th>
<th bgcolor="#1145A6" width="25%"><strong><font color="#FFFFFF">Status</font></strong></th>
<th bgcolor="#1145A6" width="25%"><strong><font color="#FFFFFF">Submission Type</font></strong></th>
<th bgcolor="#1145A6" width="5%"><strong><font color="#FFFFFF">Submitter</font></strong></th>
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
<% if (forwardName_int == -Constants.PROCESS_APPROVE_INTERNAL_PRIMERS)
{%><td><input type=checkbox name=chkPrimer id="<%= chkName %>" value=<%=ol.getId() %> <%if (ol.getStatus() != Oligo.STATUS_DESIGNED){%> checked <%}%> ></td><%
}%>
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
<%ocalc_ids.put(new Integer(olc.getId()), "");}%>
 
<%}%>
</TABLE>
<% if (forwardName_int == -Constants.PROCESS_APPROVE_INTERNAL_PRIMERS)
{%><div align="center">  <p> <input type="submit" value="Submit" > </div>
<input name="forwardName" type="hidden" value="<%= forwardName %>" >
 <input name="forwardAllApprovedPrimerIds" type="hidden" value="<%= request.getAttribute("forwardAllApprovedPrimerIds") %>" ><%}%>
 
</body>
</html>