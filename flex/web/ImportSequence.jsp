<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Import Sequences into FLEXGene</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Import Sequences into FLEXGene</h2>
<hr>
<html:errors/>
<p><i>This page allows you to upload the sequences and requested users into the
database from three files: <b>sequence file</b>, <b>name file</b> and <b>request file</b>. 
The <b>sequence file</b> contains sequence id, species, CDS start, CDS stop, CDS length, GC content,
cDNA source, chromosome and sequence text. The sequence id is the unique identifier
for each sequence. It will be used for cross reference to other two files and 
won't be entered into the database. 
The <b>name file</b> contains sequence id,
name type ("GenBank Accession"), name value (the GenBank accession
number for "GenBank Accession" name type), name url and description. 
The sequence id is the same id used in the sequence file. The name type field must be
valid name types in the database. Please click 
<a href="GetNametype.do">here</a>
to see a list of the valid 
name types. If you don't see your name type in the database, please click 
<a href="AddNametype.jsp">here</a>
to add a valid name type.
The <b>request file</b>
contains username and sequenceid. The username must be a valid username in the
database (i.e. registered user), otherwise, the import will be aborted. Please
click 
<html:link forward="register" target="_top">here</html:link> 
to register new users. 
All the fields in each file are listed in the above order and separated
by delimiter. We currently support the following delimiters: !, tab (\t).</i>

<p>
<html:form action="/ImportSequence.do" enctype="multipart/form-data"> 

<table>
<tr>
    <td class="prompt">Please select the sequence file:</td>
    <td><html:file property="sequenceFile" /></td>
    <td>[<a href="ViewSequenceSampleFile.jsp">sample file</a>]</td>
</tr>
<tr>
    <td class="prompt">Please select the name file:</td>
    <td><html:file property="nameFile" /></td>
    <td>[<a href="ViewNameSampleFile.jsp">sample file</a>]</td>
</tr>
<tr>
    <td class="prompt">Please select the request file:</td>
    <td><html:file property="requestFile" /></td>
    <td>[<a href="ViewRequestSampleFile.jsp">sample file</a>]</td>
</tr>
<tr>
    <td class="prompt">Please select the project:</td>
    <td><html:select property="projectid">
        <html:options
        collection="projects"
        property="id"
        labelProperty="name"
        />
    </html:select>
    </td>
</tr>
</table>
<html:submit/>
</html:form>

<p>
<ul>
<li><html:link forward="register" target="_top">Register new user</html:link> 
<li><a href="GetNametype.do">View valid name types</a>
<li><a href="AddNametype.jsp">Add name type</a>
</ul>
</body>
</html:html>
