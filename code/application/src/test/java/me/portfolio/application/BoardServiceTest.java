package me.portfolio.application;

import me.portfolio.application.service.BoardServiceImpl;
import me.portfolio.application.service.PieceServiceImpl;
import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Game;
import me.portfolio.library.util.PieceTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:user_test.properties")
public class BoardServiceTest {
    @MockBean
    private PieceServiceImpl pieceService;
    @Autowired
    private BoardServiceImpl boardService;

    @Test
    public void initBoardTest() {
        List<PieceTypeEnum> types = new ArrayList<>();
        when(pieceService.initPiece(any(Board.class), any(PieceTypeEnum.class))).thenAnswer(invocationOnMock -> {
            types.add(invocationOnMock.getArgument(1));
            return new HashMap<>();
        });
        boardService.initBoard(new Game(), -1);
        assertThat(types).containsExactlyElementsOf(Arrays.stream(PieceTypeEnum.values()).collect(Collectors.toList()));
    }
}
