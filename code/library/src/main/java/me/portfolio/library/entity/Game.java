package me.portfolio.library.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document("games")
public class Game {
    @Id
    private String id;
    private GameStatusEnum status;
    private long totalSteps;
    @Reference(to = User.class)
    private Map<PieceColorEnum, User> users;
    @Reference(to = Board.class)
    private Map<String, Board> boards;

    public Game(String id, GameStatusEnum status, long totalSteps, Map<PieceColorEnum, User> users, Map<String, Board> boards) {
        this.id = id;
        this.status = status;
        this.totalSteps = totalSteps;
        this.boards = boards;
        this.users = users;
    }

    public Game() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GameStatusEnum getStatus() {
        return status;
    }

    public void setStatus(GameStatusEnum status) {
        this.status = status;
    }

    public long getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(long totalSteps) {
        this.totalSteps = totalSteps;
    }

    public Map<PieceColorEnum, User> getUsers() {
        return users;
    }

    public void setUsers(Map<PieceColorEnum, User> users) {
        this.users = users;
    }

    public Map<String, Board> getBoards() {
        return boards;
    }

    public void setBoards(Map<String, Board> boards) {
        this.boards = boards;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", users=" + users +
                ", boards=" + boards +
                '}';
    }
}
