<%@ page language="java" %>
<%@ page import="java.util.*"%>
<%@ page import="edu.harvard.med.hip.bec.user.*"%>
<%@ page import="edu.harvard.med.hip.bec.Constants"%>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<html>

<head>
<script language="JavaScript" src="/BEC/navcond.js"></script>
<script language="JavaScript">

/*
Top Navigational Bar II (By Mike Hall)
Last updated: 00/05/08
Permission granted and modified by Dynamicdrive.com to include script in archive
For this and 100's more DHTML scripts, visit http://dynamicdrive.com
*/

var myNavBar1 = new NavBar(0);
var dhtmlMenu;

//define menu items (first parameter of NavBarMenu specifies main category width, second specifies sub category width in pixels)
//add more menus simply by adding more "blocks" of same code below

dhtmlMenu = new NavBarMenu(80, 0);
dhtmlMenu.addItem(new NavBarMenuItem("About", "about_bec.jsp"));
myNavBar1.addMenu(dhtmlMenu);

<% User user = (User)session.getAttribute(Constants.USER_KEY);
	System.out.println(user.getUserGroup());

%>
<% if (user.getUserGroup().equals("Administrator"))
{
    
%>
	dhtmlMenu = new NavBarMenu(150, 290);
	dhtmlMenu.addItem(new NavBarMenuItem("Configure System", ""));
	dhtmlMenu.addItem(new NavBarMenuItem("End Reads Evaluation", "Seq_EnterEndReadsParameters.jsp"));
	dhtmlMenu.addItem(new NavBarMenuItem("Biological Evaluation of Clones", "Seq_EnterFullSeqParameters.jsp"));
	dhtmlMenu.addItem(new NavBarMenuItem("Primer Design", "Seq_EnterPrimerParameters.jsp"));
	dhtmlMenu.addItem(new NavBarMenuItem("Polymorphism Finder", "Seq_EnterPolymorphismParameters.jsp"));
dhtmlMenu.addItem(new NavBarMenuItem("Available Vectors Information", "/BEC/Seq_GetItem.do?forwardName=<%=Constants.AVAILABLE_VECTORS_DEFINITION_INT%>"));
dhtmlMenu.addItem(new NavBarMenuItem("Available Linkers Information", "/BEC/Seq_GetItem.do?forwardName=<%=Constants.AVAILABLE_LINKERS_DEFINITION_INT%>"));
	dhtmlMenu.addItem(new NavBarMenuItem("Help", "Help_ConfigureSystem.jsp"));
	myNavBar1.addMenu(dhtmlMenu);


<%}%>

<% if (!user.getUserGroup().equals("Researcher"))
{
    
%>      dhtmlMenu = new NavBarMenu(100, 200);
	dhtmlMenu.addItem(new NavBarMenuItem("Process", "SelectProcess.jsp"));
	myNavBar1.addMenu(dhtmlMenu);
<%}%>
dhtmlMenu = new NavBarMenu(100, 220);
dhtmlMenu.addItem(new NavBarMenuItem("Search", ""));
dhtmlMenu.addItem(new NavBarMenuItem ("Container History", "ContainerScan.jsp?forwardName=<%=Constants.CONTAINER_PROCESS_HISTORY%>&amp;<%=Constants.JSP_TITLE%>=container Process History"));
dhtmlMenu.addItem(new NavBarMenuItem("Container Description", "ContainerScan.jsp?forwardName=<%=Constants.CONTAINER_DEFINITION_INT%>&amp;<%=Constants.JSP_TITLE%>=container Description"));
dhtmlMenu.addItem(new NavBarMenuItem("Container Results", "ContainerScan.jsp?forwardName=<%=Constants.CONTAINER_RESULTS_VIEW%>&amp;<%=Constants.JSP_TITLE%>=container Results"));
<!--dhtmlMenu.addItem(new NavBarMenuItem("Container Results", "ContainerScan.jsp?forwardName=<%=Constants.CONTAINER_ISOLATE_RANKER_REPORT%>&amp;<%=Constants.JSP_TITLE%>=container Isolate Ranker Report")); -->
dhtmlMenu.addItem(new NavBarMenuItem("Clone History", "/BEC/logon.do"));
dhtmlMenu.addItem(new NavBarMenuItem("Sample History", "ChipGeneGeneAnalysis_1.jsp"));
myNavBar1.addMenu(dhtmlMenu);

dhtmlMenu = new NavBarMenu(100, 220);
dhtmlMenu.addItem(new NavBarMenuItem("Help", ""));
dhtmlMenu.addItem(new NavBarMenuItem("Plate Information Uploader", "Help_PlateUploader.jsp"));
dhtmlMenu.addItem(new NavBarMenuItem("System Configuration", "Help_ConfigureSystem.jsp"));
dhtmlMenu.addItem(new NavBarMenuItem("Isolate Ranker", "Help_IsolateRanker.jsp"));
dhtmlMenu.addItem(new NavBarMenuItem("Discrepancy Finder", "Help_DiscrepancyFinder.jsp"));
dhtmlMenu.addItem(new NavBarMenuItem("Polymorphism Finder", "Help_PolymorphismFinder.jsp"));
dhtmlMenu.addItem(new NavBarMenuItem("Primer Designer", "Help_PrimerDesigner.jsp"));
myNavBar1.addMenu(dhtmlMenu);


dhtmlMenu = new NavBarMenu(100, 120);
dhtmlMenu.addItem(new NavBarMenuItem("Contact us", "mailto:elena_taycher@hms.harvard.edu"));
myNavBar1.addMenu(dhtmlMenu);

dhtmlMenu = new NavBarMenu(100, 120);
dhtmlMenu.addItem(new NavBarMenuItem("Log out", "Logout.do"));
myNavBar1.addMenu(dhtmlMenu);

//set menu colors
//myNavBar1.setColors("#ffffff", "#ffffff", "#3366cc", "#000000", "#99ccff", "#000000", "#cccccc", "#ffffff", "#000080");
myNavBar1.setColors("#ffffff", "#ffffff", "#3366cc", "#ffffff", "#e31801", "#0a318a", "#99ccff", "#ffffff", "#000080");

//uncomment below line to center the menu (valid values are "left", "center", and "right"
//myNavBar1.setAlign("center")

//set menu font
myNavBar1.setFonts("MS Sans Serif", "plain", "bold", "10pt", "Verdana", "plain", "bold", "9pt");

//set size
myNavBar1.setSizes(1, 4, 1);

var fullWidth;

function init() {

  // Get width of window, need to account for scrollbar width in Netscape.

  fullWidth = getWindowWidth() 
    - (isMinNS4 && getWindowHeight() < getPageHeight() ? 16 : 0);

  //myNavBar1.resize(fullWidth);
  myNavBar1.resize(900);
  myNavBar1.create();
  myNavBar1.setzIndex(2);  
  myNavBar1.moveTo(60, 110);
}
</script>
</head>

<body onload="init()">
<table width="90%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td width="80%"><font color="#3333CC"> 
      <h1><strong>Biological Evaluation of Clones (BEC)</strong></h1>
      </font></td>
    <td > <img align="top" border="0" src="./jpg/earth.gif" width=76 height="76" > 
    </td>
    <td><img border="0" src="./jpg/pc&woman.gif"  width=96 height="76" ></td>
  </tr>
 
 
</table>

</body>
