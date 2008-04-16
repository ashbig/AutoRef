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

    <p><div alighn="center"> <b>Add new items</b></div></p>
      
    <ul> 
        <% if (user_level >=  SADMIN ){%> 
        <li><a href="/FLEX/AddResearcher.jsp" target="display">Add New Researcher</a> </li><%}%>
        <% if (user_level >= RESEARCHER){%>
        <li><a href="/FLEX/AddItems.do?forwardName=<%= String.valueOf(ConstantsImport.PROCESS_IMPORT_INTO_NAMESTABLE) %>" >Add new Name Type(s)  </a> </li>
        <li><a href="/FLEX/AddItems.do?forwardName=<%= String.valueOf(ConstantsImport.PROCESS_IMPORT_LINKERS) %>">Add new Linker(s)   </a> </li>
        <li><a href="/FLEX/AddItems.do?forwardName=<%= String.valueOf(ConstantsImport.PROCESS_IMPORT_VECTORS) %>">Add new Vector(s)  </a> </li>
        <li><a href="/FLEX/AddItems.do?forwardName=<%= String.valueOf(ConstantsImport.PROCESS_IMPORT_CLONING_STRATEGIES) %>">Add new Cloning Strategy    </a> </li>  
         <%}%>
        <P></p>
        <% if (user_level >= SADMIN){%> 
        <li><a href="/FLEX/GetProjects.do?forwardName=IMPORT_SEQUENCES" target="display">Import Sequence Requests</a></li><%}%>
        <% if (user_level >= WADMIN ){%> 
        <li><a href="/FLEX/GetProjects.do?forwardName=APPROVE_SEQUENCES" target="display">Approve sequences</a></li><%}%>
         <% if (user_level >= RESEARCHER){%>
        <li><a href="/FLEX/CustomerRequest.do" target="display">Request Genes</a></li><%}%>
        
    </ul>
   
<p>
     <% if (user_level >= RESEARCHER){%>
     <p><div alighn="center"> <b>Change plate status</b></div></p>
     <ul> 
<li><a href="/FLEX/AddItems.do?forwardName=<%= String.valueOf(ConstantsImport.PROCESS_PUT_PLATES_FOR_SEQUENCING) %>">Notify FLEX what plates were sequenced    </a> </li>  

</ul>
<%}%>

</body>
</html:html>
