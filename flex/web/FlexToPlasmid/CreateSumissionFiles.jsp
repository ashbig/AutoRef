<%@ page language="java" %>
<%@ page errorPage="../ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

 
<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ page import="edu.harvard.med.hip.flex.infoimport.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.growthcondition.*" %>
<%@ page import="edu.harvard.med.hip.flex.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.flex.infoimport.plasmidimport.filemanipulation.*" %>
 
 <%@ page import="edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions" %>
<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : <bean:message key="flex.name"/> <%= PlasmidImporterDefinitions.IMPORT_ACTIONS.CREATE_SUBMISSION_FILES.getMainPageTitle() %></title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>

</head>
<body>
  <%
  PlasmidImporterDefinitions.IMPORT_ACTIONS cur_process = 
    PlasmidImporterDefinitions.IMPORT_ACTIONS.valueOf( (String)request.getAttribute("forwardName"));%>
    
    
<h2><bean:message key="flex.name"/> : <%= cur_process.getMainPageTitle() %>
</h2><hr>
<html:errors/>
 
 <h3 style="text-align: left; color: rgb(0, 128, 0); white-space: nowrap;">

<%= cur_process.getPageTitle() %></h3>
<html:form action="/FlexToPlasmidFileSubmission.do" enctype="multipart/form-data"   method="post"> 
<input type="hidden" name="forwardName"  value="<%= cur_process.getNextProcess() %>" >   

<table cellpadding="2" cellspacing="2" width="90%" align="center">
    
<tr class="headerSectionRow">  
    <td colspan="2"  >     Select items type  </td>  </tr>
<tr>  <td width='40%' colspan=2 > 
<strong>       <input type='radio' name='itemType' value='<%= edu.harvard.med.hip.flex.infoimport.ConstantsImport.ITEM_TYPE.ITEM_TYPE_PLATE_LABELS.toString() %>' checked >
<%= edu.harvard.med.hip.flex.infoimport.ConstantsImport.ITEM_TYPE.ITEM_TYPE_PLATE_LABELS.getTitle() %>
</strong></td> </tr> 
<tr>  <td width='40%' colspan=2 > <strong>       <input type='radio' name='itemType' value='<%= edu.harvard.med.hip.flex.infoimport.ConstantsImport.ITEM_TYPE.ITEM_TYPE_CLONEID.toString() %>'   >
<%= edu.harvard.med.hip.flex.infoimport.ConstantsImport.ITEM_TYPE.ITEM_TYPE_CLONEID.getTitle() %>
</strong></td></tr>    


<tr class="headerSectionRow">  
    <td colspan="2" >     Enter all items  </td>  </tr>
    <tr> <td  colspan="2"> <div align="center"> <textarea rows=6  name="itemids"> </textarea></div></td></tr>


<tr class="headerSectionRow">  
    <td colspan="2"  > Specify clone upload rules  </td>  </tr>


<tr>  <td colspan="2">  
    <html:radio property="cloneStatus" value="<%= edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.PLASMID_TRANSFER_CLONE_STATUS.READY_FOR_TRANSFER.toString() %>" /> 
        <%= edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.PLASMID_TRANSFER_CLONE_STATUS.READY_FOR_TRANSFER.getDisplayPlateSubmissionRule() %>
<bR> <html:radio property="cloneStatus" value="<%= edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.PLASMID_TRANSFER_CLONE_STATUS.NOT_READY_FOR_TRANSFER.toString() %>" />
        <%= edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.PLASMID_TRANSFER_CLONE_STATUS.NOT_READY_FOR_TRANSFER.getDisplayPlateSubmissionRule() %>

        </td>
    
</tr>
<tr><td colspan=2>&nbsp;</td></tr>
<tr><td class="prompt" colspan=2>Batch FLEX to PLASMID property map:
      <html:file property="<%= PlasmIDFileType.PLATE.toString() %>" /> </td></tr>
  

<tr class="headerSectionRow">  
    <td colspan="2" height="29">     Select common parameters**  </td>  </tr>
  <tr class="rowDark" >    <td  width="40%">Clone collection(s):</td>
<td>  
 
<html:select property="cloneCollections" size="4" multiple="true">
        <option   value ="<%= BioDefinitions.BIO_UNITS.NONE.toString()%>" selected >
        <%= BioDefinitions.BIO_UNITS.NONE.getDisplayTitle()%>
        <html:options
        collection="plasmidCollections"
        property="name"
        labelProperty="name"
/></html:select>
            
</td></tr>
<tr class="evenRow">    <td  class="prompt" >Growth Conditions:</td>
<td> 
<html:select property="growthCondition1">
        <option value ="<%= BioDefinitions.BIO_UNITS.NONE.toString()%>" selected ><%= BioDefinitions.BIO_UNITS.NONE.getDisplayTitle()%>
        <html:options
        collection="plasmidGrowthConditions"
        property="name"
        labelProperty="name"
        /></html:select>

    <input checked type="checkbox" name="isRecomendedGC1">Is recomended?</checkbox>
<br>

    <html:select property="growthCondition2">
        <option value ="<%= BioDefinitions.BIO_UNITS.NONE.toString()%>" selected ><%= BioDefinitions.BIO_UNITS.NONE.getDisplayTitle()%>
        <html:options
        collection="plasmidGrowthConditions"
        property="name"
        labelProperty="name"
        /></html:select> 
    
   
    <input type="checkbox" name="isRecomendedGC2">Is recomended?</checkbox>

</td></tr>
<tr class="rowDark"><td class="prompt">Restriction:</td>
<td>
 
<select name="restriction">
    <option  value ="<%= BioDefinitions.BIO_UNITS.NONE.toString()%>" selected ><%= BioDefinitions.BIO_UNITS.NONE.getDisplayTitle()%> 
   
<logic:iterate id="item" name="plasmidRestrictions"  >
	     <option value="<bean:write name="item" />">
	     <bean:write name="item" />  
     </logic:iterate>
</select>
        </td></tr>
<tr class="evenRow"> <td class="prompt">Host Type:</td>
    <td>  
    <select name="hosttype">
   <option  value ="<%= BioDefinitions.BIO_UNITS.NONE.toString()%>" selected ><%= BioDefinitions.BIO_UNITS.NONE.getDisplayTitle()%> 
    <logic:iterate id="item" name="plasmidHostType"  >
    	     <option value="<bean:write name="item" />">
    	     <bean:write name="item" />  
         </logic:iterate>
</select>
</td></tr>
<tr class="rowDark">    <td class="prompt">Marker:</td>
<td>  
<select name="marker">
<option value ="<%= BioDefinitions.BIO_UNITS.NONE.toString()%>" selected ><%= BioDefinitions.BIO_UNITS.NONE.getDisplayTitle()%>
<logic:iterate id="item" name="plasmidMarkers"  >
	     <option value="<bean:write name="item" />">
	     <bean:write name="item" />  
     </logic:iterate>
</select>
</td></tr>

<tr>    <td  colspan="2">&nbsp;  </td></tr>

<tr class="evenRow" > <td colspan="2" class="prompt" align="center">Please describe clone Host:</td></tr>
<tr class="rowDark">    <td class="prompt"  >First Host:</td>
<td><table >
<tr class="rowDark">    <td class="prompt" >Host strain:</td><td><html:text property="hoststrain" size="50"/></td></tr>
            <tr class="rowDark">    <td class="prompt" >Is in use:</td><td> <html:radio property="hoststrainIsInUse" value="Y" />Yes
              <html:radio property="hoststrainIsInUse" value="N"/>No </td></tr>
            <tr class="rowDark">    <td class="prompt" >Description:</td><td> 
              <textarea rows=3  name="hoststrainDescription" size="250" width="50">
                  </textarea   ></td></tr></table>
</td>
</tr>
<tr class="evenRow">    <td class="prompt"  >Second Host:</td>
<td><table >
<tr class="evenRow">    <td class="prompt" >Host strain:</td><td><html:text property="hoststrain1" size="50"/></td></tr>
            <tr class="evenRow">    <td class="prompt" >Is in use:</td><td> <html:radio property="hoststrainIsInUse1" value="Y" />Yes
              <html:radio property="hoststrainIsInUse1" value="N"/>No </td></tr>
            <tr class="evenRow">    <td class="prompt" >Description:</td><td> 
              <textarea rows=3  name="hoststrainDescription" size="250" width="50">
                  </textarea   ></td></tr></table></td>
</tr>

<tr> <td colspan="2">
    <font SIZE=-4>
    <i><b>**Note:</b></i> Please be advised that additional information for clones that is
    not stored in FLEX:<ul>
        <li>Growth Conditions;</li>
        <li>Restrictions;</li>
        <li>Clone Collection.</li>
    </ul> is applied  to <b>all clones</b> on submitted plates. You can select "Not applicable" 
    if information can not be applied to all clones, however, in this case the submission 
    files should be altered to provide missing information.</font></td> </tr>
<tr><td align="center" colspan="2"><html:submit/></td></tr>

</table>
<P><P>

</html:form>


</body>
</html:html>
