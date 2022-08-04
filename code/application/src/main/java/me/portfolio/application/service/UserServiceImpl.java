package me.portfolio.application.service;


import me.portfolio.application.DAO.UserDAO;
import me.portfolio.library.entity.User;
import me.portfolio.library.entity.UserStatusEnum;
import me.portfolio.library.exceptions.EntityNotFoundException;
import me.portfolio.library.exceptions.InvalidOperationException;
import me.portfolio.library.util.MatchingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final Map<String, Timer> OFFLINE_TIMER = new HashMap<>();
    @Value("${user.offline_time}")
    private Long OFFLINE_TIME;

    private UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    @Override
    public User setUserStatus(final User user) {
        User entity = userDAO.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException(User.class, user.getId()));
        Timer curTimer = OFFLINE_TIMER.get(user.getId());
        String uid = entity.getId();
        UserStatusEnum status = user.getStatus();

        switch (status) {
            case ONLINE:
                if (MatchingQueue.getQueue().contains(uid))
                    dequeue(entity);

                if (curTimer != null) {
                    curTimer.cancel();
                }
                Timer timer = new Timer();
                timer.schedule(new SetUserOfflineTask(uid), OFFLINE_TIME);
                entity.setStatus(UserStatusEnum.ONLINE);
                OFFLINE_TIMER.remove(uid);
                OFFLINE_TIMER.put(uid, timer);
                break;
            case OFFLINE:
                if (entity.getStatus() == UserStatusEnum.OFFLINE)
                    break;
                curTimer.cancel();
                OFFLINE_TIMER.remove(uid);
                if (entity.getStatus() == UserStatusEnum.MATCHING)
                    dequeue(entity);
                entity.setStatus(UserStatusEnum.OFFLINE);
                break;
            case MATCHING:
                LOGGER.info("User offline time: " + OFFLINE_TIME);
                entity = enqueue(entity);
                break;
        }

        return userDAO.save(entity);
    }

    class SetUserOfflineTask extends java.util.TimerTask {

        private final String uid;

        SetUserOfflineTask(String uid) {
            this.uid = uid;
        }

        @Override
        public void run() {
            OFFLINE_TIMER.remove(uid);

            User user = userDAO.findById(uid).get();
            if (user.getStatus() == UserStatusEnum.MATCHING)
                dequeue(user);
            user.setStatus(UserStatusEnum.OFFLINE);
            userDAO.save(user);
        }
    }

    private User enqueue(User user) {
        Queue<String> queue = MatchingQueue.getQueue();
        if (user.getStatus() == UserStatusEnum.ONLINE && queue.offer(user.getId())) {
            user.setStatus(UserStatusEnum.MATCHING);
        } else {
            throw new InvalidOperationException(User.class, user.getId());
        }

        return user;
    }

    private User dequeue(User user) {
        Queue<String> queue = MatchingQueue.getQueue();
        if (user.getStatus() == UserStatusEnum.MATCHING && queue.remove(user.getId())) {
            user.setStatus(UserStatusEnum.ONLINE);
        } else {
            throw new InvalidOperationException(User.class, user.getId());
        }

        return user;
    }
}
