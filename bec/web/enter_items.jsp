<%@ page import="edu.harvard.med.hip.bec.*" %>
<script defer="defer" type="text/javascript"><!--
  
/*Start of form validation: items not empty*/

  function validateForm(formElement)
   {
        var str =  trim(formElement.items.value);
        if (( formElement.items.value == null) || ( str == "" ) )
	{
		alert("Please submit items to process.");
                formElement.items.focus();
                return false;
   	}
   	else { return true;}
   }
   function trim(strText)
    { 
		// this will get rid of leading spaces 

		while (strText.substring(0,1) == ' ') 
			strText = strText.substring(1, strText.length); 

		// this will get rid of trailing spaces 
		while (strText.substring(strText.length-1,strText.length) == ' ') 
			strText = strText.substring(0, strText.length-1); 

	   return strText; 
	} 

 



	   /*End of functions.*/
//--></script>

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
int forwardName_int = 0;
if (forwardName instanceof String) forwardName_int = Integer.parseInt((String)forwardName);
else if (forwardName instanceof Integer) forwardName_int = ((Integer) forwardName).intValue();
boolean isCloneIdOnly = false;
if ( forwardName_int == Constants.PROCESS_SHOW_CLONE_HISTORY 
|| forwardName_int == Constants.PROCESS_ORDER_INTERNAL_PRIMERS
|| forwardName_int == Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS
|| forwardName_int == Constants.PROCESS_NOMATCH_REPORT
|| forwardName_int == Constants.PROCESS_RUN_DECISION_TOOL)
{
isCloneIdOnly = true;
}
%>


<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td colspan=2 bgColor="#1145A6"> <font color="#FFFFFF"><strong>Select Items Type: </strong></font></td>
  </tr>
<% if ( forwardName_int == Constants.PROCESS_NOMATCH_REPORT || forwardName_int == Constants.PROCESS_RUN_DECISION_TOOL)
{%>
  <tr>  
    <td><strong><input type="radio" name="item_type" value="<%= Constants.ITEM_TYPE_PLATE_LABELS%>" checked>Container Labels</strong></td>
  </tr>
<%}%> 
<% if ( !isCloneIdOnly )
{%>
  <tr>  
    <td><strong><input type="radio" name="item_type" value="<%= Constants.ITEM_TYPE_PLATE_LABELS%>" checked>Container Labels</strong></td>
    <td><strong><input type="radio" name="item_type" value="<%= Constants.ITEM_TYPE_BECSEQUENCE_ID%>" >BEC Sequence ID(clone sequence id)</strong></td>
  </tr>
<%}%>
  <tr> 
    <td><strong>  <input type="radio" name="item_type" value="<%=Constants.ITEM_TYPE_CLONEID%>"
     <% if ( isCloneIdOnly){%>checked <%}%>
    >Clone Ids</strong></td>
<td>
<% if ( forwardName_int == Constants.PROCESS_RUN_PRIMER3 || forwardName_int == Constants.PROCESS_VIEW_INTERNAL_PRIMERS)
{%>
<strong>  <input type="radio" name="item_type" value="<%= Constants.ITEM_TYPE_FLEXSEQUENCE_ID %>">FLEX Sequence Ids</strong>
<%}
else
{%>&nbsp;<%}%>
</td>
  </tr>
  <tr> 
    <td bgColor="#1145A6" colspan=2> <font color="#FFFFFF"><strong>Enter All search Items</strong></font></td>
    
</tr>
  <tr> 
    <td align="center" colspan=2><textarea name="items"  rows="10"></textarea></td>
  </tr>
</table>

