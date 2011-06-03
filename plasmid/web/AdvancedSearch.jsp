<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.CloneProperty" %> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title>PlasmID Database</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
</head>

<body>
<jsp:include page="orderTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menu.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
	<jsp:include page="advancedSearchTitle.jsp" />
      <html:form action="AdvancedSearch.do">
          
<table width="100%" border="0">
  <tr> 
    <td width="15%" class="formlabel">Gene name</td>
    <td width="15%">
        <html:select property="geneNameOp" styleClass="itemtext">
          <html:option value="<%=Constants.OPERATOR_EQUALS %>"/>
          <html:option value="<%=Constants.OPERATOR_CONTAINS %>"/>
        </html:select>
    </td>
    <td width="36%">
        <html:text styleClass="itemtext" property="geneName" size="50"/>
    </td>
    <!--
    <td class="formlabel" width="18%">
          <html:radio property="geneNameAndOr" value="and"/>and
          <html:radio property="geneNameAndOr" value="or"/>or
    </td>
    -->
  </tr>
  <tr> 
    <td width="15%" class="formlabel">Vector name</td>
    <td width="15%">
        <html:select property="vectorNameOp" styleClass="itemtext">
          <html:option value="<%=Constants.OPERATOR_EQUALS %>"/>
          <html:option value="<%=Constants.OPERATOR_CONTAINS %>"/>
        </html:select>
    </td>
    <td width="36%">
        <html:text styleClass="itemtext" property="vectorName" size="50"/>
    </td>
    <!--
    <td width="18%">
          <html:radio property="vectorNameAndOr" value="and"/>
          <html:radio property="vectorNameAndOr" value="or"/>
    </td>
    -->
  </tr>
  <tr> 
    <td width="15%" class="formlabel">Vector feature</td>
    <td width="15%">
        <html:select property="vectorFeatureOp" styleClass="itemtext">
          <html:option value="<%=Constants.OPERATOR_EQUALS %>"/>
          <html:option value="<%=Constants.OPERATOR_CONTAINS %>"/>
        </html:select>
    </td>
    <td width="36%">
        <html:text styleClass="itemtext" property="vectorFeature" size="50"/>
    </td>
    <!--
    <td width="18%">
          <html:radio property="vectorFeatureAndOr" value="and"/>
          <html:radio property="vectorFeatureAndOr" value="or"/>
    </td>
    -->
  </tr>
  <tr> 
    <td width="15%" class="formlabel">Author name</td>
    <td width="15%">
        <html:select property="authorNameOp" styleClass="itemtext">
          <html:option value="<%=Constants.OPERATOR_EQUALS %>"/>
          <html:option value="<%=Constants.OPERATOR_CONTAINS %>"/>
        </html:select>
    </td>
    <td width="36%">
        <html:text styleClass="itemtext" property="authorName" size="50"/>
    </td>
    <!--
    <td width="18%">
          <html:radio property="authorNameAndOr" value="and"/>
          <html:radio property="authorNameAndOr" value="or"/>
    </td>
    -->
  </tr>
  <tr> 
    <td width="15%" class="formlabel">PubMed ID</td>
    <td width="15%">
        <html:select property="pmidOp" styleClass="itemtext">
          <html:option value="<%=Constants.OPERATOR_EQUALS %>"/>
        </html:select>
    </td>
    <td width="36%">
        <html:text styleClass="itemtext" property="pmid" size="50"/>
    </td>
    <!--
    <td width="18%">
          <html:radio property="pmidAndOr" value="and"/>
          <html:radio property="pmidAndOr" value="or"/>
    </td>
    -->
  </tr>
  <tr> 
    <td width="15%" class="formlabel">Species</td>
    <td colspan="2">
        <html:select property="species" styleClass="itemtext">
          <html:options name="species"/>
        </html:select>
    </td>
    <!--
    <td width="18%">
          <html:radio property="pmidAndOr" value="and"/>
          <html:radio property="pmidAndOr" value="or"/>
    </td>
    -->
  </tr>
  
  <logic:equal name="psi" value="1">
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3" class="tableheader">For PSI only</td>
  </tr>
  <tr> 
    <td width="15%" class="formlabel">PDB ID</td>
    <td width="15%">
        <html:select property="pdbidOp" styleClass="itemtext">
          <html:option value="<%=Constants.OPERATOR_EQUALS %>"/>
        </html:select>
    </td>
    <td width="36%">
        <html:text styleClass="itemtext" property="pdbid" size="50"/>
    </td>
    <td class="itemtext">
        (use * to include all)
    </td>
    <!--
    <td width="18%">
          <html:radio property="pmidAndOr" value="and"/>
          <html:radio property="pmidAndOr" value="or"/>
    </td>
    -->
  </tr>
  <tr> 
    <td width="15%" class="formlabel">TargetDB ID</td>
    <td width="15%">
        <html:select property="targetidOp" styleClass="itemtext">
          <html:option value="<%=Constants.OPERATOR_EQUALS %>"/>
        </html:select>
    </td>
    <td width="36%">
        <html:text styleClass="itemtext" property="targetid" size="50"/>
    </td>
    <!--
    <td width="18%">
          <html:radio property="pmidAndOr" value="and"/>
          <html:radio property="pmidAndOr" value="or"/>
    </td>
    -->
  </tr>
  <tr> 
    <td colspan="2" class="formlabel">Protein Express Result</td>
    <td width="36%">
        <html:select property="proteinExpress" styleClass="itemtext">
            <html:option value="">-- Select Result --</html:option>
          <html:option value="<%=CloneProperty.NOT_TESTED %>"/>
          <html:option value="<%=CloneProperty.NOT_APP %>"/>
          <html:option value="<%=CloneProperty.NOT_FOUND %>"/>
          <html:option value="<%=CloneProperty.PROTEIN_CONFIRMED %>"/>
        </html:select>
    </td>
    <!--
    <td width="18%">
          <html:radio property="pmidAndOr" value="and"/>
          <html:radio property="pmidAndOr" value="or"/>
    </td>
    -->
  </tr>
  <tr>
    <td colspan="2" class="formlabel">Protein Soluble Result</td>
    <td width="36%">
        <html:select property="proteinSoluble" styleClass="itemtext">
            <html:option value="">-- Select Result --</html:option>
          <html:option value="<%=CloneProperty.NOT_TESTED %>"/>
          <html:option value="<%=CloneProperty.NOT_APP %>"/>
          <html:option value="<%=CloneProperty.NOT_SOLUBLE %>"/>
          <html:option value="<%=CloneProperty.SOLUBLE %>"/>
        </html:select>
    </td>
    <!--
    <td width="18%">
          <html:radio property="pmidAndOr" value="and"/>
          <html:radio property="pmidAndOr" value="or"/>
    </td>
    -->
  </tr>
  <tr>
    <td colspan="2" class="formlabel">Protein Purification Result</td>
    <td width="36%">
        <html:select property="proteinPurified" styleClass="itemtext">
            <html:option value="">-- Select Result --</html:option>
          <html:option value="<%=CloneProperty.NOT_TESTED %>"/>
          <html:option value="<%=CloneProperty.NOT_APP %>"/>
          <html:option value="<%=CloneProperty.NOT_PURIFIED %>"/>
          <html:option value="<%=CloneProperty.PURIFIED %>"/>
        </html:select>
    </td>
    <!--
    <td width="18%">
          <html:radio property="pmidAndOr" value="and"/>
          <html:radio property="pmidAndOr" value="or"/>
    </td>
    -->
  </tr>
  <tr>
    <td colspan="2" class="formlabel">Plasmids provided by</td>
    <td width="36%">
        <html:select property="psicenter" styleClass="itemtext">
          <html:options name="psicenters"/>
        </html:select>
    </td>
    <!--
    <td width="18%">
          <html:radio property="pmidAndOr" value="and"/>
          <html:radio property="pmidAndOr" value="or"/>
    </td>
    -->
  </tr>
  </logic:equal>
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td><html:submit styleClass="formlabel" value="Search"/></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>

<input type="hidden" name="psi" value="<bean:write name="psi"/>"/>
      </html:form></td>
  </tr>
</table> 
<jsp:include page="footer.jsp" /></body>
</html>

