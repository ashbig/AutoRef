<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="edu.harvard.med.hip.flex.Constants"%>
<%@ page import="edu.harvard.med.hip.flex.seqprocess.core.sequence.*" %>
<%@ page import="edu.harvard.med.hip.flex.seqprocess.core.feature.*" %>
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
%>
<input type="hidden" id ="res"   name="blastn" value="<bean:write name="<%=Constants.FULL_SEQUENCE_BLAST_N_FORMATED%>"/>">
<input type="hidden" id ="res"   name="blastp" value="<bean:write name="<%=Constants.FULL_SEQUENCE_BLAST_P_FORMATED%>"/>">
<TABLE WIDTH="80%" ALIGN="CENTER">
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
        <input type=BUTTON onclick="showBlastN()"  value='BLASTN'>
        
        <% if (fl.getBlastpFileName() != null)
{%>
            <input type=BUTTON onclick='showBlastP()'  value='BLASTP'>
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
                 <th>Number</th> 
                 <th>RNA Mutation</th>
                 <th>Amino Acid Mutation</th>
            </tr>
            
            
            <% ArrayList muts= fl.getAllMutations() ;
              
               int cur_number = 0;
               Mutation mut = null;
               RNAMutation rm = null;
               AAMutation am = null;
               for (int count = 0; count < muts.size();count++)
                {
                     mut = (Mutation)muts.get(count);
                     System.out.println(mut.toString());
                    if (cur_number != mut.getNumber())
                    {%></tr><%}
                    if (mut.getType() == Mutation.RNA)
                    {

                        rm = (RNAMutation)muts.get(count);
                        cur_number++;
                        
                        %>
                        <tr>
                        <td><%= cur_number %></td>
                        <td><%= rm.toHTMLString()%></td>
                     <%}
                     else
                    {
                        am = (AAMutation)muts.get(count);
                        
                        %>
                        <td><%= am.toHTMLString()%></td>
                   
                    <%}
                        
                     

        }%>
        </table>
    
<%}
else
{%>
No Discreptancies resolved for the sequence.
<%}%>
 
   <SCRIPT LANGUAGE = "JavaScript">
<!--

function showBlastN() {

 
var s = window.document.all.blastn.value;	
var w = window.open("", "blast", 
"height=137,width=450,screenX=160,left=170,screenY=200,top=200,channelmode=0,dependent=0,directories=0,fullscreen=0,location=0,menubar=1,resizable=1,scrollbars=1,status=0,toolbar=1", "");
with(w.document) {
open("text/html");
write("<HTML>");
write('<HEAD><TITLE>Blast bresult...</TITLE></HEAD>');
write("<BODY ><PRE>");
write(s);
write("</PRE></BODY></HTML>");
}
}
function showBlastP() {

 
var s = window.document.all.blastp.value;	
var w = window.open("", "blast", 
"height=137,width=450,screenX=160,left=170,screenY=200,top=200,channelmode=0,dependent=0,directories=0,fullscreen=0,location=0,menubar=1,resizable=1,scrollbars=1,status=0,toolbar=1", "");
with(w.document) {
open("text/html");
write("<HTML>");
write('<HEAD><TITLE>Blast bresult...</TITLE></HEAD>');
write("<BODY ><PRE>");
write(s);
write("</PRE></BODY></HTML>");
}
}
-->
</SCRIPT>

</body>
</html>
