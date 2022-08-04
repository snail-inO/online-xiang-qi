package me.portfolio.application.service;

import me.portfolio.library.entity.User;
import me.portfolio.library.entity.UserStatusEnum;

public interface UserService {
    User setUserStatus(final User user);
}
