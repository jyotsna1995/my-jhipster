(function() {
    'use strict';
    angular
        .module('testApp')
        .factory('Food', Food);

    Food.$inject = ['$resource'];

    function Food ($resource) {
        var resourceUrl =  'api/foods/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
