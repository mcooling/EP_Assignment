// js utility functions
// build up as required, from source js file

// new function, requested by lab 2 exercise 1
// called by xhtml onclick, for xml data request
// derived from xmlCityTable
// takes in the div id as param
function xmlFilmResults(resultRegion) {
    let address = "two-richest-customers";          // servlet address
    let data = "format=";

    ajaxPost(address, data,                         // calls new function
        function (request) {
            showXmlCustomerInfo(request, resultRegion)
        });
}

// handles the ajax post request from the client
// takes in address, data and response handler
function ajaxPost(address, data, responseHandler) {
    let request = getRequestObject();
    request.onreadystatechange =
        function () { responseHandler(request); };
    request.open("POST", address, true);
    request.setRequestHeader(
        "Content-Type",
        "application/x-www-form-urlencoded");
    request.send(data);
}

function getRequestObject() {
    if (window.XMLHttpRequest) {
        return(new XMLHttpRequest());
    } else if (window.ActiveXObject) {
        return(new ActiveXObject("Microsoft.XMLHTTP"));
    } else {
        return(null);
    }
}

// new function, derived from showXmlCityInfo
// called by ajaxPost response handler function
// extracts response xml from server request body
// invokes functions to client-side table and insert in html
function showXmlCustomerInfo(request, resultRegion) {
    if ((request.readyState == 4) && (request.status == 200)) {
        let xmlDocument = request.responseXML;

        let displayData = "Hello World";

        /*let headings = getXmlValues(xmlDocument, "heading");
        let customers = xmlDocument.getElementsByTagName("customer");
        let rows = new Array(customers.length);
        let subElementNames =
            ["id", "firstName", "lastName", "balance"];
        for (let i = 0; i < customers.length; i++) {
            rows[i] = getElementValues(customers[i], subElementNames);
        }
        let table = getTable(headings, rows);*/

        htmlInsert(resultRegion, displayData);
    }
}

// insert html data into an element with a specified id
function htmlInsert(id, htmlData) {
    document.getElementById(id).innerHTML = htmlData;
}

// takes in an xml document and xml element name
// returns an array of values for that element
function getXmlValues(xmlDocument, xmlElementName) {
    let elementArray =
        xmlDocument.getElementsByTagName(xmlElementName);
    let valueArray = new Array();
    for (let i = 0; i < elementArray.length; i++) {
        valueArray[i] = getBodyContent(elementArray[i]);
    }
    return(valueArray);
}

// takes an element object and array of sub-element names
// returns an array of values of the sub-elements
function getElementValues(element, subElementNames) {
    let values = new Array(subElementNames.length);
    for (let i = 0; i < subElementNames.length; i++) {
        let name = subElementNames[i];
        let subElement = element.getElementsByTagName(name)[0];
        values[i] = getBodyContent(subElement);
    }
    return(values);
}

// takes in an xml element and returns body content
// escape removes any whitespace/characters
function getBodyContent(element) {
    element.normalize();
    return(element.childNodes[0].nodeValue);
}


//Put response text in the HTML element that has given ID.
function showResponseText(request, resultRegion) {
    if ((request.readyState == 4) &&
        (request.status == 200)) {
        htmlInsert(resultRegion, request.responseText);
    }
}




