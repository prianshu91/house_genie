    //Importing pre-dedefind modules
    var Express = require("express");
    var log = require("log4js").getLogger();
    var router = Express.Router();
    var async = require("async");
    var Deferred = require("promised-io/promise").Deferred;
    var ROUTER = require("./AdminRoutes");
    var Category = require("../../models/Category");
    var Employee = require("../../models/Employee");
    var Location = require("../../models/Location");
    var Verification = require("../../models/Verification");
    //Importing other modules
    var EmpService = require("./EmployeeApis");
    let LocService = require("./LocationsApis");
    let Categories = require("../../collections/Categories");

    log.info("Routers info");
    log.info(ROUTER);



    router.post(ROUTER.CATEGORY.INSERT, function(req, res) {

        let count = Object.keys(req.body).length;

        if (count == 0 || count === 0) {
            res.status(500).send('no data send');
        } else {

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
                    log.info('Records Successfully inserted ');
                    res.status(200).send("All records have been processed successfully");
                }
            });

        }


    });


    router.post(ROUTER.CATEGORY.DELETE, function(req, res) {

        let count = Object.keys(req.body).length;

        if (count == 0 || count === 0) {
            res.status(500).send('no data send');
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
                        log.info('Records have been deleted successfully');
                        res.status(200).send("Records have been deleted successfully");
                    }
                });

        }
    });

    router.post(ROUTER.CATEGORY.UPDATE, function(req, res) {

        let count = Object.keys(req.body).length;

        if (count == 0 || count === 0) {
            res.status(500).send('no data send');
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
                            callback("No record found  " + row.id);
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

        let count = Object.keys(req.body).length;

        if (count == 0 || count === 0) {
            res.status(500).send('no data send');
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
                            callback("No record found  " + row.id);
                        } else {
                            Category.where({ id: row.id }).fetch().
                            then(function(data, err) {
                                if (err) callback(err)
                                else res.status(200).send(data);
                            })

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



    router.post(ROUTER.EMPLOYEE.INSERT, function(req, res) {
        //Counting number of records
        let count = Object.keys(req.body).length;

        if (count == 0) {
            res.send("no data to send").status(200);
        } else {
            let records = req.body;

            async.each(records, function(emp, callback) {

                    let employee = new Employee();

                    let verf_record = {
                        pan_card: emp.pan,
                        aadhar_card: emp.aadhar,
                        driving_license: emp.license
                    };

                    EmpService.Verify(verf_record).
                    then(function(obj) {

                        let verification_id = obj.id;
                        if (!verification_id) {
                            callback("Unverified Account!! " + emp.fname);
                        } else {
                            let emp_record = {
                                first_name: emp.fname,
                                last_name: emp.lname,
                                contact_number: emp.phone,
                                address: emp.address,
                                verification_id: verification_id,
                                police_verified: ((verification_id) ? 1 : 2)
                            };
                            //Saving Employee
                            employee.save(emp_record).
                            then(function(obj, err) {
                                if (err) callback("Error in record " + emp.fname);
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
                        log.info('Records have been inserted successfully');
                        res.status(200).send("Records have been inserted successfully");
                    }
                });

        }

    });


    router.post(ROUTER.EMPLOYEE.UPDATE, function(req, res) {
        let count = Object.keys(req.body).length;

        if (count == 0) {
            res.send("no data to send").status(200);
        } else {
            let records = req.body;

            async.each(records, function(emp, callback) {

                    let employee = new Employee({ id: emp.id });

                    let emp_record = {
                        first_name: employee.fname,
                        last_name: employee.lname,
                        contact_number: employee.phone,
                        address: employee.address,
                    };
                    Employee.where({ id: emp.id }).count().
                    then(function(count) {
                        if (count == 0 || count === 0) {
                            dfd.reject("No Record found!!Unable to update");
                        } else {
                            employee.save(emp_record, { method: "update" }).
                            then(function(obj) {
                                    callback();
                                },
                                function(err) {
                                    callback(err);
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
                        log.info('Records have been inserted successfully');
                        res.status(200).send("Records have been inserted successfully");
                    }
                });

        }
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
        Category.fetchAll(function(data) {
            res.status(200).send(data);
        }).
        catch(function(err) {
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
        let count = Object.keys(req.body).length;


        if (count == 0 || count === 0) {
            res.status(500).send('no data send');
        } else if (count == 1 || count === 1) {
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
        } else if (count > 1) {
            let records = req.body;

            async.each(records, function(row, callback) {
                    log.info('Processing record ' + row.id);

                    let record = {
                        id: row.id,
                        area_name: row.area_name,
                        area_pincode: row.pincode,
                        division_name: row.division_name,
                        city: row.city,
                        taluk: row.taluk,
                        district: row.district,
                        state: row.state
                    };
                    let location = new Location();

                    location.save(record).then(function(obj, err) {
                        if (err) callback("Error in record " + row.id + "   " + err);
                        else
                            callback();
                    });
                },
                function(err) {
                    if (err) {
                        log.info(err);
                        res.status(500).send("Error in record");
                    } else {
                        log.info("Records have been processed successfully");
                        res.status(200).send("Records have benn process Successfully");
                    }
                });
        }

    });



    router.post(ROUTER.LOCATION.UPDATE, function(req, res) {
        let count = Object.keys(req.body).length;

        if (count == 0 || count === 0) {
            res.status(500).send('no data send');
        } else if (count == 1 || count === 1) {
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
                res.status(500).saend("Error Please find the error message" + err);
            });
        } else {
            async.each(records, function(row, callback) {
                    log.info('Processing record ' + row.id);
                    let record = {
                        id: row.id,
                        area_name: row.area_name,
                        area_pincode: row.pincode,
                        division_name: row.division_name,
                        city: row.city,
                        taluk: row.taluk,
                        district: row.district,
                        state: row.state
                    };
                    let location = new Location();

                    location.save({
                        area_name: record.area_name,
                        area_pincode: record.area_pincode,
                        division_name: record.division_name,
                        city: record.city,
                        taluk: record.taluk,
                        district: record.district,
                        state: record.state
                    }, { method: "update" }).then(function(obj, err) {
                        if (err) callback("Error in record " + row.id + "   " + err);
                        else callback();
                    });
                },
                function(err) {
                    if (err) {
                        log.info(err);
                        res.status(500).send("Error in record");
                    } else {
                        log.info("Records have been processed successfully");
                        res.status(200).send("Records have benn process Successfully");
                    }
                });
        }
    });




    //Exporting Modules
    module.exports = router;