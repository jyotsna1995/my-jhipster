(function() {
    'use strict';

    angular
        .module('testApp')
        .controller('LanguageDetailController', LanguageDetailController);

    LanguageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Language', 'Staff'];

    function LanguageDetailController($scope, $rootScope, $stateParams, previousState, entity, Language, Staff) {
        var vm = this;

        vm.language = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('testApp:languageUpdate', function(event, result) {
            vm.language = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
