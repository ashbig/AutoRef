<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Cloning Request History</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Cloning Request History: Confirm Selection</h2>
<hr>
<html:errors/>
<p>
<h3>You have submitted the following sequences. They are grouped into different
categories based on similarity to FLEXGene sequences and qualities. Please make
appropriate selection and submit your cloning request to FLEXGene.</h3>

<p>
<logic:present name="submittedSequences">
<logic:iterate id="submittedSeq" name="submittedSequences">
<bean:write name="submittedSeq" property="accession" />
<br>
</logic:iterate>
</logic:present>

<html:form action="/ConfirmSelection.do">

<logic:present name="newSequences">
<p>
<h3>Category: Good quality sequences not already present in FLEXGene</h3>
<table border="1">
    <tr class="headerRow">
        <th></th><th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>
    </tr>
    <logic:iterate id="ns" name="newSequences">
    <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
        <logic:equal name="ns" property="speciesCategory" value="allowed">
        <td><input name="selection" type="checkbox" value="<bean:write name="ns" property="gi"/>"></td>
        </logic:equal>
        <logic:notEqual name="ns" property="speciesCategory" value="allowed">
        <td></td>
        </logic:notEqual>
        <td><a target="_new" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="ns" property="gi"/>&dopt=GenBank"><bean:write name="ns" property="accession"/></a></td>
        <td><bean:write name="ns" property="description"/></td>
        <td><bean:write name="ns" property="gi"/></td>
        <td><bean:write name="ns" property="species"/></td>
        <td><bean:write name="ns" property="flexstatus"/></td>
        <td><bean:write name="ns" property="quality"/></td>
    </flex:row>
    </logic:iterate>
</table>
</logic:present>

<logic:present name="sameSequence">
<p>
<h3>Category: Good quality sequences already present in FLEXGene</h3>
<table border="1">
    <tr class="headerRow">
        <th></th><th>FLEXGene ID</th><th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>
    </tr>

    <logic:iterate id="ss" name="sameSequence">
    <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
        <logic:equal name="ss" property="speciesCategory" value="allowed">
        <td><input name="selection" type="checkbox" value="<bean:write name="ss" property="gi"/>"></td>
        </logic:equal>
        <logic:notEqual name="ss" property="speciesCategory" value="allowed">
        <td></td>
        </logic:notEqual>
        <td><a href="ViewSequence.do?<%= Constants.FLEX_SEQUENCE_ID_KEY %>=<bean:write name="ss" property="id"/>"><bean:write name="ss" property="id"/></a></td>
        <td><a target="_new" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="ss" property="gi"/>&dopt=GenBank"><bean:write name="ss" property="accession"/></a></td>
        <td><bean:write name="ss" property="description"/></td>
        <td><bean:write name="ss" property="gi"/></td>
        <td><bean:write name="ss" property="species"/></td>
        <td><bean:write name="ss" property="flexstatus"/></td>
        <td><bean:write name="ss" property="quality"/></td>
    </flex:row>
    </logic:iterate>
</table>
</logic:present>

<logic:present name="displayHomologs">
<p>
<h3>Category: Good quality sequences with related FLEXGene sequence</h3>

<logic:present name="cdsMatchSequences">
<logic:iterate id="cdsMatch" name="cdsMatchSequences">
<p>
Evalue: <bean:write name="cdsMatch" property="value.blastResults.evalue"/><br>
Identity: <bean:write name="cdsMatch" property="value.blastResults.identity"/><br>
Query CDS length: <bean:write name="cdsMatch" property="value.blastResults.cdslength"/>      
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
[ <a target=_new href="ViewAlignment.do?gi=<bean:write name="cdsMatch" property="key"/>">View Alignment</a> ]
<table border=1>
    <tr class="headerRow">
        <td></td><th>FLEXGene ID</th><th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>
    </tr>
    <logic:iterate id="h" name="cdsMatch" property="value.homolog">
    <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
        <logic:equal name="h" property="id" value="-1">
        <td><font color=red><bean:message key="flex.notapplicable" /><br></font></td>
        </logic:equal>
        <logic:notEqual name="h" property="id" value="-1">
        <td ROWSPAN=2><input name="selection" type="checkbox" value="<bean:write name="h" property="gi"/>"></td>
        <td><a href="ViewSequence.do?<%= Constants.FLEX_SEQUENCE_ID_KEY %>=<bean:write name="h" property="id"/>"><bean:write name="h" property="id"/></a><br></td>
        </logic:notEqual>
        <td><a target="_new" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="h" property="gi"/>&dopt=GenBank"><bean:write name="h" property="accession"/><br></a></td>
        <td><bean:write name="h" property="description"/></td>
        <td><bean:write name="h" property="gi"/></td>
        <td><bean:write name="h" property="species"/></td>
        <td><bean:write name="h" property="flexstatus"/></td>
        <td><bean:write name="h" property="quality"/></td>
    </flex:row>
    </logic:iterate>
</table>
</logic:iterate>
</logic:present>

<logic:present name="homologs">
<logic:iterate id="homo" name="homologs">
<p>
Evalue: <bean:write name="homo" property="value.blastResults.evalue"/><br>
Identity: <bean:write name="homo" property="value.blastResults.identity"/><br>
Query CDS length: <bean:write name="homo" property="value.blastResults.cdslength"/>      
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
[ <a target=_new href="ViewAlignment.do?gi=<bean:write name="homo" property="key"/>">View Alignment</a> ]
<table border=1>
    <tr class="headerRow">
        <td></td><th>FLEXGene ID</th><th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>
    </tr>
    <logic:iterate id="h" name="homo" property="value.homolog">
    <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
        <logic:equal name="h" property="speciesCategory" value="allowed">
        <td><input name="selection" type="checkbox" value="<bean:write name="h" property="gi"/>"></td>
        </logic:equal>
        <logic:notEqual name="h" property="speciesCategory" value="allowed">
        <td></td>
        </logic:notEqual>
        <logic:equal name="h" property="id" value="-1">
        <td><font color=red><bean:message key="flex.notapplicable" /><br></font></td>
        </logic:equal>
        <logic:notEqual name="h" property="id" value="-1">
        <td><a href="ViewSequence.do?<%= Constants.FLEX_SEQUENCE_ID_KEY %>=<bean:write name="h" property="id"/>"><bean:write name="h" property="id"/></a><br></td>
        </logic:notEqual>
        <td><a target="_new" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="h" property="gi"/>&dopt=GenBank"><bean:write name="h" property="accession"/><br></a></td>
        <td><bean:write name="h" property="description"/></td>
        <td><bean:write name="h" property="gi"/></td>
        <td><bean:write name="h" property="species"/></td>
        <td><bean:write name="h" property="flexstatus"/></td>
        <td><bean:write name="h" property="quality"/></td>
    </flex:row>
    </logic:iterate>
</table>
</logic:iterate>
</logic:present>

</logic:present>

<logic:present name="badSequences">
<p>
<h3>Category: Questionable quality sequences</h3>
<table border=1>
    <tr class="headerRow">
        <th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>
    </tr>
    <logic:iterate id="bs" name="badSequences">
    <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
        <td><a target="_new" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="bs" property="gi"/>&dopt=GenBank"><bean:write name="bs" property="accession"/></a></td>
        <td><bean:write name="bs" property="description"/></td>
        <td><bean:write name="bs" property="gi"/></td>
        <td><flex:write name="bs" property="species"/></td>
        <td><bean:write name="bs" property="flexstatus"/></td>
        <td><bean:write name="bs" property="quality"/></td>
    </flex:row>
    </logic:iterate>
</table>
</logic:present>

<p>
<center><input type=submit value="Submit"> <input type=reset value="Clear"></center>

</html:form>

</body>
</html:html>
