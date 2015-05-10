//load config file first, then bootstrap angular afterwards
$.getJSON('js/config/config.json', function(data) {
    apiKey = data.apiKey;
    username = data.username;

    angular.bootstrap(document, ['r-manager']);
});

var app = angular.module('r-manager', []);

app.run(function($window, $rootScope) {
    console.log("app run");
});

app.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
}]);

app.controller('MatchFindingController', function($scope, $http) {

    $http.get('http://localhost:8080/players/??/matches')
	    .success(function(data, status, headers, config) {
		$scope.myMatches = data;

	    })
	    .error(function(data, status, headers, config) {
		// todo
	    });

    $scope.createMatch = function createMatch() {
	$http.post('http://localhost:8080/matches', 'playerToken=')
		.success(function(data, status, headers, config) {
		    console.log(data);
		}).
		error(function(data, status, headers, config) {
		    // todo
		});
    };

});