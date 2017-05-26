(function() {
    'use strict';

    angular
        .module('testApp')
        .controller('LanguageDialogController', LanguageDialogController);

    LanguageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Language', 'Staff'];

    function LanguageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Language, Staff) {
        var vm = this;

        vm.language = entity;
        vm.clear = clear;
        vm.save = save;
        vm.staff = Staff.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.language.id !== null) {
                Language.update(vm.language, onSaveSuccess, onSaveError);
            } else {
                Language.save(vm.language, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('testApp:languageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
