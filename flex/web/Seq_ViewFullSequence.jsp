<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : FLEX Sequence Info</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<H2><bean:message key="flex.name"/> : FLEX Sequence Info</h2>
<hr>
<html:errors/>
<p>
<% FullSequence fl = (FullSequence) request.getAttribute("fullsequence"); %>
<TABLE WIDTH=80% ALIGN=CENTER>
    <TR>
        <TD class="label">Flex ID:</td>
        <td><%= fl.getId() %></TD>
    </TR>
    <TR>
        <TD class="label">Sequence Type:</td>
        <td>Full Sequence</TD>
    </TR>
    <TR>
        <TD class="label">Sequence Length:</td>
        <td><%= fl.getLength() %></TD>
    </TR>
    <TR>
        <TD class="label">Sequence Status:</td>
        <td><%= fl.getStatusName() %></TD>
    </TR>
    <TR>
        <TD class="label">Sequence Quality:</td>
        <td><%= fl.getQualityName() %></TD>
    </TR>
    <TR>
        <TD class="label">Blast Results:</td>
        <td>&nbsp;</TD>
        <TD >
        <input type=BUTTON onclick='' value='BLASTN'>
        </td>
        <td>
        <% if (fl.getBlastpFileName() != null)
{%>
            <input type=BUTTON onclick='' value='BLASTP'>
<%}%>
        </TD>
    </TR>
    <TR>
        <TD class="label">Sequence:
        <bean:write name="SEQUENCE" property="text"/>
        </TD>
    </TR>
    <TR>
<% if fl.getAllMutations() != null && fl.getAllMutations().size !=0)
{%>
    <TD class="label">Mutations:</td>
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
    <TD>
<%}
else
{%>
&nbsp;
<%}%>
        
      
    </TR>
   
   
</TABLE>
</body>
</html>
