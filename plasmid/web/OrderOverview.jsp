<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

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
	<jsp:include page="orderoverviewTitle.jsp" />

<p class="text">Use the menu bar on the left to choose a search type or view a list of clones in a special collection. 
<P class="itemtext"><u>Search for plasmid clones</u></P>
        <ul>
          <li class="underbullet">Choose a search type from the menu bar on the 
            left</li>
          <li class="underbullet">&quot;Search by reference sequence&quot; queries 
            insert information only (use this search to find cDNAs, genomic fragments, 
            shRNAs, etc. inserted into various vectors using a gene symbol, GenBank 
            number, or an organism-specific ID)</li>
          <li class="underbullet">&quot;Search by vector&quot; retrieves all clones 
            in vectors with selected features</li>
        </ul>
<p class="itemtext"><u>View clone lists</u></p>
        <ul>
          <li class="underbullet">Certain donor labs (for example, the Harvard 
            Institute of Proteomics) have special collections listed here</li>
          <li class="underbullet">Click on a specific set to view all the clones 
            in that set</li>
          <li class="underbullet">Clone orders can be done for individual clones 
            in the set or for the entire set (special pricing available for some 
            sets)</li>
        </ul>
        <span class="mainbodytextlarge">Please give credit where credit is due!</span><span class="red"> 
        </span><span class="featuretext">When you have searched for and ordered 
        your clones, be sure to make note of the appropriate reference(s) associated 
        with the clone and the author(s) who donated the clone. The people who 
        constructed, published, and donated the clones should be cited whenever 
        you publish work that uses the clones that you ordered.</span></p>
    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

