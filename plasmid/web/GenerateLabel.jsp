<%-- 
    Document   : GenerateLabel
    Created on : Jun 22, 2012, 9:43:48 PM
    Author     : dongmei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
        <style type='text/css'>

body {	font-family:  Arial;
	font-size: 11px;
	background-color: #FFFFFF;
	color: #333333;
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}.Title1 {	font-size: 14px;
	color: #575659;
	font-weight: bold;
}.Title2 {	font-size: 14px;
	color: #660099;
	font-weight: bold;
}.Title3 {	font-size: 13px;
	color: #ffffff;
	font-weight: bold;
}.Title4 {	font-family:  Arial;
	font-size: 11px;
	height: 18px;
}.Title5 {	font-family: Times New Roman;
	font-size: 32px;
	color: #808080;
	font-weight: bold;
}.Hr1 {	height: 2px;
	color: #660099;
}.Hr2 {	height: 1px;
	color: #000000;
}.Hr3 {	height: 2px;
	color: #000000;
}.Hr4 {	height: 1px;
	color: #808080;
}.Text1 {	font-weight: bold;
	font-size: 10px;
	color: #af9999;
}.PanelHeader {	background-color: #660099;
	font-size: 13px;
	color: #ffffff;
	font-weight: bold;
	height: 25px;
	padding: 6 6 6 6;
}.PanelHeader2 {	background-color: #DCDCDC;
	font-size: 13px;
	color: #5F5F5F;
	font-weight: bold;
	height: 25px;
}.PanelHeader3 {	background-color: #999999;
	font-size: 13px;
	color: #ffffff;
	font-weight: bold;
	height: 25px;
}.PanelContent {	color: #000000;
	background-color: #e6e6e6;
	font-size: 11px;
	height: 30px;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 10px;
	padding-bottom: 10px;
}.PanelContent2 {	color: #999999;
	background-color: #CCCCCC;
	font-size: 10px;
	height: 30px;
}.PanelContent3 {	background-color: #DCDCDC;
	font-size: 12px;
	color: #5F5F5F;
	height: 25px;
}.Buttons {	font-size: 13px;
	background-color: #7C1FAA;
	color: #ffffff;
	height: 24px;
	border-right: #7F7F7F 2px solid;
	border-top: #CCCCCC 2px solid;
	border-left: #CCCCCC 2px solid;
	border-bottom: #7F7F7F 2px solid;
}.BackButtons {	font-size: 13px;
	background-color: #666;
	color: #ffffff;
	height: 24px;
	border-right: #7F7F7F 2px solid;
	border-top: #CCCCCC 2px solid;
	border-left: #CCCCCC 2px solid;
	border-bottom: #7F7F7F 2px solid;
}.Buttons2 {	background-color: #000000;
	font-size: 13px;
	color: #ffffff;
	height: 24px;
	border-right: #7F7F7F 2px solid;
	border-top: #CCCCCC 2px solid;
	border-left: #CCCCCC 2px solid;
	border-bottom: #7F7F7F 2px solid;
}.TrWhite {	color: #666666;
	background-color: #fff;
	font-size: 12px;
}.TrGrey {	color: #666666;
	background-color: #e6e6e6;
	font-size: 12px;
}.TextGrey {	color: #666666;
	font-size: 12px;
}.FedExLink {	color: #660099;
}.Intro {	background: #f5f5f5;
	color: #333333;
	font-size: 11px;
	border-bottom: solid 1px #660099;
	border-left: solid 1px #660099;
	border-right: solid 1px #660099;
	border-top: solid 1px #660099;
}.Intro_BACKUP {	background: #e6e6e6;
	color: #000000;
	font-size: 11px;
	padding: 10px;
	border-bottom: solid 1px #660099;
	border-left: solid 1px #660099;
	border-right: solid 1px #660099;
	border-top: solid 1px #660099;
}.ResultTable {	background: #e6e6e6;
	color: #666666;
	font-size: 12px;
	border: solid 1px #660099;
}.CurrentStep {	color: #FF6600;
	font-weight: bold;
	text-decoration: underline;
	font-size: 18px;
}.CurrentStep {	color: #000000;
	font-weight: bold;
	text-decoration: none;
	font-size: 12px;
}.FinishedStep {	font-size: 12px;
	color: #660099;
	font-weight: bold;
	text-decoration: underline;
	border :0px;
}.UnFinishedStep {	font-size: 12px;
	color: #999999;
	text-decoration: none;
}.mainheader1 {	font-size: 18px;
	color:#660099;
	font-weight: normal;
}.mainheader2 {	font-size: 14px;
	color: #999999;
	font-weight: normal;
}.mainheader3 {	font-size: 16px;
	color:#660099;
	font-weight: normal;
}.mainheader4 {	font-size: 14px;
	color:#660099;
	font-weight: bold;
	border-bottom: solid 2px #660099;
	border-left: solid 0px #660099;
	border-right: solid 0px #660099;
	border-top: solid 0px #660099;
	height: 18px;
}.clearSelection {	font-size: 11px;
	color:#660099;
}.designPreviewBox{	font-family:  Arial;
	font-size: 11px;
	color: #333333;
	border-bottom: solid 1px #868686;
	border-left: solid 1px #868686;
	border-right: solid 1px #868686;
	border-top: solid 1px #868686;
	}.textbox{	font-family:  Arial;
	font-size: 11px;
	color: #333333;
	border-bottom: solid 1px #868686;
	border-left: solid 1px #868686;
	border-right: solid 1px #868686;
	border-top: solid 1px #868686;
	width: 165px;
	}.dropdown{	font-family:  Arial;
	font-size: 11px;
	color: #333333;
	border-bottom: solid 1px #868686;
	border-left: solid 1px #868686;
	border-right: solid 1px #868686;
	border-top: solid 1px #868686;
	}</style>

<script language='javascript' type='text/javascript'>

 var originCntryCode 			= 'US';
var recipientCntryCode 			= 'null';
var originCntryCodeChecked 		= 'false';
var recipientCntryCodeChecked 	= 'true';
 var liste = [        	             [ "AD", "EUR" ],        	             [ "AE", "AED" ],        	             [ "AF", "" ],        	             [ "AG", "XCD" ],        	             [ "AI", "XCD" ],        	             [ "AL", "" ],        	             [ "AM", "" ],        	             [ "AN", "ANG" ],        	             [ "AO", "" ],        	             [ "AR", "ARN" ],        	             [ "AS", "USD" ],        	             [ "AT", "EUR" ],        	             [ "AU", "AUD" ],        	             [ "AW", "AWG" ],        	             [ "AZ", "" ],        	             [ "BA", "" ],        	             [ "BB", "BBD" ],        	             [ "BD", "" ],        	             [ "BE", "EUR" ],        	             [ "BF", "" ],        	             [ "BG", "" ],        	             [ "BH", "BHD" ],        	             [ "BI", "" ],        	             [ "BJ", "" ],        	             [ "BM", "BMD" ],        	             [ "BN", "BND" ],        	             [ "BO", "" ],        	             [ "BR", "BRL" ],        	             [ "BS", "BSD" ],        	             [ "BT", "" ],        	             [ "BW", "" ],        	             [ "BY", "" ],        	             [ "BZ", "" ],        	             [ "CA", "CAD" ],        	             [ "CD", "" ],        	             [ "CG", "" ],        	             [ "CH", "CHF" ],        	             [ "CI", "" ],        	             [ "CK", "NZD" ],        	             [ "CL", "CLP" ],        	             [ "CM", "" ],        	             [ "CN", "CNY" ],        	             [ "CO", "COP" ],        	             [ "CR", "CRC" ],        	             [ "CV", "" ],        	             [ "CY", "EUR" ],        	             [ "CZ", "CZK" ],        	             [ "DE", "EUR" ],        	             [ "DJ", "" ],        	             [ "DK", "DKK" ],        	             [ "DM", "XCD" ],        	             [ "DO", "DOP" ],        	             [ "DZ", "" ],        	             [ "EC", "" ],        	             [ "EE", "EUR" ],        	             [ "EG", "EGP" ],        	             [ "ER", "" ],        	             [ "ES", "EUR" ],        	             [ "ET", "" ],        	             [ "FI", "EUR" ],        	             [ "FJ", "" ],        	             [ "FM", "USD" ],        	             [ "FO", "DKK" ],        	             [ "FR", "EUR" ],        	             [ "GA", "" ],        	             [ "GB", "GBP" ],        	             [ "GD", "XCD" ],        	             [ "GE", "" ],        	             [ "GF", "" ],        	             [ "GH", "" ],        	             [ "GI", "" ],        	             [ "GL", "DKK" ],        	             [ "GM", "" ],        	             [ "GN", "" ],        	             [ "GP", "EUR" ],        	             [ "GQ", "" ],        	             [ "GR", "EUR" ],        	             [ "GT", "GTQ" ],        	             [ "GU", "USD" ],        	             [ "GY", "" ],        	             [ "HK", "HKD" ],        	             [ "HN", "" ],        	             [ "HR", "" ],        	             [ "HT", "" ],        	             [ "HU", "HUF" ],        	             [ "ID", "" ],        	             [ "IE", "EUR" ],        	             [ "IL", "" ],        	             [ "IN", "INR" ],        	             [ "IQ", "" ],        	             [ "IS", "" ],        	             [ "IT", "EUR" ],        	             [ "JM", "JMD" ],        	             [ "JO", "" ],        	             [ "JP", "JYE" ],        	             [ "KE", "" ],        	             [ "KG", "" ],        	             [ "KH", "" ],        	             [ "KN", "XCD" ],        	             [ "KR", "KRW" ],        	             [ "KW", "KWD" ],        	             [ "KY", "KYD" ],        	             [ "KZ", "" ],        	             [ "LA", "" ],        	             [ "LB", "" ],        	             [ "LC", "XCD" ],        	             [ "LI", "CHF" ],        	             [ "LK", "" ],        	             [ "LR", "" ],        	             [ "LS", "" ],        	             [ "LT", "LTL" ],        	             [ "LU", "EUR" ],        	             [ "LV", "LVL" ],        	             [ "LY", "" ],        	             [ "MA", "" ],        	             [ "MC", "EUR" ],        	             [ "MD", "" ],        	             [ "ME", "EUR" ],        	             [ "MG", "" ],        	             [ "MH", "USD" ],        	             [ "MK", "" ],        	             [ "ML", "" ],        	             [ "MN", "" ],        	             [ "MO", "MOP" ],        	             [ "MP", "USD" ],        	             [ "MQ", "EUR" ],        	             [ "MR", "" ],        	             [ "MS", "XCD" ],        	             [ "MT", "EUR" ],        	             [ "MU", "" ],        	             [ "MV", "" ],        	             [ "MW", "" ],        	             [ "MX", "NMP" ],        	             [ "MY", "MYR" ],        	             [ "MZ", "" ],        	             [ "NA", "" ],        	             [ "NC", "" ],        	             [ "NE", "" ],        	             [ "NG", "" ],        	             [ "NI", "" ],        	             [ "NL", "EUR" ],        	             [ "NO", "NOK" ],        	             [ "NP", "" ],        	             [ "NZ", "NZD" ],        	             [ "OM", "" ],        	             [ "PA", "" ],        	             [ "PE", "" ],        	             [ "PF", "" ],        	             [ "PG", "" ],        	             [ "PH", "PHP" ],        	             [ "PK", "PKR" ],        	             [ "PL", "PLN" ],        	             [ "PR", "USD" ],        	             [ "PS", "" ],        	             [ "PT", "EUR" ],        	             [ "PW", "USD" ],        	             [ "PY", "" ],        	             [ "QA", "" ],        	             [ "RE", "EUR" ],        	             [ "RO", "" ],        	             [ "RS", "" ],        	             [ "RU", "" ],        	             [ "RW", "" ],        	             [ "SA", "SAR" ],        	             [ "SC", "" ],        	             [ "SE", "SEK" ],        	             [ "SG", "SGD" ],        	             [ "SI", "EUR" ],        	             [ "SK", "EUR" ],        	             [ "SN", "" ],        	             [ "SR", "" ],        	             [ "SV", "" ],        	             [ "SY", "" ],        	             [ "SZ", "" ],        	             [ "TC", "USD" ],        	             [ "TD", "" ],        	             [ "TG", "" ],        	             [ "TH", "THB" ],        	             [ "TL", "" ],        	             [ "TM", "" ],        	             [ "TN", "" ],        	             [ "TO", "" ],        	             [ "TR", "TRY" ],        	             [ "TT", "TTD" ],        	             [ "TW", "TWD" ],        	             [ "TZ", "" ],        	             [ "UA", "" ],        	             [ "UG", "" ],        	             [ "US", "USD" ],        	             [ "UY", "UYU" ],        	             [ "UZ", "" ],        	             [ "VC", "XCD" ],        	             [ "VE", "VEB" ],        	             [ "VG", "USD" ],        	             [ "VI", "USD" ],        	             [ "VN", "" ],        	             [ "VU", "" ],        	             [ "WF", "" ],        	             [ "WS", "" ],        	             [ "YE", "" ],        	             [ "ZA", "ZAR" ],        	             [ "ZM", "" ],        	             [ "ZW", "" ]        	            ];
		 	        function enableButtonsRowChecked(checkBox) {         	        	var btn = FindControl('btnHazMatAdd');
			if (btn != null) {   					btn.disabled=checkBox.checked;
			}			var btn = FindControl('btnHazMatEdit');
			if (btn != null) {   					btn.disabled=!checkBox.checked;
			}	    	var btn = FindControl('btnHazMatDelete');
			if (btn != null) {   					btn.disabled=!checkBox.checked;
			}        }        function disableSummaryButtons() {         	var btn = FindControl('btnHazMatAdd');
			if (btn != null) {   					btn.disabled=true;
			}			var btn = FindControl('btnHazMatEdit');
			if (btn != null) {   					btn.disabled=true;
			}	    	var btn = FindControl('btnHazMatDelete');
			if (btn != null) {   					btn.disabled=true;
			}        }          function enableSummaryButtons() {         	var btn = FindControl('btnHazMatAdd');
			if (btn != null) {   					btn.disabled=false;
			}			var btn = FindControl('btnHazMatEdit');
			if (btn != null) {   					btn.disabled=true;
			}	    	var btn = FindControl('btnHazMatDelete');
			if (btn != null) {   					btn.disabled=true;
			}			UnCheckTheCheckBox('chkHazMatCommodityCheck1');
			UnCheckTheCheckBox('chkHazMatCommodityCheck2');
			UnCheckTheCheckBox('chkHazMatCommodityCheck3');
        }        function Row1Check() {        	var theField = FindControl('chkHazMatCommodityCheck1');
    		    	if (theField != null && theField.checked) {	    		UnCheckTheCheckBox('chkHazMatCommodityCheck2');
	    		UnCheckTheCheckBox('chkHazMatCommodityCheck3');
	    	}	    	enableButtonsRowChecked(theField);
	    	        }        function Row2Check() {        	var theField = FindControl('chkHazMatCommodityCheck2');
    		    	if (theField != null && theField.checked) {	    		UnCheckTheCheckBox('chkHazMatCommodityCheck1');
	    		UnCheckTheCheckBox('chkHazMatCommodityCheck3');
	    	}	    	enableButtonsRowChecked(theField);
        }         function Row3Check() {        	var theField = FindControl('chkHazMatCommodityCheck3');
    		    	if (theField != null && theField.checked) {	    		UnCheckTheCheckBox('chkHazMatCommodityCheck1');
	    		UnCheckTheCheckBox('chkHazMatCommodityCheck2');
	    	}	    	enableButtonsRowChecked(theField);
        }    					function textCounter(field, maxlimit)		{			if (field.value.length >

 maxlimit) 				field.value = field.value.substring(0, maxlimit);
		}			 	function isHazMatAvailable()        {            var theAnswer = false;
             var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');
	        var isGroundSelected = false;
			        	for (var i=0;
 i<ddlServiceType.length;
 i++)			{		   		if(ddlServiceType.options[i].selected == true)		   		{			   			var serviceTypeValue = ddlServiceType.options[i].value;
		   						   		if (serviceTypeValue == 6) {			   				isGroundSelected = true;
			   		}		   		}			 }	 			if (isGroundSelected && 				isPackageCountEqualToOne() &&				isOriginCountryUSA() &&				isRecipientCountryUSA() && 				isOriginStateNotAkOrHi() &&				isRecipientStateNotAkOrHi()) {				theAnswer = true;
			}					 			return theAnswer;
        }                             	function isLithiumBatteryAvailable()         {             var theAnswer = false;
             var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');
	        var isGroundOrHomeDeliverySelected = false;
	              		        	for (var i=0;
 i<ddlServiceType.length;
 i++)			{		   		if(ddlServiceType.options[i].selected == true)		   		{			   			var serviceTypeValue = ddlServiceType.options[i].value;
		   						   		if (serviceTypeValue == 6 || serviceTypeValue == 15) {			   				isGroundOrHomeDeliverySelected = true;
			   		}		   					   		}			 }			 			if (isGroundOrHomeDeliverySelected && 				isPackageCountEqualToOne() &&				isOriginCountryUSA() &&				isRecipientCountryUSA()) {				theAnswer = true;
			}			 			return theAnswer;
        }                         function isOriginCountryUSA() {         		    var isOriginCntryUserEntry				= false ;
		    var isOriginCntryNotUserEntryButUSA  	= true ;
	    	var ddlCountry 							= FindControl('ddlCountry');
					    var isCountryUSA = false;
 		    if (isOriginCntryUserEntry)  {		   	    if (ddlCountry != null && ddlCountry.value != '-1' && ddlCountry.value == 'US' ) {		   	    	isCountryUSA = true;
		   	    } 			} else {				isCountryUSA = isOriginCntryNotUserEntryButUSA;
			}        	return isCountryUSA;
        }                  function isOriginStateNotAkOrHi() {         		    var isOriginStateOrProvinceUserEntry		= false ;
		    var isOriginStateNotUserEntryButNotAkOrHi  	= true ;
	        var ddlOriginState 							= FindControl('ddlUSAStates');
	        		    var isValidState = false;
 		    if (isOriginStateOrProvinceUserEntry)  {		   	    if (ddlOriginState != null && ( ddlOriginState.value != '-1' &&  ddlOriginState.value != 'AK' && ddlOriginState.value != 'HI') ) {		   	    	isValidState = true;
		   	    } 			} else {				isValidState = isOriginStateNotUserEntryButNotAkOrHi;
			}        	return isValidState;
        }                         function isRecipientCountryUSA() {         		    var isRecipientCntryUserEntry			= true ;
		    var isRecipientCntryNotUserEntryButUSA  = false ;
		    var ddlRecipientCountry 				= FindControl('ddlRecipientCountry');
	        		    var isCountryUSA = false;
 		    if (isRecipientCntryUserEntry)  {		   	    if (ddlRecipientCountry != null && ddlRecipientCountry.value != '-1' && ddlRecipientCountry.value == 'US') {		   	    	isCountryUSA = true;
		   	    } 			} else {				isCountryUSA = isRecipientCntryNotUserEntryButUSA;
			}        	return isCountryUSA;
        }                        function isRecipientStateNotAkOrHi() {         		    var isRecipientStateOrProvinceUserEntry			= true ;
		    var isRecipientStateNotUserEntryButNotAkOrHi  	= false ;
		    var ddlRecipientState 							= FindControl('ddlRecipientUSAStates');
	        		    var isValidState = false;
 		    if (isRecipientStateOrProvinceUserEntry)  {		   	    if (ddlRecipientState != null && ( ddlRecipientState.value != '-1' && ddlRecipientState.value != 'AK' && ddlRecipientState.value != 'HI') ) {		   	    	isValidState = true;
		   	    } 			} else {				isValidState = isRecipientStateNotUserEntryButNotAkOrHi;
			}        	return isValidState;
        }                        function isPackageCountEqualToOne() {                 var isPackagePieceCountChecked 				= true ;
         var ddlNoOfPkgs = document.getElementById('ddlPackageShippingDetailNumberOfPackages');
               var packagePieceCount = 0 ;
         			 if(isPackagePieceCountChecked){		   if  (ddlNoOfPkgs.value == '-1') { 		    	 packagePieceCount = 0;
			} else {				packagePieceCount = ddlNoOfPkgs.value;
			}		  } 						return  packagePieceCount == 1;
		}		              	function isSQEAvailable()         {            return isORMDAvailable();
         }              	function isORMDAvailable()         {             var theAnswer = false;
             var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');
	        var isGroundOrHomeDeliverySelected = false;
				              		        	for (var i=0;
 i<ddlServiceType.length;
 i++)			{		   		if(ddlServiceType.options[i].selected == true)		   		{			   			var serviceTypeValue = ddlServiceType.options[i].value;
		   						   		if (serviceTypeValue == 6 || serviceTypeValue == 15) {			   				isGroundOrHomeDeliverySelected = true;
			   		}		   					   		}			 }	 			if (isGroundOrHomeDeliverySelected && 				isPackageCountEqualToOne() &&				isOriginCountryUSA() &&				isRecipientCountryUSA() && 				isOriginStateNotAkOrHi() &&				isRecipientStateNotAkOrHi()) {				theAnswer = true;
			}					 			return theAnswer;
         }		function isDangerousGoodsAvailable()         {                       var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');
          	 	        var isValidServiceSelected = false;
	        var isYourPkgSelected = isYourPackagingSelected();
	              		        	for (var i=0;
 i<ddlServiceType.length;
 i++)			{		   		if(ddlServiceType.options[i].selected == true)		   		{			   			var serviceTypeValue = ddlServiceType.options[i].value;
		   			if(isYourPkgSelected &&  (serviceTypeValue == 7 ||		   			   						   serviceTypeValue == 13 ||								   			   serviceTypeValue == 14 ||								   			   serviceTypeValue == 20 ||								   			   serviceTypeValue == 2 ||								   			   serviceTypeValue == 5 ||								   			   serviceTypeValue == 21 ||								   			   serviceTypeValue == 1 ||								   			   serviceTypeValue == 3 ||		   			   								   			   serviceTypeValue == 4 || 								   			   serviceTypeValue == 10 ||		   			   								   			   serviceTypeValue == 11 ||	   			   								   			   serviceTypeValue == 8)		   			   )		   			{						isValidServiceSelected = true;
								   			}		   		}			 }			 			return isValidServiceSelected;
         }		function OnOriginUSAStatesChanged() {         	CheckDryIce();
         	CheckORMD();
	       	CheckSQE();
	       	CheckLithiumBattery();
	       	CheckHazMat();
         }         function OnRecipientUSAStatesChanged() {         	CheckDryIce();
         	CheckORMD();
	       	CheckSQE();
	       	CheckLithiumBattery();
	       	CheckHazMat();
         }    	 function isYourPackagingSelected()         {         	var ddlPackagingType = FindControl('ddlPackageShippingDetailPackageType');
                  	var isYourPackagingSel = false;
         					for(var i=0;
 i< ddlPackagingType.options.length;
 i++){			  var isSelected = ddlPackagingType.options[i].selected;
			  var selectedValue = ddlPackagingType.options[i].value;
			  if (isSelected && selectedValue == '6') {		 		isYourPackagingSel = true;
		 	  }   		 	}         	return isYourPackagingSel;
         }                function isDryIceAvailable()         {              return isDryIceAvailableCondition1() && isDryIceAvailableCondition2() ;
         }                function isDryIceAvailableCondition1()         {                      var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');
          	 	        var isValidServiceSelected = false;
	        var isYourPkgSelected = isYourPackagingSelected();
	              		        	for (var i=0;
 i<ddlServiceType.length;
 i++)			{		   		if(ddlServiceType.options[i].selected == true)		   		{			   			var serviceTypeValue = ddlServiceType.options[i].value;
		   			if(isYourPkgSelected &&  (serviceTypeValue == 7 ||		   			   						   serviceTypeValue == 13 ||								   			   serviceTypeValue == 14 ||								   			   serviceTypeValue == 20 ||								   			   serviceTypeValue == 2 ||								   			   serviceTypeValue == 5 ||								   			   serviceTypeValue == 21 ||								   			   serviceTypeValue == 1 ||								   			   serviceTypeValue == 3 ||		   			   								   			   serviceTypeValue == 4 || 								   			   serviceTypeValue == 10 ||		   			   								   			   serviceTypeValue == 11 ||	   			   								   			   serviceTypeValue == 8)		   			   )		   			{						isValidServiceSelected = true;
								   			} else 			   		if (serviceTypeValue == 6 || serviceTypeValue == 15) {			   				isValidServiceSelected = true;
			   		}		   					   		}			 }			 			return isValidServiceSelected;
         }                 function isDryIceAvailableCondition2()         {              var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');
          	var theAnswer = false;
 	        var isGroundOrHomeDeliverySelected = false;
	              		        	for (var i=0;
 i<ddlServiceType.length;
 i++)			{		   		if(ddlServiceType.options[i].selected == true)		   		{			   			var serviceTypeValue = ddlServiceType.options[i].value;
		   			if(serviceTypeValue == 6 ||		   			   serviceTypeValue == 15 )		   			{						isGroundOrHomeDeliverySelected = true;
								   			} 		   		}			}											  if (isGroundOrHomeDeliverySelected ) {			    if (isOriginCountryUSA() &&					isRecipientCountryUSA() &&					isOriginStateNotAkOrHi() &&					isRecipientStateNotAkOrHi()) {				theAnswer = true;
				}			} else {				theAnswer = true;
			}			 			return theAnswer;
         }        							function FindControl(control){    return document.getElementById(control);
}function IsHidden(control){	var obj = FindControl(control);
	if (obj != null && obj.style.display == "none") 		return true;
	else 		return false;
}function Show(){    for(var i=0;
 i<arguments.length;
 i++)      {     	var obj =  FindControl(arguments[i]);
     	if (obj != null)        	obj.style.display = "";
     }}function Hide(){     for(var i=0;
 i<arguments.length;
 i++)      {     	var obj =  FindControl(arguments[i]);
     	if (obj != null)        	obj.style.display = "none";
     }}function CheckTheCheckBox(checkbox){	if(FindControl(checkbox) != null){		FindControl(checkbox).checked = true;
	}}function UnCheckTheCheckBox(checkbox){	if(FindControl(checkbox) != null){		FindControl(checkbox).checked = false;
	}}function IsChecked(checkbox){	if(FindControl(checkbox) != null){		return FindControl(checkbox).checked;
	}}function GetDdlSelectedValue(ddl){        var  list = FindControl(ddl);
        for(i = 0;
 i < list.options.length;
i++)        {            if( list.options[i].selected)               return list.options[i].value;
        }	}function IsCheckboxChecked(checkbox) {	if(FindControl(checkbox) != null){		if(FindControl(checkbox).checked == 1) {			return true;
		}else {			return false;
		}	}}function SafelySetValue(theControl,theValue){	if(FindControl(theControl) != null){		FindControl(theControl).value = theValue;
	}}			function UpdateCurrency(myObj)			{							var ddlCurrencyType = document.getElementById('ddlCurrencyType');
								if (myObj != null && ddlCurrencyType != null) {					ChangeDdlSelectedIndex('ddlCurrencyType','-1');
					for(var i = 0;
 i<liste.length ;
 i++)					{						if(myObj.options[myObj.selectedIndex].value == liste[i][0])						{							for(var j = 0;
 j < ddlCurrencyType.options.length;
 j++)								if(ddlCurrencyType.options[j].value == liste[i][1]) ddlCurrencyType.options[j].selected = true;
						}					}								}			}						function ChangeCurrencyType(value) {				var txtCurrency = document.getElementById('txtCurrency');
				var txtCurrency2 = document.getElementById('txtCurrency2');
				var txtCurrency3 = document.getElementById('txtCurrency3');
				var txtCurrency4 = document.getElementById('txtCurrency4');
				var txtCurrency5 = document.getElementById('txtCurrency5');
				var txtCustomsCurrency = document.getElementById('txtCustomsCurrency');
						if(document.getElementById('trCurrencyType') != null){					if(document.getElementById('trCurrencyType').style.display == 'none'){						if (txtCurrency != null && value != '-1'){							txtCurrency.value = value;
							txtCurrency2.value = value;
							txtCurrency3.value = value;
							txtCurrency4.value = value;
							txtCurrency5.value = value;
								txtCustomsCurrency.value = value;
						}					}else{						if (txtCurrency != null && value != '-1'){							txtCurrency.value = "";
							txtCurrency2.value = "";
							txtCurrency3.value = "";
							txtCurrency4.value = "";
							txtCurrency5.value = "";
							txtCustomsCurrency.value = "";
							}						}				}			}						function SetStateVisibility()			{				var ddlCountry = document.getElementById('ddlCountry');
				var ddlUSAStates = document.getElementById('ddlUSAStates');
				var ddlCanadaStates = document.getElementById('ddlCanadaStates');
								UpdateCurrency(ddlCountry);
				ShowHideGenerateCustoms();
				ShowHideGoodsNotInFreeCirculation();
				ShowHideBillDutiesAndTaxes();
								if ( ddlCountry != null && ddlUSAStates != null && ddlCanadaStates != null){					if(ddlCountry.value == 'US')					{						ddlCanadaStates.style.display = "none";
						ddlUSAStates.style.display = "";
						ddlUSAStates.disabled = false;
						ddlCanadaStates.value = -1;
					}					else if(ddlCountry.value == 'CA')					{						ddlCanadaStates.style.display = "";
						ddlUSAStates.style.display = "none";
						ddlCanadaStates.disabled = false;
						ddlUSAStates.value = -1;
											}					else					{						ddlUSAStates.style.display = "none";
						ddlCanadaStates.style.display = "";
						ddlCanadaStates.disabled = true;
						ddlUSAStates.disabled = true;
						ddlUSAStates.value = -1;
						ddlCanadaStates.value = -1;
					}															SetSpecialServices();
								}								if (false == true){ 					ddlUSAStates.style.display = "";
					ddlUSAStates.disabled = false;
									}								if (false == true){ 					ddlCanadaStates.style.display = "";
					ddlCanadaStates.disabled = false;
				}				SetSubmitButtonLabel();
								}			function SetRecipientStateVisibility()			{				var ddlRecipientCountry = document.getElementById('ddlRecipientCountry');
				var ddlRecipientUSAStates = document.getElementById('ddlRecipientUSAStates');
				var ddlRecipientCanadaStates = document.getElementById('ddlRecipientCanadaStates');
				var addressValidation = document.getElementById('txtRecipientDetailedAddressCheck');
				if (ddlRecipientCountry != null && ddlRecipientUSAStates != null && ddlRecipientCanadaStates != null){					if(ddlRecipientCountry.value == 'US')					{						ddlRecipientCanadaStates.style.display = "none";
						ddlRecipientUSAStates.style.display = "";
						ddlRecipientUSAStates.disabled = false;
						if (addressValidation != null){							addressValidation.disabled = false;
						}						ddlRecipientCanadaStates.value = -1;
					}					else if(ddlRecipientCountry.value == 'CA')					{						ddlRecipientCanadaStates.style.display = "";
						ddlRecipientUSAStates.style.display = "none";
						ddlRecipientCanadaStates.disabled = false;
						if (addressValidation != null){							addressValidation.disabled = false;
						}						ddlRecipientUSAStates.value = -1;
					}					else					{						ddlRecipientCanadaStates.style.display = "none";
						ddlRecipientUSAStates.style.display = "";
						ddlRecipientCanadaStates.disabled = true;
						ddlRecipientUSAStates.disabled = true;
						if (addressValidation != null){							addressValidation.checked = false;
							addressValidation.disabled = true;
						}						ddlRecipientCanadaStates.value = -1;
						ddlRecipientUSAStates.value = -1;
					}					ShowHideGenerateCustoms();
					ShowHideGoodsNotInFreeCirculation();
					SetSpecialServices();
										if (false == true){ 						ddlRecipientUSAStates.style.display = "";
						ddlRecipientUSAStates.disabled = false;
						if (addressValidation != null){							addressValidation.disabled = false;
						}					}										if (false == true){ 						ddlRecipientCanadaStates.style.display = "";
						ddlRecipientCanadaStates.disabled = false;
						if (addressValidation != null){							addressValidation.disabled = false;
						}					}					}								SetSubmitButtonLabel();
							}		   	function SetSubmitButtonLabel()			{				var ddlRecipientCountry = FindControl('ddlRecipientCountry');
				var ddlCountry = FindControl('ddlCountry');
				var serviceType = FindControl('ddlPackageShippingDetailServiceType').value;
				var originCntrySelected = getOriginCountrySelected(originCntryCodeChecked,originCntryCode);
				var recipientCntrySelected = getRecipientCountrySelected(recipientCntryCodeChecked,recipientCntryCode);
				var continueButtonSet = false;
		   							if((originCntryCodeChecked == 'true' && ddlCountry.value == '-1') || ( recipientCntryCodeChecked == 'true' && ddlRecipientCountry.value == '-1')){					SetContinueButton();
				}else if ( isOriginNotSameAsRecipient(originCntrySelected,recipientCntrySelected) &&  isContinueButtonAllowedForServiceType() && IsHidden('trGoodsNotInFreeCirculation')) {						SetContinueButton();
 				}else if ((!IsHidden('trGoodsNotInFreeCirculation')) && IsCheckboxChecked('chkFreeCirculation')) {					SetContinueButton();
				}else if (IsGroundCustomsChecked(originCntrySelected,recipientCntrySelected)){					SetContinueButton();
				}else					SetGenerateLabel();
				}	  function isContinueButtonAllowedForServiceType(){		   	 var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');
		   	 var isServiceTypeSelected = false;
		   	 var allowedServiceType = true;
		            				   	for (var i=0;
 i<ddlServiceType.length;
 i++)		   	{		   	   	if(ddlServiceType.options[i].selected == true)		   	   	{			   	   		isServiceTypeSelected = true;
		   	   		var serviceType = ddlServiceType.options[i].value;
		   					   		   	if (serviceType == '16' || serviceType == '17' || serviceType == '18' || serviceType == '19' || serviceType == '15' || serviceType == '6') {		   		   		allowedServiceType = false;
		   		   	}		   	   				   		   break;
 			   	   	}		   	}		   		 		   	if(!isServiceTypeSelected){		   		return false;
			   	}else {		   		if(allowedServiceType)		   			return true;
		   		else		   			return false;
		   	}	   }	   	function SetSpecialServices(){		var serviceType = document.getElementById('ddlPackageShippingDetailServiceType').value;
		ShowHideBillDutiesAndTaxes();
		ShowHideEmailNotificationCheckBox();
				if(serviceType == '-1'){			if (true){				Hide('tblSpecialServices');
			}			Hide('trDropoffType');
            Hide('trSmartPostDropoffType');
	                			Hide('tblSmartPostSpecialServices');
			Hide('trHubId');
						Hide('trSmartPostNote');
		}else{						if(serviceType != '16' && serviceType != '17' && serviceType != '18' && serviceType != '19')			{								Hide('trSmartPostNote');
				Show('trDropoffType');
                Hide('trSmartPostDropoffType');
                if(FindControl('ddlSmartPostDropoffType') != null){                 	ChangeDdlSelectedIndex('ddlSmartPostDropoffType', -1);
                }                													Hide('tblSmartPostSpecialServices');
				Hide('trHubId');
				if (true){		       		var options = document.getElementById('hdnSpecialServicesOptions').value;
		      				      	if (options != 'null' && options != '')			      	{			      		Show('tblSpecialServices');
			      		ResetSmartPostSpecialService();
			      	}			      	if (options == 'null' || options == '')			      	{				   		Hide('trSignatureOption');
			   			Hide('trSignatureOptionType');
			   			document.getElementById('chkSignatureOptions').checked = "";
			   						   			Hide('trHomeDeliveryPremium');
			   			Hide('trHomeDeliveryPremiumType');
			   			document.getElementById('chkHomeDeliveryPremium').checked = "";
			   						   			Hide('trFutureDayShipment');
			   			document.getElementById('chkFutureDayShipment').checked = "";
			   						   			Hide('trDryIce');
			   			Hide('trDryIce2');
			   			UnCheckTheCheckBox('chkDryIce');
			   			Hide('trDangerousGoods');
			   			Hide('trDangerousGoodsType');
			   			UnCheckTheCheckBox('chkDangerousGoods');
			   						   			Hide('tblSpecialServices');
				    } else	if (options.search("1") != '-1' )			   		{			   			Show('trSignatureOption');
			   		}			   		 else 			   		{			   			Hide('trSignatureOption');
			   			Hide('trSignatureOptionType');
			   			document.getElementById('chkSignatureOptions').checked = "";
			   		}			   					   		if (options.search("2") != '-1')			   		{			   			if(serviceType == '15'){			   				Show('trHomeDeliveryPremium');
			   			}else{			   				Hide('trHomeDeliveryPremium');
			   				Hide('trHomeDeliveryPremiumType');
			   				Hide('trHomeDeliveryDate');
			   				Hide('trHomeDeliveryPhone');
			   				Hide('trHomeDeliveryPremium');
		   					document.getElementById('chkHomeDeliveryPremium').checked = "";
		   							   				ChangeDdlSelectedIndex('ddlHomeDeliveryPremiumType', -1);
							FindControl('txtHomeDeliveryDate').value = '';
							FindControl('txtHomeDeliveryPhone').value = '';
			   			}			   		} 			   		else 			   		{			   			Hide('trHomeDeliveryPremium');
			   			Hide('trHomeDeliveryPremiumType');
			   			Hide('trHomeDeliveryDate');
			   			Hide('trHomeDeliveryPhone');
			   			Hide('trHomeDeliveryPremium');
			   			document.getElementById('chkHomeDeliveryPremium').checked = "";
			   						   			ChangeDdlSelectedIndex('ddlHomeDeliveryPremiumType', -1);
						FindControl('txtHomeDeliveryDate').value = '';
						FindControl('txtHomeDeliveryPhone').value = '';
			   		}			   					   		if (options.search("3") != '-1' ) 			   		{			   			Show('trFutureDayShipment');
			   		} 			   		else 			   		{			   			Hide('trFutureDayShipment');
			   			document.getElementById('chkFutureDayShipment').checked = "";
			   		}			   					   		if (options.search("4") != '-1' )			   		{			   			Show('trDryIce');
			   		} 			   		else 			   		{			   			Hide('trDryIce');
			   			Hide('trDryIce2');
			   			UnCheckTheCheckBox('chkDryIce');
			   		}			   					   		if (options.search("5") != '-1' )			   		{			   			Show('trDangerousGoods');
			   		} 			   		else 			   		{			   			Hide('trDangerousGoods');
			   			Hide('trDangerousGoodsType');
			   			UnCheckTheCheckBox('chkDangerousGoods');
			   		}  			   	}			}			else			{								Show('trSmartPostNote');
				if(document.getElementById('hdnSmartPostPickupCarrier').value == '0'){					Show('trSmartPostDropoffType');
				}				Hide('trDropoffType');
   				if(FindControl('ddlDropoffType') != null){                         	ChangeDdlSelectedIndex('ddlDropoffType', -1);
                }                                Hide('tblSpecialServices');
				Show('tblSmartPostSpecialServices');
				ResetSpecialService();
				Show('trHubId');
										}	   	}		   	EnableDisablePackages();
	   	SetSubmitButtonLabel();
		   	if (true){	   		CheckDryIce();
	   		CheckDangerousGoods();
	   		CheckORMD();
	   		CheckSQE();
        	CheckLithiumBattery();
        	CheckHazMat();
  	   	}   	}   	   	function ResetSpecialService()   	{   		if (true)   		{			FindControl('chkSignatureOptions').checked = "";
			ChangeDdlSelectedIndex('ddlSignatureOptionType', -1);
						FindControl('chkHomeDeliveryPremium').checked = "";
			ChangeDdlSelectedIndex('ddlHomeDeliveryPremiumType', -1);
			FindControl('txtHomeDeliveryDate').value = '';
			FindControl('txtHomeDeliveryPhone').value = '';
									UnCheckTheCheckBox('chkDryIce');
			SafelySetValue('txtDryIceWeight', '');
						UnCheckTheCheckBox('chkDangerousGoods');
						ChangeDdlSelectedIndex('ddlDangerousGoodsType', -1);
		}   	}   	   	function ResetSmartPostSpecialService(){   		if(FindControl('ddlSmartPostUndeliverable') != null){                 	ChangeDdlSelectedIndex('ddlSmartPostUndeliverable', -1);
        }   	}   	   	function CheckDimensions(obj){    	if (obj.value == 'L' || obj.value == 'W'  || obj.value == 'H')    		obj.value = '';
    }	     	    function CheckSignatureOptions()    {    	if (document.getElementById('chkSignatureOptions').checked)    	{    		Show('trSignatureOptionType');
    	} else    	{			Hide('trSignatureOptionType');
		}	}		function CheckHomeDeliveryPremium()    {    	if (document.getElementById('chkHomeDeliveryPremium').checked)    	{    		Show('trHomeDeliveryPremiumType');
    		Show('trHomeDeliveryDate');
    		Show('trHomeDeliveryPhone');
    	} else    	{			Hide('trHomeDeliveryPremiumType');
			Hide('trHomeDeliveryDate');
    		Hide('trHomeDeliveryPhone');
		}	}	function CheckDryIce()        {    	Hide('trDryIce');
;
    	Hide('trDryIce2');
    		    	if (isDryIceAvailable())    	{    		Show('trDryIce');
    		if (IsChecked('chkDryIce'))    		{	    		var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');
	    		var serviceType = ddlServiceType.value;
    			if(!(serviceType == '6' || serviceType == '15'))     			{    				Show('trDryIce2');
    			}    		}    		    	}else {    		UnCheckTheCheckBox('chkDryIce');
    	}     	}		function CheckDangerousGoods()    {        	Hide('trDangerousGoods');
;
    	Hide('trDangerousGoodsType');
    		    	if (isDangerousGoodsAvailable())    	{    		Show('trDangerousGoods');
    		    		if (IsChecked('chkDangerousGoods'))    		{	    		    Show('trDangerousGoodsType');
    		}    		    	}else {    		UnCheckTheCheckBox('chkDangerousGoods');
    	}     	}		function CheckORMD()    {    	    	var theField = FindControl('chkORMD');
    	    	Hide('trORMD');
    	    	if (isORMDAvailable())    	{    		Show('trORMD');
    		    	}else {    		UnCheckTheCheckBox('chkORMD');
    	}    	    	if (theField != null && theField.checked) {    		UnCheckTheCheckBox('chkSQE');
    		UnCheckTheCheckBox('chkHazardousMaterials');
    			     	var hzMatSummary = FindControl('trHazMatSummary');
    			    	if (hzMatSummary != null) {   					hzMatSummary.style.visibility = "hidden";
			}    		    		    		    	}    	    	}		function CheckSQE()    {    	var theField = FindControl('chkSQE');
    	Hide('trSQE');
    		    	if (isSQEAvailable())    	{    		Show('trSQE');
    		    	}else {    		UnCheckTheCheckBox('chkSQE');
    	}    	    	if (theField != null && theField.checked) {    		UnCheckTheCheckBox('chkORMD');
    		UnCheckTheCheckBox('chkHazardousMaterials');
    			     	var hzMatSummary = FindControl('trHazMatSummary');
    			    	if (hzMatSummary != null) {   					hzMatSummary.style.visibility = "hidden";
			}    		    		    		    	}    	}			function CheckLithiumBattery()    {        	Hide('trLithiumBattery');
    		    	if (isLithiumBatteryAvailable())    	{    		Show('trLithiumBattery');
    		    	}else {    		UnCheckTheCheckBox('chkLithiumBattery');
    	}    		}		function CheckHazMat()    {    	Hide('trHazMat');
         	var hzMatSummary = FindControl('trHazMatSummary');
     	     	    	if (hzMatSummary != null) {   				hzMatSummary.style.visibility = "hidden";
		}		    	if (isHazMatAvailable())    	{	    		Show('trHazMat');
    		    	   	var theField = FindControl('chkHazardousMaterials');
    		    	if (theField != null && theField.checked) {	    		UnCheckTheCheckBox('chkORMD');
	    		UnCheckTheCheckBox('chkSQE');
	    			    		if (hzMatSummary != null) {   						hzMatSummary.style.visibility = "";
				}								disableSummaryButtons();
				var btn = FindControl('btnHazMatAdd');
				if (btn != null) {   						btn.disabled=false;
				}							    	}    	}    	else    	{    		UnCheckTheCheckBox('chkHazardousMaterials');
     		var btn = FindControl('btnGenerateLabel');
			if (btn != null) {   					btn.disabled=false;
			}  	    	    	}    		}		 function EnableDisablePackages()         {             	var ddlPackageShippingDetailNumberOfPackages = document.getElementById('ddlPackageShippingDetailNumberOfPackages');
         	var hdnPackagePieceCount = document.getElementById('hdnPackagePieceCount');
         	var serviceType = document.getElementById('ddlPackageShippingDetailServiceType').value;
         	var value;
         	if (ddlPackageShippingDetailNumberOfPackages == null){         		if(hdnPackagePieceCount != null){         			value = hdnPackagePieceCount.value;
         		}else{         			value = '-1';
         		}         	}         	else         		value = ddlPackageShippingDetailNumberOfPackages.value;
         	         	if (value == '-1')            {            	                     Show('trWeight');
                                Hide('trPackage1');
                Hide('trPackage2');
                Hide('trPackage3');
                Hide('trPackage4');
                Hide('trPackage5');
                                Hide('trWeight1');
                Hide('trWeight2');
                   Hide('trWeight3');
                Hide('trWeight4');
                    Hide('trWeight5');
                                Hide('trPackageDimensions1');
                Hide('trPackageDimensions2');
                Hide('trPackageDimensions3');
                Hide('trPackageDimensions4');
                Hide('trPackageDimensions5');
                                Hide('trCurrencyType');
                Hide('trDeclaredValue1');
                Hide('trDeclaredValue2');
                Hide('trDeclaredValue3');
                Hide('trDeclaredValue4');
                Hide('trDeclaredValue5');
            } else {	         	if(value == '1')	            {   	                	                Hide('trPackage2');
	                Hide('trPackage3');
	                Hide('trPackage4');
	                Hide('trPackage5');
	                	                Hide('trPackageDimensions2');
	                Hide('trPackageDimensions3');
	                Hide('trPackageDimensions4');
	                Hide('trPackageDimensions5');
    	                	                	                if ( 'true' == 'true' ){            			Show('trPackageDimensions1');
            			if(serviceType == '16' || serviceType == '17' || serviceType == '18' || serviceType == '19'){            				Show('lblPackageDimensions1NoAst');
            				Hide('lblPackageDimensions1');
            			}else{            				Show('lblPackageDimensions1');
            				Hide('lblPackageDimensions1NoAst');
            			}            			Hide('trPackage1');
            		}            		else  {            			Hide('trPackageDimensions1');
            			if ( 'true' == 'true' ){            				Show('trPackage1');
            			}else{            				Hide('trPackage1');
            			}            			if(serviceType == '16' || serviceType == '17' || serviceType == '18' || serviceType == '19'){            				Show('lblPackage1NoAst');
            				Hide('lblPackage1');
            			}else{            				Show('lblPackage1');
            				Hide('lblPackage1NoAst');
            			}              		}            		            		if(ddlPackageShippingDetailNumberOfPackages != null){            			Show('trWeight');
            		}else{            			Hide('trWeight');
            		}           			                	                Hide('trWeight1');
	                Hide('trWeight2');
   	                Hide('trWeight3');
	                Hide('trWeight4');
    	                Hide('trWeight5');
	                	                if(serviceType != '16' && serviceType != '17' && serviceType != '18' && serviceType != '19'){	                	Show('trDeclaredValue1');
	                	if ('$CurrencyChecked$' == 'true'){	                		Show('trCurrencyType');
	                	}else{	                		Hide('trCurrencyType');
	                	}	                }else{                		Hide('trDeclaredValue1');
                		Hide('trCurrencyType');
                	}                 	Hide('trDeclaredValue2');
                	Hide('trDeclaredValue3');
                	Hide('trDeclaredValue4');
                	Hide('trDeclaredValue5');
	    	            }	            else if(value == '2')	            { 	                	                Hide('trPackage3');
	                Hide('trPackage4');
	                Hide('trPackage5');
 	                	                Hide('trPackageDimensions3');
	                Hide('trPackageDimensions4');
	                Hide('trPackageDimensions5');
 	                	                if ( 'true' == 'true' ){            			Show('trPackageDimensions1');
            			Show('trPackageDimensions2');
            			if(serviceType == '16' || serviceType == '17' || serviceType == '18' || serviceType == '19'){            				Show('lblPackageDimensions1NoAst');
            				Hide('lblPackageDimensions1');
            				Show('lblPackageDimensions2NoAst');
            				Hide('lblPackageDimensions2');
            			}else{            				Show('lblPackageDimensions1');
            				Hide('lblPackageDimensions1NoAst');
            				Show('lblPackageDimensions2');
            				Hide('lblPackageDimensions2NoAst');
            			}            			Hide('trPackage1');
            			Hide('trPackage2');
            		}            		else  {            			Hide('trPackageDimensions1');
            			Hide('trPackageDimensions2');
            			if ( 'true' == 'true' ){	            			Show('trPackage1');
		                	Show('trPackage2');
	                	}else{	                		Hide('trPackage1');
		                	Hide('trPackage2');
	                	}	                	if(serviceType == '16' || serviceType == '17' || serviceType == '18' || serviceType == '19'){            				Show('lblPackage1NoAst');
            				Hide('lblPackage1');
            				Show('lblPackage2NoAst');
            				Hide('lblPackage2');
            			}else{            				Show('lblPackage1');
            				Hide('lblPackage1NoAst');
            				Show('lblPackage2');
            				Hide('lblPackage2NoAst');
            			}            		}	                Hide('trWeight');
	                	                if(ddlPackageShippingDetailNumberOfPackages != null){	                	Show('trWeight1');
	                	Show('trWeight2');
 	                }else{	                	Hide('trWeight1');
	                	Hide('trWeight2');
 	                }  	                Hide('trWeight3');
	                Hide('trWeight4');
    	                Hide('trWeight5');
   	                	                if(serviceType != '16' && serviceType != '17' && serviceType != '18' && serviceType != '19'){		                Show('trDeclaredValue1');
	                	Show('trDeclaredValue2');
	                	if ('$CurrencyChecked$' == 'true'){	                		Show('trCurrencyType');
	                	}else{	                		Hide('trCurrencyType');
	                	}                	}else{                		Hide('trDeclaredValue1');
	                	Hide('trDeclaredValue2');
	                	Hide('trCurrencyType');
                	}                 	Hide('trDeclaredValue3');
                	Hide('trDeclaredValue4');
                	Hide('trDeclaredValue5');
            	    	            }	            else if(value == '3')	            { 	                	                Hide('trPackage4');
	                Hide('trPackage5');
	                	                Hide('trPackageDimensions4');
	                Hide('trPackageDimensions5');
	                	                if ( 'true' == 'true' ){            			Show('trPackageDimensions1');
            			Show('trPackageDimensions2');
            			Show('trPackageDimensions3');
            			if(serviceType == '16' || serviceType == '17' || serviceType == '18' || serviceType == '19'){            				Show('lblPackageDimensions1NoAst');
            				Hide('lblPackageDimensions1');
            				Show('lblPackageDimensions2NoAst');
            				Hide('lblPackageDimensions2');
            				Show('lblPackageDimensions3NoAst');
            				Hide('lblPackageDimensions3');
            			}else{            				Show('lblPackageDimensions1');
            				Hide('lblPackageDimensions1NoAst');
            				Show('lblPackageDimensions2');
            				Hide('lblPackageDimensions2NoAst');
            				Show('lblPackageDimensions3');
            				Hide('lblPackageDimensions3NoAst');
            			}            			Hide('trPackage1');
            			Hide('trPackage2');
            			Hide('trPackage3');
            		}            		else  {            			Hide('trPackageDimensions1');
            			Hide('trPackageDimensions2');
            			Hide('trPackageDimensions3');
            			if ( 'true' == 'true' ){	            			Show('trPackage1');
		                	Show('trPackage2');
		                	Show('trPackage3');
	                	}else{	                		Hide('trPackage1');
		                	Hide('trPackage2');
		                	Hide('trPackage3');
	                	}	                	if(serviceType == '16' || serviceType == '17' || serviceType == '18' || serviceType == '19'){            				Show('lblPackage1NoAst');
            				Hide('lblPackage1');
            				Show('lblPackage2NoAst');
            				Hide('lblPackage2');
            				Show('lblPackage3NoAst');
            				Hide('lblPackage3');
            			}else{            				Show('lblPackage1');
            				Hide('lblPackage1NoAst');
            				Show('lblPackage2');
            				Hide('lblPackage2NoAst');
            				Show('lblPackage3');
            				Hide('lblPackage3NoAst');
            			}            		}	                Hide('trWeight');
	                	                if(ddlPackageShippingDetailNumberOfPackages != null){		                Show('trWeight1');
		                Show('trWeight2');
   		                Show('trWeight3');
	                }else{	                	Hide('trWeight1');
		                Hide('trWeight2');
   		                Hide('trWeight3');
	                }	                Hide('trWeight4');
    	                Hide('trWeight5');
	                	                if(serviceType != '16' && serviceType != '17' && serviceType != '18' && serviceType != '19'){		                Show('trDeclaredValue1');
	                	Show('trDeclaredValue2');
	                	Show('trDeclaredValue3');
	                	if ('$CurrencyChecked$' == 'true'){	                		Show('trCurrencyType');
	                	}else{	                		Hide('trCurrencyType');
	                	}                	}else{                		Hide('trDeclaredValue1');
	                	Hide('trDeclaredValue2');
	                	Hide('trDeclaredValue3');
	                	Hide('trCurrencyType');
                	}                 	Hide('trDeclaredValue4');
                	Hide('trDeclaredValue5');
      	            }	            else if(value == '4')	            {   	                Hide('trPackage5');
	                	                Hide('trPackageDimensions5');
	                	                if ( 'true' == 'true'  ){            			Show('trPackageDimensions1');
            			Show('trPackageDimensions2');
            			Show('trPackageDimensions3');
            			Show('trPackageDimensions4');
            			if(serviceType == '16' || serviceType == '17' || serviceType == '18' || serviceType == '19'){            				Show('lblPackageDimensions1NoAst');
            				Hide('lblPackageDimensions1');
            				Show('lblPackageDimensions2NoAst');
            				Hide('lblPackageDimensions2');
            				Show('lblPackageDimensions3NoAst');
            				Hide('lblPackageDimensions3');
            				Show('lblPackageDimensions4NoAst');
            				Hide('lblPackageDimensions4');
            			}else{            				Show('lblPackageDimensions1');
            				Hide('lblPackageDimensions1NoAst');
            				Show('lblPackageDimensions2');
            				Hide('lblPackageDimensions2NoAst');
            				Show('lblPackageDimensions3');
            				Hide('lblPackageDimensions3NoAst');
            				Show('lblPackageDimensions4');
            				Hide('lblPackageDimensions4NoAst');
            			}            			Hide('trPackage1');
            			Hide('trPackage2');
            			Hide('trPackage3');
            			Hide('trPackage4');
            		}            		else  {            			Hide('trPackageDimensions1');
            			Hide('trPackageDimensions2');
            			Hide('trPackageDimensions3');
            			Hide('trPackageDimensions4');
            			if ( 'true' == 'true' ){	            			Show('trPackage1');
		                	Show('trPackage2');
		                	Show('trPackage3');
		                	Show('trPackage4');
	                	}else{	                		Hide('trPackage1');
		                	Hide('trPackage2');
		                	Hide('trPackage3');
		                	Hide('trPackage4');
	                	}	                	if(serviceType == '16' || serviceType == '17' || serviceType == '18' || serviceType == '19'){            				Show('lblPackage1NoAst');
            				Hide('lblPackage1');
            				Show('lblPackage2NoAst');
            				Hide('lblPackage2');
            				Show('lblPackage3NoAst');
            				Hide('lblPackage3');
            				Show('lblPackage4NoAst');
            				Hide('lblPackage4');
            			}else{            				Show('lblPackage1');
            				Hide('lblPackage1NoAst');
            				Show('lblPackage2');
            				Hide('lblPackage2NoAst');
            				Show('lblPackage3');
            				Hide('lblPackage3NoAst');
            				Show('lblPackage4');
            				Hide('lblPackage4NoAst');
            			}            		}	                Hide('trWeight');
	                	                if(ddlPackageShippingDetailNumberOfPackages != null){		                Show('trWeight1');
		                Show('trWeight2');
   		                Show('trWeight3');
		                Show('trWeight4');
  	                } else{	                	Hide('trWeight1');
		                Hide('trWeight2');
   		                Hide('trWeight3');
		                Hide('trWeight4');
  	                } 	                Hide('trWeight5');
  	                	                if(serviceType != '16' && serviceType != '17' && serviceType != '18' && serviceType != '19'){		                Show('trDeclaredValue1');
	                	Show('trDeclaredValue2');
	                	Show('trDeclaredValue3');
	                	Show('trDeclaredValue4');
	                	if ('$CurrencyChecked$' == 'true'){	                		Show('trCurrencyType');
	                	}else{	                		Hide('trCurrencyType');
	                	}                	}else{                		Hide('trDeclaredValue1');
	                	Hide('trDeclaredValue2');
	                	Hide('trDeclaredValue3');
	                	Hide('trDeclaredValue4');
	                	Hide('trCurrencyType');
                	}                 	Hide('trDeclaredValue5');
   	            }	            else if(value == '5')	            {	                if ( 'true' == 'true'  ){            			Show('trPackageDimensions1');
            			Show('trPackageDimensions2');
            			Show('trPackageDimensions3');
            			Show('trPackageDimensions4');
            			Show('trPackageDimensions5');
            			if(serviceType == '16' || serviceType == '17' || serviceType == '18' || serviceType == '19'){            				Show('lblPackageDimensions1NoAst');
            				Hide('lblPackageDimensions1');
            				Show('lblPackageDimensions2NoAst');
            				Hide('lblPackageDimensions2');
            				Show('lblPackageDimensions3NoAst');
            				Hide('lblPackageDimensions3');
            				Show('lblPackageDimensions4NoAst');
            				Hide('lblPackageDimensions4');
            				Show('lblPackageDimensions5NoAst');
            				Hide('lblPackageDimensions5');
            			}else{            				Show('lblPackageDimensions1');
            				Hide('lblPackageDimensions1NoAst');
            				Show('lblPackageDimensions2');
            				Hide('lblPackageDimensions2NoAst');
            				Show('lblPackageDimensions3');
            				Hide('lblPackageDimensions3NoAst');
            				Show('lblPackageDimensions4');
            				Hide('lblPackageDimensions4NoAst');
            				Show('lblPackageDimensions5');
            				Hide('lblPackageDimensions5NoAst');
            			}            			Hide('trPackage1');
            			Hide('trPackage2');
            			Hide('trPackage3');
            			Hide('trPackage4');
            			Hide('trPackage5');
            		}            		else  {            			Hide('trPackageDimensions1');
            			Hide('trPackageDimensions2');
            			Hide('trPackageDimensions3');
            			Hide('trPackageDimensions4');
            			Hide('trPackageDimensions5');
            			if ( 'true' == 'true' ){	            			Show('trPackage1');
		                	Show('trPackage2');
		                	Show('trPackage3');
		                	Show('trPackage4');
		                	Show('trPackage5');
	                	}else{	                		Hide('trPackage1');
		                	Hide('trPackage2');
		                	Hide('trPackage3');
		                	Hide('trPackage4');
		                	Hide('trPackage5');
	                	}	                	if(serviceType == '16' || serviceType == '17' || serviceType == '18' || serviceType == '19'){            				Show('lblPackage1NoAst');
            				Hide('lblPackage1');
            				Show('lblPackage2NoAst');
            				Hide('lblPackage2');
            				Show('lblPackage3NoAst');
            				Hide('lblPackage3');
            				Show('lblPackage4NoAst');
            				Hide('lblPackage4');
            				Show('lblPackage5NoAst');
            				Hide('lblPackage5');
            			}else{            				Show('lblPackage1');
            				Hide('lblPackage1NoAst');
            				Show('lblPackage2');
            				Hide('lblPackage2NoAst');
            				Show('lblPackage3');
            				Hide('lblPackage3NoAst');
            				Show('lblPackage4');
            				Hide('lblPackage4NoAst');
            				Show('lblPackage5');
            				Hide('lblPackage5NoAst');
            			}            		}            		Hide('trWeight');
	                if(ddlPackageShippingDetailNumberOfPackages != null){		                Show('trWeight1');
		                Show('trWeight2');
   		                Show('trWeight3');
		                Show('trWeight4');
    		                Show('trWeight5');
 	                }else{	                	Hide('trWeight1');
		                Hide('trWeight2');
   		                Hide('trWeight3');
		                Hide('trWeight4');
    		                Hide('trWeight5');
 	                }	                	                if(serviceType != '16' && serviceType != '17' && serviceType != '18' && serviceType != '19'){		                Show('trDeclaredValue1');
	                	Show('trDeclaredValue2');
	                	Show('trDeclaredValue3');
	                	Show('trDeclaredValue4');
	                	Show('trDeclaredValue5');
 	                	if ('$CurrencyChecked$' == 'true'){	                		Show('trCurrencyType');
	                	}else{	                		Hide('trCurrencyType');
	                	}                	}else{                		Hide('trDeclaredValue1');
	                	Hide('trDeclaredValue2');
	                	Hide('trDeclaredValue3');
	                	Hide('trDeclaredValue4');
	                	Hide('trDeclaredValue5');
	                	Hide('trCurrencyType');
                	} 	            } 	            	         }	         if ( '3' == '1' || document.getElementById('ddlPackageShippingDetailPackageType').value != '6')            {            	                     Hide('trPackage1');
                Hide('trPackage2');
                Hide('trPackage3');
                Hide('trPackage4');
                Hide('trPackage5');
                                Hide('trPackageDimensions1');
                Hide('trPackageDimensions2');
                Hide('trPackageDimensions3');
                Hide('trPackageDimensions4');
                Hide('trPackageDimensions5');
            }                        if ( 'false' == 'true'  ){            	Show('trPackageDimensions');
            }            else{            	Hide('trPackageDimensions');
	            }                          CheckDryIce();
                   CheckDangerousGoods();
            CheckSQE();
            CheckORMD();
        	CheckLithiumBattery();
        	CheckHazMat();
           }            function ChangeAdditionalRecipients(){				var additionalRecipient = document.getElementById('additionalRecipient');
				if (additionalRecipient != null ){					if (additionalRecipient.value == '1') 						additionalRecipient.value = '0';
					else 						additionalRecipient.value = '1';
				}								additionalRecipients();
											}            function additionalRecipients()			{				var spnAdditionalRecipients1 = document.getElementById('additionalRecipients1');
				var spnAdditionalRecipients2 = document.getElementById('additionalRecipients2');
				var spnAdditionalRecipients3 = document.getElementById('additionalRecipients3');
				var spnAdditionalRecipients4 = document.getElementById('additionalRecipients4');
								var additionalRecipient = document.getElementById('additionalRecipient');
				if (additionalRecipient != null && additionalRecipient.value == '1') {						spnAdditionalRecipients1.style.display = "";
						spnAdditionalRecipients2.style.display = "";
						spnAdditionalRecipients3.style.display = "";
						spnAdditionalRecipients4.style.display = "";
				}				else {						spnAdditionalRecipients1.style.display = "none";
						spnAdditionalRecipients2.style.display = "none";
						spnAdditionalRecipients3.style.display = "none";
						spnAdditionalRecipients4.style.display = "none";
				}			}						function ChangePersonalMessage(){				var txtPersonalMessage = document.getElementById('personalMessage');
				if (txtPersonalMessage != null ){					if (txtPersonalMessage.value == '1') 						txtPersonalMessage.value = '0';
					else 						txtPersonalMessage.value = '1';
				}								personalMessage();
											}            function personalMessage()			{				var spnPersonalMessage = document.getElementById('trPersonalMessage');
				var txtPersonalMessage = document.getElementById('personalMessage');
				if (txtPersonalMessage != null && txtPersonalMessage.value == '1') {						spnPersonalMessage.style.display = "";
				}				else {						spnPersonalMessage.style.display = "none";
				}			}					function ChangeDdlSelectedIndex(control, value) 		{			var list = document.getElementById(control);
						if(!list)				return;
							for (i = 0;
 i < list.options.length;
 i++) {				if (list.options[i].value == value)					list.options[i].selected = "selected";
				else					list.options[i].selected = "";
			}		}				function ShowHideEmailNotificationCheckBox()		{			var serviceType = FindControl('ddlPackageShippingDetailServiceType').value;
				var isSmartPostDeliveryEnabled = FindControl('hdnSmartPostDeliveryConfirmation').value;
			var isTenderedEmailAllowed = FindControl('hdnIsTenderedEmailAllowed').value;
						if(serviceType == '16' || serviceType == '17' || serviceType == '18' || serviceType == '19' || serviceType == '-1'){	        	Hide('tdExceptionNotification');
	        	Hide('tdEmailNotificationsSenderException');
	            Hide('tdEmailNotificationsRecipientException');
	            Hide('tdEmailNotificationsOther1Exception');
	            Hide('tdEmailNotificationsOther2Exception');
	            UnCheckTheCheckBox('rdbEmailNotificationsSenderException');
	            UnCheckTheCheckBox('rdbEmailNotificationsRecipientException');
	            UnCheckTheCheckBox('rdbEmailNotificationsOther1Exception');
	            UnCheckTheCheckBox('rdbEmailNotificationsOther2Exception');
	            	            if(isSmartPostDeliveryEnabled != '1'){	            	Hide('tdDeliveryNotification');
		        	Hide('tdEmailNotificationsSenderDelivery');
		            Hide('tdEmailNotificationsRecipientDelivery');
		            Hide('tdEmailNotificationsOther1Delivery');
		            Hide('tdEmailNotificationsOther2Delivery');
		            UnCheckTheCheckBox('rdbEmailNotificationsSenderDelivery');
		            UnCheckTheCheckBox('rdbEmailNotificationsRecipientDelivery');
		            UnCheckTheCheckBox('rdbEmailNotificationsOther1Delivery');
		            UnCheckTheCheckBox('rdbEmailNotificationsOther2Delivery');
	            }else{	            	Show('tdDeliveryNotification');
		        	Show('tdEmailNotificationsSenderDelivery');
		            Show('tdEmailNotificationsRecipientDelivery');
		            Show('tdEmailNotificationsOther1Delivery');
		            Show('tdEmailNotificationsOther2Delivery');
	            }	            	            if(isTenderedEmailAllowed == 'true'){					Hide('tdShipNotification');
	        		Hide('tdEmailNotificationsSenderShip');
	        		Hide('tdEmailNotificationsRecipientShip');
	        		Hide('tdEmailNotificationsOther1Ship');
	        		Hide('tdEmailNotificationsOther2Ship');
		        	UnCheckTheCheckBox('rdbEmailNotificationsSenderShip');
		        	UnCheckTheCheckBox('rdbEmailNotificationsRecipientShip');
	        		UnCheckTheCheckBox('rdbEmailNotificationsOther1Ship');
	        		UnCheckTheCheckBox('rdbEmailNotificationsOther2Ship');
	        			        		Show('tdTenderedNotification');
	        		Show('tdEmailNotificationsSenderTendered');
	        		Show('tdEmailNotificationsRecipientTendered');
	        		Show('tdEmailNotificationsOther1Tendered');
	        		Show('tdEmailNotificationsOther2Tendered');
				}else{					Show('tdShipNotification');
		        	Show('tdEmailNotificationsSenderShip');
		        	Show('tdEmailNotificationsRecipientShip');
	        		Show('tdEmailNotificationsOther1Ship');
	        		Show('tdEmailNotificationsOther2Ship');
	        			        		Hide('tdTenderedNotification');
	        		Hide('tdEmailNotificationsSenderTendered');
	        		Hide('tdEmailNotificationsRecipientTendered');
	        		Hide('tdEmailNotificationsOther1Tendered');
	        		Hide('tdEmailNotificationsOther2Tendered');
	        			        		UnCheckTheCheckBox('rdbEmailNotificationsSenderTendered');
	        		UnCheckTheCheckBox('rdbEmailNotificationsRecipientTendered');
	        		UnCheckTheCheckBox('rdbEmailNotificationsOther1Tendered');
	        		UnCheckTheCheckBox('rdbEmailNotificationsOther2Tendered');
				} 	            	        }else{	        	Show('tdExceptionNotification');
	        	Show('tdEmailNotificationsSenderException');
	            Show('tdEmailNotificationsRecipientException');
	            Show('tdEmailNotificationsOther1Exception');
	            Show('tdEmailNotificationsOther2Exception');
	            	            Show('tdDeliveryNotification');
		        Show('tdEmailNotificationsSenderDelivery');
		        Show('tdEmailNotificationsRecipientDelivery');
		        Show('tdEmailNotificationsOther1Delivery');
		        Show('tdEmailNotificationsOther2Delivery');
			        		        if(isTenderedEmailAllowed == 'true'){					Show('tdShipNotification');
	        		Show('tdEmailNotificationsSenderShip');
	        		Show('tdEmailNotificationsRecipientShip');
	        		Show('tdEmailNotificationsOther1Ship');
	        		Show('tdEmailNotificationsOther2Ship');
	        			        		Show('tdTenderedNotification');
	        		Show('tdEmailNotificationsSenderTendered');
	        		Show('tdEmailNotificationsRecipientTendered');
	        		Show('tdEmailNotificationsOther1Tendered');
	        		Show('tdEmailNotificationsOther2Tendered');
				}else{					Show('tdShipNotification');
		        	Show('tdEmailNotificationsSenderShip');
		        	Show('tdEmailNotificationsRecipientShip');
	        		Show('tdEmailNotificationsOther1Ship');
	        		Show('tdEmailNotificationsOther2Ship');
	        			        		Hide('tdTenderedNotification');
	        		Hide('tdEmailNotificationsSenderTendered');
	        		Hide('tdEmailNotificationsRecipientTendered');
	        		Hide('tdEmailNotificationsOther1Tendered');
	        		Hide('tdEmailNotificationsOther2Tendered');
	        			        		UnCheckTheCheckBox('rdbEmailNotificationsSenderTendered');
	        		UnCheckTheCheckBox('rdbEmailNotificationsRecipientTendered');
	        		UnCheckTheCheckBox('rdbEmailNotificationsOther1Tendered');
	        		UnCheckTheCheckBox('rdbEmailNotificationsOther2Tendered');
				}         	        }		}		function ShowHideBillDutiesAndTaxes() {    		var originCntrySelected = getOriginCountrySelected(originCntryCodeChecked,originCntryCode);
    		var recipientCntrySelected = getRecipientCountrySelected(recipientCntryCodeChecked,recipientCntryCode);
	    	if (isBillDutiesAndTaxesAllowed(originCntrySelected,recipientCntrySelected)){	    		Show('trBillingDutiesAndTaxesTo');
 	    		Show('trBillingDutiesAndTaxesAccount');
	    		Show('trBillingDutiesAndTaxesCountry');
 			}else {				hideDutiesAndTaxesFields();
			}	    	}		function ShowHideGoodsNotInFreeCirculation()	{		var originCntrySelected = getOriginCountrySelected(originCntryCodeChecked,originCntryCode);
    	var recipientCntrySelected = getRecipientCountrySelected(recipientCntryCodeChecked,recipientCntryCode);
		var isOriginInEU = isEUCountry(originCntrySelected);
		var isRecipientInEU = isEUCountry(recipientCntrySelected);
		var isServiceTypeAllowed = isGoodsFreeCirculationAllowedServiceType();
		if (isServiceTypeAllowed && isOriginNotSameAsRecipient(originCntrySelected,recipientCntrySelected) && isOriginInEU && isRecipientInEU)		{			Show('trGoodsNotInFreeCirculation');
		}else{			Hide('trGoodsNotInFreeCirculation');
			UnCheckTheCheckBox('chkFreeCirculation');
		}			}		function ShowHideGenerateCustoms() {        	var originCntrySelected = getOriginCountrySelected(originCntryCodeChecked,originCntryCode);
        	var recipientCntrySelected = getRecipientCountrySelected(recipientCntryCodeChecked,recipientCntryCode);
    	    var isCustomsSelected = 'false';
    	    var isGroundSelected = isFedExGroundSelected();
    	            	            	    if (isGroundSelected && isOriginNotSameAsRecipient(originCntrySelected,recipientCntrySelected) && isCustomsSelected == 'true')  {    	        Show('trCustomsDoc');
    	    }else {    	        Hide('trCustomsDoc');
    	        UnCheckTheCheckBox('chkCustomsDoc');
    	    }    	    var isCustomsGenerationHidden = IsHidden('trCustomsDoc');
     	    var isCustomsGenerationShown = !IsHidden('trCustomsDoc');
    	    if(isGroundSelected && isOriginNotSameAsRecipient(originCntrySelected,recipientCntrySelected) && (isCustomsGenerationHidden || (isCustomsGenerationShown && !IsCheckboxChecked('chkCustomsDoc') ))) {    			Show('trCustomsValue');
    		}else {    			Hide('trCustomsValue');
    		}       	 }			   function getOriginCountrySelected(originCntryChecked, originCntryCode) {	            	    	var isOriginCntryUserEntry				=  originCntryChecked;
	    	var originCountryCode					=  null;
	        var ddlCountry 							=  FindControl('ddlCountry');
	    		 	    	if (isOriginCntryUserEntry  == 'true')  {	    	   	if (ddlCountry != null && ddlCountry.value != '-1') {	    	   	   originCountryCode = GetDdlSelectedValue('ddlCountry');
	    	   	 }	    	}else {	    			originCountryCode = originCntryCode;
	    	}	        return originCountryCode;
	    }		    function getRecipientCountrySelected(recipientCntryChecked,recipientCntryCode) {	        	        var isRecipientCntryUserEntry				=  recipientCntryChecked;
	        var recipientCountryCode					=  null ;
	    	var ddlRecipientCountry 					=  FindControl('ddlRecipientCountry');
		        if (isRecipientCntryUserEntry  == 'true')  {	       	    if (ddlRecipientCountry != null && ddlRecipientCountry.value != '-1') {	       	    	recipientCountryCode = GetDdlSelectedValue('ddlRecipientCountry');
	       	    }	       	     	    	} else {	    			recipientCountryCode = recipientCntryCode;
	    	}	    	return recipientCountryCode;
	    }		    function isOriginNotSameAsRecipient(originCntry,recipientCntry) {	    	   return (originCntry !=  null && recipientCntry != null && originCntry != recipientCntry);
	    }		    function isOriginSameAsRecipient(originCntry,recipientCntry) {	    	   return (originCntry !=  null && recipientCntry != null && originCntry == recipientCntry);
	    }	    function isCntryNotUSAorCAorMX(cntry){	    	return (cntry != null && cntry != 'US' && cntry != 'MX' && cntry != 'CA');
	    }	    function isFedExGroundSelected()	    {  	        var serviceTypeValue = GetDdlSelectedValue('ddlPackageShippingDetailServiceType');
	        if(serviceTypeValue == 6)	       	{	     		return true;
							         }	         return false;
	    }   		    function isEUCountry(country)	    {	    	var euCountriesList =   ['AT','BE','BG','CY','CZ','DL','EE','FI','FR','DE','GR','HU','IE','IT','LV','LT','LU','MT','NL','PL','RO','SK','SI','ES','SE','GB'];
	    	for(var i = 0;
 i<euCountriesList.length ;
 i++)	    	{	    		var euCountry  = null;
	    		if (country != null && country == euCountriesList[i]){	    			euCountry = euCountriesList[i];
	    			break;
	    		}	    	}	    	if (euCountry != null)	    		return true;
	    		    	return false;
	    }	    function isGoodsFreeCirculationAllowedServiceType() {	   	 var serviceTypeValue = GetDdlSelectedValue('ddlPackageShippingDetailServiceType');
	   	 if(serviceTypeValue == 0 || serviceTypeValue == 10 || serviceTypeValue == 11 || serviceTypeValue == 8)	   	 {	   	      return true;
							   	 }	   	 return false;
	   }	   function isBillDutiesAndTaxesAllowed(originCntry,recipientCntry){	   	if (originCntry != null && recipientCntry != null && isOriginNotSameAsRecipient(originCntry,recipientCntry)){	   		 var serviceTypeValue = GetDdlSelectedValue('ddlPackageShippingDetailServiceType');
	   		 if ((serviceTypeValue != null && serviceTypeValue == '-1') || (serviceTypeValue != null && serviceTypeValue != '16' && serviceTypeValue != '17' && serviceTypeValue != '18' && serviceTypeValue != '19')){	     			 		return true;
	   		 }	   		 return false;
	   	}	   	return false;
	   }	   function hideDutiesAndTaxesFields(){	   	Hide('trBillingDutiesAndTaxesTo');
 	   	Hide('trBillingDutiesAndTaxesAccount');
	   	Hide('trBillingDutiesAndTaxesCountry');
 	   }	   function SetContinueButton() {	       FindControl('btnGenerateLabel').value = "Continue";
	   }	    function SetGenerateLabel() {	       	FindControl('btnGenerateLabel').value = "Generate Label";
	   }	   function IsGroundCustomsChecked(originCode, recipientCode) {	   	 var isGroundSelected = isFedExGroundSelected();
	   	 var isCustomsGenerationShown = !IsHidden('trCustomsDoc');
	   	 if(isGroundSelected && isOriginNotSameAsRecipient(originCode,recipientCode) && (isCustomsGenerationShown && IsCheckboxChecked('chkCustomsDoc') )) {	   		return true;
	   	  }	   	return false;
  	    }	   function ShowHideSenderTaxId(){		   var originCntrySelected = getOriginCountrySelected(originCntryCodeChecked,originCntryCode);
       	   var recipientCntrySelected = getRecipientCountrySelected(recipientCntryCodeChecked,recipientCntryCode);
		    var senderTaxIdUserEntry = 'false';
		    if (senderTaxIdUserEntry == "true" && isOriginNotSameAsRecipient(originCntrySelected,recipientCntrySelected) && isServiceTypeAllowedForTaxId()){			    Show('trSenderTaxId');
		    }else{			    Hide('trSenderTaxId');
		    }			    	   	}		function ShowHideRecipientTaxId(){			var originCntrySelected = getOriginCountrySelected(originCntryCodeChecked,originCntryCode);
        	var recipientCntrySelected = getRecipientCountrySelected(recipientCntryCodeChecked,recipientCntryCode);
		    var recipientTaxIdUserEntry = 'true';
		    if (recipientTaxIdUserEntry == "true" && isOriginNotSameAsRecipient(originCntrySelected,recipientCntrySelected) && isServiceTypeAllowedForTaxId()){			    Show('trRecipientTaxId');
		    }else{			    Hide('trRecipientTaxId');
		    }			    	   	}		function isServiceTypeAllowedForTaxId(){			 var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');
			 var isServiceTypeSelected = false;
			 var allowedServiceType = true;
		           					for (var i=0;
 i<ddlServiceType.length;
 i++)			{			   	if(ddlServiceType.options[i].selected == true)			   	{				   		isServiceTypeSelected = true;
			   		var serviceTypeValue = ddlServiceType.options[i].value;
		   							   	if (serviceTypeValue == 16 || serviceTypeValue == 17 || serviceTypeValue == 19) {				   		allowedServiceType = false;
				   	}			   	   break;
				   	}			}				 			if(!isServiceTypeSelected){				return false;
				}else {				if(allowedServiceType)					return true;
				else					return false;
			}		}	        function ShowHideTable()          {         	var originCountry = 'US';
         	if (originCountry != 'null'){        		ChangeDdlSelectedIndex('ddlCountry', originCountry);
        		Hide("trOriginCountry");
        		        	}        		        	var recipientCountry = 'null';
        	if (recipientCountry != 'null'){        		ChangeDdlSelectedIndex('ddlRecipientCountry', recipientCountry);
        		Hide("trRecipientCountry");
        	}        	        		        	SetStateVisibility();
        	SetRecipientStateVisibility();
        	ShowSmartPostNote();
        	        	if (true){	    	    SetSpecialServices();
	    	    CheckSignatureOptions();
				CheckHomeDeliveryPremium();
				CheckDryIce();
				CheckDangerousGoods();
				CheckSQE();
				CheckORMD();
        		CheckLithiumBattery();
        		CheckHazMat();
   			} 						var selectedServiceType = '13';
			if (selectedServiceType != 'null')					ChangeDdlSelectedIndex('ddlPackageShippingDetailServiceType', selectedServiceType);
								var selectedPackagingType = 'null';
			if (selectedPackagingType != 'null')				ChangeDdlSelectedIndex('ddlPackageShippingDetailPackageType', selectedPackagingType);
							ShowDeclaredValues();
						EnableDisablePackages();
 			ShowHideEmailNotificationCheckBox();
			ShowHideGenerateCustoms();
			ShowHideGoodsNotInFreeCirculation();
			ShowHideBillDutiesAndTaxes();
			ShowHideSenderTaxId();
			ShowHideRecipientTaxId();
			SetSubmitButtonLabel();
        }                  function ShowDeclaredValues(){        	var packagePieceCount = 'null' ;
   			   			if (packagePieceCount == '1'){   				Show('trDeclaredValue1');
   			}    			else if (packagePieceCount == '2'){   				Show('trDeclaredValue1');
   				Show('trDeclaredValue2');
   			}   			else if (packagePieceCount == '3'){   				Show('trDeclaredValue1');
   				Show('trDeclaredValue2');
   				Show('trDeclaredValue3');
   			}   			else if (packagePieceCount == '4'){   				Show('trDeclaredValue1');
   				Show('trDeclaredValue2');
   				Show('trDeclaredValue3');
   				Show('trDeclaredValue4');
   			}   			else if (packagePieceCount == '5'){   				Show('trDeclaredValue1');
   				Show('trDeclaredValue2');
   				Show('trDeclaredValue3');
   				Show('trDeclaredValue4');
   				Show('trDeclaredValue5');
   			}	        }         function ShowSmartPostNote(){        	var serviceType = FindControl('ddlPackageShippingDetailServiceType').value;
	        	if (serviceType == 16 || serviceType == 17 || serviceType == 18 || serviceType == 19){            	Show('trSmartPostNote');
        	}        	Hide('trSmartPostNote');
        }            </script>

    <style type="text/css">

        .style1        {            width: 65%;        }        .style2        {            width: 5px;        }        .style3        {            width: 30%;        }    
    </style>
    </head>
    
<body  style="width: 695px;" onLoad="ShowHideTable()">

<form name='mainForm' method='post' action='https://www.fedex.com/FWIW/Client/Shipping_v4/ClientShippingStandAloneInit' id='mainForm'>

<table width='100%' border='0'>

	<tr>

		<td align='left'>

	<table style="width: 100%;" border='0'>

		<tr>

			<td align='left'>

			<td style='height: 234px'>

<br />

<br />

			<table class="designPreviewBox" border="0" cellpadding="5" cellspacing="0" width="100%">

													         <tr >

<td>

* Designates a required field</td>

</tr>

				<tr>

			<!-- begin tdMain -->

			<td valign="top" id="tdMain">

				<table>

					<tr>

									 <!-- end tblOrigin -->

					</tr>

										<tr>

										<!--Recipient Information-->

	<!-- begin tblRecipient -->

	                <td>

	                					<table id="tblShippingRecipientInformation" border="0" cellpadding="3" cellspacing="0" width="100%" style='border: #660099 1px solid; font-family:Arial; text-align: left; color:Black; background-color:Whitesmoke; font-size:11px;'>

					<tr>

						<td colspan="3"							style="font-weight: bold; color: white; background-color: #660099; height: 20px;">

						&nbsp;Recipient Information</td>

					</tr>

					<!-- begin trContactName -->

					<tr>

						<td style="width: 34%;">

&nbsp;<span id="lblRecipientContactName">

*Contact name</span>

</td>

						<td style="width: 1%;">

</td>

						<td style="width: 65%">

<input name="txtRecipientContactName" value="<bean:write name="address" property="name"/>"						type="text" maxlength="35" id="txtRecipientContactName" style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

						</td>

					</tr>

					<!-- end trContactName -->

					<!-- begin trCompany -->

					<tr>

						<td style="width: 34%;">

&nbsp;<span id="lblRecipientCompanyName">

Company</span>

</td>

						<td style="width: 1%;">

</td>

						<td style="width: 65%">

<input name="txtRecipientCompanyName"	value="<bean:write name="address" property="organization"/>"						type="text" maxlength="35" id="txtRecipientCompanyName" style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

						</td>

					</tr>

					<!-- end trCompany -->

										<tr id = "trRecipientCountry">

						<td style="width: 34%;">

&nbsp;<span id="lblRecipientCountry">

*Country</span>

</td>

						<td style="width: 1%;">

</td>

						<td style="width: 65%">

<select name="ddlRecipientCountry" id="ddlRecipientCountry" class="dropdown" onChange="SetRecipientStateVisibility();ShowHideSenderTaxId();ShowHideRecipientTaxId();" style='width: 180px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >
    <logic:present name="country">
        <option  value="<bean:write name="country" property="code"/>" selected="true"/><bean:write name="country" property="name"/>
    </logic:present>
    <logic:notPresent name="country">
        <option value="-1">Select</option>    
    </logic:notPresent>

								<!-- begin AllCountries -->

								<!-- begin OutboundShipment2 -->

								<option value="AF">

Afghanistan</option>

								<option value="AL">

Albania</option>

								<option value="DZ">

Algeria</option>

								<option value="AS">

American Samoa</option>

								<option value="AD">

Andorra</option>

								<option value="AO">

Angola</option>

								<option value="AI">

Anguilla</option>

								<option value="AG">

Antigua/Barbuda</option>

								<option value="AR">

Argentina</option>

								<option value="AM">

Armenia</option>

								<option value="AW">

Aruba</option>

								<option value="AU">

Australia</option>

								<option value="AT">

Austria</option>

								<option value="AZ">

Azerbaijan</option>

								<option value="BS">

Bahamas</option>

								<option value="BH">

Bahrain</option>

								<option value="BD">

Bangladesh</option>

								<option value="BB">

Barbados</option>

								<option value="BY">

Belarus</option>

								<option value="BE">

Belgium</option>

								<option value="BZ">

Belize</option>

								<option value="BJ">

Benin</option>

								<option value="BM">

Bermuda</option>

								<option value="BT">

Bhutan</option>

								<option value="BO">

Bolivia</option>

								<option value="BA">

Bosnia-Herzegovina</option>

								<option value="BW">

Botswana</option>

								<option value="BR">

Brazil</option>

								<option value="VG">

British Virgin Islands</option>

								<option value="BN">

Brunei</option>

								<option value="BG">

Bulgaria</option>

								<option value="BF">

Burkina Faso</option>

								<option value="BI">

Burundi</option>

								<option value="KH">

Cambodia</option>

								<option value="CM">

Cameroon</option>

								<option value="CA">

Canada</option>

								<option value="CV">

Cape Verde</option>

								<option value="KY">

Cayman Islands</option>

								<option value="TD">

Chad</option>

								<option value="CL">

Chile</option>

								<option value="CN">

China</option>

								<option value="CO">

Colombia</option>

								<option value="CG">

Congo Brazzaville</option>

								<option value="CD">

Congo Democratic Rep. of</option>

								<option value="CK">

Cook Islands</option>

								<option value="CR">

Costa Rica</option>

								<option value="HR">

Croatia</option>

								<option value="CY">

Cyprus</option>

								<option value="CZ">

Czech Republic</option>

								<option value="DK">

Denmark</option>

								<option value="DJ">

Djibouti</option>

								<option value="DM">

Dominica</option>

								<option value="DO">

Dominican Republic</option>

								<option value="TL">

East Timor</option>

								<option value="EC">

Ecuador</option>

								<option value="EG">

Egypt</option>

								<option value="SV">

El Salvador</option>

								<option value="GQ">

Equatorial Guinea</option>

								<option value="ER">

Eritrea</option>

								<option value="EE">

Estonia</option>

								<option value="ET">

Ethiopia</option>

								<option value="FO">

Faeroe Islands</option>

								<option value="FJ">

Fiji</option>

								<option value="FI">

Finland</option>

								<option value="FR">

France</option>

								<option value="GF">

French Guiana</option>

								<option value="PF">

French Polynesia</option>

								<option value="GA">

Gabon</option>

								<option value="GM">

Gambia</option>

								<option value="GE">

Georgia</option>

								<option value="DE">

Germany</option>

								<option value="GH">

Ghana</option>

								<option value="GI">

Gibraltar</option>

								<option value="GR">

Greece</option>

								<option value="GL">

Greenland</option>

								<option value="GD">

Grenada</option>

								<option value="GP">

Guadeloupe</option>

								<option value="GU">

Guam</option>

								<option value="GT">

Guatemala</option>

								<option value="GN">

Guinea</option>

								<option value="GY">

Guyana</option>

								<option value="HT">

Haiti</option>

								<option value="HN">

Honduras</option>

								<option value="HK">

Hong Kong</option>

								<option value="HU">

Hungary</option>

								<option value="IS">

Iceland</option>

								<option value="IN">

India</option>

								<option value="ID">

Indonesia</option>

								<option value="IQ">

Iraq</option>

								<option value="IE">

Ireland</option>

								<option value="IL">

Israel</option>

								<option value="IT">

Italy</option>

								<option value="CI">

Ivory Coast</option>

								<option value="JM">

Jamaica</option>

								<option value="JP">

Japan</option>

								<option value="JO">

Jordan</option>

								<option value="KZ">

Kazakhstan</option>

								<option value="KE">

Kenya</option>

								<option value="KW">

Kuwait</option>

								<option value="KG">

Kyrgyzstan</option>

								<option value="LA">

Laos</option>

								<option value="LV">

Latvia</option>

								<option value="LB">

Lebanon</option>

								<option value="LS">

Lesotho</option>

								<option value="LR">

Liberia</option>

								<option value="LY">

Libya</option>

								<option value="LI">

Liechtenstein</option>

								<option value="LT">

Lithuania</option>

								<option value="LU">

Luxembourg</option>

								<option value="MO">

Macau</option>

								<option value="MK">

Macedonia</option>

								<option value="MG">

Madagascar</option>

								<option value="MW">

Malawi</option>

								<option value="MY">

Malaysia</option>

								<option value="MV">

Maldives</option>

								<option value="ML">

Mali</option>

								<option value="MT">

Malta</option>

								<option value="MH">

Marshall Islands</option>

								<option value="MQ">

Martinique</option>

								<option value="MR">

Mauritania</option>

								<option value="MU">

Mauritius</option>

								<option value="MX">

Mexico</option>

								<option value="FM">

Micronesia</option>

								<option value="MD">

Moldova</option>

								<option value="MC">

Monaco</option>

								<option value="MN">

Mongolia</option>

								<option value="ME">

Montenegro</option>

								<option value="MS">

Montserrat</option>

								<option value="MA">

Morocco</option>

								<option value="MZ">

Mozambique</option>

								<option value="NA">

Namibia</option>

								<option value="NP">

Nepal</option>

								<option value="NL">

Netherlands</option>

								<option value="AN">

Netherlands Antilles</option>

								<option value="NC">

New Caledonia</option>

								<option value="NZ">

New Zealand</option>

								<option value="NI">

Nicaragua</option>

								<option value="NE">

Niger</option>

								<option value="NG">

Nigeria</option>

								<option value="NO">

Norway</option>

								<option value="OM">

Oman</option>

								<option value="PK">

Pakistan</option>

								<option value="PW">

Palau</option>

								<option value="PS">

Palestine Autonomous</option>

								<option value="PA">

Panama</option>

								<option value="PG">

Papua New Guinea</option>

								<option value="PY">

Paraguay</option>

								<option value="PE">

Peru</option>

								<option value="PH">

Philippines</option>

								<option value="PL">

Poland</option>

								<option value="PT">

Portugal</option>

								<option value="PR">

Puerto Rico</option>

								<option value="QA">

Qatar</option>

								<option value="RE">

Reunion</option>

								<option value="RO">

Romania</option>

								<option value="RU">

Russian Federation</option>

								<option value="RW">

Rwanda</option>

								<option value="MP">

Saipan</option>

								<option value="WS">

Samoa</option>

								<option value="SA">

Saudi Arabia</option>

								<option value="SN">

Senegal</option>

								<option value="RS">

Serbia</option>

								<option value="SC">

Seychelles</option>

								<option value="SG">

Singapore</option>

								<option value="SK">

Slovakia</option>

								<option value="SI">

Slovenia</option>

								<option value="ZA">

South Africa</option>

								<option value="KR">

South Korea</option>

								<option value="ES">

Spain</option>

								<option value="LK">

Sri Lanka</option>

								<option value="KN">

St. Kitts/Nevis</option>

								<option value="LC">

St. Lucia</option>

								<option value="VC">

St. Vincent</option>

								<option value="SR">

Suriname</option>

								<option value="SZ">

Swaziland</option>

								<option value="SE">

Sweden</option>

								<option value="CH">

Switzerland</option>

								<option value="SY">

Syria</option>

								<option value="TW">

Taiwan</option>

								<option value="TZ">

Tanzania</option>

								<option value="TH">

Thailand</option>

								<option value="TG">

Togo</option>

								<option value="TO">

Tonga</option>

								<option value="TT">

Trinidad/Tobago</option>

								<option value="TN">

Tunisia</option>

								<option value="TR">

Turkey</option>

								<option value="TM">

Turkmenistan</option>

								<option value="TC">

Turks &amp; Caicos Islands</option>

								<option value="UG">

Uganda</option>

								<option value="UA">

Ukraine</option>

								<option value="AE">

United Arab Emirates</option>

								<option value="GB">

United Kingdom</option>

								<option value="VI">

U.S. Virgin Islands</option>

								<!-- end OutboundShipment2 -->

								<option value="US">

U.S.A.</option>

								<!-- begin OutboundShipment2a -->

																<option value="UY">

Uruguay</option>

								<option value="UZ">

Uzbekistan</option>

								<option value="VU">

Vanuatu</option>

								<option value="VE">

Venezuela</option>

								<option value="VN">

Vietnam</option>

								<option value="WF">

Wallis &amp; Futuna</option>

								<option value="YE">

Yemen</option>

								<option value="ZM">

Zambia</option>

								<option value="ZW">

Zimbabwe</option>

								<!-- end OutboundShipment2a -->

								<!-- end AllCountries -->

								 <!-- end SmartPostCountries -->

						</select>

					</td>

				</tr>

							<!-- begin trAddress1 -->

				<tr>

					<td style="width: 34%;">

&nbsp;<span id="lblRecipientAddress1">

*Address 1</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 65%">

<input name="txtRecipientAddress1"	value="<bean:write name="address" property="addressline1"/>"					type="text" maxlength="35" id="txtRecipientAddress1" style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

					</td>

				</tr>

				<!-- end trAddress1 -->

								 <!-- end trAddress2 -->

								<!-- begin trCity -->

				<tr>

					<td style="width: 34%;">

&nbsp;<span id="lblRecipientCity">

*City</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 65%">

<input name="txtRecipientCity"	value="<bean:write name="address" property="city"/>"					type="text" maxlength="35" id="txtRecipientCity" style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

					</td>

				</tr>

				<!-- end trCity -->

								<!-- begin trStateOrProvinceCode -->

				<tr>

					<td style="width: 34%;">

&nbsp;<span id="lblRecipientStateOrProvince">

*State/province</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 65%">

					<!-- begin ddlRecipientCanadaStates -->

						<select name="ddlRecipientCanadaStates" id="ddlRecipientCanadaStates" class="dropdown" style='width: 180px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >

    <logic:present name="state">
        <option  value="<bean:write name="state" property="code"/>" selected="true"/><bean:write name="state" property="name"/>
    </logic:present>
    <logic:notPresent name="state">
        <option value="-1">Select</option>    
    </logic:notPresent>

							<option value="AB">

Alberta</option>

							<option value="BC">

British Columbia</option>

							<option value="MB">

Manitoba</option>

							<option value="NB">

New Brunswick</option>

							<option value="NL">

Newfoundland</option>

							<option value="NT">

Northwest Territories</option>

							<option value="NS">

Nova Scotia</option>

							<option value="NU">

Nunavut</option>

							<option value="ON">

Ontario</option>

							<option value="PE">

Prince Edward Island</option>

							<option value="QC">

Quebec</option>

							<option value="SK">

Saskatchewan</option>

							<option value="YT">

Yukon</option>

						</select>

 						<!-- end ddlRecipientCanadaStates -->

												<!-- begin ddlRecipientUSAStates -->

						<select name="ddlRecipientUSAStates" id="ddlRecipientUSAStates" class="dropdown" onChange="OnRecipientUSAStatesChanged();"  style='width: 180px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black; ' >

    <logic:present name="state">
        <option  value="<bean:write name="state" property="code"/>" selected="true"/><bean:write name="state" property="name"/>
    </logic:present>
    <logic:notPresent name="state">
        <option value="-1">Select</option>    
    </logic:notPresent>

							<option value="AL">

Alabama</option>

							<option value="AK">

Alaska</option>

							<option value="AZ">

Arizona</option>

							<option value="AR">

Arkansas</option>

							 <!-- end ddlArmedForcesStates -->

							<option value="CA">

California</option>

							<option value="CO">

Colorado</option>

							<option value="CT">

Connecticut</option>

							<option value="DE">

Delaware</option>

							<option value="DC">

District of Columbia</option>

							<option value="FL">

Florida</option>

							<option value="GA">

Georgia</option>

							<option value="HI">

Hawaii</option>

							<option value="ID">

Idaho</option>

							<option value="IL">

Illinois</option>

							<option value="IN">

Indiana</option>

							<option value="IA">

Iowa</option>

							<option value="KS">

Kansas</option>

							<option value="KY">

Kentucky</option>

							<option value="LA">

Louisiana</option>

							<option value="ME">

Maine</option>

							<option value="MD">

Maryland</option>

							<option value="MA">

Massachusetts</option>

							<option value="MI">

Michigan</option>

							<option value="MN">

Minnesota</option>

							<option value="MS">

Mississippi</option>

							<option value="MO">

Missouri</option>

							<option value="MT">

Montana</option>

							<option value="NE">

Nebraska</option>

							<option value="NV">

Nevada</option>

							<option value="NH">

New Hampshire</option>

							<option value="NJ">

New Jersey</option>

							<option value="NM">

New Mexico</option>

							<option value="NY">

New York</option>

							<option value="NC">

North Carolina</option>

							<option value="ND">

North Dakota</option>

							<option value="OH">

Ohio</option>

							<option value="OK">

Oklahoma</option>

							<option value="OR">

Oregon</option>

							<option value="PA">

Pennsylvania</option>

							<option value="RI">

Rhode Island</option>

							<option value="SC">

South Carolina</option>

							<option value="SD">

South Dakota</option>

							<option value="TN">

Tennessee</option>

							<option value="TX">

Texas</option>

							<option value="UT">

Utah</option>

							<option value="VT">

Vermont</option>

							<option value="VA">

Virginia</option>

							<option value="WA">

Washington</option>

							<option value="WV">

West Virginia</option>

							<option value="WI">

Wisconsin</option>

							<option value="WY">

Wyoming</option>

						</select>

						<!-- end ddlRecipientUSAStates -->

 					</td>

				</tr>

				<!-- end trStateOrProvinceCode -->

								<!-- begin trPostalCode -->

				<tr>

					<td style="width: 34%;">

&nbsp;<span id="lblRecipientPostalCode">

*Postal code</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 65%">

<input name="txtRecipientPostalCode"	value="<bean:write name="address" property="zipcode"/>"					type="text" maxlength="16" id="txtRecipientPostalCode" style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

					</td>

				</tr>

				<!-- end trPostalCode -->

								<!-- begin trTelephone -->

				<tr>

					<td style="width: 34%;">

&nbsp;<span id="lblRecipientTelephone">

*Phone no.</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 65%">

<input name="txtRecipientTelephone"	value="<bean:write name="address" property="phone"/>"					type="text" maxlength="15" id="txtRecipientTelephone" style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;'/>

					</td>

				</tr>

				<!-- end trTelephone -->

									<!-- begin trRecipientTaxId -->

				<tr id="trRecipientTaxId">

					<td style="width: 34%;">

&nbsp;<span id="lblRecipientTaxId">

Recipient tax<br>

&nbsp;ID no.</span>

					<img src="https://www.fedex.com/FWIW/Client/Images/Common/appModuleHelpIcon.gif"  title="Examples of Tax ID numbers are Internal Revenue Service (IRS) Employer Identification Number (EIN), Value Added Tax (VAT) Number, and Economic Operators Registration and Identification Number (EORI)."/>

</td>

						<td style="width: 1%;">

</td>

																		<td style="width: 65%">

<input name="txtRecipientTaxId"						type="text" maxlength="17" id="txtRecipientTaxId" style='width:180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

					</td>

				</tr>

				<!-- end trRecipientTaxId -->

								<!-- begin trRecipientDetailedAddressCheck -->

				<tr>

					<td style="width: 34%;"/>

					<td style="width: 1%;">

</td>

					<td style="width: 65%">

<input name="txtRecipientDetailedAddressCheck" type="checkbox" id="txtRecipientDetailedAddressCheck" />

&nbsp;Perform detailed address checking					</td>

				</tr>

				<!-- end trRecipientDetailedAddressCheck -->

								<!-- begin trRecipientResidentialAddress -->

				<tr>

					<td style="width: 34%;"/>

					<td style="width: 1%;">

</td>

					<td style="width: 65%">

<input name="txtRecipientResidentialAddress" type="checkbox" id="txtRecipientResidentialAddress" />

&nbsp;This is a residential address					</td>

				</tr>

				<!-- end trRecipientResidentialAddress -->

      	  	</table>

      	  	      	</td>

      	<!-- end tblRecipient -->

										</tr>

										<tr>

					<!--Billing Details-->

							<td>

                           							<table id="tblShippingBillingDetails" border="0" cellpadding="3" cellspacing="0" width="100%" style='border: #660099 1px solid; font-family:Arial; text-align: left; color:Black; background-color:Whitesmoke; font-size:11px;'>

							<tr>

								<td colspan="3"										style="font-weight: bold; color: white; background-color: #660099; height: 20px;">

										&nbsp;Billing Details</td>

							</tr>

							<!-- begin tblBillingDetails -->

							<tr>

								<td style="width: 34%;">

&nbsp;<span id="lblBillingDetailBillTransportationTo">

*Bill transportation to</span>

</td>

								<td style="width: 1%;">

</td>

								<td style="width: 65%">

									<select name="ddlBillTransportationTo" id="ddlBillTransportationTo" class="dropdown" style='width: 180px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >

										<option value="-1">

Select</option>

					        			<option value="1">

Sender</option>

					                    <!-- begin returnBillingDetails -->

					                                                                					                    <option value="2">

Recipient</option>

					                    <!-- end returnBillingDetails -->

					                                                                					                    <option value="3">

Third Party</option>

									</select>

								</td>

							</tr>

							<tr>

								<td style="width: 34%;">

&nbsp;<span id="lblBillingDetailAccountNumber">

*Transportation account no.</span>

</td>

								<td style="width: 1%;">

</td>

								<td style="width: 65%">

<input name="txtBillingDetailAccountNumber" type="text" maxlength="9" id="txtBillingDetailAccountNumber" style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

								</td>

							</tr>

																																			<tr>

								<td style="width: 34%;">

&nbsp;<span id="lblBillingCountryCode">

*Transportation billing country</span>

</td>

								<td style="width: 1%;">

</td>

								<td style="width: 65%">

<select name="ddlBillingCountry" id="ddlBillingCountry" class="dropdown" style='width: 180px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >

									<option value="-1">

Select</option>

									<option value="AF">

Afghanistan</option>

									<option value="AL">

Albania</option>

									<option value="DZ">

Algeria</option>

									<option value="AS">

American Samoa</option>

									<option value="AD">

Andorra</option>

									<option value="AO">

Angola</option>

									<option value="AI">

Anguilla</option>

									<option value="AG">

Antigua/Barbuda</option>

									<option value="AR">

Argentina</option>

									<option value="AM">

Armenia</option>

									<option value="AW">

Aruba</option>

									<option value="AU">

Australia</option>

									<option value="AT">

Austria</option>

									<option value="AZ">

Azerbaijan</option>

									<option value="BS">

Bahamas</option>

									<option value="BH">

Bahrain</option>

									<option value="BD">

Bangladesh</option>

									<option value="BB">

Barbados</option>

									<option value="BY">

Belarus</option>

									<option value="BE">

Belgium</option>

									<option value="BZ">

Belize</option>

									<option value="BJ">

Benin</option>

									<option value="BM">

Bermuda</option>

									<option value="BT">

Bhutan</option>

									<option value="BO">

Bolivia</option>

									<option value="BA">

Bosnia-Herzegovina</option>

									<option value="BW">

Botswana</option>

									<option value="BR">

Brazil</option>

									<option value="VG">

British Virgin Islands</option>

									<option value="BN">

Brunei</option>

									<option value="BG">

Bulgaria</option>

									<option value="BF">

Burkina Faso</option>

									<option value="BI">

Burundi</option>

									<option value="KH">

Cambodia</option>

									<option value="CM">

Cameroon</option>

									<option value="CA">

Canada</option>

									<option value="CV">

Cape Verde</option>

									<option value="KY">

Cayman Islands</option>

									<option value="TD">

Chad</option>

									<option value="CL">

Chile</option>

									<option value="CN">

China</option>

									<option value="CO">

Colombia</option>

									<option value="CG">

Congo Brazzaville</option>

									<option value="CD">

Congo Democratic Rep. of</option>

									<option value="CK">

Cook Islands</option>

									<option value="CR">

Costa Rica</option>

									<option value="HR">

Croatia</option>

									<option value="CY">

Cyprus</option>

									<option value="CZ">

Czech Republic</option>

									<option value="DK">

Denmark</option>

									<option value="DJ">

Djibouti</option>

									<option value="DM">

Dominica</option>

									<option value="DO">

Dominican Republic</option>

									<option value="TL">

East Timor</option>

									<option value="EC">

Ecuador</option>

									<option value="EG">

Egypt</option>

									<option value="SV">

El Salvador</option>

									<option value="GQ">

Equatorial Guinea</option>

									<option value="ER">

Eritrea</option>

									<option value="EE">

Estonia</option>

									<option value="ET">

Ethiopia</option>

									<option value="FO">

Faeroe Islands</option>

									<option value="FJ">

Fiji</option>

									<option value="FI">

Finland</option>

									<option value="FR">

France</option>

									<option value="GF">

French Guiana</option>

									<option value="PF">

French Polynesia</option>

									<option value="GA">

Gabon</option>

									<option value="GM">

Gambia</option>

									<option value="GE">

Georgia</option>

									<option value="DE">

Germany</option>

									<option value="GH">

Ghana</option>

									<option value="GI">

Gibraltar</option>

									<option value="GR">

Greece</option>

									<option value="GL">

Greenland</option>

									<option value="GD">

Grenada</option>

									<option value="GP">

Guadeloupe</option>

									<option value="GU">

Guam</option>

									<option value="GT">

Guatemala</option>

									<option value="GN">

Guinea</option>

									<option value="GY">

Guyana</option>

									<option value="HT">

Haiti</option>

									<option value="HN">

Honduras</option>

									<option value="HK">

Hong Kong</option>

									<option value="HU">

Hungary</option>

									<option value="IS">

Iceland</option>

									<option value="IN">

India</option>

									<option value="ID">

Indonesia</option>

									<option value="IQ">

Iraq</option>

									<option value="IE">

Ireland</option>

									<option value="IL">

Israel</option>

									<option value="IT">

Italy</option>

									<option value="CI">

Ivory Coast</option>

									<option value="JM">

Jamaica</option>

									<option value="JP">

Japan</option>

									<option value="JO">

Jordan</option>

									<option value="KZ">

Kazakhstan</option>

									<option value="KE">

Kenya</option>

									<option value="KW">

Kuwait</option>

									<option value="KG">

Kyrgyzstan</option>

									<option value="LA">

Laos</option>

									<option value="LV">

Latvia</option>

									<option value="LB">

Lebanon</option>

									<option value="LS">

Lesotho</option>

									<option value="LR">

Liberia</option>

									<option value="LY">

Libya</option>

									<option value="LI">

Liechtenstein</option>

									<option value="LT">

Lithuania</option>

									<option value="LU">

Luxembourg</option>

									<option value="MO">

Macau</option>

									<option value="MK">

Macedonia</option>

									<option value="MG">

Madagascar</option>

									<option value="MW">

Malawi</option>

									<option value="MY">

Malaysia</option>

									<option value="MV">

Maldives</option>

									<option value="ML">

Mali</option>

									<option value="MT">

Malta</option>

									<option value="MH">

Marshall Islands</option>

									<option value="MQ">

Martinique</option>

									<option value="MR">

Mauritania</option>

									<option value="MU">

Mauritius</option>

									<option value="MX">

Mexico</option>

									<option value="FM">

Micronesia</option>

									<option value="MD">

Moldova</option>

									<option value="MC">

Monaco</option>

									<option value="MN">

Mongolia</option>

									<option value="ME">

Montenegro</option>

									<option value="MS">

Montserrat</option>

									<option value="MA">

Morocco</option>

									<option value="MZ">

Mozambique</option>

									<option value="NA">

Namibia</option>

									<option value="NP">

Nepal</option>

									<option value="NL">

Netherlands</option>

									<option value="AN">

Netherlands Antilles</option>

									<option value="NC">

New Caledonia</option>

									<option value="NZ">

New Zealand</option>

									<option value="NI">

Nicaragua</option>

									<option value="NE">

Niger</option>

									<option value="NG">

Nigeria</option>

									<option value="NO">

Norway</option>

									<option value="OM">

Oman</option>

									<option value="PK">

Pakistan</option>

									<option value="PW">

Palau</option>

									<option value="PS">

Palestine Autonomous</option>

									<option value="PA">

Panama</option>

									<option value="PG">

Papua New Guinea</option>

									<option value="PY">

Paraguay</option>

									<option value="PE">

Peru</option>

									<option value="PH">

Philippines</option>

									<option value="PL">

Poland</option>

									<option value="PT">

Portugal</option>

									<option value="PR">

Puerto Rico</option>

									<option value="QA">

Qatar</option>

									<option value="RE">

Reunion</option>

									<option value="RO">

Romania</option>

									<option value="RU">

Russian Federation</option>

									<option value="RW">

Rwanda</option>

									<option value="MP">

Saipan</option>

									<option value="WS">

Samoa</option>

									<option value="SA">

Saudi Arabia</option>

									<option value="SN">

Senegal</option>

									<option value="RS">

Serbia</option>

									<option value="SC">

Seychelles</option>

									<option value="SG">

Singapore</option>

									<option value="SK">

Slovakia</option>

									<option value="SI">

Slovenia</option>

									<option value="ZA">

South Africa</option>

									<option value="KR">

South Korea</option>

									<option value="ES">

Spain</option>

									<option value="LK">

Sri Lanka</option>

									<option value="KN">

St. Kitts/Nevis</option>

									<option value="LC">

St. Lucia</option>

									<option value="VC">

St. Vincent</option>

									<option value="SR">

Suriname</option>

									<option value="SZ">

Swaziland</option>

									<option value="SE">

Sweden</option>

									<option value="CH">

Switzerland</option>

									<option value="SY">

Syria</option>

									<option value="TW">

Taiwan</option>

									<option value="TZ">

Tanzania</option>

									<option value="TH">

Thailand</option>

									<option value="TG">

Togo</option>

									<option value="TO">

Tonga</option>

									<option value="TT">

Trinidad/Tobago</option>

									<option value="TN">

Tunisia</option>

									<option value="TR">

Turkey</option>

									<option value="TM">

Turkmenistan</option>

									<option value="TC">

Turks &amp; Caicos Islands</option>

									<option value="UG">

Uganda</option>

									<option value="UA">

Ukraine</option>

									<option value="AE">

United Arab Emirates</option>

									<option value="GB">

United Kingdom</option>

									<option value="VI">

U.S. Virgin Islands</option>

									<option value="US">

U.S.A.</option>

																		<option value="UY">

Uruguay</option>

									<option value="UZ">

Uzbekistan</option>

									<option value="VU">

Vanuatu</option>

									<option value="VE">

Venezuela</option>

									<option value="VN">

Vietnam</option>

									<option value="WF">

Wallis &amp; Futuna</option>

									<option value="YE">

Yemen</option>

									<option value="ZM">

Zambia</option>

									<option value="ZW">

Zimbabwe</option>

															</select>

							</td>

						</tr>

												<tr id="trBillingDutiesAndTaxesTo">

								<td style="width: 34%;">

&nbsp;<span id="lblBillingDetailBillDutiesAndTaxesTo">

*Bill duties/taxes to</span>

</td>

								<td style="width: 1%;">

</td>

								<td style="width: 65%">

									<select name="ddlBillDutiesAndTaxesTo" id="ddlBillDutiesAndTaxesTo" class="dropdown" style='width: 180px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >

										<option value="-1">

Select</option>

					        			<option value="1">

Sender</option>

					        			<option value="2">

Recipient</option>

					        			<option value="3">

Third Party</option>

									</select>

								</td>

							</tr>

							<tr id="trBillingDutiesAndTaxesAccount">

								<td style="width: 34%;">

&nbsp;<span id="lblBillingDutiesAndTaxesAccountNumber">

*Duties/taxes account no.</span>

</td>

								<td style="width: 1%;">

</td>

								<td style="width: 65%">

<input name="txtBillingDutiesAndTaxesAccountNumber" type="text" maxlength="9" id="txtBillingDutiesAndTaxesAccountNumber" style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

								</td>

							</tr>

																																			<tr id="trBillingDutiesAndTaxesCountry">

								<td style="width: 34%;">

&nbsp;<span id="lblBillingDutiesAndTaxesCountryCode">

*Duties/taxes billing country</span>

</td>

								<td style="width: 1%;">

</td>

								<td style="width: 65%">

<select name="ddlBillingDutiesAndTaxesCountry" id="ddlBillingDutiesAndCountry" class="dropdown" style='width: 180px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >

									<option value="-1">

Select</option>

									<option value="AF">

Afghanistan</option>

									<option value="AL">

Albania</option>

									<option value="DZ">

Algeria</option>

									<option value="AS">

American Samoa</option>

									<option value="AD">

Andorra</option>

									<option value="AO">

Angola</option>

									<option value="AI">

Anguilla</option>

									<option value="AG">

Antigua/Barbuda</option>

									<option value="AR">

Argentina</option>

									<option value="AM">

Armenia</option>

									<option value="AW">

Aruba</option>

									<option value="AU">

Australia</option>

									<option value="AT">

Austria</option>

									<option value="AZ">

Azerbaijan</option>

									<option value="BS">

Bahamas</option>

									<option value="BH">

Bahrain</option>

									<option value="BD">

Bangladesh</option>

									<option value="BB">

Barbados</option>

									<option value="BY">

Belarus</option>

									<option value="BE">

Belgium</option>

									<option value="BZ">

Belize</option>

									<option value="BJ">

Benin</option>

									<option value="BM">

Bermuda</option>

									<option value="BT">

Bhutan</option>

									<option value="BO">

Bolivia</option>

									<option value="BA">

Bosnia-Herzegovina</option>

									<option value="BW">

Botswana</option>

									<option value="BR">

Brazil</option>

									<option value="VG">

British Virgin Islands</option>

									<option value="BN">

Brunei</option>

									<option value="BG">

Bulgaria</option>

									<option value="BF">

Burkina Faso</option>

									<option value="BI">

Burundi</option>

									<option value="KH">

Cambodia</option>

									<option value="CM">

Cameroon</option>

									<option value="CA">

Canada</option>

									<option value="CV">

Cape Verde</option>

									<option value="KY">

Cayman Islands</option>

									<option value="TD">

Chad</option>

									<option value="CL">

Chile</option>

									<option value="CN">

China</option>

									<option value="CO">

Colombia</option>

									<option value="CG">

Congo Brazzaville</option>

									<option value="CD">

Congo Democratic Rep. of</option>

									<option value="CK">

Cook Islands</option>

									<option value="CR">

Costa Rica</option>

									<option value="HR">

Croatia</option>

									<option value="CY">

Cyprus</option>

									<option value="CZ">

Czech Republic</option>

									<option value="DK">

Denmark</option>

									<option value="DJ">

Djibouti</option>

									<option value="DM">

Dominica</option>

									<option value="DO">

Dominican Republic</option>

									<option value="TL">

East Timor</option>

									<option value="EC">

Ecuador</option>

									<option value="EG">

Egypt</option>

									<option value="SV">

El Salvador</option>

									<option value="GQ">

Equatorial Guinea</option>

									<option value="ER">

Eritrea</option>

									<option value="EE">

Estonia</option>

									<option value="ET">

Ethiopia</option>

									<option value="FO">

Faeroe Islands</option>

									<option value="FJ">

Fiji</option>

									<option value="FI">

Finland</option>

									<option value="FR">

France</option>

									<option value="GF">

French Guiana</option>

									<option value="PF">

French Polynesia</option>

									<option value="GA">

Gabon</option>

									<option value="GM">

Gambia</option>

									<option value="GE">

Georgia</option>

									<option value="DE">

Germany</option>

									<option value="GH">

Ghana</option>

									<option value="GI">

Gibraltar</option>

									<option value="GR">

Greece</option>

									<option value="GL">

Greenland</option>

									<option value="GD">

Grenada</option>

									<option value="GP">

Guadeloupe</option>

									<option value="GU">

Guam</option>

									<option value="GT">

Guatemala</option>

									<option value="GN">

Guinea</option>

									<option value="GY">

Guyana</option>

									<option value="HT">

Haiti</option>

									<option value="HN">

Honduras</option>

									<option value="HK">

Hong Kong</option>

									<option value="HU">

Hungary</option>

									<option value="IS">

Iceland</option>

									<option value="IN">

India</option>

									<option value="ID">

Indonesia</option>

									<option value="IQ">

Iraq</option>

									<option value="IE">

Ireland</option>

									<option value="IL">

Israel</option>

									<option value="IT">

Italy</option>

									<option value="CI">

Ivory Coast</option>

									<option value="JM">

Jamaica</option>

									<option value="JP">

Japan</option>

									<option value="JO">

Jordan</option>

									<option value="KZ">

Kazakhstan</option>

									<option value="KE">

Kenya</option>

									<option value="KW">

Kuwait</option>

									<option value="KG">

Kyrgyzstan</option>

									<option value="LA">

Laos</option>

									<option value="LV">

Latvia</option>

									<option value="LB">

Lebanon</option>

									<option value="LS">

Lesotho</option>

									<option value="LR">

Liberia</option>

									<option value="LY">

Libya</option>

									<option value="LI">

Liechtenstein</option>

									<option value="LT">

Lithuania</option>

									<option value="LU">

Luxembourg</option>

									<option value="MO">

Macau</option>

									<option value="MK">

Macedonia</option>

									<option value="MG">

Madagascar</option>

									<option value="MW">

Malawi</option>

									<option value="MY">

Malaysia</option>

									<option value="MV">

Maldives</option>

									<option value="ML">

Mali</option>

									<option value="MT">

Malta</option>

									<option value="MH">

Marshall Islands</option>

									<option value="MQ">

Martinique</option>

									<option value="MR">

Mauritania</option>

									<option value="MU">

Mauritius</option>

									<option value="MX">

Mexico</option>

									<option value="FM">

Micronesia</option>

									<option value="MD">

Moldova</option>

									<option value="MC">

Monaco</option>

									<option value="MN">

Mongolia</option>

									<option value="ME">

Montenegro</option>

									<option value="MS">

Montserrat</option>

									<option value="MA">

Morocco</option>

									<option value="MZ">

Mozambique</option>

									<option value="NA">

Namibia</option>

									<option value="NP">

Nepal</option>

									<option value="NL">

Netherlands</option>

									<option value="AN">

Netherlands Antilles</option>

									<option value="NC">

New Caledonia</option>

									<option value="NZ">

New Zealand</option>

									<option value="NI">

Nicaragua</option>

									<option value="NE">

Niger</option>

									<option value="NG">

Nigeria</option>

									<option value="NO">

Norway</option>

									<option value="OM">

Oman</option>

									<option value="PK">

Pakistan</option>

									<option value="PW">

Palau</option>

									<option value="PS">

Palestine Autonomous</option>

									<option value="PA">

Panama</option>

									<option value="PG">

Papua New Guinea</option>

									<option value="PY">

Paraguay</option>

									<option value="PE">

Peru</option>

									<option value="PH">

Philippines</option>

									<option value="PL">

Poland</option>

									<option value="PT">

Portugal</option>

									<option value="PR">

Puerto Rico</option>

									<option value="QA">

Qatar</option>

									<option value="RE">

Reunion</option>

									<option value="RO">

Romania</option>

									<option value="RU">

Russian Federation</option>

									<option value="RW">

Rwanda</option>

									<option value="MP">

Saipan</option>

									<option value="WS">

Samoa</option>

									<option value="SA">

Saudi Arabia</option>

									<option value="SN">

Senegal</option>

									<option value="RS">

Serbia</option>

									<option value="SC">

Seychelles</option>

									<option value="SG">

Singapore</option>

									<option value="SK">

Slovakia</option>

									<option value="SI">

Slovenia</option>

									<option value="ZA">

South Africa</option>

									<option value="KR">

South Korea</option>

									<option value="ES">

Spain</option>

									<option value="LK">

Sri Lanka</option>

									<option value="KN">

St. Kitts/Nevis</option>

									<option value="LC">

St. Lucia</option>

									<option value="VC">

St. Vincent</option>

									<option value="SR">

Suriname</option>

									<option value="SZ">

Swaziland</option>

									<option value="SE">

Sweden</option>

									<option value="CH">

Switzerland</option>

									<option value="SY">

Syria</option>

									<option value="TW">

Taiwan</option>

									<option value="TZ">

Tanzania</option>

									<option value="TH">

Thailand</option>

									<option value="TG">

Togo</option>

									<option value="TO">

Tonga</option>

									<option value="TT">

Trinidad/Tobago</option>

									<option value="TN">

Tunisia</option>

									<option value="TR">

Turkey</option>

									<option value="TM">

Turkmenistan</option>

									<option value="TC">

Turks &amp; Caicos Islands</option>

									<option value="UG">

Uganda</option>

									<option value="UA">

Ukraine</option>

									<option value="AE">

United Arab Emirates</option>

									<option value="GB">

United Kingdom</option>

									<option value="VI">

U.S. Virgin Islands</option>

									<option value="US">

U.S.A.</option>

																		<option value="UY">

Uruguay</option>

									<option value="UZ">

Uzbekistan</option>

									<option value="VU">

Vanuatu</option>

									<option value="VE">

Venezuela</option>

									<option value="VN">

Vietnam</option>

									<option value="WF">

Wallis &amp; Futuna</option>

									<option value="YE">

Yemen</option>

									<option value="ZM">

Zambia</option>

									<option value="ZW">

Zimbabwe</option>

															</select>

							</td>

						</tr>

						<!-- end tblBillingDetails -->

						                    </table>

                                                                 </td>

	                                                                  											</tr>

										<!-- begin tblReferences -->

					<tr>

						<td>

                           							<table id="tblReferences" border="0" cellpadding="3" cellspacing="0" width="100%" 								style='border: #660099 1px solid; font-family:Arial; text-align: left; color:Black; background-color:Whitesmoke; font-size:11px;'>

							<tr>

								<td colspan="3"										style="font-weight: bold; color: white; background-color: #660099; height: 20px;">

										&nbsp;References</td>

							</tr>

							<!-- begin referenceYourReference -->

							<tr>

								<td style="width: 34%;">

&nbsp;<span id="lblReferenceYourReference">

Your reference</span>

</td>

								<td style="width: 1%;">

</td>

								<td style="width: 65%">

<input name="txtReferenceYourReference" type="text" maxlength="30" id="txtReferenceYourReference" 									style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

								</td>

							</tr>

								<!-- end referenceYourReference -->

							<!-- begin referenceDeptNbr -->

							<tr>

								<td style="width: 34%;">

&nbsp;<span id="lblReferenceDepartmentNo">

Department no.</span>

</td>

								<td style="width: 1%;">

</td>

								<td style="width: 65%">

<input name="txtReferenceDepartmentNo" type="text" maxlength="30" id="txtReferenceDepartmentNo" 									style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

								</td>

							</tr>

								<!-- end referenceDeptNbr -->

								<!-- begin referenceInvoiceNbr -->

							<tr>

								<td style="width: 34%;">

&nbsp;<span id="lblReferenceInvoiceNo">

Invoice no.</span>

</td>

								<td style="width: 1%;">

</td>

								<td style="width: 65%">

<input name="txtReferenceInvoiceNo" type="text" maxlength="30" id="txtReferenceInvoiceNo" 									style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

								</td>

							</tr>

								<!-- end referenceInvoiceNbr -->

								<!-- begin referencePONbr -->

							<tr>

								<td style="width: 34%;">

&nbsp;<span id="lblReferencePoNo">

P.O. no.</span>

</td>

								<td style="width: 1%;">

</td>

								<td style="width: 65%">

<input name="txtReferencePoNo" type="text" maxlength="30" id="txtReferencePoNo" 									style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

								</td>

							</tr>

								<!-- end referencePONbr -->

	                    </table>

                    </td>

					</tr>

					<!-- end tblReferences -->

									</table>

			</td>

			<!-- end tdMain -->

						<td valign="top" align="left">

							<table>

									<tr>

										<!--Package and Shipment Details-->

	        <td >

	        					<table id="tblShippingPackageDetails" border="0" cellpadding="3" cellspacing="0" width="100%" style='border: #660099 1px solid; font-family:Arial; text-align: left; color:Black; background-color:Whitesmoke; font-size:11px;'>

				<tr>

					<td colspan="3"						style="font-weight: bold; color: white; background-color: #660099; height: 20px;">

					&nbsp;Package and Shipment Details</td>

				</tr>

				<tr>

					<td style="width: 49%;">

&nbsp;<span id="lblPackageShippingDetailServiceType">

*Service type</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

<select name="ddlPackageShippingDetailServiceType" class="dropdown" id="ddlPackageShippingDetailServiceType" onChange="SetSpecialServices();ShowHideGenerateCustoms();ShowHideGoodsNotInFreeCirculation();ShowHideSenderTaxId();ShowHideRecipientTaxId();" style='width:160px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >

							<option value="-1">

Select</option>

						  <option value='13'>

FedEx Priority Overnight&#174</option>

						</select>

											</td>

				</tr>

				<tr>

					<td style="width: 49%;">

&nbsp;<span id="lblPackageShippingDetailPackageType">

*Package type</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

<select name="ddlPackageShippingDetailPackageType" class="dropdown" id="ddlPackageShippingDetailPackageType" onChange="EnableDisablePackages();" style='width: 160px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >

							<option value="-1">

Select</option>

							<option value='3'>

FedEx&#174 Envelope</option>

<option value='6'>

Your Packaging</option>

						</select>

 											</td>

				</tr>

								 <!-- end trDropoffType -->

								<!-- begin trPackagesCount -->

				<tr>

					<td style="width: 49%;">

&nbsp;<span id="lblPackageShippingDetailNumberOfPackages">

*No. of packages</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

<select name="ddlPackageShippingDetailNumberOfPackages" class="dropdown" id="ddlPackageShippingDetailNumberOfPackages" onchange="EnableDisablePackages();" style='width: 160px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >

							<option value="-1">

Select</option>

							<option value="1">

1</option>

																				<option value="2">

2</option>

							<option value="3">

3</option>

							<option value="4">

4</option>

							<option value="5">

5</option>

						</select>

 					</td>

				</tr>

				<!-- end trPackagesCount -->

								<tr id="trPackage1" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackage1">

*Package 1 dimensions</span>

<span id="lblPackage1NoAst">

Package 1 dimensions</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

<select name="ddlPackage1" id="ddlPackage1" class="dropdown" style="width:160px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;" >

							<option value="-1">

Select</option>

													</select>

 					</td>

				</tr>

								<tr id="trPackageDimensions1" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackageDimensions1">

*Package 1 dimensions</span>

<span id="lblPackageDimensions1NoAst">

Package 1 dimensions</span>

</td>

					<td style="width: 1%">

</td>

					<td style="width: 50%">

<input name="txtDimensionsLength1" type="text" maxlength="5" id="txtDimensionsLength1" value="L" onclick="CheckDimensions(this)" style='width: 30px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

					<input name="txtDimensionsWidth1" type="text" maxlength="5" id="txtDimensionsWidth1" value="W" onclick="CheckDimensions(this)" style='width: 30px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

					<input name="txtDimensionsHeight1" type="text" maxlength="5" id="txtDimensionsHeight1" value="H" onclick="CheckDimensions(this)" style='width: 30px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

					<select name="ddlDimensionsUnits1" id="ddlDimensionsUnits1" class="dropdown" style="width:49px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;" >

							<option value="-1">

Select</option>

							<option value="CM">

cm</option>

                            <option value="IN">

in</option>

						</select>

 					</td>

				</tr>

								<tr id="trWeight1" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackage1">

*Package 1 weight</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

						<input name="txtWeight1" type="text" maxlength="5" 						id="txtWeight1" 						style='width: 160px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

					</td>

				</tr>

								<tr id="trPackage2" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackage2">

*Package 2 dimensions</span>

<span id="lblPackage2NoAst">

Package 2 dimensions</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

<select name="ddlPackage2" id="ddlPackage2" class="dropdown" style="width:160px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;" >

							<option value="-1">

Select</option>

													</select>

 					</td>

				</tr>

								<tr id="trPackageDimensions2" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackageDimensions2">

*Package 2 dimensions</span>

<span id="lblPackageDimensions2NoAst">

Package 2 dimensions</span>

</td>

					<td style="width: 1%">

</td>

					<td style="width: 50%">

<input name="txtDimensionsLength2" type="text" maxlength="5" id="txtDimensionsLength2" value="L" onclick="CheckDimensions(this)" style='width: 30px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

						<input name="txtDimensionsWidth2" type="text" maxlength="5"  id="txtDimensionsWidth2" value="W" onclick="CheckDimensions(this)"  style="width:30px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

						<input name="txtDimensionsHeight2" type="text" maxlength="5"  id="txtDimensionsHeight2" value="H" onclick="CheckDimensions(this)" style="width:30px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

										</td>

				</tr>

								<tr id="trWeight2" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackage2">

*Package 2 weight</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

						<input name="txtWeight2" type="text" maxlength="5" 						id="txtWeight2" 						style="width:160px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

					</td>

				</tr>

								<tr id="trPackage3" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackage3">

*Package 3 dimensions</span>

<span id="lblPackage3NoAst">

Package 3 dimensions</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

<select name="ddlPackage3" id="ddlPackage3" class="dropdown" style="width:160px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;" >

							<option value="-1">

Select</option>

													</select>

 					</td>

				</tr>

								<tr id="trPackageDimensions3" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackageDimensions3">

*Package 3 dimensions</span>

<span id="lblPackageDimensions3NoAst">

Package 3 dimensions</span>

</td>

					<td style="width: 1%">

</td>

					<td style="width: 50%">

<input name="txtDimensionsLength3" type="text" maxlength="5" value="L" id="txtDimensionsLength3" onclick="CheckDimensions(this)" style="width:30px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

						<input name="txtDimensionsWidth3" type="text" maxlength="5"  id="txtDimensionsWidth3" value="W" onclick="CheckDimensions(this)" style="width:30px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

						<input name="txtDimensionsHeight3" type="text" maxlength="5"  id="txtDimensionsHeight3" value="H" onclick="CheckDimensions(this)" style="width:30px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

										</td>

				</tr>

								<tr id="trWeight3" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackage3">

*Package 3 weight</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

						<input name="txtWeight3" type="text" maxlength="5" 						id="txtWeight3" 						style="width:160px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

					</td>

				</tr>

								<tr id="trPackage4" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackage4">

*Package 4 dimensions</span>

<span id="lblPackage4NoAst">

Package 4 dimensions</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

<select name="ddlPackage4" id="ddlPackage4" class="dropdown" style="width:160px;background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;" >

							<option value="-1">

Select</option>

													</select>

 					</td>

				</tr>

								<tr id="trPackageDimensions4" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackageDimensions4">

*Package 4 dimensions</span>

<span id="lblPackageDimensions4NoAst">

Package 4 dimensions</span>

</td>

					<td style="width: 1%">

</td>

					<td style="width: 50%">

<input name="txtDimensionsLength4" type="text" maxlength="5" value="L" id="txtDimensionsLength4" onclick="CheckDimensions(this)" style="width:30px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

						<input name="txtDimensionsWidth4" type="text" maxlength="5"  id="txtDimensionsWidth4" value="W" onclick="CheckDimensions(this)" style="width:30px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

						<input name="txtDimensionsHeight4" type="text" maxlength="5"  id="txtDimensionsHeight4" value="H" onclick="CheckDimensions(this)" style="width:30px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

										</td>

				</tr>

								<tr id="trWeight4" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackage4">

*Package 4 weight</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

						<input name="txtWeight4" type="text" maxlength="5" 						id="txtWeight4" 						style="width:160px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

					</td>

				</tr>

								<tr id="trPackage5" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackage5">

*Package 5 dimensions</span>

<span id="lblPackage5NoAst">

Package 5 dimensions</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

<select name="ddlPackage5" id="ddlPackage5" class="dropdown" style="width:160px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;" >

							<option value="-1">

Select</option>

													</select>

 					</td>

				</tr>

								<tr id="trPackageDimensions5" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackageDimensions5">

*Package 5 dimensions</span>

<span id="lblPackageDimensions5NoAst">

Package 5 dimensions</span>

</td>

					<td style="width: 1%">

</td>

					<td style="width: 50%">

<input name="txtDimensionsLength5" type="text" maxlength="5" value="L" id="txtDimensionsLength5" onclick="CheckDimensions(this)" style="width:30px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

						<input name="txtDimensionsWidth5" type="text" maxlength="5"  id="txtDimensionsWidth5" value="W" onclick="CheckDimensions(this)" style="width:30px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

						<input name="txtDimensionsHeight5" type="text" maxlength="5"  id="txtDimensionsHeight5" value="H" onclick="CheckDimensions(this)" style="width:30px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

										</td>

				</tr>

								<tr id="trWeight5" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackage5">

*Package 5 weight</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

						<input name="txtWeight5" type="text" maxlength="5" 						id="txtWeight5" 						style="width:160px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

					</td>

				</tr>

								<tr id="trPackageDimensions" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackageDimensions">

*Package dimensions</span>

</td>

					<td style="width: 1%">

</td>

					<td style="width: 50%">

<input name="txtDimensionsLength" type="text" maxlength="5" value="L" id="txtDimensionsLength" onclick="CheckDimensions(this)" style="width:30px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

					<input name="txtDimensionsWidth" type="text" maxlength="5" id="txtDimensionsWidth" value="W" onclick="CheckDimensions(this)"  style="width:30px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

					<input name="txtDimensionsHeight" type="text" maxlength="5" id="txtDimensionsHeight" value="H" onclick="CheckDimensions(this)" style="width:30px;border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;" />

					<select name="ddlPackageUnits" id="ddlPackageUnits" class="dropdown" style="width:49px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;" >

							<option value="-1">

Select</option>

							<option value="CM">

cm</option>

                            <option value="IN">

in</option>

						</select>

 					</td>

				</tr>

												<!-- begin trPackagesWeight -->

				<tr id="trWeight">

					<td style="width: 49%;">

&nbsp;<span id="lblPackageShippingDetailEstimatedWeight">

*Weight</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

<input name="txtPackageShippingDetailEstimatedWeight" type="text" maxlength="5" id="txtPackageShippingDetailEstimatedWeight" style='width: 160px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

					</td>

				</tr>

				<!-- end trPackagesWeight -->

				<!-- begin trPackagesWeightUnit -->

								<tr id="trEstimatedWeightUnit">

					<td style="width: 49%;">

&nbsp;<span id="lblPackageShippingDetailEstimatedWeightUnit">

*Weight unit of measure</span>

</td>

					<td style="width: 1%">

</td>

					<td style="width: 50%">

<select name="ddlWeightUnit" class="dropdown" id="ddlWeightUnit" style='width:160px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >

							<option value="-1">

Select</option>

							<option value="1">

lbs</option>

							<option value="2">

kg</option>

						</select>

 					</td>

				</tr>

				<!-- end trPackagesWeightUnit -->

								 <!-- end trHubId -->

				<tr id="trDeclaredValue1" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackageShippingDetailDeclaredValue1">

Declared value 1</span>

					<img src="https://www.fedex.com/FWIW/Client/Images/Common/appModuleHelpIcon.gif" title="The value of any package represents FedEx's maximum liability in connection with a shipment of that package.  A declared value greater than $100 will incur additional charges.  If you intend to purchase insurance from a third party, please enter a value of 0."/>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

<input name="txtDeclaredValue1" type="text" maxlength="10" id="txtDeclaredValue1" style='width: 40px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

					<input name="txtCurrency" id="txtCurrency" style="width:105px; font-family:Arial; font-size:11px; color:Black; background-color:Whitesmoke; border:0px;"  readonly type="text"  value="United States Dollars" />

</td>

				</tr>

								<tr id="trDeclaredValue2" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackageShippingDetailDeclaredValue2">

Declared value 2</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

<input name="txtDeclaredValue2" type="text" maxlength="10" id="txtDeclaredValue2" style='width: 40px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

					<input name="txtCurrency2" id="txtCurrency2" style="width:105px; font-family:Arial; font-size:11px; color:Black; background-color:Whitesmoke; border:0px;"  readonly type="text"  value="United States Dollars" />

</td>

				</tr>

								<tr id="trDeclaredValue3" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackageShippingDetailDeclaredValue3">

Declared value 3</span>

</td>

						<td style="width: 1%;">

</td>

					<td style="width: 50%">

<input name="txtDeclaredValue3" type="text" maxlength="10" id="txtDeclaredValue3" style='width: 40px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

					<input name="txtCurrency3" id="txtCurrency3" style="width:105px; font-family:Arial; font-size:11px; color:Black; background-color:Whitesmoke; border:0px;"  readonly type="text"  value="United States Dollars" />

</td>

				</tr>

								<tr id="trDeclaredValue4" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackageShippingDetailDeclaredValue4">

Declared value 4</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

<input name="txtDeclaredValue4" type="text" maxlength="10" id="txtDeclaredValue4" style='width: 40px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

					<input name="txtCurrency4" id="txtCurrency4" style="width:105px; font-family:Arial; font-size:11px; color:Black; background-color:Whitesmoke; border:0px;"  readonly type="text"  value="United States Dollars" />

</td>

				</tr>

								<tr id="trDeclaredValue5" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackageShippingDetailDeclaredValue5">

Declared value 5</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

<input name="txtDeclaredValue5" type="text" maxlength="10" id="txtDeclaredValue5" style='width: 40px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

					<input name="txtCurrency5" id="txtCurrency5" style="width:105px; font-family:Arial; font-size:11px; color:Black; background-color:Whitesmoke; border:0px;"  readonly type="text"  value="United States Dollars" />

</td>

				</tr>

								<!-- begin trCurrencyType -->

			   <tr id="trCurrencyType">

					<td style="width: 49%;">

&nbsp;<span id="lblCurrencyType">

*Currency type</span>

</td>

					<td style="width: 1%">

</td>

					<td style="width: 50%">

<select name="ddlCurrencyType" id="ddlCurrencyType" class="dropdown"  					style='width:160px; border-color:Black; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' onChange="ChangeCurrencyType(this.value)" >

							<option value="-1">

Select</option>

							 <option value="ARN">

Argentina Pesos</option>

												<option value="AWG">

Aruba Guilders</option>

												<option value="AUD">

Australia Dollars</option>

												<option value="BSD">

Bahamas Dollars</option>

												<option value="BHD">

Bahrain Dinars</option>

												<option value="BBD">

Barbados Dollars</option>

												<option value="BMD">

Bermuda Dollars</option>

												<option value="BRL">

Brazil Reais</option>

												<option value="BND">

Brunei Dollars</option>

												<option value="CAD">

Canada Dollars</option>

												<option value="KYD">

Cayman Islands Dollars</option>

												<option value="CLP">

Chile Pesos</option>

												<option value="CNY">

China Renminbi</option>

												<option value="COP">

Colombia Pesos</option>

												<option value="CRC">

Costa Rica Colones</option>

												<option value="CZK">

Czech Republic Koruny</option>

												<option value="DKK">

Denmark Kroner</option>

												<option value="DOP">

Dominican Republic Pesos</option>

												<option value="XCD">

E. Caribbean Dollars</option>

												<option value="EGP">

Egypt Pounds</option>

												<option value="EUR">

Euro</option>

												<option value="GTQ">

Guatemala Quetzales</option>

												<option value="HKD">

Hong Kong Dollars</option>

												<option value="HUF">

Hungary Forint</option>

												<option value="INR">

India Rupees</option>

												<option value="XXX">

Israel Shekels</option>

												<option value="JMD">

Jamaica Dollars</option>

												<option value="JYE">

Japan Yen</option>

												<option value="XXX">

Kazakhstan Tenge</option>

												<option value="XXX">

Kenya Shillings</option>

												<option value="KWD">

Kuwait Dinars</option>

												<option value="LVL">

Latvia Lati</option>

												<option value="LTL">

Lithuania Litai</option>

												<option value="MOP">

Macau Patacas</option>

												<option value="MYR">

Malaysia Ringgits</option>

												<option value="NMP">

Mexico New Pesos</option>

												<option value="ANG">

Netherlands Antilles Guilders</option>

												<option value="NZD">

New Zealand Dollars</option>

												<option value="NOK">

Norway Kroner</option>

												<option value="PKR">

Pakistan Rupees</option>

												<option value="XXX">

Panama Balboas</option>

												<option value="PHP">

Philippines Pesos</option>

												<option value="PLN">

Poland Zloty</option>

												<option value="XXX">

Russia Rouble</option>

												<option value="SAR">

Saudi Arabia Riyals</option>

												<option value="SGD">

Singapore Dollars</option>

												<option value="XXX">

Slovakia Koruny</option>

												<option value="ZAR">

South Africa Rand</option>

												<option value="KRW">

South Korea Won</option>

												<option value="SEK">

Sweden Kronor</option>

												<option value="CHF">

Switzerland Francs</option>

												<option value="TWD">

Taiwan New Dollars</option>

												<option value="THB">

Thailand Baht</option>

												<option value="TTD">

Trinidad &amp; Tobago Dollars</option>

												<option value="TRY">

Turkey New Lira</option>

												<option value="AED">

UAE Dirhams</option>

												<option value="XXX">

Uganda Shillings</option>

												<option value="GBP">

United Kingdom Pounds</option>

												<option value="USD">

United States Dollars</option>

												<option value="UYU">

Uruguay New Pesos</option>

												<option value="VEB">

Venezuela Bolivares Fuertes</option>

												<option value="XXX">

Western Samoa Tala</option>

						</select>

					</td>

				</tr>

				<!-- end trCurrencyType -->

							<tr id="trGoodsNotInFreeCirculation" >

					<td style="width: 100%;" nowrap="nowrap">

<input  id="chkFreeCirculation" type="checkbox"  name="chkGoodNotInFreeCirculation"/>

&nbsp; Goods not in free circulation <img src="https://www.fedex.com/FWIW/Client/Images/Common/appModuleHelpIcon.gif" title="Goods are in free circulation within the European Union when they have complied with all import formalities and all import charges have been paid.  If the goods are not in free circulation, click this check box."/>

</td>

				</tr>

									<!-- begin trShipmentDate -->

				<tr id="trShipmentDate">

				<!--ShipmentDate-->

					<td style="width: 49%;">

&nbsp;<span id="lblShipmentDate">

*Ship date</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

<select name="ddlShipmentDate" id="ddlShipmentDate" class="dropdown" style='width:160px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >

						<option value="-1">

Select</option>

                		<option value="0">

Today</option>

                           <option value="1">

Tomorrow</option>

                        <option value="2">

in 2 Days</option>

                        <option value="3">

in 3 Days</option>

                        <option value="4">

in 4 Days</option>

                        <option value="5">

in 5 Days</option>

                        <option value="6">

in 6 Days</option>

                        <option value="7">

in 7 Days</option>

                            <option value="8">

in 8 Days</option>

                        <option value="9">

in 9 Days</option>

                        <option value="10">

in 10 Days</option>

     						</select>

											</td>

				</tr>

				<!-- end trShipmentDate -->

					<tr id="trCustomsDoc" >

					<td style="width: 100%;" nowrap="nowrap">

 						<input  id="chkCustomsDoc" type="checkbox"  						name="chkCustomsDocGeneration" onclick="ShowHideGenerateCustoms();SetSubmitButtonLabel();"/>

&nbspGenerate FedEx customs documentation for this shipment</td>

				</tr>

							<!-- end trCustomsDoc -->

				<!-- begin trCustomsValue -->

				</tr>

<tr id="trCustomsValue" style="display: none">

					<td style="width: 49%;">

&nbsp;<span id="lblPackageShippingDetailCustomsValue">

*Customs value</span>

</td>

					<td style="width: 1%;">

</td>

					<td style="width: 50%">

<input name="txtCustomsValue" type="text" maxlength="15" id="txtCustomsValue" style='width: 40px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

					<input name="txtCustomsCurrency" id="txtCustomsCurrency" style="width:105px; font-family:Arial; font-size:11px; color:Black; background-color:Whitesmoke; border:0px;"  readonly type="text"  value="United States Dollars"  />

</td>

				</tr>

					<!-- end trCustomsValue -->

				            </table>

                        </td>

			</tr>

											<tr>

				<td>

				<!--Special Services Table -->

    	        	<!-- begin tblSpecialServices -->

   													<table id="tblSpecialServices" border="0" cellpadding="3" cellspacing="0" width="100%" style='border: #660099 1px solid; font-family:Arial; text-align: left; color:Black; background-color:Whitesmoke; font-size:11px;'>

													<tr>

														<td colspan="3"															style="font-weight: bold; color: white; background-color: #660099; height: 20px;">

														&nbsp;Special Services </td>

																											</tr>

																										<tr id="trSignatureOption" >

														<td colspan="3" style="width: 100%;">

 														<input  id="chkSignatureOptions" type="checkbox"                                                                                                                        name="chkSignatureOptions" onclick="CheckSignatureOptions();" />

                                                            &nbsp;Signature Options</td>

													</tr>

													<tr id='trSignatureOptionType' style='display: none' >

														<td style="width: 49%;">

&nbsp;&nbsp;*Signature option type</td>

														<td style="width: 1%">

</td>

														<td style="width: 50%">

<select name="ddlSignatureOptionType" class="dropdown" id="ddlSignatureOptionType" style='width:160px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >

																<option value="-1">

Select</option>

																<option value="1">

Adult</option>

																<option value="2">

Direct</option>

																<option value="3">

Indirect</option>

																<option value="4">

No Signature required</option>

																<option value="5">

Service default</option>

															</select>

 														</td>

													</tr>

																										<tr id="trHomeDeliveryPremium" >

														<td colspan="3" style="width: 100%;">

 														<input  id="chkHomeDeliveryPremium" type="checkbox"                                                                                                                        name="chkHomeDeliveryPremium" onclick="CheckHomeDeliveryPremium();" />

&nbsp;Home Delivery Premium</td>

													</tr>

																										<tr id='trHomeDeliveryPremiumType' style='display: none' >

														<td style="width: 49%;">

&nbsp;&nbsp;*Home delivery premium type</td>

														<td style="width: 1%">

</td>

														<td style="width: 50%">

<select name="ddlHomeDeliveryPremiumType" class="dropdown" id="ddlHomeDeliveryPremiumType" style='width:160px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >

																<option value="-1">

Select</option>

																<option value="1">

Appointment</option>

																<option value="2">

Date Certain</option>

																<option value="3">

Evening</option>

															</select>

 														</td>

													</tr>

																										<tr id='trHomeDeliveryDate' style='display: none'>

														<td style="width: 49%;">

&nbsp;&nbsp;*Delivery date (MM/dd/yyyy)</td>

														<td style="width: 1%">

</td>

														<td style="width: 50%">

<input name="txtHomeDeliveryDate" type="text" maxlength="10" id="txtHomeDeliveryDate" style='width: 140px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

														</td>

													</tr>

																										<tr id='trHomeDeliveryPhone' style='display: none'>

														<td style="width: 49%;">

&nbsp;&nbsp;*Delivery phone no.</td>

														<td style="width: 1%">

</td>

														<td style="width: 50%">

<input name="txtHomeDeliveryPhone" type="text" maxlength="35" id="txtHomeDeliveryPhone" style='width: 140px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

														</td>

													</tr>

																										<tr id="trFutureDayShipment" >

														<td colspan="3" style="width: 100%;">

 														<input  id="chkFutureDayShipment" type="checkbox"                                                                                                                        name="chkFutureDayShipment" />

&nbsp;Future day shipment</td>

													</tr>

																										<!-- begin trDryIce -->

													<tr id="trDryIce" >

														<td colspan="3" style="width: 100%;">

 														<input  id="chkDryIce" type="checkbox"                                                                                                                        name="chkDryIce" onclick="CheckDryIce();" />

&nbsp;Dry Ice</td>

													</tr>

																																							<tr id='trDryIce2' style='display: none'>

														<td style="width: 49%;">

&nbsp;&nbsp;*Total dry ice weight</td>

														<td style="width: 1%">

</td>

														<td style="width: 50%">

<input name="txtDryIceWeight" type="text" maxlength="35" id="txtDryIceWeight" style='width: 140px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />

														kg</td>

													</tr>

																										<!-- end trDryIce -->

													 <!-- end trDangerousGoods -->

																										 <!-- end trORMD -->

																										 <!-- end trSQE -->

																										 <!-- end trLithiumBattery -->

																								 <!-- end trHazMat -->

													                                                  </table>

                                                 <!-- end tblSpecialServices -->

                                                                                       <!-- end tblSmartPostSpecialServices -->

												</td>

					</tr>

										<tr>

										<!--E-mail notifications-->

		 <!-- end tblShipmentNotifications -->

     						</tr>

									</table>

						</td>

				</tr>

     	<tr id="trSmartPostNote">

		<td colspan="2">

&nbsp;<b>

Please note:</b>

</br>

&nbsp;If your recipient's address is an APO, FPO, or DPO location, 				please ensure that Form 2976A is included with this shipment. 				More </br>

&nbsp;information on the necessary documentation for shipping to military addresses can be obtained 				from the US Postal Service website.</td>

													</tr>

	<tr>

		<td>

&nbsp;</td>

		<td style="align: right;">

			<input type="submit" name="btnGenerateLabel" value="Continue" id="btnGenerateLabel" class="Buttons" style="border-color:Black; color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; height: 25px;" />

  		</td>

	</tr>

										</table>

									</td>

	</tr>

	</table>

<input id="query" name="query" type="hidden" value="fYDgcRJsl8x8vYDflQ4LjGUk48R6eONeh4mYLcStZgOqqF/36LL1vJtZdmfM3WM0dfpff8dmqwnUlw8anYe0phYV5vkSeqkMzSnIiVCUsvC0YA4o4DD17WY0iRhroweouGtlBi5j3F7lLVXK9qPdAyX/6UwjJmvlZJ47ur+eXgs1pLeOYICLpoWh/0NhIo13R6fWGLB/IHhB1nKaM5g48bg2ORj/1b5nGL1NXJynaPr5kpDdCaQW3jDkQieuXtuw2h8Mo2Oj6O9G0U6e4vq853rNnBKlgFSOnZ7XH0rD+78VUK4f/F14Jij2tIiZWS08DTntB1ZeUzf8sn1t0TMLIfsKt56+mFXWflMfaR7ME9US2CC93ZjiBBWphHEO2MnOypt7n45qZY6w3ZMazSzDuniWqQY4Gc9GWhHWscFPzvog6yq0Ab5n1QV0t+PDjH+6U7mDy5JBn1Ztz84W0rI7/9qoOJmrx7gWdXMY+NMeN4C/ln0EOdxPfmsfD9CAOz0n+hTezMeD7zZRNaCuGXlqf1G5FnmXnNJzCowMyXR1jPkwLijehCnPioL68oJBhcbUudol0q6T1/wY7IZ2ecq+MnEQcbA4lXY17XiAb7CeHoiWzvOHh4qsnHicnk9LOQDpC19Itcdo9UfdP1t4zUPVB2fcw48zqVoUWoQzqGz6Vwg0eRbPaiZBndyEiEE1qmbDTdfb9sXxVUpAQFrqtTJrUxt81rCn1ZhPfqM0pTQUEKEN9LZoPGCP1Ic8Cn6QUo7YQI8kHAWQonMhTSGzuGNmHMtKihHCZSJe2N+1KajWKPRMd1ZBuwyLPcyuaG6RRvNHYl2YEiPrAMuaz/h9FbZ67pXH8ik8Jd/bi5FtxgKVgVR5p1ON4SIIobNih9WUeJjX0AZSn4tPbx+GlqJPw5mUadnEQFJp7WRi+5sfrPFxlTZHIZ2J3rOOHqU21zdHjI9aEsxjXBYhDUD+QR3ir+mb50chnYnes44epTbXN0eMj1rJ8xGVgJUBY1BrTCE1woyxMW6VcD9HEWxbyXJMDi9Dc0ZmrwgFt2ki+Rcil17hvb7GKgoWedDc1fQnRIzTz2Rpke9KpBuuFE469bJUQWCuAsHPYyoXpjlfiKDU2WFdOlNImbzKuXKu8gU1we7mnmXM2MfkqyfryM+RuFwsouQy/IZPRhJQWMLX11mTREgGca0lSc9gkCbkeAPEWTKKuM6QJb8qpa4N+3LZCFea7rt59aCQw2VBKp12/xy0pRQomo/HLaERHJykeDw0wiq8RGUqypwxhD2+tzWTKE+21S8qdVOTVVhppHCTfllqh7KjWcgBM2ZRVbR1YQhU577N8ZBHC3r8ZTvLlex4RhKjO8q08FyLvYnRPncW0wuasw87trn9twMsS2K5I3Xz/bgxBRN6CPRlMIwms0uqDUmdo38Di2akP28YJSlhAd+fHFAwJfl+vXs3k5TtKK9qhy6cDaVPoD1OtR6YxFe4xsz+cOfPx8LNcHi57A8pgpI1WPLVu/EpfRbymIdROS5OJkQjIdvKDZC+5pW4PeATxp2M0UsIEQaE362mQZIX5isJcUcZmrYjo02qCdInr0GsWUhH8o2GHuiQvRVe+4Px3lu/4zwr7M0pWkcjq1B4Cmzdw63pONuRem7ME8FIw1U6ycgV5BWcqq7BL8oppShKY+vj8G33STiQB0mqNo35/dsFCf3eFuhmnlPXq0diNKlNjgey2EI4QiDcdKvwHGRLoRny4rQ1zkl0M6FW4v9BRprimlhMTfcy317C1XBrpU9hOGFhGEvtUHv0O9Fu5EEksmwERBw4rO0WNsqJas065xvPbLt9F4Q=" />

<input name="hdnSpecialServicesOptions" type="hidden" id="hdnSpecialServicesOptions" value="4," />

<input name="hdnPackage1Dimensions" type="hidden" id="hdnPackage1Dimensions" value="" />

<input name="hdnPackagePieceCount" type="hidden" id="hdnPackagePieceCount" value="$hdnPackagePieceCount$" />

<!-- FXSP=1 and FDXG=0 -->

<input name="hdnSmartPostPickupCarrier" type="hidden" id="hdnSmartPostPickupCarrier" value="$hdnSmartPostPickupCarrier$" />

<input name="hdnSmartPostDeliveryConfirmation" type="hidden" id="hdnSmartPostDeliveryConfirmation" value="$hdnSmartPostDeliveryConfirmation$" />

<input name="hdnIsTenderedEmailAllowed" type="hidden" id="hdnIsTenderedEmailAllowed" value="true" />

		</td>

	</tr>

</table>

</form>
    </body>
</html>
