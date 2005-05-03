<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title>Clone Detail</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
</head>

<body>
<p>Clone: <bean:write name="clone" property="name"/></p>
<table width="700" border="0">
  <tr> 
    <td width="15%" class="tablebody">Clone ID:</td>
    <td width="27%" class="mainbodytext"><bean:write name="clone" property="name"/></td>
    <td width="18%" class="tablebody">Type:</td>
    <td width="40%" class="mainbodytext"><bean:write name="clone" property="type"/></td>
  </tr>
  <tr> 
    <td class="tablebody">Is Verified:</td>
    <td class="mainbodytext"><bean:write name="clone" property="verified"/></td>
    <td class="tablebody">Verification Method:</td>
    <td class="mainbodytext"><bean:write name="clone" property="vermethod"/></td>
  </tr>
  <tr> 
    <td class="tablebody">Domain:</td>
    <td class="mainbodytext"><bean:write name="clone" property="domain"/></td>
    <td class="tablebody">Subdomain:</td>
    <td class="mainbodytext"><bean:write name="clone" property="subdomain"/></td>
  </tr>
  <tr> 
    <td class="tablebody">Status:</td>
    <td class="mainbodytext"><bean:write name="clone" property="status"/></td>
    <td class="tablebody">Restriction:</td>
    <td class="mainbodytext"><bean:write name="clone" property="restriction"/></td>
  </tr>
  <tr> 
    <td class="tablebody">Comments:</td>
    <td colspan="3" class="mainbodytext"><bean:write name="clone" property="comments"/></td>
  </tr>
  <tr> 
    <td class="tablebody">Map:</td>
    <td colspan="3" class="mainbodytext"><bean:write name="clone" property="clonemap"/></td>
  </tr>
</table>
<logic:present name="clone" property="names">
<p>Related Identifiers:</p>
<table width="700" border="0">
<logic:iterate id="clonename" name="clone" property="names">
  <tr>
    <td width="201" class="tablebody"><bean:write name="clonename" property="type"/></td>
    <td width="566" class="tableinfo"><bean:write name="clonename" property="value"/></td>
  </tr>
</logic:iterate>
</table>
</logic:present>
<logic:present name="clone" property="inserts">
<p>Insert Information:</p>
<table width="700" border="0">
  <tr> 
    <td width="5%" class="tablebody">Insert</td>
    <td width="8%" class="tablebody">Size (bp)</td>
    <td width="10%" class="tablebody">Species</td>
    <td width="6%" class="tablebody">Format</td>
    <td width="13%" class="tablebody">Tissue Source</td>
    <td width="7%" class="tablebody">Gene ID</td>
    <td width="8%" class="tablebody">Gene Symbol</td>
    <td width="29%" class="tablebody">Gene Name</td>
    <td width="14%" class="tablebody">Target Genbank</td>
  </tr>
<logic:iterate id="insert" name="clone" property="inserts">
  <tr> 
    <td class="tableinfo"><bean:write name="insert" property="order"/></td>
    <td class="tableinfo"><bean:write name="insert" property="size"/></td>
    <td class="tableinfo"><bean:write name="insert" property="species"/></td>
    <td class="tableinfo"><bean:write name="insert" property="format"/></td>
    <td class="tableinfo"><bean:write name="insert" property="source"/></td>
    <td class="tableinfo"><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=Retrieve&dopt=Graphics&list_uids=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    <td class="tableinfo"><bean:write name="insert" property="name"/></td>
    <td class="tableinfo"><bean:write name="insert" property="description"/></td>
    <td class="tableinfo"><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="targetseqid"/>"><bean:write name="insert" property="targetgenbank"/></a></td>
  </tr>
</logic:iterate>
</table>
</logic:present>
<logic:present name="clone" property="hosts">
<p>Host Information:</p>
<table width="700" border="0">
  <tr>
    <td width="11%" class="tablebody">Host Strain</td>
    <td width="19%" class="tablebody">Is Used In Our Lab</td>
    <td width="70%" class="tablebody">Description</td>
  </tr>
<logic:iterate id="host" name="clone" property="hosts">
  <tr>
    <td class="tableinfo"><bean:write name="host" property="hoststrain"/></td>
    <td class="tableinfo"><bean:write name="host" property="isinuse"/></td>
    <td class="tableinfo"><bean:write name="host" property="description"/></td>
  </tr>
</logic:iterate>
</table>
</logic:present>
<logic:present name="clone" property="recommendedGrowthCondition">
<p>Recommended Growth Condition:</p>
<table width="700" border="0">
  <tr>
    <td width="15%" class="tablebody">Name</td>
    <td width="12%" class="tablebody">Host Type</td>
    <td width="20%" class="tablebody">Antibiotic Selection</td>
    <td width="25%" class="tablebody">Growth Condition</td>
    <td width="28%" class="tablebody">Comments</td>
  </tr>
  <tr>
    <td class="tableinfo"><bean:write name="clone" property="recommendedGrowthCondition.name"/></td>
    <td class="tableinfo"><bean:write name="clone" property="recommendedGrowthCondition.hosttype"/></td>
    <td class="tableinfo"><bean:write name="clone" property="recommendedGrowthCondition.selection"/></td>
    <td class="tableinfo"><bean:write name="clone" property="recommendedGrowthCondition.condition"/></td>
    <td class="tableinfo"><bean:write name="clone" property="recommendedGrowthCondition.comments"/></td>
  </tr>
</table>
</logic:present>
<logic:present name="clone" property="selections">
<p>Antibiotic Selections:</p>
<table width="700" border="0">
  <tr>
    <td width="24%" class="tablebody">Host Type</td>
    <td width="76%" class="tablebody">Marker</td>
  </tr>
<logic:iterate id="selection" name="clone" property="selections">
  <tr>
    <td class="tableinfo"><bean:write name="selection" property="hosttype"/></td>
    <td class="tableinfo"><bean:write name="selection" property="marker"/></td>
  </tr>
</logic:iterate>
</table>
</logic:present>
<logic:present name="clone" property="authors">
<p>Authors:</p>
<table width="700" border="0">
  <tr>
    <td class="tablebody">Author Name</td>
    <td class="tablebody">Author Type</td>
  </tr>
<logic:iterate id="author" name="clone" property="authors">
  <tr>
    <td class="tableinfo"><bean:write name="author" property="authorname"/></td>
    <td class="tableinfo"><bean:write name="author" property="authortype"/></td>
  </tr>
</logic:iterate>
</table>
</logic:present>
<logic:present name="clone" property="publications">
<p>Publications:</p>
<table width="700" border="0">
  <tr>
    <td width="11%" class="tablebody">PMID</td>
    <td width="89%" class="tablebody">Title</td>
  </tr>
<logic:iterate id="publication" name="clone" property="publications">
  <tr>
    <td class="tableinfo"><bean:write name="publication" property="pmid"/></td>
    <td class="tableinfo"><bean:write name="publication" property="title"/></td>
  </tr>
</logic:iterate>
</table>
</logic:present>
</body>
</html>
