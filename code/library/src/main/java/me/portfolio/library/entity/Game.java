package me.portfolio.library.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document
public class Game {
    @Id
    private String id;
    private GameStatusEnum status;
    private Map<User, PieceColorEnum> users;
    private List<Board> boards;

    public Game(String id, GameStatusEnum status, Map<User, PieceColorEnum> users, List<Board> boards) {
        this.id = id;
        this.status = status;
        this.users = users;
        this.boards = boards;
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

    public Map<User, PieceColorEnum> getUsers() {
        return users;
    }

    public void setUsers(Map<User, PieceColorEnum> users) {
        this.users = users;
    }

    public List<Board> getBoards() {
        return boards;
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
    }
}
