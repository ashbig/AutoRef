<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="edu.harvard.med.hip.flex.Constants"%>
<%@ page import="edu.harvard.med.hip.flex.seqprocess.core.sequence.*" %>
<%@ page import="java.util.*" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : FLEX Sequence Info</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<H2><bean:message key="flex.name"/> : Experimental Sequence Information</h2>
<hr>
<html:errors/>
<p>
<% FullSequence fl = (FullSequence) request.getAttribute("FULL_SEQUENCE");
System.out.println(fl.getId());%>
<TABLE WIDTH=80% ALIGN=CENTER>
    <TR>
        <TD class="label">Flex ID:</td>
        <td><bean:write name="FULL_SEQUENCE" property="id"/></TD>
    </TR>
    <TR>
        <TD class="label">Sequence Type:</td>
        <td>Full Sequence</TD>
    </TR>
    <TR>
        <TD class="label">Sequence Length:</td>
        <td><bean:write name="FULL_SEQUENCE" property="length"/></TD>
    </TR>
    <TR>
        <TD class="label">Sequence Status:</td>
        <td><bean:write name="FULL_SEQUENCE" property="statusName"/></TD>
    </TR>
    <TR>
        <TD class="label">Sequence Quality:</td>
        <td><%= fl.getQualityName() %></TD>
    </TR>
    <TR>
        <TD class="label">Blast Results:</td>
        
        <TD >
        <input type=BUTTON onclick='' value='BLASTN'>
        
        <% if (fl.getBlastpFileName() != null)
{%>
            <input type=BUTTON onclick='' value='BLASTP'>
<%}%>
        </TD>
    </TR>
     
</TABLE>  
   
<h2>Sequence:</h2>
<p>
<bean:write name="COLOR_SEQUENCE" />
<p>
<h2>Discreptancies Description:</h2>
<p>
<% if ( fl.getAllMutations() != null && fl.getAllMutations().size() !=0)
{%>

    
        <table border='1'>
             <tr class="headerRow">
                 <th>&nbsp;Number;&nbsp;</th> 
                 <th>&nbsp;RNA Mutation;&nbsp;</th>
                 <th>&nbsp;Amino Acid Mutation;&nbsp;</th>
            </tr>
            <tr>
            <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">

            </flex:row>
        </table>
    
<%}
else
{%>
No Discreptancies resolved for the sequence.
<%}%>
 
   

</body>
</html>
