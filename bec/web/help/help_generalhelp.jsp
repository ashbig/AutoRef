<!--Copyright 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.--> <%@page contentType="text/html"%>

<%@ page import="edu.harvard.med.hip.bec.*" %> <html> <head><title>JSP Page</title> <style>
<!--
 li.MsoNormal
	{mso-style-parent:"";
	margin-bottom:.0001pt;
	font-size:12.0pt;
	font-family:"Times New Roman";
	margin-left:0in; margin-right:0in; margin-top:0in}
-->
</style>
    
</head>
<body>


<table border="0" cellpadding="0" cellspacing="0"
width="74%" align=center>
    <tr>
        
    <td > <h1><font color="#0099CC" > ACE tutorial</font> </h1>
      <hr>    <p>    </td>
    </tr>
</table>

<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0"
width="80%">
    <tr>      <td width="100%"><html:errors/></td>   
</tr>
  </table>
  </center>
</div>


<table width="74%" border="0" cellpadding="2"
cellspacing="2" align="center">
  <tr> 
    <td> <p class="MsoNormal"><strong><em>Automated Clone Evaluation (ACE)</em></strong> 
        system is a software suite designed to address the main goals of sequence 
        verification process for projects of any scale: (1) determine the sequence 
        of each clone accurately; (2) identify if and where that sequence varies 
        from the intended target sequence; (3) evaluate and annotate the polypeptide 
        consequences of any variations; and (4) determine if these observed differences 
        are acceptable based on user defined criteria. ACE was designed allows 
        to eliminating bad clones as early as possible in the validation process, 
        and minimize the need to produce additional sequencing primers.</p>
      <p class="MsoNormal"><strong><em>Note: </em></strong>ACE GUI consists of 
        the main window and left hand drop-down menu. Main window displays forms 
        according to user selectiong from the left-hand menu. Throughout this 
        tutorial the notation like 'Process &gt; Read Manipulation' means that 
        user expands 'Process' menu and selects 'Read manipulation' item.</p>
      <h2 ><font color="#0099CC">ACE tutorial</font></h2>
      <ul>
        <li><a href="#where_to_start">Where to start?</a></li>
        <li><a href="#ace_configuration">System settings</a></li>
        <li><a href="#create_spec">Analysis setting</a></li>
        <li><a href="#upload_plates">Plate upload</a></li>
        <li><a href="#primers">Primers design and management</a></li>
        <li><a href="#trace_upload">Trace files upload</a></li>
        <li><a href="#data_processing">Sequencing data processing and clone evaluation.</a></li>
        <li><a href="#delete_data">Data clean-up</a></li>
        <li><a href="#report_data">Reports</a></li>
        <li><a href="#view_data">Real time views</a></li>
      </ul></p>
      <h3><a name="where_to_start">Where to start</a></h3>
      <p>Before uploading plates and trace files into ACE all steps on the 'Cloning 
        project settings' menu should be performed (see next section). </p>
      <h3><a name="ace_configuration">System settings.</a></h3>
      All definitions created in this section are shared among all users of ACE 
      and all projects. The main window for every kind of settings provides the 
      form to define new item and summary table of all existing items of this 
      kind. 
      <ul>
        <li>ACE project is a logical container for all related plates. One common 
          project can be created and used for all plates submitted into ACE. To 
          <strong>create new project</strong> select '<em>Cloning Project Settings 
          &gt; Project Definition</em>' fill project name and optional project 
          description fields.</li>
        <li>To introduce <strong>new biological species </strong>to ACE select 
          <em>'Cloning Project Settings &gt; Species Definition</em>'. Clones 
          within a project can belong to different species.</li>
        <li>A clone has a target (reference) sequence against which it will be 
          verified by ACE. Reference sequence can be annotated according to various 
          annotation conventions. ACE can give arbitrary names to these conventions 
          using <em>'Cloning Project Settings &gt; Annotation Type</em>' menu. 
          These <strong>annotation types</strong> will appear as parameter names 
          for reference sequences submission.</li>
        <li>To introduce <strong>new vector</strong> definitions into ACE select 
          '<em>Cloning Project Settings &gt; Vector Information</em>' and submit 
          <a href="help_vector_xml_format.html"
target="_blank">XML file</a> with vector(s) description. Each vector is described 
          by name, source, type (1 for destination vector, 0 for master vector), 
          filename, filepath. In addition a vector may have a set of features. 
          Feature description contains: feature name ('recombination site'), feature 
          property (1 for added, 0 for remain, -1 for lost when insert is integrated), 
          feature description.</li>
        <li>To introduce <strong>new sequencing (none gene-specific) primer</strong> 
          to ACE select '<em>Cloning Project Settings &gt; Sequencing Primer</em>' 
          and provide primer name, sequence (if sequence is unknown enter 'NN'), 
          Tm and primer type.</li>
        <li>For each vector only certain universal primers are used for end reads 
          by your sequencing facility. To specify this relationship between vector 
          and primers use <em>'Cloning Project Settings &gt; Link Vector with 
          Sequencing Primer'</em>. Several primers can be used for the same vector.</li>
        <li>Linker is a part of vector sequence up-stream or down-stream of the 
          insert that is important for clone function and thus, user wishes to 
          be sequence verify. To define a linker use '<em>Cloning Project Settings 
          &gt; Add Linker</em>'.</li>
        <li>Cloning strategy in ACE is a collection of the following parameters: 
          sequencing vector, 5' and 3' linkers, start and stop codons. Application 
          administrator defines start and stop codon possible values in <a
href="help_ACEConfigurationFile.html"
target="_blank">ACE Configuration file</a>. To create new cloning strategy use 
          '<em>Cloning Project Settings &gt; Add Cloning Strategy</em>'. </li>
      </ul>
      <h3><a name="create_spec">Analysis setting</a></h3>
      <p>Particular processing modules imbeded into ACE require analysis specifications 
        (spec) to be predefined. All analysis specifications are shared among 
        all users of ACE and all projects. The main window that allow to create 
        new spec of any kind (see 'Page' column in the following table) provides 
        the form to define name and set of parameters for new spec, and links 
        to description of all specifications of the same type created by (a) current 
        user of ACE; (b) all ACE users. Consult following table for types of analysis 
        specification and their use. Click specification name for details.</p>
      <div align="center"></div>
      <table  border="1">
        <tr bgcolor="#0033FF"> 
          <td><div align="center"><strong><font color="#FFFFFF">Specification 
              Name</font></strong></div></td>
          <td><div align="center"><strong><font color="#FFFFFF">Page</font></strong></div></td>
          <td><div align="center"><strong><font color="#FFFFFF">Module</font></strong></div></td>
        </tr>
        <tr> 
          <td valign="top">Clone Ranking</td>
          <td valign="top">Analysis Settings &gt; Clone Ranking </td>
          <td valign="top">Isolate Ranker</td>
          </tr>
        <tr> 
          <td valign="top"><a href="specs/help_acceptance_spec.htm" target="_blank">Clone Acceptance 
            Criteria </a></td>
          <td valign="top">Analysis Settings &gt; Clone Acceptance Criteria </td>
          <td valign="top"><dl>
              <dt>Decision Tool</dt>
              <dt>Quick Decision Tool</dt>
              <dt>Isolate Ranker</dt>
            </dl></td>
         </tr>
        <tr> 
          <td valign="top">Set of Parameter for Primer Design </td>
          <td valign="top">Analysis Settings &gt; Primer Designer </td>
          <td valign="top">Primer Designer</td>
         </tr>
        <tr> 
          <td valign="top">Set of Parameter for Polymorphism Detection </td>
          <td valign="top">Analysis Settings &gt; Polymorphism Detection</td>
          <td valign="top">Polymorphism Finder</td>
        </tr>
        <tr> 
          <td valign="top">Set of Parameter for Sequence Trimming </td>
          <td valign="top">Analysis Settings &gt; Sequence Trimming </td>
          <td valign="top"><dl>
              <dt>Gap MapperLow Confidence Regions Finder </dt>
            </dl></td>
        </tr>
      </table>
      <font color="#FFFFFF">&nbsp;</font><font color="#FFFFFF">&nbsp;</font></div> 
      <h3><a name="upload_plates">Plate upload</a></h3>
      <p>User Goal: Upload plate mapping information and target sequence(s) description 
        into ACE.</p>
      <p><em><strong>For non-HIP users:</strong></em></p>
      <ul>
        <li>Select cloning strategy for plates (see 'System settings' section 
          if you need create new cloning strategy).</li>
        <li><a
href="help_XML_files_format_description.htm"
target="_blank"	>Prepare XML files for submission</a>.</li>
        <li>Select <em>'Process &gt; Upload plates information &gt; Upload reference 
          sequence information from file'</em> and upload XML file with reference 
          sequence information.</li>
        <li>Select <em>'Process &gt; Upload plates information&gt; Upload clone 
          collection from file' </em> and upload XML file with plate mapping information.</li>
      </ul>
      <p><em></em></p>
      <p><em><strong>For HIP users:</strong></em></p>
      <ul>
        <li>Confirm with informatics team that your plates have been submitted 
          for sequencing. </li>
        <li>Select <em>'Process &gt; Upload plates information'</em>. 
          <ul>
            <li>Submit plate labels (case sensitive) (<em>Note: </em>only plates 
              cloned using the same cloning strategy can be uploaded together). 
            </li>
            <li>Choose from next step in plate processing (available selection: 
              'Run end reads' and 'Run clone evaluation').</li>
            <li>Choose Start Codon (available selection: ATG; Natural - as submitted 
              from GenBank for reference sequence. If your clones have been normalized 
              with start codon other than 'ATG' please contact informatics team).</li>
            <li>Choose Fusion Stop Codon (available selection: GGA; TTG. If your 
              clones have been normalized with different fusion codon please contact 
              informatics team).</li>
            <li>Choose Closed Stop Codon (available selection: TGA; TAA; TAG; 
              Natural).</li>
            <li>Choose cloning vector. If your vector is not available goto <em>'Cloning 
              project settings > Add vector</em>. 
            <li>Choose 5' upstream and 3' downstream linkers.</li>
          </ul>
        </li>
      </ul>
      <p>Uppon finishing the upload ACE sends e-mail report to the user.</p>
      <p><a href="#upload_plates">Back</a> </p>
      <h3><a name="primers">Primers design and managment</a></h3>
      <p>This set of modules allows users to: (a) design gene-specific sequencing 
        primers; (b) select specific primer(s) for vendor order; (c) order primers; 
        (d) track primer plates;</p>
      <ul>
        <li>To <strong>order gene-specific primers</strong> use '<em>Process &gt; 
          Internal Primer Design and Order &gt; Run Primer Designer</em>', specify 
          how to extract clone information. User can submit (a) plate labels - 
          primers will be designed for all clones from these plates; (b) clone 
          IDs; (c) ACE reference sequence IDs; (d) user reference sequence IDs*. 
          Next choose specification for Primer3 and define what region of target 
          sequence primer should be covered. Select '<em>Design primers for Reference 
          Sequence</em>' option if you would like to design primers for a complete 
          primer walk. Select '<em>Design primers for Stretch Collection</em>' 
          to design primers needed to complete coverage (i.e., fill in gaps or 
          cover low confidence discrepancies). Stretch collection is a result 
          of Gap Mapper or Low Confidens Regions Finder run for the particular 
          clone. ACE extracts last stretch collection defined for the clone. 
          <p>Primer Designer can be run in try mode, in this case information 
            about designed primers is sent to user in <a href="reports/primer_design.htm">tab 
            delimited text file</a>, however, no information is stored in database 
            and, hence, cannot be used for vendor primer order.<br>
            *<strong><em>Note:</em></strong> Options (c) and (d) should not be 
            used in conjunction with '<em>Design primers for Stretch Collection</em>' 
            mode. </li>
        <li>Designed primers must be approved by user to be included into vendor 
          order. To <strong>approve primers</strong> select '<em>Process &gt; 
          Internal Primer Design and Order &gt; Approve Internal Primers</em>', 
          specify clone IDs and select what kind of primers (designed for reference 
          sequence or for stretch collection) </li> should be displayed. </li>
        <li>To <strong>place primers order</strong> use <em>'Process &gt; Internal 
          Primer Design and Order &gt; Order Internal Primers'</em>, specify clone 
          IDs, choose type of approved primers that should be included in the 
          order. Available options are (a) only primers designed for stretch collections; 
          (b) only primers designed for reference sequence; (c) primers designed 
          for stretch collections and reference sequence. ACE pools out all primers 
          aproved for the specified clones according to the user selection and 
          starts to put them in 96-well plate format. User has an option to specify 
          wells where first and last primers will be placed on the plate allowing 
          to preserve empty wells for controls if desired. ACE allows user to 
          create an order file in format required by vendor ( <a href="reports/sample1_order_oligo.html">sample_order_file1</a>, 
          <a href="reports/sample2_order_oligo.html">sample_order_file_2</a>).</li>
        <li>To store your <strong>information</strong> regarding when <strong>primer 
          plates</strong> were ordered or used for sequencing select '<em>Process 
          &gt; Internal Primer Design and Order &gt; Track Oligo Plate</em>' choose 
          plate status and enter comments.<br>
        </li>
      </ul>
      <h3><a name="trace_upload">Trace files upload</a></h3>
      <li>Copy trace files into directory specified in <a href="help_ACEConfigurationFile.html" target="_blank">ACE 
        configuration file</a> (TRACE_FILES_TRANCFER_INPUT_DIR). </li>
      <li>Create mapping file which is tab delimited text file with one record 
        per plate. Each record should contain two fields: 
        <p>&lt;sequencing facility plate label>Tab< HIP plate label>.</li>
      <li>In ACE go to <em>'Trace Files -&gt; Create Renaming File'</em>. Select 
        mapping file, <a
href="help_TraceFilesNamingFormats.html"
onClick="window.open(href, 'GeneralReport', 'width=800,height=400,scrollbars=yes');">trace 
        files' naming format</a> and type of reads. The 'Internal reads' option 
        currently implemented only for primers designed by ACE. Click 'Submit' 
        button. ACE writes 'renaming_xxx.txt' file into TRACE_FILES_TRANCFER_INPUT_DIR 
        directory, the same file is sent to user by e-mail. Renaming file contains 
        one entry per each trace file in the format: <original file name><em> 
        &lt;Trace File name&gt; Tab </em>&lt;ACE recognizable file name>. Make 
        sure that number of entries in 'renaming_xxx.txt' file is equal to the 
        number of trace files. If 'renaming_xxx.txt' file is empty or number of 
        the entries is less than expected one of the following problems occured: 
        <ul>
          <li>Errors in mapping file or plate indicated in mapping file have not 
            been submitted into ACE .</li>
          <li>There is no trace files named according to the selected format. 
          </li>
          <li>Some of the files have not been named properly, for example, some 
            trace files for plate have wrong position annotation. </li>
        </ul>
      </li>
      <li>Upload trace files on ACE server: <em>use 'Trace Files -&gt; Upload 
        Trace Files'</em>, select renaming_xxx.txt file created by ACE on the 
        previous step. ACE will send user e-mail when transfer trace files transfer 
        will be finished.</li>
      <p>**********************************************</p>
      <h3><a name="data_processing">Sequencing data processing and clone evaluation.</a></h3>
      <p>User Goal: inform ACE ACE what type of end read he has orded and what 
        primer were used for sequencing. This information MUST be submitted before 
        running 'End Reads Wrapper'.</p>
      <p>User Action: </p>
      <ul>
        <li>Select in ACE menu Process->Put request for end reads sequencing. 
        </li>
        <li>Choose vector that was used for cloning, click 'Submit' button. </li>
        <li>Choose primers that will be used for sequencing, select 'NONE' as 
          primer type if this direction is not sequenced. Please, pay close attention 
          to primers chooce, the possible output of wrong primer selection - clone 
          will be described as 'No match' clone. </li>
        <li>Specify plates. </li>
      </ul>
      <p>If your primers are not listed by ACE for the selected vector, please 
        contact informatics team. </p>
      <P>ACE will send user e-mail after action will be performed. This e-mail 
        contains as attachment naming files for end read (one file per plate/direction)</P>
      <h3><strong> </strong> </h3>
      <p>End Reads wrapper module performes the following actions </p>
      <ul>
        <li>distribute all none active chromat files into appropriate directories 
          (none active chromat files are trace files that (a)are named not according 
          to ACE nomenculature; (b) trace files for empty wells; (c) trace files 
          for controls); </li>
        <li>distribute all internal reads chromat files; </li>
        <li>for each end read chromat file: 
          <ul>
            <li>run base calling program (phred, with trimming );</li>
            <li>submit read information (scored sequence, type of the read, start/stop 
              of trimmed sequence);</li>
            <li>distribute chromat file into appropriate directorie;</li>
          </ul>
        </li>
      </ul>
      User input information for the module - list of plates which trace files 
      should be distributed. 
      <p>ACE will send user e-mail after action will be performed. </p>
      <p>**********************************</p>
      <h3><a name="delete_data">Data clean up</a></h3>
      <p>ACE allows removal of trace files and clone information from the database. 
        This is useful in cases when: (a) wrong plates have been submitted into 
        ACE; (b) errors occurred in clones mapping on the plate; (c) plates have 
        been mislabeled; (d) incorrect data from sequencing facilities, etc.</p>
      <ul>
        <li>To <strong>delete plate</strong> select '<em>Process &gt; Delete Data 
          &gt; Delete plate</em>' and submit plate label (case insensitive).</li>
        <li>To <strong>delete clone forward and reverse end reads</strong> select 
          '<em>Process &gt; Delete data &gt; Clone forward and reverse end reads 
          from database</em>' and submit plate label (case insensitive) or clone 
          IDs. This action deletes records for the reads from the database, however, 
          trace files for these end reads stored on the hard drive are not affected.</li>
        <li>To <strong>delete forward end reads</strong> only select '<em>Process 
          &gt; Delete data &gt; Delete clone forward end reads from database</em>' 
          and submit plate label (case insensitive) or clone IDs. This action 
          deletes records for the forward reads from the database, however, trace 
          files for these forward end reads stored on the hard drive are not affected.</li>
        <li>To <strong>delete clone reverse end reads </strong> select <em>'Process 
          &gt; Delete data &gt; Delete clone reverse end reads from database</em>' 
          and submit plate label (case insensitive) or clone IDs. This action 
          deletes records for the reverse reads from the database, however, trace 
          files for these reverse end reads stored on the hard drive are not affected.</li>
        <li>To <strong>delete clone sequence</strong> select '<em>Process &gt; 
          Delete data &gt; Delete clone sequence from database</em>' and submit 
          clone IDs or clone sequence IDs (run General Report to get clone sequence 
          ID).</li>
        <li>To <strong>delete trace files</strong> from the hard drive, first, 
          select '<em>Process &gt; Delete data &gt; Get trace file names</em>'. 
          ACE will send e-mail with attached file that contains trace file names 
          for the submitted clone IDs. Save this file. Next select '<em>Process 
          &gt; Delete data &gt; Delete trace files from hard drive (no recovery 
          possible)</em>' and submit saved file. ACE will permanently delete traces 
          from the hard drive.</li>
        <li>To <strong>remove trace files</strong> to the storage directory first 
          get file with trace file name from ACE (see above). Next select <em>'Process 
          &gt; Delete data &gt; Move trace file from clone directory into temporary 
          directory (allows trace files recovery)</em>' - traces will be moved 
          to the storage directory. </li>
        <li>To <strong>delete intermidiate files</strong> created during clone 
          sequence assembly by PhredPhrap from hard drive select '<em>Process 
          &gt; Delete data &gt; Clean-up hard drive</em>'.<br>
        </li>
      </ul>
      <p><a href="#delete_data">Back</a> </p>
      <h3><a name="report_data">Reports</a></h3>
      ACE sends reports by e-mail as attached tab delimited files. 
      <ul>
        <li>'Quick Decision Tool' report is a simplified version of Decision Tool 
          which determines if clone(s) meets user defined acceptance criteria. 
          To run the report select <em>Reports &gt; Quick Decision Tool'</em> 
          and specify user acceptance criteria and set of clones. Execution time 
          is proportional to the number of specified clones. The 'Quick Decision 
          Tool' provides feedback on the screen in addition to sending the report 
          by e-mail.</li>
        <li>To run <a href="help_ACE_overview.htm#decision">Decision Tool </a> 
          choose <em>'Reports &gt; Detailed Decision Tool'</em> and fill the query 
          form. There is no limit on the number of processed clones.</li>
        <li>'Mismatched Clones' report helps to detect cases of misidentified 
          input data such as (a) mislabeled or rotated plates; (b) plates cross-contamination; 
          (c) systematic sequencing facility errors. The report blasts end reads' 
          sequences against user selected blastable database which usually contains 
          reference sequences for the cloning project and vector sequences. To 
          run report select <em>' Reports &gt; Mismatched Clones'</em>, specify 
          clones and blastable database. There is no limit on the number of clones.</li>
        <li>General report provides snapshot of <a href="help_ReportRunner.html"
target="_blank"> requested information</a> for the specified set of clones. To 
          run general report choose <em>'Reports &gt; General Report'</em> and 
          fill the query form. There is no limit on the number of clones.</li>
        <li>To examine quality of existing trace files choose <em>'Reports &gt; 
          Trace Files Quality Report'</em> and provide clone IDs. For each clone 
          trace file ACE extracts Phred confidence base scores and determines 
          whether a read meets minimum quality requirements: (a) the read must 
          be longer than the user-defined minimum length; (b) the average confidence 
          score for all non-ambiguous bases between the first and last base must 
          be above the minimum confidence score. The value of these thresholds 
          are defined in <a href="help_ACEConfigurationFile.html" target="_blank">ACE 
          configuration file</a>. There is no limit on the number of processed 
          clones.</li>
      </ul>
      <p>&nbsp;</p>
      <p><a href="#report_data">Back</a> </p>
      <h3><a name="view_data">Real time views</a></h3>
      <ul>
        <li><strong><a
href="image/image_Fig4.htm">Comprehensive plate viewer</a></strong> gives access 
          to complete information about plate clones. Select 'View &gt; Plate 
          Results', enter plate label (case insensitive), check 'Show Isolate 
          Ranker Output' option. </li>
        <li>To view<strong> plate history</strong> select '<em>View &gt; Plate 
          history</em>' and enter plate label (case insensitive);</li>
        <li>To view <strong>plate description</strong> select '<em>View &gt; Plate 
          Description</em>' and enter plate label (case insensitive). The view 
          displays (1) plate label; (2) plate unique ID; (3) plate type (96 well 
          plate); (4) link to cloning strategy description; (5) for each well: 
          (a) well number; (b) sample type - Isolate, Control, Empty; (c) clone 
          ID; (d) clone final status; (e) description of the last process run 
          on clone data; (f) link to reference sequence description; (g) link 
          to clone sequence description if available. </li>
        <li>To view <strong>clone processing history </strong>select '<em>View 
          &gt; Clone History</em>' and enter clone IDs;</li>
        <li>To view <strong>all plates uploaded into ACE</strong> select '<em>View 
          &gt; Plates</em>'. Plates are grouped by project name (alphabetical 
          order). Click on the check box located before project name to hide all 
          plates related to the project.</li>
        <li>To veiw <strong>all analysis specifications </strong> stored in ACE 
          select 'View &gt; Process Configurations'. <strong><em>Note: </em></strong>you 
          can view only one specification at a time. </li>
        <li>To view complete information about <strong>vectors</strong> uploaded 
          into ACE select '<em>View &gt; Vectors</em>'. </li>
        <li>To view complete information about <strong>linkers</strong> uploaded 
          into ACE select '<em>View &gt; Linker</em>'. </li>
        <li>To examine <strong>quality of the uploaded end reads</strong> select 
          '<em>Process &gt; View Process Results &gt; View available end reads</em>'. 
          The resulting view displays (1) plate label; (2) plate unique ID; (3) 
          plate type (96 well plate); (4) link to cloning strategy information; 
          (5) for each well: (a) well number; (b) sample type - Isolate, Control, 
          Empty; (c) quality status of forward end read (Pass / Fail); (d) quality 
          status of reverse end read. The view helps to detect systematic problems 
          with submitted end reads, for example, cases when all forward or reverse 
          reads failed.</li>
        <li> To view <strong>primers designed for clone(s)</strong> select '<em>Process 
          &gt; View Process Results &gt; View Internal Primers</em> ', submit 
          clone IDs and indicate type of primers: primers designed for reference 
          sequence or primers designed for stretch collection (see description 
          of GapMapper module for details). <a href="screen_shots/report_view_primers.html"
target="_blank">Sample report</a>.</li>
        <li> To view <strong>all primers ordered for the clone</strong> select 
          '<em>Process &gt; View Process Results &gt; View Oligo Order(s) for 
          Clone(s)</em>'. <a target="_blank"
href="screen_shots/report_ordered_primers.html">Sample report</a>.</li>
        <li> To view <strong>ordered oligo plates</strong> select '<em>Process 
          &gt; View Process Results &gt; View Oligo Plate</em>'. <a target="_blank" href="screen_shots/report_oligo_plate.html">Sample 
          report.</a></li>
        <li> To view <strong>all contig collections</strong> created by Gap Mapper 
          for the clone select '<em>Process &gt; View Process Results &gt; View 
          all contig collections </em>'. Multiple contig collections are created 
          when Gap Mapper is launched several times with different specification 
          or with additional trace files.</li>
        <li> To view <strong>last contig collection </strong>designed for the 
          clone by Gap Mapper select <em>Process &gt; View Process Results &gt; 
          View latest contig collection</em>. <a href="screen_shots/report_view_contigs.html" target="_blank">Sample 
          report.</a> </li>
        <li> To view set of <strong>low confidence regions</strong> for the clone 
          created by Low Confidence Finder select <em>Process &gt; View Process 
          Results &gt; View low confidence regions for clone sequences' </em> 
          and specify clone IDs. <a href="screen_shots/report_view-lqr.html">Sample 
          report.</a> </li>
      </ul>
      <p><a href="#view_data">Back</a> </p>
      <h3>&nbsp;</h3>
      <p class="MsoNormal">&nbsp;</p>
      <p class="MsoNormal">&nbsp;</p></td>
  </tr></table>
</body>
</html>



 
