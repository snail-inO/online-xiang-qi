package me.portfolio.application.service;

import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.util.PieceTypeEnum;

import java.util.Map;

public interface PieceService {
    Map<Integer, Piece> initPiece(Board board, PieceTypeEnum type);
    void updatePiece(Piece piece, boolean alive, Board board);
}
