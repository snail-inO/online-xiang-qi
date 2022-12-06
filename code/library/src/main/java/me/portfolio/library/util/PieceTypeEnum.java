package me.portfolio.library.util;

public enum PieceTypeEnum {
    SHUAI(0, 4, 0, 1, 97),
    SHI(0, 3, 1, 1, 2),
    XIANG(0, 2, 2, 2, 2),
    MA(0, 1, 1, 2, 4),
    JU(0, 0, 0, 10, 9),
    PAO(2, 1, 0, 10, 4.5F),
    BING(3, 0, 0, 1, 1);

    private final int initRow;
    private final int initCol;
    private final int min;
    private final int max;
    private final float score;

    PieceTypeEnum(int initRow, int initCol, int min, int max, float score) {
        this.initRow = initRow;
        this.initCol = initCol;
        this.min = min;
        this.max = max;
        this.score = score;
    }

    public int getInitRow() {
        return initRow;
    }

    public int getInitCol() {
        return initCol;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public float getScore() {
        return score;
    }
}
