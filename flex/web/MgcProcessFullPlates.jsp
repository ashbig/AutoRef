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
    <td class="prompt">Number of plates (rearray based on marker):</td>
    <td><bean:write name="full_plates_marker" /></td>
    </tr>
<tr>
    <td class="prompt">Number of plates (rearray not based on marker):</td>
    <td><bean:write name="full_plates_no_marker" /></td>
    </tr>
</table>
<% 
boolean isRunDuplicatesCheck = false;
ArrayList plates_marker = (ArrayList)request.getAttribute("platesInfo_marker") ;
ArrayList plates_no_marker = (ArrayList)request.getAttribute("platesInfo_no_marker") ;
if (plates_marker.size() > 0)
{      
   
  %>
<h3>Plates Information:</h3>
 
    <i>Default rearray: based on marker information</i>

<table  cellpadding=0 cellspacing=2 border=1 width="80%">
    <tr class="headerRow">
        <TH>&nbsp;&nbsp;Plate Number&nbsp;&nbsp;</TH>
        
        <TH>&nbsp;&nbsp;Vector&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;Sequences&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;Source Containers&nbsp;&nbsp;</TH>
    </TR>
<% 

for (int plate_count = 0 ; plate_count < plates_marker.size();    plate_count++  )
{
        PlateDescription plate = (PlateDescription)plates_marker.get(plate_count);
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



<h3>Plates Information:</h3>
 
    <i>Rearray: not based on marker information</i>

<table  cellpadding=0 cellspacing=2 border=1 width="80%">
    <tr class="headerRow">
        <TH>&nbsp;&nbsp;Plate Number&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;Sequences&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;Source Containers&nbsp;&nbsp;</TH>
    </TR>
<% 

for (int plate_count = 0 ; plate_count < plates_no_marker.size();    plate_count++  )
{
        PlateDescription plate = (PlateDescription)plates_no_marker.get(plate_count);
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
            <TD align="right"> <%= plate.getNumberOfSequences() %> 
</TD>
            <TD> 
                        <%
                            ArrayList desc_no_duplicates = new ArrayList();
                        for (int container_count =0; container_count < plate.getContainers().size(); container_count++)
                        {
                            ContainerDescription desc = (ContainerDescription)plate.getContainers().get(container_count);
                          
                            if (  ! desc_no_duplicates.contains(desc.getLabel()) )
                            {

                                desc_no_duplicates.add(desc.getLabel());

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
                            }
                        }%>
            </TD>    
          </flex:row>
          </tr>
  
<% 

}
%>
</TABLE>


<P><P>
<table>
<tr>
<td class="prompt">Rearray based on marker?</td>
    <td>
         
         <html:radio property="isMarker" value="true"/>Yes
       <html:radio property="isMarker" value="false"/>No
    </td>
</tr> 
<tr>
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