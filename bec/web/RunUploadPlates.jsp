<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.flex.seqprocess.spec.*" %>
<%@ page import="edu.harvard.med.hip.flex.seqprocess.core.oligo.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>

<head>

<title>Primer Calculating Parameters</title>

<link href="FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body background='background.gif'>
<h2><bean:message key="bec.name"/> : Upload plates</h2>
<hr>

<html:errors/>
<html:form action="/RunProcess.do" >  
<input name="forwardName" type="hidden" value="<%=forwardName%>" > 
<h3 >&nbsp;</h3>

<p><i>If you are not sure about certain settings, please, consult help </i> <a href="Help_SequenceEvaluation.html">[parameter 
  help file]</a>.</b> </p>


<p>&nbsp;</p>

<p><strong>Upload plate information: 
  <input name="platefileinfo" type="text" id="platefileinfo">
  <input type="submit" name="Submit" value="Browse...">
  </strong> 
<p><strong>Next step</strong> 
  <select name="nextstep">
    <option selected value=1>Run end reads</option>
    <option value=2>Run clone evaluation</option>
  </select>
  <strong> </strong> 
<h3>Common parameters</h3>
<table border=0 cellspacing="4" width="80%" align="center">
  <tr> 
    <td  background="barbkgde.gif" width="50%"> <b>Vector trimming sequence</b> 
    </td>
    <TD background="barbkgde.gif" > 
    <html:select property="VECTOR">
    <html:options
            collection="vectors"
            property="id"
            labelProperty="name"
        /> 
    </html:select> 
       
      </div></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><b>5' clone tail seqment</b></td>
    <TD background="barbkgde.gif"> 
     <html:select property="5TAIL"> 
     <html:options
            collection="tails"
            property="id"
            labelProperty="name"
        /> 
     </html:select> 
      </div></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"> <b>3' clone tail seqment</b></td>
    <TD background="barbkgde.gif">
       <html:select property="3TAIL"> <html:options
            collection="tails"
            property="id"
            labelProperty="name"
        /> </html:select> 
      
      </div></td>
  </tr>
</table>
<p> 
<p> 
<div align="center"> 
  <p> 
    <input type="submit" value="Submit" name="B1">
    &nbsp; 
    <input type="reset" value="Reset" name="B2">
</div>
</html:form> 
</body>
</html>


