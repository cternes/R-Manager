'use strict';

/* Controllers */

var appControllers = angular.module('appControllers', []);

appControllers.controller('LoginController', ['$scope', '$http', '$location',
    function($scope, $http, $location) {
	
	$scope.getPlayer = function getPlayer(name) {
	     $http.get('http://localhost:8080/players/' + name)
	    .success(function(data, status, headers, config) {
		localStorage.setItem('playerToken', angular.toJson(data.id));
		$location.url('/lobby');
	    })
	    .error(function(data, status, headers, config) {
		// todo
	    });
	};
    }]);

appControllers.controller('LobbyController', ['$scope', '$http', '$location',
    function($scope, $http, $location) {
	
	var playerToken = angular.fromJson(localStorage.getItem('playerToken'));
	
	$http.get('http://localhost:8080/players/' + playerToken + '/matches')
	    .success(function(data, status, headers, config) {
		$scope.myMatches = data;
	    })
	    .error(function(data, status, headers, config) {
		// todo
	    });

	$scope.createMatch = function createMatch() {
	    $http.post('http://localhost:8080/matches', 'playerToken=' + playerToken)
		.success(function(data, status, headers, config) {
		    // todo
		})
		.error(function(data, status, headers, config) {
		    // todo
		});
	};
	
	$scope.joinRandomMatch = function joinRandomMatch() {
	    $http.get('http://localhost:8080/matches')
		.success(function(data, status, headers, config) {
		    var id = data.id;
		    
		    $http.post('http://localhost:8080/matches/' + id + '/join', 'playerId=' + playerToken)
			.success(function(data, status, headers, config) {
			    // todo
			})
			.error(function(data, status, headers, config) {
			    // todo
			});
		})
		.error(function(data, status, headers, config) {
		    // todo
		});
	};
    }]);

appControllers.controller('MatchController', ['$scope', '$http', '$location',
    function($scope, $http, $location) {
	
	$http.get('http://localhost:8080/matches/14c46d2d-3516-47ad-8a33-72ddf8586ed9')
	    .success(function(data, status, headers, config) {
		$scope.currentMatch = data;
		$scope.currentMatch.data = angular.fromJson(atob(data.matchData));
	    })
	    .error(function(data, status, headers, config) {
		// todo
	    });
	
    }]);