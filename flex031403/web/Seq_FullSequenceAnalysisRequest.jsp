<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.flex.seqprocess.engine.*" %>
<html>

<head><title>JSP Page</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : <bean:message key="flex.name"/> Sequence Analysis</h2>
<hr>
<html:errors/> 

<html:form action="/Seq_SequenceAnalysis.do" focus="searchValue"> 

<p>
<table border=0 cellspacing=10 cellpadding=2 align="center" width="90%">
    <tr>
    <td class="prompt">Search Term:</td>
    <td><select name="searchType">
            
            <option value="<%= FullSequenceAnalysis.SEARCH_BY_SEQUENCEID%>">
                    Reference Sequence ID
            <option value="<%= FullSequenceAnalysis.SEARCH_BY_GI%>">GI
        </select></td>
    </td>
    </tr>
    <tr>
    <td class="prompt">Search Value:</td>
    <td> <input name="searchValue" type="text"  value="" size="25"></td>
    </tr>


</table>
<table border=0 cellspacing=10 cellpadding=2 align="center" width="90%">
<tr>
<td class="prompt">
Enter experimental sequence text:</td>
<td>
<textarea name="fullsequence" rows=6 cols=60></textarea>
</td></tr>
</table>


  <p> 
 <!--   <input type="button" value="Submit" name="B1" onFocus="return checkrequired(this)"> -->
   <input type="submit" value="Submit" > 

</html:form> 

</body>
</html>
