<%--
        $Id: MgcDisplayNewPlates.jsp,v 1.1 2002-05-14 21:31:40 Elena Exp $ 

       
        Author  : htaycher

        Display the list of labels for new mgc plates which info was imported
        from specified file and allow the Researcher print them
	
--%>

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ page import="edu.harvard.med.hip.flex.Constants"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : New Mgc Clone Containers</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
 </head>
<body>
    
    <h2><bean:message key="flex.name"/> : New Mgc Clone Containers</h2>
    <hr>
    <html:errors/>
    <p>
    <html:form action="ProcessQueue.do">

    <input type="hidden" name="fileName" value=<bean:write name="fileName"/>>
    <h3> Print barcodes for New Mgc Plates.</h3>
    (Distribution file <bean:write name="fileName"/> )
</table>
<td><tr>
<html:submit property= "action" value = "Print">
</tr><tr>
<html:submit property= "action" value = "Print All">
</tr></td></table>
<
<p>
 
<!-- create main table -->
        </table>
        </td>
        </tr>
    <tr class="headerRow">
        <TH>Original Mgc Plate Label</TH>
        <TH>Flex Mgc Plate Label</TH>
        <TH> Print </TH>
        
    </TR>
    
    <!-- iterate through each QueueItem (sequence) that is in the queue -->
    <!-- keep track of the count -->

    <logic:iterate  id="clone" name="LABELS"> 
        <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
            <TD>
                <bean:write name="clone" property="value"/>
             </TD>
            <TD>
                <bean:write name="clone" property="key"/>
            </TD>
            <TD>
                <input type="checkbox" name="selection" value="<bean:write name="clone" property="key"/>" >
            </TD>
         </flex:row>
   </logic:iterate>
  
</TABLE>

<br>
<p>


</html:form>
</body>


</html>