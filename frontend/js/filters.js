'use strict';

angular.module('appFilters', []).filter('money', function() {
  return function(input) {
    return accounting.formatNumber(input);
  };
});