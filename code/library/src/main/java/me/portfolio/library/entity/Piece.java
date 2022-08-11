package me.portfolio.library.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import me.portfolio.library.service.PieceStrategy;
import me.portfolio.library.service.PieceStrategySelector;
import me.portfolio.library.util.PieceColorEnum;
import me.portfolio.library.util.PieceTypeEnum;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
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
    @Reference(to = Board.class)
    private List<Board> boards;
    @JsonIgnore
    @LastModifiedDate
    private Date lastModifiedDate;
    @JsonIgnore
    @CreatedDate
    private Date createDate;

    public Piece() {
    }

    public Piece(PieceTypeEnum type) {
        this.type = type;
        this.strategy = PieceStrategySelector.SELECT_BY_TYPE(type);
    }

    public Piece(String id, PieceTypeEnum type, PieceColorEnum color, int row, int col, boolean alive, List<Board> boards) {
        this.id = id;
        this.type = type;
        this.strategy = PieceStrategySelector.SELECT_BY_TYPE(type);
        this.color = color;
        this.row = row;
        this.col = col;
        this.alive = alive;
        this.boards = boards;
    }

    public Piece(Piece piece) {
        this.id = piece.id;
        this.type = piece.type;
        this.strategy = piece.strategy;
        this.color = piece.color;
        this.row = piece.row;
        this.col = piece.col;
        this.alive = piece.alive;
        this.boards = piece.boards;
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

    public List<Board> getBoards() {
        return boards;
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece)) return false;
        Piece piece = (Piece) o;
        return row == piece.row && col == piece.col && alive == piece.alive && Objects.equals(id, piece.id) && type == piece.type && Objects.equals(strategy, piece.strategy) && color == piece.color && Objects.equals(boards, piece.boards) && Objects.equals(lastModifiedDate, piece.lastModifiedDate) && Objects.equals(createDate, piece.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, strategy, color, row, col, alive, boards, lastModifiedDate, createDate);
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
                ", boards=" + boards +
                ", lastModifiedDate=" + lastModifiedDate +
                ", createDate=" + createDate +
                '}';
    }
}
