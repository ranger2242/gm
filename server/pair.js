/**
 * Pair
 * @todo Implement algebraic structures: Setoid, Functor
 */
var Pair = function(fst, snd) {
    if (this instanceof Pair) {
        if (Array.isArray(fst) && fst.length === 2 && typeof snd == 'undefined') {
            this[0] = fst[0];
            this[1] = fst[1];
        } else {
            this[0] = fst;
            this[1] = snd;
        }
        this.length = 2;
    } else {
        return new Pair(fst, snd);
    }
};
Pair.prototype.fst = function() {
    return this[0];
};
Pair.prototype.snd = function() {
    return this[1];
};
Pair.of = Pair.prototype.of = function(a1, a2) {
    return new Pair(a1, a2);
};

/**
 * Create fst() and snd() functions
 */
var fst = function(pair) {
    if (pair instanceof Pair) {
        return pair.fst();
    } else if (Array.isArray(pair) && pair.length === 2) {
        return pair[0];
    }
    throw 'Not a pair: ' + pair;
};
var snd = function(pair) {
    if (pair instanceof Pair) {
        return pair.snd();
    } else if (Array.isArray(pair) && pair.length === 2) {
        return pair[1];
    }
    throw 'Not a pair: ' + pair;
};