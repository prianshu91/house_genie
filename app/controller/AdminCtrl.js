

var Express = require("express");
var router = Express.Router();
var AdminService = require("../service/AdminService");

router.get("/viewAll",AdminService.VIEW_ALL_RECORDS);
router.get("/view/:id",AdminService.VIEW_ALL_INFO);

//Exporting Modules
module.exports = router;

