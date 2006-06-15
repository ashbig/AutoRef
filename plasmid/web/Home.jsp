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
<jsp:include page="homeTitle.jsp" />
<table width="100%" border="0" cellspacing="3" cellpadding="3">
  <tr> 
    <td width="50%" height="275" align="left" valign="top">
	     <!-- ImageReady Slices (monday_3.ai) -->
      <IMG SRC="monday_4.gif" WIDTH=376 HEIGHT=275 BORDER=0 ALT="" USEMAP="#monday_4_Map"> 
      <MAP NAME="monday_4_Map">
        <AREA SHAPE="rect" ALT="" COORDS="99,91,277,119" HREF="http://128.103.32.228/PLASMID/Submission.jsp">
        <AREA SHAPE="rect" ALT="" COORDS="72,180,304,208" HREF="http://128.103.32.228/PLASMID/collection_overview.jsp" target="_blank">
        <AREA SHAPE="rect" ALT="" COORDS="86,136,290,163" HREF="http://128.103.32.228/PLASMID/OrderOverview.jsp">
      </MAP> 
      <!-- End ImageReady Slices -->  
      </td>
    <td width="50%" height="275"> 
 	 <!-- Begin "New in the Repository" Blockquote -->
	 <blockquote> 
        <p align="left" class="homepageText3">New in the Repository</p>
        <ul>
          <li> 
            <div align="left">New search tools and defined collections make it easier for you to find clones of interest
            </div>
          </li>
          <li> 
            <div align="left">Human kinase and Breast Cancer-related ORF collections (request single clones or whole collection)
</div>
          </li>
          <li> 
            <div align="left">Sequence-verified ORF collections for microbes now include P. aeruginosa, Y. pestis, F. tularensis, and V. cholerae
</div>
          </li>
        </ul>
      </blockquote>
	   <!-- End "New in the Repository" Blockquote -->
    </td>
  </tr>
  <tr>
    <td width="50%" align="left" valign="top"><span class="alert">Please register 
      and sign in.<br>
      Sign-in status affects what you can view and request.<br><br></span>
    <span class="text">Click <a href="cloning_strategies.htm" target="_blank">here</a> to view the cloning methods used for the plasmids in the repository
    </span></td>
    <td height="75"><html:form action="Logon.do"><table bgcolor="#CCCCCC" width="350" border="0">
  <tr> 
    <td width="12%" valign="baseline" class="formlabel">Email:</td>
    <td width="28%">
        <html:text property="email" styleClass="text" size="30"/>
    </td>    
    <td valign="baseline" class="text"><html:submit styleClass="text" value="Login"/></td>
  </tr>
  <tr> 
    <td width="12%" valign="baseline" class="formlabel">Password:</td>
    <td width="28%">
        <html:password property="password" styleClass="text" size="30" maxlength="20"/>        
    </td>
    <td valign="baseline" class="text"><a href="FindPassword.jsp">Find Password</a></td>
  </tr>
</table>
      </html:form></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="1" cellpadding="4">
          <tr align="left" valign="top" bgcolor="#6699cc" class="homepageText3"> 
            <td width="12%" class="homeMainText"> DF/HCC Member Institutions:</td>
            <td width="12%" class="homeMainText"><a href="http://www.bidmc.harvard.edu/sites/bidmc/home.asp" title="Beth Israel Deaconess Medical Center" target="_blank">Beth 
              Israel Deaconess Medical Center</a></td>
            <td width="12%" class="homeMainText"><a href="http://www.brighamandwomens.org/" title="Brigham & Women's Hospital" target="_blank">Brigham 
              & Women's Hospital</a></td>
            <td width="12%" class="homeMainText"><a href="http://www.childrenshospital.org/" title="Children's Hospital Boston" target="_blank">Children's 
              Hospital Boston</a></td>
            <td width="12%" class="homeMainText"><a href="http://www.dfci.harvard.edu/" title="Dana Farber Cancer Institute" target="_blank">Dana 
              Farber Cancer Institute</a></td>
            <td width="12%" class="homeMainText"><a href="http://hms.harvard.edu/hms/home.asp" title="Harvard Medical School" target="_blank">Harvard 
              Medical School</a></td>
            <td width="12%" class="homeMainText"><a href="http://www.hsph.harvard.edu/" title="Harvard School of Public Health" target="_blank">Harvard 
              School of Public Health</a></td>
            <td width="12%"><a href="http://www.mgh.harvard.edu/" title="Massachusetts General Hospital" target="_blank" class="homeMainText">Massachusetts 
              General Hospital</a></td>
          </tr>
        </table>
      </TD>
    </TR>
    <TR> 
      <TD width="100%"></TD>
    </TR>
    <TR>
      <TD width="100%" align="left" valign="top"> 
        <div align="center" class="homeMainText">PlasmID was created and is maintained 
          by the <a href="http://dnaseq.med.harvard.edu" target="_blank">DF/HCC 
          DNA Resource Core</a></div></TD>
    </TR>
  </TBODY>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

