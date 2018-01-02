module.exports = class Room {
    constructor(name) {
        this.name = name;
        this.players = [];
        this.count = 0;
        this.level = 0;
        this.positions = [];
    }

    addPlayer(id) {
        this.players[this.count] = id;
        this.count++;
    }
    getPop(){
        return this.count;
    }

    playerListString() {
        let s = "";
        for (let i = 0; i < this.players.length; i++) {
            s += this.players[i] + "~";
        }
        return s;
    }

    getPlayers() {
        return this.players;
    }

    getName() {
        return this.name;
    }

    drop(id) {
        this.players.clean(id);
        this.count--;
    }

    getCount() {
        return this.count;
    }

    updatePos(id, pos) {
        this.positions[id] = pos;
    }

    getPositions() {
        let s = "";

        for (let i = 0; i < this.players.length; i++) {
            s += this.positions[this.players[i]] + '~';
        }
        return s;
    }
}
