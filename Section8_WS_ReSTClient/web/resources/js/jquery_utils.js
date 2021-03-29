// called when clicking update or delete button in table results
$(document).on( "click",
    ".ajaxTable td",
    function(e) {

    let td = $(this).parent().children().index($(this));
    let tableRow = $(this).closest("tr");

    if (td === 6) {
        populateUpdateFilm($(this));

    } else if (td === 7) {
        let filmId = parseInt(
                tableRow.find("td:eq(0)").text(),10);

        // todo null value in deleteFilm
        deleteFilm(filmId,'restwebservice');
    }
    e.preventDefault();
});

/**
 * called by webform onclick<br>
 * fetches all films from db<br>
 * @param servletAddress
 * @param dataFormat
 */
function getAllFilms(servletAddress, action, dataFormat) {
    $.ajax({

        url: document.getElementById(servletAddress).value,     // http request URL
        // type: "POST",                                        // http request type
        type: "GET",
        dataType : dataFormat,                                  // data type expected back in the response
        data : {action : action,
                format : dataFormat},                           // data format requested in servlet call

        // action to take if request type is successful
        success: function(servletResponse) {                    // object returned from post request
            tableResponseHandler(servletResponse, dataFormat, servletAddress);   // response handler function
            //responseHandler(servletAddress, servletResponse);
        }
    });
}

/**
 * called by webform onclick<br>
 * fetches film(s) from film name submitted<br>
 * @param filmname film name search string, entered into form
 * @returns {string} string value of filmname element
 */
function getFilmsByName(servletAddress, action, dataFormat, searchString) {

    let filmName = document.getElementById(searchString).value;

    $.ajax({
        url: document.getElementById(servletAddress).value,
        type: "GET",
        dataType : dataFormat,
        data : {action : action,
                filmName: filmName,
                format : dataFormat},

        success: function(servletResponse) {
            tableResponseHandler(servletResponse, dataFormat,
                servletAddress);
        }
    });
}

/**
 * fetches film from db, using film id
 * @param servletAddress GetFilmById
 * @param dataFormat requested (xml, json, text)
 * @param {string} film_Id
 */
function getFilmById(servletAddress, action, dataFormat, film_Id) {

    let filmId = document.getElementById(film_Id).value;

    $.ajax({
        url: document.getElementById(servletAddress).value,
        type: "GET",
        dataType: dataFormat,
        data: {action : action,
                id: filmId,
                format: dataFormat},

        success: function (servletResponse) {
            tableResponseHandler(servletResponse, dataFormat,
                servletAddress);
        }
    });
}

/**
 * called from webform<br>
 * takes film object attributes<br>
 * handles ajax call to servlet<br>
 * handles success action from servlet response<br>
 * @param {string} filmName
 * @param {string} year
 * @param {string} stars
 * @param {string} director
 * @param {string} review
 * @param {*} servletAddress
 * @param {*} dataFormat
 */

function addFilm(filmName, year, stars, director,
                 review, servletAddress, dataFormat) {

    $.ajax({

        url: document.getElementById(servletAddress).value,
        type: "POST",
        dataType: "text",
        data: {
            title: document.getElementById(filmName).value,
            year: document.getElementById(year).value,
            stars: document.getElementById(stars).value,
            director: document.getElementById(director).value,
            review: document.getElementById(review).value,
            format: dataFormat
        },

        success: function (servletResponse) {
            responseHandler(servletResponse);
        }
    });
}

/**
 * called from webform<br>
 * takes film object attributes<br>
 * handles ajax call to servlet<br>
 * handles success action from servlet response<br>
 * @param {string} filmId
 * @param {string} filmName
 * @param {string} year
 * @param {string} director
 * @param {string} stars
 * @param {string} review
 * @param {*} servletAddress
 * @param {string} dataFormat
 */
function updateFilm(filmId, filmName, year, director,
                    stars, review, servletAddress, dataFormat) {

    // writes the updated film values to disk
    // todo some refactoring comments to consider
    // effectively 'add film'
    // using standard xml / json / txt buttons
    // will require servlet / server side code
    // could use either response handler - depends whether I want to display updated table or not

    // should also disable the update film button again

    // todo have had to change app server to Wildfly. Tomcat doesn't support PUT as standard
    // may need to work out how to configure Tomcat to support

    $.ajax({
        url: document.getElementById(servletAddress).value,
        type: "PUT",
        dataType: "text",
        data: {
                id: document.getElementById(filmId).value,
                title: document.getElementById(filmName).value,
                year: document.getElementById(year).value,
                director: document.getElementById(director).value,
                stars: document.getElementById(stars).value,
                review: document.getElementById(review).value
                //format: dataFormat
        },

        success: function (servletResponse) {
                responseHandler(servletResponse);
        },

        // todo add these throughout. Good practice
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert("Status: " + textStatus);
            alert("Error: " + errorThrown);
        }
    });
}

/**
 * called from webform<br>
 * handles ajax call to servlet<br>
 * handles success action from servlet response<br>
 * @param {number|string} deleteFilmId
 * @param {*} servletAddress
 */
function deleteFilm(deleteFilmId, servletAddress) {

    let filmId = null;

    // todo check added, to handle film id passed as string or number
    // passed as string from 'Delete Film', but passed as int when deleted from table
    if (isNaN(deleteFilmId)) {
        filmId = document.getElementById(deleteFilmId).value;
    } else filmId = deleteFilmId;

    $.ajax({

        url: document.getElementById(servletAddress).value,
        type: "DELETE",
        dataType: "text",
        data: {id: filmId},

        // action to take if request type is successful
        success: function (servletResponse) {

            // $("#deletefilm").append("<p>" + servletResponse + "</p>");
            responseHandler(servletResponse);
        }
    });
}

/**
 * called when user clicks 'Update' button, from film results table<br>
 * scrapes data from 'getfilm' results table<br>
 * populates details into Update Film fields
 */
function populateUpdateFilm(thisObject) {

    // fetch value for each td cell in getfilmbyid div
    // add to update film fields

    // console.log("Hi");

    let tableRow = $(thisObject).closest("tr");

    // let u_filmid = tableRow.find("td:eq(0)").text();
    let u_filmname = tableRow.find("td:eq(1)").text();
    let u_year = tableRow.find("td:eq(2)").text();
    let u_director = tableRow.find("td:eq(3)").text();
    let u_stars = tableRow.find("td:eq(4)").text();
    let u_review = tableRow.find("td:eq(5)").text();

    // $("#u_filmid").val(u_filmid);
    $("#u_filmname").val(u_filmname);
    $("#u_year").val(u_year);
    $("#u_director").val(u_director);
    $("#u_stars").val(u_stars);
    $("#u_review").val(u_review);

    // scrollToForm();

    // pops them in the update film fields
    // look at invisible controls on screen

    // update enabled when id added (change in getfilmbyid, not here)

    // update button on get film by id
    //}

}

function scrollToForm() {
    $([document.documentElement, document.body]).animate({
        scrollTop: $("form").offset().top
    }, 1000);
}

/*
.ajaxTable tr:not(:first-child) {
    cursor: pointer;
}
 */

/**
 * used by update & delete film flows<br>
 * called by JQuery function<br>
 * returns success / failed message, based on sql response code
 */
function responseHandler(servletResponse) {

    $("#filmtable").html('');
    $("#filmtable").append("<p>" + servletResponse + "</p>");

    /*if (servletAddress === "DeleteFilm") {


    } else if (servletAddress === "UpdateFilm") {
            $("#updatefilm").append("<p>" + servletResponse + "</p>");
    }*/
}

// used by get and add film flows
// returns a populated table of films
function tableResponseHandler(servletResponse, dataFormat, servletAddress) {

    $("#filmtable").html('');

        // create base table structure object, with headings
        let htmlTableStructure =
            "<table border='1' class='ajaxTable'>" +
            "<tr>" +
                "<th>Film Id</th><th>Name</th><th>Year</th>" +
                "<th>Director</th><th>Cast</th><th>Plot</th>" +
                "<th></th><th></th>" +
            "</tr>";

        // if data format passed in is json
        if (dataFormat === "json") {

            // append rows to html table structure
            $.each(servletResponse.films, function (i, filmObject) {
                htmlTableStructure += "<tr>";
                $.each(filmObject, function (key, value) {
                    htmlTableStructure += "<td>" + value + "</td>";
                });
                htmlTableStructure +=
                        "<td><button id='updateButton'>Update</button></td>" +
                        "<td><button id='deleteButton'>Delete</button></td>" +
                    "</tr>";
            });

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
                        "<td><button id='updateButton'>Update</button></td>" +
                        "<td><button id='deleteButton'>Delete</button></td>" +
                    "</tr>";
            });

            // if data format passed in is text
        } else if (dataFormat === "text") {

            // append rows to html table structure

            // split servlet response into rows using $ delimiter
            // ignore first row (headings row is hardcoded)
            let rowString = servletResponse.split("$"); // removed slice

            // then for each remaining row, split each field by # delimiter and wrap row in <tr>
            // todo getting a random last row in table display. come back and fix
            $.each(rowString, function (i, rowField) {

                let rowParts = rowString[i].split('#');
                htmlTableStructure += "<tr>";

                $.each(rowParts, function (i, data) {
                    htmlTableStructure += "<td>" + data + "</td>";
                });
                // todo not rendering properly
                htmlTableStructure +=
                        "<td><button id='updateButton'>Update</button></td>" +
                        "<td><button id='deleteButton'>Delete</button></td>" +
                    "</tr>";
            });
        }
        $("#filmtable").append(htmlTableStructure + "</table>");
}


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


