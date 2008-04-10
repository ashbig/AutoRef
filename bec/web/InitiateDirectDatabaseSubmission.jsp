<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
 

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
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>
<% String redirection = edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") ;%>
<title>  <%=Constants.JSP_TITLE%></title>
  
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
	 int forwardName_int = 0;
        if (forwardName instanceof String) forwardName_int = Integer.parseInt((String)forwardName);
        else if (forwardName instanceof Integer) forwardName_int = ((Integer) forwardName).intValue(); 
String page_html = "";
 

switch( forwardName_int)
{
            //settings database
   case -Constants.PROCESS_ADD_NEW_LINKER  : 
       {
            page_html = "<tr><td width='25%' bgColor='#b8c6ed'>Please enter linker name:  </td>";
	 page_html += "<td  bgColor='#b8c6ed'><input type='text' name='linkername' size='20' value=''></td></tr>";
 page_html += "<tr> 	<td  bgColor='#e4e9f8' nowrap >Please enter linker sequence (sense direction):</td>";
page_html += "<td bgColor='#e4e9f8'> <input type='text' name='linkersequence' size='60' value=''> </td></tr>";
page_html += "<tr><td colspan=2 ><i><P><b>Linker</b> is vector sequence up-stream and/or down-stream of the insert sequence that is important for clone function and thus, user wishes to sequence verify. <b>Example:</b> 5' lox-P site for the creator clones would be defined as the linker.  </td></tr>";

          break;}
   case -Constants.PROCESS_ADD_NEW_VECTOR  : 
       {
           StringBuffer st = new StringBuffer();
st.append("<tr><td>This page allows you to upload the<i> vector information </i>from XML file. ");
st.append("The <b>vector element</b> is described by the following properties:  name,   source,   type (1 for destination vector, 0 for master vector),");
st.append(" filename, filepath. ");
st.append("Each vector can be described by a set of features. <b>Feature </b> description contains:  feature name ('recombination site'), feature property (1 for added, 0  for remain, -1 for lost when insert is integrated), feature description. ");
st.append("</td></tr> <tr>   <td  bgColor='#b8c6ed'>Please select the vector information file:");
st.append(" <input type='file' name='fileName' id='fileName' value=''>");
st.append("     [<a href='"+redirection +"help/help_vector_xml_format.html'>sample template file</a>]");
st.append("     [<a href='"+ redirection+"help/help_vector_submission.html'>sample file</a>]</td></tr>");
     page_html = st.toString();	
	
            break;	}
   case -Constants.PROCESS_ADD_NAME_TYPE  : 
   {
    //   page_html = view + Constants.PROCESS_VIEW_ALL_NAME_TYPE+ after_forward_id  + "View All available Name Types "+ after_view;
           page_html += "<tr><td bgColor='#b8c6ed'>Please enter reference sequence annotation type:</td><td  bgColor='#b8c6ed'> <input type='text' name='nametype' size='20' value=''></td></tr>";
           page_html += "<tr><td > <P><i>Annotation type is the reference annotation type. Example: GI, SGD</i></td><tr>";
break;
   }
   case -Constants.PROCESS_ADD_SPECIES_DEFINITION  : 
   {
    //   page_html = view +">View All available Species "+ after_view;
       
           page_html += "<tr><td  bgColor='#b8c6ed'>Please enter species name:</td><td  bgColor='#b8c6ed'> <input type='text' name='speciesname' size='20' value=''></td></tr>";
           page_html += "<tr><td  bgColor='#e4e9f8' >Please enter species specific identifier (optional):</td><td bgColor='#e4e9f8' >  <input type='text' name='speciesid' size='20' value=''></td></tr>";
	
            break;
   }
   case -Constants.PROCESS_ADD_PROJECT_DEFINITION  : 
    {
      //  page_html = view + Constants.PROCESS_VIEW_ALL_PROJECT_DEFINITION+ after_forward_id  + "View All available Project Definitions"+ after_view;
      
            page_html += "<tr><td  bgColor='#b8c6ed'>Please enter project name:</td><td  bgColor='#b8c6ed'>  <input type='text' name='"+UI_Constants.PROJECT_NAME +"' size='20' value=''></td></tr>";
	 page_html += "<tr><td  bgColor='#e4e9f8' >Please enter project description:</td><td bgColor='#e4e9f8' >  <textarea name='"+UI_Constants.PROJECT_DESCRIPTION +"' cols='20' rows='6'></textarea></td></tr>";
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
st.append( "<tr><td  bgColor='#e4e9f8'  >Please select sequencing primer:</td><td bgColor='#e4e9f8'> ");
st.append( "<select name ='primerid' >");
for (int primer_count = 0; primer_count < primers.size(); primer_count++)
    {
        primer = (Oligo) primers.get(primer_count);
st.append( "<option selected value = '"+ primer.getId() +"'>"+ primer.getName());
}
st.append( "</selected> </td></tr>");

st.append( "<tr><td  bgColor='#b8c6ed'  >Please select sequencing primer position: </td><td bgColor='#b8c6ed'> ");
st.append( "<select name ='primerposition' ><option selected value = '"+ Oligo.POSITION_5PRIME +"'>5 prime");
st.append("<option value = '"+ Oligo.POSITION_3PRIME +"'>3 prime</selected> </td></tr>");

st.append( "<tr><td  bgColor='#e4e9f8'  >Please select sequencing primer orientation </td><td bgColor='#e4e9f8'>");
st.append( "<select name ='primerorientation' ><option selected value = '"+ Oligo.ORIENTATION_SENSE +"'>Sense");
st.append("<option value = '"+ Oligo.ORIENTATION_ANTISENSE +"'>Antisense</selected> </td></tr>");
page_html = st.toString();
break;
    }
    case -Constants.PROCESS_ADD_NEW_COMMON_PRIMER :
    {
          StringBuffer st = new StringBuffer();
st.append( "<tr><td bgColor='#b8c6ed'>  Please enter primer name:  </td><td  bgColor='#b8c6ed'><input type='text' name='primername' size='20' value=''></td></tr>");
st.append( "<tr><td  bgColor='#e4e9f8'  >Please enter sequence:</td><td bgColor='#e4e9f8'> <input type='text' name='sequence' size='60' value=''> </td></tr>");
st.append( "<tr><td  bgColor='#b8c6ed'  >Please enter Tm (optional): </td><td bgColor='#b8c6ed'> <input type='text' name='tm' size='20' value='0.0' onBlur= \";checkNumeric(this,0,100,'.','',''); \"> </td></tr>");
st.append( "<tr><td  bgColor='#e4e9f8'  >Please select type: </td><td bgColor='#e4e9f8'>");
st.append( "<select name ='primertype' >");
st.append("<option value = '"+ Oligo.TYPE_UNIVERSAL +"'>Universal ");
st.append("<option value = '" + Oligo.TYPE_VECTORSPECIFIC+"'>Vector specific</selected> </td></tr>");
page_html = st.toString();
break;
        }
    
    case Constants.PROCESS_VERIFY_TRACE_FILE_FORMAT:
        {
           
break;
            }
    
    case -Constants.PROCESS_ADD_NEW_CLONINGSTRATEGY:
    {
         
break;
        }
   // outside submission
   case -Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  : 
   {
            StringBuffer st = new StringBuffer();
st.append("<tr><td>This page allows you to upload the<i> reference sequences information </i>from XML file. ");
st.append("The <b>sequence element</b> contains: <b>sequence id</b>, <b>sequence</b>, species, <b>CDS start</b>, <b>CDS stop</b>, ");
st.append("cDNA source, chromosome and sequence properties (terms in bold are required). The sequence id is the unique identifier");
st.append(" for each sequence and will be used as 'user reference sequence id'. ");
st.append("Each sequence can be described by features. <b>The feature element</b> description contains:  feature name type ('GenBank Accession'), feature name value (the GenBank accession number for 'GenBank Accession' name type), feature url and description. ");
st.append(" The annotation type field must use valid annotation types from the database. Please define all annotation types prior to reference sequence submission.");
st.append("</td></tr> <tr>   <td  bgColor='#b8c6ed'>Please select the sequence file:");
st.append(" <input type='file' name='fileName' id='fileName' value=''>");
st.append("     [<a href='"+ redirection+"help/help_reference_sequence_xml_format.html'>sample file</a>]</td></tr></table>");
    page_html = st.toString();	
	
            break;
   }
   case -Constants.PROCESS_SUBMIT_CLONE_SEQUENCES:  
   {
            StringBuffer st = new StringBuffer();
st.append("<tr><td>This page allows you to upload the<i> clone sequences information </i>from FASTA format file. ");
st.append("This meant for clones where trace files will not be uploaded. The <b>sequence ID MUST</b> be ");
st.append("clone ID. You can submit several sequences per clone; however, only the last one will be analyzed by ACE.");
st.append("</td></tr> <tr>   <td  bgColor='#b8c6ed'>Please select the sequence file:");
st.append(" <input type='file' name='fileName' id='fileName' value=''>");
st.append("     [<a href='"+ redirection+"help/help_clone_sequence_xml_format.html'>sample file</a>]</td></tr></table>");
    page_html = st.toString();	
	
            break;
   }
   
   
   case -Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : 
       {
          StringBuffer st = new StringBuffer();
st.append("<tr><td>This page allows you to upload the<i> clone collection information </i>from an XML file. ");
st.append("The <b>clone collection element</b> describes plate. It has the followinf requered properties: user id (unique), name (plate label).");
st.append(" ");
st.append(" ");
st.append(" ");
st.append("</td></tr> <tr>   <td  bgColor='#b8c6ed'>Please select the clone collection file:");
st.append(" <input type='file' name='fileName' id='fileName' value=''>");
st.append("     [<a href='"+ redirection+"help/help_clonecollection_xml_format.html' target='_blank'>sample file</a>]</td></tr></table>");
    page_html = st.toString();	

break;}
}


StringBuffer st = new StringBuffer();        //settings database
      int new_forwardName=0;
      boolean isSubmitFromFile=false;
 if( forwardName_int == Constants.PROCESS_ADD_NEW_LINKER  
        || forwardName_int ==  -Constants.PROCESS_ADD_NEW_VECTOR  
        || forwardName_int ==  -Constants.PROCESS_ADD_NAME_TYPE  
        || forwardName_int ==  -Constants.PROCESS_ADD_SPECIES_DEFINITION  
        || forwardName_int ==  -Constants.PROCESS_ADD_NEW_CLONINGSTRATEGY)
{
      
isSubmitFromFile=true;  
switch( forwardName_int)
{
    case   -Constants.PROCESS_ADD_NEW_VECTOR:  
    {
st.append(" <tr>   <td  bgColor='#b8c6ed'>Please submit new vectors from flat files:</td></tr>");
st.append(" <tr>   <td  bgColor='#b8c6ed'>Please select the vector information file:<input type='file' name='fileName' id='fileName' value=''>");
st.append("     [<a href='"+redirection +"help/VectorInfoFile.html'>sample template file</a>]</td></tr>");
st.append(" <tr>   <td  bgColor='#b8c6ed'>Please select the vector feature information file:<input type='file' name='fileName' id='fileName' value=''>");
st.append("     [<a href='"+redirection +"help/VectorFeaturesInfoFile.html'>sample template file</a>]</td></tr>");

       
       st.append("  <i>Note: Do not change file header, append new linker information to the end of the file, not duplicates will be submited.</i></td></tr>");


       new_forwardName= -Constants.PROCESS_ADD_NEW_VECTOR_FROM_FILE ;
       break;
    }
   case -Constants.PROCESS_ADD_NEW_LINKER  : 
       {
st.append(" <tr>   <td  bgColor='#b8c6ed'>Please select the linker information file:");
st.append(" <input type='file' name='fileName' id='fileName' value=''>");
st.append("     [<a href='"+redirection +"help/LinkerInfoFile.html'>sample template file</a>]");
st.append("  <i>Note: Do not change file header, append new linker information to the end of the file, not duplicates will be submited.</i></td></tr>");


       new_forwardName= -Constants.PROCESS_ADD_NEW_LINKER_FROM_FILE ;
       break;
       }
   case -Constants.PROCESS_ADD_NAME_TYPE  : 
       {
st.append(" <tr>   <td  bgColor='#b8c6ed'>Please submit new nametypes from file:");
st.append(" <input type='file' name='fileName' id='fileName' value=''>");
st.append("     [<a href='"+redirection +"help/help_name_file_format.html'>sample template file</a>]</td></tr>");
st.append(" <tr>   <td > <i>Note: Do not change file header, append new nametypes to the end of the file, not duplicates will be submited.</i></td></tr>");


       new_forwardName= -Constants.PROCESS_ADD_NAME_TYPE_FROM_FILE ;
       break;
       }
   case -Constants.PROCESS_ADD_SPECIES_DEFINITION  :
   {
   st.append(" <tr>   <td  bgColor='#b8c6ed'>Please submit new species definitions from file:");
st.append(" <input type='file' name='fileName' id='fileName' value=''>");
st.append("     [<a href='"+redirection +"help/help_species_definitions.html'>sample template file</a>]</td></tr>");
st.append(" <tr>   <td > <i>Note: Do not change file header, append new species to the end of the file, not duplicates will be submited.</i></td></tr>");

       new_forwardName= -Constants.PROCESS_ADD_SPECIES_DEFINITION_FROM_FILE ;
       break;
       }
   case -Constants.PROCESS_ADD_NEW_CLONINGSTRATEGY:
       {
           st.append(" <tr>   <td  bgColor='#b8c6ed'>Please select the cloning strategy information file:");
st.append(" <input type='file' name='fileName' id='fileName' value=''>");
st.append("     [<a href='"+redirection +"help/ClonStrInfoFile.html'>sample template file</a>]</td></tr>");
st.append(" <tr>   <td > <i>Note: Do not change file header, append new cloning strategy information to the end of the file, not duplicates will be submited. ");
st.append("      Make sure that all linkers and vector have been submitted to ACE, vector and linker names in cloning strategy definition MUST be exactly the same. </i></td></tr>");

       new_forwardName= -Constants.PROCESS_ADD_NEW_CLONINGSTRATEGY_FROM_FILE ;
       break;
    
       }
            
   }}
System.out.println(st.toString());
%>

</head>

<body>
 <table width="100%" border="0" cellpadding="10" style='padding: 0; margin: 0; '>
  <tr>
    <td><%@ include file="page_application_title.html" %></td>
  </tr>
  <tr>
    <td ><%@ include file="page_menu_bar.jsp" %></td>
  </tr>
  <tr>
    <td><table width="100%" border="0">
        <tr> 
          <td  rowspan="3" align='left' valign="top" width="160"  bgcolor='#1145A6'>
		  <jsp:include page="page_left_menu.jsp" /></td>
          <td  valign="top"> <jsp:include page="page_location.jsp" />
           </td>
        </tr>
        <tr> 
          <td valign="top"> <jsp:include page="page_title.jsp" /></td>
        </tr>
        <tr> 
          <td><!-- TemplateBeginEditable name="EditRegion1" -->
<div align="center">
  <center>  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%"><html:errors/></td>    </tr>  </table>
  </center></div>
  
  
 <!-- define validation function -->
 <%
String onsubmit = "";
if (forwardName_int== Constants.PROCESS_VERIFY_TRACE_FILE_FORMAT)
{onsubmit = "onsubmit='return validate_add_format(this);'";}
else if (forwardName_int== -Constants.PROCESS_ADD_PROJECT_DEFINITION)
{ onsubmit = "onsubmit='return validate_add_project(this);'";}
 else if (forwardName_int== -Constants.PROCESS_ADD_SPECIES_DEFINITION)
{ onsubmit = "onsubmit='return validate_add_speciesname(this);'";}
 else if (forwardName_int== -Constants.PROCESS_ADD_NAME_TYPE)
{ onsubmit = "onsubmit='return validate_add_nametype(this);'";}
 else if (forwardName_int== -Constants.PROCESS_ADD_NEW_COMMON_PRIMER)
{ onsubmit = "onsubmit='return validate_add_sequencingprimer(this);'";}
 else if (forwardName_int== -Constants.PROCESS_ADD_NEW_LINKER)
{ onsubmit = "onsubmit='return validate_add_linker(this);'";}
%> 


<form name ="DirectDatabaseCommunications" action="DirectDatabaseCommunications.do" 
METHOD="POST" ENCTYPE="multipart/form-data" <%= onsubmit%> > 

<input name="forwardName" type="hidden" value="<%= forwardName %>" > 



 
<table border="0" cellpadding="5" cellspacing="1" width="90%" align=center>
 <% switch ( forwardName_int)
 {
case  Constants.PROCESS_VERIFY_TRACE_FILE_FORMAT:
{%>
    <jsp:include page="configuration/add_trace_file_format.jsp" />
    <%
break;
} 
  case -Constants.PROCESS_ADD_NEW_CLONINGSTRATEGY:
   {%>
    <jsp:include page="configuration/add_clonning_strategy.jsp" />
   
     <%
break;
} 
 case -Constants.PROCESS_ADD_TRACE_FILE_NAME_FORMAT:
 {%>
    <jsp:include page="configuration/verify_trace_file_name_format.jsp" />
    <%
break;
} 
default: %>  <%= page_html %> 
<%}%>
 
</table>
<% 
String button_name = "Submit";
if (forwardName_int== Constants.PROCESS_VERIFY_TRACE_FILE_FORMAT)
{ button_name="Verify new format";}
else if (forwardName_int== -Constants.PROCESS_ADD_TRACE_FILE_NAME_FORMAT)
{ button_name="Add new format" ;}%>

<div align="center">   <p>     <input type="submit" value="<%= button_name %>" ></DIV>


</form> 
<% if ( isSubmitFromFile){%>
<table border="0" cellpadding="5" cellspacing="1" width="90%" align=center>
    
    <form name ="DirectDatabaseCommunications" action="DirectDatabaseCommunications.do" 
          METHOD="POST" ENCTYPE="multipart/form-data"  > 
        
        <input name="forwardName" type="hidden" value="<%= String.valueOf(new_forwardName) %>" > 
        <%= st.toString() %> 
        <tr><td>       <div align="center">   <p>     <input type="submit" value="Submit" ></DIV></td></tr>
    </form>
</table>
<%}%>
<jsp:include page="configuration/display_array_of_items.jsp" />
 </tr>
      </table></td>
  </tr>
  <tr>
    <td><%@ include file="page_footer.jsp" %></td>
  </tr>
</table>
</body>
</html>






 