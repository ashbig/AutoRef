<%--


    File : FlexSequenceUI.jsp

    Description :
        
        Displays all information about a FLEX sequence.
 
    
 Author : Juan Munoz (jmunoz@3rdmill.com)
 
 See COPYRIGHT file for copyright information
 
 
 The following information is used by CVS
 $Revision: 1.11 $
 $Date: 2001-07-30 19:04:52 $
 $Author: jmunoz $
 
 ******************************************************************************
 
  Revision history (Started on May 30, 2001) :
 
     Add entries here when updating the code. Remember to date and insert
     your 3 letters initials.
 
     May-30-YYYY : JMM - Class Created


|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
--%>

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : FLEX Sequence Info</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<H2><bean:message key="flex.name"/> : FLEX Sequence Info</h2>
<hr>
<html:errors/>
<p>
<TABLE WIDTH=80% ALIGN=CENTER>
    <TR>
        <TD class="label">Flex ID:</td>
        <td><bean:write name="FLEX_SEQUENCE" property="id"/></TD>
    </TR>
    <TR>
        <TD class="label">Sequence Description:</td>
        <td><bean:write name="FLEX_SEQUENCE" property="description"/></TD>
    </TR>
    <TR>
        <TD class="label">Sequence Length:</td>
        <td><bean:write name="FLEX_SEQUENCE" property="sequenceLength"/></TD>
    </TR>
    <TR>
        <TD class="label">GI Number:</td>
        <td><bean:write name="FLEX_SEQUENCE" property="gi"/></TD>
        <TD class="label">Accession Number:</td>
        <td><bean:write name="FLEX_SEQUENCE" property="accession"/></TD>
    </TR>
    <TR>
        <TD class="label">Start Position:</td>
        <td><bean:write name="FLEX_SEQUENCE" property="cdsstart"/></TD>
        <TD class="label">Stop Position:</td>
        <td><bean:write name="FLEX_SEQUENCE" property="cdsstop"/></TD>
    </TR>
    <TR>
        <TD class="label">CDS Length:</td>
        <td><bean:write name="FLEX_SEQUENCE" property="cdslength"/></TD>
    </TR>
    <TR>
        <TD class="label">Species:</td>
        <td><bean:write name="FLEX_SEQUENCE" property="species"/></TD>
    </TR>
    <TR>
        <TD class="label">Quality</td>
        <td><bean:write name="FLEX_SEQUENCE" property="quality"/></TD>
    </TR>
    <TR>
        <TD class="label">Date Added:</td>
        <td><bean:write name="FLEX_SEQUENCE" property="dateadded"/></TD>
    </TR>
    <TR>
        <TD class="label">GC content:</td>
        <td><bean:write name="FLEX_SEQUENCE" property="gccontent"/></TD>
    </TR>
    <TR>
        <TD class="label">Sequence:</TD>
    </TR>
    
    <TR><TD COLSPAN=2>
    <%--
        make the cds region in red.
        NOTE: Sequence indexes start at 1 while String indexes start at 0
        --%>
        <PRE><bean:write name="FASTA_COLOR_SEQUENCE" filter="false"/></PRE>
        </TD>
    </TR>
</TABLE>
</body>
</html>