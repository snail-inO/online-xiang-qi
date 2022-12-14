package me.portfolio.library.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;

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

    public Board(Board board) {
        this.id = board.id;
        this.step = board.step;
        this.pieces = board.pieces;
        this.game = board.game;
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


}
