var RxVisibilityApplication = angular.module('RxVisibilityApplication', [ 'ngRoute' ]);

RxVisibilityApplication.config(
		
		function($routeProvider,$locationProvider) {
			$locationProvider.html5Mode(false);
			
			$routeProvider.when('/', {
				templateUrl : 'pages/menuPage.html',
				controller : 'genRXPageController',
				controllerAs : 'genRXPageController'
			});
			
		} );

RxVisibilityApplication.factory('LoadMainTransaction', function($http, $window) {

	return {
		
		fetchItemInformation : function(url, storeNumber, ndcNumber) {
			return $http({
				url : url,
				method : "GET",
				params : {
					'fetchRequest' : ndcOrderRequest
				},
				
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded'
				}
			});
		},
		
	};
});

RxVisibilityApplication
		.controller(
				'genRXPageController',['$route','$scope','$http','$location','$timeout','$interval','$window','LoadMainTransaction',
						function($route,$scope, $http, $location,$timeout,$interval,$window, LoadMainTransaction) {
							
							/* Angular Variables */
							$scope.transactionPage = 1;
							$scope.searchPage = 1;
							$scope.manualInput = 'pages/searchPage.html';
							$scope.showMenu=true;
							$scope.user = [];
							$scope.storeNumber="";
							$scope.ndcNumber="";
							$scope.fieldValue="";
							$scope.dconhand = "";
							$scope.value = "";
							$scope.errorDescriptionList = [];
							
							$http.get('properties/rxvcommon.properties').success(function(response) {
								$scope.fieldValue = response;
								
							});
							
							$scope.fetchDCInformation = function(storeNumber,ndcNumber) {
								$scope.fieldValue="";
								$scope.jsonObj="";
								
								$http.get('properties/rxvcommon.properties').success(function(response) {
									$scope.fieldValue = response;
								
								$scope.dconhand = "";
								$scope.errorDescriptionList = [];
								
								var isStoreValid = true;
								var isNdcValid = true;
								if($scope.checkingEmpty(storeNumber)) {
									isStoreValid = false;
									$scope.errorDescriptionList.push(angular.copy($scope.fieldValue.rxv_error_field_enter_store));
								}else if (!$scope.validateNumeric(storeNumber)){
									isStoreValid = false;
									$scope.errorDescriptionList.push(angular.copy($scope.fieldValue.rxv_error_field_notNumeric_store));
								}else if (storeNumber.length >5){
									isStoreValid = false;
									$scope.errorDescriptionList.push(angular.copy($scope.fieldValue.rxv_error_field_invalid_enter_store));
								}
								if($scope.checkingEmpty(ndcNumber)) {
									isNdcValid = false;
									$scope.errorDescriptionList.push(angular.copy($scope.fieldValue.rxv_error_field_enter_ndc));
								}else if (!$scope.validateNumeric(ndcNumber)){
									isNdcValid = false;
									$scope.errorDescriptionList.push(angular.copy($scope.fieldValue.rxv_error_field_notNumeric_ndc));
								}else if (ndcNumber.length >10){
									isNdcValid = false;
									$scope.errorDescriptionList.push(angular.copy($scope.fieldValue.rxv_error_field_invalid_enter_ndc));
								}
								
								if(isStoreValid && isNdcValid){
									$scope.dconhand = '3813';
									$scope.ndcdescription = 'ACTIQ 400MCG LOZ   30';
									
									jsonObj = {
											storeNumber : storeNumber,
											ndcNumber  : ndcNumber
									};
									
									LoadMainTransaction
									.fetchItemInformation($scope.fieldValue.rxv_webservice_url,
											storeNumber , ndcNumber )
									.success(
											function(data, status, headers,
													config) {
												//console.log(data);
												
												});
									$http.jsonp($scope.fieldValue.rxv_webservice_url+'storeNumber=234&ndcNumber=4567'
											encoding+type+"&callback=JSON_CALL_BACK");
									
									$http.jsonp($scope.fieldValue.rxv_webservice_url+'storeNumber=234&ndcNumber=4567'
											+"&callback=JSON_CALL_BACK");
								}
								});
							};	
							
							if((window.location.href.indexOf('?') > 0)){
                                var splittingUrl = window.location.href.slice(window.location.href.indexOf('?') + 1).split('=');
                                splitdata = splittingUrl[1].split('&');
                                vars=splitdata[1];
                                $scope.storeNumber = splitdata[0];
                                $scope.ndcNumber = splittingUrl[2];
                                $scope.fetchDCInformation(splitdata[0],splittingUrl[2]);
                                }      
							
							$scope.validateNumeric = function(object) {
								var refNumeric = /^[0-9]+$/;		
								if (!object.match(refNumeric)) {
									return false;
								}
								return true;
							};
							
							$scope.checkingEmpty=function(object){
								var emptyValue=/^$/;
							if (!object.match(emptyValue)) {
									return false;
								}
								return true;
							};
								}]);




							