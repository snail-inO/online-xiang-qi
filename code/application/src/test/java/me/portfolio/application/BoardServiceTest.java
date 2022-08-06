package me.portfolio.application;

import me.portfolio.application.DAO.BoardDAO;
import me.portfolio.application.service.BoardServiceImpl;
import me.portfolio.application.service.PieceServiceImpl;
import me.portfolio.library.entity.PieceTypeEnum;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

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
        when(pieceService.initPiece(any(PieceTypeEnum.class))).thenAnswer(invocationOnMock -> {
            types.add(invocationOnMock.getArgument(0));
            return new HashMap<>();
        });
        boardService.initBoard();
        assertThat(types).containsExactlyElementsOf(Arrays.stream(PieceTypeEnum.values()).collect(Collectors.toList()));
    }
}
