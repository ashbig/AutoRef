<%@page contentType="text/html"%>
<html>
<head><title>JSP Page</title></head>
<body>
<h2>Help: Container Labels</h2>
<TABLE border="0" cellpadding="2" cellspacing="2">
<tr> <td class="label">MGC000000</td>
        <td>MGC master plate</td>    </tr>
<tr>    <td class="label">MLI000000</td>
        <td>Culture block from MGC master plate</td>    </tr>
<tr>    <td class="label">MGS000000</td>
        <td>Glycerol stock from MGC master plate</td>    </tr>
<tr>    <td class="label">MDN000000</td>
        <td>DNA plate from MGC culture block    </td>    </tr>
<tr></tr>
<tr>    <td  aligment="left">
<P>
      
   <br>    
<P>
<P><h4>Container Labels for Project Plates</h4>
<TABLE border="0" cellpadding="2" cellspacing="2">
<tr><td> <b><i> PST0000000-n</b></i></td></tr>
<tr><td aligment ="right">where P      </td><td>   project code</td></tr>
  <tr><td aligment ="right">    ST      </td><td>  propcess code</td></tr>
  <tr><td aligment ="right">    000000  </td><td>  thread Id</td></tr>
  <tr><td aligment ="right">   n       </td><td>  subthread Id</td></tr>
  </table>
  
  
 <h5> Process codes</h5>
<TABLE border="0" cellpadding="2" cellspacing="2">
<tr> <td class="label">OU</td>
        <td>Oligo upstream (5p)</td>    </tr>
<tr> <td class="label">OF</td>
        <td>Oligo fusion (3P)</td>    </tr>
<tr> <td class="label">OC</td>
        <td>Oligo closed (3P)</td>    </tr>
<tr> <td class="label">PA</td>
        <td>First round PCR</td>    </tr>
<tr> <td class="label">GL</td>
        <td>PCR Gel</td>    </tr>
<tr> <td class="label">FI</td>
        <td>Filter</td>    </tr>
<tr> <td class="label">BP</td>
        <td>BP Reaction</td>    </tr>
<tr> <td class="label">DN</td>
        <td>DNA</td>    </tr>
<tr> <td class="label">TR</td>
        <td>Transformation</td>    </tr>
<tr> <td class="label">AA ..AZ</td>
        <td>Agar gels</td>    </tr>
<tr> <td class="label">LI</td>
        <td>Liquid isolate (culture)</td>    </tr>
</table>


<P>
<h5>Subthread Id</h5>
<TABLE border="0" cellpadding="2" cellspacing="2">
<tr><td>f        </td><td>    Fusion</td>    </tr>
<tr><td>c        </td><td>    Closed</td>    </tr>
<tr><td>1..8     </td><td>    Isolate plates</td>    </tr>
</table>
</body>
</html>
