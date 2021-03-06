(function() {
    'use strict';
    angular
        .module('testApp')
        .factory('Staff', Staff);

    Staff.$inject = ['$resource'];

    function Staff ($resource) {
        var resourceUrl =  'api/staff/:id';

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
