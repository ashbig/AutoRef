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

    <h2><bean:message key="flex.name"/> :  Add items</h2>
<hr>
<html:errors/>

<p>
<html:form action="/AddItems.do"  method="POST">
<p><b>Please select one of the following:</b></p>

      
    <dl>    <dd>    <html:radio property="forwardName" value="<%= String.valueOf(ConstantsImport.PROCESS_IMPORT_INTO_NAMESTABLE) %>">Add new Name Type(s)    </html:radio>
    </dd>
    <dd>
    <html:radio property="forwardName" value="<%= String.valueOf(ConstantsImport.PROCESS_IMPORT_LINKERS) %>">Add new Linker(s)    </html:radio>    </dd>
    <dd>    <html:radio property="forwardName" value="<%= String.valueOf(ConstantsImport.PROCESS_IMPORT_VECTORS) %>">Add new Vector(s)    </html:radio>    </dd>  
    <dd>
    <html:radio property="forwardName" value="<%= String.valueOf(ConstantsImport.PROCESS_IMPORT_CLONING_STRATEGIES) %>">Add new Cloning Strategy    </html:radio>   
   <!-- <html:radio property="forwardName" value="<%= String.valueOf(ConstantsImport.PROCESS_IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX) %>">     </html:radio>  --> 

</dd>
    <p>
    <dd>
    <html:submit property="submit" value="Continue"/>
    <html:reset/>
    </dd>
    </dl>
  
     

</html:form>

</body>
</html:html>
