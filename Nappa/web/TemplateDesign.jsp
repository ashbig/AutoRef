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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.templateDesign}"/></td>
                </tr>
            </table>

            <t:saveState id="isTemplateID" value="#{templateDesignBean.design}" />
            <h:outputText styleClass="errors" value="#{templateDesignBean.message}"/>
            
            <h:form id="layoutDesignForm">
                <h:panelGrid columns="2" columnClasses="top,top"> 
                    <h:outputLabel styleClass="prompt" value="#{msgs.chooseLayoutName}:" for="layoutname"/>
                    <h:selectOneMenu styleClass="text" id="layoutname" value="#{templateDesignBean.layoutname}">	
                        <f:selectItems value="#{templateDesignBean.allLayouts}" />
                    </h:selectOneMenu> 
                    <h:outputLabel styleClass="prompt" value="#{msgs.templateName}:" for="name"/>
                    <h:panelGroup>
                    <h:inputText id="name" value="#{templateDesignBean.name}" required="true"/>
                        <h:message styleClass="errors" for="name" />
                    </h:panelGroup>
                    <h:outputLabel styleClass="prompt" value="#{msgs.templateDescription}:" for="desc"/>
                    <h:inputTextarea rows="10" cols="50" id="desc" value="#{templateDesignBean.description}" required="false"/>
                </h:panelGrid>
                
                <h:commandButton value="#{msgs.submitButton}" action="#{templateDesignBean.designTemplate}"/>    
            </h:form> 

        </f:view>
    </body>
</html>
