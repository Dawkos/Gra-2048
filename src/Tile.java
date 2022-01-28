class Tile {
    private boolean merged;
    private int value;

    Tile(int val) {
        value = val;
    }

    int getValue() {
        return value;
    }

    void setMerged() {
        merged = false;
    }

    boolean canMergeWith(Tile other) {
        return !merged && other != null && !other.merged && value == other.getValue() && value<8192;
    }

    int mergeWith(Tile other) {
        if (canMergeWith(other)) {
            value *= 2;
            merged = true;
            return value;
        }
        return -1;
    }
}