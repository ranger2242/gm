var app = require('express')();
var server = require('http').Server();
var io = require('socket.io')(server);
var players = [];
var rooms = [];
var roomKeys = [];
var playerCount = 0;
var roomCount = 0;
var ships = [];
var started = false;

const Room = require('./room.js');

io.listen(2242, function () {
    console.log("Server is now running...");
});
// Add a connect listener
io.on('connection', function (socket) {
    players[playerCount] = socket.id;
    playerCount += 1;
    socket.emit('storeID', socket.id, playerCount);
    slog(playerCount + " :" + socket.id, 0);
    sendList(socket, players, "sendPlayerList");
    sendRoomList(socket);

    socket.on('makeRoom', function (name) {
        makeRoom(socket, name);

    });
    socket.on('joinRoom', function (name) {
        joinRoom(socket, name);

    });
    socket.on('dropPlayer', function (room, id) {
        dropPlayer(socket, room, id);
    });
    socket.on('start', function (room) {
        startRoom(room);
    });
    socket.on('disconnect', function () {
        var index = players.indexOf(socket.id);
        players.splice(index, 1);
        players.clean(index);
        ships.splice(index, 1);
        ships.clean(index);
        playerCount = players.length;
        emitAll(socket, 'syncID', players);
        socket.emit('storeIndex', players.indexOf(socket.id));
        if (playerCount === 0)
            started = false;
        slog(socket.id, -1);
        sendList(socket, players, "sendPlayerList");
        sendRoomList(socket);

    });
});

function startRoom(room) {
    if (rooms[room]){
        emitToList(rooms[room].getPlayers(),"start",null);
        slog(room,5);
    }
}

function makeRoom(socket, name) {
    if (!rooms[name]) {
        rooms[name] = new Room(name);
        roomKeys[roomCount] = name;
        roomCount++;
        sendRoomList(socket);
        slog(name, 2);
    }

}

function dropPlayer(socket,room, id) {
    rooms[room].drop(id);
    slog(room + " : " + id, 4);
    emitToList(rooms[room].getPlayers(), "roomNames", rooms[room].playerListString());
}

function joinRoom(socket, room) {
    if (rooms[room].getCount() < 4) {
        rooms[room].addPlayer(socket.id);
        slog(socket.id + " : " + room, 3)
        socket.emit("joinSuccess", rooms[room].getName());
        emitToList(rooms[room].getPlayers(), "roomNames", rooms[room].playerListString());
    } else
        socket.emit("joinFail");
}

function emitToList( list, event, arg) {
    for (let j = 0; j < list.length; j++) {
        io.to(list[j]).emit(event, arg);
    }
}

function emitAll(socket, event, arg) {
    socket.emit(event, arg);
    socket.broadcast.emit(event, arg);
}

function sendRoomList(socket) {
    var s = "";
    for (var i = 0; i < roomCount; i++) {
        var key = roomKeys[i];
        if (rooms[key])
            s += rooms[key].getName() + "~";
    }
    emitAll(socket, "sendRoomList", s);

}

function sendList(socket, list, msg) {
    var s = "";
    for (var i = 0; i < list.length; i++) {
        s += list[i] + "~";
    }
    emitAll(socket, msg, s);

}

function slog(msg, type) {
    var s = "";
    var d = new Date();
    var a = d.toLocaleDateString() + "::" + d.toLocaleTimeString() + ": ";
    var r = d.getDate() + " " + d.getTime();
    var datetime = d.getDate() + "/"
        + (d.getMonth() + 1) + "/"
        + d.getFullYear() + "::"
        + d.getHours() + ":"
        + d.getMinutes() + ":"
        + d.getSeconds() + " ";
    switch (type) {
        case -1:
            s += "DISCONNECTED:\t";

            break;
        case 0:
            s += "CONNECTED:\t";
            break;
        case 1:
            s += "MESSAGE:\t";
            break;
        case 2:
            s += "ROOM CREATE:\t";
            break;
        case 3:
            s += "JOIN ROOM:\t";
            break;
        case 4:
            s += "ROOM DROP:\t";
            break;
        case 5:
            s += "GAME START:\t";
            break;
        default:
            s += "UNDEF:\t";
            break;
    }
    console.log(a + s + msg);

}

Array.prototype.clean = function (deleteValue) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == deleteValue) {
            this.splice(i, 1);
            i--;
        }
    }
    return this;
};

