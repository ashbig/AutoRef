<%@ page language="java" %>
<%@ page import="java.util.*"%>
<%@ page import="edu.harvard.med.hip.flex.user.*"%>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="edu.harvard.med.hip.flex.Constants"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<% User user = (User)session.getAttribute(Constants.USER_KEY);
int user_level = 0;
int CUSTOMER = 0;
int COLLABORATOR = 1;
int RESEARCHER = 2;
int WADMIN = 3; 
int SADMIN = 4;
if (user.getUserGroup().equals("Customer")) user_level = CUSTOMER;
else if (user.getUserGroup().equals("Collaborator")) user_level = COLLABORATOR;
else if (user.getUserGroup().equals("Researcher")) user_level =RESEARCHER;
else if (user.getUserGroup().equals("Workflow Admin")) user_level = WADMIN;
else if (user.getUserGroup().equals("System Admin")) user_level = SADMIN;


%>
<html>
<head>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>



<html>
<head>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body bgcolor="#9bbad6">
<center>
<div>
<table>
<tr>
    <TD>
        <center><h3><bean:message key="flex.name"/></h3></center>
    </td>
</tr>
<tr>
    <td><hr></td>
</tr>
<tr>
    <td class="label">
        <small>        <a href="/FLEX/overview.jsp" target="display">Home</a>        </small>
    </td>
</tr>

<% if (user_level >=  RESEARCHER ){%> <tr>    <td>&nbsp;</td>  </tr>  <%}%>

<% if (user_level >=  SADMIN ){%> 
<tr>
    <td class="label">
        <small>        <a href="/FLEX/AddResearcher.jsp" target="display">Add New Researcher</a> </small>
    </td></tr><%}%>

<% if (user_level >= RESEARCHER){%>         
<tr>
    <td class="label">
        <small>
        <a href="/FLEX/CustomerRequest.do" target="display">Request Genes</a>
        </small>
    </td>
</tr><%}%>
<% if (user_level >= WADMIN ){%> 
<tr>
<td class="label">
    <small>
    <a href="/FLEX/GetProjects.do?forwardName=APPROVE_SEQUENCES" target="display">Approve sequences</a>
    </small>
</td></tr> <%}%>

<% if (user_level >= SADMIN){%> 
<tr>
    <td class="label">
        <small>
        <a href="/FLEX/GetProjects.do?forwardName=IMPORT_SEQUENCES" target="display">Import Sequence Requests</a>
        </small>
    </td>
</tr><%}%>

 <% if (user_level >= RESEARCHER){%> <TR><TD><B>Process</b> 
 <table  border="0">
 <% if (user_level >= WADMIN){%> 
<td width="10%"> &nbsp;</td>     <td class="label">
<small>
<a href="/FLEX/GetProjects.do?forwardName=SPECIAL_OLIGO_ORDER" target="display">Special Oligo Order</a>
</small>    </td></tr><%}%>
 <% if (user_level >= RESEARCHER){%>      
<td width="10%"> &nbsp;</td>     <td class="label">
            <small>
            <a href="/FLEX/SetReceiveDate.do" target="display">Receive Oligo Orders</a>
            </small>
        </td>
   </tr><%}%>
 <% if (user_level >= RESEARCHER){%>      
<td width="10%"> &nbsp;</td>     <td class="label">
        <small>
        <a href="/FLEX/GetProjects.do?forwardName=CREATE_PROCESS_PLATES" target="display">Create Process Plates</a>
        </small>
    </td>
</tr><%}%>
 <% if (user_level >= RESEARCHER){%>      
<td width="10%"> &nbsp;</td>     <td class="label">
   <small>
        <a href="/FLEX/GetProjects.do?forwardName=ENTER_RESULT" target="display">Enter Process Results</a>
        </small>
    </td>
</tr><%}%>
  <% if (user_level >= WADMIN){%>    
<td width="10%"> &nbsp;</td>     <td class="label">
   
      <small>
        <a href="/FLEX/RearraySelection.jsp" target="display">Rearray</a>
        </small>
    </td>
</tr><%}%>
  <% if (user_level >= RESEARCHER){%>    
<td width="10%"> &nbsp;</td>     <td class="label">
     <small>
        <a href="/FLEX/ExpressionCloneEntry.jsp" target="display">Expression Clones</a>
        </small>
    </td>
</tr><%}%>
  <% if (user_level >= RESEARCHER){%>    
<td width="10%"> &nbsp;</td>     <td class="label">
       <small>
        <a href="/FLEX/GetProjects.do?forwardName=PLATE_CONDENSATION" target="display">Plate Condensation</a>
        </small>
    </td>
</tr><%}%>
  <% if (user_level >= SADMIN){%>    
<td width="10%"> &nbsp;</td>     <td class="label">
       <small>
        <a href="/FLEX/ACEtoFLEXImporterInput.jsp" target="display">ACE to FLEX data transfer</a>
        </small>
    </td>
</tr><%}%>           

                </table><%}      %>
                </td></tr>
                
              
            
 <% if (user_level >= COLLABORATOR){%>  
 		<tr><td>&nbsp;</tr></td>
               <TR><TD><B>History</b>
                <table  border="0">
            
 <% if (user_level >= COLLABORATOR){%>    
<td width="10%"> &nbsp;</td>     <td class="label">
           <small>
            <a href="/FLEX/ContainerScan.jsp" target="display">Container History</a>
            </small>
        </td>
   </tr><%}%>
 <% if (user_level >= COLLABORATOR){%>    
<td width="10%"> &nbsp;</td>     <td class="label">

            <small>
            <a href="/FLEX/QuerySequenceHistory.do" target="display">Clone History</a>
            </small>
        </td>
   </tr><%}%>
 <% if (user_level >= COLLABORATOR){%>    
<td width="10%"> &nbsp;</td>     <td class="label">
            <small>
            <a href="/FLEX/QueryStorageCloneInput.jsp" target="display">Clone Storage</a>
            </small>
        </td>
   </tr><%}%>
           

                </table><%}%>
                </td></tr>
                
              
<% if (user_level >= RESEARCHER){%>    
            	<tr><td>&nbsp;</tr></td>
                <TR><TD><B>MGC Project</b>
                <table  border="0">
            
  <% if (user_level >= WADMIN){%>    
<td width="10%"> &nbsp;</td>     <td class="label">
              <small>
            <a href="/FLEX/ImportMgcCloneList.jsp" target="display">Import MGC Master List</a>
            </small>
        </td>
   </tr><%}%>
  <% if (user_level >= WADMIN){%>    
<td width="10%"> &nbsp;</td>     <td class="label">
        <small>
        <a href="/FLEX/GetProjects.do?forwardName=MGC_REQUEST_IMPORT" target="display">Import MGC Request</a>
        </small>
    </td>
</tr><%}%>
  <% if (user_level >= RESEARCHER){%>    
<td width="10%"> &nbsp;</td>     <td class="label">
        <small>
        <a href="/FLEX/GetProjects.do?forwardName=MGC_PLATE_HANDLE" target="display">MGC Plate Handle</a>
        </small>
    </td>
</tr><%}%>
  <% if (user_level >= RESEARCHER){%>    
<td width="10%"> &nbsp;</td>     <td class="label">
            <small>
            <a href="/FLEX/MgcPBSelectFile.jsp" target="display">Print New MGC Plates</a>
            </small>
        </td>
   </tr><%}%>
           

                </table><%}%>
                </td></tr>
                
              
 <% if (user_level >= CUSTOMER){%>    
           	<tr><td>&nbsp;</tr></td>
                <TR><TD><B>Query</b>
                <table  border="0">
            
   <% if (user_level >= COLLABORATOR){%>    
<td width="10%"> &nbsp;</td>     <td class="label">

       <small>
        <a href="/FLEX/SequenceQueryEntry.jsp" target="display">Query FLEXGene</a>
        </small>
    </td>
</tr><%}%>
   <% if (user_level >= CUSTOMER){%>    
<td width="10%"> &nbsp;</td>     <td class="label">
            <small>
            <a href="/FLEX/BrowseFlex.jsp" target="display">View Available Clones</a>
            </small>
        </td>
   </tr><%}%>
   <% if (user_level >= CUSTOMER){%>    
<td width="10%"> &nbsp;</td>     <td class="label">
            <small>
            <a href="/FLEX/GetSearchTerms.do" target="display">Search FLEXGene</a>
            </small>
        </td>
   </tr><%}%>
    <% if (user_level >= CUSTOMER){%>    
<td width="10%"> &nbsp;</td>     <td class="label">
        <small>
        <a href="/FLEX/GetAllSearchRecords.do" target="display">My Search History</a>
        </small>
    </td>
</tr><%}%>
           

                </table><%}%>
                </td></tr>
                <tr><td>&nbsp;</tr></td>
     <% if (user_level == CUSTOMER){%>    
           <tr>
                <td class="label">
                    <small>
                    <a href="/FLEX/Help.jsp" target="display">Help</a>
                    </small>
                </td>
            </tr><%}%>

        


<tr>
    <td class="label">
    <small>
    <a href="/FLEX/Logout.do" target="_top">Logout</a>
    </small>
    </td>
</tr> 
<tr>
    <td><hr></td>
</tr>
<tr>
    <td>&nbsp</td>
</tr> 
<tr>
    <td>
        <small>
        <address><a href="mailto:HIP_Informatics@hms.harvard.edu">FLEXGene Support</a></address>
        </small>
     </td>
</tr>
<tr>
    <td>&nbsp</td>
</tr>

</table>

<small>** This system and the underlying database was built in conjunction with
<a href="http://www.3rdmill.com" target="_blank">3rd Millennium Inc.</a> **</small>

</div>
</center>
</body>
</html>
