<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Search FLEXGene Database</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Search FLEXGene Database</h2>
<hr>
<html:errors/>

<p>
You can search FLEXGene database by using the following terms: 
<ul>
<li>Genbank Accession
<li>GI
<li>Locus ID
<li>Gene Symbol
</ul>
<p>
For each term except GI, the program will search LocusLink database to find all the matching mRNAs and their GI numbers. It then performs two step query againt the FLEXGene database for each GI number. The first step is to find all the matching sequence records by comparing GI numbers. If at least one sequence record is found, the search returns the results. If no matching sequences are found in the database by this comparison, the program will retrieve the sequences using GI numbers from Genbank, and blast against the FLEXGene database to find all the sequence records that matches the user defined blast criteria. Two blast criteria are used to define a blast match: "Percent Identity" and "Alignment Length". The default setting is 90% for percent identity and 100nt for alignment length, which means any blast hit with at least one alignment above or equal to 90% over 100nt will be returned to the user as match. User can define the maximum number of blast hits shown on the results (default to five). User can also choose which database to blast against. The default sets to all the human genes cloned or attempted to be cloned in the FLEXGene repository. 

<p>Searches are performed offline, and each search is identified by a "Search Name" defined by user. User can view the search results and history from the link "My Search History" on the left menu bar.
<!--<p>Click <a href="query_flow_v1.pdf">here</a> to view the flow chart.-->
<p><hr>
<html:form action="/SearchFlex.do" focus="searchTerm" method="POST" enctype="multipart/form-data">

<table border=0 cellspacing=10 cellpadding=2>
    <tr>
    <td class="prompt">Search Name:</td>
    <td><html:text property="searchName"/></td>
    <td></td>
    </tr>

    <tr>
    <td class="prompt">Search Term:</td>
    <td><html:select property="searchType">
        <html:options
        collection="searchTerms"
        property="searchName"
        labelProperty="searchValue"
        />
    </html:select></td>
    <td></td>
    </tr>

    <tr>
    <td></td>
    <td><html:radio property="searchTermType" value="nonfile">
        Enter all the search terms: (separated by spaces)
        </html:radio>
    </td>
    <td><html:radio property="searchTermType" value="file">
        Upload the file containing all the search terms: (separated by spaces)
        </html:radio>
    </td>
    </tr>

    <tr>
    <td></td>
    <td><html:textarea property="searchTerm" rows="10"/>
    </td>
    <td valign="top"><html:file property="filename" />
    </td>
    </tr>

    <tr>
    <td class="prompt">Blast Parameters:</td>
    <td></td><td></td>
    </tr>

    <tr>
    <td></td>
    <td class="prompt">Percent Identity (%):
    <td><html:text property="pid"/></td>
    </tr>

    <tr>
    <td></td>
    <td class="prompt">Alignment Length:
    <td><html:text property="length"/></td>
    </tr>

    <tr>
    <td></td>
    <td class="prompt">Maximum Hits:
    <td><html:text property="hits"/></td>
    </tr>

    <tr>
    <td></td>
    <td class="prompt">Database:
    <td><html:select property="searchDatabase">
        <html:options
        collection="searchDatabases"
        property="name"
        labelProperty="value"
        />
    </html:select></td>
    </tr>

 
    <tr>
    <td></td>
    <td><html:submit property="submit" value="Continue"/></td>
    <td><html:reset/></td>
    </tr>
</table>
</html:form>

</body>
</html:html>