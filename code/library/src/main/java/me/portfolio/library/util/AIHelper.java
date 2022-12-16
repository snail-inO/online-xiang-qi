package me.portfolio.library.util;


import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.entity.State;
import me.portfolio.library.entity.Tensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public interface AIHelper {
    static int maxChildIndex(State state, PieceColorEnum color) {
        int sign = color.equals(PieceColorEnum.BLACK) ? -1 : 1;
        int maxChild = -1;
        Float maxValue = null;
        List<State> children = state.getChildren();
        System.out.println();
        for (int i = 0; i < children.size(); i++) {
            if (state.getDistribution()[state.getActions().get(i)[0]] * sign <= 0) {
                continue;
            }
            Float score = sign * children.get(i).getEstimateScore();
            System.out.print(score + " ");
            if (maxValue == null || score > maxValue) {
                maxChild = i;
                maxValue = score;
            }
        }
        System.out.println();
        System.out.println(maxValue);

        return maxChild;
    }

    static Tensor toTensor(State state) {
        int[] temp = state.getDistribution();
        Tensor tensor = new Tensor(null, new ArrayList<>());
        for (int v : temp) {
            Boolean[] index = new Boolean[15];
            if (v < 0) {
                v = -v + 7;
            }
            index[v] = true;
            tensor.getBoard().add(Arrays.asList(index));
        }

        return tensor;
    }
    static int leastVisitIndex(State state, PieceColorEnum color) {
        int sign = color.equals(PieceColorEnum.BLACK) ? -1 : 1;
        List<State> children = state.getChildren();
        int lowestCount = children.get(0).getVisitCount();
        int lowestIndex = 0;
        for (int i = 1; i < children.size(); i++) {
            boolean validAction = state.getDistribution()[state.getActions().get(i)[0]] * sign > 0;
            if (validAction && children.get(i).getVisitCount() < lowestCount) {
                lowestIndex = i;
            }
        }

        return lowestIndex;
    }

    static int[] transformAction(Piece prePiece, Piece postPiece) {
        return new int[]{GetPieceIndex.getIndex(prePiece), GetPieceIndex.getIndex(postPiece)};
    }

    static List<Piece> transformAction(Board board, int[] action) {
        List<Piece> res = new ArrayList<>();
        Piece curPiece = board.getPieces().get(action[0]);
        Piece nextPiece = new Piece(curPiece);

        res.add(curPiece);
        nextPiece.setRow(action[1] / BoundaryEnum.BOARD_BOUNDARY.getRight());
        nextPiece.setCol(action[1] % BoundaryEnum.BOARD_BOUNDARY.getRight());
        res.add(nextPiece);

        return res;
    }

    static Piece generatePiece(int index, int num) {
        Piece piece = new Piece(PieceTypeEnum.values()[Math.abs(num) - 1]);
        piece.setColor(num < 0 ? PieceColorEnum.BLACK : PieceColorEnum.RED);
        piece.setAlive(true);
        piece.setRow(index / BoundaryEnum.BOARD_BOUNDARY.getRight());
        piece.setCol(index % BoundaryEnum.BOARD_BOUNDARY.getRight());

        return piece;
    }

    static State performAction(State curState, int[] action) {
        State nextState = new State();
        nextState.setDistribution(curState.getDistribution().clone());
//         System.out.println();
//         System.out.println("action 0: " + action[0]);
//         System.out.println("action 1: " + action[1]);
        nextState.getDistribution()[action[1]] = nextState.getDistribution()[action[0]];
        nextState.getDistribution()[action[0]] = 0;

        return nextState;
    }

    static Board stateToBoard(State state) {
        Board board = new Board();
        board.setPieces(new HashMap<>());

        for (int i = 0; i < state.getDistribution().length; i++) {
            if (state.getDistribution()[i] == 0) {
                continue;
            }
            board.getPieces().put(i, generatePiece(i, state.getDistribution()[i]));
        }

        return board;
    }
}
