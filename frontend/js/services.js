'use strict';

var appServices = angular.module('appServices', []);

appServices.factory('matchService', ['$http', function($http) {
    var matches = [];
    
    return {
	getMatch: function(matchId) {
	    var match = matches[matchId];

	    if(match === undefined) {
		$http.get('http://localhost:8080/matches/' + matchId)
		    .success(function(data, status, headers, config) {
			matches[matchId] = data;
			matches[matchId].data = angular.fromJson(atob(data.matchData)); 
		    })
		    .error(function(data, status, headers, config) {
			// todo
		    });
	    }
	}
    };
}]);