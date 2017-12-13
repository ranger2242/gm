var app = require('express')();
var server = require('http').Server();
var io = require('socket.io')(server);
var players = [];
var rocksList = [];
var playerCount = 0;
var ships = [];
var started = false;
var death=[];

io.listen(2242, function () {
    console.log("Server is now running...");
});

// Add a connect listener
io.on('connection', function (socket) {
    players[playerCount] = socket.id;
    socket.emit('storeIndex', playerCount, socket.id);
    slog(playerCount + " :" + socket.id);
    playerCount += 1;
    slog(players);
    if (started)
        socket.emit("start", null);


    console.log('Client connected.');

    socket.on('start', function () {
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

    });

    socket.on('disconnect', function () {
        var index = players.indexOf(socket.id);
        players.splice(index, 1);
        players.clean(index);
        ships.splice(index, 1);
        ships.clean(index);
        playerCount = players.length;
        emitAll(socket, 'syncID', players);
        slog(players);
        socket.emit('storeIndex', players.indexOf(socket.id));
        if(playerCount===0)
            started=false;
        console.log(socket.id + ': Client disconnected.');
    });
});


function emitAll(socket, event, arg) {
    socket.emit(event, arg);
    socket.broadcast.emit(event, arg);
}

function slog(msg) {
    console.log("SERVER :" + msg);

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
