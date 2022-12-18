package me.portfolio.application.config;

import me.portfolio.application.DAO.UserDAO;
import me.portfolio.application.service.AIServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AIConfig implements CommandLineRunner {
    private final UserDAO userDAO;

    public AIConfig(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void run(String... args) throws Exception {
        AIServiceImpl.setUserDAO(userDAO);
        AIServiceImpl.getUser();
    }
}
