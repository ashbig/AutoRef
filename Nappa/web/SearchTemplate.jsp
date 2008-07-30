<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="nappa.css" rel="stylesheet" type="text/css"/>
        <title>JSP Page</title>
    </head>
    <body>
        <f:view>
            <f:loadBundle basename="bean.messages" var="msgs"/>
            
            <table width="100%">
                <tr>
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.searchTemplate}"/></td>
                </tr>
            </table>
            
            <h:form id="searchTemplateForm">
                <h:panelGrid columns="1"> 
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.enterTemplatenamePrompt}:" for="templatenameid"/>
                    <h:inputText styleClass="text" id="templatenameid" value="#{templateDesignBean.name}" required="true"/>
                    <h:message styleClass="errors" for="templatenameid" />
                    
                </h:panelGrid>
                
                <h:commandButton value="#{msgs.submitButton}" action="#{templateDesignBean.findTemplates}"/>    
            </h:form> 
            
        </f:view>
    </body>
</html>
