package me.portfolio.library.entity.test;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("metadata")
public class Metadata {
    @Id
    String id;
    Integer size;
    Float treeScore;
    Integer nodeCount;

    public Metadata() {
    }

    public Metadata(String id, Integer size, Float treeScore, Integer nodeCount) {
        this.id = id;
        this.size = size;
        this.treeScore = treeScore;
        this.nodeCount = nodeCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Float getTreeScore() {
        return treeScore;
    }

    public void setTreeScore(Float treeScore) {
        this.treeScore = treeScore;
    }

    public Integer getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(Integer nodeCount) {
        this.nodeCount = nodeCount;
    }
}
