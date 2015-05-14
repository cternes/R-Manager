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

appControllers.controller('LobbyController', ['$scope', '$http', '$location', 'playerService', 
    function($scope, $http, $location, playerService) {
	
	var playerToken = playerService.checkPlayerToken();
	
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

appControllers.controller('MatchController', ['$scope', '$http', '$location', '$routeParams', 'matchService',
    function($scope, $http, $location, $routeParams, matchService) {
	
	$scope.matchId = $routeParams.matchId;
	matchService.getMatch($routeParams.matchId).then(function() {
	    // set match
	    $scope.currentMatch = matchService.currentMatch();
	    
	    // set player
	    var playerToken = angular.fromJson(localStorage.getItem('playerToken'));
	    if($scope.currentMatch.player1.id===playerToken) {
		$scope.player = $scope.currentMatch.data.playerOne;
	    }
	    else {
		$scope.player = $scope.currentMatch.data.playerTwo;
	    }
	    
	    // set current city
	    $scope.currentCity = getCityById($scope.player.currentCity.id);
	});
	
	$scope.buyEstate = function(estateId) {
	    var estate = getEstateById(estateId);
	    estate.isSold = true;
	    
	    // reduce money
	    $scope.player.money = $scope.player.money - estate.totalPrice;
	    
	    // add to player
	    $scope.player.estates.push(estate);
	    
	    // add to actions
	    if($scope.player.actions === undefined) {
		$scope.player.actions = [];
	    }
	     
	    $scope.player.actions.push({type: 1, id: estateId});
	};
	
	$scope.buyBuilding = function(estateId, buildingTypeId) {
	    var estate = getItemById(estateId, $scope.player.estates);
	    var buildingType = getItemById(buildingTypeId, $scope.currentMatch.data.buildingTypes);
	    
	    // reduce money
	    $scope.player.money = $scope.player.money - buildingType.price;
	    
	    // get building id
	    var buildingId = $scope.currentMatch.data.buildingIds.pop();
	    
	    // add to estate
	    estate.building = {id: buildingId, buildingType: buildingType};
	    
	    // add to actions
	    $scope.player.actions.push({type: 3, estateId: estateId, buildingId: buildingId, buildingTypeId: buildingType.id});
	};
	
	function getEstateById(id) {
	    for (var i=0;i < $scope.currentCity.estates.length;i++) {
		if($scope.currentCity.estates[i].id === id) {
		    return $scope.currentCity.estates[i];
		}
	    }
	    
	    return undefined;
	}
	
	function getCityById(id) {
	    for (var i=0;i < $scope.currentMatch.data.cities.length;i++) {
		if($scope.currentMatch.data.cities[i].id === id) {
		    return $scope.currentMatch.data.cities[i];
		}
	    }
	    
	    return undefined;
	}
	
	function getItemById(id, list) {
	    for (var i=0;i < list.length;i++) {
		if(list[i].id === id) {
		    return list[i];
		}
	    }
	    
	    return undefined;
	}
    }]);