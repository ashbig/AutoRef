<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>
<%@ page import="edu.harvard.med.hip.flex.query.handler.QueryManager" %>
<%@ page import="edu.harvard.med.hip.flex.form.QueryFlexForm" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.CloneInfo" %>
<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Query Results</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<H2><bean:message key="flex.name"/> : Query Results</h2>
<hr>
<html:errors/>

<h3>Search Parameters:</h3>
<table>
<logic:iterate name="params" id="param">
<tr>
    <td class="prompt"><small><bean:write name="param" property="name"/></small></td>
    <td><small><bean:write name="param" property="value"/></small></td>
</tr>
</logic:iterate>
</table>

<p>
<html:form action="/GetSearchResults.do">
    View options: <html:select property="searchCriteria">
                    <html:option value="<%=QueryManager.SUMMARY%>"/>
                    <html:option value="<%=QueryManager.DETAIL%>"/>
                  </html:select>
                 
                  <html:select property="cloneCriteria">
                    <html:option value="<%=QueryManager.ALL%>"/>
                    <html:option value="<%=QueryManager.SEQUENCE_VERIFIED%>"/>
                  </html:select>
                 
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    Total pages: <bean:write name="pages"/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    Page   <html:select property="currentPage">
            <html:options collection="allPages" property="name" labelProperty="value"/>  
            </html:select>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
    Display <html:select property="pageSize">
            <html:options collection="pageSizes" property="name" labelProperty="value"/>  
            </html:select>
<html:hidden property="searchid"/>
<html:hidden property="condition"/>

<logic:present name="lastFounds">
    <input type="hidden" name="lastFounds" value="<bean:write name="lastFounds"/>">
</logic:present>

<html:submit property="submitButton" value="Go"/>
<html:submit property="submitButton" value="Export"/>

<p>
<TABLE border=1>
    <tr bgcolor="#9bbad6">
    <th rowspan="2">Search Term</th><th rowspan="2">Match Genbank</th>
    <th rowspan="2">Locus ID</th><th rowspan="2">Match FLEXGene</th>
    <th rowspan="2">Clone Status</th><th rowspan="2">Found By</th>
    <th rowspan="2">Alignments</th>
    <th rowspan="2">Version</th>
    <logic:equal name="<%=Constants.ISDISPLAY%>" value="1">   
    <th rowspan="2">Project</th>
    <th rowspan="2">Workflow</th>
    </logic:equal>
    <th rowspan="2">Status</th>
    <th colspan="6">Available Clones</th>
    </tr>
    <tr bgcolor="#9bbad6">
    <th>Clone ID</th><th>Clone Name</th><th>Clone Type</th>
    <th>Cloning Strategy</th><th>Vector</th><th>Status</th>
    </tr>
    <% int index=0; %>
    <logic:iterate name="results" id="result">
    <tr>
        <td rowspan="<bean:write name="result" property="numOfFoundClones"/>"><bean:write name="result" property="searchTerm"/></td>
        <logic:iterate name="result" property="found" id="mgr">
            <td rowspan="<bean:write name="mgr" property="numOfMatchClones"/>">
            <A target="_blank" HREF="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="mgr" property="gi"/>&dopt=GenBank"> 
                <flex:write name="mgr" property="ganbankAccession"/>
            </A>
            </td>
            <td rowspan="<bean:write name="mgr" property="numOfMatchClones"/>">
                <logic:equal name="mgr" property="hasLocusid" value="0">
                &nbsp;
                </logic:equal>
                <logic:iterate name="mgr" property="locusidList" id="locusid">
                <A target="_blank" HREF="http://www.ncbi.nlm.nih.gov/LocusLink/LocRpt.cgi?l=<bean:write name="locusid"/>"> 
                    <flex:write name="locusid"/>
                </A>&nbsp;
                </logic:iterate>
            </td>
            <logic:iterate name="mgr" property="matchFlexSequence" id="mfs">
                <td rowspan="<bean:write name="mfs" property="numOfClones"/>">
                <A target="_blank" HREF="ViewSequence.do?FLEX_SEQUENCE_ID=<bean:write name="mfs" property="flexsequenceid"/>">
                    <flex:write name="mfs" property="flexsequenceid"/>
                </A>
                </td>

                <td rowspan="<bean:write name="mfs" property="numOfClones"/>">
                <logic:equal name="mfs" property="flexSequence.isCloned" value="true">
                <A target="_blank" HREF="ViewConstructsInfo.do?sequenceid=<bean:write name="mfs" property="flexsequenceid"/>">
                </logic:equal>
                <flex:write name="mfs" property="flexSequence.flexstatus"/>
                <logic:equal name="mfs" property="flexSequence.isCloned" value="true">
                </A>
                </logic:equal>
                </td>

                <td rowspan="<bean:write name="mfs" property="numOfClones"/>"><flex:write name="mfs" property="foundBy"/></td>
                <logic:equal name="mfs" property="isMatchByGi" value="F">
                <td rowspan="<bean:write name="mfs" property="numOfClones"/>"><A target="_blank" href="ViewBlastOutput.do?outputFile=<bean:write name="mfs" property="blastHit.outputFile"/>&sequenceid=<bean:write name="mfs" property="flexsequenceid"/>">View Alignments</A></td>
                </logic:equal>
                <logic:notEqual name="mfs" property="isMatchByGi" value="F">
                <td rowspan="<bean:write name="mfs" property="numOfClones"/>">NA</td>
                </logic:notEqual>
                <!--<td rowspan="<bean:write name="mfs" property="numOfClones"/>"><flex:write name="mgr" property="searchMethod"/></td>-->
                                    
                <logic:equal name="mfs" property="hasClones" value="0">
                    <td>NA</td>
                    <logic:equal name="<%=Constants.ISDISPLAY%>" value="1">
                    <td>NA</td>
                    <td>NA</td>
                    </logic:equal>
                    <td>NA</td>
                    <td>NA</td>
                    <td>NA</td>
                    <td>NA</td>
                    <td>NA</td>
                    <td>NA</td>
                    <td>NA</td>
                </logic:equal>

                <logic:iterate name="mfs" property="constructInfos" id="constructInfo">
                    <td rowspan="<bean:write name="constructInfo" property="numOfClones"/>"><flex:write name="constructInfo" property="constructType"/></td>
                    <logic:equal name="<%=Constants.ISDISPLAY%>" value="1">  
                    <td rowspan="<bean:write name="constructInfo" property="numOfClones"/>"><flex:write name="constructInfo" property="projectName"/></td>
                    <td rowspan="<bean:write name="constructInfo" property="numOfClones"/>"><flex:write name="constructInfo" property="workflowName"/></td>
                    </logic:equal>
                    <td rowspan="<bean:write name="constructInfo" property="numOfClones"/>"><flex:write name="constructInfo" property="status"/></td>
            
                    <logic:equal name="constructInfo" property="numOfClones" value="0">
                    <td>NA</td>
                    <td>NA</td>
                    <td>NA</td>
                    <td>NA</td>
                    <td>NA</td>
                    <td>NA</td>
                    </logic:equal>

                    <logic:iterate name="constructInfo" property="clones" id="clone">
                        <td>
                            <A target="_blank" href="ViewClone.do?cloneid=<bean:write name="clone" property="cloneid"/>&<%=Constants.ISDISPLAY%>=<bean:write name="<%=Constants.ISDISPLAY%>"/>">
                            <flex:write name="clone" property="cloneid"/>
                            </A>
                            <bean:define id="form" name="queryFlexForm"/>

                            <logic:equal name="<%=Constants.ISDISPLAY%>" value="1">
                                <input type="checkbox" name="checkedClone[<%=index%>]" value="<bean:write name="clone" property="exportId"/>"
                            </logic:equal>
                            <logic:equal name="<%=Constants.ISDISPLAY%>" value="0"> 
                                <input type="checkbox" name="checkedClone[<%=index%>]" value="<bean:write name="clone" property="limitedExportId"/>"
                            </logic:equal> <%
                                    int isDisplay = ((Integer)request.getAttribute(Constants.ISDISPLAY)).intValue();
                                    String selectedClone;
                                    if(isDisplay == 1) {
                                        selectedClone = ((CloneInfo)clone).getExportId();
                                    } else {
                                        selectedClone = ((CloneInfo)clone).getLimitedExportId();
                                    }
                                    List selectedClones = ((QueryFlexForm)form).getSelectedClones();
                                    if(selectedClones != null) {
                                        for(int i=0; i<selectedClones.size(); i++) {
                                            String c = (String)selectedClones.get(i);
                                            if(c.equals(selectedClone)) {
                                                out.println(" checked");
                                                break;
                                            }
                                        }
                                    }%>>
                        </td>
                            <logic:equal name="<%=Constants.ISDISPLAY%>" value="1"> 
                                <input type="hidden" name="allClone[<%=index%>]" value="<bean:write name="clone" property="exportId"/>">
                            </logic:equal>
                            <logic:equal name="<%=Constants.ISDISPLAY%>" value="0">
                                <input type="hidden" name="allClone[<%=index%>]" value="<bean:write name="clone" property="limitedExportId"/>">
                            </logic:equal>
                            <% index++; %>
                        <td><flex:write name="clone" property="clonename"/></td>
                        <td><flex:write name="clone" property="clonetype"/></td>
                        <td><flex:write name="clone" property="cloningstrategy.name"/></td>
                        <td><flex:write name="clone" property="cloningstrategy.clonevector.name"/></td>
                        <td><flex:write name="clone" property="status"/></td>
                        </tr>
                    </logic:iterate>
                    </tr>
                </logic:iterate>
                </tr>
            </logic:iterate>
        </logic:iterate>    
    </TR>
    </logic:iterate>
</table>

<logic:present name="selectedClones">
    <logic:iterate name="selectedClones" id="sc" indexId="count">
        <input type="hidden" name="selectedClone[<%=count%>]" value="<bean:write name="sc"/>">
    </logic:iterate>
</logic:present>
</html:form>

</body>
</html>