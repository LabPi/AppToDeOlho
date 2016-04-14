angular.module('starter.controllers', [])

    .controller('AppCtrl', function ($scope, $ionicModal, $timeout) {

        // With the new view caching in Ionic, Controllers are only called
        // when they are recreated or on app start, instead of every page change.
        // To listen for when this page is active (for example, to refresh data),
        // listen for the $ionicView.enter event:
        //$scope.$on('$ionicView.enter', function(e) {
        //});

        // Form data for the login modal
        $scope.loginData = {};

        // Create the login modal that we will use later
        $ionicModal.fromTemplateUrl('templates/login.html', {
            scope: $scope
        }).then(function (modal) {
            $scope.modal = modal;
        });

        // Triggered in the login modal to close it
        $scope.closeLogin = function () {
            $scope.modal.hide();
        };

        // Open the login modal
        $scope.login = function () {
            $scope.modal.show();
        };

        // Perform the login action when the user submits the login form
        $scope.doLogin = function () {
            console.log('Doing login', $scope.loginData);

            // Simulate a login delay. Remove this and replace with your login
            // code if using a login system
            $timeout(function () {
                $scope.closeLogin();
            }, 1000);
        };
    })

    .controller('PlaylistsCtrl', function ($scope) {
        $scope.playlists = [
            {
                title: 'Ponte Sobre o Rio Tocantins',
                subtitle: 'Imperatriz-MA, Bairro Beira Rio',
                id: 1,
                image: 'img/video1.jpg',
                textogrande: 'Ponte que fará a ligação entre os estados do Maranhão e Tocantins, facilitando o trânsito entre a população e dessa forma diminuindo o tempo de viagem.'
            },
            {title: 'Quadra Polispostiva', subtitle: 'Palmas-TO, 906 Sul', id: 2, image: 'img/video2.jpg', textogrande: 'Construção de quadra polispostiva da quadra 906 sul em Palmas-TO'},
            {title: 'Escola Infatil', subtitle: 'Araguaina-TO, Setor Sudoeste', id: 3, image: 'img/video3.jpg', textogrande: 'Construção de escola infantil de tempo integral na cidade de Araguiana-TO '}
        ];
        $scope.navTitle = '<img Style="width:48px; margin-top:5px; margin-left: 50%; left:-24px; position: relative" src="img/logo.jpg" />';
    })

    .controller('PlaylistCtrl', function ($scope, $stateParams) {
    })

    .controller('MapController', function($scope, $ionicLoading, $compile) {
        function initialize() {
            var myLatlng = new google.maps.LatLng(-10.184898, -48.400469);

            var mapOptions = {
                center: myLatlng,
                zoom: 13,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
            var map = new google.maps.Map(document.getElementById("map"),
                mapOptions);

            //Marker + infowindow + angularjs compiled ng-click
            var contentString = "<div style='margin: auto'><a href='#/app/playlists/1' <!--ng-click='clickTest()'-->><img style='width: 140px; margin: auto' src='img/video1.jpg'><br><b>Construção da Ponte Sobre o Rio Tocantins</b></a></div>";
            var compiled = $compile(contentString)($scope);

            var infowindow = new google.maps.InfoWindow({
                content: compiled[0]
            });

            var marker = new google.maps.Marker({
                position: myLatlng,
                map: map,
                title: 'Uluru (Ayers Rock)'
            });

            google.maps.event.addListener(marker, 'click', function() {
                infowindow.open(map,marker);
            });

            $scope.map = map;
        }
        google.maps.event.addDomListener(window, 'load', initialize);

        $scope.centerOnMe = function() {
            if(!$scope.map) {
                return;
            }

            $scope.loading = $ionicLoading.show({
                content: 'Getting current location...',
                showBackdrop: false
            });

            navigator.geolocation.getCurrentPosition(function(pos) {
                $scope.map.setCenter(new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude));
                $scope.loading.hide();
            }, function(error) {
                alert('Unable to get location: ' + error.message);
            });
        };

        $scope.clickTest = function() {
            
        };

    })
;
