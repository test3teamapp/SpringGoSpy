function initMap() {
    // The location of the map
    var shop = { lat: 50.5039, lng: 4.4699 };
    var map = new google.maps.Map(
      document.getElementById('map-container'), { zoom: 4, center: shop });
    var marker = new google.maps.Marker({ position: shop, map: map });
  }