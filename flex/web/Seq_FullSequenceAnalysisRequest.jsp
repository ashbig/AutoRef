<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
<script>

/*
Check required form elements script-

*/
<!--
function checkrequired(e)
{
	var pass=true; 
        var refseqid ;
        var giid;
// Identify the form

   f = e.form;
    // Loop over all elements in the form
    for (iElt=0; iElt < f.elements.length; iElt++)
   {
      currElt = f.elements[iElt];	
      if (currElt.name == 'REFSEQID') refseqid = currElt.value;
      if (currElt.name == 'GI') giid = currElt.value;

    }  // end loop
	if ( refseqid =='' && giid=='')
    	{
		pass=false;
	}
	if (!pass)
	{

            alert("Please enter sequence id or sequence GI number and then submit again!");
            return false;
	}
	else
            document.forms[0].submit();

}
-->
</script>
<head><title>JSP Page</title></head>
<body>

<h1>Sequence Analysis</h1>
<html:errors/> 
<html:form action="/Seq_SequenceAnalysis.do"> 
<hr>
<p>
<% 
String sequenceexists = (String) request.getAttribute("sequenceexists");
if ( sequenceexists == null ||  sequenceexists.equalsIgnoreCase("yes") )
{%>
<h3>Please enter referal sequence FLEX ID:</h3>
 <input name="REFSEQID" type="text"  value="" size="25">
<h3>Please enter GI number referal sequence:</h3>
 <input name="GI" type="text"  value="" size="25">
<%}
else
{%>

<h3>Please enter referal sequence FLEX ID:</h3>
 <input name="REFSEQID" type="text"  value="" size="25">
<h3>Please enter GI number referal sequence:</h3>
 <input name="GI" type="text"  value="" size="25">
<p>
<h3>Please enter experimental sequence:</h3>
<p>
<textarea name="FULLSEQUENCE" rows=6 cols=60></textarea>

<%}%>

  <p> 
    <input type="button" value="Submit" name="B1" onFocus="return checkrequired(this)">
   

</html:form> 

</body>
</html>
