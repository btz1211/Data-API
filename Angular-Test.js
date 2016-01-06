(function(){
	var dataApi = angular.module('dataApi', []);
	var version = "1.0";
	
	//the main app controller
	dataApi.controller('AppController', function(){
		this.setMenuTab = 1;
		
		this.selectMenuTab = function(setTab){
			this.setMenuTab = setTab;
		};
		
		this.menuTabSelected = function(checkTab){
			return this.setMenuTab === checkTab;
		};
	});
	
	//custom validation for data method
	dataApi.directive('methodValidator', function(){
		return{
			require: 'ngModel',
			restrict: 'A',
			link: function(scope, elem, attr, ngModel){
				ngModel.$parsers.unshift(function(value){
					if(value){
						scope.getDataMethodInfo(value, 'dataMethodValidator', ngModel);
						return value;
					}
				});			
			}
		};
	});
	
	//custom validation for data source
	dataApi.directive('sourceValidator', function(){
		return{
			require: 'ngModel',
			restrict: 'A',
			link: function(scope, elem, attr, ngModel){
				ngModel.$parsers.unshift(function(value){
					if(value){
						scope.getDataSourceInfo(value, 'dataSourceValidator', ngModel);
						return value;
					}
				});			
			}
		};
	});
	
	
	dataApi.controller('DataController', function($scope, $http){
		$scope.data = [];
		$scope.dataHeaders = [];
		$scope.previousData = [];
		$scope.parameters = {};
		$scope.fetchSize = null;
		$scope.startIndex = null;
		$scope.linkToNextBatch = null;
		$scope.dataRequestError = null;
		
		$scope.sqlCommand = "";
		
		//fetch data
		$scope.fetchData = function(url){
			$scope.data = [];
			$scope.dataHeaders = [];
			$scope.linkToNextBatch = null;
			$scope.dataRequestError = null;
			
			//build url
			if(url == null){
				//new request, wipe out previous data cache
				$scope.previousData = [];
				
				//build url
				var url = 'http://localhost:8002/v1/data?sql_command='+$scope.sqlCommand;
			}
			
			alert('url::'+url);
			
			$http.get(url).then(function(result){
			
				if(result.status === 200){
					//populate data and link to next
					$scope.data = result.data.data;
					//$scope.linkToNextBatch = result.data.link; 
					
					//set data header
					if($scope.data.length > 0){
						var row = $scope.data[0];				
						for(var name in row){
							if(row.hasOwnProperty(name)){
								$scope.dataHeaders.push(name);
							}
						}
					}
				}else{
					$scope.dataRequestError = {};
					$scope.dataRequestError['error_message'] = result.statusText;
				}
				
			}, function(result){
				if(result.data.error){
					$scope.dataRequestError = result.data.error;
				}else{
					$scope.dataRequestError = {};
					$scope.dataRequestError['error_message'] = result.statusText;
				}
			});
		};
		
		//set data method info
		$scope.getDataMethodInfo = function(dataMethod, directive, ngModel){
			//reset parameters
			$scope.parameters = {};
			
			$http.get('http://localhost:8002/v1/data_methods?id='+dataMethod).
			success(function(result){
				if(result.data_methods != null && result.data_methods.length > 0){
					ngModel.$setValidity(directive, true)
					$scope.dataMethodInfo = result.data_methods[0];
					$scope.dataMethodValid = true;
					
					//add parameters
					for(var i = 0; i< $scope.dataMethodInfo.params.length; ++i){
						$scope.parameters[$scope.dataMethodInfo.params[i].name] = null;
					}
					
				}else{
					ngModel.$setValidity(directive, false)
				}
			});
		};
		
		//set data source info
		$scope.getDataSourceInfo = function(dataSource, directive, ngModel){
			$http.get('http://localhost:8002/v1/data_sources?id='+dataSource).success(function(result){
				if(result.data_sources != null && result.data_sources.length > 0){
					$scope.dataSourceInfo = result.data_sources[0];
					ngModel.$setValidity(directive, true)
				}else{
					ngModel.$setValidity(directive, false)
				}
			});
		};
		
		//get next batch
		$scope.getNextBatch = function(){
			var previousBatchInfo = {};
			previousBatchInfo['data'] = $scope.data;
			previousBatchInfo['link'] = $scope.linkToNextBatch;
			
			$scope.previousData.push(previousBatchInfo);
			$scope.fetchData($scope.linkToNextBatch.href.replace(/127\.0\.0\.1/, 'http://localhost'));
		}
		
		//get previous batch
		$scope.getPreviousBatch = function(){
			var previousBatch = $scope.previousData.pop();
			$scope.data = previousBatch.data;
			$scope.linkToNextBatch = previousBatch.link;
		}
	});
	
	
	

})();

