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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.ViewContainer}"/></td>
                </tr>
            </table>
            
            <h:form id="layoutContainerDetailViewForm">
                <t:dataTable id="table" value="#{layoutViewBean.plateModel}" var="plate" frame="box">
                    <t:column id="rowlabel">
                        <h:outputText value="#{layoutViewBean.rowLabel.cell.posx}"/>
                    </t:column>
                    <t:columns id="col" value="#{layoutViewBean.headerModel}" var="colHeader" styleClass="#{layoutViewBean.plateValue.cell.control? 'controlcell' : 'regularcell'}">        
                        <f:facet name="header">
                            <h:outputText value="#{colHeader}"/>
                        </f:facet>
                        <h:outputText value="#{layoutViewBean.plateValue.cell.pos},#{layoutViewBean.plateValue.cell.type}"/>
                        <t:dataTable value="#{layoutViewBean.plateValue.pre}" var="precell">
                            <t:column>
                                <h:outputText value="#{precell.containerlabel},#{precell.posx},#{precell.posy}"/>
                            </t:column>
                        </t:dataTable>
                        <h:outputText rendered="#{!layoutViewBean.editcontrol}" id="reagentid1" value="#{layoutViewBean.plateValue.cell.controlreagent}"/>
                        <h:selectOneMenu rendered="#{layoutViewBean.editcontrol && layoutViewBean.plateValue.cell.control}" styleClass="text" id="reagentid" value="#{layoutViewBean.plateValue.cell.controlreagent}">	
                            <f:selectItems value="#{layoutViewBean.controlreagents}" />
                        </h:selectOneMenu> 
                    </t:columns>
                </t:dataTable>
                <h:commandButton rendered="#{layoutViewBean.editcontrol && layoutViewBean.container.iscontrol}" value="#{msgs.saveButton}" action="#{layoutViewBean.saveControls}"/>  
            </h:form>
        </f:view>
    </body>
</html>
