<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.util_objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.ui_objects.*" %>
<html>

<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> container Process History  </font>
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
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<tr><td>
<%  
  
    int item_type = Integer.parseInt( (String)request.getAttribute("item_type"));
    String item_title ="Id:";
    if (item_type == Constants.ITEM_TYPE_CLONEID)
        item_title ="Clone "+item_title;
    String row_color_even =" bgColor='#e4e9f8'";
    String row_color_odd =" bgColor='#b8c6ed'";
    ArrayList items = (ArrayList) request.getAttribute("process_items");
    ItemHistory history = null;
 ProcessHistory pr_history = null; String row_color = null;
   for (int index = 0; index < items.size(); index ++)
   {
         history = (ItemHistory)items.get(index);
        %>
         <table border='0' width="100%">        <TR><TD>    <b> <%= item_title + " "+ history.getItemId()%>    </b>       </TD></TR>
         <TR><TD>
        <%
        if ( history.getStatus() == ItemHistory.HISTORY_PROCESSED)
        {%>
             <table border="0" cellpadding="0" cellspacing="0" width="90%" align=center>
            <tr >
               <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Protocol</font></strong></th>
                <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Specification</font></strong></th>
                <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Execution Date</font></strong></th>
                <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Researcher</font></strong></th>
            </tr>
<%
    for (int count = 0; count < history.getHistory().size(); count ++)
	{
		pr_history = (ProcessHistory)history.getHistory().get(count);
		if (count % 2 == 0)
                    row_color =row_color_even;
		else
		   row_color =row_color_odd;
%>
	<tr>
        	<td <%= row_color %>> <%= pr_history.getName() %> </td>
		<td <%= row_color %> >
		<% 
		for (int s = 0; s < pr_history.getConfigs().size();s++)
		{
			UIConfigItem cs=(UIConfigItem)pr_history.getConfigs().get(s);
		
		if ( cs.getType() == Spec.PRIMER3_SPEC_INT ||  cs.getType() == Spec.END_READS_SPEC_INT 
		||  cs.getType() == Spec.FULL_SEQ_SPEC_INT ||  cs.getType() == Spec.POLYMORPHISM_SPEC_INT )
		{%>
			<a href="#" onCLick="window.open('/BEC/Seq_GetSpec.do?forwardName=<%= cs.getId()  * Spec.SPEC_SHOW_SPEC %>' ,'newWndRefseqNt','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;"> 
			 <%=cs.getId() %> </a>
		<%}
		else
		{%>
		&nbsp;
		<%}}%>
		</td>
		<td <%= row_color %>>	<%= pr_history.getDate () %></td>
		<td <%= row_color %>> <%= pr_history.getUsername () %></td>
	</tr>
	<%}%>
        </table>
	</td></tr>           
         <%}
        else
        {%>
           
                <TR><TD>   Error: <%=      history.getHistory().get(0) %>      </TD></TR>
           
        <%}%>
         </TABLE>
  <% }%>
    

</td></tr>
</table>
</html>