<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : <bean:message key="flex.name"/> Database Search</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : <bean:message key="flex.name"/> Database Search</h2>
<hr>
<html:errors/>

<p>
<html:form action="/SequenceQueryEntry.do" focus="searchTerm" method="POST">
<p><b>Please select one of the following:</b></p>

    <dl>
    <dd>
    <html:radio property="querySelect" value="queryAllGenes">
        Search available genes in the FLEXGene database.
    </html:radio>
    </dd>
    <dd>
    <html:radio property="querySelect" value="queryGenes">
        Search genes currently in the cloning process (including cloning failed
        and sequence verified genes).
    </html:radio>
    </dd>
    <dd>
    <html:radio property="querySelect" value="queryClones">
        Search available sequence verified clones for specific genes.
    </html:radio>
    </dd>
    <dd>
    <html:radio property="querySelect" value="querySuccessRate">
        Estimate project success rate.
    </html:radio>
    </dd>
    <p>
    <dd>
    <html:submit property="submit" value="Continue"/>
    <html:submit property="reset" value="Reset"/>
    </dd>
    </dl>
</html:form>

</body>
</html:html>