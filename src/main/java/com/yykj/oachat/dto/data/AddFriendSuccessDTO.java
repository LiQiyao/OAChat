package com.yykj.oachat.dto.data;

/**
 * Created by Lee on 2017/11/23.
 */
public class AddFriendSuccessDTO {

    private UserDetailDTO userDetailDTO;

    public AddFriendSuccessDTO() {
    }

    public AddFriendSuccessDTO(UserDetailDTO userDetailDTO) {
        this.userDetailDTO = userDetailDTO;
    }

    public UserDetailDTO getUserDetailDTO() {
        return userDetailDTO;
    }

    public void setUserDetailDTO(UserDetailDTO userDetailDTO) {
        this.userDetailDTO = userDetailDTO;
    }
}
