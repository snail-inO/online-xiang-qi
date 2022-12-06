package me.portfolio.application.service;

import me.portfolio.library.entity.User;

public interface UserService {
    User setUserStatus(final User user);

    User addWinCount(final User user);
}
