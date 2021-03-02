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

function showXmlFilmInfo(request, resultRegion) {
    if ((request.readyState == 4) && (request.status == 200)) {

        let xmlDocument = request.responseXML;
        let displayData = ""; // passed into insertHtml function

        // fetches xml object for each film element
        let xmlFilmElements = xmlDocument.getElementsByTagName("film");

        // testing data fetch is valid
        console.log(xmlFilmElements);
        console.log(xmlFilmElements.length);

        // test loop, to fetch & print unformatted results
        // loops through xml film element
        // gets element values for each element and adds to display string
        let subElementNames = ["id", "title", "year", "director", "stars", "review"];
        for (let i = 0; i < xmlFilmElements.length; i++) {
            displayData = displayData + getElementValues(
                xmlFilmElements[i], subElementNames);
        }

        /*let customers = xmlDocument.getElementsByTagName("customer");
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

/**
 * called by showXmlCustomerInfo<br>
 * handles data display in the browser<br>
 * @param resultRegion html div id
 * @param displayData data to be displayed in result region
 */
function htmlInsert(resultRegion, displayData) {
    document.getElementById(resultRegion).innerHTML = displayData;
}

/**
 * called by showXmlFilmInfo<br>
 * fetches tag values for each xmlFilmElement passed in
 * @param xmlFilmElement to fetch values for
 * @param subElementNames array of film element tag names
 * @returns {any[]} array of values for each xmlFilmElement
 */
function getElementValues(xmlFilmElement, subElementNames) {
    let values = new Array(subElementNames.length);
    for (let i = 0; i < subElementNames.length; i++) {
        let xmlSubElementName = subElementNames[i];
        let xmlSubElement = xmlFilmElement.getElementsByTagName(xmlSubElementName)[0];
        values[i] = getBodyContent(xmlSubElement);
    }
    return(values);
}

/**
 * called by getElementValues<br>
 * normalizes tags, to treat body content as a node<br>
 * @param xmlSubElement
 * @returns {any} node values for sub element child nodes
 */
function getBodyContent(xmlSubElement) {
    xmlSubElement.normalize();
    return(xmlSubElement.childNodes[0].nodeValue);
}
