function activate() {
  var mobileFormData = JSON.stringify({
    'mobile': $("#mobile").val(),
  });

  console.log("here we are")
  $.ajax({
    type: "POST",
    url: "http://localhost:8080/api/auth/getcode",
    data: mobileFormData,
    contentType: "application/json;charset=utf-8",
    success: function(result){
        $("#activationCode").val(result.activationCode);
    }
  });
}

function login() {
  var loginFormData = JSON.stringify({
    'mobile': $("#mobile").val(),
    'activationCode': $("#activationCode").val(),
  });

  $.ajax({
    type: "POST",
    url: "http://localhost:8080/api/auth/login",
    data: loginFormData,
    contentType: "application/json;charset=utf-8",
    success: function(result){
        $("#accessToken").val(result.accessToken);
    }
  });
}

function loadContacts() {
  var getcontactsFormData = JSON.stringify({
    'accessToken': $("#accessToken").val(),
  });

  $("#contactlist").empty();
  $.ajax({
    type: "POST",
    url: "http://localhost:8080/api/auth/getcontacts",
    data: getcontactsFormData,
    contentType: "application/json;charset=utf-8",
    success: function(result){
        console.log("here the contacts: ", JSON.stringify(result));
        result.contacts.forEach(function(c) {
            console.log(c.user.mobile);
            $("#contactlist").append('<tr><td><input type="checkbox" id="sendTo" value="' + c.contactUserId + '">' + c.user.mobile + '</input></td></tr>');
        });
    }
  });
}

// credits: https://www.javainuse.com/spring/boot-websocket
var ws;
function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	if(connected) {
	    loadContacts();
	    $("#chatbox").show();
	} else {
	    $("#chatbox").hide();
	}
}

function connect() {
	ws = new WebSocket('ws://localhost:8080/messaging?accessToken=' + $("#accessToken").val());
    ws.addEventListener('error', (error) => {
		console.log("Error: ", error.message);
		setConnected(false);
    });
    ws.addEventListener('close', (event) => {
		console.log("Close: ", event.code + " - " + event.reason);
	    setConnected(false);
    })
	ws.onmessage = function(event) {
		helloWorld(event.data);
		console.log("Connected to websocket " + event.data);
	}
	setConnected(true);
}

function disconnect() {
	if (ws != null) {
		ws.close();
	}
	$(".chatbox").hide();
	setConnected(false);
	console.log("Websocket is in disconnected state");
}

function sendData() {
	var data = JSON.stringify({
	  'topic': 'SEND_MESSAGE',
	  'message': {
	    'accessToken': $("#accessToken").val(),
	    'sendTo': $("#sendTo").val(),
	    'msg': $("#messageArea").val(),
	  }
	})
	ws.send(data);
}

function helloWorld(message) {
	$("#chatmessages").append("<tr><td>" + message + "</td></tr>");
}

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
    $("#activate").click(function() {
        activate();
    });
    $("#login").click(function() {
        login();
    });
	$("#connect").click(function() {
		connect();
	});
	$("#disconnect").click(function() {
		disconnect();
	});
	$("#send").click(function() {
		sendData();
	});
});
