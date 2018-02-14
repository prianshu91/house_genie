'use strict';

promelleApp
    .controller(
        'employeeDetailCtrl', [
            '$scope',
            'employeeService',
            '$stateParams',
            '$state',
            function($scope, employeeService, $stateParams, $state,
                $location) {
                $scope.reviewLimit = 3;
                $scope.employeeData = {};

                $scope.empData = {
                    "image": "http://graph.facebook.com/v2.5/301/picture?height=200&height=200",
                    "description": "Quia in rerum qui assumenda minima.Sint numquam cupiditate qui architecto dolores ex.Non autem qui ab et mollitia iusto doloremque.Incidunt dicta excepturi nam sint cum ut.Adipisci voluptas enim qui.",
                    "overview": "18 of years experience",
                    "full_name": "Devrat Nair",
                    "employeeId": 4943,
                    "first_name": "Gemine",
                    "last_name": "Pillai",
                    "contact_number": "+91556-302-4554",
                    "secondary_contact_number": "+91-249-2176052",
                    "house_number": "78007",
                    "street": "Reddy Drives",
                    "area": "Jay",
                    "pincode": 582421,
                    "city": "West Bhushit",
                    "job": "Principal",
                    "experience": 18,
                    "all_review": [{
                            "review_id": 39889,
                            "user": "Agrata Bhattathiri",
                            "ratings": "3.40",
                            "comment": "Estates Throughway",
                            "comment_date": "January 11, 2018"
                        },
                        {
                            "review_id": 22514,
                            "user": "Maggie Casper",
                            "ratings": "3.25",
                            "comment": "integrate orchid plum",
                            "comment_date": "January 21, 2018"
                        },
                        {
                            "review_id": 43442,
                            "user": "Vishnu Acharya",
                            "ratings": "5.88",
                            "comment": "Forward",
                            "comment_date": "January 12, 2018"
                        },
                        {
                            "review_id": 85790,
                            "user": "Alex Herman",
                            "ratings": "2.77",
                            "comment": "XSS Automotive withdrawal",
                            "comment_date": "January 14, 2018"
                        },
                        {
                            "review_id": 86248,
                            "user": "Gobinda Tandon",
                            "ratings": "1.15",
                            "comment": "innovate maroon",
                            "comment_date": "January 13, 2018"
                        },
                        {
                            "review_id": 46118,
                            "user": "Janet Hamill",
                            "ratings": "2.15",
                            "comment": "customized Marketing",
                            "comment_date": "January 10, 2018"
                        },
                        {
                            "review_id": 57208,
                            "user": "Maheswar Marar",
                            "ratings": "2.17",
                            "comment": "B2C",
                            "comment_date": "January 24, 2018"
                        },
                        {
                            "review_id": 63751,
                            "user": "Mario Kreiger",
                            "ratings": "3.69",
                            "comment": "navigating Awesome niches",
                            "comment_date": "January 11, 2018"
                        },
                        {
                            "review_id": 1299,
                            "user": "Daiwik Dhawan",
                            "ratings": "3.81",
                            "comment": "Automotive",
                            "comment_date": "January 17, 2018"
                        },
                        {
                            "review_id": 13357,
                            "user": "Ben Cremin",
                            "ratings": "2.79",
                            "comment": "Convertible Marks Gujarat",
                            "comment_date": "January 24, 2018"
                        },
                        {
                            "review_id": 10193,
                            "user": "Shubha Bhattathiri",
                            "ratings": "5.02",
                            "comment": "convergence",
                            "comment_date": "January 26, 2018"
                        },
                        {
                            "review_id": 21607,
                            "user": "Arnold Bradtke",
                            "ratings": "4.24",
                            "comment": "deliver",
                            "comment_date": "January 19, 2018"
                        },
                        {
                            "review_id": 88873,
                            "user": "Mani Ahluwalia",
                            "ratings": "5.28",
                            "comment": "Sweden",
                            "comment_date": "January 13, 2018"
                        },
                        {
                            "review_id": 18984,
                            "user": "Mrs. Delia Hyatt",
                            "ratings": "3.28",
                            "comment": "infomediaries Grocery",
                            "comment_date": "January 8, 2018"
                        },
                        {
                            "review_id": 62109,
                            "user": "Radha Kapoor",
                            "ratings": "1.51",
                            "comment": "invoice Beauty virtual",
                            "comment_date": "January 14, 2018"
                        },
                        {
                            "review_id": 48819,
                            "user": "Lorraine Boyer",
                            "ratings": "5.06",
                            "comment": "Metal panel Congolese Franc",
                            "comment_date": "January 14, 2018"
                        },
                        {
                            "review_id": 89814,
                            "user": "Chinmayanand Mehra",
                            "ratings": "5.12",
                            "comment": "morph Pants",
                            "comment_date": "January 17, 2018"
                        },
                        {
                            "review_id": 46013,
                            "user": "Mr. Carmen Dooley",
                            "ratings": "2.87",
                            "comment": "benchmark Streets visionary",
                            "comment_date": "January 15, 2018"
                        },
                        {
                            "review_id": 93112,
                            "user": "Bhagirathi Devar",
                            "ratings": "2.05",
                            "comment": "Investor solid state",
                            "comment_date": "January 25, 2018"
                        },
                        {
                            "review_id": 74174,
                            "user": "Linda Koch I",
                            "ratings": "2.89",
                            "comment": "generating Micronesia",
                            "comment_date": "January 10, 2018"
                        },
                        {
                            "review_id": 46990,
                            "user": "Menka Nehru",
                            "ratings": "1.41",
                            "comment": "solid state card Ville",
                            "comment_date": "January 17, 2018"
                        },
                        {
                            "review_id": 84534,
                            "user": "Arnold Dickens Sr.",
                            "ratings": "1.64",
                            "comment": "Hollow Fantastic Movies",
                            "comment_date": "January 16, 2018"
                        },
                        {
                            "review_id": 97499,
                            "user": "Vrund Dwivedi",
                            "ratings": "5.34",
                            "comment": "synergize lime Cedi",
                            "comment_date": "January 20, 2018"
                        },
                        {
                            "review_id": 8059,
                            "user": "Kristopher King I",
                            "ratings": "1.50",
                            "comment": "Direct Checking Account",
                            "comment_date": "January 22, 2018"
                        },
                        {
                            "review_id": 57832,
                            "user": "Baladitya Verma",
                            "ratings": "1.16",
                            "comment": "Facilitator",
                            "comment_date": "January 9, 2018"
                        },
                        {
                            "review_id": 15433,
                            "user": "Mr. Darnell Erdman",
                            "ratings": "4.45",
                            "comment": "approach proactive",
                            "comment_date": "January 21, 2018"
                        },
                        {
                            "review_id": 8419,
                            "user": "Akshat Khanna",
                            "ratings": "1.07",
                            "comment": "Sports EXE Saint Pierre and Miquelon",
                            "comment_date": "January 26, 2018"
                        },
                        {
                            "review_id": 42959,
                            "user": "Steven Mertz III",
                            "ratings": "1.58",
                            "comment": "Rustic Frozen Bike",
                            "comment_date": "January 21, 2018"
                        },
                        {
                            "review_id": 80702,
                            "user": "Hamsini Sethi",
                            "ratings": "2.38",
                            "comment": "transmit",
                            "comment_date": "January 21, 2018"
                        },
                        {
                            "review_id": 29878,
                            "user": "Florence Bartell Sr.",
                            "ratings": "2.96",
                            "comment": "Licensed Metal Chair Rubber",
                            "comment_date": "January 11, 2018"
                        },
                        {
                            "review_id": 54317,
                            "user": "Trilokesh Kaniyar",
                            "ratings": "1.56",
                            "comment": "invoice protocol Liaison",
                            "comment_date": "January 20, 2018"
                        },
                        {
                            "review_id": 34302,
                            "user": "Leah Schulist",
                            "ratings": "4.61",
                            "comment": "Developer bricks-and-clicks bluetooth",
                            "comment_date": "January 20, 2018"
                        },
                        {
                            "review_id": 20555,
                            "user": "Dhanu Tagore",
                            "ratings": "4.02",
                            "comment": "Cambridgeshire Courts",
                            "comment_date": "January 13, 2018"
                        },
                        {
                            "review_id": 78149,
                            "user": "Annette Kuhic",
                            "ratings": "5.79",
                            "comment": "integrated Gibraltar withdrawal",
                            "comment_date": "January 14, 2018"
                        },
                        {
                            "review_id": 97693,
                            "user": "Prema Mehra",
                            "ratings": "5.98",
                            "comment": "Som Highway",
                            "comment_date": "January 13, 2018"
                        },
                        {
                            "review_id": 5861,
                            "user": "Louis O'Kon",
                            "ratings": "4.94",
                            "comment": "Intelligent Soap",
                            "comment_date": "January 12, 2018"
                        },
                        {
                            "review_id": 46876,
                            "user": "Keerti Kaniyar",
                            "ratings": "4.41",
                            "comment": "Flats Stravenue",
                            "comment_date": "January 8, 2018"
                        },
                        {
                            "review_id": 83851,
                            "user": "April Mohr MD",
                            "ratings": "5.86",
                            "comment": "system Light",
                            "comment_date": "January 16, 2018"
                        },
                        {
                            "review_id": 36593,
                            "user": "Navin Varma",
                            "ratings": "3.53",
                            "comment": "alarm Credit Card Account",
                            "comment_date": "January 25, 2018"
                        },
                        {
                            "review_id": 40933,
                            "user": "Olive Reinger",
                            "ratings": "5.57",
                            "comment": "application time-frame",
                            "comment_date": "January 8, 2018"
                        },
                        {
                            "review_id": 88606,
                            "user": "Bhaswar Joshi",
                            "ratings": "2.65",
                            "comment": "Clothing",
                            "comment_date": "January 19, 2018"
                        },
                        {
                            "review_id": 63180,
                            "user": "Rene Monahan",
                            "ratings": "4.81",
                            "comment": "primary Naira",
                            "comment_date": "January 20, 2018"
                        },
                        {
                            "review_id": 37199,
                            "user": "Dhanalakshmi Bhattacharya",
                            "ratings": "2.03",
                            "comment": "Optimization EXE Investment Account",
                            "comment_date": "January 9, 2018"
                        },
                        {
                            "review_id": 31318,
                            "user": "Mona Osinski V",
                            "ratings": "4.95",
                            "comment": "Specialist",
                            "comment_date": "January 11, 2018"
                        },
                        {
                            "review_id": 94896,
                            "user": "Adinath Bandopadhyay",
                            "ratings": "1.83",
                            "comment": "Consultant",
                            "comment_date": "January 24, 2018"
                        },
                        {
                            "review_id": 24731,
                            "user": "Alton Ryan",
                            "ratings": "2.14",
                            "comment": "orchid Chicken",
                            "comment_date": "January 7, 2018"
                        },
                        {
                            "review_id": 49828,
                            "user": "Bodhan Talwar",
                            "ratings": "2.16",
                            "comment": "Handmade non-volatile",
                            "comment_date": "January 21, 2018"
                        },
                        {
                            "review_id": 16494,
                            "user": "Miss Lisa Emard",
                            "ratings": "1.14",
                            "comment": "hard drive",
                            "comment_date": "January 8, 2018"
                        },
                        {
                            "review_id": 23941,
                            "user": "Jyotis Guneta",
                            "ratings": "1.26",
                            "comment": "bluetooth methodology Global",
                            "comment_date": "January 26, 2018"
                        },
                        {
                            "review_id": 59377,
                            "user": "Mr. Peggy Okuneva",
                            "ratings": "2.72",
                            "comment": "Bedfordshire Garden",
                            "comment_date": "January 12, 2018"
                        },
                        {
                            "review_id": 35588,
                            "user": "Chitramala Devar",
                            "ratings": "2.14",
                            "comment": "Savings Account",
                            "comment_date": "January 24, 2018"
                        },
                        {
                            "review_id": 7521,
                            "user": "Dora Hintz",
                            "ratings": "5.31",
                            "comment": "virtual",
                            "comment_date": "January 19, 2018"
                        },
                        {
                            "review_id": 95668,
                            "user": "Ekaksh Nehru",
                            "ratings": "1.84",
                            "comment": "zero defect indigo",
                            "comment_date": "January 17, 2018"
                        },
                        {
                            "review_id": 62191,
                            "user": "Mr. Colleen Ritchie",
                            "ratings": "5.73",
                            "comment": "Investor Bedfordshire",
                            "comment_date": "January 14, 2018"
                        },
                        {
                            "review_id": 52778,
                            "user": "Deenabandhu Deshpande",
                            "ratings": "4.60",
                            "comment": "Bedfordshire Netherlands Antilles",
                            "comment_date": "January 8, 2018"
                        },
                        {
                            "review_id": 10194,
                            "user": "Brittany Rippin",
                            "ratings": "2.00",
                            "comment": "extranet customer loyalty clicks-and-mortar",
                            "comment_date": "January 11, 2018"
                        },
                        {
                            "review_id": 54861,
                            "user": "Indra Jha",
                            "ratings": "1.90",
                            "comment": "feed",
                            "comment_date": "January 15, 2018"
                        },
                        {
                            "review_id": 38849,
                            "user": "Teri Hammes",
                            "ratings": "1.01",
                            "comment": "world-class Spur Shores",
                            "comment_date": "January 26, 2018"
                        },
                        {
                            "review_id": 58146,
                            "user": "Aatmaj Gupta",
                            "ratings": "3.87",
                            "comment": "Ergonomic Dadar and Nagar Haveli",
                            "comment_date": "January 18, 2018"
                        },
                        {
                            "review_id": 76023,
                            "user": "Lynn Block",
                            "ratings": "3.64",
                            "comment": "Handcrafted Soft Keyboard Computer Nagaland",
                            "comment_date": "January 22, 2018"
                        },
                        {
                            "review_id": 41578,
                            "user": "Akshata Devar",
                            "ratings": "1.50",
                            "comment": "Designer redundant",
                            "comment_date": "January 13, 2018"
                        },
                        {
                            "review_id": 64447,
                            "user": "Brian Glover",
                            "ratings": "2.37",
                            "comment": "Checking Account Incredible Steel Soap optimize",
                            "comment_date": "January 12, 2018"
                        },
                        {
                            "review_id": 28025,
                            "user": "Yoginder Khanna",
                            "ratings": "3.27",
                            "comment": "Forint Profound brand",
                            "comment_date": "January 12, 2018"
                        },
                        {
                            "review_id": 52045,
                            "user": "Melvin Ondricka",
                            "ratings": "4.31",
                            "comment": "wireless",
                            "comment_date": "January 26, 2018"
                        },
                        {
                            "review_id": 1250,
                            "user": "Shakuntala Arora",
                            "ratings": "2.72",
                            "comment": "reinvent Balanced Incredible",
                            "comment_date": "January 15, 2018"
                        },
                        {
                            "review_id": 70997,
                            "user": "Beulah Hudson III",
                            "ratings": "4.16",
                            "comment": "Gibraltar Synchronised",
                            "comment_date": "January 23, 2018"
                        },
                        {
                            "review_id": 37817,
                            "user": "Shrishti Bandopadhyay",
                            "ratings": "4.00",
                            "comment": "Refined Credit Card Account Generic",
                            "comment_date": "January 17, 2018"
                        },
                        {
                            "review_id": 67623,
                            "user": "Kyle Boyer DDS",
                            "ratings": "1.82",
                            "comment": "transmitting",
                            "comment_date": "January 17, 2018"
                        },
                        {
                            "review_id": 39327,
                            "user": "Mohinder Jain",
                            "ratings": "4.99",
                            "comment": "repurpose feed Orchestrator",
                            "comment_date": "January 16, 2018"
                        },
                        {
                            "review_id": 19312,
                            "user": "Mrs. Constance Schuster",
                            "ratings": "4.63",
                            "comment": "card",
                            "comment_date": "January 11, 2018"
                        },
                        {
                            "review_id": 88946,
                            "user": "Chandramani Kapoor",
                            "ratings": "1.82",
                            "comment": "Daman and Diu",
                            "comment_date": "January 13, 2018"
                        },
                        {
                            "review_id": 41296,
                            "user": "Connie Cronin I",
                            "ratings": "4.49",
                            "comment": "input Buckinghamshire Versatile",
                            "comment_date": "January 10, 2018"
                        },
                        {
                            "review_id": 75067,
                            "user": "Bhagvan Varman",
                            "ratings": "3.57",
                            "comment": "Solomon Islands Dollar",
                            "comment_date": "January 8, 2018"
                        },
                        {
                            "review_id": 86590,
                            "user": "Bryant Christiansen",
                            "ratings": "1.68",
                            "comment": "pink",
                            "comment_date": "January 18, 2018"
                        },
                        {
                            "review_id": 20510,
                            "user": "Mani Tandon",
                            "ratings": "2.59",
                            "comment": "Tuvalu",
                            "comment_date": "January 26, 2018"
                        },
                        {
                            "review_id": 95981,
                            "user": "Antoinette Robel",
                            "ratings": "5.13",
                            "comment": "Bedfordshire Mountains",
                            "comment_date": "January 15, 2018"
                        },
                        {
                            "review_id": 39639,
                            "user": "Chakrika Chattopadhyay",
                            "ratings": "1.53",
                            "comment": "Practical",
                            "comment_date": "January 21, 2018"
                        },
                        {
                            "review_id": 2067,
                            "user": "Sonia Emmerich V",
                            "ratings": "3.54",
                            "comment": "withdrawal Principal scalable",
                            "comment_date": "January 16, 2018"
                        },
                        {
                            "review_id": 801,
                            "user": "Menka Dubashi",
                            "ratings": "1.67",
                            "comment": "Maharashtra Fresh",
                            "comment_date": "January 16, 2018"
                        },
                        {
                            "review_id": 64814,
                            "user": "Byron Lesch",
                            "ratings": "4.39",
                            "comment": "User-centric Handmade Markets",
                            "comment_date": "January 23, 2018"
                        },
                        {
                            "review_id": 96625,
                            "user": "Rageshwari Jain",
                            "ratings": "2.59",
                            "comment": "conglomeration Gorgeous drive",
                            "comment_date": "January 18, 2018"
                        },
                        {
                            "review_id": 24364,
                            "user": "Robyn Collins",
                            "ratings": "5.98",
                            "comment": "override Sports",
                            "comment_date": "January 24, 2018"
                        },
                        {
                            "review_id": 56731,
                            "user": "Chaturbhuj Gandhi",
                            "ratings": "5.03",
                            "comment": "SSL enterprise",
                            "comment_date": "January 10, 2018"
                        },
                        {
                            "review_id": 68406,
                            "user": "Sue Bednar",
                            "ratings": "4.27",
                            "comment": "database architect",
                            "comment_date": "January 18, 2018"
                        },
                        {
                            "review_id": 72629,
                            "user": "Inder Desai",
                            "ratings": "3.39",
                            "comment": "Garden Salad",
                            "comment_date": "January 20, 2018"
                        },
                        {
                            "review_id": 73888,
                            "user": "Olga Orn",
                            "ratings": "4.60",
                            "comment": "withdrawal",
                            "comment_date": "January 23, 2018"
                        },
                        {
                            "review_id": 63426,
                            "user": "Charuvrat Jain",
                            "ratings": "4.20",
                            "comment": "payment Denar",
                            "comment_date": "January 18, 2018"
                        },
                        {
                            "review_id": 30672,
                            "user": "Amy Larson",
                            "ratings": "4.59",
                            "comment": "Cotton AGP",
                            "comment_date": "January 17, 2018"
                        },
                        {
                            "review_id": 68179,
                            "user": "Pranay Prajapat",
                            "ratings": "5.60",
                            "comment": "wireless",
                            "comment_date": "January 8, 2018"
                        },
                        {
                            "review_id": 33980,
                            "user": "Elizabeth Mante",
                            "ratings": "2.76",
                            "comment": "Industrial",
                            "comment_date": "January 8, 2018"
                        },
                        {
                            "review_id": 2659,
                            "user": "Mahendra Chopra",
                            "ratings": "4.23",
                            "comment": "systems strategic",
                            "comment_date": "January 16, 2018"
                        },
                        {
                            "review_id": 74095,
                            "user": "Rhonda Will",
                            "ratings": "3.97",
                            "comment": "Cheese",
                            "comment_date": "January 8, 2018"
                        },
                        {
                            "review_id": 13580,
                            "user": "Anasooya Nehru",
                            "ratings": "3.00",
                            "comment": "SAS Home Games",
                            "comment_date": "January 19, 2018"
                        },
                        {
                            "review_id": 91967,
                            "user": "Joshua Buckridge",
                            "ratings": "2.98",
                            "comment": "Paradigm purple",
                            "comment_date": "January 18, 2018"
                        },
                        {
                            "review_id": 91340,
                            "user": "Aadidev Achari",
                            "ratings": "1.88",
                            "comment": "Cayman Islands Dollar Tunisian Dinar",
                            "comment_date": "January 11, 2018"
                        },
                        {
                            "review_id": 27040,
                            "user": "Mr. Willie Herzog",
                            "ratings": "5.25",
                            "comment": "olive morph",
                            "comment_date": "January 7, 2018"
                        },
                        {
                            "review_id": 26816,
                            "user": "Padma Saini",
                            "ratings": "2.25",
                            "comment": "withdrawal deposit programming",
                            "comment_date": "January 19, 2018"
                        },
                        {
                            "review_id": 97361,
                            "user": "Vernon Howell",
                            "ratings": "1.53",
                            "comment": "Handcrafted Soft Pizza Cross-platform",
                            "comment_date": "January 10, 2018"
                        },
                        {
                            "review_id": 29337,
                            "user": "Agasti Adiga",
                            "ratings": "4.35",
                            "comment": "Dynamic invoice",
                            "comment_date": "January 7, 2018"
                        },
                        {
                            "review_id": 11516,
                            "user": "Ms. Dustin Pacocha",
                            "ratings": "3.23",
                            "comment": "intuitive platforms ivory",
                            "comment_date": "January 25, 2018"
                        },
                        {
                            "review_id": 73881,
                            "user": "Ritesh Iyengar",
                            "ratings": "2.34",
                            "comment": "Plastic",
                            "comment_date": "January 17, 2018"
                        },
                        {
                            "review_id": 16399,
                            "user": "Mrs. Nettie Beahan",
                            "ratings": "1.54",
                            "comment": "Future",
                            "comment_date": "January 10, 2018"
                        },
                        {
                            "review_id": 33953,
                            "user": "Dhatri Reddy",
                            "ratings": "5.23",
                            "comment": "4th generation",
                            "comment_date": "January 9, 2018"
                        },
                        {
                            "review_id": 5016,
                            "user": "Johanna Torp",
                            "ratings": "2.58",
                            "comment": "Singapore Dollar",
                            "comment_date": "January 22, 2018"
                        },
                        {
                            "review_id": 74612,
                            "user": "Dhanadeepa Agarwal",
                            "ratings": "1.55",
                            "comment": "back-end",
                            "comment_date": "January 22, 2018"
                        },
                        {
                            "review_id": 28994,
                            "user": "Pablo Tremblay",
                            "ratings": "1.88",
                            "comment": "Legacy program markets",
                            "comment_date": "January 8, 2018"
                        },
                        {
                            "review_id": 64674,
                            "user": "Girika Gill",
                            "ratings": "5.44",
                            "comment": "firewall B2C Exclusive",
                            "comment_date": "January 26, 2018"
                        },
                        {
                            "review_id": 4154,
                            "user": "Margarita Stanton",
                            "ratings": "5.68",
                            "comment": "Games Operations",
                            "comment_date": "January 20, 2018"
                        },
                        {
                            "review_id": 19799,
                            "user": "Shantanu Embranthiri",
                            "ratings": "1.52",
                            "comment": "Gujarat",
                            "comment_date": "January 26, 2018"
                        },
                        {
                            "review_id": 34309,
                            "user": "Karen Rohan",
                            "ratings": "1.01",
                            "comment": "synthesize Concrete Organic",
                            "comment_date": "January 19, 2018"
                        },
                        {
                            "review_id": 97633,
                            "user": "Rupinder Verma",
                            "ratings": "4.42",
                            "comment": "hybrid",
                            "comment_date": "January 22, 2018"
                        },
                        {
                            "review_id": 77003,
                            "user": "Ian Upton",
                            "ratings": "3.88",
                            "comment": "Platinum calculating Sleek Granite Salad",
                            "comment_date": "January 11, 2018"
                        },
                        {
                            "review_id": 10809,
                            "user": "Vishwamitra Gowda",
                            "ratings": "3.09",
                            "comment": "Ergonomic Papua New Guinea",
                            "comment_date": "January 12, 2018"
                        },
                        {
                            "review_id": 5791,
                            "user": "Phil Prohaska",
                            "ratings": "3.84",
                            "comment": "Handmade Granite Salad",
                            "comment_date": "January 7, 2018"
                        },
                        {
                            "review_id": 40545,
                            "user": "Bela Tandon",
                            "ratings": "5.40",
                            "comment": "projection",
                            "comment_date": "January 21, 2018"
                        },
                        {
                            "review_id": 9557,
                            "user": "Shane Stroman",
                            "ratings": "3.02",
                            "comment": "Officer",
                            "comment_date": "January 7, 2018"
                        },
                        {
                            "review_id": 39257,
                            "user": "Chakravarti Asan",
                            "ratings": "3.51",
                            "comment": "Customer",
                            "comment_date": "January 12, 2018"
                        },
                        {
                            "review_id": 61872,
                            "user": "Ms. Joy Leannon",
                            "ratings": "5.97",
                            "comment": "Toys Jammu and Kashmir Karnataka",
                            "comment_date": "January 13, 2018"
                        },
                        {
                            "review_id": 11289,
                            "user": "Dayaamay Varrier",
                            "ratings": "2.84",
                            "comment": "next-generation orchid",
                            "comment_date": "January 7, 2018"
                        },
                        {
                            "review_id": 82852,
                            "user": "Darryl Greenfelder IV",
                            "ratings": "3.27",
                            "comment": "solid state orchestration Jewelery",
                            "comment_date": "January 12, 2018"
                        },
                        {
                            "review_id": 77876,
                            "user": "Geeta Reddy",
                            "ratings": "4.42",
                            "comment": "RSS",
                            "comment_date": "January 18, 2018"
                        },
                        {
                            "review_id": 19637,
                            "user": "Lela Gutmann",
                            "ratings": "3.48",
                            "comment": "compressing core",
                            "comment_date": "January 11, 2018"
                        },
                        {
                            "review_id": 84112,
                            "user": "Prayag Talwar",
                            "ratings": "4.72",
                            "comment": "Money Market Account",
                            "comment_date": "January 26, 2018"
                        },
                        {
                            "review_id": 48277,
                            "user": "Erika Reilly",
                            "ratings": "1.96",
                            "comment": "Salad",
                            "comment_date": "January 7, 2018"
                        },
                        {
                            "review_id": 36020,
                            "user": "Harita Nair",
                            "ratings": "1.79",
                            "comment": "Response",
                            "comment_date": "January 9, 2018"
                        },
                        {
                            "review_id": 92796,
                            "user": "Rex Roob",
                            "ratings": "1.09",
                            "comment": "Home Loan Account Brunei Dollar copying",
                            "comment_date": "January 26, 2018"
                        },
                        {
                            "review_id": 41650,
                            "user": "Mohan Adiga",
                            "ratings": "2.21",
                            "comment": "India Uzbekistan Sum",
                            "comment_date": "January 14, 2018"
                        },
                        {
                            "review_id": 37051,
                            "user": "Bennie Kihn",
                            "ratings": "1.65",
                            "comment": "circuit input",
                            "comment_date": "January 7, 2018"
                        },
                        {
                            "review_id": 97329,
                            "user": "Gurdev Abbott",
                            "ratings": "4.15",
                            "comment": "Buckinghamshire",
                            "comment_date": "January 8, 2018"
                        },
                        {
                            "review_id": 73513,
                            "user": "Jason Hodkiewicz",
                            "ratings": "1.68",
                            "comment": "Soft",
                            "comment_date": "January 26, 2018"
                        },
                        {
                            "review_id": 29860,
                            "user": "Atmananda Tagore",
                            "ratings": "4.16",
                            "comment": "disintermediate Money Market Account",
                            "comment_date": "January 9, 2018"
                        },
                        {
                            "review_id": 16927,
                            "user": "Naomi Becker",
                            "ratings": "5.08",
                            "comment": "FTP Baht",
                            "comment_date": "January 9, 2018"
                        },
                        {
                            "review_id": 58,
                            "user": "Chandi Trivedi",
                            "ratings": "4.67",
                            "comment": "Automotive knowledge user",
                            "comment_date": "January 19, 2018"
                        },
                        {
                            "review_id": 43457,
                            "user": "Jacquelyn Hudson IV",
                            "ratings": "5.39",
                            "comment": "Savings Account",
                            "comment_date": "January 25, 2018"
                        },
                        {
                            "review_id": 27514,
                            "user": "Anamika Pilla",
                            "ratings": "5.43",
                            "comment": "EXE e-business",
                            "comment_date": "January 8, 2018"
                        },
                        {
                            "review_id": 12252,
                            "user": "Ms. Mike Hyatt",
                            "ratings": "2.71",
                            "comment": "Belize Dollar Concrete unleash",
                            "comment_date": "January 13, 2018"
                        },
                        {
                            "review_id": 75711,
                            "user": "Ashlesh Somayaji",
                            "ratings": "5.23",
                            "comment": "Ball feed ubiquitous",
                            "comment_date": "January 10, 2018"
                        },
                        {
                            "review_id": 87352,
                            "user": "Jason Murphy",
                            "ratings": "1.18",
                            "comment": "Serbia best-of-breed",
                            "comment_date": "January 8, 2018"
                        },
                        {
                            "review_id": 5694,
                            "user": "Kannan Varma",
                            "ratings": "1.31",
                            "comment": "logistical",
                            "comment_date": "January 12, 2018"
                        },
                        {
                            "review_id": 43167,
                            "user": "Doris Larson",
                            "ratings": "5.32",
                            "comment": "integrate Jewelery Operations",
                            "comment_date": "January 13, 2018"
                        },
                        {
                            "review_id": 90189,
                            "user": "Devak Verma",
                            "ratings": "3.42",
                            "comment": "Supervisor sexy Specialist",
                            "comment_date": "January 18, 2018"
                        },
                        {
                            "review_id": 58513,
                            "user": "Richard Koch",
                            "ratings": "2.54",
                            "comment": "Punjab Bedfordshire",
                            "comment_date": "January 8, 2018"
                        },
                        {
                            "review_id": 25080,
                            "user": "Veda Patel",
                            "ratings": "4.58",
                            "comment": "Industrial",
                            "comment_date": "January 16, 2018"
                        },
                        {
                            "review_id": 33147,
                            "user": "Estelle Hoeger IV",
                            "ratings": "5.60",
                            "comment": "applications Incredible Plastic Sausages",
                            "comment_date": "January 12, 2018"
                        },
                        {
                            "review_id": 59580,
                            "user": "Ajit Ahuja",
                            "ratings": "3.49",
                            "comment": "viral bleeding-edge",
                            "comment_date": "January 26, 2018"
                        },
                        {
                            "review_id": 99498,
                            "user": "Dallas Swift",
                            "ratings": "2.95",
                            "comment": "Optimization Square",
                            "comment_date": "January 25, 2018"
                        },
                        {
                            "review_id": 10383,
                            "user": "Balagopal Jain",
                            "ratings": "5.06",
                            "comment": "Automotive Shoes",
                            "comment_date": "January 23, 2018"
                        },
                        {
                            "review_id": 80412,
                            "user": "Brandon O'Reilly MD",
                            "ratings": "5.70",
                            "comment": "Shoals",
                            "comment_date": "January 14, 2018"
                        },
                        {
                            "review_id": 57212,
                            "user": "Gautami Embranthiri",
                            "ratings": "3.83",
                            "comment": "green",
                            "comment_date": "January 13, 2018"
                        },
                        {
                            "review_id": 67280,
                            "user": "Brandon Kertzmann",
                            "ratings": "1.56",
                            "comment": "Factors",
                            "comment_date": "January 18, 2018"
                        },
                        {
                            "review_id": 61592,
                            "user": "Chandni Pothuvaal",
                            "ratings": "3.72",
                            "comment": "Diverse artificial intelligence",
                            "comment_date": "January 10, 2018"
                        },
                        {
                            "review_id": 57603,
                            "user": "Randolph Ebert III",
                            "ratings": "5.32",
                            "comment": "Personal Loan Account Regional",
                            "comment_date": "January 8, 2018"
                        },
                        {
                            "review_id": 35172,
                            "user": "Eekalabya Saini",
                            "ratings": "2.61",
                            "comment": "Steel interfaces",
                            "comment_date": "January 18, 2018"
                        },
                        {
                            "review_id": 15899,
                            "user": "Mr. Loretta Funk",
                            "ratings": "1.81",
                            "comment": "Shirt",
                            "comment_date": "January 21, 2018"
                        },
                        {
                            "review_id": 76693,
                            "user": "Rohana Gowda",
                            "ratings": "3.73",
                            "comment": "Shoes Dominican Peso Brook",
                            "comment_date": "January 13, 2018"
                        },
                        {
                            "review_id": 67006,
                            "user": "Maurice Nikolaus",
                            "ratings": "5.02",
                            "comment": "Refined SMS Walk",
                            "comment_date": "January 21, 2018"
                        },
                        {
                            "review_id": 88766,
                            "user": "Harinakshi Kaur",
                            "ratings": "4.77",
                            "comment": "deposit syndicate",
                            "comment_date": "January 7, 2018"
                        },
                        {
                            "review_id": 86296,
                            "user": "Patrick Murphy III",
                            "ratings": "1.95",
                            "comment": "USB Libyan Dinar",
                            "comment_date": "January 26, 2018"
                        },
                        {
                            "review_id": 43598,
                            "user": "Adripathi Shukla",
                            "ratings": "3.54",
                            "comment": "Wooden",
                            "comment_date": "January 13, 2018"
                        },
                        {
                            "review_id": 54227,
                            "user": "Ora Schamberger Jr.",
                            "ratings": "5.51",
                            "comment": "Keyboard",
                            "comment_date": "January 21, 2018"
                        },
                        {
                            "review_id": 77557,
                            "user": "Anish Arora",
                            "ratings": "3.38",
                            "comment": "Direct",
                            "comment_date": "January 18, 2018"
                        },
                        {
                            "review_id": 74951,
                            "user": "Blanche Hamill",
                            "ratings": "3.47",
                            "comment": "Handcrafted",
                            "comment_date": "January 25, 2018"
                        },
                        {
                            "review_id": 18901,
                            "user": "Kannen Johar",
                            "ratings": "4.11",
                            "comment": "Corporate transmitter green",
                            "comment_date": "January 13, 2018"
                        },
                        {
                            "review_id": 5019,
                            "user": "Rose Tromp DVM",
                            "ratings": "1.60",
                            "comment": "global Accountability Beauty",
                            "comment_date": "January 12, 2018"
                        },
                        {
                            "review_id": 54581,
                            "user": "Kin Chopra",
                            "ratings": "2.02",
                            "comment": "orchid",
                            "comment_date": "January 7, 2018"
                        },
                        {
                            "review_id": 66769,
                            "user": "Antonio Harris",
                            "ratings": "4.74",
                            "comment": "whiteboard Licensed Rubber Soap",
                            "comment_date": "January 19, 2018"
                        },
                        {
                            "review_id": 12795,
                            "user": "Amritambu Naik",
                            "ratings": "4.33",
                            "comment": "Designer",
                            "comment_date": "January 9, 2018"
                        },
                        {
                            "review_id": 95022,
                            "user": "Walter Johnson",
                            "ratings": "1.01",
                            "comment": "Metal",
                            "comment_date": "January 25, 2018"
                        },
                        {
                            "review_id": 91569,
                            "user": "Birjesh Gandhi",
                            "ratings": "3.12",
                            "comment": "quantifying SQL",
                            "comment_date": "January 20, 2018"
                        },
                        {
                            "review_id": 35269,
                            "user": "Janie Carroll",
                            "ratings": "4.82",
                            "comment": "Bermuda",
                            "comment_date": "January 14, 2018"
                        },
                        {
                            "review_id": 54696,
                            "user": "Kanti Ahuja",
                            "ratings": "1.63",
                            "comment": "e-enable bandwidth",
                            "comment_date": "January 23, 2018"
                        },
                        {
                            "review_id": 75041,
                            "user": "Dan Bednar",
                            "ratings": "1.38",
                            "comment": "morph navigating",
                            "comment_date": "January 13, 2018"
                        },
                        {
                            "review_id": 32174,
                            "user": "Bilva Tandon",
                            "ratings": "4.41",
                            "comment": "Uzbekistan Sum",
                            "comment_date": "January 18, 2018"
                        },
                        {
                            "review_id": 27119,
                            "user": "Claudia O'Conner MD",
                            "ratings": "1.04",
                            "comment": "International",
                            "comment_date": "January 18, 2018"
                        },
                        {
                            "review_id": 11099,
                            "user": "Prayag Bhattathiri",
                            "ratings": "2.69",
                            "comment": "Configuration",
                            "comment_date": "January 14, 2018"
                        },
                        {
                            "review_id": 5828,
                            "user": "Mrs. Brent Kovacek",
                            "ratings": "4.10",
                            "comment": "online Neck",
                            "comment_date": "January 7, 2018"
                        },
                        {
                            "review_id": 19650,
                            "user": "Gorakhanatha Gowda",
                            "ratings": "2.64",
                            "comment": "payment ADP",
                            "comment_date": "January 23, 2018"
                        },
                        {
                            "review_id": 41960,
                            "user": "Cary Halvorson",
                            "ratings": "3.30",
                            "comment": "Tamil Nadu",
                            "comment_date": "January 8, 2018"
                        },
                        {
                            "review_id": 96167,
                            "user": "Gobinda Tandon",
                            "ratings": "4.92",
                            "comment": "Interactions architectures Creative",
                            "comment_date": "January 25, 2018"
                        },
                        {
                            "review_id": 91103,
                            "user": "Debbie Reinger Jr.",
                            "ratings": "4.34",
                            "comment": "Kerala Table",
                            "comment_date": "January 12, 2018"
                        },
                        {
                            "review_id": 39620,
                            "user": "Dinesh Jha",
                            "ratings": "5.95",
                            "comment": "Gujarat interactive",
                            "comment_date": "January 7, 2018"
                        },
                        {
                            "review_id": 46496,
                            "user": "Roderick Herman",
                            "ratings": "5.02",
                            "comment": "Democratic People's Republic of Korea Small implement",
                            "comment_date": "January 7, 2018"
                        },
                        {
                            "review_id": 24885,
                            "user": "Rakesh Ganaka",
                            "ratings": "4.59",
                            "comment": "Checking Account Function-based PNG",
                            "comment_date": "January 12, 2018"
                        },
                        {
                            "review_id": 1575,
                            "user": "Calvin Waelchi",
                            "ratings": "4.60",
                            "comment": "action-items",
                            "comment_date": "January 12, 2018"
                        },
                        {
                            "review_id": 61063,
                            "user": "Baidehi Nair",
                            "ratings": "1.28",
                            "comment": "auxiliary deposit demand-driven",
                            "comment_date": "January 26, 2018"
                        },
                        {
                            "review_id": 3450,
                            "user": "Jimmy Green",
                            "ratings": "4.00",
                            "comment": "invoice override",
                            "comment_date": "January 26, 2018"
                        },
                        {
                            "review_id": 79580,
                            "user": "Girja Shah",
                            "ratings": "5.03",
                            "comment": "Hryvnia",
                            "comment_date": "January 13, 2018"
                        },
                        {
                            "review_id": 48144,
                            "user": "Ada Bashirian",
                            "ratings": "2.20",
                            "comment": "Mongolia Customer",
                            "comment_date": "January 7, 2018"
                        },
                        {
                            "review_id": 60226,
                            "user": "Naveen Kaniyar",
                            "ratings": "3.01",
                            "comment": "e-business",
                            "comment_date": "January 15, 2018"
                        },
                        {
                            "review_id": 86341,
                            "user": "Lonnie Herzog",
                            "ratings": "1.22",
                            "comment": "Hat",
                            "comment_date": "January 17, 2018"
                        },
                        {
                            "review_id": 72720,
                            "user": "Mandaakin Varma",
                            "ratings": "2.52",
                            "comment": "Persevering multi-byte",
                            "comment_date": "January 10, 2018"
                        },
                        {
                            "review_id": 89861,
                            "user": "Nick Jerde Jr.",
                            "ratings": "5.69",
                            "comment": "Cheese",
                            "comment_date": "January 25, 2018"
                        },
                        {
                            "review_id": 61604,
                            "user": "Diksha Butt",
                            "ratings": "2.71",
                            "comment": "Mobility",
                            "comment_date": "January 24, 2018"
                        },
                        {
                            "review_id": 62160,
                            "user": "Kate Dibbert",
                            "ratings": "2.89",
                            "comment": "Manipur Salad parse",
                            "comment_date": "January 13, 2018"
                        },
                        {
                            "review_id": 77029,
                            "user": "Diptendu Kocchar",
                            "ratings": "4.25",
                            "comment": "Unbranded",
                            "comment_date": "January 14, 2018"
                        },
                        {
                            "review_id": 86359,
                            "user": "Mr. Beatrice Fisher",
                            "ratings": "1.70",
                            "comment": "installation Wooden Keyboard",
                            "comment_date": "January 25, 2018"
                        }, {
                            "review_id": 94314,
                            "user": "Subhashini Talwar",
                            "ratings": "3.76",
                            "comment": "Goa",
                            "comment_date": "January 13, 2018"
                        }, {
                            "review_id": 66780,
                            "user": "Ron Runolfsdottir",
                            "ratings": "5.71",
                            "comment": "HDD",
                            "comment_date": "January 17, 2018"
                        }, {
                            "review_id": 55724,
                            "user": "Chandni Adiga",
                            "ratings": "5.48",
                            "comment": "Uttaranchal",
                            "comment_date": "January 20, 2018"
                        }, {
                            "review_id": 34750,
                            "user": "Florence Wintheiser",
                            "ratings": "3.99",
                            "comment": "pink",
                            "comment_date": "January 20, 2018"
                        }
                    ],
                    "state": "Dadar and Nagar Haveli",
                    "address": "78007, Ahluwalia Walks, Jay, Port Chandakland-333067"
                };
                $scope.details = $scope.empData;
                $scope.overview = $scope.empData.overview;

                $scope.options = ["High to Low", "Low to High", "Recent", "Oldest"];

                $scope.sum = function() {
                    let size = $scope.details.all_review.length;
                    let sum = 0
                    for (var i = 0; i < size; i++) {
                        sum += parseFloat($scope.details.all_review[i].ratings);
                    }
                    return sum;
                }

                let size = $scope.details.all_review.length;
                let total = $scope.sum();
                $scope.reviews = {
                    total: size,
                    avg: (total / size).toFixed(2),
                };
                employeeService.getEmployeeById(
                    $stateParams.employeeId).then(function() {
                    $scope.employeeData = response.data;
                }, function(errorInfo) {
                    console.log(errorInfo);
                });

                /* review data */
                employeeService
                    .getReviewList("query")
                    .then(
                        function(response) {
                            $scope.isReadyResponse = true;
                            $scope.employeeReviewData = response.data
                            $scope.reviewCount = $scope.employeeReviewData.length;
                            var totalReviewVal = 0,
                                counter = 0;

                            $scope.employeeReviewData
                                .forEach(function(
                                    review) {
                                    if (review.rating) {
                                        totalReviewVal = totalReviewVal +
                                            review.rating;
                                        counter++;
                                    }
                                });

                            if ($scope.reviewCount >= 1) {
                                $scope.averageRatingCount = Math
                                    .ceil(totalReviewVal /
                                        counter);
                            } else {
                                $scope.averageRatingCount = 0;
                            }
                        },
                        function(errorInfo) {
                            console.log(errorInfo);
                        });

                // Function to sort ratings
                $scope.sortRatings = function(order) {
                    $scope.sort = employeeService
                        .getSortingOrder(order);

                }

                //Function to view all reviews
                $scope.viewReviews = function() {
                    $scope.showRating = true;
                    $scope.ratingData = $scope.empData.all_review;
                }

            }
        ]);