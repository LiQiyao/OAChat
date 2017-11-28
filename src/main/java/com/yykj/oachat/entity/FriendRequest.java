package com.yykj.oachat.entity;

public class FriendRequest {
    private Long id;

    private Long fromUserId;

    private Long toUserId;

    private Long sendTime;

    private Boolean accepted;

    public FriendRequest(Long id, Long fromUserId, Long toUserId, Long sendTime, Boolean accepted) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.sendTime = sendTime;
        this.accepted = accepted;
    }

    public FriendRequest() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}