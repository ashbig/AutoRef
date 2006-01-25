<%@ page language="java" %>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<html>
<head>
<title>ACE Help</title>
</head>
<body >

<table border="0" cellpadding="0" cellspacing="0" width="90%" >

<tr><td align='center'><h1>ACE help</h1> </td></td>
<tr><td align='center'><hr><h3>ACE set-up (for application administrator only)</h3> </td></td>
<tr><td > <a href = "<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>help/help_installation.jsp"  > How to set-up ACE</td></tr>
<tr><td > <a href = "<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>help/help_ACEConfigurationFile.html"  > How to configure ACE </td></tr>

<tr><td align='center'><hr><h3>ACE user help</h3> </td></td>
<tr><td > <a href = "<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>help/help_generalhelp.jsp"  >User general help</td></tr>
<tr><td > <a href = "<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>help/help_ReportRunner.html"  > Report runner</td></tr>
<tr><td > <a href = "<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>help/help_TraceFilesNamingFormats.html"  >How to creat trace file names' format</td></tr>

<tr><td > <b>File formats for data submission</b></td></tr>

<tr><td > <a href = "<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>help/help_reference_sequence_xml_format.html"  >Reference sequence submission XML format</td></tr>
<tr><td > <a href = "<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>help/help_vector_xml_format.html"  >Vector submission XML format</td></tr>
<tr><td > <a href = "<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>help/help_clone_sequence_xml_format.html"  >Clone sequence submission XML format</td></tr>
<tr><td > <a href = "<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>help/help_clonecollection_xml_format.html"  >Clone collection(plate) submission XML format</td></tr>

</table>
</body>
</html>