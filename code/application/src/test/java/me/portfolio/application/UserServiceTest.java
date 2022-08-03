package me.portfolio.application;

import me.portfolio.application.DAO.UserDAO;
import me.portfolio.library.entity.User;
import me.portfolio.library.entity.UserStatusEnum;
import me.portfolio.application.service.UserServiceImpl;
import me.portfolio.library.util.MatchingQueue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static reactor.core.publisher.Mono.when;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserDAO userDAO;
    @Autowired
    UserServiceImpl userService;

    @Test
    public void newUserTest() throws InterruptedException {
        User user = userService.newUser();
        assertThat(user).isEqualTo(userDAO.findById(user.getId()).get());
        assertThat(user.getStatus()).isEqualTo(UserStatusEnum.ONLINE);
        assertThat(MatchingQueue.PEEK()).isEqualTo(user.getId());
        Thread.sleep(6000);
        assertThat(MatchingQueue.PEEK()).isNull();
        assertThat(userDAO.findById(user.getId()).get().getStatus()).isEqualTo(UserStatusEnum.OFFLINE);

    }

}
