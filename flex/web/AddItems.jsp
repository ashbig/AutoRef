<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ page import="edu.harvard.med.hip.flex.infoimport.*" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : <bean:message key="flex.name"/>Add items</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
<%@ include file="define_user_access_level.jsp" %>

    <h2><bean:message key="flex.name"/> :  Add items</h2>
<hr>
<html:errors/>

<p>
<p><b>Please select one of the following:</b></p>
<table width="95%" border="0" cellspacing="4" cellpadding="4">
  <tr class="headerSectionRow">  
    <td width="50%" align="center">        Add new items     </td>
    <td  align="center" >Target sequence management</td>
  </tr>
      
    <tr>
    <td valign="top"  >
        
        
        <ul> 
        <% if (user_level >=  SADMIN ){%> 
        <li><a href="/FLEX/AddResearcher.jsp" target="display" >Add New Researcher</a> </li><%}%>
        <% if (user_level >= RESEARCHER){%>
        <li><a href="/FLEX/AddItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.IMPORT_INTO_NAMESTABLE_INPUT %>" ><%= ConstantsImport.PROCESS_NTYPE.IMPORT_INTO_NAMESTABLE_INPUT.getTitle() %>  </a> </li>
        <li><a href="/FLEX/AddItems.do?forwardName=<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_LINKERS_INPUT %>"><%=  ConstantsImport.PROCESS_NTYPE.IMPORT_LINKERS_INPUT.getTitle() %>   </a> </li>
        <li><a href="/FLEX/AddItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.IMPORT_VECTORS_INPUT %>"><%= ConstantsImport.PROCESS_NTYPE.IMPORT_VECTORS_INPUT.getTitle() %>  </a> </li>
        <li><a href="/FLEX/AddItems.do?forwardName=<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_CLONING_STRATEGIES_INPUT %>"><%=  ConstantsImport.PROCESS_NTYPE.IMPORT_CLONING_STRATEGIES_INPUT.getTitle() %>   </a> </li>  
         <%}%>
       
    </ul> </td>
   
    <td valign="top">
    <ul> 
      <% if (user_level >= SADMIN){%> 
 <li><a href="/FLEX/GetProjects.do?forwardName=IMPORT_SEQUENCES" target="display">Import Sequence Requests</a></li><%}%>
        <% if (user_level >= WADMIN ){%> 
        <li><a href="/FLEX/GetProjects.do?forwardName=APPROVE_SEQUENCES" target="display">Approve sequences</a></li><%}%>
         <% if (user_level >= RESEARCHER){%>
        <li><a href="/FLEX/CustomerRequest.do" target="display">Request Genes</a></li><%}%>
    </ul>   </td>
  </tr>
  <tr class="headerSectionRow" ><td align="center" > Change plate status</td>
  <td align="center">&nbsp;</td>
   <!-- <td align="center">Define vector growth conditions</td>-->
  
  </tr>
  <tr >
    <td  valign="top" > <% if (user_level >= RESEARCHER){%>
    
     <ul> 
<li><a href="/FLEX/AddItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.PUT_PLATES_FOR_SEQUENCING_INPUT %>">
<%= ConstantsImport.PROCESS_NTYPE.PUT_PLATES_FOR_SEQUENCING_INPUT.getTitle() %>    </a> </li>  
 <% if (user_level >= WADMIN){%>
 <li><a href="/FLEX/GetProjects.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.PUT_PLATES_IN_PIPELINE %>">
<%= ConstantsImport.PROCESS_NTYPE.PUT_PLATES_IN_PIPELINE.getTitle() %>    </a> </li>  
<%}%>
 <% if (user_level >= WADMIN){%>
 <li><a href="/FLEX/AddItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.FLEX_TABLE_POPULATE_INPUT %>">
<%= ConstantsImport.PROCESS_NTYPE.FLEX_TABLE_POPULATE_INPUT.getTitle() %>    </a> </li>  
<%}%>
</ul>
<%}%></td>
   <td valign="top">
   <!-- <ul> 
        <% if (user_level >=  WADMIN ){%> 
        <li><a href="/FLEX/AddGrowthConditionItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.ADD_BIOMATERIAL_INPUT %>" ><%= ConstantsImport.PROCESS_NTYPE.ADD_BIOMATERIAL_INPUT.getTitle() %>  </a> </li>  
        <li><a href="/FLEX/AddGrowthConditionItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.ADD_BIOMATERIAL_CONDITION_INPUT %>" ><%= ConstantsImport.PROCESS_NTYPE.ADD_BIOMATERIAL_CONDITION_INPUT.getTitle() %>  </a> </li>  
        <li><a href="/FLEX/AddGrowthConditionItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.ADD_BIOMATERIAL_COMBINATION_INPUT %>" ><%= ConstantsImport.PROCESS_NTYPE.ADD_BIOMATERIAL_COMBINATION_INPUT.getTitle() %>  </a> </li>  
        <li><a href="/FLEX/AddGrowthConditionItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.ADD_GROWTHCONDITION_INPUT %>" ><%= ConstantsImport.PROCESS_NTYPE.ADD_GROWTHCONDITION_INPUT.getTitle() %>  </a> </li>  
       <li><a href="/FLEX/AddGrowthConditionItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.ASSIGN_VECTOR_GROWTH_CONDITON_INPUT %>" ><%= ConstantsImport.PROCESS_NTYPE.ASSIGN_VECTOR_GROWTH_CONDITON_INPUT.getTitle() %>  </a> </li>  
       <%}%>
    </ul>  --> 
    </td>  
  </tr>
  
</table>

    
  

</body>
</html:html>
