<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.coreobject.RefseqNameType" %> 
<%@ page import="plasmid.coreobject.Clone" %> 
<%@ page import="plasmid.coreobject.Sample" %> 
<%@ page import="plasmid.Constants" %> 

<html>
<head>
<title>PlasmID Database</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
 <link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
        <link href="boilerplate.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="respond.min.js"></script>
</head>
     <div class="gridContainer clearfix">


<body>
<jsp:include page="homeTitle.jsp" />
<table width="100%" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menuHome.jsp" />
	</td>--%>
    <td width="83%" align="left" valign="top">
	<%--<jsp:include page="viewContainersTitle.jsp" />--%>

<p class="text">Growth Conditions:
<table>
  <tr>
    <td class="tableheader">Name</td>
    <td class="tableheader">Host</td>
    <td class="tableheader">Antibiotic Selection</td>
    <td class="tableheader">Growth Condition</td>
    <td class="tableheader">Comments</td>
  </tr>  
  <logic:iterate name="growths" id="growth">
  <tr class="tableinfo"> 
    <td><bean:write name="growth" property="name"/>
    <td><bean:write name="growth" property="hosttype"/>
    <td><bean:write name="growth" property="selection"/>
    <td><bean:write name="growth" property="condition"/>
    <td><bean:write name="growth" property="comments"/>
  </tr> 
  </logic:iterate>
</table>

<p align="right">
<html:form action="ViewContainers.do">
<html:hidden property="labelString"/>
<html:submit property="button" value="<%=Constants.BUTTON_DOWNLOAD_CONTAINERS%>"/>
</html:form>
</p>

<logic:iterate name="containers" id="container">
<p>
<table>
  <tr>
    <td class="tablebody">Container Label:</td>
    <td class="mainbodytext"><bean:write name="container" property="label"/></td>
  </tr>  
  <tr>
    <td class="tablebody">Container Type:</td>
    <td class="mainbodytext"><bean:write name="container" property="type"/></td>
  </tr> 
</table>
<table width="100%" border="0">
  <tr>
    <td class="tableheader">Well</td>
    <td class="tableheader">Type</td>
    <td class="tableheader">Clone ID</td>
    <td class="tableheader">Clone Type</td>
    <td class="tableheader">Species Specific ID</td>
    <td class="tableheader">Gene Symbol</td>
    <td class="tableheader">Gene Name</td>
    <td class="tableheader">Reference Sequence</td>
    <td class="tableheader">Mutation</td>
    <td class="tableheader">Discrepancy</td>
    <td class="tableheader">Insert Format</td>
    <td class="tableheader">Vector</td>
    <td class="tableheader">Selection Markers</td>
    <td class="tableheader">Growth Condition</td>
  </tr>

  <logic:iterate name="container" property="samples" id="sample">
  <tr class="tableinfo"> 
    <td><bean:write name="sample" property="well"/></td>
    <td><bean:write name="sample" property="type"/></td>
    <logic:equal name="sample" property="type" value="<%=Sample.EMPTY%>">
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    </logic:equal>
    <logic:notEqual name="sample" property="type" value="<%=Sample.EMPTY%>">
    <td><a target="_blank" href="GetCloneDetail.do?cloneid=<bean:write name="sample" property="clone.cloneid"/>"><bean:write name="sample" property="clone.name"/></a></td>
    <td><bean:write name="sample" property="clone.type"/></td>
    <logic:equal name="sample" property="clone.type" value="<%=Clone.NOINSERT%>">
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    </logic:equal>

    <logic:notEqual name="sample" property="clone.type" value="<%=Clone.NOINSERT%>">
    <logic:iterate name="sample" property="clone.inserts" id="insert">
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.GENEID%>">
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=Retrieve&dopt=Graphics&list_uids=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.PA%>">
    <td><a target="_blank" href="http://www.pseudomonas.com/AnnotationByPAU.asp?PA=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.SGD%>">
    <td><a target="_blank" href="http://db.yeastgenome.org/cgi-bin/locus.pl?locus=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.GENBANK%>">
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.VCNUMBER%>">
    <td><a target="_blank" href="http://www.tigr.org/tigr-scripts/CMR2/GenePage.spl?locus=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.FTNUMBER%>">
    <td><bean:write name="insert" property="geneid"/></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.FBID%>">
    <td><a target="_blank" href="http://www.flybase.org/.bin/fbidq.html?<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.WBGENEID%>">
    <td><a target="_blank" href="http://www.wormbase.org/db/gene/gene?name=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
    </logic:equal>
    <td><bean:write name="insert" property="name"/></td>
    <td><bean:write name="insert" property="description"/></td>
    <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="targetseqidForNCBI"/>"><bean:write name="insert" property="targetgenbank"/></a></td>
    <td><bean:write name="insert" property="hasmutation"/></td>
    <td><bean:write name="insert" property="hasdiscrepancy"/></td>
    <td><bean:write name="insert" property="format"/></td>
    </logic:iterate>
    </logic:notEqual>
    <td><a target="_blank" href="GetVectorDetail.do?vectorid=<bean:write name="sample" property="clone.vectorid"/>"><bean:write name="sample" property="clone.vectorname"/></a></td>
    <td>
    <logic:iterate name="sample" property="clone.selections" id="selection">
        <bean:write name="selection" property="hosttype"/>: <bean:write name="selection" property="marker"/>
    </logic:iterate>
    </td>
    <td><bean:write name="sample" property="clone.recommendedGrowthCondition.name"/></td>
    </logic:notEqual>
    </tr>
  </logic:iterate>

</table>
</logic:iterate>

<p align="right">
<html:form action="ViewContainers.do">
<html:hidden property="labelString"/>
<html:submit property="button" value="<%=Constants.BUTTON_DOWNLOAD_CONTAINERS%>"/>
</html:form>
</p>

      </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
     </div>
</html>

