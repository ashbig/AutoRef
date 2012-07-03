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
<title>FedEx Web Integration Wizard</title>
<!-- FWIW Version 1.0.0 --><!-- begin calendar -->
<!-- end calendar -->
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
	}
	</style>
<script language='javascript' type='text/javascript'> var originCntryCode 			= 'US';var recipientCntryCode 			= 'null';var originCntryCodeChecked 		= 'false';var recipientCntryCodeChecked 	= 'true';var isIntraBrazilShippingAllowed = 'false';var senderIEUserEntry = 'false';var senderIE = 'null';var senderTaxIdUserEntry = 'false';var senderTaxId = 'null';var recipientIEUserEntry = 'true';var recipientIE = 'null';var recipientTaxIdUserEntry = 'true';var recipientTaxId = 'null';var commercialInvoiceUserEntry = 'false';var recipientBrazilStateDisplay = 'false';var isRecipientStateProvinceChecked = 'true';var recipientStateCode = 'null';var brazilStateDisplay = 'false';var isStateProvinceChecked = 'false';var originStateCode = 'MA'; var liste = [        	             [ "AD", "EUR" ],        	             [ "AE", "AED" ],        	             [ "AF", "" ],        	             [ "AG", "XCD" ],        	             [ "AI", "XCD" ],        	             [ "AL", "" ],        	             [ "AM", "" ],        	             [ "AN", "ANG" ],        	             [ "AO", "" ],        	             [ "AR", "ARN" ],        	             [ "AS", "USD" ],        	             [ "AT", "EUR" ],        	             [ "AU", "AUD" ],        	             [ "AW", "AWG" ],        	             [ "AZ", "" ],        	             [ "BA", "" ],        	             [ "BB", "BBD" ],        	             [ "BD", "" ],        	             [ "BE", "EUR" ],        	             [ "BF", "" ],        	             [ "BG", "" ],        	             [ "BH", "BHD" ],        	             [ "BI", "" ],        	             [ "BJ", "" ],        	             [ "BM", "BMD" ],        	             [ "BN", "BND" ],        	             [ "BO", "" ],        	             [ "BR", "BRL" ],        	             [ "BS", "BSD" ],        	             [ "BT", "" ],        	             [ "BW", "" ],        	             [ "BY", "" ],        	             [ "BZ", "" ],        	             [ "CA", "CAD" ],        	             [ "CD", "" ],        	             [ "CG", "" ],        	             [ "CH", "CHF" ],        	             [ "CI", "" ],        	             [ "CK", "NZD" ],        	             [ "CL", "CLP" ],        	             [ "CM", "" ],        	             [ "CN", "CNY" ],        	             [ "CO", "COP" ],        	             [ "CR", "CRC" ],        	             [ "CV", "" ],        	             [ "CY", "EUR" ],        	             [ "CZ", "CZK" ],        	             [ "DE", "EUR" ],        	             [ "DJ", "" ],        	             [ "DK", "DKK" ],        	             [ "DM", "XCD" ],        	             [ "DO", "DOP" ],        	             [ "DZ", "" ],        	             [ "EC", "" ],        	             [ "EE", "EUR" ],        	             [ "EG", "EGP" ],        	             [ "ER", "" ],        	             [ "ES", "EUR" ],        	             [ "ET", "" ],        	             [ "FI", "EUR" ],        	             [ "FJ", "" ],        	             [ "FM", "USD" ],        	             [ "FO", "DKK" ],        	             [ "FR", "EUR" ],        	             [ "GA", "" ],        	             [ "GB", "GBP" ],        	             [ "GD", "XCD" ],        	             [ "GE", "" ],        	             [ "GF", "" ],        	             [ "GH", "" ],        	             [ "GI", "" ],        	             [ "GL", "DKK" ],        	             [ "GM", "" ],        	             [ "GN", "" ],        	             [ "GP", "EUR" ],        	             [ "GQ", "" ],        	             [ "GR", "EUR" ],        	             [ "GT", "GTQ" ],        	             [ "GU", "USD" ],        	             [ "GY", "" ],        	             [ "HK", "HKD" ],        	             [ "HN", "" ],        	             [ "HR", "" ],        	             [ "HT", "" ],        	             [ "HU", "HUF" ],        	             [ "ID", "" ],        	             [ "IE", "EUR" ],        	             [ "IL", "ILS" ],        	             [ "IN", "INR" ],        	             [ "IQ", "" ],        	             [ "IS", "" ],        	             [ "IT", "EUR" ],        	             [ "JM", "JAD" ],        	             [ "JO", "" ],        	             [ "JP", "JYE" ],        	             [ "KE", "KES" ],        	             [ "KG", "KZT" ],        	             [ "KH", "" ],        	             [ "KN", "XCD" ],        	             [ "KR", "KRW" ],        	             [ "KW", "KWD" ],        	             [ "KY", "KYD" ],        	             [ "KZ", "" ],        	             [ "LA", "" ],        	             [ "LB", "" ],        	             [ "LC", "XCD" ],        	             [ "LI", "CHF" ],        	             [ "LK", "" ],        	             [ "LR", "" ],        	             [ "LS", "" ],        	             [ "LT", "LTL" ],        	             [ "LU", "EUR" ],        	             [ "LV", "LVL" ],        	             [ "LY", "" ],        	             [ "MA", "" ],        	             [ "MC", "EUR" ],        	             [ "MD", "" ],        	             [ "ME", "EUR" ],        	             [ "MG", "" ],        	             [ "MH", "USD" ],        	             [ "MK", "" ],        	             [ "ML", "" ],        	             [ "MN", "" ],        	             [ "MO", "MOP" ],        	             [ "MP", "USD" ],        	             [ "MQ", "EUR" ],        	             [ "MR", "" ],        	             [ "MS", "XCD" ],        	             [ "MT", "EUR" ],        	             [ "MU", "" ],        	             [ "MV", "" ],        	             [ "MW", "" ],        	             [ "MX", "NMP" ],        	             [ "MY", "MYR" ],        	             [ "MZ", "" ],        	             [ "NA", "" ],        	             [ "NC", "" ],        	             [ "NE", "" ],        	             [ "NG", "" ],        	             [ "NI", "" ],        	             [ "NL", "EUR" ],        	             [ "NO", "NOK" ],        	             [ "NP", "" ],        	             [ "NZ", "NZD" ],        	             [ "OM", "" ],        	             [ "PA", "PAB" ],        	             [ "PE", "" ],        	             [ "PF", "" ],        	             [ "PG", "" ],        	             [ "PH", "PHP" ],        	             [ "PK", "PKR" ],        	             [ "PL", "PLN" ],        	             [ "PR", "USD" ],        	             [ "PS", "" ],        	             [ "PT", "EUR" ],        	             [ "PW", "USD" ],        	             [ "PY", "" ],        	             [ "QA", "" ],        	             [ "RE", "EUR" ],        	             [ "RO", "" ],        	             [ "RS", "" ],        	             [ "RU", "RUR" ],        	             [ "RW", "" ],        	             [ "SA", "SAR" ],        	             [ "SC", "" ],        	             [ "SE", "SEK" ],        	             [ "SG", "SGD" ],        	             [ "SI", "EUR" ],        	             [ "SK", "EUR" ],        	             [ "SN", "" ],        	             [ "SR", "" ],        	             [ "SV", "" ],        	             [ "SY", "" ],        	             [ "SZ", "" ],        	             [ "TC", "USD" ],        	             [ "TD", "" ],        	             [ "TG", "" ],        	             [ "TH", "THB" ],        	             [ "TL", "" ],        	             [ "TM", "" ],        	             [ "TN", "" ],        	             [ "TO", "" ],        	             [ "TR", "TRY" ],        	             [ "TT", "TTD" ],        	             [ "TW", "TWD" ],        	             [ "TZ", "" ],        	             [ "UA", "" ],        	             [ "UG", "UGX" ],        	             [ "US", "USD" ],        	             [ "UY", "UYU" ],        	             [ "UZ", "" ],        	             [ "VC", "XCD" ],        	             [ "VE", "VEB" ],        	             [ "VG", "USD" ],        	             [ "VI", "USD" ],        	             [ "VN", "" ],        	             [ "VU", "" ],        	             [ "WF", "" ],        	             [ "WS", "WST" ],        	             [ "YE", "" ],        	             [ "ZA", "ZAR" ],        	             [ "ZM", "" ],        	             [ "ZW", "" ]        	            ];		 	        function enableButtonsRowChecked(checkBox) {         	        	var btn = FindControl('btnHazMatAdd');			if (btn != null) {   					btn.disabled=checkBox.checked;			}			var btn = FindControl('btnHazMatEdit');			if (btn != null) {   					btn.disabled=!checkBox.checked;			}	    	var btn = FindControl('btnHazMatDelete');			if (btn != null) {   					btn.disabled=!checkBox.checked;			}        }        function disableSummaryButtons() {         	var btn = FindControl('btnHazMatAdd');			if (btn != null) {   					btn.disabled=true;			}			var btn = FindControl('btnHazMatEdit');			if (btn != null) {   					btn.disabled=true;			}	    	var btn = FindControl('btnHazMatDelete');			if (btn != null) {   					btn.disabled=true;			}        }          function enableSummaryButtons() {         	var btn = FindControl('btnHazMatAdd');			if (btn != null) {   					btn.disabled=false;			}			var btn = FindControl('btnHazMatEdit');			if (btn != null) {   					btn.disabled=true;			}	    	var btn = FindControl('btnHazMatDelete');			if (btn != null) {   					btn.disabled=true;			}			UnCheckTheCheckBox('chkHazMatCommodityCheck1');			UnCheckTheCheckBox('chkHazMatCommodityCheck2');			UnCheckTheCheckBox('chkHazMatCommodityCheck3');        }        function Row1Check() {        	var theField = FindControl('chkHazMatCommodityCheck1');    		    	if (theField != null && theField.checked) {	    		UnCheckTheCheckBox('chkHazMatCommodityCheck2');	    		UnCheckTheCheckBox('chkHazMatCommodityCheck3');	    	}	    	enableButtonsRowChecked(theField);	    	        }        function Row2Check() {        	var theField = FindControl('chkHazMatCommodityCheck2');    		    	if (theField != null && theField.checked) {	    		UnCheckTheCheckBox('chkHazMatCommodityCheck1');	    		UnCheckTheCheckBox('chkHazMatCommodityCheck3');	    	}	    	enableButtonsRowChecked(theField);        }         function Row3Check() {        	var theField = FindControl('chkHazMatCommodityCheck3');    		    	if (theField != null && theField.checked) {	    		UnCheckTheCheckBox('chkHazMatCommodityCheck1');	    		UnCheckTheCheckBox('chkHazMatCommodityCheck2');	    	}	    	enableButtonsRowChecked(theField);        }    					function textCounter(field, maxlimit)		{			if (field.value.length > maxlimit) 				field.value = field.value.substring(0, maxlimit);		}			 	function isHazMatAvailable()        {            var theAnswer = false;             var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');	        var isGroundSelected = false;			        	for (var i=0; i<ddlServiceType.length; i++)			{		   		if(ddlServiceType.options[i].selected == true)		   		{			   			var serviceTypeValue = ddlServiceType.options[i].value;		   						   		if (serviceTypeValue == 6) {			   				isGroundSelected = true;			   		}		   		}			 }	 			if (isGroundSelected && 				isPackageCountEqualToOne() &&				isOriginCountryUSA() &&				isRecipientCountryUSA() && 				isOriginStateNotAkOrHi() &&				isRecipientStateNotAkOrHi()) {				theAnswer = true;			}					 			return theAnswer;        }                             	function isLithiumBatteryAvailable()         {             var theAnswer = false;             var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');	        var isGroundOrHomeDeliverySelected = false;	              		        	for (var i=0; i<ddlServiceType.length; i++)			{		   		if(ddlServiceType.options[i].selected == true)		   		{			   			var serviceTypeValue = ddlServiceType.options[i].value;		   						   		if (serviceTypeValue == 6 || serviceTypeValue == 15) {			   				isGroundOrHomeDeliverySelected = true;			   		}		   					   		}			 }			 			if (isGroundOrHomeDeliverySelected && 				isPackageCountEqualToOne() &&				isOriginCountryUSA() &&				isRecipientCountryUSA()) {				theAnswer = true;			}			 			return theAnswer;        }                         function isOriginCountryUSA() {         		    var isOriginCntryUserEntry				= false ;		    var isOriginCntryNotUserEntryButUSA  	= true ;	    	var ddlCountry 							= FindControl('ddlCountry');					    var isCountryUSA = false; 		    if (isOriginCntryUserEntry)  {		   	    if (ddlCountry != null && ddlCountry.value != '-1' && ddlCountry.value == 'US' ) {		   	    	isCountryUSA = true;		   	    } 			} else {				isCountryUSA = isOriginCntryNotUserEntryButUSA;			}        	return isCountryUSA;        }                  function isOriginStateNotAkOrHi() {         		    var isOriginStateOrProvinceUserEntry		= false ;		    var isOriginStateNotUserEntryButNotAkOrHi  	= true ;	        var ddlOriginState 							= FindControl('ddlUSAStates');	        		    var isValidState = false; 		    if (isOriginStateOrProvinceUserEntry)  {		   	    if (ddlOriginState != null && ( ddlOriginState.value != '-1' &&  ddlOriginState.value != 'AK' && ddlOriginState.value != 'HI') ) {		   	    	isValidState = true;		   	    } 			} else {				isValidState = isOriginStateNotUserEntryButNotAkOrHi;			}        	return isValidState;        }                         function isRecipientCountryUSA() {         		    var isRecipientCntryUserEntry			= true ;		    var isRecipientCntryNotUserEntryButUSA  = false ;		    var ddlRecipientCountry 				= FindControl('ddlRecipientCountry');	        		    var isCountryUSA = false; 		    if (isRecipientCntryUserEntry)  {		   	    if (ddlRecipientCountry != null && ddlRecipientCountry.value != '-1' && ddlRecipientCountry.value == 'US') {		   	    	isCountryUSA = true;		   	    } 			} else {				isCountryUSA = isRecipientCntryNotUserEntryButUSA;			}        	return isCountryUSA;        }                        function isRecipientStateNotAkOrHi() {         		    var isRecipientStateOrProvinceUserEntry			= true ;		    var isRecipientStateNotUserEntryButNotAkOrHi  	= false ;		    var ddlRecipientState 							= FindControl('ddlRecipientUSAStates');	        		    var isValidState = false; 		    if (isRecipientStateOrProvinceUserEntry)  {		   	    if (ddlRecipientState != null && ( ddlRecipientState.value != '-1' && ddlRecipientState.value != 'AK' && ddlRecipientState.value != 'HI') ) {		   	    	isValidState = true;		   	    } 			} else {				isValidState = isRecipientStateNotUserEntryButNotAkOrHi;			}        	return isValidState;        }                        function isPackageCountEqualToOne() {                 var isPackagePieceCountChecked 				= false ;         var ddlNoOfPkgs = document.getElementById('ddlPackageShippingDetailNumberOfPackages');               var packagePieceCount = 0 ;         			 if(isPackagePieceCountChecked){		   if  (ddlNoOfPkgs.value == '-1') { 		    	 packagePieceCount = 0;			} else {				packagePieceCount = ddlNoOfPkgs.value;			}		  } 						return  packagePieceCount == 1;		}		              	function isSQEAvailable()         {            return isORMDAvailable();         }              	function isORMDAvailable()         {             var theAnswer = false;             var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');	        var isGroundOrHomeDeliverySelected = false;				              		        	for (var i=0; i<ddlServiceType.length; i++)			{		   		if(ddlServiceType.options[i].selected == true)		   		{			   			var serviceTypeValue = ddlServiceType.options[i].value;		   						   		if (serviceTypeValue == 6 || serviceTypeValue == 15) {			   				isGroundOrHomeDeliverySelected = true;			   		}		   					   		}			 }	 			if (isGroundOrHomeDeliverySelected && 				isPackageCountEqualToOne() &&				isOriginCountryUSA() &&				isRecipientCountryUSA() && 				isOriginStateNotAkOrHi() &&				isRecipientStateNotAkOrHi()) {				theAnswer = true;			}					 			return theAnswer;         }		function isDangerousGoodsAvailable()         {                       var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');          	 	        var isValidServiceSelected = false;	        var isYourPkgSelected = isYourPackagingSelected();	              		        	for (var i=0; i<ddlServiceType.length; i++)			{		   		if(ddlServiceType.options[i].selected == true)		   		{			   			var serviceTypeValue = ddlServiceType.options[i].value;		   			if(isYourPkgSelected &&  (serviceTypeValue == 7 ||		   			   						   serviceTypeValue == 13 ||								   			   serviceTypeValue == 14 ||								   			   serviceTypeValue == 20 ||								   			   serviceTypeValue == 2 ||								   			   serviceTypeValue == 5 ||								   			   serviceTypeValue == 21 ||								   			   serviceTypeValue == 1 ||								   			   serviceTypeValue == 3 ||		   			   								   			   serviceTypeValue == 4 || 								   			   serviceTypeValue == 10 ||		   			   								   			   serviceTypeValue == 11 ||	   			   								   			   serviceTypeValue == 8)		   			   )		   			{						isValidServiceSelected = true;								   			}		   		}			 }			 			return isValidServiceSelected;         }		function OnOriginUSAStatesChanged() {         	CheckDryIce();         	CheckORMD();	       	CheckSQE();	       	CheckLithiumBattery();	       	CheckHazMat();         }         function OnRecipientUSAStatesChanged() {         	CheckDryIce();         	CheckORMD();	       	CheckSQE();	       	CheckLithiumBattery();	       	CheckHazMat();         }    	 function isYourPackagingSelected()         {         	var ddlPackagingType = FindControl('ddlPackageShippingDetailPackageType');                  	var isYourPackagingSel = false;         					for(var i=0; i< ddlPackagingType.options.length; i++){			  var isSelected = ddlPackagingType.options[i].selected;			  var selectedValue = ddlPackagingType.options[i].value;			  if (isSelected && selectedValue == '6') {		 		isYourPackagingSel = true;		 	  }   		 	}         	return isYourPackagingSel;         }                function isDryIceAvailable()         {              return isDryIceAvailableCondition1() && isDryIceAvailableCondition2() ;         }                function isDryIceAvailableCondition1()         {                      var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');          	 	        var isValidServiceSelected = false;	        var isYourPkgSelected = isYourPackagingSelected();	              		        	for (var i=0; i<ddlServiceType.length; i++)			{		   		if(ddlServiceType.options[i].selected == true)		   		{			   			var serviceTypeValue = ddlServiceType.options[i].value;		   			if(isYourPkgSelected &&  (serviceTypeValue == 7 ||		   			   						   serviceTypeValue == 13 ||								   			   serviceTypeValue == 14 ||								   			   serviceTypeValue == 20 ||								   			   serviceTypeValue == 2 ||								   			   serviceTypeValue == 5 ||								   			   serviceTypeValue == 21 ||								   			   serviceTypeValue == 1 ||								   			   serviceTypeValue == 3 ||		   			   								   			   serviceTypeValue == 4 || 								   			   serviceTypeValue == 10 ||		   			   								   			   serviceTypeValue == 11 ||	   			   								   			   serviceTypeValue == 8)		   			   )		   			{						isValidServiceSelected = true;								   			} else 			   		if (serviceTypeValue == 6 || serviceTypeValue == 15) {			   				isValidServiceSelected = true;			   		}		   					   		}			 }			 			return isValidServiceSelected;         }                 function isDryIceAvailableCondition2()         {              var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');          	var theAnswer = false; 	        var isGroundOrHomeDeliverySelected = false;	              		        	for (var i=0; i<ddlServiceType.length; i++)			{		   		if(ddlServiceType.options[i].selected == true)		   		{			   			var serviceTypeValue = ddlServiceType.options[i].value;		   			if(serviceTypeValue == 6 ||		   			   serviceTypeValue == 15 )		   			{						isGroundOrHomeDeliverySelected = true;								   			} 		   		}			}											  if (isGroundOrHomeDeliverySelected ) {			    if (isOriginCountryUSA() &&					isRecipientCountryUSA() &&					isOriginStateNotAkOrHi() &&					isRecipientStateNotAkOrHi()) {				theAnswer = true;				}			} else {				theAnswer = true;			}			 			return theAnswer;         }        							function FindControl(control){    return document.getElementById(control);}function IsHidden(control){	var obj = FindControl(control);	if (obj != null && obj.style.display == "none") 		return true;	else 		return false;}function Show(){    for(var i=0; i<arguments.length; i++)      {     	var obj =  FindControl(arguments[i]);     	if (obj != null)        	obj.style.display = "";     }}function Hide(){     for(var i=0; i<arguments.length; i++)      {     	var obj =  FindControl(arguments[i]);     	if (obj != null)        	obj.style.display = "none";     }}function CheckTheCheckBox(checkbox){	if(FindControl(checkbox) != null){		FindControl(checkbox).checked = true;	}}function UnCheckTheCheckBox(checkbox){	if(FindControl(checkbox) != null){		FindControl(checkbox).checked = false;	}}function IsChecked(checkbox){	if(FindControl(checkbox) != null){		return FindControl(checkbox).checked;	}}function GetDdlSelectedValue(ddl){        var  list = FindControl(ddl);        for(i = 0; i < list.options.length;i++)        {            if( list.options[i].selected)               return list.options[i].value;        }	}function IsCheckboxChecked(checkbox) {	if(FindControl(checkbox) != null){		if(FindControl(checkbox).checked == 1) {			return true;		}else {			return false;		}	}}function SafelySetValue(theControl,theValue){	if(FindControl(theControl) != null){		FindControl(theControl).value = theValue;	}}			function UpdateCurrency(myObj)			{							var ddlCurrencyType = document.getElementById('ddlCurrencyType');								if (myObj != null && ddlCurrencyType != null) {					ChangeDdlSelectedIndex('ddlCurrencyType','-1');					for(var i = 0; i<liste.length ; i++)					{						if(myObj.options[myObj.selectedIndex].value == liste[i][0])						{							for(var j = 0; j < ddlCurrencyType.options.length; j++)								if(ddlCurrencyType.options[j].value == liste[i][1]) ddlCurrencyType.options[j].selected = true;						}					}								}			}						function SetStateVisibility()			{				var ddlCountry = document.getElementById('ddlCountry');				var ddlUSAStates = document.getElementById('ddlUSAStates');				var ddlCanadaStates = document.getElementById('ddlCanadaStates');				var ddlBrazilStates = document.getElementById('ddlBrazilStates');				var ddlRecipientCountry = document.getElementById('ddlRecipientCountry');								UpdateCurrency(ddlCountry);				ShowHideGenerateCustoms();				ShowHideGoodsNotInFreeCirculation();				ShowHideBillDutiesAndTaxes();								if ( ddlCountry != null && ddlUSAStates != null && ddlCanadaStates != null && ddlBrazilStates != null){					if(ddlCountry.value == 'US')					{						ddlUSAStates.style.display = "";						ddlUSAStates.disabled = false;							ddlCanadaStates.style.display = "none";						ddlCanadaStates.value = -1;						ddlBrazilStates.style.display = "none";						ddlBrazilStates.value = -1;					}					else if(ddlCountry.value == 'CA')					{						ddlCanadaStates.style.display = "";						ddlCanadaStates.disabled = false;						ddlUSAStates.style.display = "none";						ddlUSAStates.value = -1;							ddlBrazilStates.style.display = "none";						ddlBrazilStates.value = -1;												}					else if(ddlCountry.value == 'BR' && ddlRecipientCountry != null &&  ddlRecipientCountry.value == 'BR' && isIntraBrazilShippingAllowed == 'true')					{						ddlBrazilStates.style.display = "";						ddlBrazilStates.disabled = false;						ddlCanadaStates.style.display = "none";						ddlCanadaStates.value = -1;						ddlUSAStates.style.display = "none";						ddlUSAStates.value = -1;											}					else					{						ddlUSAStates.style.display = "none";						ddlCanadaStates.style.display = "";						ddlBrazilStates.style.display = "none";						ddlCanadaStates.disabled = true;						ddlBrazilStates.disabled = true;						ddlUSAStates.disabled = true;						ddlUSAStates.value = -1;						ddlCanadaStates.value = -1;						ddlBrazilStates.value = -1;											}															SetSpecialServices();								}								if (false == true){ 					ddlUSAStates.style.display = "";					ddlUSAStates.disabled = false;				}								if (false == true){ 					ddlCanadaStates.style.display = "";					ddlCanadaStates.disabled = false;				}				ShowHideBrazilStates();				SetSubmitButtonLabel();								}			function ShowHideBrazilStates() {				var stateProvinceCode = getOriginStateProvinceSelected(isStateProvinceChecked, originStateCode);				var originCntrySelected = getOriginCountrySelected(originCntryCodeChecked,originCntryCode);		    	var recipientCntrySelected = getRecipientCountrySelected(recipientCntryCodeChecked,recipientCntryCode);		    	var ddlBrazilStates = FindControl('ddlBrazilStates');		    	if ( ddlBrazilStates != null)		    		ddlBrazilStates.disabled = true;		    	if (((isStateProvinceChecked == 'false' &&  stateProvinceCode == 'null') || (isStateProvinceChecked == 'true')) && originCntrySelected == 'BR' && recipientCntrySelected == 'BR' && isIntraBrazilShippingAllowed == 'true'){		    		Show('ddlBrazilStates');		    		if ( ddlBrazilStates != null){			    		ddlBrazilStates.disabled = false;			    		if (stateProvinceCode != 'null')			    			ChangeDdlSelectedIndex('ddlBrazilStates', stateProvinceCode);		    		}	    			Hide('ddlCanadaStates');	    			Hide('ddlUSAStates');		    	}			}			function SetSenderTaxIdLabel()			{				var originCntrySelected = getOriginCountrySelected(originCntryCodeChecked,originCntryCode);	        	var recipientCntrySelected = getRecipientCountrySelected(recipientCntryCodeChecked,recipientCntryCode);			  	var senderTaxId = FindControl('tdSenderTaxId');  				var ddlCountry = FindControl('ddlCountry');				var ddlRecipientCountry = FindControl('ddlRecipientCountry');			    if (senderTaxId != null){			   		var img   = document.createElement('IMG');			   		img.setAttribute('src', 'https://www.fedex.com/FWIW/Client/Images/Common/appModuleHelpIcon.gif');			   		img.setAttribute('height', '14px'); 				 	img.setAttribute('width', '14px'); 			    	if ( originCntrySelected == 'BR' && recipientCntrySelected != 'BR' && isIntraBrazilShippingAllowed == 'true'){ 			      	 	senderTaxId.innerHTML = "&nbsp;&nbsp;Sender<br>&nbsp;&nbsp;CNPJ/CPF&nbsp";			      	 	img.setAttribute('title','The CNPJ is the Tax ID number of a company in Brazil while the CPF is the Tax ID number of an individual.');			   		}else if ( originCntrySelected == 'BR'  && recipientCntrySelected == 'BR' && isIntraBrazilShippingAllowed == 'true'){			    	   senderTaxId.innerHTML = "&nbsp;&nbsp;*Sender<br>&nbsp;&nbsp;CNPJ/CPF&nbsp";			      	   img.setAttribute('title','The CNPJ is the Tax ID number of a company in Brazil while the CPF is the Tax ID number of an individual.'); 			   		}else {			   		   senderTaxId.innerHTML = "&nbsp;&nbsp;Sender tax <br>&nbsp;&nbsp;ID no.&nbsp;";			   		   img.setAttribute('title','Examples of Tax ID numbers are Internal Revenue Service (IRS) Employer Identification Number (EIN), Value Added Tax (VAT) Number, and Economic Operators Registration and Identification Number (EORI). For Brazil domestic shipments, this value is required and contains the CNPJ/CPF.');			     	}			    	senderTaxId.appendChild(img);			   }			}			function SetRecipientTaxIdLabel()			{				var originCntrySelected = getOriginCountrySelected(originCntryCodeChecked,originCntryCode);	        	var recipientCntrySelected = getRecipientCountrySelected(recipientCntryCodeChecked,recipientCntryCode);				 var recipientTaxId = FindControl('tdRecipientTaxId');  				 var ddlCountry = FindControl('ddlCountry');				 var ddlRecipientCountry = FindControl('ddlRecipientCountry');				 if (recipientTaxId != null){					 var img   = document.createElement('IMG');					 img.setAttribute('src', 'https://www.fedex.com/FWIW/Client/Images/Common/appModuleHelpIcon.gif');					 img.setAttribute('height', '14px'); 					 img.setAttribute('width', '14px'); 				 	 if ( recipientCntrySelected == 'BR' && originCntrySelected != 'BR' && isIntraBrazilShippingAllowed == 'true'){ 				   		recipientTaxId.innerHTML = "&nbsp;&nbsp;Recipient<br>&nbsp;&nbsp;CNPJ/CPF&nbsp";				     	img.setAttribute('title','The CNPJ is the Tax ID number of a company in Brazil while the CPF is the Tax ID number of an individual.');				 	 }else if (originCntrySelected == 'BR' && recipientCntrySelected == 'BR' && isIntraBrazilShippingAllowed == 'true'){				    	recipientTaxId.innerHTML = "&nbsp;&nbsp;*Recipient<br>&nbsp;&nbsp;CNPJ/CPF&nbsp";				    	img.setAttribute('title','The CNPJ is the Tax ID number of a company in Brazil while the CPF is the Tax ID number of an individual.'); 				  	}else {				   		recipientTaxId.innerHTML = "&nbsp;&nbsp;Recipient tax <br>&nbsp;&nbsp;ID no.&nbsp;";				   		img.setAttribute('title','Examples of Tax ID numbers are Internal Revenue Service (IRS) Employer Identification Number (EIN), Value Added Tax (VAT) Number, and Economic Operators Registration and Identification Number (EORI). For Brazil domestic shipments, this value is required and contains the CNPJ/CPF.');				  	}				 	 recipientTaxId.appendChild(img);				 }			}			function getOriginStateProvinceSelected(originStateProvinceChecked, originStateCode) {			    var originStateProvinceCode					=  null;				var ddlBrazilStates 							=  FindControl('ddlBrazilStates');				 			    if (isStateProvinceChecked  == 'true')  {			   	    if (ddlBrazilStates != null && ddlBrazilStates.value != '-1') {			   	    	originStateProvinceCode = GetDdlSelectedValue('ddlBrazilStates');			   	 	}				}else {					originStateProvinceCode = originStateCode;				}				return originStateProvinceCode;			}			function getRecipientStateProvinceSelected(isRecipientStateProvinceChecked, recipientStateCode) {			    var recipientStateProvinceCode					=  null;				var ddlRecipientBrazilStates 							=  FindControl('ddlRecipientBrazilStates');				 			    if (isRecipientStateProvinceChecked  == 'true')  {			   	    if (ddlRecipientBrazilStates != null && ddlRecipientBrazilStates.value != '-1') {			   	    	recipientStateProvinceCode = GetDdlSelectedValue('ddlRecipientBrazilStates');			   	 	}				}else {					recipientStateProvinceCode = recipientStateCode;				}				return recipientStateProvinceCode;			}			function ShowHideRecipientBrazilStates(originCntrySelected, recipientCntrySelected) {				var recipientStateProvinceCode = getRecipientStateProvinceSelected(isRecipientStateProvinceChecked, recipientStateCode);		    	var addressValidation = document.getElementById('txtRecipientDetailedAddressCheck');		    	var recipientBrazilStates = FindControl('ddlRecipientBrazilStates');    			if (recipientBrazilStates != null)    				recipientBrazilStates.disabled = true;		    			    	if (((isRecipientStateProvinceChecked == 'false' && recipientStateProvinceCode == 'null') || (isRecipientStateProvinceChecked == 'true')) && originCntrySelected == 'BR' && recipientCntrySelected == 'BR' &&  isIntraBrazilShippingAllowed == 'true'){		    		Show('ddlRecipientBrazilStates');		    		Hide('ddlRecipientCanadaStates');		    		Hide('ddlRecipientUSAStates');		    		if (recipientBrazilStates != null){		    			recipientBrazilStates.disabled = false;		    			if (recipientStateProvinceCode != 'null'){		    				ChangeDdlSelectedIndex('ddlRecipientBrazilStates', recipientStateProvinceCode);			    		}		    		}		    				    		if (addressValidation != null){						addressValidation.disabled = true;					}		    	}			}			function SetRecipientStateVisibility()			{				var ddlRecipientCountry = document.getElementById('ddlRecipientCountry');				var ddlCountry = document.getElementById('ddlCountry');				var ddlRecipientUSAStates = document.getElementById('ddlRecipientUSAStates');				var ddlRecipientCanadaStates = document.getElementById('ddlRecipientCanadaStates');				var ddlRecipientBrazilStates = document.getElementById('ddlRecipientBrazilStates');				var addressValidation = document.getElementById('txtRecipientDetailedAddressCheck');				var originCntrySelected = getOriginCountrySelected(originCntryCodeChecked,originCntryCode);		    	var recipientCntrySelected = getRecipientCountrySelected(recipientCntryCodeChecked,recipientCntryCode);				if (ddlRecipientCountry != null && ddlRecipientUSAStates != null && ddlRecipientCanadaStates != null &&  ddlRecipientBrazilStates != null){					if(ddlRecipientCountry.value == 'US')					{						ddlRecipientCanadaStates.style.display = "none";						ddlRecipientBrazilStates.style.display = "none";						ddlRecipientUSAStates.style.display = "";						ddlRecipientUSAStates.disabled = false;						if (addressValidation != null){							addressValidation.disabled = false;						}						ddlRecipientCanadaStates.value = -1;						ddlRecipientBrazilStates.value = -1;					}					else if(ddlRecipientCountry.value == 'CA')					{						ddlRecipientCanadaStates.style.display = "";						ddlRecipientUSAStates.style.display = "none";						ddlRecipientBrazilStates.style.display = "none";						ddlRecipientCanadaStates.disabled = false;						if (addressValidation != null){							addressValidation.disabled = false;						}						ddlRecipientUSAStates.value = -1;						ddlRecipientBrazilStates.value = -1;					}					else if(ddlRecipientCountry.value == 'BR' && ddlCountry != null && ddlCountry.value == 'BR' && isIntraBrazilShippingAllowed == 'true')					{						ddlRecipientCanadaStates.style.display = "none";						ddlRecipientUSAStates.style.display = "none";						ddlRecipientBrazilStates.disabled = false;						ddlRecipientBrazilStates.style.display = "";						if (addressValidation != null){							addressValidation.disabled = true;						}						ddlRecipientCanadaStates.value = -1;						ddlRecipientUSAStates.value = -1;					}					else					{						ddlRecipientCanadaStates.style.display = "none";						ddlRecipientBrazilStates.style.display = "none";						ddlRecipientUSAStates.style.display = "";						ddlRecipientCanadaStates.disabled = true;						ddlRecipientBrazilStates.disabled = true;						ddlRecipientUSAStates.disabled = true;						if (addressValidation != null){							addressValidation.checked = false;							addressValidation.disabled = true;						}						ddlRecipientCanadaStates.value = -1;						ddlRecipientBrazilStates.value = -1;						ddlRecipientUSAStates.value = -1;					}					ShowHideGenerateCustoms();					ShowHideGoodsNotInFreeCirculation();					SetSpecialServices();										if (false == true){ 						ddlRecipientUSAStates.style.display = "";						ddlRecipientUSAStates.disabled = false;						if (addressValidation != null){							addressValidation.disabled = false;						}					}										if (false == true){ 						ddlRecipientCanadaStates.style.display = "";						ddlRecipientCanadaStates.disabled = false;						if (addressValidation != null){							addressValidation.disabled = false;						}					}					}				ShowHideRecipientBrazilStates(originCntrySelected, recipientCntrySelected);				SetSubmitButtonLabel();							}			function isContinueButtonAllowedForServiceType(){			   	 var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');			   	 var isServiceTypeSelected = false;			   	 var allowedServiceType = true;			            					   	var allowedSeviceTypeList = ['1','2','3','4','5','6','7','9','12','13','14','15','16','17','18','19','20','21','23'];	     						for (var i=0; i<ddlServiceType.length; i++)				{				   	if(ddlServiceType.options[i].selected == true)				   	{					   		isServiceTypeSelected = true;				   		var serviceType = ddlServiceType.options[i].value;				   		for(var j = 0; j<allowedSeviceTypeList.length ; j++){				   			if (serviceType == allowedSeviceTypeList[j]) {				   				allowedServiceType = false;				   				break;				   			}				   		}				   	  break;					   	}				}			   		 			   	if(!isServiceTypeSelected){			   		return false;				   	}else {			   		if(allowedServiceType)			   			return true;			   		else			   			return false;			   	}		   }			function isOriginAndRecipientPR(originCntryChecked, originCntryCode, recipientCntryChecked,recipientCntryCode) {				   var originCntry  = getOriginCountrySelected(originCntryChecked,originCntryCode);				   var recipientCntry = getRecipientCountrySelected(recipientCntryChecked, recipientCntryCode);				   return (originCntry !=  null && recipientCntry != null && originCntry == 'PR' && recipientCntry == 'PR');			}							   	function SetSubmitButtonLabel()			{				var ddlRecipientCountry = FindControl('ddlRecipientCountry');				var ddlCountry = FindControl('ddlCountry');				var serviceType = FindControl('ddlPackageShippingDetailServiceType').value;				var originCntrySelected = getOriginCountrySelected(originCntryCodeChecked,originCntryCode);				var recipientCntrySelected = getRecipientCountrySelected(recipientCntryCodeChecked,recipientCntryCode);				var continueButtonSet = false;				var goodInFreeCirculationEligiible = (IsHidden('trGoodsNotInFreeCirculation') || (IsShown('trGoodsNotInFreeCirculation') && IsCheckboxChecked('chkFreeCirculation'))); 				var generateCustomsEligible =  (IsHidden('trCustomsDoc') || (IsShown('trCustomsDoc') && IsCheckboxChecked('chkCustomsDoc') ));				var isOriginRecipientEligible = (isOriginNotSameAsRecipient(originCntrySelected,recipientCntrySelected) || isOriginAndRecipientPR(originCntrySelected,originCntryCode,recipientCntrySelected,recipientCntryCode));		   							if((originCntryCodeChecked == 'true' && ddlCountry.value == '-1') || ( recipientCntryCodeChecked == 'true' && ddlRecipientCountry.value == '-1')){					SetContinueButton();				}else if ( isOriginRecipientEligible &&  isContinueButtonAllowedForServiceType() && goodInFreeCirculationEligiible && generateCustomsEligible) {					SetContinueButton();				}else					SetGenerateLabel();			}	  	  function isSignatureOptionAvailable() {		   	if (isServiceTypeValidForDisplay())		   	        return true;		   	 return false;		}	  function isFedExHomeDeliverySelected()	  {  	      var serviceTypeValue = GetDdlSelectedValue('ddlPackageShippingDetailServiceType');	      if(serviceTypeValue == 15)	     	{	   		return true;							       }	       return false;	  } 		function isHomeDeliveryAvailable(){		   	 	if(isFedExHomeDeliverySelected())		   	 		 return true;		   	    return false;		}		   	 		   	 		function CheckSignatureOption()		{		   		Hide('trSignatureOption');		   		Hide('trSignatureOptionType');		   		var specialService = document.getElementById('hdnSpecialServicesOptions').value;		   		if (isSignatureOptionAvailable() && specialService.search("1") != '-1')		   		{		   			Show('trSignatureOption');		   			if (document.getElementById('chkSignatureOptions').checked)		   			{			   			    Show('trSignatureOptionType');		   			}		   		}else {		   					   			UnCheckTheCheckBox('chkSignatureOptions');		   		} 		}		  function CheckHomeDeliveryPremium()		 {		   		Hide('trHomeDeliveryPremium');		   		Hide('trHomeDeliveryDate');		   		Hide('trHomeDeliveryPremiumType');		   		Hide('trHomeDeliveryPhone');		   		var specialService = document.getElementById('hdnSpecialServicesOptions').value;		   		if (isHomeDeliveryAvailable() && specialService.search("2") != '-1')		   		{		   			Show('trHomeDeliveryPremium');		   			if (document.getElementById('chkHomeDeliveryPremium').checked)		   			{			   				Show('trHomeDeliveryPremiumType');		   				var premiumType = GetDdlSelectedValue('ddlHomeDeliveryPremiumType');		   				if (premiumType == '2'){		   					Show('trHomeDeliveryDate');		   				}		   				if (premiumType == '1' || premiumType == '2'){		   					Show('trHomeDeliveryPhone');		   				}		   			}		   			else{		   				ChangeDdlSelectedIndex('ddlHomeDeliveryPremiumType', -1);			   			FindControl('txtHomeDeliveryDate').value = '';			   			FindControl('txtHomeDeliveryPhone').value = '';		   			}		   		}else {		   			UnCheckTheCheckBox('chkHomeDeliveryPremium');		   		} 			}		      		  function isAnySpecialServiceValidForDisplay(){		      if (isDryIceAvailable() ||  isHazMatAvailable() || isDangerousGoodsAvailable() || isSignatureOptionAvailable() || isHomeDeliveryAvailable() || isLithiumBatteryAvailable() || isORMDAvailable() || isSQEAvailable())		        	   	   	return true;		       return false;		       		  }	 function displayHubId(hubIdsString){		if (hubIdsString != "") {			var arrayValue = hubIdsString.split(",");			var count = 0;			for(i = 0; i < arrayValue.length-1; i++)	{				count++;			}			if (count >= 2)				return true;			return false;		}	    return false;	 }	 function HideDropOffType(){			Hide('trDropoffType');		    Hide('trSmartPostDropoffType');			}		function HideSmartPostFields(){			Hide('tblSmartPostSpecialServices');			Hide('trHubId');			Hide('trSmartPostNote');		}		function getSelectedDropOffType(ddlType){			var selectedPickupDropOffValue = null;			if(ddlType != null){				for (var i=0; i<ddlType.length; i++)				{					if(ddlType.options[i].selected == true)					{							selectedPickupDropOffValue = ddlType.options[i].value;					}				}  				for(i = ddlType.options.length -1;i>=0;i--)					ddlType.remove(i);				return selectedPickupDropOffValue;							}		}		function DisplayPickupDropOff()		{  			document.getElementById('trDropoffType').style.display = "";			var ddlDropoffType = FindControl('ddlDropoffType');						var selectedPickupDropOffValue = getSelectedDropOffType(ddlDropoffType);			        				if (ddlDropoffType != null){				ddlDropoffType.options.length = 6;			    var option = new Option("Select", -1);				ddlDropoffType.options[0] = option;   							option = new Option("Regular pickup", 0);				ddlDropoffType.options[1] = option;									option = new Option("Will request courier", 1);				ddlDropoffType.options[2] = option;				option = new Option("Drop box", 2);				ddlDropoffType.options[3] = option;				option = new Option("Business service center", 3);				ddlDropoffType.options[4] = option;				option = new Option("FedEx station", 4);				ddlDropoffType.options[5] = option;			}			ChangeDdlSelectedIndex('ddlDropoffType', selectedPickupDropOffValue);		}		function DisplaySmartPostPickupDropOff()		{  			document.getElementById('trSmartPostDropoffType').style.display = "";			var ddlDropoffType = FindControl('ddlSmartPostDropoffType');			        				var selectedPickupDropOffValue = getSelectedDropOffType(ddlDropoffType);						if(ddlDropoffType != null)			{         							ddlDropoffType.options.length = 5;			        var option = new Option("Select", -1);				ddlDropoffType.options[0] = option;   							option = new Option("Regular pickup", 0);				ddlDropoffType.options[1] = option;									option = new Option("Will request courier", 1);				ddlDropoffType.options[2] = option;				option = new Option("Business service center", 3);				ddlDropoffType.options[3] = option;				option = new Option("FedEx station", 4);				ddlDropoffType.options[4] = option;							}			ChangeDdlSelectedIndex('ddlSmartPostDropoffType', selectedPickupDropOffValue);		}     		            	   	function SetSpecialServices(){		var serviceType = document.getElementById('ddlPackageShippingDetailServiceType').value;		ShowHideBillDutiesAndTaxes();		ShowHideEmailNotificationCheckBox();				if(serviceType == '-1'){			if (true){				Hide('tblSpecialServices');			}			HideSmartPostFields();			HideDropOffType();			ChangeDdlSelectedIndex('ddlDropoffType', '-1');			ChangeDdlSelectedIndex('ddlSmartPostDropoffType', '-1');		}else{						if(serviceType != '16' && serviceType != '17' && serviceType != '18' && serviceType != '19')			{								HideSmartPostFields();				HideDropOffType();				if('true' == 'true'){					if(serviceType == '6' || serviceType == '15' || serviceType == '22'){						DisplaySmartPostPickupDropOff();						ChangeDdlSelectedIndex('ddlDropoffType', '-1');					}else{						DisplayPickupDropOff();						ChangeDdlSelectedIndex('ddlSmartPostDropoffType', '-1');					}				}               													if (true){		       		var options = document.getElementById('hdnSpecialServicesOptions').value;		      				      	if (options != 'null' && options != '' &&  isAnySpecialServiceValidForDisplay())			      	{			      		Show('tblSpecialServices');			      		ResetSmartPostSpecialService();			      	}			      	if (options == 'null' || options == '')			      	{				   		Hide('trSignatureOption');			   			Hide('trSignatureOptionType');			   			document.getElementById('chkSignatureOptions').checked = "";			   						   			Hide('trHomeDeliveryPremium');			   			Hide('trHomeDeliveryPremiumType');			   			document.getElementById('chkHomeDeliveryPremium').checked = "";			   						   			Hide('trFutureDayShipment');			   			document.getElementById('chkFutureDayShipment').checked = "";			   						   			Hide('trDryIce');			   			Hide('trDryIce2');			   			UnCheckTheCheckBox('chkDryIce');			   			Hide('trDangerousGoods');			   			Hide('trDangerousGoodsType');			   			UnCheckTheCheckBox('chkDangerousGoods');			   						   			Hide('tblSpecialServices');				    } else	{				    	 CheckSignatureOption();				         CheckHomeDeliveryPremium();			   			if (options.search("3") != '-1' ) 			   			{			   				Show('trFutureDayShipment');			   			} 			   			else 			   			{			   				Hide('trFutureDayShipment');			   				document.getElementById('chkFutureDayShipment').checked = "";			   			}			   					   			if (options.search("4") != '-1' )			   			{			   				Show('trDryIce');			   			} 			   			else 			   			{			   				Hide('trDryIce');			   				Hide('trDryIce2');			   				UnCheckTheCheckBox('chkDryIce');			   			}			   					   			if (options.search("5") != '-1' )			   			{			   				Show('trDangerousGoods');			   			} 			   			else 			   			{			   				Hide('trDangerousGoods');			   				Hide('trDangerousGoodsType');			   				UnCheckTheCheckBox('chkDangerousGoods');			   			}  			   		}				}			}			else			{								Show('trSmartPostNote');				HideDropOffType();				ChangeDdlSelectedIndex('ddlDropoffType', '-1');				if('true' == 'true'){					if('$hdnSmartPostPickupCarrier$' == '0'){						DisplaySmartPostPickupDropOff();					}				} 							 	if(displayHubId('$ListOfHubIDs$')){		            Show('trHubId');			 	}                                Hide('tblSpecialServices');				Show('tblSmartPostSpecialServices');				ResetSpecialService();			}	   	}		   	EnableDisablePackages();	   	SetSubmitButtonLabel();		   	if (true){	   		CheckDryIce();	   		CheckDangerousGoods();	   		CheckORMD();	   		CheckSQE();        	CheckLithiumBattery();        	CheckHazMat();          	CheckSignatureOption();            CheckHomeDeliveryPremium();  	   	}   	}   	   	function ResetSpecialService()   	{   		if (true)   		{			FindControl('chkSignatureOptions').checked = "";			ChangeDdlSelectedIndex('ddlSignatureOptionType', -1);						FindControl('chkHomeDeliveryPremium').checked = "";			ChangeDdlSelectedIndex('ddlHomeDeliveryPremiumType', -1);			FindControl('txtHomeDeliveryDate').value = '';			FindControl('txtHomeDeliveryPhone').value = '';									UnCheckTheCheckBox('chkDryIce');			SafelySetValue('txtDryIceWeight', '');						UnCheckTheCheckBox('chkDangerousGoods');						ChangeDdlSelectedIndex('ddlDangerousGoodsType', -1);		}   	}   	   	function ResetSmartPostSpecialService(){   		if(FindControl('ddlSmartPostUndeliverable') != null){                 	ChangeDdlSelectedIndex('ddlSmartPostUndeliverable', -1);        }   	}   	   	function CheckDimensions(obj){    	if (obj.value == 'L' || obj.value == 'W'  || obj.value == 'H')    		obj.value = '';    }	     	  	function CheckDryIce()        {    	Hide('trDryIce');;    	Hide('trDryIce2');    		    	if (isDryIceAvailable())    	{    		Show('trDryIce');    		if (IsChecked('chkDryIce'))    		{	    		var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');	    		var serviceType = ddlServiceType.value;    			if(!(serviceType == '6' || serviceType == '15'))     			{    				Show('trDryIce2');    			}    		}    		    	}else {    		UnCheckTheCheckBox('chkDryIce');    	}     	}		function CheckDangerousGoods()    {        	Hide('trDangerousGoods');;    	Hide('trDangerousGoodsType');    		    	if (isDangerousGoodsAvailable())    	{    		Show('trDangerousGoods');    		    		if (IsChecked('chkDangerousGoods'))    		{	    		    Show('trDangerousGoodsType');    		}    		    	}else {    		UnCheckTheCheckBox('chkDangerousGoods');    	}     	}		function CheckORMD()    {    	    	var theField = FindControl('chkORMD');    	    	Hide('trORMD');    	    	if (isORMDAvailable())    	{    		Show('trORMD');    		    	}else {    		UnCheckTheCheckBox('chkORMD');    	}    	    	if (theField != null && theField.checked) {    		UnCheckTheCheckBox('chkSQE');    		UnCheckTheCheckBox('chkHazardousMaterials');    			     	var hzMatSummary = FindControl('trHazMatSummary');    			    	if (hzMatSummary != null) {   					hzMatSummary.style.visibility = "hidden";			}    		    		    		    	}    	    	}		function CheckSQE()    {    	var theField = FindControl('chkSQE');    	Hide('trSQE');    		    	if (isSQEAvailable())    	{    		Show('trSQE');    		    	}else {    		UnCheckTheCheckBox('chkSQE');    	}    	    	if (theField != null && theField.checked) {    		UnCheckTheCheckBox('chkORMD');    		UnCheckTheCheckBox('chkHazardousMaterials');    			     	var hzMatSummary = FindControl('trHazMatSummary');    			    	if (hzMatSummary != null) {   					hzMatSummary.style.visibility = "hidden";			}    		    		    		    	}    	}			function CheckLithiumBattery()    {        	Hide('trLithiumBattery');    		    	if (isLithiumBatteryAvailable())    	{    		Show('trLithiumBattery');    		    	}else {    		UnCheckTheCheckBox('chkLithiumBattery');    	}    		}		function CheckHazMat()    {    	Hide('trHazMat');         	var hzMatSummary = FindControl('trHazMatSummary');     	     	    	if (hzMatSummary != null) {   				hzMatSummary.style.visibility = "hidden";		}		    	if (isHazMatAvailable())    	{	    		Show('trHazMat');    		    	   	var theField = FindControl('chkHazardousMaterials');    		    	if (theField != null && theField.checked) {	    		UnCheckTheCheckBox('chkORMD');	    		UnCheckTheCheckBox('chkSQE');	    			    		if (hzMatSummary != null) {   						hzMatSummary.style.visibility = "";				}								disableSummaryButtons();				var btn = FindControl('btnHazMatAdd');				if (btn != null) {   						btn.disabled=false;				}							    	}    	}    	else    	{    		UnCheckTheCheckBox('chkHazardousMaterials');     		var btn = FindControl('btnGenerateLabel');			if (btn != null) {   					btn.disabled=false;			}  	    	    	}    		}		 function isPackageTypeYourPackaging(){			var isYourPkgSelected = isYourPackagingSelected();			if((IsShown('ddlPackageShippingDetailPackageType') && isYourPkgSelected) || (IsHidden('ddlPackageShippingDetailPackageType') && isYourPkgSelected))				return true;			return false;		 }		 function EnableDisablePackages()         {             	var ddlPackageShippingDetailNumberOfPackages = document.getElementById('ddlPackageShippingDetailNumberOfPackages');         	var hdnPackagePieceCount = document.getElementById('hdnPackagePieceCount');         	var serviceType = document.getElementById('ddlPackageShippingDetailServiceType').value;         	var value;         	var serviceType = 'true';         	if (serviceType == 'false'){        		Hide("trServiceType");        	}else {            	Show("trServiceType");        	}        	var packageType = 'true';         	if (packageType == 'false'){        		Hide("trPackageType");        	}else {            	Show("trPackageType");        	}        	         	if (ddlPackageShippingDetailNumberOfPackages == null){         		if(hdnPackagePieceCount != null){         			value = hdnPackagePieceCount.value;         		}else{         			value = '-1';         		}         	}         	else         		value = ddlPackageShippingDetailNumberOfPackages.value;         	         	if (value == '-1')            {            	                     Show('trWeight');                                Hide('trPackage1');                Hide('trPackage2');                Hide('trPackage3');                Hide('trPackage4');                Hide('trPackage5');                                Hide('trWeight1');                Hide('trWeight2');                   Hide('trWeight3');                Hide('trWeight4');                    Hide('trWeight5');                                Hide('trPackageDimensions1');                Hide('trPackageDimensions2');                Hide('trPackageDimensions3');                Hide('trPackageDimensions4');                Hide('trPackageDimensions5');                                Hide('trCurrencyType');                Hide('trDeclaredValue1');                Hide('trDeclaredValue2');                Hide('trDeclaredValue3');                Hide('trDeclaredValue4');                Hide('trDeclaredValue5');            } else {	         	if(value == '1')	            {   	                	                Hide('trPackage2');	                Hide('trPackage3');	                Hide('trPackage4');	                Hide('trPackage5');	                	                Hide('trPackageDimensions2');	                Hide('trPackageDimensions3');	                Hide('trPackageDimensions4');	                Hide('trPackageDimensions5');    	                	                	                if ( 'true' == 'true' && isPackageTypeYourPackaging() ){            			Show('trPackageDimensions1');            			if(!isServiceTypeValidForDisplay()){            				Show('lblPackageDimensions1NoAst');            				Hide('lblPackageDimensions1');            			}else{            				Show('lblPackageDimensions1');            				Hide('lblPackageDimensions1NoAst');            			}            			Hide('trPackage1');            		}            		else  {            			Hide('trPackageDimensions1');            			if ( 'true' == 'true' && isPackageTypeYourPackaging()){            				Show('trPackage1');            			}else{            				Hide('trPackage1');            			}            			if(!isServiceTypeValidForDisplay()){            				Show('lblPackage1NoAst');            				Hide('lblPackage1');            			}else{            				Show('lblPackage1');            				Hide('lblPackage1NoAst');            			}              		}            		            		if(ddlPackageShippingDetailNumberOfPackages != null){            			Show('trWeight');            		}else{            			Hide('trWeight');            		}           			                	                Hide('trWeight1');	                Hide('trWeight2');   	                Hide('trWeight3');	                Hide('trWeight4');    	                Hide('trWeight5');	                	                if(isServiceTypeValidForDisplay()){	                		Show('trDeclaredValue1');	                		Show('trCurrencyType');	                }else{                		Hide('trDeclaredValue1');                		Hide('trCurrencyType');                	}                 	Hide('trDeclaredValue2');                	Hide('trDeclaredValue3');                	Hide('trDeclaredValue4');                	Hide('trDeclaredValue5');	    	            }	            else if(value == '2')	            { 	                	                Hide('trPackage3');	                Hide('trPackage4');	                Hide('trPackage5'); 	                	                Hide('trPackageDimensions3');	                Hide('trPackageDimensions4');	                Hide('trPackageDimensions5'); 	                	                if ( 'true' == 'true' && isPackageTypeYourPackaging()){            			Show('trPackageDimensions1');            			Show('trPackageDimensions2');            			if(!isServiceTypeValidForDisplay()){            				Show('lblPackageDimensions1NoAst');            				Hide('lblPackageDimensions1');            				Show('lblPackageDimensions2NoAst');            				Hide('lblPackageDimensions2');            			}else{            				Show('lblPackageDimensions1');            				Hide('lblPackageDimensions1NoAst');            				Show('lblPackageDimensions2');            				Hide('lblPackageDimensions2NoAst');            			}            			Hide('trPackage1');            			Hide('trPackage2');            		}            		else  {            			Hide('trPackageDimensions1');            			Hide('trPackageDimensions2');            			if ( 'true' == 'true' && isPackageTypeYourPackaging()){	            			Show('trPackage1');		                	Show('trPackage2');	                	}else{	                		Hide('trPackage1');		                	Hide('trPackage2');	                	}	                	if(!isServiceTypeValidForDisplay()){            				Show('lblPackage1NoAst');            				Hide('lblPackage1');            				Show('lblPackage2NoAst');            				Hide('lblPackage2');            			}else{            				Show('lblPackage1');            				Hide('lblPackage1NoAst');            				Show('lblPackage2');            				Hide('lblPackage2NoAst');            			}            		}	                Hide('trWeight');	                	                if(ddlPackageShippingDetailNumberOfPackages != null){	                	Show('trWeight1');	                	Show('trWeight2'); 	                }else{	                	Hide('trWeight1');	                	Hide('trWeight2'); 	                }  	                Hide('trWeight3');	                Hide('trWeight4');    	                Hide('trWeight5');   	                	                if(isServiceTypeValidForDisplay()){		                Show('trDeclaredValue1');	                	Show('trDeclaredValue2');	                	Show('trCurrencyType');                	}else{                		Hide('trDeclaredValue1');	                	Hide('trDeclaredValue2');	                	Hide('trCurrencyType');                	}                 	Hide('trDeclaredValue3');                	Hide('trDeclaredValue4');                	Hide('trDeclaredValue5');            	    	            }	            else if(value == '3')	            { 	                	                Hide('trPackage4');	                Hide('trPackage5');	                	                Hide('trPackageDimensions4');	                Hide('trPackageDimensions5');	                	                if ( 'true' == 'true' && isPackageTypeYourPackaging()){            			Show('trPackageDimensions1');            			Show('trPackageDimensions2');            			Show('trPackageDimensions3');            			if(!isServiceTypeValidForDisplay()){            				Show('lblPackageDimensions1NoAst');            				Hide('lblPackageDimensions1');            				Show('lblPackageDimensions2NoAst');            				Hide('lblPackageDimensions2');            				Show('lblPackageDimensions3NoAst');            				Hide('lblPackageDimensions3');            			}else{            				Show('lblPackageDimensions1');            				Hide('lblPackageDimensions1NoAst');            				Show('lblPackageDimensions2');            				Hide('lblPackageDimensions2NoAst');            				Show('lblPackageDimensions3');            				Hide('lblPackageDimensions3NoAst');            			}            			Hide('trPackage1');            			Hide('trPackage2');            			Hide('trPackage3');            		}            		else  {            			Hide('trPackageDimensions1');            			Hide('trPackageDimensions2');            			Hide('trPackageDimensions3');            			if ( 'true' == 'true' && isPackageTypeYourPackaging()){	            			Show('trPackage1');		                	Show('trPackage2');		                	Show('trPackage3');	                	}else{	                		Hide('trPackage1');		                	Hide('trPackage2');		                	Hide('trPackage3');	                	}	                	if(!isServiceTypeValidForDisplay()){            				Show('lblPackage1NoAst');            				Hide('lblPackage1');            				Show('lblPackage2NoAst');            				Hide('lblPackage2');            				Show('lblPackage3NoAst');            				Hide('lblPackage3');            			}else{            				Show('lblPackage1');            				Hide('lblPackage1NoAst');            				Show('lblPackage2');            				Hide('lblPackage2NoAst');            				Show('lblPackage3');            				Hide('lblPackage3NoAst');            			}            		}	                Hide('trWeight');	                	                if(ddlPackageShippingDetailNumberOfPackages != null){		                Show('trWeight1');		                Show('trWeight2');   		                Show('trWeight3');	                }else{	                	Hide('trWeight1');		                Hide('trWeight2');   		                Hide('trWeight3');	                }	                Hide('trWeight4');    	                Hide('trWeight5');	                	                if(isServiceTypeValidForDisplay()){		                Show('trDeclaredValue1');	                	Show('trDeclaredValue2');	                	Show('trDeclaredValue3');	                	Show('trCurrencyType');                	}else{                		Hide('trDeclaredValue1');	                	Hide('trDeclaredValue2');	                	Hide('trDeclaredValue3');	                	Hide('trCurrencyType');                	}                 	Hide('trDeclaredValue4');                	Hide('trDeclaredValue5');      	            }	            else if(value == '4')	            {   	                Hide('trPackage5');	                	                Hide('trPackageDimensions5');	                	                if ( 'true' == 'true' && isPackageTypeYourPackaging()){            			Show('trPackageDimensions1');            			Show('trPackageDimensions2');            			Show('trPackageDimensions3');            			Show('trPackageDimensions4');            			if(!isServiceTypeValidForDisplay()){            				Show('lblPackageDimensions1NoAst');            				Hide('lblPackageDimensions1');            				Show('lblPackageDimensions2NoAst');            				Hide('lblPackageDimensions2');            				Show('lblPackageDimensions3NoAst');            				Hide('lblPackageDimensions3');            				Show('lblPackageDimensions4NoAst');            				Hide('lblPackageDimensions4');            			}else{            				Show('lblPackageDimensions1');            				Hide('lblPackageDimensions1NoAst');            				Show('lblPackageDimensions2');            				Hide('lblPackageDimensions2NoAst');            				Show('lblPackageDimensions3');            				Hide('lblPackageDimensions3NoAst');            				Show('lblPackageDimensions4');            				Hide('lblPackageDimensions4NoAst');            			}            			Hide('trPackage1');            			Hide('trPackage2');            			Hide('trPackage3');            			Hide('trPackage4');            		}            		else  {            			Hide('trPackageDimensions1');            			Hide('trPackageDimensions2');            			Hide('trPackageDimensions3');            			Hide('trPackageDimensions4');            			if ( 'true' == 'true' && isPackageTypeYourPackaging()){	            			Show('trPackage1');		                	Show('trPackage2');		                	Show('trPackage3');		                	Show('trPackage4');	                	}else{	                		Hide('trPackage1');		                	Hide('trPackage2');		                	Hide('trPackage3');		                	Hide('trPackage4');	                	}	                	if(!isServiceTypeValidForDisplay()){            				Show('lblPackage1NoAst');            				Hide('lblPackage1');            				Show('lblPackage2NoAst');            				Hide('lblPackage2');            				Show('lblPackage3NoAst');            				Hide('lblPackage3');            				Show('lblPackage4NoAst');            				Hide('lblPackage4');            			}else{            				Show('lblPackage1');            				Hide('lblPackage1NoAst');            				Show('lblPackage2');            				Hide('lblPackage2NoAst');            				Show('lblPackage3');            				Hide('lblPackage3NoAst');            				Show('lblPackage4');            				Hide('lblPackage4NoAst');            			}            		}	                Hide('trWeight');	                	                if(ddlPackageShippingDetailNumberOfPackages != null){		                Show('trWeight1');		                Show('trWeight2');   		                Show('trWeight3');		                Show('trWeight4');  	                } else{	                	Hide('trWeight1');		                Hide('trWeight2');   		                Hide('trWeight3');		                Hide('trWeight4');  	                } 	                Hide('trWeight5');  	                	                if(isServiceTypeValidForDisplay()){		                Show('trDeclaredValue1');	                	Show('trDeclaredValue2');	                	Show('trDeclaredValue3');	                	Show('trDeclaredValue4');	                	Show('trCurrencyType');                	}else{                		Hide('trDeclaredValue1');	                	Hide('trDeclaredValue2');	                	Hide('trDeclaredValue3');	                	Hide('trDeclaredValue4');	                	Hide('trCurrencyType');                	}                 	Hide('trDeclaredValue5');   	            }	            else if(value == '5')	            {	                if ( 'true' == 'true' && isPackageTypeYourPackaging()  ){            			Show('trPackageDimensions1');            			Show('trPackageDimensions2');            			Show('trPackageDimensions3');            			Show('trPackageDimensions4');            			Show('trPackageDimensions5');            			if(!isServiceTypeValidForDisplay()){            				Show('lblPackageDimensions1NoAst');            				Hide('lblPackageDimensions1');            				Show('lblPackageDimensions2NoAst');            				Hide('lblPackageDimensions2');            				Show('lblPackageDimensions3NoAst');            				Hide('lblPackageDimensions3');            				Show('lblPackageDimensions4NoAst');            				Hide('lblPackageDimensions4');            				Show('lblPackageDimensions5NoAst');            				Hide('lblPackageDimensions5');            			}else{            				Show('lblPackageDimensions1');            				Hide('lblPackageDimensions1NoAst');            				Show('lblPackageDimensions2');            				Hide('lblPackageDimensions2NoAst');            				Show('lblPackageDimensions3');            				Hide('lblPackageDimensions3NoAst');            				Show('lblPackageDimensions4');            				Hide('lblPackageDimensions4NoAst');            				Show('lblPackageDimensions5');            				Hide('lblPackageDimensions5NoAst');            			}            			Hide('trPackage1');            			Hide('trPackage2');            			Hide('trPackage3');            			Hide('trPackage4');            			Hide('trPackage5');            		}            		else  {            			Hide('trPackageDimensions1');            			Hide('trPackageDimensions2');            			Hide('trPackageDimensions3');            			Hide('trPackageDimensions4');            			Hide('trPackageDimensions5');            			if ( 'true' == 'true' && isPackageTypeYourPackaging() ){	            			Show('trPackage1');		                	Show('trPackage2');		                	Show('trPackage3');		                	Show('trPackage4');		                	Show('trPackage5');	                	}else{	                		Hide('trPackage1');		                	Hide('trPackage2');		                	Hide('trPackage3');		                	Hide('trPackage4');		                	Hide('trPackage5');	                	}	                	if(!isServiceTypeValidForDisplay()){            				Show('lblPackage1NoAst');            				Hide('lblPackage1');            				Show('lblPackage2NoAst');            				Hide('lblPackage2');            				Show('lblPackage3NoAst');            				Hide('lblPackage3');            				Show('lblPackage4NoAst');            				Hide('lblPackage4');            				Show('lblPackage5NoAst');            				Hide('lblPackage5');            			}else{            				Show('lblPackage1');            				Hide('lblPackage1NoAst');            				Show('lblPackage2');            				Hide('lblPackage2NoAst');            				Show('lblPackage3');            				Hide('lblPackage3NoAst');            				Show('lblPackage4');            				Hide('lblPackage4NoAst');            				Show('lblPackage5');            				Hide('lblPackage5NoAst');            			}            		}            		Hide('trWeight');	                if(ddlPackageShippingDetailNumberOfPackages != null){		                Show('trWeight1');		                Show('trWeight2');   		                Show('trWeight3');		                Show('trWeight4');    		                Show('trWeight5'); 	                }else{	                	Hide('trWeight1');		                Hide('trWeight2');   		                Hide('trWeight3');		                Hide('trWeight4');    		                Hide('trWeight5'); 	                }	                	                if(isServiceTypeValidForDisplay()){		                Show('trDeclaredValue1');	                	Show('trDeclaredValue2');	                	Show('trDeclaredValue3');	                	Show('trDeclaredValue4');	                	Show('trDeclaredValue5'); 	                	Show('trCurrencyType');                	}else{                		Hide('trDeclaredValue1');	                	Hide('trDeclaredValue2');	                	Hide('trDeclaredValue3');	                	Hide('trDeclaredValue4');	                	Hide('trDeclaredValue5');	                	Hide('trCurrencyType');                	} 	            } 	            	         }	         if ( '3' == '1' || document.getElementById('ddlPackageShippingDetailPackageType').value != '6')            {            	                     Hide('trPackage1');                Hide('trPackage2');                Hide('trPackage3');                Hide('trPackage4');                Hide('trPackage5');                                Hide('trPackageDimensions1');                Hide('trPackageDimensions2');                Hide('trPackageDimensions3');                Hide('trPackageDimensions4');                Hide('trPackageDimensions5');            }                        if ( 'false' == 'true'  ){            	Show('trPackageDimensions');            }            else{            	Hide('trPackageDimensions');	            }                          CheckDryIce();                   CheckDangerousGoods();            CheckSQE();            CheckORMD();        	CheckLithiumBattery();        	CheckHazMat();          	CheckSignatureOption();            CheckHomeDeliveryPremium();         }            function ChangeAdditionalRecipients(){				var additionalRecipient = document.getElementById('additionalRecipient');				if (additionalRecipient != null ){					if (additionalRecipient.value == '1') 						additionalRecipient.value = '0';					else 						additionalRecipient.value = '1';				}								additionalRecipients();											}            function additionalRecipients()			{				var spnAdditionalRecipients1 = document.getElementById('additionalRecipients1');				var spnAdditionalRecipients3 = document.getElementById('additionalRecipients3');								var additionalRecipient = document.getElementById('additionalRecipient');				if (additionalRecipient != null && additionalRecipient.value == '1') {						spnAdditionalRecipients1.style.display = "";						spnAdditionalRecipients3.style.display = "";				}				else {						spnAdditionalRecipients1.style.display = "none";						spnAdditionalRecipients3.style.display = "none";				}				ShowHideEmailNotificationCheckBox();			}						function ChangePersonalMessage(){				var txtPersonalMessage = document.getElementById('personalMessage');				if (txtPersonalMessage != null ){					if (txtPersonalMessage.value == '1') 						txtPersonalMessage.value = '0';					else 						txtPersonalMessage.value = '1';				}								personalMessage();											}            function personalMessage()			{				var spnPersonalMessage = document.getElementById('trPersonalMessage');				var txtPersonalMessage = document.getElementById('personalMessage');				if (txtPersonalMessage != null && txtPersonalMessage.value == '1') {						spnPersonalMessage.style.display = "";				}				else {						spnPersonalMessage.style.display = "none";				}			}					function ChangeDdlSelectedIndex(control, value) 		{			var list = document.getElementById(control);						if(!list)				return;							for (i = 0; i < list.options.length; i++) {				if (list.options[i].value == value)					list.options[i].selected = "selected";				else					list.options[i].selected = "";			}		}				function ShowHideEmailNotificationCheckBox()		{			var serviceType = FindControl('ddlPackageShippingDetailServiceType').value;				var isSmartPostDeliveryEnabled = FindControl('hdnSmartPostDeliveryConfirmation').value;			var isTenderedEmailAllowed = FindControl('hdnIsTenderedEmailAllowed').value;						if(serviceType == '16' || serviceType == '17' || serviceType == '18' || serviceType == '19' || serviceType == '-1'){	        	Hide('tdExceptionNotification');	        	Hide('tdEmailNotificationsSenderException');	            Hide('tdEmailNotificationsRecipientException');	            Hide('tdEmailNotificationsOther1Exception');	            Hide('tdEmailNotificationsOther2Exception');	            UnCheckTheCheckBox('rdbEmailNotificationsSenderException');	            UnCheckTheCheckBox('rdbEmailNotificationsRecipientException');	            UnCheckTheCheckBox('rdbEmailNotificationsOther1Exception');	            UnCheckTheCheckBox('rdbEmailNotificationsOther2Exception');	            	            if(isSmartPostDeliveryEnabled != '1'){	            	Hide('tdDeliveryNotification');		        	Hide('tdEmailNotificationsSenderDelivery');		            Hide('tdEmailNotificationsRecipientDelivery');		            Hide('tdEmailNotificationsOther1Delivery');		            Hide('tdEmailNotificationsOther2Delivery');		            UnCheckTheCheckBox('rdbEmailNotificationsSenderDelivery');		            UnCheckTheCheckBox('rdbEmailNotificationsRecipientDelivery');		            UnCheckTheCheckBox('rdbEmailNotificationsOther1Delivery');		            UnCheckTheCheckBox('rdbEmailNotificationsOther2Delivery');	            }else{	            	Show('tdDeliveryNotification');		        	Show('tdEmailNotificationsSenderDelivery');		            Show('tdEmailNotificationsRecipientDelivery');		            Show('tdEmailNotificationsOther1Delivery');		            Show('tdEmailNotificationsOther2Delivery');	            }	            	            if(isTenderedEmailAllowed == 'true'){					Hide('tdShipNotification');	        		Hide('tdEmailNotificationsSenderShip');	        		Hide('tdEmailNotificationsRecipientShip');	        		Hide('tdEmailNotificationsOther1Ship');	        		Hide('tdEmailNotificationsOther2Ship');		        	UnCheckTheCheckBox('rdbEmailNotificationsSenderShip');		        	UnCheckTheCheckBox('rdbEmailNotificationsRecipientShip');	        		UnCheckTheCheckBox('rdbEmailNotificationsOther1Ship');	        		UnCheckTheCheckBox('rdbEmailNotificationsOther2Ship');	        			        		Show('tdTenderedNotification');	        		Show('tdEmailNotificationsSenderTendered');	        		Show('tdEmailNotificationsRecipientTendered');	        		Show('tdEmailNotificationsOther1Tendered');	        		Show('tdEmailNotificationsOther2Tendered');				}else{					Show('tdShipNotification');		        	Show('tdEmailNotificationsSenderShip');		        	Show('tdEmailNotificationsRecipientShip');	        		Show('tdEmailNotificationsOther1Ship');	        		Show('tdEmailNotificationsOther2Ship');	        			        		Hide('tdTenderedNotification');	        		Hide('tdEmailNotificationsSenderTendered');	        		Hide('tdEmailNotificationsRecipientTendered');	        		Hide('tdEmailNotificationsOther1Tendered');	        		Hide('tdEmailNotificationsOther2Tendered');	        			        		UnCheckTheCheckBox('rdbEmailNotificationsSenderTendered');	        		UnCheckTheCheckBox('rdbEmailNotificationsRecipientTendered');	        		UnCheckTheCheckBox('rdbEmailNotificationsOther1Tendered');	        		UnCheckTheCheckBox('rdbEmailNotificationsOther2Tendered');				} 	            	        }else{	        	Show('tdExceptionNotification');	        	Show('tdEmailNotificationsSenderException');	            Show('tdEmailNotificationsRecipientException');	            Show('tdEmailNotificationsOther1Exception');	            Show('tdEmailNotificationsOther2Exception');	            	            Show('tdDeliveryNotification');		        Show('tdEmailNotificationsSenderDelivery');		        Show('tdEmailNotificationsRecipientDelivery');		        Show('tdEmailNotificationsOther1Delivery');		        Show('tdEmailNotificationsOther2Delivery');			        		        if(isTenderedEmailAllowed == 'true'){					Show('tdShipNotification');	        		Show('tdEmailNotificationsSenderShip');	        		Show('tdEmailNotificationsRecipientShip');	        		Show('tdEmailNotificationsOther1Ship');	        		Show('tdEmailNotificationsOther2Ship');	        			        		Show('tdTenderedNotification');	        		Show('tdEmailNotificationsSenderTendered');	        		Show('tdEmailNotificationsRecipientTendered');	        		Show('tdEmailNotificationsOther1Tendered');	        		Show('tdEmailNotificationsOther2Tendered');				}else{					Show('tdShipNotification');		        	Show('tdEmailNotificationsSenderShip');		        	Show('tdEmailNotificationsRecipientShip');	        		Show('tdEmailNotificationsOther1Ship');	        		Show('tdEmailNotificationsOther2Ship');	        			        		Hide('tdTenderedNotification');	        		Hide('tdEmailNotificationsSenderTendered');	        		Hide('tdEmailNotificationsRecipientTendered');	        		Hide('tdEmailNotificationsOther1Tendered');	        		Hide('tdEmailNotificationsOther2Tendered');	        			        		UnCheckTheCheckBox('rdbEmailNotificationsSenderTendered');	        		UnCheckTheCheckBox('rdbEmailNotificationsRecipientTendered');	        		UnCheckTheCheckBox('rdbEmailNotificationsOther1Tendered');	        		UnCheckTheCheckBox('rdbEmailNotificationsOther2Tendered');				}         	        }		}		function ShowHideBillDutiesAndTaxes() {    		var originCntrySelected = getOriginCountrySelected(originCntryCodeChecked,originCntryCode);    		var recipientCntrySelected = getRecipientCountrySelected(recipientCntryCodeChecked,recipientCntryCode);	    	if (isBillDutiesAndTaxesAllowed(originCntrySelected,recipientCntrySelected)){	    		Show('trBillingDutiesAndTaxesTo'); 	    		Show('trBillingDutiesAndTaxesAccount');	    		Show('trBillingDutiesAndTaxesCountry'); 			}else {				hideDutiesAndTaxesFields();			}	    	}		function ShowHideGoodsNotInFreeCirculation()	{		var originCntrySelected = getOriginCountrySelected(originCntryCodeChecked,originCntryCode);    	var recipientCntrySelected = getRecipientCountrySelected(recipientCntryCodeChecked,recipientCntryCode);		var isOriginInEU = isEUCountry(originCntrySelected);		var isRecipientInEU = isEUCountry(recipientCntrySelected);		var isServiceTypeAllowed = isGoodsFreeCirculationAllowedServiceType();		if (isServiceTypeAllowed && isOriginNotSameAsRecipient(originCntrySelected,recipientCntrySelected) && isOriginInEU && isRecipientInEU)		{			Show('trGoodsNotInFreeCirculation');		}else{			Hide('trGoodsNotInFreeCirculation');			UnCheckTheCheckBox('chkFreeCirculation');		}			}		function ShowHideGenerateCustoms() {        	var originCntrySelected = getOriginCountrySelected(originCntryCodeChecked,originCntryCode);        	var recipientCntrySelected = getRecipientCountrySelected(recipientCntryCodeChecked,recipientCntryCode);    	    var isCustomsSelected = 'true';    	    var isGroundSelected = isFedExInternationalGroundSelected();    	            	            	    if (isGroundSelected && isOriginNotSameAsRecipient(originCntrySelected,recipientCntrySelected) && isCustomsSelected == 'true')  {    	        Show('trCustomsDoc');    	    }else {    	        Hide('trCustomsDoc');    	        UnCheckTheCheckBox('chkCustomsDoc');    	    }    	    var isCustomsGenerationHidden = IsHidden('trCustomsDoc');     	    var isCustomsGenerationShown = !IsHidden('trCustomsDoc');    	    if((isGroundSelected && isOriginNotSameAsRecipient(originCntrySelected,recipientCntrySelected) && (isCustomsGenerationHidden || (isCustomsGenerationShown && !IsCheckboxChecked('chkCustomsDoc') ))) || (isServiceTypeValidForDisplay() && isOriginAndRecipientUnitedArabEmirates(originCntryCodeChecked, originCntryCode, recipientCntryCodeChecked,recipientCntryCode))) {    			Show('trCustomsValue');    		}else {    			Hide('trCustomsValue');    		}       	 }			   function getOriginCountrySelected(originCntryChecked, originCntryCode) {	            	    	var isOriginCntryUserEntry				=  originCntryChecked;	    	var originCountryCode					=  null;	        var ddlCountry 							=  FindControl('ddlCountry');	    		 	    	if (isOriginCntryUserEntry  == 'true')  {	    	   	if (ddlCountry != null && ddlCountry.value != '-1') {	    	   	   originCountryCode = GetDdlSelectedValue('ddlCountry');	    	   	 }	    	}else {	    			originCountryCode = originCntryCode;	    	}	        return originCountryCode;	    }		    function getRecipientCountrySelected(recipientCntryChecked,recipientCntryCode) {	        	        var isRecipientCntryUserEntry				=  recipientCntryChecked;	        var recipientCountryCode					=  null ;	    	var ddlRecipientCountry 					=  FindControl('ddlRecipientCountry');		        if (isRecipientCntryUserEntry  == 'true')  {	       	    if (ddlRecipientCountry != null && ddlRecipientCountry.value != '-1') {	       	    	recipientCountryCode = GetDdlSelectedValue('ddlRecipientCountry');	       	    }	       	     	    	} else {	    			recipientCountryCode = recipientCntryCode;	    	}	    	return recipientCountryCode;	    }		    function isOriginNotSameAsRecipient(originCntry,recipientCntry) {	    	   return (originCntry !=  null && recipientCntry != null && originCntry != recipientCntry);	    }		    function isOriginSameAsRecipient(originCntry,recipientCntry) {	    	   return (originCntry !=  null && recipientCntry != null && originCntry == recipientCntry);	    }	    function isCntryNotUSAorCAorMX(cntry){	    	return (cntry != null && cntry != 'US' && cntry != 'MX' && cntry != 'CA');	    }	    function isFedExGroundSelected()	    {  	        var serviceTypeValue = GetDdlSelectedValue('ddlPackageShippingDetailServiceType');	        if(serviceTypeValue == 6)	       	{	     		return true;							         }	         return false;	    }   	    function isFedExInternationalGroundSelected()	    {  	        var serviceTypeValue = GetDdlSelectedValue('ddlPackageShippingDetailServiceType');	        if(serviceTypeValue == 22)	       	{	     		return true;							         }	         return false;	    }   		    function isEUCountry(country)	    {	    	var euCountriesList =   ['AT','BE','BG','CY','CZ','DL','EE','FI','FR','DE','GR','HU','IE','IT','LV','LT','LU','MT','NL','PL','RO','SK','SI','ES','SE','GB'];	    	for(var i = 0; i<euCountriesList.length ; i++)	    	{	    		var euCountry  = null;	    		if (country != null && country == euCountriesList[i]){	    			euCountry = euCountriesList[i];	    			break;	    		}	    	}	    	if (euCountry != null)	    		return true;	    		    	return false;	    }	    function isGoodsFreeCirculationAllowedServiceType() {	   	 var serviceTypeValue = GetDdlSelectedValue('ddlPackageShippingDetailServiceType');	   	 if(serviceTypeValue == 0 || serviceTypeValue == 10 || serviceTypeValue == 11 || serviceTypeValue == 8)	   	 {	   	      return true;							   	 }	   	 return false;	   }	   function isBillDutiesAndTaxesAllowed(originCntry,recipientCntry){	   	if (originCntry != null && recipientCntry != null && isOriginNotSameAsRecipient(originCntry,recipientCntry)){	   		 if (isServiceTypeValidForDisplay()){	     			 		return true;	   		 }	   		 return false;	   	}	   	return false;	   }	   function hideDutiesAndTaxesFields(){	   	Hide('trBillingDutiesAndTaxesTo'); 	   	Hide('trBillingDutiesAndTaxesAccount');	   	Hide('trBillingDutiesAndTaxesCountry');	   	ChangeDdlSelectedIndex('ddlBillDutiesAndTaxesTo', '-1');		ChangeDdlSelectedIndex('ddlBillingDutiesAndCountry', '-1'); 	   }	   function SetContinueButton() {	       FindControl('btnGenerateLabel').value = "Continue";	   }	    function SetGenerateLabel() {	       	FindControl('btnGenerateLabel').value = "Generate Label";	   }	   function IsGroundCustomsChecked(originCode, recipientCode) {	   	 var isGroundSelected = isFedExGroundSelected();	   	 var isCustomsGenerationShown = !IsHidden('trCustomsDoc');	   	 if(isGroundSelected && isOriginNotSameAsRecipient(originCode,recipientCode) && (isCustomsGenerationShown && IsCheckboxChecked('chkCustomsDoc') )) {	   		return true;	   	  }	   	return false;  	    }	   function IsShown(control)	   {	   	var obj = FindControl(control);	   	if (obj != null && obj.style.display == "") 	   		return true;	   	else 	   		return false;	   }	   function isServiceTypeDisplayedAndNotSelected(){			 var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');			 if (IsShown('ddlPackageShippingDetailServiceType') && ddlServiceType.value == '-1')				 return true;			return false;		}		function isServiceTypeValidForDisplay(){			if (isServiceTypeDisplayedAndNotSelected() || (IsShown('ddlPackageShippingDetailServiceType') && isServiceTypeAllowedForTaxId()) || (IsHidden('ddlPackageShippingDetailServiceType') && isServiceTypeAllowedForTaxId()))				return true;			return false;		}	   function isServiceTypeAllowedForTaxId(){			 var ddlServiceType = FindControl('ddlPackageShippingDetailServiceType');			 var isServiceTypeSelected = false;			 var allowedServiceType = true;		           					for (var i=0; i<ddlServiceType.length; i++)			{			   	if(ddlServiceType.options[i].selected == true)			   	{				   		isServiceTypeSelected = true;			   		var serviceTypeValue = ddlServiceType.options[i].value;		   							   	if (serviceTypeValue == 16 || serviceTypeValue == 17 || serviceTypeValue == 19) {				   		allowedServiceType = false;				   	}			   	   break;				   	}			}				 			if(!isServiceTypeSelected){				return false;				}else {				if(allowedServiceType)					return true;				else					return false;			}		}		  function ShowHideSenderTaxId(){			   var originCntrySelected = getOriginCountrySelected(originCntryCodeChecked,originCntryCode);	       	   var recipientCntrySelected = getRecipientCountrySelected(recipientCntryCodeChecked,recipientCntryCode);			    if (senderTaxIdUserEntry == 'true' && isOriginNotSameAsRecipient(originCntrySelected,recipientCntrySelected) && isServiceTypeAllowedForTaxId()){				    Show('trSenderTaxId');			    }else if ((senderTaxIdUserEntry == 'true' || (senderTaxIdUserEntry == 'false' && senderTaxId == 'null')) && isOriginAndRecipientBrazil(originCntryCodeChecked,originCntryCode,recipientCntryCodeChecked,recipientCntryCode) && isIntraBrazilShippingAllowed == 'true' && isServiceTypeValidForDisplay()) {			    	Show('trSenderTaxId');			    }else{				    Hide('trSenderTaxId');			    }				    		  }		  function ShowHideSenderIE(senderIEUserEntry,senderIE, originCntryCodeChecked,originCntryCode,recipientCntryCodeChecked,recipientCntryCode,isIntraBrazilShippingAllowed ){				if ((senderIEUserEntry == 'true' || (senderIEUserEntry == 'false' && senderIE == 'null')) && isOriginAndRecipientBrazil(originCntryCodeChecked,originCntryCode,recipientCntryCodeChecked,recipientCntryCode) && isIntraBrazilShippingAllowed == 'true' && isServiceTypeValidForDisplay()) {			    	Show('trSenderIE');			    }else{				    Hide('trSenderIE');			    }		  }		 function isOriginAndRecipientBrazil(originCntryChecked, originCntryCode, recipientCntryChecked,recipientCntryCode) {			   var originCntry  = getOriginCountrySelected(originCntryChecked,originCntryCode);			   var recipientCntry = getRecipientCountrySelected(recipientCntryChecked, recipientCntryCode);			   return (originCntry !=  null && recipientCntry != null && originCntry == 'BR' && recipientCntry == 'BR');		}		 function isOriginAndRecipientColombia(originCntryChecked, originCntryCode, recipientCntryChecked,recipientCntryCode) {			   var originCntry  = getOriginCountrySelected(originCntryChecked,originCntryCode);			   var recipientCntry = getRecipientCountrySelected(recipientCntryChecked, recipientCntryCode);			   return (originCntry !=  null && recipientCntry != null && originCntry == 'CO' && recipientCntry == 'CO');		}		function ShowHideRecipientTaxId(){				var originCntrySelected = getOriginCountrySelected(originCntryCodeChecked,originCntryCode);	        	var recipientCntrySelected = getRecipientCountrySelected(recipientCntryCodeChecked,recipientCntryCode);			    if (recipientTaxIdUserEntry == 'true' && isOriginNotSameAsRecipient(originCntrySelected,recipientCntrySelected) && isServiceTypeAllowedForTaxId()){				    Show('trRecipientTaxId');			    }else if ((recipientTaxIdUserEntry == 'true' || (recipientTaxIdUserEntry == 'false' && recipientTaxId == 'null')) && isOriginAndRecipientBrazil(originCntryCodeChecked,originCntryCode,recipientCntryCodeChecked,recipientCntryCode) && isIntraBrazilShippingAllowed == 'true' && isServiceTypeValidForDisplay()) {			    	Show('trRecipientTaxId');			    }else{				    Hide('trRecipientTaxId');			    }				    		}		 function ShowHideRecipientIE(recipientIEUserEntry,recipientIE, originCntryCodeChecked,originCntryCode,recipientCntryCodeChecked,recipientCntryCode,isIntraBrazilShippingAllowed ){				if ((recipientIEUserEntry == 'true' || (recipientIEUserEntry == 'false' && recipientIE == 'null')) && isOriginAndRecipientBrazil(originCntryCodeChecked,originCntryCode,recipientCntryCodeChecked,recipientCntryCode) && isIntraBrazilShippingAllowed == 'true' && isServiceTypeValidForDisplay()) {			    	Show('trRecipientIE');			    }else{				    Hide('trRecipientIE');			    }		}		function isOriginAndRecipientUnitedArabEmirates(originCntryChecked, originCntryCode, recipientCntryChecked,recipientCntryCode) {			   var originCntry  = getOriginCountrySelected(originCntryChecked,originCntryCode);			   var recipientCntry = getRecipientCountrySelected(recipientCntryChecked, recipientCntryCode);			   return (originCntry !=  null && recipientCntry != null && originCntry == 'AE' && recipientCntry == 'AE');		}		function isOriginAndRecipientSwitzerland(originCntryChecked, originCntryCode, recipientCntryChecked,recipientCntryCode) {			   var originCntry  = getOriginCountrySelected(originCntryChecked,originCntryCode);			   var recipientCntry = getRecipientCountrySelected(recipientCntryChecked, recipientCntryCode);			   return (originCntry !=  null && recipientCntry != null && originCntry == 'CH' && recipientCntry == 'CH');		}		 function ShowHidePackageContents(originCntryCodeChecked,originCntryCode,recipientCntryCodeChecked,recipientCntryCode){				if (isServiceTypeValidForDisplay() && isOriginAndRecipientUnitedArabEmirates(originCntryCodeChecked, originCntryCode, recipientCntryCodeChecked,recipientCntryCode)){					Show('trPackageContents');				}else{					Hide('trPackageContents');				}		}		function ShowHideItemDescription(originCntryChecked,originCntryCode,recipientCntryChecked,recipientCntryCode){				if (isServiceTypeValidForDisplay() && (isOriginAndRecipientUnitedArabEmirates(originCntryChecked, originCntryCode, recipientCntryChecked,recipientCntryCode) || isOriginAndRecipientSwitzerland(originCntryChecked, originCntryCode, recipientCntryChecked,recipientCntryCode)  || isOriginAndRecipientColombia(originCntryChecked, originCntryCode, recipientCntryChecked,recipientCntryCode) || isOriginAndRecipientBrazil(originCntryChecked, originCntryCode, recipientCntryChecked,recipientCntryCode))){					Show('trItemDescription');				}else{					Hide('trItemDescription');				}								var itemDescription = FindControl('tdItemDescription');				if (itemDescription != null && IsShown('tdItemDescription')){					if ( isOriginAndRecipientUnitedArabEmirates(originCntryChecked, originCntryCode, recipientCntryChecked,recipientCntryCode)){						itemDescription.innerHTML = "*Item description";					}else{						itemDescription.innerHTML = "Item description";					}				}							}		function CheckAvailabilityReferences(){			if (commercialInvoiceUserEntry == 'true' && isServiceTypeValidForDisplay() && isOriginAndRecipientColombia(originCntryCodeChecked, originCntryCode, recipientCntryCodeChecked, recipientCntryCode)){				Show('trReferenceCommercialInvoiceNo');			}else {				Hide('trReferenceCommercialInvoiceNo');			}					if (isServiceTypeValidForDisplay() && isIntraBrazilShippingAllowed == 'true' && isOriginAndRecipientBrazil(originCntryCodeChecked, originCntryCode, recipientCntryCodeChecked, recipientCntryCode)){				Show('trReferenceNotaFiscal');			}else {				Hide('trReferenceNotaFiscal');			}		}		function updateScreenWithCountryChange(){			ShowHideSenderTaxId();			ShowHideRecipientTaxId();			SetSenderTaxIdLabel();			SetRecipientTaxIdLabel();			ShowHideSenderIE(senderIEUserEntry,senderIE, originCntryCodeChecked,originCntryCode,recipientCntryCodeChecked,recipientCntryCode,isIntraBrazilShippingAllowed);			ShowHideRecipientIE(recipientIEUserEntry,recipientIE, originCntryCodeChecked,originCntryCode,recipientCntryCodeChecked,recipientCntryCode,isIntraBrazilShippingAllowed);			CheckAvailabilityReferences();			ShowHidePackageContents(originCntryCodeChecked,originCntryCode,recipientCntryCodeChecked,recipientCntryCode);			ShowHideItemDescription(originCntryCodeChecked,originCntryCode,recipientCntryCodeChecked,recipientCntryCode);		}		        function ShowHideTable()          {         	var originCountry = 'US';         	if (originCountry != 'null'){        		ChangeDdlSelectedIndex('ddlCountry', originCountry);        		Hide("trOriginCountry");        		        	}        		        	var recipientCountry = 'null';        	if (recipientCountry != 'null'){        		ChangeDdlSelectedIndex('ddlRecipientCountry', recipientCountry);        		Hide("trRecipientCountry");        	}        	var selectedServiceType = 'null';			if (selectedServiceType != 'null')					ChangeDdlSelectedIndex('ddlPackageShippingDetailServiceType', selectedServiceType);								var selectedPackagingType = 'null';			if (selectedPackagingType != 'null')				ChangeDdlSelectedIndex('ddlPackageShippingDetailPackageType', selectedPackagingType);        	        		        	SetStateVisibility();        	SetRecipientStateVisibility();        	ShowSmartPostNote();        	        	if (true){	    	    SetSpecialServices();	    	    CheckSignatureOption();				CheckHomeDeliveryPremium();				CheckDryIce();				CheckDangerousGoods();				CheckSQE();				CheckORMD();        		CheckLithiumBattery();        		CheckHazMat(); 			} 							ShowDeclaredValues();						EnableDisablePackages(); 			ShowHideEmailNotificationCheckBox();			ShowHideGenerateCustoms();			ShowHideGoodsNotInFreeCirculation();			ShowHideBillDutiesAndTaxes();			updateScreenWithCountryChange();			SetSubmitButtonLabel();        }                  function ShowDeclaredValues(){        	var packagePieceCount = '1' ;   			   			if (packagePieceCount == '1'){   				Show('trDeclaredValue1');   			}    			else if (packagePieceCount == '2'){   				Show('trDeclaredValue1');   				Show('trDeclaredValue2');   			}   			else if (packagePieceCount == '3'){   				Show('trDeclaredValue1');   				Show('trDeclaredValue2');   				Show('trDeclaredValue3');   			}   			else if (packagePieceCount == '4'){   				Show('trDeclaredValue1');   				Show('trDeclaredValue2');   				Show('trDeclaredValue3');   				Show('trDeclaredValue4');   			}   			else if (packagePieceCount == '5'){   				Show('trDeclaredValue1');   				Show('trDeclaredValue2');   				Show('trDeclaredValue3');   				Show('trDeclaredValue4');   				Show('trDeclaredValue5');   			}	        }         function ShowSmartPostNote(){        	var serviceType = FindControl('ddlPackageShippingDetailServiceType').value;	        	if (serviceType == 16 || serviceType == 17 || serviceType == 18 || serviceType == 19){            	Show('trSmartPostNote');        	}        	Hide('trSmartPostNote');        }            
</script>    
<style type="text/css">        .style1        {            width: 65%;        }        .style2        {            width: 5px;        }        .style3        {            width: 30%;        }    
</style></head>
    
<body  style="width: 695px;" onLoad="ShowHideTable()">
<form name='mainForm' method='post' target="_blank" action='https://www.fedex.com/FWIW/Client/Shipping_v5/ClientShippingStandAloneInit' id='mainForm'>
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
* Denotes required field</td>
</tr>
				<tr>
			<!-- begin tdMain -->
			<td valign="top" id="tdMain">
				<table>
					<tr>
									<!-- begin tblOrigin -->
	        <td >
	        	        				<table id="tblShippingOrigin" border="0" cellpadding="3" cellspacing="0" width="100%" style='border: #660099 1px solid; font-family:Arial; text-align: left; color:Black; background-color:Whitesmoke; font-size:11px;'>
				<tr>
					<td colspan="3"						style="font-weight: bold; color: white; background-color: #660099; height: 20px;">
					&nbsp;Sender Information</td>
				</tr>
								 <!-- end trOriginContactName -->
				<!-- begin trOriginContactName_NoEdit -->
				<tr>
					<td style="width: 34%;">
&nbsp;<span id="lblContactName">
*Contact name</span>
</td>
					<td style="width: 1%;">
</td>
																			<td style="width: 65%">
<span id="txtContactName">
Glenn Beeman<!-- end txtContactName -->
</span>
					</td>
				</tr>
				<!-- end trOriginContactName_NoEdit -->
				 <!-- end trOriginCompany -->
				<!-- begin trOriginCompany_NoEdit -->
				<tr>
					<td style="width: 34%;">
&nbsp;<span id="lblCompany">
Company</span>
</td>
					<td style="width: 1%;">
</td>
																			<td style="width: 65%">
<span id="txtCompany">
Harvard University<!-- end txtCompany -->
</span>
					</td>
				</tr>
				<!-- end trOriginCompany_NoEdit -->
								 <!-- end trOriginAddress1 -->
				<!-- begin trOriginAddress1_NoEdit -->
				<tr>
					<td style="width: 34%;">
&nbsp;<span id="lblAddress1">
*Address 1</span>
</td>
					<td style="width: 1%;">
</td>
																			<td style="width: 65%">
<span id="txtAddress1">
240 Longwood Ave.<!-- end txtAddress1 -->
</span>
					</td>
				</tr>
				<!-- end trOriginAddress1_NoEdit -->
								 <!-- end trOriginAddress2 -->
				<!-- begin trOriginAddress2_NoEdit -->
				<tr>
					<td style="width: 34%;">
&nbsp;<span id="lblAddress2">
Address 2</span>
</td>
					<td style="width: 1%;">
</td>
																			<td style="width: 65%">
<span id="txtAddress2">
LHRRB 212<!-- end txtAddress2 -->
</span>
					</td>
				</tr>
				<!-- end trOriginAddress2_NoEdit -->
												<tr id = "trOriginCountry">
					<td style="width: 34%;">
&nbsp;<span id="lblCountry">
*Country</span>
</td>
						<td style="width: 1%;">
</td>
																		<td style="width: 65%">
<select name="ddlCountry" id="ddlCountry" class="dropdown" onchange="SetStateVisibility();SetRecipientStateVisibility();updateScreenWithCountryChange();" style='width:180px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >
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
				<!-- begin trOriginCountry_NoEdit -->
				<tr id="trCountry_NonEdit">
					<td style="width: 34%;">
&nbsp;<span id="lblCountry">
*Country</span>
</td>
					<td style="width: 1%;">
</td>
																			<td style="width: 65%">
<span id="txtCountry">
U.S.A.<!-- end txtCountry -->
</span>
					</td>
				</tr>
				<!-- end trOriginCountry_NoEdit -->
												 <!-- end trOriginCity -->
				<!-- begin trOriginCity_NoEdit -->
				<tr>
					<td style="width: 34%;">
&nbsp;<span id="lblCity">
*City</span>
</td>
					<td style="width: 1%;">
</td>
																			<td style="width: 65%">
<span id="txtCity">
Boston<!-- end txtCity -->
</span>
					</td>
				</tr>
				<!-- end trOriginCity_NoEdit -->
				 <!-- end trOriginStateOrProvinceCode -->
				<!-- begin trOriginStateOrProvinceCode_NoEdit -->
				<tr>
					<td style="width: 34%;">
&nbsp;<span id="lblStateOrProvince">
*State/province</span>
</td>
					<td style="width: 1%;">
</td>
																			<td style="width: 65%">
<span id="txtStates">
Massachusetts<!-- end txtStates -->
</span>
					</td>
				</tr>
				<!-- end trOriginStateOrProvinceCode_NoEdit -->
								 <!-- end trOriginPostalCode -->
				<!-- begin trOriginPostalCode_NoEdit -->
				<tr>
					<td style="width: 34%;">
&nbsp;<span id="lblPostalCode">
*Postal code</span>
</td>
					<td style="width: 1%;">
</td>
																			<td style="width: 65%">
<span id="txtPostalCode">
02115<!-- end txtPostalCode -->
</span>
					</td>
				</tr>
				<!-- end trOriginPostalCode_NoEdit -->
								 <!-- end trOriginTelephoneCode -->
				<!-- begin trOriginTelephoneCode_NoEdit -->
				<tr>
					<td style="width: 34%;">
&nbsp;<span id="lblTelephone">
*Phone no.</span>
</td>
					<td style="width: 1%;">
</td>
																			<td style="width: 65%">
<span id="txtTelephone">
617-432-7708<!-- end txtTelephone -->
</span>
					</td>
				</tr>
				<!-- end trOriginTelephoneCode_NoEdit -->
				 <!-- end trSenderTaxId -->
				 <!-- end trSenderTaxId_NoEdit -->
								 <!-- end trSenderIE -->
			</table>
				      </td>
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
<input name="txtRecipientContactName" value="<bean:write name="address" property="name"/>" type="text" maxlength="35" id="txtRecipientContactName" style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
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
<input name="txtRecipientCompanyName" value="<bean:write name="address" property="organization"/>" type="text" maxlength="35" id="txtRecipientCompanyName" style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
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
<select name="ddlRecipientCountry" id="ddlRecipientCountry" class="dropdown" onChange="SetStateVisibility();SetRecipientStateVisibility();updateScreenWithCountryChange();" style='width: 180px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >
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
<input name="txtRecipientAddress1" value="<bean:write name="address" property="addressline1"/>" type="text" maxlength="35" id="txtRecipientAddress1" style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
					</td>
				</tr>
				<!-- end trAddress1 -->
								<!-- begin trAddress2 -->
				<tr>
					<td style="width: 34%;">
&nbsp;<span id="lblRecipientAddress2">
Address 2</span>
</td>
					<td style="width: 1%;">
</td>
					<td style="width: 65%">
<input name="txtRecipientAddress2" value="<bean:write name="address" property="addressline2"/>" type="text" maxlength="35" id="txtRecipientAddress2" style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
					</td>
				</tr>
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
<input name="txtRecipientCity"	value="<bean:write name="address" property="city"/>" type="text" maxlength="35" id="txtRecipientCity" style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
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
						 <!-- begin ddlRecipientBrazilStates -->
							<select name="ddlRecipientBrazilStates" id="ddlRecipientBrazilStates" class="dropdown" style='width:180px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black; '>
    <logic:present name="state">
        <option  value="<bean:write name="state" property="code"/>" selected="true"/><bean:write name="state" property="name"/>
    </logic:present>
    <logic:notPresent name="state">
        <option value="-1">Select</option>    
    </logic:notPresent>
								<option value='AC'>
Acre</option>
  								 <option value='AL'>
Alagoas</option>
  								 <option value='AP'>
Amap&#225</option>
  								 <option value='AM'>
Amazonas</option>
  								 <option value='BA'>
Bahia</option>
  								 <option value='CE'>
Cear&#225</option>
  								 <option value='DF'>
Distrito Federal</option>
  								 <option value='ES'>
Esp&#237rito Santo</option>
  								 <option value='GO'>
Goi&#225s</option>
  								 <option value='MA'>
Maranh&#227o</option>
  								 <option value='MT'>
Mato Grosso</option>
  								 <option value='MS'>
Mato Grosso do Sul</option>
  								 <option value='MG'>
Minas Gerais</option>
  								 <option value='PA'>
Par&#225</option>
  								 <option value='PB'>
Para&#237ba</option>
  								 <option value='PR'>
Paran&#225</option>
  								 <option value='PE'>
Pernambuco</option>
  								 <option value='PI'>
Piau&#237</option>
  								 <option value='RJ'>
Rio de Janeiro</option>
  								 <option value='RN'>
Rio Grande do Norte</option>
  								 <option value='RS'>
Rio Grande do Sul</option>
  								 <option value='RO'>
Rond&#244nia</option>
  								 <option value='RR'>
Roraima</option>
  								 <option value='SC'>
Santa Catarina</option>
  								 <option value='SP'>
S&#227o Paulo</option>
  								 <option value='SE'>
Sergipe</option>
  								 <option value='TO'>
Tocantins</option>
  							</select>
 							<!-- end ddlRecipientBrazilStates -->
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
<input name="txtRecipientPostalCode"	value="<bean:write name="address" property="zipcode"/>" type="text" maxlength="16" id="txtRecipientPostalCode" style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
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
<input name="txtRecipientTelephone" value="<bean:write name="address" property="phone"/>" type="text" maxlength="15" id="txtRecipientTelephone" style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;'/>
					</td>
				</tr>
				<!-- end trTelephone -->
								<!-- begin trRecipientTaxId -->
				<tr id="trRecipientTaxId">
					<td id="tdRecipientTaxId" style="width: 34%;">
&nbsp;<span id="lblRecipientTaxId">
Recipient tax<br>
&nbsp;ID no.</span>
					<img src="https://www.fedex.com/FWIW/Client/Images/Common/appModuleHelpIcon.gif"  title="Examples of Tax ID numbers are Internal Revenue Service (IRS) Employer Identification Number (EIN), Value Added Tax (VAT) Number, and Economic Operators Registration and Identification Number (EORI)."/>
</td>
						<td style="width: 1%;">
</td>
																		<td style="width: 65%">
<input name="txtRecipientTaxId"						type="text" maxlength="18" id="txtRecipientTaxId" style='width:180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
					</td>
				</tr>
				<!-- end trRecipientTaxId -->
								<!-- begin trRecipientIE -->
				<tr id="trRecipientIE">
					<td style="width: 34%;">
&nbsp;<span id="lblRecipientIE">
*Recipient IE</span>
					<img src="https://www.fedex.com/FWIW/Client/Images/Common/appModuleHelpIcon.gif"  title="The inscriýýo estadual (IE) is the Brazil state tax ID number."/>
</td>
						<td style="width: 1%;">
</td>
																		<td style="width: 65%">
<input name="txtRecipientIE"						type="text" maxlength="12" id="txtRecipientIE" style='width:180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
					</td>
				</tr>
				<!-- end trRecipientIE -->
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
					        			<option value="1" selected>
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
<input name="txtBillingDetailAccountNumber" value="325074027" type="text" maxlength="9" id="txtBillingDetailAccountNumber" style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
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
									<option value="US" selected>
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
														<tr id="trReferenceCommercialInvoiceNo">
								<td style="width: 34%;" nowrap="nowrap">
&nbsp;<span id="lblReferenceCommercialInvoiceNo">
Commercial invoice no.</span>
</td>
								<td style="width: 1%;">
</td>
								<td style="width: 65%">
<input name="txtReferenceCommercialInvoiceNo" type="text" maxlength="30" id="txtReferenceCommercialInvoiceNo" 									style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
								</td>
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
<input name="txtReferenceYourReference" value="<bean:write name="order" property="orderid"/>" type="text" maxlength="30" id="txtReferenceYourReference" 									style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
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
<input name="txtReferenceInvoiceNo" value="DFHCC_<bean:write name="order" property="orderid"/>" type="text" maxlength="30" id="txtReferenceInvoiceNo" 									style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
								</td>
							</tr>
								<!-- end referenceInvoiceNbr -->
														<tr id="trReferenceNotaFiscal">
								<td style="width: 34%;">
&nbsp;<span id="lblReferenceNotaFiscal">
*Nota Fiscal/</br>
&nbsp;Declara&#231&#227o</span>
</td>
								<td style="width: 1%;">
</td>
								<td style="width: 65%">
<input name="txtReferenceNotaFiscal" type="text" maxlength="18" id="txtReferenceNotaFiscal" 									style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
								</td>
							</tr>
																<!-- begin referencePONbr -->
							<tr>
								<td style="width: 34%;">
&nbsp;<span id="lblReferencePoNo">
P.O. no.</span>
</td>
								<td style="width: 1%;">
</td>
								<td style="width: 65%">
<input name="txtReferencePoNo" value="<bean:write name="order" property="ponumber"/>" type="text" maxlength="30" id="txtReferencePoNo" 									style='width: 180px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
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
				<tr id="trServiceType">
					<td style="width: 49%;">
&nbsp;<span id="lblPackageShippingDetailServiceType">
*Service type</span>
</td>
					<td style="width: 1%;">
</td>
					<td style="width: 50%">
<select name="ddlPackageShippingDetailServiceType" class="dropdown" id="ddlPackageShippingDetailServiceType" onChange="ShowHideGenerateCustoms();ShowHideGoodsNotInFreeCirculation();SetSpecialServices();updateScreenWithCountryChange();" style='width:160px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >
							<option value="-1">
Select</option>
						  <option value='13' selected>
FedEx Priority Overnight&#174</option>
<option value='11'>
FedEx International Priority&#174</option>
						</select>
											</td>
				</tr>
				<tr id="trPackageType">
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
							<option value='3' selected>
FedEx&#174 Envelope</option>
<option value='6'>
Your Packaging</option>
						</select>
 											</td>
				</tr>
								<!-- begin trDropoffType -->
				<tr id="trDropoffType" style="display : none">
					<td style="width: 49%;">
&nbsp;<span id="lblDropoffType">
*Pickup/drop-off type</span>
</td>
					<td style="width: 1%;">
</td>
					<td style="width: 50%">
<select name="ddlDropoffType" id="ddlDropoffType" class="dropdown" style='width: 160px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >
							<option value="-1">
Select</option>
							<option value="0">
Regular pickup</option>
							<option value="1">
Will request courier</option>
							<option value="2" selected>
Drop box</option>
							<option value="3">
Business service center</option>
							<option value="4">
FedEx station</option>
						</select>
 											</td>
				</tr>
				<tr id="trSmartPostDropoffType" style="display : none">
					<td style="width: 49%;">
&nbsp;<span id="lblSmartPostDropoffType">
*Pickup/drop-off type</span>
</td>
					<td style="width: 1%;">
</td>
					<td style="width: 50%">
<select name="ddlSmartPostDropoffType" id="ddlSmartPostDropoffType" class="dropdown" style='width: 160px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >
							<option value="-1">
Select</option>
							<option value="0">
Regular pickup</option>
							<option value="1">
Will request courier</option>
							<option value="3">
Business service center</option>
							<option value="4">
FedEx station</option>
						</select>
 											</td>
				</tr>
				<!-- end trDropoffType -->
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
												 <!-- end trPackagesWeight -->
				 <!-- end trPackagesWeightUnit -->
								 <!-- end trHubId -->
				<tr id="trDeclaredValue1" style="display: none">
					<td style="width: 49%;">
&nbsp;<span id="lblPackageShippingDetailDeclaredValue1">
Declared value 1</span>
					<img src="https://www.fedex.com/FWIW/Client/Images/Common/appModuleHelpIcon.gif" title="The value of any package represents FedEx's maximum liability in connection with a shipment of that package.  A declared value greater than $100 will incur additional charges.  If you intend to purchase insurance from a third party and do not want to declare a value for carriage, please enter a value of 0."/>
</td>
					<td style="width: 1%;">
</td>
					<td style="width: 50%">
<input name="txtDeclaredValue1" type="text" maxlength="10" id="txtDeclaredValue1" style=' border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
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
<input name="txtDeclaredValue2" type="text" maxlength="10" id="txtDeclaredValue2" style='border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
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
<input name="txtDeclaredValue3" type="text" maxlength="10" id="txtDeclaredValue3" style='border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
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
<input name="txtDeclaredValue4" type="text" maxlength="10" id="txtDeclaredValue4" style='border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
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
<input name="txtDeclaredValue5" type="text" maxlength="10" id="txtDeclaredValue5" style='border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
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
<select name="ddlCurrencyType" id="ddlCurrencyType" class="dropdown"  					style='width:160px; border-color:Black; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >
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
												<option value="ILS">
Israel Shekels</option>
												<option value="JAD">
Jamaica Dollars</option>
												<option value="JYE">
Japan Yen</option>
												<option value="KZT">
Kazakhstan Tenge</option>
												<option value="KES">
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
												<option value="PAB">
Panama Balboas</option>
												<option value="PHP">
Philippines Pesos</option>
												<option value="PLN">
Poland Zloty</option>
												<option value="RUR">
Russia Rouble</option>
												<option value="SAR">
Saudi Arabia Riyals</option>
												<option value="SGD">
Singapore Dollars</option>
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
												<option value="UGX">
Uganda Shillings</option>
												<option value="GBP">
United Kingdom Pounds</option>
												<option value="USD">
United States Dollars</option>
												<option value="UYU">
Uruguay New Pesos</option>
												<option value="VEB">
Venezuela Bolivares Fuertes</option>
												<option value="WST">
Western Samoa Tala</option>
						</select>
					</td>
				</tr>
				<!-- end trCurrencyType -->
							<tr id="trGoodsNotInFreeCirculation" >
					<td style="width: 100%;" nowrap="nowrap">
<input  id="chkFreeCirculation" type="checkbox" onclick="SetSubmitButtonLabel();" name="chkGoodNotInFreeCirculation"/>
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
                		<option value="0" selected>
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
									<tr id="trPackageContents">
					<td style="width: 49%;">
&nbsp;<span id="lblPackageContents">
*Package contents</span>
</td>
					<td style="width: 1%">
</td>
					<td style="width: 50%">
							<input id="rdPackageShipmentDetailDocuments" type="radio"  value="0" name="rdPackageShipmentDetailContentType">
							Documents							<br>
							<input id="rdPackageShipmentDetailCommodities" type="radio" name="rdPackageShipmentDetailContentType" value="1">
							Products/Commodities					</td>
				</tr>
																	<tr id="trItemDescription">
					<td id="tdItemDescription" style="width: 49%;">
&nbsp;<span id="lblItemDescription">
Item description</span>
</td>
					<td style="width: 1%">
</td>
					<td style="width: 50%">
<input name="txtItemDescription" type="text" maxlength="35" id="txtItemDescription" style='border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;'/>
</td>
				</tr>
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
<input name="txtCustomsValue" type="text" maxlength="15" id="txtCustomsValue" style='border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
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
 														<input  id="chkSignatureOptions" type="checkbox"                                                                                                                        name="chkSignatureOptions" onclick="CheckSignatureOption();" />
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
No Signature Required</option>
																<option value="5">
Service Default</option>
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
<select name="ddlHomeDeliveryPremiumType" class="dropdown" id="ddlHomeDeliveryPremiumType" onchange="CheckHomeDeliveryPremium();" style='width:160px; background-color:Whitesmoke; font-family:Arial; font-size:11px; color:Black;' >
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
&nbsp;&nbsp;*Delivery date (MM/DD/YYYY)</td>
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
<input name="txtHomeDeliveryPhone" type="text" maxlength="10" id="txtHomeDeliveryPhone" style='width: 140px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
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
<input name="txtDryIceWeight" type="text" maxlength="5" id="txtDryIceWeight" style='width: 140px; border-color:Black; background-color:Whitesmoke; border-width:1px; font-family:Arial; font-size:11px; color:Black;' />
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
<input id="query" name="query" type="hidden" value="fYDgcRJsl8x8vYDflQ4LjGUk48R6eONeh4mYLcStZgOqqF/36LL1vJtZdmfM3WM0DIz51Y5v8478AQGWub3WGA4FgVudWrxu8t50cQci0zf2rFk66IXd6BnEoKos643zt+WKCHqzUCCa/BLk9vzmQNAR3/CfAuar9zgRlA+A196ImVAM6LqYIDMPr3DIvKeFqSX1gRbVvNYqfw/vwyiS53qm+GzzdMKOR0PgBZjFfWzRp+f8NBepZGlRTr5wMUjv9yq5C4s2GlnXwoLw1bpy7oaILrSNKeqM78GiAN6OcU/c4j15yjo46v69bLcYb45xJeMjs/K2Dv/GMpKd0WDM0sQ1BIOXpTf46BH4R89RiwHTWD4+B8NBjV7cdHBsvaWo0K8XwIjs6SnZ61JFFQc8R2IZpTM/UYdkRxZnT/tOpWph1wSOKQxTEqAmXhi18KTCzsSW9x/ve+dEqdR4CxYIlCb1cRCaVC0yDkLsGCau79hDthSQR5XKcHux0UnNwxuQg6+JVgl7wpBBwRIQIBw6/QJFrfTdBQDkTq7FrJBlCdSuK7PTmY/nldslOqjNFSh1RIY9L1T4/HEgvnog5OysbbepBLw0oWM0BdQrslpR3IMepBbqIXCQSfEHiNSuwtnSzAamSuFLQRfrH8xjRPxB2u76IUqa9Q67NdS5TZPXvGPEsFRO8JloygIBPRIStyZaNVaz6g3AARhBCtrkqOSAn2Csft9Q+y3k9gdC3S4k9dsjbBa6T2qeM8shRG8NQtTcjOYu+c7vnsyMNubKukzkeaW0PYkoAca+tOBZVqAVp4ZoraIpC9vgtzhfSfYr3fKPgefz7L1GNBdNi5LwsChbZKacvdI6J6BZOdLKmjqI5JYdxZuWoay8G/gw4fzl3+LZxXUkDOgF2qhiz0vOlVTT472yhX8sijWs+cReGKc61kVndxgb4+s/+Z7zTjthgtjF7CwD0vLx1/OI5VZMPHb2ZZ7tZDEtX8gDGuhFwEweEj5aIN/tD/IhVtaCrMGz/70ydF48JiSVHnFwGCFPOq8SBjL9TaYhqrSNFFDAYqeLSKLRZdej5rO2F42GoaEPJMv9AkuBwQ0xy7JYU0V3RCGvm/IOFYQDHmZk8OfZxsCdwEbWqbcG8N3I18VdtiPpglbS5jx6JwEVm/nCiDDR5Za58jyL/x02HYHy9QxC92iGmIQO2sBHA8kjcBorZenAfbB0oMoF7WmvhOQb84aleEcUwiauenKHywNpUX1wrQHN53rsFJXjvG2oHM3a3pEQvU1aTPM3hm7/qEQc76sL8HikDuSrCBUp4mL1+EtYQmMFMw1D/Z+TDb1pciwN/+wvymu/18+li9kxR8IrtNm4OERseJFIFXEeCek8PZ53xyo5g9X5clu4mMoWKmOYQ6eMJkS9NrCvkd34ZZwZ80Chv79Snf4ODu7YzGfL7dCPLnawslHSLal8wtDULeWCtNxD84WHUO+SiMTfxlZn/oESZyktY0b6X1lca7TKJ36obHRYNMspGsclldTEYEKk0BUOIYHtBUTvQPegfrKAKcCUAdulCeI0sLsUpGOhe9NmjnUGb6QaKIJwqgnV7FCjbNuoGZPVfNJnurR9Fpz1BsuIv0qudWNRfmL9rgennfRUjib9gyxZ3/3p14ra+aZJNRmhRzC+gBgk93MSI2d3mWpN54GSYgpf9JideAOy/Z99gvTExFA=" />
<input name="hdnSpecialServicesOptions" type="hidden" id="hdnSpecialServicesOptions" value="4," />
<input name="hdnPackage1Dimensions" type="hidden" id="hdnPackage1Dimensions" value="" />
<input name="hdnPackagePieceCount" type="hidden" id="hdnPackagePieceCount" value="1" />
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
