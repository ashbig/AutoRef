<%--
        $Id: PendingRequests.jsp,v 1.2 2001-05-31 19:26:25 dongmei_zuo Exp $ 

        File    : PendingRequests.jsp
        Date    : 05042001
        Author  : Juan Munoz

        Display the list of pending sequence requests and allow the 
	Workflow manager to Accept, Reject or leave the sequence
	as pending
--%>

<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts.tld" prefix="struts" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html>
<head><title>Pending Requests</title></head>
<body>
    <H1><CENTER>Pending Requests</CENTER></H1> 
    <BR>
    
    <FORM method="POST" action="ProcessQueue.jsp">

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
    <logic:iterate id="curQueueItem" name="<%=edu.harvard.med.hip.flex.Constants.QUEUE_ITEM_LIST_KEY%>"> 
        <TR>
            <TD>
                
                <a href="ViewSequence.do?<%=edu.harvard.med.hip.flex.Constants.FLEX_SEQUENCE_ID_KEY%>=<bean:write name="curQueueItem" property="item.id"/>">
                    <bean:write name="curQueueItem" property="item.id"/>
                </a>
               
            </TD>
            <TD>
                <bean:write name="curQueueItem" property="item.description"/>
            </TD>
            <TD>
                <bean:write name="curQueueItem" property="item.quality"/>
            </TD>
            <TD>
                <A HREF="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="curQueueItem" property="item.gi"/>&dopt=GenBank"> 
                    <bean:write name="curQueueItem" property="item.accession"/>
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
                <SELECT name=<bean:write name="curQueueItem" property="item.id" />>
                    <Option > Pending
                    <Option > Rejected
                    <Option> Accepted
                </SELECT>   
            </TD>
        </TR>
    </logic:iterate> 


</TABLE>
<INPUT title="Process Requests" name="Process Requests" type="submit">
<INPUT type="reset">
</FORM>
</body>


</html>