<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>
<%@ page import="edu.harvard.med.hip.flex.process.Result" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Enter Expression Plate Result </title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>

<script type="text/javascript">
function put()
{
    index = document.forms[0].pcrSelect.selectedIndex;

    for(i=0; i<document.forms[0].elements.length; i++){
        if(document.forms[0].elements[i].name.indexOf('pcrResult') != -1) {
            document.forms[0].elements[i].selectedIndex = index;
        }
    }
}

function put1()
{
    index = document.forms[0].floSelect.selectedIndex;

    for(i=0; i<document.forms[0].elements.length; i++){
        if(document.forms[0].elements[i].name.indexOf('floResult') != -1) {
            document.forms[0].elements[i].selectedIndex = index;
        }
    }
}

function put2()
{
    index = document.forms[0].proteinSelect.selectedIndex;

    for(i=0; i<document.forms[0].elements.length; i++){
        if(document.forms[0].elements[i].name.indexOf('proteinResult') != -1) {
            document.forms[0].elements[i].selectedIndex = index;
        }
    }
}

function put3()
{
    index = document.forms[0].restrictionSelect.selectedIndex;

    for(i=0; i<document.forms[0].elements.length; i++){
        if(document.forms[0].elements[i].name.indexOf('restrictionResult') != -1) {
            document.forms[0].elements[i].selectedIndex = index;
        }
    }
}

function put4()
{
    index = document.forms[0].colonySelect.selectedIndex;

    for(i=0; i<document.forms[0].elements.length; i++){
        if(document.forms[0].elements[i].name.indexOf('colonyResult') != -1) {
            document.forms[0].elements[i].selectedIndex = index;
        }
    }
}

function put5()
{
    index = document.forms[0].statusSelect.selectedIndex;

    for(i=0; i<document.forms[0].elements.length; i++){
        if(document.forms[0].elements[i].name.indexOf('cloneStatus') != -1) {
            document.forms[0].elements[i].selectedIndex = index;
        }
    }
}
</script>
</head>
<body>

<h2><bean:message key="flex.name"/> : Enter Expression Plate Result</h2>
<hr>
<html:errors/>
<p>
<table>
    <tr>
        <td class="prompt">Plate:</td>
        <td><bean:write name="newPlate"/></td>
    </tr>
</table>

<html:form action="/EnterExpressionResult.do" enctype="multipart/form-data">
<html:hidden property="newPlate"/>
<bean:define id="allSamples" name="newExpressionPlate" property="samples"/>
<p>
<TABLE border="1" cellpadding="2" cellspacing="0">
    <tr class="headerRow">
    <logic:present name="sampleid">
        <th>Sample Id</th>
        <html:hidden property="sampleid"/>
    </logic:present>
    <logic:present name="well">
        <th>Well</th>
        <html:hidden property="well"/>
    </logic:present>
    <logic:present name="geneSymbol">
        <th>Gene Symbol</th>  
        <html:hidden property="geneSymbol"/>
    </logic:present>
    <logic:present name="pa">
        <th>PA</th>  
        <html:hidden property="pa"/>
    </logic:present>
    <logic:present name="sgd">
        <th>SGD</th> 
        <html:hidden property="sgd"/>
    </logic:present>
    <logic:present name="masterClone">
        <th>Master Clone ID</th> 
        <html:hidden property="masterClone"/>
    </logic:present>  
    <logic:present name="colony">
        <th>Colony Result
            <select name="colonySelect" onchange="put4()">
                <option>NOT DONE
                <option>PASS
                <option>FAIL
            </select>
        </th>  
        <html:hidden property="colony"/>
    </logic:present>
    <logic:present name="pcr">
        <th>PRC Result         
            <select name="pcrSelect" onchange="put()">
                <option>NOT DONE
                <option>PASS
                <option>FAIL
            </select>
        </th>
        <html:hidden property="pcr"/>
    </logic:present> 
    <logic:present name="florescence">
        <th>Florescence Result
            <select name="floSelect"  onchange="put1()">
                <option>NOT DONE
                <option>PASS
                <option>FAIL
            </select>
        </th>
        <html:hidden property="florescence"/>
    </logic:present>
    <logic:present name="protein">
        <th>Protein Expression Result
        <select name="proteinSelect" onchange="put2()" >
                <option>NOT DONE
                <option>PASS
                <option>FAIL
            </select>
        </th> 
        <html:hidden property="protein"/>
    </logic:present>
    <logic:present name="restriction">
        <th>Restriction Digestion Result
            <select name="restrictionSelect"  onchange="put3()">
                <option>NOT DONE
                <option>PASS
                <option>FAIL
            </select>
        </th> 
        <html:hidden property="restriction"/>
    </logic:present> 
    <logic:present name="status">
        <th>Final Status
            <select name="statusSelect" onchange="put5()" >
                <option>IN PROCESS
                <option>SUCESSFUL
                <option>FAIL
            </select>
        </th>  
        <html:hidden property="status"/>
    </logic:present>
    <logic:present name="researcher">
        <th>Author</th>  
        <html:hidden property="researcher"/>
    </logic:present>
    <th>Start Date</th>  
    <th>Comments</th>  
    </tr>

    <logic:iterate id="sample" name="allSamples" indexId="i">
    <logic:equal name="sample" property="type" value="ISOLATE">
    <html:hidden property='<%="cloneid["+ i +"]" %>'/>
    <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
    <logic:present name="sampleid">
    <td>
        <bean:write name="sample" property="id"/>
    </td>
    </logic:present>
    <logic:present name="well">
    <td>
        <bean:write name="sample" property="position"/>
    </td>
    </logic:present>
    <logic:present name="geneSymbol">
    <td>
        <flex:write name="sample" property="geneSymbol"/>
    </td>
    </logic:present>
    <logic:present name="pa">
    <td>
        <flex:write name="sample" property="pa"/>
    </td>
    </logic:present>
    <logic:present name="sgd">
    <td>
        <flex:write name="sample" property="sgd"/>
    </td>
    </logic:present>
    <logic:present name="masterClone">
    <td>
        <bean:write name="sample" property="mastercloneid"/>
    </td>
    </logic:present>
    <logic:present name="colony">
    <td>
        <select name='<%="colonyResult["+ i +"]" %>'>                 
            <logic:equal name="enterResultForm" property='<%="colonyResult["+ i +"]" %>' value="NOT DONE">
                <option selected>NOT DONE
                <option>PASS
                <option>FAIL
            </logic:equal>
            <logic:equal name="enterResultForm" property='<%="colonyResult["+ i +"]" %>' value="PASS">
                <option>NOT DONE
                <option selected>PASS
                <option>FAIL
            </logic:equal>
            <logic:equal name="enterResultForm" property='<%="colonyResult["+ i +"]" %>' value="FAIL">
                <option>NOT DONE
                <option>PASS
                <option selected>FAIL
            </logic:equal>
        </select>  
    <a target="resulthistory" href="/FLEX/ExpressionResultHistory.do?well=<bean:write name="sample" property="position"/>&sampleid=<bean:write name="sample" property="id"/>&resulttype=<%=Result.EXPRESSION_COLONY %>&gene=<flex:write name="sample" property="geneSymbol"/>&plate=<bean:write name="newPlate"/>">history</a>
    </td>
    </logic:present>
    <logic:present name="pcr">
    <td>
        <select name='<%="pcrResult["+ i +"]" %>'>                            
            <logic:equal name="enterResultForm" property='<%="pcrResult["+ i +"]" %>' value="NOT DONE">
                <option selected>NOT DONE
                <option>PASS
                <option>FAIL
            </logic:equal>
            <logic:equal name="enterResultForm" property='<%="pcrResult["+ i +"]" %>' value="PASS">
                <option>NOT DONE
                <option selected>PASS
                <option>FAIL
            </logic:equal>
            <logic:equal name="enterResultForm" property='<%="pcrResult["+ i +"]" %>' value="FAIL">
                <option>NOT DONE
                <option>PASS
                <option selected>FAIL
            </logic:equal>
        </select>
    <a target="resulthistory" href="/FLEX/ExpressionResultHistory.do?well=<bean:write name="sample" property="position"/>&sampleid=<bean:write name="sample" property="id"/>&resulttype=<%=Result.EXPRESSION_PCR %>&gene=<flex:write name="sample" property="geneSymbol"/>&plate=<bean:write name="newPlate"/>">history</a>
    </td>
    </logic:present>
    <logic:present name="florescence">
    <td>
        <select name='<%="floResult["+ i +"]" %>'>                 
            <logic:equal name="enterResultForm" property='<%="floResult["+ i +"]" %>' value="NOT DONE">
                <option selected>NOT DONE
                <option>PASS
                <option>FAIL
            </logic:equal>
            <logic:equal name="enterResultForm" property='<%="floResult["+ i +"]" %>' value="PASS">
                <option>NOT DONE
                <option selected>PASS
                <option>FAIL
            </logic:equal>
            <logic:equal name="enterResultForm" property='<%="floResult["+ i +"]" %>' value="FAIL">
                <option>NOT DONE
                <option>PASS
                <option selected>FAIL
            </logic:equal>
        </select>
    <a target="resulthistory" href="/FLEX/ExpressionResultHistory.do?well=<bean:write name="sample" property="position"/>&sampleid=<bean:write name="sample" property="id"/>&resulttype=<%=Result.EXPRESSION_FLORESCENCE %>&gene=<flex:write name="sample" property="geneSymbol"/>&plate=<bean:write name="newPlate"/>">history</a>
    </td>
    </logic:present>
    <logic:present name="protein">
    <td>      
        <select name='<%="proteinResult["+ i +"]" %>' >                 
            <logic:equal name="enterResultForm" property='<%="proteinResult["+ i +"]" %>' value="NOT DONE">
                <option selected>NOT DONE
                <option>PASS
                <option>FAIL
            </logic:equal>
            <logic:equal name="enterResultForm" property='<%="proteinResult["+ i +"]" %>' value="PASS">
                <option>NOT DONE
                <option selected>PASS
                <option>FAIL
            </logic:equal>
            <logic:equal name="enterResultForm" property='<%="proteinResult["+ i +"]" %>' value="FAIL">
                <option>NOT DONE
                <option>PASS
                <option selected>FAIL
            </logic:equal>
        </select>
    <a target="resulthistory" href="/FLEX/ExpressionResultHistory.do?well=<bean:write name="sample" property="position"/>&sampleid=<bean:write name="sample" property="id"/>&resulttype=<%=Result.EXPRESSION_PROTEIN %>&gene=<flex:write name="sample" property="geneSymbol"/>&plate=<bean:write name="newPlate"/>">history</a>
    </td>
    </logic:present>
    <logic:present name="restriction">
    <td>  
        <select name='<%="restrictionResult["+ i +"]" %>' >                 
            <logic:equal name="enterResultForm" property='<%="restrictionResult["+ i +"]" %>' value="NOT DONE">
                <option selected>NOT DONE
                <option>PASS
                <option>FAIL
            </logic:equal>
            <logic:equal name="enterResultForm" property='<%="restrictionResult["+ i +"]" %>' value="PASS">
                <option>NOT DONE
                <option selected>PASS
                <option>FAIL
            </logic:equal>
            <logic:equal name="enterResultForm" property='<%="restrictionResult["+ i +"]" %>' value="FAIL">
                <option>NOT DONE
                <option>PASS
                <option selected>FAIL
            </logic:equal>
        </select>       
    <a target="resulthistory" href="/FLEX/ExpressionResultHistory.do?well=<bean:write name="sample" property="position"/>&sampleid=<bean:write name="sample" property="id"/>&resulttype=<%=Result.EXPRESSION_RESTRICTION %>&gene=<flex:write name="sample" property="geneSymbol"/>&plate=<bean:write name="newPlate"/>">history</a>            
    </td>
    </logic:present>
    <logic:present name="status">
    <td>
        <select name='<%="cloneStatus["+ i +"]" %>' >                 
            <logic:equal name="enterResultForm" property='<%="cloneStatus["+ i +"]" %>' value="IN PROCESS">
                <option selected>IN PROCESS
                <option>SUCESSFUL
                <option>FAIL
            </logic:equal>
            <logic:equal name="enterResultForm" property='<%="cloneStatus["+ i +"]" %>' value="SUCESSFUL">
                <option>IN PROCESS
                <option selected>SUCESSFUL
                <option>FAIL
            </logic:equal>
            <logic:equal name="enterResultForm" property='<%="cloneStatus["+ i +"]" %>' value="FAIL">
                <option>IN PROCESS
                <option>SUCESSFUL
                <option selected>FAIL
            </logic:equal>
        </select>  
    </td>
    </logic:present>
    <logic:present name="researcher">
    <td>
        <bean:write name="sample" property="author"/>
    </td>
    </logic:present>
    <td>
        <bean:write name="sample" property="startdate"/>
    </td>
    <td>
        <html:textarea property='<%="comments["+ i +"]" %>'/>
    </td>
    </flex:row>
    </logic:equal>
    </logic:iterate>
    <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">    
        <logic:present name="sampleid">
            <td>&nbsp;</td>
        </logic:present>
        <logic:present name="well">
            <td>&nbsp;</td>
        </logic:present>
        <logic:present name="geneSymbol">
            <td>&nbsp;</td>
        </logic:present>
        <logic:present name="pa">
            <td>&nbsp;</td>
        </logic:present>
        <logic:present name="sgd">
            <td>&nbsp;</td>
        </logic:present>
        <logic:present name="masterClone">
            <td>&nbsp;</td>
        </logic:present>  
        <logic:present name="colony">
            <td class="prompt">Upload file: <html:file property="colonyFilename" /></td> 
        </logic:present>    
        <logic:present name="pcr">
            <td class="prompt">Upload file: <html:file property="filename" /></td> 
        </logic:present>  
        <logic:present name="florescence">
            <td class="prompt">Upload file: <html:file property="floFilename" /></td> 
        </logic:present>  
        <logic:present name="protein">
            <td class="prompt">Upload file: <html:file property="proFilename" /></td> 
        </logic:present>  
        <logic:present name="restriction">
            <td class="prompt">Upload file: <html:file property="restrictFilename" /></td> 
        </logic:present>      
        <logic:present name="status">
            <td>&nbsp;</td>
        </logic:present>
        <logic:present name="researcher">
            <td>&nbsp;</td>
        </logic:present>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
    </flex:row>
</table>

<p><b>Existing files associated with this plate:</b></p>
<TABLE border="1" cellpadding="2" cellspacing="0">
    <tr class="headerRow">
        <th>File Name</th><th>File Type</th>
    </tr>
<logic:iterate id="file" name="newExpressionPlate" property="fileReferences">
    <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
        <td>
            <flex:linkFileRef name="file">
                <bean:write name="file" property="baseName"/>
            </flex:linkFileRef>
        </td>
        <td><bean:write name="file" property="fileType"/></td>
    </flex:row>
</logic:iterate>
</table>



<p>
<table>

    <tr>
    <td></td><td><html:submit property="submit" value="Submit"/></td>
    </tr>
</table>
</html:form>

</body>
</html>

