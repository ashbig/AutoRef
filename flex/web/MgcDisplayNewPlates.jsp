<%--
        $Id: MgcDisplayNewPlates.jsp,v 1.5 2002-07-22 19:02:35 Elena Exp $ 

       
        Author  : htaycher

        Display the list of labels for new mgc plates which info was imported
        from specified file and allow the Researcher print them
	
--%>

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
    
    <h2><bean:message key="flex.name"/> : New Mgc Containers</h2>
    <hr>
    <html:errors/>
    
    <p>
 <html:form action="MgcPrintLabels.do"  >



    <h3> Print barcodes for New Mgc Containers.</h3>
<table border='0' cellpadding='0' cellspacing='9' >
    <tr>
        <td>
            Distribution file  
        </td><Td>
            <bean:write name="filename" /> 
        </td>
    </tr><tr>
        <td>
            Number of MGC Plates:
        </td><Td align="right">
           <%= ((ArrayList)request.getAttribute("LABELS")).size() %> 
        </td>
     </tr><tr>
</table>    

<p>
 
<% if ( ((ArrayList)request.getAttribute("LABELS")).size() > 0 )
{%> 
 <html:submit property="submit" value="  Print  "/>
      
<p>
<p>
<!-- create main table -->
<table  cellpadding=0 cellspacing=2 border=1>
     
     <tr class="headerRow">
        
        <TH>&nbsp;&nbsp;FLEX Id&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;Original Mgc Plate Label&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;Flex Mgc Plate Label&nbsp;&nbsp;</TH>
        <TH>&nbsp;<input type="checkbox" name="all" value="All" onclick="SetChecked(this, this.checked)">&nbsp&nbspPrint&nbsp&nbsp</TH>
      </TR>

 <logic:iterate  id="curPlate" name="LABELS"> 
        <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
            
            <TD align="right">
                <a href="/FLEX/MgcViewContainerDetails.do?CONTAINER_ID=<bean:write name="curPlate" property="id"/>">
                   &nbsp; <bean:write name="curPlate" property="id"/>&nbsp;
                </a>
            </TD>
            <TD>
                &nbsp;<bean:write name="curPlate" property="originalContainer"/>&nbsp;
            </TD>
            <TD>
                &nbsp;<bean:write name="curPlate" property="label"/>&nbsp;
            </TD>
            <TD>
              
              &nbsp;<input type="checkbox" name="chkPrint" value='<bean:write name="curPlate" property="label"/>'>    &nbsp;  
                
            </TD>

        </flex:row>
    </logic:iterate> 



</TABLE>

<br>
<p>

<%
}%>

</html:form>
</body>
</html>


  