package me.portfolio.library.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.portfolio.library.util.UserStatusEnum;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.util.Date;
import java.util.Objects;

@Document("users")
public class User {
    @Id
    private String id;
    @NonNull
    @Indexed(unique = true)
    private String name;
    private UserStatusEnum status;
    @JsonIgnore
    @LastModifiedDate
    private Date lastOnlineTime;
    @JsonIgnore
    @CreatedDate
    private Date createDate;
    private long totalGames;
    private long wins;

    public User() {
    }

    public User(String id, String name, UserStatusEnum status, Date lastOnlineTime, Date createDate, long totalGames, long wins) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.lastOnlineTime = lastOnlineTime;
        this.createDate = createDate;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", lastOnlineTime=" + lastOnlineTime +
                ", totalGames=" + totalGames +
                ", wins=" + wins +
                '}';
    }
}
