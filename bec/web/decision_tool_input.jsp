<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<%@ page import="edu.harvard.med.hip.bec.*" %>
<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>Output format:</strong></td>
       <td>
       <table border=0 ><tr><td><strong><input type='radio' name='output_format' value='<%= Constants.OUTPUT_TYPE_ONE_FILE%>'>Print results for all groups in one file</strong></td></tr>
       <tr><td><strong><input type='radio' name='output_format' value='<%= Constants.OUTPUT_TYPE_GROUP_PER_FILE %>' checked>Print results for each group into separate file</strong></td></tr></table>
       </td></tr>


 <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>User Comments:</strong></td>
 <td><textarea name='user_comment'  rows='5'></textarea> </td></tr>

<tr><td colspan = 2>
<table width="100%" border="0" cellpadding="2" cellspacing="2">
  <tr>
    <td colspan="2" bgColor="#1145A6"> <font color="#FFFFFF"><strong>Select fields
      to be reported</strong></font></td>
  </tr>
  <tr>
    <td width="50%" bgColor="#e4e9f8" ><div align="center"><font color="000080"><strong>&nbsp;&nbsp;&nbsp;Clone storage information: </strong></font></div></td>
    <td width="50%" bgColor="#e4e9f8"><div align="center"><font color="000080"><strong>Clone raw data information              </strong></font></div></td>
  </tr>
  <tr>    <td >  <input type="checkbox" checked name="l" value="1" disabled> Clone ID</td> 
   <td ><input type="checkbox"  name="is_forward_er_uploaded" value="1" > Is forward end read uploaded? </td>  </tr>

  <tr>
    <td> <input checked type="checkbox" name="clone_final_status" value="1" disabled> Clone final status</td>
	 <td><input type="checkbox"  name="is_reverse_er_uploaded" value="1" > Is reverse end read uploaded? </td>
  </tr>
  <tr>
    <td> <input type="checkbox" name="plate_label" value="1"> Plate label</td>
	 <td><input type="checkbox"  name="is_ordered_internals_oligos" value="1" > Number of ordered internal primers </td>
  </tr>
  <tr>
    <td> <input type="checkbox" name="sample_type" value="1">      Sample type</td>
    <td><input type="checkbox"  name="is_internal_traces" value="1" > Number of submitted internal reads </td>
  </tr>
  <tr>
    <td> <input type="checkbox" name="position" value="1">      Well</td>
    <td>&nbsp;</td>
  </tr>

  <tr>
    <td bgColor="#e4e9f8"><div align="center"><font color="000080"><strong>Reference
        sequence information</strong></font></div></td>
    <td bgColor="#e4e9f8"><div align="center" ><font color="000080"><strong> Clone
        assembly information</strong></font></div></td>
  </tr>
  <tr>
    <td>      <input type="checkbox" name="ref_sequence_id" value="1">
          Reference sequence ID</td>
    <td>      <input type="checkbox" name="clone_seq_id" value="1">
          Clone sequence ID</td>
  </tr>
  <tr>
    <td><input type="checkbox" name="ref_cds_start" value="1">
          Reference sequence CDS start</td>
    <td><input type="checkbox" name="clone_sequence_assembly_status" value="1">
          Clone sequence assembly attempt status </td>
  </tr>
  <tr>
    <td> <input type="checkbox" name="ref_cds_stop" value="1">
          Reference CDS stop</td>
    <td><input type="checkbox" name="clone_sequence_analysis_status" value="1">  Clone sequence analysis status  </td>
  </tr>
  <tr>
    <td><input type="checkbox" name="ref_cds_length" value="1">
          Reference CDS length</td>
    <td>  <input type="checkbox" name="clone_sequence_cds_start" value="1">
          Clone sequence CDS start</td>
  </tr>
  <tr>
    <td > <input type="checkbox" name="ref_seq_text" value="1">
          Reference sequence text</td>
      <td>  <input type="checkbox" name="clone_seq_text" value="1">
          Clone sequence CDS stop </td>
   </tr>
  <tr>
    <td> <input type="checkbox" name="ref_seq_cds" value="1">
          Reference CDS</td>
    <td><input type="checkbox" name="clone_sequence_text" value="1">  Clone sequence  </td>

  </tr>
  <tr>
    <td> <input type="checkbox" name="ref_gene_symbol" value="1">   Gene symbol</td>
        <td><input type="checkbox" name="clone_sequence_cds" value="1">
          Clone sequence CDS</td>

  </tr>
  <tr>
    <td> <input type="checkbox" name="ref_gi" value="1">      GI number</td>
        <td> <input type="checkbox" name="clone_sequence_disc_high" value="1">
           High quality discrepancies(separated by type)</td>
  </tr>
  <tr>
    <td>  <input type="checkbox" name="ref_5_linker" value="1">  5' linker sequence    </td>
        <td> <input type="checkbox" name="clone_sequence_disc_low" value="1">
          Low quality discrepancies (separated by type)</td>
  </tr>
  <tr>
    <td>
      <input type="checkbox" name="ref_3_linker" value="1">     3' linker sequence</td>
        <td><input type="checkbox" name="clone_sequence_disc_det" value="1">
          Detailed discrepancy report </td>
  </tr>
  <tr>
    <td><input type="checkbox" name="ref_species_id" value="1">
          Species specific ID</td>
        <td><input type="checkbox"  name="is_gaps_last_stretch_collection" value="1" >
 Number of gaps in most recent assembly</td>
  </tr>
  <tr>
     <td><input type="checkbox" name="ref_ids" value="1">
          All available identifiers</td>
     <td><input type="checkbox"  name="is_lqd_last_assembly" value="1" > 
Number of low quality discrepancies</td>
  </tr>
</table>


 </td></tr>