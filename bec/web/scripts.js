function IsEmpty(aTextField) {
   if ((aTextField.value.length==0) ||
   (aTextField.value==null)) {
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
var checkOK = "0123456789" + comma + period + hyphen;
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





   

