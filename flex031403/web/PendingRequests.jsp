<%--
        $Id: PendingRequests.jsp,v 1.12 2001-07-16 19:39:31 jmunoz Exp $ 

        File    : PendingRequests.jsp
        Date    : 05042001
        Author  : Juan Munoz

        Display the list of pending sequence requests and allow the 
	Workflow manager to Accept, Reject or leave the sequence
	as pending
--%>

<%@ page language="java" %>
<%@ page import="edu.harvard.med.hip.flex.Constants"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html>
<head><title><bean:message key="flex.name"/> : Pending Requests</title></head>
<body>
    
    <h2><bean:message key="flex.name"/> : Pending Requests</h2>
    <hr>
    <html:errors/>
    <p>
    <FORM method="POST" action="ProcessQueue.do">

    <TABLE BORDER>
    <TR>
        <TH>FLEX id</TH>
        <TH>Description</TH>
        <TH>Quality</TH>
        <TH>GenBank</TH>
        <TH>Requesting Users</TH>
        <TH>Status</TH>
    </TR>
    
    <!-- iterate through each QueueItem (sequence) that is in the queue -->
    <!-- keep track of the count -->
    <% int seqCount = 0;%>
    <logic:iterate id="curQueueItem" name="<%=edu.harvard.med.hip.flex.Constants.QUEUE_ITEM_LIST_KEY%>"> 
        <TR>
            <TD>
                <flex:linkFlexSequence sequenceName="curQueueItem" seqProperty="item">
                    <bean:write name="curQueueItem" property="item.id"/>
                </flex:linkFlexSequence>
                           
            </TD>
            <TD>
                <bean:write name="curQueueItem" property="item.description"/>
            </TD>
            <TD>
                <bean:write name="curQueueItem" property="item.quality"/>
            </TD>
            <TD>
                <A target="_new" HREF="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="curQueueItem" property="item.gi"/>&dopt=GenBank"> 
                    <bean:write name="curQueueItem" property="item.gi"/>
                </A>
            </TD>
            <TD>
                <logic:iterate id="curUser" name="curQueueItem" property="item.requestingUsers">
                    <A HREF="mailto:<bean:write name="curUser" property="userEmail"/>">
                        <bean:write name="curUser" property="username"/>
                    </A>
                </logic:iterate>
            </TD>
            <TD>
                <SELECT name="INDEX<%=seqCount++%>">
                    <Option> Pending</option>
                    <Option> Rejected</option>
                    <Option> Accepted</option>
                </SELECT>   
            </TD>
        </TR>
    </logic:iterate> 


</TABLE>

<br>

<INPUT title="Process Requests" name="Process Requests" type="submit">
<INPUT type="reset">
</FORM>
</body>


</html>