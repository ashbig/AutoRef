<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : <bean:message key="flex.name"/>Browse FLEXGene Clone Collection</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : <bean:message key="flex.name"/> Database Search</h2>
<hr>
<html:errors/>

<p>
<html:form action="/BrowseFlex.do" focus="searchTerm" method="POST">
<p><b>Please select one of the following:</b></p>

    <dl>
    <dd>
    <html:radio property="browseSelect" value="human">
        View all sequence verified human clones.
    </html:radio> 
    <dd>
    <html:radio property="browseSelect" value="kinase">
        View all sequence verified human kinase related clones.
    </html:radio>
    </dd>
    <p>
    <dd>
    <html:submit property="submit" value="Continue"/>
    <html:reset/>
    </dl>
</html:form>

</body>
</html:html>