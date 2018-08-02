var app = angular.module('myApp', []);
      app.controller('myCtrl', function($scope, $http, $timeout) {
       
    	$scope.sortType     = 'successDiff'; // set the default sort type
    	$scope.sortReverse  = false;  // set the default sort order
    	  
       $http({
        method : "GET",
        url : "http://localhost:5555/build/comparestats"
      }).then(function mySuccess(response) {
        
        $scope.buildData = response.data;
        angular.element(document).ready(function() {
         dTable = $('.example')
         dTable.DataTable(
        		 {
                     "order": [[ 7, "desc" ]]
                 }		 
         );
       });
        $scope.dtOptions = DTOptionsBuilder.newOptions() .withOption('order', [7, 'desc']);
        $scope.showAgentStatsBreakUp=function(stats,agentstats)
        {
         $scope.ds='';
         $http.get('http://localhost:5555/build/agentdailystats?buildip='+stats.buildIps+'&env='+stats.environament+'&class='+agentstats.className).then(function(response) {
          $scope.ds=response.data;
        });
       };

     }, function myError(response) {
      $scope.buildData = response.statusText;
 });