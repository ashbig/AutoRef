<%-- 
    Document   : Admin
    Created on : Dec 15, 2014, 3:06:52 PM
    Author     : user
--%>

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <title>Internal Administrative Page</title>
    </head>
    <div class="gridContainer clearfix">
    <body>
        <jsp:include page="signinMenuBar.jsp" />
        <div id='admin'>
            <ul>
                    <li><a href="ViewContainers.jsp" title="View Containers">View Containers</a></li>
                    <li><a href="vSearch.jsp" title="Vector Submission">Vector Submission</a></li>
                    <li><a href="vSearchSFD.jsp" title="Vector For Distribution">Vector For Distribution</a></li>
                    <li><a href="PrepareSearchInvoice.do" title="Search Invoices">Search Invoices</a></li>
                    <li><a href="pReceiveSearch.jsp" title="Recieve Plasmids">Receive Plasmids</a></li>
                    <li><a href="AddInstitutionsInput.jsp" title="Add Institutions">Add Institutions</a></li>
                    <li><a href="AddEmtaMembersInput.jsp" title="Add Institution to Expedited MTA Members">Add EP-MTA Members</a></li>   
                    <li><a href="SEQ_InvoiceHome.jsp">Sequencing Invoices</a></li>
            </ul>
        </div>
        <div id='mobilehome'>
            <ul>
                    <li><a href="ViewContainers.jsp" title="View Containers">View Containers</a></li>
                    <li><a href="vSearch.jsp" title="Vector Submission">Vector Submission</a></li>
                    <li><a href="vSearchSFD.jsp" title="Vector For Distribution">Vector For Distribution</a></li>
                    <li><a href="PrepareSearchInvoice.do" title="Search Invoices">Search Invoices</a></li>
                    <li><a href="pReceiveSearch.jsp" title="Recieve Plasmids">Receive Plasmids</a></li>
                    <li><a href="AddInstitutionsInput.jsp" title="Add Institutions">Add Institutions</a></li>
                    <li><a href="AddEmtaMembersInput.jsp" title="Add Institution to Expedited MTA Members">Add EP-MTA Members</a></li>   
                    <li><a href="SEQ_InvoiceHome.jsp">Sequencing Invoices</a></li>
            </ul>
        </div>   
        <jsp:include page="footer.jsp" />
    </body>
    </div>
</html>
