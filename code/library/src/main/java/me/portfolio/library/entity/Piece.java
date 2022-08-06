package me.portfolio.library.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import me.portfolio.library.service.PieceStrategy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document("pieces")
public class Piece {
    @Id
    private String id;
    private PieceTypeEnum type;
    @Transient
    @JsonIgnore
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

    public Piece(Piece piece) {
        this.id = piece.id;
        this.type = piece.type;
        this.strategy = piece.strategy;
        this.color = piece.color;
        this.row = piece.row;
        this.col = piece.col;
        this.alive = piece.alive;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece)) return false;
        Piece piece = (Piece) o;
        return row == piece.row && col == piece.col && alive == piece.alive && id.equals(piece.id) && type == piece.type && Objects.equals(strategy, piece.strategy) && color == piece.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, strategy, color, row, col, alive);
    }

    @Override
    public String toString() {
        return "Piece{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", strategy=" + strategy +
                ", color=" + color +
                ", row=" + row +
                ", col=" + col +
                ", alive=" + alive +
                '}';
    }
}
