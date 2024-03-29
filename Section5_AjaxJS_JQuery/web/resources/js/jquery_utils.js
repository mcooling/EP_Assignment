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

        deleteFilm(filmId,'DeleteFilm');
    }
    e.preventDefault();
});

// todo check with jc. format no longer passed in from webform
// radio button value now checked from inside JQ function
// then passed into the ajax call
/**
 * Fetches all films from db
 * @param servletAddress GetAllFilms servlet
 */
function getAllFilms(servletAddress) {

    // checks the value of the radio button clicked in html
    let radioValue = $('input[name="format"]:checked').val();

    $.ajax({

        url: servletAddress,                                // http request URL
        type: "POST",                                       // http request type
        dataType : radioValue,                              // data type expected back in the response
        data : {format : radioValue},                       // data format requested in servlet call

        // action to take if request type is successful
        success: function(servletResponse) {                // object returned from post request
            tableResponseHandler(servletResponse, radioValue, servletAddress);   // response handler function
        }
    });
}

/**
 * called by webform onclick<br>
 * fetches film(s) from film name submitted<br>
 * @param filmname film name search string, entered into form
 * @returns {string} string value of film objects
 */
function getFilmsByName(servletAddress, searchString) {

    let filmName = null;

    // logic added to handle unified search input field
    // needs to handle text or number
    if (isNaN(searchString)) {
        filmName = document.getElementById(searchString).value;
    } else filmName = searchString;

    // let filmName = document.getElementById(searchString).value;
    let radioFormat = $('input[name="format"]:checked').val();

    $.ajax({

        url: servletAddress,                     // http request URL
        type: "POST",                                       // http request type
        dataType : radioFormat,                              // data type expected back in the response
        data : {format : radioFormat, filmname: filmName},                       // data format requested in servlet call

        // action to take if request type is successful
        success: function(servletResponse) {                // object returned from post request
            tableResponseHandler(servletResponse, radioFormat,
                servletAddress);
        }
    });
}

/**
 * fetches film from db, using film id
 * @param servletAddress GetFilmById
 * @param {string} film_Id film id
 */
function getFilmById(servletAddress, filmIdIn) {

    let filmId = null;

    // logic added to handle unified search input field
    // needs to handle text or number
    if (isNaN(filmIdIn)) {
        filmId = document.getElementById(filmIdIn).value;
    } else filmId = filmIdIn;

    // let film = document.getElementById(film_Id).value;
    let radioFormat = $('input[name="format"]:checked').val();

    $.ajax({
        url: servletAddress,                                // http request URL
        type: "POST",                                       // http request type
        dataType: radioFormat,                               // data type expected back in the response
        data: {format: radioFormat, filmId: filmId},           // data requested in servlet call

        // action to take if request type is successful
        success: function (servletResponse) {               // object returned from post request
            tableResponseHandler(servletResponse, radioFormat,
                servletAddress);
        }
    });
}

/**
 * called when user clicks 'Update' button, from film results table<br>
 * scrapes data from 'getfilm' results table<br>
 * populates details into Update Film fields
 */
function populateUpdateFilm(thisObject) {

    $("#updateForm").show(); // enable hidden 'Update Film' fieldset in HTML

    let tableRow = $(thisObject).closest("tr");

    let u_filmid = tableRow.find("td:eq(0)").text();
    let u_filmname = tableRow.find("td:eq(1)").text();
    let u_year = tableRow.find("td:eq(2)").text();
    let u_director = tableRow.find("td:eq(3)").text();
    let u_stars = tableRow.find("td:eq(4)").text();
    let u_review = tableRow.find("td:eq(5)").text();

    $("#u_filmid").val(u_filmid);
    $("#u_filmname").val(u_filmname);
    $("#u_year").val(u_year);
    $("#u_director").val(u_director);
    $("#u_stars").val(u_stars);
    $("#u_plot").val(u_review);

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

// todo update film. not quite working
// seems to be writing to disk ok, but doesn't display message in div as expected?
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
                    stars, review, servletAddress) {

    // writes the updated film values to disk
    // todo some refactoring comments to consider
    // effectively 'add film'
    // using standard xml / json / txt buttons
    // will require servlet / server side code
    // could use either response handler - depends whether I want to display updated table or not

    // should also disable the update film button again

    let dataFormat = $('input[name="format"]:checked').val();

    $.ajax({
        url: servletAddress,
        type: "POST",
        dataType: "text",
        data: {
                filmid: document.getElementById(filmId).value,
                name: document.getElementById(filmName).value, // reconfirm where prefix values are pointing
                year: document.getElementById(year).value,
                director: document.getElementById(director).value,
                stars: document.getElementById(stars).value,
                review: document.getElementById(review).value,
                format: dataFormat
        },

        success: function (servletResponse) {
                responseHandler(servletAddress, servletResponse);
        },

        // todo add these throughout. Good practice
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert("Status: " + textStatus); alert("Error: " + errorThrown);
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

    // check added, to handle film id passed as string or number
    if (isNaN(deleteFilmId)) {
        filmId = document.getElementById(deleteFilmId).value;
    } else filmId = deleteFilmId;

    $.ajax({

        url: servletAddress,                                    // http request URL
        type: "POST",
        dataType: "text",
        data: {filmId: filmId},                           // data requested in servlet call

        // action to take if request type is successful
        success: function (servletResponse) {                   // object returned from post request
            responseHandler(servletAddress, servletResponse);
        }
    });
}

$(document).on("click",function () {
    $("#addForm").show();
})

/**
 * called from webform<br>
 * adds film to database<br>
 * takes film object attributes<br>
 * handles ajax call to servlet<br>
 * handles success action from servlet response<br>
 * @param {string} filmName
 * @param {string} year
 * @param {string} stars
 * @param {string} director
 * @param {string} review
 * @param {*} servletAddress
 */
function addFilm(filmName, year, stars, director,
                 review, servletAddress) {

    let dataFormat = $('input[name="format"]:checked').val();

    $.ajax({

        url: servletAddress,
        type: "POST",
        dataType: "text",
        data: {
            name: document.getElementById(filmName).value, // reconfirm where prefix values are pointing
            year: document.getElementById(year).value,
            stars: document.getElementById(stars).value,
            director: document.getElementById(director).value,
            review: document.getElementById(review).value,
            format: dataFormat
        },

        // want to return message, not table
        success: function (servletResponse) {
            responseHandler(servletAddress, servletResponse);
                //tableResponseHandler(servletResponse, dataFormat, servletAddress)
            }
        });
}

/**
 * used by update & delete film flows<br>
 * called by JQuery function<br>
 * returns success / failed message, based on sql response code
 */
function responseHandler(servletAddress, servletResponse) {

    // clear any previous div content
    $("#addfilm").html('');
    $("#updatefilm").html('');
    $("#deletefilm").html('');

    if (servletAddress === "AddFilm") {
        $("#addfilm").append("<p>" + servletResponse + "</p>");

    } else if (servletAddress === "UpdateFilm") {
        $("#updatefilm").append("<p>" + servletResponse + "</p>");

    } else if (servletAddress === "DeleteFilm") {
        $("#deletefilm").append("<p>" + servletResponse + "</p>");
    }
}

// used by get and add film flows
// returns a populated table of films
function tableResponseHandler(servletResponse, dataFormat, servletAddress) {

    // clear any previous div content
    $("#getallfilms").html('');
    $("#getfilmsbyname").html('');
    $("#getfilmbyid").html('');

        // create base table structure object, with headings
        let htmlTableStructure =
            "<table class='ajaxTable'>" +
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
                    "<td><input type='button' className='button button5' value='Edit'/></td>" +
                    "<td><input type='button' className='button button5' value='Delete'/></td></tr>";
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
            let rowString = servletResponse.split("$").slice(1);

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

        if (servletAddress === 'GetAllFilms') {
            $("#getallfilms").append(htmlTableStructure + "</table>");
        } else if (servletAddress === 'GetFilmsByName') {
            $("#getfilmsbyname").append(htmlTableStructure + "</table>");
        } else if (servletAddress === 'GetFilmById') {
            $("#getfilmbyid").append(htmlTableStructure + "</table>");
        }

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


