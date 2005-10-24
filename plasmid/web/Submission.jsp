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
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
<TBODY>
    <TR> 
        <TD width="100%">
<p class="homeMainText">Plasmid Submission </p>
<p class="itemtext"><u>What to expect when you submit clones</u></P> 
<ul>
  <li class="underbullet"> We&#8217;ll work with you to try to make transfer of 
    clones and supporting information as easy as possible by providing support, 
    supplies, and pick-up</li>
  <li class="underbullet">We prefer to receive plasmids as DNA rather than in 
    bacteria but if this presents an undo burden on your lab, we can receive plasmids 
    in bacteria</li>
  <li class="underbullet"> Information can be submitted in any of several formats 
    supported by the core, including but not limited to PDF, Excel, FileMaker, 
    Clone Manager, Vector NTI, and GenBank format files</li>
  <li class="underbullet">The minimum information needed for plasmid submission 
    is a plasmid map or sequence, information about selection and growth conditions, 
    and the name of the authors and/or references that should be cited by those 
    who order the clone. If you can give us more information &#8230; so much the 
    better</li>
  <li class="underbullet">In some cases, approval from your institution&#8217;s 
    technology transfer office will be required before the clones can be entered 
    into our repository</li>
</ul>
<p class="itemtext"><u>What will happen to the clones and supporting information 
  after they are gathered</u></p>
        
<ul>
  <li class="underbullet">Clones will be transformed into an appropriate phage-resistant 
    host strain</li>
  <li class="underbullet">Two or more glycerol stocks will be created from each 
    clone (archival stock and one or more stocks for distribution) and stored 
    in separate locations</li>
  <li class="underbullet">Information will be curated and reformatted for entry 
    into the database, and a PDF file of vector maps and/or clone maps will be 
    made available along with any sequence text files provided by the donor</li>
  <li class="underbullet">Clones will be distributed to academic researchers who 
    request the plasmid after a signed copy of the UB-MTA has been returned to 
    the core facility (further restrictions on distribution or special MTAs can 
    be added)</li>
</ul>
        
<span class="featuretext">If you would like to submit clones to the repository, 
please contact Stephanie Mohr <a href="mailto:stephanie_mohr@hms.harvard.edu"><span class="featuretext">by 
email</span></a> or by phone 617-324-4251.</span>
</p>
<ul>
  <li class="underbullet"><a target="_blank" href="MQF.pdf">Material Qualification Form</a></li>
  <li class="underbullet"><a target="_blank" href="PIT.pdf">Plasmid Intake Template</a></li>
</ul>
</p>
        </TD>
    </TR>
  </TBODY>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

