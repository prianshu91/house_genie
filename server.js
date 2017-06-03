var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var Router = require("./app/routes/router");
var port = 8051;


//Server Configuration
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));

app.use(express.static(__dirname + '/public'));
app.use('/bower_components',  express.static(__dirname + '/bower_components'));
app.get("/maidApp",function(req,res){

	res.sendFile(__dirname + "/public/index.html");
});

app.use("/",Router.SEARCH);
app.use("/admin",Router.ADMIN);

app.listen(port);

console.log("Server running on port " + port);