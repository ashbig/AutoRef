<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*" %>
<%@ page import="edu.harvard.med.hip.flex.infoimport.*" %>
<%@ page import="edu.harvard.med.hip.flex.workflow.*" %>

 




<html>
<head>
    <title><bean:message key="flex.name"/> :<%= request.getAttribute(Constants.UI_PAGE_TITLE) %></title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
<h2><bean:message key="flex.name"/> : <%= request.getAttribute(Constants.UI_PAGE_TITLE) %></h2>
<hr>
<html:errors/>
<p>


 
   <br>
 <div align="center"> 
<%--Loop through all the items  --%>
<logic:present name="<%= Constants.UI_TABLE_NO_DATA %>">
    <%= request.getAttribute(Constants.UI_TABLE_NO_DATA) %>
   
</logic:present>

<logic:present name="linkers">
       
         
       	
        
    <TABLE border="1" cellpadding="2" cellspacing="0">
        <tr class="headerRow">                       <td>Linker ID</td><td>Linker Name</td><td>Linker Sequence</td>    </tr>
         <logic:iterate id="linker" name="linkers"  >
        
             <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
                
                 <logic:empty name="linker" property="id">  <td>&nbsp;</td> </logic:empty>
                 <logic:notEmpty name="linker" property="id"> <td> <flex:write name="linker" property="id"/>   </td>    </logic:notEmpty>       
                
                    <logic:empty name="linker" property="name">  <td>&nbsp;</td> </logic:empty>
                 <logic:notEmpty name="linker" property="name"> <td> <flex:write name="linker" property="name"/>   </td>    </logic:notEmpty>       
             
                             <logic:empty name="linker" property="sequence">  <td>&nbsp;</td> </logic:empty>
		                    <logic:notEmpty name="linker" property="sequence"> <td> <flex:write name="linker" property="sequence"/>   </td>    </logic:notEmpty>   
		                     
          
           
            </flex:row>
             
        </logic:iterate>
    </table>
    
</logic:present>

<logic:present name="vectors">
    <TABLE border="1" cellpadding="2" cellspacing="0">
        <tr class="headerRow"> <td>Vector ID</td><td>Vector Name</td><td>Vector Source</td><td>Vector Type</td> 
   <td>Description</td> <td>HIP Name</td>  </tr>
       
        <logic:iterate id="vector" name="vectors"  >
          <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
   
              <logic:empty name="vector" property="vectorid" >           <td>&nbsp;</td>  </logic:empty>
      <logic:notEmpty name="vector" property="vectorid"  ><td> <bean:write name="vector" property="vectorid"/>  </td> </logic:notEmpty>
 
          <logic:empty name="vector" property="name" >           <td>&nbsp;</td>  </logic:empty>
      <logic:notEmpty name="vector" property="name"  ><td> <bean:write name="vector" property="name"/>  </td> </logic:notEmpty>
 
         <logic:empty name="vector" property="source" >           <td>&nbsp;</td>  </logic:empty>
      <logic:notEmpty name="vector" property="source"  ><td> <bean:write name="vector" property="source"/>  </td> </logic:notEmpty>
 
      <logic:empty name="vector" property="type" ><td>&nbsp;</td></logic:empty>
     <logic:notEmpty name="vector" property="type"  > <td> <bean:write name="vector" property="type"/>  </td>                       </logic:notEmpty>
        
        <logic:empty name="vector" property="description" ><td>&nbsp;</td></logic:empty>
	 <logic:notEmpty name="vector" property="description"  > <td><bean:write name="vector" property="description"/>  </td>         </logic:notEmpty>
       
         <logic:empty name="vector" property="hipname" ><td>&nbsp;</td></logic:empty>
	 <logic:notEmpty name="vector" property="hipname"  > <td><bean:write name="vector" property="hipname"/>  </td>         </logic:notEmpty>
       
          
          </flex:row>
        </logic:iterate>
    </table>
</logic:present>


<logic:present name="clstrategies">
    <TABLE border="1" cellpadding="2" cellspacing="0">
        <tr class="headerRow"> <td>Cloning Strategy Name</td><td>Vector Name</td><td>5p Linker Name</td>    <td>3p Linker Name</td> <td>Type</td>  </tr>
 <% ArrayList cl = (ArrayList) request.getAttribute("clstrategies");
        %>
        
        <logic:iterate id="str" name="clstrategies"  >
          <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
          <logic:empty name="str" property="name" > <td>&nbsp;</td></logic:empty>
          <logic:notEmpty name="str" property="name"  > <td><bean:write name="str" property="name"/></td> </logic:notEmpty>
         
          <logic:empty name="str" property="clonevector.name" > <td>&nbsp;</td></logic:empty>
          <logic:notEmpty name="str" property="clonevector.name"  > <td><bean:write name="str" property="clonevector.name"/></td> </logic:notEmpty>
         
          <logic:empty name="str" property="linker5p.name" > <td>&nbsp;</td></logic:empty>
          <logic:notEmpty name="str" property="linker5p.name"  > <td><bean:write name="str" property="linker5p.name"/></td> </logic:notEmpty>
         
          <logic:empty name="str" property="linker3p.name" > <td>&nbsp;</td></logic:empty>
          <logic:notEmpty name="str" property="linker3p.name"  > <td><bean:write name="str" property="linker3p.name"/></td> </logic:notEmpty>
         
          <logic:empty name="str" property="type" > <td>&nbsp;</td></logic:empty>
          <logic:notEmpty name="str" property="type"  > <td><bean:write name="str" property="type"/></td> </logic:notEmpty>
            
      
      
            </flex:row>
        </logic:iterate>
    </table>
</logic:present>

<logic:present name="author">
<TABLE border="0" cellpadding="2" cellspacing="2">
    <tr><td class="label">Author Name:</td><td><bean:write name="author" property="name"/></td> </tr>
    <tr><td class="label">Author Type:</td><td><bean:write name="author" property="type"/></td> </tr>
    
    <tr><td class="label">Author First Name:</td><td><bean:write name="author" property="FNName"/></td> </tr>
    <tr><td class="label">Author Last Name:</td><td><bean:write name="author" property="FLName"/></td> </tr>
    
    <tr><td class="label">Author Address:</td><td><bean:write name="author" property="adress"/></td> </tr>
    <tr><td class="label">Author Telephone:</td><td><bean:write name="author" property="tel"/></td> </tr>
    <tr><td class="label">Author FAX:</td><td><bean:write name="author" property="fax"/></td> </tr>
    <tr><td class="label">Author E-mail:</td><td><bean:write name="author" property="EMail"/></td> </tr>
    <tr><td class="label">Author WWW:</td><td><bean:write name="author" property="WWW"/></td> </tr>
    <tr><td class="label">Author Description:</td><td><bean:write name="author" property="description"/></td> </tr>
  
    
</table>
    
</logic:present>
    

<logic:present name="workflows">
    <TABLE border="1" cellpadding="2" cellspacing="0">
        <tr class="headerRow"> <td>Workflow ID</td><td>Workflow Name</td><td>Structure</td>  </tr>
          <logic:iterate id="str" name="workflows"  >
          <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
            <td><bean:write name="str" property="id"/></td>
            <td><bean:write name="str" property="name"/></td>
          <td>   <input type=BUTTON value="Display" onCLick="window.open('AddWorkflowItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.DISPLAY_WORKFLOW.toString()%>&amp;workflowid=<bean:write name="str" property="id"/>','DisplayWorkflow','width=700,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;" >
          </td>
          
          </flex:row>
      </logic:iterate>
    </table>
    </logic:present>
<br>


</div> 
</body>
</html>