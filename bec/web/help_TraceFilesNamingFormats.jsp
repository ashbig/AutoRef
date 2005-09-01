<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<jsp:include page="NavigatorBar_Administrator.jsp" />
<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <td width="100%"><html:errors/></td>
    </tr>
	
  </table>
  
    <p>&nbsp;</p>
  </center>
</div>
<p></p>
<table width="74%" border="0" cellpadding="2" cellspacing="2" align="center">
  <tr> 
    <td><div align="center"><font color="#0099CC"><strong>Trace Files' Naming 
        Format</strong></font></div></td>
  </tr>
  <tr> 
    <td><p>&nbsp;</p>
      <p><font color="#0099CC"><strong>Introduction</strong></font></p>
      <p>ACE stores trace files in hierarchical directory structure where all 
        files for one clone are distributed into a single directory to insure 
        that multiple nearly identical reads in a set that come from different 
        clones was not included in one contig assembly. The only information about 
        what clone trace files belongs to is contained in trace file name. This 
        information must be parsed and captured in order to distribute reads appropriately. 
        The problem is that most sequencing centers have their own naming formats 
        for the reads. Moreover, they frequently change the formats. To insure 
        that any ttrace files can be submitted into ACE, we have developed utility 
        that allows user to describe new each format in terms that allow application 
        to parse trace files names.</p></td>
  </tr>
  <tr> 
    <td><p><font color="#0099CC"><strong>How trace files' name formats defined</strong></font> 
      <p>To be recognizable by ACE trace file name MUST include information about 
        plate label and position of the clone on the plate. For end reads only, 
        information about direction of the read (sence / unsence) MUST be included 
        as well. 
      <br>
        There two ways to describe how to extract information about the particular 
        item (plate label, position, direction). <em><strong>First: </strong></em>to 
        define start position and length of the item description. For example, 
        position description in file name is from posiotn 4 to position 6 (three 
        leters or digits).<em><strong> Second:</strong></em> to define separator 
        that allows to devide file name into several column and give number of 
        the column. Moreover, user can define that in column N only M first letters/digits 
        describe the item.
    </td>
  </tr>
  <tr> 
    <td><p><font color="#0099CC"><strong>How to find what trace files' names formats 
        exist</strong></font> 
      <p>1. On the Configure System menu, click ACE Configuration, and then select 
        Add Trace File Name Format. <br>
        2. All existing ACE trace files' names formtas will be listed below table 
        that allows user to create new format.</td>
  </tr>
  <tr> 
    <td><p><font color="#0099CC"><strong>How to create new format</strong></font> 
      <p>1. On the Configure System menu, click ACE Configuration, and then select 
        Add Trace File Name Format. <br>
        2. Give new format user freindly name.<br>
        3. Define in what direction trace file name should be read by ACE.<br>
        4. Define how plate label information should be extracted from file name 
        by ACE.<br>
        5. Define how position information should be extracted from file name 
        by ACE.<br>
        For end reads only<br>
        6. Define how direction of the read is annotated.<br>
        7. Define how direction information should be extracted from file name 
        by ACE.
      <p><strong><font size="+1"><em>Note: </em></font></strong>Do not change 
        value of the field that are not involved in format definition. <br>
      
    </td>
  </tr>
  <tr> 
    <td><p><font color="#0099CC"><strong>Examples</strong></font></p>
      <table border="0" cellpadding="1" cellspacing="1" align=center>
        <tr> 
          <td bgColor='#b8c6ed' align='center' nonwrap><font size="2"> Format 
            Name </font></td>
          <td bgColor='#b8c6ed' align='center' nonwrap><font size="2"> File name 
            reading direction </font></td>
          <td bgColor='#b8c6ed' align='center' nonwrap><font size="2"> Plate Label 
            Separator </font></td>
          <td bgColor='#b8c6ed' align='center' nonwrap><font size="2"> Plate Label 
            Column </font></td>
          <td bgColor='#b8c6ed' align='center' nonwrap><font size="2"> Plate Label 
            Start </font></td>
          <td bgColor='#b8c6ed' align='center' nonwrap><font size="2"> Plate Label 
            Length </font></td>
          <td bgColor='#b8c6ed' align='center' nonwrap><font size="2"> Position 
            Separator </font></td>
          <td bgColor='#b8c6ed' align='center' nonwrap><font size="2"> Position 
            Column </font></td>
          <td bgColor='#b8c6ed' align='center' nonwrap><font size="2"> Position 
            Start </font></td>
          <td bgColor='#b8c6ed' align='center' nonwrap><font size="2"> Position 
            Length </font></td>
          <td bgColor='#b8c6ed' align='center' nonwrap><font size="2"> Direction 
            Forward </font></td>
          <td bgColor='#b8c6ed' align='center' nonwrap><font size="2"> Direction 
            Reverse </font></td>
          <td bgColor='#b8c6ed' align='center' nonwrap><font size="2"> Direction 
            Separator </font></td>
          <td bgColor='#b8c6ed' align='center' nonwrap><font size="2"> Direction 
            Column </font></td>
          <td bgColor='#b8c6ed' align='center' nonwrap><font size="2"> Direction 
            Length </font></td>
          <td bgColor='#b8c6ed' align='center' nonwrap><font size="2"> Direction 
            Start </font></td>
        </tr>
        <tr> 
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> DFHCC (-) core </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> Left to right </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> _ </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> 3 </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> Not set </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> 11 </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> - </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> 3 </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> Not set </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> Not set </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> F </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> R </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> - </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> 4 </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> 1 </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> Not set </font></td>
        </tr>
        <tr> 
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> Willson </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> Left to right </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> - </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> 1 </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> Not set </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> Not set </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> - </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> 2 </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> Not set </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> 3 </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> F </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> R </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> Not set </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> Not set </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> Not set </font></td>
          <td  bgColor='#e4e9f8'  nonwrap><font size="2"> Not set </font></td>
        </tr>
      </table>
      <blockquote> 
     
        <table width="90%" border="0">
          <tr>
            <td width="25%"><strong>Format</strong><em><font color="#663399"> DFHCC(-) core</font></em></td>
            <td width="75%"><strong>File name: </strong><em>222_H01_KSG002233-2-H05-F0.ab1</em>, 
              where 'KSG002233-2' - is plate name, H05 - position, F - direction 
              of the read</td>
          </tr>
          <tr>
            <td><strong>Format <em><font color="#663399">Willson</font></em></strong></td>
            <td><strong> File name: </strong><em>KSG002233-H05_I.ab1</em>, where 
              'KSG002233' - is plate name, H05 - position</td>
          </tr>
        </table>
        <p><em><strong><font color="#663399"></font></strong></em></p>
      </blockquote>
      <p>&nbsp; </p></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>

</body>
</html>
