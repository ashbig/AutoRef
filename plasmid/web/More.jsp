<%-- 
    Document   : more
    Created on : Mar 10, 2015, 12:12:51 PM
    Author     : user
--%>

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.User" %> 
<!DOCTYPE html>
<html>
    <head>

        <link href="layout.css" rel="stylesheet" type="text/css" />
        <link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
        <link href="boilerplate.css" rel="stylesheet" type="text/css" />
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
        <link rel="shortcut icon" href="dnacore.ico">
        <link rel="stylesheet" type="text/css" href="print.css" media="print">
        <!--<link rel="stylesheet" href="responsivemobilemenu.css" type="text/css"/>
        <script type="text/javascript" src="http://code.jquery.com/jquery.min.js"></script>
        <script type="text/javascript" src="responsivemobilemenu.js"></script>-->
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mobile Menu: More</title>
    </head>
    <style>
.rmm {
	display:block;
	position:fixed;
        top:-0px;
        right: 0%;
        left: -40px;
        width: 115%;
	text-align: left;
	line-height:19px !important;
        z-index:1;
}
.rmm * {
	-webkit-tap-highlight-color:transparent !important;
	font-family:Arial;
}
.rmm a {
	color:#ebebeb;
	text-decoration:none;
}
.rmm li {
	margin:0px;
	padding:0px;
}
.rmm ul {
	display:block;
	width:auto !important;
	margin:0 auto !important;
	overflow:hidden;
	list-style:none;
}


.rmm li {
	display:inline;
	padding: 0px;
	margin:0px !important;
}

.rmm ul li {
	display:block;
	margin:0 auto !important;
}




/* GRAPHITE STYLE */

.rmm li a {
	display:inline-block;
	padding:8px 30px 8px 30px;
	margin:0px -3px 0px -3px;
	font-size:15px;
	text-shadow:1px 1px 1px #333333;
	background-color:#444444;
	border-left:1px solid #555555;
	background-image:url('../rmm-img/graphite-menu-bg.png');
	background-repeat:repeat-x;
        width:100%;
}

.rmm ul li a {
	display:block;
	width:100%;
	background-color:#555555;
	text-align:center;
	padding:15px 0px 15px 0px;
	border-bottom:1px solid #333333;
	border-top:1px solid #777777;
	text-shadow:1px 1px 1px #333333;
        font-size:16px;
}
.rmm ul li a:active {
	background-color:#444444;
	border-bottom:1px solid #444444;
	border-top:1px solid #444444;
    </style>
    <body>
<p align="center">Mobile Only</p>
<div class="gridContainer clearfix">        
<div class="rmm" data-menu-style = "graphite" data-menu-title = "... More" id="rmm">
        <ul>
                            <li><a href="Home.xhtml">&#9194; &nbsp;Main Menu</a></li>
                            <!--<li><a href="GetVectorsByType.do">Vectors</a></li>
                            <li><a href="GetCollectionList.do" title="View List of Collections">Collections</a></li>-->
                            <li><a href="Submission.jsp" title="Submit plasmids">Submission</a></li>
                            <li><a href="Pricing.jsp" title="pricing">Pricing</a></li>
                            <li><a href="cloningstrategies.jsp" title="Learn About Cloning Methods">Learn</a></li>
                            <li><a href="Contactus.jsp">Contact</a></li>
                            <li><a href="AboutUs.jsp">About Us</a></li>
                            <li><a target="_blank" href="FAQ.jsp">FAQ</a></li>                
                            <logic:present name="<%=Constants.USER_KEY%>" scope="session">
                            <li><a href="Logout.do">Sign Out</a></li>     
                            </logic:present>  
       </ul>
</div>
</div>
</html>
    </body>
</html>
