
<%@ page language="java" %>
<%@ page errorPage="../ProcessError.do"%>

<%@ page import="edu.harvard.med.hip.flex.Constants"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.harvard.med.hip.flex.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : New Containers</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>

 

 </head>
<body>
    
    <h2><bean:message key="flex.name"/> : New Containers</h2>
    <hr>
    <html:errors/>
    
    <p>
  
 <h3> Populate containers based on Norgren log file.</h3>


<p>
 
 
<p>
<!-- create main table -->
 <% ArrayList containers = (ArrayList)request.getAttribute("LABELS"); 
 if (containers != null ){ 
 
<table  cellpadding=2 cellspacing=2 border=1>
     
     <tr class="headerRow">
        
        <TH ALIGN="center">Container ID</TH>
        <TH ALIGN="center">Container Label</TH>
       </TR>
      
 <logic:iterate  id="curPlate" name="LABELS"> 
        <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
<TD align="right">
                <a href="/FLEX/ViewContainerDetails.do?CONTAINER_ID=<bean:write name="curPlate" property="id"/>">
                  <bean:write name="curPlate" property="id"/> </a>
            </TD>
 <TD><bean:write name="curPlate" property="label"/>   </TD>
 </flex:row>
</logic:iterate> 

</TABLE>
 
<%}%>
<br>
<p>

 
</body>
</html>


  