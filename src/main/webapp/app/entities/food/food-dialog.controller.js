(function() {
    'use strict';

    angular
        .module('testApp')
        .controller('FoodDialogController', FoodDialogController);

    FoodDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Food'];

    function FoodDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Food) {
        var vm = this;

        vm.food = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.food.id !== null) {
                Food.update(vm.food, onSaveSuccess, onSaveError);
            } else {
                Food.save(vm.food, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('testApp:foodUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
