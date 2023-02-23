var pageGMap;
function initMap() {
   var centerOfInitialMap = { lat: 38.2, lng: 21.7 };
    pageGMap = new google.maps.Map(
      document.getElementById('map-container'), { zoom: 7, center: centerOfInitialMap });
    //addMarker(centerOfInitialMap.lat,centerOfInitialMap.lng);
  }

function addMarker(latitude,longitude){
    console.log("latitude = " + latitude + " / longitude = " + longitude);
    var latNum = Number(latitude);
    var lngNum = Number(longitude);
     // The location of the map
     var loc = { lat: latNum, lng: lngNum };
     var marker = new google.maps.Marker({ position: loc, map: pageGMap });
     pageGMap.setCenter(loc);
     pageGMap.setZoom(13);

  }

  function addMarkerWithTitle(latitude,longitude,markerTitle){
      console.log("latitude = " + latitude + " / longitude = " + longitude);
      var latNum = Number(latitude);
      var lngNum = Number(longitude);
       // The location of the map
       var loc = { lat: latNum, lng: lngNum };
       var marker = new google.maps.Marker({ position: loc, map: pageGMap, title: markerTitle });
       pageGMap.setCenter(loc);
       pageGMap.setZoom(13);
    }
