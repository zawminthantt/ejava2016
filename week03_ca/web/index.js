/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


(function() {
    var e = angular.module("AppointmentApp", ["ui.router"]);
    var t = function(e, t) {
        e.state("people", {
            url: "/people",
            templateUrl: "views/people.html",
            controller: "PeopleCtrl as peopleCtrl"
        }).state("appointment", {
            url: "/appointment",
            templateUrl: "views/appointment.html",
            controller: "AppointmentCtrl as appointmentCtrl"
        });
        t.otherwise("/people")
    };
    var n = function(e, t, n, i) {
        this.showPanel = function(t) {
            e.$broadcast("Appointment:show-panel", t)
        };
        this.findByEmail = function(e) {
            var i = n.defer();
            t.get("api/people", {
                params: {
                    email: e
                }
            }).then(function(e) {
                i.resolve(e.data)
            }, function(e) {
                i.reject(e)
            });
            return i.promise
        };
        this.createPeople = function(e) {
            var a = n.defer();
            t({
                method: "POST",
                url: "api/people",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                data: i(e)
            }).then(function() {
                a.resolve()
            }, function(e) {
                a.reject(e)
            });
            return a.promise
        };
        this.getAllAppointments = function(e) {
            var i = n.defer();
            t.get("api/appointment/" + e).then(function(e) {
                i.resolve(e.data)
            }, function(e) {
                i.reject(e)
            });
            return i.promise
        };
        this.createAppointment = function(e) {
            var a = n.defer();
            t({
                method: "POST",
                url: "api/appointment",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                data: i(e)
            }).then(function() {
                a.resolve()
            }, function(e) {
                a.reject(e)
            });
            return a.promise
        }
    };
    var i = function(e) {
        var t = this;
        t.email = "";
        t.date = null;
        t.description = "";
        t.validEmail = false;
        t.invalidEmail = false;
        t.people = {};
        t.appointments = [];
        t.verify = function() {
            e.findByEmail(t.email).then(function(e) {
                t.people = e;
                t.validEmail = true;
                t.invalidEmail = false;
                return t.email
            }, function(e) {
                t.invalidEmail = true;
                t.validEmail = !t.invalidEmail;
                console.error(e)
            }).then(e.getAllAppointments).then(function(e) {
                t.appointments = e
            }, function(t) {
                e.showPanel({
                    failure: true,
                    title: "Get appointments",
                    message: "Cannot get the list of appointments for the person"
                })
            })
        };
        t.add = function() {
            e.createAppointment({
                email: t.email,
                date: t.date.getTime(),
                description: t.description
            }).then(function() {
                return t.email
            }).then(e.getAllAppointments).then(function(e) {
                t.appointments = e;
                t.date = null;
                t.description = ""
            }).catch(function(t) {
                e.showPanel({
                    failure: true,
                    title: "Create Appointment",
                    message: t
                })
            })
        }
    };
    var a = function(e) {
        var t = this;
        t.showPanel = false;
        t.name = "";
        t.email = "";
        t.dismissPanel = function() {
            t.showPanel = false
        };
        t.add = function() {
            e.createPeople({
                name: t.name,
                email: t.email
            }).then(function() {
                e.showPanel({
                    success: true,
                    title: "Add People",
                    message: "Added " + t.name
                });
                t.name = "";
                t.email = ""
            }, function(t) {
                e.showPanel({
                    failure: true,
                    title: "Add People",
                    message: "Error " + JSON.stringify(t)
                })
            })
        }
    };
    var o = function(e) {
        var t = this;
        t.showPanel = false;
        t.success = false;
        t.failure = false;
        t.title = "";
        t.message = "";
        t.dismissPanel = function() {
            t.showPanel = false
        };
        e.$on("Appointment:show-panel", function(e, n) {
            t.success = n.success || false;
            t.failure = n.failure || false;
            t.title = n.title || "Info";
            t.message = n.message;
            t.showPanel = true
        })
    };
    e.config(["$stateProvider", "$urlRouterProvider", t]);
    e.service("AppointmentSvc", ["$rootScope", "$http", "$q", "$httpParamSerializerJQLike", n]);
    e.controller("PeopleCtrl", ["AppointmentSvc", a]);
    e.controller("AppointmentCtrl", ["AppointmentSvc", i]);
    e.controller("MainCtrl", ["$scope", o])
})();