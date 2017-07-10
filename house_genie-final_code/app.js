var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var Router = require("./routes/router");
var port = 8065;
var i18n = require("i18n");
var path = require('path');
var passport = require('passport');
var connect = require('connect');
var session = require("express-session");
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var bookshelf = require("./db/bookshelf");
var loader = require("express-load");
app.use(cookieParser());
/*app.use(session({
 secret : 'keyboard cat',
 resave : false,
 saveUninitialized : false
 }));*/
app.use(bodyParser.json());
//app.use(passport.initialize());
//app.use(passport.session());
app.use(bodyParser.urlencoded({
    extended: true
}));
app.set('schema', bookshelf);

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
    if (typeof(req.headers['content-type']) === 'undefined') {
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

app.use("/Admin", Router.Admin);

/*loader( 'models' )
    .then( 'collections' )
    .then( 'controllers' )
    .then( 'routes' )
    .into( app );*/
console.log("Starting Server on port " + port);
app.listen(port);
console.log("Server running on port " + port);
