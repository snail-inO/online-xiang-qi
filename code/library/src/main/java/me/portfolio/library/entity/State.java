package me.portfolio.library.entity;

import me.portfolio.library.util.BoundaryEnum;
import me.portfolio.library.util.PieceColorEnum;
import me.portfolio.library.util.PieceTypeEnum;

import java.util.Arrays;
import java.util.List;

public class State {
    private int[] distribution;
    private int visitCount;
    private Float totalScore;
    private Float estimateScore;
    private Float utility;
    private List<State> children;
    private List<int[]> actions;

    public State() {
    }

    public State(Board board) {
        children = null;
        utility = Float.MIN_VALUE;
        visitCount = 0;
        totalScore = null;
        estimateScore = null;
        distribution = new int[BoundaryEnum.BOARD_BOUNDARY.getRight() * BoundaryEnum.BOARD_BOUNDARY.getTop()];
        board.getPieces().entrySet().forEach(entry -> {
            Piece piece = entry.getValue();
            if (piece.isAlive()) {
                distribution[entry.getKey()] = (piece.getType().ordinal() + 1) * (piece.getColor().equals(PieceColorEnum.BLACK) ? -1 : 1);
            }
        });
    }

    public boolean isLeaf() {
        return getUtility() > 48 || getUtility() < -48;
    }

    public int[] getDistribution() {
        return distribution;
    }

    public void setDistribution(int[] distribution) {
        this.distribution = distribution;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public void visitCountPlus(int operand) {
        visitCount += operand;
    }

    public Float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Float totalScore) {
        this.totalScore = totalScore;
    }

    public void totalScorePlus(Float operand) {
        if (totalScore == null) {
            totalScore = 0F;
        }
        totalScore += operand;
    }

    public Float getEstimateScore() {
        if (isLeaf()) {
            return getUtility();
        }
        if (estimateScore == null) {
            return getUtility();
        }
        return estimateScore;
    }

    public void setEstimateScore(Float estimateScore) {
        this.estimateScore = estimateScore;
    }

    public Float getUtility() {
        if (utility == null) {
            utility = 0F;
            for (int piece : distribution) {
                if (piece == 0) {
                    continue;
                }
                utility += PieceTypeEnum.values()[Math.abs(piece) - 1].getScore() * (piece < 0 ? -1 : 1);
            }
        }

        return utility;
    }

    public void setUtility(Float utility) {
        this.utility = utility;
    }

    public List<State> getChildren() {
        return children;
    }

    public void setChildren(List<State> children) {
        this.children = children;
    }

    public List<int[]> getActions() {
        return actions;
    }

    public void setActions(List<int[]> actions) {
        this.actions = actions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return Arrays.equals(getDistribution(), state.getDistribution());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getDistribution());
    }
}
