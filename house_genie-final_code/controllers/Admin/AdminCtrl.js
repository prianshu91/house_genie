//Importing pre-dedefind modules
var Express = require("express");
var log = require("log4js").getLogger();
var router = Express.Router();
var async = require("async");
var ROUTER = require("./AdminRoutes");
var Category = require("../../models/Category");
//Importing other modules
var CatgService = require("./CategoryApis");
var EmpService = require("./EmployeeApis");
let LocService = require("./LocationsApis");
let Categories = require("../../collections/Categories");


router.post(ROUTER.CATEGORY.INSERT, function(req, res) {

    let count = Object.keys(req.body).length;


    if (count == 0) {
        res.status(500).send('no data send');
    } else if (count == 1) {
        let category_record = {
            id: req.body.id,
            name: req.body.name,
            description: req.body.description
        };

        CatgService.Insert(category_record).
        then(function(data) {
            log.info("Success");
            res.status(200).send(data);

        }, function(err) {
            log.info(err);
            res.status(500).send("Error Please find the error message" + err);
        });
    } else if (count > 1) {

        let rows = req.body;
        async.each(rows, function(row, callback) {

            // Perform operation on record here.
            log.info('Processing record ' + row.name);

            let category_record = {
                id: row.id,
                name: row.name,
                description: row.desc
            };
            let category = new Category();
            category.save(category_record).then(function(obj, err) {
                if (err) callback("Error in record " + row.id + "   " + err);
                else
                    callback();
            });


        }, function(err) {
            // if any of the record processing produced an error, err would equal that error
            if (err) {
                // One of the iterations produced an error.
                // All processing will now stop.
                log.info(err);
                res.status(500).send(err);
            } else {
                log.info('All records have been processed successfully');
                res.status(200).send("All records have been processed successfully");
            }
        });

    }


});


router.post(ROUTER.CATEGORY.DELETE, function(req, res) {

    let count = Object.keys(req.body).length;
    if (count == 0) {
        res.status(500).send('no data send');
    } else if (count == 1) {
        CatgService.Delete(req.body.id).then(
            function(data) {
                log.info("Record deleted");
                res.status(200).send("Record has been Successfully deleted");

            },
            function(err) {
                log.info(err);
                res.status(500).send(err);
            });
    } else {

        async.each(req.body, function(row, callback) {

                // Perform operation on record here.
                log.info('Processing record ' + row.id);

                let category_record = {
                    id: row.id,
                    name: row.name,
                    description: row.desc
                };
                let category = new Category({
                    id: row.id
                });


                Category.where({
                    id: row.id
                }).count().
                then(function(count) {
                    if (count == 0 || count === 0) {
                        callback("No record found with " + row.id);
                    } else {
                        category.destroy().then(function(obj, err) {
                            if (err) callback(err);
                            else
                                callback();
                        });
                    }
                });


            },
            function(err) {
                // if any of the record processing produced an error, err would equal that error
                if (err) {
                    // One of the iterations produced an error.
                    // All processing will now stop.
                    log.info(err);

                    res.status(500).send(err);
                } else {
                    log.info('All records have been deleted successfully');
                    res.status(200).send("All records have been deleted successfully");
                }
            });

    }
});

router.post(ROUTER.CATEGORY.UPDATE, function(req, res) {

    let count = Object.keys(req.body).length;
    if (count == 0) {
        res.status(500).send('no data send');
    } else if (count == 1) {
        let category_record = {
            id: req.body.id,
            name: req.body.name,
            desc: req.body.desc
        };

        CatgService.Update(category_record).then(
            function(data) {
                log.info("Record updated");
                res.status(200).send(data);

            },
            function(err) {
                log.info(err);
                res.status(500).send(err);
            }
        );
    } else {

        async.each(req.body, function(row, callback) {

                // Perform operation on record here.
                log.info('Processing record ' + row.id);


                let category = new Category({
                    id: row.id
                });


                Category.where({ id: row.id }).count().
                then(function(count) {
                    if (count == 0 || count === 0) {
                        callback("No record found + " + row.id);
                    } else {
                        category.save({ name: row.name, description: row.desc }, { method: "update" }).
                        then(function(record, err) {
                            if (err) callback(err);
                            else callback();
                        });
                    }
                });


            },
            function(err) {
                // if any of the record processing produced an error, err would equal that error
                if (err) {
                    // One of the iterations produced an error.
                    // All processing will now stop.
                    log.info(err);

                    res.status(500).send(err);
                } else {
                    log.info('All records have been updated successfully');
                    res.status(200).send("All records have been updated successfully");
                }
            });
    }
});

router.get(ROUTER.CATEGORY.SEARCH, function(req, res) {

    CatgService.Search(req.body.id).then(
        function(data) {
            log.info("Record found");
            res.status(200).send(data);

        },
        function(err) {
            log.info(err);
            res.status(500).send(err);
        }
    );
});


router.post(ROUTER.EMPLOYEE.INSERT, function(req, res) {
    let verf_record = {
        pan_card: req.body.pan,
        aadhar_card: req.body.aadhar,
        driving_license: req.body.license
    }

    let emp_record = {
        fname: req.body.fname,
        lname: req.body.lname,
        phone: req.body.phone,
        address: req.body.address
    }
    EmpService.Insert(verf_record, emp_record).then(
        function(data) {
            log.info(data);
            res.status(200).send(data);
        },
        function(err) {
            log.info(err)
            res.status(500).send(err);
        });
});


router.post(ROUTER.EMPLOYEE.UPDATE, function(req, res) {
    let emp_record = {
        id: req.body.id,
        fname: req.body.fname,
        lname: req.body.lname,
        phone: req.body.phone,
        address: req.body.address
    }
    EmpService.Update(emp_record).then(
        function(data) {
            log.info(data);
            res.status(200).send(data);
        },
        function(err) {
            log.info(err)
            res.status(500).send(err);
        });
});


router.get(ROUTER.EMPLOYEE.SEARCH, function(req, res) {

    EmpService.Search(req.body.id).then(
        function(data) {
            log.info("Record found");
            res.status(200).send(data);

        },
        function(err) {
            log.info(err);
            res.status(500).send(err);
        }
    );
});

router.post("/employee/insertCateg", function(req, res) {

    let record = {
        category_id: req.body.id,
        employee_id: req.body.empid
    }
    EmpService.InsertEmpCategory(record).then(
        function(data) {
            log.info("Record saved");
            res.status(200).send(data);
        },
        function(err) {
            log.info(err);
            res.status(500).send(err);
        });
});


router.get("/employee", function(req, res) {
    EmpService.View().then(
        function(data) {
            res.status(200).send(data);
        },
        function(err) {
            res.status(500).send(err);
        });
    s
});

router.get("/category", function(req, res) {
    CatgService.View().then(
        function(data) {
            res.status(200).send(data);
        },
        function(err) {
            res.status(500).send(err);
        });
});

router.get("/locations", function(req, res) {
    LocService.View().then(
        function(data) {
            res.status(200).send(data);
        },
        function(err) {
            res.status(500).send(err);
        });
});

router.get("/location/city/:id", function(req, res) {

    LocService.City(req.params.id).then(
        function(data) {
            res.status(200).send(data);
        },
        function(err) {
            res.status(500).send(err);
        });
});

router.get("/location/state/:id", function(req, res) {

    LocService.State(req.params.id).then(
        function(data) {
            res.status(200).send(data);
        },
        function(err) {
            res.status(500).send(err);
        });
});

router.get("/location/area/:id", function(req, res) {
    LocService.Area(req.params.id).then(
        function(data) {
            res.status(200).send(data);
        },
        function(err) {
            res.status(500).send(err);
        });
})

router.post(ROUTER.LOCATION.INSERT, function(req, res) {
    //'1', 'Ada B.O', '504293', 'Adilabad', 'Hyderabad', 'Asifabad', 'Adilabad', 'ANDHRA PRADESH'

    let record = {
        id: req.body.id,
        area_name: req.body.area_name,
        area_pincode: req.body.pincode,
        division_name: req.body.division_name,
        city: req.body.city,
        taluk: req.body.taluk,
        district: req.body.district,
        state: req.body.state
    };

    LocService.Insert(record).
    then(function(data) {
        log.info("Success");
        res.status(200).send(data);

    }, function(err) {
        log.info(err);
        res.status(500).send("Error Please find the error message" + err);
    });

});

router.post(ROUTER.LOCATION.UPDATE, function(req, res) {

    let record = {
        id: req.body.id,
        area_name: req.body.area_name,
        area_pincode: req.body.pincode,
        division_name: req.body.division,
        city: req.body.city,
        taluk: req.body.taluk,
        district: req.body.district,
        state: req.body.state
    };

    LocService.Update(record).
    then(function(data) {
        log.info("Success");
        res.status(200).send(data);

    }, function(err) {
        log.info(err);
        res.status(500).send("Error Please find the error message" + err);
    });

});



//Exporting Modules
module.exports = router;
