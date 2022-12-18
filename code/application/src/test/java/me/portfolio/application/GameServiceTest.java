package me.portfolio.application;

import me.portfolio.application.service.BoardServiceImpl;
import me.portfolio.application.service.GameServiceImpl;
import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Game;
import me.portfolio.library.entity.User;
import me.portfolio.library.util.GameStatusEnum;
import me.portfolio.library.util.PieceColorEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:user_test.properties")
public class GameServiceTest {
    @Autowired
    private GameServiceImpl gameService;
    @MockBean
    private BoardServiceImpl boardService;

    @Test
    public void initGameTest() {
        when(boardService.initBoard(any(Game.class), -1)).thenReturn(new Board());
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId("1");
        users.add(user);
        user = new User();
        user.setId("2");
        users.add(user);
        Game game = gameService.initGame(users, -1);
        assertThat(game.getStatus()).isEqualTo(GameStatusEnum.IN_PROGRESS);
        assertThat(game.getUsers().keySet()).containsExactlyInAnyOrderElementsOf(Arrays.stream(PieceColorEnum.values()).collect(Collectors.toList()));
    }
}
