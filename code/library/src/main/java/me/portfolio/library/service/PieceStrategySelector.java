package me.portfolio.library.service;

import me.portfolio.library.entity.PieceTypeEnum;
import org.springframework.stereotype.Service;

@Service
public class PieceStrategySelector {
    public static PieceStrategy SELECT_BY_TYPE(PieceTypeEnum type) {
        return null;
    }
}
