(function() {
    'use strict';

    angular
        .module('testApp')
        .controller('StaffDetailController', StaffDetailController);

    StaffDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Staff'];

    function StaffDetailController($scope, $rootScope, $stateParams, previousState, entity, Staff) {
        var vm = this;

        vm.staff = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('testApp:staffUpdate', function(event, result) {
            vm.staff = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
