<%@page contentType="text/html"%>
<%@ page errorPage="ProcessError.do"%>
<html>
<head><title>JSP Page</title></head>
<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<p><P>
<br>
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
<p><strong>ACE (Automatic Clones Evaluation)</strong> - modular software package 
  to automate and facilitate the process of biologically evaluating cDNA clones. 
  ACE is designed to perform the following operations:</p>
<ul>
  <li><strong><em>Clone Sequence Assembly</em></strong> - First step is presorting 
    the sequence reads from these nearly identical clones before assembling the 
    sequences, otherwise it will appear to most sequence assembly software that 
    they all came from the same clone. (This is because assembly software was 
    originally developed for genomic sequencing and therefore assumes that all 
    of the multiple sequence reads belong to the same genome). Second step is 
    assembling of clone sequence. 
    <p></p>
  </li>
  <li><em><strong>Discrepancies annotation</strong></em> - ACE evaluates each clone 
    in comparison to the expected reference sequence to categorize all discrepancies 
    and determines if the clones are acceptable for protein expression based on 
    user specified criteria. 
    <P></P>
  </li>
  <li> <strong><em>Isolates Picking</em></strong> &#8211; In situations where 
    multiple isolates for the same gene have been created, it is advantageous 
    to sequence the isolates sequentially (instead of in parallel) in order to 
    save the costs of sequencing clones unnecessarily. ACE approach here is to 
    sequence all clones with end reads only and then to pick a &#8220;best candidate&#8221; 
    for full-length sequencing. 
    <P></P>
  </li>
  <li><strong><em>Primer Design for Internal Sequencing</em></strong> &#8211; 
    Protein expression cloning projects often use primer walking as a sequencing 
    strategy because the clones are rarely in vectors that are optimal for other 
    sequencing methods, such as transposon insertion. Additionally, the ability 
    to use the same set of primers on multiple isolates of the same gene lowers 
    the relative cost of this approach. Moreover, the expected sequence of the 
    clones is known, so the primers can be designed and acquired in advance. 
    <P></P>
  </li>
  <li><strong><em>Gap Mapping</em></strong> - would the sequence assembly was 
    not successful as a result of wrong primers' placement or failure of some sequencing 
    reads ACE is able to locates segments of sequence that require additional 
    reads and design primers to cover these regions only.</li>
</ul>
<p><strong>ACE Tutorial </strong>(for <strong>HIP users ONLY</strong>)</p>
<ol>
  <li>Create sequencing plate and send it for sequencing.</li>
  <li>Inform informatics team that plates have been send for sequencing. The following 
    information should be provided: (a) plate labels (case sensitive); (b) sequencing 
    facility name.</li>
  <li>Get conformation from informatics team that database reflects request 
    from step 2.</li>
  <li><a name="back_upload_plates">&nbsp;</a><a href="#upload_plates">Upload plates 
    information</a> into ACE (Process-&gt;Upload template plates information). 
    Wait for e-mail that confirms plate upload.</li>
  <li><a name="back_order_er">&nbsp;</a><a href="#order_er">Inform ACE which end 
    reads were ordered </a>(forward, reverse, both) and which primers have been 
    used by go to Process-&gt; Put request for end reads sequencing. </li>
  <li><a name="back_submit_trace_files">&nbsp;</a><a href="#submit_trace_files">Submit 
    trace files. </a></li>
  <li><a name="back_er_wrapper">&nbsp;</a><a href="#er_wrapper">Run End reads 
    Wrapper</a></li>
  <li><a name="back_assembler">&nbsp;</a><a href="#assembler">Run assembler for 
    end reads</a></li>
  <li><a name="back_is_ranker">&nbsp;</a><a href="#is_ranker">Run Isolate Runker</a></li>
  <li>Get <a name="back_general_report">&nbsp;</a><a href="#general_report">cloneids 
    </a>for the best candidate for full-length sequencing, <em> e.i.</em> clones 
    that were evaluated by ACE as acceptable by were not assembled based on end 
    reads.</li>
  <li><a name="back_primer_ds">&nbsp;</a><a href="#primer_ds">Design primers for 
    clones</a></li>
  <li>Submit internal reads trace files and <a href="#assembler">assemble</a> 
    sequences (see step 6)</li>
  <li>For clone that failed assembly find out wether <a name="back_er_failed_report">&nbsp;</a><a href="#er_failed_report">end 
    reads should be repeated </a> to get assembled sequence and repeat them.</li>
  <li><a name="back_gap_mapper">&nbsp;</a><a href="#gap_mapper">Run Gap Mapper 
    </a>for not assembled clones</li>
  <li><a href="#primer_ds">Design primers</a> to cover gaps in sequence coverage 
    and repeate steps 6, 12.</li>
  <li>For clones that have low quality discrepancy run <a name="back_lqr">&nbsp;</a><a href="#lqr">Low Quality 
    Regions Finder</a>, design primers to cover low quality regions and repeat 
    steps 6, 12 and 17.</li>
  <li>Discrepancy finder can be run at any time to annotate clone sequence discrepancies.<br>
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
  <li>Delete all files from HIPDRIVE\Sequences for BEC\files_to_transfer directory 
    prior to upload. If you cannot find the directory on hip drive contact Hip 
    System Administrator.</li>
  <li>Load trace files into HIPDRIVE\Sequences for BEC\files_to_transfer directory. 
  </li>
  <li>Create mapping file of the following format: sequencing facility plate label 
    <em>Tab</em> HIP plate label.</li>
  <li>In ACE go to Trace Files -&gt; Create Renaming File. Select mapping file, 
    sequencing facility name and type of reads. The 'Internal reads' option currently 
    implemented only for HTMBC and primers designed by ACE. Click 'Submit' button. 
    ACE writes 'renaming.txt' file into HIPDRIVE\Sequences for BEC\files_to_transfer 
    directory, the same file is sent to user by e-mail. Renaming file contains 
    one entry per each trace file in the format: original file name<em> Tab </em>ACE 
    file name. Make sure that number of entries in 'renaming.txt' file is equal 
    to the number of trace files. If 'renaming.txt' file is empty or number of 
    the entries is less than expected one of the following problems occured: 
    <ul>
      <li>Indicated in mapping file plates have not been submitted into ACE.</li>
      <li>Indicated in mapping file plates have not been submitted for end reads 
        sequencing into ACE or primers were not designed by ACE.</li>
      <li>Trace files have not been named properly by sequencing facility. The 
        following formats are accepted: 
        <table width="80%" border="1" cellspacing="2" cellpadding="2">
          <tr> 
            <td width="34%"><div align="center"><strong>Sequencing facility name</strong></div></td>
            <td width="50%"><div align="center"><strong>Trace file name format</strong></div></td>
            <td width="16%"><div align="center"><strong>Example</strong></div></td>
          </tr>
          <tr> 
            <td><strong>Broad </strong></td>
            <td>Plate name 8 letters or digits; F/R for direction; well name </td>
            <td>D248P100FA1.T0.scf</td>
          </tr>
          <tr> 
            <td><strong>HTMBC</strong></td>
            <td><p>IP#_JasonWell_orderNumber</p>
              <p>_hipPlateName_HipWellName_F/R_number.ab1 </p></td>
            <td>5412_A03_JLJK_ASA001213_A03_F_031.ab1</td>
          </tr>
          <tr> 
            <td><strong>Agencort</strong></td>
            <td>Hipplate_hipwell_F/R </td>
            <td>PRG000548_A01_F.ab1</td>
          </tr>
        </table>
      </li>
      <li>Some of the files can be named not properly that will result in the 
        number of rows in renaming file less than number of trace files.<br>
      </li>
    </ul>
  </li>
  <li>Upload trace files on server: Trace Files -&gt; Upload Trace Files. Select 
    renaming.txt file created by ACE on the previous step. ACE will send user 
    e-mail when finish transfer trace files. </li>
</ul>
<p><a href="#back_submit_trace_files">Back</a> </p>
<h3>General report </h3>
<p>To get information available to ACE about clones user should run General report 
  for the list of cloneids/plate labels by selecting the following menu items 
  'Search-&gt;Genral Report. For running General report user: </p>
<ul>
  <li>selects type of input data he(she) provides (number of clones that user 
    can get information in a time is not limited)</li>
  <li> type of information about each clone he(she) would like to get. See <a href="Help_ReportRunner.jsp" onClick="window.open(href, 'GeneralReport', 'width=800,height=400,scrollbars=yes');">help</a> 
    for more details.</P> </li>
  <li>report tab delimited file will be sent to user by e-mail.</li>
</ul>
<p><a href="#back_general_report">Back</a> </p>
</td></tr></table>
</body>
</html>
