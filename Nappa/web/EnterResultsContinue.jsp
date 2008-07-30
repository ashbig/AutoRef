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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.enterResults}"/></td>
                </tr>
            </table>
            
            <t:saveState id="enterResultsControllerID1" value="#{enterResultsBean.controller}" />

            <h:outputText rendered="#{enterResultsBean.shownofound}" styleClass="errors" value="No files were found for the following barcodes: #{enterResultsBean.controller.nofoundLabels}"/>
            <br><br>
            <h:outputText styleClass="prompt" value="Please Confirm:"/>
            <t:dataTable id="container" value="#{enterResultsBean.containerModel}" var="plate">
                <t:column id="eachplate">       
                    <t:dataTable id="resultid" value="#{plate.results}" var="result" frame="box">
                        <f:facet name="header">
                            <h:outputText styleClass="text" value="Container: #{enterResultsBean.containerbarcode}"/>
                        </f:facet>
                        <t:column id="rowlabel">
                            <h:outputText value="#{plate.rowLabel.cell.posx}"/>
                        </t:column>
                        <t:columns id="col" value="#{plate.header}" var="colHeader">       
                            <f:facet name="header">
                                <h:outputText value="#{colHeader}"/>
                            </f:facet>
                            <t:dataTable value="#{plate.plateValue.results}" var="result">
                                <t:column>
                                    <h:outputText value="#{result.value}"/>
                                </t:column>
                            </t:dataTable>  
                        </t:columns>
                    </t:dataTable>
                </t:column>
            </t:dataTable>
            
            <h:form id="enterResultsForm">
            <h:commandButton value="#{msgs.submitButton}" action="#{enterResultsBean.enterResults}"/>   
            </h:form>
        </f:view>
    </body>
</html>
