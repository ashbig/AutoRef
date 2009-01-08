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
                    
                        <table width="100%" border="0">
                            <tr> 
                                <td width="20%" class="formlabel">Choose program:</td>
                                <td>
                                    <html:select property="program" styleClass="itemtext">
                                        <html:options name="programs"/>
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
                                <td colspan="2" width="20%" class="formlabel">Enter sequence or Accession/GI: 
                                    <html:select property="inputformat" styleClass="itemtext">
                                        <html:options name="formats"/>
                                    </html:select>
                                <br>(Multiple sequences should be in FASTA format. Multiple Accession/GI numbers should be separated by white space)
                                </td>
                            </tr>
                            <tr> 
                                <td colspan="2" class="itemtext">
                                    <html:textarea rows="10" cols="80" property="sequence"/>
                                </td>
                            </tr>
                            <tr> 
                                <td width="20%" class="formlabel">Optimize for: </td>
                                <td class="itemtext">
                                    <html:checkbox property="isMegablast">Highly similar sequences (megablast)</html:checkbox>
                                </td>
                            </tr>
                            <tr> 
                                <td width="20%" class="formlabel">&nbsp;</td>
                                <td class="itemtext">
                                    <html:checkbox property="isLowcomp">Filter low complexity regions</html:checkbox>
                                </td>
                            </tr>
                            <tr> 
                                <td width="20%" class="formlabel">&nbsp;</td>
                                <td class="itemtext">
                                    <html:checkbox property="isMaskLowercase">Mask lower case letters </html:checkbox>
                                </td>
                            </tr>
                        </table>
                        
                        <p class="formlabelitalic">Parameters</p>
                        <table width="100%" border="0">
                            <tr> 
                                <td width="20%" class="text">Max target sequences:</td>
                                <td>
                                    <html:select property="maxseqs" styleClass="itemtext">
                                        <html:options name="seqs"/>
                                    </html:select>
                                </td>
                            </tr>
                            <tr> 
                                <td width="20%" class="text">Expect threshold:</td>
                                <td>
                                    <html:text property="expect" styleClass="itemtext"/>
                                </td>
                            </tr>
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