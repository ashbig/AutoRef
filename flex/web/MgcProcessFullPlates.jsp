<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %> 
<%@ page import="edu.harvard.med.hip.flex.util.*" %> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Full Plates</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Process plates</h2>
<hr>
<html:errors/>
<p>
<html:form action="MgcOrderOligo.do">
<input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
<input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">
<input type="hidden" name="workflowname" value="<bean:write name="workflowname"/>">
<input type="hidden" name="processname" value="<bean:write name="processname"/>">

<table>
    <tr>
    <td class="prompt">Project name:</td>
    <td><bean:write name="projectname" /></td>
    </tr>
    <tr>
    <td class="prompt">Workflow name:</td>
    <td><bean:write name="workflowname" /></td>
    </tr>
    <tr>
    <td class="prompt">Process name:</td>
    <td><bean:write name="processname" /></td>
    </tr>
    <tr>
    <td class="prompt">Number of clones:</td>
    <td><bean:write name="sequences_count" /></td>
    </tr>
<tr>
    <td class="prompt">Number of plates:</td>
    <td><bean:write name="full_plates" /></td>
    </tr>
</table>
<% 
ArrayList plates = (ArrayList)request.getAttribute("platesInfo") ;
if (plates.size() > 0)
{      
 
 %>
<h3>Plates Information:</h3>

<table  cellpadding=0 cellspacing=2 border=1>
    <tr class="headerRow">
        <TH>&nbsp;&nbsp;Plate Number&nbsp;&nbsp;</TH>
        
        <TH>&nbsp;&nbsp;Vector&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;Sequences&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;Source Containers&nbsp;&nbsp;</TH>
    </TR>
<% 

for (int plate_count = 0 ; plate_count < plates.size();    plate_count++  )
{
        PlateDescription plate = (PlateDescription)plates.get(plate_count);
%>
<tr>
        <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
<% if ( !plate.getStatus() ) 
{%>

            <TD><font color=red bold=true> <%= (plate_count+1) %> </font> </TD>

<%} 
else
{%>
            <TD align="right"> <%= (plate_count + 1)%>  </TD>
<%}%>
            <TD>&nbsp; <%= plate.getVector()     %> &nbsp;</TD>
            <TD align="right"> <%= plate.getNumberOfSequences() %>    </TD>
            <TD> 
                        <% for (int container_count =0; container_count < plate.getContainers().size(); container_count++)
                        {
                            ContainerDescription desc = (ContainerDescription)plate.getContainers().get(container_count);
                            if ( !desc.getStatus() )
                            {
                            %>
                            <font color=red >
                                &nbsp;<%= desc.getLabel() %>
                            </font>
                            <%
                            }
                            else
                            {
                            %>
                                &nbsp;<%= desc.getLabel() %>
                            <%
                            }
                        }%>
            </TD>    
          </flex:row>
          </tr>
  
<% 

}
%>
</TABLE>

<table>
 <td class="prompt">Is full plate required?</td>
    <td>
         
         <html:radio property="isFullPlate" value="true"/>Yes
       <html:radio property="isFullPlate" value="false"/>No
    </td>
</tr> 
</table>



<p>

    <p><em><bean:message key="flex.researcher.barcode.prompt"/></em>
    <html:password property="researcherBarcode" size="40"/>

<p>
<P>
    <html:submit property="submit" value="Submit"/>
   
<%}%>
</html:form>

</body>
</html>