package me.portfolio.application.websocket;

import me.portfolio.library.entity.Game;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static me.portfolio.application.websocket.WebSocketConfiguration.MESSAGE_PREFIX;

@Component
@RepositoryEventHandler
public class EventHandler {
   private final SimpMessagingTemplate websocket;
   private final EntityLinks entityLinks;

    public EventHandler(SimpMessagingTemplate websocket, EntityLinks entityLinks) {
        this.websocket = websocket;
        this.entityLinks = entityLinks;
    }

    @HandleAfterCreate
    public void newGame(Game game) {
        websocket.convertAndSend(MESSAGE_PREFIX + "/newGame" +
                entityLinks.linkToItemResource(Game.class, game.getId()).toUri().getPath());
    }
}
