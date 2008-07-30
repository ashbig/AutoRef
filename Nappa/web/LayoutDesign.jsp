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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.layoutDesign}"/></td>
                </tr>
            </table>
            
            <t:saveState id="isdesignID" value="#{layoutDesignBean.design}" />
            <h:outputText styleClass="errors" value="#{layoutDesignBean.message}"/>
            
            <h:form id="layoutDesignForm">
                <h:panelGrid columns="2" columnClasses="top,top"> 
                    <h:outputLabel styleClass="prompt" value="#{msgs.layoutName}:" for="name"/>
                    <h:panelGroup>
                        <h:inputText id="name" value="#{layoutDesignBean.name}" required="true"/>
                        <h:message styleClass="errors" for="name" />
                    </h:panelGroup>
                    <h:outputLabel styleClass="prompt" value="#{msgs.layoutDescription}:" for="desc"/>
                    <h:inputTextarea rows="10" cols="50" id="desc" value="#{layoutDesignBean.description}" required="false"/>
                    <h:outputLabel styleClass="prompt" value="#{msgs.program1Promp}:" for="program1"/>
                    <h:selectOneMenu styleClass="text" id="program1" value="#{layoutDesignBean.program1}">	
                        <f:selectItems value="#{layoutDesignBean.allPrograms}" />
                    </h:selectOneMenu> 
                    <h:outputLabel styleClass="prompt" value="#{msgs.program2Promp}:" for="program2"/>
                    <h:selectOneMenu styleClass="text" id="program2" value="#{layoutDesignBean.program2}">	
                        <f:selectItems value="#{layoutDesignBean.allPrograms}" />
                    </h:selectOneMenu> 
                </h:panelGrid>
                
                <h:commandButton value="#{msgs.submitButton}" action="#{layoutDesignBean.designLayout}"/>    
            </h:form> 
            
        </f:view>
    </body>
</html>
