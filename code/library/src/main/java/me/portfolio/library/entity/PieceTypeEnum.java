package me.portfolio.library.entity;

public enum PieceTypeEnum {
    SHUAI(0, 4),
    SHI(0, 3),
    XIANG(0, 2),
    MA(0, 1),
    JU(0, 0),
    PAO(2, 1),
    ZU(3, 0);

    private final int initRow;
    private final int initCol;

    PieceTypeEnum(int initRow, int initCol) {
        this.initRow = initRow;
        this.initCol = initCol;
    }

    public int getInitRow() {
        return initRow;
    }

    public int getInitCol() {
        return initCol;
    }
}
