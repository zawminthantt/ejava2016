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
            socket.onopen = function () {
                $("#notes").val("Connected to \n" + $("#notes").val());
            };
            socket.onmessage = function (evt) {
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
        var note = "";
        if ($.isArray(data)) {
            if (data !== null) {
                $.each(data, function (index, eachNote) {
                    note = note + buildMessage(eachNote);
                });
            }
        } else {
            note = buildMessage(data)
        }

        $("#notes").val(note + $("#notes").val());
    }

    function buildMessage(note) {
        var msg = note.title + ": " + note.time + ": " + note.who + ((category === "all") ? (": " + note.category) : "") + ": " + note.content + "\n";
        return msg;
    }
});
