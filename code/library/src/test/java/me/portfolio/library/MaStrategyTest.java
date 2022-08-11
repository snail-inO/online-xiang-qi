package me.portfolio.library;

import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.service.PieceStrategyMaImpl;
import me.portfolio.library.util.PieceColorEnum;
import me.portfolio.library.util.PieceTypeEnum;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MaStrategyTest {
    @Test
    public void testMaStrategy() {
        PieceStrategyMaImpl strategy = new PieceStrategyMaImpl();
        Board testBoard = new Board();
        Map testHash = new HashMap<>();
        Piece ma = new Piece(PieceTypeEnum.MA);

        ma.setColor(PieceColorEnum.BLACK);
        Piece maJiao = new Piece(PieceTypeEnum.MA);
        maJiao.setColor(PieceColorEnum.BLACK);
        testHash.put(9,maJiao);

        testBoard.setPieces(testHash);

        ma.setRow(0);
        ma.setCol(1);
        assertThat(strategy.move(testBoard, ma, 0, 0)).isEqualTo(ma);


        ma.setRow(-1);
        ma.setCol(0);
        assertThat(strategy.move(testBoard, ma, 0, 0)).isEqualTo(ma);

        ma.setRow(0);
        ma.setCol(1);
        assertThat(strategy.move(testBoard, ma, -1, 0)).isEqualTo(ma);

        //上马脚
        ma.setCol(1);
        ma.setRow(0);
        assertThat(strategy.move(testBoard, ma, 0, 2)).isEqualTo(ma);
        assertThat(strategy.move(testBoard, ma, 2, 2)).isEqualTo(ma);


        //右马脚
        ma.setCol(0);
        ma.setRow(1);
        assertThat(strategy.move(testBoard, ma, 2, 0)).isEqualTo(ma);
        assertThat(strategy.move(testBoard, ma, 2, 2)).isEqualTo(ma);


        //下马脚
        ma.setCol(1);
        ma.setRow(2);
        assertThat(strategy.move(testBoard, ma, 0, 0)).isEqualTo(ma);
        assertThat(strategy.move(testBoard, ma, 2, 0)).isEqualTo(ma);


        //左马脚
        ma.setCol(2);
        ma.setRow(1);
        assertThat(strategy.move(testBoard, ma, 0, 0)).isEqualTo(ma);
        assertThat(strategy.move(testBoard, ma, 0, 2)).isEqualTo(ma);

        //success
        ma.setRow(2);
        ma.setCol(1);
        Piece curMa = new Piece(ma);
        curMa.setCol(3);
        curMa.setRow(3);
        assertThat(strategy.move(testBoard, ma, 3, 3)).isEqualTo(curMa);

        //same color
        Piece curChess = new Piece(ma);
        curChess.setColor(PieceColorEnum.BLACK);
        testHash.put(29,curChess);
        testBoard.setPieces(testHash);
        assertThat(strategy.move(testBoard, ma, 3, 3)).isEqualTo(ma);

        //diff-color
        curChess.setColor(PieceColorEnum.RED);
        testHash.replace(29,curChess);
        testBoard.setPieces(testHash);
        assertThat(strategy.move(testBoard, ma, 3, 3)).isEqualTo(curMa);

    }
}
