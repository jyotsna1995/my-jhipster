(function() {
    'use strict';

    angular
        .module('testApp')
        .controller('StaffDialogController', StaffDialogController);

    StaffDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Staff'];

    function StaffDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Staff) {
        var vm = this;

        vm.staff = entity;
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
            if (vm.staff.id !== null) {
                Staff.update(vm.staff, onSaveSuccess, onSaveError);
            } else {
                Staff.save(vm.staff, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('testApp:staffUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
