package me.portfolio.application.service;

import me.portfolio.application.DAO.UserDAO;
import me.portfolio.library.entity.*;
import me.portfolio.library.exceptions.InvalidOperationException;
import me.portfolio.library.service.PieceStrategySelector;
import me.portfolio.library.util.AIHelper;
import me.portfolio.library.util.BoundaryEnum;
import me.portfolio.library.util.GetPieceIndex;
import me.portfolio.library.util.PieceColorEnum;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class AIServiceImpl {
    private static final Map<State, State> states = new HashMap<>();
    private static UserDAO userDAO;
    private static User user = null;
    private static PieceColorEnum color;

    private AIServiceImpl() {
    }

    public static User getUser() {
        if (user == null) {
            user = new User();
            user.setName("AI");
            user = userDAO.save(user);
        }
        return user;
    }

    public static int getNodeCount() {
        int size = states.size();
        states.clear();
        return size;
    }

    public static void setUserDAO(UserDAO userDAO) {
        AIServiceImpl.userDAO = userDAO;
    }

    public static void clearStates() {
        states.clear();
    }

    public static List<Piece> act(Game game, int strategy, PieceColorEnum setColor) {
        Board board = game.latestBoard();
        State curState = new State(game.latestBoard());
        states.putIfAbsent(curState, curState);
        curState = states.get(curState);
        if (setColor == null) {
            color = getColor(game);
        } else {
            color = setColor;
        }

        int nextIndex = act(curState, strategy);
//        System.out.println("next action: " + Arrays.toString(curState.getActions().get(nextIndex)));
        return AIHelper.transformAction(board, curState.getActions().get(nextIndex));
    }

    private static int act(State curState, int strategy) {
        if (strategy == 2) {
            if (curState.getChildren() == null) {
                curState.setActions(validActions(curState));
            }
            return uniformlyRandom(curState);
        }
        for (int i = 0; i < 10; i++) {
//            System.out.println("rollout: " + i);
            rollout(curState, strategy, 20, 1);
        }
        return AIHelper.maxChildIndex(curState, color);
    }

    private static float rollout(State state, int strategy, int depth, int sign) {
        float res;
        if (depth <= 0 || state.isLeaf()) {
//            System.out.println("find leaf: " + state + ", score: " + state.getEstimateScore());
            return state.getEstimateScore();
        } else {
            res = rollout(chooseNext(state, strategy, sign), strategy, depth - 1, sign * -1);
        }
        state.visitCountPlus(1);
        state.totalScorePlus(res);
        state.setEstimateScore(state.getTotalScore() / state.getVisitCount());

//        System.out.println("depth: " + depth + ", state: " + state + ", visit count: " + state.getVisitCount() + ", total score: " + state.getTotalScore() + ", estimate score: " + state.getEstimateScore());
        return res;
    }

    private static State chooseNext(State state, int strategy, int sign) {
        if (state.getChildren() == null) {
            state.setActions(validActions(state));
        }

        switch (strategy) {
            case 1:
                return UCT(state, sign);
            case 3:
                return CNN(state, sign);
            default:
                throw new InvalidOperationException(AIServiceImpl.class, "1");
        }
    }

    private static int uniformlyRandom(State state) {
        Random rnd = new Random();
        int sign = color.equals(PieceColorEnum.BLACK) ? -1 : 1;

        int nextIndex = rnd.nextInt(state.getChildren().size());
//        State nextSate = state.getChildren().get(rnd.nextInt(state.getChildren().size()));
        int[] action = state.getActions().get(nextIndex);
        while (state.getDistribution()[action[0]] * sign < 0) {
            nextIndex = rnd.nextInt(state.getChildren().size());
//            nextSate = state.getChildren().get(rnd.nextInt(state.getChildren().size()));
            action = state.getActions().get(nextIndex);
        }

        return nextIndex;
    }

    private static State exploitation(State state) {
        return state.getChildren().get(AIHelper.maxChildIndex(state, color));
    }

    private static State exploration(State state) {
        return state.getChildren().get(AIHelper.leastVisitIndex(state, color));
    }

    private static State UCT(State state, int sign2) {
        int sign = color.equals(PieceColorEnum.BLACK) ? -1 : 1;

        List<Double> utilities = new ArrayList<>();
        List<Integer> index = new ArrayList<>();
//        System.out.println("evaluate " + color + ", sign: " + sign2 * sign);
        for (int i = 0; i < state.getChildren().size(); i++) {
            boolean validAction = state.getDistribution()[state.getActions().get(i)[0]] * sign * sign2 > 0;
            if (!validAction) {
                continue;
            }
            double util = state.getChildren().get(i).getEstimateScore() + 20 * Math.pow(Math.log((state.getVisitCount() + 1)) / (state.getChildren().get(i).getVisitCount() + 1), 0.5);
            utilities.add(util);
            index.add(i);
        }
        if (sign * sign2 > 0) {
            double max = Collections.max(utilities);
            return state.getChildren().get(index.get(utilities.indexOf(max)));
        } else {
            double min = Collections.min(utilities);
            return state.getChildren().get(index.get(utilities.indexOf(min)));
        }
    }

    private static State CNN(State state, int sign2) {
        List<int[][]> states = new ArrayList<>();
        List<Integer> index = new ArrayList<>();
        int sign = color.equals(PieceColorEnum.BLACK) ? -1 : 1;
        for (int i = 0; i < state.getChildren().size(); i++) {
            boolean validAction = state.getDistribution()[state.getActions().get(i)[0]] * sign * sign2 > 0;
            if (!validAction) {
                continue;
            }
            states.add(AIHelper.toTensor(state.getChildren().get(i)).getBoard());
            index.add(i);
        }
        RestTemplate restTemplate = new RestTemplate();
        Map<String, List<Double>> res = restTemplate.postForObject("http://nn:7070/", Collections.singletonMap("states", states), Map.class);
//        System.out.println("NN: " + res);
        assert res != null;
        List<Double> utilities = res.get("utils");
        if (sign * sign2 > 0) {
            double max = Collections.max(utilities);
            return state.getChildren().get(index.get(utilities.indexOf(max)));
        } else {
            double min = Collections.min(utilities);
            return state.getChildren().get(index.get(utilities.indexOf(min)));
        }
    }


    private static List<int[]> validActions(State state) {
        state.setChildren(new ArrayList<>());
        List<int[]> res = new ArrayList<>();
        Board board = AIHelper.stateToBoard(state);

        board.getPieces().values().forEach(piece -> {
            List<int[]> temp = validActions(piece, board);
            if (!temp.isEmpty()) {
                res.addAll(temp);
                temp.forEach(action -> {
                    State newState = AIHelper.performAction(state, action);
                    states.putIfAbsent(newState, newState);
                    state.getChildren().add(states.get(newState));
                });
            }
        });

        return res;
    }

    private static List<int[]> validActions(Piece piece, Board board) {
        List<int[]> actions = new ArrayList<>();
        int curPieceIndex = GetPieceIndex.getIndex(piece);
        if (piece.isAlive()) {
            for (int row = BoundaryEnum.BOARD_BOUNDARY.getBottom(); row < BoundaryEnum.BOARD_BOUNDARY.getTop(); row++) {
                for (int col = BoundaryEnum.BOARD_BOUNDARY.getLeft(); col < BoundaryEnum.BOARD_BOUNDARY.getRight(); col++) {
                    try {
                        Piece postPiece = PieceStrategySelector.SELECT_BY_TYPE(piece.getType()).move(board, piece, col,
                                row);
                        actions.add(new int[]{curPieceIndex, GetPieceIndex.getIndex(postPiece)});
                    } catch (Exception e) {
                    }
                }
            }
        }

        return actions;
    }

    public static PieceColorEnum getColor(Game game) {
        if (game.getUsers().get(PieceColorEnum.RED).getId().equals(getUser().getId())) {
            return PieceColorEnum.RED;
        } else if (game.getUsers().get(PieceColorEnum.BLACK).getId().equals(getUser().getId())) {
            return PieceColorEnum.BLACK;
        }
        return null;
    }

}
