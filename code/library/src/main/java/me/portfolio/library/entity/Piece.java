package me.portfolio.library.entity;


import me.portfolio.library.service.PieceStrategy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Piece {
    @Id
    private String id;
    private PieceTypeEnum type;
    @Transient
    private PieceStrategy strategy;
    private PieceColorEnum color;
    private int row;
    private int col;
    private boolean alive;

    public Piece() {
    }

    public Piece(String id, PieceTypeEnum type, PieceStrategy strategy, PieceColorEnum color, int row, int col, boolean alive) {
        this.id = id;
        this.type = type;
        this.strategy = strategy;
        this.color = color;
        this.row = row;
        this.col = col;
        this.alive = alive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PieceTypeEnum getType() {
        return type;
    }

    public void setType(PieceTypeEnum type) {
        this.type = type;
    }

    public PieceStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(PieceStrategy strategy) {
        this.strategy = strategy;
    }

    public PieceColorEnum getColor() {
        return color;
    }

    public void setColor(PieceColorEnum color) {
        this.color = color;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
