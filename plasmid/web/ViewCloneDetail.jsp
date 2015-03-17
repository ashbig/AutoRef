<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.coreobject.RefseqNameType" %> 

<html>
<head>
    <title><logic:iterate id="insert" name="clone" property="inserts"><bean:write name="insert" property="name"/></logic:iterate> in <bean:write name="clone" property="vector.name"/></title>
    <meta name="description" content="Details about the <logic:iterate id="insert" name="clone" property="inserts"><bean:write name="insert" property="name"/></logic:iterate> plasmid (cDNA, ORF, or shRNA); including sequence, author, publications, and links to public references."</meta>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="dnacore.ico">
</head>
<div class="gridContainer clearfix">
    
<jsp:include page="signinMenuBar.jsp" />    
<div class="content">

    <p class="mainbodytexthead">Clone: <bean:write name="clone" property="name"/></p>
    <div id="content">
    <table width="100%" style='max-width: 67em;' border="0">
        <colgroup><col width='150px'><col width='auto'></colgroup>
        <tr><td class="tablebody">Clone ID:</td><td class="tableinfo"><bean:write name="clone" property="name"/></td></tr>
        <tr><td class="tablebody">Is Verified:</td><td class="tableinfo"><bean:write name="clone" property="verified"/></td></tr>
        <tr><td class="tablebody">Status:</td><td class="tableinfo"><bean:write name="clone" property="status"/></td></tr>
        <tr><td class="tablebody">Source:</td><td class="tableinfo"><bean:write name="clone" property="source"/></td></tr>
        <tr><td class="tablebody">Description:</td><td class="tableinfo"><bean:write name="clone" property="description"/></td></tr>
        <tr><td class="tablebody">Comments:</td><td class="tableinfo"><bean:write name="clone" property="comments"/></td></tr>
        <tr><td class="tablebody">Type:</td><td class="tableinfo"><bean:write name="clone" property="type"/></td></tr>            
        <tr><td class="tablebody">Verification Method:</td><td class="tableinfo"><bean:write name="clone" property="vermethod"/></td></tr>        
        <tr><td class="tablebody">Distribution:</td><td class="tableinfo"><bean:write name="clone" property="restriction"/></td></tr>
        <tr><td class="tablebody">Special MTA:</td><td class="tableinfo"><bean:write name="clone" property="specialtreatment"/></td></tr>        
        </table>
    </div>
    <div id='content'style="margin-top: 0px; padding-top: 0px;">
    <table border="0" style='max-width: 67em;'>
        <colgroup><col width='150px'><col width='auto'></colgroup>        
        <tr><td class="tablebody">Map:</td><td class="tableinfo"><a target="blank" href="../PlasmidRepository/file/map/<bean:write name="clone" property="clonemap"/>"><bean:write name="clone" property="clonemap"/></a></td></tr>
        </table>
    </div>        
<div id="content">
    <logic:present name="clone" property="names">
        <p class="mainbodytext">Related Identifiers:</p>
    <table border="0" width="100%" style='max-width: 67em;'>
        <colgroup><col width='150px'><col width='auto'></colgroup>
            <logic:iterate id="clonename" name="clone" property="names">
                <tr>
                    <td class="tablebody"><bean:write name="clonename" property="type"/></td>
                    <td class="tableinfo">
                        <logic:notEqual name="clonename" property="urlLength" value="0">
                            <a target="_blank" href="<bean:write name="clonename" property="url"/>">
                            </logic:notEqual>
                            <bean:write name="clonename" property="value"/>
                            <logic:notEqual name="clonename" property="urlLength" value="0">
                            </a>
                        </logic:notEqual>
                    </td>
                </tr>
            </logic:iterate>
        </table>
    </logic:present>
       
    
    <logic:present name="clone" property="properties">
        <p class="mainbodytext">Property:</p>
    <table border="0" width="100%" style='max-width: 67em;'>
            <logic:iterate id="p" name="clone" property="properties">
                <tr>
                    <td width='150px' class="tablebody"><bean:write name="p" property="type"/>:</td>
                    <td class="tableinfo"><bean:write name="p" property="value"/></td>
                    <td class="tableinfo"><bean:write name="p" property="extrainfo"/></td>
                </tr>
            </logic:iterate>
        </table>
    </logic:present>
    
    <logic:present name="clone" property="inserts">
        <p class="mainbodytext">Insert Information:</p>
    <table border="0" style='max-width: 67em;' id="cart">
            <tr> 
                <td class="tablebody">Insert</td>
                <td class="tablebody">Size (bp)</td>
                <td class="tablebody">Species</td>
                <!--<td class="tablebody">Mutation</td>
                <td class="tablebody">Discrepancy</td>-->
                <td class="tablebody">Format</td>
                <td class="tablebody">Tissue Source</td>
                <td class="tablebody">Species Specific ID</td>
                <td class="tablebody">Gene Symbol</td>
                <td class="tablebody">Gene Name</td>
                <td class="tablebody">Target Genbank</td>
                <td class="tablebody">Keyword</td>
            </tr>
            <logic:iterate id="insert" name="clone" property="inserts">
                <tr class="tableinfo"> 
                    <td class="tableinfo"><bean:write name="insert" property="order"/></td>
                    <td class="tableinfo"><bean:write name="insert" property="size"/></td>
                    <td class="tableinfo"><bean:write name="insert" property="species"/></td>
                    <%--<td class="tableinfo"><bean:write name="insert" property="hasmutation"/></td>
                    <td class="tableinfo"><bean:write name="insert" property="hasdiscrepancy"/></td>--%>
                    <td class="tableinfo"><bean:write name="insert" property="format"/></td>
                    <td class="tableinfo"><bean:write name="insert" property="source"/></td>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.GENEID%>">
                        <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=Retrieve&dopt=Graphics&list_uids=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                    </logic:equal>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.PA%>">
                        <td><a target="_blank" href="http://www.pseudomonas.com/AnnotationByPAU.asp?PA=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                    </logic:equal>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.SGD%>">
                        <td><a target="_blank" href="http://db.yeastgenome.org/cgi-bin/locus.pl?locus=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                    </logic:equal>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.GENBANK%>">
                        <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/nuccore/<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                    </logic:equal>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.PRO_GI%>">
                        <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/protein/<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                    </logic:equal>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.FBID%>">
                        <td><a target="_blank" href="http://www.flybase.org/.bin/fbidq.html?<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                    </logic:equal>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.WBGENEID%>">
                        <td><a target="_blank" href="http://www.wormbase.org/db/gene/gene?name=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                    </logic:equal>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.TAIR%>">
                        <td><a target="_blank" href="http://arabidopsis.org/servlets/TairObject?type=locus&name=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                    </logic:equal>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.LOCUS_TAG%>">
                        <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/sites/entrez?db=gene&cmd=&term=<bean:write name="insert" property="geneid"/>&go=Go"><bean:write name="insert" property="geneid"/></a></td>
                    </logic:equal>
                    <td class="tableinfo"><bean:write name="insert" property="name"/></td>
                    <td class="tableinfo"><bean:write name="insert" property="description"/></td>
                    <td class="tableinfo"><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="targetseqidForNCBI"/>"><bean:write name="insert" property="targetgenbank"/></a></td>
                    <td class="tableinfo"><bean:write name="insert" property="annotation"/></td>
                </tr>
            </logic:iterate>
        </table>
        
        <table  width="100%" style='max-width: 67em;' border="0" id="mobilehome">
                    <colgroup><col width='150px'><col width='auto'></colgroup>
            <logic:iterate id="insert" name="clone" property="inserts">
                <tr><td class="tablebody">Insert</td><td class="tableinfo"><bean:write name="insert" property="order"/></td></tr>
                <tr><td class="tablebody">Size (bp)</td><td class="tableinfo"><bean:write name="insert" property="size"/></td></tr>
                <tr><td class="tablebody">Species</td><td class="tableinfo"><bean:write name="insert" property="species"/></td><tr>
                    <%--<tr><td class="tablebody">Mutation</td><td class="tableinfo"><bean:write name="insert" property="hasmutation"/></td></tr>
                    <tr><td class="tablebody">Discrepancy</td><td class="tableinfo"><bean:write name="insert" property="hasdiscrepancy"/></td></tr>--%>
                <tr><td class="tablebody">Format</td><td class="tableinfo"><bean:write name="insert" property="format"/></td></tr>
                <tr><td class="tablebody">Tissue Source</td><td class="tableinfo"><bean:write name="insert" property="source"/></td></tr>
                <tr><td class="tablebody">Species Specific ID</td>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.GENEID%>">
                        <td class="tableinfo"><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=Retrieve&dopt=Graphics&list_uids=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                    </logic:equal>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.PA%>">
                        <td class="tableinfo"><a target="_blank" href="http://www.pseudomonas.com/AnnotationByPAU.asp?PA=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                    </logic:equal>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.SGD%>">
                        <td class="tableinfo"><a target="_blank" href="http://db.yeastgenome.org/cgi-bin/locus.pl?locus=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                    </logic:equal>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.GENBANK%>">
                        <td class="tableinfo"><a target="_blank" href="http://www.ncbi.nlm.nih.gov/nuccore/<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                    </logic:equal>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.PRO_GI%>">
                        <td class="tableinfo"><a target="_blank" href="http://www.ncbi.nlm.nih.gov/protein/<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                    </logic:equal>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.FBID%>">
                        <td class="tableinfo"><a target="_blank" href="http://www.flybase.org/.bin/fbidq.html?<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                    </logic:equal>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.WBGENEID%>">
                        <td class="tableinfo"><a target="_blank" href="http://www.wormbase.org/db/gene/gene?name=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                    </logic:equal>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.TAIR%>">
                        <td class="tableinfo"><a target="_blank" href="http://arabidopsis.org/servlets/TairObject?type=locus&name=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                    </logic:equal>
                    <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.LOCUS_TAG%>">
                        <td class="tableinfo"><a target="_blank" href="http://www.ncbi.nlm.nih.gov/sites/entrez?db=gene&cmd=&term=<bean:write name="insert" property="geneid"/>&go=Go"><bean:write name="insert" property="geneid"/></a></td>
                        </logic:equal></tr>
                <tr><td class="tablebody">Gene Symbol</td><td class="tableinfo"><bean:write name="insert" property="name"/></td></tr>
                <tr><td class="tablebody">Gene Name</td><td class="tableinfo"><bean:write name="insert" property="description"/></td></tr>
                <tr><td class="tablebody">Target Genbank</td><td class="tableinfo"><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="targetseqidForNCBI"/>"><bean:write name="insert" property="targetgenbank"/></a></td></tr>
                <tr><td class="tablebody">Keyword</td><td class="tableinfo"><bean:write name="insert" property="annotation"/></td></tr>
 
            </logic:iterate>
        </table>
        
        <p class="mainbodytext">Insert Sequence:</p>
        <logic:iterate id="insert" name="clone" property="inserts">
            <p>Insert: <bean:write name="insert" property="order"/></p>
            <p><pre><bean:write name="insert" property="fastaSequence"/></pre></P>
        </logic:iterate>
        </table>
        
        <logic:iterate id="insert" name="clone" property="inserts">
            <logic:present name="insert" property="properties">
                <p class="mainbodytext">Insert Property: Insert <bean:write name="insert" property="order"/></p>
    <table border="0" style='max-width: 67em;'>
        <colgroup><col width='50%'><col width='50%'><col width='100%'></colgroup>          
                    <tr> 
                        <td class="tablebody">Type</td>
                        <td class="tablebody">Value</td>
                        <td class="tablebody">Extra Information</td>
                    </tr>
                    <logic:iterate id="p" name="insert" property="properties">
                        <tr> 
                            <td class="tableinfo"><bean:write name="p" property="type"/></td>
                            <td class="tableinfo"><bean:write name="p" property="value"/></td>
                            <td class="tableinfo"><bean:write name="p" property="extrainfo"/></td>
                        </tr>
                    </logic:iterate>
                </table>
            </logic:present>
        </logic:iterate>
    </logic:present>
    
    <logic:present name="clone" property="vector">
        <p class="mainbodytext">Vector Information:</p>
    <table border="0" style='max-width: 67em;'>
        <colgroup><col width='150px'><col width='auto'></colgroup>    
            <tr><td class="tablebody">Vector Name:</td><td class="tableinfo"><a href="GetVectorDetail.do?vectorid=<bean:write name="clone" property="vector.vectorid"/>"><bean:write name="clone" property="vector.name"/></a></td></tr>
            <tr><td class="tablebody">Type:</td><td class="tableinfo"><bean:write name="clone" property="vector.type"/></td></tr>
            <tr><td class="tablebody">Description:</td><td class="tableinfo"><bean:write name="clone" property="vector.description"/></td></tr>
            <tr><td class="tablebody">Properties:</td><td class="tableinfo"><bean:write name="clone" property="vector.propertyString"/></td></tr>
            <tr><td class="tablebody">Comments:</td><td class="tableinfo"><bean:write name="clone" property="vector.comments"/></td></tr>
            <tr><td class="tablebody">Map:</td><td class="tableinfo"><a target="blank" href="../PlasmidRepository/file/map/<bean:write name="clone" property="vector.mapfilename"/>"><bean:write name="clone" property="vector.mapfilename"/></a></td></tr>
            <tr><td class="tablebody">Sequence:</td><td class="tableinfo"><a target="blank" href="../PlasmidRepository/file/sequence/<bean:write name="clone" property="vector.seqfilename"/>"><bean:write name="clone" property="vector.seqfilename"/></a></td></tr>
            <tr><td class="tablebody">Size (bp):</td><td class="tableinfo"><bean:write name="clone" property="vector.size"/></td></tr>
            <tr><td class="tablebody">Form:</td><td class="tableinfo"><bean:write name="clone" property="vector.form"/></td></tr>
        </table>
    </logic:present>
    
    <logic:present name="clone" property="hosts">
        <p class="mainbodytext">Host Information:</p>
    <table border="0" style='max-width: 67em;'>
            <tr>
                <td width='25%' class="tablebody">Host Strain</td>
                <td width='75%' class="tablebody">Is Used In Distribution</td>
                <td width='100%' class="tablebody">Description</td>
            </tr>
            <logic:iterate id="host" name="clone" property="hosts">
                <tr>
                    <td class="tableinfo"><bean:write name="host" property="hoststrain"/></td>
                    <td class="tableinfo"><bean:write name="host" property="isinuse"/></td>
                    <td class="tableinfo"><bean:write name="host" property="description"/></td>
                </tr>
            </logic:iterate>
        </table>
    </logic:present>
    
    <logic:present name="clone" property="selections">
        <p class="mainbodytext">Antibiotic Selections:</p>
    <table border="0" width='100%' style='max-width: 67em;'>
            <tr>
                <td class="tablebody">Host Type</td>
                <td class="tablebody">Marker</td>
            </tr>
            <logic:iterate id="selection" name="clone" property="selections">
                <tr>
                    <td class="tableinfo"><bean:write name="selection" property="hosttype"/></td>
                    <td class="tableinfo"><bean:write name="selection" property="marker"/></td>
                </tr>
            </logic:iterate>
        </table>
    </logic:present>
    
    <logic:present name="clone" property="recommendedGrowthCondition">
        <p class="mainbodytext">Recommended Growth Condition:</p>
     <table border="0" width="100%" style='max-width: 67em;'>
            <tr style="background:#dddddd; vertical-align: top;">
                <td class="tablebody">Host Type</td>
                <td class="tablebody">Selection Condition</td>
                <td id='extrainfo' class="tablebody">Growth Condition</td>
                <td class="tablebody">Comments</td>
            </tr>
            <tr>
                <td class="tableinfo"><bean:write name="clone" property="recommendedGrowthCondition.hosttype"/></td>
                <td class="tableinfo"><bean:write name="clone" property="recommendedGrowthCondition.selection"/></td>
                <td id='extrainfo' class="tableinfo"><bean:write name="clone" property="recommendedGrowthCondition.condition"/></td>
                <td class="tableinfo"><bean:write name="clone" property="recommendedGrowthCondition.comments"/></td>
            </tr>
        </table>
    </logic:present>
    
    <logic:present name="clone" property="authors">
        <p class="mainbodytext">Authors:</p>
    <table border="0" width="100%" style='max-width: 67em;'>
       
            <tr>
                <td width="150px" class="tablebody">Author Name</td>
                <td class="tablebody">Author Type</td>
            </tr>
            <logic:iterate id="author" name="clone" property="authors">
                <tr>
                    <td class="tableinfo"><bean:write name="author" property="authorname"/></td>
                    <td class="tableinfo"><bean:write name="author" property="authortype"/></td>
                </tr>
            </logic:iterate>
        </table>
    </logic:present>
    <logic:present name="clone" property="publications">
        <p class="mainbodytext">Publications:</p>
    <table border="0" width="100%" style='max-width: 67em;'>
            <tr>
                <td class="tablebody">PMID</td>
                <td class="tablebody">Title</td>
                           </tr>
            <logic:iterate id="publication" name="clone" property="publications">
                <tr>

                    <td class="tableinfo"><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Search&db=pubmed&term=<bean:write name="publication" property="pmid"/>"><bean:write name="publication" property="pmid"/></a></td>
                    <td class="tableinfo"><bean:write name="publication" property="title"/></td>
                </tr>
            </logic:iterate>
        </table>
    </logic:present>
        <br></br>
        </div> 
</div>
</body>
</div>
</html>
