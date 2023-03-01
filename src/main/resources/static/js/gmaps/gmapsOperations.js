var pageGMap;
var stompClient = null;
var $;

function initMap() {
  var centerOfInitialMap = { lat: 38.2, lng: 21.7 };
  pageGMap = new google.maps.Map(
    document.getElementById('map-container'), { zoom: 7, center: centerOfInitialMap });
  //addMarker(centerOfInitialMap.lat,centerOfInitialMap.lng);
}

function addMarker(latitude, longitude) {
  console.log("latitude = " + latitude + " / longitude = " + longitude);
  var latNum = Number(latitude);
  var lngNum = Number(longitude);
  // The location of the map
  var loc = { lat: latNum, lng: lngNum };
  var marker = new google.maps.Marker({ position: loc, map: pageGMap });
  pageGMap.setCenter(loc);
  pageGMap.setZoom(17);

}

function addMarkerWithTitle(latitude, longitude, markerTitle) {
  console.log("latitude = " + latitude + " / longitude = " + longitude);
  var latNum = Number(latitude);
  var lngNum = Number(longitude);
  // The location of the map
  var loc = { lat: latNum, lng: lngNum };
  var marker = new google.maps.Marker({ position: loc, map: pageGMap, title: markerTitle });
  pageGMap.setCenter(loc);
  pageGMap.setZoom(17);
}

function clearMap() {
  var centerOfMap = pageGMap.getCenter();
  pageGMap = new google.maps.Map(
    document.getElementById('map-container'), { zoom: 7, center: centerOfMap });
}

function connectSocket() {
  var socket = new SockJS('/maps');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/user/page/maps-reply', function (messageOutput) {
      console.log("Received in page: 'maps-reply'");
      handleMapsReply(JSON.parse(messageOutput.body));
    });
  });
}

function disconnectSocket() {
  if (stompClient != null) {
    stompClient.disconnect();
  }
  console.log("Disconnected");
}

function sendCommandMessage(command) {
  var e = document.getElementById("deviceSelectionList");
  var value = e.value;
  stompClient.send("/app/maps-sendcommand", {},
    JSON.stringify({ 'deviceId': value, 'command': command }));

  if (command === "TRIGGER_LU") {
    stompClient.send("/app/maps-waitforlu", {}, value);
  }
}

/*
enum Alarmtype {
    NONE = "",
    INFO = "info",
    SUCCESS =  "success",
    WARNING =  "warning",
    DANGER =  "danger"
  }
*/
function handleMapsReply(commandReplyObj) {

  console.log('Received msg: error = ' + commandReplyObj.error +
    ' name = ' + commandReplyObj.name + ' command = ' +
    commandReplyObj.command + ' message = ' + commandReplyObj.message);

  var msgType = 'info';
  var msgString = '';
  var msgIcon = "pe-7s-check"; //"pe-7s-attention" // pe-7s-map-marker 

  if (commandReplyObj.error != null) {
    msgType = 'danger';
    msgString = commandReplyObj.error;
    msgIcon = "pe-7s-attention";
  } else if (commandReplyObj.message != null) {
    msgType = 'success';
    msgString = "Device " + commandReplyObj.name + "  was found !";
    msgIcon = "pe-7s-map-marker";

    // add marker on map
    addMarkerWithTitle(commandReplyObj.message.latitude, commandReplyObj.message.longitude, commandReplyObj.name);

  } else {
    msgString = "Command " + commandReplyObj.command + " sent to device " + commandReplyObj.name;
  }

  $.notify(
    {
      icon: msgIcon,
      message: msgString,
    },
    {
      type: msgType,
      timer: 2000,
      placement: {
        from: "top",
        align: "center",
      },
    }
  );
}
