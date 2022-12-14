package me.portfolio.application;

import me.portfolio.application.DAO.UserDAO;
import me.portfolio.application.service.UserServiceImpl;
import me.portfolio.library.entity.User;
import me.portfolio.library.util.MatchingQueue;
import me.portfolio.library.util.UserStatusEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:user_test.properties")
public class UserServiceTest {
    @Autowired
    UserDAO userDAO;
    @Autowired
    UserServiceImpl userService;

    @Test
    public void matchingStatusTest() {
        Queue<String> queue = MatchingQueue.getQueue();
        User user = new User();
        user.setStatus(UserStatusEnum.OFFLINE);
        user = userDAO.save(user);

        user.setStatus(UserStatusEnum.ONLINE);
        userService.setUserStatus(user);
        assertThat(queue.peek()).isNull();

        user.setStatus(UserStatusEnum.MATCHING);
        userService.setUserStatus(user);
        assertThat(queue.peek()).isEqualTo(user.getId());

        user.setStatus(UserStatusEnum.ONLINE);
        userService.setUserStatus(user);
        assertThat(queue.peek()).isNull();

        user.setStatus(UserStatusEnum.MATCHING);
        userService.setUserStatus(user);
        assertThat(queue.peek()).isEqualTo(user.getId());

        user.setStatus(UserStatusEnum.OFFLINE);
        userService.setUserStatus(user);
        assertThat(queue.peek()).isNull();

        user.setStatus(UserStatusEnum.MATCHING);
        User finalUser = user;
        Assertions.assertThrows(RuntimeException.class,
                () -> userService.setUserStatus(finalUser));
    }

    @Test
    public void onlineStatusTest() throws InterruptedException {
        User user = userDAO.save(new User());
        Queue<String> queue = MatchingQueue.getQueue();

        user.setStatus(UserStatusEnum.ONLINE);
        userService.setUserStatus(user);
        assertThat(userDAO.findById(user.getId()).get().getStatus()).isEqualTo(UserStatusEnum.ONLINE);

        user.setStatus(UserStatusEnum.OFFLINE);
        userService.setUserStatus(user);
        assertThat(userDAO.findById(user.getId()).get().getStatus()).isEqualTo(UserStatusEnum.OFFLINE);

        user.setStatus(UserStatusEnum.ONLINE);
        userService.setUserStatus(user);
        assertThat(userDAO.findById(user.getId()).get().getStatus()).isEqualTo(UserStatusEnum.ONLINE);

        user.setStatus(UserStatusEnum.MATCHING);
        userService.setUserStatus(user);
        assertThat(userDAO.findById(user.getId()).get().getStatus()).isEqualTo(UserStatusEnum.MATCHING);
        assertThat(queue.peek()).isEqualTo(user.getId());
        queue.poll();
        user.setStatus(UserStatusEnum.GAMING);
        userService.setUserStatus(user);
        assertThat(userDAO.findById(user.getId()).get().getStatus()).isEqualTo(UserStatusEnum.GAMING);
        user.setStatus(UserStatusEnum.ONLINE);
        userService.setUserStatus(user);
        assertThat(userDAO.findById(user.getId()).get().getStatus()).isEqualTo(UserStatusEnum.ONLINE);

    }

}
