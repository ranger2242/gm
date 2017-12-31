var app = require('express')();
var server = require('http').Server();
var io = require('socket.io')(server);
var players = [];
var rooms = [];
var rocksList = [];
var playerCount = 0;
var roomCount = 0;
var ships = [];
var started = false;
var death = [];

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
    sendList(socket, rooms, "sendRoomList");

    socket.on('makeRoom', function (name) {
        if (!rooms.includes(name)) {
            rooms[roomCount] =Room(name);
            roomCount++;
            sendList(socket, rooms, "sendRoomList");
            slog(name, 2);
        }
    });
    socket.on('joinRoom', function (name) {

    });
    /* socket.on('start', function () {
         emitAll(socket, "start", null);
         started = true;
         slog("Game Started");
     });
     socket.on('stop', function () {
        started=false;
     });
     socket.on('getCount', function () {
         socket.emit('receiveCount',playerCount);
     });
     socket.on('sendShip', function (tri) {
         ships[players.indexOf(socket.id)] = JSON.parse(tri);
     });
     socket.on('syncRocks', function (rocks) {
         rocksList = JSON.parse(rocks);
         socket.broadcast.emit('receiveRocks', rocksList);
     });
     socket.on('addRocks', function (r1, r2) {
         slog("Adding 2");
         socket.broadcast.emit('addRocks', r1, r2);

     });
     socket.on('addRock', function (r1) {
         slog("Adding 1");
         emitAll(socket,  'addRock', r1);

     });
     socket.on('sendDeath', function (b) {
         death[players.indexOf(socket.id)]=b;
         var v= true;
         for(var i=0;i<death.length;i++){
             if(!death[i])
                 v=false;
         }

         socket.emit("allDead",v);
     });
     socket.on('removeRocks', function (ind, r1, r2) {
         slog("REMOVE:"+ ind);
         socket.broadcast.emit('removeRock', ind, r1, r2);
     });
     socket.on('pullRocks', function () {
         socket.emit('receiveRocks', rocksList);
     });
     socket.on('getShips', function () {
         var s = [];
         var index = players.indexOf(socket.id);
         var c = 0;
         for (var i = 0; i < ships.length; i++) {
             if (i !== index) {
                 s[c] = ships[i];
                 c++;
             }
         }
         socket.emit('receiveShips', ships);

     });*/

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
        sendList(socket, rooms, "sendRoomList");

    });
});


function emitAll(socket, event, arg) {
    socket.emit(event, arg);
    socket.broadcast.emit(event, arg);
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
            break
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

class Room {
    constructor(name) {
        this.name=name;
        this.players=[4];
        this.count=0;
    }
    addPlayer(id){
        this.players[count]=id;
        this.count++;
    }
}