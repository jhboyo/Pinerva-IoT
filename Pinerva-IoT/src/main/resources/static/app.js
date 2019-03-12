/**
 * 
 */

var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}



//The connect() function uses SockJS and stomp.js to open a connection to "/gs-guide-websocket", 
//which is where our SockJS server is waiting for connections. 
function connect() {
	// create a new socket
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        
        //destination
        stompClient.subscribe('/topic/greetings', function (greeting) {
        	
            showGreeting(JSON.parse(greeting.body).content);
            
        });
    });
}


function showGreeting(message) {
	//console.log("message: " + message)
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}


//The sendName() function retrieves the name entered by the user and 
//uses the STOMP client to send it to the "/app/hello" destination 
//(where GreetingController.greeting() will receive it).
function sendName() {
	
 stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
 
}





function disconnect() {
	
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}






$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});