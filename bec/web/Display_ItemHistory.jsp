<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.util_objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.ui_objects.*" %>

<LINK REL=STYLESHEET       HREF="application_styles.css"      TYPE="text/css">

<html>

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
  <center><table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr><td width="100%"><html:errors/></td></tr></table>
  </center></div>
<p></p>
<table border="0" cellpadding="0" cellspacing="0" width="90%" align=center>
<tr><td>

<%  
  
    int item_type = Integer.parseInt( (String)request.getAttribute("item_type"));
    String item_title ="ID:"; 
    if (item_type == Constants.ITEM_TYPE_CLONEID)
     {
        item_title ="Clone "+item_title;
        
     }
    String row_style=null;
    ArrayList items = (ArrayList) request.getAttribute("process_items");
    ItemHistory history = null;
 ProcessHistory pr_history = null; String row_color = null;
    
   for (int index = 0; index < items.size(); index ++)
   {
         history = (ItemHistory)items.get(index);
        %>
        <table border='0' width="90%" align=center>
        <tr><td>&nbsp;</td></tr>
       <TR><TD>    <b> <%= item_title + " "+ history.getItemId()%>    </b>       </TD></TR>
         <TR><TD>
        <%
        if ( history.getStatus() == ItemHistory.HISTORY_PROCESSED)
        {%>
             <table border="1" cellpadding="0" cellspacing="0" width="100%" align=center>
            <tr class='headerRow'>
               <th >Protocol</th>
                <th >Specification</th>
                <th>Execution Date</th>
                <th >Researcher</th>
            </tr>
<%
    for (int count = 0; count < history.getHistory().size(); count ++)
	{
		pr_history = (ProcessHistory)history.getHistory().get(count);
		row_style = (count % 2 == 0) ? "'evenRow'" : "'oddRow'";// bgColor='#e4e9f8'";
		
%>
	<tr class=<%= row_style %> >
        	<td > <%= pr_history.getName() %> </td>
		<td  align=right>
		<% 
		for (int s = 0; s < pr_history.getConfigs().size();s++)
		{
			UIConfigItem cs=(UIConfigItem)pr_history.getConfigs().get(s);
		
		if ( cs.getType() == Spec.PRIMER3_SPEC_INT ||  cs.getType() == Spec.END_READS_SPEC_INT 
		||  cs.getType() == Spec.FULL_SEQ_SPEC_INT ||  cs.getType() == Spec.POLYMORPHISM_SPEC_INT )
		{%>
			<a href="#" onCLick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetSpec.do?forwardName=<%= cs.getId()  * Spec.SPEC_SHOW_SPEC %>' ,'newWndRefseqNt','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;"> 
			 <%=cs.getId() %> </a>
		<%}
		else
		{%>
		&nbsp;
		<%}}%>
		</td>
		<td >	<%= pr_history.getDate () %></td>
		<td > <%= pr_history.getUsername () %></td>
	</tr>
	
	<%}%>
   </TABLE> 
	</td></tr>           
         <%}
        else
        {%>
           
                <TR><TD>   Error: <%=      history.getHistory().get(0) %>      </TD></TR>
          
        <%}%>
              </table>
         
  <% }%>
   

</td></tr>
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