<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="java.util.*" %>
<link href="application_styles.css" rel="stylesheet" type="text/css">

<script defer="defer" type="text/javascript">

<!--
  
/*Start of form validation: items not empty*/

  function validateForm(formElement)
   {
   
   for ( count = 0; count < formElement.item_type.length; count++)
    {
        if (formElement.item_type[count].checked==true     && formElement.item_type[count].value== <%= Constants.ITEM_TYPE_PROJECT_NAME %>)
        {
              formElement.items.value =formElement.project_name.value;
               }
    }

    
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
int project_cell = -1;
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
boolean isPlateLabel = false;boolean isACECloneSequenceId = false;
boolean isCloneIdChecked = false;boolean isFLEXSequenceIdChecked = false;
boolean isPlateLabelChecked = false;boolean isACECloneSequenceIdChecked = false;
boolean isProjectList = false;boolean isProjectListChecked = false;
boolean isACERefSequenceId = false;boolean isACERefSequenceIdChecked = false;
switch( forwardName_int)
{
case Constants.PROCESS_SET_CLONE_FINAL_STATUS:{ isCloneId = true; isCloneIdChecked= true;break;}
case Constants.PROCESS_SHOW_CLONE_HISTORY :{ isCloneId = true; isCloneIdChecked= true;break;}
case Constants.PROCESS_ORDER_INTERNAL_PRIMERS:{ isCloneId = true; isCloneIdChecked= true;break;}
case Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS:{ isCloneId = true; isCloneIdChecked= true;break;}


case Constants.PROCESS_RUN_DECISION_TOOL:{ isCloneId = true;isPlateLabel = true; isPlateLabelChecked=true;break;}
case Constants.PROCESS_RUN_DECISION_TOOL_NEW:{ isCloneId = true;isPlateLabel = true; isProjectList= true; isPlateLabelChecked=true;break;}

case Constants.PROCESS_PROCESS_OLIGO_PLATE:{ isPlateLabel = true;  isPlateLabelChecked=true;break;}


case Constants.PROCESS_FIND_GAPS:{ isCloneId = true; isCloneIdChecked= true;break;}
case Constants.STRETCH_COLLECTION_REPORT_INT:{ isCloneId = true; isCloneIdChecked= true;break;}
case Constants.STRETCH_COLLECTION_REPORT_ALL_INT:{ isCloneId = true;isCloneIdChecked= true; break;}
case Constants.LQR_COLLECTION_REPORT_INT:{ isCloneId = true;isCloneIdChecked= true; break;}
case Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE:{ isCloneId = true; isCloneIdChecked= true;break;}

case Constants.PROCESS_RUN_END_READS_WRAPPER:{ isPlateLabel = true; isPlateLabelChecked=true;break;}
case Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS:{ isPlateLabel = true; isPlateLabelChecked=true;break;}

case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER :{ isPlateLabel = true; isCloneId = true; isFLEXSequenceId = true;isPlateLabelChecked=true; isACERefSequenceId = true;break;}
case Constants.PROCESS_APPROVE_INTERNAL_PRIMERS :{ isCloneId = true; isCloneIdChecked= true;break;}//{ isPlateLabel = true;isPlateLabelChecked=true;  isCloneId = true; isACESequenceId = true;break;}
            
case Constants.PROCESS_RUNPOLYMORPHISM_FINDER:{  isCloneId = true; isCloneIdChecked= true; break;}            
case Constants.PROCESS_RUN_DISCREPANCY_FINDER:{ isPlateLabel = true; isPlateLabelChecked=true; isCloneId = true; isACECloneSequenceId = true;break;}

case Constants.PROCESS_NOMATCH_REPORT:{ isPlateLabel = true; isPlateLabelChecked=true; isCloneId = true;break;}

case Constants.PROCESS_RUN_PRIMER3 :{ isPlateLabel = true; isPlateLabelChecked=true; isCloneId = true; isFLEXSequenceId = true; isACERefSequenceId = true;break;}
 case Constants.PROCESS_VIEW_INTERNAL_PRIMERS:{ isCloneId = true; isCloneIdChecked= true;break;}//{ isPlateLabel = true; isPlateLabelChecked=true; isCloneId = true; isFLEXSequenceId = true; isACESequenceId = true;break;}
case Constants.PROCESS_CREATE_REPORT:{ isPlateLabel = true; isPlateLabelChecked=true; isCloneId = true;  isFLEXSequenceId = true;isACECloneSequenceId = true;break;}
case Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING  :{ isPlateLabel = true;isPlateLabelChecked=true; break;}
case Constants.PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING  :{ isCloneId = true; isCloneIdChecked= true; break;}
case Constants.PROCESS_CREATE_REPORT_TRACEFILES_QUALITY:  { isCloneId = true;isPlateLabel = true; isCloneIdChecked= true; break;}
case Constants.PROCESS_DELETE_PLATE :{ isPlateLabel = true;isPlateLabelChecked=true; break;}
case Constants.PROCESS_DELETE_CLONE_READS  :{ isPlateLabel = true;isPlateLabelChecked=true;isCloneId = true;   break;}
case Constants.PROCESS_DELETE_CLONE_FORWARD_READ :{ isPlateLabel = true;isPlateLabelChecked=true; isCloneId = true;  break;}
case Constants.PROCESS_DELETE_CLONE_REVERSE_READ  :{ isPlateLabel = true;isPlateLabelChecked=true; isCloneId = true;  break;}
case Constants.PROCESS_DELETE_CLONE_SEQUENCE: { isCloneId = true; isCloneIdChecked= true;isACECloneSequenceId = true;break;}
case  Constants.PROCESS_GET_TRACE_FILE_NAMES :{ isCloneId = true; isCloneIdChecked= true;break;}
case Constants.PROCESS_VIEW_OLIGO_ORDER_BY_CLONEID:{ isCloneId = true; isCloneIdChecked= true;break;}
case  Constants.PROCESS_DELETE_TRACE_FILES :{ break;}
 case Constants.PROCESS_MOVE_TRACE_FILES  :{ break;}     
   case Constants.PROCESS_REANALYZE_CLONE_SEQUENCE:{isACECloneSequenceId = true;isCloneId = true; isCloneIdChecked= true;break;  }           
}
String[] cells = new String[6]; 
cells[0]="&nbsp;";cells[1]="&nbsp;";
cells[2]="&nbsp;";cells[3]="&nbsp;";
cells[4]="&nbsp;";cells[5]="&nbsp;";
int current_entity = 0;

if ( isPlateLabel )
{ 
    cells[current_entity] = "<strong><input type='radio' name='item_type' value='"+ Constants.ITEM_TYPE_PLATE_LABELS +"'";
    if (isPlateLabelChecked) cells[current_entity] += "checked";
cells[current_entity++] += ">Plate Labels</strong>";

}
 if ( isCloneId)
{
    cells[current_entity] = "<strong>  <input type='radio' name='item_type' value='"+Constants.ITEM_TYPE_CLONEID+"'";
    if (isCloneIdChecked)cells[current_entity] += "checked";
    cells[current_entity++] += ">Clone ID </strong>";

}
 if ( isACECloneSequenceId )
{
    cells[current_entity] = "<strong>  <input type='radio' name='item_type' value='"+Constants.ITEM_TYPE_ACE_CLONE_SEQUENCE_ID+"'";
    if (isACECloneSequenceIdChecked) cells[current_entity] += "checked";
    cells[current_entity++] += ">Clone Sequence ID </strong>";

}

 if ( isACERefSequenceId )
{
    cells[current_entity] = "<strong>  <input type='radio' name='item_type' value='"+Constants.ITEM_TYPE_ACE_REF_SEQUENCE_ID+"'";
    if (isACERefSequenceIdChecked) cells[current_entity] += "checked";
    cells[current_entity++] += ">ACE Reference Sequence ID </strong>";

}
 if ( isFLEXSequenceId )
{
    cells[current_entity] = "<strong>  <input type='radio' name='item_type' value='"+Constants.ITEM_TYPE_FLEXSEQUENCE_ID+"'";
    if (isFLEXSequenceIdChecked) cells[current_entity] += "checked";
    cells[current_entity++] += ">User Reference Sequence ID</strong>";

}

String project_combo_text = null;
if ( isProjectList && DatabaseToApplicationDataLoader.getProjectDefinitions() != null && DatabaseToApplicationDataLoader.getProjectDefinitions().size()>0)
 {
     project_cell=current_entity;
    cells[current_entity] = "<strong>  <input  type='radio' name='item_type'  value='"+Constants.ITEM_TYPE_PROJECT_NAME+"'";
    if (isProjectListChecked) cells[current_entity] += "checked";
  cells[current_entity++] += ">Project Name</strong>";

 project_combo_text = "<select name='project_name' >";
String project_code = null;


for ( Enumeration e = DatabaseToApplicationDataLoader.getProjectDefinitions().keys(); e.hasMoreElements() ;)
        {
            project_code = (String)  e.nextElement();
            project_combo_text += "<option value = '"+project_code+"' >" + (String)DatabaseToApplicationDataLoader.getProjectDefinitions().get(project_code);
                 }

project_combo_text+="</select>";

}   
    



%>



<table width="100%" border="0" cellspacing="2" cellpadding="2">
<tr class='headerRow'> <td colspan=2 >Select Items Type: </td></tr>
<tr>  <td width='40%'> <%=cells[0] %> <%if ( project_cell==0) {%> <%= project_combo_text %> <%}%></td>
<td>  <%=cells[2] %>  <%if ( project_cell==2) {%> <%= project_combo_text %> <%}%></td></tr>
<tr> <td>  <%=cells[1] %>  <%if ( project_cell==1) {%> <%= project_combo_text %> <%}%></td>
<td>   <%=cells[3] %>   <%if ( project_cell==3) {%> <%= project_combo_text %> <%}%></td></tr>
<% if ( !cells[4].equals("&nbsp;" )){%><tr> <td>  <%=cells[4] %> <%if ( project_cell==4) {%> <%= project_combo_text %> <%}%> </td>
<td>   <%=cells[5] %>   <%if ( project_cell==5) {%> <%= project_combo_text %> <%}%></td></tr><%}%> 


  <tr class='headerRow'> <td colspan=2 >Enter All search Items</td>   </tr>
  <tr> 
    <td align="center" colspan=2><textarea name="items"  rows="10"></textarea></td>
  </tr>
</table>