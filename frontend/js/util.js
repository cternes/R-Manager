'use strict';

function showError($scope, $timeout, text) {
    $scope.error = {
	showError: true,
	text: text
    };
    
    $timeout(function() {
	$scope.error.showError = false;
    }, 8000);
}