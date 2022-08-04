package me.portfolio.library.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;

@Document
public class User {
    @Id
    private String id;
    private String name;
    private UserStatusEnum status;
    @LastModifiedDate
    private Date lastOnlineTime;
    private long totalGames;
    private long wins;

    public User() {
    }

    public User(String id, String name, UserStatusEnum status, Date lastOnlineTime, long totalGames, long wins) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.lastOnlineTime = lastOnlineTime;
        this.totalGames = totalGames;
        this.wins = wins;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(long totalGames) {
        this.totalGames = totalGames;
    }

    public long getWins() {
        return wins;
    }

    public void setWins(long wins) {
        this.wins = wins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return totalGames == user.totalGames && wins == user.wins && id.equals(user.id) && Objects.equals(name, user.name) && status == user.status && Objects.equals(lastOnlineTime, user.lastOnlineTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, lastOnlineTime, totalGames, wins);
    }
}
