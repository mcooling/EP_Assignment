// todo consolidated function. review with kaleem
function getAllFilms(servletAddress, dataFormat) {
    $.ajax({

        url: servletAddress,                                // http request URL
        type: "POST",                                       // http request type
        dataType : dataFormat,                              // data type expected back in the response
        data : {format : dataFormat},                       // data format requested in servlet call

        // action to take if request type is successful
        success: function(servletResponse) {                // object returned from post request
            responseHandler(servletResponse, dataFormat);   // response handler function
        }
    });
}

// todo consolidated function. review with kaleem
function responseHandler(servletResponse, dataFormat) {

    /*You're treating htmlTableStructure as a string. ' +
    'Rather than use htmlTableStructure = $("<table>"); htmlTableStructure .append("<tr><td></td></tr>")
    just keep it as a string: htmlTableStructure = "<table>"; ' +
    'htmlTableStructure += "<tr><td></td></tr>"; ' +
    'htmlTableStructure += "</table>"; ' +
    '$("#getall").append(htmlTableStructure); - ' +
    'but you're then responsible to put closing tags in the right place
    - so it's generally safer to build nodes and append to them ' +
    '- just don't treat them as a string*/

    // create base table structure object, with headings
    let htmlTableStructure =
        "<table border='1' class='ajaxTable'>" +
        "<tr>" +
            "<th>Film Id</th>" +
            "<th>Name</th>" +
            "<th>Year</th>" +
            "<th>Director</th>" +
            "<th>Cast</th>" +
            "<th>Plot</th>" +
        "</tr>";

    // original version
    /*let htmlTableStructure = $(
        "<table border='1' class='ajaxTable'>" +
        "<tr>" +
        "<th>Film Id</th>" +
        "<th>Name</th>" +
        "<th>Year</th>" +
        "<th>Director</th>" +
        "<th>Cast</th>" +
        "<th>Plot</th>" +
        "</tr>"
    );*/

    // if data format passed in is json
    if (dataFormat === "json") {

        // append rows to html table structure
        $.each(servletResponse.films, function(i, filmObject) {
            htmlTableStructure += "<tr>";
            $.each(filmObject, function(key, value){
                htmlTableStructure += "<td>" + value + "</td>";
            })
            htmlTableStructure += "</tr>";
        });

        // original version
        /*/ append rows to html table structure
        $.each(servletResponse.films, function(i, filmObject) {
            htmlTableStructure.append("<tr>");
            $.each(filmObject, function(key, value){
                htmlTableStructure.append("<td>" + value + "</td>")
            })
            htmlTableStructure.append("</tr>");
        });*/

    // if data format passed in is xml
    } else if (dataFormat === "xml") {

        // append rows to html table structure
        // loop through each film node in xml & get child node values
        $(servletResponse).find('film').each(function () {
            htmlTableStructure +=
                "<tr>" +
                    "<td>" + $(this).find('id').text() + "</td>" +
                    "<td>" + $(this).find('title').text() + "</td>" +
                    "<td>" + $(this).find('year').text() + "</td>" +
                    "<td>" + $(this).find('director').text() + "</td>" +
                    "<td>" + $(this).find('stars').text() + "</td>" +
                    "<td>" + $(this).find('review').text() + "</td>" +
                "</tr>";
        });

        // original version
        /*
        $(servletResponse).find('film').each(function () {
            htmlTableStructure.append("" +
                "<tr>" +
                    "<td>" + $(this).find('id').text() + "</td>" +
                    "<td>" + $(this).find('title').text() + "</td>" +
                    "<td>" + $(this).find('year').text() + "</td>" +
                    "<td>" + $(this).find('director').text() + "</td>" +
                    "<td>" + $(this).find('stars').text() + "</td>" +
                    "<td>" + $(this).find('review').text() + "</td>" +
                "</tr>"
            );
        });
         */

    // if data format passed in is text
    } else {

        // append rows to html table structure

        // split servlet response into rows using $ delimiter (rows 3 & 10)
        // ignore first row (this is the header, which we've already hardcoded)
        let rowString = servletResponse.split("$").slice(1);

        // then for each remaining row, split each field by # delimiter and wrap row in <tr>
        $.each(rowString, function (i, rowField) {

            let rowParts = rowString[i].split('#');
            htmlTableStructure += "<tr>";

            $.each(rowParts, function (i, data) {
                htmlTableStructure +=

                    "<td>" + data + "</td>";

            });
            htmlTableStructure += "</tr>";
        })

        // original version
        /*
        $.each(rowString, function (i, stringLine) {
            htmlTableStructure.append(
                    "<tr>" +
                        "<td>" + stringLine.split('#') + "</td>" +
                    "</tr>"
            );
        })
         */

    } $("#getallfilms").append(htmlTableStructure + "</table>");
}


// todo not done getFilmByName yet...or the other crud ops!!
// collection of early 'longhand' functions

/*

// todo refactored table function...this feels like a lot more code than before
// even if the 3 js functions were merged, i think there would be less code than here
// am i over-engineering this?
function getTable(tableHeadings, tableRows) {

    // overarching container for the table
    // gets built up through appends
    // todo lots of appending going on here!
    let htmlTableStructure = $("");

    // table header
    let htmlTableHeader = "<table border='1' class='ajaxTable'>\n";

    // htmlTableStructure.append(htmlTableHeader);

    // <tr/th> table headings structure
    let tableHeadingStructure = $("<tr>");   // tr open tag

    // th headings
    $(tableHeadings).find("headings").each(function () {
        let heading = $(this).find("heading").text();
        tableHeadingStructure.append("<th>" + heading + "</th>");
    });

    tableHeadingStructure.append("</th>");   // tr close tag

    // htmlTableStructure.append(headingStructure);

    // <tr/td> table body / row structure
    let tableBodyStructure = $("<tr>");   // tr open tag

    // get td content for each film element
    $(tableRows).find("film").each(function () {

        let id = $(this).find("id").text();
        let name = $(this).find("title").text();
        let year = $(this).find("year").text();
        let cast = $(this).find("stars").text();
        let director = $(this).find("director").text();
        let plot = $(this).find("review").text();

        // build td structure
        tableBodyStructure.append(
                "<td>" + id + "</td>" +
                "<td>" + name + "</td>" +
                "<td>" + year + "</td>" +
                "<td>" + cast + "</td>" +
                "<td>" + director + "</td>" +
                "<td>" + plot + "</td>" +
            "</tr>"
        );

        // htmlTableStructure.append(trStructure);

        htmlTableStructure.append(
            htmlTableHeader +
            tableHeadingStructure +
            tableBodyStructure
        );

        // finally appended to dive, with fully compiled html table structure
        $("#getallfilms").append(htmlTableStructure);
    });
}




function getAllFilmsJson(servletAddress) {

    $.ajax({

        url: servletAddress,        // http request URL
        type: "POST",               // http request type
        dataType : "json",          // data type expected back in the response
        data : {format : "json"},   // data format requested in servlet call

        // action to take if request type is successful
        success: function(servletResponse) {    // object returned from post request
            outputJson(servletResponse);        // response handler function
        }
    });
}

function outputJson(jsonData) {

    $.each(jsonData.films, function(i, filmObject) {
        $.each(filmObject, function(key, value){
            $("#getallfilms").append(key + " = " + value + "; ");
        })
    });
}

function getAllFilmsXml(servletAddress) {
    $.ajax({

        url: servletAddress,
        type: "POST",
        dataType : "xml",
        data : {format : "xml"},

        success: function(servletResponse) {
            outputXml(servletResponse);
        }
    });
}

function outputXml(servletResponse) {

    // loop through each film node in xml
    // for each node, return text values for each child
    $(servletResponse).find('film').each(function () {

        // now need to get to each sub element
        let id = $(this).find('id').text();
        let title = $(this).find('title').text();
        let year = $(this).find('year').text();
        let director = $(this).find('director').text();
        let stars = $(this).find('stars').text();
        let review = $(this).find('review').text();

        // then append to div
        $("#getallfilms").append(
            id + ", " +
            title + ", " +
            year + ", " +
            director + ", " +
            stars + ", " +
            review + "\n"
        );
    });
}

function getAllFilmsText(servletAddress) {
    $.ajax({

        url: servletAddress,
        type: "POST",
        dataType : "text",
        data : {format : "text"},

        success: function(servletResponse) {
            outputText(servletResponse);
        }
    });
}

function outputText(servletResponse) {

    let rowString = servletResponse.split("$");    // splits each line in text file

    $.each(rowString, function (i, stringLine) {
        $("#getallfilms").append(stringLine + "; ");
    })
} */


