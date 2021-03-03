// javascript utility functions
// client side code, called by html

// builds html

// Get the browser-specific request object, either for
// Firefox, Safari, Opera, Mozilla, Netscape, or IE 7 (top entry);
// or for Internet Explorer 5 and 6 (bottom entry).
function getRequestObject() {
    if (window.XMLHttpRequest) {
        return(new XMLHttpRequest());
    } else if (window.ActiveXObject) {
        return(new ActiveXObject("Microsoft.XMLHTTP"));
    } else {
        return(null);
    }
}

// creates headings & rows values, to pass into getTable
// inserts resultant table into html display region
function clientTable(displayRegion) {
    let headings = ["Quarter", "Apples", "Oranges"];
    let rows = [["Q1", randomSales(), randomSales()],
        ["Q2", randomSales(), randomSales()],
        ["Q3", randomSales(), randomSales()],
        ["Q4", randomSales(), randomSales()]];
    let table = getTable(headings, rows);
    htmlInsert(displayRegion, table);
}

// builds a table
// takes in an array of headings and and array of row arrays
// calls separate functions to build tr (headings) and td (rows) tags
function getTable(headings, rows) {
    let table = "<table border='1' class='ajaxTable'>\n" +
        getTableHeadings(headings) +
        getTableBody(rows) +
        "</table>";
    return(table);
}

// takes in content of headings array
// builds strings representation of tr/th html headings structure
function getTableHeadings(headings) {
    let firstRow = "  <tr>";
    for (let i = 0; i < headings.length; i++) {
        firstRow += "<th>" + headings[i] + "</th>";
    }
    firstRow += "</tr>\n";
    return(firstRow);
}

// takes in content of rows (array of arrays)
// this is based on nick's json format, which uses array of arrays, not array of json objects
// builds strings representation of tr/td html rows structure
function getTableBody(rows) {
    let body = "";
    for (let i = 0; i < rows.length; i++) {
        body += "  <tr>";
        let row = rows[i];
        for (let j = 0; j < row.length; j++) {
            body += "<td>" + row[j] + "</td>";
        }
        body += "</tr>\n";
    }
    return(body);
}

// takes in content of rows (array of customer objects)
// this is based on kaleem's json format, not nick's
// builds strings representation of tr/td html r
function getTableBodyJson(rows) {
    let body = "";

    for (const key of Object.keys(rows)) {
        body += "  <tr>";

        console.log(key, rows[key]);
    }
    
    /*for (let i = 0; i < rows.length; i++) {
        body += "  <tr>";
        let row = rows[i];
        for (let j = 0; j < row.length; j++) {
            body += "<td>" + row[j] + "</td>";
        }
        body += "</tr>\n";
    }
    return(body);*/
}

// insert html data into an element with a specified id
function htmlInsert(id, htmlData) {
    document.getElementById(id).innerHTML = htmlData;
}

// generates random $ value for numbers of sales
function randomSales() {
    let sales = 1000 + (Math.round(Math.random() * 9000));
    return("$" + sales);
}

// return the 'escaped' value of text field content
function getValue(id) {
    return(escape(document.getElementById(id).value));
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

// Make an HTTP request to the given address.
// Display result in the HTML element that has given ID.
function ajaxResult(address, resultRegion) {
    let request = getRequestObject();
    request.onreadystatechange =
        function() { showResponseText(request,
            resultRegion); };
    request.open("GET", address, true);
    request.send(null);
}

//Put response text in the HTML element that has given ID.
function showResponseText(request, resultRegion) {
    if ((request.readyState == 4) &&
        (request.status == 200)) {
        htmlInsert(resultRegion, request.responseText);
    }
}

// called by xhtml onclick, for xml data request
// passing in a parameter
function xmlCityTable(inputField, resultRegion) {
    let address = "show-cities";
    let data = "cityType=" + getValue(inputField) +
        "&format=xml";
    ajaxPost(address, data,
        function (request) {
        showXmlCityInfo(request, resultRegion);
        });
}

// new function, requested by lab 2 exercise 1
// called by xhtml onclick, for xml data request
// derived from xmlCityTable
// takes in the div id as param
function xmlCustomerTable(resultRegion) {
    let address = "two-richest-customers";          // servlet address
    let data = "format=";

    ajaxPost(address, data,                         // calls new function
        function (request) {
            showXmlCustomerInfo(request, resultRegion)
        });
}

// new function, requested by lab 2 exercise 2
// derived from xmlCityTable
// only takes resultRegion
// called by xhtml onclick, for json data request
function jsonCustomerTable(resultRegion) {
    let address = "two-richest-customers";
    let data = "format=";

    ajaxPost(address, data,
        function (request) {
            showJsonCustomerInfo(request, resultRegion);
        });
}

// new function, derived from showXmlCityInfo
// called by ajaxPost response handler function
function showJsonCustomerInfo(request, resultRegion) {
    if ((request.readyState == 4) && (request.status == 200)) {
        // console.log(request.responseText);
        let jsonDocument = JSON.parse(request.responseText);
        // let customer0 = jsonDocument.customers[1].firstname;
        // console.log(jsonDocument.customers[1].firstname);

        // let jsonDocument = request.responseText;
        // let data = eval("(" + jsonDocument + ")");
        // let table = data.stars;
        // htmlInsert(resultRegion, table);
        // console.log(request.responseText);
    }
}

/* boilerplate js provided in lab
function showJsonCityInfo(request, resultRegion) {
  if ((request.readyState == 4) &&
      (request.status == 200)) {
    var rawData = request.responseText;
    var data = eval("(" + rawData + ")");
    var table = getTable(data.headings, data.cities);
    htmlInsert(resultRegion, table);
  }
}
 */

// new function, derived from showXmlCityInfo
// called by ajaxPost response handler function
// extracts response xml from server request body
// invokes functions to client-side table and insert in html
function showXmlCustomerInfo(request, resultRegion) {
    if ((request.readyState == 4) && (request.status == 200)) {
        let xmlDocument = request.responseXML;
        let headings = getXmlValues(xmlDocument, "heading");
        let customers = xmlDocument.getElementsByTagName("customer");
        let rows = new Array(customers.length);
        let subElementNames =
            ["id", "firstName", "lastName", "balance"];
        for (let i = 0; i < customers.length; i++) {
            rows[i] = getElementValues(customers[i], subElementNames);
        }
        let table = getTable(headings, rows);
        htmlInsert(resultRegion, table);
    }
}

// new function, used in Wk Ajax lab
// called by EX_3 xhtml onclick, for string data request
// takes in the div id as param
function stringCustomerTable(resultRegion) {
    let address = "two-richest-customers";
    let data = "format=";

    ajaxPost(address, data,
        function (request) {
            showStringCustomerInfo(request, resultRegion);
        });
}

// new function, derived from showXmlCityInfo
// called by ajaxPost response handler function
// extracts response string from server request body
// invokes functions to client-side table and insert in html
function showStringCustomerInfo(request, resultRegion) {
    let rawData = request.responseText;
    let rowStrings = rawData.split(/[\n\r]+/);
    let headings = rowStrings[0].split("#");
    let rows = new Array(rowStrings.length-1);

    for (let i = 1; i < rowStrings.length; i++) {
        rows[i-1] = rowStrings[i].split("#");
    }

    let table = getTable(headings, rows); // not resolving
    htmlInsert(resultRegion, table);
}

// extracts response xml from server request body
// invokes functions to client-side table and insert in html
function showXmlCityInfo(request, resultRegion) {
    if ((request.readyState == 4) && (request.status == 200)) {
        let xmlDocument = request.responseXML;
        let headings = getXmlValues(xmlDocument, "heading");
        let cities = xmlDocument.getElementsByTagName("city");
        let rows = new Array(cities.length);
        let subElementNames =
            ["name", "time", "population"];
        for (let i = 0; i < cities.length; i++) {
            rows[i] = getElementValues(cities[i], subElementNames);
        }
        let table = getTable(headings, rows);
        htmlInsert(resultRegion, table);
    }
}

// called by xhtml onclick, for json data request
function jsonCityTable(inputField, resultRegion) {
    let address = "show-cities";
    let data = "cityType=" + getValue(inputField) +
        "&format=json";
    ajaxPost(address, data,
        function (request) {
            showJsonCityInfo(request, resultRegion);
        });
}

// extracts response xml from server request body
// invokes functions to client-side table and insert in html
function showJsonCityInfo(request, resultRegion) {
    if ((request.readyState == 4) &&
        (request.status == 200)) {
        let rawData = request.responseText; // not resolving responseText
        let data = eval("(" + rawData + ")");
        let table = getTable(data.headings, data.cities); // not resolving
        htmlInsert(resultRegion, table);
    }
}

function stringCityTable(inputField, resultRegion) {
    let address = "show-cities";
    let data = "cityType=" + getValue(inputField) +
        "&format=string";
    ajaxPost(address, data,
        function (request) {
            showStringCityInfo(request, resultRegion);
        });
}

function showStringCityInfo(request, resultRegion) {
    if ((request.readyState == 4) &&
        (request.status == 200)) {

        let rawData = request.responseText;
        let rowStrings = rawData.split(/[\n\r]+/);
        let headings = rowStrings[0].split("#");
        let rows = new Array(rowStrings.length-1);

        for (let i = 1; i < rowStrings.length; i++) {
            rows[i-1] = rowStrings[i].split("#");
        }

        let table = getTable(headings, rows);
        htmlInsert(resultRegion, table);
    }
}



