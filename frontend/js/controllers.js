'use strict';

/* Controllers */

var appControllers = angular.module('appControllers', []);

appControllers.controller('LoginController', ['$scope', '$http', '$location', '$timeout',
    function($scope, $http, $location, $timeout) {
	
	$scope.login = function(name, password) {
	     $http.post('http://localhost:8080/players/login', 'name=' + name + '&password=' + password)
	    .success(function(data, status, headers, config) {
		debugger;
		localStorage.setItem('playerToken', angular.toJson(data.id));
		$location.url('/lobby');
	    })
	    .error(function(data, status, headers, config) {
		showError($scope, $timeout, data.message);
	    });
	};
    }]);

appControllers.controller('LobbyController', ['$scope', '$http', '$location', '$timeout', '$interval', 'playerService', 
    function($scope, $http, $location, $timeout, $interval, playerService) {
	
	$scope.playerToken = playerService.checkPlayerToken();

	getActiveMatches();
	// call every 5s
	var promise = $interval(function(){getActiveMatches();}, 5000);

	// stop calling getActiveMatches when scope is destroyed (e.g. on navigation)
	$scope.$on('$destroy', function() {
	    $interval.cancel(promise);
	});

	$scope.createMatch = function() {
	    $http.post('http://localhost:8080/matches', 'playerToken=' + $scope.playerToken)
		.success(function(data, status, headers, config) {
		    getActiveMatches();
		})
		.error(function(data, status, headers, config) {
		    showError($scope, $timeout, 'Could not create match.');
		});
	};
	
	$scope.joinRandomMatch = function() {
	    $http.get('http://localhost:8080/matches')
		.success(function(data, status, headers, config) {
		    var id = data.id;
		    
		    $http.post('http://localhost:8080/matches/' + id + '/join', 'playerId=' + $scope.playerToken)
			.success(function(data, status, headers, config) {
			    getActiveMatches();
			})
			.error(function(data, status, headers, config) {
			    showError($scope, $timeout, 'Could not find any open matches to join. Consider creating one.');
			});
		})
		.error(function(data, status, headers, config) {
		    showError($scope, $timeout, 'Could not find any open matches to join. Consider creating one.');
		});
	};
	
	$scope.deleteMatch = function(matchId) {
	    $http.delete('http://localhost:8080/matches/' + matchId + '?playerToken=' + $scope.playerToken)
		.success(function(data, status, headers, config) {
			getActiveMatches();
		})
		.error(function(data, status, headers, config) {
		    // todo
		});
	};
	
	function getActiveMatches() {
	    $http.get('http://localhost:8080/players/' + $scope.playerToken + '/matches')
		.success(function(data, status, headers, config) {
		    $scope.myMatches = data;
	    
		    for (var i=0;i < $scope.myMatches.length;i++) {
			var match = $scope.myMatches[i];
		    
			if(match.player1.id === $scope.playerToken) {
			    if(match.player2 === null) {
				match.opponent = {name: '?'};	
			    }
			    else {
				match.opponent = {name: match.player2.name};	
			    }
			}
			else {
			    match.opponent = {name: match.player1.name};
			}
		    }
		})
		.error(function(data, status, headers, config) {
		    showError($scope, $timeout, 'Could not retrieve active matches. Pleasy try again later.');
		});
	}
    }]);

appControllers.controller('MatchController', ['$scope', '$http', '$location', '$routeParams', '$timeout', 'matchService', 'playerService',
    function($scope, $http, $location, $routeParams, $timeout, matchService, playerService) {
	
	$scope.matchId = $routeParams.matchId;
	$scope.departmentId = $routeParams.departmentId;
	matchService.getMatch($routeParams.matchId).then(function() {
	    // set match
	    $scope.currentMatch = matchService.currentMatch();
	    
	    // set player
	    var playerToken = angular.fromJson(localStorage.getItem('playerToken'));
	    if($scope.currentMatch.player1.id===playerToken) {
		$scope.player = $scope.currentMatch.data.playerOne;
		$scope.opponent = $scope.currentMatch.data.playerTwo;
	    }
	    else {
		$scope.player = $scope.currentMatch.data.playerTwo;
		$scope.opponent = $scope.currentMatch.data.playerOne;
	    }
	    
	    // set current city
	    $scope.currentCity = getItemById($scope.player.currentCity.id, $scope.currentMatch.data.cities);
	    
	    // set number of owned buildings
	    var numBuildings = 0;
	    for (var i=0;i < $scope.player.estates.length;i++) {
		if($scope.player.estates[i].building !== null) {
		    numBuildings++;
		}
	    }
	    $scope.player.numBuildings = numBuildings;
	    
	    // set current building if available
	    var buildingId = $routeParams.buildingId;
	    if(buildingId !== undefined) {
		$scope.building = getBuildingById(buildingId, $scope.player.estates);	
	    }
	    
	    // define action list
	    if($scope.player.actions === undefined) {
		$scope.player.actions = [];	
	    }
	});
	
	$scope.buyEstate = function(estateId) {
	    var estate = getItemById(estateId, $scope.currentCity.estates);
	    estate.sold = true;
	    
	    // reduce money
	    $scope.player.money = $scope.player.money - estate.totalPrice;
	    
	    // add to player
	    $scope.player.estates.push(estate);
	    
	    // add to actions
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
	    var departments = [];
	    departments['Dininghall'] = { cabinets: [], personnel: [], type: 'Dininghall', maxSpaceUnits: 4, numberOfCabinets: 0 };
	    departments['Facilities'] = { cabinets: [], personnel: [], type: 'Facilities', maxSpaceUnits: 1, numberOfCabinets: 0 };
	    departments['Kitchen'] = { cabinets: [], personnel: [], type: 'Kitchen', maxSpaceUnits: 1, numberOfCabinets: 0 };
	    departments['Reefer'] = { cabinets: [], personnel: [], type: 'Reefer', maxSpaceUnits: 2, numberOfCabinets: 0 };
	    departments['Laundry'] = { cabinets: [], personnel: [], type: 'Laundry', maxSpaceUnits: 1, numberOfCabinets: 0 };
		
	    estate.building = {id: buildingId, buildingType: buildingType, departments: departments};
	    
	    // add to actions
	    $scope.player.actions.push({type: 3, estateId: estateId, buildingId: buildingId, buildingTypeId: buildingType.id});
	};
	
	$scope.hirePerson = function(personId) {
	    var person = getItemById(personId, $scope.currentCity.availablePersonnel);
	    person.hired = true;
	    
	    var buildingId = $routeParams.buildingId;
	    var building = getBuildingById(buildingId, $scope.player.estates);
	    
	    // add to department
	    building.departments[person.departmentType].personnel.push(person);
	    
	    // add to actions
	    $scope.player.actions.push({type: 5, personId: personId, buildingId: buildingId});
	};
	
	$scope.increaseCabinetQuantity = function(cabinet) {
	    cabinet.quantityToBuy = cabinet.quantityToBuy + 1;
	};
	
	$scope.decreaseCabinetQuantity = function(cabinet) {
	    if(cabinet.quantityToBuy !== 1) {
		cabinet.quantityToBuy = cabinet.quantityToBuy - 1;	
	    }
	};
	
	$scope.buyCabinet = function(cabinet) {
	    var buildingId = $routeParams.buildingId;
	    var building = getBuildingById(buildingId, $scope.player.estates);
	    
	    if((cabinet.quantity + cabinet.quantityToBuy) > building.departments[cabinet.departmentType].maxSpaceUnits) {
		showError($scope, $timeout, 'You cannot buy more equipment than you have capacity!');
		return;
	    }
	    
	    cabinet.quantity = cabinet.quantity + cabinet.quantityToBuy;
	    
	    // add to department
	    var existingCabinet = getItemById(cabinet.id, building.departments[cabinet.departmentType].cabinets);
	    
	    if(existingCabinet === undefined) {
		building.departments[cabinet.departmentType].cabinets.push(cabinet);	
	    }
	    
	    // add to actions
	    $scope.player.actions.push({type: 7, buildingId: buildingId, cabinetId: cabinet.id,	quantity: cabinet.quantity});
	    
	    // reduce money
	    $scope.player.money = $scope.player.money - (cabinet.quantity * cabinet.price);
	    
	    // increase cabinet number in department
	    building.departments[cabinet.departmentType].numberOfCabinets = building.departments[cabinet.departmentType].numberOfCabinets + cabinet.quantityToBuy;
	    
	    // reset quantity
	    cabinet.quantityToBuy = 1;
	};
	
	$scope.endTurn = function() {
	    var matchId = $scope.currentMatch.id;
	    var turnData = btoa(angular.toJson($scope.player.actions));
	    var playerToken = playerService.checkPlayerToken();
	    
	    $http.post('http://localhost:8080//matches/' + matchId + '/turns', 'playerToken=' + playerToken + '&turnData=' + turnData)
		.success(function(data, status, headers, config) {
		    matchService.clearMatch(matchId);
		    $location.url('/match/' + matchId + '/turnsummary');
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
	
	function getBuildingById(id, estates) {
	    for (var i=0;i < estates.length;i++) {
		if(estates[i].building.id === id) {
		    return estates[i].building;
		}
	    }
	    
	    return undefined;
	}
    }]);

appControllers.controller('TurnSummaryController', ['$scope', '$http', '$location', '$routeParams', 'matchService',
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
	});
	
    }]);
