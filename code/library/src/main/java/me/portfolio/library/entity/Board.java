package me.portfolio.library.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;
import java.util.Map;

@Document("boards")
public class Board {
    @Id
    private String id;
    private long step;
    @Reference(to = Piece.class)
    private Map<Integer, Piece> pieces;
    @Reference(to = Game.class)
    private Game game;
    @JsonIgnore
    @LastModifiedDate
    private Date lastModifiedDate;
    @JsonIgnore
    @CreatedDate
    private Date createDate;

    public Board() {
    }

    public Board(String id, long step, Map<Integer, Piece> pieces, Game game) {
        this.id = id;
        this.step = step;
        this.pieces = pieces;
        this.game = game;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getStep() {
        return step;
    }

    public void setStep(long step) {
        this.step = step;
    }

    public Map<Integer, Piece> getPieces() {
        return pieces;
    }

    public void setPieces(Map<Integer, Piece> pieces) {
        this.pieces = pieces;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
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
    public String toString() {
        return "Board{" +
                "id='" + id + '\'' +
                ", step=" + step +
                ", pieces=" + pieces +
                ", game=" + game +
                ", lastModifiedDate=" + lastModifiedDate +
                ", createDate=" + createDate +
                '}';
    }
}
