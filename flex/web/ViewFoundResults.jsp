<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Query Results</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<H2><bean:message key="flex.name"/> : Query Results</h2>
<hr>
<html:errors/>

<p>
<html:form action="/GetSearchResults.do">
    Total pages: <bean:write name="pages"/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    Page   <html:select property="currentPage">
            <html:options collection="allPages" property="name" labelProperty="value"/>  
            </html:select>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    Display <html:select property="pageSize">
            <html:options collection="pageSizes" property="name" labelProperty="value"/>  
            </html:select>
<html:hidden property="searchid"/>
<html:hidden property="condition"/>
<html:submit value="Go"/>
</html:form>
<TABLE border=1>
    <tr bgcolor="#9bbad6">
    <th>Search Term</th><th>Match Genbank</th><th>Match FLEXGene</th><th>Status</th><th>Found By</th><th>Alignments</th><th>Search Method</th>
    </tr>
    <logic:iterate name="results" id="result">
    <tr>
        <td rowspan="<bean:write name="result" property="numOfFoundFlex"/>"><bean:write name="result" property="searchTerm"/></td>
        <logic:iterate name="result" property="found" id="mgr">
            <td rowspan="<bean:write name="mgr" property="numOfMatchFlexSequence"/>">
            <A target="_blank" HREF="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="mgr" property="gi"/>&dopt=GenBank"> 
                <flex:write name="mgr" property="ganbankAccession"/>
            </A>
            </td>
            <logic:iterate name="mgr" property="matchFlexSequence" id="mfs">
                <td>
                <A target="_blank" HREF="ViewSequence.do?FLEX_SEQUENCE_ID=<bean:write name="mfs" property="flexsequenceid"/>">
                    <flex:write name="mfs" property="flexsequenceid"/>
                </A>
                </td>
                <td><flex:write name="mfs" property="flexSequence.flexstatus"/></td>
                <td><flex:write name="mfs" property="foundBy"/></td>
                <logic:equal name="mfs" property="isMatchByGi" value="F">
                <td><A target="_blank" href="ViewBlastOutput.do?outputFile=<bean:write name="mfs" property="blastHit.outputFile"/>">View Alignments</A></td>
                </logic:equal>
                <logic:notEqual name="mfs" property="isMatchByGi" value="F">
                <td>NA</td>
                </logic:notEqual>
                <td><flex:write name="mgr" property="searchMethod"/></td>
                </tr>
            </logic:iterate>
        </logic:iterate>    
    </TR>
    </logic:iterate>
</table>

</body>
</html>