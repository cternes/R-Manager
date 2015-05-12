'use strict';

var appServices = angular.module('appServices', []);

appServices.factory('matchService', ['$http', '$q', function($http, $q) {
    var deffered = $q.defer();
    var matches = [];
    var currentMatch = [];
    
    return {
	getMatch: function(matchId) {
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
    };
}]);