package com.yykj.oachat.dto.data;

/**
 * Created by Lee on 2017/11/23.
 */
public class AddFriendSuccessDTO {

    private Long addUserId;

    private String addUsername;

    private String addNickName;

    private String addGender;

    private String addAddress;

    private String addTelephoneNumber;

    private String addIcon;

    public AddFriendSuccessDTO() {
    }

    public Long getAddUserId() {
        return addUserId;
    }

    public void setAddUserId(Long addUserId) {
        this.addUserId = addUserId;
    }

    public String getAddUsername() {
        return addUsername;
    }

    public void setAddUsername(String addUsername) {
        this.addUsername = addUsername;
    }

    public String getAddNickName() {
        return addNickName;
    }

    public void setAddNickName(String addNickName) {
        this.addNickName = addNickName;
    }

    public String getAddGender() {
        return addGender;
    }

    public void setAddGender(String addGender) {
        this.addGender = addGender;
    }

    public String getAddAddress() {
        return addAddress;
    }

    public void setAddAddress(String addAddress) {
        this.addAddress = addAddress;
    }

    public String getAddTelephoneNumber() {
        return addTelephoneNumber;
    }

    public void setAddTelephoneNumber(String addTelephoneNumber) {
        this.addTelephoneNumber = addTelephoneNumber;
    }

    public String getAddIcon() {
        return addIcon;
    }

    public void setAddIcon(String addIcon) {
        this.addIcon = addIcon;
    }
}
