package com.yykj.oachat.entity;

public class Friend {
    private Long id;

    private Long user1Id;

    private Long user2Id;

    private Long createTime;

    public Friend(Long id, Long user1Id, Long user2Id, Long createTime) {
        this.id = id;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.createTime = createTime;
    }

    public Friend() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(Long user1Id) {
        this.user1Id = user1Id;
    }

    public Long getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(Long user2Id) {
        this.user2Id = user2Id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}