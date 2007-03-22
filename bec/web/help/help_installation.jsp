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
      <td><h2><font color="#0099CC">1. Install third-party programs </font></h2>
        <p>Automatic Clone Evaluation software uses the following third-party 
          programs: (1) <a href="http://www.phrap.org" target="_blank">PhredPhrap 
          </a> for contig assembly; (2) <a href="http://emboss.sourceforge.net/apps/needle.html" target="_blank">needle</a> 
          (<a href="http://emboss.sourceforge.net/</a>" target="_blank">Emboss</a> 
          package) for building global contig alignments; (3) <a href="http://0-www.ncbi.nlm.nih.gov.library.vu.edu.au/BLAST/" target="_blank"> 
          NCBI BLAST </a>for local alignment; (4) <a href="http://frodo.wi.mit.edu/primer3/primer3_code.html"  target="_blank">Primer3</a> 
          for primer calculation. They all need to be downloaded from respective 
          providers (see links below) and installed following their installation 
          instructions.<font color="#0099CC"></font></p>
        <ul>
          <li> If you are installing ACE on a Windows server <a href="http://www.cygwin.com/"  target="_blank">cygwin 
            </a>should be installed first. EMBOSS and PhredPhrap packages do not 
            have Windows versions. Map ACE relevant hard drive(s) under cygwin. 
          </li>
          <li>Install <a href="http://www.phrap.org" target="_blank">PhredPhrap 
            package</a>.</li>
          <li> Install <a href="http://www.emboss.org"  target="_blank">Emboss 
            package</a>. ACE uses only the <em>needle</em> program from the package.</li>
          <li> Install <a href="ftp://ftp.ncbi.nih.gov/blast/"  target="_blank">NCBI 
            BLAST</a>. ACE uses a local blast version to verify cases of mismatch 
            against researcher databases. You do not need to install NCBI BLAST 
            if you are not going to use 'Mismatched clones' report.</li>
          <li>Install <a href="http://frodo.wi.mit.edu/primer3/primer3_code.html"  target="_blank">Primer3</a>. 
          </li>
        </ul>
        <h2> <font color="#0099CC">2. Install Tomcat Web server </font></h2>
        <ul>
          <li>Download <a href="http://java.sun.com/javase/downloads/index.jsp" target="_blank">JDK</a>. 
            Set up environmental variable: JAVA_HOME</li>
          <li>Install <a href="http://jakarta.apache.org/tomcat/"  target="_blank">Apache 
            Tomcat Web server</a>. ACE was tested on Tomcat 4.6 &amp; 5.59 versions 
            for Windows and v.5.5.15 for UNIX</li>
          <li>Set up CATALINA_HOME environmental variable to point to the installation 
            directory of Tomcat</li>
          <li>Set up CLASSPATH environmental variable to include /Tomcat/webapps/ACE/WEB-INF/lib</li>
        </ul>
        <h2> <font color="#0099CC">3. Create ACE database</font></h2>
        <ul>
          <li>Import ORACLE dump file provided by HIP into your Oracle database 
            (tested on versions 8i, 9i and 10g). 
        </ul>
        <h2> <font color="#0099CC"> 4. Create ACE application directories</font></h2>
        <ul>
          <li>Create all directories as described in <a href="help_ACEConfigurationFile.html" target="_blank">ACE 
            configuration file</a>.</li>
        </ul>
        <p>&nbsp;</li> </ul> 
        <h2> <font color="#0099CC"> 5. Install and configure ACE application</font></h2>
        <ul>
          <li> Place ACE package distribution .war file from Harvard Institute 
            of Proteomics into [myserver]/webapps directory of Tomcat server</li>
          <li>Start-up server. Shutdown server. The .war file will be deployed 
            by Tomcat (consult<a href="http://tomcat.apache.org/tomcat-5.5-doc/deployer-howto.html" target="_blank"> 
            documentation </a>for statical deployment on Tomcat server)</li>
          <li>Edit<a href="<%= redirection%>help/help_WebXMLChange.jsp" target="_blank"> 
            web.xml</a> file under $CATALINA_HOME/webapps/ACE/WEB-INF to reflect 
            location of ACE database.</li>
          <li>Build user blastable databases</li>
          <li>Edit <a href="<%= redirection%>help/help_ACEConfigurationFile.html"  target="_blank">ACE 
            configuration file</a> found in $CATALINA_HOME/webapps/ACE/WEB-INF/classes/config 
            to reflect server settings. To check settings start-up Tomcat server: 
            <a href="help_ACE_installation_error_messages.html" target="_blank">error 
            messages</a> will be printed into Tomcat window if something was not 
            setup properly. In case of an error shutdown Tomcat, edit configuration 
            file and restart the server</li>
          <li>If you are planning to use Polymorphism Finder (see ACE user guide), 
            edit <a href="<%= redirection%>help/help_PolymFinderConfigurationFile.html"  target="_blank">configuration 
            file for the module</a></li>
          <li>Replace phredPhrap.perl script with the one <a href="<%= redirection%>help/help_phredPhrapScript.jsp"  target="_blank"> 
            provided by HIP</a>. The following perl scripts should be edited according 
            to installation instructions for PhredPhrap package: phredPhrap.perl, 
            tagRepeats.perl, findSequenceMatchesforConsed.perl, addReads2Conseq.perl, 
            transferConsensusTags.perl</li>
          <li>Prepare <b>vector sequence</b> libraries (FASTA format file) that 
            will be used by cross-match for vector trimming according to installation 
            instructions for PhredPhrap package. All vector files should be placed 
            in one directory. Edit<b> phredPhrap.perl</b> script to reflect location 
            of the cloning vector sequence files <a href="<%= redirection%>help/help_phredPhrapScript.jsp" target="_blank">(see 
            example of phredPhrap.perl)</a></li>
          <li>Copy Trimming_java_script.class and Trimming_java_script$ScoredElement.class 
            provided by HIP to ACE server hard drive. This script is used for 
            additional quality trimming of the trace files before start of assembly 
            (see ACE user guide).</li>
          <li>Edit <a href="help_PhredPhrap.html"  target="_blank"> Phred Parameter 
            File (phredpar.dat) </a>to add a new chemistry if needed. Test phredPhrap 
            package (according to installation instructions for PhredPhrap package)</li>
          <li>Set-up scheduled tasks / cron jobs to: (a) clean up <em>tmp</em> 
            directory; (b) clean up all *.in files from <em>output/needle_output</em> 
            directory; (c) clean up <em>output/tmp_assembly </em>directory; (d) 
            Optional: rebuild user blastable databases if applicable</li>
        </ul>
        <p class="disclaimer">&copy; 2005 by <a href="mailto:<%=  BecProperties.getInstance().getACEEmailAddress() %>" >Helen 
          Taycher </a><a href="mailto:<%=  BecProperties.getInstance().getACEEmailAddress() %>"></a> 
          last changed February 15, 2007</p></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
</table>
</BODY>
</HTML>
