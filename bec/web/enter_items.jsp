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
boolean isCloneIdChecked = false;boolean isFLEXSequenceIdChecked = false;
boolean isPlateLabelChecked = false;boolean isACESequenceIdChecked = false;
switch( forwardName_int)
{
case Constants.PROCESS_SHOW_CLONE_HISTORY :{ isCloneId = true; isCloneIdChecked= true;break;}
case Constants.PROCESS_ORDER_INTERNAL_PRIMERS:{ isCloneId = true; isCloneIdChecked= true;break;}
case Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS:{ isCloneId = true; isCloneIdChecked= true;break;}
case Constants.PROCESS_RUN_DECISION_TOOL:{ isCloneId = true;isPlateLabel = true; isPlateLabelChecked=true;break;}
case Constants.PROCESS_FIND_GAPS:{ isCloneId = true; isCloneIdChecked= true;break;}
case Constants.STRETCH_COLLECTION_REPORT_INT:{ isCloneId = true; isCloneIdChecked= true;break;}
case Constants.STRETCH_COLLECTION_REPORT_ALL_INT:{ isCloneId = true;isCloneIdChecked= true; break;}
case Constants.LQR_COLLECTION_REPORT_INT:{ isCloneId = true;isCloneIdChecked= true; break;}
case Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE:{ isCloneId = true; isCloneIdChecked= true;break;}

case Constants.PROCESS_RUN_END_READS_WRAPPER:{ isPlateLabel = true; isPlateLabelChecked=true;break;}
case Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS:{ isPlateLabel = true; isPlateLabelChecked=true;break;}

case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER :{ isFLEXSequenceId = true;isFLEXSequenceIdChecked=true; isACESequenceId = true;break;}
case Constants.PROCESS_APPROVE_INTERNAL_PRIMERS :{ isPlateLabel = true;isPlateLabelChecked=true;  isCloneId = true; isACESequenceId = true;break;}
            
case Constants.PROCESS_RUNPOLYMORPHISM_FINDER:{ isPlateLabel = true; isPlateLabelChecked=true; isCloneId = true; isACESequenceId = true;break;}            
case Constants.PROCESS_RUN_DISCREPANCY_FINDER:{ isPlateLabel = true; isPlateLabelChecked=true; isCloneId = true; isACESequenceId = true;break;}

case Constants.PROCESS_NOMATCH_REPORT:{ isPlateLabel = true; isPlateLabelChecked=true; isCloneId = true;break;}

case Constants.PROCESS_RUN_PRIMER3 :{ isPlateLabel = true; isPlateLabelChecked=true; isCloneId = true; isFLEXSequenceId = true; isACESequenceId = true;break;}
 case Constants.PROCESS_VIEW_INTERNAL_PRIMERS:{ isPlateLabel = true; isPlateLabelChecked=true; isCloneId = true; isFLEXSequenceId = true; isACESequenceId = true;break;}
case Constants.PROCESS_CREATE_REPORT:{ isPlateLabel = true; isPlateLabelChecked=true; isCloneId = true;  isACESequenceId = true;break;}
case Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING  :{ isPlateLabel = true;isPlateLabelChecked=true; break;}
case Constants.PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING  :{ isCloneId = true; isCloneIdChecked= true; break;}
case Constants.PROCESS_DELETE_PLATE :{ isPlateLabel = true;isPlateLabelChecked=true; break;}
case Constants.PROCESS_DELETE_CLONE_READS  :{ isPlateLabel = true;isPlateLabelChecked=true;isCloneId = true;   break;}
case Constants.PROCESS_DELETE_CLONE_FORWARD_READ :{ isPlateLabel = true;isPlateLabelChecked=true; isCloneId = true;  break;}
case Constants.PROCESS_DELETE_CLONE_REVERSE_READ  :{ isPlateLabel = true;isPlateLabelChecked=true; isCloneId = true;  break;}
case Constants.PROCESS_DELETE_CLONE_SEQUENCE: { isCloneId = true; isCloneIdChecked= true;isACESequenceId = true;break;}
case  Constants.PROCESS_GET_TRACE_FILE_NAMES :{ isCloneId = true; isCloneIdChecked= true;break;}
case  Constants.PROCESS_DELETE_TRACE_FILES :{ break;}
                      
}
String[] cells = new String[4]; cells[0]="&nbsp;";cells[1]="&nbsp;";cells[2]="&nbsp;";cells[3]="&nbsp;";int current_entity = 0;
if ( isPlateLabel )
{ 
    cells[current_entity] = "<strong><input type='radio' name='item_type' value='"+ Constants.ITEM_TYPE_PLATE_LABELS +"'";
    if (isPlateLabelChecked) cells[current_entity] += "checked";
    cells[current_entity++] += ">Container Labels</strong>";
}
 if ( isCloneId)
{
    cells[current_entity] = "<strong>  <input type='radio' name='item_type' value='"+Constants.ITEM_TYPE_CLONEID+"'";
    if (isCloneIdChecked)cells[current_entity] += "checked";
    cells[current_entity++] += ">Clone Id </strong>";
}
 if ( isACESequenceId )
{
    cells[current_entity] = "<strong>  <input type='radio' name='item_type' value='"+Constants.ITEM_TYPE_BECSEQUENCE_ID+"'";
    if (isACESequenceIdChecked) cells[current_entity] += "checked";
    cells[current_entity++] += ">ACE Sequence Id </strong>";
}
 if ( isFLEXSequenceId )
{
    cells[current_entity] = "<strong>  <input type='radio' name='item_type' value='"+Constants.ITEM_TYPE_FLEXSEQUENCE_ID+"'";
    if (isFLEXSequenceIdChecked) cells[current_entity] += "checked";
    cells[current_entity++] += ">FLEX Sequence Id</strong>";
}
%>



<table width="100%" border="0" cellspacing="2" cellpadding="2">
<tr> 
<td colspan=2 bgColor="#1145A6"> <font color="#FFFFFF"><strong>Select Items Type: </strong></font></td>
</tr>
<tr>  <td> <%=cells[0] %> </td><td>  <%=cells[2] %>  </td></tr>
<tr> <td>  <%=cells[1] %>  </td><td>   <%=cells[3] %>   </td></tr>

  <tr> 
    <td bgColor="#1145A6" colspan=2> <font color="#FFFFFF"><strong>Enter All search Items</strong></font></td>
    
</tr>
  <tr> 
    <td align="center" colspan=2><textarea name="items"  rows="10"></textarea></td>
  </tr>
</table>

