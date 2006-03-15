<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>

      
     
<table border="0" align="center" width="90%">
<tr><td><H1 align="center"> <font color="#0099CC">Chages to phredPhrap script</font></H1>
</td></tr>
  <tr> 
    <td> 
      <h2><font color="#0099CC">        Vector Trimming        </font></h2>
	  <P> Raw sequence data typically contains vector sequences. phredPhrap uses 
        <a href="http://bozeman.genome.washington.edu/">CrossMatch</a> for vector 
        masking. Vector libraries are submitted in FASTA-format text files. ACE 
        allows user to have unlimmited number of vector files to be used by cross_match. 
        Each vector library should be mapped in <a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("../../../../BEC_TEST/bec/web/help/JSP_REDIRECTION") %>help/help_ACEConfigurationFile.html">AceConfiguration.properties</a> 
        file. 
    </td>
  </tr>
  <tr> 
    <td><h2>&nbsp;</h2>
      <h2><font color="#0099CC">Quality Trimming</font></h2>
      <p>We found that additional trimming of trace files can be useful in many 
        cases, specially density of sequencing coverage is low. ACE allows user 
        to specify criteria for trace files trimming (how many bases to trim at 
        the begining/end of the trace file, and phed score for quality trimming).</p></td>
  </tr>
  <tr> 
    <td><p>&nbsp;</p>
      <h2><font color="#0099CC">Changes to phradPhrap scrip</font></h2> <p>To 
        run ACE you need to change phredPhrap.perl script to allow:</p>
      <ul>
        <li> use of multipal vector sequence libraries by cross_match</li>
        <li>user specified quality trimming of trace files. </li>
      </ul></td>
  </tr>
  <tr>
    <td> <p><strong># Load library</strong>
      <p> use Getopt::Long;<br>
      </p>
      <p><strong># Get paramenters from user input.</strong><br>
        <br>
        <br>
        $trim_score =&quot;&quot;;<br>
        $trim_first_base = &quot;&quot;;<br>
        $trim_last_base = &quot;&quot;; </p>
      <p>GetOptions(&quot;clonepath=s&quot; =&gt; \$clone_path,<br>
        &quot;vectorfile=s&quot; =&gt; \$vectorfilename,<br>
        &quot;outputfilename=s&quot; =&gt; \$outputfilename,<br>
        &quot;trim_score=i&quot; =&gt; \$trim_score,<br>
        &quot;trim_first_base=i&quot; =&gt; \$trim_first_base,<br>
        &quot;trim_last_base=i&quot; =&gt; \$trim_last_base,<br>
        &quot;lqr_is_use_lqr=i&quot; =&gt; \$lqr_is_use_lqr,<br>
        &quot;lqr_is_delete_lqr=i&quot; =&gt; \$lqr_is_delete_lqr,<br>
        &quot;lqr_pass_score=i&quot; =&gt; \$lqr_pass_score,<br>
        &quot;lqr_first_base=i&quot; =&gt; \$lqr_first_base,<br>
        &quot;lqr_last_base=i&quot; =&gt; \$lqr_last_base,<br>
        &quot;lqr_min_length=i&quot; =&gt; \$lqr_min_length);</p>
      <p>&nbsp;</p>
      <p>$chromatDirPath = $clone_path.&quot;/chromat_dir&quot;<br>
        $phdDirPath = $clone_path.&quot;/phd_dir&quot;;<br>
        #construct output file path<br>
        $outputPath = $clone_path.&quot;/contig_dir&quot;;<br>
      </p>
      <p><strong>#data for trace files quality trimming by ACE</strong><br>
        <strong>$java_pass&lt;/strong&gt; = &quot;/hard_drive/j2sdk1.4.1_02/bin/java&quot;;<br>
        $java_trimming_script&lt;/strong&gt; = &quot;/path to trimming script 
        location/ &nbsp;&nbsp;Trimming_java_script&quot;;</strong></p>
      <p>$trim_score_value = 0;$trim_first_base_value = 0;$trim_last_base_value 
        = 0;<br>
      </p>
      <p>if ($trim_score ne &quot;&quot;) { $trim_score_value = $trim_score ;}<br>
        if ( $trim_first_base ne &quot;&quot;) { $trim_first_base_value = $trim_first_base 
        ;}<br>
        if ($trim_last_base ne &quot;&quot;) {$trim_last_base_value = $trim_last_base 
        ;}</p>
      <p><br>
        <strong># settings for vector trimming<br>
        # at least one empty vector sequence file requered.<br>
        # change this to reflect wherever you put you fasta file of vector sequences</strong></p>
      <p>$szVectorFilePath = &quot;/c/vectors/&quot;;</p>
      <p>$szDefaultVectorFileName = &quot;vector_empty.seq&quot;;<br>
        if ($vectorfilename eq &quot;&quot;)<br>
        {<br>
        <br>
        $szDefaultVectorFile = $szVectorFilePath.$szDefaultVectorFileName;<br>
        }<br>
        else<br>
        {<br>
        $szDefaultVectorFile = $szVectorFilePath.$vectorfilename;<br>
        }</p>
      <p><strong># Reassign current directory path</strong></p>
      <p>if ( !$bProjectNameOnCommandLine ) {<br>
        <strong><em>#$szCurrentDirectory = `$pwdExe`; ----&gt; original script<br>
        $szCurrentDirectory = $outputPath; ----&gt; change</em></strong><br>
      </p>
      <p>print &quot;\n\n--------------------------------------------------------\n&quot;;<br>
        print &quot;<strong>Now running trimming</strong>...\n&quot;;<br>
        print &quot;--------------------------------------------------------\n\n\n&quot;;</p>
      <p>$java_parameters = &quot;$clone_path $trim_score $trim_first_base $trim_last_base 
        &quot;<br>
        .&quot; $lqr_is_use_lqr $lqr_is_delete_lqr $lqr_pass_score $lqr_first_base 
        $lqr_last_base $lqr_min_length&quot;;<br>
        <br>
        !system( &quot;$java_pass -cp $java_trimming_script $java_parameters&quot;) 
        || die &quot;some problem running trimming&quot;;<br>
      </p>
      <p>if (`ls $clone_path./phd_dir` eq &quot;&quot; )<br>
        {<br>
        die &quot;no *.phd files&quot;;<br>
        }</p>
      <p><strong> #File clean up (place this code before exit(0) statement)</strong></strong>&nbsp;</p>
      <p>$newAceFilePath = $outputPath.&quot;/&quot;.$szAceFileToBeProduced;<br>
        $deleteFiles = $szBaseName.&quot;*&quot;;<br>
        <br>
        system(&quot;mv $szAceFileToBeProduced $newAceFilePath&quot;);<br>
        system(&quot;rm $deleteFiles&quot;);<br>
      </p>
      <pre>&nbsp;
</pre></td>
  </tr>
 
</table>

</body>
</html>
