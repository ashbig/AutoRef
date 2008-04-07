<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="edu.harvard.med.hip.flex.Constants"%>
<%@ page import="edu.harvard.med.hip.flex.infoimport.*" %>

<%@ page import="java.util.*"%>
<%@ page import="edu.harvard.med.hip.flex.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>

        <title>Add items conformation</title>
    </head>
    <body>
  <h2>
      <logic:equal name="forwardName"  value="<%= String.valueOf(-ConstantsImport.PROCESS_IMPORT_VECTORS) %>">
            <bean:message key="flex.name"/> : <bean:message key="add.vector.title"/>  
     </logic:equal>
     <logic:equal name="forwardName"  value="<%= String.valueOf(-ConstantsImport.PROCESS_IMPORT_CLONING_STRATEGIES) %>">
        <bean:message key="flex.name"/> : <bean:message key="add.cloningstrategy.title"/> 
     </logic:equal>
     <logic:equal name="forwardName"  value="<%= String.valueOf(-ConstantsImport.PROCESS_IMPORT_INTO_NAMESTABLE) %>">
        <bean:message key="flex.name"/> : <bean:message key="add.name.title"/> 
     </logic:equal>
       <logic:equal  name="forwardName"  value="<%= String.valueOf(-ConstantsImport.PROCESS_IMPORT_LINKERS) %>">
            <bean:message key="flex.name"/> : <bean:message key="add.linker.title"/> 
        </logic:equal>
          <logic:equal  name="forwardName"  value="<%= String.valueOf(-ConstantsImport.PROCESS_PUT_PLATES_FOR_SEQUENCING) %>">
            <bean:message key="flex.name"/> : <bean:message key="put.plates.forsequencing.title"/> 
        </logic:equal>
            <logic:equal  name="forwardName"  value="<%= String.valueOf(-ConstantsImport.PROCESS_IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX) %>">
            <bean:message key="flex.name"/> : <bean:message key="add.items.plates.from.thirdparty"/> 
        </logic:equal>
      
  </h2>
    <hr>
    <html:errors/>
  
<hr>

  <logic:equal name="forwardName"  value="<%= String.valueOf(-ConstantsImport.PROCESS_IMPORT_VECTORS) %>">
            <bean:message key="flex.name"/> : <bean:message key="add.items.notificaton"/>  
     </logic:equal>
     <logic:equal name="forwardName"  value="<%= String.valueOf(-ConstantsImport.PROCESS_IMPORT_CLONING_STRATEGIES) %>">
        <bean:message key="flex.name"/> : <bean:message key="add.items.notificaton"/> 
     </logic:equal>
     <logic:equal name="forwardName"  value="<%= String.valueOf(-ConstantsImport.PROCESS_IMPORT_INTO_NAMESTABLE) %>">
        <bean:message key="flex.name"/> : <bean:message key="add.items.notificaton"/> 
     </logic:equal>
       <logic:equal  name="forwardName"  value="<%= String.valueOf(-ConstantsImport.PROCESS_IMPORT_LINKERS) %>">
            <bean:message key="flex.name"/> : <bean:message key="add.items.notificaton"/> 
        </logic:equal>
          <logic:equal  name="forwardName"  value="<%= String.valueOf(-ConstantsImport.PROCESS_IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX) %>">
            <bean:message key="flex.name"/> : <bean:message key="add.items.notificaton"/> 
        </logic:equal>
     
          <logic:equal  name="forwardName"  value="<%= String.valueOf(-ConstantsImport.PROCESS_PUT_PLATES_FOR_SEQUENCING) %>">
            <bean:message key="flex.name"/> : 
            <logic:present  name="plateLabels">
                <bean:message key="put.plates.forsequencing.notification"/> 
                <bean:message key="plateLabels"/>
            </logic:present>
            <logic:notPresent  name="plateLabels">
                All plates have been processed before
            </logic:notPresent>
        </logic:equal>
 

    
    </body>
</html>
