<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>NAPPA Tracking System</title>
    </head>
    <body>

    <h2>NAPPA Tracking</h2>
    <br/>
            <p>Design
            <ul>
                <li><a href="./faces/AddProgram.jsp" target="rightFrame">Add Robot Program</a></li>
                <li><a href="./faces/LayoutDesign.jsp" target="rightFrame">Design Slide Layout</a></li>
                <li><a href="./faces/TemplateDesign.jsp" target="rightFrame">Design Slide Template</a></li>
            </ul>
            <p>Process
            <ul>
                <li><a href="./faces/ImportClones.jsp" target="rightFrame">Import Reagents</a></li>
                <li><a href="./faces/RegisterLabels.jsp" target="rightFrame">Register Container Labels</a></li>
                <li><a href="./faces/DirectContainerMapping.jsp" target="rightFrame">Generate New Containers</a></li>
                <li><a href="./faces/EnterResults.jsp" target="rightFrame">Enter Results</a></li>
                <li><a href="./faces/SearchContainer.jsp" target="rightFrame">Add Controls</a></li>
            </ul>
            <p>View
            <ul>
                <li><a href="./faces/SearchLayout.jsp" target="rightFrame">Search Slide Layout</a></li>
                <li><a href="./faces/SearchTemplate.jsp" target="rightFrame">Search Slide Template</a></li>
                <li><a href="./faces/SearchContainers.jsp" target="rightFrame">Search Containers</a></li>
            </ul>
            <p><a href="./Logout.jsp" target="_parent">Logout</a>
    
    <%--
    This example uses JSTL, uncomment the taglib directive above.
    To test, display the page like this: index.jsp?sayHello=true&name=Murphy
    --%>
    <%--
    <c:if test="${param.sayHello}">
        <!-- Let's welcome the user ${param.name} -->
        Hello ${param.name}!
    </c:if>
    --%>
    
    </body>
</html>