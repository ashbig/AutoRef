<%--
        $Id: PendingRequests.jsp,v 1.14 2001-07-19 17:15:09 jmunoz Exp $ 

        File    : PendingRequests.jsp
        Date    : 05042001
        Author  : Juan Munoz

        Display the list of pending sequence requests and allow the 
	Workflow manager to Accept, Reject or leave the sequence
	as pending
--%>

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

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
    <html:form action="ProcessQueue.do">
    
    <logic:present name="PAGES">
        Jump to page: 
        <logic:iterate name="PAGES" id="pageLink">
            <logic:notEqual name="CURRENT_PAGE" value="<%=pageLink.toString()%>">
                <html:link forward="approveSequences" paramId="<%=Constants.PAGE_KEY%>" paramName="pageLink">
                    <bean:write name="pageLink"/>
                </html:link>
            </logic:notEqual>
            <logic:equal name="CURRENT_PAGE" value="<%=pageLink.toString()%>">
                <bean:write name="pageLink"/>
            </logic:equal>
        </logic:iterate>
        &nbsp;
    </logic:present>

    <br>

    <logic:present name="prevPage">
        <html:link forward="approveSequences" paramId="<%=Constants.PAGE_KEY%>" paramName="prevPage">
        << Previous
        </html:link>
    </logic:present>
   
    <logic:notPresent name="prevPage">
        << Previous
    </logic:notPresent>
    |
    <logic:present name="nextPage">
        <html:link forward="approveSequences" paramId="<%=Constants.PAGE_KEY%>" paramName="nextPage">
            Next >>
        </html:link>
    </logic:present>
    <logic:notPresent name="nextPage">
        Next >>
    </logic:notPresent>
    
    <TABLE BORDER>
    <TR>
        <TH>FLEX Id</TH>
        <TH>Description</TH>
        <TH>Quality</TH>
        <TH>GenBank</TH>
        <TH>Requesting Users</TH>
        <TH>Status</TH>
    </TR>
    
    <!-- iterate through each QueueItem (sequence) that is in the queue -->
    <!-- keep track of the count -->

    <logic:iterate indexId="seqCount" id="curQueueItem" name="<%=edu.harvard.med.hip.flex.Constants.QUEUE_ITEM_LIST_KEY%>"> 
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
                <logic:iterate id="curUser" name="curQueueItem" property="item.requestingUsers" indexId="userCount">
                    <A HREF="mailto:<bean:write name="curUser" property="userEmail"/>">
                        <bean:write name="curUser" property="username"/>
                    </A>
                    <logic:notEqual name="userCount" value="0">
                        <br>
                    </logic:notEqual>
                </logic:iterate>
            </TD>
            <TD>
                <html:select property='<%="status["+seqCount+"]"%>'>
                    <html:option value="Pending">Pending</html:option>
                    <html:option value="Rejected">Rejected</html:option>
                    <html:option value="Accepted">Accepted</html:option>
                </html:select>
            </TD>
        </TR>
    </logic:iterate> 


</TABLE>

<br>
<p>
<INPUT title="Process Requests" name="Process Requests" type="submit" value="Process Requests">
<INPUT type="reset">
</html:form>
</body>


</html>