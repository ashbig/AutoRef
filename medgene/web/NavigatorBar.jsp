<%@ page language="java" %>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<script language="JavaScript" src="navcond.js"></script>
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
dhtmlMenu.addItem(new NavBarMenuItem("About", "about_medgene.html"));
myNavBar1.addMenu(dhtmlMenu);

dhtmlMenu = new NavBarMenu(180, 290);
dhtmlMenu.addItem(new NavBarMenuItem("Disease Gene Search", ""));
dhtmlMenu.addItem(new NavBarMenuItem("Genes associated with a disease", "DiseaseSearch.jsp"));
dhtmlMenu.addItem(new NavBarMenuItem("Genes associated with multiple diseases", "MultipleDiseaseSearch.jsp"));
dhtmlMenu.addItem(new NavBarMenuItem("Genes associated with a gene", "SelectSpeciesForGeneGene.jsp"));
dhtmlMenu.addItem(new NavBarMenuItem("Diseases associated with a gene", "SelectSpeciesForGeneDisease.jsp"));
myNavBar1.addMenu(dhtmlMenu);

dhtmlMenu = new NavBarMenu(170, 220);
dhtmlMenu.addItem(new NavBarMenuItem("Gene List Analysis", ""));
dhtmlMenu.addItem(new NavBarMenuItem("sort gene list (disease related)", "ChipGeneAnalysis_1.jsp"));
dhtmlMenu.addItem(new NavBarMenuItem("sort gene list (gene related)", "ChipGeneGeneAnalysis_1.jsp"));
myNavBar1.addMenu(dhtmlMenu);

dhtmlMenu = new NavBarMenu(100, 120);
dhtmlMenu.addItem(new NavBarMenuItem("Contact us", "mailto:hip_informatics@hms.harvard.edu"));
myNavBar1.addMenu(dhtmlMenu);

dhtmlMenu = new NavBarMenu(100, 150);
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
  myNavBar1.moveTo(53, 97);
}
</script>
</head>

<body onload="init()">
</body>
