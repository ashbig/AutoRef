<%-- 
    Document   : Vectormap
    Created on : Nov 15, 2012, 10:52:39 PM
    Author     : dongmei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="plasmid.Constants" %> 
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Vector Map</title>
        <script src="http://code.jquery.com/jquery-1.8.1.min.js"></script>
        <script type="text/javascript" src="http://www.labgeni.us/static/javascript/plasmID_map.js"></script>
</head>
<body>
    <div class="map""></div>
    <input type="hidden" name="XML_location" id="XML_location" value="<bean:write name="vectormapurl"/>"/>
</body>
</html>
