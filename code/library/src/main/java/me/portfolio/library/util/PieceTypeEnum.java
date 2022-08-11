package me.portfolio.library.util;

public enum PieceTypeEnum {
    SHUAI(0, 4, 0, 1),
    SHI(0, 3, 1, 1),
    XIANG(0, 2, 2, 2),
    MA(0, 1, 1, 2),
    JU(0, 0, 0, 10),
    PAO(2, 1, 0, 10),
    BING(3, 0, 0, 1);

    private final int initRow;
    private final int initCol;
    private final int min;
    private final int max;

    PieceTypeEnum(int initRow, int initCol, int min, int max) {
        this.initRow = initRow;
        this.initCol = initCol;
        this.min = min;
        this.max = max;
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
}
