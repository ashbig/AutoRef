<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
    <head>
        <title>MedGene : Login</title>
    </head>
    <body> 

<table width="70%" align="center" border="0"><tr><td>
    <center>
    <h1>Welcome to the MedGene<b style='mso-bidi-font-weight:normal'><sup><span
  style='font-size:14.0pt;mso-bidi-font-size:24.0pt'>SM  </span></sup></b>Database</h1>
<A HREF="http://www.hip.harvard.edu" target="_blank"><font size=4><b>Institute of Proteomics</b></font></A><BR>
<A HREF="http://www.hms.harvard.edu" target="_blank"><font size=4><b>Harvard Medical School</b></font></A><BR>

    <hr>
    <html:errors/>
    <p>  <br>
</center>
<A><font size=3>The accelerated pace of biological research and the advent of genomics and proteomics have resulted in the collection of prodigious amounts of data on diseases and the genes that play a role in them.  Computational methods are needed to assimilate this surfeit of information.  The MedGene project was designed to develop the automated extraction of biomedical knowledge from Medline database to create a human gene-to-disease co-occurrence network for all named human genes and all human diseases by automated analysis of MeSH indexes, title and abstracts in over 11 million Medline records.  Statistical analysis is applied to score the association between a known human gene and a particular human disease based on the frequency with which they were co-cited in Medline records.  Users can review:</font></A><BR>
<BR>
<A><font size=3>1. A list of human genes associated with a particular human disease in ranking order</A></font><BR>
<A><font size=3>2. A list of human genes associated with multiple human diseases in ranking order</A></font><BR>
<A><font size=3>3. A list of human diseases associated with a particular human gene in ranking order</A></font><BR>
<A><font size=3>4. A list of human genes associated with a particular human gene in ranking order</A></font><BR>
<A><font size=3>5. The analyzed gene list from other disease related high-throughput experiments, such as micro-array</A></font><BR>
<A><font size=3>6. The analyzed gene list from other gene related high-throughput experiments, such as micro-array</A></font><BR>
<center>
<br>&nbsp;

    <html:form action="logon.do" focus="username" target="_top">
        <table>
            <tr>
                <TD>User Name:</TD>
                <td><html:text property="username"/></td>
            </tr>
            <tr> 
                <td>Password:</td>
                <td><html:password property="password"/></td>
            </tr>
        </table>
        <p>
        <html:submit property="submit" value="continue"/>
    </html:form>
<br>
     <html:link forward="register" target="_top">User registration</html:link>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     <a href="mailto:yanhui_hu@hms.harvard.edu">email us</a>

<!--    
    <html:link forward="findRegistration" target="_top">Forgot your password?</html:link>
-->
<br><br>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td width="7%" valign="top"><font color="#FF0000"><b>Note:</b></font></td>
    <td width="93%" valign="top"><font size='2'> MedGene is freely available for all research
      purposes. For information on commercial use please contact 
      the Harvard Medical School Office of Technology Licensing at (617) 432-0922.</font></td>
  </tr>
</table>

    <br><br>

</center>
</td></tr></table>
</body>
</html>
