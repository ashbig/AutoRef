
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