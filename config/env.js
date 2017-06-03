

var mysql = require("mysql");
var MySQL = require( "./" + require("./db/env").env + ".js" );


var connection = mysql.createPool({
	 waitForConnections : true,
	connection : MySQL.connection,
	port : MySQL.port,
   host     : MySQL.host,
  user     : MySQL.user,
  password :  MySQL.password,
  database :  MySQL.database
 // ,debug: true
});


/*connection.connect(function(err) {
  if (err) {
    console.error('error connecting: ' + err.stack);
    return;
  }
 
  console.log('connected as id ' + connection.threadId);
});*/

module.exports = connection;