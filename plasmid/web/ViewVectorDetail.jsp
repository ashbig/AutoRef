<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html>
<head>
<title>Vector Detail</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
</head>

<body>
<p class="text">Vector: <bean:write name="vector" property="name"/></p>
<table width="700" border="0">
  <tr> 
    <td width="15%" class="mainbodytexthead"> Name:</td>
    <td class="mainbodytext"><bean:write name="vector" property="name"/></td>
  </tr>
  <tr> 
    <td class="mainbodytexthead">Description:</td>
    <td class="mainbodytext"><bean:write name="vector" property="description"/></td>
  </tr>
  <tr> 
    <td class="mainbodytexthead">Synonyms:</td>
    <td class="mainbodytext"><bean:write name="vector" property="synonymString"/></td>
  </tr>
  <tr> 
    <td class="mainbodytexthead">Type:</td>
    <td class="mainbodytext"><bean:write name="vector" property="type"/></td>
  </tr>
  <tr> 
    <td class="mainbodytexthead">Form:</td>
    <td class="mainbodytext"><bean:write name="vector" property="form"/></td>
  </tr>
  <tr> 
    <td class="mainbodytexthead">Size (bp)::</td>
    <td class="mainbodytext"><bean:write name="vector" property="size"/></td>
  </tr>
  <tr> 
    <td class="mainbodytexthead">Properties:</td>
    <td class="mainbodytext"><bean:write name="vector" property="propertyString"/></td>
  </tr>
  <tr> 
    <td class="mainbodytexthead">Vector Map:</td>
    <td class="mainbodytext">
        <a target="blank" href="../PlasmidRepository/file/map/<bean:write name="vector" property="mapfilename"/>"><bean:write name="vector" property="mapfilename"/></a>
    </td>
  </tr>
  <tr> 
    <td class="mainbodytexthead">Vector Sequence:</td>
    <td class="mainbodytext"><a target="blank" href="../PlasmidRepository/file/sequence/<bean:write name="vector" property="seqfilename"/>"><bean:write name="vector" property="seqfilename"/></a></td>
  </tr>
  <tr> 
    <td class="mainbodytexthead">Comments:</td>
    <td class="mainbodytext"><bean:write name="vector" property="comments"/></td>
  </tr>
</table>

<logic:present name="vector" property="vectorfeatures">
    <logic:equal name="vector" property="featureEmpty" value="false">
    <html:form action="ViewVectormap.do" target="blank">
        <p class="text">Features:
            <input type="hidden" name="vectorname" value="<bean:write name="vectorfilename"/>"/>
            <input name="button" type="submit" class="itemtext" value="Map the features"/>
        </p>
    </html:form>
    </logic:equal>
<table width="700" border="0">
  <tr>
    <td width="110" class="tablebody">Type</td>
    <td width="147" class="tablebody">Name</td>
    <td width="260" class="tablebody">Description</td>
    <td width="84" class="tablebody">Start Position</td>
    <td width="77" class="tablebody">End Position</td>
  </tr>
  <logic:iterate name="vector" property="vectorfeatures" id="feature">
  <tr>
    <td class="tableinfo"><bean:write name="feature" property="maptype"/></td>
    <td class="tableinfo"><bean:write name="feature" property="name"/></td>
    <td class="tableinfo"><bean:write name="feature" property="description"/></td>
    <td class="tableinfo"><bean:write name="feature" property="start"/></td>
    <td class="tableinfo"><bean:write name="feature" property="stop"/></td>
  </tr>
  </logic:iterate>
</table>
</logic:present>

<logic:present name="vector" property="authors">
<p class="text">Authors:</p>
<table width="700" border="0">
  <tr> 
    <td width="137" class="tablebody">Author Name</td>
    <td width="194" class="tablebody">Author Type</td>
    <td width="355" class="tablebody">Creation Date</td>
  </tr>
  <logic:iterate name="vector" property="authors" id="author">
  <tr> 
    <td class="tableinfo"><bean:write name="author" property="name"/></td>
    <td class="tableinfo"><bean:write name="author" property="type"/></td>
    <td class="tableinfo"><bean:write name="author" property="date"/></td>
  </tr>
  </logic:iterate>
</table>
</logic:present>

<logic:present name="vector" property="publications">
<p class="text">Publications:</p>
<table width="700" border="0">
  <tr>
    <td width="11%" class="tablebody">PMID</td>
    <td width="89%" class="tablebody">Title</td>
  </tr>
  <logic:iterate name="vector" property="publications" id="publication">
  <tr>
    <td class="tableinfo"><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Search&db=pubmed&term=<bean:write name="publication" property="pmid"/>"><bean:write name="publication" property="pmid"/></a></td>
    <td class="tableinfo"><bean:write name="publication" property="title"/></td>
  </tr>
  </logic:iterate>
</table>
</logic:present>

<logic:present name="vector" property="vectorparents">
<p class="text">Parent Vector:</p>
<table width="700" border="0">
  <tr> 
    <td width="28%" class="tablebody">Name</td>
    <td width="72%" class="tablebody">Comments</td>
  </tr>
  <logic:iterate name="vector" property="vectorparents" id="parent">
  <tr> 
    <td class="tableinfo">
        <logic:notEqual name="parent" property="parentvectorid" value="0">
        <a href="GetVectorDetail.do?vectorid=<bean:write name="parent" property="parentvectorid"/>">
        </logic:notEqual>
        <bean:write name="parent" property="parentvectorname"/>
        <logic:notEqual name="parent" property="parentvectorid" value="0">
        </a>
        </logic:notEqual>
    </td>
    <td class="tableinfo"><bean:write name="parent" property="comments"/></td>
  </tr>
  </logic:iterate>
</table>
</logic:present>
</body>
</html>
