//Importing pre-dedefind modules
var log = require("log4js").getLogger();
var Deferred = require("promised-io/promise").Deferred;
//Importing other modules
var Location = require("../../models/Location");
//Sorting Parameters
let sortParam = {
    Id: "id",
    Area: "area_name",
    State: "state",
    City: "city"
}
let sortOrder = { ASC: "ASC", DESC: "DESC" };
//Function Implemenatitons

//Get All locations
function getAllLocatons() {
    let dfd = new Deferred();
    let location = new Location();

    location.forge().orderBy(sortParam.Id, sortOrder.ASC).fetchAll().
    then(function(obj) {
            dfd.resolve(obj);
        },
        function(err) {
            dfd.reject(err);
        })
    return dfd.promise;
}

//Get Location by City
function getLocInfoByCity(city) {

    let dfd = new Deferred();
    let location = new Location();

    location.where({ city: city }).orderBy(sortParam.City, sortOrder.ASC).fetchAll().
    then(function(obj) {
            dfd.resolve(obj);
        },
        function(err) {
            dfd.reject(obj);
        });

    return dfd.promise;
}

//Get Location by State
function getLocInfoByState(state) {

    let dfd = new Deferred();
    let location = new Location();

    location.where({ state: state }).orderBy(sortParam.State, sortOrder.ASC).fetchAll().
    then(function(obj) {
            dfd.resolve(obj);
        },
        function(err) {
            dfd.reject(obj);
        });

    return dfd.promise;
}

//Get Location by Area
function getLocationByArea(area) {
    let dfd = new Deferred();
    let location = new Location();
    
    location.where({ area_name: area }).orderBy(sortParam.Area, sortOrder.ASC).fetchAll().
    then(function(obj) {
            dfd.resolve();
        },
        function(err) {
            dfd.reject();
        });
    return dfd.promise;
}

//Update Location Info
function updateLocation(record) {
    let location = new Location({ id: record.id });
    let dfd = new Deferred();

    location.save({
        area_name: record.area_name,
        area_pincode: record.area_pincode,
        division_name: record.division_name,
        city: record.city,
        taluk: record.taluk,
        district: record.district,
        state: record.state
    }, { method: "update" }).
    then(function(obj) {
        dfd.resolve(obj);

    }, function(err) {
        dfd.reject(err);
    });
    return dfd.promise;
}

//Inserting Locations
function insertLocation(record) {
    let location = new Location();
    let dfd = new Deferred();

    location.save(record).
    then(function(obj) {
            dfd.resolve(obj);
        },
        function(err) {
            dfd.reject(err);
        });
    return dfd.promise;
}

let locationService = {
        City: getLocInfoByCity,
        State: getLocInfoByState,
        Area: getLocationByArea,
        View: getAllLocatons,
        Insert: insertLocation,
        Update: updateLocation
    }
    //Exporting Modules
module.exports = locationService;
