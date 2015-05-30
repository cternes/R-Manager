'use strict';

var app = angular.module('r-manager', [
    'ngRoute',
    'appControllers',
    'appServices',
    'appFilters'
]);

app.config(['$routeProvider',
    function($routeProvider) {
	$routeProvider.
		when('/', {
		    templateUrl: 'views/login.html',
		    controller: 'LoginController'
		}).
		when('/create-account', {
		    templateUrl: 'views/create-account.html',
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
		  controller: 'MatchController'
		}).
		when('/match/:matchId/estates', {
		  templateUrl: 'views/estates.html',
		  controller: 'MatchController'
		}).
		when('/match/:matchId/buildings', {
		  templateUrl: 'views/buildings.html',
		  controller: 'MatchController'
		}).
		when('/match/:matchId/turnsummary', {
		  templateUrl: 'views/turnsummary.html',
		  controller: 'TurnSummaryController'
		}).
		when('/match/:matchId/employees', {
		  templateUrl: 'views/employees.html',
		  controller: 'MatchController'
		}).
		when('/match/:matchId/employees/:buildingId/current', {
		  templateUrl: 'views/current-employees.html',
		  controller: 'MatchController'
		}).
		when('/match/:matchId/employees/:buildingId/hire', {
		  templateUrl: 'views/hire-employees.html',
		  controller: 'MatchController'
		}).
		when('/match/:matchId/inventory', {
		  templateUrl: 'views/inventory.html',
		  controller: 'MatchController'
		}).
		when('/match/:matchId/inventory/:buildingId/buy/:departmentId', {
		  templateUrl: 'views/buy-inventory.html',
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