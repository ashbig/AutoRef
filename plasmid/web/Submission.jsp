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
<jsp:include page="submissionTitle.jsp" />
<p>Sharing plasmids with the <a href="http://dnaseq.med.harvard.edu/">
DF/HCC DNA Resource Core</a> benefits you by alleviating the burden of storage, maintenance and distribution, and benefits the general research community by making plasmids available from a central source.</p>
<p>We encourage submission of plasmids from researchers at any of the <a href="http://www.dfhcc.harvard.edu/">
DF/HCC</a> parent institutions and from the research community at large. Small and large collections are equally welcome.</p>
<p>Submitting plasmids generally involves the following three steps. (1) Gaining permission from your institution to include the plasmids in the repository for distribution under specific terms (usually, the UB-MTA). NOTE that this permission has already been granted by all seven <a href="http://www.dfhcc.harvard.edu/">
  DF/HCC</a> institutions. (2) Providing us with information about the plasmids, such as maps or sequence files, growth conditions, relevant authors and publications, etc. so we are set up to handle the plasmids when they arrive. We can provide templates for submission of information. And (3) providing the plasmids as purified DNA in solution or spotted to a dry matrix, along with a map that matches plasmid information with sample locations.</p>
<p>What happens here? We generally curate the information and import it into PlasmID, transform the DNA into a phage-resistance bacterial host strain, and perform DNA sequencing and/or other analyses to verify the plasmids. Once those steps are complete, we will notify you that we are ready to distribute the plasmids and will make the plasmids searchable at PlasmID. In general, we store a working copy of the glycerol stock, along with one or two archival back-up copies, at -80&#176C and we typically also store the original DNA sample shared with the core facility dry on matrix.</p>
<p>To get things started, please contact <a href="mailto:plasmidhelp@hms.harvard.edu">PlasmID help</a>.</p>

<jsp:include page="footer.jsp" /></body>
</html>

