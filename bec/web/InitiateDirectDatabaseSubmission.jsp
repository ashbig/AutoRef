 

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.mapping.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.oligo.*" %>
<%@ page import="edu.harvard.med.hip.bec.action_runners.*" %>
<html>

<head>

<title> <bean:message key="bec.name"/> : <%=Constants.JSP_TITLE%></title>
  
<%
        Object forwardName = null;
        if ( request.getAttribute("forwardName") != null)
        {
                forwardName = request.getAttribute("forwardName") ;
        }
        else
        {
                forwardName = request.getParameter("forwardName") ;
        }
	 Object title = null;
	 if (request.getAttribute(Constants.JSP_TITLE ) == null)
	 { 
	 	title =  request.getParameter(  Constants.JSP_TITLE  );
	}
	else
	{
		title = request.getAttribute( Constants.JSP_TITLE );
	}
	int forwardName_int = 0;
        if (forwardName instanceof String) forwardName_int = Integer.parseInt((String)forwardName);
        else if (forwardName instanceof Integer) forwardName_int = ((Integer) forwardName).intValue(); 
String page_html = "";
 
/*
String view =     "<tr><td colspan =2><div align='right'><b> <A HREF=''";
view += " onClick='window.open(' ";
view += edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION");
view += "DirectDatabaseCommunications.do?forwardName= ";

String after_forward_id=" ','newWndNt','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;'>";

String after_view ="</a></div>   <p><p><p><p></p>      <p></td></tr>";
  */
switch( forwardName_int)
{
            //settings database
   case -Constants.PROCESS_ADD_NEW_LINKER  : 
       {
            page_html = "<tr><td width='25%' bgColor='#b8c6ed'>  <b>Please enter Linker Name</b>  </td>";
	 page_html += "<td  bgColor='#b8c6ed'><input type='text' name='linkername' size='20' value=''></td></tr>";
 page_html += "<tr> 	<td  bgColor='#e4e9f8' nowrap ><strong>Please enter  Linker Sequence: </strong></td>";
page_html += "<td bgColor='#e4e9f8'> <input type='text' name='linkersequence' size='60' value=''> </td></tr>";

          break;}
   case -Constants.PROCESS_ADD_NEW_VECTOR  : 
       {
           StringBuffer st = new StringBuffer();
st.append("<tr><td>This page allows you to upload the<i> vector information </i>from XML file. ");
st.append("The <b>vector element</b> described by the following properties  name,   source,   type ( 1 for destination vector, 0 for master vector),");
st.append(" filename,filepath");
st.append("Each vector can be discribed by set of features. <b>Feature </b> description contains  feature name ('recombination site Lost attP2'), feature type ( 1 for added, 0  for remain, -1 for lost), feature description. ");
st.append("</td></tr> <tr>   <td  bgColor='#b8c6ed'>Please select the vector information file:");
st.append(" <input type='file' name='fileName' id='fileName' value=''>");
st.append("     [<a href='Help_VectorSampleFile.jsp'>sample file</a>]</td></tr></table>");
     page_html = st.toString();	
	
            break;	}
   case -Constants.PROCESS_ADD_NAME_TYPE  : 
   {
    //   page_html = view + Constants.PROCESS_VIEW_ALL_NAME_TYPE+ after_forward_id  + "View All available Name Types "+ after_view;
           page_html += "<tr><td bgColor='#b8c6ed'>Please enter the name type:</td><td  bgColor='#b8c6ed'> <input type='text' name='nametype' size='20' value=''></td></tr>";
           break;
   }
   case -Constants.PROCESS_ADD_SPECIES_DEFINITION  : 
   {
    //   page_html = view +">View All available Species "+ after_view;
       
           page_html += "<tr><td  bgColor='#b8c6ed'>Please enter the species name:</td><td  bgColor='#b8c6ed'> <input type='text' name='speciesname' size='20' value=''></td></tr>";
           page_html += "<tr><td  bgColor='#e4e9f8' >Please enter the species specific Id</td><td bgColor='#e4e9f8' >  <input type='text' name='speciesid' size='20' value=''></td></tr>";
	
            break;
   }
   case -Constants.PROCESS_ADD_PROJECT_DEFINITION  : 
    {
      //  page_html = view + Constants.PROCESS_VIEW_ALL_PROJECT_DEFINITION+ after_forward_id  + "View All available Project Definitions"+ after_view;
      
            page_html += "<tr><td  bgColor='#b8c6ed'>Please enter the Project name</td><td  bgColor='#b8c6ed'>  <input type='text' name='projectname' size='20' value=''></td></tr>";
	 page_html += "<tr><td  bgColor='#e4e9f8' >Please enter the Project code</td><td bgColor='#e4e9f8' >  <input type='text' name='projectcode' size='20' value='' width='1'></td></tr>";
	break;
    }
    case -Constants.PROCESS_ADD_NEW_CONNECTION_VECTOR_LINKER:
    {
        ArrayList vectors = (ArrayList) request.getAttribute("vectors");
        ArrayList primers = (ArrayList) request.getAttribute("primers");
         StringBuffer st = new StringBuffer(); Oligo primer = null;BioVector vector = null;
st.append( "<tr><td bgColor='#b8c6ed'>  Please select vector:  </td><td  bgColor='#b8c6ed'>");
st.append( "<select name ='vectorid' >");
for (int vector_count = 0; vector_count < vectors.size(); vector_count++)
    {
        vector = (BioVector)vectors.get(vector_count);
st.append( "<option selected value = '"+ vector.getId() +"'>"+ vector.getName());
}
st.append( "</selected> </td></tr>");
st.append( "<tr><td  bgColor='#e4e9f8'  >Please select primer:</td><td bgColor='#e4e9f8'> ");
st.append( "<select name ='primerid' >");
for (int primer_count = 0; primer_count < primers.size(); primer_count++)
    {
        primer = (Oligo) primers.get(primer_count);
st.append( "<option selected value = '"+ primer.getId() +"'>"+ primer.getName());
}
st.append( "</selected> </td></tr>");

st.append( "<tr><td  bgColor='#b8c6ed'  >Please select primer position: </td><td bgColor='#b8c6ed'> ");
st.append( "<select name ='primerposition' ><option selected value = '"+ Oligo.POSITION_5PRIME +"'>5 prime");
st.append("<option value = '"+ Oligo.POSITION_3PRIME +"'>3 prime</selected> </td></tr>");

st.append( "<tr><td  bgColor='#e4e9f8'  >Please select primer orientation </td><td bgColor='#e4e9f8'>");
st.append( "<select name ='primerorientation' ><option selected value = '"+ Oligo.ORIENTATION_SENSE +"'>Sense");
st.append("<option value = '"+ Oligo.ORIENTATION_ANTISENSE +"'>Antisense</selected> </td></tr>");
page_html = st.toString();
break;
    }
    case -Constants.PROCESS_ADD_NEW_COMMON_PRIMER :
    {
          StringBuffer st = new StringBuffer();
st.append( "<tr><td bgColor='#b8c6ed'>  Please enter Name:  </td><td  bgColor='#b8c6ed'><input type='text' name='primername' size='20' value=''></td></tr>");
st.append( "<tr><td  bgColor='#e4e9f8'  >Please enter Sequence:</td><td bgColor='#e4e9f8'> <input type='text' name='sequence' size='60' value=''> </td></tr>");
st.append( "<tr><td  bgColor='#b8c6ed'  >Please enter Tm: </td><td bgColor='#b8c6ed'> <input type='text' name='tm' size='20' value='0.0' onBlur= \";checkNumeric(this,0,100,'','',''); \"> </td></tr>");
st.append( "<tr><td  bgColor='#e4e9f8'  >Please select Type: </td><td bgColor='#e4e9f8'>");
st.append( "<select name ='primertype' ><option selected value = '"+ Oligo.TYPE_COMMON +"'> Common");
st.append("<option value = '"+ Oligo.TYPE_UNIVERSAL +"'>Universal ");
st.append("<option value = '" + Oligo.TYPE_VECTORSPECIFIC+"'>Vector specific</selected> </td></tr>");
page_html = st.toString();
break;
        }
   // outside submission
   case -Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  : 
   {
            StringBuffer st = new StringBuffer();
st.append("<tr><td>This page allows you to upload the<i> reference sequences information </i>from XML file. ");
st.append("The <b>sequence element</b> contains sequence id, species, CDS start, CDS stop, ");
st.append("cDNA source, chromosome and sequence properties. The sequence id is the unique identifier");
st.append(" for each sequence and will be used as 'user reference sequence id'. ");
st.append("Each sequence can be discribed by features. <b>Feature element</b> description contains  feature name type ('GenBank Accession'), feature name value (the GenBank accession number for 'GenBank Accession' name type), feature url and description. ");
st.append(" The name type field must be valid name types in the database. Please submit all name types prior to reference sequence submission.");
st.append("</td></tr> <tr>   <td  bgColor='#b8c6ed'>Please select the sequence file:");
st.append(" <input type='file' name='fileName' id='fileName' value=''>");
st.append("     [<a href='Help_SequenceSampleFile.jsp'>sample file</a>]</td></tr></table>");
    page_html = st.toString();	
	
            break;
   }
   case -Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : 
       {
        

break;}
}
%>

</head>

<body> <jsp:include page="NavigatorBar_Administrator.jsp" /> 
	<p>
<br>

<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>        <td >    <font color="#008000" size="5"><b> <%= title %> </b></font>
    <hr>    </td> </tr></table>

<div align="center">
  <center>  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%"><html:errors/></td>    </tr>  </table>
  </center></div>
  
<form name ="DirectDatabaseCommunications" action="DirectDatabaseCommunications.do" METHOD="POST" ENCTYPE="multipart/form-data" > 
<input name="forwardName" type="hidden" value="<%= forwardName %>" > 



 
<table border="0" cellpadding="10" cellspacing="2" width="74%" align=center>
  
  <%= page_html %>
 
</table>

<div align="center">   <p>     <input type="submit" value="Submit" ></DIV>
</form> 

<jsp:include page="Display_ArrayOfItems.jsp" />
</body>
</html>






 