(function() {
    'use strict';

    angular
        .module('testApp')
        .controller('FoodDetailController', FoodDetailController);

    FoodDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Food'];

    function FoodDetailController($scope, $rootScope, $stateParams, previousState, entity, Food) {
        var vm = this;

        vm.food = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('testApp:foodUpdate', function(event, result) {
            vm.food = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
