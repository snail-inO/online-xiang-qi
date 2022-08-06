package me.portfolio.application.service;

import me.portfolio.library.entity.Piece;
import me.portfolio.library.entity.PieceTypeEnum;

import java.util.Map;

public interface PieceService {
    Map<String, Piece> initPiece(PieceTypeEnum type);
}
