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
<A HREF="http://www.hip.harvard.edu"><font size=4><b>Institute of Proteomics</b></font></A><BR>
<A HREF="http://www.hms.harvard.edu"><font size=4><b>Harvard Medical School</b></font></A><BR>

    <hr>
    <html:errors/>
    <p>  <br>
</center>
<A><font size=3>The accelerated pace of biological research and the advent of genomics and proteomics have resulted in the collection of prodigious amounts of data on diseases and the genes that play a role in them.  Computational methods are needed to assimilate this surfeit of information.  MedGene project was designed to develop the automated extraction of biomedical knowledge from Medline database to create a human gene-to-disease co-occurrence network for all named human genes and all human diseases by automated analysis of MeSH indexes, title and abstracts in over 11 million Medline records.  Statistical analysis was applied to score the association between a known human gene and a particular human disease based on the frequency that they were co-cited in Medline records.  Users can review most reported genes related to any human disease or genes shared by multiple diseases in ranking order and vice versa.</font></A>
<center>
    <html:form action="logon.do" focus="username" target="_top">
<br><br>
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
        <html:submit property="submit" value="Submit"/>
    </html:form>

     <html:link forward="register" target="_top">Customer registration</html:link>
    <br>
    <html:link forward="findRegistration" target="_top">Forgot your password?</html:link>
    <br><br>
    <a href="mailto:yanhui_hu@hms.harvard.edu">email us</a>
    <br><br>

</center>
</td></tr></table>
</body>
</html>
