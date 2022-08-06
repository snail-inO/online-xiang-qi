package me.portfolio.application;

import me.portfolio.application.DAO.PieceDAO;
import me.portfolio.application.service.PieceServiceImpl;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.entity.PieceTypeEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:user_test.properties")
public class PieceServiceTest {
    @Autowired
    private PieceDAO pieceDAO;
    @Autowired
    private PieceServiceImpl pieceService;

    @Test
    public void initPieceTest() {
        Map<String, Piece> pieceMap = pieceService.initPiece(PieceTypeEnum.ZU);
        assertThat(pieceMap).hasSize(10);
        assertThat(pieceMap.values()).allMatch(piece -> piece.getType() == PieceTypeEnum.ZU);
        Map<String, Piece> finalPieceMap = pieceMap;
        Assertions.assertDoesNotThrow( () -> finalPieceMap.keySet().stream().map(key -> pieceDAO.findById(key)));

        pieceMap = pieceService.initPiece(PieceTypeEnum.SHUAI);
        assertThat(pieceMap).hasSize(2);
        assertThat(pieceMap.values()).allMatch(piece -> piece.getType() == PieceTypeEnum.SHUAI);
        Map<String, Piece> finalPieceMap1 = pieceMap;
        Assertions.assertDoesNotThrow( () -> finalPieceMap1.keySet().stream().map(key -> pieceDAO.findById(key)));


        pieceMap = pieceService.initPiece(PieceTypeEnum.MA);
        assertThat(pieceMap).hasSize(4);
        assertThat(pieceMap.values()).allMatch(piece -> piece.getType() == PieceTypeEnum.MA);
        Map<String, Piece> finalPieceMap2 = pieceMap;
        Assertions.assertDoesNotThrow( () -> finalPieceMap2.keySet().stream().map(key -> pieceDAO.findById(key)));
    }
}
