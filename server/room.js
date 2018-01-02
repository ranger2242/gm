module.exports = class Room {
    constructor(name) {
        this.name = name;
        this.players = [4];
        this.count = 0;
        this.level=0;
    }

    addPlayer(id) {
        this.players[this.count] = id;
        this.count++;
    }
    playerListString(){
        let s = "";
        for (let i = 0; i < this.players.length; i++) {
            s += this.players[i] + "~";
        }
        return s;
    }
    getPlayers(){
        return this.players;
    }
    getName() {
        return this.name;
    }
    drop(id){
        this.players.clean(id);
        this.count--;
    }
    getCount() {
        return this.count;
    }
}
