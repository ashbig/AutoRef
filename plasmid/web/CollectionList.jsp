<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>PlasmID Database</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
    </head>
<div class="gridContainer clearfix">

    <body>
        <jsp:include page="orderTitle.jsp" />
        <table width="100%" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                
                <td width="83%" align="left" valign="top">
                    <!--<jsp:include page="collectionListTitle.jsp" />-->

                    <p class="text">List of plamid collections available to you. (Please note, you 
                        may see more collections available after you sign in.)
                    </p>
                    <p class="text">
                        Click on a specific collection below in order to:
                    </p>
                    <ol>
                        <li class="text">view a list of clones in a particular collection (small collections); or 
                        <li class="text">download an Excel spreadsheet that lists clones in the collection (large collections).
                    </ol>
                    <ul>
                        <logic:iterate name="<%=Constants.COLLECTION%>" id="collection">
                            <li><a class="itemtext" href="GetCollection.do?collectionName=<bean:write name="collection" property="name"/>"><bean:write name="collection" property="name"/></a></li>
                        </logic:iterate>
                    </ul>

                </td>
            </tr>
        </table>
                    <jsp:include page="footer.jsp" /></body></div>
</html>

