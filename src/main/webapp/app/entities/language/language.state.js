(function() {
    'use strict';

    angular
        .module('testApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('language', {
            parent: 'entity',
            url: '/language?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Languages'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/language/languages.html',
                    controller: 'LanguageController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('language-detail', {
            parent: 'language',
            url: '/language/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Language'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/language/language-detail.html',
                    controller: 'LanguageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Language', function($stateParams, Language) {
                    return Language.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'language',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('language-detail.edit', {
            parent: 'language-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/language/language-dialog.html',
                    controller: 'LanguageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Language', function(Language) {
                            return Language.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('language.new', {
            parent: 'language',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/language/language-dialog.html',
                    controller: 'LanguageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                region: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('language', null, { reload: 'language' });
                }, function() {
                    $state.go('language');
                });
            }]
        })
        .state('language.edit', {
            parent: 'language',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/language/language-dialog.html',
                    controller: 'LanguageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Language', function(Language) {
                            return Language.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('language', null, { reload: 'language' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('language.delete', {
            parent: 'language',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/language/language-delete-dialog.html',
                    controller: 'LanguageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Language', function(Language) {
                            return Language.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('language', null, { reload: 'language' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
