<!--Copyright 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<HTML>
<HEAD>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
	

       <link rel=stylesheet type="text/css" href="/style/format.css">
       <% String redirection = edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") ; %>
<TITLE>ACE</TITLE>

</HEAD>
<BODY>
	
<table  class=menu align="center">
  <tbody>
    <tr> 
      <td align="center" > <h1><font color="#0099CC" >Installation Manual for 
          ACE</font></h1>
        <hr></td>
    </tr>
    <tr>
      <td>Automatic Clone Evaluation software, uses the following third-party 
        programs to perform specific tasks: (1) <a href="http://www.phrap.org" target="_blank">phredPhrap 
        </a> for contig assembly; (2) <a href="http://emboss.sourceforge.net/apps/needle.html" target="_blank">needle</a> 
        (<a href="http://emboss.sourceforge.net/</a>" target="_blank">Emboss</a> 
        package) for building global contig alignments; (3) <a href="http://0-www.ncbi.nlm.nih.gov.library.vu.edu.au/BLAST/" target="_blank"> 
        NCBI BLAST </a>for local aligment; (4) <a href="http://frodo.wi.mit.edu/primer3/primer3_code.html"  target="_blank">Primer3</a> 
        for oligo calculation.</p> <h2> <font color="#0099CC">1. Install functional 
          programs </font></h2>
        <ul>
          <li> Download and install <a href="http://www.phrap.org" target="_blank">phredPhrap 
            package</a> from The University of Washington.</li>
          <li> Download and install <a href="http://www.emboss.org"  target="_blank">Emboss 
            package</a>. ACE uses only <em>needle</em> program from the package.</li>
          <li> Download and install the <a href="ftp://ftp.ncbi.nih.gov/blast/"  target="_blank">NCBI 
            BLAST</a>. ACE uses local blast version to verify cases of mismatch 
            against researcher databases.</li>
          <li>Download and install the <a href="http://frodo.wi.mit.edu/primer3/primer3_code.html"  target="_blank">Primer3</a> 
            used by ACE for oligo design.</li>
          <li>Download and install <a href="http://www.cygwin.com/"  target="_blank">cygwin</a> 
            for Windows server only. EMBOSS and phredPhrap packages do not have 
            Windows versions. Map hard drive(s) under cygwin. </li>
        </ul>
        <h2> <font color="#0099CC">2. Install Tomcat Web server </font></h2>
        <ul>
          <li>Download JDK. Set up enviromental variable: JAVA_HOME.</li>
          <li>Download and install <a href="http://jakarta.apache.org/tomcat/"  target="_blank">Apache 
            Tomcat Web server</a>. ACE was tested on Tomcat 4.6 &amp; 5.59 versions 
            for Windows and v.5.5.15 for UNIX.</li>
          <li>Set up CLASSPATH enviromental variable to include /Tomcat/webapps/ACE/WEB-INF/lib.</li>
        </ul> <h2> <font color="#0099CC">3. Create ACE database</font></h2>
		<ul>
          <li>Import ORACLE dump file provided by HIP into your Oracle database. 
            (tested on versions 8i, 9i and 10g)
</ul>
           <h2> <font color="#0099CC"> 4. Create ACE application directories.</font></h2>
		   
        Create the following set of directories. The directory structure is suggested, 
        however, user can create any directory structure and give any name to 
        the directories (see below setting application properties file). However, 
        the directories specified by star (*) must exist on serve hard drive.: 
        <table width="90%" border="0">
          <tr> 
            <td width="20%"><strong><em>/usr/tmp</em></strong></td>
            <td  >Store all temporary files created by ACE, the directory should 
              be empted periodicly (see Clean up cron jobs)</td>
          </tr>
          <tr> 
            <td>/usr/blast_db</td>
            <td>Storage for user blastable databses</td>
          </tr>
          <tr> 
            <td><strong><em>/usr/output/needle_output</em></strong></td>
            <td>Directory for <em>needle</em> alignments.</td>
          </tr>
          <tr> 
            <td><strong><em>/usr/output/tmp_assembly</em></strong></td>
            <td> Output directory for temporary needle alignments, the directory 
              should be cleaned up periodicly.. Optionally the same files can 
              be directed into temp directory</td>
          </tr>
          <tr> 
            <td><strong><em>/usr/output/polymorphism_finder</em></strong></td>
            <td>Input/output files for Polymorphism Finder will be stored here. 
              You need this directory only if you are going to use Polymorphism 
              Finder </td>
          </tr>
          <tr> 
            <td><strong><em>/usr/trace_files_input_directory</em></strong></td>
            <td>The initial trace files storage. This directory can resize on 
              any computer, however, server running ACE should be able to see 
              the directory. </td>
          </tr>
          <tr> 
            <td><strong><em>/usr/trace_files_dump</em></strong></td>
            <td>The directory on ACE server where renamed trace files will be 
              loaded (see ACE User Guide)</td>
          </tr>
          <tr> 
            <td><strong><em>/usr/trace_files_root</em></strong></td>
            <td>The directory on ACE server where trace files will distributed 
              to subfolders per clone (see ACE User Guide)</td>
          </tr>
        </table>
            &nbsp;</li>
        </ul>
		 <h2> <font color="#0099CC"> 5. Install and configure ACE application.</font></h2>
        <ul>
          <li> Put ACE package distribution .war file from Harvard Institute of 
            Proteomics into /webapps directory of Tomcat server and start-up server. 
            Shutdown server. </li>
          <li>Edit<a href="<%= redirection%>help/help_WebXMLChange.jsp" target="_blank"> 
            web.xml</a> file under $CATALINA_HOME/webapps/ACE/WEB-INF to reflect 
            location of ACE database.</li>
          <li>Build user blastable databases.</li>
          <li>Edit <a href="<%= redirection%>help/help_ACEConfigurationFile.html"  target="_blank">ACE 
            configuration file</a> found in $CATALINA_HOME/webapps/ACE/WEB-INF/classes/config 
            to reflect server settings. Start-up Tomcat server: you will see <a href="help_ACE_installation_error_messages.html" target="_blank">error 
            messages</a> printed in Tomcat window, if something was not setup 
            properly. In the case of any error: shutdown Tomcat, edit configuration 
            file and restart server.</li>
          <li>If you are planning to use Polymorphism Finder (see ACE user guide), 
            edit <a href="<%= redirection%>help/help_PolymFinderConfigurationFile.html"  target="_blank">configuration 
            file for the module</a>.</li>
          <li>Replace phredPhrap script by one <a href="<%= redirection%>help/help_phredPhrapScript.jsp"  target="_blank"> 
            provided by HIP</a>. Edit the <b>phred, cross_match and phrap</b> 
            scripts. The following perl scripts should be edited (phredPhrap, 
            tagRepeats.perl, findSequenceMatchesforConsed.perl, addReads2Conseq.perl, 
            transferConsensusTags.perl).</li>
          <li>Prepare a <b>vector sequence</b> libraries (Fasta format file) that 
            will be used by cross-match for vector trimming. All vector files 
            should be placed in one directory. Edit<b> phredPhrap.perl</b> script 
            to reflect location of the cloning vectors' sequence files <a href="<%= redirection%>help/help_phredPhrapScript.jsp" target="_blank">(see 
            example of phredPhrap.perl)</a>.</li>
          <li>Copy Trimming_java_script.class and Trimming_java_script$ScoredElement.class 
            provided by Hip on harddrive. This script is used to allow user additional 
            quality trimming of the trace files before start of assembly (see 
            ACE user guide).</li>
          <li>Edit <a href="help_PhredPhrap.html"  target="_blank"> Phred Parameter 
            File (phredpar.dat) </a>to add a new chemistry if needed. Test phredPhrap 
            package. </li>
          <li>Set-up scheduled tasks / cron jobs to: (a) clean up <em>tmp</em> 
            directory; (b) clean up all *.in files from <em>output/needle_output</em> 
            directory; (c) clean up <em>output/tmp_assembly </em>directory; (d) 
            Optional: rebuild user blastable databases if applicable.</li>
        </ul>
        <p class="disclaimer">&copy; 2005 by <a href="mailto:<%=  BecProperties.getInstance().getACEEmailAddress() %>" >Helen 
          Taycher </a><a href="mailto:<%=  BecProperties.getInstance().getACEEmailAddress() %>"></a> 
          last changed September 15, 2007</p></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
</table>
</BODY>
</HTML>
