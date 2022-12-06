package me.portfolio.application.service;


import me.portfolio.application.DAO.UserDAO;
import me.portfolio.application.websocket.EntityEvent;
import me.portfolio.library.entity.User;
import me.portfolio.library.exceptions.EntityNotFoundException;
import me.portfolio.library.exceptions.InvalidOperationException;
import me.portfolio.library.util.MatchingQueue;
import me.portfolio.library.util.UserStatusEnum;
import me.portfolio.log.aop.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final Map<String, Timer> OFFLINE_TIMER = new HashMap<>();
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserDAO userDAO;
    @Value("${user.offline_time}")
    private Long OFFLINE_TIME;

    public UserServiceImpl(ApplicationEventPublisher applicationEventPublisher, UserDAO userDAO) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.userDAO = userDAO;
    }

    @Logging
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
                OFFLINE_TIMER.remove(uid);

                entity.setStatus(UserStatusEnum.ONLINE);
                break;
            case OFFLINE:
                if (entity.getStatus() == UserStatusEnum.OFFLINE)
                    break;
                if (curTimer != null) {
                    curTimer.cancel();
                }
                OFFLINE_TIMER.remove(uid);
                if (entity.getStatus() == UserStatusEnum.MATCHING)
                    dequeue(entity);
                entity.setStatus(UserStatusEnum.OFFLINE);
                break;
            case MATCHING:
                LOGGER.info("User offline time: " + OFFLINE_TIME);
                entity = enqueue(entity);
                break;
            case GAMING:
                if (entity.getStatus() != UserStatusEnum.MATCHING && entity.getStatus() != UserStatusEnum.GAMING)
                    throw new InvalidOperationException(User.class, entity.getId());

                if (entity.getStatus() == UserStatusEnum.MATCHING) {
                    entity.setStatus(UserStatusEnum.GAMING);
                    entity.setTotalGames(entity.getTotalGames() + 1);
                    break;
                }
                if (curTimer != null) {
                    curTimer.cancel();
                    OFFLINE_TIMER.remove(uid);
                } else {
                    Timer timer = new Timer();
                    timer.schedule(new SetUserOfflineTask(uid), OFFLINE_TIME);
                    OFFLINE_TIMER.put(uid, timer);
                }
                break;
        }

        entity = userDAO.save(entity);
        return entity;
    }

    @Override
    public User addWinCount(User user) {
        user.setWins(user.getWins() + 1);
        return userDAO.save(user);
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

    private void publishSetUserStatusEvent(final User user) {
        EntityEvent entityEvent = new EntityEvent(this, user);
        applicationEventPublisher.publishEvent(entityEvent);
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
            user.setStatus(UserStatusEnum.OFFLINE);
            userDAO.save(user);
        }
    }
}
