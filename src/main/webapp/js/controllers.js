"use strict";

var aiCourseControllers = angular.module('aiCourseControllers', []);

aiCourseControllers.controller('NavTabsCtrl', ['$scope', '$location',
    function NavTabsCtrl($scope, $location) {
        $scope.tabs = [
            {name: 'Главная', url: '/main'},
            {name: 'Задание №1', url: '/assignment/1'},
            {name: 'Задание №2', url: '/assignment/2'},
            {name: 'Результаты', url: '/results'}
        ];

        $scope.isActive = function(tab) {
            return tab.url === $location.path();
        };
    }]);

aiCourseControllers.controller('MainCtrl', [
    function MainCtrl() {
    }
]);

aiCourseControllers.controller('ResultsCtrl', ['$scope', '$http',
    function ResultsCtrl($scope, $http) {
        $http.get('rest/results').success(function(data){
            $scope.results = data;
        });
    }
]);

aiCourseControllers.controller('AssignmentCtrl', ['$scope', '$routeParams',
    function AssignmentCtrl($scope, $routeParams) {
        $scope.assignment = {};
        $scope.assignment.number = $routeParams.assignmentId;
        $scope.loading = false;
        $scope.uploadComplete = function(content, completed) {
            if (completed && content) {
                console.log("Upload finished!");
                $scope.loading = false;
                var response = angular.fromJson(content);
                if (response.status !== 200) {
                    switch (response.code) {
                        case 1:
                            alert("Расширение файла неверно!\nЗагрузите файл с расширением .pkg");
                            break;
                        case 2:
                            alert("В решении найдена ошибка!\n" + response.message);
                            break;
                        case 3:
                            alert("Почтовый адрес некорректен!\nПопробуйте еще раз");
                            break;
                        case 4:
                            alert("Мухлюешь?!\nРабота точь-в-точь совпадает с уже сданной работой другого студента!");
                            break;
                        case 5:
                            alert("Ошибка сервера проверки решений!\n"
                                    + "Пожалуйста, свяжитесь с администратором (kolchinmax@niuitmo.ru). Спасибо!");
                            break;
                        default :
                            alert("Ошибка при проверке решения!\nКод ошибки: " + response.code);
                    }
                } else {
                    alert("Молодец, решение верно!\nВаш результат записан.");
                }
            } else {
                $scope.loading = true;
                console.log("Upload failed!");
                if (content !== "Please wait...") {
                    console.log(content);
                    alert("Произошла ошибка при загрузке!\n"
                            + "Пожалуйста, свяжитесь с администратором (kolchinmax@niuitmo.ru). Спасибо!");
                }
            }
        };
    }
]);