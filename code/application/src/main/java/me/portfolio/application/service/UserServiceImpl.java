package me.portfolio.application.service;


import me.portfolio.application.DAO.UserDAO;
import me.portfolio.library.entity.User;
import me.portfolio.library.entity.UserStatusEnum;
import me.portfolio.library.util.MatchingQueue;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static final Map<String, Timer> OFFLINE_TIMER = new HashMap<>();

    private UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User newUser() {
        User user = new User();
        user.setStatus(UserStatusEnum.ONLINE);
        user.setLastOnlineTime(new Date());
        user = userDAO.save(user);

        Timer timer = new Timer();
        timer.schedule(new SetUserOfflineTask(user.getId()), 5000);


        MatchingQueue.IN_QUEUE(user.getId());
        OFFLINE_TIMER.put(user.getId(), timer);

        return user;
    }

    class SetUserOfflineTask extends java.util.TimerTask {

        private final String uid;

        SetUserOfflineTask(String uid) {
            this.uid = uid;
        }

        @Override
        public void run() {
            OFFLINE_TIMER.remove(uid);
            MatchingQueue.REMOVE(uid);


            User user = userDAO.findById(uid).get();
            user.setStatus(UserStatusEnum.OFFLINE);
            userDAO.save(user);


        }
    }
}
