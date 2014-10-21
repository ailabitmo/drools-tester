<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html lang="run" ng-app="ai-course">
    <head>
        <title>Курс "Интеллектуальные системы" - Система проверки решений</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta charset="utf-8">
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap-theme.min.css">
        <link rel="stylesheet" type="text/css" href="css/main.css">
        <script src="http://code.angularjs.org/1.2.1/angular.min.js"></script>
        <script src="http://code.angularjs.org/1.2.1/angular-route.min.js"></script>
        <script src="lib/angular/ng-upload.min.js"></script>
        <script src="js/app.js"></script>
        <script src="js/controllers.js"></script>
    </head>
    <body>
        <div class="container">
            <div class="ai-header">
                <h1 class="text-center">Система проверки решений</h1>
                <p class="text-center"><em>для курса <a href="https://versal.com/c/ntyptp/">"Интеллектуальные системы"</a></em></p>
            </div>
            <ul ng-controller="NavTabsCtrl" id="nav-tabs" class="nav nav-tabs">
                <li ng-repeat="tab in tabs" ng-class="{active: isActive(tab)}"><a href="{{'#' + tab.url}}">{{tab.name}}</a></li>
            </ul>

            <div ng-view></div>

            <footer class="ai-footer">
                Разработано в <a href="http://ailab.ifmo.ru">Лаборатории интеллектуальных систем</a>
            </footer>
        </div>
    </body>
</html>
