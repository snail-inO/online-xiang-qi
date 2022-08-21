package me.portfolio.library.util;

public enum BoundaryEnum {
    BOARD_BOUNDARY(0, 0, 9, 10);
    private final int left;
    private final int bottom;
    private final int right;
    private final int top;

    BoundaryEnum(int left, int bottom, int right, int top) {
        this.left = left;
        this.bottom = bottom;
        this.right = right;
        this.top = top;
    }

    public int getBottom() {
        return bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getTop() {
        return top;
    }
}
