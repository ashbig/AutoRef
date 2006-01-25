<%@page contentType="text/html"%>

<%@ page import="edu.harvard.med.hip.bec.*" %>
<html>
<head><title>JSP Page</title>
<style>
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


<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        
    <td > <font color="#008000" size="5"><b> ACE help</font> 
      <hr>    <p>    </td>
    </tr>
</table>

<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%"><html:errors/></td>    </tr>
  </table>
  </center>
</div>


<table width="74%" border="0" cellpadding="2" cellspacing="2" align="center">
  <tr> <td>
  <p class="MsoNormal"><b>ACE, the Automatic Clone Evaluation software, 
  facilitates the process of making sequence-verified, single-gene cDNA 
  collections.</b></p>
  <p class="MsoNormal"><b>ACE can help you to answer the following questions:</b></p>
      <p class="MsoNormal" style="margin-bottom: 0in">&nbsp;
      <ul style="margin-top: 0in; margin-bottom: 0in" type="disc">
        <li class="MsoNormal"><i>Which isolates match the reference sequence?</i></li>
        <li class="MsoNormal"><i>What base-pair changes, insertions, or deletions 
          are detected?</i></li>
        <li class="MsoNormal"><i>Which of several isolates is the best one to 
          choose for the cDNA collection?</i></li>
      </ul>
  <p class="MsoNormal"><b>To do this, ACE software tools can perform the 
  following steps:</b></p>
  <p class="MsoNormal"><i>Clone Sequence Assembly</i></p>
  <p class="MsoNormal">To assemble sequences for each of your clone isolates, 
  you'll first import sequence trace files into ACE for assembly and quality 
  control.&nbsp; In the early stages of clone evaluation, the trace files will 
  probably be end reads that is, sequences obtained with vector-specific 
  primers that may or may not cover the entire clone.&nbsp; Later, the trace files 
  will include internal sequence reads.&nbsp; The ACE &quot;Assembler&quot; tool will 
  assemble sequences so they can be compared to reference sequences.</p>
  <p class="MsoNormal"><i>Define gaps in the sequence (gap mapping)</i></p>
  <p class="MsoNormal">After end-reads and have been imported and assembled, you 
  can use the Gap Mapper tool to identify gaps in the sequence (for example, for 
  large cDNA clones where end-reads with vector-specific primers will not cover the 
  whole clone).</p>
  <p class="MsoNormal"><i>Primer Design for Internal Sequencing</i></p>
  <p class="MsoNormal">After end-reads have been imported and assembled, you can 
  use the Primer Designer tool to design sequence-specific primers for your 
  clone and create a digital array of the primers in an appropriate format (such 
  as 96-well plate format).&nbsp; The sequence reads obtained with internal primers 
  will then be used for another round of sequence assembly and evaluation.</p>
  <p class="MsoNormal"><i>Evaluate Clones based on Sequence Quality and 
  Discrepancies</i></p>
  <p class="MsoNormal">After each round of sequencing (end-reads or internal 
  primers), ACE can assemble sequences and use your criteria to (1) throw out 
  low-quality sequence regions and (2) identify clones that have mutations or&nbsp; 
    discrepancies as compared with the reference sequence.&nbsp; </p>
  <p class="MsoNormal"><i>Choose the Best Clones to send on through the pipeline 
  ( Isolate Ranking )</i></p>
  <p class="MsoNormal">After assembling sequences and identifying discrepancies, 
  you can ask ACE to accept or reject clones based on various user-defined 
  parameters. You can choose to throw out isolates for which high-quality 
  sequences reveal the presence of non-conservative amino acid substitutions, 
  non-sense mutations, or other unacceptable discrepancies.</p>
<p><strong>ACE Tutorial </strong>(for <strong>HIP users ONLY</strong>)</p>
<ol>
        <li>Create a sequencing plate and send for sequencing.</li>
        <li>Let the informatics team know that you have sent plates for sequencing. 
          Provide the following information: (1) Plate labels (case sensitive); 
          (2) the name of sequencing facility you are using.</li>
  <li>Get conformation from informatics team that database reflects request 
    from step 2.</li>
        <li><a name="back_upload_plates">&nbsp;</a><a href="#upload_plates">Upload 
          plates information</a> into ACE (select &quot;Upload template plates 
          information&quot; from Process menu). Submit requested information and 
          wait for e-mail that the plates were uploaded.</li>
        <li><a name="back_order_er">&nbsp;</a><a href="#order_er">Enter into ACE 
          which end reads were ordered </a>(forward, reverse, or both) and what 
          primers were used (choose from the list). </li>
        <li><a name="back_submit_trace_files">&nbsp;</a><a href="#submit_trace_files">Submit 
          trace files. </a>( be sure that you have used the correct naming convention).</li>
        <li><a name="back_er_wrapper">&nbsp;</a><a href="#er_wrapper">Run &quot;End 
          reads Wrapper&quot;</a> to evaluate end readds.</li>
        <li><a name="back_assembler">&nbsp;</a><a href="#assembler">Run &quot;Assembler 
          for end reads</a>&quot;.</li>
        <li><a name="back_is_ranker">&nbsp;</a><a href="#is_ranker">Run &quot;Isolate 
          Runker</a>&quot; to determined which clones to send through the pipeline.</li>
        <li>Ask ACE to provide list of <a name="back_general_report">&nbsp;</a><a href="#general_report">cloneids 
          </a>for the best candidate to continue to working on..</li>
        <li>Use &quot;Primer Designer&quot; tool to <a name="back_primer_ds">&nbsp;</a><a href="#primer_ds">design 
          internal primers for clones</a></li>
        <li>Submit you trace files and use &quot;Assembler&quot; to <a href="#assembler">assemble</a> 
          sequences (see step 6).</li>
        <li>Ask ACE to provide list of Clone IDs for which <a name="back_er_failed_report">&nbsp;</a><a href="#er_failed_report">end 
          reads should be repeated </a> in order to get an assembled sequence.</li>
        <li><a name="back_gap_mapper">&nbsp;</a><a href="#gap_mapper">Run Gap 
          Mapper </a> to define regions where you still need more primers to get 
          assembled sequence for the clones.</li>
        <li>Use &quot;Primer Dsigner&quot; to <a href="#primer_ds">Design primers</a> 
          to cover gaps in sequence coverage and repeate steps 6, 12.</li>
        <li>Ask for a list of clones with low quality discrepancis run <a name="back_lqr">&nbsp;</a><a href="#lqr">Low 
          Quality Regions Finder</a>, design primers to cover low quality regions 
          and repeat steps 6, 12 and 17.</li>
        <li>AT ANY TIME - run &quot;Discrepancy finder&quot; to annotate clone 
          sequences .<br>
  </li>
</ol>
<hr>
 <h3><strong><a name="upload_plates">Upload plates     information</a> </strong> </h3>
 
<p>User Goal: Transfer plate mapping information and related ORF reference information 
  from FLEX into ACE.</p>
<p>User Actions: </p>
<ul>
  <li>Confirm with informatics team that your plates have been submitted for sequencing. 
  </li>
  <li>Upload plate information by selecting Process -> Upload template plates 
    information in ACE. Submit plate labels (case sencitive). Provide the following 
    information for the plate. (<em>Note: </em>only plates cloned using the same 
    Cloning Strategy can be uploaded together.)
    <ul>
      <li> Select Next step in plate processing (available selection: Run end 
        reads; Run clone evaluation);</li>
      <li> Start Codon (available selection: ATG; Natural - as submitted from 
        GenBank for reference sequence);</li>
      <li> Fusion Stop Codon (available selection: GGA; TTG);</li>
      <li> Closed Stop Codon (available selection: TGA; TAA; TAG; Natural );</li>
      <li> Vector (available selection: pDONR201; pDNR-Dual; pBY011; pDONR221);</li>
      <li> 5' upstream seqment &amp; 3' downstream segment;</li>
    </ul>
  </li>
</ul>
      <p>Please, contact informatics team if your Cloning Strategy cannot be described 
        by available values. ACE sends e-mail to the user with report indicating 
        sucess/failur to appload plate information.</p>
      <p><a href="#back_upload_plates">Back</a> </p>
      <h3><strong><a name="order_er">End Reads' sequencing order</a> </strong> </h3>
 
 
  
<p>User Goal: inform ACE ACE what type of end read he has orded and what primer 
  were used for sequencing. This information MUST be submitted before running 
  'End Reads Wrapper'.</p>
<p>User Action: </p>
<ul>
  <li>Select in ACE menu Process->Put request for end reads sequencing. </li>
  <li>Select vector that was used for cloning, click 'Submit' button. </li>
  <li>Select primers that will be used for sequencing, select 'NONE' as primer 
    type if this direction is not sequenced. Please, pay close attention to primers 
    chooce, the possible output of wrong primer selection - clone will be described 
    as 'No match' clone. </li>
  <li>Select plates. </li>
</ul>
<p>If your primers are not listed by ACE for the selected vector, please contact 
  informatics team. </p>
<P>ACE will send user e-mail after action will be performed. This e-mail contains as attachment naming files for end read (one file per plate/direction)</P>
  <p><a href="#back_order_er">Back</a>
  <h3><strong><a name="er_wrapper">End Reads wrapper</a> </strong> </h3>
 
<p>End Reads wrapper module performes the following actions </p>
<ul>
  <li>distribute all none active chromat files into appropriate directories (none 
    active chromat files are trace files that (a)are named not according to ACE 
    nomenculature; (b) trace files for empty wells; (c) trace files for controls); 
  </li>
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
User input information for the module - list of plates which trace files should be distributed.
<p>ACE will send user e-mail after action will be performed. </p>
<p><a href="#back_er_wrapper">Back</a>
  
  <h3><strong><a name="assembler">Assembler</a> </strong> </h3>
 
 ACE will send user e-mail after action will be performed. 
  <p><a href="#assembler">Back</a>
  
  <h3><strong><a name="is_ranker">Isolate Ranker</a> </strong> </h3>
 
 
  
<p><a href="#back_is_ranker">Back</a> 
 <h3><strong><a name="primer_ds">Primer Designer</a> </strong> </h3>
 
 
  <p><a href="#back_primer_ds">Back</a>
   
<h3><strong><a name="gap_mapper">Gap Mapper </a></strong> </h3>
  <p><a href="#back_gap_mapper">Back</a>
  
<h3><strong><a name="er_failed_report">Order list for reads repeat </a></strong> 
</h3>
 <p></p> <a href="#back_er_failed_report">Back</a>
  
   <h3><strong><a name="lqr">Low Quality Regions Finder</a> </strong> </h3>
 
 
  <p><a href="#back_lqr">Back</a>
<p>&nbsp; 
<h3><strong><a name="submit_trace_files">Trace Files Submission:</a></strong> </h3>
      <ul>
        <li>Delete all files from HIPDRIVE\Sequences for BEC\files_to_transfer 
          directory prior to upload. If you cannot find the directory on hip drive 
          contact Hip System Administrator.</li>
        <li>Load trace files into HIPDRIVE\Sequences for BEC\files_to_transfer 
          directory. </li>
        <li>Create mapping file of the following format: sequencing facility plate 
          label <em>Tab</em> HIP plate label.</li>
        <li>In ACE go to Trace Files -&gt; Create Renaming File. Select mapping 
          file, <a href="help_TraceFilesNamingFormats.html" onClick="window.open(href, 'GeneralReport', 'width=800,height=400,scrollbars=yes');">trace 
          files' naming format</a> and type of reads. The 'Internal reads' option 
          currently implemented only for HTMBC and primers designed by ACE. Click 
          'Submit' button. ACE writes 'renaming.txt' file into HIPDRIVE\Sequences 
          for BEC\files_to_transfer directory, the same file is sent to user by 
          e-mail. Renaming file contains one entry per each trace file in the 
          format: original file name<em> Tab </em>ACE file name. Make sure that 
          number of entries in 'renaming.txt' file is equal to the number of trace 
          files. If 'renaming.txt' file is empty or number of the entries is less 
          than expected one of the following problems occured: 
          <ul>
            <li>Plates indicated in mapping file have not been submitted into 
              ACE (for end reads option only).</li>
            <li>Trace files have not been named according to select format. </li>
            <li>Some of the files have not been named properly that will result 
              in the number of records in renaming file less than number of trace 
              files in HIPDRIVE\Sequences for BEC\files_to_transfer directory. 
              For example, some trace files for the plate have wrong position 
              annotation. </li>
          </ul>
        </li>
        <li>Upload trace files on server: Trace Files -&gt; Upload Trace Files. 
          Select renaming.txt file created by ACE on the previous step. ACE will 
          send user e-mail when finish transfer trace files. </li>
      </ul>
<p><a href="#back_submit_trace_files">Back</a> </p>
<h3>General report </h3>
<p>To get information available to ACE about clones user should run General report 
  for the list of cloneids/plate labels by selecting the following menu items 
  'Search-&gt;Genral Report. For running General report user: </p>
<ul>
  <li>selects type of input data he(she) provides (number of clones that user 
    can get information in a time is not limited)</li>
  <li> type of information about each clone he(she) would like to get. See <a href="help_ReportRunner.html" onClick="window.open(href, 'GeneralReport', 'width=800,height=400,scrollbars=yes');">help</a> 
    for more details.</P> </li>
  <li>report tab delimited file will be sent to user by e-mail.</li>
</ul>
<p><a href="#back_general_report">Back</a> </p>
</td></tr></table>
</body>
</html>
