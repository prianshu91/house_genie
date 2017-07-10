let CONSTANTS = require("../ApplicationConstants");

let routes = {
    CATEGORY: {
        INSERT: "/" + CONSTANTS.CATEGORY + "/" + CONSTANTS.INSERT,
        DELETE: "/" + CONSTANTS.CATEGORY + "/" + CONSTANTS.DELETE,
        SEARCH: "/" + CONSTANTS.CATEGORY + "/" + CONSTANTS.SEARCH,
        UPDATE: "/" + CONSTANTS.CATEGORY + "/" + CONSTANTS.UPDATE,
    },
    LOCATION: {
        INSERT: "/" + CONSTANTS.LOCATION + "/" + CONSTANTS.INSERT,
        DELETE: "/" + CONSTANTS.LOCATION + "/" + CONSTANTS.DELETE,
        SEARCH: "/" + CONSTANTS.LOCATION + "/" + CONSTANTS.SEARCH,
        UPDATE: "/" + CONSTANTS.LOCATION + "/" + CONSTANTS.UPDATE,
    },
    EMPLOYEE: {
        INSERT: "/" + CONSTANTS.EMPLOYEE + "/" + CONSTANTS.INSERT,
        DELETE: "/" + CONSTANTS.EMPLOYEE + "/" + CONSTANTS.DELETE,
        SEARCH: "/" + CONSTANTS.EMPLOYEE + "/" + CONSTANTS.SEARCH,
        UPDATE: "/" + CONSTANTS.EMPLOYEE + "/" + CONSTANTS.UPDATE
    }
};
module.exports = routes;
