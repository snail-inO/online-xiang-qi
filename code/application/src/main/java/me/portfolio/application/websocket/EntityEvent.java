package me.portfolio.application.websocket;

import org.springframework.context.ApplicationEvent;

public class EntityEvent<T> extends ApplicationEvent {
    private T entity;

    public EntityEvent(Object source, T entity) {
        super(source);
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }
}
