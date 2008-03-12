<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ page import="edu.harvard.med.hip.flex.infoimport.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*" %>
<%@ page import="edu.harvard.med.hip.flex.workflow.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html>
<head>
<title><bean:message key="flex.name"/> : Create Process Plate from File </title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<!-- set up for radio buttons-->
<% 
      String projectname= (String) request.getAttribute("projectname");
      boolean isPutOnQueue = false;
      boolean isDefineConstructSizeBySequence = false;
      boolean isCheckTargetSequenceInFLEX = false;
      boolean isFillInCLoneTables = false;
      boolean   isGetFLEXSequenceFromNCBI=false;
      boolean isOtherProject = false;
      boolean isInsertControlNegativeForEmptyWell= false;
      int submissionType = OutsidePlatesImporter.SUBMISSION_TYPE_NOTE_KNOWN;
       if(Project.PROJECT_NAME_MGC.equals(projectname))
      {
            isCheckTargetSequenceInFLEX =true;
            submissionType = OutsidePlatesImporter.SUBMISSION_TYPE_MGC;
      }
      else  if(Project.PROJECT_NAME_ORF.equals(projectname))
      {
            isFillInCLoneTables=true;
           isInsertControlNegativeForEmptyWell=true;
            isGetFLEXSequenceFromNCBI=true;
            isCheckTargetSequenceInFLEX =true;
             submissionType = OutsidePlatesImporter.SUBMISSION_TYPE_ONE_FILE;
       }
      else if(Project.PROJECT_NAME_PSI.equals(projectname))
       {
           isPutOnQueue = true;
           isDefineConstructSizeBySequence = true;
           isInsertControlNegativeForEmptyWell= true;
           isFillInCLoneTables = true;
           submissionType = OutsidePlatesImporter.SUBMISSION_TYPE_PSI;
      }
      else
      {
           isCheckTargetSequenceInFLEX = true;
           isFillInCLoneTables = true;
           isOtherProject = true;
      }
       %>
<body>

<h2><bean:message key="flex.name"/> : Create Process Plates from File</h2>
<hr>
<html:errors/>
<p>
<html:form action="/AddNewPlatesFromFile.do" enctype="multipart/form-data">
        
      <input type="hidden" name="processid" value="<bean:write name="processid"/>">
      
        <input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
        <input type="hidden" name="projectname" value="<bean:write name="projectname"/>">
        
        <input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">
        <input type="hidden" name="workflowname" value="<bean:write name="workflowname"/>" >
        
        <input type="hidden" name="processname" value="<bean:write name="processname"/>">
        <input type="hidden" name="forwardName"    value="<%= String.valueOf(-ConstantsImport.PROCESS_IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX) %>">
        
        
        <input type="hidden" name="numberofwells" value="96">
        
        <table>
        <tr>
            <td class="prompt">Project name:</td>
            <td><bean:write name="projectname"/></td>
        </tr>
        <tr>
            <td class="prompt">Workflow name:</td>
            <td><bean:write name="workflowname"/></td>
        </tr>
        <tr>
            <td class="prompt">Process name</td>     <td><bean:write name="processname"/></td>
        </tr>
        <tr><td colspan=2>&nbsp;</td></tr>
        <tr><td colspan=2>    <h3>Parameter setting:</h3></td></tr>
        <tr><td colspan=2> 
            <table border=0 width="100%" bgcolor=e5f6ff>
            <tr>
                <td class="prompt">New plate type:</td>
                <td><select name="plateType">
                        <option value="96 WELL PLATE"/>96 Well Plate
                    </select>
                </td>
            </tr>
            <tr>
                <td class="prompt">New sample type:</td>
                <td><select name="sampleType">
                        <option value="DN" selected> DNA plate
                        <option value="GP"/> Glycerol PSI original plate
                        <option value="GS"/> Glycerol plate
                        <option value="AD"/> DNA archive storage plate
                     
                        <option value="AG"/> Glycerol archive storage plate
                        <option value="BP"/> BP reaction plate
                        <option value="CR"/> Infusion reaction plate
                        <option value="DD"/> Distribution DNA plate
                        <option value="DG"/> Distribution glycerol stocks
                        <option value="LI"/> Culture block plate
                        <option value="MD"/> Working DNA plate
                        <option value="MG"/> Working glycerol stocks
                        <option value="RA"/> Rearrayed DNA plate
                        <option value="RC"/> Rearrayed plate based on sequencing results
                        <option value="RD"/> DNA template plate
                        <option value="RG"/> Rearrayed glycerol plate
                        <option value="RT"/> Rearrayed transformation plate
                        <option value="SD"/> Sequencing DNA plate
                        <option value="SG"/> Sequencing glycerol plate
                        <option value="XD"/> Expression DNA plate
                        <option value="XG"/> Expression glycerol plate
                        <option value="XT"/> Transfection plate
                    </select>
                </td>
            </tr>
            <tr>
                <td class="prompt">New plate location:</td>
                <td><select name="plateLocation">
                        <option value="<%= Location.CODE_FREEZER%>"><%= Location.FREEZER %>
                        <option value="<%= Location.CODE_REFRIGERATOR%>"><%= Location.REFRIGERATOR %>
                        <option value="<%= Location.CODE_WORKBENCH%>"><%= Location.WORKBENCH %>
                        <option value="<%= Location.CODE_UNAVAILABLE%>"><%= Location.UNAVAILABLE %>
                    </select>
                </td>
                </select>
        <tr>   <td>&nbsp;        </td></tr> </td></tr>
        <tr> <p>  <td  class="prompt" >Put new plate on queue for further processing </td> 
            <td>     <input type="radio" name="isPutOnQueue" value="true" <% if(isPutOnQueue){%>checked<%}%> >Yes
                                                                                                                  <input type="radio" name="isPutOnQueue" value="false"  <% if(!isPutOnQueue){%>checked<%}%>  >No
                                                                                                     </td>  </tr>
        <tr>   <td  class="prompt" >Create clone record for each sample </td>  
            <td>     <input type="radio" name="isFillInCLoneTables" value="true" <% if(isFillInCLoneTables){%>checked<%}%> >Yes
 <input type="radio" name="isFillInCLoneTables" value="false" <% if(!isFillInCLoneTables){%>checked<%}%>>No
</td>  </tr>


    <tr>   <td  class="prompt" >Create control negative samples per each empty well </td>  
            <td>     <input type="radio" name="isInsertControlNegativeForEmptyWell" value="true" <% if(isInsertControlNegativeForEmptyWell){%>checked<%}%> >Yes
 <input type="radio" name="isInsertControlNegativeForEmptyWell" value="false" <% if(!isInsertControlNegativeForEmptyWell){%>checked<%}%>>No
</td>  </tr>
        
        <tr>   <td  class="prompt">Define clone construct by last codon of target sequence </td>  
            <td>    <input type="radio" name="isDefineConstructSizeBySequence" value="true" <% if(isDefineConstructSizeBySequence){%>checked<%}%>>Yes
                                                                                                                                                      <input type="radio" name="isDefineConstructSizeBySequence" value="false" <% if(!isDefineConstructSizeBySequence){%>checked<%}%>>No
                                                                                                                                        </td>  </tr>
        <tr>   <td  class="prompt" >Check if target sequence in FLEX database </td>  
            <td>     <input type="radio" name="isCheckTargetSequenceInFLEX" value="true" <% if(isCheckTargetSequenceInFLEX){%>checked<%}%>>Yes
                                                                                                                                               <input type="radio" name="isCheckTargetSequenceInFLEX" value="false"  <% if(!isCheckTargetSequenceInFLEX){%>checked<%}%>>No
                                                                                                                                 </td>  </tr>
        <tr>   <td  class="prompt" >Get target sequence from NCBI </td>  
            <td>     <html:radio property="isGetFLEXSequenceFromNCBI" value="true"/>Yes
                <html:radio property="isGetFLEXSequenceFromNCBI" value="false"/>No
        </td>  </tr>
        <tr>   <td  class="prompt" >Target sequence ID is GI? </td>  
            <td>     <html:radio property="isFLEXSequenceIDGI" value="true"/>Yes
                <html:radio property="isFLEXSequenceIDGI" value="false"/>No
        </td>  </tr>
        
        
        
        
        </table>
       
        </td></tr>
          <tr><td colspan=2>&nbsp;</td></tr>
       
        <tr><td colspan=2>    <h3>Submission instructions:</h3></td></tr>
        <tr><td  colspan=2>
                <table border=0  bgcolor=e5f6ff width="100%">
                    <% if ( submissionType == OutsidePlatesImporter.SUBMISSION_TYPE_NOTE_KNOWN) {%> 
                    <tr>
                        <td class="prompt">Select submission type:</td>
                        <td ><select name="submissionType">
                                <option value="<%= OutsidePlatesImporter.SUBMISSION_TYPE_ONE_FILE%> ">All data in one file
                                <option value="<%= OutsidePlatesImporter.SUBMISSION_TYPE_REFSEQUENCE_LOCATION_FILES %>">Reference sequence and plate mapping in different files
                                <option value="<%= OutsidePlatesImporter.SUBMISSION_TYPE_PSI%>">PSI schema
                        </select></td>
                    </tr><%}
                    else
                    {%>    <input type="hidden" name="submissionType" value="<%= submissionType %>">     <%}%>
                    
                    
                    <tr>
                        <td class="prompt">Upload map file:</td>
                        <td><html:file property="mapFile" /></td>
                        <logic:equal name="projectname"  value="<%= Project.PROJECT_NAME_PSI %>" >
                            <td>[<a target="_blank" href="/FLEX/<bean:message key="add.map.psi.sample.jsp"/>">sample file</a>]</td>
                            <td>[<a target="_blank"  href="/FLEX/<bean:message key="add.map.mfiles.psi.sample.jsp"/>">sample file</a>]</td>
                        </logic:equal>
                        <logic:equal name="projectname"  value="<%= Project.PROJECT_NAME_MGC %>" >
                            <td>[<a target="_blank"  href="/FLEX/<bean:message key="add.map.mgc.sample.jsp"/>">sample file</a>]</td>
                        </logic:equal>
                        <logic:equal name="projectname"  value="<%= Project.PROJECT_NAME_ORF %>" >
                            <td>[<a target="_blank"  href="/FLEX/<bean:message key="add.map.orf.sample.jsp"/>">sample file</a>]</td>
                        </logic:equal>
                        <% if (isOtherProject){ %>
                        <td>[<a target="_blank"  href="/FLEX/<bean:message key="add.map.yh.sample.jsp"/>">sample file</a>]</td>
                        <%}%>
                        
                        
                </tr></table>
        </td></tr>
     <tr><td colspan=2>&nbsp;</td></tr>
       
        <tr><td colspan=2>    <h3>Input files:</h3></td></tr>
  <tr><td  colspan=2>
      <table border=0  bgcolor=e5f6ff width="100%">
    <tr>
        <td class="prompt">Upload plate information file:</td>
        <td><html:file property="inputFile" /></td>
        <td>
            [<a target="_blank"  href="/FLEX/<bean:message key="add.plate.mgc.onefile.sample.jsp"/>">sample file: plate </a>]
         <P>[<a target="_blank"  href="/FLEX/<bean:message key="add.plate.mfile.sample.jsp"/>">sample file: complete </a>]
     
      <logic:equal name="projectname"  value="<%= Project.PROJECT_NAME_PSI %>" >
            <td>[<a target="_blank"  href="/FLEX/<bean:message key="add.plate.mfile.sample.jsp"/>">sample file</a>]</td>
            <td>[<a target="_blank"  href="/FLEX/<bean:message key="add.plate.psi.onefile.sample.jsp"/>">sample file</a>]</td>
        </logic:equal>
         <logic:equal name="projectname"  value="<%= Project.PROJECT_NAME_MGC %>" >
            <td>[<a target="_blank"  href="/FLEX/<bean:message key="add.plate.mgc.onefile.sample.jsp"/>">sample file</a>]</td>
        </logic:equal>
         <logic:equal name="projectname"  value="<%= Project.PROJECT_NAME_ORF %>" >
            <td>[<a target="_blank"  href="/FLEX/<bean:message key="add.plate.orf.onefile.sample.jsp"/>">sample file</a>]</td>
        </logic:equal>
         <% if (isOtherProject){ %>
            <td>[<a target="_blank"  href="/FLEX/<bean:message key="add.plate.yh.onefile.sample.jsp"/>">sample file</a>]</td>
        <%}%>
        
   

     
     
     
     </td>
    </tr>
       <!--   <llogic:equal name="projectname"  value="< %= Project.PROJECT_NAME_PSI %>" > -->
       <% if (submissionType == OutsidePlatesImporter.SUBMISSION_TYPE_NOTE_KNOWN
                ||
                submissionType == OutsidePlatesImporter.SUBMISSION_TYPE_PSI){%>
                <tr>
                    <td  class="prompt">Upload sequence information file :</td>
                    <td><html:file property="inputFile1" /></td><td>
                    [<a target="_blank"   href="/FLEX/<bean:message key="add.sequence.sample.jsp"/>">sample file </a>]</td>
                </tr>
              
               <tr>
                    <td class="prompt">Upload gene information file:</td>
                    <td><html:file property="inputGene" /></td><td>
                    [<a target="_blank"   href="/FLEX/<bean:message key="add.gene.sample.jsp"/>">sample file </a>]</td>
                </tr>
                <tr>    <td class="prompt" colspan=2>&nbsp;</td></tr>
          
                <tr>    <td class="prompt" colspan=2>Upload author information:</td></tr>
                <tr>
                    <td class="prompt">Author information file:</td>
                    <td><html:file property="inputAuthor" /></td><td>
                    [<a target="_blank"   href="/FLEX/<bean:message key="add.author.sample.jsp"/>">sample file</a>]</td>
                </tr>
                 <tr>
                    <td class="prompt">Author to clone connection file:</td>
                    <td><html:file property="inputAuthorConnection" /></td><td>
                    [<a target="_blank"   href="/FLEX/<bean:message key="add.author.connector.sample.jsp"/>">sample file</a>]</td>
                </tr>
                  <%}%>
          <!-- <//logic:equal>  -->   
</table></td></tr>
 <tr><td colspan=2>&nbsp;</td></tr>
       
<tr><td colspan="2"><h3>Researcher Information</h3></td></tr>
<tr bgcolor=e5f6ff><td class="prompt" >Researcher ID: </td><td><html:password property="researcherBarcode"/> </td></tr>  
 <tr><td colspan=2>&nbsp;</td></tr>
       
<tr><td colspan="2" align="center"><html:submit/></td>    </tr>
</table>
</html:form>
</body>
</html>
