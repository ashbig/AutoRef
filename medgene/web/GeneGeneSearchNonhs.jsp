<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
    <head>
        <title>MedGene : Gene Search</title>
    </head>
    <body>
    <center>
    <h1><br>Genes Associated with a Particular Gene</h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td>
    <html:errors/>
   
    <html:form action="GeneGeneSearchNonhs.do">   
    <p><br>Please type in your gene term (LocusID, Unigene or Accession). <br></p>
    <table width="80%" align="left">
        <tr>
            <td><html:select property="term">
                <html:option value="1"> LocusID </html:option>
                <html:option value="2"> Unigene </html:option>
                <html:option value="3"> Accession </html:option>
                 </html:select>
            </td>
            <td><html:text property="searchTerm" size="50"/></td>
        </tr>
    </table>

    <p><br><br><br><br>Please choose a <a href="statistic_menu.jsp" target="_blank">statistical method</a> to rank the disease list:
    <html:select property="stat">
        <html:options
        collection = "stats"
        property = "id"
        labelProperty = "type" 
        />
    </html:select>

    <p><br>Please choose the number of genes for your list:
    <html:select property="number">
        <% int i = ((Integer)(session.getAttribute("user_type"))).intValue();
           if (i != 1) { %>
            <html:option key="top 5" value="10"/>
            <html:option key="top 10" value="25"/>
            <html:option key="top 50" value="100"/>
        <% } 
           else { %>
            <html:option key="top 25" value="25"/>
            <html:option key="top 50" value="50"/>
            <html:option key="top 100" value="100"/>
            <html:option key="top 500" value="500"/>
            <html:option key="top 1000" value="1000"/>
        <% } %>
    </html:select>

    <p>
<p><br>For performance issue, we only display limited genes here.  If you want longer list, please <a href="mailto:yanhui_hu@hms.harvard.edu">email us</a>. 
<p><br>
    <html:submit property="submit" value="Get Genes"/>
    <html:submit property="submit" value="New Search"/>


    </html:form>

<br><br><br><br><br><p>
<jsp:include page="links.jsp" flush="true"/>
</body>
</html>

