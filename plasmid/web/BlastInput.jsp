<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.blast.BlastWrapper" %> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>BLAST Search</title>
        <meta name='description' content='Search for specific plasmids by providing a sequence and using the BLAST algorithm to find corresponding plasmids.'>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
            <script type="text/javascript">
            function adjustAlignment(form)
            {
                var s = document.blastForm.sequence.value;
                s = trimAll(s);
                if(s.length<100 && document.blastForm.inputformat.value=="Sequence") {
                    document.blastForm.alength.value=s.length;
                } else {
                    document.blastForm.alength.value=100;
                }
            }
            
            function leftTrim(sString) 
                {
                while (sString.substring(0,1) == ' ')
                {
                sString = sString.substring(1, sString.length);
                }
                return sString;
            }

            function rightTrim(sString) 
            {
                while (sString.substring(sString.length-1, sString.length) == ' ')
                {
                sString = sString.substring(0,sString.length-1);
                }
                return sString;
            } 

            function trimAll(sString) 
            {
                while (sString.substring(0,1) == ' ')
                {
                sString = sString.substring(1, sString.length);
                }
                while (sString.substring(sString.length-1, sString.length) == ' ')
                {
                sString = sString.substring(0,sString.length-1);
                }
                return sString;  
            }
            
            </script>
     <link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
        <link href="boilerplate.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="respond.min.js"></script>
</head>
     <div class="gridContainer clearfix">

    
    <body>
        <jsp:include page="orderTitle.jsp" />
        <table id='content' width="100%" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="menu.jsp" />
                </td>--%>
                <td width="83%" align="left" valign="top">
                    <%--<jsp:include page="blastSearchTitle.jsp" />--%>
                    <html:form action="BlastSearch.do">
                    <html:errors/>
                        <table width="100%" border="0">
                            <tr><td class="formlabel">Input</td></tr>
                            <tr> 
                                
                                <td colspan="2">1. 
                                    <html:select property="program" styleClass="itemtext">
                                        <html:options collection="programs" property="name" labelProperty="value"/>
                                    </html:select>
                                    Choose Program <a href="blasthelp.html#program" target="blasthelp">[i]</a>
                                </td>
                            </tr>
                            <tr> 
                                
                                <td colspan="2">2. 
                                    <html:select property="database" styleClass="itemtext">
                                        <html:options name="dbs"/>
                                    </html:select>
                                    Choose Database
                                </td>
                            </tr>
                            <tr> 
                                <td colspan="2">3. 
                                    <html:select property="inputformat" styleClass="itemtext" onchange="adjustAlignment()">
                                        <html:options name="formats"/>
                                    </html:select>
                                    Choose Input Type <a href="blasthelp.html#input" target="blasthelp">[i]</a>
                                </td></tr>
                            <tr>
                                <td colspan="2">(Multiple sequences should be in FASTA format. Multiple Accession/GI numbers should be separated by white space)</td>
                            </tr>
                            <tr> 
                                <td colspan="2" class="itemtext">
                                    <html:textarea rows="10" cols="80" property="sequence" onkeyup="adjustAlignment()"/>
                                </td>
                            </tr>
                            <tr> 
                                <td colspan="2" class="itemtext">
                                    <html:checkbox property="isLowcomp">Filter low complexity regions</html:checkbox><a href="blasthelp.html#optimization" target="blasthelp">[i]</a>
                                </td>
                            </tr>
                        </table>
                        
                                <br>
                        <table width="100%" border="0">
                            <tr><td class="formlabel">Parameters <a href="blasthelp.html#parameters" target="blasthelp">[i]</a></td></tr>
                            <tr> 
                                <td class="text">1. 
                                    <html:text property="pid" styleClass="itemtext"/>Min % ID <a href="blasthelp.html#parameters" target="blasthelp">[i]</a>
                                </td></tr>
                            <tr><td>2. 
                                    <html:text property="alength" styleClass="itemtext"/>Min alignment length (nt) <a href="blasthelp.html#parameters" target="blasthelp">[i]</a>
                                </td>
                            </tr>
                        </table>
                        
                        <html:submit/>    <html:reset/>
                    </html:form>
                    
                </td>
            </tr>
        </table> 
    <jsp:include page="footer.jsp" /></body>
     </div>
</html>