<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ page import="edu.harvard.med.hip.flex.Constants"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Available Clones</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
 </head>
<body>
    
    <h2><bean:message key="flex.name"/> : Available Clones</h2>
    <hr>
    <html:errors/>
    <p>
    Total clones: <bean:write name="totalClones"/>
<p>
    <TABLE width="100%" border="1" cellpadding="2" cellspacing="0">
    <tr class="headerRow">
        <TH><a href="CloneSort.do?sortby=flexseqid&isSort=1&totalClones=<bean:write name="totalClones"/>">FLEX Sequence Id</a></TH>        
        <TH><a href="CloneSort.do?sortby=cloneAcc&isSort=1&totalClones=<bean:write name="totalClones"/>">Genbank</a></TH>
        <TH><a href="CloneSort.do?sortby=genbank&isSort=1&totalClones=<bean:write name="totalClones"/>">Reference GenBank</a></TH>
        <TH><a href="CloneSort.do?sortby=genesymbol&isSort=1&totalClones=<bean:write name="totalClones"/>">Gene Symbol</a></TH>
        <TH><a href="CloneSort.do?sortby=clonename&isSort=1&totalClones=<bean:write name="totalClones"/>">FLEX Clone ID</a></TH>
        <TH><a href="CloneSort.do?sortby=constructtype&isSort=1&totalClones=<bean:write name="totalClones"/>">Version</a></TH>
        <TH>Cloning Strategy</TH>
        <TH>Vector</TH>
    </TR>
    
    <!-- iterate through each QueueItem (sequence) that is in the queue -->
    <!-- keep track of the count -->
    <logic:iterate name="currentInfo" id="clone"> 
        <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
            <TD>
                <A target="_blank" HREF="ViewSequence.do?FLEX_SEQUENCE_ID=<bean:write name="clone" property="refsequenceid"/>">
                    <flex:write name="clone" property="refsequenceid"/>
                </A>                           
            </TD>
            <TD>
                <A target="_blank" HREF="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="clone" property="nameinfo.cloneGi"/>&dopt=GenBank"> 
                    <flex:write name="clone" property="nameinfo.cloneAcc"/>
                </A>
            </TD>
            <TD>
                <A target="_blank" HREF="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="clone" property="nameinfo.gi"/>&dopt=GenBank"> 
                    <flex:write name="clone" property="nameinfo.genbank"/>
                </A>
            </TD>
            <TD>
                <flex:write name="clone" property="nameinfo.genesymbol"/>
            </TD>
            <TD>
                <a target="_blank" href="ViewClone.do?cloneid=<flex:write name="clone" property="cloneid"/>&<%=Constants.ISDISPLAY%>=<bean:write name="<%=Constants.ISDISPLAY%>"/>"><flex:write name="clone" property="clonename"/></a>
            </TD>
            <TD>
                <flex:write name="clone" property="constructtype"/>
            </TD>
            <TD>
                <flex:write name="clone" property="cloningstrategy.name"/>
            </TD>
            <TD>
                <a target="_blank" href="ViewVector.do?vectorname=<bean:write name="clone" property="cloningstrategy.clonevector.name"/>">
                    <flex:write name="clone" property="cloningstrategy.clonevector.name"/>
                </a>
            </TD>
        </flex:row>
    </logic:iterate> 


</TABLE>

<p align="right">
<html:form action="/CloneSort.do" method="POST">

Page&nbsp;<bean:write name="pageindex"/>&nbsp;&nbsp&nbsp;

<input type="hidden" name="isSort" value="0">
<input type="hidden" name="totalClones" value="<bean:write name="totalClones"/>">
 
<logic:equal name="prev" value="1">
<input type="hidden" name="pageindex" value="<bean:write name="pageindex"/>">
<input type="hidden" name="pagerecord" value="<bean:write name="pagerecord"/>">
<html:submit property="prevButton" value="Prev"/>
</logic:equal>

<logic:equal name="next" value="1">
<input type="hidden" name="pageindex" value="<bean:write name="pageindex"/>">
<input type="hidden" name="pagerecord" value="<bean:write name="pagerecord"/>">
<html:submit property="prevButton" value="Next"/>
</logic:equal>

</html:form>
<!--
<p align="center">
<html:submit property="export" value="Export"/>
-->
<logic:notPresent name="<%=Constants.USER_KEY%>" scope="session">
<jsp:include page="footer.jsp"/>
</logic:notPresent>

</body>
</html>