<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ page import="edu.harvard.med.hip.flex.Constants"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.harvard.med.hip.flex.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : New MGC Containers</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>

<script language="JavaScript">
<!--
function SetChecked(e, val) {
  // Check all of the checkboxes in a group
  // Initialization
  var iElt, currElt, f;

    // Identify the form
   f = e.form;
    // Loop over all elements in the form
    for (iElt=0; iElt < f.elements.length; iElt++)
 {
      currElt = f.elements[iElt];	
      // If the element is one of the checkboxes in the group containing the checkbox which was just clicked...
      if (currElt.name == 'chkPrint')
      {
       // Check the checkbox
        currElt.checked = val;
      }  // end if
    }  // end loop
  
}  // end of function CheckAllCheckboxesInGroup
-->
</script>

 </head>
<body>
    
    <h2><bean:message key="flex.name"/> : User Uploaded Containers</h2>
    <hr>
    <html:errors/>
    
    <p>
 <html:form action="MgcPrintLabels.do"  >



    <h3> Print barcodes for Uploaded Containers.</h3>
<table border='0' cellpadding='0' cellspacing='9' >
    <td class="prompt">Project name:</td>
    <td><bean:write name="projectname" />
</table>    

<p>
 
<% 
ArrayList labels = (ArrayList)request.getAttribute("LABELS");

if ( labels.size() == 0 )
{%>
No user plates have been uploaded for the project
<%}
else{%> 
 <html:submit property="submit" value="  Print  "/>
      
<p>
<p>
<!-- create main table -->
<table  cellpadding=0 cellspacing=2 border=1>
     
     <tr class="headerRow">
        
        <TH>&nbsp;&nbsp;FLEX Container ID&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;Original Plate Label&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;FLEX Plate Label&nbsp;&nbsp;</TH>
        <TH>&nbsp;<input type="checkbox" name="all" value="All" onclick="SetChecked(this, this.checked)">&nbsp&nbspPrint&nbsp&nbsp</TH>
      </TR>

      <% 
          String[] row_styles = {"oddRow","evenRow"};
          String row_style = row_styles[0];
          String[] plate_data = null;
          String show_instruction = null;
          String project_name = (String) request.getAttribute("projectname");
          for (int count = 0; count < labels.size();  count++)
          {
               row_style = row_styles[count % 2];
               plate_data = (String[]) labels.get(count);
               if ( project_name.equalsIgnoreCase("MGC Project"))
                     show_instruction="/FLEX/MgcViewContainerDetails.do?CONTAINER_ID="+plate_data[0];
               else
                   show_instruction="/FLEX/ViewContainerDetails.do?forwardName="+Constants.VIEW_CONTAINER+"&amp;"+Constants.CONTAINER_BARCODE_KEY+"="+plate_data[2];
               %>
               <tr style="<%= row_style %>">
                   <TD align="right">
                       <a href="<%= show_instruction%>">
                         <%= plate_data[0]%>
                       </a>
                   </TD> 
                   <TD><%= plate_data[1]%>  </TD>
                   <TD><%= plate_data[2]%>  </TD>
                   <TD><input type="checkbox" name="chkPrint" value='<%= plate_data[2]%> '>   </td>
               </tr>
               <%
          }%>

          
         
</TABLE>

<br>
<p>

<%
}%>

</html:form>
</body>
</html>


  