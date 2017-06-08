var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var Router = require("./app/routes/router");
var port = 8051;
var i18n = require("i18n");
var path = require('path');
var connect = require('connect');

// //Server Configuration
// app.use(bodyParser.json());
// app.use(bodyParser.urlencoded({
//     extended: true
// }));
//
// app.use(express.static(__dirname + '/public'));
// app.use('/bower_components',  express.static(__dirname + '/bower_components'));
// app.get("/maidApp",function(req,res){
//
// 	res.sendFile(__dirname + "/public/index.html");
// });
//
// app.use("/",Router.SEARCH);
// app.use("/admin",Router.ADMIN);
//
// app.listen(port);
//
// console.log("Server running on port " + port);

var Bookshelf = require( 'bookshelf' );


var express = require( 'express' );
var loader  = require( 'express-load' );

// var bookshelf = Bookshelf.initialize({
//     client: 'mysql',
//     debug: true,
//     connection: {
//         host: 'localhost',
//         user: 'root',
//         password: 'infoobjects1',
//         database: 'house_genie'
//     }
// });

var knex = require('knex')({
    client: 'mysql',
    debug: true,
    connection: {
        host: 'localhost',
        user: 'root',
        password: 'infoobjects1',
        database: 'house_genie'
    }
});
var bookshelf = require('bookshelf')(knex);
var Bookshelf = require('bookshelf');
Bookshelf.DB = bookshelf;
app.set('schema', bookshelf);
/*
 * Add the registry plugin to handle table relations definitions without
 * curcular dependencies.
 */

console.log("bookshelf..........",Bookshelf.DB)

Bookshelf.DB.plugin( 'registry' );
// THIS FIXES A BUG IN BS MODEL RESOLUTION VISA the registry plugin!!
Bookshelf.DB.model( '_unused', {} );
Bookshelf.DB.collection( '_unused', {} );
/*
 * Add the visibility plugin to hide model fields on toJSON
 */
Bookshelf.DB.plugin( 'visibility' );

// Create the main express app
var app = express();

// Boot the app (see boot.js), then use the very
// cool express-load utility to suck in all of the
// modules located under lib, models, collections, etc.
// and finally, start the server!

app.use(function(req, res, next) {
    var jsonp = res.jsonp; // save original function
    res.jsonp = function(obj) {
        if (req.param('fmt') == 'xml') {
            try {
                var o = JSON.parse(JSON.stringify(obj));
                body = xml.render(o);
                body = body.replace(/count\+tail/g, 'count_tail');
                res.header('Content-Type', 'text/xml');
                res.send(body);
            } catch (err) {
                return next(err);
            }
        } else {
            jsonp.call(res, obj);
        }
    };
    next();
});

app.use(function(req, res, next) {
    // IE9 doesn't set headers for cross-domain ajax requests
    if(typeof(req.headers['content-type']) === 'undefined'){
        req.headers['content-type'] = "application/json; charset=UTF-8";
    }
    next();
})

// CORS
app.use(function(req, res, next) {
    res.header('Access-Control-Allow-Origin', req.headers.origin);
    res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE');
    res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept, Final-Length, Offset, Content-Range, Content-Disposition');
    res.header('Access-Control-Allow-Credentials', 'true');
    if (req.method == 'OPTIONS') {
        res.send(200);
    } else {
        next();
    }
});

app.use(express.json());
app.use(express.urlencoded());
app.use(express.methodOverride());

app.use(i18n.init);

app.use(express.cookieParser());
app.use(express.bodyParser())
    .use(app.router);

loader( 'models' )
    .then( 'collections' )
    .then( 'controllers' )
    .then( 'routes' )
    .into( app );
console.log(app.controllers.Category)
app.listen( port)

