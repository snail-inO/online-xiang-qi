package me.portfolio.library.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.portfolio.library.util.GameStatusEnum;
import me.portfolio.library.util.PieceColorEnum;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Document("games")
public class Game {
    @Id
    private String id;
    private GameStatusEnum status;
    private long totalSteps;
    @Reference(to = User.class)
    private Map<PieceColorEnum, User> users;
    @Reference(to = Board.class)
    private List<Board> boards;
    @JsonIgnore
    @LastModifiedDate
    private Date lastModifiedDate;
    @JsonIgnore
    @CreatedDate
    private Date createDate;

    public Game(String id, GameStatusEnum status, long totalSteps, Map<PieceColorEnum, User> users, List<Board> boards) {
        this.id = id;
        this.status = status;
        this.totalSteps = totalSteps;
        this.boards = boards;
        this.users = users;
    }

    public Game() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GameStatusEnum getStatus() {
        return status;
    }

    public void setStatus(GameStatusEnum status) {
        this.status = status;
    }

    public long getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(long totalSteps) {
        this.totalSteps = totalSteps;
    }

    public Map<PieceColorEnum, User> getUsers() {
        return users;
    }

    public void setUsers(Map<PieceColorEnum, User> users) {
        this.users = users;
    }

    public List<Board> getBoards() {
        return boards;
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", users=" + users +
                ", boards=" + boards +
                '}';
    }
}
