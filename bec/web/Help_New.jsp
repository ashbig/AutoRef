<html>
<head>
<title>create report</title>
</head>
<body >
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        
    <td > <font color="#008000" size="5"><b> Help : Release Notes</font> 
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
  <tr> 
    <td> <p><strong><em>Release 9/13/04 </em></strong></p>
      <h3><strong>Major Development: </strong></h3>
      <ul>
        <li> LQR finder : report provided for try mode now includes - discrepancy 
          information - request for end reads in report (LQR with discrepancies 
          less than 300 bases from ATG) - request for internal reads </li>
        <li>End Reads Wrapper and Assembler Based on End Reads: runs only on user 
          ordered plates; </li>
        <li>Limit on number of items to be processed at a time is removed for 
          the following modules: 
          <ul>
            <li>End Reads Wrapper, </li>
            <li> Assembly Wrapper, </li>
            <li>Report Runner i</li>
            <li> Special Report Runner </li>
            <li>No match report runner </li>
            <li>Discrepancy finder </li>
          </ul>
        </li>
        <li>New Action: Prepare order list for sequencing rerun - is implamented. 
          To create order list select Search -&gt; Run Report -&gt; Select 'Order 
          list for End Reads repeat' report. Report works on container level ONLY. 
          Order list for needed repeats is send to user by e-mail (file format 
          PlateLabel Well F/R).</li>
        <li> End Reads wrapper does not submit bad reads from now on. The read 
          is considered of bad quality if any of the following conditions met: 
          <ul>
            <li> length of the trimmed by phred stretch less than 0; </li>
            <li>read is short (length of the trimmed by phred stretch less than 
              50 + 65 linker length);</li>
            <li> number of bases with score < 20 no more than 100 from the first 
              300 bases of the read; </li>
          </ul>
        </li>
        <li> Discrepancy Quality Definition is changed. New implementation consider 
          any discrepancy to be of low quality if at least 1 base from of discrepancy 
          bases && 4 on each side has score below min score. For IsolateRanker 
          min score set by user, For DiscrepancyFinder min score = 20;</li>
        <li> Delete options are added to the Process menu. 
          <ul>
            <li> Delete container (by container label) NOTE: plate can be deleted 
              before GApMapper/LQR finder run ii. </li>
            <li>Delete all reads for the clone (cloneid && container label) iii. 
              Delete forward read only(cloneid && container label);</li>
            <li>Delete reverse read only(cloneid && container label)</li>
            <li>Delete clone sequence;</li>
          </ul>
        </li>
      </ul> 
      <h3>Bug Fixes:</h3>
      <ul>
        <li> wrong well mapping presentation on UI Search/Container Results/Show 
          Isolate Ranker Output </li>
        <li>Broad trace files renaming fixed</li>
        <li> No Match Report: fixed crash for NONE selected as additional ID </li>
        <li> Discrepancy report UI changed to display N/Y/Not known instead of 
          +/- </li>
        <li> Isolate Ranker: module not always picks up clone sequence for analysis. 
          Exsplicit call to getSequence() added ;</li>
        <li>Each module provides report in file with unique name ;</li>
        <li> LQR Finder/GapMapper do not pick discrepancy in 3' region. ;</li>
        <li> UI for LQR presentation polished New Application: New application 
          (ACE_EXP_EVAL under the same server path) was installed for clones' 
          verification ACE code was reconfigured to allow any number of separate 
          applications to be build on ACE code </li> </li>
      </ul></ul>
    </td>
  </tr>
  <tr> 
    <td> <p><strong><em>Release 9/30/04 </em></strong></p>
      <h3><strong>Major Development: </strong></h3>
      <ul>
        <li> General Report <br>
          <ul>
            <li> New field - assembly attempt status;</li>
            <li> Print discrepancy summary for contigs if assembled sequence not 
              avaiable;<br>
            </li>
          </ul>
        <li>Delete options</li>
        <ul>
          <li>UI reconfigured;</li>
          <li>Added option: print list of trace files available for the clone;</li>
          <li> Added option: delete trace files which name submitted in file;</li>
        </ul>
        <li>End reads wrapper: names of trace files that were not submitted because 
          of low quality printed into error log; </li>
        <li> Error messages are send in attached file for all modules;</li>
        <li> Data input clean up: all none digit items will be deleted for input 
          data submitted as clone id, ACE sequence id, FLEX sequence id;
<li>GapMapper: report is send  to user regardless of the run mode;</li>
        <li>All .phd files are removed prior to assembly run to prevent 
          assembly with refsequence.phd created on Gap Mapper run;</li>
        <li> Discrepancy Finder: if assembled sequence is not available module 
          analized contigs collection.</li>
      </ul>
      <h3>Bug Fixes:</h3>
      <ul>
        <li>LQR Finder: report shows description of RNA discrepancies in report. 
        </li>
      </ul>
      <br> <br>
      12. General Report: bug fix: show all controls<br> <br></p>
      </li> </li> </td>
  </tr></table></body>