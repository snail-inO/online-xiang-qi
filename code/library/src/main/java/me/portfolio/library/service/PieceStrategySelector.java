package me.portfolio.library.service;

import me.portfolio.library.util.PieceTypeEnum;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

public class PieceStrategySelector {
    private final static Map<PieceTypeEnum, PieceStrategy> StrategyPool = new HashMap<>();

    private PieceStrategySelector(){

    }
    public static PieceStrategy SELECT_BY_TYPE(PieceTypeEnum type) {
        if (StrategyPool.containsKey(type)) {
            return StrategyPool.get(type);
        }
        switch (type) {
            case SHUAI:
                StrategyPool.put(type, new PieceStrategyShuaiImpl());
                return StrategyPool.get(type);
            case SHI:
                StrategyPool.put(type, new PieceStrategyShiImpl());
                return StrategyPool.get(type);
            case XIANG:
                StrategyPool.put(type, new PieceStrategyXiangImpl());
                return StrategyPool.get(type);
            case MA:
                StrategyPool.put(type, new PieceStrategyMaImpl());
                return StrategyPool.get(type);
            case JU: ;
                StrategyPool.put(type, new PieceStrategyJuImpl());
                return StrategyPool.get(type);
            case PAO:
                StrategyPool.put(type, new PieceStrategyPaoImpl());
                return StrategyPool.get(type);
            case BING:
                StrategyPool.put(type, new PieceStrategyBingImpl());
                return StrategyPool.get(type);
        }
        return null;
    }

}

