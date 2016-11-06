$(function () {
    $('input:radio[name="category"]').change(function () {
        if (category !== $(this).val()) {
            category = $(this).val();
            connect();
        }
    });

    var category = null;
    var socket = null;
    function connect() {
        try {            
            if (socket !== null) {
                socket.onclose();
            }
            socket = new WebSocket("ws://localhost:8080/ca2/notes/" + category);
            socket.onopen = function (evt) {
                $("#notes").val("Connected to \n" + $("#notes").val());
                $.each(evt.data, function (index, note) {
                        displayNote(note);
                    });
            };
            socket.onmessage = function (evt) {
//                var data = JSON.parse(evt.data);
//                var note = data.time + ": " 
//                            + (category === "all") ? data.category + ": " : "" 
//                            + data.title + ": " + data.content;
//                
//                $("#notes").val(note + "\n" + $("#notes").val());
                displayNote(evt.data);
            };
            socket.onclose = function () {
                $("#notes").val("");
            };
        } catch (exception) {
            message('<p>Error' + exception);
        }
    }
    
    function displayNote(incomingData) {
        var data = JSON.parse(incomingData);
        var postedNotes = data.isnotes;
        if (postedNotes === null) {
        var note = data.title 
                        + ": " + data.time 
                        + ": " + data.who 
                        + (category === "all") ? ": " + data.category : ""
                        + ": " + data.content;
            }
                
        $("#notes").val(note + "\n" + $("#notes").val());
    }
});
