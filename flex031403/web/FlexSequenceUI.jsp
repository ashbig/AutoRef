<%--


    File : FlexSequenceUI.jsp

    Description :
        
        Displays all information about a FLEX sequence.
 
    
 Author : Juan Munoz (jmunoz@3rdmill.com)
 
 See COPYRIGHT file for copyright information
 
 
 The following information is used by CVS
 $Revision: 1.3 $
 $Date: 2001-06-13 16:29:10 $
 $Author: dongmei_zuo $
 
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
<head><title>Flex Sequence Info</title></head>
<body>
<html:errors/>
<H1><CENTER>HIP <bean:message key="flex.name"/> Database</CENTER></H1>
<HR><BR>
<TABLE WIDTH=80% ALIGN=CENTER>
    <TR>
        <TD><B>Flex ID: </B><bean:write name="FLEX_SEQUENCE" property="id"/></TD>
    </TR>
    <TR>
        <TD COLSPAN=2><B>Sequence Description: </B> </B><bean:write name="FLEX_SEQUENCE" property="description"/></TD>
    </TR>
    <TR>
        <TD><B>Sequence Length: </B></B><bean:write name="FLEX_SEQUENCE" property="sequenceLength"/></TD>
    </TR>
    <TR>
        <TD><B>GI Number: </B></B><bean:write name="FLEX_SEQUENCE" property="gi"/></TD>
        <TD><B>Accession Number: </B></B><bean:write name="FLEX_SEQUENCE" property="accession"/></TD></TR>
    <TR>
        <TD><B>Start Position: </B><bean:write name="FLEX_SEQUENCE" property="cdsstart"/></TD>
        <TD><B>Stop Position: </B><bean:write name="FLEX_SEQUENCE" property="cdsstop"/></TD>
    </TR>
    <TR>
        <TD><B>CDS Length: </B><bean:write name="FLEX_SEQUENCE" property="cdslength"/></TD>
    </TR>
    <TR>
        <TD><B>Species: </B><bean:write name="FLEX_SEQUENCE" property="species"/></TD>
    </TR>
    <TR>
        <TD><B>Quality: </B><bean:write name="FLEX_SEQUENCE" property="quality"/></TD>
    </TR>
    <TR>
        <TD><B>Date Added: </B><bean:write name="FLEX_SEQUENCE" property="dateadded"/></TD>
    </TR>
    <TR>
        <TD><B>GC content: </B><bean:write name="FLEX_SEQUENCE" property="gccontent"/></TD>
    </TR>
    <TR>
        <TD><B>Sequence: </B></TD>
    </TR>
    
    <TR><TD COLSPAN=2>
        <PRE>
        <%--
        make the cds region in red.
        NOTE: Sequence indexes start at 1 while String indexes start at 0
        --%>
            <bean:write name="FASTA_COLOR_SEQUENCE" filter="false"/>


            </PRE>
        </TD>
    </TR>
</TABLE>
</body>
</html>




