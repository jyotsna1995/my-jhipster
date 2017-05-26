(function() {
    'use strict';

    angular
        .module('testApp')
        .controller('FlightDetailController', FlightDetailController);

    FlightDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Flight'];

    function FlightDetailController($scope, $rootScope, $stateParams, previousState, entity, Flight) {
        var vm = this;

        vm.flight = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('testApp:flightUpdate', function(event, result) {
            vm.flight = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
