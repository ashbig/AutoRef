<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>MedGene : Search Associated Genes For a Particular Gene</title>
    </head>
    <body>

    <center>
    <h1>Genes Associated with a Particular Gene</h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td> 
    <html:errors/>

    <p>Following are the corresponding gene symbol(s). Please choose one:</p>

    <html:form action="SearchGenesForGene_step2.do">  

    <html:select property="gene">
        <html:options
        collection="geneIndexes"
        property="indexid"
        labelProperty="index"
        />
    </html:select>

    <p>Please choose a <a href="statistic_menu.jsp" target="_blank">statistical method</a> to rank the gene list:
<p>
    <html:select property="stat">
        <html:options
        collection = "stats"
        property = "id"
        labelProperty = "type" 
        />
    </html:select>

    <p>Please choose the number of genes for your list:
    <html:select property="number">
        <% int i = ((Integer)(session.getAttribute("user_type"))).intValue();
           if (i != 1) { %>
            <html:option key="top 10" value="10"/>
            <html:option key="top 25" value="25"/>
        <% } 
           else { %>
           <html:option key="top 25" value="25"/>
           <html:option key="top 50" value="50"/>
           <html:option key="top 100" value="100"/>
           <html:option key="top 500" value="500"/>
           <html:option key="top 1000" value="1000"/>
           <html:option key="top 2000" value="2000"/>
        <% } %>
    </html:select>

    <p>
<p>For performance issue, we only display limited genes here.  If you want longer list, please <a href="mailto:yanhui_hu@hms.harvard.edu">email us</a>. 
<p>
    <html:submit property="submit" value="Get Genes"/>
    <html:submit property="submit" value="New Search"/>
    </html:form>
<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>