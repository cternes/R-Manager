'use strict';

var app = angular.module('r-manager', [
    'ngRoute',
    'appControllers',
    'appServices'
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
		when('/match/:matchId', {
		  templateUrl: 'views/match.html',
		  controller: 'MatchController'
		}).
		when('/match/:matchId/trainstation', {
		  templateUrl: 'views/trainstation.html',
		  controller: 'TrainstationController'
		}).
		otherwise({
		    redirectTo: '/'
		});
    }]);

app.config(['$httpProvider',
    function($httpProvider) {
	$httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
    }]);