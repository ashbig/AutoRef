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
      <td align="center" class=menu> <font color="#ff9933">&#149;</font><strong><font color="#0099CC" size="+2">Automatic 
        Evaluation of Clones software (ACE)</font></strong><font color="#0099CC">&nbsp; 
        </font><font color="#ff9933">&#149;</font></td>
    </tr>
    <tr valign=top> 
      <td style="width:600px;"><P> <h2><font color="#0099CC">Introduction</font></h2>
        <p> <b>ACE, the Automatic Clone Evaluation software, </b>
  facilitates the process of making sequence-verified, single-gene cDNA 
  collections. The software package uses the following third-party programs: (1) <a href="http://www.phrap.org" target="_blank">phredPhrap
          </a> for contig assembly; (2) <a href="http://emboss.sourceforge.net/apps/needle.html" target="_blank">needle</a> 
           (<a href="http://emboss.sourceforge.net/" target="_blank">Emboss</a> package) for building contig alignments;  
          (3) <a href="http://frodo.wi.mit.edu/primer3/primer3_code.html"  target="_blank">Primer3</a> for oligo calculation.</p>
        <h2> <font color="#0099CC">Instructions</font></h2>
        <ul>
          <li> Get  <a href="http://www.phrap.org" target="_blank">phredPhrap </a> software package from The University 
            of Washington and install them. Make sure that chemistry file is setup properly.</li>
          <li> Download and install the <a href="http://www.emboss.org"  target="_blank">Emboss 
            package</a>. ACE uses only needle program from the package.</li>
          <li> Download and install the <a href="ftp://ftp.ncbi.nih.gov/blast/"  target="_blank">NCBI 
            Blast</a>. ACE uses local blast version to verify cases of mismatch 
            against researcher databases.</li>
          <li>Download and install the <a href="http://frodo.wi.mit.edu/primer3/primer3_code.html"  target="_blank">Primer3</a> used by ACE for oligo design.</li>
          <li>Install Oracle Database software. Import dump file, provided by Hip, to create ACE database. </li>
          <li>Download and install <a href="http://jakarta.apache.org/tomcat/"  target="_blank">Tomcat 
            Web server</a>. ACE was tested for Tomcat 4.6 &amp; 5.59 versions for 
            Windows.</li>
          <li>Download and install <a href="http://www.cygwin.com/"  target="_blank">cygwin</a> 
            if server OS is Win. EMBOSS  and phredPhrap packages do not 
            have Windows versions. Map hard drives under cygwin. </li>
          <li>Create the following set of directories. The directory structure 
            is recommended, however, the user can create any directory structure 
            and give any name to the directories (see below setting application 
            properties file). However, the directories specified by star (*) must 
            exist on serve hard drive.: 
            <table width="90%" border="0">
              <tr> 
                <td width="7%"><div align="center">*</div></td>
                <td width="20%">tmp</td>
                <td  >store all temporary files created by ACE, the directory 
                  should be empted overnight</td>
              </tr>
              <tr> 
                <td width="7%"><div align="center"></div></td>
                <td>output</td>
                <td>root directory for all output files created by ACE that should 
                  be stored </td>
              </tr>
              <tr> 
                <td width="7%"><div align="center">*</div></td>
                <td>output/needle_output</td>
                <td>subdirectory of output directory. Needle alignments will be 
                  stored here.</td>
              </tr>
              <tr> 
                <td width="7%"><div align="center"></div></td>
                <td>output/tmp_assembly</td>
                <td>subdirectory of output directory. Temporary needle alignments 
                  will be stored here. Optionally  the same files can be directed 
                  into temp directory</td>
              </tr>
              <tr> 
                <td><div align="center"></div></td>
                <td>output/polymorphism_finder</td>
                <td>subdirectory of output directory. Input/output files for Polymorphism 
                  Finder will be stored here. You need this directory only if 
                  you are going to use Polymorphism Finder (see ACE User Guide)</td>
              </tr>
              <tr> 
                <td><div align="center">*</div></td>
                <td>trace_files_input_directory</td>
                <td>The initial trace files storage. This directory can resize 
                  on any computer, however, server running ACE should see this 
                  directory. </td>
              </tr>
              <tr> 
                <td><div align="center">*</div></td>
                <td>trace_files_dump</td>
                <td>The directory on ACE server where renamed trace files will 
                  be loaded (see ACE User Guide)</td>
              </tr>
              <tr> 
                <td><div align="center">*</div></td>
                <td>trace_files_root</td>
                <td>The directory on ACE server where trace files will distributed 
                  to subfolders per clone (see ACE User Guide)</td>
              </tr>
            </table>
            &nbsp;</li>
          <li> Prepare a <b>vector sequence</b> libraries (Fasta format file) 
            that will be used by cross-match for vector trimming. All vector files 
            should be placed in one directory. Edit<b> phredPhrap.perl</b> script 
            to reflect location of the cloning vectors' sequence files <a href="<%= redirection%>help/help_phredPhrapScript.jsp" target="_blank">(see 
            example of phredPhrap.perl)</a>.</li>
          <li> Edit the <b>phred, cross_match and phrap</b> scripts. The following 
            perl scripts should be edited (phredPhrap.perl, tagRepeats.perl, findSequenceMatchesforConsed.perl, 
            addReads2Conseq.perl, transferConsensusTags.perl).. </li>
          <li>Edit <a href="help_PhredPhrap.html"  target="_blank"> 
            Phred Parameter File (phredpar.dat) </a>to add a new chemistry if 
            needed.</li>
          <li>Compile Trimming_java_script.java and <a href="<%= redirection%>help/help_phredPhrapScript.jsp"  target="_blank">reflect 
            location of the script</a> in phredPhrap.perl. This script is used 
            to allow user additional quality trimming of the trace files before 
            start of assembly (see ACE user guide).</li>
            <li>Set-up scheduled tasks / cron jobs to: (a) clean up <em>tmp</em> 
            directory; (b) clean up all *.in files from <em>output/needle_output</em> 
            directory; (c) clean up <em>output/tmp_assembly </em>directory; (d) 
            Optional: rebuild user blastable databases if applicable.</li>
          <li> Get ACE package distribution .war file from Harvard Institute of 
            Proteomics and load it..</li>
          <li>Change<a href="<%= redirection%>help/help_WebXMLChange.jsp" target="_blank"> 
            web.xml</a> file to reflect location of ACE database schema.</li>
          <li> Edit <a href="<%= redirection%>help/help_ACEConfigurationFile.html"  target="_blank">ACE 
            configuration file</a> to reflect server settings. Check whether all 
            server configuration settings were properly reflected in configuration 
            file on ACE Start-up. You will see error messages printed in Tomcat 
            window, if something was not setup properly. In the case of any error: 
            shutdown Tomcat, edit configuration file and rebuild ACE.</li>
          <li>If you are planning to use Polymorphism Finder (see ACE user guide), 
            edit <a href="<%= redirection%>help/help_PolymFinderConfigurationFile.html"  target="_blank">configuration 
            file for the module</a>.</li>
        </ul>
        <p class="disclaimer">&copy; 2005 by <a href="mailto:<%=  BecProperties.getInstance().getACEEmailAddress() %>" >Helen 
          Taycher </a><a href="mailto:<%=  BecProperties.getInstance().getACEEmailAddress() %>"></a> last changed 
          September 1, 2005</p></td>
    </tr>
   
</table>
</BODY>
</HTML>
