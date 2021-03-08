// js utility functions
// build up as required, from source js file

/**
 * called by on click in webform<br>
 *     used for 'GetAllFilms' requests<br>
 *     builds http request url to pass into ajax post request<br>
 * @param resultRegion html div id for where results are displayed
 */
function xmlFilmResults(resultRegion) {

    let servletAddress = "GetAllFilms";
    let dataFormat = "format=xml";

    ajaxPost(servletAddress, dataFormat,
        function (request) {
            showXmlFilmInfo(request, resultRegion)
        });
}

/**
 * called by on click in webform<br>
 * builds http request url to pass into ajax post request<br>
 * @param resultRegion html div id for where results are displayed
 */
function jsonFilmResults(resultRegion) {
    let servletAddress = "GetAllFilms";
    let dataFormat = "format=json";

    ajaxPost(servletAddress, dataFormat,
        function (request) {
            showJsonFilmInfo(request, resultRegion);
        });
}

/**
 * called by on click in webform<br>
 * builds http request url to pass into ajax post request<br>
 * @param resultRegion html div id for where results are displayed
 */
function stringFilmResults(resultRegion) {
    let servletAddress = "GetAllFilms";
    let dataFormat = "format=text";

    ajaxPost(servletAddress, dataFormat,
        function (request) {
            showStringFilmInfo(request, resultRegion);
        });
}

/**
 * called by on click in webform<br>
 *     used by 'GetFilms' requests<br>
 *     builds http request url to pass into ajax post request<br>
 * @param filmname film name search string, entered into form
 * @param resultRegion html div id for where results are displayed
 */
function xmlFilmResultsFromName(filmname, resultRegion) {

    let baseServletAddress = "GetFilms";
    let servletParams = "?filmname=" + getValue(filmname);

    let fullServletAddress = baseServletAddress + servletParams;
    let dataFormat = "format=xml";

    ajaxPost(fullServletAddress, dataFormat,
        function (request) {
            showXmlFilmInfo(request, resultRegion)
        });
}

// todo may be able to simplify
// one function for 'getall films' and one for 'getfilmbyname'
// then pass in additional field for format and checks if xml / json / string?
/**
 * called by on click in webform<br>
 *     used for 'GetFilms' requests<br>
 *     builds http request url to pass into ajax post request<br>
 * @param filmname film name search string, entered into form
 * @param resultRegion html div id for where results are displayed
 */
function jsonFilmResultsFromName(filmname, resultRegion) {
    let baseServletAddress = "GetFilms";
    let servletParams = "?filmname=" + getValue(filmname);
    let fullServletAddress = baseServletAddress + servletParams;
    let dataFormat = "format=json";

    ajaxPost(fullServletAddress, dataFormat,
        function (request) {
            showJsonFilmInfo(request, resultRegion)
        });
}

// todo may be able to simplify
// one function for 'getall films' and one for 'getfilmbyname'
// just passes in additional field for format and checks if xml / json / string?
/**
 * called by on click in webform<br>
 *     used for 'GetFilms' requests<br>
 *     builds http request url to pass into ajax post request<br>
 * @param filmname film name search string, entered into form
 * @param resultRegion html div id for where results are displayed
 */
function stringFilmResultsFromName(filmname, resultRegion) {
    let baseServletAddress = "GetFilms";
    let servletParams = "?filmname=" + getValue(filmname);
    let fullServletAddress = baseServletAddress + servletParams;
    let dataFormat = "format=text";

    ajaxPost(fullServletAddress, dataFormat,
        function (request) {
            showStringFilmInfo(request, resultRegion)
        });
}

// todo review with jason. merged three previous 'getresultsbyname' methods into 1
function getFilmResultsFromName(filmname, resultRegion, dataFormatIn) {

    let baseServletAddress = "GetFilms";
    let servletParams = "?filmname=" + getValue(filmname);
    let fullServletAddress = baseServletAddress + servletParams;
    let dataFormat = "format=" + dataFormatIn;

    if (dataFormatIn === "xml") {
        ajaxPost(fullServletAddress, dataFormat,
            function (request) {
                showXmlFilmInfo(request, resultRegion)
            });

    } else if (dataFormatIn === "json") {
        ajaxPost(fullServletAddress, dataFormat,
            function (request) {
                showJsonFilmInfo(request, resultRegion)
            });

    } else {
        ajaxPost(fullServletAddress, dataFormat,
            function (request) {
                showStringFilmInfo(request, resultRegion)
            });
    }
}

// todo review with jason. merged three previous 'getfilmresults' methods into 1
/**
 * called by on click in webform<br>
 *     used for 'GetAllFilms'<br>
 * builds http request url to pass into ajax post request<br>
 * @param resultRegion html div id for where results are displayed
 * @param {string} dataFormatIn data format type requested
 */
// todo check with jc. this looks like it works
// does this mean we can just have one generic function for all three buttons?
// now takes in the data format value, passed in by webform
// then adds if-else, to decide which 'show info' function to call
// todo include this in 'code refactored' section of report if it's a goer
function getAllFilmResults(resultRegion, dataFormatIn) {
    let servletAddress = "GetAllFilms";
    // let dataFormat = "format=json";

    let dataFormat = "format=" + dataFormatIn;

    if (dataFormatIn === "json") {
        ajaxPost(servletAddress, dataFormat,
            function (request) {
                showJsonFilmInfo(request, resultRegion);
            });
    } else if (dataFormatIn === "xml") {
        ajaxPost(servletAddress, dataFormat,
            function (request) {
                showXmlFilmInfo(request, resultRegion)
            });
    } else {
        ajaxPost(servletAddress, dataFormat,
            function (request) {
                showStringFilmInfo(request, resultRegion)
            });
    }
}

/**
 * compiles the results data to be displayed<br>
 *     called by ajax post<br>
 *     extracts response xml from request body<br>
 *     formats xml data into a html table (via method calls)<br>
 *     inserts output into html region (via method call)
 * @param request http request object
 * @param resultRegion html div id location
 */
function showXmlFilmInfo(request, resultRegion) {
    if ((request.readyState == 4) && (request.status == 200)) {

        let xmlDocument = request.responseXML;

        // fetches xml object for each film element
        let xmlFilmElements = xmlDocument.getElementsByTagName("film");
        let tableHeadings = getXmlValues(xmlDocument, "heading");
        let tableRows = new Array(xmlFilmElements.length);
        let subElementNames =
            ["id", "title", "year", "director", "stars", "review"];

        for (let i = 0; i < xmlFilmElements.length; i++) {
            tableRows[i] = getElementValues(xmlFilmElements[i],
                subElementNames);
        }

        let htmlTable = getTable(tableHeadings, tableRows);
        htmlInsert(resultRegion, htmlTable);

    }
}

/**
 * compiles the results data to be displayed<br>
 *     called by ajax post<br>
 *     extracts response json from request body<br>
 *     inserts output into html region (via method call)
 * @param request http request object
 * @param resultRegion html div id location
 */
function showJsonFilmInfo(request, resultRegion) {
    if ((request.readyState == 4) && (request.status == 200)) {

        let rawJsonData = request.responseText;
        // let displayData = rawJsonData.toString();

        //console.log(rawJsonData.toString());              // log out to test content of request
        let parsedJsonData = JSON.parse(rawJsonData);       // parse raw data into javascript object
        // let filmTest = parsedJsonData.films[1].stars;    // create test json object
        //console.log(filmTest);                            // log out to test object values

        // create variable for headings and rows
        // fetches json objects
        let tableHeadings = parsedJsonData.headings;
        let tableRows = parsedJsonData.films;

        let htmlTable = getJsonTable(tableHeadings, tableRows);

        // htmlInsert(resultRegion, displayData);           // display full json string in html
        // htmlInsert(resultRegion, filmTest);              // display object value in html
        htmlInsert(resultRegion, htmlTable);

        /*
        let rawData = request.responseText;
        let data = eval("(" + rawData + ")");
        let table = getTable(data.headings, data.cities);
        htmlInsert(resultRegion, table);
         */
    }
}

/**
 * compiles the results data to be displayed<br>
 *     called by ajax post<br>
 *     extracts response string from request body<br>
 *     inserts output into html region (via method call)
 * @param request http request object
 * @param resultRegion html div id location
 */
function showStringFilmInfo(request, resultRegion) {

    let rawStringData = request.responseText;
    // console.log(rawStringData.toString());              // log out to test string data in response text
    // let displayData = rawStringData.toString();

    let rowStrings = rawStringData.split("$");          // split each row on $ separator
    let tableHeadings = rowStrings[0].split("#");       // split each heading on # separator
    let tableRows = new Array(rowStrings.length-1);

    for (let i = 1; i < rowStrings.length; i++) {
        tableRows[i-1] = rowStrings[i].split("#");
        //console.log(tableRows[i-1]);
    }
    console.log(tableRows[0]);
    let table = getTable(tableHeadings, tableRows);
    htmlInsert(resultRegion, table);                        // test full string displays in html
}

/**
 * called by showXmlFilmInfo and showStringFilmInfo<br>
 * builds html table, to format the raw data in the View jsp<br>
 * makes separate function calls, to compile table headings and rows<br>
 * wraps table tags around heading and row content
 * @param tableHeadings array of xml objects, for each table heading
 * @param tableRows array of xml objects, for each film
 * @returns {string} string representation of compiled html table
 */
function getTable(tableHeadings, tableRows) {
    let table = "<table border='1' class='ajaxTable'>\n" +
        getTableHeadings(tableHeadings) +
        getTableBody(tableRows) +
        "</table>";
    return(table);
}

/**
 * called by getTable. builds html table headings row<br>
 * compiles tr/th html around table headings xml objects<br>
 * @param tableHeadings array of xml objects, for each table heading
 * @returns {string} string representation of compiled html heading row
 */
function getTableHeadings(tableHeadings) {
    let firstRow = "  <tr>";
    for (let i = 0; i < tableHeadings.length; i++) {
        firstRow += "<th>" + tableHeadings[i] + "</th>";
    }
    firstRow += "</tr>\n";
    return(firstRow);
}

/**
 * called by getTable. builds html for table body rows<br>
 * compiles tr/td html around table rows xml objects<br>
 * @param tableRows array of xml objects, for each film
 * @returns {string} string representation of compiled html, for each table row
 */
function getTableBody(tableRows) {
    let body = "";
    for (let i = 0; i < tableRows.length; i++) {
        body += "  <tr>";
        let row = tableRows[i];
        for (let j = 0; j < row.length; j++) {
            body += "<td>" + row[j] + "</td>";
        }
        body += "</tr>\n";
    }
    return(body);
}

// todo written separate function for json
// calls a different table body function
// just check in with jc on my thinking here...
/**
 * called by showJsonFilmInfo<br>
 * separate function to xml & string, as it calls a different table body function<br>
 * builds html table, to format the raw data in the View jsp<br>
 * makes separate function calls, to compile table headings and rows<br>
 * wraps table tags around heading and row content
 * @param tableHeadings array of json objects, for each table heading
 * @param tableRows array of json objects, for each film
 * @returns {string} string representation of compiled html table
 */
function getJsonTable(tableHeadings, tableRows) {
    let table = "<table border='1' class='ajaxTable'>\n" +
        getTableHeadings(tableHeadings) +
        getTableBodyJson(tableRows) +
        "</table>";
    return(table);
}

/**
 * called by getJsonTable. builds html for table body rows<br>
 * compiles tr/td html around table rows json objects<br>
 * @param tableRows array of json objects, for each film
 * @returns {string} string representation of compiled html, for each table row
 */
function getTableBodyJson(tableRows) {

    // todo technically seems to work, but not sure if it's the most elegant way
    // other methods suggest Object.entries() but I just couldn't get it to work
    // couldn't get past value just returning the Object instance
    // see comments below..

    // approach was to loop through array of json objects
    // create a json object variable
    // then build up each td tag by accessing key value
    // todo get an 'undefined' final row, due to that annoying empty object

    let body = "";

    for (let i = 0; i < tableRows.length; i++) {

        let jsonObject = tableRows[i];

        body +=
            " <tr>\n" +
            "   <td>" + jsonObject.id + "</td\n>" +
            "   <td>" + jsonObject.title + "</td\n>" +
            "   <td>" + jsonObject.year + "</td\n>" +
            "   <td>" + jsonObject.director + "</td\n>" +
            "   <td>" + jsonObject.stars + "</td\n>" +
            "   <td>" + jsonObject.review + "</td\n>" +
            " </tr>\n";

    }

    return(body);

    /*for (let [key, value] of Object.entries(tableRows)) {
      console.log(`${key}: ${value}`);
    }*/

    /*for (const [key, value] of Object.entries(tableRows)) {
        console.log(`${key}: ${value}`);
    }*/

    /*for (let key of Object.entries(tableRows)) {
        body +=
            " <tr>\n" +
            " <td>" + `${Object.valueOf()}` + "</td\n>" +
            //" <td>" + `${Object.toString()}` + "</td\n>" +
            //"  <td>" + `${key.toString()}: ${value.toString()}` + "</td\n>" +
            " </tr>\n";
    }*/
    //return(body);
    /*
    let body = "";
    for (let i = 0; i < tableRows.length; i++) {
        body += "  <tr>";
        let row = tableRows[i];
        for (let j = 0; j < row.length; j++) {
            body += "<td>" + row[j] + "</td>";
        }
        body += "</tr>\n";
    }*/

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
 * called by showXmlFilmInfo
 * gets xml values for table headings, passed into getTable
 * loops through array of element nodes
 * makes further method call, to get body content for each element node
 * @param xmlDocument xml DOM
 * @param xmlElementName 'heading' xml element name
 * @returns {any[]} array of element node values for 'heading'
 */
function getXmlValues(xmlDocument, xmlElementName) {
    let elementArray =
        xmlDocument.getElementsByTagName(xmlElementName);
    let valueArray = new Array();
    for (let i = 0; i < elementArray.length; i++) {
        valueArray[i] = getBodyContent(elementArray[i]);
    }
    return(valueArray);
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

/**
 * called by json/stringResultsFromName<br>
 * fetches value for 'filmname'<br>
 * @param filmname film name search string, entered into form
 * @returns {string} string value of filmname element
 */
function getValue(filmname) {
    return(escape(document.getElementById(filmname).value));
}


