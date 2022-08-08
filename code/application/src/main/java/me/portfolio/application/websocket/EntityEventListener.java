package me.portfolio.application.websocket;

import me.portfolio.library.entity.Game;
import me.portfolio.log.aop.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static me.portfolio.application.websocket.WebSocketConfiguration.MESSAGE_PREFIX;

@Component
public class EntityEventListener implements ApplicationListener<EntityEvent> {
    private final SimpMessagingTemplate websocket;
    private final EntityLinks entityLinks;
    private final static Logger LOGGER = LoggerFactory.getLogger(EntityEventListener.class);

    public EntityEventListener(SimpMessagingTemplate websocket, EntityLinks entityLinks) {
        this.websocket = websocket;
        this.entityLinks = entityLinks;
    }

    @Override
    public void onApplicationEvent(EntityEvent event) {
        Game game = (Game) event.getEntity();
        game.getUsers().values().forEach(user -> {
            String path = MESSAGE_PREFIX + "/newGame/" + user.getId();
            websocket.convertAndSend(path, entityLinks.linkToItemResource(Game.class, game.getId()).toUri());
            LOGGER.info("Event listener triggered. Sending to: " + path);
        });

    }
}
