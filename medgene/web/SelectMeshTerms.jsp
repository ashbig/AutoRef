<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>MedGene : Disease Search</title>
    </head>
    <body>

    <center><br>
    <h1>Genes Associated with Multiple Diseases</h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td> 
    <html:errors/>

    <p><br>Following are the correspondng MeSH term(s). Please choose one from each group:</p>

    <html:form action="SelectMeshTerms.do">   

    <logic:iterate id="meshTerm" name="meshTerms">
    <p>
    <html:select property="diseaseTerms">
        <html:options
        collection="meshTerm"
        property="id"
        labelProperty="term"
        />
    </html:select>
    </p>
    </logic:iterate>

    <p><br>Please choose a <a href="statistic_menu.jsp" target="_blank">statistical method</a> to rank the gene list:
    <html:select property="stat">
        <html:options
        collection = "stats"
        property = "id"
        labelProperty = "type" 
        />
    </html:select>

    <p><br>
<p>For performance issue, we only display limited genes here.  If you want longer list, please <a href="mailto:yanhui_hu@hms.harvard.edu">email us</a>. 
<p><br>
    <html:submit property="submit" value="Get Genes"/>&nbsp;&nbsp;
    <html:submit property="submit" value="New Search"/>
    </html:form>
<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>