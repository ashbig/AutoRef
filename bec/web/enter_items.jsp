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
boolean isCloneId = false;boolean isFLEXSequenceId = false;
boolean isPlateLabel = false;boolean isACESequenceId = false;
switch( forwardName_int)
{
case Constants.PROCESS_SHOW_CLONE_HISTORY :{ isCloneId = true; break;}
case Constants.PROCESS_ORDER_INTERNAL_PRIMERS:{ isCloneId = true; break;}
case Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS:{ isCloneId = true; break;}
case Constants.PROCESS_RUN_DECISION_TOOL:{ isCloneId = true;isPlateLabel = true; break;}
case Constants.PROCESS_FIND_GAPS:{ isCloneId = true; break;}
case Constants.STRETCH_COLLECTION_REPORT_INT:{ isCloneId = true; break;}
case Constants.STRETCH_COLLECTION_REPORT_ALL_INT:{ isCloneId = true; break;}
case Constants.LQR_COLLECTION_REPORT_INT:{ isCloneId = true; break;}
case Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE:{ isCloneId = true; break;}

case Constants.PROCESS_RUN_END_READS_WRAPPER:{ isPlateLabel = true; break;}
case Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS:{ isPlateLabel = true; break;}

case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER :{ isFLEXSequenceId = true; isACESequenceId = true;break;}
case Constants.PROCESS_APPROVE_INTERNAL_PRIMERS :{ isPlateLabel = true;  isCloneId = true; isACESequenceId = true;break;}
            
case Constants.PROCESS_RUNPOLYMORPHISM_FINDER:{ isPlateLabel = true;  isCloneId = true; isACESequenceId = true;break;}            
case Constants.PROCESS_RUN_DISCREPANCY_FINDER:{ isPlateLabel = true;  isCloneId = true; isACESequenceId = true;break;}

case Constants.PROCESS_NOMATCH_REPORT:{ isPlateLabel = true;  isCloneId = true;break;}

case Constants.PROCESS_RUN_PRIMER3 :{ isPlateLabel = true;  isCloneId = true; isFLEXSequenceId = true; isACESequenceId = true;break;}
 case Constants.PROCESS_VIEW_INTERNAL_PRIMERS:{ isPlateLabel = true;  isCloneId = true; isFLEXSequenceId = true; isACESequenceId = true;break;}
case Constants.PROCESS_CREATE_REPORT:{ isPlateLabel = true;  isCloneId = true;  isACESequenceId = true;break;}
}
%>


<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td colspan=2 bgColor="#1145A6"> <font color="#FFFFFF"><strong>Select Items Type: </strong></font></td>
  </tr>
  <tr>  
<% if ( isPlateLabel ){%> <td><strong><input type="radio" name="item_type" value="<%= Constants.ITEM_TYPE_PLATE_LABELS%>" checked>Container Labels</strong></td>
<%} else {%> <td>&nbsp;</td> <%}%>

<% if ( isACESequenceId ) {%>   <td><strong><input type="radio" name="item_type" value="<%= Constants.ITEM_TYPE_BECSEQUENCE_ID%>" >ACE Sequence ID(clone sequence id)</strong></td>
<%} else {%> <td>&nbsp;</td> <%}%>

 </tr>
<tr>
<% if ( isCloneId){%>  <td><strong>  <input type="radio" name="item_type" value="<%=Constants.ITEM_TYPE_CLONEID%>" > CloneId </strong></td>
<%} else {%> <td>&nbsp;</td> <%}%>
<% if ( isFLEXSequenceId ){%> <td><strong>  <input type="radio" name="item_type" value="<%= Constants.ITEM_TYPE_FLEXSEQUENCE_ID %>">FLEX Sequence Ids</strong></td>
<%} else {%> <td>&nbsp;</td> <%}%>
 </tr>
  <tr> 
    <td bgColor="#1145A6" colspan=2> <font color="#FFFFFF"><strong>Enter All search Items</strong></font></td>
    
</tr>
  <tr> 
    <td align="center" colspan=2><textarea name="items"  rows="10"></textarea></td>
  </tr>
</table>

