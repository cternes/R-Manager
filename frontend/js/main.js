'use strict';

var app = angular.module('r-manager', [
    'ngRoute',
    'appControllers'
]);

app.config(['$routeProvider',
    function($routeProvider) {
	$routeProvider.
		when('/', {
		    templateUrl: 'views/login.html',
		    controller: 'LoginController'
		}).
		when('/lobby', {
		  templateUrl: 'views/lobby.html',
		  controller: 'LobbyController'
		}).
		when('/match', {
		  templateUrl: 'views/match.html',
		  controller: 'MatchController'
		}).
		when('/trainstation', {
		  templateUrl: 'views/trainstation.html',
		  controller: 'MatchController'
		}).
		otherwise({
		    redirectTo: '/'
		});
    }]);

app.config(['$httpProvider',
    function($httpProvider) {
	$httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
    }]);