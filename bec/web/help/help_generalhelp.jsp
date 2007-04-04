<!--Copyright 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.--> <%@page contentType="text/html"%>

<%@ page import="edu.harvard.med.hip.bec.*" %> <html> <head><style>
<!--
 li.MsoNormal
	{mso-style-parent:"";
	margin-bottom:.0001pt;
	font-size:12.0pt;
	font-family:"Times New Roman";
	margin-left:0in; margin-right:0in; margin-top:0in}
-->
</style>
   <title>ACE Tutorial</title> 
</head>
<body>


<table border="0" cellpadding="0" cellspacing="0"
width="74%" align=center>
    <tr>
        
    <td > <h1><font color="#0099CC" > ACE Tutorial</font> </h1>
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
        are acceptable based on user defined criteria. ACE was designed to eliminate 
        bad clones as early as possible in the validation process, and minimize 
        the need to produce additional sequencing primers.</p>
      <p class="MsoNormal">An effective way to use this tutorial is to have both 
        the tutorial and ACE application open simultaneously and split on the 
        computer screen. This split view will allow the user to follow the tutorial 
        on the actual GUI or main window making the instructions far easier to 
        comprehend. If the ACE application has not been referred for an extended 
        period of time it will time out and need to be logged into again, you 
        will get an 'expired message' when this happened.</p>
      <p class="MsoNormal"><strong><em>Note: </em></strong>ACE GUI consists of 
        the main window and left hand drop-down menu. The main window displays 
        forms according to user selection from the left-hand menu. Throughout 
        this tutorial a notation like 'Process &gt; Read Manipulation' means that 
        user should select 'Process' menu and select 'Read manipulation' item 
        to get to the desired application.</p>
      <h2 ><font color="#0099CC">ACE Tutorial</font></h2>
      <ul>
        <li><a href="#where_to_start">Getting started</a></li>
        <li>Progect and Analysis Settings and definitions</li>
        <ul>
          <li><a href="#ace_configuration">Clonning project settings</a></li>
          <li><a href="#create_spec">Analysis setting</a></li>
        </ul>
        <li>Defining new targets</li>
        <ul>
          <li><a href="#upload_plates">Reference plate upload</a></li>
        </ul>
        <li>Operational Steps</li>
        <ul>
          <li><a href="#trace_upload">Trace files upload</a></li>
          <li><a href="#data_processing">Sequencing data processing and clone 
            evaluation</a></li>
        
          <li><a href="#report_data">Reports</a></li>
          <li><a href="#primers">Primer design and management</a></li>
          <li><a href="#view_data">Real time views</a></li>
		    <li><a href="#delete_data">Data clean-up</a></li>
        </ul>
      </ul></p>
      <h3><a name="where_to_start">Getting started </a></h3>
      <p>Before any analysis can be performed the project and analysis settings 
        and definitions must be pre-defined in ACE. Once these settings have been 
        entered they do not have to be entered again unless projects or definitions 
        change.</p>
      <h3><a name="ace_configuration">Cloning project settings.</a></h3>
      All definitions created in this section are shared among site users and 
      site projects. The main window for each menu item provides the form to be 
      filled in and submitted to create or update new definitions and/or summary 
      tablesthat are to be used in all analysis. 
      <ul>
        <li>Related plates are organized into ACE project. One project can be 
          created and used for all plates in ACE. To <strong>create a new project</strong> 
          select '<em>Cloning Project Settings &gt; Project Definition</em>' and 
          fill in project name and optional project description fields.</li>
        <li>To introduce <strong>new biological specie </strong>select <em>'Cloning 
          Project Settings &gt; Species Definition</em>': clones within a project 
          can belong to different species.</li>
        <li>A clone has a target (reference) sequence against which it will be 
          verified by ACE. The reference sequence can be annotated according to 
          various annotation conventions. ACE can accepts any names for these 
          descriptors using <em>'Cloning Project Settings &gt; Annotation Type</em>'. 
          These <strong>annotation types</strong> will appear as parameter names 
          for reference sequence submission. Examples of annotation type are GI, 
          gene symbol, SGD etc.</li>
        <li>To introduce <strong>new vector</strong> definitions into ACE select 
          '<em>Cloning Project Settings &gt; Vector Information</em>' and submit 
          an <a href="help_vector_xml_format.xml"
target="_blank">XML file</a> with vector description(s) (several vectors can be 
          submitted from one XML file). Each vector is described by its name, 
          source, type (1 for destination vector, 0 for master vector), filename, 
          file path. In addition a vector may have a set of features. Feature 
          description contains: feature name ('recombination site'), feature property 
          (1 for added, 0 for remain, -1 for lost when insert is integrated), 
          feature description. These features will be displayed under '<em>View 
          &gt; Vectors</em>'.</li>
        <li>To introduce <strong> a new sequencing primer (none gene-specific) 
          </strong> to ACE select '<em>Cloning Project Settings &gt; Sequencing 
          Primer</em>' and provide primer name, sequence (if sequence is unknown 
          enter 'NN'), Tm and select primer type from drop-down menu.</li>
        <li>For each vector only certain universal primers are used for end reads. 
          To specify a relationship between vector and universal primers use <em>'Cloning 
          Project Settings &gt; Link Vector with Sequencing Primer'</em>. Several 
          primer-vector combinations can be created for the same vector (see <a href="screen_shots/vector-primer-relation.html">screenshot</a> 
          for details). </li>
        <li>A linker is defined in ACE as part of the vector sequence located 
          up-stream or down-stream of the insert and is considered important for 
          clone function and sequence verification is required for acceptance. 
          To define a linker use '<em>Cloning Project Settings &gt; Add Linker</em>'.</li>
        <li>Cloning strategy in ACE is a collection of the following parameters: 
          sequencing vector, 5' and 3' linkers, start and stop codons. The application 
          administrator defines possible start and stop codon values in an <a
href="help_ACEConfigurationFile.html"
target="_blank"> ACE Configuration file</a>. To create a new cloning strategy 
          use '<em>Cloning Project Settings &gt; Add Cloning Strategy</em>'. </li>
      </ul>
      <h3><a name="create_spec">Analysis setting</a></h3>
      <p>&nbsp;</p>
      <p>Certain processing modules imbedded in ACE require analysis specifications 
        (spec). All analysis specificationsin this section are shared among site 
        users of ACE and site projects.The table below list Specification Name 
        (i.e. Clone acceptance criteria from the menu), Page (path to the main 
        window to create or view spec), Feature (name of feature where spec is 
        required). When creating a new spec user can view all specs created by 
        user or all specs created at site to avoid duplication. New analysis settings 
        can be added at any time and used during the analysis. Often a set of 
        clones will be re-analyzed using multiple analysis settings. Click specification 
        name for details.</p>
      <div align="center"></div>
      <table  border="1">
        <tr bgcolor="#0033FF"> 
          <td><div align="center"><strong><font color="#FFFFFF">Specification 
              Name</font></strong></div></td>
          <td><div align="center"><strong><font color="#FFFFFF">Page</font></strong></div></td>
          <td><div align="center"><strong><font color="#FFFFFF">Feature</font></strong></div></td>
        </tr>
        <tr> 
          <td valign="top"><a href="specs/help_rank_criteria.htm" target="_blank">Clone 
            Ranking</a></td>
          <td valign="top">Analysis Settings &gt; Clone Ranking </td>
          <td valign="top">Isolate Ranker</td>
        </tr>
        <tr> 
          <td valign="top"><a href="specs/help_acceptance_spec.htm" target="_blank">Clone 
            Acceptance Criteria </a></td>
          <td valign="top">Analysis Settings &gt; Clone Acceptance Criteria </td>
          <td valign="top"><dl>
              <dt>Decision Tool</dt>
              <dt>Quick Decision Tool</dt>
              <dt>Isolate Ranker</dt>
            </dl></td>
        </tr>
        <tr> 
          <td valign="top"><a href="specs/help_primer3_spec.htm" target="_blank"> 
            Primer Design Specification</a></td>
          <td valign="top">Analysis Settings &gt; Primer Designer </td>
          <td valign="top">Primer Designer</td>
        </tr>
        <tr> 
          <td valign="top"><a href="specs/help_polym_finder.htm" target="_blank">Polymorphism 
            Detector Specification</a></td>
          <td valign="top">Analysis Settings &gt; Polymorphism Detection</td>
          <td valign="top">Polymorphism Finder</td>
        </tr>
        <tr> 
          <td valign="top"><a href="specs/help_sequence_trim.htm" target="_blank">Sequence 
            Trimming Specification</a></td>
          <td valign="top">Analysis Settings &gt; Sequence Trimming </td>
          <td valign="top"><dl>
              <dt>Gap MapperLow Confidence Regions Finder </dt>
            </dl></td>
        </tr>
      </table>
      <font color="#FFFFFF">&nbsp;</font><font color="#FFFFFF">&nbsp;</font></div> 
      <h3><a name="upload_plates">Upload reference plate information</a></h3>
      <p>User Goal: Upload plate mapping information (location of target in a 
        96-well microtiter plate format, A01, A02 etc.) and target sequences into 
        ACE.</p>
      <p><em><strong>For non-HIP users:</strong></em></p>
      <ul>
        <li>Select cloning strategy to be used (see 'Cloning Project settings' 
          if new cloning strategy needs to be created).</li>
        <li><a
href="help_XML_files_format_description.htm"
target="_blank"	>Prepare XML files for submission</a>.</li>
        <li>Upload XML files:</li>
        <ul>
          <li> upload reference sequence information using <em>'Process &gt; Upload 
            plates information &gt; Upload reference sequence information from 
            file';</em></li>
          <li>upload plate mapping information using<em>'Process &gt; Upload plates 
            information&gt; Upload clone collection from file'</em> .</li>
        </ul>
      </ul>
      <p><em></em></p>
      <p><em><strong>For HIP users only: </strong></em>The HIP version of ACE 
        interacts with HIP&#8217;s cloning database, FLEX. The following section 
        is for HIP users only and is not available to other sites.</p>
      <ul>
        <li>Confirm with informatics team that plates have been submitted for 
          sequencing in FLEX. </li>
        <li>Select <em>'Process &gt; Upload plates information'</em>. 
          <ul>
            <li>Submit plate labels (case sensitive) (<em>Note: </em>only plates 
              cloned using the same cloning strategy can be uploaded together). 
            </li>
            <li>Choose from next step in plate processing (available selection: 
              'Run end reads' and 'Run clone evaluation').</li>
            <li>Choose 'Start' Codon (available selection: ATG; Natural - as submitted 
              from GenBank for reference sequence. If your clones have been normalized 
              with start codon other than 'ATG' please contact informatics team).</li>
            <li>Choose Fusion 'Stop' Codon (available selection: GGA; TTG. If 
              your clones have been normalized with different fusion codon please 
              contact informatics team).</li>
            <li>Choose Closed 'Stop' Codon (available selection: TGA; TAA; TAG; 
              Natural).</li>
            <li>Choose cloning vector. If your vector is not available go to <em>'Cloning 
              project settings > Add vector'</em> before proceesing. 
            <li>Choose 5' upstream and 3' downstream linkers.</li>
            <li>Choose project.</li>
          </ul>
        </li>
      </ul>
      <p>Upon finishing the upload ACE will send e-mail report to the user.</p>
      <p><a href="#upload_plates">Back</a> </p>
      <h3>Operational Steps</h3>
      <p>Once all the above parameters are entered in ACE the application is ready 
        to recieve sequence data and begin to analyze the clones. 
      <h3><a name="trace_upload">Trace file upload</a></h3>
      <p>Uploading the trace files to ACE is a critical operation that links the 
        unique plate name created by the sequencing facility to the physical plate 
        of clones and reference information stored in ACE. </p>
      
      <li>Copy trace files into directory specified in <a href="help_ACEConfigurationFile.html" target="_blank">ACE 
        configuration file</a> (TRACE_FILES_TRANSFER_INPUT_DIR). </li>
      <li>Create mapping file, atab delimited text file with one record per plate. 
        Each record should contain two fields: 
        <p>&lt;sequencing facility plate label>Tab< HIP plate label></li>
      <li>In ACE go to <em>'Trace Files &gt; Create Renaming File'</em>. Select 
        mapping file, <a
href="help_TraceFilesNamingFormats.html" target="_blank">trace files' naming format</a> 
        and type of reads. The 'Internal reads' option currently implemented only 
        supports primers designed by ACE. Click 'Submit' button. ACE writes 'renaming_xxx.txt' 
        file into the TRACE_FILES_TRANSFER_INPUT_DIR directory, the same file 
        is sent to user by e-mail. Renaming file contains one entry per trace 
        file in the format: <original file name><em> &lt;Trace File name&gt; Tab 
        </em>&lt;ACE recognizable file name>. Make sure that the number of entries 
        in renaming file is equal to the number of trace files to be submitted. 
        If the renaming file is empty or number of the entries is less than expected 
        one of the following problems occurred: 
        <ul>
          <li>Error in mapping file</li>
          <li>The plate indicated in mapping file was notsubmitted to ACE .</li>
          <li>There is no trace file name in the selected format. </li>
          <li>Some of the files have not been named properly, for example, some 
            trace files for plate have wrong position annotation. </li>
        </ul>
      </li>
      <li>Upload trace files on ACE server: <em>use 'Trace Files &gt; Upload Trace 
        Files'</em>, select renaming_xxx.txt file created by ACE on the previous 
        step. ACE will send user an e-mail when transfer of the trace files is 
        completed.</li>
      <p>&nbsp;</p>
      
   
      <h3><a name="data_processing">Sequencing data processing and clone evaluation</a></h3>
      <ul>
        <li><a href="#end_read_pr">End Read Processing</a></li>
        <li><a href="#assembler">Sequence Assembly </a></li>
        <li><a href="#discrepancy">Discrepancy Finder</a></li>
        <li><a href="#polym">Polymorphism Finder</a></li>
        <li><a href="#isolate">Isolate Ranker </a> </li>
        <li><a href="#mapper">Gap Mapper</a></li>
        <li><a href="#lqr">Low Confidence Regions Finder</a></li>
        <li><a href="#finish">Finished Clones</a></li>
      </ul>
      <p><a name="end_read_pr"><strong>End Read Processing</strong></a> </p>
      <p>End reads are treated slightly differently from internal reads because 
        some users employ them to select the best candidate from multiple isolates 
        for a single target. End reads processing consists of two steps: (1) placing 
        end read order; (2) uploading of the end reads in ACE. 
      <ul>
        <li>To <strong>order end reads </strong>select '<em>Process &gt; Read 
          Manipulation &gt; Request End Reads Sequencing</em>', choose vector 
          from drop-down list and click 'Submit' button, select universal primers 
          for forward and reverse reads and select plates to sequence. Be sure 
          to select proper primers, because sequencing primer orientation for 
          vector (see <a href="#ace_configuration">'Cloning project settings'</a> 
          section for details) determines the way ACE chooses the strand of the 
          sequence while building alignment to target sequence in <a href="http://www.emboss.org"  target="_blank"><em>needle</em></a>. 
          E.g. if you mistakenly specified M13R as a forward primer while in fact 
          you used M13F for sequencing, ACE will use complement of the end read 
          sequence to build the alignment which will show no similarity to the 
          target sequence. If you run only forward or only reverse reads select 
          'NONE' option from drop-down list when choosing corresponding primer.</li>
        <li>After traces have been transfered to ACE server (see '<a href="#trace_upload">Trace 
          files upload' </a>) user needs to <b>submit end reads</em> </b>use<b> 
          '</b><em>Process &gt; Read Manipulation &gt; Check Quality and Distribute 
          End Reads</em></em>' and specify plate name(s). ACE distributes all 
          'inactive' and internal reads chromate files into appropriate directories 
          ('inactive' traces correspond to empty wells and controls). End read 
          trace files for the requested plate(s) that <a href="help_ACE_overview.htm#end_read_pr" target="_blank">pass 
          the quality check</a> get distributed into corresponding clone directories 
          and their data get submitted into ACE database. ACE sends the user an 
          e-mail with attached report listing all traces that did not pass quality 
          check. This report can be used to upload low quality end reads as 'internal' 
          reads for analysis purposes by using '<em>Process &gt; Read Manipulation 
          &gt; Submit low quality end reads</em>' (internal reads are not checked 
          for quality in ACE ; rather, they are stored as trace files in the clone 
          directories). </li>
      </ul>
      <p><a name="assembler"><strong>Sequence Assembly </strong></a></p>
      <p>There are two ways to <strong>run assembler</strong> in ACE:</p>
      <ul>
        <li> '<em>Process &gt; Read Manipulation &gt; Run Assembler for End Reads</em>'</li>
        <li> '<em>Process &gt; Read Manipulation &gt; Assemble Clone Sequences</em>'</li>
      </ul>
      <p>The first option is used after end reads have been submitted only. It 
        applies additional pocessing steps when only one end read of acceptable 
        quality and target sequence is short (&lt; 800 bp). In such cases, ACE 
        extracts the end read sequence from the database, aligns it with the target 
        sequence, verifies that complete coverage is achieved (at least 20 bases 
        upstream / downstream of target sequence are covered by end read sequence) 
        and submits the end read sequence as 'assembled' sequence into the database 
        if coverage is given.</p>
      <p>In both cases the user is asked to:</p>
      <ul>
        <li>select library for vector trimming (see '<a href="help_ACE_overview.htm#assembler" target="_blank">ACE 
          overview</a>' for description of vector trimming and <a href="help_ACEConfigurationFile.html">ACE 
          configuration file</a> for vector libraries setup); </li>
        <li>determine how low quality reads should be treated; </li>
        <li> set parameters for the quality trimming of independent sequencing 
          reads during the assembly. </li>
      </ul>
      <br> </p> <p><a name="discrepancy"><strong>Discrepancy Finder</strong></a> 
      </p>
      <p>To run Discrepancy Finder (see<a href="help_ACE_overview.htm#discrepancy" target="_blank"> 
        ACE overview </a> for details) use '<em>Process &gt; Evaluate Clones &gt; 
        Run Discrepancy Finder</em>' and specify items that should be analyzed: 
        (a) plate label(s) (case insensitive) - all clones from submitted plate(s) 
        will be analyzed; (b) clone IDs; (c) clone sequence IDs (user can get 
        clone sequence ID by running '<a href="help_ReportRunner.html" target="_blank">General 
        Report</a>'), and set quality cut-off based on Phred score (default set 
        to 25). </p>
      <p><a name="polym"><strong>Polymorphism Finder</strong></a> </p>
      <p>To run Polymorphism Finder (see<a href="help_ACE_overview.htm#polym" target="_blank"> 
        ACE overview </a> for details) use '<em>Process &gt; Evaluate Clones &gt; 
        Run Polymorphism Finder</em>', specify items that should be analyzed and 
        choose <a href="specs/help_polym_finder.htm" target="_blank">Polymorphism 
        Finder Specification</a>. ACE creates three intermediate files and puts 
        them into a directory specified by POLYMORPHISM_FINDER_DATA_DIRECTORY 
        variable in <a href="help_ACEConfigurationFile.html" target="_blank">ACE 
        configuration file</a>. These files will be automatically transferred 
        to the dedicated server that hosts GenBank database(s), and results of 
        Polymorphism Finder run performed on the server will be transferred back 
        and uploaded into ACE. Please contact ACE administrator for the details 
        when and how the data file transfer is arranged. You can view Polymorphism 
        Finder results by using comprehensive plate viewer ('<em>View &gt; Plate 
        Results</em>'), click on well link for your clone, click '<a href="reports/discr_report.html" target="_blank">Discrepancy 
        Report</a>' button on sample view for clone sequence - the value in 'Polymorphism' 
        column for each discrepancy will be changed to 'Y' (discrepancy id known 
        polymorphism) or 'N' (no hit for discrepancy was found). <a href="reports/polym_report_discr.html" target="_blank">'Details'</a> 
        button gives you access to the list of GenBank GI record(s) that were 
        found as hits for the particular discrepancy.</p>
      <p><a name="isolate" ><strong>Isolate Ranker</strong> </a> </p>
      <p>To run Isolate Ranker (see<a href="help_ACE_overview.htm#isolate" target="_blank"> 
        ACE overview </a> for details) select '<em>Process &gt; Evaluate Clones 
        &gt; Run Isolate Ranker</em>' and specify plate label(s) (case insensitive) 
        and process specifications (see <a href="#create_spec">'Analysis settings'</a> 
        section for details). Isolate Ranker can operate on isolates for the same 
        target located on different plates; however, these plates must be run 
        by module at the same time. Isolate Ranker will sort isolates based on 
        the most recent contig for the particular clone, e.g., if clone has end 
        reads, collection of contigs and gaps defined by Gap Mapper and several 
        clone sequences assembled under different conditions (see Assembler description 
        for details) isolate will be ranked based on LAST assembled sequence. 
        The result is shown in <a
href="image/image_Fig4.htm" target="_blank">'comprehensive plate viewer'</a>.</p>
      <p><a name="mapper"><strong>Gap Mapper</strong></a> </p>
      <p>User may run 'Gap Mapper' to determine gaps in clone coverage (see<a href="help_ACE_overview.htm#mapper" target="_blank"> 
        ACE overview </a> for details). </p>
      <p>To operate 'Gap Mapper' use '<em>Process &gt; Evaluate Clones &gt; Run 
        Gap Mapper</em>', enter clone IDs and select <a href="#assembler">sequence 
        assembly parameters</a>. Sequence assembly These parameters should set 
        because Gap Mapper invokes sequence assembler as first step of processing 
        (consult <a href="help_ACE_overview.htm#mapper" target="_blank"> ACE overview)</a>. 
        User can instruct ACE to calculate low confidence regions (LCR) of assembled 
        contigs by '<em>Run LQR Finder on contig sequences</em>' option. LCR and 
        gaps defined by Gap Mapper will become tagets for primer(s) design to 
        finish clone sequencing. Gap Mapper can be run in the &quot;try mode&quot;; 
        in this case the information about gaps ans LCRs is emailed to the user 
        as <a href="reports/gap_mapper_report.html" target="_blank">tab delimited 
        text file</a>, but is not stored in database and; hence, cannot be used 
        later for primer(s) design. </p>
      <p>&nbsp; </p>
      <p><a name="lqr" ><strong>Low Confidence Region Finder</strong></a></p>
      <p>If an assembled clone sequence has low confidence discrepancies (<a href="help_generalhelp.jsp#discrepancy">Discrepancy 
        Finder</a>) that should be resolved before clone acceptance, the user 
        should run 'Low Confidence Region Finder' to determine the presence of 
        a low confidence region(s) (LCR), which allows for region specific design 
        of internal primer(s) in 'Primer Designer' (see<a href="help_ACE_overview.htm#lqr" target="_blank"> 
        ACE overview </a> for details). 
      <p>To access this functionality use '<em>Process &gt; Evaluate Clones &gt; 
        Run Low Confidence Regions Finder in Clone Sequences</em>', specify clone 
        IDs and select specification (see <a href="#create_spec">'Analysis settings'</a> 
        section for details). If you are going to repeat end reads using universal 
        primers, set the number of bases that are expected to be covered by these 
        end reads. 'Low Confidence Regions Finder' will not define LCR for these 
        regions. The Finder can be run in &quot;try mode&quot;; in this case the 
        information about LCRs is only emailed to the user as a <a href="reports/lqf_report.html" target="_blank">tab 
        delimited text file</a>, but not stored in database and cannot be used 
        later for region specific primer(s) design. 
      <p> <a name="finish"><strong>Finished clones</strong></a> 
      <p>Once user decides that a clone has met acceptance or rejection criteria 
        (based on Decision Tool report or manual inspection), it is possible to 
        assign a final status for the clone in ACE. This prohibits further processing 
        of clone data, thus reducing the job size and focusing the efforts on 
        clones still pending. A clone with status other than 'In Process' (default 
        clone status assigned on clone submission into ACE) is excluded from the 
        following processes: (a) assembly; (b) primer design and order; (c) polymorphism 
        search; (d) data deletion (see <a href="#delete_data">Data clean-up</a> 
        section for details). To set <strong>clone final status</strong> select 
        '<em>Process &gt; Set Final Clones Status</em>' and specify clone IDs 
        and final status. Optionally, an XML file with results of clone analysis 
        can be created. User can change clone status back to 'In Process' at any 
        time, which allows full processing again. 
      <p>&nbsp; 
      <h3><a name="report_data">Reports</a></h3>
      In the report section 6 types of reports can be requested from ACE, all 
      will be send to user by e-mail as attached, tab delimited file(s), and where 
      noted below also created on-line in ACE. <ul>
        <li>'<strong>Quick Decision Tool</strong>' report is a simplified version 
          of the <a href="help_ACE_overview.htm#decision">Decision Tool</a> which 
          determines if clone(s) meets user defined acceptance criteria. To run 
          the report select <em>Reports &gt; Quick Decision Tool'</em> and specify 
          user acceptance criteria and set of clones. Execution time is proportional 
          to the number of specified clones. The 'Quick Decision Tool' provides 
          feedback online in addition to sending a report by e-mail.</li>
        <li>To run <a href="help_ACE_overview.htm#decision" target="_blank"><strong>Detailed 
          Decision Tool </strong></a>choose <em>'Reports &gt; Detailed Decision 
          Tool'</em> and fill the query form. There is no limit on the number 
          of clones that can be processed.</li>
        <li>'<strong>Mismatched Clones</strong>' report helps to detect cases 
          of misidentified input data such as (a) mislabeled or rotated plates; 
          (b) cross-contamination on the clone plate; (c) systematic errors. End 
          read sequences are compared against user selected database using NCBI 
          BLAST. Database should contain reference sequences for the project and 
          vector sequences. To run the report select <em>' Reports &gt; Mismatched 
          Clones'</em>, specify clones and database. There is no limit on the 
          number of clones that can be processed at any given time.</li>
        <li><strong>'General report'</strong> provides a snapshot of <a href="help_ReportRunner.html"
target="_blank"> requested information</a> for the specified set of query objects, 
          which can be either plate(s), clone IDs, user reference sequence IDs, 
          assembled clone sequence IDs (clone sequence IDs can be extracted from 
          General Report or Detaile Decision Tool report files). To run general 
          report choose <em>'Reports &gt; General Report'</em> and fill the query 
          form. There is no limit on the number of query objects that can be processed 
          .</li>
        <li>The <strong>'Trace File Quality' </strong>report allows to examine 
          quality of existing trace files, and can be accessed at <em>'Reports 
          &gt; Trace Files Quality Report'</em>. Either plate labels or clone 
          IDs can be provide, and for each clone trace file ACE extracts Phred 
          confidence base scores and determines whether a read meets minimum quality 
          requirements: (a) the read must be longer than the user-defined minimum 
          length; (b) the average confidence score for all non-ambiguous bases 
          between the first and last base must be above the minimum confidence 
          score. The value of these thresholds are defined in <a href="help_ACEConfigurationFile.html" target="_blank">ACE 
          configuration file</a>. There is no limit on the number of clones that 
          can be processed.</li>
        <li>To obtain a list of end reads that failed and should repeated the 
          user can invoke the <strong>End Read Report</strong> '<em>Reports &gt; 
          End Reads Repor</em>t' for any plate(s).</li>
      </ul>
      <p><a href="#report_data">Back</a> </p>
      <h3><a name="primers">Primer design and management</a></h3>
      <p>This set of features allow users to: (a) design gene-specific sequencing 
        primers; (b) select specific primer(s) for synthesis; (c) order primers; 
        (d) track primer plates;</p>
      <ul>
        <li>To <strong>order gene-specific primers</strong> use '<em>Process &gt; 
          Internal Primer Design and Order &gt; Run Primer Designer</em>', specify 
          how to extract clone information. User can submit (a) plate labels - 
          primers will be designed for all clones from these plates; (b) clone 
          IDs; (c) ACE reference sequence IDs; (d) user reference sequence IDs*. 
          Next choose specification for Primer3 and define what region of the 
          target sequence the primer should cover. Select '<em>Design primers 
          for Reference Sequence</em>' option if you would like to design primers 
          for a primer walk independent of (end read) coverage. Select '<em>Design 
          primers for Stretch Collection</em>' to design primers needed to complete 
          coverage (fill in gaps or cover low confidence discrepancy region). 
          Stretch collection is stored in ACE after Gap Mapper or Low Confidence 
          Regions Finder was run for a particular clone. ACE extracts only the 
          most recent stretch collection defined for a clone to be used in primer 
          design based on stretch collections 
          <p>Primer Designer can be run in try mode, in this case information 
            about designed primers is only sent to user in <a href="reports/primer_design.htm" target="_blank">tab 
            delimited text file</a>, however, no information is stored in database.<br>
            *<strong><em>Note:</em></strong> Options (c) and (d) should not be 
            used in conjunction with '<em>Design primers for Stretch Collection</em>' 
            mode. </li>
        <li>Designed primers must be approved by user to be included in vendor 
          order. To <strong>approve primers</strong> select '<em>Process &gt; 
          Internal Primer Design and Order &gt; Approve Internal Primers</em>', 
          specify clone IDs and select the kind of primers (designed for reference 
          sequence or for stretch collection) that </li>
        should be displayed for approval. User can approve primers designed under 
        both options for any clone for primer synthesis</li> 
        <li>To <strong>create a primer order</strong> use <em>'Process &gt; Internal 
          Primer Design and Order &gt; Order Internal Primers'</em>, specify clone 
          IDs and choose type of approved primers that should be included in the 
          order. Available options are (a) only primers designed for stretch collections; 
          (b) only primers designed for reference sequence; (c) primers designed 
          for stretch collection(s) together with primers for reference sequence. 
          ACE selects all primers approved for the specified clones according 
          to the user selection and maps them in A01-H12 96-well plate format. 
          User has the option to specify the first and last primer position on 
          a plate, preserving empty wells for controls if desired. ACE supports 
          multipal output formats to create an order file compatible with vendor 
          order sheets (<a href="reports/sample1_order_oligo.html" target="_blank">sample_order_file1</a>, 
          <a href="reports/sample2_order_oligo.html" target="_blank">sample_order_file_2</a>)</li>
        <li>To store <strong>information</strong> regarding <strong>primer plate</strong> 
          orders or usage select '<em>Process &gt; Internal Primer Design and 
          Order &gt; Track Oligo Plate</em>', choose plate status and enter comments 
          (optional). This step is requered if user intends to use primers more 
          than once (i.e., second clone for target sequence)<br>
        </li>
      </ul>
      <h3><a name="view_data">Real time views</a></h3>
	  Two different view optiona are available. While under <em>'View'</em> more 
      general, plate, clone, or project oriented data provided, the outcome of 
      specific processes visually accessible under '<em>Process &gt; View Process 
      Results</em>'. 
      <ul>
        <li><strong><a
href="image/image_Fig4.htm" target="_blank">Comprehensive plate viewer</a></strong> 
          gives access to complete information about a plate. Select '<em>View 
          &gt; Plate Results</em>', enter plate label (case insensitive), check 
          'Show Isolate Ranker Output' option. </li>
        <li>To view<strong> plate history</strong> select '<em>View &gt; Plate 
          history</em>' and enter plate label (case insensitive);</li>
        <li>To view <strong>plate description</strong> select '<em>View &gt; Plate 
          Description</em>' and enter plate label (case insensitive). The view 
          displays (1) plate label; (2) plate unique ID; (3) plate type (96 well 
          plate); (4) link to cloning strategy description; (5) for each well: 
          (a) well number; (b) sample type - Isolate, Control, Empty; (c) clone 
          ID; (d) clone final status; (e) description of the last process run; 
          (f) link to reference sequence description; (g) link to clone sequence 
          description if available. </li>
        <li>To view <strong>clone processing history </strong>select '<em>View 
          &gt; Clone History</em>' and enter clone IDs. The interface gives the 
          user information about all steps a clone has passed in ACE, links to 
          step specific specification, date and time of execution for each step, 
          and user performed task.</li>
        <li>To view <strong>all plates uploaded into ACE</strong> select '<em>View 
          &gt; Plates</em>'. Plates are grouped by project name (alpha numerical 
          order). Click on the check box located before project name to hide all 
          plates related to the project.</li>
        <li>To view <strong>any analysis specifications </strong> stored in ACE 
          select 'View &gt; Process Configurations'. <strong><em>Note: </em></strong>user 
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
        <li> To view <strong>primer designed for clone(s)</strong> select '<em>Process 
          &gt; View Process Results &gt; View Internal Primers</em> ', submit 
          clone IDs and indicate type of primers: primers designed for reference 
          sequence or primers designed for stretch collection (see description 
          of GapMapper module for details). <a href="screen_shots/report_view_primers.html"
target="_blank">Sample report</a>.</li>
        <li> To view <strong>all primer ordered for a clone</strong> select '<em>Process 
          &gt; View Process Results &gt; View Oligo Order(s) for Clone(s)</em>'. 
          <a target="_blank"
href="screen_shots/report_ordered_primers.html">Sample report</a>.</li>
        <li> To view <strong>ordered oligo plate(s)</strong> select '<em>Process 
          &gt; View Process Results &gt; View Oligo Plate</em>'. <a target="_blank" href="screen_shots/report_oligo_plate.html">Sample 
          report.</a></li>
        <li> To view <strong>all contig collections</strong> created by Gap Mapper 
          for a clone select '<em>Process &gt; View Process Results &gt; View 
          all contig collections </em>'. Multiple contig collections are created 
          when Gap Mapper is launched several times with different specification 
          or with additional trace files.</li>
        <li> To view <strong>only the last contig collection </strong>designed 
          for the clone by Gap Mapper select <em>Process &gt; View Process Results 
          &gt; View latest contig collection</em>. <a href="screen_shots/report_view_contigs.html" target="_blank">Sample 
          report.</a> </li>
        <li> To view <strong>low confidence region(s)</strong> for a clone created 
          by Low Confidence Finder select <em>Process &gt; View Process Results 
          &gt; View low confidence regions for clone sequences' </em> and specify 
          clone IDs. <a href="screen_shots/report_view-lqr.html" target="_blank">Sample 
          report.</a> </li>
      </ul>
      <p><a href="#view_data">Back</a> </p>
	  
	   
      <h3><a name="delete_data">Data clean up</a></h3>
      <p>ACE allows removal of trace files and clone information from the database. 
        This is useful in cases when: (a) wrong plates have been submitted to 
        ACE; (b) errors occurred in clone mapping on the plate; (c) plates have 
        been mislabeled; (d) incorrect data from sequencing facilities, etc.</p>
      <ul>
        <li>To <strong>delete plate</strong> select '<em>Process &gt; Delete Data 
          &gt; Delete plate</em>' and submit plate label (case insensitive).</li>
        <li>To <strong>delete clone forward and reverse end reads</strong> select 
          '<em>Process &gt; Delete data &gt; Clone forward and reverse end reads 
          from database</em>' and submit plate label (case insensitive) or clone 
          IDs. This action deletes records from the database; however, trace files 
          for these end reads stored on the hard drive are not affected.</li>
        <li>To <strong>delete forward end reads</strong> only select '<em>Process 
          &gt; Delete data &gt; Delete clone forward end reads from database</em>' 
          and submit plate label (case insensitive) or clone IDs. This action 
          deletes records for the forward reads from the database; however, trace 
          files for these forward end reads stored on the hard drive are not affected.</li>
        <li>To <strong>delete clone reverse end reads </strong> select <em>'Process 
          &gt; Delete data &gt; Delete clone reverse end reads from database</em>' 
          and submit plate label (case insensitive) or clone IDs. This action 
          deletes records for the reverse reads from the database; however, trace 
          files for these reverse end reads stored on the hard drive are not affected.</li>
        <li>To <strong>delete clone sequence</strong> select '<em>Process &gt; 
          Delete data &gt; Delete clone sequence from database</em>' and submit 
          clone ID or clone sequence ID (run General Report to get clone sequence 
          ID).</li>
        <li>To <strong>delete trace files</strong> from the hard drive, first, 
          select '<em>Process &gt; Delete data &gt; Get trace file names</em>'. 
          ACE will send e-mail with attached file that contains trace file names 
          for the submitted clone IDs. Save this file. Next select '<em>Process 
          &gt; Delete data &gt; Delete trace files from hard drive (no recovery 
          possible)</em>' and submit saved file. ACE will permanently delete traces 
          from the hard drive.</li>
        <li>To <strong>remove trace files</strong> to storage directory first 
          get file with trace file name from ACE (see above). Next select <em>'Process 
          &gt; Delete data &gt; Move trace file from clone directory into temporary 
          directory (allows trace files recovery)</em>' - traces will be moved 
          to the storage directory. </li>
        <li>To <strong>delete intermediate files</strong> created during clone 
          sequence assembly by Phred/Phrap from hard drive select '<em>Process 
          &gt; Delete data &gt; Clean-up hard drive</em>'.<br>
        </li>
      </ul>
      <p><a href="#delete_data">Back</a> </p>
     </td>
  </tr>
  
  <tr> <td colspan="2"><p class="disclaimer">&copy; 2005 by Helen 
          Taycher  
          last changed March 17, 2007</p></td></tr></table>
</body>
</html>



 
