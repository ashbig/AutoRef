<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.blast.BlastWrapper" %> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>PlasmID Database</title>
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
    </head>
    
    <body>
        <jsp:include page="orderTitle.jsp" />
        <table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="menu.jsp" />
                </td>
                <td width="83%" align="left" valign="top">
                    <jsp:include page="blastSearchTitle.jsp" />
                    <html:form action="BlastSearch.do">
                    <html:errors/>
                        <table width="100%" border="0">
                            <tr> 
                                <td width="20%" class="formlabel">Choose program: <a href="blasthelp.html#program" target="blasthelp">[i]</a></td>
                                <td>
                                    <html:select property="program" styleClass="itemtext">
                                        <html:options collection="programs" property="name" labelProperty="value"/>
                                    </html:select>
                                </td>
                            </tr>
                            <tr> 
                                <td width="20%" class="formlabel">Choose database:</td>
                                <td>
                                    <html:select property="database" styleClass="itemtext">
                                        <html:options name="dbs"/>
                                    </html:select>
                                </td>
                            </tr>
                            <tr> 
                                <td colspan="2" width="20%" class="formlabel">Enter sequence or Accession/GI:  <a href="blasthelp.html#input" target="blasthelp">[i]</a>
                                    <html:select property="inputformat" styleClass="itemtext" onchange="adjustAlignment()">
                                        <html:options name="formats"/>
                                    </html:select>
                                <br>(Multiple sequences should be in FASTA format. Multiple Accession/GI numbers should be separated by white space)
                                </td>
                            </tr>
                            <tr> 
                                <td colspan="2" class="itemtext">
                                    <html:textarea rows="10" cols="80" property="sequence" onkeyup="adjustAlignment()"/>
                                </td>
                            </tr>
                            <tr> 
                                <td width="20%" class="formlabel">Optimize for: <a href="blasthelp.html#optimization" target="blasthelp">[i]</a></td>
                                <td class="itemtext">
                                    <html:checkbox property="isLowcomp">Filter low complexity regions</html:checkbox>
                                </td>
                            </tr>
                        </table>
                        
                        <p class="formlabelitalic">Parameters <a href="blasthelp.html#parameters" target="blasthelp">[i]</a></p>
                        <table width="100%" border="0">
                            <tr> 
                                <td width="20%" class="text">Minumum percent (%) identity:</td>
                                <td class="itemtext">
                                    <html:text property="pid" styleClass="itemtext"/>%
                                </td>
                            </tr>
                            <tr> 
                                <td width="20%" class="text">Minimum alignment length:</td>
                                <td>
                                    <html:text property="alength" styleClass="itemtext"/>nt
                                </td>
                            </tr>
                        </table>
                        
                        <html:submit/>    <html:reset/>
                    </html:form>
                    
                </td>
            </tr>
        </table> 
    <jsp:include page="footer.jsp" /></body>
</html>