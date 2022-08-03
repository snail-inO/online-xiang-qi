package me.portfolio.library.entity;

import org.springframework.data.annotation.Id;

import java.util.Map;

public class Board {
    @Id
    private String id;
    private long step;
    private Map<String, Piece> pieces;

    public Board() {
    }

    public Board(String id, long step, Map<String, Piece> pieces) {
        this.id = id;
        this.step = step;
        this.pieces = pieces;
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

    public Map<String, Piece> getPieces() {
        return pieces;
    }

    public void setPieces(Map<String, Piece> pieces) {
        this.pieces = pieces;
    }
}