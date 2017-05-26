(function() {
    'use strict';
    angular
        .module('testApp')
        .factory('Flight', Flight);

    Flight.$inject = ['$resource', 'DateUtils'];

    function Flight ($resource, DateUtils) {
        var resourceUrl =  'api/flights/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.flda = DateUtils.convertDateTimeFromServer(data.flda);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
