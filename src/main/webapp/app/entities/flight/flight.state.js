(function() {
    'use strict';

    angular
        .module('testApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('flight', {
            parent: 'entity',
            url: '/flight?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Flights'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flight/flights.html',
                    controller: 'FlightController',
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
        .state('flight-detail', {
            parent: 'flight',
            url: '/flight/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Flight'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flight/flight-detail.html',
                    controller: 'FlightDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Flight', function($stateParams, Flight) {
                    return Flight.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'flight',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('flight-detail.edit', {
            parent: 'flight-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flight/flight-dialog.html',
                    controller: 'FlightDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Flight', function(Flight) {
                            return Flight.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flight.new', {
            parent: 'flight',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flight/flight-dialog.html',
                    controller: 'FlightDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                flno: null,
                                orgn: null,
                                dest: null,
                                flda: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('flight', null, { reload: 'flight' });
                }, function() {
                    $state.go('flight');
                });
            }]
        })
        .state('flight.edit', {
            parent: 'flight',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flight/flight-dialog.html',
                    controller: 'FlightDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Flight', function(Flight) {
                            return Flight.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flight', null, { reload: 'flight' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flight.delete', {
            parent: 'flight',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flight/flight-delete-dialog.html',
                    controller: 'FlightDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Flight', function(Flight) {
                            return Flight.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flight', null, { reload: 'flight' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
