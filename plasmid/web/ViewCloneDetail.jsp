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
    <logic:present name="clonename" property="url">
    <a target="_blank" href="<bean:write name="clonename" property="url"/>">
    </logic:present>
    <td width="566" class="tableinfo"><bean:write name="clonename" property="value"/></td>
    <logic:present name="clonename" property="url">
    </a>
    </logic:present>
  </tr>
</logic:iterate>
</table>
</logic:present>

<logic:present name="clone" property="properties">
<p>Property:</p>
<table width="700" border="0">
<logic:iterate id="p" name="clone" property="properties">
  <tr>
    <td class="tablebody"><bean:write name="p" property="type"/></td>
    <td class="tableinfo"><bean:write name="p" property="value"/></td>
    <td class="tableinfo"><bean:write name="p" property="extrainfo"/></td>
  </tr>
</logic:iterate>
</table>
</logic:present>

<logic:present name="clone" property="inserts">
<p>Insert Information:</p>
<table width="700" border="0">
  <tr> 
    <td width="5%" class="tablebody">Insert</td>
    <td width="6%" class="tablebody">Size (bp)</td>
    <td width="8%" class="tablebody">Species</td>
    <td width="6%" class="tablebody">Mutation</td>
    <td width="6%" class="tablebody">Discrepancy</td>
    <td width="6%" class="tablebody">Format</td>
    <td width="10%" class="tablebody">Tissue Source</td>
    <td width="7%" class="tablebody">Species Specific ID</td>
    <td width="8%" class="tablebody">Gene Symbol</td>
    <td width="26%" class="tablebody">Gene Name</td>
    <td width="12%" class="tablebody">Target Genbank</td>
  </tr>
<logic:iterate id="insert" name="clone" property="inserts">
  <tr> 
    <td class="tableinfo"><bean:write name="insert" property="order"/></td>
    <td class="tableinfo"><bean:write name="insert" property="size"/></td>
    <td class="tableinfo"><bean:write name="insert" property="species"/></td>
    <td class="tableinfo"><bean:write name="insert" property="hasmutation"/></td>
    <td class="tableinfo"><bean:write name="insert" property="hasdiscrepancy"/></td>
    <td class="tableinfo"><bean:write name="insert" property="format"/></td>
    <td class="tableinfo"><bean:write name="insert" property="source"/></td>
    <logic:equal name="insert" property="species" value="Homo sapiens">
    <td class="tableinfo"><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=Retrieve&dopt=Graphics&list_uids=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="species" value="Pseudomonas aeruginosa">
    <td class="tableinfo"><a target="_blank" href="http://www.pseudomonas.com/AnnotationByPAU.asp?PA=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="species" value="Saccharomyces cerevisiae">
    <td class="tableinfo"><a target="_blank" href="http://db.yeastgenome.org/cgi-bin/locus.pl?locus=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <td class="tableinfo"><bean:write name="insert" property="name"/></td>
    <td class="tableinfo"><bean:write name="insert" property="description"/></td>
    <td class="tableinfo"><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="targetseqid"/>"><bean:write name="insert" property="targetgenbank"/></a></td>
  </tr>
</logic:iterate>
</table>

<logic:iterate id="insert" name="clone" property="inserts">
<logic:present name="insert" property="properties">
<p>Insert Property: Insert <bean:write name="insert" property="order"/></p>
<table width="700" border="0">
  <tr> 
    <td class="tablebody">Type</td>
    <td class="tablebody">Value</td>
    <td class="tablebody">Extra Information</td>
  </tr>
<logic:iterate id="p" name="insert" property="properties">
  <tr> 
    <td class="tableinfo"><bean:write name="p" property="type"/></td>
    <td class="tableinfo"><bean:write name="p" property="value"/></td>
    <td class="tableinfo"><bean:write name="p" property="extrainfo"/></td>
  </tr>
</logic:iterate>
</table>
</logic:present>
</logic:iterate>
</logic:present>

<logic:present name="clone" property="vector">
<p>Vector Information:</p>
<table width="700" border="0">
  <tr> 
    <td width="15%" class="tablebody">Vector Name:</td>
    <td width="27%" class="mainbodytext"><a href="GetVectorDetail.do?vectorid=<bean:write name="clone" property="vector.vectorid"/>"><bean:write name="clone" property="vector.name"/></a></td>
    <td width="18%" class="tablebody">Size (bp):</td>
    <td width="40%" class="mainbodytext"><bean:write name="clone" property="vector.size"/></td>
  </tr>
  <tr> 
    <td class="tablebody">Type:</td>
    <td class="mainbodytext"><bean:write name="clone" property="vector.type"/></td>
    <td class="tablebody">Form:</td>
    <td class="mainbodytext"><bean:write name="clone" property="vector.form"/></td>
  </tr>
  <tr> 
    <td class="tablebody">Description:</td>
    <td colspan="3" class="mainbodytext"><bean:write name="clone" property="vector.description"/></td>
  </tr>
  <tr> 
    <td class="tablebody">Properties:</td>
    <td colspan="3" class="mainbodytext"><bean:write name="clone" property="vector.propertyString"/></td>
  </tr>
  <tr> 
    <td class="tablebody">Comments:</td>
    <td colspan="3" class="mainbodytext"><bean:write name="clone" property="vector.comments"/></td>
  </tr>
  <tr> 
    <td class="tablebody">Map:</td>
    <td colspan="3" class="mainbodytext"><bean:write name="clone" property="vector.mapfilename"/></td>
  </tr>
</table>
</logic:present>

<logic:present name="clone" property="hosts">
<p>Host Information:</p>
<table width="700" border="0">
  <tr>
    <td width="31%" class="tablebody">Host Strain</td>
    <td width="19%" class="tablebody">Is Used In Distribution</td>
    <td width="50%" class="tablebody">Description</td>
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

<logic:present name="clone" property="recommendedGrowthCondition">
<p>Recommended Growth Condition:</p>
<table width="700" border="0">
  <tr>
    <td width="12%" class="tablebody">Host Type</td>
    <td width="20%" class="tablebody">Selection Condition</td>
    <td width="25%" class="tablebody">Growth Condition</td>
    <td width="28%" class="tablebody">Comments</td>
  </tr>
  <tr>
    <td class="tableinfo"><bean:write name="clone" property="recommendedGrowthCondition.hosttype"/></td>
    <td class="tableinfo"><bean:write name="clone" property="recommendedGrowthCondition.selection"/></td>
    <td class="tableinfo"><bean:write name="clone" property="recommendedGrowthCondition.condition"/></td>
    <td class="tableinfo"><bean:write name="clone" property="recommendedGrowthCondition.comments"/></td>
  </tr>
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
