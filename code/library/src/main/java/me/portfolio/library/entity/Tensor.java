package me.portfolio.library.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("tensor")
public class Tensor {
    @Id
    private String id;
    private List<List<Boolean>> board;

    public Tensor() {
    }

    public Tensor(String id, List<List<Boolean>> board) {
        this.id = id;
        this.board = board;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<List<Boolean>> getBoard() {
        return board;
    }

    public void setBoard(List<List<Boolean>> board) {
        this.board = board;
    }
}
