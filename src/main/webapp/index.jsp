<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html lang="run" ng-app="ai-course">
    <head>
        <title>Система проверки решений</title>
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
            <a href="https://github.com/ailabitmo/drools-tester">
              <img style="position: absolute; top: 0; left: 0; border: 0;" 
                src="https://camo.githubusercontent.com/567c3a48d796e2fc06ea80409cc9dd82bf714434/68747470733a2f2f73332e616d617a6f6e6177732e636f6d2f6769746875622f726962626f6e732f666f726b6d655f6c6566745f6461726b626c75655f3132313632312e706e67" 
                alt="Fork me on GitHub" data-canonical-src="https://s3.amazonaws.com/github/ribbons/forkme_left_darkblue_121621.png">
            </a>
            <div class="ai-header">
                <h1 class="text-center">Система проверки решений</h1>
            </div>
            <ul ng-controller="NavTabsCtrl" id="nav-tabs" class="nav nav-tabs">
                <li ng-repeat="tab in tabs" ng-class="{active: isActive(tab)}"><a href="{{'#' + tab.url}}">{{tab.name}}</a></li>
            </ul>

            <div ng-view></div>

            <footer class="ai-footer">
                Разработано на кафедре <a href="http://iam.ifmo.ru">Информатики и прикладной математики</a> @ <a href="http://ifmo.ru">Университет ИТМО</a>
            </footer>
        </div>
    </body>
</html>
