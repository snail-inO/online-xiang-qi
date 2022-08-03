package me.portfolio.library.entity;

import java.util.Date;

public class UserStatus {
    private UserStatusEnum status;
    private Date lastOnlineTime;

    public UserStatus() {
    }

    public UserStatus(UserStatusEnum status, Date lastOnlineTime) {
        this.status = status;
        this.lastOnlineTime = lastOnlineTime;
    }

    public UserStatusEnum getStatus() {
        return status;
    }

    public void setStatus(UserStatusEnum status) {
        this.status = status;
    }

    public Date getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Date lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }
}
