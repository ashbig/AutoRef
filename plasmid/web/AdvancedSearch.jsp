<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 

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
          <html:option value="<%=Constants.OPERATOR_CONTAINS %>"/>
          <html:option value="<%=Constants.OPERATOR_EQUALS %>"/>
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
          <html:option value="<%=Constants.OPERATOR_CONTAINS %>"/>
          <html:option value="<%=Constants.OPERATOR_EQUALS %>"/>
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
          <html:option value="<%=Constants.OPERATOR_CONTAINS %>"/>
          <html:option value="<%=Constants.OPERATOR_EQUALS %>"/>
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
          <html:option value="<%=Constants.OPERATOR_CONTAINS %>"/>
          <html:option value="<%=Constants.OPERATOR_EQUALS %>"/>
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
    <td width="15%" class="formlabel">PMID</td>
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
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

