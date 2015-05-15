'use strict';

var appServices = angular.module('appServices', []);

appServices.factory('matchService', ['$http', '$q', function($http, $q) {
    var matches = [];
    var currentMatch = [];
    
    return {
	getMatch: function(matchId) {
	    var deffered = $q.defer();
	    var match = matches[matchId];


	    if(match === undefined) {
		$http.get('http://localhost:8080/matches/' + matchId)
		    .success(function(data, status, headers, config) {
			matches[matchId] = data;
			matches[matchId].data = angular.fromJson(atob(data.matchData)); 
			
			currentMatch = matches[matchId];
			deffered.resolve();
		    })
		    .error(function(data, status, headers, config) {
			// todo
		    });
		    
		return deffered.promise;
	    }
	    else {
		currentMatch = matches[matchId];
		
		deffered.resolve();
		return deffered.promise;
	    }
	}
	,currentMatch: function() { return currentMatch; }
	,clearMatch: function(matchId) {
	    matches[matchId] = undefined;
	}
    };
}]);

appServices.factory('playerService', ['$http', '$location', function($http, $location) {
    return {
	checkPlayerToken: function() {
	    var playerToken = angular.fromJson(localStorage.getItem('playerToken'));

	    if(playerToken === null) {
		$location.url('/');
	    }
	    else {
		return 	playerToken;
	    }
	}
    };
}]);