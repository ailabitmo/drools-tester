"use strict";

var aiCourse = angular.module('ai-course', [
    'ngRoute',
    'ngUpload',
    'aiCourseControllers'
]);

aiCourse.config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/main', {
            templateUrl: 'partials/main.html',
            controller: 'MainCtrl'
        }).when('/assignment/:assignmentId', {
            templateUrl: 'partials/assignment.html',
            controller: 'AssignmentCtrl'
        }).when('/results', {
            templateUrl: 'partials/results.html',
            controller: 'ResultsCtrl'
        }).otherwise({
            redirectTo: '/main'
        });
    }]);