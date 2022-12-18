package me.portfolio.library.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("tensor")
public class Tensor {
    @Id
    private String id;
    private int[][] board;
    private Float score;

    public Tensor() {
    }

    public Tensor(String id, int[][] board, Float score) {
        this.id = id;
        this.board = board;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }
}
