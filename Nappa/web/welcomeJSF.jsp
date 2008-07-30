<html>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
    
    <f:view>
        <head> 
            <link href="styles.css" rel="stylesheet" type="text/css"/>
            <title>
                <h:outputText value="#{msgs.windowTitle}"/>
            </title>
        </head>
        
        <body>
            <h:form>
                <h:panelGrid styleClass="tabbedPane" columnClasses="displayPanel">
                    <%-- Tabs --%>
  
                    <f:facet name="header">
                        <h:panelGrid columns="5" styleClass="tabbedPaneHeader">
                            
                            <h:commandLink tabindex="1"
                                           title="#{tp.jeffersonTooltip}"
                                           styleClass="#{tp.jeffersonStyle}"
                                           actionListener="#{tp.jeffersonAction}">
                                
                                <h:outputText value="#{msgs.jeffersonTabText}"/>
                            </h:commandLink>
                            
                            <h:commandLink tabindex="2"
                                           title="#{tp.rooseveltTooltip}"
                                           styleClass="#{tp.rooseveltStyle}"
                                           actionListener="#{tp.rooseveltAction}">
                                
                                <h:outputText value="#{msgs.rooseveltTabText}"/>
                            </h:commandLink>
                            
                            <h:commandLink tabindex="3"
                                           title="#{tp.lincolnTooltip}"
                                           styleClass="#{tp.lincolnStyle}"
                                           actionListener="#{tp.lincolnAction}">
                                
                                <h:outputText value="#{msgs.lincolnTabText}"/>
                            </h:commandLink>
                            
                            <h:commandLink tabindex="4"
                                           title="#{tp.washingtonTooltip}"
                                           styleClass="#{tp.washingtonStyle}"
                                           actionListener="#{tp.washingtonAction}">
                                
                                <h:outputText value="#{msgs.washingtonTabText}"/>
                            </h:commandLink>
                        </h:panelGrid>
                    </f:facet>
                    
                    <%-- Tabbed pane content --%>

                    <%@ include file="ImportClones.jsp" %>   
                    <%@ include file="SearchContainers.jsp" %>   
                    <%@ include file="RegisterLabels.jsp" %>   
                    <%@ include file="DirectContainerMapping.jsp" %>   
                </h:panelGrid>
            </h:form>
        </body>
    </f:view>
</html>  
