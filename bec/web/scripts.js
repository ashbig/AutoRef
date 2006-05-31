
<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

function validate_add_format(formElement)
{
 
if( IsEmpty(formElement.FORMATNAME)) {
    alert("Format name cannot be empty");
    return false;
}
 if ( IsEmpty(formElement.EXAMPLE_TRACE_FILE_NAME)){
alert("Example trace files name cannot be empty");
    return false;
}

 if( IsEmpty(formElement.PLATE_LABEL_COLUMN)){
alert("Plate label column cannot be empty. Default value -1.");
    return false;
}
if ( IsEmpty(formElement.PLATE_LABEL_START)){
alert("Plate label start cannot be empty. Default value -1.");
    return false;
}
 if( IsEmpty(formElement.PLATE_LABEL_LENGTH)){
alert("Plate label length cannot be empty. Default value -1.");
    return false;
}
if( IsEmpty(formElement.POSITION_COLUMN)) {
alert("Position column cannot be empty. Default value -1.");
    return false;
}
if( IsEmpty(formElement.POSITION_START)){
alert("Position start cannot be empty. Default value -1.");
    return false;
}
if (IsEmpty(formElement.POSITION_LENGTH)){
alert("Position length cannot be empty. Default value -1.");
    return false;
}

if ( IsEmpty(formElement.DIRECTION_COLUMN) ){
alert("Direction column cannot be empty. Default value -1.");
    return false;
}
if ( IsEmpty(formElement.DIRECTION_START)){
alert("Direction start cannot be empty. Default value -1.");
    return false;
}
if ( IsEmpty(formElement.DIRECTION_LENGTH)){
alert("Direction lengh cannot be empty. Default value -1.");
    return false;
}


return true;
}


//-------------------------------------------------------------
function validate_add_format(formElement)
{
 
if( IsEmpty(formElement.FORMATNAME)) 
{
    alert("Format name cannot be empty");
    return false;
}
if( IsEmpty(formElement.FORMATNAME)) 
{
    alert("Format name cannot be empty");
    return false;
}
return true;
}

//------------------------------------------------
function validate_add_project(formElement)
{
if( IsEmpty(formElement.PROJECT_NAME)) 
{
    alert("Projectt name cannot be empty");
    return false;
}

return true;
}


//-------------------------------------------------

function validate_add_speciesname(formElement)
{
if( IsEmpty(formElement.speciesname)) 
{
    alert("Species name cannot be empty");
    return false;
}

return true;
}


//-------------------------------------------------
 

function validate_add_nametype(formElement)
{
if( IsEmpty(formElement.nametype)) 
{
    alert("Annotation type cannot be empty");
    return false;
}

return true;
}


//-------------------------------------------------



function validate_add_sequencingprimer(formElement)
{
if( IsEmpty(formElement.primername)) 
{
    alert("Primer name cannot be empty");
    return false;
}
if( IsEmpty(formElement.sequence)) 
{
    alert("Sequence cannot be empty");
    return false;
}

return true;
}


//-------------------------------------------------

function validate_add_linker(formElement)
{
if( IsEmpty(formElement.linkername)) 
{
    alert("Linker name cannot be empty");
    return false;
}
if( IsEmpty(formElement.linkersequence)) 
{
    alert("Sequence cannot be empty");
    return false;
}

return true;
}


//-------------------------------------------
function validate_initprocess(formElement, item_type_project_name)
   {
for ( count = 0; count < formElement.item_type.length; count++)
{
if (formElement.item_type[count].checked==true     && formElement.item_type[count].value==  item_type_project_name)
{
      formElement.items.value =formElement.project_name.value;
}
}

if( IsEmpty(formElement.items)) 
{
    alert("Please submit items to process.");
    return false;
}
return true;
   }
   //---------------------------
function validate_upload_hipplates(formElement)
{
if( IsEmpty(formElement.plate_names)) 
{
    alert("Please submit plate names.");
    return false;
}


return true;
}



function validate_run_report(formElement)
{
if( IsEmpty(formElement.items)) 
{
    alert("Please submit items to process.");
    return false;
}
return true;
}

//-------------------------------------------------
 

   function trim(strText)
    { 
		// this will get rid of leading spaces 

		while (strText.substring(0,1) == ' ') 
			strText = strText.substring(1, strText.length); 

		// this will get rid of trailing spaces 
		while (strText.substring(strText.length-1,strText.length) == ' ') 
			strText = strText.substring(0, strText.length-1); 

	   return strText; 
	} 



function IsEmpty(aTextField) 
{

var aTextField_value = aTextField.value;
aTextField_value=trim(aTextField_value);
   if (aTextField_value==null || aTextField_value.length==0)
 
{
      aTextField.focus();
      return true;
   }
   else { return false; }
}	



// Show/Hide functions for non-pointer layer/objects
function showhide(divName, show_spec)
{

   if (show_spec) 	show(divName); 
   else 	hide(divName);
 
 }
 function show(divName)
 {

               if(document.getElementById) {
                    document.getElementById(divName).style.display = 'block';
                    document.getElementById(divName).style.visibility='visible';
               }
               else if(document.layers) {
                    for (i=0; i < document.layers.length; i++) {
                         var whichEl = document.layers[i];

                         if (whichEl.id.indexOf(divName) != -1) {
                              whichEl.display = 'block';
                              whichEl.visibility='show';
                         }
                    }
               }
               else if(document.all) {
                    document.all(divName).style.display = 'block';
                    document.all(divName).style.visibility='visible';
               }
          }

          function hide(divName){
		
               if(document.getElementById) {
                    document.getElementById(divName).style.visibility='hidden';
                    document.getElementById(divName).style.display = 'none';
               }
               else if(document.layers) {
                    for (i=0; i < document.layers.length; i++) {
                         var whichEl = document.layers[i];

                         if (whichEl.id.indexOf(divName) != -1) {
                              whichEl.display = 'none';
                              whichEl.visibility='hide';
                         }
                    }
               }
               else if(document.all) {
                    document.layers[divName].display = 'none';
                    document.all(divName).style.visibility='hidden';
               }
          }
       
//function checks , unchecks checkboxes in a group
function SetChecked(e, val,checkBoxName)
 {
  // Check all of the checkboxes in a group
  // Initialization
  var iElt, currElt, f;

    // Identify the form
   f = e.form;
    // Loop over all elements in the form
    for (iElt=0; iElt < f.elements.length; iElt++)
    {
      currElt = f.elements[iElt];	
      // If the element is one of the checkboxes in the group containing the checkbox which was just clicked...
      if (currElt.id == checkBoxName)
      {
       // Check the checkbox

        currElt.checked = val;
      }  // end if
    }  // end loop
  
}  // end of function CheckAllCheckboxesInGroup

function checkNumeric(objName,minval, maxval,comma,period,hyphen)
{

	var numberfield = objName;
	if (chkNumeric(objName,minval,maxval,comma,period,hyphen) == false)
	{
		numberfield.select();
		numberfield.focus();
		return false;
	}
	else
	{
		return true;
	}
}

function chkNumeric(objName,minval,maxval,comma,period,hyphen)
{
// only allow 0-9 be entered, plus any values passed
// (can be in any order, and don't have to be comma, period, or hyphen)
// if all numbers allow commas, periods, hyphens or whatever,
// just hard code it here and take out the passed parameters
var checkOK = "-+0123456789" + comma + period + hyphen;
var checkStr = objName;
var allValid = true;
var decPoints = 0;
var allNum = "";

for (i = 0;  i < checkStr.value.length;  i++)
{
ch = checkStr.value.charAt(i);
for (j = 0;  j < checkOK.length;  j++)
if (ch == checkOK.charAt(j))
break;
if (j == checkOK.length)
{
allValid = false;
break;
}
if (ch != ",")
allNum += ch;
}
if (!allValid)
{	
alertsay = "Please enter only these values \""
alertsay = alertsay + checkOK + "\" in the  field."
alert(alertsay);
return (false);
}

// set the minimum and maximum
var chkVal = allNum;
var prsVal = parseInt(allNum);
if (chkVal != "" && !(prsVal >= minval && prsVal <= maxval))
{
alertsay = "Please enter a value greater than or "
alertsay = alertsay + "equal to \"" + minval + "\" and less than or "
alertsay = alertsay + "equal to \"" + maxval + "\" in the field."
alert(alertsay);
return (false);
}
}





   

