// js utility functions
// build up as required, from source js file

/**
 * called by on click in webform<br>
 *     builds http request url to pass into ajax post request<br>
 * @param resultRegion html div id for where results are displayed
 */
function xmlFilmResults(resultRegion) {

    let servletAddress = "";

    // decides which servlet to call, based on div id passed in
    if (resultRegion === "getallfilms") {
        servletAddress = "GetAllFilms";
    } else {
        servletAddress = "GetFilms";
    }

    let dataFormat = "format=xml";

    ajaxPost(servletAddress, dataFormat,
        function (request) {
            showXmlFilmInfo(request, resultRegion)
        });
}

/**
 * handles the ajax post request from the client<br>
 *     passes servlet address and data format into request<br>
 * @param servletAddress
 * @param dataFormat
 * @param responseHandler
 */
function ajaxPost(servletAddress, dataFormat, responseHandler) {
    let request = getRequestObject();
    request.onreadystatechange =
        function () { responseHandler(request); };
    request.open("POST", servletAddress, true);
    request.setRequestHeader(
        "Content-Type",
        "application/x-www-form-urlencoded");
    request.send(dataFormat);
}

/**
 * fetches request object from the client browser<br>
 *     XMLHttpRequest, or ActiveX for legacy browsers
 * @returns {XMLHttpRequest|null|any}
 */
function getRequestObject() {
    if (window.XMLHttpRequest) {
        return(new XMLHttpRequest());
    } else if (window.ActiveXObject) {
        return(new ActiveXObject("Microsoft.XMLHTTP"));
    } else {
        return(null);
    }
}

/**
 * compiles the results data to be displayed<br>
 *     called by ajax post<br>
 *     extracts response xml from request body<br>
 *     inserts output into html region (via method call)
 * @param request object
 * @param resultRegion html div id location
 */

// todo doesn't seem to be fetching any content
function showXmlFilmInfo(request, resultRegion) {
    if ((request.readyState == 4) && (request.status == 200)) {

        // todo debug seems to be null
        let xmlDocument = request.responseXML;

        // passed into insertHtml function
        let displayData = "";

        // todo add console logs to test output
        // doesn't seem to returning any body content
        // kinda makes sense if xmlDocument is empty!?

        // console.log(xmlDocument.getElementsByName("films"));
        let xmlFilmElement = xmlDocument.getElementsByTagName("film");

        console.log(xmlFilmElement);
        console.log(xmlFilmElement.length);

        let subElementNames = ["id", "title", "year", "director", "stars", "review"];

        for (let i = 0; i < xmlFilmElement.length; i++) {
            displayData = displayData + getElementValues(
                xmlFilmElement[i], subElementNames);
        }

        /*let customers = xmlDocument.getElementsByTagName("customer");
        let rows = new Array(customers.length);
        let subElementNames =
            ["id", "firstName", "lastName", "balance"];
        for (let i = 0; i < customers.length; i++) {
            rows[i] = getElementValues(customers[i], subElementNames);
        }
        let table = getTable(headings, rows);*/

        // todo this is where result set is passed into html div
        htmlInsert(resultRegion, displayData);
    }
}

/**
 * called by showXmlCustomerInfo<br>
 * handles data display in the browser<br>
 * @param resultRegion html div id
 * @param displayData data to be displayed in result region
 */
function htmlInsert(resultRegion, displayData) {
    document.getElementById(resultRegion).innerHTML = displayData;
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
