
<%@ page language="java" %>
<%@ page errorPage="../ProcessError.do"%>
<%@ page import="edu.harvard.med.hip.flex.core.*"%>
<%@ page import="edu.harvard.med.hip.flex.Constants"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.harvard.med.hip.flex.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : New Containers</title>
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
    
    <h2><bean:message key="flex.name"/> : New Containers</h2>
    <hr>
    <html:errors/>
    
    <p>
 <html:form action="MgcPrintLabels.do"  >
 <h3> Print barcodes for new containers.</h3>


<p>
 
<html:submit property="submit" value="  Print  "/>

<p>
<!-- create main table -->
 <% ArrayList containers = (ArrayList)request.getAttribute("LABELS"); 
 if (containers == null ){ %>
 No containers was created: you have enough empty containers.<%}else{%>
<table  cellpadding=2 cellspacing=2 border=1>
     
     <tr class="headerRow">
        
        <TH ALIGN="center">Container ID</TH>
        <TH ALIGN="center">Container Label</TH>
         <TH ALIGN="center"><input type="checkbox" name="all" value="All" onclick="SetChecked(this, this.checked)"> Print </TH>
      </TR>
      
 <logic:iterate  id="curPlate" name="LABELS"> 
        <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
<TD align="right">
                <a href="/FLEX/ViewContainerDetails.do?CONTAINER_ID=<bean:write name="curPlate" property="id"/>">
                  <bean:write name="curPlate" property="id"/> </a>
            </TD>
 <TD><bean:write name="curPlate" property="label"/>   </TD>
 <TD><input type="checkbox" name="chkPrint" value='<bean:write name="curPlate" property="label"/>'>  </TD>
 </flex:row>
</logic:iterate> 

</TABLE>
 
<%}%>
<br>
<p>

</html:form>
</body>
</html>


  