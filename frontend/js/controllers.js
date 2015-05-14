'use strict';

/* Controllers */

var appControllers = angular.module('appControllers', []);

appControllers.controller('LoginController', ['$scope', '$http', '$location',
    function($scope, $http, $location) {
	
	$scope.getPlayer = function(name) {
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

appControllers.controller('LobbyController', ['$scope', '$http', '$location', '$timeout', 'playerService', 
    function($scope, $http, $location, $timeout, playerService) {
	
	var playerToken = playerService.checkPlayerToken();
	
	getActiveMatches();

	$scope.createMatch = function() {
	    $http.post('http://localhost:8080/matches', 'playerToken=' + playerToken)
		.success(function(data, status, headers, config) {
		    getActiveMatches();
		})
		.error(function(data, status, headers, config) {
		    // todo
		});
	};
	
	$scope.joinRandomMatch = function() {
	    $http.get('http://localhost:8080/matches')
		.success(function(data, status, headers, config) {
		    var id = data.id;
		    
		    $http.post('http://localhost:8080/matches/' + id + '/join', 'playerId=' + playerToken)
			.success(function(data, status, headers, config) {
			    getActiveMatches();
			})
			.error(function(data, status, headers, config) {
			    $scope.error = {showError: true, text: 'Could not find any open matches to join. Consider creating one.'};
		    
			    $timeout(function() { 
				//debugger;
				$scope.error.showError = false;}, 5000);
			});
		})
		.error(function(data, status, headers, config) {
		    // todo
		});
	};
	
	$scope.deleteMatch = function(matchId) {
	    $http.delete('http://localhost:8080/matches/' + matchId + '?playerToken=' + playerToken)
		.success(function(data, status, headers, config) {
			getActiveMatches();
		})
		.error(function(data, status, headers, config) {
		    // todo
		});
	};
	
	function getActiveMatches() {
	    $http.get('http://localhost:8080/players/' + playerToken + '/matches')
		.success(function(data, status, headers, config) {
		    $scope.myMatches = data;
		})
		.error(function(data, status, headers, config) {
		    // todo
		});
	}
    }]);

appControllers.controller('MatchController', ['$scope', '$http', '$location', '$routeParams', 'matchService', 'playerService',
    function($scope, $http, $location, $routeParams, matchService, playerService) {
	
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
	    $scope.currentCity = getItemById($scope.player.currentCity.id, $scope.currentMatch.data.cities);
	    
	    var numBuildings = 0;
	    for (var i=0;i < $scope.player.estates.length;i++) {
		if($scope.player.estates[i].building !== null) {
		    numBuildings++;
		}
	    }
	    $scope.player.numBuildings = numBuildings;
	});
	
	$scope.buyEstate = function(estateId) {
	    var estate = getItemById(estateId, $scope.currentCity.estates);
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
	
	$scope.endTurn = function() {
	    debugger;
	    var matchId = $scope.currentMatch.id;
	    var turnData = btoa(angular.toJson($scope.player.actions));
	    var playerToken = playerService.checkPlayerToken();
	    
	    $http.post('http://localhost:8080//matches/' + matchId + '/turns', 'playerToken=' + playerToken + '&turnData=' + turnData)
		.success(function(data, status, headers, config) {
		    // todo
		})
		.error(function(data, status, headers, config) {
		    // todo
		});
	};
	
	function getItemById(id, list) {
	    for (var i=0;i < list.length;i++) {
		if(list[i].id === id) {
		    return list[i];
		}
	    }
	    
	    return undefined;
	}
    }]);